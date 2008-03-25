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

C# source file for the Event class, and associated elements, that are integral
members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Abstract simulation event base class.</summary>

<remarks>Abstract base class representing all simulation events.  To use this
class, you should derive a sub-class that implements the <see cref="OnDispatch
()" /> method.

<para>In the Facsimile library, actual (rather than proxy) events are
represented by sub-class instances of the <see cref="Event" /> class.</para>

<para>Events fulfill the State design pattern's "Context" role.  (Refer to
Gamma, et al: "Design Patterns: Elements of Reusable Object-Oriented Software",
Addison-Wesley, for further information.)  Consequently, much of each event's
behavior is delegated to its current state (represented by a <see
cref="EventState" /> sub-class instance.</para>

<para>User code may associate handlers for the event's state changes, and for
the event's states' state changes.  These handlers are guaranteed to remain in
effect from event creation through dispatching of the event.</para></remarks>

<seealso cref="IEvent" />

<seealso cref="Facsimile.Common.StateContext {FinalContext, BaseState}" />

<seealso cref="EventQueue" />

<seealso cref="EventState" />
*/
//=============================================================================

    public class Event:
        Facsimile.Common.StateContext <Event, EventState>, IEvent
    {

/**
<summary>Event active state.</summary>

<remarks>State representing active events; that is, events that are scheduled
in their owning <see cref="EventQueue" />.

<para>Note: An active event is not necessarily a scheduled event.</para>

<para>This value cannot be null.</para></remarks>

<seealso cref="IsActive" />

<seealso cref="IsScheduled" />
*/

        private static readonly EventActiveState activeState;

/**
<summary>Event suspended state.</summary>

<remarks>State representing suspended events; that is, events that have been
suspended from execution.

<para>This value cannot be null.</para></remarks>

<seealso cref="IsSuspended" />
*/

        private static readonly EventSuspendedState suspendedState;

/**
<summary>Event descheduled state.</summary>

<remarks>State representing descheduled events; that is, events that have been
removed from their <see cref="EventQueue" /> and awaiting something new to
occur.

<para>Note: This state is primarily intended for proxy events that have no
proxied event available.  Consequently, this state provides a means to avoid
the proxy from having the Suspended state.</para>

<para>This value cannot be null.</para></remarks>
*/

        private static readonly EventDescheduledState descheduledState;

/**
<summary>Event dispatching state.</summary>

<remarks>State representing dispatching events; that is, events that are
currently in the process of being dispatched.

<para>Only a single event can be dispatched at a time, and consequently, at
most one event at any given instant can be in this state during the entire
course of program execution.  The event with this state is termed the "current
event".</para>

<para>This value cannot be null.</para></remarks>
*/

        private static readonly EventDispatchingState dispatchingState;

/**
<summary>Event dispatched state.</summary>

<remarks>State representing dispatched events; that is, events that have been
dispatched and that will play no further part in the simulation.

<para>References to dispatched events should be cleared so that resources
held by such events can be reclaimed by the garbage collector.</para>

<para>This value cannot be null.</para></remarks>
*/

        private static readonly EventDispatchedState dispatchedState;

/**
<summary>This event's owning event queue.</summary>

<remarks>This value cannot be modified once assigned.</remarks>

<seealso cref="Owner" />
*/

        private readonly EventQueue owner;

/**
<summary>Associated time value.</summary>

<remarks>The interpretation of this value depends upon the event's current
state:

<list type="table">
    <listheader>
        <term>State</term>
        <description>Use of "time" field</description>
    </listheader>
    <item>
        <term>Active</term>
        <description>Time due (relative to parent's clock.</description>
    </item>
    <item>
        <term>Suspended</term>
        <description>Time remaining once resumed.</description>
    </item>
    <item>
        <term>Descheduled</term>
        <description>Not applicable to a <see cref="Event" /> sub-class
        instance - "time" has, er, no meaning.</description>
    </item>
    <item>
        <term>Dispatching</term>
        <description>Time due (relative to parent's clock.</description>
    </item>
    <item>
        <term>Dispatched</term>
        <description>Time due (relative to parent's clock.</description>
    </item>
</list>

<para>This field is maintained by the <see cref="EventState</para></remarks>
*/

        private Facsimile.Common.Measure <Facsimile.Common.TimeUnit> time;

/**
<summary>This event's priority.</summary>

<remarks>This value cannot be modified once assigned.</remarks>

<seealso cref="Priority" />
*/

        private readonly int priority;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialize static data members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Event ()
        {

/*
Single active state instance.
*/

            activeState = new EventActiveState ();
            System.Diagnostics.Debug.Assert (activeState);

/*
Single suspended state instance.
*/

            suspendedState = new EventSuspendedState ();
            System.Diagnostics.Debug.Assert (suspendedState);

/*
Single descheduled state instance.
*/

            descheduledState = new EventDescheduledState ();
            System.Diagnostics.Debug.Assert (descheduledState);

/*
Single dispatching state instance.
*/


            dispatchingState = new EventDispatchingState ();
            System.Diagnostics.Debug.Assert (dispatchingState);

/*
Single dispatched state instance.
*/

            dispatchedState = new EventDispatchedState ();
            System.Diagnostics.Debug.Assert (dispatchedState);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor.</summary>

<remarks>Creates a new event instance and schedules it to be executed.

<para>The event will be scheduled on the specified event queue, and is
initially active (though not necessarily scheduled - see <see
cref="IsScheduled" />).</para></remarks>

<param name="eventQueue">A reference to the <see cref="EventQueue" /> that will
be used to store the event.  This cannot be null or an exception will be
thrown.</param>

<param name="eventPriority">The relative priority of the event, stored as a
<see cref="System.Int32" />.  The higher the value, the higher the priority
(refer to <see cref="Priority" /> for further information).  Events that are
scheduled at the same time are dispatched in order of their priorities, with
the highest priority event being dispatched first.</param>

<param name="timeToEvent">The time that must elapse on the <paramref
name="eventQueue" /> before the event is dispatched.  This value cannot be
negative.</param>

<exception cref="System.ArgumentNullException">Thrown if <paramref
name="eventQueue" /> is null.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Event (EventQueue eventQueue, int eventPriority,
        Facsimile.Common.Measure <Facsimile.Common.TimeUnit> timeToEvent):
            base (activeState)
        {

/*
Store the owner.  (This cannot be null - but we do not explicitly check this.
If it is null, a null reference exception will result when we try to use it to
schedule the event later on.)
*/

            owner = eventQueue;

/*
Store the priority.
*/

            priority = eventPriority;

/*
In the active state, the "time" member is the due time of the event, relative
to the owning event queue.  The active state can calculate this for us.  (When
we subsequently change state, this field will be updated by the new state.)
*/
UP TO HERE
            System.Diagnostics.Debug.Assert (CurrentState == activeState);
            time = EventState.RelativeToAbsoluteTime (owner, timeToEvent);

/*
Schedule the event.  If a null reference exception occurs, then owner (and,
hence, the eventQueue argument) must be null, so we must throw a null argument
exception.

It is more efficient to just use the owner reference as is, and catch the
resuling null reference exception should it be a null value, rather than to
explicitly check that it is not null, since valid code will execute faster.
(Invalid code will execute way slower, but we do not care about the performance
of invalid code.)

We throw an argument null exception instead of passing the original exception,
because the former is the right type of exception if an argument is null while
the latter is a result of the implementation.
*/

            try
            {
                owner.Schedule (this);
            }
            catch (System.NullReferenceException)
            {
                System.Diagnostics.Debug.Assert (owner == null);
                throw System.ArgumentNullException ("eventQueue");
            }
            System.Diagnostics.Debug.Assert (owner != null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.Priority"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int Priority
        {

/*
Trivially report the event's current priority.
*/

            get
            {
                return priority;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.RelativeTimeRemaining"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Facsimile.Common.Measure <Facsimile.Common.TimeUnit>
        RelativeTimeRemaining
        {

/*
The current state maintains the time remaining.  Have the state report this.
*/

            get
            {
                return CurrentState.TimeRemaining (this, time);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.RelativeDueTime"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Facsimile.Common.Measure <Facsimile.Common.TimeUnit>
        RelativeDueTime
        {

/*
The current state maintains the due time.  Have the state report this time.
*/

            get
            {
                return CurrentState.TimeDue (this, time);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml" path='commonDoc/member[@name="IEvent.Owner"]/*'
/>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public EventQueue Owner
        {

/*
Trivially report the event's owning event queue.
*/

            get
            {
                return owner;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.IsActive"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsActive
        {

/*
The current state maintains this status.
*/

            get
            {
                return CurrentState.IsActive;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.IsScheduled"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsScheduled
        {

/*
We're scheduled if this event is active, and our parent is scheduled.
*/

            get
            {
                return IsActive && owner.IsScheduled;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Property identifying whether event is suspended.</summary>

<remarks>This property should be true if the event's suspend count (the
difference between the number of successful calls to <see cref="Suspend ()" />
and <See cref="Resume ()" /> is greater than zero, or false if the suspend
count is zero.

<para>This property should not throw any exceptions.</para></remarks>

<value>A <see cref="System.Boolean" /> that is true if the object is currently
suspended and false otherwise.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsSuspended
        {

/*
This is handled by our current state.
*/

            get
            {
                return CurrentState.IsSuspended;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<include file="IEvent.doc.xml"
path='commonDoc/member[@name="IEvent.ActualEvent"]/*' />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Event ActualEvent
        {

/*
Event sub-classes merely report themselves.
*/

            get
            {
                return this;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Increment event's suspend count.</summary>

<remarks>This function increments the event's suspend count.  If the event is
active and its suspend count is current zero, then calling this function
results in the event's state changing from active to suspended; for suspend
counts higher than zero, the object remains suspended following the call to
this function.

<para>An exception may be thrown if the event is currently in a state that
does not support it becoming suspended.</para>

<para>To resume a suspended event, one call to <see cref="Resume ()" /> must
be made for each corresponding call to this function.</para></remarks>

<exception cref="System.InvalidOperationException">Thrown if the object is
currently in a state that does not support suspension of the
object.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void Suspend ()
        {

/*
This is handled by our current state.
*/

            CurrentState.Suspend ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Decrement event's suspend count.</summary>

<remarks>This function decrements the event's suspend count.  If the event is
active and its suspend count is currently zero, then calling this function
results in an exception; the suspend count can never be less than zero.  If the
event is currently suspended and the suspend count is one, then calling this
function results in the event's state changing from suspended to active; for
suspend counts higher than 1, the event remains suspended following the call
to this function.

<para>When an event is resumed, and changes its status back to active from
suspended, the event is re-scheduled in the owner's event queue.</para>

<para>An exception may be thrown if the object is currently in a state that
does not support suspension of the object.</para>

<para>To resume a suspended object, one call to <see cref="Resume ()" /> must
be made for each corresponding call to this function.</para></remarks>

<exception cref="System.InvalidOperationException">Thrown if the event is not
currently suspended.</exception
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void Resume ()
        {

/*
This is handled by our current state.
*/

            CurrentState.Resume ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Compare to another event.</summary>

<remarks>This method is primarily used to order events in their <see
cref="EventQueue" /> so that they can be dispatched in the correct order.  It
should not be used for any other purpose.

<para>Events are compared in terms of their elapsed time due and priorities
only.  Events that are inactive (see <see cref="IsActive" />) or
that are on different event queues are not strictly comparable, and so this
function should not be used in user code as a predictor of the likely event
dispatch sequence.  In any case, the method provides only a snapshot of likely
event execution since either event involved can be subsequently
suspended.</para></remarks>

<param name="other">The event instance being compared to this event.</param>

<returns>A 32-bit signed integer that indicates the relative order of the
Events being compared. The return value has the following meanings:

<list type="table">
    <listheader>
        <term>Value</term>
        <description>Meaning</description>
    </listheader>
    <item>
        <term>Less than zero</term>
        <description>This event is "less than" the <paramref name="other"
        event.  That is, it will be dispatched before the other
        event.</description>
    </item>
    <item>
        <term>Zero</term>
        <description>This event is "equal to" the <paramref name="other" event.
        That is, the two events have the same time due and
        priority; they will be dispatched in first-come, first-served
        order.</description>
    </item>
    <item>
        <term>Greater than zero</term>
        <description>This event is "greater than" the <paramref name="other" />
        event.  That is, it will be dispatched after the other
        event.  Note that this is also the result if <paramref name="other" />
        is null (this is required for full implementation of the <see
        cref="System.IComparable {T}.CompareTo (T)" /> function.</description>
    </item>
</list></returns>

<seealso cref="IEvent" />

<seealso cref="System.IComparable {T}.CompareTo (T)" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int CompareTo (IEvent other)
        {

/*
Try to compare the time remaining and store the result.
*/

            int compareTimes;
            try
            {
                compareTimes = ElapsedTimeDue.CompareTo (other.ElapsedTimeDue);
            }

/*
If we get a null reference exception, then "other" is null.  To fulfill the
terms of the System.IComparable <T>.CompareTo (T) specification, we must return
a positive value in this case.

This is a rather inefficient piece of code - but only if "other" is null, which
should occur rarely in practice.  
*/

            catch (System.NullReferenceException)
            {
                System.Diagnostics.Debug.Assert (other == null);
                return 1;
            }

/*
OK.  If the times do not compare equal, then return the result of the
comparison.
*/

            if (compareTimes != 0)
            {
                System.Diagnostics.Debug.Assert (other != null);
                return compareTimes;
            }

/*
Otherwise, the two time dues must be equal, so we'll return the comparison of
the priorities as the tie breaker.
*/

            return Priority.CompareTo (other.Priority);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Change event status.</summary>

<remarks>This method, which it is intended be called only locally or from
within the <see cref="EventState" /> class (alas, C# has no concept like C++'s
"friends", so we cannot enforce this), allows the state of the event to be
changed.

<para>If an attempt is made to change the state of an event before it has
finished a previous state change operation, then an exception will be thrown.
Similarly, if an attempt is made to transition to a state that is forbidden by
the current state, then an exception will arise from that also.  In either
case, the state will not be changed.</para></remarks>

<param name="newState">The <see cref="EventState" /> sub-class instance that
will become the event's new state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal void ChangeState (EventState newState)
        {

/*
Trivially invoke the CurrentState's set method.
*/

            System.Diagnostics.Debug.Assert (newState != null);
            CurrentState = newState;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Event's active state.</summary>

<remarks>Reports the event's active state reference.</remarks>

<value>The <see cref="EventActiveState" /> instance representing this event's
active state.  This value cannot be null.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public EventActiveState ActiveState
        {

/*
Trivially report the event's active state.
*/

            get
            {
                System.Diagnostics.Debug.Assert (activeState != null);
                return activeState;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Event's suspended state.</summary>

<remarks>Reports the event's suspended state reference.</remarks>

<value>The <see cref="EventSuspendedState" /> instance representing this
event's suspended state.  This value cannot be null.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public EventSuspendedState SuspendedState
        {

/*
Report the suspended state instance.
*/

            get
            {

/*
If we do not yet have a suspended state instance, then create one.
*/

                if (suspendedState == null)
                {
                    suspendedState = new EventSuspendedState (this);
                }
                System.Diagnostics.Debug.Assert (suspendedState != null);
                return suspendedState;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Event's descheduled state.</summary>

<remarks>Reports the event's descheduled state reference.</remarks>

<value>The <see cref="EventDescheduledState" /> instance representing this
event's descheduled state.  This value cannot be null.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public EventDescheduledState DescheduledState
        {

/*
Report the descheduled state instance.
*/

            get
            {

/*
If we do not yet have a descheduled state instance, then create one.
*/

                if (descheduledState == null)
                {
                    descheduledState = new EventDescheduledState (this);
                }
                System.Diagnostics.Debug.Assert (descheduledState != null);
                return descheduledState;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Event's dispatching state.</summary>

<remarks>Reports the event's dispatching state reference.</remarks>

<value>The <see cref="EventDispatchingState" /> instance representing this
event's dispatching state.  This value cannot be null.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public EventDispatchingState DispatchingState
        {

/*
Report the dispatching state instance.
*/

            get
            {

/*
If we do not yet have a dispatching state instance, then create one.
*/

                if (dispatchingState == null)
                {
                    dispatchingState = new EventDispatchingState (this);
                }
                System.Diagnostics.Debug.Assert (dispatchingState != null);
                return dispatchingState;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Event's dispatched state.</summary>

<remarks>Reports the event's dispatched state reference.</remarks>

<value>The <see cref="EventDispatchedState" /> instance representing this
event's dispatched state.  This value cannot be null.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public EventDispatchedState DispatchedState
        {

/*
Report the dispatched state instance.
*/

            get
            {

/*
If we do not yet have a dispatched state instance, then create one.
*/

                if (dispatchedState == null)
                {
                    dispatchedState = new EventDispatchedState (this);
                }
                System.Diagnostics.Debug.Assert (dispatchedState != null);
                return dispatchedState;
            }
        }
    }
}
