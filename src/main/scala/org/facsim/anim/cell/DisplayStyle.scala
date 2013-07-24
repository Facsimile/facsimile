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
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import org.facsim.LibResource

//=============================================================================
/**
Display style enumeration.

Encodes ''[[http://www.automod.com/ AutoMod®]]'' display style codes and maps
them to the corresponding display styles.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/DisplayStyle.html
Display Styles]]

@since 0.0
*/
//=============================================================================

private [cell] object DisplayStyle extends Enumeration {

/**
Wireframe, having the ''cell'' display style 0.
*/

  final val Wireframe = Value

/**
Solid, having the ''cell'' display style 1.
*/

  final val Solid = Value

/**
Transparent 1, having the ''cell'' display style 2.  Almost solid.
*/

  final val Transparent1 = Value

/**
Transparent 2, having the ''cell'' display style 3.
*/

  final val Transparent2 = Value

/**
Transparent 3, having the ''cell'' display style 4.
*/

  final val Transparent3 = Value

/**
Transparent 4, having the ''cell'' display style 5.
*/

  final val Transparent4 = Value

/**
Transparent 5, having the ''cell'' display style 6.
*/

  final val Transparent5 = Value

/**
Transparent 6, having the ''cell'' display style 7.
*/

  final val Transparent6 = Value

/**
Transparent 7, having the ''cell'' display style 8.
*/

  final val Transparent7 = Value

/**
Transparent 8, having the ''cell'' display style 9.
*/

  final val Transparent8 = Value

/**
Transparent 9, having the ''cell'' display style 10.
*/

  final val Transparent9 = Value

/**
Transparent 10, having the ''cell'' display style 11.
*/

  final val Transparent10 = Value

/**
Transparent 11, having the ''cell'' display style 12.
*/

  final val Transparent11 = Value

/**
Transparent 12, having the ''cell'' display style 13.
*/

  final val Transparent12 = Value

/**
Transparent 13, having the ''cell'' display style 14.
*/

  final val Transparent13 = Value

/**
Transparent 14, having the ''cell'' display style 15.
*/

  final val Transparent14 = Value

/**
Transparent 15, having the ''cell'' display style 16.  Almost invisible.
*/

  final val Transparent15 = Value

/**
Default display style, which is used if an explicit style is not available.
*/

  final val default = Solid

/**
Minimum display style code value.
*/

  final val minValue = 0

/**
Maximum display style code value.
*/

  final val maxValue = maxId - 1

//-----------------------------------------------------------------------------
/**
Verify a display style code.

@param displayStyleCode Code for the display style to be verified.

@return `true` if the code maps to a valid display style, `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def verify (displayStyleCode: Int) =
  (displayStyleCode >= minValue && displayStyleCode <= maxValue)

//-----------------------------------------------------------------------------
/**
Read display style from ''cell'' data stream.

@param scene Scene from which the display style is to be read.

@return Display style read, if valid.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/DisplayStyle.html
Display Styles]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def read (scene: CellScene) = {

/*
Read the display style code from the data stream.
*/

    val code = scene.readInt (verify (_), LibResource
    ("anim.cell.DisplayStyle.read", minValue, maxValue))

/*
Convert to a display style and return.
*/

    DisplayStyle (code)
  }
}
