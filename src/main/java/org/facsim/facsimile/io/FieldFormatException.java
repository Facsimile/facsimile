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

Java source file belonging to the org.facsim.facsimile.io package.
*/
//=============================================================================

package org.facsim.facsimile.io;

import java.io.IOException;
import org.facsim.facsimile.util.PackagePrivate;
import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p>Exception indicating that a field retrieved from a tokenized stream was
not formatted as expected.</p>
*/
//=============================================================================

@Immutable
public final class FieldFormatException
extends IOException
{

/**
<p>Serialization version UID.</p>
*/

    private static final long serialVersionUID = 1L;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Number format exception constructor.</p>

<p>Construct this exception from the indicated number format exception.</p>

@param numberException String conversion exception, occurring during an I/O
read operation, that will be stored as the underlying cause of this exception.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    FieldFormatException (NumberFormatException numberException)
    {

/*
Initialize the super class.
*/

        super ();

/*
Initialize the cause of this exception to be the passed exception.
*/

        if (numberException != null)
        {
            initCause (numberException);
        }
    }
}
