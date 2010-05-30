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

Java source file belonging to the org.facsim.facsimile.engine package.
*/
//=============================================================================

package org.facsim.facsimile.engine;

import net.jcip.annotations.Immutable;
import org.facsim.facsimile.util.PackagePrivate;
import org.facsim.facsimile.util.Resource;
import org.facsim.facsimile.util.TestCode;

//=============================================================================
/**
<p>Signals that the simulation has run out of events.</p>

<p>In terminating simulations, this exception signals that the simulation has
run to completion.  In non-terminating simulations, this exception represents a
problem, since there has been a failure to propagate further events.</p>
*/
//=============================================================================

@Immutable
public final class OutOfEventsException
extends Exception
{

/**
<p>Serialization version UID.</p>
*/

    private static final long serialVersionUID = 1L;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Create an out-of-events exception and report its error message.</p>

<p>This function is intended for use by testing code only and should not be
called either within the library or from within third-party simulation
models.</p>

@return The error message reported by the exception's {@linkplain
#getMessage()} method.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @TestCode
    public static String getTestMessage ()
    {
        OutOfEventsException e1 = new OutOfEventsException ();
        return e1.getMessage ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Constructor.</p>

<p>This constructor has default access to prevent the exception from being
thrown by third-party code.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    OutOfEventsException ()
    {
        super ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Throwable#getMessage()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public String getMessage ()
    {
        return Resource.format ("engine.outOfEvents");
    }
}
