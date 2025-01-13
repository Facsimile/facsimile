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
// Scala source file belonging to the org.facsim.sim.application package.
//======================================================================================================================
package org.facsim.sim.application

import java.io.File
import org.facsim.sim.LibResource
import org.facsim.util.log.Severity
import org.facsim.util.{LS, SQ}
import scopt.OptionParser

/** Define the supported command line syntax and parse command line arguments.
 *
 *  Command line arguments are parsed even if the application is being run with an animation. Indeed, the command line
 *  must be parsed to determine whether an animation is to be utilized.
 *
 *  @constructor Create a new _command line interface_ (_CLI_) parser.
 *
 *  @param appName Name of the application.
 *
 *  @param appCopyright Application's copyright message, as a list of strings.
 *
 *  @param appVersionString Application's version string.
 */
private[application] final class CLIParser(appName: String, appCopyright: Seq[String], appVersionString: String):

  /** Command line parser for this application. */
  private final val parser = new OptionParser[FacsimileConfig](appName):

    // Version header information.
    val versionHeader = LibResource("application.CLIParser.VersionHeader", appVersionString)

    // Output header lines.
    head(s"$appName$LS${appCopyright.mkString(LS)}$LS$versionHeader")

    // Add a note explaining what this command does.
    note(LibResource("application.CLIParser.Note") + LS)

    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    // +++ The following define options, which must be sorted by their long name. +++
    // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    // Option defining the HOCON configuration file for the simulation.
    //
    // This option is not necessary to run the simulation. Only a single configuration file may be specified.
    //
    // Note: The file argument should not be validated until we attempt to open it.
    opt[File]('c', "config-file")
    .valueName("<file>")
    .text(LibResource("application.CLIParser.ConfigFileText"))
    .optional()
    .maxOccurs(1)
    .action: (f, c) =>

      // Update the configuration.
      c.copy(configFile = Some(f))

    // Option to run the simulation in "headless" mode, without a GUI interface.
    //
    // If this option is provided, then the simulation will not produce a graphical user interface (GUI) for the
    // simulation run, and will not illustrate what is happening inside the simulation using a 3D animation. However,
    // this will speed-up the simulation run significantly, permitting it to run to completion far faster. As a
    // consequence, this option is highly recommended when performing experiments. For similar reasons, it is highly
    // undesirable to use this option when debugging a model.
    opt[Unit]('H', "headless")
    .text(LibResource("application.CLIParser.HeadlessText"))
    .optional()
    .maxOccurs(1)
    .action: (_, c) =>
      c.copy(useGUI = false)

    // Option to provide command line help about the application and immediately exit.
    //
    // Note: This is defined as an option, instead of using the built-in Scopt "help" definition, in order to better
    // handle application termination during testing. (The "help" definition will terminate the application, making
    // testing challenging.)
    opt[Unit]('h', "help")
    .text(LibResource("application.CLIParser.HelpText"))
    .optional()
    .maxOccurs(1)
    .action: (_, c) =>
      c.copy(runModel = false, showUsage = true)

    // Option defining the output file for writing simulation log messages.
    //
    // This option is not necessary to run the simulation. Only a single log file may be specified.
    //
    // Note: A warning will be issued if the simulation produces no output of any kind. Unless the simulation is doing
    // something unusual, there's a danger that the simulation will just consume CPU for no purpose.
    //
    // Note: The file argument should not be validated until we attempt to open it.
    opt[File]('l', "log-file")
    .valueName(CLIParser.FileValueName)
    .text(LibResource("application.CLIParser.LogFileText"))
    .optional()
    .maxOccurs(1)
    .action: (f, c) =>

      // Update the configuration.
      c.copy(logFile = Some(f))

    // Option to specify the log level, or verbosity, for use when writing log messages to the log-file, and to the
    // standard output, if no animation is present.
    opt[String]('v', "log-level")
    .valueName(LibResource("application.CLIParser.LogLevelValueName"))
    .text(LibResource("application.CLIParser.LogLevelText", Severity.severityList.mkString(SQ, ", ", SQ),
    FacsimileConfig().logLevel.name))
    .optional()
    .maxOccurs(1)
    .validate: sl =>

      // Validate that the log level is valid. This is case sensitive.
      Severity.withName(sl) match
        case Some(_) => success
        case None => failure(LibResource("application.CLIParser.LogLevelFailure", sl))
    .action: (sl, c) =>
      c.copy(logLevel = Severity.withName(sl).get)

    // Option defining the report output file for this simulation run.
    //
    // This option is not necessary to run the simulation. Only a single report file may be specified.
    //
    // Note: A warning will be issued if the simulation produces no output of any kind. Unless the simulation is doing
    // something unusual, there's a danger that the simulation will just consume CPU for no purpose.
    //
    // Note: The file argument should not be validated until we attempt to open it.
    opt[File]('r', "report-file")
    .valueName(CLIParser.FileValueName)
    .text(LibResource("application.CLIParser.ReportFileText"))
    .optional()
    .maxOccurs(1)
    .action: (f, c) =>

      // Update the configuration.
      c.copy(reportFile = Some(f))

    // Option to provide application version information and immediately exit.
    //
    // Note: This is defined as an option, instead of using the built-in Scopt "version" definition, in order to better
    // handle application termination during testing. (The "version" definition will terminate the application, making
    // testing challenging.)
    opt[Unit]('V', "version")
    .text(LibResource("application.CLIParser.VersionText"))
    .optional()
    .maxOccurs(1)
    .action: (_, c) =>
      c.copy(runModel = false, showVersion = true)

  /** Parse the command line arguments provided.
   *
   *  @param args Command line arguments provided.
   *
   *  @return Resulting application configuration wrapped in [[Some]] if successfully parsed, or [[None]] if the command
   *  line could not be parsed.
   */
  private[application] def parse(args: Seq[String]): Option[FacsimileConfig] = parser.parse(args, FacsimileConfig())

  /** Report application usage information.
   *
   *  Supports the application's "--help" option.
   *
   *  @return String containing the application's usage information.
   */
  private[application] def usage: String = parser.usage

  /** Report application version information.
   *
   *  Supports the application's "--version" option.
   *
   *  @return String containing the application's usage information.
   */
  private[application] def version: String = parser.header

/** Command line interpreter parser companion object. */
private object CLIParser:

  /** Value description to output for a command line file parameter or option. */
  private val FileValueName = LibResource("application.CLIParser.FileValueName")
