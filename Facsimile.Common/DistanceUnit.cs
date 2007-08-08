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

C# source file for the DistanceUnit class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Represents the family of distance and length measurement
units.</summary>

<remarks>There will be a single instance of this class for each supported
measurement unit, such as "meters", "centimeters", "millimeters", "kilometers",
"inches", "feet", "yards", "miles" etc.

<para>The SI standard length unit is the meter.</para></remarks>
*/
//=============================================================================

    public sealed class DistanceUnit:
        MeasurementUnit
    {

/**
<summary>Meter length measurement unit.</summary>
*/

        private static readonly DistanceUnit meters;

/**
<summary>Centimeter length measurement unit.</summary>
*/

        private static readonly DistanceUnit centimeters;

/**
<summary>Millimeter length measurement unit.</summary>
*/

        private static readonly DistanceUnit millimeters;

/**
<summary>Kilometer length measurement unit.</summary>
*/

        private static readonly DistanceUnit kilometers;

/**
<summary>Inch time measurement unit.</summary>
*/

        private static readonly DistanceUnit inches;

/**
<summary>Foot length measurement unit.</summary>
*/

        private static readonly DistanceUnit feet;

/**
<summary>Yard length measurement unit.</summary>
*/

        private static readonly DistanceUnit yards;

/**
<summary>Mile length measurement unit.</summary>
*/

        private static readonly DistanceUnit miles;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static DistanceUnit ()
        {

/*
Create the standard distance unit - meters.
*/

            System.Diagnostics.Debug.Assert (meters == null);
            meters = new DistanceUnit ();

/*
Create each of the non-standard distance measurement units.

Note: The scaling factor from meters to yards was defined by Australia,
Canada, New Zealand, South Africa, United Kingdom and United States of America
on July 1st, 1959.  Reference Appendix B.9 in the document titled "Guide for
the Use of the International System of Units (SI)", by B. N. Taylor, available
at:

    http://physics.nist.gov/Pubs/SP811/
*/

            double metersPerCentimeter = 1.0 / 100.0;
            System.Diagnostics.Debug.Assert (centimeters == null);
            centimeters = new DistanceUnit (metersPerCentimeter);

            double metersPerMillimeter = metersPerCentimeter / 10.0;
            System.Diagnostics.Debug.Assert (millimeters == null);
            millimeters = new DistanceUnit (metersPerMillimeter);

            double metersPerKilometer = 1000.0;
            System.Diagnostics.Debug.Assert (kilometers == null);
            kilometers = new DistanceUnit (metersPerKilometer);

            double metersPerYard = 0.9144;
            System.Diagnostics.Debug.Assert (yards == null);
            yards = new DistanceUnit (metersPerYard);

            double metersPerFoot = metersPerYard / 3.0;
            System.Diagnostics.Debug.Assert (feet == null);
            feet = new DistanceUnit (metersPerFoot);

            double metersPerInch = metersPerFoot / 12.0;
            System.Diagnostics.Debug.Assert (inches == null);
            inches = new DistanceUnit (metersPerInch);

            double metersPerMile = 1760.0 * metersPerYard;
            System.Diagnostics.Debug.Assert (miles == null);
            miles = new DistanceUnit (metersPerMile);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the meters distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Meters
        {
            get
            {
                return meters;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the centimeters distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Centimeters
        {
            get
            {
                return centimeters;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the millimeters distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Millimeters
        {
            get
            {
                return millimeters;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the kilometers distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Kilometers
        {
            get
            {
                return kilometers;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the inches distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Inches
        {
            get
            {
                return inches;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the feet distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Feet
        {
            get
            {
                return feet;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the yards distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Yards
        {
            get
            {
                return yards;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the miles distance unit.</summary>

<value>A <see name="DistanceUnit" /> instance representing the chosen distance
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static DistanceUnit Miles
        {
            get
            {
                return miles;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Standard distance unit constructor.</summary>

<remarks>This constructor is used to create that standard distance units.  Any
valid double value is a valid standard distance measurement.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private DistanceUnit ():
            base ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard distance unit constructor.</summary>

<remarks>This constructor is used to create non-standard distance
units.</remarks>

<param name="unitScaleFactor">A <see cref="System.Double" /> defining the
number of standard units corresponding to a single unit of these units.  This
value must be positive.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private DistanceUnit (double unitScaleFactor):
            base (unitScaleFactor, 0.0)
        {
        }
    }
}
