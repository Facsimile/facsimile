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

Java source file for the Counter class, and associated elements, that are
integral members of the org.facsim.Facsimile.Common package.
===============================================================================
*/

package org.facsim.Facsimile.Common;

//=============================================================================
/**
<p>Maintains a non-negative, integer count.</p>

<p>Counters cannot be modified except via the unit {@link #increment()} and
{@link #decrement()} functions and can be only be examined to determine if they
are {@linkplain #isFull() full} or {@linkplain #isEmpty() empty}.  A comparison
of one counter to another is permitted, but counters cannot be compared to, or
converted to, values in the underlying storage type.  Counters cannot be
assigned a value.  All counters are initialised to be empty (i.e. their initial
count is 0).</p>

<p>These restrictions help prevent accidental corruption of counts, and aid the
development of robust code by preventing the comparison of counts to unrelated
quantities.</p>

<p>Exceptions are thrown if attempts are made to decrement a zero count, or
increment a count that is already at its maximum.</p>
*/
//=============================================================================

public final class Counter
{

/**
<p>Limit, or maximum capacity, of the counter's value.</p>

<p>This value cannot be negative.</p>
*/

    private final int limit;

/**
<p>Current count value.</p>

<p>This value is constrained to be between 0 and the value held by the
{@link #limit} data member.</p>
*/

    private int count;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Default constructor.</p>

<p>Constructs an <em>unconstrained</em> counter, in which the counter's limit
is the largest value supported by the underlying storage type.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Counter ()
    {
        this (Integer.MAX_VALUE);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Constrained counter constructor.</p>

<p>Constructs a <em>constrained</em> counter that restricts the counter's limit
to the value identified.</p>

@param maximumCapacity The highest value that the new counter will be allowed
to reach.  This value cannot be less than zero.  If this value is zero,
then the counter will be <em>unusable</em> (exceptions will be thrown whenever
any modification (i.e {@link #increment() increment} or {@link #decrement()
decrement} is attempted on the counter).

@throws IllegalArgumentException Thrown if maximumCapacity is less than zero.
Valid values are zero or higher.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Counter (int maximumCapacity)
    throws IllegalArgumentException
    {

/*
Validate the supplied maximum capacity.
*/

        if (maximumCapacity < 0)
        {
            throw new IllegalIntArgumentException
            ("maximumCapacity", 0, Integer.MAX_VALUE, //$NON-NLS-1$
            maximumCapacity);
        }

/*
Member initialisation.
*/

        this.limit = maximumCapacity;
        this.count = 0;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve Counter's maximum capacity.</p>

<p>The capacity is guaranteed to be ≥ 0.</p>

@return An int value storing the maximum capacity of the counter.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final int getMaximumCapacity ()
    {
        assert this.limit >= 0;
        return this.limit;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Is counter empty?</p>

<p>The counter is considered empty when the number of increment and decrement
operations are in balance, such that the current count is 0.</p>

@return A boolean value that is true if the counter is empty or false
otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final boolean isEmpty ()
    {
        assert this.count >= 0;
        return (this.count == 0);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Is counter full?</p>

<p>The counter is considered full when the difference between the number of
increment and decrement operations has reached the allowed limit, such that the
current count is at its maximum valuue.</p>

@return A boolean value that is true if the counter is full or false otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final boolean isFull ()
    {
        assert this.count <= this.limit;
        return (this.count == this.limit);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Increment counter.</p>

<p>Increments the counter's currently tracked count.</p>

@throws OverflowException Thrown if the counter is already at its maximum value
when the increment attempt is made.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final void increment ()
    throws OverflowException
    {

/*
If we're already at the maximum count, then throw the overflow exception.
*/

        if (isFull ())
        {
            throw new CounterIncrementException (this.limit);
        }

/*
Increment the count.
*/

        ++this.count;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Decrement counter.</p>

<p>Decrements the counter's currently tracked count.</p>

<p>Avoid placing calls to {@link #decrement()} within finalisation code.
Finalisation is peformed by the garbage collector at unpredictable intervals
and will cause your code to execute erratically.</p>

@throws OverflowException Thrown if the counter is already at its minimum value
when the increment attempt is made.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final void decrement ()
    throws OverflowException
    {

/*
If we're already at zero, then throw the overflow exception.
*/

        if (isEmpty ())
        {
            throw new CounterDecrementException ();
        }

/*
Decrement the count.
*/

        --this.count;
    }
}
