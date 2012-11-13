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

trait Conversion {

//-----------------------------------------------------------------------------
/**
Returns imported measurement quantity value.

Convert a measurement quantity's '''value''', expressed in the units associated
with this converter instance, to a value in the standard $SI units for this
type of measurement.

@param value Value of the measurement quantity, expressed in the associated
units, to be converted.

@return Converted value of the measurement quantity, expressed in the standard
$SI units for this type of measurement.

@see $SI_FULL

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [measure] def importValue (value: Double): Double

//-----------------------------------------------------------------------------
/**
Export a measurement quantity, converting a value from the standard $SI units
to the associated units.

Convert a measurement quantity's '''value''', expressed in the standard $SI
units for this type of measurement, to a value in the units associated with
this converted instance.

@param value Value of the measurement quantity, expressed in standard $SI
units, to be converted.

@return Converted value of the measurement quantity, expressed in the
associated units.

@see $SI_FULL

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [measure] def exportValue (value: Double): Double
}