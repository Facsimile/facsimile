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

package org.facsim.facsimile.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.StreamTokenizer;
import org.facsim.facsimile.util.NullArgumentException;
import net.jcip.annotations.ThreadSafe;

//=============================================================================
/**
<p>Helper class to assist with the processing of text streams.</p>

<p>This class is intended to be more robust and easier to use than the
{@linkplain StreamTokenizer} class.</p>
*/
//=============================================================================

@ThreadSafe
public final class Tokenizer
{

/**
<p>Default buffer size to be used when marking the reader stream.</p>
*/

    private static final int bufferSize = 4096;

/**
<p>Text stream reader to be processed.</p>

<p>The stream is processed using a {@linkplain TextReader} instance to assist
with the processing of delimiters and <em>end-of-line</em> sequences.</p>
*/

    private final TextReader reader;

/**
<p>Delimiter to separate fields in the text stream.</p>
*/

    private final Delimiter delimiter;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Reader constructor.</p>

<p>Construct a new tokenizer from a {@linkplain TextReader} instance, to be
parsed using the {@linkplain Delimiter} instance.</p>

<p>Note that the same reader instance can be associated with more than one
{@linkplain Tokenizer} instance.  In the event that a reader requires multiple
delimiter types to separate its fields, the reader can utilize different
tokenizers to assist with its processing.</p>

@param reader Reader to be tokenized.  This value cannot be null.

@param delimiter Delimiter to be used to separate tokens.  This value cannot be
null.

@throws NullPointerException If reader or delimiter is null.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Tokenizer (TextReader reader, Delimiter delimiter)
    {

/*
If the supplied reader is null, then throw the exception.  Otherwise store it
for future processing.
*/

        if (reader == null)
        {
            throw new NullArgumentException ("reader");
        }
        this.reader = reader;

/*
If the supplied delimiter is null, then throw the exception.  Otherwise store
it for future processing.
*/

        if (delimiter == null)
        {
            throw new NullArgumentException ("delimiter");
        }
        this.delimiter = delimiter;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from stream using indicated delimiter.</p>

<p>Reads from the current stream position until the specified delimiter is
encountered.</p>

<p>Note that this procedure marks the associated reader before reading from it.
This will replace any prior marked location; do not call this function unless
you are certain that you do not wish to reset to the previously marked
location.</p>

@return Next field read from the stream as a string.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public synchronized final String readField ()
    throws EOFException, EOLAcknowledgmentException, IOException
    {

/*
Verify that we're ready to read from the stream.
*/

        this.reader.verifyReaderState ();

/*
Mark the reader in case we need to come back to this point later on (which can
be done explicitly by calling the reset() function).
*/

        assert this.reader.markSupported ();
        this.reader.mark (bufferSize);

/*
Retrieve the field from the reader, using our delimiter to identify when the
field has ened.  Any I/O errors will result in an IOException being thrown.
*/

        return this.reader.readField (this.delimiter);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Reset the associated reader to is last marked position.</p>

<p>Each time a tokenizer instance reads a field from the reader, the tokenizer
first marks the reader.  In this way, if an exception occurs while the field is
being read, the reader can be reset to its prior position.</p>

@throws IOException If the stream has not been marked, which is possible if no
fields have yet been read using a tokenizer instance, or if more characters
where read since the start of the last field read operation that the buffer
size allows for.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final void reset ()
    throws IOException
    {
        this.reader.reset ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the stream as a boolean value using the specified
delimiter.</p>

<p>An exception will result if the next field is not a valid boolean value,
which is to say, the values "TRUE" (or "true") and "FALSE" (or "false").  If
data is in the form "1" (meaning true) or "0" (meaning false), then use
{@linkplain #readBinaryField()} instead.</p>

<p>Note that this procedure marks the associated reader before reading from it.
This will replace any prior marked location; do not call this function unless
you are certain that you do not wish to reset to the previously marked
location.</p>

@return Boolean value of the field read.

@throws FieldFormatException If the field is not formatted as boolean data.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final boolean readBooleanField ()
    throws FieldFormatException, EOFException, EOLAcknowledgmentException,
    IOException
    {

/*
Retrieve the field as a string, convert to a boolean and return.  If the field
cannot be converted into a boolean, throw the field format exception (a type of
IOException).
*/

        String value = readField ();
        if (value == "TRUE" || value == "true")
        {
            return true;
        }
        if (value == "FALSE" || value == "false")
        {
            return false;
        }
        throw new FieldFormatException (null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the stream as a binary value using the specified
delimiter.</p>

<p>An exception will result if the next field is not a valid binary value,
which is to say, the values "1" (true) and "0" (false).  If data is in the form
"TRUE" (or "true") or "FALSE" (or "false"), then use {@linkplain
#readBooleanField()} instead.</p>

<p>Note that this procedure marks the associated reader before reading from it.
This will replace any prior marked location; do not call this function unless
you are certain that you do not wish to reset to the previously marked
location.</p>

@return Boolean value of the field read.

@throws FieldFormatException If the field is not formatted as binary data.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final boolean readBinaryField ()
    throws FieldFormatException, EOFException, EOLAcknowledgmentException,
    IOException
    {

/*
Retrieve the field as a string, convert to a boolean and return.  If the field
cannot be converted into a boolean, throw the field format exception (a type of
IOException).
*/

        String value = readField ();
        if (value == "1")
        {
            return true;
        }
        if (value == "0")
        {
            return false;
        }
        throw new FieldFormatException (null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the stream as a byte value using the specified
delimiter.</p>

<p>An exception will result if the next field is not a valid byte value.  The
field is not unread in this event, so any opportunity to re-read the field in a
different format will be lost.  Unless the next field is guaranteed to be a
byte, you should consider using {@linkplain #readField ()} instead.</p>

<p>Note that this procedure marks the associated reader before reading from it.
This will replace any prior marked location; do not call this function unless
you are certain that you do not wish to reset to the previously marked
location.</p>

@return Byte value of the field read.

@throws FieldFormatException If the field is not formatted as boolean data.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final byte readByteField ()
    throws FieldFormatException, EOFException, EOLAcknowledgmentException,
    IOException
    {

/*
Retrieve the field as a string, convert to a byte and return.  If the field
cannot be converted into a byte, throw the field format exception (a type of
IOException).
*/

        try
        {
            return Byte.parseByte (readField ());
        }
        catch (NumberFormatException numberException)
        {
            throw new FieldFormatException (numberException);
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the stream as a short value using the specified
delimiter.</p>

<p>An exception will result if the next field is not a valid short value.  The
field is not unread in this event, so any opportunity to re-read the field in a
different format will be lost.  Unless the next field is guaranteed to be a
short, you should consider using {@linkplain #readField ()} instead.</p>

<p>Note that this procedure marks the associated reader before reading from it.
This will replace any prior marked location; do not call this function unless
you are certain that you do not wish to reset to the previously marked
location.</p>

@return Short value of the field read.

@throws FieldFormatException If the field is not formatted as boolean data.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final short readShortField ()
    throws FieldFormatException, EOFException, EOLAcknowledgmentException,
    IOException
    {

/*
Retrieve the field as a string, convert to a short integer and return.  If the
field cannot be converted into a short, throw the field format exception (a
type of IOException).
*/

        try
        {
            return Short.parseShort (readField ());
        }
        catch (NumberFormatException numberException)
        {
            throw new FieldFormatException (numberException);
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the stream as an integer value using the specified
delimiter.</p>

<p>An exception will result if the next field is not a valid integer value.
The field is not unread in this event, so any opportunity to re-read the field
in a different format will be lost.  Unless the next field is guaranteed to be
an integer, you should consider using {@linkplain #readField ()} instead.</p>

<p>Note that this procedure marks the associated reader before reading from it.
This will replace any prior marked location; do not call this function unless
you are certain that you do not wish to reset to the previously marked
location.</p>

@return Integer value of the field read.

@throws FieldFormatException If the field is not formatted as boolean data.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final int readIntField ()
    throws FieldFormatException, EOFException, EOLAcknowledgmentException,
    IOException
    {

/*
Retrieve the field as a string, convert to an integer and return.  If the field
cannot be converted into an integer, throw the field format exception (a type
of IOException).
*/

        try
        {
            return Integer.parseInt (readField ());
        }
        catch (NumberFormatException numberException)
        {
            throw new FieldFormatException (numberException);
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the stream as a long value using the specified
delimiter.</p>

<p>An exception will result if the next field is not a valid long value.  The
field is not unread in this event, so any opportunity to re-read the field in a
different format will be lost.  Unless the next field is guaranteed to be a
long, you should consider using {@linkplain #readField ()} instead.</p>

<p>Note that this procedure marks the associated reader before reading from it.
This will replace any prior marked location; do not call this function unless
you are certain that you do not wish to reset to the previously marked
location.</p>

@return Long value of the field read.

@throws FieldFormatException If the field is not formatted as boolean data.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final long readLongField ()
    throws FieldFormatException, EOFException, EOLAcknowledgmentException,
    IOException
    {

/*
Retrieve the field as a string, convert to a long integer and return.  If the
field cannot be converted into a long, throw the field format exception (a type
of IOException).
*/

        try
        {
            return Long.parseLong (readField ());
        }
        catch (NumberFormatException numberException)
        {
            throw new FieldFormatException (numberException);
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the stream as a double value using the specified
delimiter.</p>

<p>An exception will result if the next field is not a valid double value.
The field is not unread in this event, so any opportunity to re-read the field
in a different format will be lost.  Unless the next field is guaranteed to be
a double, you should consider using {@linkplain #readField ()} instead.</p>

@return Double value of the field read.

@throws FieldFormatException If the field is not formatted as boolean data.

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final double readDoubleField ()
    throws FieldFormatException, EOFException, EOLAcknowledgmentException,
    IOException
    {

/*
Retrieve the field as a string, convert to a double and return.  If the field
cannot be converted into a double, throw the field format exception (a type of
IOException).
*/

        try
        {
            return Double.parseDouble (readField ());
        }
        catch (NumberFormatException numberException)
        {
            throw new FieldFormatException (numberException);
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Acknowledge an <em>end-of-line</em> sequence.</p>

@throws EOLAcknowledgmentException If the previous field read from the stream
was not terminated by an <em>end-of-line</em> sequence, or if this delimiter
treats <em>end-of-line</em> sequences as a regular delimiter.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public synchronized final void acknowledgeEOL ()
    throws EOLAcknowledgmentException
    {
        this.reader.acknowledgeEOL ();
    }
}
