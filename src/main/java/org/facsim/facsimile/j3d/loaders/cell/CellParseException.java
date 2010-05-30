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

Java source file belonging to the org.facsim.facsimile.j3d.loaders.cell package.
*/
//=============================================================================

package org.facsim.facsimile.j3d.loaders.cell;

import org.facsim.facsimile.io.TextReader;
import org.facsim.facsimile.util.PackagePrivate;
import org.facsim.facsimile.util.Resource;
import net.jcip.annotations.Immutable;
import com.sun.j3d.loaders.ParsingErrorException;

//=============================================================================
/**
<p>Exception indicating that an error occurred while parsing a <em>cell</em>
formatted stream.</p>

<p>This exception is thrown by the {@linkplain CellLoader} class if the
contents of an <em><a href="http://www.automod.com/">AutoMod</a>
cell</em>-formatted stream could not be parsed for whatever reason.</p>

<p>If the stream is not formatted as <em>AutoMod cell</em> data, then
{@linkplain CellFormatException} will be thrown instead.</p>

@see CellLoader
@see CellFormatException
*/
//=============================================================================

@Immutable
final class CellParseException
extends ParsingErrorException
{

/**
<p>Serialization version UID.</p>
*/
    
    private static final long serialVersionUID = 1L;

/**
<p>Reader in which the parse error was detected.  If this value is null, then
the reader failed during construction.</p>
*/

    private final TextReader reader;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Create a new <em><a href="http://www.automod.com/">AutoMod</a> cell</em>
parse error.</p>

@param reader Reader that detected the parse error.  This may be null if the
failure occurred during construction of the reader.

@param cause Exception that is the underlying cause of the parse failure.  This
can be null if no such exception is available.
 */
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    CellParseException (TextReader reader, Throwable cause)
    {

/*
Construct the base class.
*/

        super ();
        initCause (cause);

/*
Store the reader for later use.
*/

        this.reader = reader;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Throwable#getMessage()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final String getMessage ()
    {

/*
If the reader supplied is non-null, then report the line and column number at
which the exception occurred using the first form of the error message.
*/

        if (this.reader != null)
        {
            return Resource.format ("j3d.loaders.cell.cellParse1",
            Integer.valueOf (this.reader.getLine ()), Integer.valueOf
            (this.reader.getColumn ()));
        }

/*
Otherwise, report the exception using the second form.
*/

        return Resource.format ("j3d.loaders.cell.cellParse2");
    }
}
