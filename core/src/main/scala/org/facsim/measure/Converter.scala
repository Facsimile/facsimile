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

//=============================================================================
/**
Physical quantity unit converter trait.

Converter subclasses support the conversion of physical quantity measurement
values between different units belonging to the same family.

More specifically, each converter instance has the capability to convert a
physical quantity measurement value from a specific unit of measure to the
standard ''[[http://en.wikipedia.org/wiki/SI SI]]'' units for the associated
unit family.

For example, a converter instance would be used to convert ''time''s expressed
in ''hours'' to or from times expressed in ''seconds''&mdash;the standard
''SI'' unit of time measurement.

The process of converting a value to ''SI'' units is termed ''importing''; the
process of converting an ''SI'' value to the associated unit is termed
''exporting''.

@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
[[http://en.wikipedia.org/ Wikipedia]].
*/
//=============================================================================

private [measure] trait Converter {

//-----------------------------------------------------------------------------
/**
Convert value to ''SI'' units.

Convert a physical quantity's `value`, expressed in the associated units to the
corresponding value in the ''SI'' units for this unit family.

@param value Value, expressed in associated units, to be converted to ''SI''
units.

@return `value` as expressed in ''SI'' units.
*/
//-----------------------------------------------------------------------------

  private [measure] def importValue (value: Double): Double

//-----------------------------------------------------------------------------
/**
Convert value from ''SI'' units.

Convert a physical quantity's `value`, expressed in the standard ''SI'' units
for this unit family, to the corresponding value in the associated units.

@param value Value, expressed in ''SI'' units, to be converted to associated
units.

@return `value` as expressed in associated units.
*/
//-----------------------------------------------------------------------------

  private [measure] def exportValue (value: Double): Double
}