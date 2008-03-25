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

C# source file for the ReflectionTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>Arbitrary test attribute for decorating an arbitrary test
class.</summary>

<remarks>This attribute is used to decorate a local class in order to validate
that it was correctly picked up by the <see cref="Reflection" />
class.</remarks>
*/
//=============================================================================

    [System.AttributeUsage (System.AttributeTargets.Class, AllowMultiple =
    false, Inherited = true)]
    public sealed class SomeTestAttribute:
        System.Attribute
    {
    }

//=============================================================================
/**
<summary>A test class decorated with the <see cref="SomeTestAttribute" />
attribute.</summary>

<remarks>We'll test that the <see cref="Reflection" /> class has picked up this
class - and its sub-classes - as being decorated with the <see
cref="SomeTestAttribute" />.</remarks>
*/
//=============================================================================

    [SomeTest]
    public class SomeTestDecoratedClass:
        System.Object
    {
    }

//=============================================================================
/**
<summary>A test class decorated with the <see cref="SomeTestAttribute" />
attribute.</summary>

<remarks>We'll test that the <see cref="Reflection" /> class has picked up this
class - and its sub-classes - as being decorated with the <see
cref="SomeTestAttribute" />.</remarks>
*/
//=============================================================================

    public class SomeTestDecoratedSubClass:
        SomeTestDecoratedClass
    {
    }

//=============================================================================
/**
<summary>NUnit test fixture for the <see cref="Reflection" /> class.</summary>
*/
//=============================================================================

    [TestFixture]
    public sealed class ReflectionTest:
        System.Object
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Validate <see cref="Reflection.GetTypesWithAttribute (System.Type)" />
function.</summary>

<remarks>Test that classes within the current test assembly, and known
decorated classes in the Facsimile assemblies, have been correctly identified
by the <see cref="Reflection.GetTypesWithAttribute (System.Type)" />
function.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void ValidateGetTypesWithAttribute ()
        {

/*
Get the list of classes decorated with the "SomeTestAttribute".  Verify that
both of the classes we created above - including the undecorated sub-class
(which ought to inherit the attribute) - are reported as having this attribute.
*/

            bool sawSomeTestDecoratedClass = false;
            bool sawSomeTestDecoratedSubClass = false;
            System.Collections.Generic.List <System.Type> list =
            Reflection.GetTypesWithAttribute (typeof (SomeTestAttribute));
            foreach (System.Type type in list)
            {
                if (type == typeof (SomeTestDecoratedClass))
                {
                    Assert.IsFalse (sawSomeTestDecoratedClass);
                    sawSomeTestDecoratedClass = true;
                }
                else if (type == typeof (SomeTestDecoratedSubClass))
                {
                    Assert.IsFalse (sawSomeTestDecoratedSubClass);
                    sawSomeTestDecoratedSubClass = true;
                }
            }
            Assert.IsTrue (sawSomeTestDecoratedClass);
            Assert.IsTrue (sawSomeTestDecoratedSubClass);

/*
We know that the AttributeUsageAttribute is used on the
AutoInstantiateAttribute (as well as on the SomeTestAttribute).  Check that
Reflection found those too.
*/

            bool sawSomeTestAttribute = false;
            bool sawAutoInstantiateAttribute = false;
            list = Reflection.GetTypesWithAttribute (typeof
            (System.AttributeUsageAttribute));
            foreach (System.Type type in list)
            {
                if (type == typeof (SomeTestAttribute))
                {
                    Assert.IsFalse (sawSomeTestAttribute);
                    sawSomeTestAttribute = true;
                }
                else if (type == typeof (AutoInstantiateAttribute))
                {
                    Assert.IsFalse (sawAutoInstantiateAttribute);
                    sawAutoInstantiateAttribute = true;
                }
            }
            Assert.IsTrue (sawSomeTestAttribute);
            Assert.IsTrue (sawAutoInstantiateAttribute);
        }
    }
}
