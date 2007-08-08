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

C# source file for the MassMeasureTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>NUnit test fixture for <see cref="Measure {UnitType}" /> <see
cref="MassUnit" /> measurements.</summary>

<remarks>This test fixture is specifically used for testing issues relating to
mass measurements.</remarks>
*/
//=============================================================================

    [TestFixture]
    public sealed class MassMeasureTest:
        NonNegativeMeasureTest <MassUnit>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that identical masss, in different units, are indeed
identical.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void ComparisonTest ()
        {

/*
Define a set of mass measurements all equal to exactly 8 kilograms in weight,
but using a different unit in each case.
*/

            Measure <MassUnit> inKilograms = new Measure <MassUnit> (8.0,
            MassUnit.Kilograms);
            Measure <MassUnit> inGrams = new Measure <MassUnit> (8.0 * 1000.0,
            MassUnit.Grams);
            Measure <MassUnit> inMilligrams = new Measure <MassUnit> (8.0 *
            1000.0 * 1000.0, MassUnit.Milligrams);
            Measure <MassUnit> inTonnes = new Measure <MassUnit> (8.0 / 1000.0,
            MassUnit.Tonnes);
            double kilogramsPerPound = 0.45359237; // See MassUnit docs...
            double eightKilogramsInPounds = 8.0 / kilogramsPerPound;
            Measure <MassUnit> inPounds = new Measure <MassUnit>
            (eightKilogramsInPounds, MassUnit.Pounds);
            Measure <MassUnit> inOunces = new Measure <MassUnit>
            (eightKilogramsInPounds * 16.0, MassUnit.Ounces);

/*
Check that the measurement in seconds (the standard units) is the same as the
measurement in all other units using the "TolerantCompareTo" method; this form
of comparison is more robust since it accommodates small rounding errors, which
would cause exact comparisons to fail.
*/

            Assert.IsTrue (inKilograms.TolerantCompareTo (inGrams) == 0);
            Assert.IsTrue (inKilograms.TolerantCompareTo (inMilligrams) == 0);
            Assert.IsTrue (inKilograms.TolerantCompareTo (inTonnes) == 0);
            Assert.IsTrue (inKilograms.TolerantCompareTo (inPounds) == 0);
            Assert.IsTrue (inKilograms.TolerantCompareTo (inOunces) == 0);
        }
    }
}
