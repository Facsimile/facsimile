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

C# source file for the ReportingAbstractState class, and associated elements,
that are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Generic state enter event handler delegate.</summary>

<remarks>Delegated function for handling state enter events raised by a
<typeparamref name="BaseState" /> sub-class instance.

<para>The state enter event is raised and processed as part of a state change
operation on a <typeparamref name="FinalContext" /> object.</para>

<para>For more detailed information on the order in which events and associated
virtual functions are called, refer to <see cref="IStateContext {FinalContext,
BaseState}" />.</para></remarks>

<param name="sender">The <typeparamref name="BaseState" /> sub-class instance
that has become the new state for the <paramref name="context" />
object.</param>

<param name="context">The <typeparamref name="FinalContext" /> sub-class
instance that just entered the <paramref name="sender" /> state.</param>

<example>TODO.</example>

<typeparam name="FinalContext">The <see cref="IStateContext {FinalContext,
BaseState}" /> implementation that represents the associated actual state
context class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="ReportingAbstractState
{FinalContext, BaseState}" /> sub-class that is the polymorphic base class
defining the set of available states for the associated <typeparamref
name="FinalContext" /> type; all classes derived from this base state class are
suitable state classes for this <typeparamref name="FinalContext" />
type.</typeparam>

<seealso cref="IStateContext {FinalContext, BaseState}" />

<seealso cref="StateContext {FinalContext, BaseState}" />

<seealso cref="AbstractState {FinalContext, BaseState}" />

<seealso cref="ReportingAbstractState {FinalContext, BaseState}" />

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="StateExitHandler {FinalContext, BaseState}" />
*/
//=============================================================================

    public delegate void StateEnterHandler <FinalContext, BaseState> (BaseState
    sender, FinalContext context)
    where FinalContext:
        IStateContext <FinalContext, BaseState>
    where BaseState:
        ReportingAbstractState <FinalContext, BaseState>
    ;

//=============================================================================
/**
<summary>Generic state exit event handler delegate.</summary>

<remarks>Delegated function for handling state exit events raised by a
<typeparamref name="BaseState" /> sub-class instance.

<para>The state exit event is raised and processed as part of a state change
operation on a <typeparamref name="FinalContext" /> object.</para>

<para>For more detailed information on the order in which events and associated
virtual functions are called, refer to <see cref="IStateContext {FinalContext,
BaseState}" />.</para></remarks>

<param name="sender">The <typeparamref name="BaseState" /> sub-class instance
that is no longer the state for the <paramref name="context" /> object.</param>

<param name="context">The <typeparamref name="FinalContext" /> sub-class
instance that just exited the <paramref name="sender" /> state.</param>

<example>TODO.</example>

<typeparam name="FinalContext">The <see cref="IStateContext {FinalContext,
BaseState}" /> implementation that represents the associated actual state
context class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="ReportingAbstractState
{FinalContext, BaseState}" /> sub-class that is the polymorphic base class
defining the set of available states for the associated <typeparamref
name="FinalContext" /> type; all classes derived from this base state class are
suitable state classes for this <typeparamref name="FinalContext" />
type.</typeparam>

<seealso cref="IStateContext {FinalContext, BaseState}" />

<seealso cref="StateContext {FinalContext, BaseState}" />

<seealso cref="AbstractState {FinalContext, BaseState}" />

<seealso cref="ReportingAbstractState {FinalContext, BaseState}" />

<seealso cref="StateChangedHandler {FinalContext, BaseState}" />

<seealso cref="StateEnterHandler {FinalContext, BaseState}" />
*/
//=============================================================================

    public delegate void StateExitHandler <FinalContext, BaseState> (BaseState
    sender, FinalContext context)
    where FinalContext:
        IStateContext <FinalContext, BaseState>
    where BaseState:
        ReportingAbstractState <FinalContext, BaseState>
    ;

//=============================================================================
/**
<summary>Generic abstract base class for reporting state objects.</summary>

<remarks>A "reporting state object" extends the capabilites of the <see
cref="AbstractState {FinalContext, BaseState}" /> base class to provide state
<see cref="Enter" /> and <see cref="Exit" /> events.</remarks>

<typeparam name="FinalContext">The <see cref="IStateContext {FinalContext,
BaseState}" /> implementation that represents the associated actual state
context class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The <see cref="ReportingAbstractState
{FinalContext, BaseState}" /> sub-class that is the polymorphic base class
defining the set of available states for the associated <typeparamref
name="FinalContext" /> type; all classes derived from this base state class are
suitable state classes for this <typeparamref name="FinalContext" />
type.</typeparam>

<seealso cref="IStateContext {FinalContext, BaseState}" />

<seealso cref="AbstractState {FinalContext, BaseState}" />

<seealso cref="StateContext {FinalContext, BaseState}" />
*/
//=============================================================================

    public abstract class ReportingAbstractState <FinalContext, BaseState>:
        AbstractState <FinalContext, BaseState>
    where FinalContext:
        IStateContext <FinalContext, BaseState>
    where BaseState:
        ReportingAbstractState <FinalContext, BaseState>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State enter event.</summary>

<remarks>Event signalling that this object has become the state for the
associated <typeparamref name="FinalContext" /> object.

<para>If your logic needs to determine when a <typeparamref name="FinalContext"
/> object changes state, rather than a change to a specific state, then you are
recommended to subscribe to the <typeparamref name="FinalContext" /> object's
<see cref="IStateContext {FinalContext, BaseState}.StateChanged" />
event.</para>

<para>This event is also raised (if applicable) when assigned as the
<typeparamref name="FinalContext" /> object's initial state.</para></remarks>

<example>TODO.</example>

<seealso cref="StateEnterHandler {FinalContext, BaseState}" />

<seealso cref="IStateContext {FinalContext, BaseState}.StateChanged" />

<seealso cref="Exit" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public event StateEnterHandler <FinalContext, BaseState> Enter;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>State enter event.</summary>

<remarks>Event signalling that this object is no longer the state for the
associated <typeparamref name="FinalContext" /> object.

<para>If your logic needs to determine when a <typeparamref name="FinalContext"
/> object changes state, rather than leaves a specific state, then you are
recommended to subscribe to the <typeparamref name="FinalContext" /> object's
<see cref="IStateContext {FinalContext, BaseState}.StateChanged" />
event.</para></remarks>

<example>TODO.</example>

<seealso cref="StateExitHandler {FinalContext, BaseState}" />

<seealso cref="IStateContext {FinalContext, BaseState}.StateChanged" />

<seealso cref="StateContext {FinalContext, BaseState}.StateChanged" />

<seealso cref="Enter" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public event StateExitHandler <FinalContext, BaseState> Exit;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Raise the state enter event.</summary>

<remarks>This function is called from the <typeparamref name="FinalContext" />
instance when it enters this state.</remarks>

<param>The <typeparamref name="FinalContext" /> instance that is entering this
state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal override void RaiseEnter (FinalContext context)
        {

/*
Implement the base class's functionality.
*/

            base.RaiseEnter (context);

/*
If there are any subscribed delegates, then invoke them now.
*/

            if (Enter != null) {
                Enter ((BaseState) this, context);
            }
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

        internal override void RaiseExit (FinalContext context)
        {

/*
Implement the base class's functionality.
*/

            base.RaiseExit (context);

/*
If there are any subscribed delegates, then invoke them now.
*/

            if (Exit != null) {
                Exit ((BaseState) this, context);
            }
        }
    }
}
