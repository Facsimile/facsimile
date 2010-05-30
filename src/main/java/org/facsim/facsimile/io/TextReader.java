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

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.facsim.facsimile.util.PackagePrivate;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

//=============================================================================
/**
<p>Text reader.</p>

<p>Adapted buffered reader, designed to provide the following benefits:</p>

<ul>
  <li>Simplifies the processing of <em>end-of-line</em> sequences by converting
  all such sequences to the <em>linefeed</em> sequence employed by
  <em>UNIX</em> and <em>UNIX</em>-like operating systems (including
  <em>Linux</em>, <em>FreeBSD</em>, <em>NetBSD</em>, etc.).  This relieves code
  that uses this reader from the chore of parsing each different platform's
  <em>end-of-line</em> sequences.</li>
  <li>Tracks current line and column number while reading from the stream, to
  assist with debugging file format errors.</li>
</ul>
*/
//=============================================================================

@ThreadSafe
public class TextReader
extends BufferedReader
{

/**
<p>Current line number.</p>

<p>Line numbering starts at 1.  The line number is incremented each time an
<em>end-of-line</em> sequence is read.</p>

<p><strong>Important</strong>: Instances of this class created from other
readers cannot accurately determine the current line number.</p>
*/

    @GuardedBy ("this")
    private int line;

/**
<p>Marked line number.</p>

<p>The value of {@linkplain #line} when the reader was last marked.</p>
*/

    @GuardedBy ("this")
    private int markedLine;

/**
<p>Current column number.</p>

<p>Column numbering starts at 0.  The column number is incremented each time a
non-<em>end-of-line</em> sequence character is read, and is reset to 0 after
processing an <em>end-of-line</em> sequence.</p>

<p>This value is also used to keep track of the next character to be read from
the input {@linkplain #lineBuffer}.</p>

<p><strong>Important</strong>: Instances of this class created from other
readers cannot accurately determine the current column number.</p>
*/

    @GuardedBy ("this")
    private int column;

/**
<p>Marked column number.</p>

<p>The value of {@linkplain #column} when the reader was last marked.</p>
*/

    @GuardedBy ("this")
    private int markedColumn;

/**
<p>Current line of data being processed.</p>

<p>This buffer stores the current line of data being processed.  It is updated
when processing passes the <em>end-of-line</em> sequence.</p>
*/

    @GuardedBy ("this")
    private String lineBuffer;

/**
<p>Marked line buffer.</p>

<p>Contents of {@linkplain #lineBuffer} when the reader was last marked.</p>
*/

    @GuardedBy ("this")
    private String markedLineBuffer;

/**
<p>Terminator of the last field read through a {@linkplain Tokenizer}.</p>

<p>This field is accessed through a private interface by the Tokenizer class.
It allows different Tokenizer instances to synchronize their behavior.  It
serves no purpose unless this reader is being processed by a Tokenizer.</p>
*/

    @GuardedBy ("this")
    private FieldTerminator lastFieldTerminator;

/**
<p>Marked last field terminator.</p>

<p>Contents of {@linkplain #lastFieldTerminator} when the reader was last
marked.</p>
*/

    @GuardedBy ("this")
    private FieldTerminator markedLastFieldTerminator;

/**
<p>Flag indicating whether the any previous <em>end-of-line</em> sequence was
acknowledged.</p>

<p>This field is accessed through a private interface by the Tokenizer class.
It allows different Tokenizer instances to synchronize their behavior.  It
serves no purpose unless this reader is being processed by a Tokenizer.</p>
*/

    @GuardedBy ("this")
    private boolean eolAcknowledged;

/**
<p>Marked EOL acknowledged flag.</p>

<p>Contents of {@linkplain #eolAcknowledged} when the reader was last
marked.</p>
*/

    @GuardedBy ("this")
    private boolean markedEOLAcknowledged;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>File constructor.</p>

@param file File instance from which the reader will be constructed.

@throws NullPointerException If file is null.

@throws FileNotFoundException If the file cannot be found, identifies a
directory instead of a file, or cannot be opened for reading.

@throws IOException If an I/O error occurs while refreshing the internal buffer
initially.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public TextReader (File file)
    throws FileNotFoundException, IOException
    {

/*
Create a file reader to open and read the file.
*/

        this (new FileReader (file));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>File descriptor constructor.</p>

@param fileDescriptor File descriptor instance from which the reader will be
constructed.

@throws NullPointerException If fileDescriptor is null.

@throws IOException If an I/O error occurs while refreshing the internal buffer
initially.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public TextReader (FileDescriptor fileDescriptor)
    throws IOException
    {

/*
Create a file reader to open and read the file.
*/

        this (new FileReader (fileDescriptor));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>String constructor.</p>

@param data String from which data will be read by this reader.

@throws NullPointerException If data is null.

@throws IOException If an I/O error occurs while refreshing the internal buffer
initially.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public TextReader (String data)
    throws IOException
    {

/*
Create a file reader to open and read the file.
*/

        this (new StringReader (data));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Reader constructor.</p>

@param reader Reader instance that supplies this reader with data to be read.

@throws NullPointerException If reader is null.

@throws IOException If an I/O error occurs while refreshing the internal buffer
initially.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public TextReader (Reader reader)
    throws IOException
    {

/*
Initialize our base class with the reader instance provided.
*/

        super (reader);

/*
Initialize the line to zero - the line and column will be updated automatically
by the refreshBuffer procedure below.
*/

        this.line = 0;

/*
Initialize the terminator of the last field read to be a regular delimiter.
This is a reasonable starting point, since it does not require any special
processing.
*/

        this.lastFieldTerminator = FieldTerminator.FT_DELIMITER;

/*
Initialize the EOL acknowledgment flag to false.  This is adequate since it is
only significant if the last field terminator requires an EOL acknowledgment.
*/

        this.eolAcknowledged = false;

/*
Refresh the buffer by reading the first line of data from the file.
*/

        refreshBuffer ();

/*
Verify that the line and column numbers are valid.
*/

        assert this.line == 1;
        assert this.column == 0;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report number of line containing next character to be read.</p>

@return Number of line containing next character to be read.  Lines numbering
commences at 1 and proceeds upwards in increments of 1.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final int getLine ()
    {
        assert this.line > 0;
        return this.line;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report number of column containing next character to be read.</p>

@return Number of column containing next character to be read.  Column
numbering commences at 0 and proceeds upwards in increments of 1.  Each
<em>Unicode</em> character counts as 1 character, so characters encoded by two
or more <em>ASCII</em> characters in a <em>UTF-8</em> encoding count as a
single character.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final int getColumn ()
    {
        assert this.column >= 0;
        return this.column;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Refresh the internal line buffer.</p>

@throws IOException If an I/O error occurs while refreshing the buffer.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final synchronized void refreshBuffer ()
    throws IOException
    {

/*
Refresh the buffer by having our parent read the next line.  The buffer will be
null if the an end-of-file/end-of-stream condition is encountered.  An I/O
exception will be thrown if any other I/O errors occur.
*/

        this.lineBuffer = super.readLine ();

/*
As we have just read another line, update the line number and reset the column
number.
*/

        ++this.line;
        this.column = 0;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.BufferedReader#mark(int)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public final synchronized void mark (int readAheadLimit)
    throws IOException
    {

/*
Mark the current location in the data so that we can reset to this point later
on.  The bulk of the hard work is done by our parent class.
*/

        super.mark (readAheadLimit);
        this.markedLine = this.line;
        this.markedColumn = this.column;
        this.markedLineBuffer = this.lineBuffer;
        this.markedLastFieldTerminator = this.lastFieldTerminator;
        this.markedEOLAcknowledged = this.eolAcknowledged;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.BufferedReader#reset()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public final synchronized void reset ()
    throws IOException
    {

/*
Reset the current location in the data to the point marked previously by the
mark function.  If there was no previous mark, or if more data has been read
since the mark was created than could be stored in the read-ahead buffer, then
an exception will arise.  The bulk of the hard work is done by our parent class.
*/

        super.reset ();
        this.line = this.markedLine;
        this.column = this.markedColumn;
        this.lineBuffer = this.markedLineBuffer;
        this.lastFieldTerminator = this.markedLastFieldTerminator;
        this.eolAcknowledged = this.markedEOLAcknowledged;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Peek at the next character in the stream.</p>

<p>Reports the character that {@linkplain TextReader#read()} would return if
called immediately afterwards; the stream is unaffected by peeking at it.</p>

@return The character read, as an integer in the range 0 to 65535
(0x00-0xffff), or -1 if the end of the stream has been reached
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final int peek ()
    {

/*
If we encounter a null pointer exception during execution, it will be due to
having a null line buffer - itself the result of encountering the end of the
stream when refreshing the buffer.  Rather than test this explicitly, with the
associated overhead of an additional test when reading each individual
character, we instead trap the exception and handle it below.
*/

        try
        {

/*
If we've passed the end of the input buffer, then return the newline
character's code.
*/

            assert this.column >= 0 && this.column <= this.lineBuffer.length
            ();
            if (this.column == this.lineBuffer.length ())
            {
                return '\n';
            }
        }

/*
Handle the null pointer exception arising out of a null line buffer, and return
-1 - indicating that the end of the stream has been reached.
*/

        catch (NullPointerException e)
        {
            return -1;
        }

/*
OK.  We have the next character in the input buffer, at the position marked by
the current file column.  Return the code of the character at this position in
the input buffer.
*/

        return this.lineBuffer.codePointAt (this.column);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.BufferedReader#read()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public final synchronized int read ()
    throws IOException
    {

/*
If we encounter a null pointer exception during execution, it will be due to
having a null line buffer - itself the result of encountering the end of the
stream when refreshing the buffer.  Rather than test this explicitly, with the
associated overhead of an additional test when reading each individual
character, we instead trap the exception and handle it below.
*/

        try
        {

/*
If we've passed the end of the input buffer, then refresh it and return the
newline character's code.
*/

            assert this.column >= 0 && this.column <= this.lineBuffer.length
            ();
            if (this.column == this.lineBuffer.length ())
            {
                refreshBuffer ();
                return '\n';
            }
        }

/*
Handle the null pointer exception arising out of a null line buffer, and return
-1 - indicating that the end of the stream has been reached.
*/

        catch (NullPointerException e)
        {
            return -1;
        }

/*
OK.  We have the next character in the input buffer, at the position marked by
the current file column.  Return the code of the character at this position in
the input buffer.
*/

        return this.lineBuffer.codePointAt (this.column++);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.BufferedReader#read(char[], int, int)

@throws NullPointerException If cbuf is NULL.

@throws IndexOutOfBoundsException If attempt is made to reference an element of
cbuf that does not exist.  Can be caused by bad values for off and len.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final synchronized int read (char [] cbuf, int off, int len)
    throws IOException
    {

/*
Initialize the number of characters read into the buffer to 0.  Return this
value when the procedure terminates.
*/

        int count = 0;

/*
Continue reading until the specified maximum number of characters have been
read, or until we encounter the end of the stream.

Note that we do not return if the stream is not ready when reading each
character.
*/

        while (count < len)
        {

/*
Read the next character.
*/

            int character = read ();

/*
If the end of stream is signaled (by a character code of -1), and it is the
first character, then return -1 - indicating that no characters were read.
Otherwise, if the count is greater than 0, then return the count.
*/

            if (character == -1)
            {
                if (count == 0)
                {
                    return -1;
                }
                return count;
            }

/*
OK.  If we made it this far, then we need to write the character that we just
read into the array, taking into account the indicated offset position.

This statement may generate a NullPointerException if cbuf is null, or an
IndexOutOfRangeException if (off + count) is not a valid index into the array.

Note: If the character is a Unicode supplemental character (that is, one with a
Unicode value in excess of OxFFFF), then it will be converted into a surrogate.
*/

            cbuf [off + count] = (char) character;
            ++count;
        }
        return count;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.Reader#read(char[])
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public final int read (char [] cbuf)
    throws IOException
    {
        return read (cbuf, 0, cbuf.length);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.Reader#read(java.nio.CharBuffer)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public final synchronized int read (CharBuffer target)
    throws IOException
    {

/*
Initialize the number of characters read into the buffer to 0.  Return this
value when the procedure terminates.
*/

        int count = 0;

/*
Continue reading until the buffer is full, or until we encounter the end of the
stream.

Note that we do not return if the stream is not ready when reading each
character.
*/

        while (target.hasRemaining ())
        {

/*
Read the next character.
*/

            int character = read ();

/*
If the end of stream is signaled (by a character code of -1), and it is the
first character, then return -1 - indicating that no characters were read.
Otherwise, if the count is greater than 0, then return the count.
*/

            if (character == -1)
            {
                if (count == 0)
                {
                    return -1;
                }
                return count;
            }

/*
OK.  If we made it this far, then we need to append the character that we just
read into the buffer.

This statement may generate a NullPointerException if target is null.  A
BufferOverflowException is possible if there is insufficient capacity within
the buffer, but we should not see one here since we check for capacity before
reading each character.

Note: If the character is a Unicode supplemental character (that is, one with a
Unicode value in excess of OxFFFF), then it will be converted into a surrogate.
*/

            target.append ((char) character);
            ++count;
        }
        return count;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.BufferedReader#readLine()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public final synchronized String readLine ()
    throws IOException
    {

/*
If there's no more data, then return null.
*/

        if (this.lineBuffer == null)
        {
            return null;
        }

/*
Otherwise, return the substring from the current location in the input buffer
to the end of the buffer, then refresh the buffer.
*/

        assert this.column >= 0 && this.column <= this.lineBuffer.length ();
        String returnValue = this.lineBuffer.substring (this.column);
        refreshBuffer ();
        return returnValue;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.io.BufferedReader#skip(long)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public final synchronized long skip (long n)
    throws IOException
    {

/*
Throw illegal argument exception if n is negative.
*/

        if (n < 0)
        {
            throw new IllegalArgumentException ();
        }

/*
Skip the required number of characters, stopping early only if we encounter the
end of the stream.  Return the number of characters actually skipped.

Note: On Windows & OS/2 systems, because we count carriage return + linefeed
end-of-line sequences as a single character, we may not always agree with the
standard Java readers which characters get skipped.  You have been warned!
*/

        long i = 0;
        while (i < n)
        {
            if (read () == -1)
            {
                return i;
            }
            ++i;
        }
        return i;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify conditions for a {@linkplain Tokenizer} to read from this stream.</p>

<p>If any special conditions are detected, then an appropriate exception will
be thrown.</p>

@throws EOFException If the end of the stream has been encountered and no more
data remains to be read.

@throws EOLAcknowledgmentException If the last field was terminated by an
<em>end-of-line</em> sequence, and that sequence has not been acknowledged.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final void verifyReaderState ()
    throws EOFException, EOLAcknowledgmentException
    {

/*
If the previous field was terminated by an end-of-file/end-of-stream, then we
can no longer read from this stream, so throw an EOFException (a subclass of
IOException).
*/

        if (this.lastFieldTerminator.hasReachedEOF ())
        {
            assert this.lineBuffer == null;
            throw new EOFException ();
        }

/*
If the previous field requires an acknowledgment that an end-of-line sequence
was encountered on the previous read, and that acknowledgment has yet to be
made, then we cannot continue reading from the stream and will throw an
EOLAcknowledgmentException (a subclass of IOException).
*/

        if (this.lastFieldTerminator.requiresEOLAcknowledgment () &&
        !this.eolAcknowledged)
        {
            throw new EOLAcknowledgmentException ();
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Provide a {@linkplain Tokenizer} with the next field from this reader.</p>

@return Field read from this reader as a String.  This may be an empty string
but should never be null.

@param delimiter Delimiter to be used to terminate the field.

@throws IOException If an I/O error occurs during the read operation.  The
exact type of IOException thrown will provide further information on the cause
of the failure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final String readField (Delimiter delimiter)
    throws IOException
    {

/*
Verify that we have a delimiter, then have the delimiter read the next field
from this class.
*/

        assert delimiter != null;
        Field field = delimiter.readNextField (this);

/*
Store the terminator that ended the field read and clear the EOL acknowledgment
flag.
*/

        this.lastFieldTerminator = field.getTerminator ();
        this.eolAcknowledged = false;

/*
Return the field read.
*/

        return field.getField ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Acknowledge an <em>end-of-line</em> sequence on behalf of a {@linkplain
Tokenizer}.</p>

@throws EOLAcknowledgmentException If the previous field read from the stream
was not terminated by an <em>end-of-line</em> sequence, or if this delimiter
treats <em>end-of-line</em> sequences as a regular delimiter.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final void acknowledgeEOL ()
    throws EOLAcknowledgmentException
    {

/*
If the previous field did not require acknowledgment, or if the acknowledgment
has already been made, then throw the EOLAcknowledgmentException (subclass of
IOException).
*/

        if (!this.lastFieldTerminator.requiresEOLAcknowledgment () ||
        this.eolAcknowledged)
        {
            throw new EOLAcknowledgmentException ();
        }

/*
Acknowledge the end-of-line at the end of the previous field.
*/

        assert this.lastFieldTerminator.requiresEOLAcknowledgment ();
        this.eolAcknowledged = true;
    }
}
