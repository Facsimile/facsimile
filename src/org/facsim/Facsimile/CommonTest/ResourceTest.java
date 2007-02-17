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

Java source file for the ResourceTest class, and associated elements, that are
integral members of the org.facsim.Facsimile.CommonTest package.
===============================================================================
*/

package org.facsim.Facsimile.CommonTest;

import org.facsim.Facsimile.Common.*;
import static org.junit.Assert.*;
import org.junit.*;

//=============================================================================
/**
<p>ResourceTest class.<p>

<p>This class is a JUnit test fixture for the {@link Resource} class.</p>
*/
//=============================================================================

public class ResourceTest
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test method for {@link Resource#format(String)}.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public final void testFormatString ()
    {

/*
Retrieve the test message and ensure that it matches our expectations without
error.
*/

        assertEquals (Resource.format ("testMessage"), //$NON-NLS-1$
        "Test message."); //$NON-NLS-1$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test method for {@link Resource#format(String,Object[])}.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public final void testFormatStringObjectArray ()
    {

/*
Retrieve the test compound message and format it with a specified argument then
test the result.
*/

        Object [] arguments = new Object []
        {
            "argument" //$NON-NLS-1$
        };
        assertEquals (Resource.format ("testCompoundMessage", //$NON-NLS-1$
        arguments), "Test compound message: argument."); //$NON-NLS-1$
    }
}
