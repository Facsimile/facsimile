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

Java source file belonging to the org.facsim.facsimile.measure package.
*/
//=============================================================================


package org.facsim.facsimile.measure;

import net.jcip.annotations.Immutable;
import org.facsim.facsimile.util.Resource;

//=============================================================================
/**
Exception thrown when an illegal measurement value is encountered.

Note that a valid measurement value for one measurement unit may not be a valid
measurement value for a different measurement family, or a different
measurement unit.  For example, a temperature of -10 degrees is acceptable for
if the temperature units are degrees Celsius, but illegal if the temperature
units are degrees Kelvin.  Similarly, a distance of -100 meters is acceptable,
but a mass of -100 grammes is illegal.
*/
//=============================================================================

@Immutable
public class IllegalMeasurementValueException
extends IllegalArgumentException
{

/**
Class serialization schema number.
*/

    private static final long serialVersionUID = 1L;

/**
The offending value.
*/

    private final double badValue;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Constructor.

@param badValue Value found to be an unacceptable measurement value.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public IllegalMeasurementValueException (double badValue)
    {

/*
Super class initialization.
*/

        super ();

/*
Store the bad value.
*/

        this.badValue = badValue;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Throwable#getLocalizedMessage()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String getLocalizedMessage ()
    {
        return Resource.format ("illegalMeasurementValue", new Double
        (this.badValue));
    }
}