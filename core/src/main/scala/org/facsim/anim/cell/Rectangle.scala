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
import scalafx.collections.ObservableFloatArray
import scalafx.collections.ObservableIntegerArray
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell rectangle''
primitives.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Rectangles.html
Rectangles]] for further information.

@constructor Construct a new rectangle primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Rectangles.html
Rectangles]] for further information.

@since 0.0
*/
//=============================================================================

private [cell] final class Rectangle (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Rectangle Y-axis dimension.

Rectangle measured along the Y-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''length'',
although most people would refer to it as the ''depth'' or ''width''. Here, we
refer to it as the Y-axis dimension to avoid confusion.
*/

  private val yDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Rectangle.readDim", 1))

/**
Rectangle X-axis dimension.

Rectangle measured along the X-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''width'',
although most people would refer to it as the ''length''. Here, we refer to it
as the X-axis dimension to avoid confusion.
*/

  private val xDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Rectangle.readDim", 0))

/*
Read, but discard, the X-axis offset.
*/

  scene.readDouble (LibResource ("anim.cell.Rectangle.readOffset", 0))

/**
Read, but discard, the X-axis offset.
*/

  scene.readDouble (LibResource ("anim.cell.Rectangle.readOffset", 1))

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Mesh3D!]]

The mesh is a custom TriangleMesh object.

Note that the rectangle is on the X-Y plane, with the center at (0, 0, 0),
relative to its parent. Since this is not being drawn in wireframe mode, the
rectangle is either drawn as a circle or a sector of a circle.
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh = new TriangleMesh {

/*
Create the list of vertices.
*/

    override val points = {

/*
Common points. Numbered counter-clockwise from top left (when looking at the
rectangle from above).
*/

      val x0_1 = -(xDim / 2.0).toFloat
      val x2_3 = (xDim / 2.0).toFloat
      val y0_3 = (yDim / 2.0).toFloat
      val y1_2 = -(yDim / 2.0).toFloat
      val z = 0.0f

/*
Create and return the point array. Numbered counter-clockwise from top left.
*/

      ObservableFloatArray (
        x0_1, y0_3, z,          // Point 0, top left.
        x0_1, y1_2, z,          // Point 1, bottom left.
        x2_3, y1_2, z,          // Point 2, bottom right.
        x2_3, y0_3, z           // Point 3, top right.
      ) 
    }

/*
Now create the list of faces (triangles), constructed from indices of the
associated points defined above.
*/

    override val faces = ObservableIntegerArray (
      0, 1, 2,                  // Face 0
      0, 2, 3                   // Face 1
    )

/*
Now create the smoothing face groups (face index map to smoothing group),
constructed from indices of the associated faces defined above.

Both faces in the rectangle belong to the same group, 0.
*/

    override val faceSmoothingGroups = ObservableIntegerArray (0, 0)

/*
For now, don't define texture mapping coordinates. We will typically not apply
textures to cells.
*/

    //override val getTexCoords =
  }
}