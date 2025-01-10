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
// Scala source file belonging to the org.facsim.sim.application.test package.
//======================================================================================================================
package org.facsim.sim.application.test

import java.io.File
import java.util.Locale
import org.facsim.sim.application.{CLIParser, FacsimileConfig}
import org.facsim.util.LS
import org.facsim.util.log._
import org.facsim.util.test.withLocale
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.io.Source
import org.scalatest.funspec.AnyFunSpec

// Disable test-problematic Scalastyle checkers.

/** Test harness for the [[CLIParser]] class.
 *
 *  @todo Some of these tests are dependent upon the en-US locale. In order to support testing under other locale's, it
 *  will be necessary to expand upon these tests.
 */
final class CLIParserTest
extends AnyFunSpec
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
     * @note This should be chosen so as not to be a valid command line option.
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

    /** Help short option. */
    lazy val helpShortOpt: String = "-h"

    /** Help long option. */
    lazy val helpLongOpt: String = "--help"

    /** Headless short option. */
    lazy val headlessShortOpt = "-H"

    /** Headless long option */
    lazy val headlessLongOpt = "--headless"

    /** Log file short option. */
    lazy val logFileShortOpt: String = "-l"

    /** Log file long option. */
    lazy val logFileLongOpt: String = "--log-file"

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

    /** Report file short option. */
    lazy val reportFileShortOpt: String = "-r"

    /** Report file long option. */
    lazy val reportFileLongOpt: String = "--report-file"

    /** Version header information. */
    lazy val versionHeader = s"$appName$LS${appCopyright.mkString(LS)}${LS}Version: $appVersion"

    /** Version short option. */
    lazy val versionShortOpt: String = "-V"

    /** Version long option. */
    lazy val versionLongOpt: String = "--version"

    /** Test a particular file option.
     *
     * @param name Type of file.
     *
     * @param short Short file option.
     *
     * @param long Long file option.
     *
     * @param expected Expected resulting configuration.
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
          assert(parser !== null)
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
            assert(c.runModel === true)
            assert(c.showUsage === false)
            assert(c.showVersion === false)
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

      // Verify that it accepts headless mode option.
      it("must accept headless mode options") {
        new TestData {
          val expectedCfg = FacsimileConfig(useGUI = false)
          assert(parser.parse(Seq(headlessShortOpt)) === Some(expectedCfg))
          assert(parser.parse(Seq(headlessLongOpt)) === Some(expectedCfg))
        }
      }

      // Verify that it accepts the help option correctly.
      it("must accept help options and exit immediately") {
        new TestData {
          val expectedCfg = FacsimileConfig(runModel = false, showUsage = true)
          assert(parser.parse(Seq(helpShortOpt)) === Some(expectedCfg))
          assert(parser.parse(Seq(helpLongOpt)) === Some(expectedCfg))
        }
      }

      // Verify that it accepts log file options.
      new TestData {
        testOption("log", logFileShortOpt, logFileLongOpt, FacsimileConfig(logFile = Some(file)))
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

      // Verify that it accepts report file options.
      new TestData {
        testOption("report", reportFileShortOpt, reportFileLongOpt, FacsimileConfig(reportFile = Some(file)))
      }

      // Verify that it accepts the version option correctly.
      it("must accept version options and exit immediately") {
        new TestData {
          val expectedCfg = FacsimileConfig(runModel = false, showVersion = true)
          assert(parser.parse(Seq(versionShortOpt)) === Some(expectedCfg))
          assert(parser.parse(Seq(versionLongOpt)) === Some(expectedCfg))
        }
      }
    }

    // Test the usage function.
    describe(".usage") {

      // Verify the returned result.
      it("must contain the correct usage information") {

        // All we can do is to compare the reported value with a cached version of the output.
        //
        // NOTE: Any changes to the parser definition may result in this test failing. After manually inspecting the
        // differences, if the changes are valid, update the target file with the cached usage.
        new TestData {
          withLocale(Locale.US) {
            val cachedFile = getClass.getResource("/CachedUsage.txt").getFile
            val cachedSource = Source.fromFile(cachedFile)
            try {
              val cachedUsage = s"$versionHeader$LS${cachedSource.mkString}"
              assert(parser.usage === cachedUsage)
            }
            finally {
              cachedSource.close()
            }
          }
        }
      }
    }

    // Test the version function.
    describe(".version") {
      it("must contain the correct version information") {
        new TestData {
          withLocale(Locale.US) {
            assert(parser.version === versionHeader)
          }
        }
      }
    }
  }
}

// Re-enable test-problematic Scalastyle checkers.

