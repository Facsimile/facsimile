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

C# source file for the Counter class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Maintains a non-negative, integer count.</summary>

<remarks>Counters cannot be modified except via the unit <see cref="Increment
()" /> and <see cref="Decrement ()" /> functions and can be only be examined to
determine if they are <see cref="IsFull">full</see> or <see
cref="IsEmpty">empty</see>.  A comparison of one counter to another is
permitted, but counters cannot be compared to, or converted to, values in the
underlying storage type.  Counters cannot be assigned a value.  All counters
are initialised to be empty (i.e. their initial count is 0).

<para>These restrictions help prevent accidental corruption of counts, and aid
the development of robust code by preventing the comparison of counts to
unrelated quantities.</para>

<para>Exceptions are thrown if attempts are made to decrement a zero count, or
increment a count that is already at its maximum.</para></remarks>
*/
//=============================================================================

    public sealed class Counter:
        System.Object
    {

/**
<summary>Limit, or maximum capacity, of the counter's value.</summary>

<remarks>This value cannot be negative.</remarks>
*/

        private readonly int limit;

/**
<summary>Current count value.</summary>

<remarks>This value is constrained to be between 0 and the value held by <see
cref="limit" /> data member.</remarks>
*/

        private int count;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>Constructs an "unconstrained" counter, in which the counter's limit is
the largest value supported by the underlying storage type.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Counter ():
            this (int.MaxValue)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constrained counter constructor.</summary>

<remarks>Constructs a "constrained" counter that restricts the counter's limit
to the value identified.</remarks>

<param name="maximumCapacity">The highest value that the new counter will be
allowed to reach.  This value cannot be less than zero.  If this value is zero,
then the counter will be "unusable" (exceptions will be thrown whenever any
modification (i.e <see cref="Increment ()">increment</see> or <see
cref="Decrement ()">decrement</see> is attempted on the counter.</param>

<exception cref="System.ArgumentOutOfRangeException">Thrown if <paramref
name="maximumCapacity" /> is less than zero.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Counter (int maximumCapacity)
        {

/*
Validate the supplied maximum capacity.
*/

            if (maximumCapacity < 0)
            {
                throw new ArgumentOutOfRangeException <int> ("maximumCapacity",
                0, int.MaxValue, maximumCapacity);
            }

/*
Member initialisation.
*/

            limit = maximumCapacity;
            count = 0;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve Counter's maximum capacity.</summary>

<remarks>The capacity is guaranteed to be ≥ 0.</remarks>

<value>A <see cref="System.Int32" /> value storing the maximum capacity of
the counter.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int MaximumCapacity
        {
            get
            {
                System.Diagnostics.Debug.Assert (limit >= 0);
                return limit;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Is counter empty?</summary>

<remarks>The counter is considered empty when the number of increment and
decrement operations are in balance, such that the current count is
0.</remarks>

<value>A <see cref="System.Boolean" /> value that is true if the counter is
empty or false otherwise.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsEmpty
        {
            get
            {
                System.Diagnostics.Debug.Assert (count >= 0);
                return (count == 0);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Is counter full?</summary>

<remarks>The counter is considered full when the difference between the number
of increment and decrement operations has reached the allowed limit, such that
the current count is at its maximum valuue.</remarks>

<value>A <see cref="System.Boolean" /> value that is true if the counter is
full or false otherwise.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsFull
        {
            get
            {
                System.Diagnostics.Debug.Assert (limit >= 0);
                System.Diagnostics.Debug.Assert (count >= 0);
                return (count == limit);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Increment counter.</summary>

<remarks>Increments the counter's currently tracked count.</remarks>

<exception cref="CounterIncrementException">Thrown if the counter is already at
its maximum value when the increment attempt is made.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void Increment ()
        {

/*
If we're already at the maximum count, then throw the overflow exception.
*/

            if (IsFull)
            {
                throw new CounterIncrementException (this);
            }

/*
Increment the count.
*/

            ++count;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Decrement counter.</summary>

<remarks>Decrements the counter's currently tracked count.

<para>Avoid placing calls to <see cref="Decrement ()" /> within finalization
code.  Finalization is peformed by the garbage collector at unpredictable
intervals and will cause your code to execute erratically.</para></remarks>

<exception cref="CounterDecrementException">Thrown if the counter is empty when
the decrement attempt is made.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public void Decrement ()
        {

/*
If we're already at zero, then throw the overflow exception.
*/

            if (IsEmpty)
            {
                throw new CounterDecrementException ();
            }

/*
Decrement the count.
*/

            --count;
        }
    }
}
