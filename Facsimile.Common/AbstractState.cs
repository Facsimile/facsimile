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

C# source file for the AbstractState class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Generic abstract base class for state objects.</summary>

<remarks>A "state object" is one that encapsulates the internal state of an
associated <typeparamref name="FinalContext" /> object, called a "state
context" object; the state context object can delegate some of its behaviour to
its associated state objects.

<para>This class supports derived state classes that implement the "State"
design pattern's "Abstract State" role.  Refer to Gamma, et al: "Design
Patterns: Elements of Reusable Object-Oriented Software", Addison-Wesley, for
further information.</para>

<para>If an operation is invalid for a specific sub-class, then that operation
should throw the <see cref="System.InvalidOperationException" /> exception, or
an exception derived from this class.</para></remarks>

<example>For an example of how to use this class, refer to the <see
cref="StateContext {FinalContext, BaseContext}" /> class
documentation.</example>

<typeparam name="FinalContext">The <see cref="IStateContext {FinalContext,
BaseState}" /> implementation that represents the associated actual state
context class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="AbstractState {FinalContext,
BaseState}" /> sub-class that is the polymorphic base class defining the set of
available states for the associated <typeparamref name="FinalContext" /> type;
all classes derived from this base state class are suitable state classes for
this <typeparamref name="FinalContext" /> type.</typeparam>

<seealso cref="IStateContext {FinalContext, BaseState}" />

<seealso cref="StateContext {FinalContext, BaseState}" />
*/
//=============================================================================

    public abstract class AbstractState <FinalContext, BaseState>:
        System.Object
    where FinalContext:
        IStateContext <FinalContext, BaseState>
    where BaseState:
        AbstractState <FinalContext, BaseState>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if a transition to specific state is possible.</summary>

<remarks>Determine whether it is possible to perform a transition from this
state to a potential new state.  This will be used to prevent state transitions
that do not make sense.

<para>Override this function to perform custom state transition checking; this
default version of the function allows all state transitions.  When overriding
this function, the criteria for your decision should be limited solely to
whether a transition from this state to <paramref name="newState" /> is
possible, so that the answer is always the same given the same <paramref
name="newState" /> value.</para>

<para>It is not possible to transition to the current state (or to a new
instance of the same state object as the current state).  This condition is
checked before this function is called, so there is no need to add this
particular type of filter.</para>

<para>This function should not pass any unhandled exceptions to the
caller.</para></remarks>

<param name="newState">The <typeparamref name="BaseState" /> sub-class object
representing a potential new state.</param>

<returns>A <see cref="System.Boolean" /> value that is true if the transition
is possible or false if it is not.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public virtual bool CanTransitionTo (BaseState newState)
        {

/*
This default version permits all transitions.  Override to filter transitions
that are not valid.
*/

            return true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State enter event handler.</summary>

<remarks>Overriding this method allows sub-classes to handle the associated
<paramref name="context" /> entering this state.

<para>Override to perform sub-class specific enter event handling; this default
version does nothing.  When overriding, be sure to call the base class's
version to avoid loss of functionality.</para>

<para>This method should not pass unhandled expceptions back to the
caller.</para></remarks>

<param name="context">The <typeparamref name="FinalContext" /> instance that
has just entered this state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual void OnEnter (FinalContext context)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State exit event handler.</summary>

<remarks>Overriding this method allows sub-classes to handle the associated
<paramref name="context" /> leaving this state.

<para>Override to perform sub-class specific exit event handling; this default
version does nothing.  When overriding, be sure to call the base class's
version to avoid loss of functionality.</para>

<para>This method should not pass unhandled expceptions back to the
caller.</para></remarks>

<param name="context">The <typeparamref name="FinalContext" /> instance that
has just left this state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual void OnExit (FinalContext context)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Raise the state enter event.</summary>

<remarks>This function is called from the <typeparamref name="FinalContext" />
instance when it enters this state.</remarks>

<param>The <typeparamref name="FinalContext" /> instance that is entering this
state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal virtual void RaiseEnter (FinalContext context)
        {

/*
Allow sub-classes to handle this event.
*/

            OnEnter (context);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Raise the state exit event.</summary>

<remarks>This function is called from the <typeparamref name="FinalContext" />
instance when it leaves this state.</remarks>

<param>The <typeparamref name="FinalContext" /> instance that is leaving this
state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal virtual void RaiseExit (FinalContext context)
        {

/*
Allow sub-classes to handle this event.
*/

            OnExit (context);
        }
    }
}
