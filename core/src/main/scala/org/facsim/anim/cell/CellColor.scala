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
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import org.facsim.LibResource
import scalafx.scene.paint.Color
import scalafx.scene.paint.PhongMaterial

//=============================================================================
/**
Cell color name enumeration.

Encodes ''[[http://www.automod.com/ AutoMod®]]'' color codes and maps them to
the corresponding ''ScalaFX'' colors and materials.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Colors.html
Face & Edge Colors]]

@since 0.0
*/
//=============================================================================

object CellColor
extends Enumeration {

/**
Vector associating each ''cell'' color with the corresponding ''ScalaFX''
color.
*/

  private val cellToColor = Vector (
    Color.BLACK,
    Color.RED,
    Color.GREEN,
    Color.YELLOW,
    Color.BLUE,
    Color.MAGENTA,
    Color.CYAN,
    Color.WHITE,
    Color.LIGHTGRAY,
    Color.DARKGRAY,
    Color.BROWN,
    Color.LIGHTBLUE,
    Color.PURPLE,
    Color.ORANGE,
    Color.LIGHTGREEN,
    Color.LIGHTYELLOW
  )

/**
Vector of materials that employ cell colors as ''diffuse'' colors.

In each case the ''specular'' color is set to white.

@note Use of this vector allows us to keep the number of materials used within
cell files small.
*/

  private val cellToMaterial: Vector [PhongMaterial] = cellToColor.map {
    color =>
    new PhongMaterial {
      diffuseColor = color
      specularColor = Color.WHITE
      specularPower = 1.0
    }
  }

/**
Black, having the ''cell'' color code 0.
*/

  val black = Value

/**
Red, having the ''cell'' color code 1.
*/

  val red = Value

/**
Green, having the ''cell'' color code 2.
*/

  val green = Value

/**
Yellow, having the ''cell'' color code 3.
*/

  val yellow = Value

/**
Blue, having the ''cell'' color code 4.
*/

  val blue = Value

/**
Magenta, having the ''cell'' color code 5.
*/

  val magenta = Value

/**
Cyan, having the ''cell'' color code 6.
*/

  val cyan = Value

/**
White, having the ''cell'' color code 7.
*/

  val white = Value

/**
Light gray, having the ''cell'' color code 8.
*/

  val lightGray = Value

/**
Dark gray, having the ''cell'' color code 9.
*/

  val darkGray = Value

/**
Brown, having the ''cell'' color code 10.
*/

  val brown = Value

/**
Light blue, having the ''cell'' color code 11.
*/

  val lightBlue = Value

/**
Purple, having the ''cell'' color code 12.
*/

  val purple = Value

/**
Orange, having the ''cell'' color code 13.
*/

  val orange = Value

/**
Light green, having the ''cell'' color code 14.
*/

  val lightGreen = Value

/**
Light yellow, having the ''cell'' color code 15.
*/

  val lightYellow = Value

/**
Default color, which is used if an explicit color is not specified.

@note This is a material instance, not a color.
*/

  val default = red

/**
Minimum color code value.
*/

  private [cell] val minValue = 0

/**
Maximum color code value.
*/

  private [cell] val maxValue = maxId - 1

//-----------------------------------------------------------------------------
/**
Conversion of ''cell'' color to ''ScalaFX'' color.

@note This could be made an implicit function, but that might encourage the use
of ''cell'' colors in regular code, when ideally we want to bury them.

@param ''Cell'' color value to be converted.

@return Corresponding ''ScalaFX'' color.

@since 0.0 
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] def toColor (color: CellColor.Value) = cellToColor (color.id)

//-----------------------------------------------------------------------------
/**
Conversion of ''cell'' color to ''ScalaFX'' material.

@note This could be made an implicit function, but that might encourage the use
of ''cell'' colors in regular code, when ideally we want to bury them.

@param ''Cell'' color value to be converted.

@return Corresponding ''ScalaFX'' material.

@since 0.0 
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] def toMaterial (color: CellColor.Value) =
  cellToMaterial (color.id)

//-----------------------------------------------------------------------------
/**
Verify a color code.

@param colorCode Code for the color to be verified.

@return `true` if the code maps to a valid color, `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def verify (colorCode: Int) = (colorCode >= minValue &&
  colorCode <= maxValue)

//-----------------------------------------------------------------------------
/**
Read color from ''cell'' data stream.

@param scene Scene from which the color is to be read.

@param colorType Type of color value to be read.

@return Cell color read, if valid.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Colors.html
Face & Edge Colors]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def read (scene: CellScene, colorType: CellColorType.Value) =
  {

/*
Read the color code from the data stream.
*/

    val code = scene.readInt (verify (_), LibResource
    ("anim.cell.CellColor.read", colorType.id, minValue, maxValue))

/*
Convert to cell color and return.
*/

    CellColor (code)
  }
}