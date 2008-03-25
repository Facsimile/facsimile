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

C# source file for the Singleton class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Singleton abstract base class.</summary>

<remarks>This class implements basic singleton behaviour for classes that need
to implement the "Singleton" design pattern, whilst at the same time allowing
base classes to be derived from it.  Refer to Gamma, et al: "Design Patterns:
Elements of Reusable Object-Oriented Software", Addison-Wesley, for further
information.

<para>The single <typeparamref name="SingetonBase" /> class instance will be
constructed automatically, using its default constructor, unless one (and only
one) of its derived classes is decorated with the <see
cref="AutoInstantiateAttribute" /> attribute.  In this latter case, an instance
of the decorated class will be constructed automatically,using its default
constructor, and used as the singleton's instance reference.</para>

<para>The following rules govern which class is instantiated as the singleton's
sole instance:</para>

<list type="number">
    <item>
        <description>If <typeparamref name="SingletonBase" /> has a single
        sub-class that is decorated with <see cref="AutoInstantiateAttribute"
        />, then it will be instantiated.</description>
    </item>
    <item>
        <description>If <typeparamref name="SingletonBase" /> does not have any
        sub-classes decorated with <see cref="AutoInstantiateAttribute" />,
        then <typeparamref name="SingletonBase" /> itself will be
        instantiated.</description>
    </item>
</list>

<para>However, the following usage criteria must be followed for singleton
class and sub-classes:</para>

<list type="bullet">
    <item>
        <description>If more than one sub-class is decorated, then the logic
        cannot determine which sub-class to instantiate, resulting in a
        <see cref="SingletonException" /> exception being
        thrown.</description>
    </item>
    <item>
        <description>Decorated sub-classes cannot be abstract.  Decorating an
        abstract sub-class will result in a <see
        cref="BadAutoInstantiateException" /> exception being
        thrown.</description>
    </item>
    <item>
        <description>Decorated sub-classes cannot be generic.  Decorating a
        generic sub-class will result in a <see
        cref="BadAutoInstantiateException" /> exception being
        thrown.</description>
    </item>
    <item>
        <description>Decorated sub-classes must have an accessible (i.e.
        public) default constructor.  Decorating a sub-class that does not have
        a public default constructor will result in a <see
        cref="BadAutoInstantiateException" /> exception being
        thrown.</description>
    </item>
    <item>
        <description>Manually instantiating <typeparamref name="SingletonBase"
        /> or any of its sub-classes manually will result in a <see
        cref="SingletonException" /> exception being thrown.  This is because
        only a single instance can be defined at one time, and this is done
        automatically by the Facsimile framework.</description>
    </item>
    <item>
        <description>The order of static and instance construction for
        Singletons is reversed.  Construction of the single instance typically
        takes place before static construction.  When writing the instance
        constructors for derived classes, do not rely upon any static class
        members as static construction will be incomplete.  Similarly, when
        writing static constructors and members, you may assume that the
        single instance has finished being constructed.</description>
    </item>
</list>

<para>Note that the constraints on <typeparamref name="SingletonBase" /> imply
that it cannot be an abstract class and must have a public constructor (even
though public constructors are undesirable).</para>

<para>Derived classes should provide static members to reference
single-instance data through the <see cref="Instance" /> property.</para>

<para>If your singleton class does not need to be polymorphic, consider making
it a static class instead of deriving from this class.</para></remarks>

<typeparam name="SingletonBase">The <see cref="Singleton {SingletonBase}"
/> sub-class that is the polymorphic singleton base class.  This class
cannot be abstract and must have a public, default constructor.</typeparam>
*/
//=============================================================================

    public abstract class Singleton <SingletonBase>:
        System.Object
    where SingletonBase:
        Singleton <SingletonBase>, new ()
    {

/**
<summary>The one-and-only instance of this object.</summary>

<remarks>C# generics newbies should note that each template instance has its
own set of static data members.  That is, Singleton &lt;A&gt;.instance is not
the same object as Singleton &lt;B&gt;.instance.  (In Java, these two objects
are identical.)  Consequently, there is exactly one instance reference for each
type of Singleton.

<para>This must be initialized to null prior to the static constructor being
called, so that the default constructor can determine whether or not this is
the first instance or an erroneously-created second instance.</para>

<para>Note that <c>readonly</c> members can be modified in the static
constructor regardless of whether they have been initialized in the declarator
or not.</para></remarks>
*/

        private static readonly SingletonBase instance = null;

/**
<summary>Static data initialized flag.</summary>

<remarks>If true, then the static constructor has been initialized
successfully.  If false, then static initialization has yet to complete.

<para>This must be initialized to false prior to the static constructor being
called, so that static members can determine whether static initialization has
completed.</para>

<para>Note that <c>readonly</c> members can be modified in the static
constructor regardless of whether they have been initialized in the declarator
or not.</para></remarks>
*/

        private static readonly bool staticDataInitialized = false;

/**
<summary>Static data initialization in-progress flag.</summary>

<remarks>If true, then the static constructor is currently executing.  This
flag should be removed when it becomes apparent that the static constructor is
not being called recursively.

<para>This must be initialized to false prior to the static constructor being
called, so that the static constructor can determine whether it is being called
recursively.</para>

<para>Note that <c>readonly</c> members can be modified in the static
constructor regardless of whether they have been initialized in the declarator
or not.</para></remarks>
*/

        private static readonly bool staticDataInitializing = false;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Constructs the single-instance of the singleton class, using
auto-instantiation (if applicable) and stores a reference to it for later use.

<para>Exactly which <typeparamref name="SingletonBase" /> sub-class we
instantiate is complex.  Refer to <see cref="Singleton {SingletonBase}" /> for
further information.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Singleton ()
        {

/*
Check that this function is not being called a second time.  I don't know
whether this can happen or not (given the way we're performing static
initialization), so I'm adding it as a safeguard.  See construction of the
single instance below for further information.

Throwing exceptions within static constructors is not exactly encouraged.  We
do it here because it's supposed to terminate the application.
*/

            if (staticDataInitializing)
            {
                throw new AssumptionFailureException ();
            }
            staticDataInitializing = true;

/*
Get a list of all of the types that are decorated with the
AutoInstantiateAttribute.
*/

            System.Collections.Generic.List <System.Type> types =
            Reflection.GetTypesWithAttribute (typeof
            (AutoInstantiateAttribute));

/*
Search through this list looking for a candidate, of which there may be either
0 or 1; if there are two or more candidates, then we have an error.
*/

            bool candidateFound = false;
            System.Type candidate = null;
            System.Type singletonBaseType = typeof (SingletonBase);
            foreach (System.Type type in types)
            {

/*
If the type is a sub-class of SingletonBase, then it is a candidate.
*/

                if (type == singletonBaseType || type.IsSubclassOf
                (singletonBaseType))
                {

/*
If we already have a candidate, then this is an error - as we cannot have more
than one.

Throwing exceptions within static constructors is not exactly encouraged.  We
do it here because it's supposed to terminate the application.
*/

                    if (candidateFound)
                    {
                        System.Diagnostics.Debug.Assert (candidate != null);
                        throw new SingletonException (singletonBaseType,
                        candidate, type);
                    }

/*
OK.  So this is our candidate.  That doesn't mean that we've accepted it -
there're a few more tests to go.
*/

                    candidateFound = true;
                    candidate = type;
                }
            }

/*
OK.  If we found our candidate, then we have to check that it is a valid
candidate before we can instantiate it.  Start by retrieving its default
constructor.
*/

            if (candidateFound)
            {
                System.Reflection.ConstructorInfo defaultCtor =
                candidate.GetConstructor (System.Type.EmptyTypes);

/*
Check that the candidate is not abstract, or generic and that it has an
accessible default constructor.  If not, we're going to have to throw an
exception once more.

Throwing exceptions within static constructors is not exactly encouraged.  We
do it here because it's supposed to terminate the application.
*/

                if (candidate.IsAbstract || candidate.IsGenericType ||
                defaultCtor == null)
                {
                    throw new BadAutoInstantiateException (candidate);
                }

/*
OK.  Here's an interesting thing.  We can create our single instance quite
simply at this point.  However, as we're not referencing the constructor
through the class, we're also not (necessarily) invoking the static constructor
for the candidate class, and maybe some of that class's base classes.

So, here's the options - and the expected drawbacks:
1. Force type initialization (static construction), before instantiating the
single instance.  The problem here is that any static constructors, which might
like to access static members of base classes (not unreasonable), will find
that some of those base classes (this one, for instance) have not yet finished
initializing.
2. Force type initialization (static construction), after instantiating the
single instance.  The problem here is that default constructors, which might
like to access static members of base classes (not unreasonable), will find
that some of those base classes (this one, for instance) have not yet finished
initializing.
3. Ignore the issue and hope it goes away.  Well, enough said.
4. Forget all about auto-instantiation and use the traditional method of having
the user instantiate their preferred Singleton-derived class manually.  This
would involve re-writing the whole Singleton class (complicating things for the
user, but simplifying things for the developer).

Since we're supposed to be accessing the Singleton's features through static
members, rather than through instance members, it appears that the natural
order of static construction followed by instance construction is reversed for
Singletons.  That is, instance construction should take place first, followed
immediately by static construction - with the user accessing the Singleton
through static members.  Bearing this in mind, we'll opt for the second choice
and we'll see how practical this is.
*/

                instance = (SingletonBase) defaultCtor.Invoke
                (System.Type.EmptyTypes);
                Util.InitializeType (candidate);
            }

/*
Otherwise, create a new instance of the default constructor.  We do not need to
force type initialization, as it will take place automatically here.
*/

            else
            {
                instance = new SingletonBase ();
            }

/*
Verify that we instantiated something.
*/

            System.Diagnostics.Debug.Assert (instance != null);

/*
Signal that static initialization has completed.
*/

            staticDataInitialized = true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>Confirm that the one-and-only instance reference is null or throw an
exception.  (This method should only execute from within the static constructor
- before the instance is actually initialized - on a single occasion.  If the
instance is non-null when this constructor is invoked, then this must be
another instantiation attempt, which is forbidden by the Singleton design
pattern.)</remarks>

<exception cref="SingletonException">Thrown if the one-and-only instance has
already been constructed.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected Singleton ()
        {

/*
Confirm that the instance is currently null, and that static initialization has
yet to complete, or throw an exception.
*/

            if (instance != null || staticDataInitialized)
            {
                throw new SingletonException (typeof (SingletonBase),
                instance.GetType (), GetType ());
            }

/*
We do not need to update instance, as this will be done in the static
constructor.
*/

        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Object single-instance reference.</summary>

<value>The <typeparamref name="SingletonBase" />-derived instance reference,
which is the sole instance of this class.  This value is guaranteed not to be
null, and will not change during the course of the simulation.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected static SingletonBase Instance
        {
            get
            {
                System.Diagnostics.Debug.Assert (staticDataInitialized);
                System.Diagnostics.Debug.Assert (instance != null);
                return instance;
            }
        }
    }
}
