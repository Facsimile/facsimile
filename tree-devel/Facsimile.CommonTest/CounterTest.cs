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

C# source file for the CounterTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>NUnit test fixture for the <see cref="Counter" /> class.</summary>
*/
//=============================================================================

    [TestFixture]
    public sealed class CounterTest
    {

/**
<summary>Array of test counters.</summary>

<remarks>The counters stored in this array are as follows:

<list type="number">
    <item>
        <description>A default-constructed counter, regarded as being
        unconstrained (although it does have a maximum of <see
        cref="System.Int32.MaxValue" />).</description>
    </item>
    <item>
        <description>A counter whose maximum value is 0; legal but
        unusable.</description>
    </item>
    <item>
        <description>A counter whose maximum value is 1; always either full or
        empty.</description>
    </item>
    <item>
        <description>A counter with a very large maximum value.</description>
    </item>
</list></remarks>
*/

        private Counter [] testCounters;

/**
<summary>Array of expected counter capacities.</summary>

<remarks>Each element of this array is the expected capacity of the
corresponding element of the <see cref="testCounters" /> array.</remarks>
*/

        private int [] counterCapacities;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that counter is empty.</summary>

<remarks>General logic for testing that a counter is empty.</remarks>

<param name="counter">The <see cref="Counter" /> instance to be tested.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void VerifyCounterEmpty (Counter counter)
        {
            Assert.IsTrue (counter.IsEmpty);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify counter maximum capacity.</summary>

<remarks>General logic for testing that a counter's maximum capacity is as
expected.</remarks>

<param name="counter">The <see cref="Counter" /> instance to be tested.</param>

<param name="capacity">The expected maximum capacity of the counter.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void VerifyCounterCapacity (Counter counter, int
        capacity)
        {
            Assert.AreEqual (counter.MaximumCapacity, capacity);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify counter empty conditions.</summary>

<remarks>Check that an empty counter throws <see
cref="CounterDecrementException"/> when decremented.</remarks>

<param name="counter">The <see cref="Counter" /> instance to be tested.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void VerifyExceptionOnEmpty (Counter counter)
        {

/*
Initialise the flag indicating whether the test succeeded or not.  Assume that
it has failed until we discover otherwise.
*/

            System.Diagnostics.Debug.Assert (counter.IsEmpty);
            bool succeeded = false;

/*
Attempt to decrement the counter.
*/

            try
            {
                counter.Decrement ();
            }

/*
If we caught the counter decrement exception, then everything is OK.  The test
succeeded - this is what should happen when we decrement an empty counter.
*/

            catch (CounterDecrementException)
            {
                succeeded = true;
            }

/*
Verify that the test succeeded.
*/

            Assert.IsTrue (succeeded);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Increment counter until full.</summary>

<remarks>General logic for testing that a counter can be incremented until
full.</remarks>

<param name="counter">The <see cref="Counter" /> instance to be tested.</param>

<returns>A <see cref="System.Int32"/> storing the number of times that the
<paramref name="counter" /> was incremented; this can then be tested against
the expected maximum capacity.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static int IncrementUntilFull (Counter counter)
        {

/*
Keep incrementing the counter until it's full.  Record how many times we were
able to increment the counter and return this value to the caller.

If any exceptions get thrown, then that's considered a test failure.
*/

            System.Diagnostics.Debug.Assert (counter.IsEmpty);
            int incrementCount = 0;
            while (!counter.IsFull)
            {
                counter.Increment ();
                ++incrementCount;
            }
            return incrementCount;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify counter full conditions.</summary>

<remarks>Check that a full counter throws <see cref="CounterIncrementException"
/> when incremented.</remarks>

<param name="counter">The <see cref="Counter" /> instance to be tested.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void VerifyExceptionOnFull (Counter counter)
        {

/*
Initialise the flag indicating whether the test succeeded or not.  Assume that
is has failed until we discover otherwise.
*/

            System.Diagnostics.Debug.Assert (counter.IsFull);
            bool succeeded = false;

/*
Attempt to increment the counter.
*/

            try
            {
                counter.Increment ();
            }

/*
If we caught the counter increment exception, then everything is OK.  The test
succeeded - this is what should happen when we increment a full counter.
*/

            catch (CounterIncrementException)
            {
                succeeded = true;
            }

/*
Verify that the test succeeded.
*/

            Assert.IsTrue (succeeded);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Decrement counter by fixed amount.</summary>

<remarks>General logic for testing that a counter can be decremented by a count
(equal to the number of times it was incremented earlier).</remarks>

<param name="counter">The <see cref="Counter" /> instance to be tested.</param>

<param name="count">A <see cref="System.Int32" /> identifying how many times
<paramref name="counter" /> should be decremented.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void DecrementByCount (Counter counter, int count)
        {

/*
Keep decrementing the counter until it's full.  Record how many times we were
able to increment the counter and return this value to the caller.

If any exceptions get thrown, then that's considered a test failure.
*/

            System.Diagnostics.Debug.Assert (counter.IsFull);
            int decrementCount = count;
            while (decrementCount > 0)
            {
                counter.Decrement ();
                --decrementCount;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>Initialise common test data.  Any exceptions thrown will be treated as
test failures.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public CounterTest ()
        {

/*
Initialise the array of expected counter capacities.
*/

            counterCapacities = new int []
            {
                int.MaxValue,
                0,
                1,
                int.MaxValue,
            };

/*
Create the base counters.
*/

            testCounters = new Counter []
            {
                new Counter (),
                new Counter (counterCapacities [1]),
                new Counter (counterCapacities [2]),
                new Counter (counterCapacities [3]),
            };
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tests for exception on illegal counter construction, #1.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (ArgumentOutOfRangeException <int>))]
        public void InvalidConstruction1 ()
        {
            new Counter (-1);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tests for exception on illegal counter construction, #2.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (ArgumentOutOfRangeException <int>))]
        public void InvalidConstruction2 ()
        {
            new Counter (int.MinValue);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tests all aspects of defined counter operation.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [Category ("Long")]
        public void CounterOperation ()
        {

/*
Repeat for each test counter.
*/

            for (int i = 0; i < testCounters.Length; ++i)
            {

/*
Check that the counter is empty initially.
*/

                VerifyCounterEmpty (testCounters [i]);

/*
Check that the counter has the correct maximum capacity.
*/

                VerifyCounterCapacity (testCounters [i], counterCapacities
                [i]);

/*
Verify that we get counter decrement exceptions if we attempt to decrement it
here.
*/

                VerifyExceptionOnEmpty (testCounters [i]);

/*
Increment the counter until it becomes full.  Record the number of increment
operations and verify against the counter capacity - they ought to be the same.
*/

                int numIncrements = IncrementUntilFull (testCounters [i]);
                VerifyCounterCapacity (testCounters [i], numIncrements);

/*
Check that the counter throws the CounterIncrementException if another
increment operation is attempted.
*/

                VerifyExceptionOnFull (testCounters [i]);

/*
Now verify that we can decrement the counter by the same number of times that
we incremented it earlier.  This should not throw any exceptions.
*/

                DecrementByCount (testCounters [i], numIncrements);

/*
Check that we get a CounterDecrementException if a decrement operation is
attempted on an empty counter.  This will complete verification of the
decrement operation.
*/

                VerifyExceptionOnEmpty (testCounters [i]);
            }
        }
    }
}
