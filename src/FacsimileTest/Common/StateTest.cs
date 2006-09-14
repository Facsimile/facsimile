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

C# source file for the State and StateContext test suite objects, which are
integral members of theFacsimileTest.Common namespace.
===============================================================================
*/

namespace FacsimileTest.Common {

//=============================================================================
/**
<summary>State base class for the test framework.</summary>

<remarks>This class extends <see cref="Facsimile.Common.StateContext
{FinalContext, BaseState}" /> and forms the basis for the states used by the
state test suite.</remarks>
*/
//=============================================================================

    public abstract class TestBaseState: Facsimile.Common.State
    <TestStateContext, TestBaseState> {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State name.</summary>

<remarks>Reports the name of this state.</remarks>

<value>A <see cref="System.String" /> containing the name of this
state.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public abstract string Name {
	    get;
	}
    }

//=============================================================================
/**
<summary>Arbitrary test state 1.</summary>

<remarks>This class extends <see cref="TestBaseState" /> and represents an
arbitrary test state that can only transition to <see cref="TestStateTwo"
/>.</remarks>
*/
//=============================================================================

    public sealed class TestStateOne: TestBaseState {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Decide whether transition to specific state is possible.</summary>

<remarks>The decision must be made without reference to the <typeparamref
name="FinalContext" /> instance concerned, forcing the decision to be based
upon the current state and potential new state only.

<para>This function should not pass any unhandled exceptions to the
caller.</para></remarks>

<returns>A <see cref="System.Boolean" /> value indicating whether the
transition is possible (true) or not (false).</returns>

<param name="newState">The <typeparamref name="BaseState" />-derived object
representing a potential new state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected override bool DecideCanTransitionTo (TestBaseState newState)
	{

/*
Can only transition to TestStateTwo.
*/

	    return (newState.GetType () == typeof (TestStateTwo));
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State name.</summary>

<remarks>Reports the name of this state.</remarks>

<value>A <see cref="System.String" /> containing the name of this
state.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public override string Name {
	    get {
		return "One";
	    }
	}
    }

//=============================================================================
/**
<summary>Arbitrary test state 2.</summary>

<remarks>This class extends <see cref="TestBaseState" /> and represents an
arbitrary test state that can only transition to <see cref="TestStateThree"
/>.</remarks>
*/
//=============================================================================

    public sealed class TestStateTwo: TestBaseState {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Decide whether transition to specific state is possible.</summary>

<remarks>The decision must be made without reference to the <typeparamref
name="FinalContext" /> instance concerned, forcing the decision to be based
upon the current state and potential new state only.

<para>This function should not pass any unhandled exceptions to the
caller.</para></remarks>

<returns>A <see cref="System.Boolean" /> value indicating whether the
transition is possible (true) or not (false).</returns>

<param name="newState">The <typeparamref name="BaseState" />-derived object
representing a potential new state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected override bool DecideCanTransitionTo (TestBaseState newState)
	{

/*
Can only transition to TestStateTwo.
*/

	    return (newState.GetType () == typeof (TestStateThree));
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State name.</summary>

<remarks>Reports the name of this state.</remarks>

<value>A <see cref="System.String" /> containing the name of this
state.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public override string Name {
	    get {
		return "Two";
	    }
	}
    }

//=============================================================================
/**
<summary>Arbitrary test state 3.</summary>

<remarks>This class extends <see cref="TestBaseState" /> and represents an
arbitrary test state that can only transition to any other state.</remarks>
*/
//=============================================================================

    public sealed class TestStateThree: TestBaseState {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State name.</summary>

<remarks>Reports the name of this state.</remarks>

<value>A <see cref="System.String" /> containing the name of this
state.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public override string Name {
	    get {
		return "Three";
	    }
	}
    }

//=============================================================================
/**
<summary>The test state context.</summary>

<remarks>Objects of this type may be in any one of the states: one, two or
three.  It extends <see cref="Facsimile.Common.StateContext {FinalContext,
BaseState}" />.</remarks>
*/
//=============================================================================

    public sealed class TestStateContext: Facsimile.Common.StateContext
    <TestStateContext, TestBaseState> {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State constructor.</summary>

<remarks>Set the initial state to that specified by the caller.</remarks>

<param name="initialState">The state that this state context object will be in
initially.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public TestStateContext (TestBaseState initialState): base
	(initialState) {

/*
Confirm that we stored the initial state correctly.
*/

	    NUnit.Framework.Assert.AreEqual (CurrentState, initialState,
	    "Initial state was not stored correctly");
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Change the state.</summary>

<remarks>Change the current state to the value indicated.</remarks>

<param name="newState">The new state to which the state context object should
be assigned.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void SetState (TestBaseState newState) {

/*
Set the current state and check that we changed it correctly.  Note that
changing the state may cause an exception to be thrown.
*/

	    CurrentState = newState;
	    NUnit.Framework.Assert.AreEqual (CurrentState, newState,
	    "New state was not stored correctly");
	}
    }

//=============================================================================
/**
<summary>Test fixture for the <see cref="Facsimile.Common.State {FinalContext,
BaseState}" /> and <see cref="Facsimile.Common.StateContext {FinalContext,
BaseState}" /> classes.</summary>

<remarks>This class consists of a set of unit tests for the <see
cref="Facsimile.Common.State {FinalContext, BaseState}" /> and <see
cref="Facsimile.Common.StateContext {FinalContext, BaseState}" /> classes.  The
unit tests themselves are executed by the NUnit unit testing framework.

<para>For further information on NUnit, please go to
http://www.nunit.org/</para></remarks>
*/
//=============================================================================

    [NUnit.Framework.TestFixture]
    public sealed class StateTest {

/**
<summary>Flag that handler was executed.</summary>
*/

	private bool handlerExecuted;

/**
<summary>Reference to most recent handler notifier.</summary>
*/

	private TestStateContext handlerSender;

/**
<summary>Reference to most recent prior state in handler.</summary>
*/

	private TestBaseState handlerPriorState;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Initialise the handler data.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void InitHandlerData () {

/*
Reset the recorded handler data.
*/

	    handlerExecuted = false;
	    handlerSender = null;
	    handlerPriorState = null;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Event change handler.</summary>

<remarks>This function is used to handle state change events and to verify the
information provided (via class members).</remarks>

<param name="sender">The state context object whose state is being
changed.</param>
<param name="priorState">The previous state of the sender.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void StateChangeHandler (TestStateContext sender, TestBaseState
	priorState) {

/*
Set up the flags as appropriate.
*/

	    handlerExecuted = true;
	    handlerSender = sender;
	    handlerPriorState = priorState;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Invalid event change handler.</summary>

<remarks>This function handles state change events, but attempts to change the
state whilst handling it.  This should result in an exception being
thrown.</remarks>

<param name="sender">The state context object whose state is being
changed.</param>
<param name="priorState">The previous state of the sender.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void InvalidStateChangeHandler (TestStateContext sender,
	TestBaseState priorState) {

/*
Set up the flags as appropriate.
*/

	    handlerExecuted = true;
	    handlerSender = sender;
	    handlerPriorState = priorState;

/*
Now attempt to change the state to state one.  This should cause an exception
to be thrown.
*/

	    sender.SetState (new TestStateOne ());
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test for null argument exception on constructor.</summary>

<remarks>Tests that attempting to construct a state context object with a null
initial state causes the <see cref="System.ArgumentNullException" /> to be
thrown.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	[NUnit.Framework.ExpectedException (typeof
	(System.ArgumentNullException))]
	public void TestStateContextNullConstruction () {
	    new TestStateContext (null);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test for null argument exception when changing state.</summary>

<remarks>Tests that attempting to assign a null state reference to a state
context object results in the <see cref="System.ArgumentNullException" /> being
thrown.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	[NUnit.Framework.ExpectedException (typeof
	(System.ArgumentNullException))]
	public void TestStateContextNullAssignment () {

/*
Initialise a state context object with an arbitrary initial state.  Catch any
exceptions during this construction - we do not want a false positive.
*/

	    TestStateContext testContext;
	    try {
		testContext = new TestStateContext (new TestStateOne ());
	    }
	    catch {
		NUnit.Framework.Assert.Fail ("Unexpected exception thrown " +
		"during state context/state construction");
		throw;
	    }

/*
Now check that assigning a null state to the state context fails with a null
argument exception.
*/

	    testContext.SetState (null);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test for invalid operation exception when transitioning to barred
state.</summary>

<remarks>Tests that attempting to change the state context object's state to
one for which there is no transition available results in an <see
cref="System.InvalidOperationException" /> being thrown.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	[NUnit.Framework.ExpectedException (typeof
	(System.InvalidOperationException))]
	public void TestStateContextBadTransition () {

/*
Initialise a state context object with state one.  Catch any exceptions during
this construction - we do not want a false positive.
*/

	    TestStateContext testContext;
	    try {
		testContext = new TestStateContext (new TestStateOne ());
	    }
	    catch {
		NUnit.Framework.Assert.Fail ("Unexpected exception thrown " +
		"during state context/state construction");
		throw;
	    }

/*
Now attempt to change the state to state three.  This should result in an
exception because state one only allows transitions to state two.
*/

	    testContext.SetState (new TestStateThree ());
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test basic state/state context operation.</summary>

<remarks>Performs a number of tests aimed at verifying basic state/state
context .</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	public void TestBasicOperation () {

/*
Create a some state instances.
*/

	    TestStateOne one = new TestStateOne ();
	    TestStateTwo two = new TestStateTwo ();
	    TestStateThree three = new TestStateThree ();

/*
Create a new state context test object, initialised to the state one object.
*/

	    TestStateContext context = new TestStateContext (one);

/*
OK.  Now register our event handler with the state context object.
*/

	    context.StateChanged += StateChangeHandler;

/*
Now initialise the handler data and perform a transition to state two.  Check
that the handler was called and that the expected data was seen.
*/

	    InitHandlerData ();
	    context.SetState (two);
	    NUnit.Framework.Assert.IsTrue (handlerExecuted, "Event handler " +
	    "was not executed on transition to state two");
	    NUnit.Framework.Assert.AreEqual (handlerSender, context,
	    "Event handler sender does not match changed state context on " +
	    "transition to state two");
	    NUnit.Framework.Assert.AreEqual (handlerPriorState, one, "Event " +
	    "handler prior state does not match expected prior state (one)");

/*
Now set the state to three.  Shouldn't see any problems here as we've just
checked most of the same functionality above.
*/

	    InitHandlerData ();
	    context.SetState (three);
	    NUnit.Framework.Assert.IsTrue (handlerExecuted, "Event handler " +
	    "was not executed on transition to state three");
	    NUnit.Framework.Assert.AreEqual (handlerSender, context,
	    "Event handler sender does not match changed state context on " +
	    "transition to state three");
	    NUnit.Framework.Assert.AreEqual (handlerPriorState, two, "Event " +
	    "handler prior state does not match expected prior state (two)");

/*
Now set the state back to one.  This is different to the other two, because
state three is not supposed to filter out any transitions.
*/

	    InitHandlerData ();
	    context.SetState (one);
	    NUnit.Framework.Assert.IsTrue (handlerExecuted, "Event handler " +
	    "was not executed on transition to state one");
	    NUnit.Framework.Assert.AreEqual (handlerSender, context,
	    "Event handler sender does not match changed state context on " +
	    "transition to state one");
	    NUnit.Framework.Assert.AreEqual (handlerPriorState, three,
	    "Event handler prior state does not match expected prior state " +
	    "(three)");
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test for invalid operation exception from bad event handler.</summary>

<remarks>Tests that attempting to change the state context object's state when
handling an event causes the <see cref="System.InvalidOperationException" /> to
be thrown.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	[NUnit.Framework.ExpectedException (typeof
	(System.InvalidOperationException))]
	public void TestBadEventHandler () {

/*
Initialise a state context object with state one.  Catch any exceptions during
this construction - we do not want a false positive.
*/

	    TestStateContext testContext;
	    TestStateOne one;
	    TestStateTwo two;
	    try {
		one = new TestStateOne ();
		two = new TestStateTwo ();
		testContext = new TestStateContext (one);
	    }
	    catch {
		NUnit.Framework.Assert.Fail ("Unexpected exception thrown " +
		"during state context/state construction");
		throw;
	    }

/*
Now associate the bad event handler with this state context object.
*/

	    testContext.StateChanged += InvalidStateChangeHandler;

/*
Initialise the handler data and change the state to two (this should be a legal
state transition).
*/

	    InitHandlerData ();
	    try {
		testContext.SetState (two);
	    }

/*
Catch all arising exceptions and validate that we called the handler and that
the object's state is still one (the state prior to the change).
*/

	    catch {
		NUnit.Framework.Assert.IsTrue (handlerExecuted, "Event " +
		"handler was not executed on transition to state two");
		NUnit.Framework.Assert.AreEqual (handlerSender, testContext,
		"Event handler sender does not match changed state context " +
		"on transition to state two");
		NUnit.Framework.Assert.AreEqual (handlerPriorState, one,
		"Event handler prior state does not match expected prior " +
		"state (one)");
		NUnit.Framework.Assert.AreEqual (testContext.CurrentState, two,
		"Test context should still have state of two");
		throw;
	    }
	}
    }
}
