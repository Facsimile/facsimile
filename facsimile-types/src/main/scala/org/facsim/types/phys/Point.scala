//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2019, Michael J Allen.
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
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types.phys

import org.facsim.util.requireNonNull

/** Represents a point in world space.
 *
 *  @constructor Create a new point in ''3D world space''.
 *
 *  @param x World ''X'' co-ordinate of this point, measured from the origin along the world ''X''-axis.
 *
 *  @param y World ''Y'' co-ordinate of this point, measured from the origin along the world ''Y''-axis.
 *
 *  @param z World ''Z'' co-ordinate of this point, measured from the origin along the world ''Z''-axis.
 *
 *  @throws NullPointerException if `x`, `y` or `z` is `null`.
 *
 *  @since 0.0
 */
final case class Point(x: Length, y: Length, z: Length) {

  // Sanity checks.
  requireNonNull(x)
  requireNonNull(y)
  requireNonNull(z)

  /** Calculate straight-line distance from this point to `other` point.
   *
   *  @note The calculated distance will always be a positive value.
   *
   *  @param other Point to which a straight-line distance measurement is required.
   *
   *  @return Straight-line distance from this point to `other` point.
   *
   *  @throws NullPointerException if `other` is null.
   *
   *  @since 0.0
   */
  def distanceTo(other: Point): Length = {
    requireNonNull(other)
    Length(
      Math.sqrt(
        Math.pow(other.x.value - x.value, 2) +
        Math.pow(other.y.value - y.value, 2) +
        Math.pow(other.z.value - z.value, 2)
      )
    )
  }

  /** Calculate distance from this point to `other` point measured on the ''X-Y'' plane.
   *
   *  @note The calculated distance will always be a positive value.
   *
   *  @param other Point to which an ''X-Y'' plane distance measurement is required.
   *
   *  @return ''X-Y'' plane distance from this point to `other` point.
   *
   *  @throws NullPointerException if `other` is null.
   *
   *  @since 0.0
   */
  def distanceToXY(other: Point): Length = {
    requireNonNull(other)
    Length(
      Math.sqrt(
        Math.pow(other.x.value - x.value, 2) +
        Math.pow(other.y.value - y.value, 2)
      )
    )
  }

  /** Calculate angle from this point, on ''X-Y'' to `other` point.
   *
   *  The angle is measured on the world ''X-Y'' plane, and is relative to the world ''X''-axis. This angle can be
   *  viewed as the direction of travel required to reach `other` from this point.
   *
   *  @note If this point and `other` are identical, or if `other` lies directly above or below this point, then this
   *  function will return an angle value of zero.
   *
   *  @param other Point to which an ''X-Y'' plane angle is required.
   *
   *  @return ''X-Y'' plane angle from this point to `other` point, in the range [0, ''τ'') ''radians'' ([0, 360)
   *  ''degrees'').
   *
   *  @throws NullPointerException if `other` is null.
   *
   *  @since 0.0
   */
  def angleToXY(other: Point): Angle = {
    requireNonNull(other)
    Angle.atan2(other.y - y, other.x - x).normalize
  }
}

/** Point companion object.
 *
 * @since 0.0
 */
object Point {

  /** World origin.
   *
   * @since 0.0
   */
  val Origin: Point = Point(Length.Zero, Length.Zero, Length.Zero)
}