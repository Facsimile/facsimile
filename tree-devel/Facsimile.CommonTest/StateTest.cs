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

C# source file for the StateTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>A custom state context object for testing purposes.</summary>

<remarks>This class doesn't implement too many aspects of the State design
pattern's "state context" role - it does not call any virtual functions on it's
current state instance - but it does put the state context/abstract state
framework classes through their paces.</remarks>
*/
//=============================================================================

    public sealed class SomeStateContext:
        StateContext <SomeStateContext, SomeBaseState>
    {

/**
<summary>The previous state for this context.</summary>

<remarks>This needs to be initialized locally as the constructor is used to
verify the initial state conditions.</remarks>
*/

        private SomeBaseState previousState = null;

/**
<summary>Sequence count.</summary>

<remarks>This value is used to verify that each virtual function and event
delegate is called in the correct sequence when changing the state.

<para>The intial value is set so that the sequence is correct for when the
initial state's enter event is raised.  The initial state is a special
condition because we have no prior state (and so no state exit event is raised)
and we do not change the state (and so no state changed event is raised).
However, the state enter event for the initial state does need to be
raised.</para>

<para>This needs to be initialized locally as the constructor is used to verify
the initial state conditions.</para></remarks>
*/

        private int sequenceCount = 2;

/**
<summary>Flag indicating whether OnStateExit virtual function called.</summary>

<remarks>This needs to be initialized locally as the constructor is used to
verify the initial state conditions.</remarks>
*/

        private bool onStateExitCalled = false;

/**
<summary>Flag indicating whether StateExit delegate function called.</summary>

<remarks>This needs to be initialized locally as the constructor is used to
verify the initial state conditions.</remarks>
*/

        private bool stateExitCalled = false;

/**
<summary>Flag indicating whether OnStateEnter virtual function
called.</summary>

<remarks>This needs to be initialized locally as the constructor is used to
verify the initial state conditions.</remarks>
*/

        private bool onStateEnterCalled = false;

/**
<summary>Flag indicating whether StateEnter delegate function called.</summary>

<remarks>This needs to be initialized locally as the constructor is used to
verify the initial state conditions.</remarks>
*/

        private bool stateEnterCalled = false;

/**
<summary>Flag indicating whether OnStateChanged virtual function
called.</summary>

<remarks>This needs to be initialized locally as the constructor is used to
verify the initial state conditions.</remarks>
*/

        private bool onStateChangedCalled = false;

/**
<summary>Flag indicating whether StateChanged delegate function
called.</summary>

<remarks>This needs to be initialized locally as the constructor is used to
verify the initial state conditions.</remarks>
*/

        private bool stateChangedCalled = false;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Initial state constructor.</summary>

<remarks>This is an unusual constructor, because most of the data members have
already been initialised and used.  It needs to check that the initial state's
events have been performed correctly, and in the correct sequence.</remarks>

<param name="initialState">The initial state of the state contect
object.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public SomeStateContext (SomeBaseState initialState):
            base (initialState)
        {

/*
Verify that the virtual and delegated state enter event functions were called,
but that none of the others were called.

NB: For the fourth test to succeed, a delegate function must be associated with
the initial state's enter event before any attempt is made to construct this
state context.
*/

            Assert.IsFalse (onStateExitCalled);
            Assert.IsFalse (stateExitCalled);
            Assert.IsTrue (onStateEnterCalled);
            Assert.IsTrue (stateEnterCalled);
            Assert.IsFalse (onStateChangedCalled);
            Assert.IsFalse (stateChangedCalled);

/*
Verify that the sequence count is 4.  (We should have executed the virtual
state enter event, which is step 3, and the delegated state event event, which
is step 4.)
*/

            Assert.AreEqual (sequenceCount, 4);

/*
Verify that we do not yet have a prior state.
*/

            Assert.IsTrue (previousState == null);

/*
Finally, verify that the initial state is our current state.
*/

            Assert.AreEqual (CurrentState, initialState);

/*
Verify that the current state is active.
*/

            Assert.IsTrue (CurrentState.IsActive);

/*
OK.  Here's a bit of construction for you: register our own function for the
state changed delegate.
*/

            StateChanged += StateChangedEvent;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Notification function that a state's OnStateExit method was
called.</summary>

<remarks>This function should be called from the state's virtual event handler
method.</remarks>

<param name="state">The state that changed.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void NotifyOnStateExitCalled (SomeBaseState state)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (state != null);
            System.Diagnostics.Debug.Assert (!onStateExitCalled);

/*
Update the sequence count and verify that we're at step 1.
*/

            ++sequenceCount;
            Assert.AreEqual (sequenceCount, 1);

/*
Verify that the state supplied is the current state (we have yet to exit this
state).
*/

            Assert.AreEqual (state, CurrentState);

/*
Verify that the state and the prior state are the same also.
*/

            Assert.AreEqual (state, previousState);

/*
Set the function called flag.
*/

            onStateExitCalled = true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Notification function that a state's Exit delegate method was
called.</summary>

<remarks>This function should be called from the state's delegated event
handler.</remarks>

<param name="state">The state that changed.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void NotifyStateExitCalled (SomeBaseState state)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (state != null);
            System.Diagnostics.Debug.Assert (!stateExitCalled);

/*
Update the sequence count and verify that we're at step 2.
*/

            ++sequenceCount;
            Assert.AreEqual (sequenceCount, 2);

/*
Verify that the state supplied is the current state (we have yet to exit this
state).
*/

            Assert.AreEqual (state, CurrentState);

/*
Verify that the state and the prior state are the same also.
*/

            Assert.AreEqual (state, previousState);

/*
Set the function called flag.
*/

            stateExitCalled = true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Notification function that a state's OnStateEnter method was
called.</summary>

<remarks>This function should be called from the state's virtual event handler
method.</remarks>

<param name="state">The state that changed.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void NotifyOnStateEnterCalled (SomeBaseState state)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (state != null);
            System.Diagnostics.Debug.Assert (!onStateEnterCalled);

/*
Update the sequence count and verify that we're at step 3.
*/

            ++sequenceCount;
            Assert.AreEqual (sequenceCount, 3);

/*
Verify that the state supplied is the current state.
*/

            Assert.AreEqual (state, CurrentState);

/*
Verify that the state and the prior state are different.
*/

            Assert.AreNotEqual (state, previousState);

/*
Set the function called flag.
*/

            onStateEnterCalled = true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Notification function that a state's Enter delegate method was
called.</summary>

<remarks>This function should be called from the state's delegated event
handler.</remarks>

<param name="state">The state that changed.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void NotifyStateEnterCalled (SomeBaseState state)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (state != null);
            System.Diagnostics.Debug.Assert (!stateEnterCalled);

/*
Update the sequence count and verify that we're at step 4.
*/

            ++sequenceCount;
            Assert.AreEqual (sequenceCount, 4);

/*
Verify that the state supplied is the current state.
*/

            Assert.AreEqual (state, CurrentState);

/*
Verify that the state and the prior state are different.
*/

            Assert.AreNotEqual (state, previousState);

/*
Set the function called flag.
*/

            stateEnterCalled = true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Virtual state changed event handler.</summary>

<remarks>React to the event that the state was changed.</remarks>

<param name="priorState">The <see cref="SomeBaseState" />-derived instance that
was our previous state.  The current state can be obtained through <see
cref="StateContext {FinalContext, BaseState}.CurrentState" />.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected override void OnStateChanged (SomeBaseState priorState)
        {

/*
Call the parent.
*/

            base.OnStateChanged (priorState);

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (!onStateChangedCalled);
            System.Diagnostics.Debug.Assert (previousState != null);

/*
Verify that the prior state was recorded correctly.
*/

            Assert.AreEqual (priorState, previousState);

/*
Verify that the prior state and the current state are different.
*/

            Assert.AreNotEqual (priorState, CurrentState);

/*
Signal that the virtual function has been called.
*/

            onStateChangedCalled = true;

/*
Increment the sequence count, and verify that we're at step 5.
*/

            ++sequenceCount;
            Assert.AreEqual (sequenceCount, 5);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Delegated state changed event handler.</summary>

<remarks>React to the event that the state was changed.</remarks>

<param name="sender">The <see cref="SomeStateContext" /> instance that raised
this event.  Should be this same class instance.</param>

<param name="priorState">The <see cref="SomeBaseState" />-derived instance that
was our previous state.  The current state can be obtained through <see
cref="StateContext {FinalContext, BaseState}.CurrentState" />.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void StateChangedEvent (SomeStateContext sender, SomeBaseState
        priorState)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (!stateChangedCalled);
            System.Diagnostics.Debug.Assert (previousState != null);

/*
Verify that the state context supplied is the correct state context.
*/

            Assert.AreEqual (sender, this);

/*
Verify that the prior state was recorded correctly.
*/

            Assert.AreEqual (priorState, previousState);

/*
Verify that the prior state and the current state are different.
*/

            Assert.AreNotEqual (priorState, CurrentState);

/*
Signal that the delegated function has been called.
*/

            stateChangedCalled = true;

/*
Increment the sequence count, and verify that we're step 6.
*/

            ++sequenceCount;
            Assert.AreEqual (sequenceCount, 6);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Change state of this context.</summary>

<param name="newState">The new state for this context.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void SetState (SomeBaseState newState)
        {

/*
Store the previous state, reset the sequence count and clear the virtual &
delegated event-function-called flags.
*/

            previousState = CurrentState;
            sequenceCount = 0;
            onStateExitCalled = false;
            stateExitCalled = false;
            onStateEnterCalled = false;
            stateEnterCalled = false;
            onStateChangedCalled = false;
            stateChangedCalled = false;

/*
Now effect the state change.
*/

            CurrentState = newState;

/*
Verify that the virtual and delegated functions were called.
*/

            Assert.IsTrue (onStateExitCalled);
            Assert.IsTrue (stateExitCalled);
            Assert.IsTrue (onStateEnterCalled);
            Assert.IsTrue (stateEnterCalled);
            Assert.IsTrue (onStateChangedCalled);
            Assert.IsTrue (stateChangedCalled);

/*
Verify that the sequence count is 6.
*/

            Assert.AreEqual (sequenceCount, 6);

/*
Verify that we correctly changed the state.
*/

            Assert.AreEqual (CurrentState, newState);

/*
Verify that the prior state is inactive and that the new state is active.
*/

            Assert.IsFalse (previousState.IsActive);
            Assert.IsTrue (CurrentState.IsActive);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Change state of this context - but in the middle of another state
change.</summary>

<remarks>This version should only be called from within the BadState's OnEnter
method.</remarks>

<param name="newState">The new state for this context.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void BadSetState (SomeBaseState newState)
        {

/*
This should fail with an exception.
*/

            CurrentState = newState;
        }
    }

//=============================================================================
/**
<summary>A custom base state object for testing purposes.</summary>
*/
//=============================================================================

    public abstract class SomeBaseState:
        AbstractState <SomeStateContext, SomeBaseState>
    {

/**
<summary>Flag indicating whether this state is active or not.</summary>

<remarks>Note: This requires that we do not re-use instances for different
state context objects simultaneously.</remarks>
*/

        private bool isActive;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public SomeBaseState ():
            base ()
        {

/*
Initially, this state is not active.
*/

            isActive = false;

/*
Add the delegates for the enter and exit events.
*/

            Enter += StateEnterEvent;
            Exit += StateExitEvent;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Report state's active status.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsActive
        {
            get
            {
                return isActive;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Virtual state enter event handler.</summary>

<remarks>React to the event that the state was entered.</remarks>

<param name="context">The <see cref="SomeStateContext" /> instance that has
just entered this state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected override void OnEnter (SomeStateContext context)
        {

/*
Call our parent.
*/

            base.OnEnter (context);

/*
Verify that this state is currently inactive, but make it active immediately
afterwards.
*/

            Assert.IsFalse (isActive);
            isActive = true;

/*
Verify that this is the context's current state.
*/

            Assert.AreEqual (this, context.CurrentState);

/*
Notify the context that this event was handled correctly.  This also validates
that this is the correct context.
*/

            context.NotifyOnStateEnterCalled (this);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Delegated state entered event handler.</summary>

<remarks>React to the event that the state was entered.</remarks>

<param name="sender">The <see cref="SomeBaseState" />-derived instance that is
the new state for the <paramref name="context" /> object.</param>

<param name="context">The <see cref="SomeStateContext" />-derived instance that
just entered the <paramref name="sender" /> state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void StateEnterEvent (SomeBaseState sender, SomeStateContext
        context)
        {

/*
Verify that this state is currently active; it should have just been changed by
the OnEnter method.
*/

            Assert.IsTrue (isActive);

/*
Verify that this is the context's current state.
*/

            Assert.AreEqual (this, context.CurrentState);

/*
Notify the context that this event was handled correctly.  This also validates
that this is the correct context.
*/

            context.NotifyStateEnterCalled (this);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Virtual state exit event handler.</summary>

<remarks>React to the event that the state was exited.</remarks>

<param name="context">The <see cref="SomeStateContext" /> instance that has
just exited this state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected override void OnExit (SomeStateContext context)
        {

/*
Call our parent.
*/

            base.OnExit (context);

/*
Verify that this state is currently active, but make it inactive immediately
afterwards.
*/

            Assert.IsTrue (isActive);
            isActive = false;

/*
Verify that this is still the context's current state (it should not have been
changed just yet).
*/

            Assert.AreEqual (this, context.CurrentState);

/*
Notify the context that this event was handled correctly.  This also validates
that this is the correct context.
*/

            context.NotifyOnStateExitCalled (this);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Delegated state exit event handler.</summary>

<remarks>React to the event that the state was exit.</remarks>

<param name="sender">The <see cref="SomeBaseState" />-derived instance that is
the old state for the <paramref name="context" /> object.</param>

<param name="context">The <see cref="SomeStateContext" />-derived instance that
just exited the <paramref name="sender" /> state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void StateExitEvent (SomeBaseState sender, SomeStateContext
        context)
        {

/*
Verify that this state is currently inactive; it should have just been changed
by the OnExit method.
*/

            Assert.IsFalse (isActive);

/*
Verify that this is still the context's current state (it should not have been
changed just yet).
*/

            Assert.AreEqual (this, context.CurrentState);

/*
Notify the context that this event was handled correctly.  This also validates
that this is the correct context.
*/

            context.NotifyStateExitCalled (this);
        }
    }

//=============================================================================
/**
<summary>Concrete state object, A.</summary>

<remarks>State A can transition to any other state, without
restriction.</remarks>
*/
//=============================================================================

    public sealed class SomeStateA:
        SomeBaseState
    {
    }

//=============================================================================
/**
<summary>Concrete state object, B.</summary>

<remarks>State B can only transition to state C, and cannot transition to state
A.</remarks>
*/
//=============================================================================

    public sealed class SomeStateB:
        SomeBaseState
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if a transition to specific state is possible.</summary>

<remarks>This state can only transition to state C, so disallow all other
options.</remarks>

<param name="newState">The <see cref="SomeBaseState" />-derived object
representing a potential new state.</param>

<returns>A <see cref="System.Boolean" /> value that is true if the transition
is possible or false if it is not.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public override bool CanTransitionTo (SomeBaseState newState)
	{

/*
If the new state is a state C state, then return true.
*/

            if (newState.GetType () == typeof (SomeStateC))
            {
                return true;
            }

/*
We cannot transition to this new state.
*/

	    return false;
	}
    }

//=============================================================================
/**
<summary>Concrete state object, C.</summary>

<remarks>State C can only transition to state A, and cannot transition to state
B.</remarks>
*/
//=============================================================================

    public sealed class SomeStateC:
        SomeBaseState
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if a transition to specific state is possible.</summary>

<remarks>This state can only transition to state A, so disallow all other
options.</remarks>

<param name="newState">The <see cref="SomeBaseState" />-derived object
representing a potential new state.</param>

<returns>A <see cref="System.Boolean" /> value that is true if the transition
is possible or false if it is not.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public override bool CanTransitionTo (SomeBaseState newState)
	{

/*
If the new state is a state C state, then return true.
*/

            if (newState.GetType () == typeof (SomeStateA))
            {
                return true;
            }

/*
We cannot transition to this new state.
*/

	    return false;
	}
    }

//=============================================================================
/**
<summary>Represents a badly behaved state.</summary>

<remarks>This state attempts to change the context's state when it handles
state events.</remarks>
*/
//=============================================================================

    public sealed class BadState:
        SomeBaseState
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Virtual state enter event handler - badly implemented.</summary>

<remarks>React to the event that the state was entered.</remarks>

<param name="context">The <see cref="SomeStateContext" /> instance that has
just entered this state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected override void OnEnter (SomeStateContext context)
        {

/*
Call our parent.
*/

            base.OnEnter (context);

/*
OK.  Now attempt to change the state to a state B instance.  This should result
in an exception being thrown.  Now, we shouldn't pass exceptions back to the
caller - as that invalidates event handling, so we'll play nice and catch it
here.
*/

            SomeStateB stateB = new SomeStateB ();
            bool sawException = false;
            try
            {
                context.BadSetState (stateB);
            }
            catch (StateChangeInProgressException <SomeStateContext,
            SomeBaseState>)
            {
                sawException = true;
            }
            Assert.IsTrue (sawException);

/*
Check that we're still the current state.
*/

            Assert.AreEqual (context.CurrentState, this);
        }
    }

//=============================================================================
/**
<summary>NUnit test fixture for the <see cref="StateContext {FinalContext,
BaseState}" /> and <see cref="AbstractState {FinalContext, BaseState}" />
classes.</summary>
*/
//=============================================================================

    [TestFixture]
    public sealed class StateTest
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check that null state context construction fails.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (System.ArgumentNullException))]
        public void NullStateContextConstruction ()
        {
            SomeStateContext context = new SomeStateContext (null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check that transtion to banned state fails.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (InvalidStateTransitionException
        <SomeStateContext, SomeBaseState>))]
        public void BadStateTransition ()
        {

/*
Create the states.
*/

            SomeStateB stateB = new SomeStateB ();
            SomeStateC stateC = new SomeStateC ();

/*
Initialise the context to state C.
*/

            SomeStateContext context = new SomeStateContext (stateC);
            Assert.AreEqual (context.CurrentState, stateC);

/*
This ought to do it.  Change the state to state B - which is a barred
transition.
*/

            context.SetState (stateB);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Basic operations.</summary>

<remarks>This test verifies that the state context and state objects meet
basic operational requirements.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void BasicOperations ()
        {

/*
Create a new instances of states A, B and C.
*/

            SomeStateA stateA = new SomeStateA ();
            BadState bad = new BadState ();
            SomeStateB stateB = new SomeStateB ();
            SomeStateC stateC = new SomeStateC ();

/*
Create a new state context object, with state A as the initial state.  Verify
that the initial state was recorded correctly.
*/

            SomeStateContext context = new SomeStateContext (stateA);
            Assert.AreEqual (context.CurrentState, stateA);

/*
Transition to the bad state.
*/

            context.SetState (bad);
            Assert.AreEqual (context.CurrentState, bad);

/*
Now transition to state B.
*/

            context.SetState (stateB);
            Assert.AreEqual (context.CurrentState, stateB);

/*
Now transition to state C.
*/

            context.SetState (stateC);
            Assert.AreEqual (context.CurrentState, stateC);
        }
    }
}
