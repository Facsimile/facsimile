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

import org.facsim.LibResource

//=============================================================================
/**
''[[http://en.wikipedia.org/wiki/Mass Mass]]'' physical quantity type.

All mass values are stored internally in
''[[http://en.wikipedia.org/wiki/Kilogram kilograms]]'', which is the
''[[http://en.wikipedia.org/wiki/SI SI]]'' standard unit of measure.

@see [[http://en.wikipedia.org/wiki/Mass Mass]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/Kilogram Kilograms]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/
//=============================================================================

object Mass
extends NonNegative {

/*
Mass measurement values.
*/

  override type Measure = MassMeasure

/*
Mass measurement units.
*/

  override type Units = MassUnits

/*
Name of this physical quantity.
*/

  override val name = "mass"

/**
Units for masses measured in
''[[http://en.wikipedia.org/wiki/Milligram#SI_multiples milligrams]]''.

@see [[http://en.wikipedia.org/wiki/Milligram#SI_multiples Milligrams]] on
''Wikipedia''.

@since 0.0
*/

  val Milligrams = new Units (new LinearScaleConverter (1.0e-6),
  LibResource ("measure.Mass.Milligram.sym"))

/**
Units for masses measured in ''[[http://en.wikipedia.org/wiki/Gram grams]]''.

@see [[http://en.wikipedia.org/wiki/Gram grams]] on ''Wikipedia''.

@since 0.0
*/

  val Grams = new Units (new LinearScaleConverter (0.001),
  LibResource ("measure.Mass.Gram.sym"))

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Kilogram
kilograms]]''.

@note ''Kilograms'' are the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard
units for mass measurement, and the units that are used to store such
measurements internally in ''Facsimile''.

In ''Facsimile'', a ''kilogram'' is defined in accordance with ''SI''
standards.

@see [[http://en.wikipedia.org/wiki/Kilogram Kilograms]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/

  val Kilograms = new Units (SIConverter,
  LibResource ("measure.Mass.Kilogram.sym"))

/**
Units for masses measured in ''[[http://en.wikipedia.org/wiki/Tonne metric
tonnes]]'' (also known as ''megagrams'').

@see [[http://en.wikipedia.org/wiki/Tonne Tonnes]] on ''Wikipedia''.

@since 0.0
*/

  val Tonnes = new Units (new LinearScaleConverter (1000.0),
  LibResource ("measure.Mass.Tonne.sym"))

/**
Number of kilograms equivalent to one avoirdupois pound.

This value is the internationally accepted rate for converting between
kilograms and pounds and is used internally to convert between metric and
Imperial/English mass units. This value '''must not''' be modified!

@see [[http://en.wikipedia.org/wiki/Kilogram Kilogram]] on ''Wikipedia''.
*/

  private val KilogramsPerPound = 0.45359237

/**
Units for masses measured in ''[[http://en.wikipedia.org/wiki/Pound_(mass)
avoirdupois pounds]]''.

@see [[http://en.wikipedia.org/wiki/Pound_(mass) Pounds]] on ''Wikipedia''.

@since 0.0
*/

  val Inches = new Units (new LinearScaleConverter (KilogramsPerPound),
  LibResource ("measure.Mass.Pound.sym"))

/**
Units for masses measured in ''[[http://en.wikipedia.org/wiki/Ounce ounces]]''.

@see [[http://en.wikipedia.org/wiki/Ounce Ounces]] on ''Wikipedia''.

@since 0.0
*/

  val Ounces = new Units (new LinearScaleConverter (KilogramsPerPound / 16.0),
  LibResource ("measure.Mass.Ounce.sym"))

/**
Physical quantity family for mass measurements.
*/

  protected [measure] val family = Family (massExponent = 1)

/*
Mass SI units.
*/

  override val siUnits = Kilograms

/*
Register this family.
*/

  Family.register (family, Mass)

//-----------------------------------------------------------------------------
/*
Mass measurement factory.
*/
//-----------------------------------------------------------------------------

  private [measure] override def apply (measure: Double) =
  new Measure (measure)

//-----------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Mass Mass]]'' measurement class.

Instances of this class represent ''mass'' measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Mass mass]]''
measurement value.

@param measure ''Mass'' measurement expressed in
''[[org.facsim.measure.Mass.Kilograms]]''. This value must be finite and
greater than or equal to zero.

@throws java.lang.IllegalArgumentException if `measure` is not finite or
is negative.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class MassMeasure private [measure] (measure: Double)
  extends NonNegativeMeasure (measure)

//-----------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Mass Mass]]'' unit of measurement family
class.

Instances of this class represent units for expressing ''mass'' measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Mass mass]]'' unit of
measurement.

@param converter Rules to be applied to convert a quantity measured in these
units to and from the standard ''mass [[http://en.wikipedia.org/wiki/SI SI]]''
units, ''kilograms''.

@param symbol Symbol to be used when outputting measurement values expressed in
these units.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class MassUnits private [measure] (converter: Converter, symbol:
  String)
  extends NonNegativeUnits (converter, symbol)
}