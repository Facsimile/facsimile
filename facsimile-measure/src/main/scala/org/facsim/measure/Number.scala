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
// Scala source file belonging to the org.facsim.measure package.
//======================================================================================================================
package org.facsim.measure

/** Trait representing the basic characteristics of a number.
  *
  * Numbers support comparisons, as well as addition, subtraction, multiplication and division, etc.
  *
  * @tparam N Final number type.
  *
  * @constructor Create a new number instance, with the specified value.
  *
  * @param value Value, in whatever units are most appropriate, for a number of type `N`
  */
abstract class Number[N <: Number[N]](protected final val value: Double)
extends Ordered[N]
with Equals {

  /** Factory element for creating new number instances.
    */
  protected def factory: NumberFactory[N]

  /** Add another number of the same type to this number.
    *
    * @param o Other number to be added to this number.
    *
    * @return New number instance containing the result of the addition.
    */
  final def +(o: N) = factory(value + o.value)

  /** Subtract another number from this number.
    *
    * @param o Other number to be added to this number.
    *
    * @return New number instance containing the result of the subtraction.
    */
  final def -(o: N) = factory(value - o.value)

  /** Multiply this number by a constant factor.
    *
    * @param f Factor used to multiply this value.
    *
    * @return New number instance containing the result of the multiplication.
    */
  final def *(f: Double) = factory(value * f)

  /** Divide this number by the specified divisor.
    *
    * @param d Divisor of this number.
    *
    * @return New number instance containing the result of the division.
    */
  final def /(d: Double) = factory(value / d)

  /** Determine whether this number can equal the specified instance.
    *
    * @param o Other instance that we're checking for possible equality.
    *
    * @return `true` if `o` is comparable with this instance, `false` otherwise. If `o` is `null`, then `false` will be
    * returned.
    */
  final override def canEqual(o: Any) = o match {

    // If the other number is an instance of N, then we can compare for equality.
    case that: N => true

    // Otherwise, we cannot.
    case _ => false
  }

  /** Determine whether this number is equal to the specified instance.
    *
    * @param o Other instance that we're being compared to for equality.
    *
    * @return `true` if this number compares as equal to the `o`, `false` otherwise. If `o` is `null`, then `false` will
    * be returned.
    */
  final override def equals(o: Any) = o match {

    // If the other number can equal this number, and their values are the same, then the values are equal; if either of
    // those considtions fail, then the values are different.
    case that: N => that.canEqual(this) && value == that.value

    // Otherwise, if o is some other type, or null, then they cannot be equal.
    case _ => false
  }

  /** Retrieve a unique hash-code for this number.
    *
    * @return
    */
  final override def hashCode = value.hashCode
}