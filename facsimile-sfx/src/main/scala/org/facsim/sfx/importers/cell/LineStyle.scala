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
// Scala source file belonging to the org.facsim.sfx.importers.cell package.
//======================================================================================================================
package org.facsim.sfx.importers.cell

/** _AutoMod cell_ scene line style definitions.
 *
 *  @note _JavaFX_ (as of release 8.0) does not currently support linestyles in 3D scenes. That said, some versions of
 *  _AutoMod_&mdash;while they support linestyles when rendering a scene&mdash;do not actually preserve linestyle
 *  information in _cell_ files. In any case, all that the _Facsimile cell_ parser can do is to verify the contents
 *  of this field and nothing else.
 */
private[cell] object LineStyle {

  /** Minimum supported linestyle code. */
  private val minCode = 0

  /** Maximum supported linestyle code. */
  private val maxCode = 3

  /** Verify the value of a linestyle field.
   *
   *  Linestyle values are as follows:
   *
   *  0 - Solid
   *  1 - Dashed
   *  2 - Dotted
   *  3 - Halftone
   *
   *  @param ls Linestyle to be verified.
   *
   *  @return `true` if `ls` is a valid linestyle code; `false` otherwise.
   */
  def verify(ls: Int) = ls >= minCode && ls <= maxCode
}