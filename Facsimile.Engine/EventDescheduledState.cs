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

C# source file for the EventDescheduledState class, and associated elements,
that are integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Event descheduled state class.</summary>

<remarks>The <see cref="EventState" /> sub-class that is used as the state for
descheduled events; that is, for events that have never been scheduled for
execution.

<para>A single instance of this class is used to represent the descheduled
state of all events.</para>

<para>This state is primarily used by proxy events that do not have a current
proxied event.  For example, a <cref="HierarchicalEventQueue" /> instance that
does not have any active events will be in the descheduled state.  This implies
that <see cref="Event" /> sub-class instances can never be in the descheduled
state.</para></remarks>

<seealso cref="Facsimile.Common.EventState {FinalContext, BaseState}" />

<seealso cref="IEvent" />
*/
//=============================================================================

    public sealed class EventDescheduledState:
        EventState
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>This constructor is marked "internal" to prevent instantiation from
outside this assembly.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal EventDescheduledState ():
            base ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.TimeRemaining"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected override Facsimile.Common.Measure
        <Facsimile.Common.TimeUnit> TimeRemaining (EventQueue eventParent,
        Facsimile.Common.Measure <Facsimile.Common.TimeUnit> eventTime)
        {

/*
Descheduled events have an infinite time remaining.
*/

            System.Diagnostics.Debug.Assert (eventParent != null);
            return new Facsimile.Common.Measure <Facsimile.Common.TimeUnit>
            (double.PositiveInfinity);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.TimeDue"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected override Facsimile.Common.Measure
        <Facsimile.Common.TimeUnit> TimeDue (EventQueue eventParent,
        Facsimile.Common.Measure <Facsimile.Common.TimeUnit> eventTime)
        {

/*
Descheduled events have an infinite due time.
*/

            System.Diagnostics.Debug.Assert (eventParent != null);
            return new Facsimile.Common.Measure <Facsimile.Common.TimeUnit>
            (double.PositiveInfinity);
        }
    }
}
