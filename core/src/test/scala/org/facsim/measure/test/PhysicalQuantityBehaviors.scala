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

import org.facsim.measure.Physical
import org.facsim.test.CommonTestMethods
import org.facsim.test.EqualsBehaviors
import org.scalatest.FunSpec

//=============================================================================
/**
Test behaviors for [[org.facsim.measure.PhysicalQuantity!]] sub-classes.

@tparam V PhysicalQuantity sub-class to be tested.
*/
//=============================================================================

trait PhysicalQuantityBehaviors [Q <: Physical] extends
CommonTestMethods {
  this: FunSpec =>

//-----------------------------------------------------------------------------
/**
Verify a [[org.facsim.measure.PhysicalQuantity!]]-implementing object.

@param fixture Test fixture providing information to be used by the tests.
*/
//-----------------------------------------------------------------------------

  final def physicalQuantityTypeBehavior (fixture: PhysicalQuantityFixture
  [Q]): Unit = {

/*
Test the physical quantity's SI units are reported correctly.
*/

    describe (".siUnits") {
      it ("must report correct SI units for this type") {
        assert (fixture.physQty.siUnits === fixture.expectedSIUnits)
      }
    }

/*
Test that the units for this physical quantity preferred by the user are being
reported correctly.

TODO: Right now, this is simply the same as the SI units, but that will change
in time.
*/

    describe (".preferredUnits") {
      it ("must report user's preferred units for this type") {
        assert (fixture.physQty.preferredUnits === fixture.expectedSIUnits)
      }
    }
  }
}