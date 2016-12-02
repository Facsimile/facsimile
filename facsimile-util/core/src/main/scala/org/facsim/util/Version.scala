/*
 * Facsimile -- A Discrete-Event Simulation Library
 * Copyright © 2004-2016, Michael J Allen.
 *
 * This file is part of Facsimile.
 *
 * Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
 * http://www.gnu.org/licenses/lgpl.
 *
 * The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
 * project home page at:
 *
 *   http://facsim.org/
 *
 * Thank you for your interest in the Facsimile project!
 *
 * IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
 * inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
 * your code fails to comply with the standard, then your patches will be rejected. For further information, please
 * visit the coding standards at:
 *
 *   http://facsim.org/Documentation/CodingStandards/
 * =====================================================================================================================
 * Scala source file belonging to the org.facsim.util package.
 */
package org.facsim.util

/**
 * Encapsulate software version information, providing information about each version's components.
 *
 * @todo This is a work-in-progress and will change in the near future. Full version comparison will be implemented to
 * permit version ordering, and determinations of whether this is a snapshot version, a pre-release version, etc. For
 * now, we just report back whatever string we're assigned.
 *
 * @constructor Construct new version from raw version string. This will change shortly. The recommended way to create
 * version instances is via [[org.facsim.util.Version.apply(String)*]].
 *
 * @param version Version string storing version information.
 *
 * @since 0.0
 */
final class Version private[facsim](version: String) {

  /**
   * Returns a representation of this object as a String.
   *
   * @return String representation of this object.
   */
  override def toString = version
}

/**
 * Version companion object.
 *
 * @since 0.0
 */
object Version {

  /**
   * Create a new version instance from a version string.
   *
   * @todo For now, the Version instance will be returned regardless of whether the contents of '''version''' encodes a
   * valid version number. When full version number processing is implemented, this will change.
   *
   * @param version Version string storing version information.
   *
   * @return Corresponding version.
   *
   * @since 0.0
   */
  def apply(version: String) = {
    requireNonNull(version)
    new Version(version)
  }
}