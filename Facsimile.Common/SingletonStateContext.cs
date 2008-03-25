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

C# source file for the SingletonStateContext class, and associated elements,
that are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
 <summary>SingletonStateContext generic abstract base class.</summary>

<remarks>In design pattern terms, this class is a "Singleton" that also
fulfills the "State Context" role of the "State" pattern.  Refer to Gamma, et
al: "Design Patterns: Elements of Reusable Object-Oriented Software",
Addison-Wesley, for further information on these patterns.

<para>The class works by fusing an implementation of the <see
cref="IStateContext {FinalContext, BaseState}" /> interface to the <see
cref="Singleton {SingletonBase}" /> class.  Refer to these types for further
information.  It combines these two features into one base class for
convenience, since C# does not fully support multiple
inheritance.</para></remarks>
*/
//=============================================================================

    public abstract class SingletonStateContext <SingletonBase, FinalContext,
    BaseState>:
        Singleton <SingletonBase>, IStateContext <FinalContext, BaseState>
    where SingletonBase:
        SingletonStateContext <SingletonBase, FinalContext, BaseState>, new ()
    where FinalContext:
        StateContext <FinalContext, BaseState>, new ()
    where BaseState:
        AbstractState <FinalContext, BaseState>
    {

/**
<summary>Our state context object.</summary>

<remarks>Static functions can reference this instance through the <see
cref="Singleton {SingletonBase}.Instance" /> property.</remarks>
*/

        private readonly FinalContext context;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State changed event.</summary>

<remarks>Event signalling that this object has successfully completed a state
change operation.

<para>If your logic needs to determine when a change to or from a specific
state has occurred, rather than a general change in state, then you are
recommended to subscribe to the state's <see cref="AbstractState {FinalContext,
BaseState}.Enter" /> and/or <see cref="AbstractState {FinalContext,
BaseState}.Exit" /> events instead.</para>

<para>Note that until the state change event handlers have completed, the
current state change operation is regarded as still being in effect (although
the state context object's state will have been changed).  For this reason,
state change handler logic cannot itself change the state context's
state.</para></remarks>

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="IStateContext {FinalContext, BaseState}.StateChanged" />

<seealso cref="AbstractState {FinalContext, BaseState}.Enter" />

<seealso cref="AbstractState {FinalContext, BaseState}.Exit" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public static event StateChangedHandler <FinalContext, BaseState>
        StateChanged
        {

/*
Override the add-accessor declaration to store the delegate on the context's
StateChanged event.  When the context's state changes, these delegates will be
called.
*/

            add
            {
                System.Diagnostics.Debug.Assert (Instance.context != null);
                Instance.context.StateChanged += value;
            }

/*
For symmetry, override the remove-accessor declaration to remove the delegate
from the context's StateChanged event.  These delegates will no longer be
called when the context's state changes.
*/

            remove
            {
                System.Diagnostics.Debug.Assert (Instance.context != null);
                Instance.context.StateChanged -= value;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Current state property.</summary>

<remarks>The previous event's <see cref="AbstractState {FinalContext,
BaseState}.Exit" /> event, the new event's <see cref="AbstractState
{FinalContext, BaseState}.Enter" /> event and this instance's <see
cref="StateChanged" /> events are raised if the current state is changed
successfully.  For the exact sequence in which these events are raised, refer
to <see cref="StateContext {FinalContext, BaseState}" />.

<para>It is not possible to change the current state if an existing state
change operation is currently in progress.  If this is attempted, a <see
cref="System.InvalidOperationException" /> exception will be
raised.</para></remarks>

<value>A <typeparamref name="BaseState" /> sub-class instance representing this
state context's current state.</value>

<exception cref="System.InvalidOperationException">Thrown if this state context
object is already in the process of changing to a new state (caused by state
change event handler code attempting to implement state changes) or by attempts
to transition to a state that is not permitted by the current
state.</exception>

<exception cref="System.ArgumentNullException">Thrown if an attempt is made to
change the state to a null object.</exception>

<seealso cref="IStateContext {FinalContext, BaseState}" />

<seealso cref="StateContext {FinalContext, BaseState}" />

<seealso cref="StateChanged" />

<seealso cref="AbstractState {FinalContext, BaseState}.Enter" />

<seealso cref="AbstractState {FinalContext, BaseState}.Exit" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


	public static BaseState CurrentState
	{
            get
            {
                System.Diagnostics.Debug.Assert (Instance.context != null);
                return Instance.context.CurrentState;
            }
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

        public static bool IsInState (System.Type stateType)
        {
            return Instance.context.IsInState (stateType);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>The primary responsibility of this constructor is to create the state
context object.  Control of this singleton's state will be handled by this
object.

<para>Note that <see cref="Singleton {SingletonBase}" /> sub-classes complete
instance initialization before they complete static
initialization, so avoid the use of static members here.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public SingletonStateContext ()
        {

/*
Create the state context object that will keep track of our state for us.

The context must automatically select the most appropriate initial state by
itself.  Since we're delegating our state-management to this object, this
should not be too much of a challenge.
*/

            context = new FinalContext ();
            System.Diagnostics.Debug.Assert (context != null);

/*
Add our handler to the context's StateChanged event so that it gets called when
the state changes.
*/

            context.StateChanged += handleStateChanged;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Context access.</summary>

<remarks>Provide sub-classes access to the context object.</remarks>

<value>A <typeparamref name="FinalContext" /> instance acting as the state
context object for this class.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected FinalContext Context
        {
            get
            {
                return context;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Handle state changed event.</summary>

<remarks>This function, which matches the <see cref="StateChangedHandler
{FinalContext, BaseState}" /> delegate function specification, handles the
state context object's state changed event.

<para>All that the function needs to do is to call the <see
cref="OnStateChanged (BaseState)" /> virtual function.</para></remarks>

<param name="sender">The <typeparamref name="FinalContext" /> instance that
just changed state.  This should match our own context reference and can be
diregarded otherwise.</param>

<param name="priorState">The <typeparamref name="BaseState" /> sub-class
instance representing the <paramref name="sender" />'s previous state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void handleStateChanged (FinalContext sender, BaseState
        priorState)
        {
            System.Diagnostics.Debug.Assert (sender == context);
            OnStateChanged (priorState);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State changed event handler.</summary>

<remarks>Overriding this method allows sub-classes to handle the state changed
event without needing to attach a delegate to the <see cref="StateChanged" />
event.  This is the preferred mechanism for sub-classes.

<para>Override to perform sub-class specific state change event handling; this
default version does nothing.  When overriding, be sure to call the base
class's version to avoid loss of functionality.</para>

<para>This method should not pass unhandled expceptions back to the
caller.</para></remarks>

<param name="priorState">The <typeparamref name="BaseState" /> sub-class
instance that was our previous state.  The current state can be obtained
through <see cref="CurrentState" />.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual void OnStateChanged (BaseState priorState)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State changed event.</summary>

<remarks>Event signalling that this object has successfully completed a state
change operation.

<para>This event is required to implement the <see cref="IStateContext
{FinalContext, BaseState}" /> interface, although it isn't particularly useful
in this class (it can only be referenced through the single instance, which is
not public, and when converted to a <see cref="IStateContext {FinalContext,
BaseState}" /> interface reference).  The event's implementation is to tie
itself to the <typeparamref name="FinalContext" />'s <see cref="StateContext
{FinalContext, BaseState}.StateChanged" /> event.</para>

<para>Most user's of this class will subscribe to the static <see
cref="StateChanged" /> event instead (which also ties itself to the
<typeparamref name="FinalContext" />'s <see cref="StateContext {FinalContext,
BaseState}.StateChanged" /> event.</para></remarks>

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="StateChanged" />

<seealso cref="AbstractState {FinalContext, BaseState}.Enter" />

<seealso cref="AbstractState {FinalContext, BaseState}.Exit" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	event StateChangedHandler <FinalContext, BaseState> IStateContext
        <FinalContext, BaseState>.StateChanged
        {

/*
Override the add-accessor declaration to store the delegate on the context's
StateChanged event.  When the context's state changes, these delegates will be
called.
*/

            add
            {
                System.Diagnostics.Debug.Assert (context != null);
                context.StateChanged += value;
            }

/*
For symmetry, override the remove-accessor declaration to remove the delegate
from the context's StateChanged event.  These delegates will no longer be
called when the context's state changes.
*/

            remove
            {
                System.Diagnostics.Debug.Assert (context != null);
                context.StateChanged -= value;
            }

        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Current state property.</summary>

<remarks>Report the state context's current state.

<para>This property is required to implement the <see cref="IStateContext
{FinalContext, BaseState}" /> interface, although it isn't particularly useful
in this class (it can only be referenced through the single instance, which is
not public, and when converted to a <see cref="IStateContext {FinalContext,
BaseState}" /> interface reference).  The property's implementation is to
report our state context object's state.</para>

<para>Most users of this class will use the static <see cref="CurrentState" />
version of this property.</para></remarks>

<value>A <typeparamref name="BaseState" /> sub-class instance representing this
state context's current state.</value>

<seealso cref="CurrentState" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


	BaseState IStateContext <FinalContext, BaseState>.CurrentState
        {
            get
            {
                System.Diagnostics.Debug.Assert (context != null);
                return context.CurrentState;
            }
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

        bool IStateContext <FinalContext, BaseState>.IsInState (System.Type
        stateType)
        {
            return context.IsInState (stateType);
        }
    }
}
