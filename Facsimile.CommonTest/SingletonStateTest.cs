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

C# source file for the SingletonStateTest class, and associated elements, that
are integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>NUnit test fixture for the <see cref="SingletonStateContext
{SingletonBase, FinalContext, BaseState}" /> class.</summary>
*/
//=============================================================================

    [TestFixture]
    public sealed class SingletonStateTest:
	System.Object
    {
UP TO HERE
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
