/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

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
Scala source file from the org.facsim.test package.
*/
//=============================================================================

package org.facsim.test

import org.facsim.requireNonNull
import org.facsim.requireValid
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim]] package object.
*/
//=============================================================================

class PackageTest extends FunSpec {

/*
Test fixture description.
*/

  describe ("org.facsim package") {

/*
requireNonNull tests.
*/

    describe (".requireNonNull (argName, argValue)") {
      it ("must throw NullPointerException if passed null argValue") {
        val e = intercept [NullPointerException] {
          requireNonNull ("testArgument", null)
        }
        assert (e.getMessage () === "Argument 'testArgument' cannot be null.")
      }
      it ("must do nothing if passed a non-null reference") {
        requireNonNull ("testArgument", "A non-null value")
      }
    }

/*
requireValid tests.
*/

    describe (".requireValid (argName, argValue, condition)") {
      it ("must throw IllegalArgumentException if condition is false") {
        val e = intercept [IllegalArgumentException] {
          requireValid ("testArgument", "someBadValue", false)
        }
        assert (e.getMessage () === "Argument 'testArgument' has illegal " +
        "value: someBadValue")
      }
      it ("must do nothing if passed a valid argument") {
        requireValid ("testArgument", "someOKValue", true)
      }
    }
  }
}