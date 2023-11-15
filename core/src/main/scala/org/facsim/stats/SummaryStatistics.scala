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

import org.facsim.types.Number

/**
 * Trait underlying all summary statistics.
 *
 * @tparam D Underlying type for data summarized by this type.
 *
 * @tparam S Final statistic type.
 */
sealed trait SummaryStatistics[D <: Number[D], S <: SummaryStatistics[D, S]]
extends Statistic[D, S] {

  /**
   * Observation count.
   *
   * This value is incremented each time a new observation is made. Many of the statistics have undefined values unless
   * the count is at least 1, and some (sample variance and standard deviation) require that at least 2 observations
   * have been made.
   *
   * @since 0.0
   */
  val size: Int

  /**
   * Minimum value observed.
   *
   * If no observations have been made, then value is `None`.
   *
   * @since 0.0
   */
  val minimum: Option[D]

  /**
   * Maximum value observed.
   *
   * If no observations have been made, then value is `None`.
   *
   * @since
   */
  val maximum: Option[D]

  /**
   * Report [[http://en.wikipedia.org/wiki/Arithmetic_mean arithmetic mean]] of observed values.
   *
   * If no observations have been added to this dataset, then there is no observed mean value, so `None` will be
   * returned instead.
   *
   * The mean value is determined as the arithmetic mean of the dataset's values.
   *
   * @return Arithmetic mean of observed values, wrapped in [[Some]], or `None` if no observations have been made.
   *
   * @see [[http://en.wikipedia.org/wiki/Arithmetic_mean Arithmetic Mean]] on Wikipedia.
   *
   * @since 0.0
   */
  def mean: Option[D]

  /**
   * Report unbiased variance estimate for observed values as a sample.
   *
   * Calculation of the unbiased estimate for variance, treating the observed values as a sample, requires at least two
   * observations to have been made; if this function is called for a dataset with fewer observations, `None` will be
   * returned.
   *
   * The unbiased variance estimate for the sample represented by the dataset (using Bessel's correction).
   *
   * @return Variance in values observed wrapped in [[Some]], or `None` otherwise.
   *
   * @see [[http://en.wikipedia.org/wiki/Bessel's_correction Bessel's Correction]] on Wikipedia.
   *
   * @see [[http://en.wikipedia.org/wiki/Variance Variance]] on Wikipedia.
   *
   * @since 0.0
   */
  def variance: Option[D]

  /**
   * Retrieve sample standard deviation on values observed since last statistics reset.
   *
   * The dataset's standard deviation is undefined unless there has been at least two observations made since the last
   * statistics reset; if fewer observations were recorded, `None` is returned.
   *
   * The standard deviation is determined as the unbiased standard deviation estimate (using Bessel's correction) for
   * the sample represented by the dataset.
   *
   * @return Standard deviation of observed values wrapped in [[Some]], or `None` if fewer than two observations have
   * been made.
   *
   * @see [[http://en.wikipedia.org/wiki/Bessel's_correction Bessel's Correction]] on Wikipedia.
   *
   * @see [[http://en.wikipedia.org/wiki/Standard_deviation Standard Deviation]] on Wikipedia.
   *
   * @since 0.0
   */
  final def stdDeviation = variance.map(Math.sqrt)

  /**
   * Sum of all observations made.
   *
   * This statistic is used to generate mean observation statistics. Whenever an observation is recorded, its value is
   * added to total.
   *
   * If no observations have been made, value must be zero.
   *
   * @since 0.0
   */
  val sum: D

  /**
   * Sum of the squares of each observation.
   *
   * This statistic is used to generate standard deviation & variance statistics. Whenever an observation is recorded,
   * the square of its value is added to this total.
   *
   * @since 0.0
   */
  val sumSquared: Double

  /**
   * Add a new observation to the dataset.
   *
   * @param observation New value to be added to this dataset.
   *
   * @return A new summary statistics instance containing the new observation.
   *
   * @since 0.0
   */
  final def add(observation: Double): SummaryStatistics = new PopulatedSummaryStatistics(this, observation)

  /**
   * Reset summary statistics.
   */
  final override def reset: SummaryStatistics = EmptySummaryStatistics
}

/**
 * Empty summary statistics dataset.
 */
object EmptySummaryStatistics
extends SummaryStatistics {

  /**
   * @inheritdoc
   */
  override val size = 0

  /**
   * @inheritdoc
   */
  override val minimum: Option[Double] = None

  /**
   * @inheritdoc
   */
  override val maximum: Option[Double] = None

  /**
   * @inheritdoc
   */
  override val mean: Option[Double] = None

  /**
   * @inheritdoc
   */
  override val variance: Option[Double] = None

  /**
   * @inheritdoc
   */
  override val sum = 0.0

  /**
   * @inheritdoc
   */
  override val sumSquared = 0.0
}

/**
 * Maintains summary statistics for a set of observations.
 *
 * Statistics reported are:
 * -  Number of observations recorded.
 * -  Minimum observation.
 * -  Maximum observation.
 * -  Mean observation.
 * -  Sample variance.
 * -  Sample standard deviation.
 *
 * @todo Population variance and population standard deviation are currently unsupported.
 *
 * @param prior Prior statistics, before addition of `observation`.
 *
 * @param obs Most recent observation added to the dataset.
 *
 * @since 0.0
 */
class PopulatedSummaryStatistics private(prior: SummaryStatistics, obs: Double)
extends SummaryStatistics {

  /**
   * @inheritdoc
   */
  final override val size = prior.size + 1

  /**
   * @inheritdoc
   */
  final override val minimum = prior.minimum.fold(Some(obs)) {
    priorMin =>
    Some(Math.min(priorMin, obs))
  }

  /**
   * @inheritdoc
   */
  final override val maximum = prior.maximum.fold(Some(obs)) {
    priorMax =>
    Some(Math.max(priorMax, obs))
  }

  /**
   * @inheritdoc
   */
  final override val sum = prior.sum + obs

  /**
   * @inheritdoc
   */
  final override val sumSquared = prior.sumSquared + Math.pow(obs, 2)

  /**
   * @inheritdoc
   */
  final override def mean = Some(sum / size)

  /**
   * @inheritdoc
   */
  final override def variance = {
    if(size == 1) None
    else Some((sumSquared - size * Math.pow(mean.get, 2)) / (size - 1))
  }
}