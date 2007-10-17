/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

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

C# source file for the FileVerifier class, and associated elements, that are
integral members of the Facsimile.Tools.VerifyFile namespace.
===============================================================================
*/

namespace Facsimile.Tools.VerifyFile
{

//=============================================================================
/**
<summary>Abstract base class for all file verification objects.</summary>

<remarks>This class defines the basic behavior for all file
verifiers.</remarks>
*/
//=============================================================================

    public abstract class FileVerifier:
        System.Object
    {

/**
<summary>The default encoding to be used when processing files.</summary>
*/

        private readonly System.Text.Encoding defaultEncoding;

/**
<summary>Flag indicating whether we're currently processing a file or
not.</summary>
*/

        private bool processingFile;

/**
<summary>The name of the current file.</summary>
*/

        private string fileName;

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
<summary>Flag indicating whether the current file is valid or not.</summary>
*/

        private bool isValid;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public FileVerifier ()
        {

/*
Our default encoding - should we not find a byte-order-mark at the start of the
file - will be ISO Western European.
*/

            defaultEncoding = System.Text.Encoding.GetEncoding ("iso-8859-1");
            System.Diagnostics.Debug.Assert (defaultEncoding != null);

/*
The following are used on a per-file, rather than a per-instance, basis - so
just initialise to values that are as invalid as possible.
*/

            processingFile = true;
            fileName = null;
            line = 0;
            column = 0;
            isValid = false;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Read and verify a file.</summary>

<remarks>This file opens the file and reads it, character by character, to
ensure that it is formatted correctly.

<para>The file is read using a <see cref="System.IO.StreamReader" /> object
whose default character encoding is 8-bit ISO Western European (iso-8859-1).
If an appropriate byte-order mark (BOM) is found at the head of the file, then
the encoding will be changed accordingly (to either UTF-8 or UTF-16 - both of
which are Unicode encodings).  Depending upon which encoding was detected, the
characters obtained from the stream will then be either 8-bit bytes (Western
European) or 16-bit words (UTF-8, UTF-16).  Part of the file verification
process is to verify that the encoding was detected correctly.  Incorrectly
encoded files will fail verification.</para></remarks>

<param name="file">A <see cref="System.String" /> identifying the file to be
read.</param>

<returns>A <see cref="System.Boolean" /> that is true if the file was verified
OK, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool VerifyFile (string file)
        {

/*
Firstly, initialise the file summary information for this file.  We'll assume
that the file is valid until we find evidence to the contrary.
*/

            System.Diagnostics.Debug.Assert (!processingFile);
            processingFile = true;
            fileName = file;
            line = 1;
            column = 0;
            isValid = true;

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
                System.IO.StreamReader (file, defaultEncoding, true))
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
                Program.Error (file + ": Cannot open (insufficient " +
                "permission)");
                isValid = false;
            }

/*
No matter what happens, restore the file summary information to invalid values.
*/

            finally
            {
                processingFile = false;
                fileName = null;
                line = 0;
                column = 0;
            }

/*
Return file file valid flag.
*/

            return isValid;
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
First-off, verify that the file encoding detected is valid.
*/

            VerifyEncoding (stream);

/*
OK.  Now read each line, and have the derived class verify it.
*/

/*
UP TO HERE
            try
            {
            }
            catch ()
            {
            }
*/
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

            System.Diagnostics.Debug.Assert (processingFile);
            System.Diagnostics.Debug.Assert (stream != null);

/*
Before we can verify the encoding, we need to read at least one character from
the stream.  One nice way to do this is to "peek" at the next character, so
that it is read, but so that the file pointer remains unchanged.  In this way,
we can update the file encoding (which ought to be done when the stream is
opened by the StreamReader constructor, IMHO) without corrupting the file read
operations.

Note: If we do not do this, then the current encoding on the stream will always
be the encoding that we supplied as a default, resulting (more often than not)
in a failure of the 
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
<summary>Report a file verification error.</summary>

<remarks>This also clears the <see cref="isValid" /> flag.</remarks>

<param name="message">A <see cref="System.String" /> containing the error
message to be output to the user.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected void Error (string message)
        {

/*
If the file is currently valid, then output the name of the file first.
*/

            if (isValid)
            {
                Program.Error (fileName + ":");
            }

/*
Report the problem with the file using the line, column and error message 
provided.
*/

            Program.Error (string.Format ("\t@({0},{1}): {2}", line, column,
            message));

/*
As this is an error, the file cannot be valid.
*/

            isValid = false;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Encoding property.</summary>

<remarks>The required encoding for this type of file.

<para>This should be overridden by derived classes to specify the required
encoding for each type of file.</para></remarks>

<value>A <see cref="System.Text.Encoding" /> instance identifying the encoding
that is required for this type of file.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected abstract System.Text.Encoding Encoding
        {
            get;
        }
    }
}
