/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file belonging to the org.facsim.stats package.
*/
//=============================================================================

package org.facsim.stats

//=============================================================================
/**
Maintains summary statistics for a set of observations.

Statistics reported are:
 -  Number of observations recorded.
 -  Minimum observation.
 -  Maximum observation.
 -  Mean observation.
 -  Sample variance.
 -  Sample standard deviation.

@todo Population variance and population standard deviation are currently
unsupported.

This is a stateful object, whose contents change with each new observation.

@todo Consider making this an immutable object, in which each new observation
results in a new summary statistic instance.  That could prove to be expensive,
but would be more functional.

@since 0.0
*/
//=============================================================================

trait SummaryStatistics
extends Statistic {

/**
Observation count.

This value is incremented each time a new observation is made.  Many of the
statistics have undefined values unless the count is at least 1, and some
(sample variance and standard deviation) require that at least 2 observations
have been made.
*/

  private final var count = 0 // scalastyle:ignore

/**
Minimum value observed.

If no observations have been made, then value is undefined.
*/

  private final var minValue = 0.0 // scalastyle:ignore

/**
Maximum value observed.

If no observations have been made, then value is undefined.
*/

  private final var maxValue = 0.0 // scalastyle:ignore

/**
Sum of all observations made.

This statistic is used to generate mean observation statistics.  Whenever an
observation is recorded, its value is added to total.

If no observations have been made, value must be zero.
*/

  private final var sumValues = 0.0 // scalastyle:ignore

/**
Sum of the squares of each observation.

This statistic is used to generate standard deviation & variance statistics.
Whenever an observation is recorded, the square of its value is added to this
total.
*/

  private final var sumValuesSquared = 0.0 // scalastyle:ignore

//-----------------------------------------------------------------------------
/**
Add a new observation to the dataset.

@param value Observed value to be added to the dataset.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def addObservation (value: Double): Unit = synchronized {

/*
Update the observation count.
*/

    assert (count >= 0)
    count += 1

/*
If the count is now 1, then we have our first observation, so define the
minimum and maximum values.  Otherwise, check for new minimum/maximum
observations.
*/

    if (count == 1) {
      minValue = value
      maxValue = value
    }
    else if (value < minValue) minValue = value
    else if (value > maxValue) maxValue = value
    assert (minValue <= maxValue)

/*
Update the sum of observations, and sum of each observation squared
*/

    sumValues += value
    sumValuesSquared += value * value

/*
Perform any required sub-class processing.
*/

    processObservation (value)
  }

//-----------------------------------------------------------------------------
/**
Processes an observation that has been added to the dataset.

@param value Observed value to be processed.

@since 0.0
*/
//-----------------------------------------------------------------------------

  protected [stats] def processObservation (value: Double): Unit = {
  }

//-----------------------------------------------------------------------------
/**
Report number of recorded observations.

@return Number of observations recorded.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def observations = count ensuring (_ >= 0)

//-----------------------------------------------------------------------------
/**
Report minimum observed value.

If no observations have been added to this dataset, then there is no observed
minimum value; an [[org.facsim.stats.InsufficientData!]] exception is thrown
instead.

@returns Minimum observed value.

@throws org.facsim.stats.InsufficientDataException if no observations have been
made.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def minimum = synchronized {
    if (count == 0) throw new
    InsufficientDataException (Names.SampleMinimum.capitalize, 1, count)
    else minValue
  }

//-----------------------------------------------------------------------------
/**
Report maximum observed value.

If no observations have been added to this dataset, then there is no observed
maximum value; an [[org.facsim.stats.InsufficientData!]] exception is thrown
instead.

@returns Maximum observed value.

@throws org.facsim.stats.InsufficientDataException if no observations have been
made.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def maximum = synchronized {
    if (count == 0) throw new
    InsufficientDataException (Names.SampleMaximum.capitalize, 1, count)
    else maxValue
  }

//-----------------------------------------------------------------------------
/**
Report [[http://en.wikipedia.org/wiki/Arithmetic_mean arithmetic mean]] of
observed values.

If no observations have been added to this dataset, then there is no observed
mean value; an [[org.facsim.stats.InsufficientData!]] exception is thrown
instead.

The mean value is determined as the arithmetic mean of the dataset's values.

@return Arithmetic mean of observed values.

@throws org.facsim.stats.InsufficientDataException if no observations have been
made.

@see [[http://en.wikipedia.org/wiki/Arithmetic_mean Arithmetic Mean]] on
Wikipedia.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def mean = synchronized {
    if (count == 0) throw new
    InsufficientDataException (Names.SampleMean.capitalize, 1, count)
    else sumValues / count
  }

//-----------------------------------------------------------------------------
/**
Calculate the sample variance.

@note This function employ's Bessel's correction to return an unbiased estimate
of the sample variance.

@param statisticName Function providing the name of the statistic that is
actually being reported, in the event that we have insufficient data.  This is
required because the variance is calculated as part of multiple statistics.
*/
//-----------------------------------------------------------------------------

  private final def calcVariance (statisticName: => String) =
  synchronized {
    if (count < 2) throw new InsufficientDataException (statisticName, 2,
    count)
    else (sumValuesSquared - count * Math.pow (mean, 2)) / (count - 1)
  }

//-----------------------------------------------------------------------------
/**
Report sample variance of observed values.

Calculation of the sample mean requires at least two observations to have been
made; if this function is called for a dataset with fewer observations, an
[[org.facsim.stats.InsufficientData!]] exception is thrown.

The unbiased variance estimate for the sample
represented by the dataset (using Bessel's correction); population variance is
unavailable.

@return Variance in values observed since the last statistics reset.

@throws org.facsim.stats.InsufficientDataException if less than two
observations have been made.

@see <a href="http://en.wikipedia.org/wiki/Bessel&apos;s_correction">Wikipedia:
Bessel&apos;s Correction</a>

@see <a href="http://en.wikipedia.org/wiki/Variance">Wikipedia: Variance</a>

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def variance = calcVariance (Names.SampleVariance.capitalize)

//-----------------------------------------------------------------------------
/**
Retrieve sample standard deviation on values observed since last statistics
reset.

The dataset's standard deviation is undefined unless there has been at least
two observations made since the last statistics reset; if fewer observations
were recorded, an exception is thrown.

The standard deviation is determined as the unbiased standard deviation
estimate (using Bessel's correction) for the sample represented by the
dataset; population standard deviation is unavailable.

@return Standard deviation in values observed since the last statistics reset.

@throws org.facsim.stats.InsufficientDataException if less than two
observations have been made.

@see <a href="http://en.wikipedia.org/wiki/Bessel&apos;s_correction">Wikipedia:
Bessel&apos;s Correction</a>

@see <a href="http://en.wikipedia.org/wiki/Standard_deviation">Wikipedia:
Standard Deviation</a>

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def stdDeviation = Math.sqrt (calcVariance
  (Names.SampleStandardDeviation.capitalize))
}