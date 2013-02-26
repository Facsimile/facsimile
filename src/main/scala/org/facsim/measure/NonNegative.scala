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

import org.facsim.requireValid

//=============================================================================
/**
Abstract base class for all physical quantity types that cannot have negative
measurement values in the associated ''[[http://en.wikipedia.org/wiki/SI SI]]''
units.

Subclasses are forbidden from having negative measurement values, which is
appropriate for a number of unit families, including
[[org.facsim.measure.Time$]], [[org.facsim.measure.Temperature$]],
[[org.facsim.measure.Mass$]], etc.

@since 0.0
*/
//=============================================================================

abstract class NonNegative extends Specific {

/**
@inheritdocs
*/

  override type Measure <: NonNegativeMeasure

/**
@inheritdocs
*/

  override type Units <: NonNegativeUnits

//-----------------------------------------------------------------------------
/**
Base class for physical quantity measurements that cannot be negative.

@constructor Construct new non-negative measurement value.

@param value Value of the measurement type in the associated ''SI'' units.
This value must be finite and non-negative.  Sub-classes may impose additional
restrictions.

@throws java.lang.IllegalArgumentException If the result is not finite or is
negative.

@since 0.0
*/
//-----------------------------------------------------------------------------

  abstract class NonNegativeMeasure private [measure] (value: Double) extends
  SpecificMeasure (value) {

/*
If the value is negative, then report an error.
*/

    requireValid ("value", value, value >= 0.0)
  }

//-----------------------------------------------------------------------------
/**
@constructor Construct new non-negative measurement units.
*/
//-----------------------------------------------------------------------------

  abstract class NonNegativeUnits private [measure] (converter: Converter,
  symbol: String) extends SpecificUnits (converter, symbol)
}