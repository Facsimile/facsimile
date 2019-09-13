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
// Scala source file belonging to the org.facsim.sim.application.test package.
//======================================================================================================================
package org.facsim.sim.application.test

import java.io.File
import org.facsim.sim.application.{CLIParser, FacsimileConfig}
import org.facsim.util.log._
import org.scalatest.FunSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

// Disable test-problematic Scalastyle checkers.
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers

/** Test harness for the [[CLIParser]] class. */
final class CLIParserTest
extends FunSpec
with ScalaCheckPropertyChecks {

  /** Test data. */
  trait TestData {

    /** Test application name. */
    private lazy val appName = "TestApp"

    /** Test copyright sequence. */
    private lazy val appCopyright = Seq(
      "Some copyright string 1",
      "Some copyright string 2"
    )

    /** Test version number. */
    private lazy val appVersion = "1.0.0"

    /** Test parser, using test data. */
    lazy val parser: CLIParser = new CLIParser(appName, appCopyright, appVersion)

    /** Undefined short option.
     *
     *  @note This should be chosen so as not to be a valid command line option.
     */
    lazy val undefinedShortOpt: String = "-X"

    /** Undefined long option. */
    lazy val undefinedLongOpt: String = "--undefined-long-opt"

    /** Invalid argument. */
    lazy val invalidArg: String = "invalid-argument"

    /** A file name.
     *
     *  It doesn't matter what the extension or name is, whether it exists, or what its attributes are.
     */
    lazy val fileName: String = "testfilename.txt"

    /** Expected resulting file definition. */
    lazy val file: File = new File(fileName)

    /** Configuration file short option. */
    lazy val configFileShortOpt: String = "-c"

    /** Configuration file long option. */
    lazy val configFileLongOpt: String = "--config-file"

    /** Headless short option. */
    lazy val headlessShortOpt = "-h"

    /** Headless long option */
    lazy val headlessLongOpt = "--headless"

    /** Log file short option. */
    lazy val logFileShortOpt: String = "-l"

    /** Log file long option. */
    lazy val logFileLongOpt: String = "--log-file"

    /** Report file short option. */
    lazy val reportFileShortOpt: String = "-r"

    /** Report file long option. */
    lazy val reportFileLongOpt: String = "--report-file"

    /** Log level short option. */
    lazy val logLevelShortOpt: String = "-v"

    /** Log level long option. */
    lazy val logLevelLongOpt: String = "--log-level"

    /** Log level options, with matching level. */
    lazy val logLevels: Seq[(String, Severity)] = Seq(
      ("debug", DebugSeverity),
      ("information", InformationSeverity),
      ("warning", WarningSeverity),
      ("important", ImportantSeverity),
      ("error", ErrorSeverity),
      ("fatal", FatalSeverity)
    )

    /** Invalid log level. */
    lazy val invalidLogLevel: String = "invalid"

    /** Test a particular file option.
     */
    protected[test] def testOption(name: String, short: String, long: String, expected: FacsimileConfig): Unit = {
      it(s"must reject invalid $name file specifications") {
        new TestData {
          assert(parser.parse(Seq(short)) === None)
          assert(parser.parse(Seq(long)) === None)
        }
      }
      it(s"must accept valid $name file specifications") {
        new TestData {
          assert(parser.parse(Seq(short, fileName)) === Some(expected))
          assert(parser.parse(Seq(long, fileName)) === Some(expected))
        }
      }
    }
  }

  // Tell the user which object we're testing.
  describe(classOf[CLIParser].getCanonicalName) {

    // Test the constructor.
    describe(".this(String, Seq[String], String)") {

      // It must create a parser without any issues.
      it("must permit construction of a valid parser") {
        new TestData {
          assert(parser !== null) //scalastyle:ignore null
        }
      }
    }

    // Test the parse method.
    describe(".parse(Seq[String])") {

      // Verify that it returns a default, but valid, configuration if supplied no arguments.
      it("must report a valid, default configuration when given no arguments") {
        new TestData {
          val result = parser.parse(Nil)
          assert(result === Some(FacsimileConfig()))

          // Verify the default configuration
          result.foreach {c =>
            assert(c.configFile === None)
            assert(c.logFile === None)
            assert(c.logLevel === WarningSeverity)
            assert(c.reportFile === None)
            assert(c.useGUI === true)
          }
        }
      }

      // Verify that it rejects undefined options.
      it("must reject undefined options") {
        new TestData {
          assert(parser.parse(Seq(undefinedShortOpt)) === None)
          assert(parser.parse(Seq(undefinedLongOpt)) === None)
        }
      }

      // Verify that it rejects invalid arguments.
      it("must reject invalid arguments") {
        new TestData {
          assert(parser.parse(Seq(invalidArg)) === None)
        }
      }

      // Verify that it accepts configuration file options.
      new TestData {
        testOption("config", configFileShortOpt, configFileLongOpt, FacsimileConfig(configFile = Some(file)))
      }

      // Verify that is accepts headless mode option.
      it("must accept headless mode option") {
        new TestData {
          val expectedCfg = FacsimileConfig(useGUI = false)
          assert(parser.parse(Seq(headlessShortOpt)) === Some(expectedCfg))
          assert(parser.parse(Seq(headlessLongOpt)) === Some(expectedCfg))
        }
      }

      // Verify that it accepts log file options.
      new TestData {
        testOption("log", logFileShortOpt, logFileLongOpt, FacsimileConfig(logFile = Some(file)))
      }

      // Verify that it accepts report file options.
      new TestData {
        testOption("report", reportFileShortOpt, reportFileLongOpt, FacsimileConfig(reportFile = Some(file)))
      }

      // Verify that accepts log level options.
      it("must reject invalid log level options") {
        new TestData {
          assert(parser.parse(Seq(logLevelShortOpt)) === None)
          assert(parser.parse(Seq(logLevelLongOpt)) === None)
          assert(parser.parse(Seq(logLevelShortOpt, invalidLogLevel)) === None)
          assert(parser.parse(Seq(logLevelLongOpt, invalidLogLevel)) === None)
        }
      }
      it("must accept valid log level options") {
        new TestData {
          logLevels.foreach {
            case(n, ll) => {
              val expectedCfg = FacsimileConfig(logLevel = ll)
              assert(parser.parse(Seq(logLevelShortOpt, n)) === Some(expectedCfg))
            }
          }
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