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

//======================================================================================================================
// Scala source file belonging to the org.facsim.collection.immutable package.
//======================================================================================================================
package org.facsim.collection.immutable

/** Trait defining an _immutable [[https://en.wikipedia.org/wiki/Heap_(data_structure) heap]]._
 *
 *  This is based upon the heap interface defined in the paper _[[http://www.brics.dk/RS/96/37/BRICS-RS-96-37.pdf
 *  Optimal Purely Functional Priority Queues]]_.
 *
 *  @tparam A Type of element to be stored in the heap. There must be an implicit ordering available for this element
 *  type.
 *
 *  @tparam H Type of the heap.
 *
 *  @since 0.0
 */
trait Heap[A, H <: Heap[A, H]]
extends Equals {

  /** Determine whether this heap is empty, and has no elements.
   *
   *  @return `true` if this heap is empty, `false` if it contains at least one element.
   *
   *  @since 0.0
   */
  def isEmpty: Boolean

  /** Determine whether this heap has elements, and is not empty.
   *
   *  @return `true` if this heap contains at least one element, `false` if it is empty.
   *
   *  @since 0.0
   */
  final def nonEmpty: Boolean = !isEmpty

  /** Add an element to the heap, resulting in a new heap.
   *
   *  @param a Element to be added to the heap.
   *
   *  @return New heap containing all of the original elements, plus `a`.
   *
   *  @since 0.0
   */
  def +(a: A): H

  /** Merge another heap with this heap, resulting a new heap.
   *
   *  @param h Heap to be merged with this heap.
   *
   *  @return New heap containing all of the elements in the original heap, plus all of the elements in `h`.
   *
   *  @since 0.0
   */
  def ++(h: H): H

  /** Find the minimum value stored in the heap.
   *
   *  @return Minimum value stored in the heap, as determined by that element's ordering, wrapped in [[scala.Some]]; or
   *  [[scala.None]] if this heap is empty.
   *
   *  @since 0.0
   */
  def minimum: Option[A]

  /** Find the minimum value stored in the heap, and remove it, resulting in a new heap.
   *
   *  @note This is a more efficient operation than calling `[[minimum]]` then `[[removeMinimum]]`, if both operations
   *  are required simultaneously.
   *
   *  @return Tuple whose first member is the minimum value of the heap, as determined by that element's ordering,
   *  wrapped in `[[scala.Some Some]]`; or `[[scala.None None]]` if this heap is empty. The second tuple member is a new
   *  heap with the minimum value removed (if the first member is defined), or the original empty heap otherwise.
   *
   *  @since 0.0
   */
  def minimumRemove: (Option[A], H)

  /** Remove the minimum value from the heap, resulting in a new heap.
   *
   *  @return New heap with its minimum value removed, wrapped in `[[scala.Some Some]]`; or `[[scala.None None]]` if
   *  this heap is empty.
   *
   *  @since 0..0
   */
  final def removeMinimum: Option[H] = minimumRemove match {
    case(mo, h) => mo.map(_ => h)
  }
}