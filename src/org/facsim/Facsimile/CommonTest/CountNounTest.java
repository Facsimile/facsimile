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

Java source file for the CountNounTest class, and associated elements, that are
integral members of the org.facsim.Facsimile.CommonTest package.
===============================================================================
*/
package org.facsim.Facsimile.CommonTest;

import org.facsim.Facsimile.Common.*;
import static org.junit.Assert.*;
import org.junit.Test;

//=============================================================================
/**
 <p>JUnit test fixture for the {@link CountNoun} class.</p>
 */
//=============================================================================

public class CountNounTest
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #1.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction1 ()
    {
        new SomeCountNoun (null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #2.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction2 ()
    {
        new SomeCountNoun (""); //$NON-NLS-1$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #3.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction3 ()
    {
        new SomeCountNoun (" "); //$NON-NLS-1$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #4.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction4 ()
    {
        new SomeCountNoun (null, "mice"); //$NON-NLS-1$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #5.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction5 ()
    {
        new SomeCountNoun ("", "mice"); //$NON-NLS-1$ //$NON-NLS-2$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #6.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction6 ()
    {
        new SomeCountNoun (" ", "mice"); //$NON-NLS-1$ //$NON-NLS-2$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #7.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction7 ()
    {
        new SomeCountNoun ("mouse", null); //$NON-NLS-1$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #8.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction8 ()
    {
        new SomeCountNoun ("mouse", ""); //$NON-NLS-1$ //$NON-NLS-2$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for exception on illegal name construction, #9.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = EmptyStringArgumentException.class)
    public void invalidConstruction9 ()
    {
        new SomeCountNoun ("mouse", " "); //$NON-NLS-1$ //$NON-NLS-2$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for correct common singular/plural name construction.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void commonNameConstruction ()
    {
        SomeCountNoun countNoun = new SomeCountNoun ("sheep"); //$NON-NLS-1$
        assertEquals (countNoun.getSingularName (), "sheep"); //$NON-NLS-1$
        assertEquals (countNoun.getPluralName (), "sheep"); //$NON-NLS-1$
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests for correct specific singular/plural name construction.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void differentNameConstruction ()
    {
        SomeCountNoun countNoun = new SomeCountNoun ("mouse", //$NON-NLS-1$
        "mice"); //$NON-NLS-1$
        assertEquals (countNoun.getSingularName (), "mouse"); //$NON-NLS-1$
        assertEquals (countNoun.getPluralName (), "mice"); //$NON-NLS-1$
    }
}
