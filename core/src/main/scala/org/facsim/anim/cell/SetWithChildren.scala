/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2015, Michael J Allen.

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
import scala.annotation.tailrec

//=============================================================================
/**
Abstract class for primitives that have children.

@see [[http://facsim.org/Documentation/Resources/Sets.htmls Sets]] for further
information.

@constructor Construct a new set primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Sets.html
Sets]]
*/
//=============================================================================

private [cell] abstract class SetWithChildren (scene: CellScene,
parent: Option [Set])
extends Set (scene, parent) {

/**
Determine the number of children and read them in.
*/

  private val childCells = readChildren ()

//-----------------------------------------------------------------------------
/**
Read in the children of the set and return them.

@return List of children belonging to the set. This may be empty if no children
are defined.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Sets.html
Sets]]
*/
//-----------------------------------------------------------------------------

  private final def readChildren () = {

/**
Helper function to read the next child from the list.

NOTE: The list will contain children defined in reserve order.
*/

    @tailrec
    def readChild (count: Int, children: List [Cell]): List [Cell] = {
      if (count == 0) children
      else readChild (count - 1, scene.readNextCell (Some (this), false) ::
      children)
    }

/*
Read in the number of children from the data stream. This must be a value that
is >= 0.
*/

    val numChildren = scene.readInt (_ >= 0, LibResource
    ("anim.cell.Set.readChildren"))

/*
Build the list of children and return it.
*/

    readChild (numChildren, Nil).reverse
  }

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Set!.getChildren]]
*/
//-----------------------------------------------------------------------------

  protected [cell] override def getChildren = childCells
}