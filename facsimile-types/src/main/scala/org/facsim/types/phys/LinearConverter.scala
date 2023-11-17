//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
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

/** Linear converter.
 *
 *  Converts physical quantity measurement units of an associated, but unspecified, unit to and from the corresponding
 *  standard _[[http://en.wikipedia.org/wiki/SI SI]]_ units for the unit family. Values are _imported_ (converted to
 *  _SI_ unit values) by multiplying by the specified linear scaling `factor` after subtracting a constant `offset`
 *  value; they are _exported_ (converted from _SI_ unit values) by dividing the value by the same `factor`, then
 *  adding the same `offset` value.
 *
 *  @constructor Create new linear converter from the specified `factor` and `offset`.
 *
 *  @param factor Linear scaling factor to be employed. This value must be finite and cannot be 0 or 1. A value of 0
 *  causes _divide-by-zero_ exceptions when exporting values and implies that the magnitude of all measurement values
 *  is 0. A value of 1 implies that the units are already _SI_ units, since no scaling is performed, in which case
 *  [[OffsetConverter]] should be preferred.
 *
 *  @param offset Constant offset value to be subtracted prior to application of the scaling `factor` on import, and
 *  added after applying the inverse scaling factor on export. This value must be finite and cannot be 0 (in which case,
 *  [[LinearScaleConverter]] is a better option).
 *
 *  @throws IllegalArgumentException if `factor` is `NaN`, infinite, 0 or 1, or if `offset` is `NaN`, infinite or 0.
 */
private[phys] final class LinearConverter(factor: Double, offset: Double)
extends LinearScaleConverter(factor) {

  // Sanity checks. Constant values must be finite and non-zero.
  requireFinite(offset)
  requireValid(offset, offset != 0.0)

  /** @inheritdoc */
  private[phys] override def importValue(value: Double): Double = super.importValue(value - offset)

  /** @inheritdoc */
  private[phys] override def exportValue(value: Double): Double = super.exportValue(value) + offset
}