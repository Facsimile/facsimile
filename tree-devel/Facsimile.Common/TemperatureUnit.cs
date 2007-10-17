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

C# source file for the TemperatureUnit class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Represents the family of temperature measurement units.</summary>

<remarks>There will be a single instance of this class for each supported
measurement unit, such as "Kelvin", "degrees Celsius" and "degrees
Fahrenheit".

<para>The degrees centigrade units are regarded as obsolete by NIST.  One
degree centigrade is only approximately one degree Celsius.  The symbol Â°C is
interpreted to mean degrees Celsius and not degrees centigrade.</para>

<para>The SI standard names both Kelvin (as a base unit) and Degrees Celsius
(as a unit derived from Kelvin) as standard temperature units.  Since we
require a single standard temperature unit, and since Kelvin units are the base
units from which Degrees Celsius are derived, we have chosen Kelvin as the
standard temperature unit for the Facsimile project.  (This is an arbitrary
determination from the user's viewpoint.)  Temperature values (in Kelvin units)
cannot be negative, as this would imply temperatures lower than "absolute
zero", or 0K.</para></remarks>
*/
//=============================================================================

    public sealed class TemperatureUnit:
        NonNegativeMeasurementUnit
    {

/**
<summary>Kelvin temperature measurement unit.</summary>
*/

        private static readonly TemperatureUnit kelvin;

/**
<summary>Celsius temperature measurement unit.</summary>
*/

        private static readonly TemperatureUnit celsius;

/**
<summary>Fahrenheit temperature measurement unit.</summary>
*/

        private static readonly TemperatureUnit fahrenheit;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static TemperatureUnit ()
        {

/*
Create the standard temperature unit - Kelvin.
*/

            System.Diagnostics.Debug.Assert (kelvin == null);
            kelvin = new TemperatureUnit ();

/*
Create each of the non-standard temperature measurement units.

Note: The origin offset from Kelvin to Celsius was taken from Appendix B.9 in
the document titled "Guide for the Use of the International System of Units
(SI)", by B. N. Taylor, available at:

    http://physics.nist.gov/Pubs/SP811/
*/

            double celsiusOriginOffset = 273.15;
            System.Diagnostics.Debug.Assert (celsius == null);
            celsius = new TemperatureUnit (1.0, celsiusOriginOffset);

            double celsiusToFahrenheitFactor = 5.0 / 9.0; 
            System.Diagnostics.Debug.Assert (fahrenheit == null);
            fahrenheit = new TemperatureUnit (celsiusToFahrenheitFactor,
            celsiusOriginOffset - (32.0 * celsiusToFahrenheitFactor));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the Kelvin temperature unit.</summary>

<value>A <see cref="TemperatureUnit" /> instance representing the chosen
temperature units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TemperatureUnit Kelvin
        {
            get
            {
                return kelvin;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the degrees Celsius temperature unit.</summary>

<remarks>Degrees Celsius is the preferred term for these units; degrees
centigrade is regarded as an obsolete temperature unit that only approximates
degrees Celsius.  For further reading on this topic, please refer to footnote
16 of the document titled "Guide for the Use of the International System of
Units (SI)", by B. N. Taylor, available at:
http://physics.nist.gov/Pubs/SP811/</remarks>

<value>A <see cref="TemperatureUnit" /> instance representing the chosen
temperature units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TemperatureUnit DegreesCelsius
        {
            get
            {
                return celsius;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the degrees Fahrenheit temperature unit.</summary>

<value>A <see cref="TemperatureUnit" /> instance representing the chosen
temperature units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TemperatureUnit DegreesFahrenheit
        {
            get
            {
                return fahrenheit;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Standard temperature unit constructor.</summary>

<remarks>This constructor is used to create that standard temperature units.
Any non-negative double value (temperatures cannot be less than Absolute Zero)
is a valid standard temperature measurement.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private TemperatureUnit ():
            base ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard temperature unit constructor.</summary>

<remarks>This constructor is used to create non-standard temperature
units.</remarks>

<param name="unitScaleFactor">A <see cref="System.Double" /> defining the
number of standard units corresponding to a single unit of these units.  This
value must be positive.</param>

<param name="unitOriginOffset">The distance, measured in standard units along
the Y-axis, between the standard units' origin and the origin for these units.
If this value is zero, then zero of the standard units is exactly equal to zero
of these units.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private TemperatureUnit (double unitScaleFactor, double
        unitOriginOffset):
            base (unitScaleFactor, unitOriginOffset)
        {
        }
    }
}
