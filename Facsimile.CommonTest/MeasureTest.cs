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

C# source file for the MeasureTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
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

<remarks>This class is valid for all types of measurement.</remarks>

<typeparam name="UnitType">The <see cref="MeasurementUnit" /> sub-class of
measurements that is to be tested.</typeparam>
*/
//=============================================================================

    public abstract class MeasureTest <UnitType>:
        System.Object
    where UnitType:
        MeasurementUnit
    {

/**
<summary>The standard measurement units for the indicated family.</summary>
*/

        private readonly UnitType standard;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Constructor</summary>

<remarks>General initialisation prior to the tests executing.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal MeasureTest ():
            base ()
        {

/*
Get the standard units for this type, and cast to the standard units.
*/

            standard = (UnitType) MeasurementUnit.GetStandard (typeof
            (UnitType));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Property for verifying and reporting standard units for family being
tested.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal UnitType Standard
        {
            get
            {
                System.Diagnostics.Debug.Assert (standard != null);
                return standard;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that supplying no units to the constructor fails.</summary>

<remarks>Passing the null value for the units should produce an
exception.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (System.ArgumentNullException))]
        public void NullUnitConstructor ()
        {
            Measure <UnitType> measure = new Measure <UnitType> (0.0, null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that NaN construction fails.</summary>

<remarks>NaN (not-a-number) is an invalid value for any measurement.  Verify
that construction with this value fails.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (ArgumentOutOfRangeException <double>))]
        public void NaNConstructor ()
        {
            Measure <UnitType> measure = new Measure <UnitType> (double.NaN,
            (UnitType) MeasurementUnit.GetStandard (typeof (UnitType)));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that minimum standard value construction succeeds.</summary>

<remarks>Minimum standard values must be valid for all measurements.  Verify
that construction with such a value succeeds.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void MinimumConstructor ()
        {
            Measure <UnitType> measure = new Measure <UnitType>
            (standard.MinimumValue, Standard);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that maximum standard value construction succeeds.</summary>

<remarks>Maximum standard values must be valid for all measurements.  Verify
that construction with such a value succeeds.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void MaximumConstructor ()
        {
            Measure <UnitType> measure = new Measure <UnitType>
            (standard.MaximumValue, Standard);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify that comparing to a non-measurement fails.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (ArgumentTypeException))]
        public void CompareToBadMeasure ()
        {
            Measure <UnitType> one = new Measure <UnitType> (1.0, Standard);
            object dummy = "Dummy";
            one.CompareTo (dummy);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Verify basic mathematical and comparison operations.</summary>

<remarks>This method perfoms some basic operations upon measurement data, then
verifies that the results are correct.  OK, so they're very basic operations...

<para>To avoid unexpected exceptions, use standard unit value ranges that are
valid for all measurements.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void BasicOperations () {

/*
Verify that comparisons to null pass off OK.  (All values are greater than null
and, therefore, are not equal to null.)
*/

            Measure <UnitType> one = new Measure <UnitType> (1.0, Standard);
            object nullObject = null;
            Assert.IsTrue (one.CompareTo (nullObject) > 0);
            Assert.IsFalse (one.Equals (nullObject));

/*
Verify that a measurement is not equal to a non-measurement.
*/

            object dummy = "dummy";
            Assert.IsFalse (one.Equals (dummy));

/*
Check that unary plus doesn't change the value.
*/

            Measure <UnitType> plusOne = +one;
            Assert.AreEqual (plusOne.TolerantCompareTo (one), 0);
            Assert.AreEqual (plusOne.CompareTo (one), 0);
            Assert.IsTrue (plusOne.Equals (one));
            Assert.IsTrue (plusOne == one);

/*
Check that one compares equal to a boxed version of plusOne.
*/

            object plusOneObject = plusOne;
            Assert.AreEqual (one.CompareTo (plusOneObject), 0);
            Assert.IsTrue (one.Equals (plusOneObject));

/*
Check the comparison operators for two different values.
*/

            Measure <UnitType> two = new Measure <UnitType> (2.0, Standard);
            Assert.IsTrue (one.TolerantCompareTo (two) < 0);
            Assert.IsTrue (one.CompareTo (two) < 0);
            Assert.IsTrue (one < two);
            Assert.IsFalse (one > two);
            Assert.IsTrue (one <= two);
            Assert.IsFalse (one >= two);
            Assert.IsFalse (one.Equals (two));
            Assert.IsFalse (one == two);
            Assert.IsTrue (one != two);
            Assert.IsTrue (two.TolerantCompareTo (one) > 0);
            Assert.IsTrue (two.CompareTo (one) > 0);
            Assert.IsFalse (two < one);
            Assert.IsTrue (two > one);
            Assert.IsFalse (two <= one);
            Assert.IsTrue (two >= one);
            Assert.IsFalse (two.Equals (one));
            Assert.IsFalse (two == one);
            Assert.IsTrue (two != one);

/*
Add one and two together.
*/

            Measure <UnitType> three = new Measure <UnitType> (3.0, Standard);
            Measure <UnitType> sum = one + two;
            Assert.AreEqual (sum.CompareTo (three), 0);
            Assert.IsTrue (sum.Equals (three));
            Assert.IsTrue (sum == three);

/*
Subtract one from two.  (Subtracting two from one will lead to an exception for
non-negative measurements, and this should not be tested here.)
*/

            Measure <UnitType> diff = two - one;
            Assert.AreEqual (diff.CompareTo (one), 0);
            Assert.IsTrue (diff.Equals (one));
            Assert.IsTrue (diff == one);
        }
    }
}
