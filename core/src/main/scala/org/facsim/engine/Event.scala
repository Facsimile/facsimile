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
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file belonging to the org.facsim.facsimile.engine package.
*/
//=============================================================================

package org.facsim.engine

import org.facsim.measure.Time
import scala.math.Ordered

//=============================================================================
/**
Class representing all $facsimile simulation events.

Events are constructed and scheduled for dispatch via the
[[org.facsim.facsimile.engine.Simulation.schedule schedule]] function; events
cannot be constructed by user code.

When an event is dispatched, its associated actions are executed, changing the
state of the simulation.

@constructor Create event by associated it with a set of actions that are to be
dispatched after the specified delay, with the specified priority.

@param action Action to be executed when this event is dispatched.

@param delay Delay, measured relative to the current time, after which the
event should be dispatched.  Events will be dispatched in order of their due
time.

@param priority Event's relative priority.  The higher this value, the higher
the priority of the event.  If two events are scheduled for dispatch at the
same due time, the event with the higher priority value be dispatched first.
If two events are scheduled for dispatch at the same due time, with the same
priority, then the events will be dispatched in the order in which they were
scheduled.

@since 0.0
*/
//=============================================================================

final class Event private [engine] (private val action: Action,
delay: Time.Measure, private val priority: Int)
extends Ordered [Event]
with NotNull {

/*
Argument verification.

Since these arguments are types that implement NotNull, this may not be
necessary, but better safe than sorry.
*/

  require (action ne null)
  require (delay ne null)

/**
Scheduled absolute dispatch time of this event.
*/

  val due = Simulation.currentTime + delay

/**
Unique event ID.

This is (in effect) the number of events created at the moment the event was
created.  It is incremented each time a new event is created.
*/

  private val id = EventId.nextId

//-----------------------------------------------------------------------------
/**
Compare this to another event.

The two events are compared on the basis of three factors:

 1. Their due time.

 1. If event due times are identical, their priority.

 1. If event due times and priorities are identical, their order of creation.

@param that Event to be compared to.

@return -1 if this event should be dispatched before the other event, 1 if this
event should be dispatched after the other event.  A value of 0, indicating
that the two events should be dispatched at the same time, should only occur if
an event is compared to itself.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def compare (that: Event): Int = {

/*
Compare the due times of the two events.  If there's a difference, return it.
*/

    val dueCompare = due.compare (that.due)
    if (dueCompare != 0) return dueCompare

/*
We only make it this far if the two events have the same due time.  Now compare
their priorities.  Note that we negate the result of the priority comparison
since we want this event to come before the other event its has a higher
priority value, and after the other event if it has a lower priority value.
*/

    assert (due == that.due && dueCompare == 0)
    val priorityCompare = priority.compare (that.priority)
    if (priorityCompare != 0) return -priorityCompare

/*
OK.  If we have made it to here, the two events have the same due time and the
same priority.  So we return the result of comparing their ids.  (If the two
events have the same ID, then it ought to be the same event.

TODO: It might be worth attempting to remove the ID from event at some point in
the future if it can be done without breaking the ordering rules.
*/

    assert (priority == that.priority && priorityCompare == 0)
    assert (id != that.id || (this eq that))
    id.compare (that.id)
  }

//-----------------------------------------------------------------------------
/**
Describe the event.

@return String containing a description of the event.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def description = action.description;

//-----------------------------------------------------------------------------
/**
Dispatch the event.

When the event is dispatched, its actions are to be executed.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [engine] final def dispatch (): Unit = action.execute ();
}
