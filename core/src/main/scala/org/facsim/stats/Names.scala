/*
 * Facsimile -- A Discrete-Event Simulation Library
 * Copyright © 2004-2016, Michael J Allen.
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

import org.facsim.LibResource

/**
 * Common statistic names.
 */
object Names
extends Nomenclature {

  /**
   * @inheritdoc
   */
  override val PopulationMean = LibResource("stats.Names.PopulationMean")

  /**
   * @inheritdoc
   */
  override val PopulationMeanEstimate = LibResource("stats.Names.PopulationMeanEstimate")

  /**
   * @inheritdoc
   */
  override val PopulationStandardDeviation = LibResource("stats.Names.PopulationStandardDeviation")

  /**
   * @inheritdoc
   */
  override val PopulationStandardDeviationEstimate = LibResource("stats.Names.PopulationStandardDeviationEstimate")

  /**
   * @inheritdoc
   */
  override val PopulationVariance = LibResource("stats.Names.PopulationVariance")

  /**
   * @inheritdoc
   */
  override val PopulationVarianceEstimate = LibResource("stats.Names.PopulationVarianceEstimate")

  /**
   * @inheritdoc
   */
  override val SampleMaximum = LibResource("stats.Names.SampleMaximum")

  /**
   * @inheritdoc
   */
  override val SampleMean = LibResource("stats.Names.SampleMean")

  /**
   * @inheritdoc
   */
  override val SampleMinimum = LibResource("stats.Names.SampleMinimum")

  /**
   * @inheritdoc
   */
  override val SampleStandardDeviation = LibResource("stats.Names.SampleStandardDeviation")

  /**
   * @inheritdoc
   */
  override val SampleVariance = LibResource("stats.Names.SampleVariance")
}