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

/** _AutoMod cell_ element flags.
 *
 *  Flags indicating the configuration of the associated _cell_ element. Certain of these flags determine which
 *  _cell_ records are present in the definition.
 *
 *  @note Only those flags that affect the parsing of
 *  @param flags Bit-field containing _cell_ configuration flags.
 */
private[cell] final class CellFlags(flags: Int) {

  /** Is the _cell attributes_ record present.
   *
   *  @return `true` if the associated _cell_ definition includes an _attributes_ record.
   */
  def attributesPresent: Boolean = (flags & 0x1) != 0

  /** Is the _cell joint data_ record present.
   *
   *  @return `true` if the associated _cell_ definition includes a _joint data_ record.
   */
  def jointDataPresent: Boolean = (flags & 0x2) != 0

  /** Is the _cell transformation_ record present.
   *
   *  @return `true` if the associated _cell_ definition includes a _transformation_ record.
   */
  def transformationPresent: Boolean = (flags & 0x4) != 0

  /** Is the _cell transformation_ record in matrix form.
   *
   *  @return `true` if the associated _cell_ definition's _transformation_ record is in matrix form.
   */
  def transformationInMatrixForm: Boolean = (flags & 0x8) != 0

  /** Does the associated _cell_ inherit its color from its parent element.
   *
   *  @return `true` if the associated _cell_ definition inherits its color from its parent.
   */
  def inheritColor: Boolean = (flags & 0x10) != 0

  /** Is the _cell bounding box_ record present.
   *
   * @return `true` if the associated _cell_ definition includes a _bounding box_ record.
   */
  def boundingBoxPresent: Boolean = (flags & 0x40) != 0
}