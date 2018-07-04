//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2018, Michael J Allen.
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
// Scala source file belonging to the org.facsim.sfx.importers.cell package.
//======================================================================================================================
package org.facsim.sfx.importers.cell

import javafx.geometry.Point3D
import javafx.scene.transform.Rotate.{X_AXIS, Y_AXIS, Z_AXIS}

/** ''AutoMod cell'' rotation order definitions.
 *
 *  Each rotation order code identifies a sequence of axes, in a unique order, to which three axis rotations will be
 *  applied.
 */
private[cell] object RotationOrder {

  /** Minimum supported rotation order code. */
  private val minCode = 0

  /** Maximum supported rotation order code. */
  private val maxCode = 5

  /** Verify the value of a rotation order code.
   *
   * @param ro Rotation order code to be verified.
   *
   * @return `true` if `ro` is a valid rotation order code; `false` otherwise.
   */
  def verify(ro: Int): Boolean = ro >= minCode && ro <= maxCode

  /** Convert a rotation order to a sequence of axes.
   *
   *  @param ro Rotation order code to be processed.
   *
   *  @return A sequence of ''3D'' axes denoting the order in which a set of rotations is to be applied to the current
   *  ''cell'' element.
   *
   *  @throws scala.MatchError if `ro` is not a valid rotation order code.
   */
  //scalastyle:off magic.number
  def apply(ro: Int): Seq[Point3D] = {

    // Match the code
    case 0 => Seq(X_AXIS, Y_AXIS, Z_AXIS)
    case 1 => Seq(X_AXIS, Z_AXIS, Y_AXIS)
    case 2 => Seq(Y_AXIS, X_AXIS, Z_AXIS)
    case 3 => Seq(Y_AXIS, Z_AXIS, X_AXIS)
    case 4 => Seq(Z_AXIS, X_AXIS, Y_AXIS)
    case 5 => Seq(Z_AXIS, Y_AXIS, X_AXIS)
  }
  //scalastyle:on magic.number
}