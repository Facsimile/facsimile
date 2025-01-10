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
// Scala source file belonging to the org.facsim.sfx.importers.cell package.
//======================================================================================================================
package org.facsim.sfx.importers.cell

/** _Cell_ joint type. */
private[cell] sealed trait JointType

/** _AutoMod cell_ scene display style definitions.
 *
 *  A display style code of 0 indicates that the associated _cell_ primitive should be drawn as in wireframe mode. All
 *  other display style codes are solid modes with varying levels of transparency: the value 1 indicates fully opaque,
 *  with 16 being almost invisible.
 *
 * @note _JavaFX_ Supports wireframe and solid drawing modes, as well as transparency (albeit largely buggy and not
 *  working  as of _JavaFX_ 8).
 */
private[cell] object JointType {

  /** Vector of joint type codes to joint types. */
  private val joints: Vector[JointType] = Vector(
    NotAJoint,
    TranslationalJoint,
    RotationalJoint,
  )

  /** Verify the value of a joint type field.
   *
   *  Values that correspond to indices in the joint type vector are valid.
   *
   *  @param jt Joint type code to be verified.
   *
   *  @return `true` if `jt` is a valid joint type code; `false` otherwise.
   */
  def verify(jt: Int): Boolean = joints.contains(jt)

  /** Convert a joint style code to a joint type.
   *
   *  @param jt Joint type code to be processed.
   *
   *  @return A corresponding joint type.
   *
   *  @throws scala.IndexOutOfBoundsException if `jt` is not a valid joint type code.
   */
  def apply(jt: Int): JointType = joints(jt)
}

/** Type indicating this isn't a joint.
 *
 *  @note This type is typically used for _terminal control frame_ (_TCF_) definitions. However, each _TCF_ can
 *  also be a joint as well.
 */
private[cell] case object NotAJoint
extends JointType

/** Translational joint type.
 *
 *  Type of joint in which values are translations along a specified axis.
 */
private[cell] case object TranslationalJoint
extends JointType

/** Rotational joint type.
 *
 *  Type of joint in which values are rotations about specified axis.
 */
private[cell] case object RotationalJoint
extends JointType