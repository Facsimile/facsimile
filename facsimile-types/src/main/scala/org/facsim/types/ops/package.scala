//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
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
// Scala source file belonging to the org.facsim.types.ops package.
//======================================================================================================================
package org.facsim.types

import org.facsim.types.algebra.AdditiveTypedSemigroup

/** Common operations. */
package object ops {

  /** Provide addition operators for operands supported by an implicit AdditiveTypedSemigroup.
   *
   *  @tparam A Type of the first addition operand.
   *
   *  @constructor Create a new addition operator for this class.
   *
   *  @param a First addition operand,
   *
   *  @since 0.0
   */
  final implicit class AdditionOps[A](val a: A)
  extends AnyVal {

    /** Addition operator.
     *
     *  @tparam B Type of the second addition operand.
     *
     *  @tparam R Type of the result of the addition operation.
     *
     *  @param b Second addition operand.
     *
     *  @param ats Implicit addition semigroup for adding `a` and `b` resulting in a value of type `R`.
     *
     *  @return Result of the addition operation.
     *
     *  @since 0.0
     */
    def +[B, R](b: B)(implicit ats: AdditiveTypedSemigroup[A, B, R]): R = ats.combine(a, b)
  }
}
