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
Facsimile::NonCopyable class C++ header file.

C++ header file for the Facsimile::NonCopyable class, and associated elements,
that are integral members of the Facsimile namespace.
*/
//=============================================================================

/**
Include guard.
*/

#ifndef FACSIMILE_NONCOPYABLE_HPP_
#define FACSIMILE_NONCOPYABLE_HPP_

/*
Relevant header files.
*/

/*
Namespace declaration.
*/

namespace Facsimile
{

//=============================================================================
/**
Abstract base for sub-classes whose instances cannot be copied.

This is a utility class that serves to prevent sub-classes from being copied.
Specifically, sub-classes cannot be copy-constructed, nor do they have a
copy-assignment operator.  Without this class, and under certain circumstances,
the C++ compiler will automatically generate these functions.
*/
//=============================================================================

    class NonCopyable
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

\remarks This constructor is protected to prevent instantiation except through
a sub-class.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected:
        NonCopyable () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Copy constructor.

\remarks This constructor is private and is not implemented; this declaration
prevents the C++ compiler from generating a default copy constructor for
sub-classes.

\param other The instance to be copied.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private:
        NonCopyable (const NonCopyable& other) throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Destructor.

\remarks This destructor is protected to prevent clean-up except through a
sub-class.  This destructor is not flagged as virtual, because sub-classes are
not necessarily polymorphic.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected:
        ~NonCopyable () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Copy assignment operator.

\remarks This operator is private and is not implemented; this declaration
prevents the C++ compiler from generating a default copy assignment operator
for sub-classes.

\param other The instance to be copied.

\return This instance after having been assigned to.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private:
        NonCopyable& operator = (const NonCopyable& other) throw ();
    };

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NonCopyable::NonCopyable () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    inline NonCopyable::NonCopyable () throw ()
    {
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
NonCopyable::~NonCopyable () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    inline NonCopyable::~NonCopyable () throw ()
    {
    }
}
#endif /*FACSIMILE_NONCOPYABLE_HPP_*/
