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

import org.facsim.measure.SIConverter
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.measure.SIConverter$]] object.
*/
//=============================================================================


class SIConverterTest extends FunSpec {

/*
Test fixture description.
*/

  describe (SIConverter.getClass.getCanonicalName) {

/*
Importing should report the value passed.
*/

    describe (".importValue(Double)") {

/*
Check that we always get the result of the argument passed.
*/

      it ("must return the argument passed") {

/*
The return value must exactly match the value passed.  There can be no rounding
errors.
*/

        def checkReturn (value: Double): Unit = {
          assert (SIConverter.importValue (value) === value)
        }
        checkReturn (Double.MinValue)
        checkReturn (-1.0)
        checkReturn (-Double.MinPositiveValue)
        checkReturn (0.0)
        checkReturn (Double.MinPositiveValue)
        checkReturn (1.0)
        checkReturn (Double.MaxValue)
      }
    }

/*
Exporting should report the value passed.
*/

    describe (".exportValue(Double)") {

/*
Check that we always get the result of the argument passed.
*/

      it ("must return the argument passed") {

/*
The return value must exactly match the value passed.  There can be no rounding
errors.
*/

        def checkReturn (value: Double): Unit = {
          assert (SIConverter.exportValue (value) === value)
        }
        checkReturn (Double.MinValue)
        checkReturn (-1.0)
        checkReturn (-Double.MinPositiveValue)
        checkReturn (0.0)
        checkReturn (Double.MinPositiveValue)
        checkReturn (1.0)
        checkReturn (Double.MaxValue)
      }
    }
  }
}