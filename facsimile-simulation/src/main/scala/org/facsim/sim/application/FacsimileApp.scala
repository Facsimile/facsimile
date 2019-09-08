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

import java.util.jar.Attributes.Name
import org.facsim.sim.LibResource
import org.facsim.util.{Manifest, Version}

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
  import FacsimileApp.missingMsg

  /** Manifest for the ultimate instance of this application, retrieved from containing ''JAR'' file.
   *
   *  If this application's type was not loaded from a ''JAR'' file, or if the ''JAR'' file has no manifest, then this
   *  value will be a ''null manifest'' with missing information.
   *
   *  @since 0.2
   */
  protected final val manifest = Manifest(getClass)

  /** Parser for processing command line arguments supplied to this program. */
  private val parser = new CLIParser(appName, appCopyright, appVersionString)

  // Parse the command line, running the program only if successful.
  parser.parse(args).map(runSimulation)

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
    //
    // TEMPORARY NOTE: Scalastyle/Scalariform cannot parse lists that terminate with a comma, so avoid doing that for
    // now.
    List(
      LibResource("application.FacsimileApp.Copyright1", appOrganization, range),
      LibResource("application.FacsimileApp.Copyright2")
    )
  }

  /** Function to run the simulation.
   *
   *  @param config Simulation configuration this run.
   */
  private def runSimulation(config: FacsimileConfig): Unit = {

  }
}

/** Facsimile application companion object. */
private object FacsimileApp {

  /** Provide missing manifest attribute message.
   *
   *  @param name Name of the missing attribute.
   *
   *  @return Properly configured missing manifest attribute message.
   */
  private def missingMsg(name: Name) = LibResource("application.FacsimileApp.MissingAttribute", name.toString)
}