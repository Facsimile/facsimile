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

//=============================================================================
/**
Class representing a basic 3D face.

@note A single face may be made up of more than one triangle.

@constructor Construct a new basic face from the cell data stream.

@param scene Reference to the CellScene of which this point is a part.

@param numPoints Number of defined points that can be used as vertex indices
for the face. 

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//=============================================================================

private [cell] final class Face (scene: CellScene, numPoints: Int) {

/*
List of vertex indices making up the face.
*/

  private val vertices = Face.read (scene, numPoints)

//-----------------------------------------------------------------------------
/**
Create a list of triangular faces from this face.

Each triangular face is made up of three vertex indices.  If the face has just
three, then we return a single triangular face.  However, it if has more than
three, we construct the triangles starting from the first point and creating
faces for each subsequence pair of indices.

@return A list of triangular face indices.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def toTriangularFaces = {

/*
The lead vertex is the first point on the list.  Create the triangles from
that.
*/

    val leadPoint = vertices.head

/*
Helper function to construct the list of triangular faces.

Note that, due to the need for tail recursion, the list of triangular faces is
built in reverse.
*/

    @tailrec
    def addTriangle (points: List [Int], faces: List [List [Int]]): List [List
    [Int]] = {
      if (points.tail.isEmpty) faces
      else {
        val face = List (leadPoint, points.head, points.tail.head)
        addTriangle (points.tail, face :: faces)
      }
    }

/*
Construct and return the list from the remaining points, reversing the list
order so that they are put back into the original order.
*/

    addTriangle (vertices.tail, Nil).reverse
  } ensuring (!_.isEmpty)
}

//=============================================================================
/**
Face companion object.

@since 0.0
*/
//=============================================================================

private object Face {

//-----------------------------------------------------------------------------
/**
Read face data from the stream.

@param scene Reference to the CellScene of which this cell is a part.

@param numPoints Number of defined points that can be used as vertex indices
for the face. 

@return List of vertex indices.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def read (scene: CellScene, numPoints: Int) = {

/**
Helper function to read the list of point indices from the data stream.

Note that, due to the need for tail recursion, the list is built in reverse
*/

    @tailrec
    def readIndex (count: Int, list: List [Int]): List [Int] = {
      if (count == 0) list
      else {
        val index = scene.readInt (i => i >= 0 && i < numPoints, LibResource
        ("anim.cell.Face.readIndex", numPoints - 1))
        readIndex (count - 1, index :: list)
      }
    }

/*
Read the number of indices from the data stream.  This value must be at least
3.
*/

    val indices = scene.readInt (_ > 2, LibResource
    ("anim.cell.Face.readCount"))

/*
Return the list of vertex indices read, reversing the list order so that they
are put back into the original order.
*/

    readIndex (indices, Nil).reverse
  } ensuring (_.size >= 3)
}