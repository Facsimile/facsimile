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

import org.facsim.util.CompareEqualTo
import squants.Time
import squants.time.Seconds

/** Encapsulates simulation system state, at an instant in (simulation) time.
 *
 *  System state tracks the current simulation time, the event currently being dispatched, and the set of events that
 *  are scheduled to be dispatched at a future simulation time.
 *
 *  @constructor Construct a new system simulation state instance.
 *
 *  @param nextEventId Identifier of the next simulation event to be created.
 *
 *  @param current Event currently being dispatched, wrapped in [[scala.Some]]; if [[scala.None]], then the simulation
 *  has typically not yet started running.
 *
 *  @param events Set of simulation events scheduled to occur at a future simulation time.
 *
 *  @param runState Current state of the simulation run.
 */
private[engine] final case class SystemState private(nextEventId: Long, current: Option[SystemState.Event],
events: PriorityQueue[SystemState.Event], runState: RunState = Initializing) {

  /** Current simulation time.
   *
   *  @return Current simulation time.
   */
  private[engine] def time: Time = current.fold(Seconds(0.0))(_.dueAt)

  /** Schedule an action to occur.
   *
   *  @param delay Simulation time that must elapse, measured from the current simulation time, before the action is due
   *  to be executed. This value cannot be less than zero. If the value is zero, `action` will be scheduled for
   *  execution at the current simulation time.
   *
   *  @param priority Relative priority for execution of the `actions`, should they be co-incident with other actions.
   *
   *  @param actions Actions to be performed.
   *
   *  @return New simulation system state resulting from scheduling the event.
   */
  private[engine] def schedule(delay: Time, priority: Int, actions: Action): SystemState = {

    // Create the event.
    val event = SystemState.Event(nextEventId, delay + time, priority, actions)

    // Add it to the event queue.
    val newEvents = events + event

    // Return the new system state.
    copy(nextEventId = Math.incrementExact(nextEventId), events = newEvents)
  }

  /** Identify the next event to be dispatched, and the new associated system state.
   *
   *  @return Simulation system state before the next event is dispatched.
   */
  private[engine] def updateEventState: SystemState = {

    // Identify the next event, as the event at the head of the event queue.
    events.minimumRemove match {

      // If the next event is None, then the simulation has run out of events and has completed.
      case (None, _) => copy(runState = Completed)

      // Otherwise, we have a new event to perform and a new set of events.
      case(newCurrent, newEvents) => copy(current = newCurrent, events = newEvents)
    }
  }
}

/** System state companion object. */
private object SystemState {

  /** Event scheduling the dispatch of specified actions at a specified simulation time.
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
  private[engine] final case class Event private(id: Long, dueAt: Time, priority: Int = 0, action: Action)
  extends Ordered[Event] {

    /** Dispatch this event.
     *
     *  Apply this event's actions to the current simulation state, returning the new simulation state in the process.
     *  The simulation state will be updated accordingly before the next event is dispatched.
     */
    private val dispatch: StateResult[Unit] = action.execute

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
    override def compare(that: Event): Int = {

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