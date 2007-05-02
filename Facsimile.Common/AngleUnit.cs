/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the:

    Free Software Foundation, Inc.
    51 Franklin St, Fifth Floor
    Boston, MA  02110-1301
    USA

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

            radians = new AngleUnit ();

/*
Now create each of the non-standard angle measurement units.
*/

            double radiansPerDegree = 2.0 * System.Math.PI / 360.0;
            degrees = new AngleUnit (radiansPerDegree);
            double radiansPerGradient = 2.0 * System.Math.PI / 400.0;
            gradients = new AngleUnit (radiansPerGradient);
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
<summary>Standard angle unit constructor.</summary>

<remarks>This constructor is used to create that standard angle units.  Any
valid double value is a valid standard angle measurement.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private AngleUnit ():
            base (double.NegativeInfinity, double.PositiveInfinity, true)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard angle unit constructor.</summary>

<remarks>This constructor is used to create non-standard angle units.</remarks>

<param name="unitScaleFactor"> A <see cref="System.Double" /> value defining
the number of standard units corresponding to a single unit of these
units.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private AngleUnit (double unitScaleFactor):
            base (unitScaleFactor, 0.0)
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Normalize an angle measurement.</summary>

<remarks>Angles may be normalized to the range [0, 2pi).  This allows
comparison of relative angle positions to be made in a consistent
manner.</remarks>

<param name="value">A <see cref="System.Double" /> angle value in radians to be
normalized.</param>

<returns>A <see cref="System.Double" /> representing the normalized <paramref
name="value" />.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal override double Normalize (double value)
        {

/*
Angles are normalized to the range [0, 2pi) radians (equivalent to [0, 360)
degrees).
*/

            return value % (2.0 * System.Math.PI);
        }
    }
}
