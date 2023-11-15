/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2023, Michael J Allen.

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
import scala.annotation.tailrec
import scalafx.scene.Group

/**
Abstract class for all ''cell text list'' primitives (except for ''cell text''
primitives).

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/TextLists.html
Test Lists]] for further information.

@constructor Construct a new basic text list primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@param textType Type of text represented by this instance.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/TextLists.html
Test Lists]] for further information.
*/

private[cell] abstract class TextList(scene: CellScene, parent: Option[Set],
textType: Text.Value)
extends Cell(scene, parent) {

/*
Sanity checks.
*/

  assertNonNull(textType)

/*
Read the text lists.

Each text list is prefixed by a set of translation co-ordinates.
*/

  private val textList = TextList.read(scene, textType)

/**
@inheritdoc

@note We currently render ''World'' and ''Unrotate'' text types as World text,
and do not render ''Screen'' text.
*/
  private[cell] final override def toNode = {
    val thisTextList = this

/*
We render World text lists correctly, but, due to current limitations in the
capabilities of JavaFX/ScalaFX, we can only render Unrotate text lists as World
Text Lists (see Issue 5). Furthermore, Screen text lists - while they are
potentially supportable at present - are currently not rendered because a
decision needs to be made about how they should be implemented (see Issue 4).
*/

    textType match {
      case Text.Screen => new Group {}
      case _ => new Group {

/*
If this cell has a name, then use it as an ID.
*/

        id = name.orNull

/*
Apply the required transformations to the node.
*/

        transforms = cellTransforms

/*
Add the text points to the group as nodes.
*/

        children = textList.map(_.toTextNode(thisTextList))
      }
    }
  }
}

/**
TextList companion object.
*/

private object TextList {

/**
Read text list data from the stream.

@param scene Reference to the CellScene of which this cell is a part.

@param textType Type of text represented by the associates TextList instance.

@return Text list read.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
  private def read(scene: CellScene, textType: Text.Value) = {

/*
Sanity checks.
*/

    assertNonNull(scene)
    assertNonNull(textType)

/*
Helper function to read the next text point from the data stream.

Note that, due to the need for tail recursion, the list is built in reverse.
*/

    @tailrec
    def readPoint(count: Int, points: List[TextListPoint]): List
   [TextListPoint] = {
      if(count == 0) points
      else {
        val point = new TextListPoint(scene, textType)
        readPoint(count - 1, point :: points)
      }
    }

/*
Read the number of points from the data stream. This value must be at least 1.
*/

    val numPoints = scene.readInt(_ > 0, LibResource
   ("anim.cell.TextList.read", textType.id))

/*
Return the list of points read, reversing the list order so that they are put
back into the original order.
*/

    readPoint(numPoints, Nil).reverse
  }
}