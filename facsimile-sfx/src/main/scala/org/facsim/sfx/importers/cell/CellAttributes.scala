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

import javafx.scene.paint.Material
import scala.collection.mutable.Map

/** Stores _AutoMod cell_ attribute information for the current _cell_ element.
 *
 *  @note A number of cell _attributres_ are unsupported by _JavaFX_ release 8, the version currently used by
 *  _Facsimile_. These are edge color, line style and line width. To add them should they become supported by a future
 *  release, it will be necessary to modify this class and its interface. Hence, do not expose this class to user code.
 *
 *  @constructor Create a new _cell_ attribute record.
 *
 *  @param material Material to be applied to the element.
 *
 *  @param opacity Opacity of the specified element, wrapped in [[scala.Some]]; if [[scala.None]], then the element is
 *  to be drawn as wireframe.
 *
 *  @param id Optional identifier of the associated cell element.
 */
private[cell] final case class CellAttributes(material: Material, opacity: Option[Double], id: Option[String])

/** _Cell_ attribute companion. */
private[cell] object CellAttributes {

  /** Cached default attributes. */
  private lazy val defaultAttributes = Map.empty[CellColorScheme, CellAttributes]

  /** Default attribute instance for use when attributes are not present.
   *
   *  The default attributes utilize the default color, full opacity and have no defined name.
   *
   *  @note This operation is synchronized to ensure thread-safety.
   *
   *  @param scheme Requested color scheme.
   *
   *  @return Default attribute instance for specified color `scheme`.
   */
  def default(scheme: CellColorScheme): CellAttributes = synchronized {

    // Report the corresponding attributes for this color scheme, creating them if necessary.
    defaultAttributes.getOrElseUpdate(scheme,
    CellAttributes(CellColor(CellColor.Default.id, scheme), DisplayStyle(DisplayStyle.Default), None))
  }
}