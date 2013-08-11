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
Abstract base class for all physical quantity types associated with specific
physical quantity families.

@since 0.0
*/
//=============================================================================

private [measure] abstract class Specific extends PhysicalQuantity  {

/**
@inheritdocs
*/

  override type Measure <: SpecificMeasure

/**
@inheritdocs
*/

  override type Units <: SpecificUnits

/**
Physical quantity family represented by this specific type.
*/

  protected [measure] val family: Family

//-----------------------------------------------------------------------------
/**
Generic measurement to specific measurement conversion function.

If an attempt is made to convert a generic measurement associated with a
different family to that of the target type, then a run-time exception will
result.

@param measure Generic measure to be converted.

@return Specific measurement equivalent to the specified generic '''measure'''.

@throws org.facsim.measure.GenericConversionException if '''measure''' is
associated with different family to the target specific family.

@since 0.0
*/
//-----------------------------------------------------------------------------

  implicit final def fromGeneric (measure: Generic.Measure): Measure = {
    if (measure.getFamily == family) apply (measure.getValue)
    else throw new GenericConversionException (measure, family)
  }

//-----------------------------------------------------------------------------
/**
Factory method to create a new measurement value in the specified units.

@param value Measurement's value in specified '''units'''.  This value must be
finite and must lie within the defined domain for the associated physical
quantity.

@param units Unit's in which the measurement's '''value''' is expressed.

@return Corresponding measurement value.

@throws java.lang.IllegalArgumentException If '''value''' in the specified
'''units''' is not finite or is outside of the defined domain for the
associated physical quantity.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def apply (value: Double, units: Units): Measure =
  apply (units.importValue (value))

//-----------------------------------------------------------------------------
/**
Factory method to create a new measurement value in
''[[http://en.wikipedia.org/wiki/SI SI]]'' units.

@note This function is not public because it introduces the potential for unit
confusion.  Measurements can only be manipulated by users as
[[org.facsim.measure.PhysicalQuantity.AbstractMeasure!]] subclass instances,
not as raw values.  Allowing access to raw values encourages by-passing of the
unit protection logic provided by these measurement classes.

@param value Measurement's value in ''SI''.  This value must be finite and must
lie within the defined domain for the associated physical quantity.

@return Corresponding measurement value.

@throws java.lang.IllegalArgumentException If '''value''' in ''SI'' units is
not finite or is outside of the defined domain for the associate physical
quantity.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [measure] def apply (value: Double): Measure

//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

  abstract class SpecificMeasure private [measure] (value: Double) extends
  AbstractMeasure (value) with Ordered [Measure] {

//.............................................................................
/**
@inheritdocs

@since 0.0
*/
//.............................................................................

    final override def getFamily = family

//.............................................................................
/**
@inheritdocs

@since 0.0
*/
//.............................................................................

    @inline
    final override def createNew (value: Double) = apply (value)

//.............................................................................
/**
@inheritdoc

@since 0.0
*/
//.............................................................................

    final override def toString = {
      val units = preferredUnits
      units.format (getValue (units))
    }

//.............................................................................
/**
@inheritdoc

@since 0.0
*/
//.............................................................................

    @inline
    final override def compare (that: Measure): Int =
    value.compare (that.getValue)
  }

//-----------------------------------------------------------------------------
/**
@constructor Create new instance of a physical quantity unit family.

@param converter Rules to be applied to convert a quantity measured in these
units to the standard ''SI'' units for this unit family.

@param symbol Symbol associated with these units.

@since 0.0
*/
//-----------------------------------------------------------------------------

  abstract class SpecificUnits private [measure] (private val converter:
  Converter, private val symbol: String) extends AbstractUnits {

//.............................................................................
/**
@inheritdoc
*/
//.............................................................................

    @inline
    private [measure] final override def importValue (value: Double): Double =
    converter.importValue (value)

//.............................................................................
/**
@inheritdoc
*/
//.............................................................................

    @inline
    private [measure] final override def exportValue (value: Double): Double =
    converter.exportValue (value)

//.............................................................................
/**
Retrieve the symbol associated with these units.

@return Standard symbol used to denote values in these units.

@since 0.0
*/
//.............................................................................

    @inline
    final def getSymbol = symbol
  }
}