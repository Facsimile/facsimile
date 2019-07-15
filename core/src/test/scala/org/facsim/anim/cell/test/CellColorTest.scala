/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2019, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file from the org.facsim.anim.cell.test package.
*/

package org.facsim.anim.cell.test

import org.facsim.anim.cell.CellColor
import org.scalatest.FunSpec
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial

/**
Test suite for the [[org.facsim.anim.cell.CellColor$]] object.
*/

class CellColorTest extends FunSpec {

/*
Test data.
*/

  trait TestData {
    val validCodes = CellColor.minValue to CellColor.maxValue
    val validCellColors = Vector[CellColor.Value](
      CellColor.Black,
      CellColor.Red,
      CellColor.Green,
      CellColor.Yellow,
      CellColor.Blue,
      CellColor.Magenta,
      CellColor.Cyan,
      CellColor.White,
      CellColor.LightGray,
      CellColor.DarkGray,
      CellColor.Brown,
      CellColor.LightBlue,
      CellColor.Purple,
      CellColor.Orange,
      CellColor.LightGreen,
      CellColor.LightYellow
    )
    val validFXColors = Vector[Color](
      Color.Black,
      Color.Red,
      Color.Green,
      Color.Yellow,
      Color.Blue,
      Color.Magenta,
      Color.Cyan,
      Color.White,
      Color.LightGray,
      Color.DarkGray,
      Color.Brown,
      Color.LightBlue,
      Color.Purple,
      Color.Orange,
      Color.LightGreen,
      Color.LightYellow
    )
    val invalidCodes = List(Int.MinValue, CellColor.minValue - 1,
    CellColor.maxValue + 1, Int.MaxValue)
  }

/*
Test fixture description.
*/

  describe(CellColor.getClass.getCanonicalName) {

/*
Test the apply function works as expected.
*/

    describe(".apply(Int)") {
      new TestData {
        it("must throw a NoSuchElementException if passed an " +
        "invalid color code") {
          invalidCodes.foreach {
            code =>
            intercept[NoSuchElementException] {
              CellColor(code)
            }
          }
        }
        it("must return the correct color if passed a valid color code") {
          validCodes.foreach {
            code =>
            assert(CellColor(code) === validCellColors(code))
          }
        }
      }
    }

/*
Test that the default color is reported correctly.
*/

    describe(".Default") {
      it("must be Red") {
        val defaultColor = CellColor.Default
        assert(CellColor.Default == CellColor.Red)
      }
    }

/*
Test the verify function works as expected.
*/

    describe(".verify(Int)") {
      new TestData {
        it("must return false if passed an invalid color code") {
          invalidCodes.foreach {
            code =>
            assert(CellColor.verify(code) === false)
          }
        }
        it("must return true if passed a valid color code") {
          validCodes.foreach {
            code =>
            assert(CellColor.verify(code) === true)
          }
        }
      }
    }

/*
Test that the toColor function works as expected.
*/

    describe(".toColor(CellColor.Value)") {
      new TestData {
        it("must return correct associated ScalaFX color") {
          CellColor.values.foreach {
            color =>
            assert(CellColor.toColor(color) === validFXColors(color.id))
          }
        }
      }
    }

/*
Test that the toMaterial function works as expected.
*/

    describe(".toMaterial(CellColor.Value)") {
      new TestData {
        it("must return correct associated ScalaFX material") {
          CellColor.values.foreach {
            color =>
            val material = CellColor.toMaterial(color)
            // This is very fugly - update ScalaFX accordingly?
            assert(material.diffuseColor.value ===
            Color.sfxColor2jfx(validFXColors(color.id)))
            assert(material.specularColor.value ===
            Color.sfxColor2jfx(Color.White))
          }
        }
      }
    }
  }
}