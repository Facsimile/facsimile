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

Java source file for the CounterIncrementException class, and associated
elements, that are integral members of the org.facsim.Facsimile.Common package.
===============================================================================
*/

package org.facsim.Facsimile.Common;

//=============================================================================
/**
<p>Exception thrown when a counter is incremented above its maximum
capacity.</p>
*/
//=============================================================================

public final class CounterIncrementException
extends OverflowException
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
    <li>The limit of the associated counter.</li>
</ol>
*/

    private final Object [] counterData;

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

<p>Passes the counter's limit to be formatted as part of the exception's
message.</p>

@param limit A {@link Integer} holding the limit of the associated counter.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public CounterIncrementException (Integer limit)
    {

/*
Construct our parent.
*/

        super ();

/*
Argument integrity assertions.
*/

        assert limit.intValue () >= 0;

/*
Store these arguments for later use.
*/

        this.counterData = new Object []
        {
            limit,
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

        return Resource.format ("counterIncrementOverflow", //$NON-NLS-1$
        this.counterData);
    }
}
