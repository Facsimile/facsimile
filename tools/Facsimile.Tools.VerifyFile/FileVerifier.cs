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

C# source file for the FileVerifier class, and associated elements, that are
integral members of the Facsimile.Tools.VerifyFile namespace.
===============================================================================
*/

namespace Facsimile.Tools.VerifyFile
{

//=============================================================================
/**
<summary>Abstract base class for all file verification objects.</summary>

<remarks>This class defines the basic behavior for all file verifiers, whether
text files or binary files.</remarks>
*/
//=============================================================================

    public abstract class FileVerifier:
        System.Object
    {

/**
<summary>The name of the current file.</summary>

<remarks>This field cannot be modified once initialized, and must include
appropriate path information for the file to be found.</remarks>
*/

        private readonly string file;

/**
<summary>Flag indicating whether the current file is valid or not.</summary>

<remarks>Assume that the file is valid until proven otherwise.  If this value
is still true at the end of the file verification process, then <see
cref="file" /> must be a valid file.</remarks>
*/

        private bool isValid;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File constructor.</summary>

<remarks>Prepare the file for verification.</remarks>

<param name="fileName">The name of the file, including any path information
(relative or absolute) required to locate it.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public FileVerifier (string fileName)
        {

/*
Store the file name.
*/

            file = fileName;

/*
As noted elsewhere, the file is assumed to be valid until proven otherwise.
*/

            isValid = true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Read and verify the file.</summary>

<remarks>This method opens the associated file and reads it, character by
character, to ensure that it is formatted correctly and is as valid as
possible.

<para>This method should be overridden by derived classes to implement the
appropriate read mechanism and verification tools.</para></remarks>

<returns>A <see cref="System.Boolean" /> that is true if the file was verified
OK, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public abstract bool VerifyFile ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Report a file verification error.</summary>

<remarks>This also clears the <see cref="isValid" /> flag; consequently, this
function MUST be called if a validation test fails on the file.</remarks>

<param name="message">A <see cref="System.String" /> containing the error
message to be output to the user.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected void Error (string message)
        {

/*
If the file is currently valid, then output the name of the file first.  If the
file has already been flagged as being invalid, then this error message is
merely added to the list.
*/

            if (isValid)
            {
                Program.Error (file + ":");
            }

/*
Report the problem with the file using the line, column and error message
provided.
*/

            Program.Error (string.Format ("\t@{0}: {1}", FileContext,
            message));

/*
As this is an error, the file cannot be valid.
*/

            isValid = false;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File name property.</summary>

<remarks>Provides the name of the file to be verified by this
instance.</remarks>

<value>A <see cref="System.String" /> instance containing the name of the
associated file.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected string FileName
        {
            get
            {
                return file;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File validity flag.</summary>

<remarks>Reports the current status of the associated file's
validity.

<para>The file is only considered valid if this flag is true after the file has
been read.  If this flag ever becomes false, then the file is definitely
invalid.</para>

<para>The only means of changing the validity flag (to false) is to raise an
error by calling the <see cref="Error (string)" /> routine.</para></remarks>

<value>A <see cref="System.Boolean" /> value that is true if a validation error
has yet to be found, or false if the file is definitely invalid.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected bool IsValid
        {
            get
            {
                return isValid;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File context property.</summary>

<remarks>Provides information about the location that we're currently
processsing in the associated file.

<para>This should be overridden by derived classes to provide information
appropriate to the type of file.</para></remarks>

<value>A <see cref="System.String" /> instance containing the file's location
context in a human readable format.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected abstract string FileContext
        {
            get;
        }
    }
}
