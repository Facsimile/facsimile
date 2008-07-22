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
Facsimile::Engine namespace C++ source file.

C++ source file documenting the Facsimile::Engine namespace; this source file
does not contain any executable code.
*/
//=============================================================================

/**
\namespace Facsimile::Engine
All %Facsimile library simulation engine elements and sub-namespaces.

The Facsimile::Engine namespace stores elements that relate directly to the
discrete-event simulation engine.

\remarks Simulation engine exception elements are stored within the
Facsimile::Engine::X namespace.

\note This namespace is reserved solely for official %Facsimile project
elements.  It is recommended that you do not place your own simulation code
within this namespace to eliminate the possibility of naming conflicts with
later releases.

\internal Only classes and elements directly related to the simulation engine
should be included here.  This typically includes the main simulation class and
base classes for events and event queues only.  To facilitate new users
learning the software, please try to keep this namespace as uncluttered and as
straightforward as possible.
*/