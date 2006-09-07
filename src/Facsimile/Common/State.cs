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
information.</para></remarks>

<example>For an example of how to use this class, refer to the <see
cref="StateContext {FinalContext, BaseContext}" /> class
documentation.</example>

<typeparam name="FinalContext">The <see cref="StateContext {FinalContext,
BaseState}" />-derived type that represents the associated actual state context
class (or the polymorphic base class of the actual state context
class).</typeparam>

<typeparam name="BaseState">The State-derived base class defining the set of
available states for the associated <typeparamref name="FinalContext"> type;
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
<summary>UP TO HERE.</summary>

<remarks>Perform custom actions when the specified state context object changes
to this state.

<para>Derived classes should override this function if necessary; the default
version of this function does nothing.

<para>Overrides of this function should avoid throwing exceptions - any
exceptions thrown will likely terminate the application.  If this state is
inappropriate for the state context, override the <see
cref="CanTransitionToState" /> function to bar transitions to this
state.</para></remarks>

<param name="stateContext">The <typeparamref name="FinalContext" /> object
whose state has just been changed to this state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected virtual void ImplementStateChange (FinalContext stateContext)
	{
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Implement state change.</summary>

<remarks>Perform custom actions when the specified state context object changes
to this state.

<para>Derived classes should override this function if necessary; the default
version of this function does nothing.

<para>Overrides of this function should avoid throwing exceptions - any
exceptions thrown will likely terminate the application.  If this state is
inappropriate for the state context, override the <see
cref="CanTransitionToState" /> function to bar transitions to this
state.</para></remarks>

<param name="stateContext">The <typeparamref name="FinalContext" /> object
whose state has just been changed to this state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected virtual void ImplementStateChange (FinalContext stateContext)
	{
	}
    }
}
