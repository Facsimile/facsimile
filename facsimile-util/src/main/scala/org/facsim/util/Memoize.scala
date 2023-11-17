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
// Scala source file belonging to the org.facsim.util package.
//======================================================================================================================
package org.facsim.util

import scala.collection.mutable.Map

/** Base class for _pure function memoization_ classes.
 *
 *  @tparam A Types of argument values (as _tuples_, for multiple argument classes) passed to function `f`.
 *
 *  @tparam R Type of result returned by `f`.
 *
 *  @param f Function to be _memoized_.
 *
 *  @since 0.0
 */
sealed abstract class Memoize[A, R] private(f: A => R):

  /** Map of argument values to results.
   */
  private final val results = Map.empty[A, R]

  /** Evaluate the function.
   *
   *  @note If the function has not been called previously, then it is executed and the result cached; otherwise, the
   *  previously cached result is returned instead. Note that if the function has any _side-effects_, that they will
   *  only occur for the first evaluation.
   *
   *  @param a Argument to be passed to the function.
   *
   *  @return Result of `f(a)`.
   */
  protected final def eval(a: A): R = synchronized(results.getOrElseUpdate(a, f(a)))

/** Memoization companion.
 *
 *  @since 0.0
 */
object Memoize:

  /** Single-argument _pure function memoization_ class.
   *
   *  @tparam A Type of argument values passed to function `f`.
   *
   *  @tparam R Type of result returned by `f`.
   *
   *  @constructor Create new _memoized_ single-argument _pure function_.
   *
   *  @param f Function to be _memoized_.
   *
   *  @since 0.0
   */
  final class Memoize1[A, R] private[Memoize](f: A => R)
  extends Memoize[A, R](f)
  with (A => R):

    /** Evaluate _memoized_ function.
     *
     *  @param a Argument value to be passed to function.
     *
     *  @return Result of `f(a)`
     *
     *  @since 0.0
     */
    override def apply(a: A): R = eval(a)

  /** Double-argument _pure function memoization_ class.
   *
   *  @tparam A1 Type of first argument's values passed to function `f`.
   *
   *  @tparam A2 Type of second argument's values passed to function `f`.
   *
   *  @tparam R Type of result returned by `f`.
   *
   *  @constructor Create new _memoized_ double-argument _pure function_.
   *
   *  @param f Function to be _memoized_.
   *
   *  @since 0.0
   */
  final class Memoize2[A1, A2, R] private[Memoize](f: (A1, A2) => R)
  extends Memoize[(A1, A2), R](f.tupled)
  with ((A1, A2) => R):

    /** Evaluate _memoized_ function.
     *
     *  @param a1 First argument value to be passed to function.
     *
     *  @param a2 Second argument value to be passed to function.
     *
     *  @return Result of `f(a1, a2)`
     *
     *  @since 0.0
     */
    override def apply(a1: A1, a2: A2): R = eval((a1, a2))

  /** Triple-argument _pure function memoization_ class.
   *
   *  @tparam A1 Type of first argument's values passed to function `f`.
   *
   *  @tparam A2 Type of second argument's values passed to function `f`.
   *
   *  @tparam A3 Type of third argument's values passed to function `f`.
   *
   *  @tparam R Type of result returned by `f`.
   *
   *  @constructor Create new _memoized_ triple-argument _pure function_.
   *
   *  @param f Function to be _memoized_.
   *
   *  @since 0.0
   */
  final class Memoize3[A1, A2, A3, R] private[Memoize](f: (A1, A2, A3) => R)
  extends Memoize[(A1, A2, A3), R](f.tupled)
  with ((A1, A2, A3) => R):

    /** Evaluate _memoized_ function.
     *
     *  @param a1 First argument value to be passed to function.
     *
     *  @param a2 Second argument value to be passed to function.
     *
     *  @param a3 Third argument value to be passed to function.
     *
     *  @return Result of `f(a1, a2, a3)`
     *
     *  @since 0.0
     */
    override def apply(a1: A1, a2: A2, a3: A3): R = eval((a1, a2, a3))

  /** Memoize a single-argument function.
   *
   *  @tparam A Type of argument passed to function `f`.
   *
   *  @tparam R Type of result returned by `f`.
   *
   *  @param f _Pure function_ to be _memoized_.
   *
   *  @return Memoized version of `f`
   *
   *  @since 0.0
   */
  def apply[A, R](f: A => R): Memoize1[A, R] = new Memoize1[A, R](f)

  /** Memoize a double-argument function.
   *
   *  @tparam A1 Type of first argument passed to function `f`.
   *
   *  @tparam A2 Type of second argument passed to function `f`.
   *
   *  @tparam R Type of result returned by `f`.
   *
   *  @param f _Pure function_ to be _memoized_.
   *
   *  @return Memoized version of `f`
   *
   *  @since 0.0
   */
  def apply[A1, A2, R](f: (A1, A2) => R): Memoize2[A1, A2, R] = new Memoize2[A1, A2, R](f)

  /** Memoize a triple-argument function.
   *
   *  @tparam A1 Type of first argument passed to function `f`.
   *
   *  @tparam A2 Type of second argument passed to function `f`.
   *
   *  @tparam A3 Type of third argument passed to function `f`.
   *
   *  @tparam R Type of result returned by `f`.
   *
   *  @param f _Pure function_ to be _memoized_.
   *
   *  @return Memoized version of `f`
   *
   *  @since 0.0
   */
  def apply[A1, A2, A3, R](f: (A1, A2, A3) => R): Memoize3[A1, A2, A3, R] = new Memoize3[A1, A2, A3, R](f)
