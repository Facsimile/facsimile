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

import scala.util.Try

/** Trait for implicit objects allowing values of the associated type to be multiplied or divided by unitless factors.
 *
 *  @tparam A Type of value being multiplied and/or divided.
 *
 *  @since 0.0
 */
trait FactorMultiply[A] {

  /** Multiply operator.
   *
   *  @param a First value to be multiplied.
   *
   *  @param b Second value to be multiplied.
   *
   *  @return Result of the multiplication of `a` by `b`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise.
   *
   *  @since 0.0
   */
  def multiply(a: A, b: Double): Try[A]

  /** Multiply operator.
   *
   *  @param a First value to be multiplied.
   *
   *  @param b Second value to be multiplied.
   *
   *  @return Result of the multiplication of `a` by `b`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise.
   *
   *  @since 0.0
   */
  final def multiply(a: Double, b: A): Try[A] = multiply(b, a)

  /** Multiply operator.
   *
   *  @param at First value to be multiplied, wrapped in a [[scala.util.Try]].
   *
   *  @param b Second value to be multiplied.
   *
   *  @return Result of the multiplication of `a` by `b`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise.
   *
   *  @since 0.0
   */
  final def multiply(at: Try[A], b: Double): Try[A] = at.flatMap(a => multiply(a, b))

  /** Multiply operator.
   *
   *  @param a First value to be multiplied.
   *
   *  @param b Second value to be multiplied, wrapped in a [[scala.util.Try]].
   *
   *  @return Result of the multiplication of `a` by `b`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise.
   *
   *  @since 0.0
   */
  final def multiply(a: Double, b: A): Try[A] = multiply(b, a)

  /** Addition operator.
   *
   *  @param a First value to be added.
   *
   *  @param bt Second value to be added, wrapped in a [[scala.util.Try]].
   *
   *  @return Result of the addition of `bt` to `a`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise. If `bt` is a failure, then its failure exception will be preserved.
   *
   *  @since 0.0
   */
  final def add(a: A, bt: Try[A]): Try[A] = bt.flatMap(b => add(a, b))

  /** Addition operator.
   *
   *  @param at First value to be added, wrapped in a [[scala.util.Try]].
   *
   *  @param b Second value to be added.
   *
   *  @return Result of the addition of `b` to `at`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise. If `at` is a failure, then its failure exception will be preserved.
   *
   *  @since 0.0
   */
  final def add(at: Try[A], b: A): Try[A] = at.flatMap(a => add(a, b))

  /** Addition operator.
   *
   *  @param at First value to be added, wrapped in a [[scala.util.Try]].
   *
   *  @param bt Second value to be added, wrapped in a [[scala.util.Try]].
   *
   *  @return Result of the addition of `bt` to `at`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise. If `at` is a failure, then its failure exception will be preserved; otherwise,
   *  if `bt` is a failure, then its failure will be preserved.
   *
   *  @since 0.0
   */
  final def add(at: Try[A], bt: Try[A]): Try[A] = at.flatMap(a => bt.flatMap(b => add(a, b)))

  /** Subtraction operator.
   *
   *  @param a First value to be subtracted.
   *
   *  @param b Second value to be subtracted.
   *
   *  @return Result of the subtraction of `b` from `a`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise.
   *
   *  @since 0.0
   */
  def subtract(a: A, b: A): Try[A]

  /** Subtraction operator.
   *
   *  @param a First value to be subtracted.
   *
   *  @param bt Second value to be subtracted, wrapped in a [[scala.util.Try]].
   *
   *  @return Result of the subtraction of `bt` from `a`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise. If `bt` is a failure, then its failure exception will be preserved.
   *
   *  @since 0.0
   */
  final def subtract(a: A, bt: Try[A]): Try[A] = bt.flatMap(b => subtract(a, b))

  /** Subtraction operator.
   *
   *  @param at First value to be subtracted, wrapped in a [[scala.util.Try]].
   *
   *  @param b Second value to be subtracted.
   *
   *  @return Result of the subtraction of `b` from `at`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise. If `at` is a failure, then its failure exception will be preserved.
   *
   *  @since 0.0
   */
  final def subtract(at: Try[A], b: A): Try[A] = at.flatMap(a => subtract(a, b))

  /** Subtraction operator.
   *
   *  @param at First value to be subtracted, wrapped in a [[scala.util.Try]].
   *
   *  @param bt Second value to be subtracted, wrapped in a [[scala.util.Try]].
   *
   *  @return Result of the subtraction of `bt` from `at`, wrapped in a [[scala.util.Success]] if successful, or a
   *  [[scala.util.Failure]] otherwise. If `at` is a failure, then its failure exception will be preserved; otherwise,
   *  if `bt` is a failure, then its failure will be preserved.
   *
   *  @since 0.0
   */
  final def subtract(at: Try[A], bt: Try[A]): Try[A] = at.flatMap(a => bt.flatMap(b => subtract(a, b)))
}