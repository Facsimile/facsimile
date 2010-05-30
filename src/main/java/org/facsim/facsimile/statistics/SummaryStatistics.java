/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2010, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

	http://www.facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

	http://www.facsim.org/Documentation/CodingStandards/
===============================================================================
$Id$

Java source file belonging to the org.facsim.facsimile.statistics package.
*/
//=============================================================================

package org.facsim.facsimile.statistics;

import net.jcip.annotations.NotThreadSafe;

//=============================================================================
/**
<p>Reports summary statistics for a set of observed values, termed a
<em>dataset</em>.</p>

<p>For a set of observations, reports summary statistics (minimum, mean,
maximum, standard deviation, variance, etc.).</p>

<p>The observed values themselves are not recorded.</p>

@see <a href="http://en.wikipedia.org/wiki/Summary_statistics">Wikipedia:
Summary Statistics</a>
*/
//=============================================================================

@NotThreadSafe
public class SummaryStatistics
extends Statistic
{

/**
<p>Observation count.</p>

<p>This value is incremented each time a new observation is made.  Many of the
statistics have undefined values unless the count is at least 1, and some (such
as sample standard deviation) require that at least 2 observations have been
made.</p>

<p>This value must be set to zero when a statistics reset is performed.</p>
*/

    private int count;

/**
<p>Minimum value observed.</p>

<p>If no observations have been made, then this value is undefined.</p>

<p>This value must be cleared when a statistics reset is performed.</p>
*/

    private double minValue;

/**
<p>Maximum value observed.</p>

<p>If no observations have been made, then this value is undefined.</p>

<p>This value must be cleared when a statistics reset is performed.</p>
*/

    private double maxValue;

/**
<p>Sum of all observations made.</p>

<p>This statistic is used to generate mean observation statistics.  Whenever
an observation is recorded, its value is added to this total.</p>

<p>If no observations have been made, this value must be zero.</p>

<p>This value must be set to zero when a statistics reset is performed.</p>
*/

    private double sumValues;

/**
<p>Sum of the squares of each observation.</p>

<p>This statistic is used to generate standard deviation & variance statistics.
Whenever an observation is recorded, the square of its value is added to this
total.</p>
*/

    private double sumValuesSquared;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Default constructor.</p>

<p>Construct a new summary statistics instance.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public SummaryStatistics ()
    {

/*
Initialize our super class.
*/

        super ();

/*
Initialize the statistics.
*/

        initialize ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Initialize/reset statistics.</p>

<p>Perform a statistics reset/initialization upon this class's members.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void initialize ()
    {

/*
Reset the number of observations.
*/

        this.count = 0;

/*
Although their values have no meaning, set the minimum and maximum observed
values to zero.
*/

        this.minValue = 0.0;
        this.maxValue = 0.0;

/*
Reset the sums to zero.
*/

        this.sumValues = 0.0;
        this.sumValuesSquared = 0.0;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Record an observed value in the dataset.</p>

<p>A statistics reset operation will remove all observations made prior to the
reset.</p>

@param value Observed value to be added to the data set.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final void recordObservation (double value)
    {
        processObservation (value);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Processes an observation that has been added to the dataset.

@param value Observed value to be processed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected void processObservation (double value)
    {

/*
Update the observation count.
*/

        assert (this.count >= 0);
        ++this.count;

/*
If the count is now 1, then we have our first observation, so define the
minimum and maximum values.  Otherwise, check for new minimum/maximum
observations.
*/

        if (this.count == 1)
        {
            this.minValue = this.maxValue = value;
        }
        else if (value < this.minValue)
        {
            this.minValue = value;
        }
        else if (value > this.maxValue)
        {
            this.maxValue = value;
        }
        assert (this.minValue <= this.maxValue);

/*
Update the sum of observations, and sum of observation-squares.
*/

        this.sumValues += value;
        this.sumValuesSquared += value * value;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report number of values observed since the last statistics reset.</p>

@return The number of values observed since the last statistics reset.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final int observations ()
    {
        assert (this.count >= 0);
        return this.count;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve minimum value observed since the last statistics reset.</p>

<p>The dataset's minimum value is undefined unless there has been at least one
observation made since the last statistics reset; if no observations were
recorded, an exception is thrown.</p>

@return Minimum value observed since the last statistics reset.

@throws org.facsim.facsimile.statistics.InsufficientData Thrown if no values
were observed since the last statistics reset.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final double minimum ()
    throws InsufficientData
    {
        if (this.count == 0)
        {
            throw new InsufficientData ();
        }
        return this.minValue;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve maximum value observed since the last statistics reset.</p>

<p>The dataset's maximum value is undefined unless there has been at least one
observation made since the last statistics reset; if no observations were
recorded, an exception is thrown.</p>

@return Maximum value observed since the last statistics reset.

@throws org.facsim.facsimile.statistics.InsufficientData Thrown if no values
were observed since the last statistics reset.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final double maximum ()
    throws InsufficientData
    {
        if (this.count == 0)
        {
            throw new InsufficientData ();
        }
        return this.maxValue;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve mean value observed since the last statistics reset.</p>

<p>The dataset's mean value is undefined unless there has been at least one
observation made since the last statistics reset; if no observations were
recorded, an exception is thrown.</p>

<p>The mean value is determined as the arithmetic mean of the dataset's
values.<p>

@return Mean value observed since the last statistics reset.

@throws org.facsim.facsimile.statistics.InsufficientData Thrown if no values
were observed since the last statistics reset.

@see <a href="http://en.wikipedia.org/wiki/Arithmetic_mean">Wikipedia:
Arithmetic Mean</a>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final double mean ()
    throws InsufficientData
    {
        if (this.count == 0)
        {
            throw new InsufficientData ();
        }
        return this.sumValues / this.count;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve sample variance on values observed since last statistics reset.</p>

<p>The dataset's variance is undefined unless there has been at least two
observations made since the last statistics reset; if fewer observations were
recorded, an exception is thrown.</p>

<p>The variance is determined as the unbiased variance estimate for the sample
represented by the dataset (using Bessel's correction); population variance is
unavailable.<p>

@return Variance in values observed since the last statistics reset.

@throws org.facsim.facsimile.statistics.InsufficientData Thrown if fewer than
two values were observed since the last statistics reset.

@see <a href="http://en.wikipedia.org/wiki/Bessel&apos;s_correction">Wikipedia:
Bessel&apos;s Correction</a>

@see <a href="http://en.wikipedia.org/wiki/Variance">Wikipedia: Variance</a>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final double variance ()
    throws InsufficientData
    {
        if (this.count < 2)
        {
            throw new InsufficientData ();
        }
        return (this.sumValuesSquared - this.count * Math.pow (mean (), 2)) /
        (this.count - 1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve sample standard deviation on values observed since last statistics
reset.</p>

<p>The dataset's standard deviation is undefined unless there has been at least
two observations made since the last statistics reset; if fewer observations
were recorded, an exception is thrown.</p>

<p>The standard deviation is determined as the unbiased standard deviation
estimate (using Bessel's correction) for the sample represented by the
dataset; population standard deviation is unavailable.<p>

@return Standard deviation in values observed since the last statistics reset.

@throws org.facsim.facsimile.statistics.InsufficientData Thrown if fewer than
two values were observed since the last statistics reset.

@see <a href="http://en.wikipedia.org/wiki/Bessel&apos;s_correction">Wikipedia:
Bessel&apos;s Correction</a>

@see <a href="http://en.wikipedia.org/wiki/Standard_deviation">Wikipedia:
Standard Deviation</a>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final double stdDeviation ()
    throws InsufficientData
    {
        return Math.sqrt (variance ());
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see org.facsim.facsimile.statistics.Statistic#reportStatistics()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    protected void reportStatistics ()
    {

        //TODO...
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see org.facsim.facsimile.statistics.Statistic#resetStatistics()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    protected void resetStatistics ()
    {

/*
Reset the statistics.
*/

        initialize ();
    }
}
