/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file belonging to the org.facsim.facsimile.engine package.
*/
//=============================================================================

package org.facsim.facsimile.engine
import scala.collection.mutable.PriorityQueue
import org.facsim.facsimile.measure.Time
import org.facsim.facsimile.measure.TimeUnit
import org.facsim.util.Resource

//=============================================================================
/**
Simulation.

@since 0.0
*/
//=============================================================================

object Simulation {

/**
Event queue.

Events are queued up and dispatched in order (see
[[org.facsim.facsimile.Event.compare(Event)]] for further information).  Events
are removed, and dispatched one-at-a-time - with the event being dispatched
right now termed the ''current event''.  The current event does not belong to
the eventQueue.
*/

  private val eventQueue = PriorityQueue [Event] ()

/**
Current event.
*/

  private var currentEvent: Event = schedule (new NullAction, Time.time (0.0,
  TimeUnit.seconds), 0)

/**
Absolute time at which the simulation's statistics were last reset.
*/

  private var resetTime: Time = Time.time (0.0, TimeUnit.seconds)

//-----------------------------------------------------------------------------
/**
Report current simulation time.

@return Current absolute simulation time.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def currentTime = currentEvent.due

//-----------------------------------------------------------------------------
/**
Schedule the simple actions for execution.

@param action to be scheduled for execution.

@param dueIn Time to elapse before action is executed.  If this value is 0.0,
then the action will be executed at the current simulation time, but not until
after the current event, and any other current event with a higher priority
scheduled at the current simulation time, has completed.

@param priority Priority of the action; the higher the value, the higher the
priority.  Actions scheduled to execute at the same time will be dispatched in
order of their priority; actions scheduled at the same time with the same
priority are dispatched in the order that they are scheduled.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def schedule (action: Action, dueIn: Time, priority: Int = 0) =
  scheduleEvent (new Event (action, dueIn, priority))

//-----------------------------------------------------------------------------
/**
Schedule event.

@param event Event to be scheduled for dispatch.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [engine] def scheduleEvent (event: Event) = {
    (eventQueue += event)
    event
  }

//-----------------------------------------------------------------------------
/**
Run simulation.

This function never returns, but might terminate if an exception occurs.

@throws OutOfEventsException if the simulation runs out of events.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [engine] def run (): Nothing = {

/*
If there are no more events left, then throw the out-of-events exception.  This
should generally be treated as a sign that event propagation has failed.
*/

    if (eventQueue.isEmpty) throw new OutOfEventsException ()

/*
Update the current event (and, hence, the current simulation time) to the event
at the head of the event queue, removing that event in the process.
*/

    currentEvent = eventQueue.dequeue ()
    assert (currentEvent ne null)

/*
Now dispatch this event - have it execute its actions.
*/

    currentEvent.dispatch ()

/*
Use tail recursion to perform the next event.
*/

    run ();
  }

//-----------------------------------------------------------------------------
/**
Report the time at which the simulation was last reset.

@return Absolute simulation time at which the simulation's statistics were last
reset.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def lastReset = resetTime

//-----------------------------------------------------------------------------
/**
Null action class.

Represents actions that should never be executed in practice.  The initial
current event is such an action, it provides the initial simulation time, but
is never actually executed.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final class NullAction
  extends Action {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Provide a description for the null actions.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override final def description =
    Resource.format ("engine.Simulation.NullEvent.description")

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Provide null actions.

Null actions should never actually be executed; if this exception is thrown, an
error is issued.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    override final def execute = sys.error (Resource.format
    ("engine.Simulation.NullEvent.executeError"))
  }
}
