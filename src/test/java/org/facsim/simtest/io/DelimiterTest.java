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

Java source file belonging to the org.facsim.simtest.io package.
*/
//=============================================================================

package org.facsim.simtest.io;

import org.facsim.facsimile.io.BadDelimiterException;
import org.facsim.facsimile.io.Delimiter;
import org.junit.Test;

//=============================================================================
/**
<p>Test fixture for the {@linkplain Delimiter} class.</p>
*/
//=============================================================================

public final class DelimiterTest
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test null delimiter construction.</p>

<p>Tests that a {@linkplain NullPointerException} is thrown when the passed
delimiter string is null.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullDelimiterConstruction ()
    {
        new Delimiter (null, false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test newline delimiter construction 1.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains a newline character.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void newlineDelimiterConstruction1 ()
    {
        new Delimiter ("\n \t", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test newline delimiter construction 2.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains a newline character.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void newlineDelimiterConstruction2 ()
    {
        new Delimiter (" \n\t", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test newline delimiter construction 3.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains a newline character.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void newlineDelimiterConstruction3 ()
    {
        new Delimiter (" \t\n", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test carriage return delimiter construction 1.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains a carriage return character.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void carriageReturnDelimiterConstruction1 ()
    {
        new Delimiter ("\r \t", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test carriage return delimiter construction 2.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains a carriage return character.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void carriageReturnDelimiterConstruction2 ()
    {
        new Delimiter (" \r\t", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test carriage return delimiter construction 3.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains a carriage return character.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void carriageReturnDelimiterConstruction3 ()
    {
        new Delimiter (" \t\r", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test duplicate valid delimiter character construction 1.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains the same valid character multiple times.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void duplicateDelimiterConstruction1 ()
    {
        new Delimiter ("\t\t", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test duplicate valid delimiter character construction 2.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains the same valid character multiple times.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void dupplicateDelimiterConstruction2 ()
    {
        new Delimiter (" \t\t", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test duplicate valid delimiter character construction 3.</p>

<p>Tests that a bad delimiter exception is thrown when the passed delimiter
string contains the same valid character multiple times.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = BadDelimiterException.class)
    public void dupplicateDelimiterConstruction3 ()
    {
        new Delimiter ("\t \t", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests valid empty delimiter construction.</p>

<p>Tests that a constructor containing an empty delimiter string is accepted
by the Delimiter constructor.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void emptyDelimiterConstruction ()
    {
        new Delimiter ("", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests valid delimiter construction.</p>

<p>Tests that a constructor containing numerous valid delimiters is accepted
by the Delimiter constructor.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void validDelimiterConstruction ()
    {
        new Delimiter ("\t ,|;", false, false);
    }
}
