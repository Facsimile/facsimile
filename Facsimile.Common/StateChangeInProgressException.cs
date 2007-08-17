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

C# source file for the StateChangeInProgressException class, and associated
elements, that are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>State change already in progress exception.</summary>

<remarks>This exception is thrown if an attempt is made to change the state of
a <see cref="StateContext {FinalContext, BaseState}" />-derived instance whilst
a previous state change operation is still underway.

<para>This exception typically arises when the attempt to change state occurs
within state change event handling logic.</para></remarks>
*/
//=============================================================================

    public sealed class StateChangeInProgressException <FinalContext,
    BaseState>:
        OperationIncompleteException
    where FinalContext:
	StateContext <FinalContext, BaseState>
    where BaseState:
	AbstractState <FinalContext, BaseState>
    {

/**
<summary>Object array.</summary>

<remarks>This array is initialised by the constructor to contain the following
values:

<list type="number">
    <item>
        <description>The context class's type full name.</description>
    </item>
    <item>
        <description>The attempted new state's type full name.</description>
    </item>
</list></remarks>
*/

        private readonly System.Object [] stateData;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor.</summary>

<remarks>Record the context and the new states names for subsequent error
reporting.</remarks>

<param name="context">The <typeparamref name="FinalContext" /> instance whose
state is currently being changed.</param>

<param name="newState">The <typeparamref name="BaseState" /> instance that was
forbidden from becoming the <paramref name="context" />'s new state.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal StateChangeInProgressException (FinalContext context,
        BaseState newState)
        {

/*
Argument integrity assertions.
*/

            System.Diagnostics.Debug.Assert (context != null);
            System.Diagnostics.Debug.Assert (newState != null);

/*
Store the appropriate information for later use.
*/

            stateData = new System.Object []
            {
                context.GetType ().FullName,
                newState.GetType ().FullName,
            };
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Explain why exception was thrown.</summary>

<remarks>Reports detailed information that allows a user to identify why the
exception was thrown.</remarks>

<value>A <see cref="System.String" /> containing the exception's
explanation.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public override string Message
        {

/*
Retrieve the compound message, format it and return it to the caller.
*/

            get
            {
                return Resource.Format ("stateChangeInProgress", stateData);
            }
        }
    }
}
