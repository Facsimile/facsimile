/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
Scala source file from the org.facsim.anim.cell.test package.
*/
//=============================================================================

package org.facsim.anim.cell.test

import org.facsim.anim.cell.DisplayStyle
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.anim.cell.DisplayStyle$]] object.
*/
//=============================================================================

class DisplayStyleTest extends FunSpec {

/*
Test data.
*/

  trait TestData {
    val validCodes = DisplayStyle.minValue to DisplayStyle.maxValue
    val validMap = Map [Int, DisplayStyle.Value] (
      0 -> DisplayStyle.Wireframe,
      1 -> DisplayStyle.Solid,
      2 -> DisplayStyle.Transparent1,
      3 -> DisplayStyle.Transparent2,
      4 -> DisplayStyle.Transparent3,
      5 -> DisplayStyle.Transparent4,
      6 -> DisplayStyle.Transparent5,
      7 -> DisplayStyle.Transparent6,
      8 -> DisplayStyle.Transparent7,
      9 -> DisplayStyle.Transparent8,
      10 -> DisplayStyle.Transparent9,
      11 -> DisplayStyle.Transparent10,
      12 -> DisplayStyle.Transparent11,
      13 -> DisplayStyle.Transparent12,
      14 -> DisplayStyle.Transparent13,
      15 -> DisplayStyle.Transparent14,
      16 -> DisplayStyle.Transparent15
    )
    val invalidCodes = List (Int.MinValue, DisplayStyle.minValue - 1,
    DisplayStyle.maxValue + 1, Int.MaxValue)
  }

/*
Test fixture description.
*/

  describe (DisplayStyle.getClass.getCanonicalName) {

/*
Test the apply function works as expected.
*/

    describe (".apply (Int)") {
      new TestData {
        it ("must throw a java.util.NoSuchElementException if passed an " +
        "invalid display style code") {
          invalidCodes.foreach {
            code =>
            intercept [NoSuchElementException] {
              DisplayStyle (code)
            }
          }
        }
        it ("must return the correct display style if passed a valid display "
        + "style code") {
          validCodes.foreach {
            code =>
            assert (DisplayStyle (code) === validMap (code))
          }
        }
      }
    }

/*
Test that the default display style is reported correctly.
*/

    describe (".default") {
      it ("must be Solid") {
        assert (DisplayStyle.Default === DisplayStyle.Solid)
      }
    }

/*
Test the verify function works as expected.
*/

    describe (".verify (Int)") {
      new TestData {
        it ("must return false if passed an invalid display style code") {
          invalidCodes.foreach {
            code =>
            assert (DisplayStyle.verify (code) === false)
          }
        }
        it ("must return true if passed a valid display style code") {
          validCodes.foreach {
            code =>
            assert (DisplayStyle.verify (code) === true)
          }
        }
      }
    }
  }
}