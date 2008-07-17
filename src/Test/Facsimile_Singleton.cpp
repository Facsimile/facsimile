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
Facsimile::Singleton<T> class test suite C++ source file.

C++ source file for the Facsimile::Singleton<T> class test suite.
*/
//=============================================================================

/*
Relevant header files.
*/

#include <boost/thread/thread.hpp>
#include "Facsimile_Singleton.hpp"

/*
Register this test suite.
*/

CPPUNIT_TEST_SUITE_REGISTRATION (Test_Facsimile_Singleton);

/*
Initialization of mutex in test class.
*/

boost::mutex Test_Facsimile_Singleton::staticMutex;

/*
Initialization of the sole singleton instance pointer.
*/

SingletonTest2* Test_Facsimile_Singleton::multiSingleton = 0;

/*
Initialization of failures found during multi-threaded execution.
*/

int Test_Facsimile_Singleton::numFailures = 0;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Singleton::testBasicConstruction () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Singleton::testBasicConstruction () throw
(CppUnit::Exception)
{

/*
Attempt to create a new singleton instance on the free store.  This should fail
will a singleton exception being thrown.
*/

    CPPUNIT_ASSERT_THROW (new SingletonTest (),
    Facsimile::X::SingletonException);

/*
OK.  Test that we can get data from the singleton via getInstance.  Store the
pointer and ensure that it does not change between calls.
*/

    SingletonTest& singleton = SingletonTest::getInstance ();
    CPPUNIT_ASSERT (&singleton == &(SingletonTest::getInstance ()));
    CPPUNIT_ASSERT (singleton.getSomeString () != "");

/*
There's a possibility, if we screwed things up, that we can now create
instances on the free store follwing valid construction.  Check that it still
fails.
*/

    CPPUNIT_ASSERT_THROW (new SingletonTest (),
    Facsimile::X::SingletonException);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Singleton::testMultiThreadConstruction () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Singleton::testMultiThreadConstruction () throw
(CppUnit::Exception)
{

/*
Firstly, some sanity checks.
*/

    assert (multiSingleton == 0);
    assert (numFailures == 0);

/*
Now create separate threads of execution to put singleton construction under
some stress.

DO NOT ASSUME THAT A SUCCESSFUL TEST MEANS THE CODE IS GOOD!  Results will be
influenced by many apparently random issues (number and states of processes
running, number and states of processors/cores, compiler used, etc.).  Failures
are almost certainly indicative of a fault, however.  The point is, we can
prove that it doesn't work (if we ever see a failure), but we can't prove that
it does work (never having seen a failure does not mean it works). 
*/

    boost::thread thread1 (threadExecute);
    boost::thread thread2 (threadExecute);
    boost::thread thread3 (threadExecute);
    boost::thread thread4 (threadExecute);
    boost::thread thread5 (threadExecute);
    boost::thread thread6 (threadExecute);
    boost::thread thread7 (threadExecute);
    boost::thread thread8 (threadExecute);
    boost::thread thread9 (threadExecute);
    boost::thread thread10 (threadExecute);

/*
Now wait for all of these threads to complete.
*/

    thread1.join ();
    thread2.join ();
    thread3.join ();
    thread4.join ();
    thread5.join ();
    thread6.join ();
    thread7.join ();
    thread8.join ();
    thread9.join ();
    thread10.join ();

/*
OK.  What do we have?
*/

    CPPUNIT_ASSERT (multiSingleton != 0);
    CPPUNIT_ASSERT (numFailures == 0);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Singleton::threadExecute () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Singleton::threadExecute () throw ()
{

/*
Retrieve the sole singleton pointer.  Only one of the threads executing this
function will actually create the singleton, the others should be constrained
from accessing the pointer until it is created.
*/

    SingletonTest2* mySingleton = &(SingletonTest2::getInstance ());

/*
Now lock the shared mutex.  We need to do this to record any failures
accurately and to examine the stored pointer.

This lock will be free automatically when the routine exits.

Note: We do not obtain the lock before the previous code otherwise we
effectively queue up the threads to access the pointer - which almost certainly
will not put that code under much stress.
*/

    boost::mutex::scoped_lock lock (staticMutex);

/*
OK.  Only a single thread can be executing this code at any given time.  If the
stored pointer is zero, then store our copy of the pointer.
*/

    if (multiSingleton == 0)
    {
        multiSingleton = mySingleton;
    }

/*
Otherwise, compare our singleton's pointer to the one stored.  They should be
the same; if not, increment the number of failures.
*/

    else if (multiSingleton != mySingleton)
    {
        ++numFailures;
    }
}
