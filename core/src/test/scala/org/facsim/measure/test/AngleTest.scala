/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.measure.test package.
*/
//=============================================================================

package org.facsim.measure.test

import org.facsim.measure.Angle
import org.facsim.measure.Angle.π
import org.facsim.measure.Angle.τ
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.measure.Angle$]] object.

If you're curious why τ is used in place of 2π, refer to the [[
*/
//=============================================================================

class AngleTest extends FunSpec with SpecificBehaviors [Angle.type] {

/*
Test fixture for angles.
*/

  trait AngleFixture extends SpecificFixture [Angle.type] {

/*
Specify the physical type.
*/

    final override val physQty = Angle

/*

 */
  }

/*
Angle test data.
*/

  trait TestData {

/*
All of these values should have a value of -τ radians (-360 degrees), which
should normalize to 0 radians (0 degrees).
*/

    val minusTauRadians = Angle (-τ, Angle.radians)
    val minusThreeHundredSixtyDegrees = Angle (-360.0, Angle.degrees)
    val minusFourHundredGradients = Angle (-400.0, Angle.gradients)
    val minusOneRevolution = Angle (-1.0, Angle.revolutions)

/*
All of these values should have a value of -τ/2 radians (-180 degrees).
*/

    val minusTauOverTwoRadians = Angle (-τ / 2.0, Angle.radians)
    val minusOneHundredEightyDegrees = Angle (-180.0, Angle.degrees)
    val minusTwoHundredGradients = Angle (-200.0, Angle.gradients)
    val minusOneHalfRevolution = Angle (-1.0 / 2.0, Angle.revolutions)

/*
All of these values should have a value of -τ/4 radians (-90 degrees).
*/

    val minusTauOverFourRadians = Angle (-τ / 4.0, Angle.radians)
    val minusNinetyDegrees = Angle (-90.0, Angle.degrees)
    val minusOneHundredGradients = Angle (-100.0, Angle.gradients)
    val minusOneQuarterRevolution = Angle (-1.0 / 4.0, Angle.revolutions)

/*
All of these values should have a value of -τ/6 radians (-60 degrees).
*/

    val minusTauOverSixRadians = Angle (-τ / 6.0, Angle.radians)
    val minusSixtyDegrees = Angle (-60.0, Angle.degrees)
    val minusSixtySixTwoThirdsGradients = Angle (-200.0 / 3.0, Angle.gradients)
    val minusOneSixthRevolution = Angle (-1.0 / 6.0, Angle.revolutions)

/*
All of these values should have a value of -τ/8 radians (-45 degrees).
*/    

    val minusTauOverEightRadians = Angle (-τ / 8.0, Angle.radians)
    val minusFortyFiveDegrees = Angle (-45.0, Angle.degrees)
    val minusFiftyGradients = Angle (-50.0, Angle.gradients)
    val minusOneEighthRevolution = Angle (-1.0 / 8.0, Angle.revolutions)

/*
All of these values should have a value of -τ/12 radians (-30 degrees).
*/

    val minusTauOverTwelveRadians = Angle (-τ / 12.0, Angle.radians)
    val minusThirtyDegrees = Angle (-30.0, Angle.degrees)
    val minusThirtyThreeThirdGradients = Angle (-100.0 / 3.0, Angle.gradients)
    val minusOneTwelfthRevolution = Angle (-1.0 / 12.0, Angle.revolutions)

/*
All of these values should have the value 0 radians (0 degrees).
*/

    val zeroRadians = Angle (0.0, Angle.radians)
    val zeroDegrees = Angle (0.0, Angle.degrees)
    val zeroGradients = Angle (0.0, Angle.gradients)
    val zeroRevolutions = Angle (0.0, Angle.revolutions)

/*
All of these values should have a value of τ/12 radians (30 degrees).
*/

    val tauOverTwelveRadians = Angle (τ / 12.0, Angle.radians)
    val thirtyDegrees = Angle (30.0, Angle.degrees)
    val thirtyThreeThirdGradients = Angle (100.0 / 3.0, Angle.gradients)
    val oneTwelfthRevolution = Angle (1.0 / 12.0, Angle.revolutions)

/*
All of these values should have a value of τ/8 radians (45 degrees).
*/    

    val tauOverEightRadians = Angle (τ / 8.0, Angle.radians)
    val fortyFiveDegrees = Angle (45.0, Angle.degrees)
    val fiftyGradients = Angle (50.0, Angle.gradients)
    val oneEighthRevolution = Angle (1.0 / 8.0, Angle.revolutions)

/*
All of these values should have a value of τ/6 radians (60 degrees).
*/

    val tauOverSixRadians = Angle (τ / 6.0, Angle.radians)
    val sixtyDegrees = Angle (60.0, Angle.degrees)
    val sixtySixTwoThirdsGradients = Angle (200.0 / 3.0, Angle.gradients)
    val oneSixthRevolution = Angle (1.0 / 6.0, Angle.revolutions)

/*
All of these values should have a value of τ/4 radians (90 degrees).
*/

    val tauOverFourRadians = Angle (τ / 4.0, Angle.radians)
    val ninetyDegrees = Angle (90.0, Angle.degrees)
    val oneHundredGradients = Angle (100.0, Angle.gradients)
    val oneQuarterRevolution = Angle (1.0 / 4.0, Angle.revolutions)

/*
All of these values should have a value of τ/2 radians (180 degrees).
*/

    val tauOverTwoRadians = Angle (τ / 2.0, Angle.radians)
    val oneHundredEightyDegrees = Angle (180.0, Angle.degrees)
    val twoHundredGradients = Angle (200.0, Angle.gradients)
    val oneHalfRevolution = Angle (1.0 / 2.0, Angle.revolutions)

/*
All of these values should have a value of τ radians (360 degrees), which
should normalize to 0 radians (0 degrees).
*/

    val tauRadians = Angle (τ, Angle.radians)
    val threeHundredSixtyDegrees = Angle (360.0, Angle.degrees)
    val fourHundredGradients = Angle (400.0, Angle.gradients)
    val oneRevolution = Angle (1.0, Angle.revolutions)
  }

/*
Test fixture description.
*/

  describe (Angle.getClass.getCanonicalName) {

/*
Test that the constant π has the correct value.
*/

    describe (".π") {
      it ("must equal the value of physical constant π") {
        assert (π === Math.PI)
      }
    }

/*
Test that the constant τ has the correct value.
*/

    describe (".τ") {
      it ("must equal the value of physical constant τ (=2π)") {
        assert (τ === 2.0 * Math.PI)
      }
    }

/*
Test basic specific measure behavior (creation, equality, addition, etc.).
This should come after the constant testing above, but before the higher-level
stuff below.
*/

    it must behave like specificBehavior (/* test fixtures instances go here */)

/*
Test the arc sine function.
*/

    describe (".arcsine (Double)") {
    }
  }
}