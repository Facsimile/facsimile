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

import org.facsim.measure.{Angle, Length, Point}
import org.facsim.util.test.CommonTestMethods
import org.scalatest.FunSpec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/**
 * Test suite for the [[Point]] class and companion object.
 */
class PointTest
extends FunSpec
with CommonTestMethods {

  /**
   * Test data.
   */
  trait TestData {
    val one = Length(1.0)
    val two = Length(2.0)
    val minusOne = Length(-1.0)
    val minusTwo = Length(-2.0)
    val sqrtTwo = Length(Math.sqrt(2.0))
    val origin = Point.Origin
    val east = Point(one, Length.Zero, Length.Zero)
    val north = Point(Length.Zero, one, Length.Zero)
    val west = Point(minusOne, Length.Zero, Length.Zero)
    val south = Point(Length.Zero, minusOne, Length.Zero)
    val up = Point(Length.Zero, Length.Zero, one)
    val down = Point(Length.Zero, Length.Zero, minusOne)
    val directions = List(east, north, west, south, up, down)
    val d0 = Angle.Zero
    val d90 = Angle.τ / 4.0
    val d180 = Angle.τ / 2.0
    val d270 = Angle.τ * 3.0 / 4.0
  }

  /*
   * Class test fixture description.
   */
  describe(classOf[Point].getCanonicalName) {

    /*
     * Construction must succeed OK.
     */
    describe(".this(Length.Measure, Length.Measure, Length.Measure)") {

      /*
       * Check that passing null values will cause a failure.
       */
      it("must throw NullPointerException if passed null x co-ordinate") {
        val e = intercept[NullPointerException] {
          Point(null, Length.Zero, Length.Zero) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "x")
      }
      it("must throw NullPointerException if passed null y co-ordinate") {
        val e = intercept[NullPointerException] {
          Point(Length.Zero, null, Length.Zero) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "y")
      }
      it("must throw NullPointerException if passed null z co-ordinate") {
        val e = intercept[NullPointerException] {
          Point(Length.Zero, Length.Zero, null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "z")
      }

      /*
       * Check that regular construction passes without error.
       */
      it("must construct points from valid arguments without error") {
        new TestData {
          Point(one, one, one)
          Point(two, two, two)
        }
      }
    }

    /*
     * Test it reports its arguments OK.
     */
    describe(".x") {
      it("must report the correct x value") {
        new TestData {
          assert(origin.x === Length.Zero)
          assert(east.x === one)
          assert(north.x === Length.Zero)
          assert(west.x === minusOne)
          assert(south.x === Length.Zero)
          assert(up.x === Length.Zero)
          assert(down.x === Length.Zero)
        }
      }
    }
    describe(".y") {
      it("must report the correct y value") {
        new TestData {
          assert(origin.y === Length.Zero)
          assert(east.y === Length.Zero)
          assert(north.y === one)
          assert(west.y === Length.Zero)
          assert(south.y === minusOne)
          assert(up.y === Length.Zero)
          assert(down.y === Length.Zero)
        }
      }
    }
    describe(".z") {
      it("must report the correct z value") {
        new TestData {
          assert(origin.z === Length.Zero)
          assert(east.z === Length.Zero)
          assert(north.z === Length.Zero)
          assert(west.z === Length.Zero)
          assert(south.z === Length.Zero)
          assert(up.z === one)
          assert(down.z === minusOne)
        }
      }
    }

    /*
     * Test that straight-line distances to other points are reported correctly.
     */
    describe(".distanceTo(Point)") {
      it("must throw a NullPointerException if passed a null point") {
        val e = intercept[NullPointerException] {
          Point.Origin.distanceTo(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "other")
      }
      it("must calculate the correct distance if passed a valid point") {
        new TestData {
          assert(origin.distanceTo(origin) === Length.Zero)
          assert(origin.distanceTo(east) === one)
          assert(origin.distanceTo(north) === one)
          assert(origin.distanceTo(west) === one)
          assert(origin.distanceTo(south) === one)
          assert(origin.distanceTo(up) === one)
          assert(origin.distanceTo(down) === one)
          assert(north.distanceTo(origin) === one)
          assert(north.distanceTo(south) === two)
          assert(north.distanceTo(east) === sqrtTwo)
          assert(north.distanceTo(west) === sqrtTwo)
          assert(north.distanceTo(up) === sqrtTwo)
          assert(north.distanceTo(down) === sqrtTwo)
          assert(south.distanceTo(east) === sqrtTwo)
          assert(south.distanceTo(west) === sqrtTwo)
          assert(south.distanceTo(up) === sqrtTwo)
          assert(south.distanceTo(down) === sqrtTwo)
          assert(south.distanceTo(origin) === one)
          assert(south.distanceTo(north) === two)
          assert(west.distanceTo(north) === sqrtTwo)
          assert(west.distanceTo(south) === sqrtTwo)
          assert(west.distanceTo(up) === sqrtTwo)
          assert(west.distanceTo(down) === sqrtTwo)
          assert(west.distanceTo(origin) === one)
          assert(west.distanceTo(east) === two)
          assert(east.distanceTo(north) === sqrtTwo)
          assert(east.distanceTo(south) === sqrtTwo)
          assert(east.distanceTo(up) === sqrtTwo)
          assert(east.distanceTo(down) === sqrtTwo)
          assert(east.distanceTo(origin) === one)
          assert(east.distanceTo(west) === two)
          assert(up.distanceTo(origin) === one)
          assert(up.distanceTo(down) === two)
          assert(up.distanceTo(north) === sqrtTwo)
          assert(up.distanceTo(south) === sqrtTwo)
          assert(up.distanceTo(east) === sqrtTwo)
          assert(up.distanceTo(west) === sqrtTwo)
          assert(down.distanceTo(origin) === one)
          assert(down.distanceTo(up) === two)
          assert(down.distanceTo(north) === sqrtTwo)
          assert(down.distanceTo(south) === sqrtTwo)
          assert(down.distanceTo(east) === sqrtTwo)
          assert(down.distanceTo(west) === sqrtTwo)
        }
      }
    }

    /*
     * Test that X-Y distances to other points are reported correctly.
     */
    describe(".distanceToXY(Point)") {
      it("must throw a NullPointerException if passed a null point") {
        val e = intercept[NullPointerException] {
          Point.Origin.distanceToXY(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "other")
      }
      it("must calculate the correct distance if passed a valid point") {
        new TestData {
          assert(origin.distanceToXY(origin) === Length.Zero)
          assert(origin.distanceToXY(east) === one)
          assert(east.distanceToXY(origin) === one)
          assert(east.distanceToXY(west) === two)
          assert(origin.distanceToXY(north) === one)
          assert(north.distanceToXY(origin) === one)
          assert(north.distanceToXY(south) === two)
          assert(origin.distanceToXY(west) === one)
          assert(west.distanceToXY(origin) === one)
          assert(west.distanceToXY(east) === two)
          assert(origin.distanceToXY(south) === one)
          assert(south.distanceToXY(origin) === one)
          assert(south.distanceToXY(north) === two)
          assert(origin.distanceToXY(up) === Length.Zero)
          assert(up.distanceToXY(origin) === Length.Zero)
          assert(up.distanceToXY(down) === Length.Zero)
          assert(origin.distanceToXY(down) === Length.Zero)
          assert(down.distanceToXY(origin) === Length.Zero)
          assert(down.distanceToXY(up) === Length.Zero)
        }
      }
    }

    /*
     * Test that X-Y angles to points are reported correctly.
     */
    describe(".angleToXY(Point)") {
      it("must throw a NullPointerException if passed a null point") {
        val e = intercept[NullPointerException] {
          Point.Origin.angleToXY(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "other")
      }
      it("must calculate the correct angle if passed a valid point") {
        new TestData {
          assert(origin.angleToXY(origin) === d0)
          assert(origin.angleToXY(east) === d0)
          assert(east.angleToXY(origin) === d180)
          assert(east.angleToXY(west) === d180)
          assert(origin.angleToXY(north) === d90)
          assert(north.angleToXY(origin) === d270)
          assert(north.angleToXY(south) === d270)
          assert(origin.angleToXY(west) === d180)
          assert(west.angleToXY(origin) === d0)
          assert(west.angleToXY(east) === d0)
          assert(origin.angleToXY(south) === d270)
          assert(south.angleToXY(origin) === d90)
          assert(south.angleToXY(north) === d90)
          assert(origin.angleToXY(up) === d0)
          assert(up.angleToXY(origin) === d0)
          assert(up.angleToXY(down) === d0)
          assert(origin.angleToXY(down) === d0)
          assert(down.angleToXY(origin) === d0)
          assert(down.angleToXY(up) === d0)
        }
      }
    }
  }

  /*
   * Companion object test fixture description.
   */
  describe(Point.getClass.getCanonicalName) {

    /*
     * Verify that the Origin is defined correctly.
     */
    describe(".Origin") {
      it("must be defined at the world origin") {
        assert(Point.Origin.x === Length.Zero)
        assert(Point.Origin.y === Length.Zero)
        assert(Point.Origin.z === Length.Zero)
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc