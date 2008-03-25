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

C# source file for the EventSuspendedState class, and associated elements, that
are integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Event suspended state class.</summary>

<remarks>The <see cref="EventState" /> sub-class that is used as the state for
suspended events; that is, for events that are suspended from executing in
their parent event queue.

<para>A single instance of this class is used to represent the suspended state
of all events.</para></remarks>

<seealso cref="Facsimile.Common.EventState {FinalContext, BaseState}" />

<seealso cref="IEvent" />
*/
//=============================================================================

    public sealed class EventSuspendedState:
        EventState
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>This constructor is marked "internal" to prevent instantiation from
outside this assembly.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal EventSuspendedState ():
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
Suspended events use the "time" field to store the time remaining, so we'll
just report this value back.
*/

            System.Diagnostics.Debug.Assert (eventParent != null);
            return eventTime;
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
Suspended events have an infinite due time.
*/

            System.Diagnostics.Debug.Assert (eventParent != null);
            return new Facsimile.Common.Measure <Facsimile.Common.TimeUnit>
            (double.PositiveInfinity);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.IsSuspended"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected override bool IsSuspended
        {

/*
Er, this state is the suspended state, so I guess we should say so!
*/

            get
            {
                return true;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.Suspend"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected override bool Suspend (Facsimile.Common.Counter
        suspendCount)
        {

/*
Sanity checks.  We ought to have a counter that is not currently empty
(otherwise, we cannot be suspended).
*/

            System.Diagnostics.Debug.Assert (suspendCount != null);
            System.Diagnostics.Debug.Assert (!suspendCount.IsEmpty);

/*
Increment the count and return false, indicating that we are already suspended,
and do not need to transition to this state.
*/

            suspendCount.Increment ();
            return false;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.Resume"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected override bool Resume (Facsimile.Common.Counter
        suspendCount)
        {

/*
Sanity checks.  We ought to have a counter that is not currently empty
(otherwise, we cannot be suspended).
*/

            System.Diagnostics.Debug.Assert (suspendCount != null);
            System.Diagnostics.Debug.Assert (!suspendCount.IsEmpty);

/*
Decrement the count and return the empty status of the count; if the count is
empty, we will transition back to the active state - otherwise, we remain in
the suspended state.
*/

            suspendCount.Decrement ();
            return suspendCount.IsEmpty;
        }
    }
}
