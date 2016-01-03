/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
Scala source file from the org.facsim.measure.test package.
*/
//=============================================================================

package org.facsim.measure.test

import org.facsim.measure.Family
import org.facsim.measure.Specific

//=============================================================================
/**
Test fixture trait for all [[org.facsim.measure.Specific$]] subclasses.

@tparam Q The `Specific` subclass being tested.
*/
//=============================================================================

trait SpecificFixture [Q <: Specific]
extends PhysicalFixture [Q] {

//-----------------------------------------------------------------------------
/**
Retrieve the expected family that this specific physical quantity should
report.

@return Expected physical quantity family.
*/
//-----------------------------------------------------------------------------

  def expectedFamily: Family

//-----------------------------------------------------------------------------
/**
List of bad measurement values, in associated SI units, that ought to be
incapable of valid measurement construction, and which should result in an
exception being thrown.

@note This function ought to provide values that test the boundaries of
acceptable ranges. For example, if valid measurements are acceptable over the
range [0, 1], then both -[[scala.Double.MinPositiveValue]] and 1 +
[[scala.Double.MinPositiveValue]] should be included as invalid values.

@note This function should not include non-finite values, which are tested
separately.

@return List of bad values. None of the bad values should be capable of valid
construction when expressed in SI units. If there are no bad values, then an
empty list should be returned.
*/
//-----------------------------------------------------------------------------

  def invalidValues: List [Double]

//-----------------------------------------------------------------------------
/**
List of good measurement values, in associated SI units, that ought to be
capable of valid measurement construction.

@note This function ought to provide values that test the boundaries of
acceptable ranges. For example, if valid measurements are acceptable over the
range [0, 1], then both 0 and 1 should be included as valid values.

@return List of values all of which should be capable of valid construction
when expressed in SI units.
*/
//-----------------------------------------------------------------------------

  def validValues: List [Double]
}