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
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types.phys

import org.facsim.types.LibResource

/** _[[http://en.wikipedia.org/wiki/Length Length]]_ physical quantity type.
 *
 *  All lengths are stored internally in _[[http://en.wikipedia.org/wiki/Metre meters]]_, which is the
 *  _[[http://en.wikipedia.org/wiki/SI SI]]_ standard unit of types.
 *
 *  @see [[http://en.wikipedia.org/wiki/Length Length]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/Metre Meters]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on _Wikipedia_.
 *
 *  @since 0.0
 */
object Length
extends Specific {

  /** @inheritdoc */
  override type Measure = LengthMeasure

  /** @inheritdoc */
  override type Units = LengthUnits

  /** @inheritdoc */
  override val name: String = LibResource("phys.Length.name")

  /** Number of meters exactly equivalent to one millimeter. */
  private val MetersPerMM = 1.0 / 1000.0

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Millimetre millimeters]]_.
   *
   *  @see _[[http://en.wikipedia.org/wiki/Millimetre Millimeters]]_ on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Millimeters: Units = new Units(new LinearScaleConverter(MetersPerMM), LibResource("phys.Length.Millimeter.sym"))

  /** Number of meters exactly equivalent to one centimeter. */
  private val MetersPerCM = 1.0 / 100.0

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Centimetre centimeters]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Centimetre Centimeters]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Centimeters: Units = new Units(new LinearScaleConverter(MetersPerCM), LibResource("phys.Length.Centimeter.sym"))

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Metre meters]]_.
   *
   *  @note _Meters_ are the _[[http://en.wikipedia.org/wiki/SI SI]]_ standard units for length measurement, and the
   *  units that are used to store such measurements internally in _Facsimile_.
   *
   *  In _Facsimile_, a _meter_ is defined in accordance with _SI_ standards.
   *
   *  @see [[http://en.wikipedia.org/wiki/Metre Meters]] on _Wikipedia_.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Meters: Units = new Units(SIConverter, LibResource("phys.Length.Meter.sym"))

  /** Number of meters exactly equivalent to one kilometer. */
  private val MetersPerKM = 1000.0

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Kilometre kilometers]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Kilometre Kilometers]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Kilometers: Units = new Units(new LinearScaleConverter(MetersPerKM), LibResource("phys.Length.Kilometer.sym"))

  /** Number of meters exactly equivalent to one inch.
   *
   *  @note This value is the internationally accepted rate for converting between meters and inches and is used
   *  internally to convert between metric and Imperial/English length units. This value *must not* be modified!
   *
   *  @see [[http://en.wikipedia.org/wiki/Inch Inch]] on _Wikipedia_.
   */
  private val MetersPerInch = 0.0254

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Inch inches]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Inch Inches]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Inches: Units = new Units(new LinearScaleConverter(MetersPerInch), LibResource("phys.Length.Inch.sym"))

  /** Number of meters exactly equivalent to one foot. */
  private val MetersPerFoot = MetersPerInch * 12.0

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Foot_(unit) feet]]_.
   *
   * @see [[http://en.wikipedia.org/wiki/Foot_(unit) Feet]] on _Wikipedia_.
   *
   * @since 0.0
   */
  val Feet: Units = new Units(new LinearScaleConverter(MetersPerFoot), LibResource("phys.Length.Foot.sym"))

  /** Number of meters exactly equivalent to one yard. */
  private val MetersPerYard = MetersPerFoot * 3.0

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Yard yards]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Yard Yards]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Yards: Units = new Units(new LinearScaleConverter(MetersPerYard), LibResource("phys.Length.Yard.sym"))

  /** Number of meters exactly equivalent to one mile. */
  private val MetersPerMile = MetersPerYard * 1760.0

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Mile miles]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Mile Miles]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Miles: Units = new Units(new LinearScaleConverter(MetersPerMile), LibResource("phys.Length.Mile.sym"))

  /** Physical quantity family for length measurements. */
  protected[phys] override val family = Family(lengthExponent = 1)

  /** @inheritdoc */
  override val siUnits: Units = Meters

  // Register this family.
  Family.register(family, Length)

  /** Length measurement factory function.
   *
   *  @param measure Measurement, in meters, to be converted into a new types.
   *
   *  @return `types` in the form of a Length measurement.
   */
  private[phys] override def apply(measure: Double) = new Measure(measure)

  /** _[[http://en.wikipedia.org/wiki/Length Length]]_ measurement class.
   *
   *  Instances of this class represent _length_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Length length]]_ measurement value.
   *
   *  @param measure _Length_ measurement expressed in _[[Meters]]_. This value must be finite.
   *
   *  @throws IllegalArgumentException if `types` is not finite.
   *
   *  @since 0.0
   */
  final class LengthMeasure private[phys](measure: Double)
  extends SpecificMeasure[LengthMeasure](measure)

  /** _[[http://en.wikipedia.org/wiki/Length Length]]_ unit of measurement family class.
   *
   *  Instances of this class represent units for expressing _length_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Length length]]_ unit of measurement.
   *
   *  @param converter Rules to be applied to convert a quantity measured in these units to and from the standard
   *  _length [[http://en.wikipedia.org/wiki/SI SI]]_ units, _meters_.
   *
   *  @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   *  @since 0.0
   */
  final class LengthUnits private[phys](converter: Converter, symbol: String)
  extends SpecificUnits(converter, symbol)
}
