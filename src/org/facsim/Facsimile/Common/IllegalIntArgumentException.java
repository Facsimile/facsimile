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

Java source file for the IllegalIntArgumentException class, and associated
elements, that are integral members of the org.facsim.Facsimile.Common package.
===============================================================================
*/

package org.facsim.Facsimile.Common;

//=============================================================================
/**
<p>Exception thrown when an integer argument is outside its valid range.</p>
*/
//=============================================================================

public final class IllegalIntArgumentException
extends IllegalArgumentException
{

/**
<p>Serialization version UID.</p>

<p>This is used to denote the schema or version of the class's serialized data
so that changes can be recognised during de-serialization.</p>
*/

    private static final long serialVersionUID;

/**
<p>Object array.</p>

<p>This array is initialised by the constructor to contain the following
values:</p>

<ol>
    <li>The name of the method argument whose value is out of range.</li>
    <li>The minimum value of the argument's permitted range.</li>
    <li>The maximum value of the argument's permitted range.</li>
    <li>The value of the method argument that caused the exception.</li>
</ol>
*/

    private final Object [] argumentData;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Static constructor.<p>

<p>Initialise static class members.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {

/*
Schema number for serialisation/de-serialisation purposes.
*/

        serialVersionUID = 0L;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Constructor.</p>

<p>Passes the relevant integer parameter information to be formatted as part of
the exception's message.</p>

@param argumentName A {@link String} holding the name of the invalid argument.

@param argumentMinimum An {@link Integer} holding he minimum valid value for
this argument.

@param argumentMaximum An {@link Integer} holding the maximum valid value for
this argument.

@param argumentValue An {@link Integer} holding the actual, invalid value of
the argument.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public IllegalIntArgumentException (String argumentName, Integer
    argumentMinimum, Integer argumentMaximum, Integer argumentValue)
    {

/*
Construct our parent.
*/

        super ();

/*
Argument integrity assertions.
*/

        assert argumentName != null && argumentName != ""; //$NON-NLS-1$
        assert argumentMinimum.intValue () <= argumentMaximum.intValue ();
        assert argumentValue.intValue () < argumentMinimum.intValue () ||
        argumentValue.intValue () > argumentMaximum.intValue ();

/*
Store these arguments for later use.
*/

        this.argumentData = new Object []
        {
            argumentName,
            argumentMinimum,
            argumentMaximum,
            argumentValue,
        };
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
@see java.lang.Throwable#getMessage()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String getMessage ()
    {

/*
Retrieve the compound message, format it and return it to the caller.
*/

        return Resource.format ("intArgumentOutOfRange", //$NON-NLS-1$
        this.argumentData);
    }
}
