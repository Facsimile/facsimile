/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

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

import org.facsim.anim.cell.RotationOrder
import org.scalatest.FunSpec
import scalafx.geometry.Point3D
import scalafx.scene.transform.Rotate

//=============================================================================
/**
Test suite for the [[org.facsim.anim.cell.RotationOrder$]] object.
*/
//=============================================================================

class RotationOrderTest extends FunSpec {

/*
Test data.
*/

  trait TestData {
    val validCodes = RotationOrder.minValue to RotationOrder.maxValue
    val validRotationOrders = Vector [RotationOrder.Value] (
      RotationOrder.XYZ,
      RotationOrder.XZY,
      RotationOrder.YXZ,
      RotationOrder.YZX,
      RotationOrder.ZXY,
      RotationOrder.ZYX
    )
    val validFXSequences = Vector [List [Point3D]] (
      List (Rotate.XAxis, Rotate.YAxis, Rotate.ZAxis),
      List (Rotate.XAxis, Rotate.ZAxis, Rotate.YAxis),
      List (Rotate.YAxis, Rotate.XAxis, Rotate.ZAxis),
      List (Rotate.YAxis, Rotate.ZAxis, Rotate.XAxis),
      List (Rotate.ZAxis, Rotate.XAxis, Rotate.YAxis),
      List (Rotate.ZAxis, Rotate.YAxis, Rotate.XAxis)
    )
    val invalidCodes = List (Int.MinValue, RotationOrder.minValue - 1,
    RotationOrder.maxValue + 1, Int.MaxValue)
  }

/*
Test fixture description.
*/

  describe (RotationOrder.getClass.getCanonicalName) {

/*
Test the apply function works as expected.
*/

    describe (".apply (Int)") {
      new TestData {
        it ("must throw a java.util.NoSuchElementException if passed an " +
        "invalid rotation order code") {
          invalidCodes.foreach {
            code =>
            intercept [NoSuchElementException] {
              RotationOrder (code)
            }
          }
        }
        it ("must return the correct rotation order if passed a valid " +
        "rotation order code") {
          validCodes.foreach {
            code =>
            assert (RotationOrder (code) === validRotationOrders (code))
          }
        }
      }
    }

/*
Test the verify function works as expected.
*/

    describe (".verify (Int)") {
      new TestData {
        it ("must return false if passed an invalid rotation order code") {
          invalidCodes.foreach {
            code =>
            assert (RotationOrder.verify (code) === false)
          }
        }
        it ("must return true if passed a valid rotation order code") {
          validCodes.foreach {
            code =>
            assert (RotationOrder.verify (code) === true)
          }
        }
      }
    }

/*
Test that the toAxisSequence function works as expected.
*/

    describe (".toAxisSequence (RotationOrder.Value)") {
      new TestData {
        it ("must return correct associated ScalaFX axis sequence") {
          RotationOrder.values.foreach {
            rotationOrder =>
            assert (RotationOrder.toAxisSequence (rotationOrder) ===
            validFXSequences (rotationOrder.id))
          }
        }
      }
    }
  }
}