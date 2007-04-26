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

C# source file for the Util class, and associated elements, that are integral
members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common {

//=============================================================================
/**
 <summary>Facsimile utilty functions.</summary>
 */
//=============================================================================

    public static class Util
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check if a string is null or empty.</summary>

<remarks>This method differs from <see cref="System.String.IsNullOrEmpty
(string)" /> because it defines an empty string as one that does not contain
any non-whitespace characters; that is, strings that consist only of one or
more whitespace characters are considered empty by this function.</remarks>

<param name="value">The <see cref="System.String" /> reference to be
checked.</param>

<returns>A <see cref="System.Boolean" /> that is true if <paramref name="value"
/> is null or empty (see definition above), or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool IsNullOrEmpty (string value)
        {

/*
If the string is null or empty, then return true.  Trim the string to determine
if it is empty.
*/

            if (value == null || value.Trim ().Length == 0) {
                return true;
            }

/*
Nope!  This is not a null or empty string.
*/

            return false;
        }
    }
}
