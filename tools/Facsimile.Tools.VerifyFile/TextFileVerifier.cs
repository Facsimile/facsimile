/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2008, Michael J Allen.

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program.  If not, see <http://www.gnu.org/licenses/>.

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

C# source file for the TextFileVerifier class, and associated elements, that
are integral members of the Facsimile.Tools.VerifyFile namespace.
===============================================================================
*/

namespace Facsimile.Tools.VerifyFile
{

//=============================================================================
/**
<summary>Abstract text file verification class.</summary>

<remarks>This class implements features and tests required to validate text
files.

<para>Text files are constructed of printable characters and a limited number
of control characters (line feed, carriage return, horizontal tab, and - rarely
form feed) and are arranged into lines.  Each line is terminated by an
end-of-line sequence that depends upon the host operating platform.  For
instance, text files on Microsoft Windows use carriage return + line feed as
the end-of-line sequence, whereas Linux (and other *nix systems) use just a
line feed.  Older MacIntosh systems used just a carriage return.</para>

<para>Text files also have an associated encoding, that governs how each
individual character is coded in the text file.</para>

<para>When validating text files, the set of characters present, the encoding,
the end-of-line sequence used (which should match the host platform) and the
line length are all checked.  Specific types of text file format (such as XML
files) must also meet addition validation requirements.</para></remarks>
*/
//=============================================================================

    public abstract class TextFileVerifier:
        FileVerifier
    {

/**
<summary>The default encoding to be used when processing files.</summary>
*/

        private static readonly System.Text.Encoding defaultEncoding;

/**
<summary>The current line number of the file being processed.</summary>

<remarks>Line numbering commences at 1 for the first line.</remarks>
*/

        private ulong line;

/**
<summary>The current column number of the file being processed.</summary>

<remarks>Column numbering commences at 1 for the first character.  Note that
the byte order mark (BOM), if any, is not included in the column count for the
first line.</remarks>
*/

        private ulong column;

/**
<summary>Code for the previous character read.</summary>

<remarks>This value is 0 initially, and once the end-of-line character has been
read.

<para>Note that not all Unicode characters can be encoded within a <see
cref="System.Int32" /> - but is is assumed that these additional characters
will not appear in any Facsimile source files.</para></remarks>
*/

        private int previousChar;

/**
<summary>Perform platform-specific end-of-line processing.</summary>
*/

        private CheckValidEndOfLine checkValidEndOfLine;

/**
<summary>Flag indicating that bad EOL reported.</summary>

<remarks>This is set to true once a bad end-of-line sequence has been detected
in the file.  Once a failure has been detected, further error notifications
about bad end-of-line sequences is inhibited.</remarks>
*/

        bool foundBadEOL;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialize static class members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static TextFileVerifier ()
        {

/*
The default encoding, should we not find a byte-order-mark at the start of the
file, will be ISO Western European.
*/

            defaultEncoding = System.Text.Encoding.GetEncoding ("iso-8859-1");
            System.Diagnostics.Debug.Assert (defaultEncoding != null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File constructor.</summary>

<remarks>Prepare the file for verification.</remarks>

<param name="fileName">The name of the file, including any path information
(relative or absolute) required to locate it.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public TextFileVerifier (string fileName):
            base (fileName)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (defaultEncoding != null);

/*
Initialise the current line and column number as appropriate.
*/

            line = 1;
            column = 0;

/*
Set up the delegate for checking end-of-line use on the current platform.
*/

            if (System.Environment.OSVersion.Platform ==
            System.PlatformID.Unix)
            {
                checkValidEndOfLine += CheckValidUnixEndOfLine;
            }
            else
            {
                checkValidEndOfLine += CheckValidPCEndOfLine;
            }

/*
So far, we have yet to find a bad end-of-line sequence.
*/

            foundBadEOL = false;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check for &amp; validate end-of-line sequence.</summary>

<remarks>Check for the presence of an end-of-line sequence and, if found,
validate whether this sequence is valid for the host platform).

<para>This is a delegated function that invokes the correct end-of-line
verification function for the current platform.</para></remarks>

<param name="stream">A <see cref="System.IO.StreamReader" /> instance
representing the contents of the file to be verified.</param>

<param name="thisChar">A <see cref="System.Int32" /> storing the Unicode of
the character that was just read from the file.  Note that not all Unicode
characters can be encoded within this type - but is is assumed that these
additional characters will not appear in any Facsimile source files.</param>

<returns>A <see cref="System.Boolean" /> that is true if an end-of-line
sequence is detected (whether valid or not) or false if no end-of-line sequence
is found.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private delegate bool CheckValidEndOfLine (System.IO.StreamReader
        stream, int thisChar);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Read and verify the file.</summary>

<remarks>This method opens the associated file and reads it, character by
character, to ensure that it is formatted correctly and is as valid as
possible.

<para>The file is read using a <see cref="System.IO.StreamReader" /> object
whose default character encoding is 8-bit ISO Western European (iso-8859-1).
If an appropriate byte-order mark (BOM) is found at the head of the file, then
the encoding will be changed accordingly (to either UTF-8 or UTF-16 - both of
which are Unicode encodings).  Depending upon which encoding was detected, the
characters obtained from the stream will then be either 8-bit bytes (Western
European) or 16-bit words (UTF-8, UTF-16).  Part of the file verification
process is to verify that the encoding was detected correctly.  Incorrectly
encoded files will fail verification.</para></remarks>

<returns>A <see cref="System.Boolean" /> that is true if the file was verified
OK, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public override bool VerifyFile ()
        {

/*
Open the file, checking for potential errors along the way.  Do not catch
exceptions that should not arise (for example, we should not get a file not
found error, because we just searched for it).
*/

            try
            {

/*
Make sure that we close the file should anything go awry.
*/

                using (System.IO.StreamReader stream = new
                System.IO.StreamReader (FileName, defaultEncoding, true))
                {

/*
Read the file and report its status.
*/

                    ReadStream (stream);
                }
            }

/*
This exception arises if we do not have permission to open this file.
*/

            catch (System.Security.SecurityException)
            {
                Error ("Cannot open - insufficient permission");
            }

/*
Return file file valid flag.
*/

            return IsValid;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Read the file and process it.</summary>

<remarks>A single verification error should not prevent the scan from
completing.  Each time an error is found, an informative message should be
output (including line and column infomation), but the scan should
continue.</remarks>

<param name="stream">A <see cref="System.IO.StreamReader" /> instance
representing the contents of the file to be verified.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void ReadStream (System.IO.StreamReader stream)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (stream != null);

/*
First-off, verify that the file encoding detected is valid.
*/

            VerifyEncoding (stream);

/*
OK.  Now read each byte, and have the derived class verify it.  We'll keep
reading until either an exception occurs or until we reach the end of the file.
The column number is incremented with each character that we read.
*/

            int thisChar;
            while ((thisChar = stream.Read ()) != -1)
            {
                ++column;
                VerifyChar (stream, thisChar);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify whether encoding is correct for this file.</summary>

<remarks>If the encoding does not match the required encoding, then this is an
error and file verification fails.</remarks>

<param name="stream">The <see cref="System.IO.StreamReader" /> instance whose
encoding is to be verified.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void VerifyEncoding (System.IO.StreamReader stream)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (stream != null);

/*
Before we can verify the encoding, we need to read at least one character from
the stream.  One nice way to do this is to "peek" at the next character, so
that it is read, but with the file pointer remaining unchanged.  In this way,
we can update the file encoding (which ought to be done when the stream is
opened by the StreamReader constructor, IMHO) without corrupting the file read
operations.

Note: If we do not do this, then the current encoding on the stream will always
be the encoding that we supplied as a default, rather than the file's actual
encoding - resulting in incorrect encoding validation.
*/

            stream.Peek ();

/*
Confirm that the encoding on the stream is what we'd expect.  If not, output an
error and record that the file is invalid.
*/

            if (stream.CurrentEncoding != Encoding)
            {
                Error ("Has " + stream.CurrentEncoding.EncodingName +
                " encoding (" + Encoding.EncodingName + " required)");
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify whether a character is valid for this file.</summary>

<remarks>If the character is invalid, or is in an inappropriate position in the
file, then it will fail verification (by reporting an error and clearing the
<see cref="FileVerifier.IsValid" /> flag.</remarks>

<param name="stream">A <see cref="System.IO.StreamReader" /> instance
representing the contents of the file to be verified.</param>

<param name="thisChar">A <see cref="System.Int32" /> storing the Unicode of
the character that was just read from the file.  Note that not all Unicode
characters can be encoded within this type - but is is assumed that these
additional characters will not appear in any Facsimile source files.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void VerifyChar (System.IO.StreamReader stream, int thisChar)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert (stream != null);
            System.Diagnostics.Debug.Assert (thisChar >= 0);

/*
Check for an end-of-line sequence and, if found, validate it for the current
platform.  This function will return true if an end-of-line sequence is found,
regardless of whether it is valid for the current platform or not.  An invalid
end-of-line sequence is indicated by the IsValid flag being cleared.

This is a function delegate that will be resolved to the appropriate function
for the current platform only.
*/

            if (checkValidEndOfLine (stream, thisChar))
            {
                return;
            }

/*
We've just filtered out the end-of-line characters.  Now we need to check that
there are no other "control" characters present.  If this is a control
character (a character belonging to the Unicode Control category), then report
an error.

Note: Tab characters are control characters, but they are typically treated as
whitespace.  In Facsimile files, we do not expect to find any tab characters,
since they can alter the visual appearance of a file depending upon how the tab
width is defined.  By using spaces instead of tabs, the visual appearance of
text files is not alterered.  Also, by excluding tab characters, the complexity
of this utility is simplified.  ;-)
*/

            if (char.IsControl ((char) thisChar))
            {
                Error (string.Format ("Control character found: 0x{0:X2}",
                thisChar));
            }

/*
Otherwise, have the derived class verify this character.
*/

            else
            {
                IsValidChar (thisChar);
            }

/*
OK.  Regardless of whether this character is valid, copy it to the previous
character.
*/

            previousChar = thisChar;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check for &amp; validate end-of-line sequence (UNIX).</summary>

<remarks>Check for the presence of an end-of-line sequence and, if found,
validate whether this sequence is valid for UNIX (and UNIX-like operating
systems such as Linux and BSD-derived systems).

<para>A valid UNIX end-of-line sequence is a single linefeed character.</para>

<para>This function should only be called when the program executes on a UNIX
or UNIX-like platform.</para></remarks>

<param name="stream">A <see cref="System.IO.StreamReader" /> instance
representing the contents of the file to be verified.</param>

<param name="thisChar">A <see cref="System.Int32" /> storing the Unicode of
the character that was just read from the file.  Note that not all Unicode
characters can be encoded within this type - but is is assumed that these
additional characters will not appear in any Facsimile source files.</param>

<returns>A <see cref="System.Boolean" /> that is true if an end-of-line
sequence is detected (whether valid or not) or false if no end-of-line sequence
is found.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private bool CheckValidUnixEndOfLine (System.IO.StreamReader stream,
        int thisChar)
        {

/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert
            (System.Environment.OSVersion.Platform == System.PlatformID.Unix);
            System.Diagnostics.Debug.Assert (System.Environment.NewLine ==
            "\n");
            System.Diagnostics.Debug.Assert (stream != null);
            System.Diagnostics.Debug.Assert (thisChar >= 0);

/*
If this character is a linefeed character, then it is a valid end-of-line
sequence for this platform.
*/

            if (thisChar == '\n')
            {
                UpdateAfterEndOfLine ();
                return true;
            }

/*
Otherwise, if this is a carriage return character, then it is an invalid
end-of-line sequence for this platofrm.  The only issue is whether the next
character is a linefeed or not.
*/

            if (thisChar == '\r')
            {

/*
If the next character on the stream is a linefeed, then we have a PC
end-of-line sequence.  That is an error.
*/

                if (stream.Peek () == '\n')
                {
                    stream.Read ();
                    if (!foundBadEOL)
                    {
                        Error ("CR-LF end-of-line sequence detected when LF " +
                        "sequence expected.");
                        foundBadEOL = true;
                    }
                }

/*
Otherwise, we have an old Mac-style end-of-line sequence.  That too is an
error.
*/

                else
                {
                    if (!foundBadEOL)
                    {
                        Error ("CR end-of-line sequence detected when LF " +
                        "sequence expected.");
                        foundBadEOL = true;
                    }
                }

/*
Update the end-of-line information.
*/

                UpdateAfterEndOfLine ();
                return true;
            }

/*
Looks like this is not an end-of-line sequence.
*/

            return false;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check for &amp; validate end-of-line sequence (PC).</summary>

<remarks>Check for the presence of an end-of-line sequence and, if found,
validate whether this sequence is valid for PC (DOS, Windows, OS/2 operating
system families).

<para>A valid PC end-of-line sequence is a carriage return character followed
by a linefeed character.</para>

<para>This function should only be called when the program executes on a PC
(DOS, Windows, OS/2) platform.</para></remarks>

<param name="stream">A <see cref="System.IO.StreamReader" /> instance
representing the contents of the file to be verified.</param>

<param name="thisChar">A <see cref="System.Int32" /> storing the Unicode of
the character that was just read from the file.  Note that not all Unicode
characters can be encoded within this type - but is is assumed that these
additional characters will not appear in any Facsimile source files.</param>

<returns>A <see cref="System.Boolean" /> that is true if an end-of-line
sequence is detected (whether valid or not) or false if no end-of-line sequence
is found.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private bool CheckValidPCEndOfLine (System.IO.StreamReader stream, int
        thisChar)
        {
/*
Sanity checks.
*/

            System.Diagnostics.Debug.Assert
            (System.Environment.OSVersion.Platform != System.PlatformID.Unix);
            System.Diagnostics.Debug.Assert (System.Environment.NewLine ==
            "\r\n");
            System.Diagnostics.Debug.Assert (stream != null);
            System.Diagnostics.Debug.Assert (thisChar >= 0);

/*
If this is a carriage return character, then we need to look at the next
character to determine if it is an invalid end-of-line sequence for this
platform.
*/

            if (thisChar == '\r')
            {

/*
If the next character on the stream is not a linefeed, then we have an invalid
PC end-of-line sequence.
*/

                if (stream.Peek () != '\n')
                {
                    if (!foundBadEOL)
                    {
                        Error ("CR end-of-line sequence detected when CR-LF " +
                        "sequence expected.");
                        foundBadEOL = true;
                    }
                }

/*
Otherwise, we must consume the linefeed character.
*/

                else
                {
                    stream.Read ();
                }

/*
Update the end-of-line information.
*/

                UpdateAfterEndOfLine ();
                return true;
            }

/*
Otherwise, if this character is a linefeed character, then it is an invalid
end-of-line sequence for this platform.
*/

            if (thisChar == '\n')
            {
                if (!foundBadEOL)
                {
                    Error ("LF end-of-line sequence detected when CR-LF " +
                    "sequence expected.");
                    foundBadEOL = true;
                }
                UpdateAfterEndOfLine ();
                return true;
            }

/*
Looks like this is not an end-of-line sequence.
*/

            return false;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Handle end-of-line detection.</summary>

<remarks>This function is called to notify interested parties (derived classes)
that an end-of-line condition has been detected.  Counts (current line &amp;
column number, etc.) are also updated.  The previous character read is
cleared.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void UpdateAfterEndOfLine ()
        {

/*
If the previous character is whitespace, then we have trailing whitespace at
the end of the line.  This is not allowed.
*/

            if (char.IsWhiteSpace ((char) previousChar))
            {
                Error ("Trailing whitespace at end of line.");
            }

/*
Also check the length of this line.  There shouldn't be more than 79 printable
characters on a line (with the end-of-line sequence making up the 80th), the
current column should not be more than 80.
*/

            if (column > MaximumLineLength)
            {
                Error (string.Format ("Line too long.  Has {0} characters, " +
                "maximum allowed is {1}.", column - 1, MaximumLineLength - 1));
            }

/*
Signal derived classes that we have detected an end-of-line character.
*/

            OnEndOfLine ();

/*
Increment the line number and reset the column number.
*/

            ++line;
            column = 0;

/*
Our new previous character is 0.
*/

            previousChar = 0;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify an individual character.</summary>

<remarks>This method is called to verify a single character read from the file.

<para>This function should be overridden by derived classes as appropriate.
The first thing each derived class should do is to call the parent's version of
the function.  If the call to the parent's function results in a failure, then
there is no need to perform additional tests.</para>

<para>This default version of the function does nothing and reports
success.</para></remarks>

<param name="thisChar">A <see cref="System.Int32" /> storing the Unicode of
the character to be verified.  Note that not all Unicode characters can be
encoded within this type - but is is assumed that these additional characters
will not appear in any Facsimile source files.</param>

<returns>A <see cref="System.Boolean" /> that is true if the character was
verified successfully or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual bool IsValidChar (int thisChar)
        {
            return true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Act upon end of line notification.</summary>

<remarks>This method is called when an end-of-line sequence (whether valid or
otherwise, is detected upon the stream.  If provides an opportunity for derived
classes to act upon this fact.

<para>This function should be overridden by derived classes as appropriate.
The first thing each derived class should do is to call the parent's version of
the function.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual void OnEndOfLine ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Encoding property.</summary>

<remarks>The required encoding for this type of file.

<para>This property should be overridden by derived files to identify the
correct encoding for that type of file.</para></remarks>

<value>A <see cref="System.Text.Encoding" /> instance identifying the encoding
that is required for this type of file.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected abstract System.Text.Encoding Encoding
        {
            get;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File context property.</summary>

<remarks>Provides information about the location that we're currently
processsing in the associated file.

<para>For text files, the context is made up of the current line and column
numbers.</para></remarks>

<value>A <see cref="System.String" /> instance containing the file's location
context in a human readable format.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected sealed override string FileContext
        {
            get
            {
                return string.Format ("[{0},{1}]", line, column);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Previous character read.</summary>

<remarks>Provides the code of the previous character read, or 0 if we've yet to
read the second character of a file or if we're at the start of a new
line.</remarks>

<value>A <see cref="System.Int32" /> containing the Unicode of the previous
character read, or 0 if there is no previous character.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected int PreviousCharacter
        {
            get
            {
                return previousChar;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Current column number.</summary>

<remarks>Identifies the column position of the current character.</remarks>

<value>A <see cref="System.UInt64" /> value identifying the column number of
the current character.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual ulong Column
        {
            get
            {
                return column;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Maximum permitted line length.</summary>

<remarks>Identifies the maximum permitted line length for this type of file.

<para>Override this property to identify a line length other than the default
value of 80.  Supporting line lengths greater than 80 is discouraged,
however.</para></remarks>

<value>A <see cref="System.UInt64" /> value identifying the maximum number of
characters that may be present on a single line within this type of text
file.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected virtual ulong MaximumLineLength
        {
            get
            {
                return 80;
            }
        }
    }
}
