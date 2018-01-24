//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2018, Michael J Allen.
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

import org.facsim.util.requireNonNull
import scala.reflect.ClassTag

/** Abstract class for value types.
 *
 *  @tparam T Type of the value class.
 *
 *  @param value Value of this instance in the underlying units.
 *
 *  @since 0.0
 */
//scalastyle:off disallow.space.before.token
abstract class Value[T <: Value[T] : ClassTag](private[facsim] final val value: Double)
extends Orderable[T] {
//scalastyle:on disallow.space.before.token

  /** Compare this value to another value.
   *
   *  @param other Value to be compared to.
   *
   *  @return A value less than 0 if this value is less than `other`; 0 if this value and `other` are equal; a value
   *  greater than zero if this value is greater than `other`.
   *
   *  @throws NullPointerException if `other` is `null`.
   *
   *  @since 0.0
   */
  final override def compare(other: T): Int = {
    requireNonNull(other)
    value.compare(other.value)
  }

  /** Factory method to create a new value instance.
   *
   *  @param newValue Value of the new instance.
   *
   *  @return New instance, if `value` is valid.
   *
   *  @throws IllegalArgumentException if `value` is outside of the permitted range for the new value.
   *
   *  @since 0.0
   */
  protected def createNew(newValue: Double): T

  /** Calculate the absolute value of this value.
   *
   *  @return The absolute value of the measurement, based upon it's ''SI'' units.
   *
   *  @throws IllegalArgumentException if the result is invalid for these units.
   *
   *  @since 0.0
   */
  final def abs: T = {
    if(value < 0.0) createNew(-value)
    else this.asInstanceOf[T] //scalastyle:ignore token
  }

  /** Change the sign of a measurement value.
   *
   *  @note All measurements that do not permit negative values will throw exceptions when this operation is invoked
   *  on a valid value.
   *
   *  @return Measurement value having a sign opposite that of this value.
   *
   *  @throws IllegalArgumentException if the result is not finite or is invalid for these units.
   *
   *  @since 0.0
   */
  final def unary_- : T = createNew(-value) //scalastyle:ignore disallow.space.before.token
}