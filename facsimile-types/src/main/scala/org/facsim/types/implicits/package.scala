//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
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

//======================================================================================================================
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types

import org.facsim.types.phys.Physical
import scala.util.Try

/** ''[[http://facsim.org/ Facsimile]]'' physical quantity implicit conversions.
 *
 *   @since 0.0
 */
package object implicits {

  /** Decorator for [[scala.util.Try]] physical measurement results.
   *
   *  @tparam A Type of physical measurement being decorated.
   *
   *  @param ta Physical measurement, wrapped in a [[scala.util.Try]] being operated upon. If the value is a
   *  [[scala.util.Failure]], then all operations will result in the same failure being returned, without evaluating the
   *  operation.
   *
   *  @since 0.0
   */
  implicit final class RichTry[A: Physical#Measure](ta: Try[A])
  extends AnyVal {

    /** Calculate multiplication of a tried physical measurement value by a defined factor.
     *
     *  @param f Factor to multiply `value` by.
     *
     *  @return Result of the multiplication wrapped in a [[scala.util.Success]] if successful, or a
     *  [[scala.util.Failure]] otherwise. If `ta` has already failed, then the original failure will be returned.
     *
     *  @since 0.0
     */
    def *(f: Double): Try[A] = ta.flatMap(_ * f)

    /** Calculate division of a tried physical measurement value by a defined denominator.
     *
     *  @param d Denominator to divide `value` by.
     *
     *  @return Result of the division wrapped in a [[scala.util.Success]] if successful, or a [[scala.util.Failure]]
     *  otherwise. If `ta` has already failed, then the original failure will be returned.
     *
     *  @since 0.0
     */
    def /(d: Double): Try[A] = ta.flatMap(_ / d)
  }

  implicit final class RichDouble(d: Double)
  extends AnyVal {


    def *[A: Physical#Measure](a: A): Try[A] = a * d
  }
}