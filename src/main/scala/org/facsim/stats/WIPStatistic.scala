/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

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
Scala source file belonging to the org.facsim.facsimile.stats package.
*/
//=============================================================================

package org.facsim.facsimile.stats
import org.facsim.facsimile.engine.Simulation
import org.facsim.facsimile.measure.Time

//=============================================================================
/**
''Work-in-progress'' - also known as ''work-in-process'' - (WIP) statistic.

Statistic that tracks the number of jobs in a system, process, buffer, etc.
As well as the current levels, the statistic tracks maximum, mean and minimum
levels.

The mean value is time-weighted, meaning that it weights the mean by the time
spent at each WIP level.

@since 0.0
*/
//=============================================================================

class WIPStatistic (initial: Int = 0) extends Statistic {

/**
Current work-in-progress statistic.

This value is not allowed to become negative.
*/

  require (initial >= 0)
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

  private var sum: Double = 0.0

/**
Absolute simulation time at which this statistic was last updated.
*/

  private var updated: Time = Simulation.currentTime

//-----------------------------------------------------------------------------
/**
Update the count by the indicated amount.

@param delta Change to be applied to the current value.

@throws SomeException if delta causes the current value to become negative.
*/
//-----------------------------------------------------------------------------

  final def update (delta: Int): Unit = {
    require (delta > 0 || -delta >= cur)
    assert (Simulation.currentTime >= updated)
    sum += cur * (Simulation.currentTime - updated).value
    updated = Simulation.currentTime
    cur += delta
    if (cur < min) min = cur
    else if (cur > max) max = cur
    assert (cur >= min)
    assert (cur <= max)
  }

  final def current = cur
  final def maximum = max
  final def mean: Double = {
    (sum + cur * (Simulation.currentTime - updated)) / Simulation.lastReset
  }
  final def minimum = min
}