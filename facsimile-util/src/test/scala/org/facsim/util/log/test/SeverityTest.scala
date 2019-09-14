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

import java.util.Locale
import org.facsim.util.log._
import org.facsim.util.test.Generator.unicodeString
import org.facsim.util.test.withLocale
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

  /** Common test data trait. */
  trait TestData {

    /** Severities, listed from lowest to highest rank. */
    lazy val severities: List[Severity] = List(
      DebugSeverity,
      InformationSeverity,
      WarningSeverity,
      ImportantSeverity,
      ErrorSeverity,
      FatalSeverity
    )

    /** Names of the severities, listed from lowest to highest rank. */
    lazy val names: List[String] = severities.map(_.name)

    /** Set of all severity names. */
    lazy val severityNameSet: Set[String] = names.toSet

    /** Tuples of name and severity. */
    lazy val namedSeverity: List[(String, Option[Severity])] = severities.map(s => (s.name, Some(s)))
  }

  // Severity companion.
  describe(Severity.getClass.getCanonicalName) {

    // Check that we get a correctly ordered list of severity names.
    describe(".severityList") {

      // Check the returned list.
      it("must return all the severities in order of rank.") {
        new TestData {
          assert(Severity.severityList === names)
        }
      }
    }

    // Check that we can look up the correct severity instance by name.
    describe(".withName(String)") {

      // Look up each name and check we get the expected result.
      it("must retrieve the correct severity, wrapped in Some, given a valid name") {
        new TestData {
          namedSeverity.foreach {
            case (name, result) => assert(Severity.withName(name) === result)
          }
        }
      }

      // Verify that it fails when given an invalid severity name.
      it("must report None given an invalid severity name") {
        new TestData {
          forAll(unicodeString) {s =>
            whenever(!severityNameSet.contains(s)) {
              assert(Severity.withName(s) === None)
            }
          }
        }
      }
    }
  }

  // DebugSeverity
  describe(DebugSeverity.getClass.getCanonicalName) {
    describe(".abbrName") {
      it("must be 'DBG'") {
        withLocale(Locale.US) {
          assert(DebugSeverity.abbrName === "DBG")
        }
      }
    }
    describe(".name") {
      it("must be 'debug'") {
        withLocale(Locale.US) {
          assert(DebugSeverity.name === "debug")
        }
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
        withLocale(Locale.US) {
          assert(InformationSeverity.abbrName === "INF")
        }
      }
    }
    describe(".name") {
      it("must be 'information'") {
        withLocale(Locale.US) {
          assert(InformationSeverity.name === "information")
        }
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
        withLocale(Locale.US) {
          assert(WarningSeverity.abbrName === "WRN")
        }
      }
    }
    describe(".name") {
      it("must be 'warning'") {
        withLocale(Locale.US) {
          assert(WarningSeverity.name === "warning")
        }
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
        withLocale(Locale.US) {
          assert(ImportantSeverity.abbrName === "IMP")
        }
      }
    }
    describe(".name") {
      it("must be 'important'") {
        withLocale(Locale.US) {
          assert(ImportantSeverity.name === "important")
        }
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
        withLocale(Locale.US) {
          assert(ErrorSeverity.abbrName === "ERR")
        }
      }
    }
    describe(".name") {
      it("must be 'error'") {
        withLocale(Locale.US) {
          assert(ErrorSeverity.name === "error")
        }
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
        withLocale(Locale.US) {
          assert(FatalSeverity.abbrName === "FTL")
        }
      }
    }
    describe(".name") {
      it("must be 'fatal'") {
        withLocale(Locale.US) {
          assert(FatalSeverity.name === "fatal")
        }
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