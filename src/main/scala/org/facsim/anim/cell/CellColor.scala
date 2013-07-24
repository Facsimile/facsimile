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

  val Black = Value

/**
Red, having the ''cell'' color code 1.
*/

  val Red = Value

/**
Green, having the ''cell'' color code 2.
*/

  val Green = Value

/**
Yellow, having the ''cell'' color code 3.
*/

  val Yellow = Value

/**
Blue, having the ''cell'' color code 4.
*/

  val Blue = Value

/**
Magenta, having the ''cell'' color code 5.
*/

  val Magenta = Value

/**
Cyan, having the ''cell'' color code 6.
*/

  val Cyan = Value

/**
White, having the ''cell'' color code 7.
*/

  val White = Value

/**
Light gray, having the ''cell'' color code 8.
*/

  val LightGray = Value

/**
Dark gray, having the ''cell'' color code 9.
*/

  val DarkGray = Value

/**
Brown, having the ''cell'' color code 10.
*/

  val Brown = Value

/**
Purple, having the ''cell'' color code 11.
*/

  val Purple = Value

/**
Orange, having the ''cell'' color code 12.
*/

  val Orange = Value

/**
Light blue, having the ''cell'' color code 13.
*/

  val LightBlue = Value

/**
Light green, having the ''cell'' color code 14.
*/

  val LightGreen = Value

/**
Light yellow, having the ''cell'' color code 15.
*/

  val LightYellow = Value

/**
Default color, which is used if no other color is available.
*/

  val default = Red

//-----------------------------------------------------------------------------
/**
Verify a color code.

@param colorCode Code for the color to be verified.

@return `true` if the code maps to a valid color, `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def verify (colorCode: Int) = (colorCode >= 0 && colorCode < maxId)
}