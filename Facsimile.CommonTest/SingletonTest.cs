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

C# source file for the SingletonTest class, and associated elements, that are
integral members of the Facsimile.CommonTest namespace.
===============================================================================
*/

using NUnit.Framework;
using Facsimile.Common;
namespace Facsimile.CommonTest
{

//=============================================================================
/**
<summary>Base for generic singleton class.</summary>

<remarks>This class is required because the contraints for the Singleton's type
argument cannot be satisfied by a generic type subclass.</remarks>
*/
//=============================================================================

    public class GenericSingletonBase:
        Singleton <GenericSingletonBase>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Do the self-destruction.</summary>

<remarks>In order to self-destruct, we need to initialise our base type.  This
is best done by using the single instance to reference a member, such as this
function.  This function will never actually get called, as an exception should
arise when we initialize the base type.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void Destruct ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Self-destruct.</summary>

<remarks>When this function is called for the first time, the <see
cref="Singleton {SingletonBase}" /> base class's static constructor will be
called and should throw an exception, since there is no obvious instance to
auto-instantiate.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static void SelfDestruct ()
        {
            Instance.Destruct ();
        }
    }

//=============================================================================
/**
<summary>Unsuitable <see cref="AutoInstantiateAttribute" />-decorated class
(class is generic).</summary>

<remarks>This class is unsuitable as an <see cref="AutoInstantiateAttribute"
/>-decorated class since it is generic.  This prevents an instance of it from
being created automatically, as the system is unable to determine which type of
class to auto-instantiate.</remarks>
*/
//=============================================================================

    [AutoInstantiate]
    public sealed class GenericSingleton <SomeType>:
        GenericSingletonBase
    {
    }

//=============================================================================
/**
<summary>Base for abstract singleton class.</summary>

<remarks>This class is required because the contraints for the Singleton's type
argument cannot be satisfied by an abstract type subclass.</remarks>
*/
//=============================================================================

    public class AbstractSingletonBase:
        Singleton <AbstractSingletonBase>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Do the self-destruction.</summary>

<remarks>In order to self-destruct, we need to initialise our base type.  This
is best done by using the single instance to reference a member, such as this
function.  This function will never actually get called, as an exception should
arise when we initialize the base type.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void Destruct ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Self-destruct.</summary>

<remarks>When this function is called for the first time, the <see
cref="Singleton {SingletonBase}" /> base class's static constructor will be
called and should throw an exception, since we cannot instantiate an abstract
class.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static void SelfDestruct ()
        {
            Instance.Destruct ();
        }
    }

//=============================================================================
/**
<summary>Unsuitable <see cref="AutoInstantiateAttribute" />-decorated class
(class is abstract).</summary>

<remarks>This class is unsuitable as an <see cref="AutoInstantiateAttribute"
/>-decorated class since it is abstract.  This prevents an instance of it from
being created automatically, since an abstract class cannot be
instantiated.</remarks>
*/
//=============================================================================

    [AutoInstantiate]
    public abstract class AbstractSingleton:
        AbstractSingletonBase
    {
    }

//=============================================================================
/**
<summary>Base for private default contructor singleton class.</summary>

<remarks>This class is required because the contraints for the Singleton's type
argument cannot be satisfied by a type without a visible constructor.</remarks>
*/
//=============================================================================

    public class PrivateCtorSingletonBase:
        Singleton <PrivateCtorSingletonBase>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Do the self-destruction.</summary>

<remarks>In order to self-destruct, we need to initialise our base type.  This
is best done by using the single instance to reference a member, such as this
function.  This function will never actually get called, as an exception should
arise when we initialize the base type.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void Destruct ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Self-destruct.</summary>

<remarks>When this function is called for the first time, the <see
cref="Singleton {SingletonBase}" /> base class's static constructor will be
called and should throw an exception, since we cannot instantiate through a
private constructor.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static void SelfDestruct ()
        {
            Instance.Destruct ();
        }
    }

//=============================================================================
/**
<summary>Unsuitable <see cref="AutoInstantiateAttribute" />-decorated class
(class has private default constructor).</summary>

<remarks>This class is unsuitable as an <see cref="AutoInstantiateAttribute"
/>-decorated class since it has a private default constructor.  This prevents
an instance from being created automatically, since the required constructor is
inaccessible.</remarks>
*/
//=============================================================================

    [AutoInstantiate]
    public sealed class PrivateCtorSingleton:
        PrivateCtorSingletonBase
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>This constructor is private and so cannot be called from outside of
this class.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private PrivateCtorSingleton ()
        {
        }
    }

//=============================================================================
/**
<summary>Base for missing default contructor singleton class.</summary>

<remarks>This class is required because the contraints for the Singleton's type
argument cannot be satisfied by a class with no default constructor.</remarks>
*/
//=============================================================================

    public class MissingCtorSingletonBase:
        Singleton <MissingCtorSingletonBase>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Do the self-destruction.</summary>

<remarks>In order to self-destruct, we need to initialise our base type.  This
is best done by using the single instance to reference a member, such as this
function.  This function will never actually get called, as an exception should
arise when we initialize the base type.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void Destruct ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Self-destruct.</summary>

<remarks>When this function is called for the first time, the <see
cref="Singleton {SingletonBase}" /> base class's static constructor will be
called and should throw an exception, since we cannot instantiate through a
missing constructor.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static void SelfDestruct ()
        {
            Instance.Destruct ();
        }
    }

//=============================================================================
/**
<summary>Unsuitable <see cref="AutoInstantiateAttribute" />-decorated class
(class has no default constructor).</summary>

<remarks>This class is unsuitable as an <see cref="AutoInstantiateAttribute"
/>-decorated class since it has no default constructor.  This prevents an
instance from being created automatically, since the required constructor does
not exist.</remarks>
*/
//=============================================================================

    [AutoInstantiate]
    public sealed class MissingCtorSingleton:
        MissingCtorSingletonBase
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-default constructor.</summary>

<remarks>The presence of this constuctor prevents the compiler from generating
a default constructor, and should therefore make this class invalid.</remarks>

<param name="someObject">A <see cref="System.Object" /> instance being passed
solely to remove a default constructor from this class.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public MissingCtorSingleton (object someObject)
        {
        }
    }

//=============================================================================
/**
<summary>Base for common base singleton class.</summary>

<remarks>This class is a polymorphic single base class, from which we'll create
two <see cref="AutoInstantiateAttribute" />'d subclasses, in order to get an
exception.</remarks>
*/
//=============================================================================

    public class CommonSingletonBase:
        Singleton <CommonSingletonBase>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Do the self-destruction.</summary>

<remarks>In order to self-destruct, we need to initialise our base type.  This
is best done by using the single instance to reference a member, such as this
function.  This function will never actually get called, as an exception should
arise when we initialize the base type.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private void Destruct ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Self-destruct.</summary>

<remarks>When this function is called for the first time, the <see
cref="Singleton {SingletonBase}" /> base class's static constructor will be
called and should throw an exception, since we cannot have two <see
cref="AutoInstantiateAttribute" />-decorated subclasses.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static void SelfDestruct ()
        {
            Instance.Destruct ();
        }
    }

//=============================================================================
/**
<summary>First common singleton instance class.</summary>

<remarks>This class is the first singleton based upon the <see
cref="CommonSingletonBase" /> class.  That would be fine until we create a
second...</remarks>
*/
//=============================================================================

    [AutoInstantiate]
    public class CommonSingleton1:
        CommonSingletonBase
    {
    }

//=============================================================================
/**
<summary>Second common singleton instance class.</summary>

<remarks>This class is the second singleton based upon the <see
cref="CommonSingletonBase" /> class.  This should cause an exception to be
thrown when we attempt to use the polymorphic base class.</remarks>
*/
//=============================================================================

    [AutoInstantiate]
    public class CommonSingleton2:
        CommonSingletonBase
    {
    }

//=============================================================================
/**
<summary>Standalone singleton.</summary>

<remarks>This class is a standalone singleton class, with no derived classes.
Although undecorated by <see cref="AutoInstantiateAttribute" />, it should
still instantiated automatically.</remarks>
*/
//=============================================================================

    public sealed class StandaloneSingleton:
        Singleton <StandaloneSingleton>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Report the actual type of the single instance.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static System.Type InstanceType
        {
            get
            {
                return Instance.GetType ();
            }
        }
    }

//=============================================================================
/**
<summary>Polymorphic singleton.</summary>

<remarks>This class is a polymorphic singleton class, instantiated through a
derived class.</remarks>
*/
//=============================================================================

    public class PolymorphicSingleton:
        Singleton <PolymorphicSingleton>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Report the actual type of the single instance.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static System.Type InstanceType
        {
            get
            {
                return Instance.GetType ();
            }
        }
    }

//=============================================================================
/**
<summary>Concrete representation of polymorphic singleton.</summary>
*/
//=============================================================================

    [AutoInstantiate]
    public sealed class ConcreteSingleton:
        PolymorphicSingleton
    {
    }

//=============================================================================
/**
<summary>NUnit test fixture for the <see cref="Singleton {SingletonBase}" />
class.</summary>
*/
//=============================================================================

    [TestFixture]
    public sealed class SingletonTest:
        System.Object
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that using a generic singleton class will fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (System.TypeInitializationException))]
        public void UseGenericSingleton ()
        {
            bool sawException = false;
            try
            {
                GenericSingletonBase.SelfDestruct ();
            }
            catch (System.TypeInitializationException e) {
                Assert.AreEqual (e.InnerException.GetType (), typeof
                (BadAutoInstantiateException));
                sawException = true;
                throw (e);
            }
            finally
            {
                Assert.IsTrue (sawException);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that using an abstract singleton class will fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (System.TypeInitializationException))]
        public void UseAbstractSingleton ()
        {
            bool sawException = false;
            try
            {
                AbstractSingletonBase.SelfDestruct ();
            }
            catch (System.TypeInitializationException e) {
                Assert.AreEqual (e.InnerException.GetType (), typeof
                (BadAutoInstantiateException));
                sawException = true;
                throw (e);
            }
            finally
            {
                Assert.IsTrue (sawException);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that using a singleton class having a private default constructor
will fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (System.TypeInitializationException))]
        public void UsePrivateCtorSingleton ()
        {
            bool sawException = false;
            try
            {
                PrivateCtorSingletonBase.SelfDestruct ();
            }
            catch (System.TypeInitializationException e) {
                Assert.AreEqual (e.InnerException.GetType (), typeof
                (BadAutoInstantiateException));
                sawException = true;
                throw (e);
            }
            finally
            {
                Assert.IsTrue (sawException);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that using a singleton class that does not have a default
constructor will fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (System.TypeInitializationException))]
        public void UseMissingCtorSingleton ()
        {
            bool sawException = false;
            try
            {
                MissingCtorSingletonBase.SelfDestruct ();
            }
            catch (System.TypeInitializationException e) {
                Assert.AreEqual (e.InnerException.GetType (), typeof
                (BadAutoInstantiateException));
                sawException = true;
                throw (e);
            }
            finally
            {
                Assert.IsTrue (sawException);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that using a singleton class that does not have a default
constructor will fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (System.TypeInitializationException))]
        public void UseCommonSingleton ()
        {
            bool sawException = false;
            try
            {
                CommonSingletonBase.SelfDestruct ();
            }
            catch (System.TypeInitializationException e) {
                Assert.AreEqual (e.InnerException.GetType (), typeof
                (SingletonException));
                sawException = true;
                throw (e);
            }
            finally
            {
                Assert.IsTrue (sawException);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that an ordinary, standalone singleton can be constructed
OK.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void UseStandaloneSingleton ()
        {
            Assert.AreEqual (StandaloneSingleton.InstanceType, typeof
            (StandaloneSingleton));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that manually instantiating a standalone singleton will
fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (SingletonException))]
        public void UseStandaloneSingletonWrongly ()
        {
            StandaloneSingleton singleton = new StandaloneSingleton ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that a concrete polymorphic singleton can be constructed
OK.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        public void UseConcreteSingleton ()
        {
            Assert.AreEqual (PolymorphicSingleton.InstanceType, typeof
            (ConcreteSingleton));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that manually instantiating a polymorphic singleton,
auto-instantiated elsewhere as a concrete singleton, will fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (SingletonException))]
        public void UsePolymorphicSingletonWrongly ()
        {
            PolymorphicSingleton singleton = new PolymorphicSingleton ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Test that manually instantiating a concrete polymorphic singleton,
already auto-instantiated as a concrete singleton, will fail.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        [Test]
        [ExpectedException (typeof (SingletonException))]
        public void UseConcreteSingletonWrongly ()
        {
            PolymorphicSingleton singleton = new ConcreteSingleton ();
        }
    }
}
