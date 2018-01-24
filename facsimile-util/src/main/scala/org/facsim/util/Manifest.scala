//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2018, Michael J Allen.
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
// Scala source file belonging to the org.facsim.util package.
//======================================================================================================================
package org.facsim.util

import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.util.jar.Attributes.Name
import java.util.jar.{Manifest => JManifest}
import scala.util.{Failure, Success, Try}

/** Provide ''manifest'' information for a library or application.
 *
 *  The manifest attributes are stored within a file named `MANIFEST.MF` located in the `/META-INF` folder of the
 *  associated ''Java archive'' file (or ''jar'' file). If there is no associated jar file, then no manifest information
 *  will be available.
 *
 *  @note ''Facsimile'' manifests, including the manifests of associated programs or simulation models, are expected to
 *  have a number of custom attributes that will not be present in all ''jar'' files.
 *
 *  @constructor Create a new instance from a ''Java'' manifest instance.
 *
 *  @param manifest Manifest from which attributes will be extracted.
 *
 *  @since 0.0
 */
final class Manifest private(manifest: JManifest) {

  // Sanity checks. We're in charge of this, so we should never pass a null manifest reference.
  assert(manifest ne null, "Manifest reference was null") //scalastyle:ignore null

  /** Entries defined in the manifest. */
  private val entries = manifest.getMainAttributes
  assert(entries ne null, "Manifest has no main attributes") //scalastyle:ignore null

  /** Try to retrieve specified manifest attribute as a string.
   *
   *  @param name Name of attribute to be retrieved.
   *
   *  @return Attribute's value as a string wrapped in a [[scala.util.Success]] if it is defined; or a
   *  [scala.util.[Failure]] otherwise. The only possible failure is a [[NoSuchAttributeException]], indicating that
   *  there is no attribute with the indicated `name`.
   *
   *  @throws scala.NullPointerException if `name` is `null`.
   *
   *  @since 0.0
   */
  def attribute(name: Name): Try[String] = {

    // Sanity checks. Name cannot be null.
    requireNonNullFn(name, "name")

    // Retrieve the specified attribute's value. If it is `null`, return the indicated failure. Otherwise wrap the
    // attribute value as a success.
    val value = entries.getValue(name)
    if(value eq null) Failure(NoSuchAttributeException(name)) //scalastyle:ignore null
    else util.Success(value)
  }

  /** Try to retrieve specified manifest attribute as a date/time.
   *
   *  Retrieve a date/time attribute of the following form from the manifest:
   *
   *  `{name}: ''timeformat''`
   *
   *  where `{name}` is the name of the attribute and `''timeformat''` is a string that can be successfully parsed by
   *  [[java.time.ZonedDateTime.parse(CharSequence)]].
   *
   *  @note If this function is used to retrieve a date string attribute value that cannot be parsed as a
   *  [[java.time.ZonedDateTime]], then a [[scala.util.Failure]] will result.
   *
   *  @param name Name of the date attribute to be retrieved.
   *
   *  @return Date & time for this attribute wrapped in a [[scala.util.Success]], or a [[scala.util.Failure]]
   *  containing the reason that the date & time could not be retrieved. Possible failures are
   *  [[NoSuchAttributeException]] and [[java.time.format.DateTimeParseException]].
   *
   *  @throws scala.NullPointerException if `name` is `null`.
   *
   *  @since 0.0
   */
  def dateAttribute(name: Name): Try[ZonedDateTime] = attribute(name).flatMap {dt =>

    // If the attribute value can be parsed, then return the result.
    try {
      Success(ZonedDateTime.parse(dt))
    }

    // Otherwise, if this is the parse exception, report that as a failure. Any other exceptions thrown above will be
    // passed on and not returned.
    catch {
      case pe: DateTimeParseException => Failure(pe)
    }
  }

  /** Try to retrieve specified manifest attribute as a version.
   *
   *  Retrieve a version attribute of the form:
   *
   *  `{name}: ''version''`
   *
   *  where `{name}` is the name of the attribute and `''version''` is a string that can be successfully parsed by
   *  [[Version.apply(String)]].
   *
   *  @param name Name of the version attribute to be retrieved.
   *
   *  @return Version represented by this attribute wrapped in [[scala.util.Success]], or [[scala.util.Failure]]
   *  containing the reason that the version could not be retrieved. Possible failures are [[NoSuchAttributeException]]
   *  and [[VersionParseException]].
   *
   *  @throws scala.NullPointerException if `name` is `null`.
   *
   *  @since 0.0
   */
  def versionAttribute(name: Name): Try[Version] = attribute(name).flatMap(Version(_))

  /** Try to retrieve the inception timestamp of this manifest.
   *
   *  This is a custom field that will likely be unavailable for many packages. To include it in your ''jar'' files,
   *  ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   *  `Inception-Timestamp: ''timeformat''`
   *
   *  where ''timeformat'' is a string that can be successfully parsed by
   *  [[java.time.ZonedDateTime.parse(CharSequence)]].
   *
   *  @return Project inception date & time wrapped in a [[scala.util.Success]], or a [[scala.util.Failure]] containing
   *  the reason that the inception date & time could not be retrieved. Possible failures are
   *  [[NoSuchAttributeException]] and [[java.time.format.DateTimeParseException]].
   *
   *  @since 0.0
   */
  def inceptionTimestamp: Try[ZonedDateTime] = dateAttribute(Manifest.InceptionTimestamp)

  /** Try to retrieve the build timestamp of this manifest.
   *
   *  This is a custom field that will likely be unavailable for many packages. To include it in your ''jar'' files,
   *  ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   *  `Build-Timestamp: ''timeformat''`
   *
   *  where ''timeformat'' is a string that can be successfully parsed by
   *  [[java.time.ZonedDateTime.parse(CharSequence)]].
   *
   *  @return Project build date & time wrapped in a [[scala.util.Success]], or a [[scala.util.Failure]] containing the
   *  reason that the build date & time could not be retrieved. Possible failures are [[NoSuchAttributeException]] and
   *  [[java.time.format.DateTimeParseException]].
   *
   *  @since 0.0
   */
  def buildTimestamp: Try[ZonedDateTime] = dateAttribute(Manifest.BuildTimestamp)

  /** Try to retrieve the title of this application or library.
   *
   *  @return Implementation title wrapped in a [[scala.util.Success]], or a [[scala.util.Failure]] containing the
   *  reason that the implementation title could not be retrieved. The only possible failure is
   *  [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  def title: Try[String] = attribute(Name.IMPLEMENTATION_TITLE)

  /** Try to retrieve name of vendor publishing this application or library.
   *
   *  If defined, this may be an individual or an organization, depending upon circumstances.
   *
   *  @return Implementation vendor name wrapped in a [[scala.util.Success]], or  a [[scala.util.Failure]] containing
   *  the reason that the vendor name could not be retrieved. The only possible failure is [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  def vendor: Try[String] = attribute(Name.IMPLEMENTATION_VENDOR)

  /** Try to retrieve the implementation version of this release of this application or library.
   *
   *  @return Implementation version wrapped in [[scala.util.Success]], or [[scala.util.Failure]] containing the reason
   *  that the version could not be retrieved. Possible failures are [[NoSuchAttributeException]] and
   *  [[VersionParseException]].
   *
   *  @since 0.0
   */
  def version: Try[Version] = versionAttribute(Name.IMPLEMENTATION_VERSION)

  /** Try to retrieve the specification title of this application or library.
   *
   *  @return Specification title wrapped in a [[scala.util.Success]], or a [[scala.util.Failure]] containing the reason
   *  that the specification title could not be retrieved. The only possible failure is [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  def specTitle: Try[String] = attribute(Name.SPECIFICATION_TITLE)

  /** Try to retrieve name of vendor specifying this application or library.
   *
   *  If defined, this may be an individual or an organization, depending upon circumstances.
   *
   *  @return Specification vendor name wrapped in a [[scala.util.Success]], or  a [[scala.util.Failure]] containing the
   *  reason that the vendor name could not be retrieved. The only possible failure is [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  def specVendor: Try[String] = attribute(Name.SPECIFICATION_VENDOR)

  /** Try to retrieve the specification version of this release of this application or library.
   *
   *  @return Specification version wrapped in [[scala.util.Success]], or [[scala.util.Failure]] containing the reason
   *  that the version could not be retrieved. Possible failures are [[NoSuchAttributeException]] and
   *  [[VersionParseException]].
   *
   *  @since 0.0
   */
  def specVersion: Try[Version] = versionAttribute(Name.SPECIFICATION_VERSION)
}

/** Manifest companion object.
 *
 *  Object defining factory methods for obtaining [[Manifest]] instances.
 *
 *  @since 0.0
 */
object Manifest {

  /** Name of the manifest inception timestamp attribute. */
  private[util] val InceptionTimestamp = new Name("Inception-Timestamp")

  /** Name of the manifest build timestamp attribute. */
  private[util] val BuildTimestamp = new Name("Build-Timestamp")

  /** Null manifest.
   *
   *  This manifest is employed for classes that were not loaded from a ''JAR'' file, or which were loaded from ''JAR''
   *  file that has no manifest information.
   */
  private[util] val NullManifest = new Manifest(new JManifest())

  /** Element type manifest factory method.
   *
   *  Create and return a new [[Manifest]] instance by retrieving the ''Java'' manifest associated with the specified
   *  `elementType`. If no manifest is associated with the element type, then an empty manifest (termed a ''null
   *  manifest'') will be returned.
   *
   *  @param elementType Element type instance for which manifest information will be obtained.
   *
   *  @return Resource [[Manifest]] information associated with `elementType`, or a ''null manifest'' (having no
   *  attributes defined) if the class has no ''JAR'' file, or if its ''JAR'' file has no manifest.
   *
   *  @since 0.0
   */
  def apply(elementType: Class[_]): Manifest = {

    // Sanity checks. Name cannot be null.
    requireNonNullFn(elementType, "elementType")

    // Retrieve the JAR file associated with the specified type.
    val manifest = jarFile(elementType).flatMap(jar => Option(jar.getManifest))

    // If a manifest was obtained, wrap it in a Manifest and return. Otherwise, report the NullManifest.
    manifest.fold(NullManifest)(new Manifest(_))
  }
}