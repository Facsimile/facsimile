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

/** _AutoMod cell_ scene line width definitions.
 *
 *  Line widths specify the number of pixels used to draw lines in _AutoMod cell_ scenes. Values other than 1 result
 *  in peculiar looking scenes and are typically avoided.
 *
 *  @note _JavaFX_ (as of release 8.0) does not currently support explicit pixel line widths in 3D scenes. All that
 *  the _Facsimile cell_ parser can do is to verify the contents of this field and nothing else.
 */
private[cell] object LineWidth {

  /** Minimum supported line width value. */
  private val minCode = 1

  /** Maximum supported line width value. */
  private val maxCode = 7

  /** Verify the value of a line width field.
   *
   *  Valid line widths are in the range [1, 7].
   *
   *  @param lw Line width to be verified.
   *
   *  @return `true` if `lw` is a valid line width value; `false` otherwise.
   */
  def verify(lw: Int): Boolean = lw >= minCode && lw <= maxCode
}