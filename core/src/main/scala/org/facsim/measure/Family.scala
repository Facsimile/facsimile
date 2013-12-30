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
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file belonging to the org.facsim.measure package.
*/
//=============================================================================

package org.facsim.measure

import scala.collection.immutable.HashMap

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

final class Family private (private val exponents: Vector [Int])
extends Equals
with NotNull {

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
family by the specified `multiplier` family.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def * (multiplier: Family) =
  Family (exponents.zip (multiplier.exponents).map ((p: (Int, Int)) => p._1 +
  p._2))

//-----------------------------------------------------------------------------
/**
Return physical quantity family that results from dividing a measurement value
in this family by a measurement value in the specified family.

@param divisor Physical quantity family instance that is dividing this
instance.

@return Physical quantity family resulting from the division of this family by
the specified `divisor` family.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def / (divisor: Family) =
  Family (exponents.zip (divisor.exponents).map ((p: (Int, Int)) => p._1 -
  p._2))

//-----------------------------------------------------------------------------
/**
Determine whether this family is ''unitless''.

@return `true` if this physical quantity family is unitless, or `false`
otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def isUnitless = Family.this == Family.unitless

//-----------------------------------------------------------------------------
/*
Determine whether another object can equal this object.

Refer to Chapter 30 of "Programming in Scala", 2nd Edition, by Odersky, Spoon &
Venners.
*/
//-----------------------------------------------------------------------------

  override def canEqual (that: Any) = that match {

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

  override def equals (that: Any): Boolean = that match {

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

  override def hashCode = exponents.hashCode

//-----------------------------------------------------------------------------
/*
Convert to a string.

@see Any.toString()
*/
//-----------------------------------------------------------------------------

  override def toString: String = Family.typeMap.get (this) match {

/*
If this family has an associated specific physical quantity type, then retrieve
and output its name.
*/

    case Some (x) => x.getClass.getSimpleName

/*
In all other cases, apply the names of the base units with the appropriate
exponents.
*/

    case _ => {
      val types = for {
        i <- 0 until exponents.length
        if exponents (i) != 0
      } yield Family.baseFamilies (i).getClass.getSimpleName + (if (exponents
      (i) != 1) "(^" + exponents (i) + ")")
      types.mkString (",")
    }
  }
}

//=============================================================================
/**
Physical quantity family companion object.

@since 0.0
*/
//=============================================================================

private [measure] object Family
extends Enumeration {

/**
Index of Time exponent in the exponents vector.
*/

  private [measure] val timeExponentIndex = Value

/**
Index of Length exponent in the exponents vector.
*/

  private [measure] val lengthExponentIndex = Value

/**
Index of Plane Angle exponent in the exponents vector.
*/

  private [measure] val planeAngleExponentIndex = Value

/**
Index of Mass exponent in the exponents vector.
*/

  private [measure] val massExponentIndex = Value

/**
Index of Temperature exponent in the exponents vector.
*/

  private [measure] val temperatureExponentIndex = Value

/**
Index of Current exponent in the exponents vector.
*/

  private [measure] val currentExponentIndex = Value

/**
Index of Luminosity exponent in the exponents vector.
*/

  private [measure] val luminousIntensityExponentIndex = Value

/**
Vector of base physical quantity families.
*/

  private [measure] val baseFamilies = Vector [Specific] (
    Time,
    Length,
    Angle,
    Mass,
    Temperature,
    Current,
    LuminousIntensity
  )

/**
Total number of base physical quantity families in the exponents vector.
*/

  private [measure] val numBaseFamilies = maxId

/**
Unitless physical quantity.

Measurements in this family are, in effect, just plain Double values.
*/

  private [measure] val unitless = apply ()

/**
Map associating families to associated types.  Families that do not have
entries in this map do not have associated types an exist as generic values
only.
*/

  private var typeMap = HashMap.empty [Family, Specific]

//-----------------------------------------------------------------------------
/**
Register a specific physical quantity type with a family value.

Registration should be performed once for each concrete
[[org.facsim.measure.Specific!]] class instance.

@param family Family value to be registered as associated with the
'''specific''' class.

@param specific Class of the associated specific physical quantity type.
*/
//-----------------------------------------------------------------------------

  private [measure] def register (family: Family, specific: Specific):
  Unit = synchronized {
    assert (!typeMap.contains (family))
    typeMap += (family -> specific)
  }

//-----------------------------------------------------------------------------
/**
Apply method to obtain family corresponding to specified exponent values.

@param timeExponent Exponent of time base family.

@param lengthExponent Exponent of length base family.

@param planeAngleExponent Exponent of plane angle base family.

@param massExponent Exponent of mass base family.

@param temperatureExponent Exponent of temperature base family.

@param currentExponent Exponent of current base family.

@param luminousIntensityExponent Exponent of luminous intensity base family.

@return corresponding physical quantity family instance.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [measure] def apply (timeExponent: Int = 0, lengthExponent: Int = 0,
  planeAngleExponent: Int = 0, massExponent: Int = 0, temperatureExponent: Int
  = 0, currentExponent: Int = 0, luminousIntensityExponent: Int = 0): Family =
  apply (Vector (timeExponent, lengthExponent, planeAngleExponent,
  massExponent, temperatureExponent, currentExponent,
  luminousIntensityExponent))

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
  private def apply (exponents: Vector [Int]) = new Family (exponents)
}