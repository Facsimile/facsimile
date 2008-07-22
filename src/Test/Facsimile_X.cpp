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

$Id$
*/
//=============================================================================
/**
\file
Facsimile::X exception class test suite C++ source file.

C++ source file for the Facsimile::X exception class test suite.  This tests
all instantiable exception classes.
*/
//=============================================================================

/*
Relevant header files.
*/

#include <typeinfo>
#include "Facsimile_X.hpp"

/*
Register this test suite.
*/

CPPUNIT_TEST_SUITE_REGISTRATION (Test_Facsimile_X);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_X::testArrayDeleteNotSupported () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_X::testArrayDeleteNotSupported () throw
(CppUnit::Exception)
{

/*
Create as many variants of the exception as possible and log their
explanations.
*/

    LOG_APPEND;
    Facsimile::X::ArrayDeleteNotSupportedException e1;
    log << UNICODE_STRING_SIMPLE ("ArrayDeleteNotSupportedException (): ") <<
    e1.cause () << std::endl;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_X::testArrayNewNotSupported () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_X::testArrayNewNotSupported () throw (CppUnit::Exception)
{

/*
Create as many variants of the exception as possible and log their
explanations.
*/

    LOG_APPEND;
    Facsimile::X::ArrayNewNotSupportedException e1;
    log << UNICODE_STRING_SIMPLE ("ArrayNewNotSupportedException (): ") <<
    e1.cause () << std::endl;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_X::testSingleton () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_X::testSingleton () throw (CppUnit::Exception)
{

/*
Create as many variants of the exception as possible and log their
explanations.
*/

    LOG_APPEND;
    Facsimile::X::SingletonException e1 (UNICODE_STRING_SIMPLE
    ("SomeTestSingleton"));
    log << UNICODE_STRING_SIMPLE ("SingletonException "
    "(\"SomeTestSingleton\"): ") << e1.cause () << std::endl;
}

