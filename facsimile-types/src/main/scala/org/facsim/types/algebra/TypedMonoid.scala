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
// Scala source file belonging to the org.facsim.types.algebra types.
//======================================================================================================================
package org.facsim.types.algebra

/** Trait for a _[[https://en.wikipedia.org/wiki/Monoid monoid]]_.
 *
 *  A _monoid_ is a _[[https://en.wikipedia.org/wiki/Semigroup semigroup]]_ with a two-sided _identity_. A
 *  _typed_ monoid identifies a neutral identity value and type, such that when combined with another value, the
 *  result is that value.
 *
 *  @tparam I Type of the [[org.facsim.types.algebra.TypedMonoid.Identity Identity]] value, such that when the identity
 *  value (of this type) is combined with any other value, it returns that value.
 */
private[algebra] sealed trait TypedMonoid[I] {

  /** Identity value.
   *
   *  @note This value (and its type) must be chosen such that when combined with another value, the result is that
   *  other value, without changing that value's type.
   *
   *  @since 0.0
   */
  val Identity: I
}

/** A _typed [[https://en.wikipedia.org/wiki/Monoid monoid]]_ whose identity type is the same as the first set of
 *  values.
 *
 *  @tparam A Type representing the first set of values that may be combined, which is also the type for the result of
 *  all combine operations.
 *
 *  @tparam B Type representing the second set of values that may be combined. This is also the type of the identity
 *  value; consequently, it must be neutral in that, when combined with a value from set `A`, the result is also from
 *  set `A`.
 *
 *  @since 0.0
 */
trait TypedMonoidA[A, B]
extends TypedSemigroup[A, B, A]
with TypedMonoid[B]

/** A _typed [[https://en.wikipedia.org/wiki/Monoid monoid]]_ whose identity type is the same as the first set of
 *  values.
 *
 *  @tparam A Type representing the first set of values that may be combined. This is also the type of the identity
 *  value; consequently, it must be neutral in that, when combined with a value from set `B`, the result is also from
 *  set `B`.
 *
 *  @tparam B Type representing the first set of values that may be combined, which is also the type for the result of
 *  all combine operations.
 *
 *  @since 0.0
 */
trait TypedMonoidB[A, B]
extends TypedSemigroup[A, B, B]
with TypedMonoid[A]