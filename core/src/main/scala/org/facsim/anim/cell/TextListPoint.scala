/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2019, Michael J Allen.

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
import scalafx.scene.text.{Text => SFXText}

/**
Class representing a 3D point with move/draw flag.

@todo Do something with the data read when there's an opportunity to do so.
Refer to [[org.facsim.anim.cell.VectorList!]] for further information.

@constructor Construct a new decorated point from the cell data stream.

@param scene Reference to the CellScene of which this point is a part.

@param textType Type of text represented by this instance.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/

private[cell] final class TextListPoint(scene: CellScene,
textType: Text.Value)
extends Point(scene, Point.TextList) {

/*
Sanity checks.
*/

  assertNonNull(textType)

/**
Read the text from the stream.
*/

  private val textValue = scene.readText(LibResource
 ("anim.cell.TextListPoint.read", textType.id))

/**
Convert this text list point to a text node.

@param textList Text list ''cell'' to which this point belongs.

@return A ''ScalaFX'' text node to be added to the ''cell'' scene.
*/
  private[cell] def toTextNode(textList: TextList) = {

/*
Sanity checks.
*/

    assertNonNull(textList)

/*
Create the new text element.
*/

    new SFXText {
      transforms = Seq(point.toTranslate)
      stroke = textList.cellPaint
      text = textValue
    }
  }
}