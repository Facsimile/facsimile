//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
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

/** _[[http://en.wikipedia.org/wiki/Mass Mass]]_ physical quantity type.
 *
 *  All mass values are stored internally in _[[http://en.wikipedia.org/wiki/Kilogram kilograms]]_, which is the
 *  _[[http://en.wikipedia.org/wiki/SI SI]]_ standard unit of types.
 *
 *  @see [[http://en.wikipedia.org/wiki/Mass Mass]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/Kilogram Kilograms]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on _Wikipedia_.
 *
 *  @since 0.0
 */
object Mass
extends NonNegative {

  /** @inheritdoc */
  override type Measure = MassMeasure

  /** @inheritdoc */
  override type Units = MassUnits

  /** @inheritdoc */
  override val name: String = LibResource("phys.Mass.name")

  /** Number of kilograms exactly equivalent to one gram. */
  private val KilogramsPerG = 1.0 / 1000.0

  /** Units for masses measured in _[[http://en.wikipedia.org/wiki/Gram grams]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Gram grams]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Grams: Units = new Units(new LinearScaleConverter(KilogramsPerG), LibResource("phys.Mass.Gram.sym"))

  /** Number of kilograms exactly equivalent to one milligram. */
  private val KilogramsPerMG = KilogramsPerG / 1000.0

  /** Units for masses measured in _[[http://en.wikipedia.org/wiki/Milligram#SI_multiples milligrams]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Milligram#SI_multiples Milligrams]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Milligrams: Units = new Units(new LinearScaleConverter(KilogramsPerMG), LibResource("phys.Mass.Milligram.sym"))

  /** Units for lengths measured in _[[http://en.wikipedia.org/wiki/Kilogram kilograms]]_.
   *
   *  @note _Kilograms_ are the _[[http://en.wikipedia.org/wiki/SI SI]]_ standard units for mass measurement, and
   *  the units that are used to store such measurements internally in _Facsimile_.
   *
   *  In _Facsimile_, a _kilogram_ is defined in accordance with _SI_ standards.
   *
   *  @see [[http://en.wikipedia.org/wiki/Kilogram Kilograms]] on _Wikipedia_.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Kilograms: Units = new Units(SIConverter, LibResource("phys.Mass.Kilogram.sym"))

  /** Number of kilograms exactly equivalent to one metric tonne. */
  private val KilogramsPerTonne = 1000.0

  /** Units for masses measured in _[[http://en.wikipedia.org/wiki/Tonne metric tonnes]]_ (also known as
   *  _megagrams_).
   *
   *  @see [[http://en.wikipedia.org/wiki/Tonne Tonnes]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Tonnes: Units = new Units(new LinearScaleConverter(KilogramsPerTonne), LibResource("phys.Mass.Tonne.sym"))

  /** Number of kilograms equivalent to one avoirdupois pound.
   *
   *  @note This value is the internationally accepted rate for converting between kilograms and pounds and is used
   *  internally to convert between metric and Imperial/English mass units. This value *must not* be modified!
   *
   *  @see [[http://en.wikipedia.org/wiki/Kilogram Kilogram]] on _Wikipedia_.
   */
  private val KilogramsPerPound = 0.45359237

  /** Units for masses measured in _[[http://en.wikipedia.org/wiki/Pound_(mass) avoirdupois pounds]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Pound_(mass) Pounds]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Pounds: Units = new Units(new LinearScaleConverter(KilogramsPerPound), LibResource("phys.Mass.Pound.sym"))

  /** Number of kilograms exactly equivalent to one ounce. */
  private val KilogramsPerOunce = KilogramsPerPound / 16.0

  /** Units for masses measured in _[[http://en.wikipedia.org/wiki/Ounce ounces]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Ounce Ounces]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Ounces: Units = new Units(new LinearScaleConverter(KilogramsPerOunce), LibResource("phys.Mass.Ounce.sym"))

  /** Physical quantity family for mass measurements. */
  protected[phys] override val family = Family(massExponent = 1)

  /** @inheritdoc */
  override val siUnits: Units = Kilograms

  // Register this family.
  Family.register(family, Mass)

  /** Mass measurement factory function.
   *
   *  @param measure Measurement, in kilograms, to be converted into a new types.
   *
   *  @return `types` in the form of a Mass measurement.
   */
  private[phys] override def apply(measure: Double) = new Measure(measure)

  /** _[[http://en.wikipedia.org/wiki/Mass Mass]]_ measurement class.
   *
   *  Instances of this class represent _mass_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Mass mass]]_ measurement value.
   *
   *  @param measure _Mass_ measurement expressed in _[[Kilograms]]_. This value must be finite and greater than or
   *  equal to zero.
   *
   *  @throws IllegalArgumentException if `types` is not finite or is negative.
   *
   *  @since 0.0
   */
  final class MassMeasure private[phys](measure: Double)
  extends NonNegativeMeasure[MassMeasure](measure)

  /** _[[http://en.wikipedia.org/wiki/Mass Mass]]_ unit of measurement family class.
   *
   *  Instances of this class represent units for expressing _mass_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Mass mass]]_ unit of measurement.
   *
   *  @param converter Rules to be applied to convert a quantity measured in these units to and from the standard _mass
   *  [[http://en.wikipedia.org/wiki/SI SI]]_ units, _kilograms_.
   *
   *  @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   *  @since 0.0
   */
  final class MassUnits private[phys](converter: Converter, symbol: String)
  extends NonNegativeUnits(converter, symbol)
}