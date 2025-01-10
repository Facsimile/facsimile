//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
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
import org.facsim.util.log.*
import org.facsim.util.test.Generator.unicodeString
import org.facsim.util.test.withLocale
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

// Disable test-problematic Scalastyle checkers.

/** Test harness for the [[Severity]] trait and subclasses.
 */
final class SeverityTest
extends AnyFunSpec, ScalaCheckPropertyChecks:

  /** Common test data trait.
   */
  trait TestData:

    /** Severities, listed from lowest to highest rank.
     */
    lazy val severities: List[Severity] = List(
      Severity.DebugSeverity,
      Severity.InformationSeverity,
      Severity.WarningSeverity,
      Severity.ImportantSeverity,
      Severity.ErrorSeverity,
      Severity.FatalSeverity
    )

    /** Names of the severities, listed from lowest to highest rank.
     */
    lazy val names: List[String] = severities.map(_.name)

    /** Set of all severity names.
     */
    lazy val severityNameSet: Set[String] = names.toSet

    /** Tuples of name and severity.
     */
    lazy val namedSeverity: List[(String, Option[Severity])] = severities.map(s => (s.name, Some(s)))

  // Severity companion.
  describe(Severity.getClass.getCanonicalName.nn):

    // Check that we get a correctly ordered list of severity names.
    describe(".severityList"):

      // Check the returned list.
      it("must return all the severities in order of rank."):
        new TestData:
          assert(Severity.severityList === names)

    // Check that we can look up the correct severity instance by name.
    describe(".withName(String)"):

      // Look up each name and check we get the expected result.
      it("must retrieve the correct severity, wrapped in Some, given a valid name"):
        new TestData:
          namedSeverity.foreach:
            case (name, result) => assert(Severity.withName(name) === result)

      // Verify that it fails when given an invalid severity name.
      it("must report None given an invalid severity name"):
        new TestData:
          forAll(unicodeString): s =>
            whenever(!severityNameSet.contains(s)):
              assert(Severity.withName(s) === None)

  // DebugSeverity
  describe("org.facsim.util.log.Severity.DebugSeverity"):
    describe(".abbrName"):
      it("must be 'DBG'"):
        withLocale(Locale.US.nn):
          assert(Severity.DebugSeverity.abbrName === "DBG")
    describe(".name"):
      it("must be 'debug'"):
        withLocale(Locale.US.nn):
          assert(Severity.DebugSeverity.name === "debug")
    describe(".compare(MessageSeverity)"):
      it("must compare as equal to itself"):
        assert(Severity.DebugSeverity.compare(Severity.DebugSeverity) === 0)
      it("must compare as less than InformationSeverity"):
        assert(Severity.DebugSeverity.compare(Severity.InformationSeverity) < 0)

  // InformationSeverity
  describe("org.facsim.util.log.Severity.InformationSeverity"):
    describe(".abbrName"):
      it("must be 'INF'"):
        withLocale(Locale.US.nn):
          assert(Severity.InformationSeverity.abbrName === "INF")
    describe(".name"):
      it("must be 'information'"):
        withLocale(Locale.US.nn):
          assert(Severity.InformationSeverity.name === "information")
    describe(".compare(MessageSeverity)"):
      it("must compare as greater than DebugSeverity"):
        assert(Severity.InformationSeverity.compare(Severity.DebugSeverity) > 0)
      it("must compare as equal to itself"):
        assert(Severity.InformationSeverity.compare(Severity.InformationSeverity) === 0)
      it("must compare as less than WarningSeverity"):
        assert(Severity.InformationSeverity.compare(Severity.WarningSeverity) < 0)

  // WarningSeverity
  describe("org.facsim.util.log.Severity.WarningSeverity"):
    describe(".abbrName"):
      it("must be 'WRN'"):
        withLocale(Locale.US.nn):
          assert(Severity.WarningSeverity.abbrName === "WRN")
    describe(".name"):
      it("must be 'warning'"):
        withLocale(Locale.US.nn):
          assert(Severity.WarningSeverity.name === "warning")
    describe(".compare(MessageSeverity)"):
      it("must compare as greater than InformationSeverity"):
        assert(Severity.WarningSeverity.compare(Severity.InformationSeverity) > 0)
      it("must compare as equal to itself"):
        assert(Severity.WarningSeverity.compare(Severity.WarningSeverity) === 0)
      it("must compare as less than ImportantSeverity"):
        assert(Severity.WarningSeverity.compare(Severity.ImportantSeverity) < 0)

  // ImportantSeverity
  describe("org.facsim.util.log.Severity.ImportantSeverity"):
    describe(".abbrName"):
      it("must be 'IMP'"):
        withLocale(Locale.US.nn):
          assert(Severity.ImportantSeverity.abbrName === "IMP")
    describe(".name"):
      it("must be 'important'"):
        withLocale(Locale.US.nn):
          assert(Severity.ImportantSeverity.name === "important")
    describe(".compare(MessageSeverity)"):
      it("must compare as greater than WarningSeverity"):
        assert(Severity.ImportantSeverity.compare(Severity.WarningSeverity) > 0)
      it("must compare as equal to itself"):
        assert(Severity.ImportantSeverity.compare(Severity.ImportantSeverity) === 0)
      it("must compare as less than ErrorSeverity"):
        assert(Severity.ImportantSeverity.compare(Severity.ErrorSeverity) < 0)

  // ErrorSeverity
  describe("org.facsim.util.log.Severity.ErrorSeverity"):
    describe(".abbrName"):
      it("must be 'ERR'"):
        withLocale(Locale.US.nn):
          assert(Severity.ErrorSeverity.abbrName === "ERR")
    describe(".name"):
      it("must be 'error'"):
        withLocale(Locale.US.nn):
          assert(Severity.ErrorSeverity.name === "error")
    describe(".compare(MessageSeverity)"):
      it("must compare as greater than ImportantSeverity"):
        assert(Severity.ErrorSeverity.compare(Severity.ImportantSeverity) > 0)
      it("must compare as equal to itself"):
        assert(Severity.ErrorSeverity.compare(Severity.ErrorSeverity) === 0)
      it("must compare as less than FatalSeverity"):
        assert(Severity.ErrorSeverity.compare(Severity.FatalSeverity) < 0)

  // FatalSeverity
  describe("org.facsim.util.log.Severity.FatalSeverity"):
    describe(".abbrName"):
      it("must be 'FTL'"):
        withLocale(Locale.US.nn):
          assert(Severity.FatalSeverity.abbrName === "FTL")
    describe(".name"):
      it("must be 'fatal'"):
        withLocale(Locale.US.nn):
          assert(Severity.FatalSeverity.name === "fatal")
    describe(".compare(MessageSeverity)"):
      it("must compare as greater than ErrorSeverity"):
        assert(Severity.FatalSeverity.compare(Severity.ErrorSeverity) > 0)
      it("must compare as equal to itself"):
        assert(Severity.FatalSeverity.compare(Severity.FatalSeverity) === 0)
