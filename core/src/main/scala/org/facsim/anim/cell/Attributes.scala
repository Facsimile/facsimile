/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2018, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file from the org.facsim.anim.cell package.
*/

package org.facsim.anim.cell

import org.facsim.{assertNonNull, LibResource}

/**
Class representing attributes belonging to a cell.

@constructor Create new ''cell'' attributes for an associated ''cell'' element
according to the cell's flags and data stream content.

@param scene ''Cell'' scene to which the ''cell'' owning these attributes
belongs.

@param flags ''Cell'' flags associated with the ''cell'' owning these
attributes.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/

private[cell] final class Attributes(scene: CellScene, flags: CellFlags)
extends CellAttributes {

/**
Flag indicating whether attribute data is present in the cell file.
*/

  private val attributesPresent = flags.attributesPresent

/**
Flag indicating whether colors are inherited from the parent cell.

Inherited colors are stored as `None`. Non-inherited colors have `Some` cell
color value.
*/

  private val inheritColors = flags.colorsInherited

/**
@inheritdoc
*/

  private[cell] override val faceColor = readColor(CellColorType.Face)

/**
@inheritdoc
*/

  private[cell] override val edgeColor = readColor(CellColorType.Edge)

/**
@inheritdoc

If attributes are present, read the line type, otherwise use the default.
*/

  private[cell] override val lineStyle =
  if(attributesPresent) LineStyle.read(scene)
  else LineStyle.Default

/**
@inheritdoc

If attributes are present, read the line width, otherwise use the default.
*/

  private[cell] override val lineWidth =
  if(attributesPresent) LineWidth.read(scene)
  else LineWidth.default

/**
@inheritdoc

If attributes are present, read the line width, otherwise use the default.
*/

  private[cell] override val displayStyle =
  if(attributesPresent) DisplayStyle.read(scene)
  else DisplayStyle.Default

/**
@inheritdoc

If attributes are present, read the name; otherwise, this cell has no name.
*/

  private[cell] override val name =
  if(attributesPresent) {
    Some(scene.readString(value => value != "", LibResource
   ("anim.cell.Attributes.nameDesc")))
  }
  else None

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

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
  private def readColor(colorType: CellColorType.Value) = {

/*
Sanity checks.
*/

    assertNonNull(colorType)

/*
If attributes are present, then we must read them from the file - even if we're
inheriting the color from the parent ''cell''.
*/

    if(attributesPresent) {
      val color = CellColor.read(scene, colorType)

/*
If we're inheriting color, then - no matter what color code we just read - the
color is None. Otherwise, we use the color associated with the color code.
*/

      if(inheritColors) None
      else Some(color)
    }

/*
Otherwise, cell attributes are not present, so we do not read anything from the
data stream. Instead, assign None if we're inheriting color, or use the default
color otherwise.
*/

    else {
      if(inheritColors) None
      else Some(CellColor.Default)
    }
  }
}