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
import scalafx.collections.ObservableIntegerArray
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell cylinder''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Cylinders.html
Cylinders]] for further information.

@constructor Construct a new cylinder primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Cylinders.html
Cylinders]] for further information.

@since 0.0
*/
//=============================================================================

private [cell] final class Cylinder (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Cylinder radius.

Base & top radius, measured on the X-Y plane.
*/

  private val radius = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Cylinder.readDim", 0))

/**
Cylinder height (Z-dimension).

Height of the cylinder measured along the Z-Axis.
*/

  private val height = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Cylinder.readDim", 1))

/**
Cylinder top X-axis offset.
*/

  private val xOffset = scene.readDouble (LibResource
  ("anim.cell.Cylinder.readOffset", 0))

/**
Cylinder top Y-axis offset.
*/

  private val yOffset = scene.readDouble (LibResource
  ("anim.cell.Cylinder.readOffset", 1))

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Mesh3D!]]

The mesh is a custom TriangleMesh object.

Note that the base is a circle on the X-Y plane, with its center at (0, 0, 0)
and that the top is also a circle on the X-Y plane, with its center at
(xOffset, yOffset, height), relative to its parent .
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh = new TriangleMesh {

/*
Create the list of vertices.
*/

    override val points = {

/*
Use the MeshUtils to generate the points for the base, and then add points for
the top of the cylinder.
*/

      MeshUtils.circleCoordinates (radius, 0.0, Cylinder.divisions, 0.0,
      0.0) ++ MeshUtils.circleCoordinates (radius, height, Cylinder.divisions,
      xOffset, yOffset)
    }

/*
Now create the list of faces (triangles), constructed from indices of the
associated points defined above.
*/

    override val faces = {

/*
Use the MeshUtils to generate the faces making up the base, then add an array
with the faces making up the walls, then add an array with the faces making up
the top of the cylinder. 
*/

      MeshUtils.circleFaces (Cylinder.divisions, 0) ++
      MeshUtils.wallFaces (Cylinder.divisions) ++
      MeshUtils.circleFaces (Cylinder.divisions, Cylinder.divisions + 1)
    }

/*
Now create the smoothing face groups (face index map to smoothing group),
constructed from indices of the associated faces defined above.

The faces making up the base all belong to the base smoothing group (0), the
faces making up the walls all belong to the wall smoothing group (1), and the
faces making up the top all belong to the top smoothing group (2). Note that
there are divisions faces in the first and last group, and 2 * divisions in the
middle group - making 4 * divisions faces altogether.
*/

    override val faceSmoothingGroups =
    ObservableIntegerArray.tabulate (Cylinder.divisions * 4) {
      face: Int =>
      if (face < Cylinder.divisions) 0
      else if (face < 3 * Cylinder.divisions) 1
      else 2
    }

/*
For now, don't define texture mapping coordinates. We will typically not apply
textures to cells.
*/

    //override val getTexCoords =
  }
}

//=============================================================================
/**
Cylinder companion object.

@since 0.0
*/
//=============================================================================

private object Cylinder {

/**
Number of divisions per cylinder.

The number of divisions for a fine cylinder in AutoMod is 16, and for a course
cylinder it's 8. For simplicity, we'll convert all cylinders to have 16
divisions.
*/

  val divisions = 16
}