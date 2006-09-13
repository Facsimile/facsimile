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

C# source file for the State class, and related elements, that are integral
members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common {

//=============================================================================
/**
<summary>Generic abstract base class for state objects.</summary>

<remarks>A "state object" is one that encapsulates the internal state of an
associated <typeparamref name="FinalContext" /> object, called a "state
context" object; the state context object can delegate some of its behaviour to
its associated state objects.

<para>This class supports derived state classes that implement the "State"
design pattern's "State" role.  Refer to Gamma, et al: "Design Patterns:
Elements of Reusable Object-Oriented Software", Addison-Wesley, for further
information.</para>

<para>If an operation is invalid for a specific base class, then that operation
should throw the <see cref="System.InvalidOperationException" /> exception, or
an exception derived from this class.</para></remarks>

<example>For an example of how to use this class, refer to the <see
cref="StateContext {FinalContext, BaseContext}" /> class
documentation.</example>

<typeparam name="FinalContext">The <see cref="StateContext {FinalContext,
BaseState}" />-derived type that represents the associated actual state context
class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The State-derived base class defining the set of
available states for the associated <typeparamref name="FinalContext" /> type;
all classes derived from this base class are suitable state classes for the
associated state context class.</typeparam>

<seealso cref="StateContext {FinalContext, BaseState}" />
*/
//=============================================================================

    public abstract class State <FinalContext, BaseState> where FinalContext:
    StateContext <FinalContext, BaseState> where BaseState: State
    <FinalContext, BaseState> {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if transition to specific state is possible.</summary>

<remarks>Determine whether it is possible to perform a transition from this
state to a potential new state.  This will be used to prevent state transitions
that do not make sense.

<para>This function should not pass any unhandled exceptions to the
caller.</para>

<para>The decision is made by the state-specific <see
cref="DecideCanTransitionTo" /> method.</para></remarks>

<returns>A <see cref="System.Boolean" /> value indicating whether the
transition is possible (true) or not (false).</returns>

<param name="newState">The <typeparamref name="BaseState" />-derived object
representing a potential new state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	internal bool CanTransitionTo (BaseState newState) {

/*
Make the decision as to whether we support this transition or not.

TODO: Trap any exceptions thrown and log/handler/terminate as appropriate.
*/

	    return DecideCanTransitionTo (newState);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Decide whether transition to specific state is possible.</summary>

<remarks>The decision must be made without reference to the <typeparamref
name="FinalContext" /> instance concerned, forcing the decision to be based
upon the current state and potential new state only.

<para>This function should not pass any unhandled exceptions to the
caller.</para>

<para>Override this method to customise the set of permissible state
transitions; the default method permits all transitions.</para></remarks>

<returns>A <see cref="System.Boolean" /> value indicating whether the
transition is possible (true) or not (false).</returns>

<param name="newState">The <typeparamref name="BaseState" />-derived object
representing a potential new state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected virtual bool DecideCanTransitionTo (BaseState newState) {

/*
This default version permits all transitions.  Override to filter transitions
that are not valid.
*/

	    return true;
	}
    }
}
