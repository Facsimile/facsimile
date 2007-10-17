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

C# source file for the TimeMeasureTest class, and associated elements, that are
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
cref="TimeUnit" /> measurements.</summary>

<remarks>This test fixture is specifically used for testing issues relating to
time measurements.</remarks>
*/
//=============================================================================

    [TestFixture]
    public sealed class TimeMeasureTest:
        NonNegativeMeasureTest <TimeUnit>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that identical times, in different units, are indeed
identical.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void ComparisonTest ()
        {

/*
Define a set of time measurements all equal to exactly two weeks in duration,
but using a different unit in each case.
*/

            Measure <TimeUnit> inMilliseconds = new Measure <TimeUnit> (2.0 *
            7.0 * 24.0 * 60.0 * 60.0 * 1000.0, TimeUnit.Milliseconds);
            Measure <TimeUnit> inSeconds = new Measure <TimeUnit> (2.0 * 7.0 *
            24.0 * 60.0 * 60.0, TimeUnit.Seconds);
            Measure <TimeUnit> inMinutes = new Measure <TimeUnit> (2.0 * 7.0 *
            24.0 * 60.0, TimeUnit.Minutes);
            Measure <TimeUnit> inHours = new Measure <TimeUnit> (2.0 * 7.0 *
            24.0, TimeUnit.Hours);
            Measure <TimeUnit> inDays = new Measure <TimeUnit> (2.0 * 7.0,
            TimeUnit.Days);
            Measure <TimeUnit> inWeeks = new Measure <TimeUnit> (2.0,
            TimeUnit.Weeks);

/*
Check that the measurement in seconds (the standard units) is the same as the
measurement in all other units using the "TolerantCompareTo" method; this form
of comparison is more robust since it accommodates small rounding errors, which
would cause exact comparisons to fail.
*/

            Assert.IsTrue (inSeconds.TolerantCompareTo (inMilliseconds) == 0);
            Assert.IsTrue (inSeconds.TolerantCompareTo (inMinutes) == 0);
            Assert.IsTrue (inSeconds.TolerantCompareTo (inHours) == 0);
            Assert.IsTrue (inSeconds.TolerantCompareTo (inDays) == 0);
            Assert.IsTrue (inSeconds.TolerantCompareTo (inWeeks) == 0);
        }
    }
}
