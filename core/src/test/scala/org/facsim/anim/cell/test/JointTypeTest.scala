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

import org.facsim.anim.cell.JointType
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.anim.cell.JointType$]] object.
*/
//=============================================================================

class JointTypeTest extends FunSpec {

/*
Test data.
*/

  trait TestData {
    val validCodes = JointType.minValue to JointType.maxValue
    val validMap = Map [Int, JointType.Value] (
      0 -> JointType.TCFOnly,
      1 -> JointType.Rotational,
      2 -> JointType.Translational
    )
    val invalidCodes = List (Int.MinValue, JointType.minValue - 1,
    JointType.maxValue + 1, Int.MaxValue)
  }

/*
Test fixture description.
*/

  describe (JointType.getClass.getCanonicalName) {

/*
Test the apply function works as expected.
*/

    describe (".apply (Int)") {
      new TestData {
        it ("must throw a java.util.NoSuchElementException if passed an " +
        "invalid joint type code") {
          invalidCodes.foreach {
            code =>
            intercept [NoSuchElementException] {
              JointType (code)
            }
          }
        }
        it ("must return the correct joint type if passed a valid joint type "
        + "code") {
          validCodes.foreach {
            code =>
            assert (JointType (code) === validMap (code))
          }
        }
      }
    }

/*
Test the verify function works as expected.
*/

    describe (".verify (Int)") {
      new TestData {
        it ("must return false if passed an invalid joint type code") {
          invalidCodes.foreach {
            code =>
            assert (JointType.verify (code) === false)
          }
        }
        it ("must return true if passed a valid joint type code") {
          validCodes.foreach {
            code =>
            assert (JointType.verify (code) === true)
          }
        }
      }
    }
  }
}