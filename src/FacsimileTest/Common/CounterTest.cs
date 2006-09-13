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
executed by the NUnit unit testing framework.

<para>For further information on NUnit, please go to
http://www.nunit.org/</para></remarks>
*/
//=============================================================================

    [NUnit.Framework.TestFixture]
    public sealed class CounterTest {

/**
<summary>"Unconstrained" counter.</summary>

<remarks>This is a counter that has an unspecified capacity (which is actually
set to the largest value supported by the underlying storage type).</remarks>
*/

	private Facsimile.Common.Counter unconstrainedCounter;

/**
<summary>"Unusable" counter.</summary>

<remarks>This is a counter that cannot be incremented or decremented because it
has zero capacity; it is simultaneously full and empty.</remarks>
*/

	private Facsimile.Common.Counter unusableCounter;

/**
<summary>"Unusuable" counter maximum capacity.</summary>
*/

	private const int UNUSABLE_MAX = 0;

/**
<summary>"Flag" counter.</summary>

<remarks>This is a counter with a capacity of 1; it is either full or empty
with no in-between states.</remarks>
*/

	private Facsimile.Common.Counter flagCounter;

/**
<summary>"Flag" counter maximum capacity.</summary>
*/

	private const int FLAG_MAX = 1;

/**
<summary>"Maximum" counter.</summary>

<remarks>This is a counter with the largest possible (positive) integer value.
It ought to be exactly the same as the <see cref="unconstrainedCounter" /> but
we cannot rely on that.  (We do not test the latter as it is actually not that
important - the default unconstrained counter could have any sufficiently large
value.)</remarks>
*/

	private Facsimile.Common.Counter maximumCounter;

/**
<summary>"Maximum" counter maximum capacity.</summary>
*/

	private const int MAXIMUM_MAX = int.MaxValue;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that counter has expected capacity.</summary>

<remarks>This function tests that <paramref name="counter" /> has the maximum
capacity specified by <paramref name="expectedCapacity" />.  If it does not,
the test fails.</remarks>

<param name="counter">A <see cref="Facsimile.Common.Counter" /> object whose
maximum capacity needs to be validated.</param>
<param name="expectedCapacity">The expected maximum capacity of the
counter.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static void DoCounterMaxCapacityTest (Facsimile.Common.Counter
	counter, int expectedCapacity) {

/*
Check that the maximum capacity is the expected value.
*/

	    NUnit.Framework.Assert.AreEqual (expectedCapacity,
	    counter.MaximumCapacity, "Counter does not have expected maximum " +
	    "capacity.");
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test counter is empty.</summary>

<remarks>This function tests that <paramref name="counter" /> is empty, and is
used as a component of other tests in this fixture.</remarks>

<param name="counter">A <see cref="Facsimile.Common.Counter" /> object that is
expected to be empty.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static void DoCounterEmptyTest (Facsimile.Common.Counter
	counter) {

/*
Verify that the specified counter is empty.
*/

	    NUnit.Framework.Assert.IsTrue (counter.IsEmpty,
	    "Counter not empty.");
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test counter is full.</summary>

<remarks>This function tests that <paramref name="counter" /> is full, and is
used as a component of other tests in this fixture.</remarks>

<param name="counter">A <see cref="Facsimile.Common.Counter" /> object that is
expected to be full.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static void DoCounterFullTest (Facsimile.Common.Counter
	counter) {

/*
Verify that the specified counter is full.
*/

	    NUnit.Framework.Assert.IsTrue (counter.IsFull, "Counter not " +
	    "full.");
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that empty counter fails decrement operation.</summary>

<remarks>This function checks that the <paramref name="counter" />, which must
be empty, fails a decrement operation correctly.</remarks>

<param name="counter">A <see cref="Facsimile.Common.Counter" /> object that is
expected to be empty when the function is called.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static void DoCounterFailDecrementTest
	(Facsimile.Common.Counter counter) {

/*
Verify that the counter is empty.
*/

	    DoCounterEmptyTest (counter);

/*
Verify that we see the ArgumentException when we decrement the counter.
*/

	    bool sawException = false;
	    try {
		counter.Decrement ();
	    }
	    catch (System.OverflowException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException, "Did not see counter "
	    + "decrement overflow exception");

/*
Verify that we did not actually change the counter's value.  The exception
should leave the counter as it was before we attempted to decrement it.
*/

	    DoCounterEmptyTest (counter);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that full counter fails increment operation.</summary>

<remarks>This function checks that the <paramref name="counter" />, which must
be full, fails an increment operation correctly.</remarks>

<param name="counter">A <see cref="Facsimile.Common.Counter" /> object that is
expected to be full when the function is called.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static void DoCounterFailIncrementTest
	(Facsimile.Common.Counter counter) {

/*
Verify that the counter is full.
*/

	    DoCounterFullTest (counter);

/*
Verify that we see the ArgumentException when we increment the counter.
*/

	    bool sawException = false;
	    try {
		counter.Increment ();
	    }
	    catch (System.OverflowException) {
		sawException = true;
	    }
	    NUnit.Framework.Assert.IsTrue (sawException, "Did not see counter "
	    + "increment overflow exception");

/*
Verify that we did not actually change the counter's value.  The exception
should leave the counter as it was before we attempted to increment it.
*/

	    DoCounterFullTest (counter);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test counter increment and decrement operations.</summary>

<remarks>This test attempts to verify that the increment and decrement
operations are functioning OK.  It also verifies that counters are initialised
to be empty.</remarks>

<param name="counter">A <see cref="Facsimile.Common.Counter" /> object that is
to be incremented and decremented.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static void DoCounterIncrementDecrementTest
	(Facsimile.Common.Counter counter) {

/*
Check that the counter is empty initally.
*/

	    DoCounterEmptyTest (counter);

/*
Now increment the counter all the way up to the maximum (this can take some time
on the large capacity counters).
*/

	    while (!counter.IsFull) {
		counter.Increment ();
	    }

/*
Now check that the counter is full and will fail the next increment operation.
This also checks that the counter is still full after the resulting exception
has been handled.
*/

	    DoCounterFailIncrementTest (counter);

/*
OK.  Now decrement the counter all the way back down to zero (again, this can
take some time for large capacity counters).
*/

	    while (!counter.IsEmpty) {
		counter.Decrement ();
	    }

/*
Now check that the counter is empty and will fail the next decrement operation.
This also checks that the counter is still empty after the resulting exception
has been handled.
*/

	    DoCounterFailDecrementTest (counter);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>All classes identified by the <see
cref="NUnit.Framework.TestFixtureAttribute" /> must be default
constructible.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public CounterTest () {

/*
Create the counters.  None of these should throw exceptions.
*/

	    unconstrainedCounter = new Facsimile.Common.Counter ();
	    unusableCounter = new Facsimile.Common.Counter (UNUSABLE_MAX);
	    flagCounter = new Facsimile.Common.Counter (FLAG_MAX);
	    maximumCounter = new Facsimile.Common.Counter (MAXIMUM_MAX);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test for construction argument out of range exception #1.</summary>

<remarks>Tests that a counter with a capacity of -1 causes the <see
cref="System.ArgumentOutOfRangeException" /> to be thrown.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	[NUnit.Framework.ExpectedException (typeof
	(System.ArgumentOutOfRangeException))]
	public void TestCounterConstruction1 () {
	    new Facsimile.Common.Counter (-1);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test for construction argument out of range exception #2.</summary>

<remarks>Tests that a counter with the largest negative value (or, in the
parlance of the Microsoft .NET documentation, the "smallest" value) for its
maximum capacity causes the <see cref="System.ArgumentOutOfRangeException" /> to
be thrown.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	[NUnit.Framework.ExpectedException (typeof
	(System.ArgumentOutOfRangeException))]
	public void TestCounterConstruction2 () {
	    new Facsimile.Common.Counter (int.MinValue);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify the maximum capacity on all counters.</summary>

<remarks>Tests that all counters have the correct maximum capacity.  This is
required as much for the tests that follow as to validate that the counters
have been constructed OK.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	public void TestCounterMaximumCapacity () {

/*
Validate that all counters have stored the correct maximum value.  The
unconstrained counter should have the same maximum capacity as the maximum
counter.
*/

	    DoCounterMaxCapacityTest (unconstrainedCounter, MAXIMUM_MAX);
	    DoCounterMaxCapacityTest (unusableCounter, UNUSABLE_MAX);
	    DoCounterMaxCapacityTest (flagCounter, FLAG_MAX);
	    DoCounterMaxCapacityTest (maximumCounter, MAXIMUM_MAX);
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that counters can be incremented and decremented
correctly.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	[NUnit.Framework.Test]
	public void TestCounterIncrementDecrement () {

/*
If the unconstrained and maximum counters have the same capacity, then we do
not need to check the increment/decrement operations on one of them (we'll pick
the unconstrained counter).  All we need to do is to check that this dropped
counter is initialised to empty.  This will save significant (run) time when we
execute the increment/decrement tests.
*/

	    NUnit.Framework.Assert.AreEqual (maximumCounter.MaximumCapacity,
	    unconstrainedCounter.MaximumCapacity);
	    DoCounterEmptyTest (unconstrainedCounter);

/*
Verify that all counters can be increment and decremented between their upper
and lower limits.  These tests may take some time for the large capacity
counters.
*/

	    DoCounterIncrementDecrementTest (unusableCounter);
	    DoCounterIncrementDecrementTest (flagCounter);
	    DoCounterIncrementDecrementTest (maximumCounter);
	}
    }
}
