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

C# source file for the ArgumentTypeException class, and associated elements,
that are integral members of the Facsimile.Common package.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Exception thrown when the type of an argument is invalid.</summary>

<remarks>This overridden version of <see cref="System.ArgumentException" />
provides additional information about the type of the argument in order to
assist with debugging.

<para>Say that an argument is declared as <see cref="System.Object" />, but a
more specific type is required.  If an incompatible type is then supplied, then
this exception will be thrown.  This kind of problem can occur frequently when
implementing non-generic interfaces.</para></remarks>
*/
//=============================================================================

    public sealed class ArgumentTypeException:
        System.ArgumentException
    {

/**
<summary>Object array.</summary>

<remarks>This array is initialised by the constructor to contain the following
values:

<list type="number">
    <item>
        <description>The name of the method argument whose value is the wrong
        type.  The type of this member should be <see cref="System.String"
        />.</description>
    </item>
    <item>
        <description>The declared type of the argument, which is enforced by
        the compiler.  Both of the subsequent types should be derived from
        this type.  The type of this member should be <see cref="System.String"
        />.</description>
    </item>
    <item>
        <description>The required type of the argument, which must be a
        sub-type of the declared type.  The type of this member should be <see
        cref="System.String" />.</description>
    </item>
    <item>
        <description>The actual type of the argument, which must be a sub-type
        of the declared type.  The type of this member should be <see
        cref="System.String" />.</description>
    </item>
</list></remarks>
*/

        private readonly System.Object [] argumentData;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor.</summary>

<remarks>Processes the relevant invalid argument data to be formatted as part
of the exception's error message.</remarks>

<param name="argumentName">A <see cref="System.String" /> reference identifying
the name of the argument that was found to be invalid.</param>

<param name="argumentType">A <see cref="System.Type" /> instance holding the
the declared type of <paramref name="argumentName" />.</param>

<param name="argumentRequiredType">A <see cref="System.Type" /> instance
identifying the expected type of <paramref name="argumentName" />.  This must
be a sub-type of <paramref name="argumentType" />.</param>

<param name="argumentActualType">A <see cref="System.Type" /> instance
identifying the actual supplied type of <paramref name="argumentName" />.  This
must be a sub-type of <paramref name="argumentType" />.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal ArgumentTypeException (string argumentName, System.Type
        argumentType, System.Type argumentRequiredType, System.Type
        argumentActualType):
            base ()
        {

/*
Argument integrity assertions.  This is not a public constructor, so these
arguments must be initialised from with the library.  Check that they make
sense.
*/

            System.Diagnostics.Debug.Assert (!Util.IsNullOrEmpty
            (argumentName));
            System.Diagnostics.Debug.Assert (argumentType != null);
            System.Diagnostics.Debug.Assert (argumentRequiredType != null &&
            argumentRequiredType.IsSubclassOf (argumentType));
            System.Diagnostics.Debug.Assert (argumentActualType != null &&
            argumentActualType.IsSubclassOf (argumentType));
            System.Diagnostics.Debug.Assert (!argumentActualType.IsSubclassOf
            (argumentRequiredType));

/*
Store these arguments for later use.
*/

            argumentData = new System.Object []
            {
                argumentName,
                argumentType.FullName,
                argumentRequiredType.FullName,
                argumentActualType.FullName,
            };
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Explain why exception was thrown.</summary>

<remarks>Reports detailed information that allows a user to identify why the
exception was thrown.</remarks>

<value>A <see cref="System.String" /> containing the exception's
explanation.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public override string Message
        {

/*
Retrieve the compound message, format it and return it to the caller.
*/

            get
            {
                return Resource.Format ("argumentType", argumentData);
            }
        }
    }
}
