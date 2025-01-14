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

/** Color schemes supported by _Facsimile_ when importing _AutoMod cell_ scenes.
 *
 *  _Cell_ scenes define 16 named colors. However, the definitions of these colors depends upon the scheme utilized.
 *  The supported color schemes are defined by sub-classes of this trait.
 *
 *  @note colors defined in _VRML_ scenes embedded within _cell_ scenes are unaffected by the choice of _cell_
 *  color scheme.
 *
 *  @since 0.0
 */
sealed trait CellColorScheme {

  /** Scheme identifier. */
  private[cell] val id: Int
}

/** Old _AutoMod_ color scheme.
 *
 *  Color scheme in effect for _AutoMod_ versions prior to release 8.0.
 *
 *  @since 0.0
 */
case object OldCellColorScheme
extends CellColorScheme {
  private[cell] override val id = 0
}

/** New _AutoMod_ color scheme.
 *
 *  Color scheme in effect for _AutoMod_ versions since release 8.0.
 *
 *  @since 0.0
 */
case object NewCellColorScheme
extends CellColorScheme {
  private[cell] override val id = 1
}

/** _JavaFX_ color scheme.
 *
 *  Color definitions in _JavaFX_ with equivalent names to the _AutoMod_ colors.
 *
 *  This color scheme is used by default in _Facsimile_ and is recommended to ensure that models look more modern.
 *
 *  @since 0.0
 */
case object JFXCellColorScheme
extends CellColorScheme {
  private[cell] override val id = 2
}
