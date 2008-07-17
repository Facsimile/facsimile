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
Facsimile::NonCopyable class test suite C++ source file.

C++ source file for the Facsimile::NonCopyable class test suite.
*/
//=============================================================================

/*
Relevant header files.
*/

#include "Facsimile_NonCopyable.hpp"

/*
Register this test suite.
*/

CPPUNIT_TEST_SUITE_REGISTRATION (Test_Facsimile_NonCopyable);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_NonCopyable::testConstructionDestruction () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_NonCopyable::testConstructionDestruction () throw
(CppUnit::Exception)
{

/*
Check that we can create an instance of a NonCopyable sub-class and check that
no errors occur on destruction either.  (That's about all we can do, if the
code is to compile.
*/

    NotCopyable someUncopyableObject (5);
    CPPUNIT_ASSERT (someUncopyableObject.getValue () == 5);
}
