//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2017, Michael J Allen.
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
// Scala source file belonging to the org.facsim.measure package.
//======================================================================================================================
package org.facsim.measure

import org.facsim.util.{requireFinite, requireValid}

/** Linear converter.
  *
  * Converts physical quantity measurement units of an associated, but unspecified, unit to and from the corresponding
  * standard ''[[http://en.wikipedia.org/wiki/SI SI]]'' units for the unit family. Values are ''imported'' (converted to
  * ''SI'' unit values) by multiplying by the specified linear scaling `factor` and then adding a constant `offset`
  * value; they are ''exported'' (converted from ''SI'' unit values) by subtracting the same `offset` value then
  * dividing the result by the same `factor`.
  *
  * @constructor Create new linear converter from the specified `factor` and `offset`.
  *
  * @param factor Linear scaling factor to be employed. This value must be finite and cannot be 0 or 1. A value of 0
  * causes ''divide-by-zero'' exceptions when exporting values and implies that the magnitude of all measurement values
  * is 0. A value of 1 implies that the units are already ''SI'' units, since no scaling is performed, in which case
  * [[OffsetConverter]] should be preferred.
  *
  * @param offset Constant offset value to be employed. This value must be finite and cannot be 0 (in which case,
  * [[LinearScaleConverter]] is a better option).
  *
  * @throws IllegalArgumentException if `factor` is `NaN`, infinite, 0 or 1, or if constant` is `NaN`, infinite or 0.
  */
private[measure] final class LinearConverter(factor: Double, offset: Double)
extends LinearScaleConverter(factor) {

  // Sanity checks. Constant values must be finite and non-zero.
  requireFinite(offset)
  requireValid(offset, offset != 0.0)

  /** @inheritdoc
    */
  private[measure] override def importValue(value: Double): Double = super.importValue(value) + offset

  /** @inheritdoc
    */
  private[measure] override def exportValue(value: Double): Double = super.exportValue(value - offset)
}