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
%Facsimile Collectable class C++ header file.

C++ header file for the Collectable class, and associated elements, that are
integral members of the Facsimile namespace.

\page features %Facsimile Library Features

\section collector Automated Garbage Collection
The %Facsimile library provides <em>automated garbage collection</em>
facilities to both simplify the coding of simulation models and to eliminate
<em>memory leaks</em>.  An automated garbage collector, henceforth referred to
simply as a <em>collector</em>, is a system for managing dyanamically-allocated
memory.  The collector is able to identify memory that is no longer referenced,
termed <em>inaccessible</em> memory, and automatically release it.  This frees
the programmer from the complexities and chores of memory management.

To take advantage of this feature, your classes must be derived from
Facsimile::Collectable, or one of its sub-classes.  All %Facsimile classes are
Facsimile::Collectable sub-classes .

There are two primary problems with manual memory management:
-# Freeing memory that is still referenced in your program.  When the freed
memory is subsequently accessed, by code that thinks the memory is still
available, application crashes become likely.
-# Failing to free memory that is no longer referenced.  Memory that is not
freed ties up precious memory resources.  This is a particular problem for
simulations that can be expected to run for days at a time, since memory leaks
will reduce the amount of memory available (not just to the simulation, but to
the entire system) until memory exhaustion occurs.  This class of problem is
termed a <em>memory leak</em>.

\see Facsimile::Collectable

\internal
The %Facsimile library provides replacements for the global C++
<code><strong>new</strong></code> and <code><strong>delete</strong></code>
operators (defined within the Collectable.cpp source file).  There are a number
of reasons why these replacement operators are required:

-# The <em>Boehm-Demers-Weiser</em> collector allocates memory in heaps that it
controls, so that it can scan them for pointers to inaccessible collectable
resources.  If pointers to collectable resources are stored in other heaps
(such as that provided by the default C++ global
<code><strong>new</strong></code> operator, or by the C <code>malloc
(size_t)</code> function - both of which are outside the control of the
collector), then the collector, being unable to scan such memory, may decide
that a collectable resource is inaccessible when it is still referenced within
the non-collector heap.  Consequently, the collector will free such resources,
only for problems to arise when accessed through the non-collector heap's
pointers.  To overcome this, the collector's allocation and de-allocation
routines must be used throughout a %Facsimile application.  For compatibility
with third-party code, these replacement routines mark memory as being
uncollectable so that they must still be freed using traditional, manual means.
(However, be aware that third-party libraries may still use their own explicit
memory allocation/de-allocation routines all the same.)
-# The Boehm-Demers-Weiser collector provides replacement C++ global
<code><strong>new</strong></code> and <code><strong>delete</strong></code>
operators.  Unfortunately, they pre-date the ISO/IEC C++ standard and do
not conform to accepted standards for new and delete.  For example, they do not
throw the <code>std::bad_alloc</code> exception if memory is exhausted
(preferring to return a 0 pointer instead).  Also, they do not allow a handler
to be specified for attempting to recover memory prior to throwing the
<code>std::bad_alloc</code> exception.  To overcome this problem, we have to
provide our own replacement global <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators.
-# Leak detection for the entire simulation applications becomes a possibility,
using tools provided by the Boehm-Demers-Weiser collector.

\note
The global <em>placement</em> <code><strong>new</strong></code> and
<code><strong>delete</strong></code> operators (both single and array forms)
cannot be replaced.  Typically, these operators are used to create objects
within existing memory.  Provided that this existing memory was claimed using
the collector's allocation routines, no problems should arise.  Otherwise,
avoid using placement <code><strong>new</strong></code> operators and/or
storing pointers to collectable objects within objects created via placement
<code><strong>new</strong></code>.
*/
//=============================================================================

#ifndef FACSIMILE_COLLECTABLE_HPP_
#define FACSIMILE_COLLECTABLE_HPP_

/*
Relevant header files.
*/

#include <new>
#include <cassert>
#include <gc/gc.h>

/*
Namespace declaration.
*/

namespace Facsimile
{

//=============================================================================
/**
Abstract base class for collectable classes.

Sub-class instances that are created on the free store, via the
<code><strong>new</strong></code> operator, are regarded as being
<em>collectable</em>.  All other, regular C++ class instances, including
sub-classes created on the stack, are regarded as being <em>uncollectable</em>;
such classes still need to have their resources manually recycled (or recycled
by the C++ compiler-generated code when it determines that an instance has gone
out of scope).

A collectable instance is classified as being either <em>accessible</em> or
<em>inaccessible</em>.  An accessible instance is one to which active pointers
exist within the program; the instance can still be accessed through these
active pointers.  By contrast, an inaccessible object has no active pointers to
it remaining, and can be safely collected and recycled.  The <em>collector</em>
automatically keeps track of which instances are inaccessible, and collects
objects as needed.

The benefits of using an automated collector are many:
- <em>Memory leaks</em>, a significant problem if they occur within a
simulation model, are are almost entirely eradicated.  A leak will result when 
memory that is no longer required is not freed, reducing available memory.
- Programmers are freed from the chore of determining when each instance can be
safely deleted, eliminating bugs resulting from accessing memory that was freed
prematurely (although see remark on this subject below).
- Improved code reliability and robustness.
- Potential for improved memory management, with reduced memory fragmentation.

You are strongly recommended to derive your own classes from
Facsimile::Collectable.

\remarks
The <code><strong>new []</strong></code> and <code><strong>delete
[]</strong></code> operators are forbidden for use when creating sub-class
objects.  Any attempt to create sub-class arrays using the <code><strong>new
[]</strong></code> operator will yield an exception.  The primary reason for
this is that it is currently not possible to have clean-up performed for all
members of an array when they are collected.  However, it is also poor C++
programming practice to create arrayed objects, instead of using an appropriate
collection (such as std::vector).

\par
It is strongly recommended that you do not use the
<code><strong>delete</strong></code> operator to manually free sub-class
instances; pointers to the deleted instance may still be held elsewhere in
your program, resulting in crashes or other unpredictable behavior when
de-referenced.  By not using the <code><strong>delete</strong></code> operator,
you will eliminate this class of bug.

\par
The behavior of sub-class virtual destructors changes subtlely compared with
regular, non-collectable classes.  Instance destruction, for an element created
on the free store via the <code><strong>new</strong></code> operator, will
occur at an unpredictable time, depending upon when the garbage collector
recognizes that the instance is no longer referenced.  In some cases, the
garbage collector may <em>never</em> collect an instance, leaving it available
for the duration of the program.  For this reason, you should use virtual
destructors solely to release any allocated resource (such as non-collectable
objects, file handles, etc.); do not schedule simulation events from with
destructors.

\par
Because virtual destructors will be invoked at times depending upon the state
of the program and the available memory, problems relating to the execution
time of virtual destructors are likely to be difficult to reproduce.

\note
This class is based upon the <code>gc</code> and <code>gc_cleanup</code>
classes, developed by John R Ellis and Jesse Hull, that are supplied with the
<em>Boehm-Demers-Weiser</em> conservative collector currently used by
%Facsimile.  However, the <code><strong>new</strong></code> operators defined
in that class have non-standard behavior, in that they return 0 (or
<code>NULL</code>), rather than throwing a <code>std::bad_alloc</code>
exception, in the event that insufficient contiguous free store memory is
available.  They also make no reference to the standard <em>new handler</em>
feature.  Furthermore, the <code>gc</code> and <code>gc_cleanup</code> classes
are defined in the global namespace, which is not a desirable feature of the
%Facsimile library.  The Facsimile::Collectable class corrects these
deficiencies, allowing for collectable sub-class memory management, whilst
maintaining similar (if simplified) functionality.  Some additional features of
the <em>Boehm-Demers-Weiser</em> collector have also been incorporated.

\see http://www.hpl.hp.com/personal/Hans_Boehm/gc/

\see ftp://ftp.parc.xerox.com/pub/ellis/gc

\internal

\todo Performance improvements: consider use of GC_MALLOC_ATOMIC for classes
that do not contain pointers and/or the use of thread local storage.
*/
//=============================================================================

    class Collectable
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Handler to clean up collected objects.

Each time an object is collected, this function is called to perform
miscellaneous finalization clean-up.  In turn, this function invokes the
corresponding instance destructor(s) so that sub-class clean-up is performed in
a more intuitive manner for C++ programmers.

\remarks Special handling is necessary when dealing with the deletion of
arrayed objects, to ensure that the destructor of each element is called.

\param base Base address of the memory that has been collected.  This value is
passed to the collector when this function is registered by the initialize()
function.

\param offset The offset, in char units, from the base address to the base of
the single object instance stored within the collected memory.  Under normal
circumstances, this value is 0 (indicating that the base address of the
collected memory and the base address of the object instance it contains as the
same), however it will be non-zero if the debug version of the garbage
collector is used to build the library (that is, if the <code>GC_DEBUG</em>
macro is defined).  This value is passed to the collector when this function is
registered by the initialize() function.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private:
        static void cleanup (GC_PTR base, GC_PTR offset) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

\remarks This constructor is protected so that construction is possible from
derived classes only.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected:
        inline Collectable () throw ()
        {
            initialize ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Copy constructor.

\param other The Collectable object to be copied during construction.

\remarks This constructor is protected so that construction is possible from
derived classes only.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected:
        inline Collectable (const Collectable&) throw ()
        {
            initialize ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Instance initialization.

This function performs initialization common to all constructors.  It is not
intended to be called from anywhere except from within a constructor.

\remarks The primary objective of this function is to register the cleanup()
function as a finalization routine for each class instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private:
        void initialize () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        virtual ~Collectable () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Normal, single <code><strong>new</strong></code> operator with automatic
collection.

<code><strong>new</strong></code> operator to be used when allocating dynamic
storage for a single sub-class instance.  This version of the operator throws
an exception, rather than returning 0, if insufficent contiguous memory is
available.

\remarks The allocated memory is automatically collected when the collector
determines that it is inaccessible and so it is not necessary (and it is not
recommended) that this memory be explicitly freed using the corresponding class
<code><strong>delete</strong></code> operator.

\par This operator replaces the global <code><strong>new</strong></code>
operator when allocating storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely allocates memory that a constructor can
initialize.

\param size The number of bytes of contiguous free store memory required.  This
is an unsigned value that cannot be negative.  A value of zero should be
treated as a valid required number of bytes.

\return A pointer to the newly allocated memory.

\exception std::bad_alloc Insufficient contiguous free store memory available
to satisfy allocation request.  No memory was allocated.

\see operator new(size_t, const std::nothrow_t&)

\see operator new[](size_t)

\see operator delete(void*)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void* operator new (size_t size) throw (std::bad_alloc);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Normal, single <code><strong>delete</strong></code> operator.

<code><strong>delete</strong></code> operator to be used when explicitly
freeing dynamic storage for a single sub-class instance that was allocated by
the corresponding class <code><strong>new</strong></code> operator.

\remarks Since all memory allocated by the corresponding class
<code><strong>new</strong></code> operator is automatically and safely
collected when the collector determines that it is inaccessible, it is not
necessary (and not recommended) that this memory be freed using this operator.

\par This operator replaces the global <code><strong>delete</strong></code>
operator when freeing storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely releases memory back to the collector.

\deprecated You are recommended not to use this operator and to instead rely on
the garbage collector to free memory when the collector determines that it is
safe to do so.

\par
Manual collection is problematic because it can never be certain that the
collected memory is <em>inaccessible</em>.  If your program contains active
pointers to the memory that you have manually deleted, then subsequent attempts
to access that memory through those pointers will have catastrophic effects.
It is far safer, and makes for simpler code, to allow such memory to be
collected automatically when it is guaranteed that the memory is inaccessible.

\param memPtr Pointer to the memory to be freed.

\see operator new(size_t)

\see operator delete(void*, const std::nothrow_t&)

\see operator delete[](void*)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void operator delete (void* memPtr) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Non-throwing, single <code><strong>new</strong></code> operator with automatic
collection.

<code><strong>new</strong></code> operator to be used when allocating dynamic
storage for a single sub-class instance.  This version of the operator returns
0, rather than throwing an exception, if insufficent contiguous memory is
available.

\remarks The allocated memory is automatically collected when the collector
determines that it is inaccessible and so it is not necessary (and it is not
recommended) that this memory be explicitly freed using the corresponding class
<code><strong>delete</strong></code> operator.

\par This operator replaces the global <code><strong>new</strong></code>
operator when allocating storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely allocates memory that a constructor can
initialize.

\param size The number of bytes of contiguous free store memory required.  This
is an unsigned value that cannot be negative.  A value of zero should be
treated as a valid required number of bytes.

\param nothrow A dummy argument whose type is used to differentiate it from the
normal, exception-throwing version of the <code><strong>new</strong></code>
operator.

\return A pointer to the newly allocated memory or 0 if insufficient memory is
available.

\see operator new(size_t)

\see operator new[](size_t, const std::nothrow_t&)

\see operator delete(void*, const std::nothrow_t&)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void* operator new (size_t size, const std::nothrow_t& nothrow) throw
        ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Non-throwing, single <code><strong>delete</strong></code> operator.

<code><strong>delete</strong></code> operator to be used when explicitly
freeing dynamic storage for a single sub-class instance that was allocated by
the corresponding class <code><strong>new</strong></code> operator.

\remarks Since all memory allocated by the corresponding class
<code><strong>new</strong></code> operator is automatically and safely
collected when the collector determines that it is inaccessible, it is not
necessary (and not recommended) that this memory be freed using this operator.

\par This operator replaces the global <code><strong>delete</strong></code>
operator when freeing storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely releases memory back to the collector.

\deprecated You are recommended not to use this operator and to instead rely on
the garbage collector to free memory when the collector determines that it is
safe to do so.

\par
Manual collection is problematic because it can never be certain that the
collected memory is <em>inaccessible</em>.  If your program contains active
pointers to the memory that you have manually deleted, then subsequent attempts
to access that memory through those pointers will have catastrophic effects.
It is far safer, and makes for simpler code, to allow such memory to be
collected automatically when it is guaranteed that the memory is inaccessible.

\param memPtr Pointer to the memory to be freed.

\param nothrow A dummy argument whose type is used to differentiate it from the
normal version of the <code><strong>delete</strong></code> operator.

\see operator new(size_t, const std::nothrow_t&)

\see operator delete(void*)

\see operator delete[](void*, const std::nothrow_t&)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void operator delete (void* memPtr, const std::nothrow_t& nothrow)
        throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Normal, array <code><strong>new []</strong></code> operator with automatic
collection.

<code><strong>new []</strong></code> operator to be used when allocating
dynamic storage for an array of sub-class instances.  This version of the
operator is intended to throw an exception, rather than returning 0, if
insufficent contiguous memory is available.  However, since the Collectable
class does not currently support the <code><strong>new []</strong></code>
operator, it merely throws a Facsimile::X::ArrayNewNotSupportedException
exception if used; this exception is not part of the exception specification
since it must conform to the standard definition.

\remarks This operator replaces the global <code><strong>new []</strong></code>
operator when allocating storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely allocates memory that a constructor can
initialize.

\deprecated Creating arrays of sub-class objects on the free store is currently
unsupported by the %Facsimile library.  It is preferred that you use a suitable
collection instead, such as the <em>standard template library</em>
<code>std::Vector</code> class.

\param size The number of bytes of contiguous free store memory required.  This
is an unsigned value that cannot be negative.  A value of zero should be
treated as a valid required number of bytes.

\return A pointer to the newly allocated memory.

\exception std::bad_alloc Insufficient contiguous free store memory available
to satisfy allocation request.  No memory was allocated.

\see operator new[](size_t, const std::nothrow_t&)

\see operator new(size_t)

\see operator delete[](void*)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void* operator new [] (size_t size) throw (std::bad_alloc);

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Normal, array <code><strong>delete []</strong></code> operator.

<code><strong>delete []</strong></code> operator to be used when explicitly
freeing dynamic storage for a single sub-class instance that was allocated by
the corresponding class <code><strong>new []</strong></code> operator.

\remarks Since the corresponding class <code><strong>new []</strong></code>
operator is currently unsupported, there should never be a need to call this
function and any attempt to do so will result in a
Facsimile::X::ArrayDeleteNotSupportedException exception; this exception is not
part of the exeception specification since it must conform to the standard
definition.

\par This operator replaces the global <code><strong>delete []</strong></code>
operator when freeing storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely releases memory back to the collector.

\deprecated Since the corresponding <code><strong>new []</strong></code>
operator is not supported for creating arrays of sub-class instances on the
free store, you should not use this operator.

\param memPtr Pointer to the memory to be freed.

\see operator new[](size_t)

\see operator delete[](void*, const std::nothrow_t&)

\see operator delete(void*)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void operator delete [] (void* memPtr) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Non-throwing, array <code><strong>new []</strong></code> operator with
automatic collection.

<code><strong>new []</strong></code> operator to be used when allocating
dynamic storage for an array of sub-class instances.  This version of the
operator is intended to return 0, rather than throwing an exception, if
insufficent contiguous memory is available.  However, since the Collectable
class does not currently support the <code><strong>new []</strong></code>
operator, it merely throws a Facsimile::X::ArrayNewNotSupportedException
exception if used; this exception is not part of the exception specification
since it must conform to the standard definition.

\remarks This operator replaces the global <code><strong>new []</strong></code>
operator when allocating storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely allocates memory that a constructor can
initialize.

\deprecated Creating arrays of sub-class objects on the free store is currently
unsupported by the %Facsimile library.  It is preferred that you use a suitable
collection instead, such as the <em>standard template library</em>
<code>std::Vector</code> class.

\param size The number of bytes of contiguous free store memory required.  This
is an unsigned value that cannot be negative.  A value of zero should be
treated as a valid required number of bytes.

\param nothrow A dummy argument whose type is used to differentiate it from the
normal, exception-throwing version of the <code><strong>new []</strong></code>
operator.

\return A pointer to the newly allocated memory or 0 if insufficient memory is
available.

\see operator new[](size_t)

\see operator new(size_t, const std::nothrow_t&)

\see operator delete[](void*, const std::nothrow_t&)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void* operator new [] (size_t size, const std::nothrow_t& nothrow)
        throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Non-throwing, array <code><strong>delete []</strong></code> operator.

<code><strong>delete []</strong></code> operator to be used when explicitly
freeing dynamic storage for a single sub-class instance that was allocated by
the corresponding class <code><strong>new []</strong></code> operator.

\remarks Since the corresponding class <code><strong>new []</strong></code>
operator is currently unsupported, there should never be a need to call this
function and any attempt to do so will result in a
Facsimile::X::ArrayDeleteNotSupportedException exception; this exception is not
part of the exeception specification since it must conform to the standard
definition.

\par This operator replaces the global <code><strong>delete []</strong></code>
operator when freeing storage for sub-class instances.

\note This is implicitly a static function.  There is no
<code><strong>this</strong></code> pointer available, and the function cannot
modify an object.  It merely releases memory back to the collector.

\deprecated Since the corresponding <code><strong>new []</strong></code>
operator is not supported for creating arrays of sub-class instances on the
free store, you should not use this operator.

\param memPtr Pointer to the memory to be freed.

\param nothrow A dummy argument whose type is used to differentiate it from the
normal version of the <code><strong>delete []</strong></code> operator.

\see operator new[](size_t, const std::nothrow_t&)

\see operator delete[](void*)

\see operator delete(void*, const std::nothrow_t&)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public:
        void operator delete [] (void* memPtr, const std::nothrow_t& nothrow)
        throw ();
    };
}
#endif /*FACSIMILE_COLLECTABLE_HPP_*/
