/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
Scala source file from the org.facsim.io.test package.
*/
//======================================================================================================================

package org.facsim.io.test

import org.facsim.io.Delimiter
import org.facsim.io.TextReader
import org.facsim.test.CommonTestMethods
import org.scalatest.FunSpec

//======================================================================================================================
/**
Test suite for the [[org.facsim.io.Delimiter!]] class.
*/
//======================================================================================================================

class DelimiterTest
extends FunSpec
with CommonTestMethods {

/*
Test fixture description.
*/

  describe (classOf [Delimiter].getCanonicalName) {

/*
Primary constructor tests.
*/

    describe (".this (Set [Int], Boolean)") {
      it ("must throw NullPointerException if passed null set") {
        val e = intercept [NullPointerException] {
          new Delimiter (null, true)
        }
        assertRequireNonNullMsg (e, "delimiters")
      }
      it ("must accept an empty set of delimiters") {
        new Delimiter (Set (), true)
      }
      it ("must throw IllegalArgumentException if passed a set containing " +
      "null character") {
        val nulSet = Set (TextReader.NUL)
        val e = intercept [IllegalArgumentException] {
          new Delimiter (nulSet, true)
        }
        assertRequireValidMsg (e, "delimiters", nulSet)
      }
      it ("must throw IllegalArgumentException if passed a set containing " +
      "carriage return character") {
        val crSet = Set (TextReader.CR)
        val e = intercept [IllegalArgumentException] {
          new Delimiter (crSet, true)
        }
        assertRequireValidMsg (e, "delimiters", crSet)
      }
    }
  }
}