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
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types.phys

import org.facsim.types.LibResource

/** ''[[http://en.wikipedia.org/wiki/Luminous_intensity Luminous intensity]]'' physical quantity type.
 *
 *  All luminous intensity values are stored internally in ''[[http://en.wikipedia.org/wiki/Candela candelas]]'', which
 *  is the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard unit of types.
 *
 *  @see [[http://en.wikipedia.org/wiki/Luminous_intensity Luminous intensity]] on ''Wikipedia''.
 *
 *  @see [[http://en.wikipedia.org/wiki/Candela Candelas]] on ''Wikipedia''.
 *
 *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on ''Wikipedia''.
 *
 *  @since 0.0
 */
object LuminousIntensity
extends NonNegative {

  /** @inheritdoc */
  override type Measure = LuminousIntensityMeasure

  /** @inheritdoc */
  override type Units = LuminousIntensityUnits

  /** @inheritdoc */
  override val name: String = LibResource("phys.LuminousIntensity.name")

  /** Units for luminous intensity measured in ''[[http://en.wikipedia.org/wiki/Candela candelas]]''.
   *
   *  @note ''Candelas'' are the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard units for luminous intensity
   *  measurement, and the units that are used to store such measurements internally in ''Facsimile''.
   *
   *  In ''Facsimile'', a ''candela'' is defined in accordance with ''SI'' standards.
   *
   *  @see [[http://en.wikipedia.org/wiki/Candela Candelas]] on ''Wikipedia''.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on ''Wikipedia''.
   *
   *  @since 0.0
   */
  val Candelas: Units = new Units(SIConverter, LibResource("phys.LuminousIntensity.Candela.sym"))

  /** Physical quantity family for luminous intensity measurements. */
  protected[phys] override val family = Family(luminousIntensityExponent = 1)

  /** @inheritdoc */
  override val siUnits: Units = Candelas

  // Register this family.
  Family.register(family, LuminousIntensity)

  /** Luminous intensity measurement factory function.
   *
   *  @param measure Measurement, in candelas, to be converted into a new types.
   *
   *  @return `types` in the form of a LuminousIntensity measurement.
   */
  private[phys] override def apply(measure: Double) = new Measure(measure)

  /** ''[[http://en.wikipedia.org/wiki/Luminous_intensity Luminous intensity]]'' measurement class.
   *
   *  Instances of this class represent ''luminous intensity'' measurements.
   *
   *  @constructor Create new ''[[http://en.wikipedia.org/wiki/Luminous_intensity luminous intensity]]'' measurement
   *  value.
   *
   *  @param measure ''Luminous intensity'' measurement expressed in ''[[Candelas]]''. This value must be finite and
   *  greater than or equal to zero.
   *
   *  @throws IllegalArgumentException if `types` is not finite or is negative.
   *
   *  @since 0.0
   */
  final class LuminousIntensityMeasure private[phys](measure: Double)
  extends NonNegativeMeasure[LuminousIntensityMeasure](measure)

  /** ''[[http://en.wikipedia.org/wiki/Luminous_intensity Luminous intensity]]'' unit of measurement family class.
   *
   *  Instances of this class represent units for expressing ''luminous intensity'' measurements.
   *
   *  @constructor Create new ''[[http://en.wikipedia.org/wiki/Luminous_intensity luminous intensity]]'' unit of
   *  measurement.
   *
   *  @param converter Rules to be applied to convert a quantity measured in these units to and from the standard
   *  ''luminous intensity [[http://en.wikipedia.org/wiki/SI SI]]'' units, ''candelas''.
   *
   *  @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   *  @since 0.0
   */
  final class LuminousIntensityUnits private[phys](converter: Converter, symbol: String)
  extends NonNegativeUnits(converter, symbol)
}