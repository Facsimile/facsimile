/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2009, Michael J Allen.

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

Java source file belonging to the org.facsim.facsimile.engine.measure package.
*/
//=============================================================================


package org.facsim.facsimile.measure;

import java.lang.Double;
import java.lang.IllegalArgumentException;
import java.lang.Override;
import net.jcip.annotations.Immutable;
import org.facsim.facsimile.measure.NonNegativeMeasure;

//=============================================================================
/**
Type class representing a time.

Both relative and absolute times can be represented by this class; times may
never be negative.

Time are stored internally in seconds, which is the SI standard unit of time.
*/
//=============================================================================

@Immutable
public final class Time
extends NonNegativeMeasure <Time>
{

/**
Class serialization schema number.
*/

    private static final long serialVersionUID = 1L;

/**
Time representing zero.
*/

    private final static Time zeroTime;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Static initialization.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {
        zeroTime = new Time (0.0);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Retrieve the time whose value is zero.

@return The time instance representing 0.0.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Time zero ()
    {
        assert zeroTime != null;
        return zeroTime;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Add two time values together.

TODO: Guard against null arguments.

@param first Time value to be added.  This value cannot be null.

@param second Time value to be added.  This value cannot be null.

@return A time representing the sum of the two parameters.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Time add (Time first, Time second)
    {
        return new Time (first.getValue () + second.getValue ());
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Construct a time from a value in seconds.

REFACTOR: This constructor may change future to avoid hard-coding the selected
time units into a simulation.  A second argument identifying the time units is
likely to be added.

@param timeValue Time value to be stored.

@throws java.lang.IllegalArgumentException if value is negative or an invalid
value (infinity, NaN, etc.)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Time (double timeValue)
    throws IllegalArgumentException
    {

/*
Superclass construction.
*/

        super (timeValue);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Object#toString()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final String toString ()
    {

/*
For now, we just output the string version of a double.  This may change in
the future - particularly because it exposes seconds as the default time unit.

TODO: Consider a string representation of a time, that ought to be relevant no
matter what time units are preferred (and add a String constructor to reverse
the process).
*/

        return Double.toString (this.getValue ());
    }
}
