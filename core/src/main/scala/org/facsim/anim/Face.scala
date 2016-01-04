/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
Scala source file from the org.facsim.anim package.
*/
//======================================================================================================================

package org.facsim.anim

import org.facsim.{requireNonNull, requireValid}
import scala.annotation.tailrec

//======================================================================================================================
/**
Class representing a 3D face.

A single face is constructed from a list of three or more vertices, which
should be coplanar; faces with more than three vertices are rendered as (''n''
- 2) ''triangular'' faces (faces represented by just three vertices), where
''n'' is the number of vertices.

Each triangular face takes the first vertex in the list and a pair of
consecutive vertices. For example, if a list of 5 vertices are provided,
labeled as ''A'', ''B'', ''C'', ''D'' and ''E'', then this face will be
rendered as three faces formed from these vertices as follows:
  - ''A'', ''B'', ''C''
  - ''A'', ''C'', ''D''
  - ''A'', ''D'', ''E''

These sets of triangular face vertices should be non-self-intersecting.

The face normal (the direction in which the face is visible), faces towards the
viewer when the listed vertices appear in counterclockwise order; if the
vertices appear in clockwise order, then the face is invisible. (This behavior
can be modified by selecting a different ScalaFX cull face option when
rendering each face.) All triangular faces making up this face should be
coplanar and should share the same face normal.

@constructor Construct an explicit texture-mapped face from a list of ''rich''
points.

@param vertices List of coplanar points defining the vertices of the face. At
least three vertices are required, with an additional contiguous face being
defined for each additional vertex. Each vertex includes a point in 3D
animation space as well as associated texture map coordinate.

@param smoothingGroup Smoothing group array for this face. If 0, then the face
belongs to no smoothing groups; otherwise, the face belongs to the smoothing
group corresponding to each bit set.

@throws java.lang.NullPointerException if `vertices` is `null`.

@throws java.lang.IllegalArgumentException if `vertices` has fewer than 3
points defined.
*/
//======================================================================================================================

private [anim] final class Face (vertices: List [RichPoint],
val smoothingGroup: Int = 0) {

/*
Sanity checks.
*/

  requireNonNull (vertices)
  requireValid (vertices, vertices.size > 2)

//----------------------------------------------------------------------------------------------------------------------
/**
Construct a neutral texture-mapped face from a list of regular points in 3D
space.

Such faces are assumed to not require a smoothing group; that is, they are
assigned 0 as a smoothing group value.

@param vertices List of coplanar points defining the vertices of the face. At
least three vertices are required, with an additional contiguous face being
defined for each additional vertex. Each vertex includes a point in 3D
animation space only; a suitable, neutral texture coordinate point will be
mapped to each point as appropriate.

@throws java.lang.NullPointerException if `vertices` is `null`.

@throws java.lang.IllegalArgumentException if `vertices` has fewer than 3
points defined.
*/
//----------------------------------------------------------------------------------------------------------------------

  def this (vertices: List [Point3D]) = this (Face.neutralize (vertices), 0)

//----------------------------------------------------------------------------------------------------------------------
/**
Create a list of individual, triangular faces from this face.

Each triangular face is made up of three vertex indices. If the face has just
three, then we return a single triangular face. However, it if has more than
three, we construct the triangles starting from the first point and creating
faces for each subsequent pair of indices.

@return A list of triangular faces.
*/
//----------------------------------------------------------------------------------------------------------------------

  def toList: List [Face] = {

/*
Determine the first vertex. This is used as the first vertex for each resulting
triangular face.
*/

    val first = vertices.head

/*
Helper function to extract the remaining triangular faces.
*/

    @tailrec
    def extractFace (remaining: List [RichPoint], faces: List [Face]):
    List [Face] = {

/*
If there is no tail for the remaining points, then we're done, so return the
*/

      if (remaining.tail.isEmpty) faces

/*
Otherwise, create a new face from the first point, the head of the remaining
points, and the head of the remaining points' tail and prepend it to the list
of faces.
*/

      else {
        val second = remaining.head
        val third = remaining.tail.head
        val face = new Face (first :: second :: third :: Nil, smoothingGroup)
        extractFace (remaining.tail, face :: faces)
      }
    }

/*
Start the ball rolling by extracting the first face. We ought to return at
least one face.
*/

    extractFace (vertices.tail, Nil)
  } ensuring (_.nonEmpty)

//----------------------------------------------------------------------------------------------------------------------
/**
Return list of 3D animation points defining this face.

@return List of 3D animation points defining this face.
*/
//----------------------------------------------------------------------------------------------------------------------

  def points = vertices.map (_.point)

//----------------------------------------------------------------------------------------------------------------------
/**
Return list of 3D animation points defining this face.

@return List of 3D animation points defining this face.
*/
//----------------------------------------------------------------------------------------------------------------------

  def texturePoints = vertices.map (_.texturePoint)

//----------------------------------------------------------------------------------------------------------------------
/**
Convert the face's 3D animation points and texture map points into a single
list of zipped index values.

The order of values returned is:

`p0`, `t0`, `p1`, `t1`, `p2`, `t2`

where `p`''x'' is the ''x''th 3D animation point index and `t`''x'' is the
''x''th texture map point index.

@param pointIdxMap Map associating each 3D point with its index value. This map
'''must''' contain all of the vertices making up this face.

@param texturePointIdxMap Map associating each texture point with its index
value. This map '''must''' contain all of the texture belonging to this face.

@return list of zipped 3D animation point and texture map point indices.
*/
//----------------------------------------------------------------------------------------------------------------------

  def indices (pointIdxMap: Map [Point3D, Int],
  texturePointIdxMap: Map [TexturePoint, Int]) = {

/*
Helper function to build the index list.
*/

    def buildIndices (ps: List [Point3D], ts: List [TexturePoint]):
    List [Int] = {

/*
If there are no more points, then, return an empty list.
*/

      if (ps.isEmpty) {
        assert (ts.isEmpty)
        Nil
      }

/*
Otherwise, prepend the head 3D point index and texture map point index to the
result of the next iteration.
*/

      else pointIdxMap (ps.head) :: texturePointIdxMap (ts.head) ::
      buildIndices (ps.tail, ts.tail)
    }

/*
Build the list from the two lists.
*/

    buildIndices (points, texturePoints)
  }
}

//======================================================================================================================
/**
Face companion object.
*/
//======================================================================================================================

private object Face {

//----------------------------------------------------------------------------------------------------------------------
/**
Convert a list of points in 3D space into rich points with neutral texture
mapping coordinates.

@param vertices List of 3D points to be converted.

@return List of rich points with neutral texture mapping coordinates mapped to
each 3D point in `vertices`.

@throws java.lang.NullPointerException if vertices is `null`.
*/
//----------------------------------------------------------------------------------------------------------------------

  def neutralize (vertices: List [Point3D]): List [RichPoint] = {

/*
Argument verification.
*/

    requireNonNull (vertices)

/*
For now, simply map each 3D to a rich point using the origin for a texture
coordinate.
*/

    vertices.map (p => RichPoint (p, TexturePoint.Origin))
  }
}