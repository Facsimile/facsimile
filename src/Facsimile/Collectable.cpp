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
%Facsimile Collectable class C++ source file.

C++ source file for the Collectable class, and associated elements, that are
integral members of the Facsimile namespace.
*/
//=============================================================================

/*
Relevant header files.
*/

#include <Facsimile/Collectable.hpp>
#include <Facsimile/X/ArrayNewNotSupportedException.hpp>
#include <Facsimile/X/ArrayDeleteNotSupportedException.hpp>

/*
Assume the Facsimile namespace throughout.
*/

using namespace Facsimile;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator new (std::size_t)</code> function.

Allocates <em>uncollectable</em> memory on the <em>collector</em>'s heap.

If insufficient memory exists to satisfy this request, the function will throw
the <code>std::bad_alloc</code> exception.  However, before throwing an
exception, the function will first call any existing <em>new handler</em>
(defined by the <code>std::set_new_handler (std::new_handler)</code> function).
This is standard C++ behavior.

\remarks This function replaces the global <code>operator new
(std::size_t)</code> function provided by your compiler.  This is necessary in
order that all dynamically allocated memory can be scanned, by the collector
for pointers to <em>collectable</em> memory.

\note The memory allocated by this function will not be automatically collected
and must be explicitly freed via the corresponding
<code><strong>delete</strong></code> operator.  Failure to do so will result in
a <em>memory leak</em>.  This functionality is required for compatibility with
third-party libraries that do not expect or require the use of a collector.

\par
This memory will be scanned by the collector to determine if it contains
pointers to any collectable memory.  If such pointers exist, then the
corresponding collectable memory is still <em>accessible</em> and will not be
collected.  The collector will automatically collect and recycle any
collectable memory that it discovers to be <em>inaccessible</em>.

\par
The memory allocated may be larger than the number of units requested.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable instead of using the global
<code><strong>new</strong></code> operator.

\param size The number of <code><strong>char</strong></code> memory units
required.  This value may be 0.

\return A pointer to the newly allocated memory.

\exception std::bad_alloc Insufficient contiguous memory available to satisfy
this request.

\see ::operator delete(void*)

\see Facsimile::Collectable::operator new(std::size_t)

\see ::operator new[](std::size_t)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* operator new (std::size_t size) throw (std::bad_alloc)
{

/*
If we are unable to obtain sufficient memory, we first check whether we have a
"new handler" to assist us.  If so, we call the handler (which will either
attempt to reclaim some memory or throw a std::bad_alloc exception) and re-try,
otherwise we throw the std::bad_alloc exception ourselves.  In order to keep
re-trying, we must place the memory allocation code within an infinite loop.
*/

    for (; ; )
    {

/*
Allocate the requested memory and mark it as being uncollectable - this memory
will still need to be manually freed by the user.  (There are two reasons why
we must do this.  Firstly, the global new operator may be used by third party
libraries and/or code that expects this type of behavior.  Secondly, if we do
not use the collector's allocation routine, then the collector cannot scan the
memory claimed by global new for pointers to collectible objects; consequently,
it may mistakenly free memory that is actually still accessible.)

Incidentally, size cannot be negative as it is an unsigned integer value.  If
size is 0, then we ought to still get a pointer to, er, nothing.

If we obtain the memory that we require, return a pointer to it.
*/

        void* memPtr =  GC_MALLOC_UNCOLLECTABLE (size);
        if (memPtr)
        {
            return memPtr;
        }

/*
If we made it this far, then we do not have sufficient virtual memory to
satisfy the caller's requirements.  If do not we have a "new handler" function
then throw the std::bad_alloc exception.
*/

        std::new_handler handler = std::set_new_handler (0);
        if (!handler)
        {
            throw std::bad_alloc ();
        }

/*
OK, we have new handler defined; call it and see what it can do.

Any new handler will be defined via the standard function
"std::set_new_handler ()".

Note: This function will loop indefinitely unless the new handler throws an
exception if unable to reclaim any additional memory.  Simply doing something,
not checking whether memory was claimed, then returning will not suffice.
Also, this exception must be std::bad_alloc (or a sub-class of std::bad_alloc)
otherwise the exception will terminate the program.  
*/

        std::set_new_handler (handler);
        handler ();
    }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator delete (void*)</code> function.

Frees <em>uncollectable</em> memory from the <em>collector</em>'s heap.

\remarks This function replaces the global <code>operator delete (void*)</code>
function provided by your compiler.  This is necessary to match up with the
replacement global operator new(std::size_t) function.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable instead of using the global
<code><strong>delete</strong></code> operator.

\param memPtr Pointer to the memory to be freed.  If this value is 0, then
nothing will happen (as required by the C++ standard).

\see ::operator new(std::size_t)

\see Facsimile::Collectable::operator delete(void*)

\see ::operator delete[](void*)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void operator delete (void* memPtr) throw ()
{

/*
Note: The C++ standard allows deletion of 0 (NULL) pointers.  GC_FREE appears
to support this and does not complain (test suite checks this condition).
Simply free the referenced memory, whether NULL or not.
*/

    GC_FREE (memPtr);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator new (std::size_t, const
std::nothrow_t&)</code> function.

Allocates <em>uncollectable</em> memory on the <em>collector</em>'s heap.

If insufficient memory exists to satisfy this request, the function will return
0, instead of throwing the <code>std::bad_alloc</code> exception.  However,
before returning 0, the function will first call any existing <em>new
handler</em> (defined by the <code>std::set_new_handler
(std::new_handler)</code> function).  This is standard C++ behavior.

\remarks This function replaces the global <code>operator new (std::size_t,
const std::nothrow_t&)</code> function provided by your compiler.  This is
necessary in order that all dynamically allocated memory can be scanned, by the
collector, for pointers to <em>collectable</em> memory.

\note The memory allocated by this function will not be automatically collected
and must be explicitly freed via the corresponding
<code><strong>delete</strong></code> operator.  Failure to do so will result in
a <em>memory leak</em>.  This functionality is required for compatibility with
third-party libraries that do not expect or require the use of a collector.

\par
This memory will be scanned by the collector to determine if it contains
pointers to any collectable memory.  If such pointers exist, then the
corresponding collectable memory is still <em>accessible</em> and will not be
collected.  The collector will automatically collect and recycle any
collectable memory that it discovers to be <em>inaccessible</em>.

\par
The memory allocated may be larger than the number of units requested.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable and use the <code>std::bad_alloc</code>
version of <code><strong>new</strong></code> instead.

\param size The number of <code><strong>char</strong></code> memory units
required.  This value may be 0.

\param nothrow A dummy argument to different this function from the
<code>std::bad_alloc</code>-throwing version.

\return A pointer to the newly allocated memory, or 0 if there is insufficient
contiguous memory available to satisfy this request.

\see ::operator delete(void*,const std::nothrow_t&)

\see Facsimile::Collectable::operator new(std::size_t,const std::nothrow_t&)

\see ::operator new[](std::size_t,const std::nothrow_t&)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* operator new (std::size_t size, const std::nothrow_t&) throw ()
{

/*
Use the regular single operator new to allocate the memory and return a pointer
to it.
*/

    try
    {
        return ::operator new (size);
    }

/*
If we caught a std::bad_alloc exception, then return 0.
*/

    catch (std::bad_alloc)
    {
        return 0;
    }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator delete (void*, const
std::nothrow_t&)</code> function.

Frees <em>uncollectable</em> memory from the <em>collector</em>'s heap.

\remarks This function replaces the global <code>operator delete (void*,
std::nothrow_t&)</code> function provided by your compiler.  This is necessary
to match up with the replacement global operator new(std::size_t,const
std::nothrow_t&) function.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable, and use the <code>std::bad_alloc</code>
version of <code><strong>new</strong></code> instead.

\param memPtr Pointer to the memory to be freed.  If this value is 0, then
nothing will happen (as required by the C++ standard).

\see ::operator new(std::size_t,const std::nothrow_t&)

\see Facsimile::Collectable::operator delete(void*,const std::nothrow_t&)

\see ::operator delete[](void*,const std::nothrow_t&)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void operator delete (void* memPtr, const std::nothrow_t&) throw ()
{

/*
The implementation of this version is identical to the regular, single version.
*/

    ::operator delete (memPtr);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator new [] (std::size_t)</code> function.

Allocates arrayed <em>uncollectable</em> memory on the <em>collector</em>'s
heap.

If insufficient memory exists to satisfy this request, the function will throw
the <code>std::bad_alloc</code> exception.  However, before throwing an
exception, the function will first call any existing <em>new handler</em>
(defined by the <code>std::set_new_handler (std::new_handler)</code> function).
This is standard C++ behavior.

\remarks This function replaces the global <code>operator new []
(std::size_t)</code> function provided by your compiler.  This is necessary in
order that all dynamically allocated memory can be scanned, by the collector
for pointers to <em>collectable</em> memory.

\note The memory allocated by this function will not be automatically collected
and must be explicitly freed via the corresponding <code><strong>delete
[]</strong></code> operator.  Failure to do so will result in a <em>memory
leak</em>.  This functionality is required for compatibility with third-party
libraries that do not expect or require the use of a collector.

\par
This memory will be scanned by the collector to determine if it contains
pointers to any collectable memory.  If such pointers exist, then the
corresponding collectable memory is still <em>accessible</em> and will not be
collected.  The collector will automatically collect and recycle any
collectable memory that it discovers to be <em>inaccessible</em>.

\par
The memory allocated may be larger than the number of units requested.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable instead of using the global
<code><strong>new []</strong></code> operator, and that you prefer the use of
container classes (such as <code>std::vector</code>) to the use of raw arrays.

\param size The number of <code><strong>char</strong></code> memory units
required.  This value may be 0.

\return A pointer to the newly allocated memory.

\exception std::bad_alloc Insufficient contiguous memory available to satisfy
this request.

\see ::operator delete[](void*)

\see Facsimile::Collectable::operator new[](std::size_t)

\see ::operator new(std::size_t)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* operator new [] (std::size_t size) throw (std::bad_alloc)
{

/*
The implementation of this version is identical to the regular, single version.
*/

    return ::operator new (size);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator delete [] (void*)</code> function.

Frees arrayed <em>uncollectable</em> memory from the <em>collector</em>'s heap.

\remarks This function replaces the global <code>operator delete []
(void*)</code> function provided by your compiler.  This is necessary to match
up with the replacement global operator new[](std::size_t) function.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable instead of using the global
<code><strong>delete []</strong></code> operator, and that you prefer the use
of container classes (such as <code>std::vector</code>) to the use of raw
arrays.

\param memPtr Pointer to the memory to be freed.  If this value is 0, then
nothing will happen (as required by the C++ standard).

\see ::operator new[](std::size_t)

\see Facsimile::Collectable::operator delete[](void*)

\see ::operator delete(void*)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void operator delete [] (void* memPtr) throw ()
{

/*
The implementation of this version is identical to the regular, single version.
*/

    ::operator delete (memPtr);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator new [] (std::size_t, const
std::nothrow_t&)</code> function.

Allocates arrayed <em>uncollectable</em> memory on the <em>collector</em>'s
heap.

If insufficient memory exists to satisfy this request, the function will return
0, instead of throwing the <code>std::bad_alloc</code> exception.  However,
before returning 0, the function will first call any existing <em>new
handler</em> (defined by the <code>std::set_new_handler
(std::new_handler)</code> function).  This is standard C++ behavior.

\remarks This function replaces the global <code>operator new [] (std::size_t,
const std::nothrow_t&)</code> function provided by your compiler.  This is
necessary in order that all dynamically allocated memory can be scanned, by the
collector, for pointers to <em>collectable</em> memory.

\note The memory allocated by this function will not be automatically collected
and must be explicitly freed via the corresponding <code><strong>delete
[]</strong></code> operator.  Failure to do so will result in a <em>memory
leak</em>.  This functionality is required for compatibility with third-party
libraries that do not expect or require the use of a collector.

\par
This memory will be scanned by the collector to determine if it contains
pointers to any collectable memory.  If such pointers exist, then the
corresponding collectable memory is still <em>accessible</em> and will not be
collected.  The collector will automatically collect and recycle any
collectable memory that it discovers to be <em>inaccessible</em>.

\par
The memory allocated may be larger than the number of units requested.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable and use the <code>std::bad_alloc</code>
version of <code><strong>new []</strong></code> instead.  We also recommend
that you prefer the use of container classes (such as <code>std::vector</code>)
to the use of raw arrays.

\param size The number of <code><strong>char</strong></code> memory units
required.  This value may be 0.

\param nothrow A dummy argument to different this function from the
<code>std::bad_alloc</code>-throwing version.

\return A pointer to the newly allocated memory, or 0 if there is insufficient
contiguous memory available to satisfy this request.

\see ::operator delete[](void*,const std::nothrow_t&)

\see Facsimile::Collectable::operator new[](std::size_t,const std::nothrow_t&)

\see ::operator new(std::size_t,const std::nothrow_t&)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* operator new [] (std::size_t size, const std::nothrow_t& nothrow) throw
()
{

/*
The implementation of this version is identical to the nothrow, single version.
*/

    return ::operator new (size, nothrow);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Replacement C++ global <code>operator delete [] (void*, const
std::nothrow_t&)</code> function.

Frees arrayed <em>uncollectable</em> memory from the <em>collector</em>'s heap.

\remarks This function replaces the global <code>operator delete [] (void*,
std::nothrow_t&)</code> function provided by your compiler.  This is necessary
to match up with the replacement global operator new[](std::size_t,const
std::nothrow_t&) function.

\deprecated
This function is provided for compatibility with any third-party libraries that
require it.  The %Facsimile project strongly recommends that you derive your
classes from Facsimile::Collectable, or use the <code>std::bad_alloc</code>
version of <code><strong>new []</strong></code> instead, or that you prefer the
use of container classes (such as <code>std::vector</code>) to the use of raw
arrays.

\param memPtr Pointer to the memory to be freed.  If this value is 0, then
nothing will happen (as required by the C++ standard).

\see ::operator new[](std::size_t,const std::nothrow_t&)

\see Facsimile::Collectable::operator delete[](void*,const std::nothrow_t&)

\see ::operator delete(void*,const std::nothrow_t&)

\see \ref collector

\see Facsimile::Collectable

\internal This function cannot be inlined.  It must be available to the linker,
so that it will be used by third-party libraries.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void operator delete [] (void* memPtr, const std::nothrow_t&) throw ()
{

/*
The implementation of this version is identical to the regular, single version.
*/

    ::operator delete (memPtr);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::cleanup (GC_PTR, GC_PTR) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Collectable::cleanup (GC_PTR base, GC_PTR offset) throw ()
{

/*
Some sanity checks.
*/

    assert (base);

/*
Convert arguments to more appropriate types.  This is necessary because of the
pointer arithmetic that is performed on these two values.
*/

    char* baseAddress = static_cast <char*> (base);
    std::ptrdiff_t offsetChars = reinterpret_cast <std::ptrdiff_t> (offset);

/*
Re-construct a pointer to the object being finalized.  This requires that we
add "offset" to "base", using char pointer arithmetic, then cast the result
to a Collectable pointer.

Note: The subsequent logic will fail if the array new (new []) operator is used
to allocate the memory, which is why the overrided operator new [] throws an
exception if used.
*/

    Collectable* collectedObject = reinterpret_cast <Collectable*> (baseAddress
    + offsetChars);
    assert (collectedObject);

/*
Invoke the virtual destructor to perform the necessary clean up operations.
*/

    collectedObject->~Collectable ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::initialize () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Collectable::initialize () throw ()
{

/*
Determine the base memory address of this instance, from the garbage
collector's viewpoint.  If this value is 0, then the memory was not allocated
on the collector heap and we need do nothing more.
*/

    char* base = static_cast <char*> (GC_base (static_cast <GC_PTR> (this)));
    if (base)
    {

/*
Determine the difference between the value of the "this" pointer and the base
address.  This becomes the offset.
*/

        assert (reinterpret_cast <char*> (this) >= base);
        std::ptrdiff_t offset = reinterpret_cast <char*> (this) - base;

/*
OK.  Now register the clean-up function, ignoring any self-references.
*/

        GC_REGISTER_FINALIZER_IGNORE_SELF (static_cast <GC_PTR> (base),
        static_cast <GC_finalization_proc> (cleanup), reinterpret_cast <GC_PTR>
        (offset), 0, 0);
    }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::~Collectable () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Collectable::~Collectable () throw ()
{

/*
Unregister the finalization function for this instance - but only if this
instance was created on the collector heap.
*/

    GC_PTR base = GC_base (static_cast <GC_PTR> (this));
    if (base)
    {
        GC_REGISTER_FINALIZER_IGNORE_SELF (base, 0, 0, 0, 0);
    }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator new (size_t) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* Collectable::operator new (size_t size) throw (std::bad_alloc)
{

/*
If we are unable to obtain sufficient memory, we first check whether we have a
"new handler" to assist us.  If so, we call the handler (which will either
attempt to reclaim some memory or throw a std::bad_alloc exception) and re-try,
otherwise we throw the std::bad_alloc exception ourselves.  In order to keep
re-trying, we must place the memory allocation code within an infinite loop.
*/

    for (; ; )
    {

/*
Allocate the requested memory and mark it as being collectable - this memory
will be freed automatically by the collector.

Incidentally, size cannot be negative as it is an unsigned integer value.  If
size is 0, then we ought to still get a pointer to, er, nothing.

If we obtain the memory that we require, return a pointer to it.
*/

        void* memPtr = GC_MALLOC (size);
        if (memPtr)
        {
            return memPtr;
        }

/*
If we made it this far, then we do not have sufficient virtual memory to
satisfy the caller's requirements.  If do not we have a "new handler" function
then throw the std::bad_alloc exception.
*/

        std::new_handler handler = std::set_new_handler (0); 
        if (!handler)
        {
            throw std::bad_alloc ();
        }

/*
OK, we have new handler defined; call it and see what it can do.

Any new handler will be defined via the standard function
"std::set_new_handler ()".

Note: This function will loop indefinitely unless the new handler throws an
exception if unable to reclaim any additional memory.  Simply doing something,
not checking whether memory was claimed, then returning will not suffice.
Also, this exception must be std::bad_alloc (or a sub-class of std::bad_alloc)
otherwise the exception will terminate the program.  
*/

        std::set_new_handler (handler);
        handler ();
    }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator delete (void*) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Collectable::operator delete (void* memPtr) throw ()
{

/*
The global delete is equivalent to this.
*/

    ::operator delete (memPtr);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator new (void*, const std::nothrow_t&) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* Collectable::operator new (size_t size, const std::nothrow_t&) throw ()
{

/*
Use the regular single operator new to allocate the memory and return a pointer
to it.
*/

    try
    {
        return Collectable::operator new (size);
    }

/*
If we caught a std::bad_alloc exception, then return 0.
*/

    catch (std::bad_alloc)
    {
        return 0;
    }
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator delete (void*, const std::nothrow_t&) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Collectable::operator delete (void* memPtr, const std::nothrow_t&) throw
()
{

/*
The global delete is equivalent to this.
*/

    ::operator delete (memPtr);
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator new [] (size_t) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* Collectable::operator new [] (size_t) throw (std::bad_alloc)
{

/*
Throw the not supported exception.
*/

    throw Facsimile::X::ArrayNewNotSupportedException ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator delete [] (void*) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Collectable::operator delete [] (void*) throw ()
{

/*
Throw the not supported exception.
*/

    throw Facsimile::X::ArrayDeleteNotSupportedException ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator new [] (size_t, const std::nothrow_t&) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void* Collectable::operator new [] (size_t, const std::nothrow_t&) throw ()
{

/*
Throw the not supported exception.
*/

    throw Facsimile::X::ArrayNewNotSupportedException ();
}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Collectable::operator delete [] (void*, const std::nothrow_t&) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

void Collectable::operator delete [] (void*, const std::nothrow_t&) throw ()
{

/*
Throw the not supported exception.
*/

    throw Facsimile::X::ArrayDeleteNotSupportedException ();
}
