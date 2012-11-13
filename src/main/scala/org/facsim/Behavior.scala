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

import org.joda.time.DateTime

//=============================================================================
/**
Application behavior trait.

Implementing classes provide functionality to applications developed with the
''Facsimile'' library.

Behavior instances are activated when applied to the [[org.facsim.App]] object.

@since 0.0
*/
//=============================================================================

trait Behavior extends AppBehaviorInterface {

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def title: String = throw new NoSuchElementException ("title")

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def organization: String = throw new
  NoSuchElementException ("organization")

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def inceptionDate: DateTime = throw new
  NoSuchElementException ("inceptionDate")

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def releaseDate: DateTime = throw new
  NoSuchElementException ("releaseDate")

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  final override def copyright: String = if (inceptionDate.getYear () <
  releaseDate.getYear ()) LibResource.format (".Behavior.CopyrightRange",
  organization, inceptionDate.toDate (), releaseDate.toDate ())
  else LibResource.format (".Behavior.CopyrightBasic", organization,
  inceptionDate.toDate ())

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def version: Version = throw new NoSuchElementException ("version")
}