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
// Scala source file belonging to the org.facsim.util package.
//======================================================================================================================
package org.facsim.measure

import scala.reflect.ClassTag

/** Abstract base class for objects that can be compared for equivalence.
 *
 *  Implementing objects can be compared for equality and inequality.
 *
 *  @note At present, this must be an ''abstract class'', rather than a ''trait'', because the latter do not currently
 *  support class bounds (such as [[ClassTag]]). Implementing types must ensure that [[hashCode]] values for equivalent
 *  objects are identical too.
 *
 *  @tparam T Type of object implementing equivalence operations, which must implement this base class.
 *
 *  @since 0.0
 */
abstract class Equivalent[T <: Equivalent[T] : ClassTag]
extends Equals {

  /** Equivalence operator.
   *
   *  @param other Object that this object is being compared to; this value cannot be `null`.
   *
   *  @note If this function returns `true`, then the [[hashCode]] values of both objects must be equal.
   *
   *  @return `true` is this object and `that` object are equivalent; `false` if they differ.
   *
   *  @throws scala.NullPointerException if `that` is `null`.
   *
   *  @since 0.0
   */
  def ===(other: T): Boolean

  /** Non-equivalence operator.
   *
   *  @param other Object that this object is being compared to; this value cannot be `null`.
   *
   *  @note If this function returns `false`, then the [[hashCode]] values of both objects must be equal.
   *
   *  @return `true` is this object and `that` object are not equivalent; `false` if they are equivalent.
   *
   *  @throws NullPointerException if `that` is `null`.
   *
   *  @since 0.0
   */
  final def =!=(other: T) = !(this === other)

  /** Determine whether another object can be compared for equality to this object.
   *
   *  @param other Object that this object is being compared to.
   *
   *  @return `true` is this object and `that` object can be compared for equality (i.e. they have the same type);
   *  `false` if the values cannot be compared for equality, including if `other` is `null`.
   *
   *  @since 0.0
   */
  override def canEqual(other: Any) = other match {
    case _: T => true
    case _ => false
  }

  /** Equality operator.
   *
   *  @param other Object that this object is being compared to. If `null`, the function will return `false`.
   *
   *  @note If this function returns `true`, then the [[hashCode]] values of both objects must be equal.
   *
   *  @return `true` is this object and `that` object are equal; `false` if they differ, including the case in which
   *  `other` is `null`.
   *
   *  @since 0.0
   */
  final override def equals(other: Any) = other match {
    case that: T => that.canEqual(this) && this === that
    case _ => false
  }
}