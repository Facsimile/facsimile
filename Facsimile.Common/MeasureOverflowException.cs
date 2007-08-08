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

C# source file for the MeasureOverflowException class, and associated elements,
that are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Exception thrown when a <see cref="Measure {UnitType}" /> value
becomes invalid as the result of an arithmetic operation.</summary>
*/
//=============================================================================

    public sealed class MeasureOverflowException:
        System.OverflowException
    {

/**
<summary>Object array.</summary>

<remarks>This array is initialised by the constructor to contain the following
values:

<list type="number">
    <item>
        <description>The full name of the exact measure type that
        overflowed.</description>
    </item>
    <item>
        <description>The minimum allowed value of this measure.</description>
    </item>
    <item>
        <description>The maximum allowed value of this measure.</description>
    </item>
    <item>
        <description>The actual overflowed (invalid) value.</description>
    </item>
</list></remarks>
*/

        private readonly System.Object [] measureData;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor.</summary>

<remarks>Processes the measure's information to be formatted as part of the
exception's message.</remarks>

<param name="standardUnits">The <see cref="MeasurementUnit" />-derived instance
idenitifying the standard units for the associated measure.</param>

<param name="invalidValue">The <see cref="System.Double" /> value representing
the resulting invalid measure.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal MeasureOverflowException (MeasurementUnit standardUnits,
        double invalidValue):
            base ()
        {

/*
Argument integrity assertions.
*/

            System.Diagnostics.Debug.Assert (standardUnits != null);
            System.Diagnostics.Debug.Assert (standardUnits.IsStandard);
            System.Diagnostics.Debug.Assert (!standardUnits.IsValid
            (invalidValue));

/*
Store the appropriate information for later use.
*/

            measureData = new System.Object []
            {
                standardUnits.GetType ().FullName,
                standardUnits.MinimumValue,
                standardUnits.MaximumValue,
                invalidValue,
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
                return Resource.Format ("measureOverflow", measureData);
            }
        }
    }
}
