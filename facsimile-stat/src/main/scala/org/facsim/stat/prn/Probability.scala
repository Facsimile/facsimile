//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2018, Michael J Allen.
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
// Scala source file belonging to the org.facsim.stat.prn package.
//======================================================================================================================
package org.facsim.stat.prn

import org.facsim.util.requireValid

/** Probability value.
 *
 *  A probability is a value in the range [0, 1], which represents the odds of an event occurring. Probabilities are
 *  real values that range from 0 (impossible) through to 1 (certain).
 *
 *  @constructor Create a new probability.
 *
 *  @param prob Probability of some event occurring. Probability values must be in the range [0, 1].
 *
 *  @throws IllegalArgumentException if `prob` is outside the range [0, 1].
 *
 *  @since 0.0
 */
final class Probability private(private val prob: Double) {

  // Probability values outside the valid range will result in an exception.
  requireValid(prob, prob >= 0.0 && prob <= 1.0)

  /** Probability of this event or an event with the other probability occurring.
   *
   *  @param that Probability of the other event occurring.
   *
   *  @return Probability of this event or `that` event occurring.
   *
   *  @throws IllegalArgumentException if the resulting probability is outside the range [0, 1].
   *
   *  @since 0.0
   */
  def or(that: Probability): Probability = Probability(prob + that.prob)

  /** Probability of this event and another event occurring.
   *
   *  @param that Probability of the other event occuring.
   *
   *  @return Probability of this event and `that` event occuring.
   *
   *  @since 0.0
   */
  def and(that: Probability): Probability = Probability(prob * that.prob)

  /** Probability of this event not occurring.
   *
   *  @return Probability of this event not occurring.
   *
   *  @since 0.0
   */
  def not: Probability = Probability(1.0 - prob)
}

/** Probability companion.
 *
 *  @since 0.0
 */
object Probability {

  /** Probability factory method.
   *
   *  @param p Value of the new probability.
   *
   *  @return New probability instance.
   *
   *  @since 0.0
   */
  def apply(p: Double): Probability = new Probability(p)
}