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

C# source file for the LanguageTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest {

//=============================================================================
/**
<summary>NUnit test fixture that attempts to establish whether features of the
C# language, and/or CLR, have are implemented consistently.</summary>
*/
//=============================================================================

    [TestFixture]
    public sealed class LanguageTest
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Floating positive infinity assertions.</summary>

<remarks>Check that the argument is a floating positive infinity value, with
the required properties.</remarks>

<param name="infinity">The value to be checked for positive infinity.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void TestForPositiveInfinity (double infinity)
        {
            Assert.IsFalse (double.IsNaN (infinity));
            Assert.IsTrue (double.IsPositiveInfinity (infinity));
            Assert.IsTrue (double.IsInfinity (infinity));
            Assert.IsTrue (infinity == double.PositiveInfinity);
            Assert.IsTrue (infinity > double.MaxValue);
            Assert.IsFalse (double.IsNegativeInfinity (infinity));
            Assert.IsFalse (double.IsNaN (infinity));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Floating negative infinity assertions.</summary>

<remarks>Check that the argument is a floating negative infinity value, with
the required properties.</remarks>

<param name="infinity">The value to be checked for negative infinity.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void TestForNegativeInfinity (double infinity)
        {
            Assert.IsFalse (double.IsNaN (infinity));
            Assert.IsTrue (double.IsNegativeInfinity (infinity));
            Assert.IsTrue (double.IsInfinity (infinity));
            Assert.IsTrue (infinity == double.NegativeInfinity);
            Assert.IsTrue (infinity < double.MinValue);
            Assert.IsFalse (double.IsPositiveInfinity (infinity));
            Assert.IsFalse (double.IsNaN (infinity));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Floating positive infinity tests.</summary>

<remarks>These tests are performed, possible reduntantly, to satisfy the
paranoia of the developers!</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void VerifyFloatingInfinity ()
        {

/*
Check that infinity means infinity.  That is, if we multiply it, it is still
infinity (although the sign may change).   If we add or subtract from it, it is
still infinity.
*/

            double infinity = double.PositiveInfinity;
            TestForPositiveInfinity (infinity);
            TestForPositiveInfinity (infinity * 2.0);
            TestForNegativeInfinity (infinity * -2.0);
            TestForPositiveInfinity (infinity + 10.0);
            TestForPositiveInfinity (infinity - 10.0);
            double negativeInfinity = double.NegativeInfinity;
            TestForNegativeInfinity (negativeInfinity);
            TestForNegativeInfinity (negativeInfinity * 2.0);
            TestForPositiveInfinity (negativeInfinity * -2.0);
            TestForNegativeInfinity (negativeInfinity + 10.0);
            TestForNegativeInfinity (negativeInfinity - 10.0);
        }
    }
}
