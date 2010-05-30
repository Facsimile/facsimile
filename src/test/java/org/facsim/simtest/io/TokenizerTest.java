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

import java.io.EOFException;
import java.io.IOException;
import org.facsim.facsimile.io.Delimiter;
import org.facsim.facsimile.io.EOLAcknowledgmentException;
import org.facsim.facsimile.io.FieldFormatException;
import org.facsim.facsimile.io.TextReader;
import org.facsim.facsimile.io.Tokenizer;
import org.junit.Test;
import static org.junit.Assert.*;

//=============================================================================
/**
<p>Test fixture for the {@linkplain Tokenizer} class.</p>
*/
//=============================================================================

public final class TokenizerTest
{

/**
<p>Tab-delimited data file test contents.</p>

<p>Tests will fail if the contents of this string are modified, so don't do
that!</p>
*/

    private static final String tabData = "1\t2.5\tthree\n4\t5.5\tsix";

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test null reader construction.</p>

<p>Tests that a tokenizer constructed with a null reader throws a
NullPointerException.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullReaderConstruction ()
    {
        new Tokenizer (null, Delimiter.getTabDelimiter ());
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test null delimiter construction.</p>

<p>Tests that a tokenizer constructed with a null delimiter throws a
NullPointerException.</p>

@throws IOException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void nullDelimiterConstruction ()
    throws IOException
    {
        new Tokenizer (new TextReader (tabData), null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that attempting to reset the tokenizer before any fields have been
read results in an IOException.</p>

@throws IOException If the reader has yet to be marked or read from via a
tokenizer.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = NullPointerException.class)
    public void resetWithoutReadField ()
    throws IOException
    {
        new Tokenizer (new TextReader (tabData), null);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as text using a tab
delimiter.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readFieldTabDelimiter ()
    throws IOException
    {

/*
Create a new tokenizer with the tab data and delimiter.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (tabData),
        Delimiter.getTabDelimiter ());

/*
Read each field in turn, verifying its contents.  Acknowledge the end-of-line
after the third field.
*/

        assertTrue (tokenizer.readField () == "1");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "2.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "three");
        tokenizer.acknowledgeEOL ();
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "4");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "5.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "six");
        checkForEOLException (tokenizer);

/*
At this point, we should have exhausted our test data.  Verify that we get an
EOFException if we attempt to read another field.
*/

        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we can reset the reader back to the point prior to each field
being read.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readFieldWithReset ()
    throws IOException
    {

/*
Create a new tokenizer with the tab data and delimiter.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (tabData),
        Delimiter.getTabDelimiter ());

/*
Read each field in turn twice, verifying its contents.  Acknowledge the
end-of-line after the third field.
*/

        assertTrue (tokenizer.readField () == "1");
        checkForEOLException (tokenizer);
        tokenizer.reset ();
        assertTrue (tokenizer.readField () == "1");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "2.5");
        checkForEOLException (tokenizer);
        tokenizer.reset ();
        assertTrue (tokenizer.readField () == "2.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "three");
        tokenizer.acknowledgeEOL ();
        checkForEOLException (tokenizer);
        tokenizer.reset ();
        assertTrue (tokenizer.readField () == "three");
        tokenizer.acknowledgeEOL ();
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "4");
        checkForEOLException (tokenizer);
        tokenizer.reset ();
        assertTrue (tokenizer.readField () == "4");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "5.5");
        checkForEOLException (tokenizer);
        tokenizer.reset ();
        assertTrue (tokenizer.readField () == "5.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "six");
        checkForEOLException (tokenizer);
        tokenizer.reset ();
        assertTrue (tokenizer.readField () == "six");
        checkForEOLException (tokenizer);

/*
At this point, we should have exhausted our test data.  Verify that we get an
EOFException if we attempt to read another field.
*/

        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as text using a
whitespace delimiter.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readFieldWhitespaceDelimiter ()
    throws IOException
    {

/*
Whitespace-delimited data file test contents.
*/

        final String whitespaceData = "1 2.5\tthree \n 4\t   \t5.5\nsix";

/*
Create a new tokenizer with the whitespace data and delimiter.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (whitespaceData),
        Delimiter.getWhitespaceDelimiter ());

/*
Read each field in turn, verifying its contents.  This delimiter does not need
to acknowledge any EOL sequences.
*/

        assertTrue (tokenizer.readField () == "1");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "2.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "three");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "4");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "5.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "six");
        checkForEOLException (tokenizer);

/*
At this point, we should have exhausted our test data.  Verify that we get an
EOFException if we attempt to read another field.
*/

        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as text using a comma
delimiter.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readFieldCommaDelimiter ()
    throws IOException
    {

/*
Comma-delimited data file test contents.
*/

        final String commaData = "1,2.5,three\n4,5.5,six";

/*
Create a new tokenizer with the comma data and delimiter.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (commaData),
        Delimiter.getCommaDelimiter ());

/*
Read each field in turn, verifying its contents.  Acknowledge the end-of-line
after the third field.
*/

        assertTrue (tokenizer.readField () == "1");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "2.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "three");
        tokenizer.acknowledgeEOL ();
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "4");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "5.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "six");
        checkForEOLException (tokenizer);

/*
At this point, we should have exhausted our test data.  Verify that we get an
EOFException if we attempt to read another field.
*/

        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as text using a line
delimiter.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readFieldLineDelimiter ()
    throws IOException
    {

/*
Line-delimited data file test contents.
*/

        final String lineData = "1\n2.5\nthree\n4\n5.5\nsix";

/*
Create a new tokenizer with the line data and delimiter.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (lineData),
        Delimiter.getLineDelimiter ());

/*
Read each field in turn, verifying its contents.  This delimiter does not need
to acknowledge any EOL sequences.
*/

        assertTrue (tokenizer.readField () == "1");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "2.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "three");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "4");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "5.5");
        checkForEOLException (tokenizer);
        assertTrue (tokenizer.readField () == "six");
        checkForEOLException (tokenizer);

/*
At this point, we should have exhausted our test data.  Verify that we get an
EOFException if we attempt to read another field.
*/

        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as boolean values.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readBooleanField ()
    throws IOException
    {

/*
Data to test boolean field reader, using a comma delimiter.
*/

        final String boolData = "TRUE,true,FALSE,false,1,0,1.0,0.0,bool,";

/*
Create a tokenizer to read this data.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (boolData),
        Delimiter.getCommaDelimiter ());

/*
Verify that the first few values are read OK.  The remainder should produce
FieldFormatExceptions.
*/

        assertTrue (tokenizer.readBooleanField ());
        assertTrue (tokenizer.readBooleanField ());
        assertFalse (tokenizer.readBooleanField ());
        assertFalse (tokenizer.readBooleanField ());
        checkForBooleanException (tokenizer);
        checkForBooleanException (tokenizer);
        checkForBooleanException (tokenizer);
        checkForBooleanException (tokenizer);
        checkForBooleanException (tokenizer);
        checkForBooleanException (tokenizer);
        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as binary digit
values.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readBinaryField ()
    throws IOException
    {

/*
Data to test boolean field reader, using a comma delimiter.
*/

        final String boolData = "1,0,TRUE,true,FALSE,false,1.0,0.0,bool,";

/*
Create a tokenizer to read this data.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (boolData),
        Delimiter.getCommaDelimiter ());

/*
Verify that the first two values are read OK.  The remainder should produce
FieldFormatExceptions.
*/

        assertTrue (tokenizer.readBinaryField ());
        assertFalse (tokenizer.readBinaryField ());
        checkForBinaryException (tokenizer);
        checkForBinaryException (tokenizer);
        checkForBinaryException (tokenizer);
        checkForBinaryException (tokenizer);
        checkForBinaryException (tokenizer);
        checkForBinaryException (tokenizer);
        checkForBinaryException (tokenizer);
        checkForBinaryException (tokenizer);
        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as byte values.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readByteField ()
    throws IOException
    {

/*
Data to test byte field reader, using a comma delimiter.
*/

        final String byteData = "0,127,-128,128,-129,0.0,byte,";

/*
Create a tokenizer to read this data.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (byteData),
        Delimiter.getCommaDelimiter ());

/*
Verify that the first three values are read OK.  The remainder should produce
FieldFormatExceptions.
*/

        assertTrue (tokenizer.readByteField () == 0);
        assertTrue (tokenizer.readByteField () == 127);
        assertTrue (tokenizer.readByteField () == -128);
        checkForByteException (tokenizer);
        checkForByteException (tokenizer);
        checkForByteException (tokenizer);
        checkForByteException (tokenizer);
        checkForByteException (tokenizer);
        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as short values.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readShortField ()
    throws IOException
    {

/*
Data to test short field reader, using a comma delimiter.
*/

        final String shortData = "0,32767,-32768,32768,-32769,0.0,short,";

/*
Create a tokenizer to read this data.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (shortData),
        Delimiter.getCommaDelimiter ());

/*
Verify that the first three values are read OK.  The remainder should produce
FieldFormatExceptions.
*/

        assertTrue (tokenizer.readShortField () == 0);
        assertTrue (tokenizer.readShortField () == 32767);
        assertTrue (tokenizer.readShortField () == -32768);
        checkForShortException (tokenizer);
        checkForShortException (tokenizer);
        checkForShortException (tokenizer);
        checkForShortException (tokenizer);
        checkForShortException (tokenizer);
        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as int values.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readIntField ()
    throws IOException
    {

/*
Data to test int field reader, using a comma delimiter.
*/

        final String intData = "0,2147483647,-2147483648,2147483648," +
        "-2147483649,0.0,int,";

/*
Create a tokenizer to read this data.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (intData),
        Delimiter.getCommaDelimiter ());

/*
Verify that the first three values are read OK.  The remainder should produce
FieldFormatExceptions.
*/

        assertTrue (tokenizer.readIntField () == 0);
        assertTrue (tokenizer.readIntField () == 2147483647);
        assertTrue (tokenizer.readIntField () == -2147483648);
        checkForIntException (tokenizer);
        checkForIntException (tokenizer);
        checkForIntException (tokenizer);
        checkForIntException (tokenizer);
        checkForIntException (tokenizer);
        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as long values.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readLongField ()
    throws IOException
    {

/*
Data to test long field reader, using a comma delimiter.
*/

        final String longData = "0,9223372036854775807,-9223372036854775808," +
        "9223372036854775808,-9223372036854775809,0.0,long,";

/*
Create a tokenizer to read this data.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (longData),
        Delimiter.getCommaDelimiter ());

/*
Verify that the first three values are read OK.  The remainder should produce
FieldFormatExceptions.
*/

        assertTrue (tokenizer.readLongField () == 0);
        assertTrue (tokenizer.readLongField () == 9223372036854775807L);
        assertTrue (tokenizer.readLongField () == -9223372036854775808L);
        checkForLongException (tokenizer);
        checkForLongException (tokenizer);
        checkForLongException (tokenizer);
        checkForLongException (tokenizer);
        checkForLongException (tokenizer);
        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test that we correctly read the contents of fields as double values.</p>

@throws IOException If an I/O error occurs.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void readDoubleField ()
    throws IOException
    {

/*
Data to test double field reader, using a comma delimiter.
*/

        final String doubleData = "0,1,-1,1e2,-1e-2,0.0,1.0,-1.0,1.0e2," +
        "-1.0e-2,double,";

/*
Create a tokenizer to read this data.
*/

        Tokenizer tokenizer = new Tokenizer (new TextReader (doubleData),
        Delimiter.getCommaDelimiter ());

/*
Verify that the first few values are read OK.  The remainder should produce
FieldFormatExceptions.
*/

        assertTrue (tokenizer.readDoubleField () == 0.0);
        assertTrue (tokenizer.readDoubleField () == 1.0);
        assertTrue (tokenizer.readDoubleField () == -1.0);
        assertTrue (tokenizer.readDoubleField () == 100.0);
        assertTrue (tokenizer.readDoubleField () == -0.01);
        assertTrue (tokenizer.readDoubleField () == 0.0);
        assertTrue (tokenizer.readDoubleField () == 1.0);
        assertTrue (tokenizer.readDoubleField () == -1.0);
        assertTrue (tokenizer.readDoubleField () == 100.0);
        assertTrue (tokenizer.readDoubleField () == -0.01);
        checkForDoubleException (tokenizer);
        checkForDoubleException (tokenizer);
        checkForEOFException (tokenizer);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that calling acknowledgeEOL on the tokenizer in its present state
yields an EOLAcknowledgmentException.</p>

@param tokenizer Tokenizer to acknowledge, incorrectly, an EOL condition on.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForEOLException (Tokenizer tokenizer)
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.acknowledgeEOL ();
        }
        catch (EOLAcknowledgmentException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that if another field is read from the specified tokenizer that an
EOFException will result.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForEOFException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readField ();
        }
        catch (EOFException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the next field produces a FieldFormatException when read as a
boolean.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForBooleanException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readBooleanField ();
        }
        catch (FieldFormatException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the next field produces a FieldFormatException when read as a
binary digit.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForBinaryException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readBinaryField ();
        }
        catch (FieldFormatException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the next field produces a FieldFormatException when read as a
byte.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForByteException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readByteField ();
        }
        catch (FieldFormatException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the next field produces a FieldFormatException when read as a
short.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForShortException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readShortField ();
        }
        catch (FieldFormatException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the next field produces a FieldFormatException when read as an
int.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForIntException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readIntField ();
        }
        catch (FieldFormatException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the next field produces a FieldFormatException when read as a
long.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForLongException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readLongField ();
        }
        catch (FieldFormatException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the next field produces a FieldFormatException when read as a
double.</p>

@param tokenizer Tokenizer to be read from.

@throws IOException Should not occur here.

@throws EOLAcknowledgmentException Should not occur here.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void checkForDoubleException (Tokenizer tokenizer)
    throws EOLAcknowledgmentException, IOException
    {
        assert tokenizer != null;
        boolean sawException = false;
        try
        {
            tokenizer.readDoubleField ();
        }
        catch (FieldFormatException e)
        {
            sawException = true;
        }
        assertTrue (sawException);
    }
}
