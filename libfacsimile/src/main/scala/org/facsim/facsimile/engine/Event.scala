/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2011, Michael J Allen.

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
with Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://www.facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://www.facsim.org/Documentation/CodingStandards/
===============================================================================
$Id$

Scala source file belonging to the org.facsim.facsimile.engine package.
*/
//=============================================================================

package org.facsim.facsimile.engine
import scala.math.Ordered
import org.facsim.facsimile.measure.Time

//=============================================================================
/**
Abstract base class for all _Facsimile_ simulation events.

Events are scheduled for dispatch during construction.

@constructor Create and schedule event to be dispatched after the specified
delay, with the specified priority.

@param delay Delay, measured relative to the current time, after which the
event should be dispatched.  Events will be dispatched in order of their due
time.

@param priority Event's relative priority.  The lower this value, the higher
the priority of the event.  If two events are scheduled for dispatch at the
same due time, the event with the lower priority value be dispatched first.  If
two events are scheduled for dispatch at the same due time, with the same
priority, then the events will be dispatched in the order in which they were
scheduled.

@since 0.0-0
*/
//=============================================================================

abstract class Event (delay: Time, private val priority: Int) extends
Ordered [Event]
{

/**
Scheduled absolute dispatch time of this event.
*/

  private val due = Simulation.currentTime + delay

/**
Unique event ID.

This is (in effect) the number of events created at the moment the event was
created.  It is incremented each time a new event is created.
*/

  private val id = EventId.nextId

/*
Schedule this event for dispatch.

TODO: I'm really not sure about this.  Here's some arguments against it:
1.  Creation of test events is not possible.  The events have to be dispatched
    at some point by the event queue.  If events are not scheduled
    automatically, then they can be discarded.
2.  If an exception occurs during construction of a sub-class, a pointer to an
    unconstructed event will be retained in the event queue.  This has some
    complex side-effects.

On the other hand, if we don't schedule events during construction, there's
always a chance that the user will forget to do so.  We then also have to worry
more about the state of the event (unscheduled, scheduled, descheduled, done)
and about the freshness of the data (due, priority, etc.).

One idea is to make Event a private final class (rather than a public abstract
class) that takes a payload method argukent, which is executed when the event
is dispatched.  This would allow Event to be hidden away under the surface,
created and accessed solely by the Simulation object's schedule method.  This
would simplify the interface dramatically, and maybe make better use of Scala's
functional capabilities.
*/

  Simulation.schedule (this)

//-----------------------------------------------------------------------------
/**
Report absolute simulation time that event is scheduled to be dispatched.

@returns Absolute simulation time the event is due for dispatch.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  final def timeDue:Time = due

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

@since 0.0-0
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
their priorities.  If there's a difference, return it.
*/

    assert (due == that.due)
    val priorityCompare = priority.compare (that.priority)
    if (priorityCompare != 0) return priorityCompare

/*
OK.  If we have made it to here, the two events have the same due time and the
same priority.  So we return the result of comparing their ids.  (If the two
events have the same ID, then it ought to be the same event.

TODO: It might be worth attempting to remove the ID from event at some point in
the future if it can be done without breaking the ordering rules.
*/

    assert (priority == that.priority)
    assert (id != that.id || (this eq that))
    id.compare (that.id)
  }

//-----------------------------------------------------------------------------
/**
Dispatch the event.

Called when the event is dispatched by the simulation engine.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  private [engine] final def dispatch (): Unit = {

/*
Execute the event.
*/

    execute;
  }

//-----------------------------------------------------------------------------
/**
Execute the event.

Events typically change the state of the simulation in some way.  This
procedure should be overridden in order to achieve this.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  protected def execute (): Unit
}
