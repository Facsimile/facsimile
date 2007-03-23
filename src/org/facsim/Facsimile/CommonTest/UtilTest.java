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

Java source file for the UtilTest class, and associated elements, that are
integral members of the org.facsim.Facsimile.CommonTest package.
===============================================================================
*/

package org.facsim.Facsimile.CommonTest;

import org.facsim.Facsimile.Common.Util;
import static org.junit.Assert.*;
import org.junit.Test;

//=============================================================================
/**
 <p>JUnit test fixture for the {@link Util} class.</p>
 */
//=============================================================================

public class UtilTest
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test the {@link Util#isNullOrEmpty (String)} utility function.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void testIsNullOrEmpty ()
    {

/*
Test that the function returns true for any null string, or strings made up of
whitespace only.
*/

        assertTrue (Util.isNullOrEmpty (null));
        assertTrue (Util.isNullOrEmpty ("")); //$NON-NLS-1$
        assertTrue (Util.isNullOrEmpty (" ")); //$NON-NLS-1$
        assertTrue (Util.isNullOrEmpty ("\r")); //$NON-NLS-1$
        assertTrue (Util.isNullOrEmpty ("\n")); //$NON-NLS-1$
        assertTrue (Util.isNullOrEmpty ("\t")); //$NON-NLS-1$
        assertTrue (Util.isNullOrEmpty (" \t\n\r")); //$NON-NLS-1$

/*
Check that the function returns true for any non-null string that contains some
non-whitespace characters.
*/

        assertFalse (Util.isNullOrEmpty ("Not empty")); //$NON-NLS-1$
        assertFalse (Util.isNullOrEmpty ("  Not empty  ")); //$NON-NLS-1$
    }
}
