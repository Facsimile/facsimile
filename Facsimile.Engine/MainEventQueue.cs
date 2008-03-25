/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2008, Michael J Allen.

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program.  If not, see <http://www.gnu.org/licenses/>.

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

C# source file for the MainEventQueue class, and associated elements, that are
integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>The root simulation event queue.</summary>

<remarks>This event queue forms the root of the hierarchy of event queues.

<para>Although technically a Singleton in design pattern terms (refer to Gamma,
et al: "Design Patterns: Elements of Reusable Object-Oriented Software",
Addison-Wesley, for further information), since the class may only be
instantiated internally, it is implemented as a regular, non-Singleton
class.</para></remarks>
*/
//=============================================================================

    public sealed class MainEventQueue:
        EventQueue
    {

/**
<summary>The "current event".</summary>

<remarks>This value is null until the first event is dispatched, and can become
null if the simulation runs out of scheuled events.

<para>This must be an actual event, and not merely a proxy event (such as a
<see cref="HierarchicalEventQueue" /> instance masquerading as its next
event).</para></remarks>
*/

        private Event current;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor.</summary>

<remarks>Construct the main event queue.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal MainEventQueue ():
            base ()
        {
            current = null;
            System.Diagnostics.Debug.Assert (current == null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Dispatch the next event.</summary>

<remarks>Retrieve and execute the next event on main event queue, updating the
simulation clock to this event's absolute due time.  If there is no next event,
then an exception is raised.</remarks>

<exception cref="OutOfEventsException">Thrown if there are no events scheduled
when called.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal void Dispatch ()
        {

/*
Get the next event that we have scheduled.  This may be a proxy event (such as
an event queue masquerading as its next event).  If this is null, then throw
the appropriate exception.
*/

            IEvent nextEvent = NextEvent;
            if (nextEvent == null)
            {
                throw new OutOfEventsException ();
            }
            //System.Diagnostics.Debug.Assert (nextEvent.IsScheduled);

/*
Get this event's actual event.
*/

            current = nextEvent.ActualEvent;
            System.Diagnostics.Debug.Assert (current != null);
            //System.Diagnostics.Debug.Assert (current.IsScheduled);

/*
De-schedule the next event by removing it from our event queue.  (If it's a
proxy, this will cascade down until the actual event is de-scheduled.)
*/

            Deschedule (nextEvent);
        }
    }
}
