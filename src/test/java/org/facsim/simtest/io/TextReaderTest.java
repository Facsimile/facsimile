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

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import org.facsim.facsimile.io.TextReader;
import org.junit.Ignore;
import org.junit.Test;

//=============================================================================
/**
<p>Test fixture for the {@linkplain TextReader} class.</p>
*/
//=============================================================================

public class TextReaderTest
{

/**
<p>Common stream data for many tests.</p>

<p>This data deliberately includes a linefeed after the first half of the
alphabet so that more than one line can be read from the file.</p>
*/

    private static final String streamData = "abcdefghijklm\nnopqrstuvwxyz";

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test null {@linkplain File} text reader construction.</p>

<p>Tests that a {@linkplain NullPointerException} is thrown when the
{@linkplain TextReader#TextReader(File)} constructor is passed a null
pointer.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullFileConstruction ()
    throws IOException
    {
        File nullFile = null;
        new TextReader (nullFile);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test missing {@linkplain File} text reader construction.</p>

<p>Tests that a {@linkplain FileNotFoundException} is thrown when the
{@linkplain TextReader#TextReader(File)} constructor is passed a missing
file.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = FileNotFoundException.class)
    public void missingFileConstruction ()
    throws IOException
    {

/*
The idea here is that data/test/MissingFile.txt does not exist.  Do not create
it!
*/

        new TextReader (new File ("data/test/MissingFile.txt"));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test valid {@linkplain File} text reader construction.</p>

<p>Tests that a valid text file reader is constructed when the {@linkplain
TextReader#TextReader(File)} constructor is passed a valid file name.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void validFileConstruction ()
    throws IOException
    {
        new TextReader (new File ("data/test/Text.txt"));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test null {@linkplain FileDescriptor} text reader construction.</p>

<p>Tests that a {@linkplain NullPointerException} is thrown when the
{@linkplain TextReader#TextReader(FileDescriptor)} constructor is passed a null
pointer.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullFileDescriptorConstruction ()
    throws IOException
    {
        FileDescriptor nullDescriptor = null;
        new TextReader (nullDescriptor);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test valid {@linkplain FileDescriptor} text reader construction.</p>

<p>Tests that a valid text file reader is constructed when the {@linkplain
TextReader#TextReader(FileDescriptor)} constructor is passed a valid file
descriptor.</p>

<p>This test is currently ignored since there is only one valid FileDescriptor
and that is the standard input.  This will cause the reader instance to
immediately attempt to read data from the input stream, which we cannot
currently satisfy.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    @Ignore
    public void validFileDescriptorConstruction ()
    throws IOException
    {
        new TextReader (FileDescriptor.in);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test null {@linkplain String} text reader construction.</p>

<p>Tests that a {@linkplain NullPointerException} is thrown when the
{@linkplain TextReader#TextReader(String)} constructor is passed a null
pointer.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullStringConstruction ()
    throws IOException
    {
        String nullString = null;
        new TextReader (nullString);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test valid {@linkplain String} text reader construction.</p>

<p>Tests that a valid text file reader is constructed when the {@linkplain
TextReader#TextReader(String)} constructor is passed a valid data string.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void validStringConstruction ()
    throws IOException
    {
        new TextReader (streamData);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test null {@linkplain Reader} text reader construction.</p>

<p>Tests that a {@linkplain NullPointerException} is thrown when the
{@linkplain TextReader#TextReader(Reader)} constructor is passed a null
pointer.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullReaderConstruction ()
    throws IOException
    {
        Reader nullReader = null;
        new TextReader (nullReader);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test valid {@linkplain Reader} text reader construction.</p>

<p>Tests that a valid text file reader is constructed when the {@linkplain
TextReader#TextReader(Reader)} constructor is passed a valid reader
reference.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void validReaderConstruction ()
    throws IOException
    {
        new TextReader (new FileReader ("data/test/Text.txt"));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that <em>end-of-line</em> sequences are uniformly converted to
<em>linefeed</em> characters.</p>

<p>This also verifies basic character and line reading operations, as well as
line and column number tracking.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void endOfLineProcessing ()
    throws IOException
    {

/*
Target data with linefeed-delimited records.  Firstly, read the data on a
character-by-character basis to verify that it matches the same string fed in.
Then re-read the same data on a line-by-line basis to verify the same thing.
*/

        final String lfData = "Line one\nLine two\nLine three";
        final String [] lfLineData =
        {
            "Line one",
            "Line two",
            "Line three",
        };
        verifyCharByCharRead (lfData, lfData);
        verifyLineByLineRead (lfData, lfLineData);

/*
Data with carriage return-delimited records.  When read, this should compare
equal to the newline data.
*/

        final String crData = "Line one\rLine two\rLine three";
        verifyCharByCharRead (crData, lfData);
        verifyLineByLineRead (crData, lfLineData);

/*
Data with carriage return & linefeed-delimited records.  When read, this should
compare equal to the newline data.
*/

        final String crlfData = "Line one\r\nLine two\r\nLine three";
        verifyCharByCharRead (crlfData, lfData);
        verifyLineByLineRead (crlfData, lfLineData);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests that {@linkplain TextReader#mark(int)} throws an {@linkplain
IllegalArgumentException} if passed a negative read-ahead buffer size.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IllegalArgumentException.class)
    public void negativeBufferMark ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        assertTrue (reader.markSupported ());
        reader.mark (-1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests that {@linkplain TextReader#mark(int)} accepts a zero read-ahead
buffer size.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void zeroBufferMark ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        assertTrue (reader.markSupported ());
        reader.mark (0);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests that {@linkplain TextReader#reset()} function throws an exception if
the stream was not previously marked.</p>

@throws IOException Expected exception.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IOException.class)
    public void resetWithoutMark ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        assertTrue (reader.markSupported ());
        reader.reset ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that the mark/reset stream feature is working correctly.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void markResetOperation ()
    throws IOException
    {

/*
Verify that we can mark a stream in a middle of a read, continue reading from
it, then resume the read operation from the original location without losing
any data.
*/

        TextReader reader = new TextReader (streamData);

/*
Read five characters, which should lease us looking at "f" as the next
character.
*/

        final int readLimit = 5;
        for (int count = 0; count < readLimit; ++count)
        {
            assertTrue (reader.getLine () == 1);
            assertTrue (reader.getColumn () == count);
            int character = reader.read ();
            assertTrue (character == streamData.codePointAt (count));
        }

/*
OK.  Mark the reader at this point, then verify that we can return to this
point in the reader later on.
*/

        assertTrue (reader.markSupported ());
        reader.mark (1024);

/*
Repeat the following code twice, verifying that we're reading the same
sequence of data both times.
*/

        for (int iterations = 0; iterations < 2; ++iterations)
        {
            for (int count = readLimit; count < streamData.length (); ++count)
            {
                int character = reader.read ();
                assertTrue (character == streamData.codePointAt (count));
            }

/*
Reset the data back to the marked location.
*/

            reader.reset ();
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that {@linkplain TextReader#read(char[])} throws a null pointer
exception when passed a null array.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullSimpleCharArrayRead ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        char [] nullArray = null;
        reader.read (nullArray);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies {@linkplain TextReader#read(char[])} operations.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void simpleCharArrayRead ()
    throws IOException
    {

/*
Firstly, create a small array that is shorter than the stream data and verify
that we read to the end of the buffer, but not past it.  The return value
should be the length of the array.
*/

        TextReader reader = new TextReader (streamData);
        char [] smallArray = new char [streamData.length () - 1];
        assert smallArray.length < streamData.length ();
        assertTrue (reader.read (smallArray) == smallArray.length);
        for (int i = 0; i < smallArray.length; ++i)
        {
            assertTrue (smallArray [i] == streamData.charAt (i));
        }

/*
Now create a buffer that's the same size as the stream data and verify that we
read every character without generating any errors.  The return value should be
the length of the array.
*/

        reader = new TextReader (streamData);
        char [] equalArray = new char [streamData.length ()];
        assert equalArray.length == streamData.length ();
        assertTrue (reader.read (equalArray) == equalArray.length);
        for (int i = 0; i < equalArray.length; ++i)
        {
            assertTrue (equalArray [i] == streamData.charAt (i));
        }

/*
At this point, any attempt to read further from the stream should result in a
value of -1, indicating that no characters were read and that the end of the
stream was encountered.  Check that too.
*/

        assertTrue (reader.read (equalArray) == -1);

/*
Now create a buffer that is larger than the stream data and verify that we read
to the end of the stream, but not past it.  The return value should be the
length of the stream.
*/

        reader = new TextReader (streamData);
        char [] bigArray = new char [streamData.length () + 1];
        assert bigArray.length > streamData.length ();
        assertTrue (reader.read (bigArray) == streamData.length ());
        for (int i = 0; i < streamData.length (); ++i)
        {
            assertTrue (equalArray [i] == streamData.charAt (i));
        }

/*
At this point, any attempt to read further from the stream should result in a
value of -1, indicating that no characters were read and that the end of the
stream was encountered.  Check that too.
*/

        assertTrue (reader.read (bigArray) == -1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that {@linkplain TextReader#read(char[],int,int)} throws a
{@linkplain NullPointerException} when passed a null array.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullComplexCharArrayRead ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        char [] nullArray = null;
        reader.read (nullArray, 0, 5);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that {@linkplain TextReader#read(char[],int,int)} throws an
{@linkplain IndexOutOfBoundsException} when passed a negative offset.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IndexOutOfBoundsException.class)
    public void negativeOffsetComplexCharArrayRead ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        char [] array = new char [5];
        reader.read (array, -1, 1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that {@linkplain TextReader#read(char[],int,int)} throws an
{@linkplain IndexOutOfBoundsException} when passed a negative number of
characters to be read.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IndexOutOfBoundsException.class)
    public void negativeLengthComplexCharArrayRead ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        char [] array = new char [5];
        reader.read (array, 0, -1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that {@linkplain TextReader#read(char[],int,int)} indicates that no
characters were read when passed a zero number of characters to be read.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void zeroLengthComplexCharArrayRead ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        char [] array = new char [5];
        assertTrue (reader.read (array, 0, 0) == 0);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that {@linkplain TextReader#read(char[],int,int)} throws an
{@linkplain IndexOutOfBoundsException} when passed an offset + length
combination that exceeds the size of the buffer (and when there is enough data
in the stream to satisfy the request).</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IndexOutOfBoundsException.class)
    public void overflowedComplexCharArrayRead ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        char [] array = new char [5];
        reader.read (array, 0, array.length + 1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies {@linkplain TextReader#read(char[],int,int)} operations.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void complexCharArrayRead ()
    throws IOException
    {

/*
Firstly, create an array that is larger than the stream data.  We can control
how much of the buffer we will allow to be read.
*/

        final int extraCapacity = 10;
        char [] array = new char [streamData.length () + extraCapacity];

/*
Create a reader and restrict the read to one character less than the size of
the stream.  The number of characters read should be the same as the
restricted size.  Repeat with offset of 0, extraCapacity / 2 and extraCapacity.
*/

        final int smallSize = streamData.length () - 1;
        verifyOffsetRead (streamData, array, 0, smallSize, smallSize);
        verifyOffsetRead (streamData, array, extraCapacity / 2, smallSize,
        smallSize);
        verifyOffsetRead (streamData, array, extraCapacity, smallSize,
        smallSize);

/*
Now repeat the process by restricting the the read to the same size as the
stream.
*/

        final int equalSize = streamData.length ();
        verifyOffsetRead (streamData, array, 0, equalSize, equalSize);
        verifyOffsetRead (streamData, array, extraCapacity / 2, equalSize,
        equalSize);
        verifyOffsetRead (streamData, array, extraCapacity, equalSize,
        equalSize);

/*
Now repeat the process by restricting the the read to one higher thatn the size
of the stream.  The number of characters read should equal the size of the
stream.
*/

        final int largeSize = streamData.length () + 1;
        verifyOffsetRead (streamData, array, 0, largeSize, equalSize);
        verifyOffsetRead (streamData, array, extraCapacity / 2, largeSize,
        equalSize);
        verifyOffsetRead (streamData, array, extraCapacity, largeSize,
        equalSize);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that {@linkplain TextReader#read(CharBuffer)} throws a null
pointer exception when passed a null pointer.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullCharBufferRead ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        CharBuffer nullBuffer = null;
        reader.read (nullBuffer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies {@linkplain TextReader#read(CharBuffer)} operations.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void charBufferRead ()
    throws IOException
    {

/*
Firstly, create a small buffer that is shorter than the stream data and verify
that we read to the end of the buffer, but not past it.  The return value
should be the length of the array.
*/

        TextReader reader = new TextReader (streamData);
        CharBuffer smallBuffer = CharBuffer.allocate (streamData.length () -
        1);
        assert smallBuffer.remaining () < streamData.length ();
        assertTrue (reader.read (smallBuffer) == smallBuffer.capacity ());
        for (int i = 0; i < smallBuffer.capacity (); ++i)
        {
            assertTrue (smallBuffer.charAt (i) == streamData.charAt (i));
        }

/*
We're at the end of the buffer now.  Any attempt to read further should result
in 0 characters being read because the buffer has no more space.  Verify that.
*/

        assertTrue (reader.read (smallBuffer) == 0);

/*
Now create a buffer that's the same size as the stream data and verify that we
read every character without generating any errors.  The return value should be
the length of the array.
*/

        reader = new TextReader (streamData);
        CharBuffer equalBuffer = CharBuffer.allocate (streamData.length ());
        assert equalBuffer.remaining () == streamData.length ();
        assertTrue (reader.read (equalBuffer) == equalBuffer.capacity ());
        for (int i = 0; i < equalBuffer.capacity (); ++i)
        {
            assertTrue (equalBuffer.charAt (i) == streamData.charAt (i));
        }

/*
We're at the end of the stream now, but also at the end of the buffer.  Any
attempt to read further should result in 0 characters being read because the
buffer has no more space.  Verify that.
*/

        assertTrue (reader.read (equalBuffer) == 0);

/*
If we clear the buffer, then any attempt to read further from the stream should
result in a value of -1, indicating that no characters were read and that the
end of the stream was encountered.  Check that too.  The buffer should be
unmodified.
*/

        equalBuffer.clear ();
        assert equalBuffer.remaining () == equalBuffer.capacity ();
        assertTrue (reader.read (equalBuffer) == -1);
        assert equalBuffer.remaining () == equalBuffer.capacity ();

/*
Now create a buffer that's larger than the stream data and verify that we read
every character without generating any errors.  The return value should be
the length of the stream.
*/

        reader = new TextReader (streamData);
        CharBuffer bigBuffer = CharBuffer.allocate (streamData.length () + 1);
        assert bigBuffer.remaining () == streamData.length ();
        assertTrue (reader.read (bigBuffer) == streamData.length ());
        for (int i = 0; i < bigBuffer.capacity (); ++i)
        {
            assertTrue (bigBuffer.charAt (i) == streamData.charAt (i));
        }

/*
At this point, any attempt to read further from the stream should result in a
value of -1, indicating that no characters were read and that the end of the
stream was encountered.  Check that too.  The buffer should be unmodified.
*/

        bigBuffer.clear ();
        assert bigBuffer.remaining () == bigBuffer.capacity ();
        assertTrue (reader.read (bigBuffer) == -1);
        assert bigBuffer.remaining () == bigBuffer.capacity ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that {@linkplain TextReader#skip(long)} throws a {@linkplain
IllegalArgumentException} if passed a negative number of characters to
skip.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = IllegalArgumentException.class)
    public void negativeSkip ()
    throws IOException
    {
        TextReader reader = new TextReader (streamData);
        reader.skip (-1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that {@linkplain TextReader#skip(long)} operates correctly.</p>

@throws IOException Should never occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void skipOperation ()
    throws IOException
    {

/*
Create the reader.
*/

        TextReader reader = new TextReader (streamData);

/*
Skip the first five characters from the stream.  This should leave us on line
1 looking at the 6th character of the alphabet.  (The file reports column 5
because it starts numbering at column 0.)
*/

        assertTrue (reader.skip (5) == 5);
        assertTrue (reader.getLine () == 1);
        assertTrue (reader.getColumn () == 5);
        assertTrue (reader.read () == 'f');
        assertTrue (reader.getLine () == 1);
        assertTrue (reader.getColumn () == 6);

/*
Skip ahead another 5, this should leave us looking at the 11th character of
the alphabet.
*/

        assertTrue (reader.skip (5) == 5);
        assertTrue (reader.getLine () == 1);
        assertTrue (reader.getColumn () == 11);
        assertTrue (reader.read () == 'k');
        assertTrue (reader.getLine () == 1);
        assertTrue (reader.getColumn () == 12);

/*
OK.  That should do.  Now skip as many characters as we can and verify that we
actually skipped the remaining characters.
*/

        assertTrue (reader.skip (streamData.length ()) == streamData.length ()
        - 12);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the supplied source data matches the expected data on a
character-by-character basis.</p>

<p>The data supplied can differ from the data read, since <em>end-of-line</em>
sequences can be modified and since <em>Unicode</em> supplemental characters
can be replaced by surrogates.</p>

@param sourceData Data to be read in.

@param expectedData Data expected to be read back.

@throws IOException If an unexpected error occurs reading the data from the
reader.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void verifyCharByCharRead (String sourceData, String expectedData)
    throws IOException
    {

/*
Create a reader to open the source data.
*/

        TextReader reader = new TextReader (sourceData);

/*
Verify that the initial reader position is at line 1, column 0.
*/

        assertTrue (reader.getLine () == 1);
        assertTrue (reader.getColumn () == 0);

/*
Now read every character from the reader one at a time.
*/

        int count = 0;
        int currentLine = 1;
        int currentColumn = 0;
        for (;;)
        {

/*
Record the line and column number, then peek at the first character, then
verify that the line and column number have not changed.
*/

            int peekLine = reader.getLine ();
            int peekColumn = reader.getColumn ();
            int peekedCharacter = reader.peek ();
            assertTrue (peekLine == reader.getLine ());
            assertTrue (peekColumn == reader.getColumn ());

/*
Now read the next character and verify that it's the same as the peeked
character.
*/

            int character = reader.read ();
            assertTrue (character == peekedCharacter);

/*
If the character read is -1, then we're done.
*/

            if (character == -1)
            {
                break;
            }

/*
Verify that the character read matches our expectations, and update the count
of characters read.
*/

            assertTrue (character == expectedData.codePointAt (count));
            ++count;

/*
Update the line and column number and verify against the reader.
*/

            if (character == '\n')
            {
                ++currentLine;
                currentColumn = 0;
            }
            else
            {
                ++currentColumn;
            }
            assertTrue (reader.getLine () == currentLine);
            assertTrue (reader.getColumn () == currentColumn);
        }

/*
Now verify that the right amount of data was processed.
*/

        assertTrue (count == expectedData.length ());
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the supplied source data matches the expected data on a
line-by-line basis.</p>

<p>The data supplied can differ from the data read, since <em>Unicode</em>
supplemental characters can be replaced by surrogates (<em>end-of-line</em>
sequences are stripped by the readLine procedure, so they do not apply
here).</p>

@param sourceData Data to be read in.

@param expectedData Array containing each line of data expected to be read
back.

@throws IOException If an unexpected error occurs reading the data from the
reader.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void verifyLineByLineRead (String sourceData, String []
    expectedData)
    throws IOException
    {

/*
Create a reader to open the source data.
*/

        TextReader reader = new TextReader (sourceData);

/*
Now read every line from the reader one at a time, counting them and comparing
them to the expected line each time.
*/

        int index = 0;
        String line;
        while ((line = reader.readLine ()) != null)
        {
            assertTrue (line.equals (expectedData [index]));
            ++index;
            assertTrue (reader.getLine () == index);
            assertTrue (reader.getColumn () == 0);
        }

/*
Now verify that the right number of lines were processed.
*/

        assertTrue (index == expectedData.length);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that we can read into the supplied character array at the indicated
offset and maximum number of characters to be read.  Also verify that the
number of characters read matches expectations.</p>

@param sourceData Data to be supplied to the reader for reading.

@param array Character array into which data is to be written.

@param offset Offset into the array at which data starts being written.

@param maxChars Maximum number of characters to be read during this operation.

@param expectedChars Expected number of characters be be reported as read.

@throws IOException If an unexpected error occurs reading the data from the
reader.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void verifyOffsetRead (String sourceData, char [] array, int
    offset, int maxChars, int expectedChars)
    throws IOException
    {

/*
Create a reader to open the source data.
*/

        TextReader reader = new TextReader (sourceData);

/*
Read the data into the array at the indicated offset.  Verify that the number
of characters read matches our expectations.
*/

        assertTrue (reader.read (array, offset, maxChars) == expectedChars);

/*
Now verify the contents of the array.
*/

        for (int i = 0; i < expectedChars; ++i)
        {
            assertTrue (array [offset + i] == sourceData.charAt (i));
        }
    }
}
