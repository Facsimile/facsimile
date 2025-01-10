//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================

//======================================================================================================================
// Scala source file belonging to the org.facsim.util.test package.
//======================================================================================================================
package org.facsim.util.test

import org.facsim.util.{AssertNonNullKey, LibResource, RequireFiniteKey, RequireNonNullKey, RequireValidKey}
import org.scalatest.funspec.AnyFunSpec

/** Test harness for the [[LibResource]] object.
 *
 *  Most behavior can be assumed to be tested by the [[ResourceTest]] test harness. Only specific remaining tests are
 *  included here.
 */
final class LibResourceTest
extends AnyFunSpec, CommonTestMethods:

  // Name the class we're testing.
  describe(LibResource.getClass.getCanonicalName.nn):

    // Test string resource formatting.
    describe("apply(String, Any*)"):

      // Test that facsimile-util messages can be retrieved.
      it("must retrieve all facsimile-util resources"):
        assert(LibResource(AssertNonNullKey, "test") === "Assertion failed: expression 'test' was evaluated as null.")
        assert(LibResource(RequireFiniteKey, "test", Double.NegativeInfinity) ===
        "Argument 'test' must be finite, but has value -∞.")
        assert(LibResource(RequireNonNullKey, "test") === "Argument 'test' cannot be null.")
        assert(LibResource(RequireValidKey, "test", -1) === "Argument 'test' has illegal value: '-1'.")