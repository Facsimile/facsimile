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
// Scala source file belonging to the org.facsim.sim.model package.
//======================================================================================================================
package org.facsim.sim.model

import com.typesafe.config.Config
import scala.collection.JavaConverters._
import squants.time.Time

/** Simulation model parameters, obtained from configuration files.
 *
 *  @constructor Create a new simulation model parameters instance.
 *
 *  @param config ''Lightbend Config'' configuration instance. It includes all configuration elements from constituent
 *  libraries and applications, as well as custom configuration identified on the command line.
 *
 *  @since 0.2
 */
final class Parameters private[sim](config: Config) {

  /** Retrieve a parameter value as a string.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned value will be valid; for example, the returned value may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned value.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a string value, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a string.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def stringParam(path: String): String = config.getString(path)

  /** Retrieve a parameter value as a list of strings.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned values are valid; for example, the returned values may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned values.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a list of strings, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a list of strings.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def stringListParam(path: String): List[String] = config.getStringList(path).asScala.toList

  /** Retrieve a parameter value as an integer.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned value will be valid; for example, the returned value may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned value.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify an integer value, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as an integer.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def intParam(path: String): Int = config.getInt(path)

  /** Retrieve a parameter value as a list of integers.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned values are valid; for example, the returned values may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned values.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a list of integers, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a list of integers.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def intListParam(path: String): List[Int] = config.getIntList(path).asScala.map(_.intValue()).toList

  /** Retrieve a parameter value as a Boolean.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a Boolean value, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a Boolean.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def boolParam(path: String): Boolean = config.getBoolean(path)

  /** Retrieve a parameter value as a list of Booleans.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a list of Booleans, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a list of Booleans.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def boolListParam(path: String): List[Boolean] = config.getBooleanList(path).asScala.map(_.booleanValue()).toList

  /** Retrieve a parameter value as a list of double precision values.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned value will be valid; for example, the returned value may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned value.
   *
   *  @note Consider using an alternative type, such as a `Time`, etc., which may reflect the usage of the parameter
   *  better. Plain double precision values should only be used for unitless types, such as scale factors, ratios, etc.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a double value, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a double precision value.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def doubleParam(path: String): Double = config.getDouble(path)

  /** Retrieve a parameter value as a list of double precision values.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned values are valid; for example, the returned values may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned values.
   *
   *  @note Consider using an alternative type, such as a `Time`, etc., which may reflect the usage of the parameter
   *  better. Plain double precision values should only be used for unitless types, such as scale factors, ratios, etc.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a list of doubles, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a list of double precision values.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   */
  def doubleListParam(path: String): List[Double] = config.getDoubleList(path).asScala.map(_.doubleValue()).toList

  /** Retrieve a parameter value as a list of time values.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned value will be valid; for example, the returned value may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned value.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a time value, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a time value.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   *
   *  @throws x.y.z if the value cannot be parsed as a time value.
   */
  def timeParam(path: String): Time = {

    // Get the value as a string first.
    val strVal = stringParam(path)

    // Now parse the value as a time.
    Time.parseString(strVal).get
  }

  /** Retrieve a parameter value as a list of time values.
   *
   *  In normal usage, this function must complete without an exception; exceptions will only be thrown if the parameter
   *  configuration is invalid.
   *
   *  There is no guarantee that the returned values are valid; for example, the returned values may be outside of a
   *  valid range, etc. It is the responsibility of the caller to ensure the validity of the returned values.
   *
   *  @param path Configuration path of the associated parameter. If the path does not identify a parameter value, or if
   *  the path does not identify a list of time values, then an exception will be thrown.
   *
   *  @return Value of the associated parameter as a list of time values.
   *
   *  @throws com.typesafe.config.ConfigException if an exception occurs while retrieving the parameter value.
   *
   *  @throws x.y.z if a value cannot be parsed as a time value.
   */
  def timeListParam(path: String): List[Time] = {

    // Get the list of values as strings first.
    val strVals = stringListParam(path)

    // Now parse the value as a time.
    strVals.map(s => Time.parseString(s).get)
  }
}