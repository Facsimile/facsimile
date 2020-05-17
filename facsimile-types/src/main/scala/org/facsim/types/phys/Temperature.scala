//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
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
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types.phys

import org.facsim.types.LibResource

/** ''[[http://en.wikipedia.org/wiki/Temperature Temperature]]'' physical quantity type.
 *
 *  All temperature values are stored internally in ''[[http://en.wikipedia.org/wiki/Kelvin degrees Kelvin]]'', which is
 *  the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard unit of types.
 *
 *  @see [[http://en.wikipedia.org/wiki/Temperature Temperature]] on ''Wikipedia''.
 *
 *  @see [[http://en.wikipedia.org/wiki/Kelvin Degrees Kelvin]] on ''Wikipedia''.
 *
 *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on ''Wikipedia''.
 *
 *  @since 0.0
 */
object Temperature
extends NonNegative {

  /** @inheritdoc */
  override type Measure = TemperatureMeasure

  /** @inheritdoc */
  override type Units = TemperatureUnits

  /** @inheritdoc */
  override val name: String = LibResource("phys.Temperature.name")

  /** ''Absolute zero'' (0K) in °C.
   *
   *  @note This is the internationally agreed value of ''absolute zero'' (zero in degrees Kelvin) on the Celsius scale.
   */
  private val AbsoluteZeroCelsius = -273.15

  /** Units for temperatures measured in ''[[http://en.wikipedia.org/wiki/Celsius degrees Celsius]]''.
   *
   *  @see [[http://en.wikipedia.org/wiki/Celsius Degrees Celsius]] on ''Wikipedia''.
   *
   *  @since 0.0
   */
  val Celsius: Units = new Units(new OffsetConverter(AbsoluteZeroCelsius), LibResource("phys.Temperature.Celsius.sym"))

  /** Degrees Kelvin/Celsius per degree Fahrenheit.
   *
   *  The magnitude of degrees Celsius and Kelvin are the same (the two scales have different origins only); however,
   *  degrees Fahrenheit have a smaller magnitude and need to be scaled during conversion.
   */
  private val KelvinPerFahrenheitDegree = 5.0 / 9.0

  /** ''Absolute zero'' (0K) in °F. */
  private val AbsoluteZeroFahrenheit = AbsoluteZeroCelsius / KelvinPerFahrenheitDegree + 32.0

  /** Units for temperatures measured in ''[[http://en.wikipedia.org/wiki/Fahrenheit degrees Fahrenheit]]''.
   *
   *  @see [[http://en.wikipedia.org/wiki/Fahrenheit Degrees Fahrenheit]] on ''Wikipedia''.
   *
   *  @since 0.0
   */
  val Fahrenheit: Units = new Units(new LinearConverter(KelvinPerFahrenheitDegree, AbsoluteZeroFahrenheit),
  LibResource("phys.Temperature.Fahrenheit.sym"))

  /** Units for temperatures measured in ''[[http://en.wikipedia.org/wiki/Kelvin degrees Kelvin]]''.
   *
   *  @note ''Degrees Kelvin'' are the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard units for temperature
   *  measurement, and the units that are used to store such measurements internally in ''Facsimile''.
   *
   *  In ''Facsimile'', a ''degree Kelvin'' is defined in accordance with ''SI'' standards.
   *
   *  @see [[http://en.wikipedia.org/wiki/Kelvin Degrees Kelvin]] on ''Wikipedia''.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on ''Wikipedia''.
   *
   *  @since 0.0
   */
  val Kelvin: Units = new Units(SIConverter, LibResource("phys.Temperature.Kelvin.sym"))

  /** Physical quantity family for temperature measurements. */
  protected[phys] override val family = Family(temperatureExponent = 1)

  /** @inheritdoc */
  override val siUnits: Units = Kelvin

  // Register this family.
  Family.register(family, Temperature)

  /** Temperature measurement factory function.
   *
   *  @param measure Measurement, in degrees Kelvin, to be converted into a new types.
   *
   *  @return `types` in the form of a Temperature measurement.
   */
  private[phys] override def apply(measure: Double) =  new Measure(measure)

  /** ''[[http://en.wikipedia.org/wiki/Temperature Temperature]]'' measurement class.
   *
   *  Instances of this class represent ''temperature'' measurements.
   *
   *  @constructor Create new ''[[http://en.wikipedia.org/wiki/Temperature temperature]]'' measurement value.
   *
   *  @param measure ''Temperature'' measurement expressed in ''[[Kelvin]]''. This value must be finite and greater than
   *  For equal to zero.
   *
   *  @throws IllegalArgumentException if `types` is not finite or is negative.
   *
   *  @since 0.0
   */
  final class TemperatureMeasure private[phys](measure: Double)
  extends NonNegativeMeasure[TemperatureMeasure](measure)

  /** ''[[http://en.wikipedia.org/wiki/Temperature Temperature]]'' unit of measurement family class.
   *
   *  Instances of this class represent units for expressing ''temperature'' measurements.
   *
   *  @constructor Create new ''[[http://en.wikipedia.org/wiki/Temperature temperature]]'' unit of measurement.
   *
   *  @param converter Rules to be applied to convert a quantity measured in these units to and from the standard
   *  ''temperature [[http://en.wikipedia.org/wiki/SI SI]]'' units, ''degrees Kelvin''.
   *
   *  @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   *  @since 0.0
   */
  final class TemperatureUnits private[phys](converter: Converter, symbol: String)
  extends NonNegativeUnits(converter, symbol)
}