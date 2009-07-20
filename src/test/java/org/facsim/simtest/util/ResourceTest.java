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

Java source file belonging to the org.facsim.simtest.util package.
*/
//=============================================================================


package org.facsim.simtest.util;

import java.util.MissingResourceException;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;
import org.facsim.facsimile.util.Resource;

//=============================================================================
/**
Test fixture for the {@link org.facsim.facsimile.util.Resource Resource} class.
*/
//=============================================================================

public class ResourceTest
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test null resource formatting fails.

Tests that a {@link java.lang.NullPointerException NullPointerException} is
thrown when a null key is passed to the {@link
org.facsim.facsimile.util.Resource#format(java.lang.String,java.lang.Object[])}
function.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void formatNullKeyFailure ()
    {
        Resource.format (null, (Object []) null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test missing resource formatting fails.

Tests that a {@link java.util.MissingResourceException} is thrown when a
non-existent key is passed to the {@link
org.facsim.facsimile.util.Resource#format(java.lang.String,java.lang.Object[])}
function.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = MissingResourceException.class)
    public void formatMissingResourceFailure ()
    {
        Resource.format ("testNoSuchKey", (Object []) null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test that attempt to format a non-string resource fails.

TODO: Complete this test.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Ignore
    @Test (expected = ClassCastException.class)
    public void formatNonStringResourceFailure ()
    {
        Resource.format ("testNotAStringResource", (Object []) null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test basic operation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void formatBasicOperation ()
    {

/*
Test the basic, zero argument version.
*/

        assertEquals (Resource.format ("testMessage", (Object []) null),
        "Test message");

/*
Test the single argument version.
*/

        assertEquals (Resource.format ("testCompoundMessage0", "zero"),
        "Test compound message 0: 0=zero");

/*
Test the dual argument version.
*/

        assertEquals (Resource.format ("testCompoundMessage1", "zero", "one"),
        "Test compound message 1: 0=zero, 1=one");

/*
Test the triple argument version.
*/

        assertEquals (Resource.format ("testCompoundMessage2", "zero", "one",
        "two"), "Test compound message 2: 0=zero, 1=one, 2=two");
    }
}
