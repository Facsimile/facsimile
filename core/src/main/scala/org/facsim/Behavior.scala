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
Application behavior trait.

Implementing classes provide functionality to applications developed with the
''Facsimile'' library.

Behavior instances are activated when applied to the [[org.facsim.App$]]
object.

@since 0.0
*/
//=============================================================================

trait Behavior extends AppBehaviorInterface with NotNull {


//-----------------------------------------------------------------------------
/**
Raise [[java.util.NoSuchElementException!]] for missing field.

@param field Name of field for which information is missing.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def raiseException (field: String): Nothing = throw new
  NoSuchElementException (LibResource ("Behavior.NoSuchElement", field))

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def title: String = raiseException ("title")

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def organization: String = raiseException ("organization")

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def inceptionDate: DateTime = raiseException ("inceptionDate")

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def releaseDate: DateTime = raiseException ("releaseDate")

//-----------------------------------------------------------------------------
/**
@inheritdoc

@note The copyright message is built from the
[[org.facsim.Behavior!.organization]], [[org.facsim.Behavior!.inceptionDate]]
and [[org.facsim.Behavior!.releaseDate]] functions.  If any of those fields are
missing, then a [[java.util.NoSuchElementException]] will result.
*/
//-----------------------------------------------------------------------------

  final override def copyright: String = {

/*
If the organization name ends in a period, then remove it.
*/

    val org = organization
    val orgAdj = if (org.last == '.') org.init else org

/*
Format and retrieve this application's copyright string.
*/

    if (inceptionDate.getYear () < releaseDate.getYear ())
    LibResource ("Behavior.CopyrightRange", orgAdj, inceptionDate.toDate (),
    releaseDate.toDate ())
    else LibResource ("Behavior.CopyrightBasic", orgAdj, inceptionDate.toDate
    ())
  }

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  override def version: Version = raiseException ("version")
}