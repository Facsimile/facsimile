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

C# source file for the TemperatureMeasureTest class, and associated elements,
that are integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>NUnit test fixture for <see cref="Measure {UnitType}" /> <see
cref="TemperatureUnit" /> measurements.</summary>

<remarks>This test fixture is specifically used for testing issues relating to
temperature measurements.</remarks>
*/
//=============================================================================

    [TestFixture]
    public sealed class TemperatureMeasureTest:
        NonNegativeMeasureTest <TemperatureUnit>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that identical temperatures, in different units, are indeed
identical.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void ComparisonTest ()
        {

/*
Define a set of temperature measurements all equal to exactly 100K, but using a
different unit in each case.
*/

            Measure <TemperatureUnit> inKelvin = new Measure <TemperatureUnit>
            (100.0, TemperatureUnit.Kelvin);
            Measure <TemperatureUnit> inCelsius = new Measure <TemperatureUnit>
            (100.0 - 273.15, TemperatureUnit.DegreesCelsius);
            Measure <TemperatureUnit> inFahrenheit = new Measure
            <TemperatureUnit> ((100.0 - 273.15) * 9.0 / 5.0 + 32.0,
            TemperatureUnit.DegreesFahrenheit);
            Measure <TemperatureUnit> freezingC = new Measure <TemperatureUnit>
            (0.0, TemperatureUnit.DegreesCelsius);
            Measure <TemperatureUnit> freezingF = new Measure <TemperatureUnit>
            (32.0, TemperatureUnit.DegreesFahrenheit);
            Measure <TemperatureUnit> boilingC = new Measure <TemperatureUnit>
            (100.0, TemperatureUnit.DegreesCelsius);
            Measure <TemperatureUnit> boilingF = new Measure <TemperatureUnit>
            (212.0, TemperatureUnit.DegreesFahrenheit);

/*
Check that the measurement in seconds (the standard units) is the same as the
measurement in all other units using the "TolerantCompareTo" method; this form
of comparison is more robust since it accommodates small rounding errors, which
would cause exact comparisons to fail.
*/

            Assert.IsTrue (inKelvin.TolerantCompareTo (inCelsius) == 0);
            Assert.IsTrue (inKelvin.TolerantCompareTo (inFahrenheit) == 0);
            Assert.IsTrue (freezingC.TolerantCompareTo (freezingF) == 0);
            Assert.IsTrue (boilingC.TolerantCompareTo (boilingF) == 0);
        }
    }
}
