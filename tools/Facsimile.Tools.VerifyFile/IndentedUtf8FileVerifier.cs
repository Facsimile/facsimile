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

C# source file for the IndentedUtf8FileVerifier class, and associated elements,
that are integral members of the Facsimile.Tools.VerifyFile namespace.
===============================================================================
*/

namespace Facsimile.Tools.VerifyFile
{

//=============================================================================
/**
<summary>File verification for tabbed UTF-8-encoded files.</summary>

<remarks>This class is used to verify UTF-8-encoded files that have an indented
structure, such as XML files or C# source files.  The class verifies that files
are indented correctly.</remarks>
*/
//=============================================================================

    public abstract class IndentedUtf8FileVerifier:
        Utf8FileVerifier
    {

/**
<summary>Non-space character (Unicode 32) on line found flag.</summary>

<remarks>Determines whether the first non-space character has been found at the
beginning of each new line and uses that to determine whether data is indented
at the correct column.</remarks>
*/

        private bool nonSpaceCharFound;

/**
<summary>Indentation level.</summary>

<remarks>The number of times the current line has been indented.  This value
will be zero until the first non-space character has been found.</remarks>
*/

        private ulong indentLevel;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>File constructor.</summary>

<remarks>Prepare the file for verification.</remarks>

<param name="fileName">The name of the file, including any path information
(relative or absolute) required to locate it.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public IndentedUtf8FileVerifier (string fileName):
            base (fileName)
        {

/*
For the first line, we have yet to find a space.
*/

            nonSpaceCharFound = false;
            indentLevel = 0;
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

        protected override bool IsValidChar (int thisChar)
        {

/*
If this isn't the space character...
*/

            if (thisChar != ' ')
            {

/*
If this is first non-space character, then check that the column number is
appropriate.
*/

                if (!nonSpaceCharFound)
                {
                    nonSpaceCharFound = true;
                    if (Column % IndentSize != 1)
                    {
                        Error ("Line contents are indented at invalid " +
                        "column.");
                    }

/*
Calculate the indentation level.
*/

                    indentLevel = Column / IndentSize;
                }
            }

/*
As far as we know, characters are valid, keep scanning for other problems.
*/

            return true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Ident size.</summary>

<remarks>Identifies the number of spaces that text is indented each
occurrence.</remarks>

<value>A <see cref="System.UInt64" /> value identifying the number of spaces
per indent level.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected ulong IndentSize
        {
            get
            {
                return 4;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Indent level.</summary>

<remarks>Identifies the number of times that text on the current line was
indented.</remarks>

<value>A <see cref="System.UInt64" /> value identifying the number of times
the current line was indented.  This value will be 0 until the first non-space
character is found.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected ulong IndentLevel
        {
            get
            {
                return indentLevel;
            }
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

        protected override void OnEndOfLine ()
        {

/*
Reset the counts.
*/

            nonSpaceCharFound = false;
            indentLevel = 0;
        }
    }
}
