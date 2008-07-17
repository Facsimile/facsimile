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
%Facsimile SingletonException class C++ header file.

C++ header file for the SingletonException class, and associated elements, that
are integral members of the Facsimile::X namespace.
*/
//=============================================================================

#ifndef FACSIMILE_X_SINGLETONEXCEPTION_HPP_
#define FACSIMILE_X_SINGLETONEXCEPTION_HPP_

/*
Relevant header files.
*/

#include <unicode/unistr.h> 
#include <Facsimile/X/LogicException.hpp>

/*
Namespace declaration.
*/

namespace Facsimile
{
    namespace X
    {

//=============================================================================
/**
Thrown if an attempt is made to instantiate a Facsimile::Singleton<T> class.

Facsimile::Singleton<T> sub-classes can only be instantiated, using lazy
initialization, through the Facsimile::Singleton<T>::getInstance() function.
Attempts to manually create such instances will result in this exception.

\remarks Singleton exceptions should generally not be caught by exception
handling code.  Instead, it is preferable to highlight the error by allowing
such exceptions to terminate the application, so that the root cause can be
identified and fixed.  Singleton exceptions should not be thrown by a
well-behaved application.  Note that the %Facsimile test suite must catch
singleton exceptions in order to validate that they are thrown appropriately.

\see Facsimile::Singleton<T>
*/
//=============================================================================

        class SingletonException:
            public LogicException
        {

/**
Name of the Facsimile::Singleton<T> sub-class that was mistreated.

\note The class name is not standardized and each compiler may produce a
different result.  Therefore, we treat the type name as a string describing
which class was affected, and do not rely on any particular formatting.
*/

        private:
            const icu::UnicodeString typeName;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Constructor.

Construct the exception, storing the type of singleton affected.

\param singletonName The type information of the singleton that was mistreated.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public:
            SingletonException (const icu::UnicodeString& singletonName) throw
            ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public:
            virtual ~SingletonException () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
Override Exception::cause.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public:
            const icu::UnicodeString cause () const throw ();
        };

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
SingletonException::SingletonException (const std::type_info&) implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        SingletonException::SingletonException (const icu::UnicodeString&
        singletonName) throw ():
            typeName (singletonName)
        {
        }
    }
}
#endif /*FACSIMILE_X_SINGLETONEXCEPTION_HPP_*/
