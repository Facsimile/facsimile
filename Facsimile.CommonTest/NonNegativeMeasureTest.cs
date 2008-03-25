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

C# source file for the NonNegativeMeasureTest class, and associated elements,
that are integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>This is a generic base class, containing a number of NUnit tests for
use with measurement testing.</summary>

<remarks>This class is valid for measurements that cannot accept negative
standard unit values.</remarks>

<typeparam name="UnitType">The <see cref="NonNegativeMeasurementUnit" />
sub-class of measurements that is to be tested.</typeparam>
*/
//=============================================================================

    public abstract class NonNegativeMeasureTest <UnitType>:
        MeasureTest <UnitType>
    where UnitType:
        NonNegativeMeasurementUnit
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that negative standard value construction fails.</summary>

<remarks>Negative values are invalid for those measurements that must be
greater than or equal to zero (such as <see cref="TemperatureUnit" /> or <see
cref="TimeUnit" />.  Verify that construction with such a value
fails.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (ArgumentOutOfRangeException <double>))]
        public void NegativeConstructor1 ()
        {
            Measure <UnitType> measure = new Measure <UnitType>
            (-double.Epsilon, Standard);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that negative standard value construction fails.</summary>

<remarks>Negative values are invalid for those measurements that must be
greater than or equal to zero (such as <see cref="TemperatureUnit" /> or <see
cref="TimeUnit" />.  Verify that construction with such a value
fails.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (ArgumentOutOfRangeException <double>))]
        public void NegativeConstructor2 ()
        {
            Measure <UnitType> measure = new Measure <UnitType> (-10.0,
            Standard);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that negative standard value construction fails.</summary>

<remarks>Negative values are invalid for those measurements that must be
greater than or equal to zero (such as <see cref="TemperatureUnit" /> or <see
cref="TimeUnit" />.  Verify that construction with such a value
fails.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (ArgumentOutOfRangeException <double>))]
        public void NegativeConstructor3 ()
        {
            Measure <UnitType> measure = new Measure <UnitType>
            (double.NegativeInfinity, Standard);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify exception thrown on negative difference.</summary>

<remarks>Any subtraction operation that results in a negative difference should
result in an exception being thrown.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (MeasureOverflowException))]
        public void NegativeDifference ()
        {
            Measure <UnitType> one = new Measure <UnitType> (1.0, Standard);
            Measure <UnitType> two = new Measure <UnitType> (2.0, Standard);

/*
This line should throw the exception, as the result is an invalid value.
*/

            Measure <UnitType> diff = one - two;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify exception thrown on unary minus of positive value.</summary>

<remarks>Whenever a unary minus operator is employed on a positive value, the
result should be an exception.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (MeasureOverflowException))]
        public void UnaryMinusOnPositive ()
        {
            Measure <UnitType> one = new Measure <UnitType> (1.0, Standard);

/*
This line should throw the exception, as the result is an invalid value.
*/

            Measure <UnitType> diff = -one;
        }
    }
}
