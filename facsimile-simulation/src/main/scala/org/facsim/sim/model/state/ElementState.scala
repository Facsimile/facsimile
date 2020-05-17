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

//======================================================================================================================
// Scala source file belonging to the org.facsim.sim.model.state package.
//======================================================================================================================
package org.facsim.sim.model.state

import org.facsim.sim.model.structure.Element
import org.facsim.sim.model.{Point, Rotation}
import scala.reflect.runtime.universe.TypeTag

// TEMPORARY NOTE:
//
// Scalastyle/Scalariform parses an error on this file ("next on empty iterator"), so disable Scalastyle for this file;
// we can re-enable it when Scalastyle is updated.
//
// #SCALASTYLE_BUG
//scalastyle:off

/** State of the associated element.
 *
 *  @tparam E Type of element whose state is being stored.
 *
 *  @tparam S Final type of the element state sub-class.

 *  @since 0.0
 */
abstract class ElementState[E <: Element[E, S]: TypeTag, S <: ElementState[E, S]: TypeTag] {

  /** Child elements, mapped by name.
   *
   *  @since 0.0
   */
  val children: Map[String, Element[E, S]]

  /** Origin of this element, relative to its parent.
   *
   *  @note Positioning of the element is applied before any rotations.
   *
   *  @since 0.0
   */
  val origin: Point

  /** Alignment of this element, relative to it's parent.
   *
   *  Alignment is defined as a set of rotations that are applied to the local axes of the element, in order,
   *  resulting in the local axes for the children of this element.
   *
   *  If the alignment is empty, then all children have the same alignment as this element.
   *
   *  @since 0.0
   */
  val alignment: Seq[Rotation]
}