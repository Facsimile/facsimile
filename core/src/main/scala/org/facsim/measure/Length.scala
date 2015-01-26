/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2015, Michael J Allen.

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
''[[http://en.wikipedia.org/wiki/Length Length]]'' physical quantity type.

All lengths are stored internally in ''[[http://en.wikipedia.org/wiki/Metre
meters]]'', which is the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard
unit of measure.

@see [[http://en.wikipedia.org/wiki/Length Length]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/Metre Meters]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/
//=============================================================================

object Length
extends Specific {

/*
Length measurement values.
*/

  override type Measure = LengthMeasure

/*
Length measurement units.
*/

  override type Units = LengthUnits

/*
Name of this physical quantity.
*/

  override val name = "length"

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Millimetre
millimeters]]''.

@see ''[[http://en.wikipedia.org/wiki/Millimetre Millimeters]]'' on
''Wikipedia''.

@since 0.0
*/

  val Millimeters = new Units (new LinearScaleConverter (0.001),
  LibResource ("measure.Length.Millimeter.sym"))

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Centimetre
centimeters]]''.

@see [[http://en.wikipedia.org/wiki/Centimetre Centimeters]] on ''Wikipedia''.

@since 0.0
*/

  val Centimeters = new Units (new LinearScaleConverter (0.01),
  LibResource ("measure.Length.Centimeter.sym"))

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Metre
meters]]''.

@note ''Meters'' are the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard
units for length measurement, and the units that are used to store such
measurements internally in ''Facsimile''.

In ''Facsimile'', a ''meter'' is defined in accordance with ''SI'' standards.

@see [[http://en.wikipedia.org/wiki/Metre Meters]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/

  val Meters = new Units (SIConverter,
  LibResource ("measure.Length.Meter.sym"))

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Kilometre
kilometers]]''.

@see [[http://en.wikipedia.org/wiki/Kilometre Kilometers]] on ''Wikipedia''.

@since 0.0
*/

  val Kilometers = new Units (new LinearScaleConverter (1000.0),
  LibResource ("measure.Length.Kilometer.sym"))

/**
Number of meters exactly equivalent to one inch.

This value is the internationally accepted rate for converting between meters
and inches and is used internally to convert between metric and
Imperial/English length units. This value '''must not''' be modified!

@see [[http://en.wikipedia.org/wiki/Inch Inch]] on ''Wikipedia''.
*/

  private val MetersPerInch = 0.0254

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Inch inches]]''.

@see [[http://en.wikipedia.org/wiki/Inch Inches]] on ''Wikipedia''.

@since 0.0
*/

  val Inches = new Units (new LinearScaleConverter (MetersPerInch),
  LibResource ("measure.Length.Inch.sym"))

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Foot_(unit)
feet]]''.

@see [[http://en.wikipedia.org/wiki/Foot_(unit) Feet]] on ''Wikipedia''.

@since 0.0
*/

  val Feet = new Units (new LinearScaleConverter (MetersPerInch * 12.0),
  LibResource ("measure.Length.Foot.sym"))

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Yard yards]]''.

@see [[http://en.wikipedia.org/wiki/Yard Yards]] on ''Wikipedia''.

@since 0.0
*/

  val Yards = new Units (new LinearScaleConverter (MetersPerInch * 36.0),
  LibResource ("measure.Length.Yard.sym"))

/**
Units for lengths measured in ''[[http://en.wikipedia.org/wiki/Mile miles]]''.

@see [[http://en.wikipedia.org/wiki/Mile Miles]] on ''Wikipedia''.

@since 0.0
*/

  val Miles = new Units (new LinearScaleConverter (MetersPerInch * 63360.0),
  LibResource ("measure.Length.Mile.sym"))

/**
Physical quantity family for length measurements.
*/

  protected [measure] val family = Family (lengthExponent = 1)

/*
Length SI units.
*/

  override val siUnits = Meters

/*
Register this family.
*/

  Family.register (family, Length)

//-----------------------------------------------------------------------------
/*
Length measurement factory.
*/
//-----------------------------------------------------------------------------

  private [measure] override def apply (measure: Double) =
  new Measure (measure)

//-----------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Length Length]]'' measurement class.

Instances of this class represent ''length'' measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Length length]]''
measurement value.

@param measure ''Length'' measurement expressed in
''[[org.facsim.measure.Length.meters]]''. This value must be finite.

@throws java.lang.IllegalArgumentException if `measure` is not finite.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class LengthMeasure private [measure] (measure: Double)
  extends SpecificMeasure (measure)

//-----------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Length Length]]'' unit of measurement family
class.

Instances of this class represent units for expressing ''length'' measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Length length]]'' unit
of measurement.

@param converter Rules to be applied to convert a quantity measured in these
units to and from the standard ''length
[[http://en.wikipedia.org/wiki/SI SI]]'' units, ''meters''.

@param symbol Symbol to be used when outputting measurement values expressed in
these units.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class LengthUnits private [measure] (converter: Converter, symbol:
  String)
  extends SpecificUnits (converter, symbol)
}