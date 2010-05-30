/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2010, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

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

Java source file belonging to the org.facsim.facsimile.engine package.
*/
//=============================================================================

package org.facsim.facsimile.engine;

import org.facsim.facsimile.measure.Time;
import org.facsim.facsimile.util.PackagePrivate;

//=============================================================================
/**
<p>Abstract base class for all events.</p>
*/
//=============================================================================

public abstract class Event
implements java.lang.Comparable <Event>
{

/**
<p>Event due time.</p>

<p>The absolute simulation clock time at which the event is currently scheduled
to occur.</p>
*/

    private Time due;

/**
<p>Event priority.</p>

<p>The higher this value, the higher the priority of this event.</p>
*/

    private int priority;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Event constructor.</p>

<p>Creates a new event and schedules it for execution in the simulation's event
queue.<p>

@param delta Relative time, measured from the current simulation time, to the
event being due for execution.

@param priority The event's relative priority.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Event (Time delta, int priority)
    {
        super ();
        this.due = Time.add (Simulation.currentTime (), delta);
        this.priority = priority;
        Simulation.schedule (this);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Object#hashCode()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final int hashCode ()
    {

/*
Hash the event by performing an exclusive-or of the priority against the due
time's hash.  Since most priorities are 0, this should be fine.
*/

        return this.due.hashCode () ^ this.priority;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Object#equals(java.lang.Object)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final boolean equals (Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass () != obj.getClass ())
        {
            return false;
        }
        Event other = (Event) obj;
        if (this.due == null)
        {
            if (other.due != null)
            {
                return false;
            }
        }
        else if (!this.due.equals (other.due))
        {
            return false;
        }
        if (this.priority != other.priority)
        {
            return false;
        }
        return true;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Determine the event's due time.

@return The time at which the event is due to occur.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final Time dueAt ()
    {
        return this.due;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Comparable#compareTo(java.lang.Object)

Note that we deliberately do not check whether the two objects are the same.
We should almost never need this procedure to do that, so the comparison would
be a waste of clock cycles.  Consequently, this procedure is expensive if the
two events are actually the same object.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final int compareTo (Event other)
    {

/*
Compare the two times, returning the comparison as long as the two due times
are not equal.

Note: If other is null, then this will result in the throwing of a
java.lang.NullPointerException.  This is not only desirable, it is a required
part of the contract of fulfilling the compareTo method.
*/

        int comparison = this.due.compareTo (other.due);
        if (comparison != 0)
        {
            return comparison;
        }

/*
OK.  So the two events occur at the same time.  Make the determination based
upon their relative priorities.  If this event has a higher priority than the
other event, then this event is less than the other event - you read that
right, this event should be dispatched BEFORE the other event.
*/

        if (this.priority > other.priority)
        {
            return -1;
        }

/*
Otherwise, if this priority is less than the other event's priority, then this
event is greater than the other event - i.e. this event should be dispatched
AFTER the other event because it has a lower priority.
*/

        if (this.priority < other.priority)
        {
            return 1;
        }

/*
OK.  We have a tie.

TODO: Give preference to the event that was created first, to preserve
first-come, first-served order?
*/

        return 0;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Dispatch the event.</p>

<p>This procedure should be invoked only by the Simulation class as part of its
event dispatch loop.  If this is not followed, then rogue events can be
dispatched outside of the control of the simulation.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    void dispatch ()
    {
        execute ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Execute this event.</p>

<p>Override this procedure to implement the event's actions when the event is
dispatched by the simulation.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected abstract void execute ();
}
