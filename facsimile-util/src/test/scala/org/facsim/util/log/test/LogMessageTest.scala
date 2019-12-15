//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2019, Michael J Allen.
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
// Scala source file belonging to the org.facsim.util.log.test package.
//======================================================================================================================
package org.facsim.util.log.test

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.funspec.AnyFunSpec

// Disable test-problematic Scalastyle checkers.
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers

/** Test harness for the [[org.facsim.util.log.LogMessage]] class. */
final class LogMessageTest
extends AnyFunSpec
with ScalaCheckPropertyChecks {

  // Class under test.
  describe("org.facsim.util.log.LogMessage[A]") {

    // Test construction.
    describe(".ctor(A, String, Scope, Severity)") {

      // Test construction succeeds OK.
      it("must construct valid log messages") {
        forAll(logs) {l =>

          // Dummy operation, just to ensure it works.
          l.toString
        }
      }
    }

    // Test that the resulting string matches expectations
    describe(".toString") {

      // It must format log messages correctly.
      it("must format correctly-formatted log messages") {
        forAll(logs) {l =>
          assert(l.toString === s"${l.prefix}: ${l.scope}: ${l.severity.abbrName}: ${l.msg}")
        }
      }
    }
  }
}

// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on magic.numbers
//scalastyle:on multiple.string.literals
//scalastyle:on public.methods.have.type
//scalastyle:on scaladoc