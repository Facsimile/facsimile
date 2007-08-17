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

C# source file for the AngleUnit class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Represents the family of plane angle measurement units.</summary>

<remarks>There will be a single instance of this class for each supported
measurement unit, such as "radians", "degrees" and "gradients".

<para>The SI standard angle unit is the radian.</para></remarks>
*/
//=============================================================================

    public sealed class AngleUnit:
        MeasurementUnit
    {

/**
<summary>Radian angle measurement unit.</summary>
*/

        private static readonly AngleUnit radians;

/**
<summary>Degree angle measurement unit.</summary>
*/

        private static readonly AngleUnit degrees;

/**
<summary>Gradients angle measurement unit.</summary>
*/

        private static readonly AngleUnit gradients;

/**
<summary>Revolutions angle measurement unit.</summary>
*/

        private static readonly AngleUnit revolutions;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static AngleUnit ()
        {

/*
Create the standard angle unit - radians.
*/

            System.Diagnostics.Debug.Assert (radians == null);
            radians = new AngleUnit ();

/*
Now create each of the non-standard angle measurement units.
*/

            double radiansPerRevolution = 2.0 * System.Math.PI;
            double radiansPerDegree = radiansPerRevolution / 360.0;
            System.Diagnostics.Debug.Assert (degrees == null);
            degrees = new AngleUnit (radiansPerDegree);

            double radiansPerGradient = radiansPerRevolution / 400.0;
            System.Diagnostics.Debug.Assert (gradients == null);
            gradients = new AngleUnit (radiansPerGradient);

            System.Diagnostics.Debug.Assert (revolutions == null);
            revolutions = new AngleUnit (radiansPerRevolution);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the radians angle unit.</summary>

<value>A <see cref="AngleUnit" /> instance representing the chosen angle
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static AngleUnit Radians
        {
            get
            {
                return radians;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the degrees angle unit.</summary>

<value>A <see cref="AngleUnit" /> instance representing the chosen angle
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static AngleUnit Degrees
        {
            get
            {
                return degrees;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the gradients angle unit.</summary>

<value>A <see cref="AngleUnit" /> instance representing the chosen angle
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static AngleUnit Gradients
        {
            get
            {
                return gradients;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the revolutions angle unit.</summary>

<remarks>Revolutions are not generally used to measure angles, but they are
frequently used in combination with time measurements to create units of
angular velocity, angular accelerations, etc.</remarks>

<value>A <see cref="AngleUnit" /> instance representing the chosen angle
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static AngleUnit Revolutions
        {
            get
            {
                return revolutions;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Standard angle unit constructor.</summary>

<remarks>This constructor is used to create that standard angle units.  Any
valid double value is a valid standard angle measurement.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private AngleUnit ():
            base ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard angle unit constructor.</summary>

<remarks>This constructor is used to create non-standard angle units.</remarks>

<param name="unitScaleFactor">A <see cref="System.Double" /> defining the
number of standard units corresponding to a single unit of these units.  This
value must be positive.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private AngleUnit (double unitScaleFactor):
            base (unitScaleFactor, 0.0)
        {
        }
    }
}
