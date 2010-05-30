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

import org.facsim.facsimile.util.PackagePrivate;
import org.facsim.facsimile.util.Resource;
import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p>Exception thrown when a bad delimiter value is encountered.<p>

<p>This exception is thrown if an invalid delimiter string is passed to a
{@linkplain Delimiter} constructor.  Refer to that class for further
information about what constitutes a bad delimiter string.</p>

@see Delimiter
*/
//=============================================================================

@Immutable
public class BadDelimiterException
extends IllegalArgumentException
{

/**
<p>Serialization version UID.</p>
*/

    private static final long serialVersionUID = 1L;

/**
<p>Bad delimiter string provided.</p>
*/

    private final String badDelimiters;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Bad delimiter constructor.</p>

@param badDelimiterString Bad delimiter string that caused this exception to be
thrown.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    BadDelimiterException (String badDelimiterString)
    {

/*
Initialize the super class.
*/

        super ();

/*
Copy the delimiter string provided.
*/

        this.badDelimiters = badDelimiterString; 
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Throwable#getMessage()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final String getMessage ()
    {

/*
Since many invalid characters do not print well during output, we'll simply
output each character as a Unicode hex value.

TODO: Tidy this up to use printable Unicode characters, translate others into
common representations (e.g. tab -> \t, etc.) where possible and only use
Unicode hex as a last resort.
*/

        String formattedDelimiters;
        if (this.badDelimiters != null)
        {
            formattedDelimiters = "";
            for (int index = 0; index < this.badDelimiters.length (); ++index)
            {
                int character = this.badDelimiters.codePointAt (index);
                formattedDelimiters += "\\u" + Integer.toHexString (character);
            }
        }
        else
        {
            formattedDelimiters = "[null]";
        }

/*
Output the result.
*/

        return Resource.format ("io.badDelimiter", formattedDelimiters);
    }
}
