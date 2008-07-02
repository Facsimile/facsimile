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
%Facsimile NotSupportedException class C++ header file.

C++ header file for the NotSupportedException class, and associated elements,
that are integral members of the Facsimile::X namespace.
*/
//=============================================================================

#ifndef FACSIMILE_X_NOTSUPPORTEDEXCEPTION_HPP_
#define FACSIMILE_X_NOTSUPPORTEDEXCEPTION_HPP_

/*
Relevant header files.
*/

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
Abstract base class for all %Facsimile library unsupported feature exceptions.

This type of exception will be thrown if a specific application of a feature is
not supported by the %Facsimile library.

\remarks Unsupported feature exceptions should generally not be caught by
exception handling code.  Instead, it is preferable to highlight the error by
allowing such exceptions to terminate the application, so that the root cause
can be identified and fixed.  Unsupported feature exceptions should not be
thrown by a well-behaved application.  Note that the %Facsimile test suite must
catch unsupported feature exceptions in order to validate that they are thrown
appropriately.

\see Facsimile::X::LogicException
*/
//=============================================================================

        class NotSupportedException:
            public LogicException
        {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

\remarks This constructor is protected so that construction is possible from
derived classes only.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected:
            inline NotSupportedException () throw ():
                LogicException ()
            {
            }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public:
            virtual ~NotSupportedException () throw ();
        };
    }
}
#endif /*FACSIMILE_X_NOTSUPPORTEDEXCEPTION_HPP_*/
