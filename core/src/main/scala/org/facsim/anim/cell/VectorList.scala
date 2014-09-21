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

import org.facsim.{assertNonNull, LibResource}
import scala.annotation.tailrec
import scalafx.scene.Group

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell vector list''
primitives.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/VectorLists.html
Vector Lists]] for further information.

@todo JavaFX 8 currently doesn't appear capable of handling 3D polylines. When
it becomes possible to do this, fully implement this class. For now, we just
read the associated data from the cell data stream and effectively ignore the
data read.

@constructor Construct a new vector list primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/VectorLists.html
Vector Lists]] for further information.
*/
//=============================================================================

private [cell] final class VectorList (scene: CellScene, parent: Option [Set])
extends Cell (scene, parent) {

/*
Read the list of pointer from the cell data stream.
*/

  VectorList.read (scene)

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Cell!.toNode]]

Vector List elements currently cannot be rendered. See Issue 3 for further
details.
*/
//-----------------------------------------------------------------------------

  private [cell] override def toNode = new Group ()
}

//=============================================================================
/**
VectorList companion object.
*/
//=============================================================================

private object VectorList {

//-----------------------------------------------------------------------------
/**
Read vector list data from the stream.

@param scene Reference to the CellScene of which this cell is a part.

@return List of move/draw points. First point must be a move point.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
//-----------------------------------------------------------------------------

  private def read (scene: CellScene) = {

/*
Sanity checks.
*/

    assertNonNull (scene)

/*
Helper function to read the next point from the data stream.

For now, we just discard the points read. In future, when there's something to
do with the data read, we'll return a list of points (see the Polyhedra class
for how that might be done).
*/

    @tailrec
    def readPoint (count: Int, isFirst: Boolean): Unit = {
      if (count > 0) {
        new VectorListPoint (scene, isFirst)
        readPoint (count - 1, false)
      }
    }

/*
Read the number of points from the data stream. This value must be at least 2.
*/

    val numPoints = scene.readInt (_ > 1, LibResource
    ("anim.cell.VectorList.read"))

/*
Read in all of the points. The first point must be a move point.
*/

    readPoint (numPoints, true)
  }
}