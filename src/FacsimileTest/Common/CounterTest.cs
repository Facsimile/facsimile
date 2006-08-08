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

C# source file for the Counter test suite objects, which are integral members
of the FacsimileTest.Common namespace.
===============================================================================
*/

namespace FacsimileTest.Common {

//=============================================================================
/**
<summary>Test fixture for the <see cref="Facsimile.Common.Counter" />
class.</summary>

<remarks>This class consists of a set of unit tests for the <see
cref="Facsimile.Common.Counter" /> class.  The unit tests themselves are
executed by the NUnit unit testing framework.</remarks>
*/
//=============================================================================

    [NUnit.Framework.TestFixture] public sealed class CounterTest {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tests to verify that counters are initialised OK.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test] public void CounterInitialisation () {

/*
Create a default (unconstrained) counter and test that it is empty and not
full.
*/

	    Facsimile.Common.Counter counterUnconstrained = new
	    Facsimile.Common.Counter ();
	    NUnit.Framework.Assert.IsTrue (counterUnconstrained.IsEmpty);
	    NUnit.Framework.Assert.IsFalse (counterUnconstrained.IsFull);

/*
Create a counter with capacity zero.  This is an unusable counter that should
be both empty and full simultaneously.  We shouldn't get an exception here
either.
*/

	    Facsimile.Common.Counter counterUnusable = new
	    Facsimile.Common.Counter (0);
	    NUnit.Framework.Assert.IsTrue (counterUnusable.IsEmpty);
	    NUnit.Framework.Assert.IsTrue (counterUnusable.IsFull);

/*
Create a counter with capacity one.  This should be empty but not full.
*/

	    Facsimile.Common.Counter counterOne = new Facsimile.Common.Counter
	    (1);
	    NUnit.Framework.Assert.IsTrue (counterOne.IsEmpty);
	    NUnit.Framework.Assert.IsFalse (counterOne.IsFull);

/*
Create a counter with capacity int.MaxValue.  This should be empty but not
full.  (In fact, should be the same as counterUnconstrained.)
*/

	    Facsimile.Common.Counter counterMax = new Facsimile.Common.Counter
	    (int.MaxValue);
	    NUnit.Framework.Assert.IsTrue (counterMax.IsEmpty);
	    NUnit.Framework.Assert.IsFalse (counterMax.IsFull);

/*
Create a counter with a capacity of -1.  This should cause the
System.ArgumentException to be thrown.
*/

	    bool sawException = false;
	    try {
		new Facsimile.Common.Counter (-1);
	    }
	    catch (System.ArgumentException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException);

/*
Just for fun, see what happens if we use a value greater than the allowed
maximum for the type.  The answer is that we get a compile time overflow
exception - nice to know!
*/

/*	    sawException = false;
	    try {
		new Facsimile.Common.Counter (int.MaxValue + 1);
	    }
	    catch (System.ArgumentException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException);*/
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tests to verify that increment &amp; decrement work asexpected.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test] public void CounterModification () {

/*
Create a default (unconstrained) counter and test that it cannot be
decremented and can be incremented.

TODO: Test that it can be incremented until the limit is reached!
*/

	    Facsimile.Common.Counter counterUnconstrained = new
	    Facsimile.Common.Counter ();
	    bool sawException = false;
	    try {
		counterUnconstrained.Decrement ();
	    }
	    catch (System.OverflowException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException);
	    NUnit.Framework.Assert.IsTrue (counterUnconstrained.IsEmpty);
	    counterUnconstrained.Increment ();

/*
Create a counter with capacity zero.  This is an unusable counter that should
be both empty and full simultaneously.  We ought to get an exception by both
incrementing and decrementing it...
*/

	    Facsimile.Common.Counter counterUnusable = new
	    Facsimile.Common.Counter (0);
	    sawException = false;
	    try {
		counterUnusable.Decrement ();
	    }
	    catch (System.OverflowException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException);
	    NUnit.Framework.Assert.IsTrue (counterUnusable.IsEmpty);
	    sawException = false;
	    try {
		counterUnusable.Increment ();
	    }
	    catch (System.OverflowException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException);

/*
Create a counter with capacity one.  We ought not to be able to decrement it,
but should be able to increment it - the first time.
*/

	    Facsimile.Common.Counter counterOne = new Facsimile.Common.Counter
	    (1);
	    sawException = false;
	    try {
		counterOne.Decrement ();
	    }
	    catch (System.OverflowException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException);
	    NUnit.Framework.Assert.IsTrue (counterOne.IsEmpty);
	    counterOne.Increment ();
	    sawException = false;
	    try {
		counterOne.Increment ();
	    }
	    catch (System.OverflowException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException);

// TODO: Add more tests...
	}
    }
}
