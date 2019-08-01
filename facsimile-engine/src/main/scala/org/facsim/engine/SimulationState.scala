//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2019, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================

//======================================================================================================================
// Scala source file belonging to the org.facsim.engine package.
//======================================================================================================================
package org.facsim.engine

import cats.data.State
import org.facsim.collection.immutable.BinomialHeap
import org.facsim.util.CompareEqualTo
import scala.annotation.tailrec
import scala.language.implicitConversions
import scala.util.{Failure, Success, Try}
import squants.Time
import squants.time.{Days, Seconds}

/** Encapsulates the state of a simulation model at in instant in (simulation) time.
 *
 *  @tparam M Final type of the simulation's model state, which must be a sub-class of `[[org.facsim.engine.ModelState
 *  ModelState]]`.
 *
 *  @param modelState Simulation model state, tracking changes in the state of the model itself. Instances must be
 *  immutable.
 *
 *  @param nextEventId Identifier of the next simulation event to be created.
 *
 *  @param current Event currently being dispatched, wrapped in `[[scala.Some Some]]`; if `[[scala.None None]]`, then
 *  the simulation has typically not yet started running.
 *
 *  @param events Set of simulation events scheduled to occur at a future simulation time.
 *
 *  @param runState Current state of the simulation run.
 *
 *  @since 0.0
 */
final class SimulationState[M <: ModelState[M]] private(private val modelState: M, private val nextEventId: Long,
private val current: Option[SimulationState.Event[M]], private val events: PriorityQueue[SimulationState.Event[M]],
private val runState: RunState) {

  /** Copy the existing state to a new state with the indicated new values.
   *
   *  @param newModelState New simulation model state, tracking changes in the state of the model itself. Instances must
   *  be immutable.
   *
   *  @param newNextEventId New identifier of the next simulation event to be created.
   *
   *  @param newCurrent New event to become the current event being dispatched, wrapped in `[[scala.Some Some]]`; if
   *  `[[scala.None None]]`, then the simulation has typically not yet started running.
   *
   *  @param newEvents New set of simulation events scheduled to occur at a future simulation time.
   *
   *  @param newRunState New run state of the simulation run.
   *
   *  @return Updated simulation state.
   */
  private[engine] def update(newModelState: M = modelState, newNextEventId: Long = nextEventId,
  newCurrent: Option[SimulationState.Event[M]] = current, newEvents: PriorityQueue[SimulationState.Event[M]] = events,
  newRunState: RunState = runState): SimulationState[M] = {
    new SimulationState(newModelState, newNextEventId, newCurrent, newEvents, newRunState)
  }

  /** Report the current simulation time.
   *
   *  @return Current simulation time.
   */
  private[engine] def simTime: Time = current.fold(Seconds(0.0))(_.dueAt)

  /** Report the model's current state.
   *
   *  @return State of the model associated with this simulation.
   */
  private[engine] def state: ModelState[M] = modelState

  /** Report the model's current execution state.
   *
   *  @return Current execution state of the model.
   */
  private[engine] def execState = runState
}

/** Simulation state companion object.
 *
 *  @since 0.0
 */
object SimulationState {

  /** Implicit conversion of simulation transitions (''actions'') to an `[[org.facsim.engine.Action Action]]` instance.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @param actions Raw actions to be converted into an `[[org.facsim.engine.AnonymousAction AnonymousAction]]`
   *  instance.
   *
   *  @return `actions` wrapped as an action suitable for dispatching by an event.
   */
  implicit def createAnonymousAction[M <: ModelState[M]](actions: SimulationAction[M]): AnonymousAction[M] = {
    new AnonymousAction[M](actions)
  }

  /** Report the current simulation time.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @return Simulation state transition combining the current simulation state and the current simulation time.
   *
   *  @since 0.0
   */
  def time[M <: ModelState[M]]: SimulationTransition[M, Time] = State.inspect(_.simTime)

  /** Report the current simulation model state.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @return Simulation state transition combining the current simulation state and the current simulation model state.
   *
   *  @since 0.0
   */
  def modelState[M <: ModelState[M]]: SimulationTransition[M, M] = State.inspect(_.modelState)

  /** Schedule actions for later execution.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @param delay Time to elapse from the current simulation time to the time at which the actions are to be executed.
   *
   *  @param priority Relative priority of the actions to be executed: two sets of actions scheduled at the exact same
   *  time will be dispatched in order of their priority, with the event having the lower priority value executing
   *  first. If two simultaneous events have the same priority, then the event that was created first will be dispatched
   *  first. In general, assigning event priorities is an advanced topic: in the majority of cases, actions can be
   *  assigned the default priority of 0.
   *
   *  @param actions Actions to be executed after the specified `delay`.
   *
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the actions: `Unit`, wrapped in a `[[scala.util.Success Success]]`, if successful; an exception
   *  instance identifying the cause of the failure, wrapped in a `[[scala.util.Failure Failure]]` otherwise.
   *
   *  @since 0.0
   */
  def at[M <: ModelState[M]](delay: Time, priority: Priority = 0)(actions: Action[M]): SimulationAction[M] = State {s =>

    // If the simulation's run state does not support scheduling of events, then report the current state with a failure
    // describing why.
    if(!s.runState.canSchedule) (s, Failure(EventScheduleStateException(s.runState)))

    // Otherwise, create and schedule the event.
    else {

      // Firstly, create the new event.
      val event = Event(s.nextEventId, s.simTime + delay, priority, actions)

      // Add it to the event queue.
      val newEvents = s.events + event

      // Update the identifier of the next event.
      val newNextEventId = Math.incrementExact(s.nextEventId)

      // Return the updated simulation state, together with a success.
      (s.update(newNextEventId = newNextEventId, newEvents = newEvents), Success(()))
    }
  }

  /** Perform simulation state transitions until a predicate succeeds.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @tparam A Type of value resulting from the simulation transitions.
   *
   *  @param ts Sequence of simulation state transitions to be processed.
   *
   *  @param terminationValue Value to be returned, together with the current state, if all of the elements in `ts`
   *  where processed.
   *
   *  @param p A ''predicate'' function that is used to determine whether to stop processing subsequent transitions. The
   *  predicate can evaluate the state and return value of each transition. If the predicate succeeds, then execution of
   *  any remaining transitions terminates and the result of the last transition processed is returned; otherwise, if
   *  the predicate fails for the current result, then the following transition will be executed.
   *
   *  @return Result (including state) of the last transition executed, or the current state plus the last value if all
   *  transitions were processed.
   *
   *  @since 0.0
   */
  def takeUntil[M <: ModelState[M], A](ts: Seq[SimulationTransition[M, A]], terminationValue: A)
  (p: ((SimulationState[M], A)) => Boolean): SimulationTransition[M, A] = {

    // Helper function to perform the iteration.
    def nextIteration(xs: Seq[SimulationTransition[M, A]]): SimulationTransition[M, A] = {

      // Determine how to process the list of transitions.
      xs match {

        // If the list of transitions is empty, then return the current state and the last value.
        case Nil => State(s => (s, terminationValue))

        // Otherwise, if we have at least one state remaining, process it. If the predicate succeeds, return the result,
        // otherwise repeat for the tail.
        case x +: xst => for {

          // Perform the head transition, and retrieve the resulting value.
          rx <- x

          // Retrieve the resulting state.
          s <- State.get

          // If the predicate succeeds for the state and result, then return it. Otherwise, return the result of the
          // next iteration.
          r <- {
            if(p((s, rx))) State.pure[SimulationState[M], A](rx)
            else nextIteration(xst)
          }
        } yield r
      }
    }

    // Start the ball rolling.
    nextIteration(ts)
  }

  /** Perform simulation state transitions as long as the transitions succeed.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @param ts Sequence of simulation state transitions to be processed.
   *
   *  @return Result (including state) of the last transition executed, or the current state plus the last value if all
   *  transitions were processed.
   *
   *  @since 0.0
   */
  def takeUntilFailure[M <: ModelState[M]](ts: Seq[SimulationAction[M]]): SimulationAction[M] = for {
    r <- takeUntil(ts, Success(())) {
      case(_, x) => x.isFailure
    }
  } yield r

  /** Initial simulation state.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @param initialModelState Initial simulation model state.
   *
   *  @return Initial simulation state, for use at the start of the simulation.
   */
  private def initialState[M <: ModelState[M]](initialModelState: M): SimulationState[M] = {
    new SimulationState(initialModelState, 0L, None, BinomialHeap.empty[SimulationState.Event[M]], Initializing)
  }

  /** Initialize the simulation.
   *
   *  Prepare the simulation to commence running. This involves the following steps:
   *
   *   1. Schedule the end of the simulation warm-up period.
   *   2. Initialize the simulation model, creating any necessary initial events to start the simulation running.
   *   3. Change the state of the simulation to be executing.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @param warmupLength Duration, measured in simulation time from the start of the simulation run, allowing the
   *  simulation to ''warm-up'' (that is, fully populating the simulation model and removing the effects of
   *  ''initialization bias''), after which simulation statistics will be reset. If omitted, this value defaults to 1
   *  week.
   *
   *  @param snapLength Duration, measured in simulation time, of each simulation ''snap'' (simulation reporting
   *  period), with the first such snap starting after the warm-up period has completed. At the end of each snap, the
   *  statistics are reset and a report generated. If omitted, this defaults to one week.
   *
   *  @param numSnaps Number of simulation snaps to be undertaken. The simulation will terminate when the last snap has
   *  completed. This value must be greater than 0, or an error will occur. This value defaults to 1.
   *
   *  @param initialization Actions required to initialize the simulation, such as creating initial events, etc.
   *
   *  @return Initial simulation executing state.
   */
  private def initialize[M <: ModelState[M]](warmupLength: Time, snapLength: Time, numSnaps: Int,
  initialization: Action[M]): SimulationAction[M] = for {

    // Determine the status of the following actions, and return it as the status of this action, together with the
    // updated simulation state.
    r <- takeUntilFailure {
      List(

        // Schedule the end of the warm-up period. This should have the lowest possible priority, so that all
        // simultaneous events are completed before the reset occurs.
        at(warmupLength, Int.MaxValue)(new EndWarmUpAction[M](snapLength, numSnaps)),

        // Perform custom initialization actions.
        initialization.dispatch,

        // Update the simulation state to be executing.
        State {s =>
          (s.update(newRunState = Executing), Success(()))
        }
      )
    }
  } yield r

  /** Update the current event.
   *
   *  Identify the next event to be dispatched, as the event at the head of the event queue. Then remove that event from
   *  the event queue. Finally, update the simulation state accordingly.
   *
   *  If the event queue is empty, then there are no more events and the simulation will have terminated; in this case,
   *  the simulation will change to Terminated and the current event will not be updated.
   *
   *  @note The new simulation time (the time at which the new current event is scheduled to occur) '''must''' be
   *  greater than or equal to the due time of the initial current event.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the update operation: `Unit`, wrapped in a `[[scala.util.Success Success]]`, if successful; an
   *  exception instance identifying the cause of the failure, wrapped in a `[[scala.util.Failure Failure]]` otherwise.
   */
  private def updateCurrentEvent[M <: ModelState[M]]: SimulationAction[M] = State {s =>

    // If the simulation's current state does not support event iteration, then report a failure as the result, together
    // with the current state.
    if(!s.runState.canIterate) (s, Failure(EventIterationStateException(s.runState)))

    // Otherwise, if the event queue is empty, them we've run out of events.
    else if(s.events.isEmpty) (s.update(newRunState = Terminated), Failure(OutOfEventsException))

    // Otherwise, update the system state accordingly and return a success.
    else {
      val (newCurrent, newEvents) = s.events.minimumRemove
      assert(newCurrent.nonEmpty)
      (s.update(newCurrent = newCurrent, newEvents = newEvents), Success(()))
    }
  }

  /** Dispatch the current event.
   *
   *  Execute the actions associated with the current event, updating the simulation state accordingly.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the dispatch operation: `Unit`, wrapped in a `[[scala.util.Success Success]]`, if successful; an
   *  exception instance identifying the cause of the failure, wrapped in a `[[scala.util.Failure Failure]]` otherwise.
   */
  private def dispatchCurrentEvent[M <: ModelState[M]]: SimulationAction[M] = State {s =>

    // Updating of events can only happen during iterations. Verify that our run-state allows this.
    assert(s.runState.canIterate)

    // Execute the actions associated with the event, returning the result.
    s.current.get.action.dispatch.run(s).value
  }

  /** Perform an ''event iteration''.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  An event iteration involves:
   *   1. Updating the current event to the event at the head of event queue, and removing that event from the event
   *      queue.
   *   1. Dispatching the new current event, so that its associated actions take place.
   *   1. Report the resulting simulation update.
   *
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the iteration operation: `Unit`, wrapped in a `[[scala.util.Success Success]]`, if successful; an
   *  exception instance identifying the cause of the failure, wrapped in a `[[scala.util.Failure Failure]]` otherwise.
   */
  private[engine] def iterate[M <: ModelState[M]]: SimulationAction[M] = for {
    r <- takeUntilFailure {
      List(

        // Update the current event.
        updateCurrentEvent[M],

        // Dispatch the current event.
        dispatchCurrentEvent[M]
      )
    }
  } yield r

  /** Run the simulation, until it completes.
   *
   *  @note A model terminates either when an error is encountered, or when the the last simulation snap is completed,
   *  whichever comes first.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @param initialModelState Initial state of the simulation model at the start of the run.
   *
   *  @param warmUpPeriod Duration, measured in simulation time from the start of the simulation run, allowing the
   *  simulation to ''warm-up'' (that is, fully populating the simulation model and removing the effects of
   *  ''initialization bias''), after which simulation statistics will be reset. If omitted, this value defaults to 1
   *  week.
   *
   *  @param snapLength Duration, measured in simulation time, of each simulation ''snap'' (simulation reporting
   *  period), with the first such snap starting after the warm-up period has completed. At the end of each snap, the
   *  statistics are reset and a report generated. If omitted, this defaults to one week.
   *
   *  @param numSnaps Number of simulation snaps to be undertaken. The simulation will terminate when the last snap has
   *  completed. This value must be greater than 0, or an error will occur. This value defaults to 1.
   *
   *  @param initialization Actions necessary to initialize the simulation, such as scheduling initial events.
   *
   *  @return Final simulation state as the first element of a tuple that also includes the result of the last
   *  transition, wrapped in a `[[scala.util.Try Try]]`.
   */
  def run[M <: ModelState[M]](initialModelState: M, warmUpPeriod: Time = Days(7.0), snapLength: Time = Days(7.0),
  numSnaps: Int = 1)(initialization: Action[M]): (SimulationState[M], Try[Unit]) = {

    // Helper function to keep performing events until the simulation has completed.
    @tailrec
    def nextEvent(result: (SimulationState[M], Try[Unit])): (SimulationState[M], Try[Unit]) = {

      // If the previous operation failed, or if the simulation cannot perform further iterations, return the result.
      if(!result._1.runState.canIterate || result._2.isFailure) result

      // Otherwise, perform another iteration.
      else nextEvent(iterate.run(result._1).value)
    }

    // Create the initial simulation state.
    val initState = initialState(initialModelState)

    // Now initialize the simulation using the initial state.
    nextEvent(initialize(warmUpPeriod, snapLength, numSnaps, initialization).run(initState).value)
  }

  /** Event scheduling the dispatch of specified actions at a specified simulation time.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @constructor Create an event instance to ensure that an action to change the simulation state is performed at a
   *  specified time in the future.
   *
   *  @param id Unique identifier for this event. As well as uniquely identifying each event, `id` values serve to
   *  record event creation order, such that when comparing two events, that with the lower `id` value was the first of
   *  the two be scheduled.
   *
   *  @param dueAt Absolute simulation time at which the event's `action` is scheduled to occur.
   *
   *  @param priority Relative priority of this event. The lower this value, the higher the priority of the associated
   *  event. Co-incidental events will be dispatched in order of their priority.
   *
   *  @param action Action to be performed by this event when it is dispatched.
   */
  private[engine] final case class Event[M <: ModelState[M]](id: Long, dueAt: Time, priority: Int = 0,
  action: Action[M])
  extends Ordered[Event[M]] {

    /** Compare this event to another event.
     *
     *  When comparing two events that have yet to occur, the event that compares as ''less than'' the other event must
     *  always be dispatched first.
     *
     *  @note It is possible for an event that is occurring, or that has already occurred, to compare as ''greater
     *  than'' an event that has yet to occur, but only if the latter was scheduled ''after'' the former was dispatched.
     *  Even so, since time cannot run backwards, the latter cannot be due at an earlier time than the former.
     *
     *  @param that Event that this event is being compared to.
     *
     *  @return An integer value indicating the result of the comparison. If this value is less than zero, then this
     *  event compares as ''less than'' `that` event; if this value is greater than zero, then this event compares as
     *  ''greater than'' `that` event. Two events should ''never'' compare as equal (unless they are the same instance),
     *  since the event's [[id]] must be unique.
     */
    override def compare(that: Event[M]): Int = {

      // Compare the two events based upon their due times. If the value is non-zero, then return that result;
      // otherwise, the events are co-incident (occurring at the same simulation time) and we must look at the two
      // events' priorities.
      val dueAtOrder = dueAt.compare(that.dueAt)
      if(dueAtOrder != CompareEqualTo) dueAtOrder
      else {

        // Compare the two events based upon their priorities. Because priorities are higher the lower the value, we can
        // simply numerically compare the two. If the value is non-zero, then return that result; otherwise, the events
        // are co-incident AND have the same priority and we must look at the two events' identifiers to determine their
        // ordering.
        val priorityOrder = priority.compare(that.priority)
        if(priorityOrder != CompareEqualTo) priorityOrder
        else {

          // Compare the two events based upon their identifiers. These should not compare equal unless that event is
          // this event.
          assert(id != that.id || (this ne that), s"This event '$this' cannot equal event '$that'")
          id.compare(that.id)
        }
      }
    }
  }
}