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

C# source file for the IEvent interface, and associated elements, that are
integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Simulation event interface.</summary>

<remarks>In the context of a discrete-event simulation model, an "event" is a
set of actions, scheduled to occur at a specific moment in time (and taking
zero elapsed simulation time units to execute), that changes the state of the
simulation in some way.

<para>In Facsimile, events are dispatched (or executed) in order of their due
time and priority, such that an event scheduled to occur at 10:30am is
dispatched ahead of an event scheduled to occur at 11:00am.  If two events are
scheduled to occur at the same time, then the event with the higher priority
will be dispatched first.  (However, note that a lower priority event can
schedule a higher priority event to occur immediately afterwards, such that -
to an observer - it appeares that the lower priority event was dispatched
before the higher priority event.)  Only a single event can be dispatched at a
time.  Events cannot be scheduled to occur prior to the current simulation
time.</para>

<para>Events are stored in an <see cref="EventQueue">event queue</see> until
they have been dispatched.  Prior to dispatch, individual events may be
suspended, indicating that some condition is delaying them.  For instance, a
breakdown may delay a process's completion event.</para>

<para>Facsimile supports the use of a hierarchy of event queues, so that a
single event queue can be used to process a set of related events, which can
all be suspended/resumed en masse.  For example, say a process has a shift
pattern and a number of operations are still pending completion when the shift
goes off.  If we associate an event queue with the shift pattern, and schedule
the associated completion events on this event queue, then we can suspend the
event queue when the shift goes off and resume it when the shift comes back on;
while the event queue is suspended, so are all the completion events.</para>

<para>Each <see cref="HierarchicalEventQueue" /> sub-class instance (which
excludes the single <see cref="MainEventQueue" /> instance) acts as a proxy for
its next event in its parent event queue.  In general, it is not possible to
modify the proxied event through the proxy to avoid the resulting confusion.
For example, if we could suspend an event through a proxy (by suspending the
proxy event), then the proxy would, most likely, refer to a different proxied
event once the suspension operation had completed - probably not what the user
would expect.  Consequently, proxy properties relate to the proxy itself - not
to the proxied event.</para>

<para>Do not create user-defined events by implementing this interface alone;
instead, you should derive suitable sub-classes from <see cref="Event" />
instead.</para>

<para>When a simulation event is dispatched, the simulation clock is updated to
the time due of that event (or the proxy of that event) on the main event
queue.</para></remarks>
*/
//=============================================================================

    public interface IEvent:
        System.IComparable <IEvent>, Facsimile.Common.ISuspendable
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.Priority"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        int Priority
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.RelativeTimeRemaining"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        Facsimile.Common.Measure <Facsimile.Common.TimeUnit>
        RelativeTimeRemaining
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.RelativeDueTime"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        Facsimile.Common.Measure <Facsimile.Common.TimeUnit> RelativeDueTime
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml" path='commonDoc/member[@name="IEvent.Owner"]/*'
/>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        EventQueue Owner
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.IsActive"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        bool IsActive
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.IsScheduled"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        bool IsScheduled
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.ActualEvent"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        Event ActualEvent
        {
            get;
        }
    }
}
