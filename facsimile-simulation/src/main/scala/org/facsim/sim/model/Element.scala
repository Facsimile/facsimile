//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
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

//======================================================================================================================
// Scala source file belonging to the org.facsim.sim.model package.
//======================================================================================================================
package org.facsim.sim.model

/** Common root for all simulation model elements.
 *
 *  A simulation element tracks the state of an element in the simulation. It has a parent, a position and (optionally)
 *  motion.
 *
 *  @note This is sealed to prevent its use as a base class by user code. User elements can extend subclasses only.
 *
 *  @since 0.0
 */
sealed trait BaseElement {

  /** Name of this element.
   *
   *  Element names must be unique in relation to their parent.
   *
   *  @since 0.0
   */
  val name: String

  /** Child elements, mapped by name.
   *
   *  @since 0.0
   */
  val children: Map[String, Element]

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

/** Base simulation model element trait.
 *
 *  @since 0.0
 */
trait Element
extends BaseElement {

  /** Parent of this element.
   *
   *  @since 0.0
   */
  val parent: Element
}

/** Class representing the root element of a simulation.
 *
 *  @param name Name of the simulation's root element.
 *
 *  @param children Children of the simulation's root element.
 *
 *  since 0.0
 */
final case class RootElement(override val name: String, children: Map[String, Element])
extends BaseElement {

  /** @inheritdoc
   *
   *  @note The simulation's root is always at the origin.
   *
   *  @todo Consider changing this in the future, so that an origin can be tracked to a specific location.
   */
  override val origin: Point = Point.Origin

  /** @inheritdoc
   *
   *  @note The simulation's root has it's children aligned with the world axes.
   *
   *  @todo Consider changing this in the future, so that the simulation axes can be tracked differently.
   */
  override val alignment: Seq[Rotation] = Nil
}