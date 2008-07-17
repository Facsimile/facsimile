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
Facsimile::Singleton<T> class test suite C++ header file.

C++ header file for the Facsimile::Singleton<T> class test suite.
*/
//=============================================================================

#ifndef TEST_FACSIMILE_SINGLETON_HPP_
#define TEST_FACSIMILE_SINGLETON_HPP_

/*
Relevant header files.
*/

#include <string>
#include <boost/thread/mutex.hpp>
#include <cppunit/TestAssert.h>
#include <cppunit/extensions/HelperMacros.h>
#include <Facsimile/Singleton.hpp>

//=============================================================================
/**
A simple singleton test class.

This class will be used to test singleton construction in a single-threaded
environment.
*/
//=============================================================================

class SingletonTest:
    public Facsimile::Singleton <SingletonTest>
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Miscellaneous test function to return instance data.

\return A string with some miscellaneous data.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    std::string getSomeString () const throw ();
};

//=============================================================================
/**
Another simple singleton test class.

This class will be used to test singleton construction in a multi-threaded
environment.
*/
//=============================================================================

class SingletonTest2:
    public Facsimile::Singleton <SingletonTest2>
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Miscellaneous test function to return instance data.

\return A string with some miscellaneous data.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    std::string getSomeString () const throw ();
};

//=============================================================================
/**
Test fixture for the Facsimile::Singleton<T> class.
*/
//=============================================================================

class Test_Facsimile_Singleton:
    public CppUnit::TestFixture
{

/*
Boilerplate code to declare the test suite.  New tests must be included in the
list.

DO NOT CHANGE THE ORDER OF THESE TESTS!
*/

    CPPUNIT_TEST_SUITE (Test_Facsimile_Singleton);
    CPPUNIT_TEST (testBasicConstruction);
    CPPUNIT_TEST (testMultiThreadConstruction);
    CPPUNIT_TEST_SUITE_END ();

/**
Mutex used to control access to static data.
*/

private:
    static boost::mutex staticMutex;

/**
Pointer to sole instance of singleton used in multi-threaded tests.

\remarks A mutex object must be locked before this value can be modified or
examined.
*/

private:
    static SingletonTest2* multiSingleton;

/**
Number of failures found during multi-threaded execution.

This value is incremented each time a thread detects that the address of the
sole singleton instance differs from the stored value.
*/

private:
    static int numFailures;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Thread execution function.

This function is called as the primary operation of a number of different
threads.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

private:
    static void threadExecute () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test basic singleton construction.

Here, we test that attempts to manually construct a singleton will fail, that
the singleton's getInstance () function succeeds and other basic stuff.  This
doesn't stress any of the multi-threaded issues.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testBasicConstruction () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test multi-threaded singleton construction.

Here, we attempt to test that construction of a singleton from multiple threads
simultaneously will succeed, with only a single instance being created.

\remarks This function creates a number of threads of execution all of which
try to create the singleton instance at the same time.  Only one of them should
succeed.

\note Multi-threaded tests such as these have random effects, since they depend
upon processor workload, number of processors available, etc.  If there is a
bug, it's theoretically possible that the test could sometimes pass and
sometimes fail.  Just because this test passes does not mean that the code is
good.  Testing many times should indicate that the code is OK.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testMultiThreadConstruction () throw (CppUnit::Exception);
};

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
SingletonTest::getSomeString () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline std::string SingletonTest::getSomeString () const throw ()
{
    return std::string ("Some single-threaded string");
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
SingletonTest2::getSomeString () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline std::string SingletonTest2::getSomeString () const throw ()
{
    return std::string ("Some multi-threaded string");
}
#endif /*TEST_FACSIMILE_SINGLETON_HPP_*/
