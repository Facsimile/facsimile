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

C# source file for the EventQueue class, and associated elements, that are
integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Abstract simulation event queue base class.</summary>

<remarks>In the context of a discrete-event simulation model, an "event queue"
is a set of simulation events, sorted in order of their due-time and priority.
Events at the head of the queue are dispatched in sequence, with the simulation
clock changing to the due-time of each dispatched event in turn.  Consequently,
the "clock" in a discrete-event simulation jumps from one discrete time to the
next, rather than progressing at a steady rate in a continuous fashion as with
most other types of clock.

<para>In general, when an event occurs, new events should be scheduled - a
process termed "event propagation".  If the simulation ever runs out of events,
that is, if an attempt is made to dispatch an event and there are no events to
dispatch, then the simulation stalls and terminates.  This is an expected
occurrence in a "terminating" simulation - a simulation that executes a
pre-defined set of operations in order to determine how long it takes to
complete.  However, in a "steady-state" simulation, the simulation is expected
to run indefinitely and should never run out of events.</para>

<para>In Facsimile, there is a hierarchy of event queues, rooted with a single
instance of the <see cref="MainEventQueue" /> class.  Each child event queue,
derived from the <see cref="HierarchicalEventQueue" /> class, implements the
<see cref="IEvent" /> interface and masquerades as their own next scheduled
event in their parent event queue.  The idea behind the child event queues is
that they can represent a resource (or set of resources), such as a shift
pattern, that have a set of dependent events.  If, say, a shift pattern goes
off-shift, then its corresponding event queue is suspended causing all events
scheduled on that shift to be suspended also until the shift comes back on
again and resumes its event queue.</para>

<para><see cref="EventQueue" /> is the abstract base class for all types of
event queue, whether the main event queue or the child event queues.  However,
it cannot be used as a base class for user-defined or for other Facsimile
classes.</para></remarks>
*/
//=============================================================================

    public abstract class EventQueue
    {

/**
<summary>The primary container holding scheduled events.</summary>

<remarks>The current implementation is a little over-the-top, and will be
replaced in the future by a C++ standard template library (STL)-style multiset
container.  This one is a little too cumbersome and inefficient (both in
execution time and storage overhead) for what is required.</remarks>
*/

        private System.Collections.Generic.SortedDictionary <IEvent, IEvent>
        queue;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>Initialise the basic event queue.

<para>This constructor has internal protected access so that only
Facsimile.Engine classes can derive from it - it cannot be used as a base class
for other Facsimile classes, or for user-defined classes.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected EventQueue ()
        {

/*
Create the event queue.
*/

            queue = new System.Collections.Generic.SortedDictionary <IEvent,
            IEvent> ();
            System.Diagnostics.Debug.Assert (queue != null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Schedule an event.</summary>

<remarks>Events cannot be scheduled (simultaneously) multiple times and so may
only be present in a single event queue at a time.  This cannot be checked
fully here, but should be controlled by the event class.

<para>A re-schedule operation is comprised of a deschedule followed by a
schedule operation.</para></remarks>

<param name="newEvent">A <see cref="IEvent" /> sub-class instance that is to be
scheduled.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal void Schedule (IEvent newEvent)
        {

/*
Argument sanity checks.
*/

            System.Diagnostics.Debug.Assert (newEvent != null);
            System.Diagnostics.Debug.Assert (newEvent.Owner == this);
            System.Diagnostics.Debug.Assert (!queue.ContainsKey (newEvent));

/*
Add the event to the queue, "scheduling" it in the process.
*/

            queue.Add (newEvent, newEvent);

/*
Perform sub-class-specific post-schedule processing.
*/

            OnSchedule (newEvent);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Deschedule an event.</summary>

<remarks>Events are descheduled when they become suspended, are in the process
of being re-scheduled, or are being dispatched.

<para>A re-schedule operation is comprised of a deschedule followed by a
schedule operation.</para></remarks>

<param name="newEvent">A <see cref="IEvent" /> sub-class instance that is to be
descheduled.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal void Deschedule (IEvent newEvent)
        {

/*
Argument sanity checks.
*/

            System.Diagnostics.Debug.Assert (newEvent != null);
            System.Diagnostics.Debug.Assert (newEvent.Owner == this);
            System.Diagnostics.Debug.Assert (queue.ContainsKey (newEvent));

/*
Remove the event from the queue, "descheduling" it in the process.
*/

            bool status = queue.Remove (newEvent);
            System.Diagnostics.Debug.Assert (status);

/*
Perform sub-class-specific post-deschedule processing.
*/

            OnDeschedule (newEvent);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Sub-class-specific post-schedule processing.</summary>

<remarks>This method should be overridden by sub-classes to implement sub-class
specific operations whenever an event is scheduled (including the cases where
an event is re-scheduled or resumed following suspension).

<para>This default version of the method does nothing.</para></remarks>

<param name="newEvent">The <see cref="IEvent" /> sub-class instance that has
just been scheduled.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual void OnSchedule (IEvent newEvent)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Sub-class-specific post-deschedule processing.</summary>

<remarks>This method should be overridden by sub-classes to implement sub-class
specific operations whenever an event is descheduled (including the cases where
an event is re-scheduled, suspended or dispatched).

<para>This default version of the method does nothing.</para></remarks>

<param name="newEvent">The <see cref="IEvent" /> sub-class instance that has
just been descheduled.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual void OnDeschedule (IEvent newEvent)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Report event that will be dispatched next.</summary>

<remarks>Only events that are currently active (i.e. not suspended) will be
reported.</remarks>

<value>A <see cref="IEvent" /> sub-class instance that represents the next
event to be scheduled on this event queue, or null if there is no next event
currently scheduled.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected IEvent NextEvent
        {
            get
            {

/*
Get the enumerator for the event queue.

TODO: When the queue container is updated, this statement must be updated too.
*/

                System.Diagnostics.Debug.Assert (queue != null);
                System.Collections.Generic.IEnumerator
                <System.Collections.Generic.KeyValuePair <IEvent, IEvent>>
                enumerator = queue.GetEnumerator ();
                System.Diagnostics.Debug.Assert (enumerator != null);

/*
Advance the enumerator to the next object in the collection.  If this returns
false, then there is no next object and we must return null.
*/

                if (!enumerator.MoveNext ())
                {
                    return null;
                }

/*
Otherwise, return the event reference after performing some verifications.
*/

                System.Diagnostics.Debug.Assert (enumerator.Current.Key ==
                enumerator.Current.Value);
                return enumerator.Current.Key;
            }
        }
    }
}
