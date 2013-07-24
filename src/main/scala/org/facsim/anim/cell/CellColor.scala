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
Cell color name enumeration.

Encodes ''[[http://www.automod.com/ AutoMod®]]'' color codes and maps them to
the corresponding colors.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Colors.html
Face & Edge Colors]]

@since 0.0
*/
//=============================================================================

private [cell] object CellColor extends Enumeration {

/**
Black, having the ''cell'' color code 0.
*/

  final val Black = Value

/**
Red, having the ''cell'' color code 1.
*/

  final val Red = Value

/**
Green, having the ''cell'' color code 2.
*/

  final val Green = Value

/**
Yellow, having the ''cell'' color code 3.
*/

  final val Yellow = Value

/**
Blue, having the ''cell'' color code 4.
*/

  final val Blue = Value

/**
Magenta, having the ''cell'' color code 5.
*/

  final val Magenta = Value

/**
Cyan, having the ''cell'' color code 6.
*/

  final val Cyan = Value

/**
White, having the ''cell'' color code 7.
*/

  final val White = Value

/**
Light gray, having the ''cell'' color code 8.
*/

  final val LightGray = Value

/**
Dark gray, having the ''cell'' color code 9.
*/

  final val DarkGray = Value

/**
Brown, having the ''cell'' color code 10.
*/

  final val Brown = Value

/**
Purple, having the ''cell'' color code 11.
*/

  final val Purple = Value

/**
Orange, having the ''cell'' color code 12.
*/

  final val Orange = Value

/**
Light blue, having the ''cell'' color code 13.
*/

  final val LightBlue = Value

/**
Light green, having the ''cell'' color code 14.
*/

  final val LightGreen = Value

/**
Light yellow, having the ''cell'' color code 15.
*/

  final val LightYellow = Value

/**
Default color, which is used if no other color is available.
*/

  final val default = Red

/**
Minimum color code value.
*/

  final val minValue = 0

/**
Maximum color code value.
*/

  final val maxValue = maxId - 1

//-----------------------------------------------------------------------------
/**
Verify a color code.

@param colorCode Code for the color to be verified.

@return `true` if the code maps to a valid color, `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def verify (colorCode: Int) = (colorCode >= minValue && colorCode <=
  maxValue)

//-----------------------------------------------------------------------------
/**
Read color from ''cell'' data stream.

@param scene Scene from which the color is to be read.

@param colorType Code indicating the type of color being read: 0 = face, 1 =
edge

@return Color read, if valid.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Colors.html
Face & Edge Colors]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def read (scene: CellScene, colorType: Int) = {

/*
Read the color code from the data stream.
*/

    val code = scene.readInt (verify (_), LibResource
    ("anim.cell.CellColor.read", colorType, minValue, maxValue))

/*
Convert to a cell color and return.
*/

    CellColor (code)
  }
}