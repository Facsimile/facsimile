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
Class representing attributes belonging to a cell.

@constructor Create new ''cell'' attributes for an associated ''cell'' element
according to the cell's flags and data stream content.

@param scene ''Cell'' scene to which the ''cell'' owning these attributes
belongs.

@param flags ''Cell'' flags associated with the ''cell'' owning these
attributes.

@since 0.0
*/
//=============================================================================

private [cell] final class Attributes (scene: CellScene, flags: CellFlags)
extends CellAttributes with NotNull {

/**
Flag indicating whether attribute data is present in the cell file.
*/

  private final val attributesPresent = flags.attributesPresent

/**
Flag indicating whether colors are inherited from the parent cell.

Inherited colors are stored as `None`.  Non-inherited colors have `Some` cell
color value.
*/

  private final val inheritColors = flags.colorsInherited

/*
@see [[org.facsim.anim.cell.CellAttributes!.faceColor]]
*/

  final override val faceColor = readColor (0)

/*
@see [[org.facsim.anim.cell.CellAttributes!.edgeColor]]
*/

  final override val edgeColor = readColor (1)

/*
@see [[org.facsim.anim.cell.CellAttributes!.lineStyle]]
*/

  final override val lineStyle = {

/*
If attributes are present, read the line style from the data stream.
*/

    if (attributesPresent) {
      val lineStyleCode = scene.readInt (LineStyle.verify (_), LibResource
      ("anim.cell.Attributes.lineStyleDesc", LineStyle.minValue,
      LineStyle.maxValue))
      LineStyle (lineStyleCode)
    }

/*
Otherwise, use the default line style.
*/

    else LineStyle.default
  }

/*
@see [[org.facsim.anim.cell.CellAttributes!.lineWidth]]
*/

  final override val lineWidth = {

/*
If attributes are present, read the line width from the data stream.  This must
be a value in the range 1-7.
*/

    if (attributesPresent) {
      new LineWidth (scene.readInt (LineWidth.verify (_), LibResource
      ("anim.cell.Attributes.lineWidthDesc", LineWidth.minValue,
      LineWidth.maxValue)))
    }

/*
Otherwise, use the default line style of 1.
*/

    else LineWidth.default
  }

/*
@see [[org.facsim.anim.cell.CellAttributes!.displayStyle]]
*/

  final override val displayStyle = {

/*
If attributes are present, read the display style from the data stream.
*/

    if (attributesPresent) {
      val displayStyleCode = scene.readInt (DisplayStyle.verify (_),
      LibResource ("anim.cell.Attributes.displayStyleDesc",
      DisplayStyle.minValue, DisplayStyle.maxValue))
      DisplayStyle (displayStyleCode)
    }

/*
Otherwise, use the default display style.
*/

    else DisplayStyle.default
  }

/*
@see [[org.facsim.anim.cell.CellAttributes!.name]]
*/

  final override val name = {

/*
If attributes are present, read the name from the data stream.
*/

    if (attributesPresent) {
      Some (scene.readString (value => value != "", LibResource
      ("anim.cell.Attributes.nameDesc")))
    }

/*
Otherwise, this cell has no name.
*/

    else None
  }

//-----------------------------------------------------------------------------
/**
Function to determine the required color.

The rules are not straightforward:
  - If attributes are present, then the color must be read from the ''cell''
    data stream, even if color is inherited from its parent ''cell''.
  - If color is inherited, then the resulting color will be None.
  - If color is not inherited and attributes are present, then the color will
    be as indicated in the ''cell'' data stream.
  - If color is not inherited and attributes are not present, then the default
    color will be reported.

@param colorType A simple integer encoding face color (0) or edge color (1),
which is passed as an argument to an exception description message.

@return Color if not inherited, `None` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def readColor (colorType: Int) = {

/*
If attributes are present, then we must read them from the file - even if we're
inheriting the color from the parent ''cell''.
*/

    if (attributesPresent) {
      val faceColorCode = scene.readInt (CellColor.verify (_), LibResource
      ("anim.cell.Attributes.colorDesc", colorType, CellColor.minValue,
      CellColor.maxValue))

/*
If we're inheriting color, then - no matter what color code we just read - the
color is None.  Otherwise, we use the color associated with the color code.
*/

      if (inheritColors) None
      else Some (CellColor (faceColorCode))
    }

/*
Otherwise, cell attributes are not present, so we do not read anything from the
data stream.  Instead, assign None if we're inheriting color, or use the
default color otherwise.
*/

    else {
      if (inheritColors) None
      else Some (CellColor.default)
    }
  }
}