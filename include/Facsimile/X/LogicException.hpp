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
%Facsimile LogicException class C++ header file.

C++ header file for the LogicException class, and associated elements, that are
integral members of the Facsimile::X namespace.
*/
//=============================================================================

#ifndef FACSIMILE_X_LOGICEXCEPTION_HPP_
#define FACSIMILE_X_LOGICEXCEPTION_HPP_

/*
Relevant header files.
*/

#include <Facsimile/X/Exception.hpp>

/*
Namespace declaration.
*/

namespace Facsimile
{
    namespace X
    {

//=============================================================================
/**
Abstract base class for all %Facsimile library logic exceptions.

This is the base class for all %Facsimile library logic exceptions.  \em Logic
exceptions, unlike run-time exceptions, are typically attributable to
programmer error; they can and should be avoided and should not arise in
well-behaved applications.  An example of a logic error is calling a function
with an illegal argument value, such as log (0).

\remarks Logic exceptions should generally not be caught by exception handling
code.  Instead, it is preferable to highlight the error, by allowing such
exceptions to terminate the application, so that the root cause can be
identified and fixed.  Note that the %Facsimile test suite must catch logic
exceptions in order to validate that they are thrown appropriately.

\see Facsimile::X::RuntimeException

\see Facsimile::X::InstallationException
*/
//=============================================================================

        class LogicException:
            public Exception
        {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Default constructor.

\remarks This constructor is protected so that construction is possible from
derived classes only.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected:
            LogicException () throw ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Virtual destructor.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public:
            virtual ~LogicException () throw ();
        };

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
LogicException::LogicException () implementation.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        inline LogicException::LogicException () throw ():
            Exception ()
        {
        }
    }
}
#endif /*FACSIMILE_X_LOGICEXCEPTION_HPP_*/
