/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the:

    Free Software Foundation, Inc.
    51 Franklin St, Fifth Floor
    Boston, MA  02110-1301
    USA

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

Java source file for the Util class, and associated elements, that are integral
members of the org.facsim.Facsimile.Common package.
===============================================================================
*/

package org.facsim.Facsimile.Common;

//=============================================================================
/**
 <p>Facsimile utilty functions.</p>
 */
//=============================================================================

public final class Util
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Check that a specifed {@link String} is either null or empty.</p>

<p>An empy {@link String} is one that is made up or zero or more whitespace
characters only; it contains zero non-whitespace characters.</p>

@param string The {@link String} to be checked.

@return True if the string is null or empty, false otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static boolean isNullOrEmpty (String string) {

/*
If the string is null, then we know the answer now...
*/

        if (string == null) {
            return true;
        }

/*
Trim the string to remove whitespace, if the length of the resulting string is
zero, then the string is empty.
*/

        if (string.trim ().length () == 0) {
            return true;
        }

/*
If we made it this far, then this is not a null or empty string.
*/

        return false;
    }
}
