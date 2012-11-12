/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

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

Scala source file belonging to the org.facsim.facsimile.measure package.
*/
//=============================================================================

package org.facsim.facsimile.measure

//=============================================================================
/**
Abstract base class for all $FACSIMILE measurement classes.

This base class is used by all measurement types, storing the underlying values
in the $SI units corresponding to the specific measurement type.

This class implements measurements at a lower level than the [[Measure]] class,
primarily to assist with equality comparisons.

No subclass should add data members.

@since 0.0-0

@construct New base measurement value.

@param value Value of the measurement type in the corresponding $SI units.  An
IllegalArgumentException is thrown if the 
*/
//=============================================================================

private [measure] abstract class BaseMeasure (private val value: Double)
extends NotNull
with Equals
{

/*
Filter out invalid values: no measurement value may be null (guaranteed by
Scala - it is impossible to assign a value type null), not-a-number or
infinite.  Sub-classes can impose additional construction constraints (such as
requiring non-negative values), but they do not need to reproduce these tests.
*/

  require (!value.isNaN && !value.isInfinite)

//-----------------------------------------------------------------------------
/**
Return measurement value in corresponding $SI units.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  private [measure] final def getValue = value

//-----------------------------------------------------------------------------
/*
Determine whether another object can equal this object.

Refer to Chapter 30 of "Programming in Scala", 2nd Edition, by Odersky, Spoon &
Venners.
*/
//-----------------------------------------------------------------------------

  final override def canEqual (that: Any): Boolean = {

/*
Catch any exceptions arising during the comparison.
*/

    try {

/*
Convert that object to a BaseMeasure instance.  If that object is null, we'll
get a null pointer exception; if it is not a base measure instance, then a
class cast exception will result.  Both exceptions will be handled below.
*/

      val other: BaseMeasure = that.asInstanceOf [BaseMeasure]

/*
If we've made it this far, that object is a BaseMeasure.  If that object and
this object are different types of base measure, that is, if they have
different classes, then return false.
*/

      if (getClass != other.getClass) return false

/*
OK.  So this object and that object belong to the same class, so they can be
compared for equality.  Return true.
*/

      return true
    }

/*
Handle exceptions arising.
*/

    catch {

/*
If we get a null pointer exception, then that object is null.  The "equals
contract" explicitly requires that we return false in this instance, since the
two objects cannot be compared for equality.
*/

    case e: NullPointerException => return false

/*
If we get a class cast exception, then that object is not a subclass of
BaseMeasure.  Since the that object must be a different type than this object,
the "equals contract" requires that we return false.
*/

    case e: ClassCastException => return false

/*
We don't expect any other exceptions to occur, so signal an error and return
false afterwards (which shouldn't happen).
*/

    case _ => {
        sys.error ("Unhandled exception")
      }
    }
  }

//-----------------------------------------------------------------------------
/*
Compare this object to another for equality.

@see Any.equals (that: Any)

If two objects compare equal, then their hash-codes must compare equal too;
Similarly, if two objects have different hash-codes, then they must not compare
equal.  However if two objects have the same hash-codes, they may or may not
compare equal, since hash-codes do not map to unique values.
*/
//-----------------------------------------------------------------------------

  final override def equals (that: Any): Boolean = {

/*
If the two objects cannot be compared, return false.
*/

    if (!canEqual (that)) return false

/*
Convert that object to a BaseMeasure instance (this should succeed, since the
objects can compare equal - an exception will result otherwise).  Return the
comparison of their values.
*/

    val other: BaseMeasure = that.asInstanceOf [BaseMeasure]
    return (value == other.getValue)
  }

//-----------------------------------------------------------------------------
/*
Return this measurement value's hash code.

@see Any.hashCode ()

If two objects compare equal, then their hash-codes must compare equal too;
Similarly, if two objects have different hash-codes, then they must not compare
equal.  However if two objects have the same hash-codes, they may or may not
compare equal, since hash-codes do not map to unique values.

In this overridden function, we simply use the measurement value's hash code
and return that, which fulfills all the requirements of the hash code/equality
contract.
*/
//-----------------------------------------------------------------------------

  final override def hashCode = value.hashCode
}
