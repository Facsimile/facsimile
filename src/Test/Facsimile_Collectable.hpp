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
Facsimile::Collectable class test suite C++ header file.

C++ header file for the Facsimile::Collectable class test suite.  This file
also includes tests for the replacement global
<code><strong>new</strong></code> and <code><strong>delete</strong></code>
operators.
*/
//=============================================================================

#ifndef TEST_FACSIMILE_COLLECTABLE_HPP_
#define TEST_FACSIMILE_COLLECTABLE_HPP_

/*
Relevant header files.
*/

#include <boost/thread/recursive_mutex.hpp>
#include <cppunit/TestAssert.h>
#include <cppunit/extensions/HelperMacros.h>
#include <Facsimile/Collectable.hpp>

/**
Count type.
*/

typedef unsigned long Count;

//=============================================================================
/**
Heap class.

Controls the size of the heap available during memory exhaustion testing.

The heap is initially set at FIRST_HEAP_SIZE (which must be done from the
main() routine before any tests are attempted.  This heap size can be used for
the majority of memory exhaustion testing.  However, once we start to test new
handlers that increase the size of the heap, resulting in successful allocation
of objects larger than FIRST_HEAP_SIZE, then we cannot shrink the heap back to
FIRST_HEAP_SIZE - even once these objects have been freed.  Consequently, we
must keep doubling both the size of the heap, and the size of the objects that
we're trying to allocate, in order to keep testing the operation of such
handlers.

The constructor sets the heap to the last recorded size, and the destructor
ensures that the heap is restored to unlimited size when the instance goes out
of scope.  This ensures that any test failures do not affect subsequent tests.

\note Allocating memory following execution of the destructor can fix the size
of the heap above the currently expected limit, causing tests that rely on
insufficient memory for allocations to fail.  For this reason and others, the
memory exhaustion tests <strong>must</strong> be performed before all others.
*/
//=============================================================================

class Heap
{

/**
Initial heap size.

This must be large enough to let the test environment run tests.
*/

private:
    static const GC_word FIRST_HEAP_SIZE = 1024 * 1024;

/*
Second heap size.

The heap is set to this size when NewHandler::nonThrowingHandler() is called
for the first time.  It cannot be reduced subsequently.
*/

private:
    static const GC_word SECOND_HEAP_SIZE = FIRST_HEAP_SIZE * 3;

/*
Third heap size.

The heap is set to this size when NewHandler::nonThrowingHandler() is called
for the second time.  It cannot be reduced subsequently.
*/

private:
    static const GC_word THIRD_HEAP_SIZE = SECOND_HEAP_SIZE * 3;

/*
Fourth heap size.

The heap is set to this size when NewHandler::nonThrowingHandler() is called
for the third time.  It cannot be reduced subsequently.
*/

private:
    static const GC_word FOURTH_HEAP_SIZE = THIRD_HEAP_SIZE * 3;

/*
Fifth heap size.

The heap is set to this size when NewHandler::nonThrowingHandler() is called
for the fourth time.  It cannot be reduced subsequently.

\note The value 0 signifies that the heap is now unconstrained (or back to
normal).
*/

private:
    static const GC_word FIFTH_HEAP_SIZE = 0;

/**
First uncollectable object type.

Creating instances of this class should encounter problems while the heap is
still at the first heap size.

\remarks This object should be used for all memory exhaustion testing of
uncollectable objects, all throwing new handler uncollectable objectvtests, and
the first non-throwing new handler uncollectable object test handler.
*/

public:
    struct FirstUncollectableObject
    {
        char largeArray [FIRST_HEAP_SIZE];
    };

/**
Second uncollectable object type.

Creating instances of this class should encounter problems while the heap is at
the second heap size.

\remarks This object should be used for the second non-throwing new handler
uncollectable object test.
*/

public:
    struct SecondUncollectableObject
    {
        char largeArray [SECOND_HEAP_SIZE];
    };

/**
First collectable object type.

Creating instances of this class should encounter problems while the heap is
still at the first heap size.

\remarks This object should be used for all memory exhaustion testing of
collectable objects and all throwing new handler collectable object tests only.
It should not be used for any non-throwing new handler collectable object
tests.
*/

public:
    struct FirstCollectableObject:
        public virtual Facsimile::Collectable
    {
        char largeArray [FIRST_HEAP_SIZE];
    };

/**
Third collectable object type.

Creating instances of this class should encounter problems while the heap is at
the third heap size.

\remarks This object should be used for the first non-throwing new handler
collectable object test.
*/

public:
    struct ThirdCollectableObject:
        public virtual Facsimile::Collectable
    {
        char largeArray [THIRD_HEAP_SIZE];
    };

/**
Fourth collectable object type.

Creating instances of this class should encounter problems while the heap is at
the fourth heap size.

\remarks This object should be used for the second non-throwing new handler
collectable object test.
*/

public:
    struct FourthCollectableObject:
        public virtual Facsimile::Collectable
    {
        char largeArray [FOURTH_HEAP_SIZE];
    };

/**
Index of current heap size.
*/

private:
    static int heapSizeIndex;

/**
Array storing the heap sizes so that they can be looked up via the index.
*/

private:
    static GC_word heapSize [5];

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Get the heap size index.

\return The current heap size index.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static int getHeapSizeIndex () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Set the heap size.

Sets the heap size to the current limit.

\note The <code>GC_set_max_heap_size ()</code> function does not report success
or failure, so we just have to trust that it has worked.  This function should
never be called by a simulation model.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static void setHeapSize () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Initialise the heap characteristics.

\note This must only be called at the start of the main() routine.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static void initialize () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Update the heap size.

Updates the heap size to the next limit.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static void updateHeapSize () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

Change the size of the collector heap to the current limit.  The destructor
restores the heap to its maximum size when the instance goes out of scope.

\bug This constructor seems never to get called!
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    Heap () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Destructor.

Restore the collector heap's size to the available memory.

\note The <code>GC_set_max_heap_size ()</code> function does not report success
or failure, so we just have to trust that it has worked.  This function should
never be called by a simulation model.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    ~Heap () throw ();
};

//=============================================================================
/**
NewHandler class.

The constructor for this class establishes a new handler function to be called
whenever a <code><strong>new</strong></code> operator is unable to find
sufficient contiguous memory.  The destructor restores the original new handler
in true "resource acquisition is initialization" fashion.
*/
//=============================================================================

class NewHandler
{

/**
Eumeration that indicates which new handler is required: the throwing version
(that does not reclaim any additional memory and throws a std::bad_alloc
exception) or the non-throwing version (that increases the heap size in order
to find sufficient memory to satisy a request).
*/

public:
    enum Type
    {
        newHandlerThrow,
        newHandlerNoThrow,
    };

/**
Original new handler.

Storage for pointer to the original new handler that was in effect prior to an
instance of the class being created.  This value is restored when an instance
goes out of scope.
*/

    std::new_handler originalHandler;

/**
New handler seen flag.

This flag is set to true if a new handler was seen.  If initialized to false,
it should remain false if a new handler was not called.

This flag is used to test that a new handler was called when memory was
exhausted.

\note This must be a static member so that it can be accessed from the new
handler.
*/

private:
    static bool newHandlerSeen;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Constructor.

Install the appropriate new handler and reset the handler seen flag.

\param type The type of handler required: throwing or non-throwing...
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    NewHandler (Type type = newHandlerThrow) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Destructor.

Re-establish the original new handler as the current new handler.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    ~NewHandler () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Reset new handler seen flag.

Reset the new handler seen flags to false.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static void resetFlag () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Report new handler seen flag.

\return True if a new handler was called, false otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static bool wasNewHandlerSeen () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
New handler that restores available memory.

This function is used when testing memory exhaustion behavior.  It doubles the
current size of the collector heap, such that a subsequent attempt to allocate
dynamic memory within a <code><strong>new</strong></code> operator succeeds.

\exception std::bad_alloc Thrown if could not find any additional memory.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

private:
    static void nonThrowingHandler () throw (std::bad_alloc);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
New handler that throws a std::bad_alloc exception when called.

This function is used when testing memory exhaustion behavior.  It throws the
std::bad_alloc exception, such that the current attempt to allocate dynamic
memory within a <code><strong>new</strong></code> operator fails.

\exception std::bad_alloc Thrown if could not find any additional memory.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

private:
    static void throwingHandler () throw (std::bad_alloc);
};

//=============================================================================
/**
Base class.

This is used by other test classes to store an integer value that can be used
to differentiate between different class instances.

\remarks On it's own, this will be uncollectable.  If used with a collectable
class, then it is collectable.
*/
//=============================================================================

class MyMemoryBase
{

/**
Active instance count.

This count is incremented when an object is constructed and decremented when an
object is destructed.

\note Since finalization (as opposed to destruction) most likely occurs on a
different thread of execution, locks must be obtained before modifications can
be made.  Locks are made using the static staticMutex member.
*/

private:
    static Count instanceCount;

/**
Mutual exclusion (mutex) object.

This object is used for thread synchronization purposes.

\note A recursive mutex object is used so that a thread does not lock if it
attempts to claim the same mutex object more than once.
*/

private:
    static boost::recursive_mutex staticMutex;

/**
Miscellaneous integer value.

We'll use this value to distinguish between different instances of this class.
*/

private:
    const int someValue;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Constructor.

Store a miscellaneous value for later use.

\param value An integer value to be stored in this instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    MyMemoryBase (int value) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    virtual ~MyMemoryBase () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Report the instance count.

\return The number of active instances of this class.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static Count getInstanceCount () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Report the value stored by this instance.

\return The same integer value that was stored by the class.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    int getValue () const throw ();
};

//=============================================================================
/**
collectable class.

This class is derived from Facsimile::Collectable, so it is collectable and
instances will be freed automatically when the collector deems this class
inaccessible.

It is used to test that %Facsimile's collector
<code><strong>new</strong></code> and <code><strong>delete</strong><code>
operators are employed correctly for Facsimile::Collectable sub-class
instances.
*/
//=============================================================================

class MyCollectable:
    public virtual Facsimile::Collectable, public virtual MyMemoryBase
{

/**
Destructor seen count.

This count is incremented when a collectable object's destructor is called.  If
initialized to 0, a positive value indicates that objects have been collected.

This count is used to test that collectable objects are correctly finalized.

\note Since finalization (as opposed to destruction) most likely occurs on a
different thread of execution, locks must be obtained before modifications can
be made.  Locks are made using the static staticMutex member.
*/

private:
    static Count finalizationCount;

/**
Mutual exclusion (mutex) object.

This object is used for thread synchronization purposes.

\note A recursive mutex object is used so that a thread does not lock if it
attempts to claim the same mutex object more than once.
*/

private:
    static boost::recursive_mutex staticMutex;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

Store a miscellaneous value for later use.

\param value An integer value to be stored in this instance.  If not specified,
this value will default to 7.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    MyCollectable (int value = 7) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.

This destructor should be called whenever a class instance is collected,
whether manually (via the <code><strong>delete</strong></code> operator) or
automatically (by the collector).
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    virtual ~MyCollectable () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Report the finalization count.

\return The number of instances of this class that have been collected (whether
manually or automatically).
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    static Count getFinalizationCount () throw ();
};

//=============================================================================
/**
Uncollectable class.

As this class is not derived from Facsimile::Collectable, it is not collectable
and instances must be freed manually.

It is used to test that %Facsimile's global <code><strong>new</strong></code>
and <code><strong>delete</strong><code> operators are employed correctly for
class instances.
*/
//=============================================================================

class MyUncollectable:
    public virtual MyMemoryBase
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default onstructor.

Store a miscellaneous value for later use.

\param value An integer value to be stored in this instance.  If not specified,
this value will default to 31.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    MyUncollectable (int value = 31) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    virtual ~MyUncollectable () throw ();
};

//=============================================================================
/**
Uncollectable class with pointer to collectable object.

As this class is not derived from Facsimile::Collectable, it is not collectable
and instances must be freed manually.  However, the class contains pointers to
collectable objects.

This class is used to test that collectable objects, that are only referenced
from within uncollectable objects, are not collected.  (If global
code><strong>new</strong></code> and <code><strong>delete</strong><code>
operators other than our own are in use, then it may be that the collector
cannot scan memory belonging to uncollectable objects and will therefore deem
the referenced collectable objects inaccessible and collect them -
incorrectly.)
*/
//=============================================================================

class MyUncollectableWithCollectablePtr:
    public virtual MyMemoryBase
{

/**
Pointer to an collectable object.

The collectable object must not be collected as long as the instance
referencing it is active.  However, it should be available for collection once
this instance is freed.

\note Do not manually free this object in the destructor!
*/

private:
    MyCollectable* someCollectableObject;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Constructor.

Store a reference to the collectable object.

\param value Some integer value to be stored.

\param collectableObject A pointer to the collectable object to be stored.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
     MyUncollectableWithCollectablePtr (int value, MyCollectable*
     collectableObject) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    virtual ~MyUncollectableWithCollectablePtr () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Report stored pointer.

\return A pointer to the collectable object.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    MyCollectable* getPointer () const throw ();
};

//=============================================================================
/**
Test fixture for the Facsimile::Collectable class.
*/
//=============================================================================

class Test_Facsimile_Collectable:
    public CppUnit::TestFixture
{

/*
Boilerplate code to declare the test suite.  New tests must be included in the
list.

DO NOT CHANGE THE ORDER OF THESE TESTS!
*/

    CPPUNIT_TEST_SUITE (Test_Facsimile_Collectable);
    CPPUNIT_TEST (testGlobalMemoryExhaustion);
    CPPUNIT_TEST (testSubClassMemoryExhaustion);
    CPPUNIT_TEST (testGlobalThrowingNewHandler);
    CPPUNIT_TEST (testSubClassThrowingNewHandler);
    CPPUNIT_TEST (testGlobalNonThrowingNewHandler);
    CPPUNIT_TEST (testSubClassNonThrowingNewHandler);
    CPPUNIT_TEST (testGlobalNewDelete);
    CPPUNIT_TEST (testSubClassNewDelete);
    CPPUNIT_TEST_SUITE_END ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test global memory exhaustion conditions.

Check how the %Facsimile library's global <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators handle memory exhaustion.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testGlobalMemoryExhaustion () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test sub-class memory exhaustion conditions.

Check how the Facsimile::Collectable <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators handle memory exhaustion.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testSubClassMemoryExhaustion () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test global new handler conditions.

Check how the %Facsimile library's global <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators handle memory exhaustion with a
std::bad_alloc-throwing new handler.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testGlobalThrowingNewHandler () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test sub-class new handler conditions.

Check how the Facsimile::Collectable <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators handle memory exhaustion with a
std::bad_alloc-throwing new handler.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testSubClassThrowingNewHandler () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test global new handler conditions.

Check how the %Facsimile library's global <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators handle memory exhaustion with a
non-throwing new handler.

\note This test changes the size of the available heap.  Earlier tests will
fail beyond this point.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testGlobalNonThrowingNewHandler () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test sub-class new handler conditions.

Check how the Facsimile::Collectable <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators handle memory exhaustion with a
non-throwing new handler.

\note This test changes the size of the available heap.  Earlier tests will
fail beyond this point.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testSubClassNonThrowingNewHandler () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test that global <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators were replaced.

Check that the %Facsimile library's global <code><strong>new</strong></code>
and <code><strong>delete</strong></code> operators are active, and not some
other versions.  Also check that these operators are working.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testGlobalNewDelete () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test that Facsimile::Collectable <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators were replaced.

Check that the Facsimile::Collectable class's <code><strong>new</strong></code>
and <code><strong>delete</strong></code> operators are active, and not some
other versions.  Also check that these operators are working.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public:
    void testSubClassNewDelete () throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Miscellanous tests performed on an integer pointer.

This function performs a number of tests on a pointer to an integer.

\param intPtr Pointer to the integer to be checked.

\param isOnHeap If true, then the int should have been created on the
collector's heap.

\param intValue The value that ought to be assigned to this integer.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

private:
    static void checkIntPointer (int* intPtr, bool isOnHeap, int intValue)
    throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Miscellanous tests performed on a MyUncollectable pointer.

This function performs a number of tests on a pointer to an MyUncollectable
instance.

\param myUncollectablePtr Pointer to the MyUncollectable instance to be
checked.

\param isOnHeap If true, then the instance should have been created on the
collector's heap.

\param intValue The value that ought to be stored by this instance.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

private:
    static void checkMyUncollectablePointer (MyUncollectable*
    myUncollectablePtr, bool isOnHeap, int intValue) throw
    (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Miscellanous tests performed on a MyCollectable pointer.

This function performs a number of tests on a pointer to an MyCollectable
instance.

\param myCollectablePtr Pointer to the MyCollectable instance to be checked.

\param isOnHeap If true, then the instance should have been created on the
collector's heap.

\param intValue The value that ought to be stored by this instance.

\exception CppUnit::Exception Thrown by any CppUnit assertions that have
failed.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

private:
    static void checkMyCollectablePointer (MyCollectable* myCollectablePtr,
    bool isOnHeap, int intValue) throw (CppUnit::Exception);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Garbage collect until finalization count reached.

Keeps forcing garbage collections until either a specified finalization count
is reached or a re-try count is reached.

\param targetCount The target finalization count.  Garbage collections will
stop when the finalization count reaches or exceeds this value.

\param retryLimit The maximum number of times that garbage collection will be
performed before the function gives up.

\return 0 if the re-run count expired, otherwise, the number of re-tries before
the finalization count was reached.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

private:
    static Count garbageCollect (Count targetCount, Count retryLimit) throw ();
};

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Heap::getHeapSizeIndex () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline int Heap::getHeapSizeIndex () throw ()
{
    return heapSizeIndex;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Heap::setHeapSize () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline void Heap::setHeapSize () throw ()
{
    GC_set_max_heap_size (heapSize [heapSizeIndex]);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Heap::initialize () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline void Heap::initialize () throw ()
{
    assert (heapSizeIndex == 0);
    setHeapSize ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Heap::updateHeapSize () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline void Heap::updateHeapSize () throw ()
{
    ++heapSizeIndex;
    setHeapSize ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Heap::Heap () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline Heap::Heap () throw ()
{
    setHeapSize ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Heap::~Heap () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline Heap::~Heap () throw ()
{
    GC_set_max_heap_size (0);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NewHandler::~NewHandler () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline NewHandler::~NewHandler () throw ()
{
    std::set_new_handler (originalHandler);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NewHandler::resetFlag () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline void NewHandler::resetFlag () throw ()
{
    newHandlerSeen = false;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NewHandler::wasNewHandlerSeen () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline bool NewHandler::wasNewHandlerSeen () throw ()
{
    return newHandlerSeen;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyMemoryBase::MyMemoryBase (int) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyMemoryBase::MyMemoryBase (int value) throw ():
    someValue (value)
{
    boost::recursive_mutex::scoped_lock lock (staticMutex);
    ++instanceCount;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyMemoryBase::~MyMemoryBase () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyMemoryBase::~MyMemoryBase () throw ()
{
    boost::recursive_mutex::scoped_lock lock (staticMutex);
    assert (instanceCount > 0);
    --instanceCount;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyMemoryBase::getInstanceCount () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline Count MyMemoryBase::getInstanceCount () throw ()
{
    boost::recursive_mutex::scoped_lock lock (staticMutex);
    return instanceCount;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyMemoryBase::getValue () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline int MyMemoryBase::getValue () const throw ()
{
    return someValue;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyCollectable::MyCollectable (int) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyCollectable::MyCollectable (int value) throw ():
    Facsimile::Collectable (), MyMemoryBase (value)
{
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyCollectable::getFinalizationCount () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline Count MyCollectable::getFinalizationCount () throw ()
{
    boost::recursive_mutex::scoped_lock lock (staticMutex);
    return finalizationCount;
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyUncollectable::MyUncollectable (int) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyUncollectable::MyUncollectable (int value) throw ():
    MyMemoryBase (value)
{
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyUncollectable::~MyUncollectable () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyUncollectable::~MyUncollectable () throw ()
{
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyUncollectableWithCollectablePtr::MyUncollectableWithCollectablePtr (int,
MyCollectable*) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyUncollectableWithCollectablePtr::MyUncollectableWithCollectablePtr
(int value, MyCollectable* collectableObject) throw ():
    MyMemoryBase (value), someCollectableObject (collectableObject)
{
    assert (collectableObject);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyUncollectableWithCollectablePtr::~MyUncollectableWithCollectablePtr ()
implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyUncollectableWithCollectablePtr::~MyUncollectableWithCollectablePtr ()
throw ()
{
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
MyUncollectableWithCollectablePtr::getPointer () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

inline MyCollectable* MyUncollectableWithCollectablePtr::getPointer () const
throw ()
{
    return someCollectableObject;
}
#endif /*TEST_FACSIMILE_COLLECTABLE_HPP_*/
