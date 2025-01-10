/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2025, Michael J Allen.

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
import scalafx.scene.Group
import scalafx.scene.text.{Text => SFXText}

/**
Abstract class for all _cell text_ primitives (except for _cell text list_
primitives).

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Text.html
Text]] for further information.

@constructor Construct a new basic text primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@param textType Type of text represented by this instance.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Text.html
Text]] for further information.
*/

private[cell] abstract class Text(scene: CellScene, parent: Option[Set],
textType: Text.Value)
extends Cell(scene, parent) {

/*
Sanity checks.
*/

  assertNonNull(textType)

/**
Read the text from the stream.
*/

  private final val textValue = scene.readText(LibResource
 ("anim.cell.Text.read", textType.id))

/**
@inheritdoc

@note We currently render _World_ and _Unrotate_ text types as World text,
and do not render _Screen_ text.
*/
  private[cell] final override def toNode = {

/*
We render World text correctly, but, due to current limitations in the
capabilities of JavaFX/ScalaFX, we can only render Unrotate text as World Text
(see Issue 5). Furthermore, Screen text - while it is potentially supportable
at present - is currently not rendered because a decision needs to be made
about how it should be implemented (see Issue 4).
*/

    textType match {
      case Text.Screen => new Group {}
      case _ => new SFXText {

/*
If this cell has a name, then use it as an ID.
*/

        id = name.orNull

/*
Apply the required transformations to the node.
*/

        transforms = cellTransforms

/*
Specify the color for this text.
*/

        stroke = cellPaint

/*
The text to be displayed.
*/

        text = textValue
      }
    }
  }
}

/**
Text companion object and text type enumeration.

This type enumeration is used for [[org.facsim.anim.cell.TestList!]]s as well.
*/

private[cell] object Text
extends Enumeration {

/**
Screen text type.

Screen text is mapped to the viewing screen.
*/

  private[cell] val Screen = Value

/**
Unrotate text type.

Unrotate text is always displayed facing the viewer.
*/

  private[cell] val Unrotate = Value

/**
World text type.

World text is displayed _in situ_ as part of the 3D scene.
*/

  private[cell] val World = Value
}