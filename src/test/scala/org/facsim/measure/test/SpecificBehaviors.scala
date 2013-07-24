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
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.measure.test package.
*/
//=============================================================================

package org.facsim.measure.test

//import org.facsim.test.EqualsBehaviors
import org.facsim.measure.Specific
import org.facsim.test.CommonTestMethods
import org.scalatest.FunSpec

//=============================================================================
/**
Test behaviors for the [[org.facsim.measure.Specific!]] abstract class.
*/
//=============================================================================

trait SpecificBehaviors [Q <: Specific] extends PhysicalQuantityBehaviors [Q] {
  this: FunSpec =>

//-----------------------------------------------------------------------------
/**
Verify a [[org.facsim.measure.Specific!]]-implementing object.

@param fixture Test fixture providing information to be used by the tests.
*/
//-----------------------------------------------------------------------------

  final def specificBehavior (fixture: SpecificFixture [Q]): Unit = {

/*
Firstly, verify the physical quantity type behavior.
*/

    it must behave like physicalQuantityTypeBehavior (fixture)

/*
Verify that the family reported for this physical quantity matches
requirements.
*/

    describe (".family") {
      it ("must report the correct family") {
        assert (fixture.physQty.family === fixture.expectedFamily)
      }
    }

/*
Now verify the apply(Double) method.
*/

    describe (".apply (Double)") {

/*
Verify that non-finite values are rejected.
*/

      it ("must throw an IllegalArgumentException if passed a non-finite " +
      "value") {
        fixture.nonFiniteValues.foreach {
          value =>
          val e = intercept [IllegalArgumentException] {
            fixture.physQty.apply (value)
          }
          assertRequireFiniteMsg (e, "value", value)
        }
      }

/*
Verify that applying bad values will be rejected with the appropriate
exception.
*/

      it ("must throw an IllegalArgumentException if passed a bad value") {
        fixture.invalidValues.foreach {
          pair =>
          val (value, condition) = pair
          val e = intercept [IllegalArgumentException] {
            fixture.physQty.apply (value)
          }
          assertRequireValidMsg (e, "value", value)
        }
      }

/*
Verify that applying good values will be accepted without any exceptions being
thrown.
*/

      it ("must not throw an exception when passed a valid value") {
        fixture.validValues.foreach {
          value =>
          assert (fixture.physQty.apply (value) ne null)
        }
      }
    }
  }
}