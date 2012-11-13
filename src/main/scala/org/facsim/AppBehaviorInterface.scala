/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

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
Ensures a common interface for [[org.facsim.App]] and [[org.facsim.Behavior]].

All function documentation should be defined here, as it will be inherited by
implementing classes and traits, ensuring consistency throughout.  Functions
should be written as though present in both `App` and `Behavior`, with any need
to customize inherited documentation minimized.

This class should '''not''' implement any functionality.

@since 0.0
*/
//=============================================================================

private [facsim] trait AppBehaviorInterface {

//-----------------------------------------------------------------------------
/**
Report the application's official title.

@return Application's title.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def title: String

//-----------------------------------------------------------------------------
/**
Report name of the organization responsible for developing this software.

@return Application's owner name.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def organization: String

//-----------------------------------------------------------------------------
/**
Report date upon which development of this application commenced.

@return Application's commencement date.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def inceptionDate: DateTime

//-----------------------------------------------------------------------------
/**
Report release date for this version of the application.

@return Application's version release date.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def releaseDate: DateTime

//-----------------------------------------------------------------------------
/**
Report this application's copyright message.

@return Application's copyright message.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def copyright: String

//-----------------------------------------------------------------------------
/**
Report application's current version.

@return Application's current version.

@throws java.util.NoSuchElementException if this information is not available.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def version: Version
}