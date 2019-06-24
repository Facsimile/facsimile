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
// Scala source file belonging to the org.facsim.types.phys.test types.
//======================================================================================================================
package org.facsim.types.phys.test

import org.facsim.types.phys.Family
import org.scalatest.FunSpec

// Disable test-problematic Scalastyle checkers.
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers
/** Test suite for the [[Family]] class and companion object. */
class FamilyTest
extends FunSpec {

  /** Test data. */
  //scalastyle:off magic.number
  trait TestData {
    val angleFamily = Family()
    val unitlessFamily = Family()
    val timeFamily = Family(timeExponent = 1)
    val lengthFamily = Family(lengthExponent = 1)
    val velocityFamily = Family(lengthExponent = 1, timeExponent = -1)
    val accelerationFamily = Family(lengthExponent = 1, timeExponent = -2)
    val frequencyFamily = Family(timeExponent = -1)
    val areaFamily = Family(lengthExponent = 2)
    val volumeFamily = Family(lengthExponent = 3)
  }
  //scalastyle:on magic.number

  // Test fixture companion object description.
  describe(Family.getClass.getCanonicalName) {

    // Test construction via apply method, which includes testing of construction. Since construction is handled
    // internally, it should be impossible
    describe(".apply(Vector[Int])") {
      it("must create required physical quantity families") {
        new TestData {}
      }
    }
  }

  // Test fixture class description.
  describe(classOf[Family].getCanonicalName) {

    // Verify that equality of unit families is handled correctly.
    //
    //TODO: This has yet to be implemented.

    // Verify that each family is correctly reported as being unitless or not.
    describe(".isUnitless") {
      new TestData {
        it("must return true for unitless family instances") {
          assert(angleFamily.isUnitless)
          assert(unitlessFamily.isUnitless)
        }
        it("must return false for family instances having units") {
          assert(!timeFamily.isUnitless)
          assert(!lengthFamily.isUnitless)
          assert(!velocityFamily.isUnitless)
          assert(!accelerationFamily.isUnitless)
          assert(!frequencyFamily.isUnitless)
          assert(!areaFamily.isUnitless)
          assert(!volumeFamily.isUnitless)
        }
      }
      ()
    }

    // Verify that multiplication of units is performed correctly.
    describe(".*(Family)") {
      new TestData {

        // If a unitless family is involved as one operand, the resulting product will be the other operand. When two
        // unitless family instances are multiplied together, the result is a unitless family instance.
        it("must handle unitless multiplications correctly") {
          assert(unitlessFamily * unitlessFamily == unitlessFamily)
          assert(unitlessFamily * timeFamily == timeFamily)
          assert(timeFamily * unitlessFamily == timeFamily)
        }

        // If both operands are non-unitless family instances, then the resulting product will be different to both of
        // them.
        it("must handle non-unitless multiplications correctly") {
          assert(timeFamily * frequencyFamily == unitlessFamily)
          assert(frequencyFamily * timeFamily == unitlessFamily)
          assert(lengthFamily * lengthFamily == areaFamily)
          assert(lengthFamily * areaFamily == volumeFamily)
          assert(areaFamily * lengthFamily == volumeFamily)
          assert(lengthFamily * frequencyFamily == velocityFamily)
          assert(frequencyFamily * lengthFamily == velocityFamily)
          assert(velocityFamily * timeFamily == lengthFamily)
          assert(timeFamily * velocityFamily == lengthFamily)
          assert(accelerationFamily * timeFamily == velocityFamily)
          assert(timeFamily * accelerationFamily == velocityFamily)
          assert(velocityFamily * frequencyFamily == accelerationFamily)
          assert(frequencyFamily * velocityFamily == accelerationFamily)
        }
      }
      ()
    }

    // Verify that division of units is performed correctly.
    describe("./(Family)") {
      new TestData {

        // If a unitless family is the right-hand operand, then the resulting quotient will be the right-hand operand.
        // If the left-hand operand is unitless, then the result will be the inverse of the right-hand operand. When two
        // unitless family instances are involved, the result is unitless.
        it("must handle unitless divisions correctly") {
          assert(unitlessFamily / timeFamily == frequencyFamily)
          assert(unitlessFamily / frequencyFamily == timeFamily)
          assert(timeFamily / unitlessFamily == timeFamily)
          assert(frequencyFamily / unitlessFamily == frequencyFamily)
          assert(unitlessFamily / unitlessFamily == unitlessFamily)
        }

        // If both operands are non-unitless family instances, then the resulting quotient will be different to both of
        // them.
        it("must handle non-unitless divisions correctly") {
          assert(timeFamily / timeFamily == unitlessFamily)
          assert(lengthFamily / timeFamily == velocityFamily)
          assert(lengthFamily / lengthFamily == unitlessFamily)
          assert(volumeFamily / lengthFamily == areaFamily)
          assert(volumeFamily / areaFamily == lengthFamily)
          assert(volumeFamily / volumeFamily == unitlessFamily)
          assert(areaFamily / lengthFamily == lengthFamily)
          assert(areaFamily / areaFamily == unitlessFamily)
          assert(velocityFamily / timeFamily == accelerationFamily)
          assert(accelerationFamily / velocityFamily == frequencyFamily)
          assert(velocityFamily / accelerationFamily == timeFamily)
        }
      }
      ()
    }
  }
}
// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on scaladoc
//scalastyle:on public.methods.have.type
//scalastyle:on multiple.string.literals
//scalastyle:on magic.numbers