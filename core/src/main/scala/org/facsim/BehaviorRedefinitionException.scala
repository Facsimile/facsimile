/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

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
Thrown if an attempt to apply a second [[org.facsim.Behavior!]] to
[[org.facsim.App$]] is made.

@note The application will retain its existing behavior.

@constructor Create behavior redefinition exception from existing and redefined
behavior instances.

@param existing Application's current applied behavior.

@param replacement Attempted replacement behavior.

@since 0.0
*/
//=============================================================================

final class BehaviorRedefinitionException private [facsim] (existing: Behavior,
replacement: Behavior) extends RuntimeException () {

//-----------------------------------------------------------------------------
/*
@see [[java.lang.Throwable!.getMessage()]]
*/
//-----------------------------------------------------------------------------

  final override def getMessage = LibResource ("BehaviorRedefinition",
  existing, replacement)
}