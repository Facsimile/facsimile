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

import javafx.collections.ObservableFloatArray
import org.facsim.LibResource
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod]] cell trapezoid''
primitives.

@note The ''cell box'' primitive is actually stored as a trapezoid.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Trapezoids.html
Trapezoids & Boxes]] for further information.

@constructor Construct a new trapezoid primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive.  If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//=============================================================================

private [cell] final class Trapezoid (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Trapezoid base Y-axis dimension.

Base of the trapezoid measured along the Y-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''base length'',
although most people would refer to it as the ''base depth'' or ''base width''.
Here, we refer to it as the base Y-axis dimension to avoid confusion.
*/

  private val baseYDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Trapezoid.readDim", 1))

/**
Trapezoid base X-axis dimension.

Base of the trapezoid measured along the X-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''base width'',
although most people would refer to it as the ''base length''.  Here, we refer
to it as the base X-axis dimension to avoid confusion.
*/

  private val baseXDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Trapezoid.readDim", 0))

/**
Trapezoid top Y-axis dimension.

Top of the trapezoid measured along the Y-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''top length'',
although most people would refer to it as the ''top depth'' or ''top width''.
Here, we refer to it as the top Y-axis dimension to avoid confusion.
*/

  private val topYDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Trapezoid.readDim", 3))

/**
Trapezoid top X-axis dimension.

Top of the trapezoid measured along the X-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''top width'',
although most people would refer to it as the ''top length''.  Here, we refer
to it as the base X-axis dimension to avoid confusion.
*/

  private val topXDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Trapezoid.readDim", 2))

/**
Trapezoid height (Z-dimension).

Height of the trapezoid measured along the Z-Axis.
*/

  private val height = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Trapezoid.readDim", 4))

/**
Trapezoid top X-axis offset.
*/

  private val xOffset = scene.readDouble (LibResource
  ("anim.cell.Trapezoid.readOffset", 0))

/**
Trapezoid top Y-axis offset.
*/

  private val yOffset = scene.readDouble (LibResource
  ("anim.cell.Trapezoid.readOffset", 1))

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Mesh3D!]]

The mesh is a custom TriangleMesh object.

Note that the base is an rectangle on the X-Y plane, with the origin at 0, 0, 0
(relative to its parent).  This rectangle is aligned so that it's bottom and
top edges are parallel to the X-axis, and it's left and right edges are
parallel to the Y-axis.
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh = new TriangleMesh {

/*
Create the list of vertices.
*/

    override val points = {

/*
Some commonly used values.
*/

      val x0_3 = -(baseXDim / 2.0).toFloat
      val x1_2 = (baseXDim / 2.0).toFloat
      val x4_7 = -((topXDim / 2.0) + xOffset).toFloat
      val x5_6 = ((topXDim / 2.0) + xOffset).toFloat
      val y0_1 = (baseYDim / 2.0).toFloat
      val y2_3 = -(baseYDim / 2.0).toFloat
      val y4_5 = ((topYDim / 2.0) + yOffset).toFloat
      val y6_7 = -((topYDim / 2.0) + yOffset).toFloat
      val z = height.toFloat

/*
Co-ordinates, ordered clockwise from the upper-left-hand corner (when
looking at the base from above).

Note that these coordinates are stored as floats.  Why?  No idea...
*/

      Array [Float] (
        x0_3, y0_1, 0.0f,       // 0, base upper-left
        x1_2, y0_1, 0.0f,       // 1, base upper-right
        x1_2, y2_3, 0.0f,       // 2, base lower-right
        x0_3, y2_3, 0.0f,       // 3, base lower-left
        x4_7, y4_5, z,          // 4, top upper-left
        x5_6, y4_5, z,          // 5, top upper-right
        x5_6, y6_7, z,          // 6, top lower-right
        x4_7, y6_7, z           // 7, top lower-left
      )
    }

/*
Now create the list of faces (triangles), constructed from indices of the
associated points defined above.
*/

    override val faces = Array [Int] (
      0, 1, 2,          // Base, first half, face 0.
      0, 2, 3,          // Base, second half, face 1.
      0, 4, 5,          // Back, first half, face 2.
      0, 5, 1,          // Back, second half, face 3.
      1, 5, 6,          // Right, first half, face 4.
      1, 6, 2,          // Right, second half, face 5.
      2, 6, 7,          // Front, first half, face 6.
      2, 7, 3,          // Front, second half, face 7.
      3, 7, 4,          // Left, first half, face 8.
      3, 4, 0,          // Left, second half, face 9.
      4, 5, 6,          // Top, first half, face 10.
      4, 6, 7           // Top, second half, face 11.
    )

/*
Now create the smoothing face groups (face index map to smoothing group),
constructed from indices of the associated points defined above.
*/

    override val faceSmoothingGroups = Array [Int] (
      0, 0,             // Base is made up of faces 0 & 1, group 0.
      1, 1,             // Back is made up of faces 2 & 3, group 1.
      2, 2,             // Right is made up of faces 4 & 5, group 2.
      3, 3,             // Front is made up of faces 6 & 7, group 3.
      4, 4,             // Left is made up of faces 8 & 9, group 4.
      5, 5              // Top is made up of faces 10 & 11, group 5.
    )

/*
For now, don't define texture mapping co-ordinates. We will typically not apply
textures to cells.
*/

  }
}