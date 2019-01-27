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
// Scala source file belonging to the org.facsim.sfx.importers.cell package.
//======================================================================================================================
package org.facsim.sfx.importers.cell

/** ''AutoMod cell'' element flags.
 *
 *  Flags indicating the configuration of the associated ''cell'' element. Certain of these flags determine which
 *  ''cell'' records are present in the definition.
 *
 *  @note Only those flags that affect the parsing of
 *  @param flags Bit-field containing ''cell'' configuration flags.
 */
private[cell] final class CellFlags(flags: Int) {

  /** Is the ''cell attributes'' record present.
   *
   *  @return `true` if the associated ''cell'' definition includes an ''attributes'' record.
   */
  def attributesPresent: Boolean = (flags & 0x1) != 0

  /** Is the ''cell joint data'' record present.
   *
   *  @return `true` if the associated ''cell'' definition includes a ''joint data'' record.
   */
  def jointDataPresent: Boolean = (flags & 0x2) != 0

  /** Is the ''cell transformation'' record present.
   *
   *  @return `true` if the associated ''cell'' definition includes a ''transformation'' record.
   */
  def transformationPresent: Boolean = (flags & 0x4) != 0

  /** Is the ''cell transformation'' record in matrix form.
   *
   *  @return `true` if the associated ''cell'' definition's ''transformation'' record is in matrix form.
   */
  def transformationInMatrixForm: Boolean = (flags & 0x8) != 0

  /** Does the associated ''cell'' inherit its color from its parent element.
   *
   *  @return `true` if the associated ''cell'' definition inherits its color from its parent.
   */
  def inheritColor: Boolean = (flags & 0x10) != 0

  /** Is the ''cell bounding box'' record present.
   *
   * @return `true` if the associated ''cell'' definition includes a ''bounding box'' record.
   */
  def boundingBoxPresent: Boolean = (flags & 0x40) != 0
}