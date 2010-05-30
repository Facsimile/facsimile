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

import java.io.IOException;
import org.facsim.facsimile.util.PackagePrivate;
import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p>Input stream field delimiter specification, to be employed by the
{@linkplain Tokenizer} class.</p>
*/
//=============================================================================

@Immutable
public final class Delimiter
{

/**
<p>Whitespace delimiter.</p>

@see #getWhitespaceDelimiter ()
*/

    private static final Delimiter whitespaceDelimiter;

/**
<p>Tab delimiter.</p>

@see #getTabDelimiter ()
*/

    private static final Delimiter tabDelimiter;

/**
<p>Comma delimiter.<p>

@see #getCommaDelimiter ()
*/

    private static final Delimiter commaDelimiter;

/**
<p>Line delimiter.</p>

@see #getLineDelimiter ()
*/

    private static final Delimiter lineDelimiter;

/**
<p>Field delimiter characters.</p>

<p>Line termination characters, that is the <em>linefeed</em> and <em>carriage
return</em> characters, are given special treatment: neither can be specified
as a delimiter character.  Furthermore, this string cannot contain any invalid
delimiter characters, nor can it contain the same character more than once.</p>
*/

    private final String delimiters;

/**
<p>Treat consecutive delimiters as one.</p>

<p>Flag indicating whether consecutive delimiter characters are treated as a
single delimiter.  If <strong>true</strong>, then a sequence of consecutive
delimiter characters is treated as a single delimiter.  If
<strong>false</strong>, then empty fields will be reported between each pair of
consecutive delimiter characters.<p>
*/

    private final boolean treatConsecutiveAsOne;

/**
<p>Flag indicating whether <em>end-of-line</em> sequences are to be treated as
regular delimiters.</p>

<p>If <strong>true</strong>, then end-of-line sequences are treated in the same
way as the delimiters contained in {@linkplain #delimiters}; end-of-line
seqeuences have no special significance, and calling {@linkplain
Tokenizer#acknowledgeEOL ()} after such a sequence is encountered will result
in an exception.  If <strong>false</strong>, then end-of-line sequences will
terminate both fields and consecutive delimiter sequences (if {@linkplain
#treatConsecutiveAsOne} is <strong>true</strong>) and will require that
{@linkplain Tokenizer#acknowledgeEOL ()} be called to acknowledge the end of
the current line before further data can be read.</p>
*/

    private final boolean eolIsDelimiter;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Static initializer.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {

/*
Create commonly used delimiters upon first use.  Refer to the respective static
getter functions for more information on these delimiter instances.
*/

        whitespaceDelimiter = new Delimiter (" \t", true, false);
        tabDelimiter = new Delimiter ("\t", false, true);
        commaDelimiter = new Delimiter (",", false, true);
        lineDelimiter = new Delimiter ("", false, false);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve whitespace delimiter.</p>

<p>Separates fields by the following characters: <em>spaces</em>,
<em>tabs</em>, <em>carriage returns</em>, <em>newlines</em>.  Consecutive
delimiters are treated as one, and <em>end-of-line</em> sequences have no
special significance.</p>

@return Whitespace delimiter instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Delimiter getWhitespaceDelimiter ()
    {
        assert whitespaceDelimiter != null;
        return whitespaceDelimiter;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve tab delimiter.</p>

<p>Separates fields by <em>tab</em> characters.  Consecutive delimiters are
treated as individual delimiters, with empty fields between them.
<em>End-of-line</em> sequences have special significance.</p>

@return Tab delimiter instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Delimiter getTabDelimiter ()
    {
        assert tabDelimiter != null;
        return tabDelimiter;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve comma delimiter.</p>

<p>Separates fields by <em>comma</em> characters.  Consecutive delimiters are
treated as individual delimiters, with empty fields between them.
<em>End-of-line</em> sequences have special significance.</p>

@return Comma delimiter instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Delimiter getCommaDelimiter ()
    {
        assert commaDelimiter != null;
        return commaDelimiter;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve line delimiter.</p>

<p>Separates fields by <em>end-of-line</em> sequences.  This delimiter
effectively treats entire lines as fields.  Consecutive delimiters are treated
as individual delimiters, with empty fields between them.  <em>End-of-line</em>
sequences have no special significance and should not be acknowledged.</p>

@return Line delimiter instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static Delimiter getLineDelimiter ()
    {
        assert lineDelimiter != null;
        return lineDelimiter;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Determine whether delimiter string is valid.</p>

@param delimiterString Delimiter string to be validated.

@return <strong>true</strong> if the delimiterString is valid,
<strong>false</strong> otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static boolean isValidDelimiterString (String delimiterString)
    {

/*
Verify that we have something to validate.
*/

        assert delimiterString != null;

/*
Check each delimiter character in turn.
*/

        int delimiterCount = delimiterString.length ();
        for (int index = 0; index < delimiterCount; ++index)
        {
            int delimiter = delimiterString.codePointAt (index);

/*
End-of-line characters cannot be used as delimiters, so this string does not
contain valid delimiter characters.
*/

            if (delimiter == '\r' || delimiter == '\n')
            {
                return false;
            }

/*
TODO: Check for other invalid characters?
*/

/*
If this character re-appears later in the delimiter string, then the delimiter
string is invalid.

While duplicate valid delimiter characters could be permitted, with only very
minor performance/memory issues, this is likely not what the user intended.
Consequently, the delimiter string is treated as being invalid.
*/

            if (index < delimiterCount - 1 && delimiterString.substring (index
            + 1).indexOf (delimiter) != -1)
            {
                return false;
            }
        }

/*
If we've made it this far, then the delimiter string appears to be OK.
*/

        return true;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Delimiter constructor.</p>

<p>Creates a new field delimiter instance based upon the specification
provided.</p>

@param delimiterCharacters String containing the set of characters that are to
be treated as field delimiters.  This string cannot be null but can be empty.
<em>End-of-line</em> characters, that is the <em>linefeed</em> and <em>carriage
return</em> characters, cannot be specified as delimiter characters.
Furthermore, the same character cannot appear more than once in this string.

@param treatConsecutiveDelimitersAsOne Flag indicating whether consecutive
delimiter characters are treated as a single delimiter.  If
<strong>true</strong>, then a sequence of consecutive delimiter characters is
treated as a single delimiter.  If <strong>false</strong>, then empty fields
will be reported between each pair of consecutive delimiter characters.

@param treatEolAsDelimiter Flag indicating whether <em>end-of-line
sequences</em> are to be treated as regular field delimiters.  If
<strong>true</strong>, then end-of-line sequences are treated in the same way
as the delimiters contained within delimiterCharacters; end-of-line sequences
have no special significance, and calling {@linkplain Tokenizer#acknowledgeEOL
()} after such a sequence is encountered will result in an exception.  If
<strong>false</strong>, then end-of-line sequences will terminate both fields
and consecutive delimiter sequences (if {@linkplain #treatConsecutiveAsOne} is
<strong>true</strong>) and will require that {@linkplain
Tokenizer#acknowledgeEOL ()} be called to acknowledge the end of the current
line before further data can be read.

@throws NullPointerException If delimiterCharacters is null.

@throws BadDelimiterException If delimiterCharacters contains an
<em>end-of-line</em> character, or contains the same character twice.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Delimiter (String delimiterCharacters, boolean
    treatConsecutiveDelimitersAsOne, boolean treatEolAsDelimiter)
    {

/*
If we do not have a delimiter string to work with, then throw the null pointer
exception.
*/

        if (delimiterCharacters == null)
        {
            throw new NullPointerException ();
        }

/*
If we cannot validate the contents of the delimiter string, then throw a bad
delimiter exception.
*/

        if (!isValidDelimiterString (delimiterCharacters))
        {
            throw new BadDelimiterException (delimiterCharacters);
        }

/*
Store the delimiter configuration.
*/

        this.delimiters = delimiterCharacters;
        this.treatConsecutiveAsOne = treatConsecutiveDelimitersAsOne;
        this.eolIsDelimiter = treatEolAsDelimiter;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve next field from the indicated stream.</p>

<p>The field starts at the reader's current character and terminates when a
delimiter or <em>end-of-line</em> sequence is detected.</p>

@param reader Text reader from which the field is to be retrieved.

@return Field read from the reader.

@throws IOException If an error occurs when reading characters from the reader.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final Field readNextField (TextReader reader)
    throws IOException
    {

/*
Guard against a subsequent NullPointerException by verifying that reader is not
null.
*/

        assert (reader != null);

/*
Keep processing characters from the reader until we find a delimiter, a newline
character (TextReader will only ever report end-of-line sequences with this
single character) or the end of the stream.  However, not that the read()
method can throw an IOException.
*/

        StringBuffer field = new StringBuffer ();
        for (;;)
        {
            int character = reader.read ();
            assert character != '\r';

/*
Examine the character just read.
*/

            switch (character)
            {

/*
A value of -1 indicates that there is no more data to be read from the stream.
Return what we have signaling that an end-of-file/end-of-stream terminated the
field.
*/

            case -1:
                return new Field (field, FieldTerminator.FT_EOF);

/*
If this is a newline character, then we have encountered an end-of-line (EOL)
sequence.

If we are not treating EOL sequences as delimiters, then return what we have
signaling that an EOL sequence terminated the field.
*/

            case '\n':
                if (!this.eolIsDelimiter)
                {
                    return new Field (field, FieldTerminator.FT_EOL);
                }

/*
OK.  So to reach this point, we have just read an EOL sequence, but we need to
treat it as a regular delimiter.  Process any consecutive delimiters before
returning what we have with a delimiter terminator.
*/

                eatDelimiters (reader);
                return new Field (field, FieldTerminator.FT_DELIMITER);

/*
Default processing.  If this character is a delimiter, then we've found the end
of the field.  Process any consecutive delimiters before returning what we have
with a delimiter terminator.
*/

            default:
                if (isDelimiter (character))
                {
                    eatDelimiters (reader);
                    return new Field (field, FieldTerminator.FT_DELIMITER);
                }
                
/*
OK.  If we've made it this far, then this character is part of the field - add
it to the field's content.
*/

                field.append (character);
                break;
            }
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Determines whether a character is a delimiter.</p>

<p>This procedure does not consider <em>end-of-line</em> characters as
delimiters, even if the {@linkplain #eolIsDelimiter} field is
<strong>true</strong>.</p>

@param character Unicode of the character to be examined.

@return <strong>true</strong> if the character is a delimiter or
<strong>false</strong> otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final boolean isDelimiter (int character)
    {

/*
If this character is contained with the delimiter string, then it is a
delimiter - otherwise it isn't.
*/

        if (this.delimiters.indexOf (character) > -1)
        {
            return true;
        }

/*
OK.  This isn't a delimiter character.
*/

        return false;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Processes consecutive delimiters in the stream.</p>

<p>If consecutive delimiters are not being treated as one, this function does
nothing.</p>

<p>The <em>newline</em> character is considered a delimiters if the {@linkplain
#eolIsDelimiter} flag is <strong>true</strong>.</p>

<p>Any non-delimiter characters read will be pushed back onto the stream.</p>

@param reader Text reader being processed.

@throws IOException If an error occurs when reading characters from the reader.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final void eatDelimiters (TextReader reader)
    throws IOException
    {

/*
If we're not treating consecutive delimiters as one, return now.
*/

        if (!this.treatConsecutiveAsOne)
        {
            return;
        }

/*
Keep reading until we find a non-delimiter character.
*/

        for (;;)
        {

/*
Peek at the next character.  We have to read it only if it is a delimiter.
*/

            int character = reader.peek ();
            assert character != '\r';

/*
Examine the character read.
*/

            int delimiter;
            switch (character)
            {

/*
If the stream has finished, then we're done.
*/

            case -1:
                return;

/*
Is this a newline character?  If we're not treating them as delimiters, then
we're done.  Otherwise, read the character and keep going.
*/

            case '\n':
                if (!this.eolIsDelimiter)
                {
                    return;
                }
                delimiter = reader.read ();
                assert character == delimiter;
                break;

/*
Otherwise, if this is not a delimiter character, then we're done.
*/

            default:
                if (!isDelimiter (character))
                {
                    return;
                }

/*
OK.  So this is a delimiter.  Eat the character and keep going.
*/

                delimiter = reader.read ();
                assert character == delimiter;
            }
        }
    }
}
