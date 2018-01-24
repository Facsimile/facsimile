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
// Scala source file belonging to the org.facsim.util package.
//======================================================================================================================
package org.facsim.util

import java.text.ParseException

/** Signaled if a string containing a ''version'' number could not be parsed.
 *
 *  @constructor Create a new ''version parse'' exception.
 *
 *  @param version Version string that could not be parsed. This value cannot be `null`.
 *
 *  @param offset Position within version at which the parser failed. This value must be within the range [0,
 *  version.length].
 *
 *  @throws scala.NullPointerException if `name` is null.
 *
 *  @throws scala.IllegalArgumentException if `offset` does not identify a position within `name`.
 *
 *  @since 0.0
 */
final case class VersionParseException(version: String, offset: Int)
extends ParseException(null, offset) { //scalastyle:ignore null

  // Sanity checks. Clearly, throwing exceptions while creating an exception is likely not desirable, so don't fail!
  requireNonNullFn(version, "version")
  requireValidFn[Int](offset, o => o >= 0 && o <= version.length, "offset")

  /** Report cause of this exception.
   *
   * @return Localised description of the cause of the exception.
   *
   * @since 0.0
   */
  override def getMessage: String = LibResource("VersionParse", version, getErrorOffset)
}