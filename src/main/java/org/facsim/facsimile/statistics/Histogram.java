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

*/
//=============================================================================

@NotThreadSafe
public class Histogram
extends SummaryStatistics
{

/**
<p>Minimum expected value in the histogram.</p>
*/

    private final double minimumValue;

/**
<p>The width of each bin.  This value must be positive.</p>
*/

    private final double width;

/**
<p>Array of bins frequencies in the histogram.</p>

<p>The 0th element of the array is the underflow bin count.  The N+1th (N being
the number of bins in the histogram) is the overflow bin count.</p>
*/

    private int frequency [];

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Histogram constructor.</p>

@param minimumBinValue Lower bound of minimum bin.  Any observations that are
lower than this value are placed in the <em>underflow</em> bin.  The maximum
bin value is determined from this value, the bin count and the bin width.

@param binCount Number of bins in the histogram.  This value cannot be less
than 2.

@param binWidth Width of each bin in the histogram.  This value must be
positive.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Histogram (double minimumBinValue, int binCount, double binWidth)
    {

/*
Verify that we have at least two bins.
*/

        if (binCount < 2)
        {
            throw new IllegalArgumentException ("TODO");
        }

/*
Verify that the bin width is positive.
*/

        if (binWidth <= 0.0)
        {
            throw new IllegalArgumentException ("TODO");
        }

/*
Store the information and setup the bin frequencies.
*/

        this.minimumValue = minimumBinValue;
        this.width = binWidth;

/*
Create the array of bin frequencies.  Two additional bins are added: the
underflow bin (index 0) and the overflow bin (index binCount + 1).
*/

        this.frequency = new int [binCount + 2];
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see org.facsim.facsimile.statistics.SummaryStatistics#processObservation(double)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    protected void processObservation (double value)
    {

/*
Have our parent class process this observation first.
*/

        super.processObservation (value);

/*
Determine which bin to place this observation in.
*/

        int bin = (int) ((value - this.minimumValue) / this.width) + 1;

/*
If this value is in the underflow bin, then put it there.
*/

        if (bin < 0)
        {
            bin = 0;
        }

/*
Otherwise, if it belongs in the overflow bin, then put it there.
*/

        else if (bin >= this.frequency.length)
        {
            bin = this.frequency.length - 1;
        }

/*
Update the frequency for the indicated bin.
*/

        ++this.frequency [bin];
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
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }
}
