//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
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
// Scala source file belonging to the org.facsim.types.phys.test types.
//======================================================================================================================
package org.facsim.types.phys.test

import org.facsim.types.phys._
import org.facsim.types.phys.Angle.{π, τ}
import org.facsim.types.test.EqualsFixture
import org.scalatest.FunSpec

// Disable test-problematic Scalastyle checkers.
//scalastyle:off indentation
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers
/** Test suite for the [[Angle]] object. */
class AngleTest
extends FunSpec
with SpecificBehaviors[Angle.type, Angle] {

  /** Fixture for testing that angle measurements fulfill the "equals contract". */
  trait AngleEqualsFixture
  extends EqualsFixture[Angle] {

    /** Specify a list for equality testing.
     *
     *  Each list within this list should contain values that compare equal and that have the same hashCode. The first
     *  member of each list should NOT compare equal to the first member of any other list.
     */
    override def equalListSample = List(

      // All of these values should have a value of -τ radians (-360 degrees), which should normalize to 0 radians (0
      // degrees).
      List(
        -τ,
        Angle(-360.0, Angle.Degrees),
        Angle(-400.0, Angle.Gradians),
        Angle(-1.0, Angle.Turns)
      ),

      // All of these values should have a value of -τ/2 radians (-180 degrees).
      List(
        -τ / 2.0,
        Angle(-180.0, Angle.Degrees),
        Angle(-200.0, Angle.Gradians),
        Angle(-1.0 / 2.0, Angle.Turns)
      ),

      // All of these values should have a value of -τ/4 radians (-90 degrees).
      List(
        -τ / 4.0,
        Angle(-90.0, Angle.Degrees),
        Angle(-100.0, Angle.Gradians),
        Angle(-1.0 / 4.0, Angle.Turns)
      ),

      // All of these values should have a value of -τ/6 radians (-60 degrees).
      List(
        -τ / 6.0,
        Angle(-60.0, Angle.Degrees),
        Angle(-200.0 / 3.0, Angle.Gradians),
        Angle(-1.0 / 6.0, Angle.Turns)
      ),

      // All of these values should have a value of -τ/8 radians (-45 degrees).
      List(
        -τ / 8.0,
        Angle(-45.0, Angle.Degrees),
        Angle(-50.0, Angle.Gradians),
        Angle(-1.0 / 8.0, Angle.Turns)
      ),

      // All of these values should have a value of -τ/12 radians (-30 degrees).
      List(
        -τ / 12.0,
        Angle(-30.0, Angle.Degrees),
        Angle(-100.0 / 3.0, Angle.Gradians),
        Angle(-1.0 / 12.0, Angle.Turns)
      ),

      // All of these values should have the value 0 radians (0 degrees).
      List(
        Angle.Zero,
        Angle(0.0, Angle.Degrees),
        Angle(0.0, Angle.Gradians),
        Angle(0.0, Angle.Turns)
      ),

      // All of these values should have a value of τ/12 radians (30 degrees).
      List(
        τ / 12.0,
        Angle(30.0, Angle.Degrees),
        Angle(100.0 / 3.0, Angle.Gradians),
        Angle(1.0 / 12.0, Angle.Turns)
      ),

      // All of these values should have a value of τ/8 radians (45 degrees).
      List(
        τ / 8.0,
        Angle(45.0, Angle.Degrees),
        Angle(50.0, Angle.Gradians),
        Angle(1.0 / 8.0, Angle.Turns)
      ),

      // All of these values should have a value of τ/6 radians (60 degrees).
      List(
        τ / 6.0,
        Angle(60.0, Angle.Degrees),
        Angle(200.0 / 3.0, Angle.Gradians),
        Angle(1.0 / 6.0, Angle.Turns)
      ),

      // All of these values should have a value of τ/4 radians (90 degrees).
      List(
        τ / 4.0,
        Angle(90.0, Angle.Degrees),
        Angle(100.0, Angle.Gradians),
        Angle(1.0 / 4.0, Angle.Turns)
      ),

      // All of these values should have a value of τ/2 radians (180 degrees).
      List(
        τ / 2.0,
        Angle(180.0, Angle.Degrees),
        Angle(200.0, Angle.Gradians),
        Angle(1.0 / 2.0, Angle.Turns)
      ),

      // All of these values should have a value of τ radians (360 degrees), which should normalize to 0 radians (0
      // degrees).
      List(
        τ,
        Angle(360.0, Angle.Degrees),
        Angle(400.0, Angle.Gradians),
        Angle(1.0, Angle.Turns)
      ),

      // All of these values should have a value of 2τ radians (720 degrees), which should normalize to 0 radians (0
      // degrees).
      List(
        τ * 2.0,
        Angle(720.0, Angle.Degrees),
        Angle(800.0, Angle.Gradians),
        Angle(2.0, Angle.Turns)
      )
    )

    /** Specify a list of values (null is tested separately and does not need to be included) that are of a different
     *  type to [[Angle.Measure]] and so should not compare equal and should fail canEqual also.
     */
    override def nonValueSample = List(
      0,
      1.0,
      "Hello",
      true,
      Current.Zero,
      Length.Zero,
      LuminousIntensity.Zero,
      Mass.Zero,
      Temperature.Zero,
      Time.Zero
    )
  }

  /** Test fixture for angles. */
  trait AngleFixture
  extends SpecificFixture[Angle.type] {

    /** Specify the Angle instance. */
    override val instance = Angle

    /** Specify the expected SI units for Angles. */
    override val expectedSIUnits = Angle.Radians

    /** Create a test fixture for testing "equals contract" fulfillment. */
    override val equalsFixture = new AngleEqualsFixture {}

    /** Specify the physical quantity family that angle measurements should belong to. */
    override val expectedFamily = Family.Unitless

    /** Specify list of invalid SI unit measurement values that should fail construction.
     *
     *  This list is empty - there are no invalid finite measurement values for angles.
     */
    override val invalidValues = Nil

    /** Specify list of valid SI unit measurement values that should lead to successful creation. */
    override val validValues = List(
      Double.MinValue,
      -Double.MinPositiveValue,
      0.0,
      Double.MinPositiveValue,
      Double.MaxValue
    )
  }

  // Test fixture description.
  describe(Angle.getClass.getCanonicalName) {

    // Test that the constant π has the correct value.
    describe(".π") {
      it("must equal the value of physical constant π") {
        assert(π === Angle(Math.PI, Angle.Radians))
      }
    }

    // Test that the constant τ has the correct value.
    describe(".τ") {
      it("must equal the value of physical constant τ (=2π)") {
        assert(τ === Angle(2.0 * Math.PI, Angle.Radians))
      }
    }

    // Test basic specific types behavior(creation, equality, addition, etc.). This should come after the constant
    // testing above, but before the higher-level stuff below.
    it must behave like specificBehavior(new AngleFixture {})
  }
}
// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on scaladoc
//scalastyle:on public.methods.have.type
//scalastyle:on multiple.string.literals
//scalastyle:on magic.numbers
//scalastyle:off indentation