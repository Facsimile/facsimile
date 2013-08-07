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

import org.facsim.LibResource
import scala.math.sqrt
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod]] cell cone''
primitives.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Tetrahedron.html
Tetrahedra]] for further information.

@constructor Construct a new tetrahedron primitive from the data stream.

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

private [cell] final class Cone (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Cone radius.

Base side radius, measured on the X-Y plane.
*/

  private val radius = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Cone.readDim", 0))

/**
Cone height (Z-dimension).

Height of the cone measured along the Z-Axis.
*/

  private val height = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Cone.readDim", 1))

/**
Cone top X-axis offset.
*/

  private val xOffset = scene.readDouble (LibResource
  ("anim.cell.Cone.readOffset", 0))

/**
Cone top Y-axis offset.
*/

  private val yOffset = scene.readDouble (LibResource
  ("anim.cell.Cone.readOffset", 1))

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Mesh3D!]]

The mesh is a custom TriangleMesh object.

Note that the base is a circle on the X-Y plane, with the center at 0, 0, 0
(relative to its parent).
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh = new TriangleMesh {

/*
Create the list of vertices.
*/

    override val points = {

/*
Use the MeshUtils to generate the points for the base of the cone, and then add
the vertex for the top of the cone.

NOTE: The number of divisions used for a fine cone in AutoMod is just 16.
We'll use the same value.
*/

      MeshUtils.circleCoordinates (radius, 0.0, Cone.divisions, 0.0, 0.0) ++
      Array [Float] (
        xOffset.toFloat,
        yOffset.toFloat,
        height.toFloat
      )
    }

/*
Now create the list of faces (triangles), constructed from indices of the
associated points defined above.
*/

    override val faces = {

/*
Use the MeshUtils to generate the faces making up the base of the cone, then
add an array with the faces making up the sides of the cone.
*/

      MeshUtils.circleFaces (Cone.divisions, 0) ++
      MeshUtils.coneFaces (Cone.divisions, Cone.divisions + 1, 1)
    }

/*
Now create the smoothing face groups (face index map to smoothing group),
constructed from indices of the associated faces defined above.

The faces making up the base all belong to the base smoothing group (0) and the
faces making up the sides all belong to the side smoothing group (1).  Note
that there are divisions faces in each group.
*/

    override val faceSmoothingGroups = Array.tabulate (Cone.divisions * 2)(_ %
    Cone.divisions)

/*
For now, don't define texture mapping co-ordinates. We will typically not apply
textures to cells.
*/

  }
}

//=============================================================================
/**
Cone companion object.

@since 0.0
*/
//=============================================================================

private object Cone {

/**
Number of divisions per cone.

The number of divisions for a fine cone in AutoMod is 16, and for a course cone
it's 8.  For simplicity, we'll convert all cones to have 16 divisions.
*/

  val divisions = 16
}