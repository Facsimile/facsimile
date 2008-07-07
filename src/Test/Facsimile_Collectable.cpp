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
Facsimile::Collectable class test suite C++ source file.

C++ source file for the Facsimile::Collectable class test suite.
*/
//=============================================================================

/*
Relevant header files.
*/

#include <Facsimile/X/ArrayNewNotSupportedException.hpp>
#include <Facsimile/X/ArrayDeleteNotSupportedException.hpp>
#include "Facsimile_Collectable.hpp"

/*
Do not register this test suite.  We need to manually add the test to the suite
(in main ()) so that these tests are guaranteed to execute first.
*/

//CPPUNIT_TEST_SUITE_REGISTRATION (Test_Facsimile_Collectable);

/*
Initialise the heap size index.
*/

int Heap::heapSizeIndex = 0;

/*
Initialise the array of heap sizes.
*/

GC_word Heap::heapSize [5] =
{
    Heap::FIRST_HEAP_SIZE,
    Heap::SECOND_HEAP_SIZE,
    Heap::THIRD_HEAP_SIZE,
    Heap::FOURTH_HEAP_SIZE,
    Heap::FIFTH_HEAP_SIZE,
};

/*
Initialise the new handler seen flag to false.
*/

bool NewHandler::newHandlerSeen = false;

/*
Initialise the MyMemoryBase instance count.
*/

Count MyMemoryBase::instanceCount = 0;

/*
Initialise the MyMemoryBase mutex.
*/

boost::recursive_mutex MyMemoryBase::staticMutex;

/*
Initialize the collectable object finalization count.
*/

Count MyCollectable::finalizationCount = 0;

/*
Initialise the MyCollectable mutex.
*/

boost::recursive_mutex MyCollectable::staticMutex;


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NewHandler::~NewHandler () implementation.
*/ 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

NewHandler::NewHandler (Type type) throw ():
    originalHandler (0)
{

/*
Reset the new handler seen flag.
*/

    newHandlerSeen = false;

/*
Install the appropriate new handler, storing the previous handler.
*/

    originalHandler = std::set_new_handler (type == newHandlerThrow?
    throwingHandler: nonThrowingHandler);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NewHandler::throwingHandler () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void NewHandler::throwingHandler () throw (std::bad_alloc)
{

/*
Set the flag to indicate that we saw a new handler.
*/

    assert (!newHandlerSeen);
    newHandlerSeen = true;

/*
Just throw the exeception.  We don't have no more stinkin' memory.
*/

    throw std::bad_alloc ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NewHandler::nonThrowingHandler () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void NewHandler::nonThrowingHandler () throw (std::bad_alloc)
{

/*
Set the flag to indicate that we saw a new handler.
*/

    assert (!newHandlerSeen);
    newHandlerSeen = true;

/*
Double the current heap size.  If this doesn't work, and the next memory
allocation fails, then this function will be called once more (so the assertion
above should fail and kick us out).
*/

    Heap::updateHeapSize ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyCollectable::~MyCollectable () implementation.
*/ 
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

MyCollectable::~MyCollectable () throw ()
{

/*
Increment the finalization count.
*/

    boost::recursive_mutex::scoped_lock lock (staticMutex);
    ++finalizationCount;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testGlobalMemoryExhaustion () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testGlobalMemoryExhaustion () throw
(CppUnit::Exception)
{

/*
Create an object that will temporarily reduce the size of the heap to allow us
to test exhaustion conditions.  The size of the heap will be restored to its
former glory when this instance goes out of scope.
*/

    Heap reducedHeap;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 0);

/*
Pointer to assist with memory allocation.
*/

    Heap::FirstUncollectableObject* largeObject = 0;

/*
Create an object larger than the size of the heap, using the throwing version
of new, and verify that the pointer remains zero and that the correct exception
was thrown.  Delete the object just in case (if the pointer is zero, nothing
will happen).
*/

    CPPUNIT_ASSERT_THROW (largeObject = new Heap::FirstUncollectableObject (),
    std::bad_alloc);
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
Now do the same for the non-throwing version (this should just return 0 and not
throw an exception).
*/

    CPPUNIT_ASSERT_NO_THROW (largeObject = new (std::nothrow)
    Heap::FirstUncollectableObject ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
Now do the same for the throwing version of global array new.
*/

    CPPUNIT_ASSERT_THROW (largeObject = new Heap::FirstUncollectableObject [3],
    std::bad_alloc);
    CPPUNIT_ASSERT (largeObject == 0);
    delete [] largeObject;

/*
Now do the same for the non-throwing version of global array new.
*/

    CPPUNIT_ASSERT_NO_THROW (largeObject = new (std::nothrow)
    Heap::FirstUncollectableObject [3]);
    CPPUNIT_ASSERT (largeObject == 0);
    delete [] largeObject;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testSubClassMemoryExhaustion () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testSubClassMemoryExhaustion () throw
(CppUnit::Exception)
{

/*
Create an object that will temporarily reduce the size of the heap to allow us
to test exhaustion conditions.  The size of the heap will be restored to its
former glory when this instance goes out of scope.
*/

    Heap reducedHeap;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 0);

/*
Pointer to assist with memory allocation.
*/

    Heap::FirstCollectableObject* largeObject = 0;

/*
Create an object larger than the size of the heap, using the throwing version
of new, and verify that the pointer remains zero and that the correct exception
was thrown.  Delete the object just in case (if the pointer is zero, nothing
will happen).
*/

    CPPUNIT_ASSERT_THROW (largeObject = new Heap::FirstCollectableObject (),
    std::bad_alloc);
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
Now do the same for the non-throwing version (this should just return 0 and not
throw an exception).
*/

    CPPUNIT_ASSERT_NO_THROW (largeObject = new (std::nothrow)
    Heap::FirstCollectableObject ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
We do not need to test array new versions, because they will only fail anyway.
*/

}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testGlobalThrowingNewHandler () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testGlobalThrowingNewHandler () throw
(CppUnit::Exception)
{

/*
Create an object that will temporarily reduce the size of the heap to allow us
to test exhaustion conditions.  The size of the heap will be restored to its
former glory when this instance goes out of scope.
*/

    Heap reducedHeap;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 0);

/*
OK.  Now we'll establish a new handler, that does not provide any extra memory,
but that throws a std::bad_alloc exception when called.  This handler will be
removed, and the original handler restored, when this instance goes out of
scope.
*/

    NewHandler handler (NewHandler::newHandlerThrow);

/*
Pointer to assist with memory allocation.
*/

    Heap::FirstUncollectableObject* largeObject = 0;

/*
OK.  Now allocate a large object, that should fail - causing our new handler to
be called, which in turn throws the std::bad_alloc exception.
*/

    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_THROW (largeObject = new Heap::FirstUncollectableObject (),
    std::bad_alloc);
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
OK.  Repeat using the non-throwing version of new.  Should get equivalent
response.
*/

    NewHandler::resetFlag ();
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_NO_THROW (largeObject = new (std::nothrow)
    Heap::FirstUncollectableObject ());
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
OK.  Now allocate an array of large objects, that should fail - causing our new
handler to be called, which in turn throws the std::bad_alloc exception.
*/

    NewHandler::resetFlag ();
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_THROW (largeObject = new Heap::FirstUncollectableObject [3],
    std::bad_alloc);
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete [] largeObject;

/*
OK.  Repeat using the non-throwing version of new.  Should get equivalent
response.
*/

    NewHandler::resetFlag ();
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_NO_THROW (largeObject = new (std::nothrow)
    Heap::FirstUncollectableObject [3]);
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete [] largeObject;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testSubClassThrowingNewHandler () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testSubClassThrowingNewHandler () throw
(CppUnit::Exception)
{

/*
Create an object that will temporarily reduce the size of the heap to allow us
to test exhaustion conditions.  The size of the heap will be restored to its
former glory when this instance goes out of scope.
*/

    Heap reducedHeap;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 0);

/*
OK.  Now we'll establish a new handler, that does not provide any extra memory,
but that throws a std::bad_alloc exception when called.  This handler will be
removed, and the original handler restored, when this instance goes out of
scope.
*/

    NewHandler handler (NewHandler::newHandlerThrow);

/*
Pointer to assist with memory allocation.
*/

    Heap::FirstCollectableObject* largeObject = 0;

/*
OK.  Now allocate a large object, that should fail - causing our new handler to
be called, which in turn throws the std::bad_alloc exception.
*/

    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_THROW (largeObject = new Heap::FirstCollectableObject (),
    std::bad_alloc);
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
OK.  Repeat using the non-throwing version of new.  Should get equivalent
response.
*/

    NewHandler::resetFlag ();
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_NO_THROW (largeObject = new (std::nothrow)
    Heap::FirstCollectableObject ());
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject == 0);
    delete largeObject;

/*
We do not need to test array new versions, because they will only fail anyway.
*/

}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testGlobalNonThrowingNewHandler () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testGlobalNonThrowingNewHandler () throw
(CppUnit::Exception)
{

/*
Create an object that will temporarily reduce the size of the heap to allow us
to test exhaustion conditions.  The size of the heap will be restored to its
former glory when this instance goes out of scope.
*/

    Heap reducedHeap;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 0);

/*
OK.  Now we'll establish a new handler, that provides extra memory by
increasing the size of the heap on each occasion; it does not throw a
std::bad_alloc exception when called.  This handler will be removed, and the
original handler restored, when this instance goes out of scope.
*/

    NewHandler handler (NewHandler::newHandlerNoThrow);

/*
OK.  Now allocate a large object, that should fail - causing our new handler to
be called, which in turn frees sufficient memory for the allocation to succeed.

Since this succeeds, we need to delete the object and bear in mind the fact
that the heap ends up twice as big as before.
*/

    Heap::FirstUncollectableObject* largeObject = 0;
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_NO_THROW (largeObject = new Heap::FirstUncollectableObject
    ());
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject);
    delete largeObject;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 1);

/*
OK.  Repeat using the non-throwing version of new.  Should get equivalent
response.  We need to use a larger object this time around, though.
*/

    NewHandler::resetFlag ();
    Heap::SecondUncollectableObject* anotherLargeObject = 0;
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_NO_THROW (anotherLargeObject = new (std::nothrow)
    Heap::SecondUncollectableObject ());
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (anotherLargeObject);
    delete anotherLargeObject;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 2);

/*
TODO: Currently not testing for new handlers with array new - heap would get
too big!
*/

}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testSubClassNonThrowingNewHandler ()
implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testSubClassNonThrowingNewHandler () throw
(CppUnit::Exception)
{

/*
Create an object that will temporarily reduce the size of the heap to allow us
to test exhaustion conditions.  The size of the heap will be restored to its
former glory when this instance goes out of scope.

Note the heap is current quadruple the initial size.
*/

    Heap reducedHeap;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 2);

/*
OK.  Now we'll establish a new handler, that provides extra memory by
increasing the size of the heap on each occasion; it does not throw a
std::bad_alloc exception when called.  This handler will be removed, and the
original handler restored, when this instance goes out of scope.
*/

    NewHandler handler (NewHandler::newHandlerNoThrow);

/*
OK.  Now allocate a large object, that should fail - causing our new handler to
be called, which in turn frees sufficient memory for the allocation to succeed.

Since this succeeds, we need to delete the object and bear in mind the fact
that the heap ends up twice as big as before.
*/

    Heap::ThirdCollectableObject* largeObject = 0;
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_NO_THROW (largeObject = new Heap::ThirdCollectableObject
    ());
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (largeObject);
    delete largeObject;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 3);

/*
OK.  Repeat using the non-throwing version of new.  Should get equivalent
response.  We need to use a larger object this time around, though.
*/

    NewHandler::resetFlag ();
    Heap::FourthCollectableObject* anotherLargeObject = 0;
    CPPUNIT_ASSERT (!NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT_NO_THROW (anotherLargeObject = new (std::nothrow)
    Heap::FourthCollectableObject ());
    CPPUNIT_ASSERT (NewHandler::wasNewHandlerSeen ());
    CPPUNIT_ASSERT (anotherLargeObject);
    delete anotherLargeObject;
    CPPUNIT_ASSERT (Heap::getHeapSizeIndex () == 4);

/*
We do not need to test array new versions, because they will only fail anyway.
*/

}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testGlobalNewDelete () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testGlobalNewDelete () throw
(CppUnit::Exception)
{

/*
Create a regular integer on the stack.  This is neither on the heap, nor is it
collectable.  Check that we're OK with it.
*/

    {
        int localInt = 2;
        checkIntPointer (&localInt, false, 2);
    }

/*
Create an uncollectable integer on the collector's heap using the throwing
version of the new and delete operators.  Check that it is OK.
*/

    int* throwIntPtr = new int ();
    *throwIntPtr = 3;
    checkIntPointer (throwIntPtr, true, 3);
    delete throwIntPtr;

/*
Create an uncollectable integer on the collector's heap using the non-throwing
version of the new and delete operators.  Check that it is OK.
*/

    int* noThrowIntPtr = new (std::nothrow) int ();
    *noThrowIntPtr = 5;
    checkIntPointer (noThrowIntPtr, true, 5);
    delete throwIntPtr;

/*
Create an array of integers on the stack.  This is neither on the heap, nor is
it collectable.  Check that we're OK with it.
*/

    {
        int localArray [] =
        {
            7, 7, 7,
        };
        for (int i = 0; i < 3; ++i)
        {
            checkIntPointer (&localArray [i], false, 7);
        }
    }

/*
Create an array of uncollectable integers on the collector's heap, using the
throwing version of the new and delete operators.  Check that each is OK.
*/

    int* throwIntArray = new int [3];
    for (int i = 0; i < 3; ++i)
    {
        throwIntArray [i] = 11;
        checkIntPointer (&throwIntArray [i], true, 11);
    }
    delete [] throwIntArray;

/*
Create an array of uncollectable integers on the collector's heap, using the
non-throwing version of the new and delete operators.  Check that each is OK.
*/

    int* noThrowIntArray = new (std::nothrow) int [3];
    for (int i = 0; i < 3; ++i)
    {
        noThrowIntArray [i] = 13;
        checkIntPointer (&noThrowIntArray [i], true, 13);
    }
    delete [] noThrowIntArray;

/*
Create a single uncollectable MyUncollectable class instance on the stack.
This is neither on the heap, nor is it collectable.  Check that we're OK with
it.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    {
        MyUncollectable localUncollectable (17);
        checkMyUncollectablePointer (&localUncollectable, false, 17); 
        CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 1);
    }

/*
Create an uncollectable MyUncollectable instance on the collector's heap using
the throwing version of the new and delete operators.  Check that it is OK.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    MyUncollectable* throwMyUncollectablePtr = new MyUncollectable (19);
    checkMyUncollectablePointer (throwMyUncollectablePtr, true, 19);
    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 1);
    delete throwMyUncollectablePtr;

/*
Create an uncollectable MyUncollectable instance on the collector's heap using
the non-throwing version of the new and delete operators.  Check that it is OK.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    MyUncollectable* noThrowMyUncollectablePtr = new (std::nothrow)
    MyUncollectable (23);
    checkMyUncollectablePointer (noThrowMyUncollectablePtr, true, 23);
    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 1);
    delete noThrowMyUncollectablePtr;

/*
Create an array of uncollectable MyCollectable instances on the stack.  This is
neither on the heap, nor is it collectable.  Check that we're OK with it.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    {
        MyUncollectable localMyUncollectableArray [3] =
        {
            MyUncollectable (29),
            MyUncollectable (29),
            MyUncollectable (29),
        };
        for (int i = 0; i < 3; ++i)
        {
            checkMyUncollectablePointer (&localMyUncollectableArray [i], false,
            29);
        }
        CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 3);
    }

/*
Create an array of uncollectable MyCollectable instances on the collector's
heap, using the throwing version of the new and delete operators.  Check that
each is OK.

Note: The default constructor assigns a value of 31.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    MyUncollectable* throwMyUncollectableArray = new MyUncollectable [3];
    for (int i = 0; i < 3; ++i)
    {
        checkMyUncollectablePointer (&throwMyUncollectableArray [i], true,
        31);
    }
    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 3);
    delete [] throwMyUncollectableArray;

/*
Create an array of uncollectable MyCollectable instances on the collector's
heap, using the non-throwing version of the new and delete operators.  Check
that each is OK.

Note: The default constructor assigns a value of 31.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    MyUncollectable* noThrowMyUncollectableArray = new (std::nothrow)
    MyUncollectable [3];
    for (int i = 0; i < 3; ++i)
    {
        checkMyUncollectablePointer (&noThrowMyUncollectableArray [i], true,
        31);
    }
    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 3);
    delete [] noThrowMyUncollectableArray;

/*
Ensure that we have no more instances out there.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::testSubClassNewDelete () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::testSubClassNewDelete () throw
(CppUnit::Exception)
{

/*
Create an (uncollectable) MyCollectable instance on the stack.  This is neither
on the heap, nor is it collectable.  Check that we're OK with it.
*/

    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    CPPUNIT_ASSERT (MyCollectable::getFinalizationCount () == 0);
    {
        MyCollectable localMyCollectable (2);
        checkMyCollectablePointer (&localMyCollectable, false, 2);
        CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 1);
    }
    CPPUNIT_ASSERT (MyCollectable::getFinalizationCount () == 1);
    Count lastCount = MyCollectable::getFinalizationCount ();

/*
Now create collectable MyCollectable instances on the collector's heap using
the throwing version of sub-class new.  Check that we collected something at
the end of all that.

NOTE: We cannot guarantee that any collections will be made.  Let's hope!
*/

    const Count INSTANCE_COUNT = 20;
    const Count RETRY_COUNT = 1;
    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == 0);
    for (Count i = 0; i < INSTANCE_COUNT; ++i)
    {
        CPPUNIT_ASSERT_NO_THROW (checkMyCollectablePointer (new MyCollectable
        (3), true, 3));
    }
    CPPUNIT_ASSERT (garbageCollect (lastCount + 1, RETRY_COUNT) > 0);
    lastCount = MyCollectable::getFinalizationCount ();

/*
Now create collectable MyCollectable instances on the collector's heap using
the non-throwing version of sub-class new.  Check that we collected something
at the end of all that.
*/

    for (Count i = 0; i < INSTANCE_COUNT; ++i)
    {
        CPPUNIT_ASSERT_NO_THROW (checkMyCollectablePointer (new (std::nothrow)
        MyCollectable (5), true, 5));
    }
    CPPUNIT_ASSERT (garbageCollect (lastCount + 1, RETRY_COUNT) > 0);

/*
Check that attempting to create an array using the throwing version of
sub-class array new yields an ArrayNewNotSupportedException.
*/

// Cannot test - exception causes test to terminate.
//    Count instanceCount = MyMemoryBase::getInstanceCount ();
//    CPPUNIT_ASSERT_THROW (new MyCollectable [3],
//    Facsimile::X::ArrayNewNotSupportedException);
//    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == instanceCount);

/*
Check that attempting to free an array using the throwing version of sub-class
array delete yields an ArrayDeleteNotSupportedException.

Note: We'll call the function explicitly to avoid any confusion.
*/

// Cannot test - exception causes test to terminate.
//    MyCollectable* nullArray = 0;
//    CPPUNIT_ASSERT_THROW (MyCollectable::operator delete [] (nullArray),
//    Facsimile::X::ArrayDeleteNotSupportedException);
//    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == instanceCount);


/*
Check that attempting to create an array using the non-throwing version of
sub-class array new yields an ArrayNewNotSupportedException.
*/

// Cannot test - exception causes test to terminate.
//    CPPUNIT_ASSERT_THROW (new (std::nothrow) MyCollectable [3],
//    Facsimile::X::ArrayNewNotSupportedException);
//    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == instanceCount);

/*
Check that attempting to free an array using the throwing version of sub-class
array delete yields an ArrayDeleteNotSupportedException.

Note: We'll call the function explicitly to avoid any confusion.
*/

// Cannot test - exception causes test to terminate.
//    CPPUNIT_ASSERT_THROW (MyCollectable::operator delete [] (nullArray,
//    std::nothrow), Facsimile::X::ArrayDeleteNotSupportedException);
//    CPPUNIT_ASSERT (MyMemoryBase::getInstanceCount () == instanceCount);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::checkIntPointer (int*, bool, int) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::checkIntPointer (int* intPtr, bool isOnHeap,
int intValue) throw (CppUnit::Exception)
{

/*
Check that we have a non-null pointer.
*/

    CPPUNIT_ASSERT (intPtr);

/*
Check whether it was created on the collector's heap or not.  If GC_base ()
returns 0, then this integer value was not created on our heap.
*/

    GC_PTR base = GC_base (static_cast <GC_PTR> (intPtr));
    CPPUNIT_ASSERT (isOnHeap == (base != 0));

/*
Check that this value is uncollectable.  We only need do this if the value was
created on the collector's heap.
*/

    if (isOnHeap)
    {
        // TODO: Currently no way to test this.  :-(
    }

/*
Check that our integer has the correct value.
*/

    CPPUNIT_ASSERT (*intPtr == intValue);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::checkMyUncollectablePointer (int*, bool, int)
implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::checkMyUncollectablePointer (MyUncollectable*
myUncollectablePtr, bool isOnHeap, int intValue) throw (CppUnit::Exception)
{

/*
Check that we have a non-null pointer.
*/

    CPPUNIT_ASSERT (myUncollectablePtr);

/*
Check whether it was created on the collector's heap or not.  If GC_base ()
returns 0, then this instance was not created on our heap.
*/

    GC_PTR base = GC_base (static_cast <GC_PTR> (myUncollectablePtr));
    CPPUNIT_ASSERT (isOnHeap == (base != 0));

/*
Check that this value is uncollectable.  We only need do this if the value was
created on the collector's heap.
*/

    if (isOnHeap)
    {
        // TODO: Currently no way to test this.  :-(
    }

/*
Check that the instance has stored the correct value.
*/

    CPPUNIT_ASSERT (myUncollectablePtr->getValue () == intValue);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::checkMyCollectablePointer (int*, bool, int)
implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Test_Facsimile_Collectable::checkMyCollectablePointer (MyCollectable*
myCollectablePtr, bool isOnHeap, int intValue) throw (CppUnit::Exception)
{

/*
Check that we have a non-null pointer.
*/

    CPPUNIT_ASSERT (myCollectablePtr);

/*
Check whether it was created on the collector's heap or not.  If GC_base ()
returns 0, then this instance was not created on our heap.
*/

    GC_PTR base = GC_base (static_cast <GC_PTR> (myCollectablePtr));
    CPPUNIT_ASSERT (isOnHeap == (base != 0));

/*
Check whether this value is collectable or not.  If it's on the heap, then it
should be collectable...
*/

    if (isOnHeap)
    {
        // TODO: Currently no way to test this.  :-(
    }

/*
Check that the instance has stored the correct value.
*/

    CPPUNIT_ASSERT (myCollectablePtr->getValue () == intValue);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Test_Facsimile_Collectable::garbageCollect (Count, Count) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Count Test_Facsimile_Collectable::garbageCollect (Count targetCount, Count
retryLimit) throw ()
{

/*
Keep looping until we've tried too many times.
*/

    assert (retryLimit > 0);
    Count tally = 0;
    while (++tally <= retryLimit)
    {

/*
Force a garbage collection.
*/

        GC_gcollect ();

/*
If the finalization count has been met or exceeded, stop.
*/

        if (MyCollectable::getFinalizationCount () >= targetCount)
        {
            assert (tally > 0);
            return tally;
        }
    }

/*
OK.  We failed.
*/

    return 0;
}
