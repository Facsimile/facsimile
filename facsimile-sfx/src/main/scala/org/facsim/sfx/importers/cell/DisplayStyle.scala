//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
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

import org.facsim.util.requireValid

/** ''AutoMod cell'' scene display style definitions.
 *
 *  A display style code of 0 indicates that the associated ''cell'' primitive should be drawn as in wireframe mode. All
 *  other display style codes are solid modes with varying levels of transparency: the value 1 indicates fully opaque,
 *  with 16 being almost invisible.
 *
 *  @note ''JavaFX'' Supports wireframe and solid drawing modes, as well as transparency (albeit largely buggy and not
 *  working  as of ''JavaFX'' 8).
 */
private[cell] object DisplayStyle {

  /** Minimum supported display style code. */
  private val minCode = 0

  /** Maximum supported display style code. */
  private val maxCode = 16

  /** Default display style code. */
  val Default = 1

  /** Verify the value of a display style field.
   *
   *  Display style values are as follows:
   *
   *  0 - Wireframe
   *  1 - Solid, fully opaque.
   *  ...
   *  16 - Almost invisible.
   *
   * @param ds Display style to be verified.
   *
   * @return `true` if `ls` is a valid display style code; `false` otherwise.
   */
  def verify(ds: Int): Boolean = ds >= minCode && ds <= maxCode

  /** Convert a display style to an opacity value.
   *
   *  @param ds Display style code to be processed.
   *
   *  @return An opacity value in the range (0.0, 1.0] wrapped in [[scala.Some]] if the code indicates that the
   *  associated element is solid, with varying degrees of opacity (1.0 is opaque, 0.0 is invisible); [[scala.None]] if
   *  the element is to be drawn in wireframe mode.
   *
   *  @throws scala.IllegalArgumentException if `ds` is not a valid display style code.
   */
  def apply(ds: Int): Option[Double] = {

    // Sanity check.
    requireValid(ds, verify(ds))

    // If this is the wireframe code, return None.
    if(ds == 0) None

    // Otherwise, calculate the opacity as a fraction of 16 and wrap the result in Some.
    else Some((17.0 - ds) / 16.0)
  }
}