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
import scala.math.sqrt
import scalafx.collections.ObservableFloatArray
import scalafx.collections.ObservableIntegerArray
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell tetrahedron''
primitives.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Tetrahedron.html
Tetrahedra]] for further information.

@constructor Construct a new tetrahedron primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Tetrahedron.html
Tetrahedra]] for further information.

@since 0.0
*/
//=============================================================================

private [cell] final class Tetrahedron (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Tetrahedron base dimension.

Base side dimension of the equilateral triangle forming the base of the
tetrahedron, measured on the X-Y plane.
*/

  private val baseDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Tetrahedron.readDim", 0))

/**
Tetrahedron top dimension.

Top side dimension of the equilateral triangle forming the top of the
tetrahedron, measured on the X-Y plane.
*/

  private val topDim = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Tetrahedron.readDim", 1))

/**
Tetrahedron height (Z-dimension).

Height of the tetrahedron measured along the Z-Axis.
*/

  private val height = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Tetrahedron.readDim", 2))

/**
Tetrahedron top X-axis offset.
*/

  private val xOffset = scene.readDouble (LibResource
  ("anim.cell.Tetrahedron.readOffset", 0))

/**
Tetrahedron top Y-axis offset.
*/

  private val yOffset = scene.readDouble (LibResource
  ("anim.cell.Tetrahedron.readOffset", 1))

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Mesh3D!]]

The mesh is a custom TriangleMesh object.

Note that the base is an equilateral triangle on the X-Y plane, with the origin
at (0, 0, 0), relative to its parent. This triangle is aligned so that it's
bottom edge is parallel to the X-axis.
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh = new TriangleMesh {

/*
Create the list of vertices.
*/

    override val points = {

/*
Some commonly used values. Numbered counter-clockwise from top (when looking at
the tetrahedron from above).

Note the y1_2 & y4_5 coordinates are obtained from the expression tan 30° = y /
b/2, where "b" is the base dimension. Since tan 30° is (√3)/3, then y is (b *
√3)/6. The value is then negated, because the base of the triangle lies below
the X-axis.

The height of the triangle, which represents the difference between y0 and y1_2
or y3 and y4_5, is given by Pythagoras: √(b² - (b²/4)) = b(√3)/2. Subtracting
the corresponding Y coordinate calculated above gives the second Y coordinate
as b(√3)/3.

A picture is worth a 1000 words - if only we had one! 
*/

      val x0 = 0.0f
      val x1 = -(baseDim / 2.0).toFloat
      val x2 = (baseDim / 2.0).toFloat
      val x3 = xOffset.toFloat
      val x4 = -((topDim / 2.0) + xOffset).toFloat
      val x5 = ((topDim / 2.0) + xOffset).toFloat
      val y0 = (baseDim * sqrt (3.0) / 3.0).toFloat
      val y1_2 = -(baseDim * sqrt (3.0) / 6.0).toFloat
      val y3 = (topDim * sqrt (3.0) / 3.0 + yOffset).toFloat
      val y4_5 = -(topDim * sqrt (3.0) / 6.0 + yOffset).toFloat
      val z0_1_2 = 0.0f
      val z3_4_5 = height.toFloat

/*
Co-ordinates, ordered clockwise from the top-hand corner (when looking at the
base from above).

Note that these coordinates are stored as floats. Why?  No idea...
*/

      ObservableFloatArray (
        x0, y0, z0_1_2,           // 0, base upper
        x1, y1_2, z0_1_2,         // 1, base lower-left
        x2, y1_2, z0_1_2,         // 2, base lower-right
        x3, y3, z3_4_5,           // 3, top upper
        x4, y4_5, z3_4_5,         // 4, top lower-left
        x5, y4_5, z3_4_5          // 5, top lower-right
      )
    }

/*
Now create the list of faces (triangles), constructed from indices of the
associated points defined above.
*/

    override val faces = ObservableIntegerArray (
      0, 1, 2,          // Base, face 0
      0, 3, 4,          // Left-rear, first half, face 1.
      0, 4, 1,          // Left-rear, second half, face 2.
      1, 4, 5,          // Front, first half, face 3.
      1, 5, 2,          // Front, second half, face 4.
      2, 5, 3,          // Right-rear, first half, face 5.
      2, 3, 0,          // Right-rear, second half, face 6.
      3, 4, 5           // Top, face 7.
    )

/*
Now create the smoothing face groups (face index map to smoothing group),
constructed from indices of the associated faces defined above.
*/

    override val faceSmoothingGroups = ObservableIntegerArray (
      0,                // Base is made up of face 0, group 0.
      1, 1,             // Left-rear is made up of faces 1 & 2, group 1.
      2, 2,             // Front is made up of faces 3 & 4, group 2.
      3, 3,             // Right-rear is made up of faces 5 & 6, group 3.
      4                 // Top is made up of face 7, group 4.
    )

/*
For now, don't define texture mapping coordinates. We will typically not apply
textures to cells.
*/

    //override val getTexCoords =
  }
}