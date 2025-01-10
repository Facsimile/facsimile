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

//======================================================================================================================
// Scala source file belonging to the org.facsim.types.properties types.
//======================================================================================================================
package org.facsim.types.properties

/** Marker trait for operators exhibiting the _[[https://en.wikipedia.org/wiki/Commutative_property commutative]]_
 *  property.
 *
 *  Instances of this trait support _commutative_ operations. That is, given some operation `op`, `op(a, b)` must
 *  equal `op(b, a)` for all `a` and `b`.
 *
 *  @note This trait is primarily used for _testing_ that subclass operator implementations are indeed commutative; it
 *  typically plays no role in the implementation itself.
 *
 *  @tparam A Type of one argument to the commutative operation.
 *
 *  @tparam B Type of the other argument to the commutative operation.
 *
 *  @tparam R Type of the result of the commutative operation.
 *
 *  @since 0.0
 */
trait Commutative[A, B, R] {

  /** Forward commutative operation. */
  private[types] val cOpF: (A, B) => R

  /** Reverse commutative operation. */
  private[types] val cOpR: (B, A) => R

  /** Test commutativity for an arbitrary pair of values.
   *
   *  @param a First argument to be tested.
   *
   *  @param b Second argument to be tested.
   *
   *  @return `true` if the result of the operation is the same, regardless of argument order; `false` otherwise.
   */
  private[types] final def checkCommutative(a: A, b: B) = cOpF(a, b) == cOpR(b, a)
}
