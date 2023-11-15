/*
 * Facsimile: A Discrete-Event Simulation Library
 * Copyright © 2004-2023, Michael J Allen.
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
 * Scala source file belonging to the org.facsim.stats package.
 */
package org.facsim.stats

import org.facsim.requireValid

/**
 * Histogram class.
 *
 * Records the frequency of observed value ranges by in ''bins''. Values that are lower than anticipated are stored in
 * an ''underflow'' bin; those that are higher than anticipated are stored in an ''overflow'' bin.
 *
 * @constructor Create a new Histogram class instance.
 *
 * @param minimumValue Minimum expected value. Observed values lower than this will be placed into an ''underflow'' bin.
 *
 * @param bins Number of regular bins in the histogram. Two additional bins, an ''underflow'' bin&mdash;storing values
 * less than '''minimumValue'''&mdash;and an ''overflow'' bin&mdash;storing values greater than '''minimumValue''' +
 * ('''bins''' * '''binWidth'''&mdash;will be included in addition to these bins. This argument must be a positive
 * integer or an exception will be thrown.
 *
 * @param binWidth Width of each regular bin in the histogram. This value must be greater than zero, or an exception
 * will be thrown.
 *
 * @tparam D Underlying data type to be stored by this class, which must be a type of number.
 *
 * @throws IllegalArgumentException if '''bins''' is zero or negative, or if '''binWidth''' is zero or negative.
 *
 * @since 0.0
 */
final class Histogram[D <: Number[D]](private val minValue: D, private val bins: Int, private val binWidth: D)
extends SummaryStatistics[D, Histogram[D]] {

  /*
   * Argument sanity checks.
   */
  requireValid(bins, bins > 0)
  requireValid(binWidth, binWidth > 0.0)

  /**
   * Frequency of values observed in each bin.
   *
   * @note Bin 0 is the ''underflow'' bin, which records the frequency of observed values less than the specified
   * minimum value. Similarly, bin (bins + 1) is the ''overflow'' bin, which records the frequency of observed values
   * that exceed (minimumValue + bins * binWidth).
   */
  private val frequency = new Array[Int](bins + 2)

  /**
   * Overflow bin number.
   */
  private val overflowBin = frequency.length - 1

  /**
   * Retrieve frequency observed in specified bin to date.
   *
   * @param bin Number of bin for which frequency sought. Bin number 0 is the underflow bin, bin number
   * ([[org.facsim.stats.Histogram!.length]] - 1) is the overflow bin. If an invalid bin number is passed, then an
   * exception will be thrown.
   *
   * @return Frequency of observations recorded for specified '''bin''' so far.
   *
   * @throws java.lang.ArrayIndexOutOfBoundsException if '''bin''' is outside of the range: [0,
   * [[org.facsim.stats.Histogram!.length]]).
   *
   * @since 0.0
   */
  @inline
  final def apply(bin: Int) = synchronized {
    frequency(bin) ensuring(_ >= 0)
  }

  /**
   * Retrieve number of bins, including underflow and overflow bins.
   *
   * @return Number of bins in the histogram including the underflow and overflow bins.
   *
   * @since 0.0
   */
  @inline
  final def length = frequency.length ensuring(_ >= 3)

  /**
   * @inheritdoc
   */
  protected[stats] final override def processObservation(value: Double): Unit = {

    /*
     * Determine which bin to place this observation in.
     */
    val bin = ((value - minimumValue) / binWidth).toInt + 1

    /*
     * Is this value in the underflow bin?
     */
    if(bin <= 0) frequency.update(0, frequency(0) + 1)

    /*
     * Otherwise, is this value in the overflow bin?
     */
    else if(bin >= overflowBin) frequency.update(overflowBin, frequency(overflowBin) + 1)

    /*
     * Otherwise, add it to the corresponding bin.
     */
    else frequency.update(bin, frequency(bin) + 1)
  }
}
