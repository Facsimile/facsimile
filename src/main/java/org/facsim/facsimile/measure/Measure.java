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

import java.lang.Comparable;
import java.lang.Override;
import net.jcip.annotations.Immutable;
import org.facsim.facsimile.measure.BaseMeasure;

//=============================================================================
/**
Abstract template base class for all measurement classes.

This is the base class used by all measurement types, storing the underlying
values in the corresponding <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a>
units.

@see org.facsim.facsimile.measure.BaseMeasure BaseMeasure

@param <T> The Measure sub-class that is being implemented.
*/
//=============================================================================

@Immutable
public abstract class Measure <T extends Measure <T>>
extends BaseMeasure
implements Comparable <T>
{

/**
Class serialization schema number.
*/

    private static final long serialVersionUID = 1L;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Construct a measurement from a value in the standard <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units
for this measurement type.

@param initialValue Initial measurement value in the underlying <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units.

@throws java.lang.IllegalArgumentException if value is invalid (infinity, NaN,
etc.)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected Measure (double initialValue)
    throws IllegalArgumentException
    {

/*
Initialize the super class.
*/

        super (initialValue);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Comparable#compareTo(java.lang.Object)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final int compareTo (T other)
    {

/*
Delegate to the Double.compare procedure.  This ought to handle all elements of
the "compareTo" contract, including the hndling of null arguments, etc.
*/

        return Double.compare (this.getValue (), other.getValue ());
    }
}
