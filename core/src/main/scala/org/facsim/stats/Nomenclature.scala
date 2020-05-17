/*
 * Facsimile: A Discrete-Event Simulation Library
 * Copyright © 2004-2020, Michael J Allen.
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

/**
 * Trait defining strings for a variety of statistical terms.
 *
 * This trait is employed by objects providing local-specific names and symbols for these terms.
 *
 * @since 0.0
 */
private[stats] trait Nomenclature {

  /**
   * Population mean.
   *
   * @since 0.0
   */
  val PopulationMean: String

  /**
   * Estimate of population mean.
   *
   * @since 0.0
   */
  val PopulationMeanEstimate: String

  /**
   * Population standard deviation.
   *
   * @since 0.0
   */
  val PopulationStandardDeviation: String

  /**
   * Estimate of population standard deviation.
   *
   * @since 0.0
   */
  val PopulationStandardDeviationEstimate: String

  /**
   * Population variance.
   *
   * @since 0.0
   */
  val PopulationVariance: String

  /**
   * Estimate of population variance.
   *
   * @since 0.0
   */
  val PopulationVarianceEstimate: String

  /**
   * Sample minimum.
   *
   * @since 0.0
   */
  val SampleMinimum: String

  /**
   * Sample mean.
   *
   * @since 0.0
   */
  val SampleMean: String

  /**
   * Sample maximum.
   *
   * @since 0.0
   */
  val SampleMaximum: String

  /**
   * Sample standard deviation.
   *
   * @since 0.0
   */
  val SampleStandardDeviation: String

  /**
   * Sample variance.
   *
   * @since 0.0
   */
  val SampleVariance: String
}