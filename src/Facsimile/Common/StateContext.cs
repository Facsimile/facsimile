/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2006, Michael J Allen BSc.

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the:

    Free Software Foundation, Inc.
    51 Franklin St, Fifth Floor
    Boston, MA  02110-1301
    USA

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

    http://facsimile.sourceforge.net/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

    http://facsimile.sourceforge.net/Documentation/CodingStandards.html
===============================================================================
$Id$

C# source file for the StateContext class, and related elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common {

//=============================================================================
/**
<summary>Generic state change event handler delegate.</summary>

<remarks>Delegated function for handling state change events raised by a <see
cref="StateContext {FinalContext, BaseState}" /> instance.

<para>The state change event is raised and processed after the change to the
new state has completed.  If virtual functions are called when the state has
been changed, the state change event will be raised once they have
completed also.</para></remarks>

<param name="sender">The <typeparamref name="FinalContext" />-derived instance
that raised the state change event.  The context instance's new state can be
obtained via its <see cref="StateContext {FinalContext,
BaseState}.CurrentState" /> property.</param>

<param name="priorState">The <typeparamref name="BaseState" />-derived instance
representing the <paramref name="sender" />'s previous state.</param>

<example>For an example of using this delegate, refer to <see
cref="StateContext {FinalContext, BaseState}" />.</example>

<typeparam name="FinalContext">The <see cref="StateContext {FinalContext,
BaseState}" />-derived type that represents the associated actual state
context class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="State {FinalContext, BaseState}"
/>-derived base class defining the set of available states for the associated
<typeparamref name="FinalContext" /> type; all classes derived from this base
class are suitable state classes for the associated state context
class.</typeparam>

<seealso cref="StateContext {FinalContext, BaseState}" />
<seealso cref="State {FinalContext, BaseState}" />
*/
//=============================================================================

    public delegate void StateChangeHandler <FinalContext, BaseState>
    (FinalContext sender, BaseState priorState) where FinalContext:
    StateContext <FinalContext, BaseState> where BaseState: State
    <FinalContext, BaseState>;

//=============================================================================
/**
<summary>Generic abstract base class for state context objects.</summary>

<remarks>A state context object is one that, at a given point in time, may be
in just one from an available set of states.  Furthermore, the behaviour of
each state context object depends upon its current state to the extent that it
may delegate some of its implementation to that state.

<para>Each state is represented by a class that is derived from <typeparamref
name="BaseState" />.  The set of available states are, therefore, the classes
that are derived from <typeparamref name="BaseState" />.  In this way, the set
of available states may be expanded with a minimum of effort.</para>

<para>This class should be employed as a base class by classes that implement
the State design pattern "Context" role.  Refer to Gamma, et al: "Design
Patterns: Elements of Reusable Object-Oriented Software", Addison-Wesley, for
further information.</para></remarks>

<example>The following C# example demonstrates a "switch" (a state context
object) that has two states: "on" and "off".

<para>The switch is here represented by the <c>Switch</c> class, which is
derived from the <see cref="StateContext {FinalContext, BaseState}" /> class.
<c>SwitchState</c>, which is derived from the <see cref="State {FinalContext,
BaseState}" /> class, is the base class for the <c>OnState</c> and
<c>OffState</c> classes that represent the "on" and "off" states respectively;
these state classes are used to report whether the switch is on or off.</para>

<code>
namespace MyProgram {
    public abstract class SwitchState: Facsimile.Common.State &lt;Switch,
    SwitchState&gt; {
	public abstract bool IsOn {
	    get;
	}
	public abstract string Name {
	    get;
	}
    }

    public sealed class OnState: SwitchState {
	public override bool IsOn {
	    get {
		return true;
	    }
	}
	public override string Name {
	    get {
		return "On";
	    }
	}
    }

    public sealed class OffState: SwitchState {
	public override bool IsOn {
	    get {
		return false;
	    }
	}
	public override string Name {
	    get {
		return "Off";
	    }
	}
    }

    public sealed class Switch: Facsimile.Common.StateContext &lt;Switch,
    SwitchState&gt; {
	private static readonly SwitchState onState;
	private static readonly SwitchState offState;
	static Switch () {
	    onState = new OnState ();
	    offState = new OffState ();
	}
	public Switch () : this (onState) {
	}
	public Switch (BaseState initialState) : base (initialState) {
	}
	public void Activate () {
	    CurrentState = onState;
	}
	public void Deactivate () {
	    CurrentState = offState;
	}
	public bool IsOn {
	    get {
		return CurrentState.IsOn;
	    }
	}
    }

    public static Program {
	public static void Main (string [] args) {
	    Switch switch = new Switch ();
	    switch.StateChanged += SwitchChanged;
	    switch.Deactivate ();
	    switch.Activate ();
	}
	public static void SwitchChanged (Switch sender, SwitchState
	priorState) {
	    System.WriteLine ("Switch is now " + sender.CurrentState.Name);
	}
    }
}
</code>

<para>This example produces the output:</para>

<code>
Switch is now off
Switch is now on
</code></example>

<typeparam name="FinalContext">The <see cref="StateContext {FinalContext,
BaseState}" />-derived type that represents the associated actual state
context class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="State {FinalContext, BaseState}"
/>-derived base class defining the set of available states for the associated
<typeparamref name="FinalContext" /> type; all classes derived from this base
class are suitable state classes for the associated state context
class.</typeparam>

<seealso cref="State {FinalContext, BaseState}" />
*/
//=============================================================================

    public abstract class StateContext <FinalContext, BaseState> where
    FinalContext: StateContext <FinalContext, BaseState> where BaseState:
    State <FinalContext, BaseState> {

/**
<summary>Current state.</summary>
*/

	private BaseState current;

/**
<summary>Currently changing state flag.</summary>

<remarks>This value is true whilst this state context object is in the process
of changing to a new state.  Whilst this flag is set, the state context object
will throw an exception if an attempt is made to change the current state
before the current state change operation has completed.</remarks>
*/

	private bool changingState;

/**
<summary>State change completed event.</summary>

<remarks>Event signalling that this object has successfully completed a state
change operation.

<para>Note that until the state change event handlers have completed, the
current state change operation is regarded as still being in effect.  For this
reason, state change handler logic cannot itself change the state context's
state.</para></remarks>

<example>For an example of handling this event, refer to <see
cref="StateContext {FinalContext, BaseState}" />.</example>

<seealso cref="StateChangeHandler {FinalContext, BaseState}" />
*/

	public event StateChangeHandler <FinalContext, BaseState> StateChanged;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Initial state constructor.</summary>

<remarks>Constructs the new state context object, setting the initial state to
the value indicated.

<para>Note that the state changed event is not raised when setting the state to
its initial value; events are only raised when the state is changed
subsequently.</para></remarks>

<param name="initialState">The <typeparamref name="BaseState" /> instance that
represents this state context's initial state.</param>

<exception cref="System.ArgumentNullException">Thrown if a null reference is
supplied as the initial state of the object.</exception>

<seealso cref="State {FinalContext, BaseState}" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public StateContext (BaseState initialState) {

/*
Verify that we have a valid initial state.
*/

	    if (initialState == null) {
		// TODO: Internationalise this error message.
		throw new System.ArgumentNullException ("initialState",
		"Initial state cannot be null.");
	    }

/*
Store the current initial state.  Note that we do not raise the state changed
event.
*/

	    current = initialState;

/*
Record the fact that we are not currently in the process of changing the state.
*/

	    changingState = false;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Current state.</summary>

<remarks>The <see cref="StateChanged" /> event is raised if the current state
is changed successfully.

<para>It is not possible to change the current state if an existing state
change operating is currently in progress.  If this is attempted, a <see
cref="System.InvalidOperationException" /> exception will be
raised.</para></remarks>

<value>A <typeparamref name="BaseState " /> instance representing this state
context's current state.</value>

<exception cref="System.InvalidOperationException">Thrown if this state context
object is already in the process of changing to a new state (caused by state
change event handler code attempting to implement state changes) or by attempts
to transition to a state that is not permitted by the current
state.</exception>

<exception cref="System.ArgumentNullException">Thrown if an attempt is made to
change the state to a null object.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public BaseState CurrentState {

/*
Return the current state.
*/

	    get {
		return current;
	    }

/*
Attempt to change the current state to the value indicated.

This is protected so states may only be changed from within state context
classes - allowing external code to change the state is not always desirable.
*/

	    protected set {

/*
Verify that the value is valid; if it is not, then we must thrown the argument
null exception.
*/

		if (value == null) {
		    // TODO: Internationalise this error message.
		    throw new System.ArgumentNullException ("value",
		    "Current state cannot be set to a null value.");
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
handlers and the state change event, checks for re-entrancy, etc.</remarks>

<param name="newState">A <typeparamref name="BaseState" /> instance
representing the new state.</param>

<exception cref="System.InvalidOperationException">Thrown if this state context
object is already in the process of changing to a new state (caused by state
change event handler code attempting to implement state changes) or by attempts
to transition to a state that is not permitted by the current
state.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void SetCurrentState (BaseState newState) {

/*
If we're currently changing the state of this object, then we can hardly do so
again until we're done.  Signal this by throwing an exception.
*/

	    if (changingState) {
		// TODO: Internationalise this message!  Derive new exception?
		throw new System.InvalidOperationException
		("Cannot change state because current state change " +
		"operation incomplete.");
	    }

/*
Confirm that we can transition to the new state.  If not, then throw an invalid
operation exception.
*/

	    if (!current.CanTransitionTo (newState)) {
		// TODO: Internationalise this message!  Derive new exception?
		throw new System.InvalidOperationException
		("Cannot transition from current state to new state");
	    }

/*
Change the current state, after recording the prior state for use when raising
the state changed event.
*/

	    BaseState priorState = current;
	    current = newState;

/*
If we made it this far, we must be changing the state, so note the fact.
*/

	    changingState = true;

/*
Raise the state change event (if we have any registered handlers).
*/

	    try {
		if (StateChanged != null) {
		    StateChanged ((FinalContext) this, priorState);
		}
	    }

/*
Regardless of what just happened, we're no longer changing the state of this
object.
*/

	    finally {
		changingState = false;
	    }
	}
    }
}
