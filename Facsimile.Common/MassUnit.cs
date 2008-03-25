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

C# source file for the MassUnit class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Represents the family of mass measurement units.</summary>

<remarks>There will be a single instance of this class for each supported
measurement unit, such as "kilograms", "grams", "milligrams", "tonnes",
"pounds", "ounces", etc.

<para>In the avoirdupois system of weights, there are deviations between the
British version and those adopted by other countries.  For this reason, the
contentious units ("tons", "hundredweights", etc.) have not yet been
implemented.</para>

<para>The SI standard mass unit is the kilogram.  Mass values cannot be
negative.</para></remarks>
*/
//=============================================================================

    public sealed class MassUnit:
        NonNegativeMeasurementUnit
    {

/**
<summary>Kilogram mass measurement unit.</summary>
*/

        private static readonly MassUnit kilograms;

/**
<summary>Gram mass measurement unit.</summary>
*/

        private static readonly MassUnit grams;

/**
<summary>Milligrams mass measurement unit.</summary>
*/

        private static readonly MassUnit milligrams;

/**
<summary>Tonnes mass measurement unit.</summary>
*/

        private static readonly MassUnit tonnes;

/**
<summary>Pounds mass measurement unit.</summary>
*/

        private static readonly MassUnit pounds;

/**
<summary>Ounces mass measurement unit.</summary>

<remarks>These are avoirdupois ounces, rather than troy (or apothecary)
ounces.</remarks>
*/

        private static readonly MassUnit ounces;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static MassUnit ()
        {

/*
Create the standard mass unit - kilograms.
*/

            System.Diagnostics.Debug.Assert (kilograms == null);
            kilograms = new MassUnit ();

/*
Create each of the non-standard mass measurement units.

Note: The scaling factor from kilograms to pounds was defined by Australia,
Canada, New Zealand, South Africa, United Kingdom and United States of America
on July 1st, 1959.  Reference footnote 23 in the document titled "Guide for the
Use of the International System of Units (SI)", by B. N. Taylor, available at:

    http://physics.nist.gov/Pubs/SP811/
*/

            double kilogramsPerGram = 1.0 / 1000.0;
            System.Diagnostics.Debug.Assert (grams == null);
            grams = new MassUnit (kilogramsPerGram);

            double kilogramsPerMilligram = kilogramsPerGram / 1000.0;
            System.Diagnostics.Debug.Assert (milligrams == null);
            milligrams = new MassUnit (kilogramsPerMilligram);

            double kilogramsPerTonne = 1000.0;
            System.Diagnostics.Debug.Assert (tonnes == null);
            tonnes = new MassUnit (kilogramsPerTonne);

            double kilogramsPerPound = 0.45359237;
            System.Diagnostics.Debug.Assert (pounds == null);
            pounds = new MassUnit (kilogramsPerPound);

            double kilogramsPerOunce = kilogramsPerPound / 16.0;
            System.Diagnostics.Debug.Assert (ounces == null);
            ounces = new MassUnit (kilogramsPerOunce);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the kilograms mass unit.</summary>

<remarks>These units are sometime also called simply "kilos".</remarks>

<value>A <see cref="MassUnit" /> instance representing the chosen mass
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static MassUnit Kilograms
        {
            get
            {
                return kilograms;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the grams mass unit.</summary>

<value>A <see cref="MassUnit" /> instance representing the chosen mass
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static MassUnit Grams
        {
            get
            {
                return grams;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the milligrams mass unit.</summary>

<value>A <see cref="MassUnit" /> instance representing the chosen mass
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static MassUnit Milligrams
        {
            get
            {
                return milligrams;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the tonnes mass unit.</summary>

<remarks>These units are called "metric tons" in the United States of
America.</remarks>

<value>A <see cref="MassUnit" /> instance representing the chosen mass
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static MassUnit Tonnes
        {
            get
            {
                return tonnes;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the pounds mass unit.</summary>

<remarks>These are avoirdupois pounds, rather than troy (or apothecary)
pounds.</remarks>

<value>A <see cref="MassUnit" /> instance representing the chosen mass
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static MassUnit Pounds
        {
            get
            {
                return pounds;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the ounces mass unit.</summary>

<remarks>These are avoirdupois ounces, rather than troy (or apothecary)
ounces.</remarks>

<value>A <see cref="MassUnit" /> instance representing the chosen mass
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static MassUnit Ounces
        {
            get
            {
                return ounces;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Standard mass unit constructor.</summary>

<remarks>This constructor is used to create that standard mass units.  Any
non-negative double value (masses cannot be negative) is a valid standard mass
measurement.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private MassUnit ():
            base ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard mass unit scale factor constructor.</summary>

<remarks>This constructor is used to create non-standard mass units.</remarks>

<param name="unitScaleFactor">A <see cref="System.Double" /> defining the
number of standard units corresponding to a single unit of these units.  This
value must be positive.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private MassUnit (double unitScaleFactor):
            base (unitScaleFactor, 0.0)
        {
        }
    }
}
