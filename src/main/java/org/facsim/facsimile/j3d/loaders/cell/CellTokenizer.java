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

Java source file belonging to the org.facsim.facsimile.j3d.loaders.cell
package.
*/
//=============================================================================

package org.facsim.facsimile.j3d.loaders.cell;

import java.io.EOFException;
import java.io.IOException;
import net.jcip.annotations.Immutable;
import org.facsim.facsimile.io.Tokenizer;
import org.facsim.facsimile.util.PackagePrivate;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.Loader;
import com.sun.j3d.loaders.ParsingErrorException;

//=============================================================================
/**
<p>Encapsulates the {@linkplain Tokenizer} class in order to convert tokenizer
exceptions into the corresponding <em><a
href="http://java3d.dev.java.net/">Java3D</a></em> {@linkplain Loader}
exceptions.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
class CellTokenizer
{

/**
<p>Associated cell loader instance.</p>
*/

    private final CellLoader loader;

/**
<p>Tokenizer instance employed by this class.</p>
*/

    private final Tokenizer tokenizer;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tokenizer constructor.</p>

@param cellLoader Cell loader associated with this tokenized stream.

@param tokenizer Tokenizer to be encapsulated.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    CellTokenizer (CellLoader cellLoader, Tokenizer tokenizer)
    {
        assert cellLoader != null;
        assert tokenizer != null;
        this.loader = cellLoader;
        this.tokenizer = tokenizer;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve cell loader associated with this tokenized stream.</p>

@return loader.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final CellLoader getLoader ()
    {
        return this.loader;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Read a field as a string from the tokenizer, converting any I/O exceptions
into {@linkplain ParsingErrorException} or {@linkplain
IncorrectFormatException} exceptions.  This simplifies processing of
<em>cell</em> data streams.</p>

@return String containing the next field read by the tokenizer.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final String readField ()
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Return the next field read, looking out for any exceptions that might arise.
*/

        try
        {
            return this.tokenizer.readField ();
        }

/*
An end-of-file exception will be treated as an incorrect format exception.
*/

        catch (EOFException eofException)
        {
            IncorrectFormatException incorrectFormat = new
            IncorrectFormatException ();
            incorrectFormat.initCause (eofException);
            throw incorrectFormat;
        }

/*
Any other I/O exception that arises will be treated as a parsing error
exception.
*/

        catch (IOException ioException)
        {
            ParsingErrorException parseError = new ParsingErrorException ();
            parseError.initCause (ioException);
            throw parseError;
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Read a field as a boolean from the tokenizer, converting any I/O exceptions
into {@linkplain ParsingErrorException} or {@linkplain
IncorrectFormatException} exceptions.  Data errors are converted into
IncorrectFormatExceptions.  This simplifies processing of <em>cell</em> data
streams.</p>

@return Value of next field read by the tokenizer as a boolean.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final boolean readBooleanField ()
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Retrieve the next field as a string.
*/

        String field = readField ();

/*
If the value is "0", return false.
*/

        if (field == "0")
        {
            return false;
        }

/*
If the value is "1", return true.
*/

        if (field == "1")
        {
            return true;
        }

/*
If we make it this far, then we have an invalid field.
*/

        throw new IncorrectFormatException ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Read a field as a byte from the tokenizer, converting any I/O exceptions
into {@linkplain ParsingErrorException} or {@linkplain
IncorrectFormatException} exceptions.  {@linkplain NumberFormatException} are
converted into IncorrectFormatExceptions.  This simplifies processing of
<em>cell</em> data streams.</p>

@return Value of next field read by the tokenizer as a byte.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final byte readByteField ()
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Retrieve the next field and convert its type, looking out for any exceptions
that might arise.
*/

        try
        {
            return Byte.parseByte (readField ());
        }

/*
A number format exception will be treated as an incorrect format exception.
*/

        catch (NumberFormatException formatException)
        {
            IncorrectFormatException incorrectFormat = new
            IncorrectFormatException ();
            incorrectFormat.initCause (formatException);
            throw incorrectFormat;
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Read a field as a short integer from the tokenizer, converting any I/O
exceptions into {@linkplain ParsingErrorException} or {@linkplain
IncorrectFormatException} exceptions.  {@linkplain NumberFormatException} are
converted into IncorrectFormatExceptions.  This simplifies processing of
<em>cell</em> data streams.</p>

@return Value of next field read by the tokenizer as a short integer.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final short readShortField ()
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Retrieve the next field and convert its type, looking out for any exceptions
that might arise.
*/

        try
        {
            return Short.parseShort (readField ());
        }

/*
A number format exception will be treated as an incorrect format exception.
*/

        catch (NumberFormatException formatException)
        {
            IncorrectFormatException incorrectFormat = new
            IncorrectFormatException ();
            incorrectFormat.initCause (formatException);
            throw incorrectFormat;
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Read a field as a double from the tokenizer, converting any I/O exceptions
into {@linkplain ParsingErrorException} or {@linkplain
IncorrectFormatException} exceptions.  {@linkplain NumberFormatException} are
converted into IncorrectFormatExceptions.  This simplifies processing of
<em>cell</em> data streams.</p>

@return Value of next field read by the tokenizer as a double.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final double readDoubleField ()
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Retrieve the next field and convert its type, looking out for any exceptions
that might arise.
*/

        try
        {
            return Double.parseDouble (readField ());
        }

/*
A number format exception will be treated as an incorrect format exception.
*/

        catch (NumberFormatException formatException)
        {
            IncorrectFormatException incorrectFormat = new
            IncorrectFormatException ();
            incorrectFormat.initCause (formatException);
            throw incorrectFormat;
        }
    }
}
