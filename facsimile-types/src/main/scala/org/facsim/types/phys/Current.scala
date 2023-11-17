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

/** _[[http://en.wikipedia.org/wiki/Electric_current Electric current]]_ physical quantity type.
 *
 *  All electrical current values are stored internally in _[[http://en.wikipedia.org/wiki/Ampere amperes]]_, which is
 *  the _[[http://en.wikipedia.org/wiki/SI SI]]_ standard unit of types.
 *
 *  @see [[http://en.wikipedia.org/wiki/Electric_current Electric current]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/Ampere Amperes]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on _Wikipedia_.
 *
 *  @since 0.0
 */
object Current
extends NonNegative {

  /** @inheritdoc */
  override type Measure = CurrentMeasure

  /** @inheritdoc */
  override type Units = CurrentUnits

  // Name of this physical quantity.
  override val name: String = LibResource("phys.Current.name")

  /** Units for current measured in _[[http://en.wikipedia.org/wiki/Ampere amperes]]_.
   *
   *  @note _Amperes_ are the _[[http://en.wikipedia.org/wiki/SI SI]]_ standard units for electric current
   *  measurement, and the units that are used to store such measurements internally in _Facsimile_.
   *
   *  In _Facsimile_, an _ampere_ is defined in accordance with _SI_ standards.
   *
   *  @see [[http://en.wikipedia.org/wiki/Ampere Ampere]] on _Wikipedia_.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Amperes = new Units(SIConverter, LibResource("phys.Current.Ampere.sym"))

  /** Physical quantity family for current measurements. */
  protected[phys] override val family: Family = Family(currentExponent = 1)

  /** @inheritdoc */
  override val siUnits: Units = Amperes

  // Register this family.
  Family.register(family, Current)

  /** Electrical current measurement factory function.
   *
   *  @param measure Measurement, in amperes, to be converted into a new types.
   *
   *  @return `types` in the form of a Current measurement.
   */
  private[phys] override def apply(measure: Double) = new Measure(measure)

  /** _[[http://en.wikipedia.org/wiki/Electric_current Electric current]]_ measurement class.
   *
   *  Instances of this class represent _electric current_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Electric_current electric current]]_ measurement value.
   *
   *  @param measure _Electric current_ measurement expressed in _[[Amperes]]_. This value must be finite and
   *  greater than or equal to zero.
   *
   *  @throws IllegalArgumentException if `types` is not finite or is negative.
   *
   *  @since 0.0
   */
  final class CurrentMeasure private[phys](measure: Double)
  extends NonNegativeMeasure[CurrentMeasure](measure)

  /** _[[http://en.wikipedia.org/wiki/Electric_current Electric current]]_ unit of measurement family class.
   *
   *  Instances of this class represent units for expressing _electric current_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Electric_current Electric current]]_ unit of
   *  measurement.
   *
   *  @param converter Rules to be applied to convert a quantity measured in these units to and from the standard
   *  _electric current [[http://en.wikipedia.org/wiki/SI SI]]_ units, _amperes_.
   *
   *  @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   *  @since 0.0
   */
  final class CurrentUnits private[phys](converter: Converter, symbol: String)
  extends NonNegativeUnits(converter, symbol)
}