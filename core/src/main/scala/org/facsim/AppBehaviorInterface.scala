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

import org.facsim.util.Version
import org.joda.time.DateTime

//=============================================================================
/**
Ensures a common interface for [[org.facsim.App$]] and
[[org.facsim.Behavior!]].

All function documentation should be defined here, as it will be inherited by
implementing classes and traits, ensuring consistency throughout. Functions
should be written as though present in both `App` and `Behavior`, with any need
to customize inherited documentation minimized.

@since 0.0
*/
//=============================================================================

private [facsim] trait AppBehaviorInterface {

//-----------------------------------------------------------------------------
/**
Report the executing application's official title.

@return Executing application's title.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def title: String

//-----------------------------------------------------------------------------
/**
Report name of the organization responsible for developing the executing
application.

@return Executing application's owner name.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def organization: String

//-----------------------------------------------------------------------------
/**
Report date upon which development of the executing application commenced.

@return Executing application's commencement date.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def inceptionDate: DateTime

//-----------------------------------------------------------------------------
/**
Report release date of this version of the executing application.

@return Release data of executing application's current version.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def releaseDate: DateTime

//-----------------------------------------------------------------------------
/**
Report the executing application's copyright message.

@return Executing application's copyright message.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def copyright: String

//-----------------------------------------------------------------------------
/**
Report executing application's version.

@return Executing application's version.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def version: Version
}