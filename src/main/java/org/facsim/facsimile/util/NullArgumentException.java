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

Java source file belonging to the org.facsim.facsimile.util package.
*/
//=============================================================================

package org.facsim.facsimile.util;

import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p>Exception signaling that an object passed as a procedure argument is
null.</p>

<p>This exception should be thrown in preference to {@linkplain
NullPointerException} when reporting null arguments, since it provides
additional information that may be of use to the programmer.</p>
*/
//=============================================================================

@Immutable
public class NullArgumentException
extends NullPointerException
{

/**
<p>Class serialization serial number.</p>
*/
    
    private static final long serialVersionUID = 1L;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Argument name constructor.</p>

<p>Constructs the exception passing the name of the argument that caused the
exception with some additional information.</p>

@param argName Name of argument found to be null.  This argument should not be
an empty or null string.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public NullArgumentException (String argName)
    {

/*
Construct the parent class with an internationalized error message.
*/

        super (Resource.format ("utilNullArgument", argName));

/*
Verify that we have a valid argument name.

Note: This has to be done after the base class is constructed, because that's
the way it is.
*/

        assert argName != null && !argName.isEmpty ();
    }
}
