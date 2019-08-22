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
// Scala source file belonging to the org.facsim.sim.application package.
//======================================================================================================================
package org.facsim.sim.application

import java.io.File
import java.util.jar.Attributes.Name
import org.facsim.sim.LibResource
import org.facsim.util.{Manifest, NL, Version}
import org.facsim.util.log.Severity
import scopt.OptionParser

/** Base class for a ''Facsimile'' application.
 *
 *  ''Facsimile'' applications commence as command line/non-graphical programs. However, if required, a GUI interface
 *  for the model is created, which also provides a ''3D'' animation of the simulation.
 *
 *  @note ''Facsimile'' applications perform one run of a specific simulation configuration; ''Facsimile'' is not a
 *  simulation model editing environment, but a library for writing simulation models.
 *
 *  @since 0.2
 */
abstract class FacsimileApp
extends App {

  // Helper
  import FacsimileApp._

  /** Manifest for the ultimate instance of this application, retrieved from containing ''JAR'' file.
   *
   *  If this application's type was not loaded from a ''JAR'' file, or if the ''JAR'' file has no manifest, then this
   *  value will be a ''null manifeat'' with missing information.
   *
   *  @since 0.2
   */
  protected final val manifest = Manifest(getClass)

  /** Report the name of this application.
   *
   *  @return Name of this application, obtained from the manifest. If the manifest does not define the application
   *  name, then a string indicating the problem is returned instead.
   *
   *  @since 0.2
   */
  final def appName: String = manifest.title.getOrElse(missingMsg(Name.IMPLEMENTATION_TITLE))

  /** Report the name of the organization that has developed this application.
   *
   *  @return Name of organization that has issued this application, obtained from the manifest. If the manifest does
   *  not define the application name, then a string indicating the problem is returned instead.
   *
   *  @since 0.2
   */
  final def appOrganization: String = manifest.vendor.getOrElse(missingMsg(Name.IMPLEMENTATION_VENDOR))

  /** Report the version of this application.
   *
   *  @return Report the application version, obtained from the manifest, wrapped in `[[scala.Some Some]]`, or
   *  `[[scala.None None]]` if version information is missing from the manifest, or is formatted incorrectly.
   *
   *  @since 0.2
   */
  final def appVersion: Option[Version] = manifest.version.toOption

  /** Report the version of this application.
   *
   *  @return Report the application version, obtained from the manifest, wrapped in `[[scala.Some Some]]`, or
   *  `[[scala.None None]]` if version information is missing from the manifest, or is formatted incorrectly.
   *
   *  @since 0.2
   */
  final def appVersionString: String = appVersion.fold(missingMsg(Name.IMPLEMENTATION_VERSION))(_.toString)

  /** Report a copyright message for this application.
   *
   *  @return Copyright message, using information obtained from the manifest. If any of the required information is
   *  missing form the manifest, then the copyright message will include messages indicating the problem.
   *
   *  @since 0.2
   */
  final def appCopyright: List[String] = {

    // Retrieve the inception year as a string.
    val inception = manifest.inceptionTimestamp.toOption
    .fold(missingMsg(Manifest.InceptionTimestamp))(_.getYear.toString)

    // Retrieve the build year as a string.
    val build = manifest.inceptionTimestamp.toOption.fold(missingMsg(Manifest.BuildTimestamp))(_.getYear.toString)

    // Create the copyright range, if the two dates are different, otherwise just use the inception year.
    val range = if(inception == build) inception else s"$inception-$build"

    // Put it all together in a list of strings.
    List(
      LibResource("application.FacsimileApp.Copyright1", appOrganization, range),
      LibResource("application.FacsimileApp.Copyright2"),
    )
  }

  /** Command line parser for this application. */
  private final val parser = new OptionParser[FacsimileConfig](appName) {

    // Output header lines.
    head(s"$appName$NL${appCopyright.mkString(NL)}${NL}Version: ${appVersionString}")

    // Add a note explaining what this command does.
    note(LibResource("application.FacsimileApp.Note"))

    // Option defining the HOCON configuration file for the simulation.
    //
    // This option is not necessary to run the simulation. Only a single configuration file may be specified.
    opt[File]('c', "config-file")
    .valueName("<file>")
    .text(LibResource("application.FacsimileApp.ConfigFileText"))
    .optional
    .maxOccurs(1)
    .validate {f =>

      // Check that the configuration file exists and that it can be read from.
      //
      // It may well be that when we attempt to open this file later on, it will fail (if the file is changed in any
      // way between the moment this code executes and the time it is opened). However, we should do as much as we can
      // up front in order to assist the user as much as possible.
      if(f.exists && f.canRead) success
      else failure(LibResource("application.FacsimileApp.ConfigFileFailure"))
    }
    .action {(f, c) =>

      // Update the configuration.
      c.copy(configFile = Some(f))
    }

    // Option to run the simulation in "headless" mode, without a GUI interface.
    opt[Unit]('h', "headless")
    .text(LibResource("application.FacsimileApp.HeadlessText"))
    .optional
    .maxOccurs(1)
    .action {(_, c) =>
      c.copy(useGUI = false)
    }

    // Option defining the output file for writing simulation log messages.
    //
    // This option is not necessary to run the simulation. Only a single log file may be specified.
    //
    // Note: A warning will be issued if the simulation produces no output of any kind. Unless the simulation is doing
    // something unusual, there's a danger that the simulation will just consume CPU for no purpose.
    opt[File]('l', "log-file")
    .valueName(FileValueName)
    .text(LibResource("application.FacsimileApp.LogFileText"))
    .optional
    .maxOccurs(1)
    .validate {f =>

      // Check that the report file exists and that it can be written to.
      //
      // It may well be that when we attempt to open this file later on, it will fail (if the file is changed in any
      // way between the moment this code executes and the time it is opened). However, we should do as much as we can
      // up front in order to assist the user as much as possible.
      //
      // TODO: Check if we have permission to create the file if it doesn't exist.
      if(!f.exists || f.canWrite) success
      else failure(LibResource("application.FacsimileApp.LogFileFailure"))
    }
    .action {(f, c) =>

      // Update the configuration.
      c.copy(logFile = Some(f))
    }

    // Option defining the report output file for this simulation run.
    //
    // This option is not necessary to run the simulation. Only a single report file may be specified.
    //
    // Note: A warning will be issued if the simulation produces no output of any kind. Unless the simulation is doing
    // something unusual, there's a danger that the simulation will just consume CPU for no purpose.
    opt[File]('r', "report-file")
    .valueName(FileValueName)
    .text(LibResource("application.FacsimileApp.ReportFileText"))
    .optional
    .maxOccurs(1)
    .validate {f =>

      // Check that the report file exists and that it can be written to.
      //
      // It may well be that when we attempt to open this file later on, it will fail (if the file is changed in any
      // way between the moment this code executes and the time it is opened). However, we should do as much as we can
      // up front in order to assist the user as much as possible.
      //
      // TODO: Check if we have permission to create the file if it doesn't exist.
      if(!f.exists || f.canWrite) success
      else failure(LibResource("application.FacsimileApp.ReportFileFailure"))
    }
    .action {(f, c) =>

      // Update the configuration.
      c.copy(reportFile = Some(f))
    }

    // Option to specify the log level, or verbosity, for use when writing log messages to the log-file, and to the
    // standard output, if no animation is present.
    opt[String]('v', "log-level")
    .valueName(LibResource("application.FacsimileApp.LogLevelValueName"))
    .text(LibResource("application.FacsimileApp.LogLevelText", Severity.severityList.mkString("'", ", ", "'"),
      FacsimileConfig().logLevel.name))
    .optional
    .maxOccurs(1)
    .validate {sl =>

      // Validate that the log level is valid. This is case sensitive.
      Severity.withName(sl) match {
        case Some(_) => success
        case None => failure(LibResource("application.FacsimileApp.LogLevelFailure", sl))
      }
    }
    .action {(sl, c) =>
      c.copy(logLevel = Severity.withName(sl).get)
    }
  }

  // Parse the command line, running the program only if successful.
  parser.parse(args, FacsimileConfig()).map(runSimulation)

  /** Function to run the simulation.
   */
  private def runSimulation(config: FacsimileConfig): Unit = {

  }
}

/** Facsimile application companion object. */
private object FacsimileApp {

  /** Value description to output for a command line file parameter or option. */
  private val FileValueName = LibResource("application.FacsimileApp.FileValueName")

  /** Provide missing manifest attribute message.
   *
   *  @param name Name of the missing attribute.
   *
   *  @return Properly configured missing manifest attribute message.
   */
  private def missingMsg(name: Name) = LibResource("application.FacsimileApp.MissingAttribute", name.toString)
}