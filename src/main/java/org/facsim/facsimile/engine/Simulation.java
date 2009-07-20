/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2009, Michael J Allen.

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

import java.util.PriorityQueue;
import java.util.NoSuchElementException;
import org.facsim.facsimile.measure.Time;

//=============================================================================
/**
Simulation class.

Controls simulation model execution.
*/
//=============================================================================

public final class Simulation
{

/**
Event queue.

Queues up events for execution in order of their due time and priority.
*/

    private static PriorityQueue <Event> eventQueue;

/**
The event currently being dispatched.
*/

    private static Event currentEvent;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Static initializer.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {

/*
Create the event queue.  This will initially contain no events.
*/

        eventQueue = new PriorityQueue <Event> ();

/*
Initially, we do not have a current event.
*/

        currentEvent = null;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Report the current simulation time.

@return The current simulation time.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Time currentTime ()
    {

/*
If we do not have a current event, then the simulation has yet to start.  Just
report 0 time.
*/

        if (currentEvent == null)
        {
            return Time.zero ();
        }

/*
Otherwise, report the event's due time.
*/

        return currentEvent.dueAt ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Schedule an event for execution.

This function should only be called by the event itself, to prevent scheduling
of rogue events.  Futhermore, the event should not be scheduled prior to the
call.

@param event The event to be scheduled.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void schedule (Event event)
    {

/*
Add the event to the event queue.
*/

        eventQueue.add (event);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Run the simulation, dispatching each event in turn.

@throws org.facsim.facsimile.engine.OutOfEventsException Thrown if the simulation has no
further scheduled events remaining.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void run ()
    throws OutOfEventsException
    {

/*
Repeat indefinitely.
*/

        for (;;)
        {

/*
Retrieve the next event from the event queue, making it the currently executing
event.  If the eventQueue throws the NoSuchElementException, meaning that the
event queue is empty, then throw the OutOfEventsException.
*/

            try
            {
                currentEvent = eventQueue.remove ();
            }
            catch (NoSuchElementException e)
            {
                throw new OutOfEventsException ();
            }

/*
Dispatch this event.
*/

            currentEvent.dispatch ();
        }
    }
}
