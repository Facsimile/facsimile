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
import scala.annotation.tailrec
import scalafx.collections.ObservableFloatArray
import scalafx.collections.ObservableIntegerArray
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell polyhedron''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Polyhedra.html
Polyhedra]] for further information.

@constructor Construct a new polyhedron primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive.  If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Polyhedra.html
Polyhedra]] for further information.

@since 0.0
*/
//=============================================================================

private [cell] final class Polyhedron (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
List of points.
*/

  private val pointList = Polyhedron.readPoints (scene)

/**
List of faces - note that these are not necessarily triangular faces.
*/

  private val faceList = Polyhedron.readFaces (scene, pointList.size)

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Mesh3D!]]

The mesh is a custom TriangleMesh object.

Note that the Polyhedron's origin is at (0, 0, 0) local to its parent: the
position of all points are relative to this origin.
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh = new TriangleMesh {

/*
Create the vertices array from the list of points.

Each point will be translated into three float values for x, y and z, and all
such points combined into a single array of all points.
*/

    override val points =
    ObservableFloatArray (pointList.map (_.toFloatList).flatten.toArray)

/*
Now create the list of faces (as triangles), constructed from indices of the
associated points defined above.
*/

    override val faces =
    ObservableIntegerArray (faceList.map (_.toTriangularFaces.flatten).flatten.
    toArray)

/*
For now, its not possible to define smoothing groups for the polyhedron.

For one thing, explicit smoothing group information is not provided as part of
Polyhedron data.  For another, even if we assume that multi-triangle faces
(polyhedron faces made up of more than 1 triangle) belong to the same face and
should be smoother, we neither track which faces can be so smoothed (right now,
although that would be relatively easy to overcome), and ScalaFX/JavaFX imposes
a hard limit of just 32 smoothing groups, which we might easily exceed.

By default, faces are not added to smoothing groups, so each is rendered with a
hard surface.  That should work out OK for us, as AutoMod/ACE does the exact
same thing.
*/

    //override val faceSmoothingGroups =

/*
For now, don't define texture mapping coordinates.  We will typically not apply
textures to cells.
*/

    //override val getTexCoords =
  }
}

//=============================================================================
/**
Polyhedron companion object.

@since 0.0
*/
//=============================================================================

private object Polyhedron {

//-----------------------------------------------------------------------------
/**
Read polyhedron point data from the stream.

@param scene Reference to the CellScene of which this cell is a part.

@return List of points.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def readPoints (scene: CellScene) = {

/**
Helper function to read the list of points from the data stream.

Note that, due to the need for tail recursion, the list is built in reverse.
*/

    @tailrec
    def readPoint (count: Int, points: List [Point]): List [Point] = {
      if (count == 0) points
      else {
        val point = new Point (scene, Point.Polyhedron)
        readPoint (count - 1, point :: points)
      }
    }

/*
Read the number of points from the data stream.  This value must be at least 3.
*/

    val points = scene.readInt (_ > 2, LibResource
    ("anim.cell.Polyhedron.readPoints"))

/*
Return the list of points read, reversing the list order so that they are put
back into the original order.
*/

    readPoint (points, Nil).reverse
  }

//-----------------------------------------------------------------------------
/**
Read polyhedron face data from the stream.

@param scene Reference to the CellScene of which this cell is a part.

@param numPoints Number of points that may be used to define the associated
faces.  It is an error for a point index to equal or exceed this value.

@return List of faces.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def readFaces (scene: CellScene, numPoints: Int) = {

/**
Helper function to read the list of faces from the data stream.

Note that, due to the need for tail recursion, the list is built in reverse.
*/

    @tailrec
    def readFace (count: Int, faces: List [Face]): List [Face] = {
      if (count == 0) faces
      else {
        val face = new Face (scene, numPoints)
        readFace (count - 1, face :: faces)
      }
    }

/*
Read the number of points from the data stream.  This value must be at least 1.
*/

    val faces = scene.readInt (_ > 0, LibResource
    ("anim.cell.Polyhedron.readFaces"))

/*
Return the list of faces read, reversing the list order so that they are put
back into the original order.
*/

    readFace (faces, Nil).reverse
  }
}