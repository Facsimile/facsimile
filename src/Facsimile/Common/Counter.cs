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

C# source file for the Counter types, and related elements, which are integral
members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common {

//=============================================================================
/**
<summary>Maintains a non-negative, integer count.</summary>

<remarks>Counters cannot be modified except via the unit <see cref="Increment"
/> and <see cref="Decrement" /> functions.  Counters can be only be examined to
determine if they are <see cref="IsEmpty">empty</see> or <see
cref="IsFull">full</see> and can only be compared to other counters.  Counters
cannot be assigned a value.  All counters are initialised to be empty (i.e.
their initial count is 0).

<para>These restrictions help prevent accidental corruption of counts, and aid
the development of robust code by preventing the comparison of counts to
unrelated quantities.</para>

<para>Exceptions are thrown if attempts are made to decrement a zero count, or
increment a count that is already at its maximum.</para>

<para>Note: There are no increment or decrement operators.  The C# language
utilises the same operator++ definition for both prefix and postfix increment
operations (<c>++i</c>, <c>i++</c> respectively), and the same operator--
definition for both prefix and postfix decrement operators (<c>--i</c>,
<c>i--</c> respectively).  The prefix versions of these operators are useful in
the context of a Counter object; however, the postfix versions have semantics
that require new counters to be created with potentially non-zero initial
counts.  Since the postfix versions of these operators violate the basic
behaviour characteristics of Counters and since it is not possible to forbid
the use of postfix increment/decrement operators, the only solution is to avoid
them completely.  (Furthermore, the requirements for implementing postfix
semantics leads to increment/decrement operators that are less efficient than
the <see cref="Increment" /> and <see cref="Decrement" /> functions provided,
making such operators redundant in any case.)</para>

<para>Ideally, we would use an unsigned integer type to store the value of
the counter and the associated maximum capacity.  Unfortunately, Microsoft's
Common Language Specification (CLS) forbids the use of unsigned types as not
all .NET languages (most notably, Java/J++) do not support them.  Consequently,
signed integer types must be used instead.</para></remarks>

<example>The following code demonstrates how a Counter might be used to control
access to a restricted resource.

<code>
    public class Resource {
	private Facsimile.Common.Counter claims;
	public Resource () {
	    claims = new Facsimile.Common.Counter (1);
	}
	public void Use () {
	    claims.Increment ();
	    try {
		useResource ();
	    }
	    finally {
		claims.Decrement ();
	    }
	}
	public bool IsInUse () {
	    return !claims.IsEmpty ();
	}
	public void useResource () {
	    // ...
	}
	// ...
    }
</code>

<para>In the example, the counter has a limit of 1, so that only a single claim
on the resource may be made at a given time.  The counter is incremented (and
the resource claimed) at the start of any attempt to "use" the resource and is
decremented (and the resource released) when the operation has completed.  If
an attempt is made to claim the resource whilst it is already claimed, a
<see cref="System.OverflowException" /> exception will be
thrown.</para></example>
*/
//=============================================================================

    public sealed class Counter {

/**
<summary>Limit, or maximum capacity, of the counter's value.</summary>

<remarks>This value cannot be negative.</remarks>
*/

	private readonly int limit;

/**
<summary>Current count value.</summary>

<remarks>This value is constrained to be between 0 and the value held by the
<see cref="limit" /> data member.</remarks>
*/

	private int count;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>Constructs an "unconstrained" counter, in which the counter's limit is
the largest value supported by the underlying storage type.

<para>This constructor is guaranteed not to throw an
exception.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Counter (): this (int.MaxValue) {
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constrained counter constructor.</summary>

<remarks>Constructs a "constrained" counter that restricts the counter's limit
to the value identified by <paramref name="maximumCapacity" />.</remarks>

<param name="maximumCapacity">The highest value that the new counter will be
allowed to reach.  This value cannot be less than zero.  If this value is zero,
then the counter will be unusable (exceptions will be thrown whenever any
modification (i.e <see cref="Increment" /> or <see cref="Decrement" />) is
attempted on the counter).</param>

<exception cref="System.ArgumentOutOfRangeException">Thrown if <paramref
name="maximumCapacity" /> is less than zero.  Valid values are zero or
higher.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Counter (int maximumCapacity) {

/*
Validate the supplied maximum capacity.
*/

	    if (maximumCapacity < 0) {
		// TODO: Internationalise this message!
		throw new System.ArgumentOutOfRangeException ("Counter " +
		"maximum capacity must be >= 0.  Value received was: " +
		maximumCapacity.ToString (), "maximumCapacity");
	    }

/*
Trivial construction.
*/

	    limit = maximumCapacity;
	    count = 0;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if counter is empty.</summary>

<remarks>The counter is considered empty when the number of increment and
decrement operations are in balance, and the current count is 0.</remarks>

<value>A <see cref="System.Boolean" /> value that is true if the count is empty
or false otherwise.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public bool IsEmpty {
	    get {
		return (count == 0);
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if counter is full.</summary>

<remarks>The counter is considered full when the difference between the number
of increment and decrement operations has reached the allowed limit, and the
current count is at its maximum valuue.</remarks>

<value>A <see cref="System.Boolean" /> value that is true if the count is empty
or false otherwise.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public bool IsFull {
	    get {
		return (count == limit);
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Counter increment function.</summary>

<remarks>Increment's the counter's currently tracked count.</remarks>

<exception cref="System.OverflowException">Thrown if the counter is already at
its maximum value when an increment operation is attempted.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void Increment () {

/*
If we're already at the maximum count, then throw the overflow exception.
*/

	    if (IsFull) {
		// TODO: Internationalise this message!
		throw new System.OverflowException ("Counter is at its " +
		"maximum value and cannot be incremented");
	    }

/*
Increment the count.
*/

	    ++count;
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Counter decrement function.</summary>

<remarks>This function decrement's the counter's currently tracked count.

<para>Avoid placing calls to <see cref="Decrement" /> within finalisation code.
Finalisation is peformed by the garbage collector at unpredictable intervals
and will cause your code to execute erratically.  Instead, have your class
implement the <see cref="System.IDisposable" /> interface, and place the
<see cref="Decrement" /> call in the <see cref="System.IDisposable.Dispose" />
method.  You also need to ensure that the "Dispose" method is called when you
no longer require the object.</para></remarks>

<exception cref="System.OverflowException">Thrown if the counter is already at
zero when a decrement operation is attempted.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void Decrement () {

/*
If we're already at zero, then throw the overflow exception.
*/

	    if (IsEmpty) {
		// TODO: Internationalise this message!
		throw new System.OverflowException ("Counter is at its " +
		"minimum value and cannot be decremented");
	    }

/*
Decrement the count.
*/

	    --count;
	}
    }
}
