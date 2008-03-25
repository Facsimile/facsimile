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

C# source file for the UtilTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>Test class for validating the operation of the <see
cref="Util.InitializeType (System.Type)" /> function.</summary>
*/
//=============================================================================

    public static class UtilTypeInitialization
    {

/**
<summary>Static count.</summary>

<remarks>This is initialised to 0 and is incremented by the static constructor.
Hence we can use it to determine how often the static constructor has been
called.</remarks>
*/

        private static int count = 0;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>This function should execute once only.  However, we're going to test
that this is the case under some severe conditions.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static UtilTypeInitialization ()
        {
            ++count;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Count property.</summary>

<remarks>Report how often the constructor has been called.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static int Count
        {
            get
            {
                return count;
            }
        }
    }

//=============================================================================
/**
<summary>NUnit test fixture for the <see cref="Util" /> class.</summary>
*/
//=============================================================================

    [TestFixture]
    public sealed class UtilTest:
        System.Object
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test the <see cref="Util.IsNullOrEmpty (System.String)" />} utility
function.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void TestIsNullOrEmpty ()
        {

/*
Test that the function returns true for any null string, or strings made up of
whitespace only.
*/

            Assert.IsTrue (Util.IsNullOrEmpty (null));
            Assert.IsTrue (Util.IsNullOrEmpty (""));
            Assert.IsTrue (Util.IsNullOrEmpty (" "));
            Assert.IsTrue (Util.IsNullOrEmpty ("  "));
            Assert.IsTrue (Util.IsNullOrEmpty ("\r"));
            Assert.IsTrue (Util.IsNullOrEmpty ("\n"));
            Assert.IsTrue (Util.IsNullOrEmpty ("\t"));
            Assert.IsTrue (Util.IsNullOrEmpty (" \t\r\n"));

/*
Check that the function returns false for any non-null string that contains
non-whitespace characters.
*/

            Assert.IsFalse (Util.IsNullOrEmpty ("Not empty"));
            Assert.IsFalse (Util.IsNullOrEmpty ("not empty "));
            Assert.IsFalse (Util.IsNullOrEmpty (" not empty"));
            Assert.IsFalse (Util.IsNullOrEmpty (" not empty "));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test the <see cref="Util.InitializeType (System.Type)" />} utility
function.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void TestTypeInitialization ()
        {

/*
Before we do anything, check that the test class has been initialised just the
once (this is normal behaviour when accessing a static member for the first
time).
*/

            Assert.AreEqual (UtilTypeInitialization.Count, 1);

/*
Now call the utility function a few times, then check that the function has
still only been called the once.
*/

            System.Type testType = typeof (UtilTypeInitialization);
            Util.InitializeType (testType);
            Util.InitializeType (testType);
            Util.InitializeType (testType);
            Util.InitializeType (testType);
            Assert.AreEqual (UtilTypeInitialization.Count, 1);
        }
    }
}
