/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

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

Java source file for the CounterTest class, and associated elements, that are
integral members of the org.facsim.Facsimile.CommonTest package.
===============================================================================
*/


package org.facsim.Facsimile.CommonTest;

import org.facsim.Facsimile.Common.*;
import static org.junit.Assert.*;
import org.junit.Test;

//=============================================================================
/**
 <p>Test fixture for the {@link Counter} class.</p>
 */
//=============================================================================

public class CounterTest
{

/**
Array of test counters.  The counters stored in this array are as follows:

<ol>
    <li>A default-constructed counter, regarded as being unconstrained
    (although it does have a maximum of Integer.MAX_VALUE).</li>
    <li>A counter with 0 as its maximum value; legal but unusable.</li>
    <li>A counter with 1 as its maximum value; always either full or
    empty.</li>
    <li>A counter with a very large maximum value.</li>
</ol>
*/

    private Counter [] testCounters;

/**
<p>Array of expected counter capacities.</p>
*/

    private int [] counterCapacities;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Check counter is empty.</p>

<p>General logic for testing that a counter is empty.</p>

@param counter The counter to be tested.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void verifyCounterEmpty (Counter counter)
    {
        assertTrue (counter.isEmpty ());
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Check counter maximum capacity.</p>

<p>General logic for testing a counter's maximum capacity.</p>

@param counter The counter to be tested.

@param capacity The expected maximum capacity of the counter.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void verifyCounterCapacity (Counter counter, int capacity)
    {
        assertEquals (new Integer (counter.getMaximumCapacity ()), new Integer
        (capacity));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify counter empty conditions.</p>

<p>Check that counter throws {@link CounterDecrementException} when decremented
when empty.</p>

@param counter The counter to be tested.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void verifyExceptionOnEmpty (Counter counter)
    {

/*
Initialise the flag indicating whether the test succeeded or not.  Assume that
is has failed until we discover otherwise.
*/

        assert counter.isEmpty ();
        boolean succeeded = false;

/*
Attempt to decrement the counter.
*/

        try
        {
            counter.decrement ();
        }

/*
If we caught the counter decrement exception, then everything is OK.  The test
succeeded - this is what should happen when we decrement an empty counter.
*/

        catch (CounterDecrementException e)
        {
            succeeded = true;
        }

/*
Verify that the test succeeded.
*/

        assertTrue (succeeded);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Increment counter until full.</p>

<p>General logic for testing that a counter can be incremented until full.</p>

@param counter The counter to be tested.

@return An int storing the number of times that the counter was incremented.
This can then be tested against the counter's expected maximum capacity.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static int incrementUntilFull (Counter counter)
    {

/*
Keep incrementing the counter until it's full.  Record how many times we were
able to increment the counter and return this value to the caller.

If any exceptions get thrown, then that's considered a test failure.
*/

        assert counter.isEmpty ();
        int incrementCount = 0;
        while (!counter.isFull ())
        {
            counter.increment ();
            ++incrementCount;
        }
        return incrementCount;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify counter full conditions.</p>

<p>Check that counter throws {@link CounterIncrementException} when incremented
when full.</p>

@param counter The counter to be tested.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void verifyExceptionOnFull (Counter counter)
    {

/*
Initialise the flag indicating whether the test succeeded or not.  Assume that
is has failed until we discover otherwise.
*/

        assert counter.isFull ();
        boolean succeeded = false;

/*
Attempt to increment the counter.
*/

        try
        {
            counter.increment ();
        }

/*
If we caught the counter increment exception, then everything is OK.  The test
succeeded - this is what should happen when we increment a full counter.
*/

        catch (CounterIncrementException e)
        {
            succeeded = true;
        }

/*
Verify that the test succeeded.
*/

        assertTrue (succeeded);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Decrement counter by fixed amount.</p>

<p>General logic for testing that a counter can be decremented by a count
equal to the number of times it was incremented earlier.</p>

@param counter The counter to be tested.

@param count An int identifying how many times the Counter should be
decremented.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void decrementByCount (Counter counter, int count)
    {

/*
Keep decrementing the counter until it's full.  Record how many times we were
able to increment the counter and return this value to the caller.

If any exceptions get thrown, then that's considered a test failure.
*/

        assert counter.isFull ();
        int decrementCount = count;
        while (decrementCount > 0)
        {
            counter.decrement ();
            --decrementCount;
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Constructor.</p>

<p>Initialise common test data.  Any exceptions thrown will be treated as a
test failure.</p>

@throws java.lang.Exception
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public CounterTest ()
    throws Exception
    {

/*
Initialise the array of expected counter capacities.
*/

        this.counterCapacities = new int []
        {
            Integer.MAX_VALUE,
            0,
            1,
            Integer.MAX_VALUE,
        };

/*
Create the base counters.
*/

        this.testCounters = new Counter []
        {
            new Counter (),
            new Counter (this.counterCapacities [1]),
            new Counter (this.counterCapacities [2]),
            new Counter (this.counterCapacities [3]),
        };
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal counter construction, #1.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IllegalIntArgumentException.class)
    public void invalidConstruction1 ()
    {
        new Counter (-1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal counter construction, #2.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IllegalIntArgumentException.class)
    public void invalidConstruction2 ()
    {
        new Counter (Integer.MIN_VALUE);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests all aspects of defined counter operation.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void counterOperation ()
    {

/*
Repeat for each test counter.
*/

        for (int i = 0; i < this.testCounters.length; ++i) {

/*
Check that the counter is empty initially.
*/

            verifyCounterEmpty (this.testCounters [i]);

/*
Check that the counter has the correct maximum capacity.
*/

            verifyCounterCapacity (this.testCounters [i],
            this.counterCapacities [i]);

/*
Verify that we get counter decrement exceptions if we attempt to decrement it
here.
*/

            verifyExceptionOnEmpty (this.testCounters [i]);

/*
Increment the counter until is becomes full.  Record the number of increment
operations and verify against the counter capacity - they ought to be the same.
*/

            int numIncrements = incrementUntilFull (this.testCounters [i]);
            verifyCounterCapacity (this.testCounters [i], numIncrements);

/*
Check that the counter throws the CounterIncrementException if another
increment operation is attempted.
*/

            verifyExceptionOnFull (this.testCounters [i]);

/*
Now verify that we can decrement the counter by the same number of times that we
incremented it earlier.  This should not throw any exceptions.
*/

            decrementByCount (this.testCounters [i], numIncrements);

/*
Check that we get a CounterDecrementException if a decrement operation is
attempted on an empty counter.  This will complete verification of the decrement
operation.
*/

            verifyExceptionOnEmpty (this.testCounters [i]);
        }
    }
}
