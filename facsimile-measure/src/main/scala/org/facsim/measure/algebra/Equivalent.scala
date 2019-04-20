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

// Scala source file belonging to the org.facsim.measure.algebra package.
//======================================================================================================================
package org.facsim.measure.algebra

/** Trait for implicit objects allowing the associated type to be compared for equivalence.
 *
 *  Associated objects can be compared for equality and inequality implicitly.
 *
 *  @tparam A Type of objects to be compared.
 *
 *  @since 0.0
 */
trait Equivalent[A] {

  /** Equivalence function.
   *
   *  @param a First value to be compared for equivalence; this value cannot be `null`.
   *
   *  @param b Second value to be compared for equivalence; this value cannot be `null`.
   *
   *  @note If this function returns `true`, then the [[hashCode]] values of both objects must also be identical.
   *
   *  @return `true` if `a` and `b` are equivalent; `false` if they are not equivalent.
   *
   *  @throws scala.NullPointerException if `a` is `null` or if `b` is `null`.
   *
   *  @since 0.0
   */
  def eqv(a: A, b: A): Boolean

  /** Non-equivalence function.
   *
   *  @param a First value to be compared for non-equivalence; this value cannot be `null`.
   *
   *  @param b Second value to be compared for non-equivalence; this value cannot be `null`.
   *
   *  @note If this function returns `false`, then the [[hashCode]] values of both objects must be equal.
   *
   *  @return `true` if `a` and `b` are not equivalent; `false` if they are equivalent.
   *
   *  @throws scala.NullPointerException if `a` is `null` or if `b` is `null`.
   *
   *  @since 0.0
   */
  final def neqv(a: A, b: A): Boolean = !eqv(a, b)
}