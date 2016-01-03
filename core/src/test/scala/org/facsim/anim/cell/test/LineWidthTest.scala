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

import org.facsim.anim.cell.LineWidth
import org.facsim.test.CommonTestMethods
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.anim.cell.LineWidth!]] object.
*/
//=============================================================================

class LineWidthTest
extends FunSpec
with CommonTestMethods {

/*
Test data.
*/

  trait TestData {
    val validValues = LineWidth.minValue to LineWidth.maxValue
    val invalidValues = List (Int.MinValue, LineWidth.minValue - 1,
    LineWidth.maxValue + 1, Int.MaxValue)
  }

/*
Test fixture description for class.
*/

  describe (classOf [LineWidth].getCanonicalName) {

/*
Test the constructor.
*/

    describe (".this (Int)") {
      new TestData {
        it ("must throw a java.lang.IllegalArgumentException if passed an " +
        "invalid line width value") {
          invalidValues.foreach {
            value =>
            val e = intercept [IllegalArgumentException] {
              new LineWidth (value)
            }
            assertRequireValidMsg (e, "lineWidth", value)
          }
        }
        it ("must accept valid line width values") {
          validValues.foreach {
            value =>
            new LineWidth (value)
          }
        }
      }
    }
  }

/*
Test fixture description for object.
*/

  describe (LineWidth.getClass.getCanonicalName) {

/*
Test that the default line width is reported correctly.
*/

    describe (".default") {
      it ("must be 1") {
        assert (LineWidth.default.lineWidth === 1)
      }
    }

/*
Test the verify function works as expected.
*/

    describe (".verify (Int)") {
      new TestData {
        it ("must return false if passed an invalid line width value") {
          invalidValues.foreach {
            value =>
            assert (LineWidth.verify (value) === false)
          }
        }
        it ("must return true if passed a valid line width value") {
          validValues.foreach {
            value =>
            assert (LineWidth.verify (value) === true)
          }
        }
      }
    }
  }
}