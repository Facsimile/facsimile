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
Facsimile::X exception class test suite C++ header file.

C++ header file for the Facsimile::X exception class test suite.  This tests
all instantiable exception classes.
*/
//=============================================================================

#ifndef TEST_FACSIMILE_X_HPP_
#define TEST_FACSIMILE_X_HPP_

/*
Relevant header files.
*/

#include <cppunit/TestAssert.h>
#include <cppunit/extensions/HelperMacros.h>
#include <Facsimile/X/ArrayDeleteNotSupportedException.hpp>
#include <Facsimile/X/ArrayNewNotSupportedException.hpp>
#include <Facsimile/X/SingletonException.hpp>
#include "LogFile.hpp"

/**
Facsimile::X namespace exception class log file name.
*/

#define FACSIMILE_X_FILENAME "log/Facsimile.X"

/**
Helper macro, to be used to clear log file.

This macro creates a LogFile instance called <code>log</code> that overwrites
any existing log data.
*/

#define LOG_CREATE LogFile log (FACSIMILE_X_FILENAME, true, true);

/**
Helper macro, to be used at the start of each test.

This macro creates a LogFile instance called <code>log</code> that allows data
to be appended to the Facsimile::X namespace exception classes.
*/

#define LOG_APPEND LogFile log (FACSIMILE_X_FILENAME, true, false);

//=============================================================================
/**
Test fixture for the Facsimile::X exception classes.

The tests included here are basic tests for exception construction and
description formatting.  Exception explanations are stored in a locale-specific
log file and must be manually verified before being accepted.

Each instantiable exception class must be represented within this test suite.
Sorry, no exceptions...

\remarks This test suite does <strong>not</strong> test how, or whether,
exceptions are thrown.  It merely tests that the exception classes themselves
are OK.
*/
//=============================================================================

class Test_Facsimile_X:
    public CppUnit::TestFixture
{

/*
Boilerplate code to declare the test suite.  New tests must be included in the
list.

DO NOT CHANGE THE ORDER OF THESE TESTS!  The clearLogFile() "test" must be
listed first.  Changes to the order will cause unncessary log file validation
failures.
*/

    CPPUNIT_TEST_SUITE (Test_Facsimile_X);
    CPPUNIT_TEST (clearLogFile);
    CPPUNIT_TEST (testArrayDeleteNotSupported);
    CPPUNIT_TEST (testArrayNewNotSupported);
    CPPUNIT_TEST (testSingleton);
    CPPUNIT_TEST_SUITE_END ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Dummy test function that merely clears the log file.

\remarks This test must execute before all others.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void clearLogFile () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Facsimile::X::ArrayDeleteNotSupportedException test function.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testArrayDeleteNotSupported () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Facsimile::X::ArrayNewNotSupportedException test function.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testArrayNewNotSupported () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Facsimile::X::SingletonException test function.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testSingleton () throw (CppUnit::Exception);
};

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_X::clearLogFile () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline void Test_Facsimile_X::clearLogFile () throw ()
{
    LOG_CREATE;
}
#endif /*TEST_FACSIMILE_X_HPP_*/
