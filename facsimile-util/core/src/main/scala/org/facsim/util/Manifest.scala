/*
 * Facsimile -- A Discrete-Event Simulation Library
 * Copyright © 2004-2016, Michael J Allen.
 *
 * This file is part of Facsimile.
 *
 * Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
 * http://www.gnu.org/licenses/lgpl.
 *
 * The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
 * project home page at:
 *
 *   http://facsim.org/
 *
 * Thank you for your interest in the Facsimile project!
 *
 * IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
 * inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
 * your code fails to comply with the standard, then your patches will be rejected. For further information, please
 * visit the coding standards at:
 *
 *   http://facsim.org/Documentation/CodingStandards/
 * =====================================================================================================================
 * Scala source file belonging to the org.facsim.util package.
 */
package org.facsim.util

import java.time.ZonedDateTime
import java.util.jar.{Manifest => JManifest}

/**
 * Provide ''manifest'' information for a library or application.
 *
 * The manifest attributes are stored within a file named `MANIFEST.MF` located in the `/META-INF` folder of the
 * associated ''Java'' archive file (or ''jar file''). If there is no associated jar file, then no manifest information
 * will be available.
 *
 * @note ''Facsimile'' manifests, including the manifests of associated programs or simulation models, are expected to
 * have a number of custom attributes that will not be present in all ''jar'' files.
 *
 * @constructor Create a new instance from a ''Java'' manifest instance.
 *
 * @param manifest [[JManifest]] instance from which manifest attributes will be extracted.
 *
 * @throws NullPointerException if `manifest` is `null`.
 *
 * @since 0.0
 */
final class Manifest private(manifest: JManifest) {

  /*
   * Sanity checks.
   *
   * Since construction is permitted only via companion factory methods, we can simply assert non null.
   */
  assert(manifest ne null) //scalastyle:ignore null

  /**
   * Entries defined in the manifest.
   */
  private val entries = manifest.getMainAttributes

  /**
   * Retrieve specified manifest attribute as a string.
   *
   * @param name Name of attribute to be retrieved.
   *
   * @return Attribute's value as a string, if the attributes is defined.
   *
   * @throws IllegalArgumentException if `name` is not a valid element name.
   *
   * @throws NoSuchElementException if there is no attribute with `name`.
   *
   * @since 0.0
   */
  def getAttribute(name: String) = {

    /*
     * Verify the argument name.
     */
    requireNonNull(name)

    /*
     * Look-up and return the element name.
     */
    getAttr(name)
  }

  /**
   * Retrieve specified manifest attribute as a string.
   *
   * @param name Name of attribute to be retrieved.
   *
   * @return Attribute's value as a string, if the attributes is defined.
   *
   * @throws NoSuchElementException if there is no attribute with `name`.
   *
   * @since 0.0
   */
  private def getAttr(name: String) = {

    /*
     * Retrieve the specified attribute's value.
     *
     * This may throw an IllegalArgumentException if name isn't a valid attribute name.
     */
    val attribute = entries.getValue(name)

    /*
     * If there was no such attribute, then throw a NoSuchElementException, as required.
     */
    if(attribute eq null) { //scalastyle:ignore null
      throw new NoSuchElementException(LibResource("Manifest.NoSuchElement.Attribute", name))
    }

    /*
     * Return the attribute.
     */
    attribute
  }

  /**
   * Retrieve the inception timestamp of this manifest.
   *
   * This is a custom field that will likely be unavailable for many packages. To include it in your ''jar'' files,
   * ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   * `Inception-Timestamp: ''timeformat''`
   *
   * where ''timeformat'' is a string that can be successfully parsed by [[ZonedDateTime.parse(CharSequence)]].
   *
   * @return Date & time that the associated project was started.
   *
   * @throws NoSuchElementException if the manifest has no inception timestamp.
   *
   * @throws java.time.format.DateTimeParseException if the inception timestamp could not be parsed correctly.
   *
   * @since 0.0
   */
  def inceptionTimestamp = ZonedDateTime.parse(getAttr("Inception-Timestamp"))

  /**
   * Retrieve the build timestamp of this manifest.
   *
   * This is a custom field that will likely be unavailable for many packages. To include it in your ''jar'' files,
   * ensure that the META-INF/MANIFEST.MF file contains an entry of the following form:
   *
   * `Build-Timestamp: ''timeformat''`
   *
   * where ''timeformat'' is a string that can be successfully parsed by [[ZonedDateTime.parse(CharSequence)]].
   *
   * @return Date & time that the associated project was built.
   *
   * @throws NoSuchElementException if the manifest has no build timestamp.
   *
   * @throws java.time.format.DateTimeParseException if the build timestamp could not be parsed correctly.
   *
   * @since 0.0
   */
  def buildTimestamp = ZonedDateTime.parse(getAttr("Build-Timestamp"))

  /**
   * Title of this application or library.
   *
   * @return Manifest implementation title.
   *
   * @throws NoSuchElementException if the manifest has no implementation title field.
   *
   * @since 0.0
   */
  def title = getAttr("Implementation-Title")

  /**
   * Vendor publishing this application or library.
   *
   * This may be an individual or an organization, depending upon circumstances.
   *
   * @return Manifest implementation vendor.
   *
   * @throws NoSuchElementException if the manifest has no vendor field.
   *
   * @since 0.0
   */
  def vendor = getAttr("Implementation-Vendor")

  /**
   * Version of this release of this application or library.
   *
   * @return Manifest implementation version.
   *
   * @throws NoSuchElementException if the manifest has no version field.
   *
   * @since 0.0
   */
  def version = new Version(getAttr("Implementation-Version"))

  /**
   * Title of the specification to which this application or library conforms.
   *
   * @return Manifest specification title.
   *
   * @throws NoSuchElementException if the manifest specification title field is undefined.
   *
   * @since 0.0
   */
  def specTitle = getAttr("Specification-Title")

  /**
   * Vendor that produced the specification to which this application or library conforms.
   *
   * This may be an individual or an organization, depending upon circumstances.
   *
   * @return Manifest specification vendor.
   *
   * @throws NoSuchElementException if the manifest has no specification vendor field.
   *
   * @since 0.0
   */
  def specVendor = getAttr("Specification-Vendor")

  /**
   * Version of the specification to which this release of the application or library conforms.
   *
   * @return Manifest specification version.
   *
   * @throws NoSuchElementException if the manifest has no specification version field.
   *
   * @since 0.0
   */
  def specVersion = new Version(getAttr("Specification-Version"))
}

/**
 * Manifest companion object.
 *
 * Object defining factory methods for obtaining [[Manifest]] instances.
 */
object Manifest {

  /**
   * Element type manifest factory method.
   *
   * Create and return a new [[Manifest]] instance by retrieving the manifest associated with the specified specified
   * `element` type.
   *
   * @param elementType Element type instance for which manifest information will be obtained.
   *
   * @return Resource [[Manifest]] information associated with `elementType`.
   *
   * @throws NullPointerException if `elementType` is `null`.
   *
   * @throws NoSuchElementException if `elementType` was not obtained from a ''jar'' file, or if a manifest could not be
   * obtained from its ''jar'' file.
   *
   * @since 0.0
   */
  def apply(elementType: Class[_]) = {

    /*
     * Argument verification.
     */
    requireNonNull(elementType)

    /*
     * Retrieve the resource URL associated with this type.
     *
     * Note: This may result in a NoSuchElement exception being thrown.
     */
    val url = resourceUrl(elementType)

    /*
     * Retrieve the identified JAR file.
     *
     * Note: This may result in a NoSuchElement exception being thrown.
     */
    val jar = jarFile(url)

    /*
     * Now the JAR file's manifest.
     */
    val manifest = jar.getManifest

    /*
     * If there is no manifest, then throw an exception.
     */
    if(manifest eq null) { //scalastyle:ignore null
      throw new NoSuchElementException(LibResource("Manifest.NoSuchElement.Missing", elementType.getName, jar.getName))
    }

    /*
     * Create and return manifest instance for this element.
     */
    new Manifest(manifest)
  }
}