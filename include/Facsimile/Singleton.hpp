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
this program.  If not, see <http://www.gnu.org/licenses.

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

$Id$
*/
//=============================================================================
/**
\file
%Facsimile Singleton class C++ header file.

C++ header file for the Singleton class, and associated elements, that are
integral members of the Facsimile namespace.
*/
//=============================================================================

/**
Include guard.
*/

#ifndef FACSIMILE_SINGLETON_HPP_
#define FACSIMILE_SINGLETON_HPP_

/*
Relevant header files.
*/

#include <cassert>
#include <typeinfo>
#include <boost/concept_check.hpp>
//include <boost/concept/assert.hpp> - Missing from Ubuntu distribution...
#include <boost/thread/once.hpp>
#include <boost/thread/recursive_mutex.hpp>
#include <Facsimile/NonCopyable.hpp>
#include <Facsimile/X/SingletonException.hpp>
/*
Namespace declaration.
*/

namespace Facsimile
{

//=============================================================================
/**
%Singleton template abstract base class.

This is a helper class that provides thread-safe support for
<em>singletons</em>, sub-classes that implement the <a
href="http://en.wikipedia.org/wiki/Singleton_pattern"><em>%Singleton</em>
design pattern</a>.  Refer to Gamma, et al, <em>Design Patterns: Elements of
Reusable Object-Oriented Software</em>, Addison Wesley.

The primary feature of a singleton is that only a single instance may be
created.

\remarks The single instance is accessed, either publicly or otherwise, via the
getInstance() static member function.  This single instance is created using
<a href="http://en.wikipedia.org/wiki/Lazy_initialization"><em>lazy
initialization</em></a>, meaning that it is created and initialized only when
getInstance() is first called.  If no calls to getInstance() are made, the
single instance is never created.  Once created, this instance lasts throughout
the owning application's lifespan.

\par
Attempts to create instances manually, either on the stack or via the
<code><strong>new</strong></code> operator, will fail with the constructor
throwing an exception in such circumstances.

\tparam T The singleton type required.  This type must be both a sub-class of
Facsimile::Singleton (the <a
href="http://en.wikipedia.org/wiki/Curiously_Recurring_Template_Pattern"
><em>Curiously Recurring Template</em> design pattern</a> is employed) and must
also be default constructible; these traits are verified during template
instantiation.

\see getInstance()

\see <a href="http://en.wikipedia.org/wiki/Singleton_pattern">Singleton Design
Pattern</a>.

\see <a ref="http://en.wikipedia.org/wiki/Curiously_Recurring_Template_Pattern"
>Curiously Recurring Template Pattern</a>.

\see <a href="http://en.wikipedia.org/wiki/Lazy_initialization">Lazy
Initialization</a>.

\internal
Lazy initialization is employed because of potential problems with the general
use of <em>eager initialization</em> (where the instance is created statically
at compile and/or link time).

\par
In case of eager initialization, the sole singleton instance will be created
during code start-up, before the <code>main ()</code> function is called.  The
primary advantage of this method is that singleton creation is thread-safe.
However, the order in which potentially dependent singletons are initialized
cannot be guaranteed, leading to dependency problems.

\par
At the same time, lazy initialization has its problems too: since the C++
language definition does not account for multiple threads of execution, the
double-checked locking pattern (used commonly to implement lazy initialization
in other programming languages) does not work reliably in C++.  Refer to Meyers
and Alexandrescu's revised <a href="http://www.ddj.com/">Dr. Dobbs Journal</a>
article <a href="http://www.aristeia.com/Papers/DDJ_Jul_Aug_2004_revised.pdf"
><em>C++ and the Perils of Double-Checked Locking</em></a>.

We've attempted to resolve these issues using an approach that involves the use
of getter functions.  The downside is that this adds an extra level of
indirection when referencing singleton data through getInstance() and therefore
reduces efficiency very slightly.  The upside is that it should help to
overcome the execution order and cache coherency isses.  Refer to getInstance()
for further information.

\see getInstanceCreate()

\see getInstanceReturn()

\see <a href="http://www.aristeia.com/Papers/DDJ_Jul_Aug_2004_revised.pdf"
>C++ and the Perils of Double-Checked Locking</a>
*/
//=============================================================================

    template <typename T>
    class Singleton:
        private NonCopyable
    {

/*
Use Boost concept-checking macros to validate some characteristics of T when
the template is instantiated.  The intention is to provide more user-friendly
compiler error messages if the characteristics of T do not match how T is being
referenced within the template.

Note: The double round brackets are essential and should not be removed in
favour of single brackets!
*/

        //BOOST_CONCEPT_ASSERT ((boost::DefaultConstructible <T>)); - Missing
        //TODO: CanCopy (T, Singleton <T>);

/**
Construction valid flag.

If set, this variable indicates that the constructor is being called from the
initialize() function and, therefore, indicates that construction is valid.  If
false, and the constructor is called, then this indicates that the constructor
is being invoked in some other way, violating the terms of the %Singleton
pattern.
*/

    private:
        static bool isConstructionValid;

/**
Singleton initialization flag.

This flag, which is used by the boost::thread library, is used to control
initialization of the single instance.  It ensures that the initialization
function is called once only.

\remarks This is necessary because, in a multi-threaded environment, it can be
difficult to ensure that only a single thread actually executes the
initialization code.  Having the initialization code execute more than once is
a big problem.
*/

    private:
        static boost::once_flag isInitialized;

/**
Mutual exclusion flag.

Assures that one, and only one, instance of the singleton is created, even in a
multi-threaded environment.

\remarks This is a <em>recursive</em> mutex since it allows the same thread to
lock the mutex more than once.  This is necessary as we need to lock the mutex
within the getInstanceCreate() function and within the default constructor.
*/

    private:
        static boost::recursive_mutex staticMutex;

/**
Single, global instance.

This value will be 0 until the first attempt to use the singleton is made.
*/

    private:
        static T* instance;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Retrieve reference to singleton instance.

\remarks This function employs <em>lazy initialization</em>, meaning that the
sole singleton instance is only created when first referenced.

\note This code is thread-safe.  However, this thread-safety does not cover
attempts to modify singleton data, which must be made thread-safe by any
implementor.

\return A reference to the sole singleton instance.

\see http://en.wikipedia.org/wiki/Lazy_initialization
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        static T& getInstance () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Create single instance and store a pointer to it.

This function is called when the sole instance is first referenced.  It is
called from the boost::thread library in a thread-safe manner that guarantees
that the function is called once only.  Furthermore, any threads that try to
access the singleton are blocked until this function has been completed.

\remarks For some background on singleton initialization in a multi-threaded
environment, particularly with reference to the commonly used double-checked
locking pattern, refer to Meyers and Alexandrescu's paper <a
href="http://www.aristeia.com/Papers/DDJ_Jul_Aug_2004_revised.pdf">C++ and the
Perils of Double-Checked Locking</a>.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private:
        static void initialize () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

\remarks This constructor verifies that only a single instance of this class
is instantiated, and that this instance has been created via the first call to
getInstance().

\exception Facsimile::X::SingletonException Attempt to create an
<em>unofficial</em> instance, that is, an instance not created via the first
call to getInstance(), failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected:
        Singleton () throw (Facsimile::X::SingletonException);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.

Having a virtual destructor forces all singletons to be polymorphic.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        virtual ~Singleton () throw ();
    };

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Template static data initialization.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

/*
Initialization of the isConstructionValid flag.
*/

    template <typename T>
    bool Singleton <T>::isConstructionValid = false;

/*
Initialization of the static mutual exclusion member.
*/

    template <typename T>
    boost::recursive_mutex Singleton <T>::staticMutex;

/*
Initialization of the isInitialized flag.
*/

    template <typename T>
    boost::once_flag Singleton <T>::isInitialized = BOOST_ONCE_INIT;

/*
Initialization of the all important instance member.
*/

    template <typename T>
    T* Singleton <T>::instance = 0;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Singleton <T>::getInstance () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    template <typename T>
    inline T& Singleton <T>::getInstance () throw ()
    {

/*
Call the initialization function, once only to set up the sole instance.  This
call ensures that only a single thread calls the initialize () function.  All
other threads are blocked until that has been done.
*/

        boost::call_once (initialize, isInitialized);

/*
Now de-reference the sole instance and return it.
*/

        assert (instance);
        return *instance;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Singleton <T>::initialize () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    template <typename T>
    void Singleton <T>::initialize () throw ()
    {

/*
Lock (recursively) the static mutex.  Why do this when this function should
only ever be called once anyway?  Well, that's because we need to claim this
same mutex a second time in the constructor in order to verify that the
constructor is being called during singleton initialization, and not through
the creation of any other instance.
*/

        boost::recursive_mutex::scoped_lock lock (staticMutex);

/*
OK.  Set the flag indicating that this call to the constructor is valid.  This
flag must be cleared by the constructor.
*/

        isConstructionValid = true;

/*
Create the new sole instance.

Note: Any exceptions arising will render the singleton unitialized and
unusable, and will - in any case - terminate the application.
*/

        instance = new T ();
        assert (instance);
        assert (!isConstructionValid);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Singleton <T>::Singleton () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    template <typename T>
    Singleton <T>::Singleton () throw (Facsimile::X::SingletonException)
    {

/*
Claim the recursive mutex.  This ensures that we're looking at the most
up-to-date set of static member data.

The lock is released automatically when the function exits.

Note: If this were a regular mutex, rather than a recursive mutex, then valid
construction would gridlock since the mutex will have already been locked by
initialize().
*/

        boost::recursive_mutex::scoped_lock lock (staticMutex);

/*
If the constructor is being called by anything other than initialize (), then
throw the singleton exception.
*/

        if (!isConstructionValid)
        {
            throw Facsimile::X::SingletonException (typeid (T).name ());
        }

/*
Now ensure that any subsequent attempt to construct a singleton sub-class
manually will fail.
*/

        isConstructionValid = false;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Singleton <T>::~Singleton () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    template <typename T>
    Singleton <T>::~Singleton () throw ()
    {
    }
}
#endif /*FACSIMILE_SINGLETON_HPP_*/
