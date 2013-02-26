/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

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
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file belonging to the org.facsim.measure package.
*/
//=============================================================================

package org.facsim.measure

//=============================================================================
/**
Class representing a physical quantity family defined in terms of exponents of
base units.

An instance having only zero base measure exponents has no units; i.e. it is
''unitless''.

@constructor Construct a new physical quantity family.  This constructor is
private and should be called only from the
[[org.facsim.measure.Family$.apply(Vector[Int])*]]
method&mdash;the rationale being that we have the possibility to re-use
existing instances rather than create new instances on each call.

@param exponents Vector of base unit exponents associated with this family.

@since 0.0
*/
//=============================================================================

final class Family private (private val exponents: Vector
[Int]) extends Equals with NotNull {

/*
Sanity check.  Since construction is tightly controlled, it should be
impossible to pass a vector with the wrong number of members to this
constructor.  As a result, we just need to assert this condition, rather than
use requireValid.
*/

  assert (exponents.length == Family.numBaseFamilies)

//-----------------------------------------------------------------------------
/**
Return physical quantity family that results from multiplying a measurement
value in this family by a measurement value in the specified family.

@param multiplier Physical quantity family instance that is multiplied with
this instance.

@return Physical quantity family resulting from the multiplication of this
family by the specified multiplier family.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def * (multiplier: Family) =
  Family (exponents.zip (multiplier.exponents).map ((p: (Int,
  Int)) => p._1 + p._2))

//-----------------------------------------------------------------------------
/**
Return physical quantity family that results from dividing a measurement value
in this family by a measurement value in the specified family.

@param divisor Physical quantity family instance that is dividing this
instance.

@return Physical quantity family resulting from the division of this family by
the specified divisor family.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def / (divisor: Family) =
  Family (exponents.zip (divisor.exponents).map ((p: (Int,
  Int)) => p._1 - p._2))

//-----------------------------------------------------------------------------
/**
Determine whether these

@return `true` if this physical quantity family is unitless, or `false`
otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def isUnitless = Family.this == PhysicalQuantityFamily.unitless

//-----------------------------------------------------------------------------
/*
Determine whether another object can equal this object.

Refer to Chapter 30 of "Programming in Scala", 2nd Edition, by Odersky, Spoon &
Venners.
*/
//-----------------------------------------------------------------------------

  final override def canEqual (that: Any) = that match {

/*
If the other object is a PhysicalQuantityFamily instance, then we can compare
them for equality.
*/

    case other: Family => true

/*
Otherwise, we cannot.
*/

    case _ => false
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

  final override def equals (that: Any): Boolean = that match {

/*
If the other object is an PhysicalQuantityFamily instance, and that value can
be compared as equal to this value, and they have the same contents, then that
equals this.
*/

    case other: Family => other.canEqual (Family.this) && exponents ==
    other.exponents

/*
In that is any other type, then the two are not equal.
*/

    case _ => false
  }

//-----------------------------------------------------------------------------
/*
Return this physical quantity family's hash code.

@see Any.hashCode ()

If two objects compare equal, then their hash-codes must compare equal too;
Similarly, if two objects have different hash-codes, then they must not compare
equal.  However if two objects have the same hash-codes, they may or may not
compare equal, since hash-codes do not necessarily map to unique values.

In this overridden function, we simply use the exponent vector's hash code and
return that, which fulfills all the requirements of the hash code/equality
contract.
*/
//-----------------------------------------------------------------------------

  @inline
  final override def hashCode = exponents.hashCode

//-----------------------------------------------------------------------------
/*
Convert to a string.

@see Any.toString()

For now, just output the vector of exponents as a string.  In future, we might
try to output a description instead (such as "velocity", "area", "force",
etc.).
*/
//-----------------------------------------------------------------------------

  final override def toString = exponents.toString
}

//=============================================================================
/**
Physical quantity family companion object.

@since 0.0
*/
//=============================================================================

object Family {

/**
Index of Time exponent in the exponents vector.
*/

  private [measure] val timeExponentIndex = 0

/**
Index of Length exponent in the exponents vector.
*/

  private [measure] val lengthExponentIndex = 1

/**
Index of Plane Angle exponent in the exponents vector.
*/

  private [measure] val planeAngleExponentIndex = 2

/**
Index of Mass exponent in the exponents vector.
*/

  private [measure] val massExponentIndex = 3

/**
Index of Temperature exponent in the exponents vector.
*/

  private [measure] val temperatureExponentIndex = 4

/**
Index of Current exponent in the exponents vector.
*/

  private [measure] val currentExponentIndex = 5

/**
Index of Luminosity exponent in the exponents vector.
*/

  private [measure] val luminosityExponentIndex = 6

/**
Vector of base physical quantity families.
*/

  private [measure] val baseFamilies = Vector [PhysicalQuantity] (Time, Length,
  Angle, Mass, Temperature, Current, Luminosity)

/**
Total number of base physical quantity families in the exponents vector.
*/

  private [measure] val numBaseFamilies = baseFamilies.length

/**
Unitless physical quantity.

Measurements in this family are, in effect, just plain Double values.
*/

  private [measure] val unitless = apply ()

//-----------------------------------------------------------------------------
/**
Apply method to obtain family corresponding to specified exponent values.

@param timeExponent Exponent of time base family.

@param lengthExponent Exponent of length base family.

@param planeAngleExponent Exponent of plane angle base family.

@param massExponent Exponent of mass base family.

@param temperatureExponent Exponent of temperature base family.

@param currentExponent Exponent of current base family.

@param luminosityExponent Exponent of luminosity base family.

@return corresponding physical quantity family instance.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [measure] final def apply (timeExponent:Int = 0, lengthExponent:Int =
  0, planeAngleExponent: Int = 0, massExponent: Int = 0, temperatureExponent:
  Int = 0, currentExponent: Int = 0, luminosityExponent: Int = 0) =
  apply (Vector (timeExponent, lengthExponent, planeAngleExponent,
  massExponent, temperatureExponent, currentExponent, luminosityExponent))

//-----------------------------------------------------------------------------
/**
Apply method to obtain family corresponding to specified exponent vector.

This method should only be called from within this file
@param exponents Base family exponents vector.

@return corresponding physical quantity family instance.

@since 0.0
*/
//-----------------------------------------------------------------------------

/*
For now, just create a new instance.  In future, it might be necessary to
re-use an existing instance for the requested family, rather than a new
instance.
*/

  @inline
  private final def apply (exponents: Vector [Int]) = new Family (exponents)
}