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

Java source file belonging to the org.facsim.facsimile.io package.
*/
//=============================================================================

package org.facsim.facsimile.io;

import org.facsim.facsimile.util.PackagePrivate;
import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p>Helper class supporting stream read operations.</p>

<p>A <em>field</em> instance consists of a string containing data read from a
stream and a flag indicating whether the field was terminated by a regular
field delimiter or, if they are being treated differently, an
<em>end-of-line</em> sequence</em> (sometimes also referred to as a
<em>line termination sequence</em>).</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
final class Field
{

/**
<p>Field read from a stream.</p>

<p>String storing the data contained within the field, excluding the field
termination characters.<p>

<p>This value may be an empty string, but cannot be <strong>null</strong>.
*/

    private final String field;

/**
<p>Flag indicating how {@linkplain #field} was terminated in the input
stream.</p>
*/

    private final FieldTerminator terminator;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Field constructor.</p>

@param fieldRead Field read from an input stream.

@param fieldTerminator Enumeration indicating how the field was terminated in
the input stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    Field (StringBuffer fieldRead, FieldTerminator fieldTerminator)
    {
        assert fieldRead != null;
        assert fieldTerminator != null;
        this.field = fieldRead.toString ();
        this.terminator = fieldTerminator;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve the field's data.</p>

@return Field data read, in the form of a character string.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final String getField ()
    {
        assert this.field != null;
        return this.field;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve description detailing how the field was terminated.</p>

@return Field terminator instance describing how field was terminated.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final FieldTerminator getTerminator ()
    {
        assert this.terminator != null;
        return this.terminator;
    }
}
