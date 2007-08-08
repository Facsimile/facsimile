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

C# source file for the DistanceMeasureTest class, and associated elements, that
are integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>NUnit test fixture for <see cref="Measure {UnitType}" /> <see
cref="DistanceUnit" /> measurements.</summary>

<remarks>This test fixture is specifically used for testing issues relating to
distance measurements.</remarks>
*/
//=============================================================================

    [TestFixture]
    public sealed class DistanceMeasureTest:
        MeasureTest <DistanceUnit>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that identical distances, in different units, are indeed
identical.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void ComparisonTest ()
        {

/*
Define a set of distance measurements all equal to exactly 5 meters, but using
a different unit in each case.
*/

            Measure <DistanceUnit> inMeters = new Measure <DistanceUnit> (5.0,
            DistanceUnit.Meters);
            Measure <DistanceUnit> inKilometers = new Measure <DistanceUnit>
            (5.0 / 1000.0, DistanceUnit.Kilometers);
            Measure <DistanceUnit> inCentimeters = new Measure <DistanceUnit>
            (5.0 * 100.0, DistanceUnit.Centimeters);
            Measure <DistanceUnit> inMillimeters = new Measure <DistanceUnit>
            (5.0 * 1000.0, DistanceUnit.Millimeters);
            double metersPerInch = 2.54e-2;
            double fiveMetersInInches = 5.0 / metersPerInch;
            Measure <DistanceUnit> inInches = new Measure <DistanceUnit>
            (fiveMetersInInches, DistanceUnit.Inches);
            Measure <DistanceUnit> inFeet = new Measure <DistanceUnit>
            (fiveMetersInInches / 12.0, DistanceUnit.Feet);
            Measure <DistanceUnit> inYards = new Measure <DistanceUnit>
            (fiveMetersInInches / (12.0 * 3.0), DistanceUnit.Yards);
            Measure <DistanceUnit> inMiles = new Measure <DistanceUnit>
            (fiveMetersInInches / (12.0 * 3.0 * 1760.0), DistanceUnit.Miles);

/*
Check that the measurement in meters (the standard units) is the same as the
measurement in all other units using the "TolerantCompareTo" method; this form
of comparison is more robust since it accommodates small rounding errors, which
would cause exact comparisons to fail.
*/

            Assert.IsTrue (inMeters.TolerantCompareTo (inKilometers) == 0);
            Assert.IsTrue (inMeters.TolerantCompareTo (inCentimeters) == 0);
            Assert.IsTrue (inMeters.TolerantCompareTo (inMillimeters) == 0);
            Assert.IsTrue (inMeters.TolerantCompareTo (inInches) == 0);
            Assert.IsTrue (inMeters.TolerantCompareTo (inFeet) == 0);
            Assert.IsTrue (inMeters.TolerantCompareTo (inYards) == 0);
            Assert.IsTrue (inMeters.TolerantCompareTo (inMiles) == 0);
        }
    }
}
