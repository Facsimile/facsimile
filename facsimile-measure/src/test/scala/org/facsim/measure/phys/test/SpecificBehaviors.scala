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
// Scala source file belonging to the org.facsim.measure.phys.test package.
//======================================================================================================================
package org.facsim.measure.phys.test

import org.facsim.measure.phys.Specific
import org.scalatest.FunSpec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/** Test behaviors for [[Specific]] subclasses.
 *
 *  @tparam Q The `Specific` subclass being tested.
 */
trait SpecificBehaviors[Q <: Specific]
extends PhysicalBehaviors[Q] {
  this: FunSpec =>

  /** Verify a [[Specific]] subclass implementation.
   *
   *  @param fixture Test fixture providing information to be used by the tests.
   */
  final def specificBehavior(fixture: SpecificFixture[Q]): Unit = {

    // Firstly, verify the physical quantity type behavior.
    it must behave like physicalBehavior(fixture)

    // Test that Zero is zero.
    describe(".Zero") {
      it("must equal to the value of 0.0 in SI units") {
        assert(fixture.instance.Zero === fixture.instance(0.0,
        fixture.expectedSIUnits.asInstanceOf[fixture.instance.Units])) //scalastyle:ignore token
      }
    }

    // Verify that the family reported for this specific physical quantity matches requirements.
    describe(".family") {
      it("must report the correct family") {
        assert(fixture.instance.family === fixture.expectedFamily)
      }
    }

    // Now verify the apply(Double) method.
    describe(".apply(Double)") {

      // Verify that non-finite values are rejected.
      it("must throw an IllegalArgumentException if passed a non-finite value") {
        fixture.nonFiniteValues.foreach {
          value =>
          val e = intercept[IllegalArgumentException] {
            fixture.instance.apply(value)
          }
          assertRequireFiniteMsg(e, "value", value)
        }
      }

      // Verify that applying bad values will be rejected with the appropriate exception.
      it("must throw an IllegalArgumentException if passed a bad value") {
        fixture.invalidValues.foreach {
          value =>
          val e = intercept[IllegalArgumentException] {
            fixture.instance.apply(value)
          }
          assertRequireValidMsg(e, "value", value)
        }
      }

      // Verify that applying good values will be accepted without any exceptions being thrown.
      it("must not throw an exception when passed a valid value") {
        fixture.validValues.foreach {
          value =>
          assert(fixture.instance.apply(value) ne null) //scalastyle:ignore null
        }
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc