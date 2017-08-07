//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2017, Michael J Allen.
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
import java.util.jar.Attributes.Name
import java.util.jar.{Manifest => JManifest}

/** Provide ''manifest'' information for a library or application.
 *
 *  The manifest attributes are stored within a file named `MANIFEST.MF` located in the `/META-INF` folder of the
 *  associated ''Java'' archive file (or ''jar file''). If there is no associated jar file, then no manifest information
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

  // Sanity checks. Alas, we cannot currently use macros in the compilation unit that they're defined in. :-(
  assert(manifest ne null) //scalastyle:ignore null

  /** Entries defined in the manifest. */
  private val entries = manifest.getMainAttributes

  /** Retrieve specified manifest attribute as a string.
   *
   *  @param name Name of attribute to be retrieved.
   *
   *  @return Attribute's value as a string wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @throws NullPointerException if `name` is `null`.
   *
   *  @since 0.0
   */
  def attribute(name: Name) = {

    // Sanity checks. Name cannot be null.
    requireNonNullFn(name, "name")

    // Retrieve the specified attribute's value, and wrap it in an option (which maps null values to Option).
    Option(entries.getValue(name))
  }

  /** Retrieve specified manifest attribute as a date/time.
   *
   *  Retrieve a date/time attribute of the form:
   *
   *  `{name}: ''timeformat''`
   *
   *  where `{name}` is the name of the attribute and `''timeformat''` is a string that can be successfully parsed by
   *  [[ZonedDateTime.parse(CharSequence)]].
   *
   *  @note If this function is used to retrieve a date string attribute value that cannot be parsed as a
   *  [[ZonedDateTime]], then an exception will result.
   *
   *  @param name Name of the date attribute to be retrieved.
   *
   *  @return Date & time for this attribute wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @throws NullPointerException if `name` is `null`.
   *
   *  @throws java.time.format.DateTimeParseException if the associated attribute could not be parsed as a date/time.
   *
   *  @since 0.0
   */
  def dateAttribute(name: Name) = attribute(name).map {t =>

    // If the attribute value can be parsed, then return the result. If it can't be parsed, a DateTimeException will be
    // thrown
    ZonedDateTime.parse(t)
  }

  /** Retrieve specified manifest attribute as a version.
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
   *  @return Version represented by this attribute wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @throws NullPointerException if `name` is `null`.
   *
   *  @throws IllegalArgumentException if the associated attribute could not be parsed as a version.
   *
   *  @since 0.0
   */
  def versionAttribute(name: Name) = attribute(name).map(Version(_))

  /** Retrieve the inception timestamp of this manifest.
   *
   *  This is a custom field that will likely be unavailable for many packages. To include it in your ''jar'' files,
   *  ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   *  `Inception-Timestamp: ''timeformat''`
   *
   *  where ''timeformat'' is a string that can be successfully parsed by [[ZonedDateTime.parse(CharSequence)]].
   *
   *  @return Project inception date & time wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @throws java.time.format.DateTimeParseException if the associated attribute could not be parsed as a date/time.
   *
   *  @since 0.0
   */
  def inceptionTimestamp = dateAttribute(Manifest.InceptionTimestamp)

  /** Retrieve the build timestamp of this manifest.
   *
   *  This is a custom field that will likely be unavailable for many packages. To include it in your ''jar'' files,
   *  ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   *  `Build-Timestamp: ''timeformat''`
   *
   *  where ''timeformat'' is a string that can be successfully parsed by [[ZonedDateTime.parse(CharSequence)]].
   *
   *  @return Project build date & time wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @throws java.time.format.DateTimeParseException if the associated attribute could not be parsed as a date/time.
   *
   *  @since 0.0
   */
  def buildTimestamp = dateAttribute(Manifest.BuildTimestamp)

  /** Title of this application or library.
   *
   *  @return Implementation title wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @since 0.0
   */
  def title = attribute(Name.IMPLEMENTATION_TITLE)

  /** Vendor publishing this application or library.
   *
   *  This may be an individual or an organization, depending upon circumstances.
   *
   *  @return Implementation vendor name wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @since 0.0
   */
  def vendor = attribute(Name.IMPLEMENTATION_VENDOR)

  /** Version of this release of this application or library.
   *
   *  @return Implementation version wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @throws IllegalArgumentException if the associated attribute could not be parsed as a version.
   *
   *  @since 0.0
   */
  def version = versionAttribute(Name.IMPLEMENTATION_VERSION)

  /** Title of the specification to which this application or library conforms.
   *
   *  @return Specification title wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @since 0.0
   */
  def specTitle = attribute(Name.SPECIFICATION_TITLE)

  /** Vendor that produced the specification to which this application or library conforms.
   *
   *  This may be an individual or an organization, depending upon circumstances.
   *
   *  @return Specification vendor name wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @since 0.0
   */
  def specVendor = attribute(Name.SPECIFICATION_VENDOR)

  /** Version of the specification to which this release of the application or library conforms.
   *
   *  @return Spectification version wrapped in [[Some]], or [[None]] if undefined.
   *
   *  @throws IllegalArgumentException if the associated attribute could not be parsed as a version.
   *
   *  @since 0.0
   */
  def specVersion = versionAttribute(Name.SPECIFICATION_VERSION)
}

/** Manifest companion object.
 *
 *  Object defining factory methods for obtaining [[Manifest]] instances.
 */
object Manifest {

  /** Name of the manifest inception timestamp attribute. */
  private[util] val InceptionTimestamp = new Name("Inception-Timestamp")

  /** Name of the manifest build timestamp attribute. */
  private[util] val BuildTimestamp = new Name("Build-Timestamp")

  /** Null manifest.
   *
   *  This manifest is employed for classes that were not loqded from a ''JAR'' file, or which were loaded from ''JAR''
   *  file that has no manifest information.
   */
  private[util] val NullManifest = new Manifest(new JManifest())

  /** Element type manifest factory method.
   *
   *  Create and return a new [[Manifest]] instance by retrieving the ''Java'' manifest associated with the specified
   *  `elementType`. If no manifest is associated with the element type, then an empty (or null) manifest will be
   *  returned.
   *
   *  @param elementType Element type instance for which manifest information will be obtained.
   *
   *  @return Resource [[Manifest]] information associated with `elementType`, or a null manifest (having no attributes
   *  defined) if the class has no ''JAR'' file, or if it's ''JAR'' file has no manifest.
   *
   *  @since 0.0
   */
  def apply(elementType: Class[_]) = {

    // Sanity checks. Name cannot be null.
    requireNonNullFn(elementType, "elementType")

    // Retrieve the JAR file associated with the specified type.
    val manifest = jarFile(elementType).flatMap(jar => Option(jar.getManifest))

    // If a manifest was obtained, wrap it in a Manifest and return. Otherwise, report the NullManifest.
    manifest.map(new Manifest(_)).getOrElse(NullManifest)
  }
}