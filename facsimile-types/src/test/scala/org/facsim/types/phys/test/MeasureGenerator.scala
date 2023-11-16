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
// Scala source file belonging to the org.facsim.types.phys.test types.
//======================================================================================================================
package org.facsim.types.phys.test

import org.facsim.types.phys._
import org.scalacheck.Gen

// Disable test-problematic Scalastyle checkers.

/** Measurement class generators.
 *
 *  @since 0.0
 */
object MeasureGenerator {

  /** Angle generator.
   *
   *  Generate valid angles, in the range [-10τ, 10τ] radians, which provides for 20 complete revolutions overall.
   */
  val angleGen = Gen.choose(-10.0 * 2.0 * Math.PI, 10 * 2.0 * Math.PI).map(Angle(_, Angle.Radians))

  /** Current generator.
   *
   *  Generate valid current values (is there a pun there?), in the range [0, ∞) amperes.
   */
  val currentGen = Gen.choose(0.0, Double.MaxValue).map(Current(_, Current.Amperes))

  /** Length generator.
   *
   *  Generate valid length values, in the range (-∞, ∞) meters.
   */
  val lengthGen = Gen.choose(-Double.MaxValue, Double.MaxValue).map(Length(_, Length.Meters))

  /** Luminous intensity generator.
   *
   *  Generate valid luminous intensity values, in the range [0, ∞) candelas.
   */
  val luminousIntesnityGen = Gen.choose(0.0, Double.MaxValue).map(LuminousIntensity(_, LuminousIntensity.Candelas))

  /** Mass generator.
   *
   *  Generate valid mass values, in the range [0, ∞) kilograms.
   */
  val massGen = Gen.choose(0.0, Double.MaxValue).map(Mass(_, Mass.Kilograms))

  /** Temperature generator.
   *
   *  Generate valid temperature values, in the range [0, ∞) degrees Kelvin.
   */
  val temperatureGen = Gen.choose(0.0, Double.MaxValue).map(Temperature(_, Temperature.Kelvin))

  /** Time generator.
   *
   *  Generate valid time values, in the range [0, ∞) seconds.
   */
  val timeGen = Gen.choose(0.0, Double.MaxValue).map(Time(_, Time.Seconds))
}
// Re-enable test-problematic Scalastyle checkers.

