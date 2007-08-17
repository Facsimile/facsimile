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

C# source file for the ArgumentOutOfRangeException <T> generic class, and
associated elements, that are integral members of the Facsimile.Common package.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Exception thrown when a value argument is outside its valid
range.</summary>

<remarks>This overridden version of <see
cref="System.ArgumentOutOfRangeException" /> provides additional information,
by default, on how the exception occurred.</remarks>

<typeparam name="ValueType">A <see cref="System.ValueType" />-derived value
type class, representing the type of the argument that was outside of its valid
range.</typeparam>
*/
//=============================================================================

    public sealed class ArgumentOutOfRangeException <ValueType>:
        System.ArgumentOutOfRangeException
    where ValueType:
        struct, System.IComparable <ValueType>, System.IEquatable <ValueType>
    {

/**
<summary>Object array.</summary>

<remarks>This array is initialised by the constructor to contain the following
values:

<list type="number">
    <item>
        <description>The name of the method argument whose value is out of
        range.  The type of this member should be <see cref="System.String"
        />.</description>
    </item>
    <item>
        <description>The minimum value of the argument's permitted
        range.  The type of this member should be <typeparamref
        name="ValueType" />.</description>
    </item>
    <item>
        <description>The maximum value of the argument's permitted
        range.  The type of this member should be <typeparamref
        name="ValueType" />.</description>
    </item>
    <item>
        <description>The value of the method argument that caused the
        exception.  The type of this member should be <typeparamref
        name="ValueType" />.</description>
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

<param name="argumentMinimum">A <typeparamref name="ValueType" /> instance
holding the minimum valid value for <paramref name="argumentName" />.</param>

<param name="argumentMaximum">A <typeparamref name="ValueType" /> instance
holding the maximum valid value for <paramref name="argumentName" />.</param>

<param name="argumentValue">A <typeparamref name="ValueType" /> instance
holding the invalid value for <paramref name="argumentName" />.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal ArgumentOutOfRangeException (string argumentName, ValueType
        argumentMinimum, ValueType argumentMaximum, ValueType argumentValue):
            base ()
        {

/*
Argument integrity assertions.  This is not a public constructor, so these
arguments must be initialised from with the library.  Check that they make
sense.
*/

            System.Diagnostics.Debug.Assert (!Util.IsNullOrEmpty
            (argumentName));
            System.Diagnostics.Debug.Assert (argumentMinimum.CompareTo
            (argumentMaximum) <= 0);
            System.Diagnostics.Debug.Assert (argumentValue.CompareTo
            (argumentMinimum) < 0 || argumentValue.CompareTo
            (argumentMaximum) > 0);

/*
Store these arguments for later use.
*/

           argumentData = new System.Object []
           {
               argumentName,
               argumentMinimum,
               argumentMaximum,
               argumentValue,
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
                return Resource.Format ("argumentOutOfRange", argumentData);
            }
        }
    }
}
