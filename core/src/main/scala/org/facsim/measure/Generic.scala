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

import scala.language.implicitConversions

//=============================================================================
/**
Class representing a measurements from a specific, but generalized, physical
quantity family.

The role of this class is to allow general multiplication and division of pairs
of measurement values.

For example, when a length measurement is divided by a time measurement the
result is a velocity measurement.  However creating tables that define the
result of each pair of divisors would be time-consuming, error prone and
non-exhaustive.

A simpler approach is to create a generic measurement capturing the family's
characteristics that can be converted, if required, to the corresponding
specific physical measurement for that family by implicit conversion.  If the
generic measurement's family is not identical to the specific measurement it is
being converted to, an exception will result.  For example, we could obtain a
generic measurement that has velocity characteristics by dividing a length by a
time.  If we then attempt to convert this generic value to a velocity, it will
succeed; however, if we attempt to convert it to a temperature, an exception
will result.
*/
//=============================================================================

object Generic extends PhysicalQuantity {

/**
There is only set of units for this type, which will be the SI units by
definition.
*/

  final val basic = new GenericUnits

/**
@inheritdoc
*/

  final override type Measure = GenericMeasure

/**
@inheritdoc
*/

  final override type Units = GenericUnits

/**
@inheritdoc
*/

  final override val siUnits = basic

//-----------------------------------------------------------------------------
/**
Implicit conversion from a generic measurement value to a Double value.

In order for this conversion to succeed, the family associated with the generic
measurement must have unitless (i.e., all base measurement exponents must be
zero).

@param measure Generic measurement value to be converted.

@return The unitless value of the measurement as a Double.
*/
//-----------------------------------------------------------------------------

  implicit final def toDouble (measure: Measure): Double = {
    if (measure.getFamily.isUnitless) measure.getValue
    else throw new GenericConversionException (measure,
    Family.unitless)
  }

//-----------------------------------------------------------------------------
/**
Create a new 
*/
//-----------------------------------------------------------------------------

  private [measure] final def apply (value: Double, family: Family) = new
  GenericMeasure (value, family)

//-----------------------------------------------------------------------------
/**
*/
//-----------------------------------------------------------------------------

  final class GenericMeasure (value: Double, private val family: Family)
  extends AbstractMeasure (value) {

//.............................................................................
/**
@inheritdoc
*/
//.............................................................................

    final override def getFamily = family

//.............................................................................
/**
@inheritdocs
*/
//.............................................................................

    @inline
    final override def createNew (value: Double) = apply (value, family)

//.............................................................................
/**
@inheritdoc
*/
//.............................................................................

    final override def toString = {
      "TODO: Need to find a way to report generic units."
    }
  }

//-----------------------------------------------------------------------------
/**
@constructor Construct a new generic unit of measure.  In the case of 
*/
//-----------------------------------------------------------------------------

  final class GenericUnits private [measure] extends AbstractUnits {

//.............................................................................
/**
@inheritdoc
*/
//.............................................................................

    @inline
    private [measure] final override def importValue (value: Double): Double =
    value

//.............................................................................
/**
@inheritdoc
*/
//.............................................................................

    @inline
    private [measure] final override def exportValue (value: Double): Double =
    value
  }
}