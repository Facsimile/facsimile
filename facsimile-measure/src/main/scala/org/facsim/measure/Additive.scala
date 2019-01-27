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
// Scala source file belonging to the org.facsim.measure package.
//======================================================================================================================
package org.facsim.measure

/** Trait supporting the addition and subtraction of subclass instances.
 *
 *  @tparam T Type of value being added and subtracted. This must be a ''subclass'' of `Additive[T]`.
 *
 *  @since 0.0
 */
trait Additive[T <: Additive[T]] {

  /** Addition operator.
   *
   *  @note When overriding this function, if the result differs from the original value, then the result should be a
   *  new instance, rather than modifying the state of this object.
   *
   *  @param other Value to be added to this value.
   *
   *  @return Result of `this` + `other` as a new `Additive` instance.
   */
  def +(other: T): T

  /** Subtraction operator.
   *
   *  @note When overriding this function, if the result differs from the original value, then the result should be a
   *  new instance, rather than modifying the state of this object.
   *
   *  @param other Value to be subtracted from this value.
   *
   *  @return Result of `this` - `other` as a new `Additive` instance.
   */
  def -(other: T): T
}