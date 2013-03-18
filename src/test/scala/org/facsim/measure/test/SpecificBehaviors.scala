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

trait SpecificBehaviors extends PhysicalQuantityBehaviors {
  this: FunSpec =>

//-----------------------------------------------------------------------------
/**
Ensure that the [[org.facsim.measure.Specific!.apply(Double,
org.facsim.measure.Specific!.Units)*]] method functions as expected.

@param pq Specific physical quantity instance to be tested.
*/
//-----------------------------------------------------------------------------

  def applyDoubleUnitsBehavior (pq: Specific) {
    describe (".apply (Double, Units)") {

/*
We must get an exception if NaN is passed as the value of the physical
quantity.
*/

      it ("must throw IllegalArgumentException if passed NaN") {
        val value = Double.NaN
        val e = intercept [IllegalArgumentException] {
          pq (value, pq.getSIUnits)
        }
        assertIllegalArgumentMessage (e, "value", value)
      }

/*
We must get an exception if -∞ is passed as the value of the physical quantity.
*/

      it ("must throw IllegalArgumentException if passed -∞") {
        val value = Double.NegativeInfinity
        val e = intercept [IllegalArgumentException] {
          pq (value, pq.getSIUnits)
        }
        assertIllegalArgumentMessage (e, "value", value)
      }

/*
We must get an exception if ∞ is passed as the value of the physical quantity.
*/

      it ("must throw IllegalArgumentException if passed ∞") {
        val value = Double.PositiveInfinity
        val e = intercept [IllegalArgumentException] {
          pq (value, pq.getSIUnits)
        }
        assertIllegalArgumentMessage (e, "value", value)
      }

/*
It must accept the value 0, return a measurement of the right type and the SI
unit value of that measurement must be zero also.
*/

      it ("must accept 0 in the SI units") {
        val measure = pq (0.0, pq.getSIUnits)
        assert (measure.getValue === 0.0)
      }

/*
It must accept the value 0, return a measurement of the right type and the SI
unit value of that measurement must be zero also.
*/

      it ("must accept 0 in the SI units") {
        val measure = pq (0.0, pq.getSIUnits)
        assert (measure.getValue === 0.0)
      }
    }
  }
}