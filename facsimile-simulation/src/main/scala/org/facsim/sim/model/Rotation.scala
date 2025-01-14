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
// Scala source file belonging to the org.facsim.sim.model package.
//======================================================================================================================
package org.facsim.sim.model

import squants.space.Angle

/** Base trait for all rotations.
 *
 *  @since 0.0
 */
sealed trait Rotation:

  /** Rotation about the associated axis.
   *
   *  Positive values represent counter-clockwise rotation, negative values represent clockwise rotation.
   *
   *  @since 0.0
   */
  val angle: Angle

/** Local X-axis rotation.
 *
 *  A rotation about the local X-axis.
 *
 *  @constructor Create a new rotation about the local X-axis.
 *
 *  @param angle Rotation about the local X-axis, positive values represent counter-clockwise rotation, negative values
 *  represent clockwise rotation.
 *
 *  @since 0.0
 */
final case class XAxisRotation(override val angle: Angle)
extends Rotation

/** Local Y-axis rotation.
 *
 *  A rotation about the local Y-axis.
 *
 *  @constructor Create a new rotation about the local Y-axis.
 *
 *  @param angle Rotation about the local Y-axis, positive values represent counter-clockwise rotation, negative values
 *  represent clockwise rotation.
 *
 *  @since 0.0
 */
final case class YAxisRotation(override val angle: Angle)
extends Rotation

/** Local Z-axis rotation.
 *
 *  A rotation about the local Z-axis.
 *
 *  @constructor Create a new rotation about the local Z-axis.
 *
 *  @param angle Rotation about the local Z-axis, positive values represent counter-clockwise rotation, negative values
 *  represent clockwise rotation.
 *
 *  @since 0.0
 */
final case class ZAxisRotation(override val angle: Angle)
extends Rotation