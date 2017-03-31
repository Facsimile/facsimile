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
// Scala source file belonging to the org.facsim.measure package.
//======================================================================================================================
package org.facsim.measure

/** ''[[http://en.wikipedia.org/wiki/Temperature Temperature]]'' physical quantity type.
  *
  * All temperature values are stored internally in ''[[http://en.wikipedia.org/wiki/Kelvin degrees Kelvin]]'', which is
  * the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard unit of measure.
  *
  * @see [[http://en.wikipedia.org/wiki/Temperature Temperature]] on ''Wikipedia''.
  *
  * @see [[http://en.wikipedia.org/wiki/Kelvin Degrees Kelvin]] on ''Wikipedia''.
  *
  * @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on ''Wikipedia''.
  *
  * @since 0.0
  */
object Temperature
extends NonNegative {

  /** @inheritdoc
    */
  override type Measure = TemperatureMeasure

  /** @inheritdoc
    */
  override type Units = TemperatureUnits

  /** @inheritdoc
    */
  override val name = "temperature"

  /** Units for temperatures measured in ''[[http://en.wikipedia.org/wiki/Celsius degrees Celsius]]''.
    *
    * @see [[http://en.wikipedia.org/wiki/Celsius Degrees Celsius]] on ''Wikipedia''.
    *
    * @since 0.0
    */
  val Celsius = new Units(new OffsetConverter(-273.15), LibResource("Temperature.Celsius.sym"))

  /** Units for temperatures measured in ''[[http://en.wikipedia.org/wiki/Fahrenheit degrees Fahrenheit]]''.
    *
    * @see [[http://en.wikipedia.org/wiki/Fahrenheit Degrees Fahrenheit]] on ''Wikipedia''.
    *
    * @since 0.0
    */
  val Fahrenheit = new Units(new LinearConverter(5.0 / 9.0, -(32.0 * 5.0 / 9.0) - 273.15),
  LibResource("Temperature.Fahrenheit.sym"))

  /** Units for temperatures measured in ''[[http://en.wikipedia.org/wiki/Kelvin degrees Kelvin]]''.
    *
    * @note ''Degrees Kelvin'' are the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard units for temperature
    * measurement, and the units that are used to store such measurements internally in ''Facsimile''.
    *
    * In ''Facsimile'', a ''degree Kelvin'' is defined in accordance with ''SI'' standards.
    *
    * @see [[http://en.wikipedia.org/wiki/Kelvin Degrees Kelvin]] on ''Wikipedia''.
    *
    * @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on ''Wikipedia''.
    *
    * @since 0.0
    */
  val Kelvin = new Units(SIConverter, LibResource("Temperature.Kelvin.sym"))

  /** Physical quantity family for temperature measurements.
    */
  protected[measure] val family = Family(temperatureExponent = 1)

  /** @inheritdoc
    */
  override val siUnits = Kelvin

  // Register this family.
  Family.register(family, Temperature)

  /** Temperature measurement factory function.
    *
    * @param measure Measurement, in degrees Kelvin, to be converted into a new measure.
    *
    * @return `measure` in the form of a Temperature measurement.
    */
  private[measure] override def apply(measure: Double) =  new Measure(measure)

  /** ''[[http://en.wikipedia.org/wiki/Temperature Temperature]]'' measurement class.
    *
    * Instances of this class represent ''temperature'' measurements.
    *
    * @constructor Create new ''[[http://en.wikipedia.org/wiki/Temperature temperature]]'' measurement value.
    *
    * @param measure ''Temperature'' measurement expressed in ''[[org.facsim.measure.Temperature.Kelvin]]''. This value
    * must be finite and greater than or equal to zero.
    *
    * @throws IllegalArgumentException if `measure` is not finite or is negative.
    *
    * @since 0.0
    */
  final class TemperatureMeasure private[measure](measure: Double)
  extends NonNegativeMeasure(measure)

  /** ''[[http://en.wikipedia.org/wiki/Temperature Temperature]]'' unit of measurement family class.
    *
    * Instances of this class represent units for expressing ''temperature'' measurements.
    *
    * @constructor Create new ''[[http://en.wikipedia.org/wiki/Temperature temperature]]'' unit of measurement.
    *
    * @param converter Rules to be applied to convert a quantity measured in these units to and from the standard
    * ''temperature [[http://en.wikipedia.org/wiki/SI SI]]'' units, ''degrees Kelvin''.
    *
    * @param symbol Symbol to be used when outputting measurement values expressed in these units.
    *
    * @since 0.0
    */
  final class TemperatureUnits private[measure](converter: Converter, symbol: String)
  extends NonNegativeUnits(converter, symbol)
}