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
// Scala source file belonging to the org.facsim.measure.test package.
//======================================================================================================================
package org.facsim.measure.test

import org.facsim.measure.LinearScaleConverter
import org.facsim.util.test.CommonTestMethods
import org.scalatest.FunSpec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/** Test suite for the [[LinearScaleConverter]] class.
  */
class LinearScaleConverterTest
extends FunSpec
with CommonTestMethods {

  /** Construction test data.
    *
    * Note: The values 0.0 and 1.0 are illegal factor values, and so are not included here.
    */
  trait ConstructorTestData {
    new LinearScaleConverter(Double.MinValue)
    new LinearScaleConverter(-1.0)
    new LinearScaleConverter(-Double.MinPositiveValue)
    new LinearScaleConverter(Double.MinPositiveValue)
    new LinearScaleConverter(Double.MaxValue)
  }

  /** Import/export test data.
    */
  trait ImportExportTestData {
    val tolerance = 1.0e-12
    val factorTen = new LinearScaleConverter(10.0)
    val factorMinusTen = new LinearScaleConverter(-10.0)
    val i1 = 10.0
    val e1_1 = 100.0
    val e1_2 = -100.0
    val i2 = -10.0
    val e2_1 = -100.0
    val e2_2 = 100.0
  }

  // Test fixture description.
  describe(classOf[LinearScaleConverter].getCanonicalName) {

    // Constructor tests.
    describe(".this(Double)") {

      // Verify that it should reject illegal factor values.
      def doFiniteFailure(badFactor: Double) = {
        val e = intercept[IllegalArgumentException] {
          new LinearScaleConverter(badFactor)
        }
        assertRequireFiniteMsg(e, "factor", badFactor)
      }
      it("must throw an IllegalArgumentException if passed NaN") {
        doFiniteFailure(Double.NaN)
      }
      it("must throw an IllegalArgumentException if passed -Infinity") {
        doFiniteFailure(Double.NegativeInfinity)
      }
      it("must throw an IllegalArgumentException if passed Infinity") {
        doFiniteFailure(Double.PositiveInfinity)
      }
      def doValidFailure(badFactor: Double) = {
        val e = intercept[IllegalArgumentException] {
          new LinearScaleConverter(badFactor)
        }
        assertRequireValidMsg(e, "factor", badFactor)
      }
      it("must throw an IllegalArgumentException if passed a zero factor") {
        doValidFailure(0.0)
      }
      it("must throw an IllegalArgumentException is passed a factor of one") {
        doValidFailure(1.0)
      }

      // Verify that it accepts just about any other value (not all of which make sense).
      it("must accept valid factor values") {
        new ConstructorTestData {}
      }
    }

    // Importing tests.
    describe(".importValue(Double)") {

      // Check that we get the right imported value.
      it("must import values correctly") {
        new ImportExportTestData {

          // Helper function to perform comparisons.
          def checkReturn(factor: LinearScaleConverter, importVal: Double, exportVal: Double): Unit = {
            assert(factor.importValue(importVal) === exportVal)
          }

          // The return value must exactly match the value passed. There can be no rounding errors.
          checkReturn(factorTen, 0.0, 0.0)
          checkReturn(factorTen, i1, e1_1)
          checkReturn(factorTen, i2, e2_1)
          checkReturn(factorMinusTen, 0.0, 0.0)
          checkReturn(factorMinusTen, i1, e1_2)
          checkReturn(factorMinusTen, i2, e2_2)
        }
      }
    }

    // Exporting tests
    describe(".exportValue(Double)") {

      // Check that we get the right imported value.
      it("must export values correctly") {
        new ImportExportTestData {

          // Helper function to perform comparisons.
          def checkReturn(factor: LinearScaleConverter, importVal: Double, exportVal: Double): Unit = {
            assert(factor.exportValue(exportVal) == importVal)
          }

          // The return value must exactly match the value passed. There can be no rounding errors.
          checkReturn(factorTen, 0.0, 0.0)
          checkReturn(factorTen, i1, e1_1)
          checkReturn(factorTen, i2, e2_1)
          checkReturn(factorMinusTen, 0.0, 0.0)
          checkReturn(factorMinusTen, i1, e1_2)
          checkReturn(factorMinusTen, i2, e2_2)
        }
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc