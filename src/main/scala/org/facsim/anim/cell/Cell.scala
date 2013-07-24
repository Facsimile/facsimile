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

import java.awt.Color
import javax.media.j3d.Appearance
import javax.media.j3d.LineAttributes
import javax.media.j3d.Material
import javax.media.j3d.TransparencyAttributes
import javax.vecmath.Color3f
import com.sun.j3d.loaders.IncorrectFormatException
import com.sun.j3d.loaders.ParsingErrorException
import org.facsim.LibResource
import org.facsim.io.TextReader

//=============================================================================
/**
Abstract base class for all ''[[http://www.automod.com/ AutoMod]] cell''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile AutoMod Cell
File Format]] for further information.

@constructor Construct a new basic cell primitive.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive.  If this value is `None`, then
this cell is the scene's root cell.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//=============================================================================

private [cell] abstract class Cell (scene: CellScene, private val parent:
Option [Set]) extends CellAttributes with NotNull {

/**
Cell flags.

These flags determine which fields, if any, are included in the cell's
definition in the cell data stream, and also which characteristics are
inherited from the cell's parent.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Flags.html
AutoMod Cell Flags]]
*/

  private val flags = new CellFlags (scene.readInt (LibResource
  ("anim.cell.Cell.flagDesc")))

/*
Process bounding box data, if present.
*/

  Cell.processBoundingBox (flags, scene)

/**
Cell attributes.

Determine the cell's attribute values.
*/

  private val attrs = new Attributes (scene, flags)
}

//=============================================================================
/**
Cell companion object.

@since 0.0
*/
//=============================================================================

private object Cell {

//-----------------------------------------------------------------------------
/**
Read it bounding box data and verify it makes sense.

If bounding box date is present, we must read it, but will ignore it.  However,
note that we must verify the data read (in terms of the consistency of the
specified minimum & maximum values, rather than whether the bounding box is
appropriately sized).  This is to ensure that we stay in sync with cell data
and so can report meaningful errors to the user.

@note This function must only be called if the current cell has bounding box
data present, and must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/BoundingBox.html
Cell Bounding Box]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def processBoundingBox (flags: CellFlags, scene: CellScene): Unit = {

/*
Read the data only if bounding box data is present.
*/

    if (flags.boundingBoxPresent) {

/*
Read in the minimum and maximum X co-ordinate values. The maximum co-ordinate
must be greater than the minimum co-ordinate.
*/

      val minX = scene.readDouble (LibResource
      ("anim.cell.Cell.readBoundingBox.minDesc", "X"))
      scene.readDouble (_ >= minX, LibResource
      ("anim.cell.Cell.readBoundingBox.maxDesc", "X", minX))

/*
Read in the minimum and maximum Y co-ordinate values. The maximum co-ordinate
must be greater than the minimum co-ordinate.
*/

      val minY = scene.readDouble (LibResource
      ("anim.cell.Cell.readBoundingBox.minDesc", "Y"))
      scene.readDouble (_ >= minY, LibResource
      ("anim.cell.Cell.readBoundingBox.maxDesc", "Y", minY))

/*
Read in the minimum and maximum Z co-ordinate values. The maximum co-ordinate
must be greater than the minimum co-ordinate.
*/

      val minZ = scene.readDouble (LibResource
      ("anim.cell.Cell.readBoundingBox.minDesc", "Z"))
      scene.readDouble (_ >= minZ, LibResource
      ("anim.cell.Cell.readBoundingBox.maxDesc", "Z", minZ))
    }
  }
}
