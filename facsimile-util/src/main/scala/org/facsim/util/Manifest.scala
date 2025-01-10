//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
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
// Scala source file belonging to the org.facsim.util package.
//======================================================================================================================
package org.facsim.util

import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.util.jar.Attributes.Name
import java.util.jar.{Manifest => JManifest}
import scala.sys.SystemProperties
import scala.util.{Failure, Success, Try}

/** Trait encapsulating _manifest_ information for a library or application.
 *
 *  Retrieval of manifest information is dependent upon the packaging used.
 *
 *  @since 0.0
 */
sealed trait Manifest:

  /** Try to retrieve specified manifest attribute as a string.
   *
   *  Retrieve an attribute of the following form from the manifest:
   *
   *  `{name}: _value_`
   *
   *  where `{name}` is the name of the attribute and `_value_` is a string storing its value.
   *
   *  @note Attributes requiring specifically formatted strings can be further processed into more refined types, which
   *  both validates the formatting of the value and extracts meaning from it.
   *
   *  @param name Name of attribute to be retrieved.
   *
   *  @return Attribute's value as a string wrapped in a [[Success]] if it is defined; or a [[Failure]] otherwise. The
   *  only possible failure is a [[NoSuchAttributeException]], indicating that there is no attribute with the indicated
   *  `name`.
   *
   *  @since 0.0
   */
  def attribute(name: Name): Try[String]

  /** Try to retrieve specified manifest attribute as a date/time.
   *
   *  Retrieve a date/time attribute of the following form from the manifest:
   *
   *  `{name}: _timeformat_`
   *
   *  where `{name}` is the name of the attribute and `_timeformat_` is a string that can be successfully parsed by
   *  [[ZonedDateTime.parse(CharSequence)]].
   *
   *  @note If this function is used to retrieve a date string attribute value that cannot be parsed as a
   *  [[ZonedDateTime]], then a [[Failure]] will result.
   *
   *  @param name Name of the date attribute to be retrieved.
   *
   *  @return Date & time for this attribute wrapped in a [[Success]], or a [[Failure]] containing the reason that the
   *  date & time could not be retrieved. Possible failures are [[NoSuchAttributeException]] and
   *  [[DateTimeParseException]].
   *
   *  @since 0.0
   */
  final def dateAttribute(name: Name): Try[ZonedDateTime] = attribute(name).flatMap: dt =>

    // If the attribute value can be parsed, then return the result.
    try
      Success(ZonedDateTime.parse(dt).nn)

    // Handle exceptions arising. Any exceptions thrown above that are not handled here will be passed on and not
    // returned.
    catch

      // If this is the parse exception, report that as a failure.
      case pe: DateTimeParseException => Failure(pe)

  /** Try to retrieve specified manifest attribute as a version.
   *
   *  Retrieve a version attribute of the form:
   *
   *  `{name}: _version_`
   *
   *  where `{name}` is the name of the attribute and `_version_` is a string that can be successfully parsed by
   *  [[Version.apply(String)]].
   *
   *  @param name Name of the version attribute to be retrieved.
   *
   *  @return Version represented by this attribute wrapped in [[Success]], or [[Failure]] containing the reason that
   *  the version could not be retrieved. Possible failures are [[NoSuchAttributeException]] and
   *  [[VersionParseException]].
   *
   *  @since 0.0
   */
  final def versionAttribute(name: Name): Try[Version] = attribute(name).flatMap(Version(_))

  /** Try to retrieve the inception timestamp of this manifest.
   *
   *  This is a custom field that will likely be unavailable for many packages. To include it in your _JAR_ files,
   *  ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   *  `Inception-Timestamp: _timeformat_`
   *
   *  where `_timeformat_` is a string that can be successfully parsed by [[ZonedDateTime.parse(CharSequence)]] and
   *  identifies when the project was commenced.
   *
   *  @return Project inception date & time wrapped in a [[Success]], or a [[Failure]] containing the reason that the
   *  inception date & time could not be retrieved. Possible failures are [[NoSuchAttributeException]] and
   *  [[DateTimeParseException]].
   *
   *  @since 0.0
   */
  final def inceptionTimestamp: Try[ZonedDateTime] = dateAttribute(Manifest.InceptionTimestamp)

  /** Try to retrieve the build timestamp of this manifest.
   *
   *  This is a custom field that will likely be unavailable for many packages. To include it in your _JAR_ files,
   *  ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   *  `Build-Timestamp: _timeformat_`
   *
   *  where `_timeformat_` is a string that can be successfully parsed by [[ZonedDateTime.parse(CharSequence)]] and
   *  identifies when the archive was created.
   *
   *  @return Project build date & time wrapped in a [[Success]], or a [[Failure]] containing the reason that the build
   *  date & time could not be retrieved. Possible failures arw [[NoSuchAttributeException]] and
   *  [[DateTimeParseException]].
   *
   *  @since 0.0
   */
  final def buildTimestamp: Try[ZonedDateTime] = dateAttribute(Manifest.BuildTimestamp)

  /** Try to retrieve the title of this application or library.
   *
   *  @return Implementation title wrapped in a [[Success]], or a [[Failure]] containing the reason that the
   *  implementation title could not be retrieved. The only possible failure is [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  final def title: Try[String] = attribute(Name.IMPLEMENTATION_TITLE.nn)

  /** Try to retrieve name of vendor publishing this application or library.
   *
   *  If defined, this may be an individual or an organization, depending upon circumstances.
   *
   *  @return Implementation vendor name wrapped in a [[Success]], or a [[Failure]] containing the reason that the
   *  vendor name could not be retrieved. The only possible failure is [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  final def vendor: Try[String] = attribute(Name.IMPLEMENTATION_VENDOR.nn)

  /** Try to retrieve the implementation version of this release of this application or library.
   *
   *  @return Implementation version wrapped in [[Success]], or [[Failure]] containing the reason that the version could
   *  not be retrieved. Possible failures are [[NoSuchAttributeException]] and [[VersionParseException]].
   *
   *  @since 0.0
   */
  final def version: Try[Version] = versionAttribute(Name.IMPLEMENTATION_VERSION.nn)

  /** Try to retrieve the specification title of this application or library.
   *
   *  @return Specification title wrapped in a [[Success]], or a [[Failure]] containing the reason that the
   *  specification title could not be retrieved. The only possible failure is [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  final def specTitle: Try[String] = attribute(Name.SPECIFICATION_TITLE.nn)

  /** Try to retrieve name of vendor specifying this application or library.
   *
   *  If defined, this may be an individual or an organization, depending upon circumstances.
   *
   *  @return Specification vendor name wrapped in a [[Success]], or a [[Failure]] containing the reason that the vendor
   *  name could not be retrieved. The only possible failure is [[NoSuchAttributeException]].
   *
   *  @since 0.0
   */
  final def specVendor: Try[String] = attribute(Name.SPECIFICATION_VENDOR.nn)

  /** Try to retrieve the specification version of this release of this application or library.
   *
   *  @return Specification version wrapped in [[Success]], or [[Failure]] containing the reason that the version could
   *  not be retrieved. Possible failures are [[NoSuchAttributeException]] and [[VersionParseException]].
   *
   *  @since 0.0
   */
  final def specVersion: Try[Version] = versionAttribute(Name.SPECIFICATION_VERSION.nn)

/** Provide _manifest_ information for a library or application, packaged as a _Java archive_ (_JAR_) file.
 *
 *  The manifest attributes are stored within a file named `MANIFEST.MF` located in the `/META-INF` folder of the
 *  associated _Java archive_ file (or _JAR_ file).
 *
 *  Note that not all _JAR_ files contain a `META-INF/MANIFEST.MF` file; if this file is not present, then no attributes
 *  will be available. Furthermore, even if a manifest is present, not all of the required attributes (including
 *  _standard_ attributes) will necessarily be present.
 *
 *  @note _Facsimile_ manifests, including the manifests of associated programs or simulation models, are expected to
 *  have a number of custom attributes that will not be present in all _JAR_ files.
 *
 *  @constructor Create a new instance from a _Java_ manifest instance.
 *
 *  @param manifest Manifest from which attributes will be extracted.
 *
 *  @since 0.2
 */
final class JARManifest private[util](manifest: JManifest)
extends Manifest:

  /** Entries defined in the manifest.
   */
  private val entries = manifest.getMainAttributes.nn

  // Retrieve named attributes from the supplied manifest.
  override def attribute(name: Name): Try[String] =

    // Retrieve the specified attribute's value. If it is `null`, return the indicated failure. Otherwise wrap the
    // attribute value as a success.
    val x = entries.getValue(name)
    if x == null then Failure(NoSuchAttributeException(name))
    else Success(x)

/** Provide _manifest_ information for the _Java runtime environment_ (_JRE_).
 *
 *  As of _Java_ 9, _JRE_ releases are packaged as a set of standard modules in a _Java image_ (_JIMAGE_) file, which do
 *  not possess manifests of the kind provided by _Java archive_ (_JAR_) files. (Prior to _Java_ 9, releases were
 *  packaged as _JAR_ files with a manifest.
 *
 *  In order to provide the necessary information, it is necessary to simulate the presence of a manifest using system
 *  properties instead.
 *
 *  @since 0.2
 */
object JREManifest
extends Manifest:

  /** System properties to be used to provide the necessary information.
   */
  private val sysProp = new SystemProperties

  /** Map of attribute names to corresponding system properties, wrapped in [[Success]].
   */
  private val nameMap: Map[Name, Try[String]] = Map(
    Manifest.BuildTimestamp -> Success(sysProp("java.version.date")),
    Name.IMPLEMENTATION_TITLE.nn -> Success(sysProp("java.runtime.name")),
    Name.IMPLEMENTATION_VENDOR.nn -> Success(sysProp("java.vendor")),
    Name.IMPLEMENTATION_VERSION.nn -> Success(sysProp("java.version")),
    Name.SPECIFICATION_TITLE.nn -> Success(sysProp("java.specification.name")),
    Name.SPECIFICATION_VENDOR.nn -> Success(sysProp("java.specification.vendor")),
    Name.SPECIFICATION_VERSION.nn -> Success(sysProp("java.specification.version"))
  )

  // Retrieve named attributes by looking at corresponding system properties instead; if there is no corresponding
  // property, then report a failure instead.
  override def attribute(name: Name): Try[String] = nameMap.getOrElse(name, Failure(NoSuchAttributeException(name)))

/** Empty (null) _manifest_ with no attributes defined.
 *
 *  This manifest is employed for classes that were not loaded from a _JAR_ file, or which were loaded from _JAR_
 *  file that has no manifest information, or which come from an unknown packaging scheme.
 *
 *  @since 0.2
 */
object NullManifest
extends Manifest:

  // Simply reject all questions with a failure of NoSuchAttributeException.
  override def attribute(name: Name): Try[String] = Failure(NoSuchAttributeException(name))

/** Manifest companion object.
 *
 *  Object defining factory methods for obtaining [[Manifest]] instances.
 *
 *  @since 0.0
 */
object Manifest:

  /** Manifest inception timestamp attribute.
   *
   *  @since 0.2
   */
  val InceptionTimestamp = new Name("Inception-Timestamp")

  /** Manifest build timestamp attribute.
   *
   *  @since 0.2
   */
  val BuildTimestamp = new Name("Build-Timestamp")

  /** Element type manifest factory method.
   *
   *  Create and return a new [[Manifest]] instance by retrieving the _Java_ manifest associated with the specified
   *  `elementType`. If no manifest is associated with the element type, then an empty manifest (termed a _null
   *  manifest_) will be returned.
   *
   *  @param elementType Element type instance for which manifest information will be obtained.
   *
   *  @return Resource `Manifest` information associated with `elementType`, or a _null manifest_ (having no attributes
   *  defined) if the class has no _JAR_ file, or if its _JAR_ file has no manifest.
   *
   *  @since 0.0
   */
  def apply(elementType: Class[?]): Manifest =

    // Retrieve and return the manifest associated with the specified type.
    manifestOf(elementType)
