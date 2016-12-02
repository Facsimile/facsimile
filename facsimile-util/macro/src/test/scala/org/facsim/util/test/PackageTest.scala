/*
 * Facsimile -- A Discrete-Event Simulation Library
 * Copyright © 2004-2016, Michael J Allen.
 *
 * This file is part of Facsimile.
 *
 * Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or(at your option) any later
 * version.
 *
 * Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
 * http://www.gnu.org/licenses/lgpl.
 *
 * The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
 * project home page at:
 *
 *   http://facsim.org/
 *
 * Thank you for your interest in the Facsimile project!
 *
 * IMPORTANT NOTE: All patches(modifications to existing files and/or the addition of new files) submitted for
 * inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
 * your code fails to comply with the standard, then your patches will be rejected. For further information, please
 * visit the coding standards at:
 *
 *   http://facsim.org/Documentation/CodingStandards/
 * =====================================================================================================================
 * Scala source file belonging to the org.facsim.util.test package.
 */
package org.facsim.util.test

import org.facsim.util.{requireFinite, requireNonNull, requireValid}
import org.scalatest.FunSpec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/**
 * Test suite for the [[org.facsim]] package object.
 */
class PackageTest
extends FunSpec
with CommonTestMethods {

  /*
   * Test fixture description.
   */
  describe("org.facsim.util package") {

    /*
     * requireNonNull macro tests.
     */
    describe(".requireNonNull(AnyRef)") {
      it("must throw NullPointerException if passed null argValue") {
        val e = intercept[NullPointerException] {
          val arg: String = null //scalastyle:ignore null
          requireNonNull(arg)
        }
        assertRequireNonNullMsg(e, "arg")
      }
      it("must do nothing if passed a non-null reference") {
        val arg = "Not null"
        requireNonNull(arg)
      }
    }

    /*
     * requireValid tests.
     */
    describe(".requireValid(Any, Boolean)") {
      it("must throw IllegalArgumentException if condition is false") {
        val arg = "Some bad value"
        val e = intercept[IllegalArgumentException] {
          requireValid(arg, false)
        }
        assertRequireValidMsg(e, "arg", arg)
      }
      it("must do nothing if passed a valid argument") {
        val arg = "Some good value"
        requireValid(arg, true)
      }
    }

    /*
     * requireFinite tests.
     */
    describe(".requireFinite(Double)") {
      def doFailure(value: Double) = {
        val e = intercept[IllegalArgumentException] {
          requireFinite(value)
        }
        assertRequireFiniteMsg(e, "value", value)
      }
      it("must throw IllegalArgumentException if value is NaN") {
        doFailure(Double.NaN)
      }
      it("must throw IllegalArgumentException if value is infinite") {
        doFailure(Double.NegativeInfinity)
        doFailure(Double.PositiveInfinity)
      }
      it("must accept values at the boundaries") {
        val argMin = Double.MinValue
        requireFinite(argMin)
        val argMax = Double.MaxValue
        requireFinite(argMax)
      }
      it("must accept some miscellaneous finite values") {
        val argZero = 0.0
        requireFinite(argZero)
        val argOne = 1.0
        requireFinite(argOne)
        val argMinusOne = -1.0
        requireFinite(argMinusOne)
        val argMinPositive = Double.MinPositiveValue
        requireFinite(argMinPositive)
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc