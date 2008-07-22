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
%Facsimile Test suite main C++ source file.

Main C++ source file for the %Facsimile test suite.  This file defines the
test suite entry point.
*/
//=============================================================================

/*
Relevant header files.
*/

#include <iostream>
#include <cppunit/extensions/TestFactoryRegistry.h>
#include <cppunit/ui/text/TestRunner.h>
#include <cppunit/TestResult.h>
#include <cppunit/TestResultCollector.h>
#include <cppunit/TextTestProgressListener.h>
#include <cppunit/TextOutputter.h>
#include "LogFile.hpp"
#include "Facsimile_Collectable.hpp"

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test suite main entry point.

\param argc Command line argument count.

\param argv Array of command line arguments.

\return 0 if successful, non-zero otherwise.  A value of 0 indicates that the
test suite ran successfully and that no tests failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

int main (int argc, char** argv)
{

/*
Before doing anything else, initialize the size of the collector's heap so that
we can test memory exhaustion.  If we don't do this here, then the heap can get
too large...
*/

    Heap::initialize ();

/*
The only command line argument that we get is suffix for log file generation.
If it's present, then store the suffix.
*/

    if (argc > 2)
    {
        std::cerr << "Too many command line arguments.  Tests aborting..." <<
        std::endl;
        return 1;
    }
    if (argc == 2)
    {
        LogFile::setSuffix (argv [1]);
    }

/*
Create the event manager and test controller.
*/

    CppUnit::TestResult controller;

/*
Create a listener that collects test results.  Add it to the controller.
*/

    CppUnit::TestResultCollector result;
    controller.addListener (&result);

/*
Add a listener that prints dots as the tests run.
*/

    CppUnit::TextTestProgressListener progress;
    controller.addListener (&progress);

/*
Create a text (console) based test runner to execute the tests, and the memory
allocation tests (that MUST come first), then add a test that encompasses all
tests in the registry.

DO NOT CHANGE THE ORDER OF THESE TESTS NOR ADD TESTS TO THE TOP OF THIS LIST.
*/

    CppUnit::TextUi::TestRunner runner;
    runner.addTest (Test_Facsimile_Collectable::suite ());
    runner.addTest (CppUnit::TestFactoryRegistry::getRegistry ().makeTest ());

/*
Now run all tests, using the controller created earlier.
*/

    runner.run (controller, "");

/*
Print test results in text format, sending output to the main output.
*/

    CppUnit::TextOutputter outputter (&result, std::cout);
    outputter.write ();

/*
Return 0 if all tests completed successfully, 1 otherwise.
*/

    return result.wasSuccessful ()? 0: 1;
}
