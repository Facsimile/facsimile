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
Facsimile::NonCopyable class test suite C++ header file.

C++ header file for the Facsimile::NonCopyable class test suite.
*/
//=============================================================================

#ifndef TEST_FACSIMILE_NONCOPYABLE_HPP_
#define TEST_FACSIMILE_NONCOPYABLE_HPP_

/*
Relevant header files.
*/

#include <cppunit/TestAssert.h>
#include <cppunit/extensions/HelperMacros.h>
#include <Facsimile/NonCopyable.hpp>

//=============================================================================
/**
Non-copyable class.

This class, derived from Facsimile::NonCopyable, is used to test that such
sub-classes are non-copyable.
*/
//=============================================================================

class NotCopyable:
    private Facsimile::NonCopyable
{

/**
Some integer that we'll initialize and test so as not to get an unused variable
warning.
*/

private:
    const int someValue;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Constructor.

\param someInt Some integer value that will be stored by the class.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    NotCopyable (int someInt) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Get value.

\return The value stored by the class instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    int getValue () const throw ();
};

//=============================================================================
/**
Test fixture for the Facsimile::NonCopyable class.
*/
//=============================================================================

class Test_Facsimile_NonCopyable:
    public CppUnit::TestFixture
{

/*
Boilerplate code to declare the test suite.  New tests must be included in the
list.

DO NOT CHANGE THE ORDER OF THESE TESTS!
*/

    CPPUNIT_TEST_SUITE (Test_Facsimile_NonCopyable);
    CPPUNIT_TEST (testConstructionDestruction);
    CPPUNIT_TEST_SUITE_END ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test that a non-copyable instance can be created and deleted.

Check that sub-classes of Facsimile::NonCopyable can be constructed and
destructed with no problems.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testConstructionDestruction () throw (CppUnit::Exception);
};

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NotCopyable::NotCopyable (int) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline NotCopyable::NotCopyable (int someInt) throw ():
    someValue (someInt)
{
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NotCopyable::getValue () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline int NotCopyable::getValue () const throw ()
{
    return someValue;
}
#endif /*TEST_FACSIMILE_NONCOPYABLE_HPP_*/
