/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

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

Note: Do NOT compare to the size of Family.baseFamilies since that Vector isn't
initialized until after a number of instances of this class have been created.
Hardcoding the expected exponents vector length here is adequate for a sanity
check.
*/

  assert (exponents.length == 7)

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
/**
Retrieve the symbol of this family, constructed from the symbols of the
preferred base units.

@return Symbol for this family.
*/
//-----------------------------------------------------------------------------

  private def baseSymbol = {

/*
Helper function to build the symbol of the units given the index of the family
and the exponent power.
*/

    def buildSymbol (idx: Int, exp: Int): String = {
      assert (exp > 0)

/*
Retrieve the symbol of this base family and append its exponent. If the
exponent is 1, then the suffix is an empty string; if 2, then it is superscript
2; 3, superscript 3; otherwise it is denoted by a circumflex followed by the
power (e.g. ^5).
*/

      val suffix = exp match {
        case 1 => ""
        case 2 => "²"
        case 3 => "³"
        case _ => s"^$exp"
      }
      Family.symbol (Family.baseFamily (idx)) + suffix
    }

/*
The first part of the name is a list of the components with positive exponents.
*/

    val positive = for {
      i <- 0 until exponents.size
      if exponents (i) > 0
    } yield buildSymbol (i, exponents (i))

/*
Repeat for the components with negative exponents, changing the sign of the
exponent in the process.
*/

    val negative = for {
      i <- 0 until exponents.size
      if exponents (i) < 0
    } yield buildSymbol (i, -exponents (i))

/*
If we have any negative content, then append it to the positive content (which
may be empty) separated by a slash.
*/

    if (!negative.isEmpty) {
      positive.mkString ("") + "/" + negative.mkString ("")
    }

/*
Otherwise, just report the positive content.
*/

    else positive.mkString (" ")
  } ensuring (_ != "")

//-----------------------------------------------------------------------------
/**
Retrieve the name of this family, constructed from the names of the base units.

@return Name of this family.
*/
//-----------------------------------------------------------------------------

  private def baseName = {

/*
Helper function to build the name of the units given the index of the family
and the exponent power.
*/

    def buildName (idx: Int, exp: Int): String = {
      assert (exp > 0)

/*
Retrieve the name of this base family and append its exponent. If the exponent
is 1, then the suffix is an empty string; if 2, then it is superscript 2; 3,
superscript 3; otherwise it is denoted by a circumflex followed by the power
(e.g. ^5).
*/

      val suffix = exp match {
        case 1 => ""
        case 2 => "²"
        case 3 => "³"
        case _ => s"^$exp"
      }
      Family.name (Family.baseFamily (idx)) + suffix
    }

/*
The first part of the name is a list of the components with positive exponents.
*/

    val positive = for {
      i <- 0 until exponents.size
      if exponents (i) > 0
    } yield buildName (i, exponents (i))

/*
Repeat for the components with negative exponents, changing the sign of the
exponent in the process.
*/

    val negative = for {
      i <- 0 until exponents.size
      if exponents (i) < 0
    } yield buildName (i, -exponents (i))

/*
If we have any negative content, then append it to the positive content (which
may be empty) separated by a slash.
*/

    if (!negative.isEmpty) {
      positive.mkString (" ") + "/" + negative.mkString (" ")
    }

/*
Otherwise, just report the positive content.
*/

    else positive.mkString (" ")
  } ensuring (_ != "")

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

  override def toString: String = {
    val name = Family.name (this)
    if (!isUnitless) {
      val symbol = Family.symbol (this)
      s"$name ($symbol)"
    }
    else name
  }
}

//=============================================================================
/**
Physical quantity family companion object.

@since 0.0
*/
//=============================================================================

private [measure] object Family {

/**
Vector of base families.

The order in which these families are listed must match the order in which the
corresponding exponents are listed in each Family instance vector.
*/

  private val baseFamily = Vector (
    apply (timeExponent = 1),
    apply (lengthExponent = 1),
    apply (planeAngleExponent = 1),
    apply (massExponent = 1),
    apply (temperatureExponent = 1),
    apply (currentExponent = 1),
    apply (luminousIntensityExponent = 1)
  )

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

  private def apply (exponents: Vector [Int]) = new Family (exponents)

//-----------------------------------------------------------------------------
/**
Retrieve symbol associated with specified family.

If there is a specific physical quantity registered with the specified
`family`, then the symbol for the preferred units of that class is returned. If
there is no registered physical quantity, then the symbols of the base
component physical quantities of this family will be listed instead, with
appropriate exponent powers. If the supplied family is unitless, then an empty
string will be returned as the symbol.

@param family Family for which a symbol is sought.

@return Symbol of the preferred units of the associated physical quantity.
*/
//-----------------------------------------------------------------------------

  private def symbol (family: Family): String = {

/*
Retrieve the specific physical quantity (if any) associated with this family.
Access to typeMap needs to be synchronized, since it is shared mutable state.
*/

    val specific = synchronized {
      typeMap.get (family)
    }

/*
Check what we have.
*/

    specific match {

/*
If there is a registered specific physical quantity for this family, then
retrieve and return the symbol of its preferred units.
*/

      case Some (x) => x.preferredUnits.symbol

/*
Otherwise, this is a generic physical quantity. If it is unitless, then return
an empty string.
*/

      case None => {
        if (family == unitless) ""
        else family.baseSymbol
      }
    }
  }

//-----------------------------------------------------------------------------
/**
Retrieve name associated with specified family.

If there is a specific physical quantity registered with the specified
`family`, then the name of that quantity is returned. If there is no registered
physical quantity, then the names of the base component physical quantities of
this family will be listed instead, with appropriate exponent powers. If the
supplied family is unitless, then "Double" will be returned as the name.

@param family Family for which a name is sought.

@return Name of the the associated physical quantity.
*/
//-----------------------------------------------------------------------------

  private def name (family: Family): String = {

/*
Retrieve the specific physical quantity (if any) associated with this family.
Access to typeMap needs to be synchronized, since it is shared mutable state.
*/

    val specific = synchronized {
      typeMap.get (family)
    }

/*
Check what we have.
*/

    specific match {

/*
If there is a registered specific physical quantity for this family, then
retrieve and return the symbol of its preferred units.
*/

      case Some (x) => x.name

/*
Otherwise, this is a generic physical quantity. If it is unitless, then return
an empty string.
*/

      case None => {
        if (family == unitless) ""
        else family.baseName
      }
    }
  }
}