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
// Scala source file belonging to the org.facsim.types.properties types.
//======================================================================================================================
package org.facsim.types.properties

/** Marker trait for operators exhibiting the _[[https://en.wikipedia.org/wiki/Associative_property associative]]_
 *  property.
 *
 *  Instances of this trait support ''associative'' operations. That is, given some operation `op`, `op(a, op(b, c))`
 *  must equal `op(op(a, b), c)` for all `a`, `b` and `c`.
 *
 *  @note This trait is primarily used for _testing_ that subclass operator implementations are indeed associative; it
 *  typically plays no role in the implementation itself.
 *
 *  @tparam A Type of one argument to the associative operation.
 *
 *  @tparam B Type of another argument to the associative operation.
 *
 *  @tparam C Type of final argument to the associative operation.
 *
 *  @tparam R Type of the result of the associative operation.
 *
 *  @since 0.0
 */
trait Associative[A, B, C, R] {

  /** First form of the operation. */
  private[types] val aOpAB: (A, B) => R

  /** Second form of the operation. */
  private[types] val aOpRC: (R, C) => R

  /** Third form of the operation. */
  private[types] val aOpAR: (A, R) => R

  /** Fourth form of the operation */
  private[types] val aOpBC: (B, C) => R

  /** Test associativity for an arbitrary set of values.
   *
   *  @param a First argument to be tested.
   *
   *  @param b Second argument to be tested.
   *
   *  @param c Third argument to be tested.
   *
   *  @return `true` if the result of the operation is the same, regardless of invocation order; `false` otherwise.
   */
  private[types] final def checkAssociative(a: A, b: B, c: C) = aOpRC(aOpAB(a, b), c) == aOpAR(a, aOpBC(b, c))
}