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

C# source file for the EventState class, and associated elements, that are
integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Abstract base class for event states.</summary>

<remarks><see cref="IEvent" /> sub-class objects may be in one of a number of
states during the course of their lifespan.  This class defines the base
functionality that these states must provide.

<para>To avoid high memory overhead, and to improve run-time performance, each
event state is represented by a single instance - such that the same event
state may be shared between a number of different events.  Consequently, event
states do not store references to their associated events, nor do they store
event-specific information.</remarks>

<seealso cref="Facsimile.Common.AbstractState {FinalContext, BaseState}" />

<seealso cref="Facsimile.Common.ReportingAbstractState {FinalContext,
BaseState}" />

<seealso cref="IEvent" />
*/
//=============================================================================

    public abstract class EventState:
        Facsimile.Common.ReportingAbstractState <IEvent, EventState>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>This constructor is marked "internal" to prevent instantiation from
outside this assembly.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal EventState ():
            base ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.TimeRemaining"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual Facsimile.Common.Measure
        <Facsimile.Common.TimeUnit> TimeRemaining (EventQueue eventParent,
        Facsimile.Common.Measure <Facsimile.Common.TimeUnit> eventTime)
        {

/*
This default version assumes that the time field stores the due time of the
event relative to its parent's clock - which is the most common use of this
field.  Consequently, we report the difference between this due time and the
parent's current clock value.  (However, note that two of the states that use
the time field in this manner need to report 0.0 as the time remaining.)
*/

            System.Diagnostics.Debug.Assert (eventParent != null);
            return eventTime - eventParent.RelativeClock;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.TimeDue"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual Facsimile.Common.Measure
        <Facsimile.Common.TimeUnit> TimeDue (EventQueue eventParent,
        Facsimile.Common.Measure <Facsimile.Common.TimeUnit> eventTime)
        {

/*
This default version assumes that the time field stores the due time of the
event relative to its parent's clock - which is the most common use of this
field.  Consequently, we simply report the due time.
*/

            System.Diagnostics.Debug.Assert (eventParent != null);
            return eventTime;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.IsActive"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual bool IsActive
        {

/*
The default is that this is not the active state - which is true for all but
one of the states.
*/

            get
            {
                return false;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.IsScheduled"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual bool IsSuspended
        {

/*
The default is that this is not the suspended state - which is true for all but
one of the states.
*/

            get
            {
                return false;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.Suspend"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual bool Suspend (Facsimile.Common.Counter
        suspendCount)
        {

/*
The majority of states cannot be suspended, so the default version of this
method is to throw the exception.
*/

            System.Diagnostics.Debug.Assert (suspendCount != null);
            throw new EventNotSuspendableException (this);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="EventState.doc.xml"
path='commonDoc/member[@name="EventState.Resume"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal protected virtual bool Resume (Facsimile.Common.Counter
        suspendCount)
        {

/*
The majority of states cannot be resumed, so the default version of this method
is to throw the exception.
*/

            System.Diagnostics.Debug.Assert (suspendCount != null);
            throw new EventNotResumableException (this);
        }
    }
}
