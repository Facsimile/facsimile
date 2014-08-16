/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim package.
*/
//=============================================================================

package org.facsim

//=============================================================================
/**
Application trait.

Provide common information and functionality for applications built with the
''Facsimile'' library.

`App` employs the common ''Scala cake pattern'' to avoid hard-coding
dependencies and to instead employ
''[[http://en.wikipedia.org/wiki/Dependency_injection dependency injection]]''
to provide access to facilities that may not be available at compile-time. In
order to compile, client applications '''must''' mix-in an
[[org.facsim.AppInformation]]-implementation instance when creating an
application ''object'' from this trait.

The application may utilize a graphical user interface, or be driven from the
command line, or both, depending upon the registered behavior.

@since 0.0
*/
//=============================================================================

trait App {
  self: AppInformation =>
}
