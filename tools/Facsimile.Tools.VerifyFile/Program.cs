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

C# source file for the Program class, and associated elements, that are
integral members of the Facsimile.Tools.VerifyFile namespace.
===============================================================================
*/

namespace Facsimile.Tools.VerifyFile
{

//=============================================================================
/**
<summary>Main class for the VerifyFile tool.</summary>

<remarks>The VerifyFile tool is used to check (as far as is possible) that
source files belonging to the Facsimile project conform to the published coding
standards.

<para>This is a command line application that verifies the contents of a file
(or set of files matching a wildcard specification).  Before files can be
committed to the source repository, they must be verified as OK by this
application.</para>

<para>The command is executed as follows:</para>

<para><c>VerifyFile FILE[ ...]</c></para>

<para>where <c>FILE</c> is a wilcard file specification.  All files matching
the wildcard specification will be verified.</para>

<para>The program terminates with a 0 exit code if all files are verified OK,
or 1 if any file fails to be verified correctly, or if an error
occurs.</para>

<para>Note that this application is not internationalized.  It is assumed that
it will be used by Facsimile developers only, and the language of Facsimile
development is (US) English.</para></remarks>
*/
//=============================================================================

    public static class Program:
        System.Object
    {

/**
<summary>Map associating file extensions to corresponing file
verifier.</summary>
*/

        private static System.Collections.Generic.Dictionary <string,
        FileVerifier> verifierMap;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Program ()
        {

/*
First off, construct the new file verification map.
*/

            verifierMap = new System.Collections.Generic.Dictionary <string,
            FileVerifier> ();

/*
Now populate it by associating support file extensions with the corresponding
file verification instance (which we'll need to create here too).
*/

            verifierMap [".cs"] = new CSharpVerifier ();
            verifierMap [".build"] = new NAntBuildVerifier ();
            //verifierMap [".resx"] = new ResXBuildVerifier ();
            verifierMap [".txt"] = new TextResourceVerifier ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Main routine.</summary>

<remarks>The program starts executing here.</remarks>

<param name="args">A <see cref="System.String" /> array containing the list of
arguments passed to the program.  As we do not have any options, each argument
is a file, or a file wildcard specification.</param>

<returns>A <see cref="System.Int32" /> value indicating the status of the
operation.  The returned value is 0 if all files were verified OK, or 1 if a
single file failed to be verified or if an error occurred.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [System.STAThread]
        public static int Main (string [] args)
        {

/*
If we have no arguments, then this is considered an error.
*/

            if (args.Length == 0)
            {
                Error ("No files supplied...", true);
                return 1;
            }

/*
Flag indicating whether we found any errors.  If the application is to succeed,
this must remain set until the program finishes.
*/

            bool noErrors = true;

/*
Each argument is a file wildcard specification.  Pass it to the file verifier.
We try to 
*/

            foreach (string file in args)
            {
                if (!MatchAndVerifyFile (file))
                {
                    noErrors = false;
                }
            }

/*
If all files were verified OK and no errors occurred, then return 0.  Otherwise
we return 1.
*/

            if (noErrors)
            {
                return 0;
            }
            return 1;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Match files to wildcard expression, and verify each matching file.</summary>

<remarks>It is considered an error if there are no files matching the wildcard
specification.</remarks>

<param name="spec">A file wildcard specification that needs to be matched to
one or more existing files.  Files that are matched are then verified.</param>

<returns>A <see cref="System.Boolean" /> that is true if all of the matching
files were verified OK, or false if a single file fails verification or if an
error occurs.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static bool MatchAndVerifyFile (string spec)
        {

/*
OK.  This requires a bit of explanation.

When we call the System.IO.Directory.GetFiles function, the first argument is
our current directory and the second is the file spec, which must be relative
to the current directory - provided that it includes sub-directories of the
current directory only.  If the spec contains ".." or is an absolute path, then
we'll get an exception.

I'd prefer to use one of the System.IO.Path function, but none of them appear
capabaly of handling wildcards.  Pah!  .NET does not appear to have been
designed to support console applications, but there you go...

This is adequate for our purposes, in which we'll run the utility from the NAnt
build script, but I just want you to be fairly warned should you try to use it
to do anything else.

In any case, first we need to catch any exceptions that arise.
*/

            string [] files;
            try
            {

/*
Now retrieve the set of matching files.
*/

                files = System.IO.Directory.GetFiles
                (System.IO.Directory.GetCurrentDirectory (), spec);
            }

/*
If we caught a null argument exception, then our spec must be null (can rule
out a null directory).  Can't think how this might happen, but apparently, it
just did.
*/

            catch (System.ArgumentNullException)
            {
                Error ("Null file specification");
                return false;
            }

/*
If we caught an argument exception, then our spec is invalid (can rule out an
invalid path).
*/

            catch (System.ArgumentException)
            {
                Error ("Invalid spec: " + spec);
                return false;
            }

/*
If we caught the path too long exception, then that's an interesting problem,
because there's nothing we can do about it!
*/

            catch (System.IO.PathTooLongException)
            {
                Error ("Path too long: " + spec);
                return false;
            }

/*
Otherwise, have we been found out with too few rights?
*/

            catch (System.UnauthorizedAccessException)
            {
                Error ("No access rights:" + spec);
                return false;
            }

/*
If we didn't find any matching files, then report an error and return false.
*/

            if (files.Length == 0)
            {
                Error ("No files match: " + spec);
                return false;
            }

/*
Finally, we have some files to work on!  Go through each matching file and
verify them.
*/

            bool noError = true;
            foreach (string file in files)
            {
                if (!VerifyFile (file))
                {
                    noError = false;
                }
            }
            return noError;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify a file according to its file extension.</summary>

<remarks>The file's extension is used to determine how the file ought to be
formatted.  An appropriate verification object is then used to perform the
comparison.</remarks>

<param name="file">The name of the file to be verified.</param>

<returns>A <see cref="System.Boolean" /> that is true if the file was verified
OK, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool VerifyFile (string file)
        {

/*
Firstly, retrieve the file extension for this file.
*/

            string extension = System.IO.Path.GetExtension (file);

/*
Now do a look-up on the extension to find the appropriate file verifier.  If
there is no such object, then report an error and return false.
*/

            if (!verifierMap.ContainsKey (extension))
            {
                Error ("Unsupported extension: " + file);
                return false;
            }

/*
Now retrieve the corresponding file verification object and return the status
of the retrieval operation.
*/

            FileVerifier verifier = verifierMap [extension];
            System.Diagnostics.Debug.Assert (verifier != null);
            return verifier.VerifyFile (file);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Report an error to the standard output.</summary>

<remarks>No usage information is appended.</remarks>

<param name="errorMessage">A <see cref="System.String" /> containing the error
message to be displayed.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static void Error (string errorMessage)
        {
            Error (errorMessage, false);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Report an error to the standard output, with optional usage
information.</summary>

<remarks>Usage information should only be appended if a fault with program
usage has been detected and if the program is going to terminate
immediately.</remarks>

<param name="errorMessage">A <see cref="System.String" /> containing the error
message to be displayed.</param>

<param name="addUsage">A <see cref="System.Boolean" /> that is true if program
usage information is to be appended to the error message, or false if no such
information is required.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void Error (string errorMessage, bool addUsage)
        {

/*
Output the supplied error message.
*/

            System.IO.TextWriter errorOutput = System.Console.Error;
            errorOutput.WriteLine (errorMessage);

/*
If we need to append usage information, then do so.
*/

            if (addUsage)
            {
                errorOutput.WriteLine ("Usage: VerifyFile FILE[ ...]");
            }
        }
    }
}
