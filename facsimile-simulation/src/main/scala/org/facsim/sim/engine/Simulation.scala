//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
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
// Scala source file belonging to the org.facsim.sim.engine package.
//======================================================================================================================
package org.facsim.sim.engine

import cats.data.State
import org.facsim.collection.immutable.BinomialHeap
import org.facsim.sim.{Priority, SimulationAction, SimulationTransition}
import org.facsim.sim.model.{Action, AnonymousAction, EndWarmUpAction, ModelState}
import scala.language.implicitConversions
import scala.reflect.runtime.universe.TypeTag
import scala.util.{Failure, Success, Try}
import squants.Time
import squants.time.Days

/** Simulation model class.
 *
 *  @tparam M Final type of the simulation's model state.
 *
 *  @since 0.0
 */
final class Simulation[M <: ModelState[M]: TypeTag] {

  /** Implicit reference to the simulation. */
  implicit val SimulationRef: Simulation[M] = this

  /** Report the current simulation time.
   *
   *  @return Simulation state transition combining the current simulation state and the current simulation time.
   *
   *  @since 0.0
   */
  def time: SimulationTransition[M, Time] = State.inspect(_.simTime)

  /** Report the current simulation model state.
   *
   *  @return Simulation state transition combining the current simulation state and the current simulation model state.
   *
   *  @since 0.0
   */
  def modelState: SimulationTransition[M, M] = State.inspect(_.modelState)

  /** Schedule actions for later execution.
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
   *  success of the actions: `Unit`, wrapped in a [[Success]], if successful; an exception
   *  instance identifying the cause of the failure, wrapped in a [[Failure]] otherwise.
   *
   *  @since 0.0
   */
  def at(delay: Time, priority: Priority = 0)(actions: Action[M]): SimulationAction[M] = State {s =>

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
   *  @tparam A Type of value resulting from the simulation transitions.
   *
   *  @param ts Sequence of simulation state transitions to be processed.
   *
   *  @param terminationValue Value to be returned, together with the current state, if all of the elements in `ts`
   *  where processed.
   *
   *  @param p A _predicate_ function that is used to determine whether to stop processing subsequent transitions. The
   *  predicate can evaluate the state and return value of each transition. If the predicate succeeds, then execution of
   *  any remaining transitions terminates and the result of the last transition processed is returned; otherwise, if
   *  the predicate fails for the current result, then the following transition will be executed.
   *
   *  @return Result (including state) of the last transition executed, or the current state plus the last value if all
   *  transitions were processed.
   *
   *  @since 0.0
   */
  def takeUntil[A: TypeTag](ts: Seq[SimulationTransition[M, A]], terminationValue: A)
  (p: ((SimulationState[M], A)) => Boolean): SimulationTransition[M, A] = {

    // Transition type.
    type Trn = SimulationTransition[M, A]

    // Helper function to perform the iteration.
    def nextIteration(xs: Seq[Trn]): Trn = {

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
   *  @param ts Sequence of simulation state transitions to be processed.
   *
   *  @return Result (including state) of the last transition executed, or the current state plus the last value if all
   *  transitions were processed.
   *
   *  @since 0.0
   */
  def takeUntilFailure(ts: Seq[SimulationAction[M]]): SimulationAction[M] = for {
    r <- takeUntil(ts, Success(())) {
      case(_, x) => x.isFailure
    }
  } yield r

  /** Update the simulation run state.
   *
   *  @param newState Updated simulation run state.
   *
   *  @return Updated simulation state, plus a success.
   */
  private[sim] def updateRunState(newState: RunState): SimulationAction[M] = State {s =>
    (s.update(newRunState = newState), Success(()))
  }

  /** Update the simulation model state.
   *
   *  @param newState Updated simulation model state.
   *
   *  @return Updated simulation state, plus a success.
   *
   *  @since 0.0
   */
  def updateModelState(newState: M): SimulationAction[M] = State {s =>
    (s.update(newModelState = newState), Success(()))
  }

  /** Initial simulation state.
   *
   *  @param initialModelState Initial simulation model state.
   *
   *  @return Initial simulation state, for use at the start of the simulation.
   */
  private def initialState(initialModelState: M): SimulationState[M] = {
    new SimulationState(initialModelState, 0L, None, BinomialHeap.empty[Event[M]], Initializing)
  }

  /** Initialize the simulation.
   *
   *  Prepare the simulation to commence running. This involves the following steps:
   *
   *   1. Schedule the end of the simulation warm-up period.
   *   2. Initialize the simulation model, creating any necessary initial events to start the simulation running.
   *   3. Change the state of the simulation to be executing.
   *
   *  @param warmupLength Duration, measured in simulation time from the start of the simulation run, allowing the
   *  simulation to _warm-up_ (that is, fully populating the simulation model and removing the effects of
   *  _initialization bias_), after which simulation statistics will be reset. If omitted, this value defaults to 1
   *  week.
   *
   *  @param snapLength Duration, measured in simulation time, of each simulation _snap_ (simulation reporting
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
  private def initialize(warmupLength: Time, snapLength: Time, numSnaps: Int, initialization: Action[M]):
  SimulationAction[M] = for {

    // Determine the status of the following actions, and return it as the status of this action, together with the
    // updated simulation state.
    r <- takeUntilFailure {
      List[SimulationAction[M]](

        // Schedule the end of the warm-up period. This should have the lowest possible priority, so that all
        // simultaneous events are completed before the reset occurs.
        at(warmupLength, Int.MaxValue)(new EndWarmUpAction[M](snapLength, numSnaps)),

        // Perform custom initialization actions.
        initialization.dispatch,

        // Update the simulation state to be executing.
        updateRunState(Executing)
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
   *  @note The new simulation time (the time at which the new current event is scheduled to occur) *must* be
   *  greater than or equal to the due time of the initial current event.
   *
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the update operation: `Unit`, wrapped in a [[Success]], if successful; an
   *  exception instance identifying the cause of the failure, wrapped in a [[Failure]] otherwise.
   */
  private def updateCurrentEvent: SimulationAction[M] = State {s =>

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
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the dispatch operation: `Unit`, wrapped in a [[Success]], if successful; an
   *  exception instance identifying the cause of the failure, wrapped in a [[Failure]] otherwise.
   */
  private def dispatchCurrentEvent: SimulationAction[M] = State {s =>

    // Updating of events can only happen during iterations. Verify that our run-state allows this.
    assert(s.runState.canIterate)

    // Execute the actions associated with the event, returning the result.
    s.current.get.action.dispatch.run(s).value
  }

  /** Perform an _event iteration_.
   *
   *  An event iteration involves:
   *   1. Updating the current event to the event at the head of event queue, and removing that event from the event
   *      queue.
   *   1. Dispatching the new current event, so that its associated actions take place.
   *   1. Report the resulting simulation update.
   *
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the iteration operation: `Unit`, wrapped in a [[Success]], if successful; an
   *  exception instance identifying the cause of the failure, wrapped in a [[Failure]] otherwise.
   */
  private def iterate: SimulationAction[M] = for {
    r <- takeUntilFailure {
      List(

        // Update the current event.
        updateCurrentEvent,

        // Dispatch the current event.
        dispatchCurrentEvent
      )
    }
  } yield r

  /** Execute all remaining events until either an error occurs or the simulation completes.
   *
   *  An event iteration involves:
   *   1. Updating the current event to the event at the head of event queue, and removing that event from the event
   *      queue.
   *   1. Dispatching the new current event, so that its associated actions take place.
   *   1. Report the resulting simulation update.
   *
   *  @return Simulation state transition containing the updated simulation state, together with a value indicating the
   *  success of the iteration operation: `Unit`, wrapped in a [[Success]], if successful; an
   *  exception instance identifying the cause of the failure, wrapped in a [[Failure]] otherwise.
   */
  private def remainingEvents: SimulationAction[M] = State {s =>

    // Perform an event iteration and consider the result.
    val result = iterate.run(s).value

    // If the iteration resulted in an error, or if the simulation completed, return the result.
    if (result._2.isFailure || !result._1.runState.canIterate) result

    // Otherwise, perform another iteration. and report the result.
    //
    // Note: It might appear that this code will result in a stack overflow, for any long simulation with large numbers
    // of events, but it does not: the Cats library state monad utilizes Eval trampolining to overcome this. (If that
    // make little sense, Google it ;-)
    else remainingEvents.run(result._1).value
  }

  /** Run the simulation, until it completes.
   *
   *  @note A model terminates either when an error is encountered, or when the the last simulation snap is completed,
   *  whichever comes first.
   *
   *  @param initialModelState Initial state of the simulation model at the start of the run.
   *
   *  @param warmUpPeriod Duration, measured in simulation time from the start of the simulation run, allowing the
   *  simulation to _warm-up_ (that is, fully populating the simulation model and removing the effects of
   *  _initialization bias_), after which simulation statistics will be reset. If omitted, this value defaults to 1
   *  week.
   *
   *  @param snapLength Duration, measured in simulation time, of each simulation _snap_ (simulation reporting
   *  period), with the first such snap starting after the warm-up period has completed. At the end of each snap, the
   *  statistics are reset and a report generated. If omitted, this defaults to one week.
   *
   *  @param numSnaps Number of simulation snaps to be undertaken. The simulation will terminate when the last snap has
   *  completed. This value must be greater than 0, or an error will occur. This value defaults to 1.
   *
   *  @param initialization Actions necessary to initialize the simulation, such as scheduling initial events.
   *
   *  @return Final simulation state as the first element of a tuple that also includes the result of the last
   *  transition, wrapped in a [[Try]].
   *
   *  @since 0.0
   */
  def run(initialModelState: M, warmUpPeriod: Time = Days(7.0), snapLength: Time = Days(7.0), numSnaps: Int = 1)
  (initialization: Action[M]): (SimulationState[M], Try[Unit]) = {

    // Execute the initialization and all subsequent events.
    val runToCompletion: SimulationAction[M] = for {
      r <- takeUntilFailure {
        List[SimulationAction[M]](
          initialize(warmUpPeriod, snapLength, numSnaps, initialization),
          remainingEvents
        )
      }
    } yield r

    // Create the initial simulation state.
    val initState = initialState(initialModelState)

    // Now initialize the simulation using the initial state and run it to completion.
    runToCompletion.run(initState).value
  }
}

/** Simulation companion object. */
object Simulation {

  /** Implicit conversion of simulation transitions (_actions_) to an [[org.facsim.sim.model.Action Action]]
   *  instance.
   *
   *  @tparam M Final type of the simulation's model state.
   *
   *  @param actions Raw actions to be converted into an [[org.facsim.sim.model.AnonymousAction AnonymousAction]]
   *  instance.
   *
   *  @return `actions` wrapped as an action suitable for dispatching by an event.
   */
  implicit def createAnonymousAction[M <: ModelState[M]: TypeTag](actions: SimulationAction[M]): AnonymousAction[M] = {
    new AnonymousAction[M](actions)
  }
}