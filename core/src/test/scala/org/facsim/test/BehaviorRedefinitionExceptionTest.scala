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
Scala source file from the org.facsim.test package.
*/
//=============================================================================

package org.facsim.test

import org.facsim.Behavior
import org.facsim.BehaviorRedefinitionException
import org.facsim.LibResource
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.BehaviorRedefinitionException!]] class.
*/
//=============================================================================

class BehaviorRedefinitionExceptionTest
extends FunSpec {

/**
Test trait.
*/

  trait TestData {
    val b = new Behavior {}
    val e = new BehaviorRedefinitionException (b, b)
  }

/*
Test fixture description.
*/

  describe (classOf [BehaviorRedefinitionException].getCanonicalName) {

/*
Construction tests.
*/

    describe (".this ()") {
      it ("must construct OK") {
        new TestData {
          assert (e ne null)
        }
      }
    }

/*
Test getMessage.
*/

    describe (".getMessage ()") {
      it ("must return correct message") {
        new TestData {
          assert (e.getMessage () === LibResource ("BehaviorRedefinition", b,
          b))
        }
      }
    }
  }
}