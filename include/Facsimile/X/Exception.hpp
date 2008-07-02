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
%Facsimile Exception class C++ header file.

C++ header file for the Exception class, and associated elements, that are
integral members of the Facsimile::X namespace.
*/
//=============================================================================

#ifndef FACSIMILE_X_EXCEPTION_HPP_
#define FACSIMILE_X_EXCEPTION_HPP_

/*
Relevant header files.
*/

#include <cassert>
#include <unicode/unistr.h>
#include <Facsimile/Collectable.hpp>

/*
Namespace declaration.
*/

namespace Facsimile
{
    namespace X
    {

//=============================================================================
/**
Abstract base class for all %Facsimile library exceptions.

Abstract base class for all %Facsimile library exceptions, and for user
exceptions built on top of the %Facsimile library.

\note This is not a suitable direct base class for user exceptions; you must
derive user exception classes from either Facsimile::X::RuntimeException or
Facsimile::X::LogicException, or their sub-classes, instead.  Do not derive
user exception classes from Facsimile::X::InstallationException.
*/
//=============================================================================

        class Exception:
            public virtual Facsimile::Collectable
        {

/*
Facsimile exceptions are either run-time exceptions, installation exceptions or
logic exceptions; these classes must be friends of this class.
*/

            friend class LogicException;
            friend class InstallationException;
            friend class RuntimeException;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

\remarks This constructor is private so that construction is possible from
derived friend classes only.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private:
            inline Exception () throw ():
                Facsimile::Collectable ()
            {
            }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public:
            virtual ~Exception () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Report why this exception was thrown.

Explain, with as much detail as possible, why this exception was thrown.  This
explanation will be in the user's preferred language (if a translation for that
language is available), or in U.S. English (if no translation is available).

\returns An icu::UnicodeString containing information about this exception
instance.

\internal This method must be overridden by sub-classes in order to provide as
much information about the thrown exception as possible.  Error messages must
be localized to allow the possibility that they may be reported in
the user's own language.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public:
            virtual const icu::UnicodeString cause () const throw () = 0;
        };
    }
}
#endif /*FACSIMILE_X_EXCEPTION_HPP_*/
