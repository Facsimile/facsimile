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

/** Trait supporting the scaling of value classes by constant factors.
 *
 *  @tparam T Type of value being factored.
 *
 *  @since 0.0
 */
trait Scalable[T <: Scalable[T]] {

  /** Scale this value by specified factor.
   *
   *  @param factor Factor used to scale this value.
   *
   *  @return Product of this value multiplied by `factor`. The result is a value of the same type as this value.
   *
   *  @throws IllegalArgumentException if the result is not finite or is invalid for these units.
   *
   *  @since 0.0
   */
  def *(factor: Double): T

  /** Divide this value by specified divisor.
   *
   *  @param divisor Divisor to be applied to this value.
   *
   *  @return Quotient of this value divided by `divisor`. The result is a value of the same type as this value.
   *
   *  @throws IllegalArgumentException if the result is not finite or invalid for the associated type. For example, an
   *  infinite result will occur if `divisor` is zero, which will cause this exception to be thrown.
   *
   *  @since 0.0
   */
  def /(divisor: Double): T
}