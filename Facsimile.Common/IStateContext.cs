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

C# source file for the IStateContext interface, and associated elements, that
are integral members of the Facsimile.Common namespace.
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
virtual functions are called, refer to <see cref="IStateContext {FinalState,
BaseState}" /> and <see cref="StateContext {FinalContext, BaseState}"
/>.</para></remarks>

<param name="sender">The <typeparamref name="FinalContext" /> implementation
instance that raised the state change event.  This context instance's new state
can be obtained via its <see cref="IStateContext {FinalContext,
BaseState}.CurrentState" /> property.</param>

<param name="priorState">The <typeparamref name="BaseState" /> sub-class
instance representing the <paramref name="sender" />'s previous state.</param>

<example>For an example of using this delegate, refer to <see
cref="StateContext {FinalContext, BaseState}" />.</example>

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

<seealso cref="AbstractState {FinalContext, BaseState}" />

<seealso cref="ReportingAbstractState {FinalContext, BaseState}" />

<seealso cref="StateEnterHandler {FinalContext, BaseState}" />

<seealso cref="StateExitHandler {FinalContext, BaseState}" />
*/
//=============================================================================

    public delegate void StateChangedHandler <FinalContext, BaseState>
    (FinalContext sender, BaseState priorState)
    where FinalContext:
        IStateContext <FinalContext, BaseState>
    where BaseState:
        AbstractState <FinalContext, BaseState>
    ;

//=============================================================================
/**
<summary>Interface for state context objects.</summary>

<remarks>A state context object is one that, at a given point in time, may be
in just one from an available set of states.  Furthermore, the behavior of each
state context object depends upon its current state to the extent that it may
delegate some of its behavior to that state.

<para>Each state is represented by a sub-class of <typeparamref
name="BaseState" />.  The set of available states are, therefore, all concrete
<typeparamref name="BaseState" /> sub-classes.  Creating a new state is simply
a matter of deriving a new state from <typeparamref name="BaseState" />.</para>

<para>State change operations involve a number of different steps, which should
proceed in the following sequence:</para>

<list type="number">
    <item>
        <description>A state change operation is attempted upon a <typeparamref
        name="FinalContext" /> instance.</description>
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
        <description>If the previous state is a <see
        cref="ReportingAbstractState {FinalContext, BaseState}" /> sub-class,
        then its <see cref="ReportingAbstractState {FinalContext,
        BaseState}.Exit" /> event is raised.</description>
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
        <description>If the new state is a <see cref="ReportingAbstractState
        {FinalContext, BaseState}" /> sub-class, then its <see
        cref="ReportingAbstractState {FinalContext, BaseState}.Enter" /> event
        is raised.</description>
    </item>
    <item>
        <description>If the <typeparamref name="FinalContext" /> instance
        implmenents an <c>OnStateChanged (BaseState)</c> virtual function, then
        this should be called next.</description>
    </item>
    <item>
        <description>The <typeparamref name="FinalContext" /> instance's <see
        cref="StateChanged" /> event is raised.</description>
    </item>
</list>

<para>Your implementation of this interface should ensure that these steps are
fulfilled.</para>

<para>This interface should be implemented by classes that fulfill the State
design pattern's "Context" role.  Refer to Gamma, et al: "Design Patterns:
Elements of Reusable Object-Oriented Software", Addison-Wesley, for further
information.</para>

<para>You are strongly recommended to make your context class a sub-class of
<see cref="StateContext {FinalContext, BaseState}" /> rather than to implement
this interface directly.</para></remarks>

<typeparam name="FinalContext">The <see cref="IStateContext {FinalContext,
BaseState}" /> sub-class that represents the associated actual state context
class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="AbstractState {FinalContext,
BaseState}" /> sub-class that is the polymorphic base class defining the set of
available states for the associated <typeparamref name="FinalContext" /> type;
all classes derived from this base state class are suitable state classes for
this <typeparamref name="FinalContext" /> type.</typeparam>

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="StateContext {FinalContext, BaseState}" />

<seealso cref="AbstractState {FinalContext, BaseState}" />

<seealso cref="ReportingAbstractState {FinalContext, BaseState}" />
*/
//=============================================================================

    public interface IStateContext <FinalContext, BaseState>
    where FinalContext:
        IStateContext <FinalContext, BaseState>
    where BaseState:
        AbstractState <FinalContext, BaseState>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State changed event.</summary>

<remarks>Event signalling that this object has successfully completed a state
change operation.

<para>If your states are sub-classes of the <see cref="ReportingAbstractState
{FinalContext, BaseState}" /> class and your logic needs to determine when a
change to or from a specific state has occurred, then rather subscribe to this
event, you are recommended to subscribe to the state's <see
cref="ReportingAbstractState {FinalContext, BaseState}.Enter" /> and/or <see
cref="ReportingAbstractState {FinalContext, BaseState}.Exit" /> events
instead.</para>

<para>Note that until the state change event handlers have completed, the
current state change operation is regarded as still being in effect (although
the state context object's state will have been changed).  For this reason,
state change handler logic cannot itself change the state context's
state (otherwise, an exception will arise).</para>

<para>Implementations should ensure that this event is raised whenever a state
change operation is performed.</para></remarks>

<example>For an example of handling this event, refer to <see
cref="StateContext {FinalContext, BaseState}" />.</example>

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="ReportingAbstractState {FinalContext, BaseState}.Enter" />

<seealso cref="ReportingAbstractState {FinalContext, BaseState}.Exit" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        event StateChangedHandler <FinalContext, BaseState> StateChanged;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Current state property.</summary>

<remarks>Implementations need to store the current state as a <typeparamref
name="BaseState" /> sub-class instance, reporting that instance here, whilst
also providing a secure mechanism for changing state.</remarks>

<value>A <typeparamref name="BaseState" /> sub-class instance representing this
state context's current state.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


        BaseState CurrentState
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Is current state derived from indicated type?.</summary>

<remarks>Determines whether the context's current state is an instance of the
indicated type.

<para>This function is provided primarily for information and verification
purposes, rather than as a foundation for creating state-specific logic.  The
preferred way to implement state-specific logic is to implement virtual
functions on the <typeparamref name="BaseState" /> class.</para></remarks>

<param name="stateType">A <see cref="System.Type" /> reference identifying the
type that we're comparing the current state's instance with.  This reference
cannot be null.  Any type, including interfaces, may be specified; the type is
not limited to sub-classes of the <typeparamref name="BaseState" />.</param>

<returns>A <see cref="System.Boolean" /> that is true if this context's current
state is an instance of the indicated <paramref name="stateType" /> or false
otherwise.</returns>

<exception cref="System.ArgumentNullException">Thrown if <paramref
name="stateType" /> is null.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        bool IsInState (System.Type stateType);
    }
}
