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
Scala source file from the org.facsim.measure.test package.
*/
//=============================================================================

package org.facsim.measure.test

//=============================================================================
/**
Test fixture for testing physical quantities.
*/
//=============================================================================

trait PhysicalQuantityFixture {

//-----------------------------------------------------------------------------
/**
List of good values, in associated SI units, that ought to be capable of valid
measurement constructor.

@return List of values all of which should be capable of valid construction
when expressed in SI units.
*/
//-----------------------------------------------------------------------------

  final def validValues = getValidValues

//-----------------------------------------------------------------------------
/**
Override to customize the list of good values for a class.

@note Values returned should be at the boundaries of acceptable ranges. Values
within the range are generally not useful.

@return List of customized values all of which should be capable of valid
construction when expressed in SI units.
*/
//-----------------------------------------------------------------------------

  def getValidValues: List [Double]

//-----------------------------------------------------------------------------
/**
List of bad values, in associated SI units, that ought to be incapable of valid
measurement constructor, and which should result in an exception being thrown.

@note Specific rangeless values (such as ''NaN'', Values returned should be at the boundaries of acceptable ranges or
should be specific rangeless values (such as NaN, etc.).

@return list of values none of which should be capable of valid construction
when expressed in SI units.
*/
//-----------------------------------------------------------------------------

  final def invalidValues = Double.NaN :: Double.NegativeInfinity ::
  Double.PositiveInfinity :: getInvalidValues
}