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

Java source file belonging to the org.facsim.facsimile.measure package.
*/
//=============================================================================

package org.facsim.facsimile.measure;

import net.jcip.annotations.Immutable;
import org.facsim.facsimile.measure.Measure;
import org.facsim.facsimile.measure.IllegalMeasurementValueException;
import org.facsim.facsimile.util.PackagePrivate;

//=============================================================================
/**
<p>Abstract template base class for all non-negative measurement classes.</p>

<p>This is the base class used by all measurement types that do not permit
negative values (in their underlying <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units).
Non-negative measurement types include mass, temperature (Kelvin scale), time,
etc.</p>

@see org.facsim.facsimile.measure.Measure Measure&lt;T&gt;

@param <T> The Measure sub-class that is being implemented.
*/
//=============================================================================

@Immutable
@PackagePrivate
abstract class NonNegativeMeasure <T extends NonNegativeMeasure <T>>
extends Measure <T>
{

/**
<p>Class serialization schema number.</p>
*/

    private static final long serialVersionUID = 1L;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Construct a non-negative measurement from a value in the standard <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units
for this measurement type.</p>

@param initialValue Initial measurement value in the underlying <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units.

@throws java.lang.IllegalArgumentException if value is negative or invalid. 
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    protected NonNegativeMeasure (double initialValue)
    {

/*
If the value is invalid, this will cause the IllegalArgumentException to be
thrown.
*/

        super (initialValue);

/*
Verify that the value we're being asked to store is not negative.  If it is,
the IllegalMeasurementValueException should be thrown - a helper subclass of
IllegalArgumentException.
*/

        if (initialValue < 0.0)
        {
            throw new IllegalMeasurementValueException (initialValue);
        }
    }
}
