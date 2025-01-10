//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================

//======================================================================================================================
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types.phys

import org.facsim.util.{requireFinite, requireValid}

/** Offset converter.
 *
 *  Converts physical quantity measurement units of an associated, but unspecified, unit to and from the corresponding
 *  standard _[[http://en.wikipedia.org/wiki/SI SI]]_ units for the unit family. Values are _imported_ (converted to
 *  _SI_ unit values) by subtracting the specified constant `offset` value; they are _exported_ (converted from
 *  _SI_ unit values) by adding the same constant `offset`.
 *
 *  @constructor Create new offset converter from the specified `offset`.
 *
 *  @param offset Constant value that, when subtracted from specific measurements in the corresponding non-_SI_ units,
 *  converts those measurements to _SI_ units. `offset` must be finite and cannot be zero (in which case,
 *  [[SIConverter]] is a better option).
 *
 *  @throws IllegalArgumentException if `offset` is `NaN`, infinite, or zero.
 */
private[phys] final class OffsetConverter(offset: Double)
extends Converter {

  // Sanity checks. Constant values must be finite and non-zero.
  requireFinite(offset)
  requireValid(offset, offset != 0.0)

  /** @inheritdoc */
  private[phys] override def importValue(value: Double): Double = value - offset

  /** @inheritdoc */
  private[phys] override def exportValue(value: Double): Double = value + offset
}