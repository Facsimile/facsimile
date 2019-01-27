/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2019, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file belonging to the org.facsim.facsimile.engine package.
*/

package org.facsim.engine

import org.facsim.{assertNonNull, requireNonNull, LibResource}
import org.facsim.measure.Time
import scala.annotation.tailrec
import scala.collection.mutable.{PriorityQueue => MutableQueue}

/**
Simulation.

@since 0.0
*/

object Simulation {

/**
Event queue.

Events are queued up and dispatched in order (see
[[org.facsim.engine.Event.compare(Event)]] for further information). Events are
removed, and dispatched one-at-a-time - with the event being dispatched right
now termed the ''current event''. The current event does not belong to the
eventQueue.
*/

  private val eventQueue = MutableQueue[Event]()

/**
Current event.
*/

  private var currentEvent: Event = // scalastyle:ignore
  schedule(NullAction, Time.Zero, 0)

/**
Absolute time at which the simulation's statistics were last reset.
*/

  private var resetTime = Time.Zero // scalastyle:ignore

/**
Report current simulation time.

@return Current absolute simulation time.

@since 0.0
*/
  final def currentTime: Time.Measure = synchronized {
    currentEvent.due
  }

/**
Schedule the simple actions for execution.

@param action to be scheduled for execution.

@param dueIn Time to elapse before action is executed. If this value is 0.0,
then the action will be executed at the current simulation time, but not until
after the current event, and any other current event with a higher priority
scheduled at the current simulation time, has completed.

@param priority Priority of the action; the higher the value, the higher the
priority. Actions scheduled to execute at the same time will be dispatched in
order of their priority; actions scheduled at the same time with the same
priority are dispatched in the order that they are scheduled.

@return The event scheduled to perform the specified action.

@throws NullPointerException if `action` or `dueIn` are `null`.

@since 0.0
*/
  final def schedule(action: Action, dueIn: Time.Measure, priority: Int = 0) =
  {

/*
Sanity checks.
*/

    requireNonNull(action)
    requireNonNull(dueIn)

/*
Synchronize access to the event queue and schedule the new event.
*/
    synchronized {
      val event = new Event(action, dueIn, priority)
      scheduleEvent(event)
      event
    }
  }

/**
Schedule event.

@param event Event to be scheduled for dispatch.
*/
  private def scheduleEvent(event: Event): Unit = synchronized {
    assertNonNull(event)
   (eventQueue += event)
  }

/**
Run simulation.

This function never returns, but might terminate if an exception occurs.

@throws OutOfEventsException if the simulation runs out of events.

@since 0.0
*/
  @tailrec
  private[engine] def run(): Nothing = {

/*
If there are no more events left, then throw the out-of-events exception.  This
should generally be treated as a sign that event propagation has failed.
*/

    if(eventQueue.isEmpty) throw new OutOfEventsException

/*
Update the current event (and, hence, the current simulation time) to the event
at the head of the event queue, removing that event in the process.
*/

    currentEvent = eventQueue.dequeue()
    assertNonNull(currentEvent)

/*
Now dispatch this event - have it execute its actions.
*/

    currentEvent.dispatch()

/*
Use tail recursion to perform the next event.
*/

    run()
  }

/**
Report the time at which the simulation was last reset.

@return Absolute simulation time at which the simulation's statistics were last
reset.

@since 0.0
*/
  def lastReset = synchronized {
    resetTime
  }

/**
Null action class.

Represents actions that should never be executed in practice.  The initial
current event is such an action, it provides the initial simulation time, but
is never actually executed.
*/
  private object NullAction
  extends Action {

/**
@inheritdoc
*/
    override def description =
    LibResource("engine.Simulation.NullAction.description")

/**
@inheritdoc

@note Null actions should never actually be executed; doing so will result in
an exception.
*/
    override def apply() = throw new NullActionException
  }
}
