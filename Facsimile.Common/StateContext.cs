/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

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

C# source file for the StateContext class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Generic state change event handler delegate.</summary>

<remarks>Delegated function for handling state change events raised by a
<typeparamref name="FinalContext" /> instance.

<para>The state change event is raised and processed as part of a state change
operation on the <typeparamref name="FinalContext" /> object.</para>

<para>For more detailed information on the order in which events and associated
virtual functions are called, refer to <see cref="StateContext {FinalContext,
BaseState}" />.</para></remarks>

<param name="sender">The <typeparamref name="FinalContext" />-derived instance
that raised the state change event.  The context instance's new state can be
obtained via its <see cref="StateContext {FinalContext,
BaseState}.CurrentState" /> property.</param>

<param name="priorState">The <typeparamref name="BaseState" />-derived instance
representing the <paramref name="sender" />'s previous state.</param>

<example>For an example of using this delegate, refer to <see
cref="StateContext {FinalContext, BaseState}" />.</example>

<typeparam name="FinalContext">The <see cref="StateContext {FinalContext,
BaseState}" />-derived type that represents the associated actual state context
class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="AbstractState {FinalContext,
BaseState}" />-derived base class defining the set of available states for the
associated <typeparamref name="FinalContext" /> type; all classes derived from
this base class are suitable state classes for the associated state context
class.</typeparam>

<seealso cref="StateContext {FinalContext, BaseState}" />

<seealso cref="AbstractState {FinalContext, BaseState}" />

<seealso cref="StateEnterHandler {FinalContext, BaseState}" />

<seealso cref="StateExitHandler {FinalContext, BaseState}" />
*/
//=============================================================================

    public delegate void StateChangedHandler <FinalContext, BaseState>
    (FinalContext sender, BaseState priorState)
    where FinalContext:
	StateContext <FinalContext, BaseState>
    where BaseState:
	AbstractState <FinalContext, BaseState>
    ;

//=============================================================================
/**
<summary>Generic abstract base class for state context objects.</summary>

<remarks>A state context object is one that, at a given point in time, may be
in just one from an available set of states.  Furthermore, the behavior of each
state context object depends upon its current state to the extent that it may
delegate some of its behavior to that state.

<para>Each state is represented by a class that is derived from <typeparamref
name="BaseState" />.  The set of available states are, therefore, the classes
that are derived from <typeparamref name="BaseState" />.  In this way, the set
of available states may be expanded with a minimum of effort.</para>

<para>State change operations involve a number of different steps, which
proceed in the following sequence:</para>

<list type="number">
    <item>
        <description>A state change operation is attempted upon a <typeparamref
        name="StateContext" /> instance.</description>
    </item>
    <item>
        <description>If this instance is currently in the process of changing
        state, or if a transition to the new state is forbidden, then an
        appropriate exception is raised, the state change is cancelled and the
        following steps do not apply.  Otherwise, if conditions are suitable
        for the state change operation to take place, then this is implemented
        in the sequence that follows.</description>
    </item>
    <item>
        <description>The previous state's <see cref="AbstractState
        {FinalContext, BaseState}.OnExit (FinalContext)" /> virtual function is
        called.</description>
    </item>
    <item>
        <description>The previous state's <see cref="AbstractState
        {FinalContext, BaseState}.Exit" /> event is raised.</description>
    </item>
    <item>
        <description>The <typeparamref name="FinalContext" /> instance's <see
        cref="CurrentState" /> is changed from the previous state to the new
        state.</description>
    </item>
    <item>
        <description>The new state's <see cref="AbstractState {FinalContext,
        BaseState}.OnEnter (FinalContext)" /> virtual function is
        called.</description>
    </item>
    <item>
        <description>The new state's <see cref="AbstractState {FinalContext,
        BaseState}.Enter" /> event is raised.</description>
    </item>
    <item>
        <description>The <typeparamref name="FinalContext" /> instance's <see
        cref="OnStateChanged (BaseState)" /> virtual function is
        called.</description>
    </item>
    <item>
        <description>The <typeparamref name="FinalContext" /> instance's <see
        cref="StateChanged" /> event is raised.</description>
    </item>
</list>

<para>This class should be employed as a base class by classes that implement
the State design pattern's "Context" role.  Refer to Gamma, et al: "Design
Patterns: Elements of Reusable Object-Oriented Software", Addison-Wesley, for
further information.</para></remarks>

<example>The following C# example demonstrates a "switch" (a state context
object) that has two states: "on" and "off".

<para>The switch is here represented by the <c>Switch</c> class, which is
derived from the <see cref="StateContext {FinalContext, BaseState}" /> class.
<c>SwitchState</c>, which is derived from the <see cref="AbstractState
{FinalContext, BaseState}" /> class, is the base class for the <c>OnState</c>
and <c>OffState</c> classes that represent the "on" and "off" states
respectively; these state classes are used to report whether the switch is on
or off.</para>

<code>
namespace MyProgram
{
    public abstract class SwitchState:
	Facsimile.Common.AbstractState &lt;Switch, SwitchState&gt;
    {
	public abstract bool IsOn
	{
	    get;
	}
	public abstract string Name
	{
	    get;
	}
    }

    public sealed class OnState:
	SwitchState
    {
	public override bool IsOn
	{
	    get
	    {
		return true;
	    }
	}
	public override string Name
	{
	    get
	    {
		return "On";
	    }
	}
    }

    public sealed class OffState:
	SwitchState
    {
	public override bool IsOn
	{
	    get
	    {
		return false;
	    }
	}
	public override string Name
	{
	    get
	    {
		return "Off";
	    }
	}
    }

    public sealed class Switch:
	Facsimile.Common.StateContext &lt;Switch, SwitchState&gt;
    {
	private static readonly SwitchState onState;
	private static readonly SwitchState offState;
	static Switch ()
	{
	    onState = new OnState ();
	    offState = new OffState ();
	}
	public Switch ():
	    base (onState)
	{
	}
	public void Activate ()
	{
	    CurrentState = onState;
	}
	public void Deactivate ()
	{
	    CurrentState = offState;
	}
	public bool IsOn
	{
	    get
	    {
		return CurrentState.IsOn;
	    }
	}
    }

    public static Program
    {
	public static void Main (string [] args)
	{
	    Switch switch = new Switch ();
	    switch.StateChanged += SwitchChanged;
	    switch.Deactivate ();
	    switch.Activate ();
	}
	public static void SwitchChanged (Switch sender, SwitchState
	priorState)
	{
	    System.WriteLine ("Switch is now " + sender.CurrentState.Name);
	}
    }
}
</code>

<para>This example produces the output:</para>

<code>
Switch is now off
Switch is now on
</code>
</example>

<typeparam name="FinalContext">The <see cref="StateContext {FinalContext,
BaseState}" />-derived type that represents the associated actual state context
class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="AbstractState {FinalContext,
BaseState}" />-derived base class defining the set of available states for the
associated <typeparamref name="FinalContext" /> type; all classes derived from
this base class are suitable state classes for the associated state context
class.</typeparam>

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="AbstractState {FinalContext, BaseState}" />
*/
//=============================================================================

    public abstract class StateContext <FinalContext, BaseState>:
        System.Object
    where FinalContext:
	StateContext <FinalContext, BaseState>
    where BaseState:
	AbstractState <FinalContext, BaseState>
    {

/**
<summary>Current state.</summary>
*/

	private BaseState currentState;

/**
<summary>Currently changing state flag.</summary>

<remarks>This value is true whilst this state context object is in the process
of changing to a new state.  Whilst this flag is set, the state context object
will throw an exception if an attempt is made to change the current state
before the current state change operation has completed.</remarks>
*/

	private bool changingState;

/**
<summary>State changed event.</summary>

<remarks>Event signalling that this object has successfully completed a state
change operation.

<para>If your logic needs to determine when a change to a specific state has
occurred, rather than a change to any state, then you are recommended to
subscribe to the event state's <see cref="AbstractState {FinalContext,
BaseState}.Enter" /> and/or <see cref="AbstractState {FinalContext,
BaseState}.Exit" /> events.</para>

<para>Note that until the state change event handlers have completed, the
current state change operation is regarded as still being in effect (although
the state context's object's state will have been changed).  For this reason,
state change handler logic cannot itself change the state context's
state.</para></remarks>

<example>For an example of handling this event, refer to <see
cref="StateContext {FinalContext, BaseState}" />.</example>

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="AbstractState {FinalContext, BaseState}.Enter" />

<seealso cref="AbstractState {FinalContext, BaseState}.Exit" />
*/

	public event StateChangedHandler <FinalContext, BaseState>
        StateChanged;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Initial state constructor.</summary>

<remarks>Constructs the new state context object, setting the initial state to
the value indicated.

<para>Note that the <see cref="StateChanged" /> event is not raised when
initializing this context's state; state changed events are only raised when
the state is changed subsequently.  However, the <typeparamref name="BaseState"
/>'s <see cref="AbstractState {FinalContext, BaseState}.Enter" /> event is
raised here.</para></remarks>

<param name="initialState">The <typeparamref name="BaseState" />-derived
instance that represents this state context's initial state.</param>

<exception cref="System.ArgumentNullException">Thrown if a null reference is
supplied as the initial state of the object.</exception>

<seealso cref="AbstractState {FinalContext, BaseState}" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public StateContext (BaseState initialState)
	{

/*
Verify that we have a valid initial state.
*/

            if (initialState == null)
            {
                throw new System.ArgumentNullException ("initialState");
            }

/*
Store the current initial state.  Note that we do not raise the state changed
event, but we do raise the state's Enter event.
*/

	    currentState = initialState;
            initialState.RaiseEnter ((FinalContext) this);

/*
Record the fact that we are not currently in the process of changing the state.
*/

	    changingState = false;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Current state property.</summary>

<remarks>The previous event's <see cref="AbstractState {FinalContext,
BaseState}.Exit" /> event, then new event's <see cref="AbstractState
{FinalContext, BaseState}.Enter" /> event and this instance's <see
cref="StateChanged" /> events are raised if the current state is changed
successfully.  For the exact sequence in which these events are raised, refer
to <see cref="StateContext {FinalContext, BaseState}" />.

<para>It is not possible to change the current state if an existing state
change operation is currently in progress.  If this is attempted, a <see
cref="System.InvalidOperationException" /> exception will be
raised.</para></remarks>

<value>A <typeparamref name="BaseState" />-derived instance representing this
state context's current state.</value>

<exception cref="System.InvalidOperationException">Thrown if this state context
object is already in the process of changing to a new state (caused by state
change event handler code attempting to implement state changes) or by attempts
to transition to a state that is not permitted by the current
state.</exception>

<exception cref="System.ArgumentNullException">Thrown if an attempt is made to
change the state to a null object.</exception>

<seealso cref="StateContext {FinalContext, BaseState}" />
<seealso cref="StateChanged" />
<seealso cref="AbstractState {FinalContext, BaseState}.Enter" />
<seealso cref="AbstractState {FinalContext, BaseState}.Exit" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public BaseState CurrentState
	{

/*
Return the current state.
*/

	    get
	    {
                System.Diagnostics.Debug.Assert (currentState != null);
		return currentState;
	    }

/*
Attempt to change the current state to the value indicated.

This is protected so states may only be changed from within state context
classes - allowing external code to change the state is not always desirable.
*/

	    protected set
	    {

/*
Verify that the value is valid; if it is not, then we must thrown the argument
null exception.
*/

                if (value == null) {
                    throw new System.ArgumentNullException ("value");
                }

/*
Set the current state.
*/

		SetCurrentState (value);
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Change the current state.</summary>

<remarks>Implements the change of the current state, invokes state change
handlers and the state change event, checks for recursion, etc.

<para>Note that a state cannot transition to itself (or to another instance of
the same state).</para></remarks>

<param name="newState">A <typeparamref name="BaseState" />-derived instance
representing the new state.</param>

<exception cref="OperationIncompleteException">Thrown if this state context
object is already in the process of changing to a new state (caused by state
change event handler code attempting to implement state changes).</exception>

<exception cref="OperationForbiddenException">Thrown if an attempt is made to
transition to an invalid state.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void SetCurrentState (BaseState newState)
	{

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (newState != null);
            System.Diagnostics.Debug.Assert (currentState != null);

/*
If we're currently changing the state of this object, then we can hardly do so
again until we're done.  Signal this by throwing an exception.
*/

	    if (changingState)
	    {
		throw new StateChangeInProgressException <FinalContext,
                BaseState> ((FinalContext) this, newState);
	    }

/*
Confirm that we can transition to the new state.  If not, then throw an invalid
operation exception.

Note that we cannot transition to the same state that we're already in.
*/

	    if (currentState.GetType () == newState.GetType () ||
            !currentState.CanTransitionTo (newState))
	    {
		throw new InvalidStateTransitionException <FinalContext,
                BaseState> ((FinalContext) this, newState);
	    }

/*
If we made it this far, we must be changing the state, so note the fact.
*/

	    changingState = true;

// TODO: Make the subsequent code exception safe.  (The nature of event
// delegatess means that it would be difficult to "unwind" back to the original
// conditions after an exception.)

/*
Record the previous state (we're about to change it, but need it when raising
the state changed event), and raise this state's exit event.
*/

	    BaseState priorState = currentState;
            priorState.RaiseExit ((FinalContext) this);

/*
OK.  Now change the current state to the new state and raise the new state's
enter event.
*/

            currentState = newState;
            currentState.RaiseEnter ((FinalContext) this);

/*
Allow derived classes to handle this event.
*/

            OnStateChanged (priorState);

/*
If there are any subscribers to the state changed event, inform them what has
just happened.
*/

            if (StateChanged != null)
            {
                StateChanged ((FinalContext) this, priorState);
            }

/*
OK.  We are no longer changing the state of this object.
*/

            changingState = false;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State changed event handler.</summary>

<remarks>Overriding this method allows derived classes to handle the state
changed event without needing to attach a delegate to the <see
cref="StateChanged" /> event.  This is the preferred mechanism for derived
classes.

<para>Override to perform derived-class specific state change event handling;
this default version does nothing.  When overriding, be sure to call the base
class's version to avoid loss of functionality.</para>

<para>This method should not pass unhandled expceptions back to the
caller.</para></remarks>

<param name="priorState">The <typeparamref name="BaseState" />-derived instance
that was our previous state.  The current state can be obtained through <see
cref="CurrentState" />.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual void OnStateChanged (BaseState priorState)
        {
        }
    }
}
