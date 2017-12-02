//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2017, Michael J Allen.
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

// Scala source file belonging to the org.facsim.measure.algebra package.
//======================================================================================================================
package org.facsim.measure.algebra

/** Trait for a _[[https://en.wikipedia.org/wiki/Monoid monoid]]_.
 *
 *  A _monoid_ is a _[[https://en.wikipedia.org/wiki/Semigroup semigroup]]_ with a two-sided _identity_.
 *
 *  @tparam A Type representing the set of values to which the monoid is applicable.
 *
 *  @since 0.0
 */
trait Monoid[A]
extends Any
with Semigroup[A] {

  /** Identity value.
   *
   *  This value must be chosen such that `combine(a, Identity)` equals `a` and `combine(Identity, a)` equals `a` also,
   *  for all `a`.
   */
  val Identity: A
}