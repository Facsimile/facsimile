/*
 * Facsimile: A Discrete-Event Simulation Library
 * Copyright © 2004-2025, Michael J Allen.
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
import org.facsim.engine.Simulation
import org.facsim.types.Time

/**
 * _Work-in-progress_ - also known as _work-in-process_ - (_WIP_) statistic.
 *
 * Statistic that tracks the number of jobs in a system, process, buffer, etc. As well as the current WIP, this element
 * tracks maximum, mean and minimum levels.
 *
 * The mean value is time-weighted, meaning that it weights the mean by the time spent at each WIP level.
 *
 * @constructor Create new WIP-tracking statistic.
 *
 * @since 0.0
 */
sealed trait WIPStatistic
extends Statistic {

  /**
   * Current work-in-progress statistic.
   *
   * This value cannot become negative.
   */
  val current = initial

/**
Maximum observed work-in-progress statistic.
*/

  val maximum = initial

/**
Minimum observed work-in-progress statistic.
*/

  val minimum = initial

  /**
   * Sum of size(i) * time(i).
   *
   * The time-weighted mean work-in-progress is calculated as:
   *
   * sum (size(i) * time(i))/(total time)
   */
  private val sum = Time.Zero

  /**
   * Absolute simulation time at which we started collecting WIP data.
   */
  private val startTime = Simulation.currentTime

  /**
   * Absolute simulation time at which this statistic was last updated.
   */
  private val updated = startTime

  /**
   * Update the count by the indicated amount.
   *
   * @param delta Change to be applied to the current value.
   *
   * @throws IllegalArgumentException if the current work-in-progress count becomes negative after applying *delta*.
   */
  final def update(delta: Int): Unit = {

/*
Sanity checks.
*/

    requireValid(delta, cur + delta >= 0)

/*
Update the statistics record for the time spent at the current size.
*/

    val now = Simulation.currentTime
    sum +=(now - updated) * cur

/*
Now update the current value and the time at which we did so.
*/

    cur += delta
    updated = now

/*
Check whether we have a new minimum or maximum value.
*/

    if(cur < min) min = cur
    else if(cur > max) max = cur
  }

  final def current = cur
  final def maximum = max
  final def mean: Double = {
    val now = Simulation.currentTime
    if(now > startTime)(sum +(now - updated) * cur) /(now - startTime)
    else 0.0
  }
  final def minimum = min
}