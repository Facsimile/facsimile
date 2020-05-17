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
// Scala source file belonging to the org.facsim.util.test types.
//======================================================================================================================
package org.facsim.types.test

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/** Fixture trait for testing if an associated object implements the ''equals contract''.
 *
 *  @tparam V Type, implementing the [[Equivalent]] class, to be tested.
 */
trait EqualsFixture[V <: Equivalent[V]] {

  // Sanity checks on the reported fixtures.
  assert(valueSample.tail.nonEmpty) // Checks we have at least two elements.
  assert(valueSample.size == valueSample.toSet.size) // Checks for uniqueness.
  assert(nonValueSample.forall(value => value.getClass != valueSample.head.getClass)) // Checks different value type.

  /** Return a list of lists, such that every value in the inner list should compare equal.
   *
   *  @return List of list of equal values.
   */
  def equalListSample: List[List[V]]

  /** Return a sample list of values of a type different to `V`.
   *
   *  @note None of the values in this list should be of type `V`. However, to improve the thoroughness of the testing,
   *  if possible the contents of these objects should match the contents of some of the objects in
   *  [[EqualsFixture.valueSample]].
   *
   *  @return List of values of a different type to `V`.
   */
  def nonValueSample: List[Any]

  /** Return a sample list of unique values.
   *
   *  @note All of the values in this list should be of type `V` (or, to be more explicit, their `canEqual` methods
   *  should return `true` for each value in this list). Duplicate values are not permitted. Further, there must be at
   *  least 2 values in the list.
   *
   *  @return List of unique values to be tested.
   */
  final def valueSample: List[V] = equalListSample.map(_.head)
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc