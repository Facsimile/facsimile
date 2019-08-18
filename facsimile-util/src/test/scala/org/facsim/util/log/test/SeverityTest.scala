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

import org.facsim.util.log._
import org.scalatest.FunSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

// Disable test-problematic Scalastyle checkers.
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers

/** Test harness for the [[Severity]] trait and sub-classes. */
final class SeverityTest
extends FunSpec
with ScalaCheckPropertyChecks {

  // DebugSeverity
  describe(DebugSeverity.getClass.getCanonicalName) {
    describe(".abbrName") {
      it("must be 'DBG'") {
        assert(DebugSeverity.abbrName === "DBG")
      }
    }
    describe(".name") {
      it("must be 'debug'") {
        assert(DebugSeverity.name === "debug")
      }
    }
    describe(".compare(MessageSeverity)") {
      it("must compare as equal to itself") {
        assert(DebugSeverity.compare(DebugSeverity) === 0)
      }
      it("must compare as less than InformationSeverity") {
        assert(DebugSeverity.compare(InformationSeverity) < 0)
      }
    }
  }

  // InformationSeverity
  describe(InformationSeverity.getClass.getCanonicalName) {
    describe(".abbrName") {
      it("must be 'INF'") {
        assert(InformationSeverity.abbrName === "INF")
      }
    }
    describe(".name") {
      it("must be 'information'") {
        assert(InformationSeverity.name === "information")
      }
    }
    describe(".compare(MessageSeverity)") {
      it("must compare as greater than DebugSeverity") {
        assert(InformationSeverity.compare(DebugSeverity) > 0)
      }
      it("must compare as equal to itself") {
        assert(InformationSeverity.compare(InformationSeverity) === 0)
      }
      it("must compare as less than WarningSeverity") {
        assert(InformationSeverity.compare(WarningSeverity) < 0)
      }
    }
  }

  // WarningSeverity
  describe(WarningSeverity.getClass.getCanonicalName) {
    describe(".abbrName") {
      it("must be 'WRN'") {
        assert(WarningSeverity.abbrName === "WRN")
      }
    }
    describe(".name") {
      it("must be 'warning'") {
        assert(WarningSeverity.name === "warning")
      }
    }
    describe(".compare(MessageSeverity)") {
      it("must compare as greater than InformationSeverity") {
        assert(WarningSeverity.compare(InformationSeverity) > 0)
      }
      it("must compare as equal to itself") {
        assert(WarningSeverity.compare(WarningSeverity) === 0)
      }
      it("must compare as less than ImportantSeverity") {
        assert(WarningSeverity.compare(ImportantSeverity) < 0)
      }
    }
  }

  // ImportantSeverity
  describe(ImportantSeverity.getClass.getCanonicalName) {
    describe(".abbrName") {
      it("must be 'IMP'") {
        assert(ImportantSeverity.abbrName === "IMP")
      }
    }
    describe(".name") {
      it("must be 'important'") {
        assert(ImportantSeverity.name === "important")
      }
    }
    describe(".compare(MessageSeverity)") {
      it("must compare as greater than WarningSeverity") {
        assert(ImportantSeverity.compare(WarningSeverity) > 0)
      }
      it("must compare as equal to itself") {
        assert(ImportantSeverity.compare(ImportantSeverity) === 0)
      }
      it("must compare as less than ErrorSeverity") {
        assert(ImportantSeverity.compare(ErrorSeverity) < 0)
      }
    }
  }

  // ErrorSeverity
  describe(ErrorSeverity.getClass.getCanonicalName) {
    describe(".abbrName") {
      it("must be 'ERR'") {
        assert(ErrorSeverity.abbrName === "ERR")
      }
    }
    describe(".name") {
      it("must be 'error'") {
        assert(ErrorSeverity.name === "error")
      }
    }
    describe(".compare(MessageSeverity)") {
      it("must compare as greater than ImportantSeverity") {
        assert(ErrorSeverity.compare(ImportantSeverity) > 0)
      }
      it("must compare as equal to itself") {
        assert(ErrorSeverity.compare(ErrorSeverity) === 0)
      }
      it("must compare as less than FatalSeverity") {
        assert(ErrorSeverity.compare(FatalSeverity) < 0)
      }
    }
  }

  // FatalSeverity
  describe(FatalSeverity.getClass.getCanonicalName) {
    describe(".abbrName") {
      it("must be 'FTL'") {
        assert(FatalSeverity.abbrName === "FTL")
      }
    }
    describe(".name") {
      it("must be 'fatal'") {
        assert(FatalSeverity.name === "fatal")
      }
    }
    describe(".compare(MessageSeverity)") {
      it("must compare as greater than ErrorSeverity") {
        assert(FatalSeverity.compare(ErrorSeverity) > 0)
      }
      it("must compare as equal to itself") {
        assert(FatalSeverity.compare(FatalSeverity) === 0)
      }
    }
  }
}

// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on magic.numbers
//scalastyle:on multiple.string.literals
//scalastyle:on public.methods.have.type
//scalastyle:on scaladoc