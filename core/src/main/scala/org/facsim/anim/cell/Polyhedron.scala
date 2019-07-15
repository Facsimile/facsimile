/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2019, Michael J Allen.

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

import org.facsim.LibResource
import org.facsim.anim.Face
import org.facsim.anim.Mesh
import org.facsim.anim.Point3D
import scala.annotation.tailrec
import scala.collection.immutable.VectorBuilder

/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell polyhedron''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Polyhedra.html
Polyhedra]] for further information.

@constructor Construct a new polyhedron primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Polyhedra.html
Polyhedra]] for further information.
*/

private[cell] final class Polyhedron(scene: CellScene, parent: Option[Set])
extends Mesh3D(scene, parent) {

/**
Points defining this polyhedron.

Points are stored as a vector, preserving the order of definition.
*/

  private val points = Polyhedron.readPoints(scene)

/**
Faces making up this polyhedron.

Faces are stored as a list of faces. The order in which faces are defined is
irrelevant.
*/

  private val faces = Polyhedron.readFaces(scene, points)

/*
Create a mesh to represent this cell and return it.

The origin of the cell is at the center of its base.

@return Mesh representing the cell.

@see [[org.facsim.anim.cell.Mesh3D.cellMesh]].
*/
  protected[cell] override def cellMesh: Mesh = new Mesh(faces)
}

/**
Polyhedron companion object.
*/

private object Polyhedron {

/**
Read polyhedron point data from the stream.

@param scene Reference to the CellScene of which this cell is a part.

@return List of points.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
  private def readPoints(scene: CellScene) = {

/*
Read the number of points from the data stream. This value must be at least 3.
*/

    val numPoints = scene.readInt(_ > 2, LibResource
   ("anim.cell.Polyhedron.readNumPoints"))

/*
Create a vector builder with sufficient space for the required number of
points.
*/

    val points = new VectorBuilder[Point3D]()
    points.sizeHint(numPoints)

/*
Helper function to read the list of points from the data stream.
*/

    @tailrec
    def readPoint(count: Int): Vector[Point3D] = {

/*
If we've processed all the points, then convert it to a vector and return.
*/

      if(count == 0) points.result()

/*
Otherwise, read the next point and add it to the vector. Then perform the next
iteration.
*/

      else {
        val point = Point.read(scene, Point.Polyhedron)
        points += point
        readPoint(count - 1)
      }
    }

/*
Return a vector containing the points read, in the order that they were read.
*/

    readPoint(numPoints)
  }

/**
Read polyhedron face data from the stream.

@param scene Reference to the CellScene of which this cell is a part.

@param points Vector of points defining face vertices. Point indices should be
in the range [0, points.size - 1].

@return List of faces belonging to the polyhedron.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
  private def readFaces(scene: CellScene, points: Vector[Point3D]):
  List[Face] = {

/*
Number of points making up this polyhedron.
*/

    val numPoints = points.size

/*
Helper function to read the list of point indices from the data stream.
*/

    @tailrec
    def readVertex(count: Int, list: List[Int]): Face = {

/*
If we've read all of the vertices, then use them to construct a face.

Note that since the list is built in reverse, we have to reverse it when
mapping it back to the defined points.
*/

      if(count == 0) new Face(list.reverse.map(i => points(i)))
      else {
        val index = scene.readInt(i => i >= 0 && i < numPoints, LibResource
       ("anim.cell.Polyhedron.readIndex", numPoints - 1))
        readVertex(count - 1, index :: list)
      }
    }

/*
Helper function to read the list of faces from the data stream.

Note that faces are listed in the reverse order that they appear in the cell
data. This doesn't matter too much, since the order in which faces are defined
is unimportant.
*/

    @tailrec
    def readFace(count: Int, faces: List[Face]): List[Face] = {

/*
If we have defined all of the faces, then return the list of faces now.
*/

      if(count == 0) faces

/*
Otherwise, read the next face.
*/

      else {

/*
Start by reading the number of vertices making up the face.

There must be at least three. Furthermore, all of these vertices should be
defined on the same plane, which is guaranteed for just 3 points, but isn't
verified if there are more than 3.
*/

        val indices = scene.readInt(_ > 2, LibResource
       ("anim.cell.Polyhedron.readNumIndices"))

/*
Now read the vertices and use them to create the face. Prepend the face to the
current list of faces and perform the next iteration.
*/

        val face = readVertex(indices, Nil)
        readFace(count - 1, face :: faces)
      }
    }

/*
Read the number of faces making up this polyhedron from the data stream. This
value must be at least 1.
*/

    val faces = scene.readInt(_ > 0, LibResource
   ("anim.cell.Polyhedron.readNumFaces"))

/*
Return the list of faces read, reversing the list order so that they are put
back into the original order.
*/

    readFace(faces, Nil)
  }
}