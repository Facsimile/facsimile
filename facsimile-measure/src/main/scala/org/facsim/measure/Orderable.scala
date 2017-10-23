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

/** Abstract base class for objects that can be compared for equality, and sorted in order.
 *
 *  Implementing objects can be ordered as well as compared for equality and inequality.
 *
 *  @note At present, this must be an ''abstract class'', rather than a ''trait'', because the latter do not currently
 *  support class bounds (such as [[ClassTag]]). Implementing types must ensure that [[compare()]] returns 0 for classes
 *  that are equivalent, and non-zero for classes that are not equivalent. Furthermore, [[hashCode]] values for
 *  equivalent objects must be identical too.
 *
 *  @tparam T Type of object implementing ordering operations, which must implement this base class.
 *
 *  @since 0.0
 */
abstract class Orderable[T <: Orderable[T] : ClassTag] //scalastyle:ignore disallow.space.before.token
extends Equivalent[T] with Ordered[T] {

  /** @inheritdoc */
  final override def ===(other: T): Boolean = compare(other) == 0
}