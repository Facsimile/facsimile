/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file belonging to the org.facsim.stats package.
*/
//=============================================================================

package org.facsim.stats

import org.facsim.requireValid
import org.facsim.engine.Simulation
import org.facsim.measure.Time

//=============================================================================
/**
''Work-in-progress'' - also known as ''work-in-process'' - (WIP) statistic.

Statistic that tracks the number of jobs in a system, process, buffer, etc.
As well as the current levels, the statistic tracks maximum, mean and minimum
levels.

The mean value is time-weighted, meaning that it weights the mean by the time
spent at each WIP level.

@constructor Create new WIP-tracking statistic.

@param initial Initial work-in-progress.  This value must be zero or positive;
an exception is thrown if this value is negative.

@throws java.lang.IllegalArgumentException if '''initial''' is negative.

@since 0.0
*/
//=============================================================================

class WIPStatistic (initial: Int = 0) extends Statistic {

/*
Argument sanity checks.
*/

  requireValid (initial, initial >= 0)

/**
Current work-in-progress statistic.

This value cannot become negative.
*/

  private var cur = initial

/**
Maximum observed work-in-progress statistic.
*/

  private var max = initial

/**
Minimum observed work-in-progress statistic.
*/

  private var min = initial

/**
Sum of size(i) * time(i).

The time-weighted mean work-in-progress is calculated as:

sum (size(i) * time(i))/(total time)
*/

  private var sum = Time (0.0, Time.seconds)

/**
Absolute simulation time at which we started collecting WIP data.
*/

  private val startTime = Simulation.currentTime
/**
Absolute simulation time at which this statistic was last updated.
*/

  private var updated = startTime

//-----------------------------------------------------------------------------
/**
Update the count by the indicated amount.

@param delta Change to be applied to the current value.

@throws java.lang.IllegalArgumentException if the current work-in-progress
count becomes negative after applying '''delta'''.
*/
//-----------------------------------------------------------------------------

  final def update (delta: Int): Unit = {

/*
Sanity checks.
*/

    requireValid (delta, cur + delta >= 0)

/*
Update the statistics record for the time spent at the current size.
*/

    val now = Simulation.currentTime
    sum += (now - updated) * cur

/*
Now update the current value and the time at which we did so.
*/

    cur += delta
    updated = now

/*
Check whether we have a new minimum or maximum value.
*/

    if (cur < min) min = cur
    else if (cur > max) max = cur
  }

  final def current = cur
  final def maximum = max
  final def mean: Double = {
    val now = Simulation.currentTime
    if (now > startTime) (sum + (now - updated) * cur) / (now - startTime)
    else 0.0
  }
  final def minimum = min
}