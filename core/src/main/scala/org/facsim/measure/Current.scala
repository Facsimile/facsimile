/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file belonging to the org.facsim.measure package.
*/
//======================================================================================================================

package org.facsim.measure

import org.facsim.LibResource

//======================================================================================================================
/**
''[[http://en.wikipedia.org/wiki/Electric_current Electric current]]'' physical
quantity type.

All electrical current values are stored internally in
''[[http://en.wikipedia.org/wiki/Ampere amperes]]'', which is the
''[[http://en.wikipedia.org/wiki/SI SI]]'' standard unit of measure.

@see [[http://en.wikipedia.org/wiki/Electric_current Electric current]] on
''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/Ampere Amperes]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/
//======================================================================================================================

object Current
extends NonNegative {

/*
Electric current measurement values.
*/

  override type Measure = CurrentMeasure

/*
Electric current measurement units.
*/

  override type Units = CurrentUnits

/*
Name of this physical quantity.
*/

  override val name = "electric current"

/**
Units for current measured in ''[[http://en.wikipedia.org/wiki/Ampere
amperes]]''.

@note ''Amperes'' are the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard
units for electric current measurement, and the units that are used to store
such measurements internally in ''Facsimile''.

In ''Facsimile'', an ''ampere'' is defined in accordance with ''SI'' standards.

@see [[http://en.wikipedia.org/wiki/Ampere Ampere]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/

  val Amperes = new Units (SIConverter,
  LibResource ("measure.Current.Ampere.sym"))

/**
Physical quantity family for current measurements.
*/

  protected [measure] val family = Family (currentExponent = 1)

/*
Electric current SI units.
*/

  override val siUnits = Amperes

/*
Register this family.
*/

  Family.register (family, Current)

//----------------------------------------------------------------------------------------------------------------------
/*
Electric current measurement factory.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [measure] override def apply (measure: Double) =
  new Measure (measure)

//----------------------------------------------------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Electric_current Electric current]]''
measurement class.

Instances of this class represent ''electric current'' measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Electric_current
electric current]]'' measurement value.

@param measure ''Electric current'' measurement expressed in
''[[org.facsim.measure.Current.Amperes]]''. This value must be finite and
greater than or equal to zero.

@throws java.lang.IllegalArgumentException if `measure` is not finite or
is negative.

@since 0.0
*/
//----------------------------------------------------------------------------------------------------------------------

  final class CurrentMeasure private [measure] (measure: Double)
  extends NonNegativeMeasure (measure)

//----------------------------------------------------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Electric_current Electric current]]'' unit of
measurement family class.

Instances of this class represent units for expressing ''electric current''
measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Electric_current
Electric current]]'' unit of measurement.

@param converter Rules to be applied to convert a quantity measured in these
units to and from the standard ''electric current
[[http://en.wikipedia.org/wiki/SI SI]]'' units, ''amperes''.

@param symbol Symbol to be used when outputting measurement values expressed in
these units.

@since 0.0
*/
//----------------------------------------------------------------------------------------------------------------------

  final class CurrentUnits private [measure] (converter: Converter, symbol:
  String)
  extends NonNegativeUnits (converter, symbol)
}