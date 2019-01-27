/*
Facsimile -- A Discrete-Event Simulation Library
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
Scala source file from the org.facsim.anim package.
*/

package org.facsim.anim

import org.facsim.assertNonNull
import org.facsim.measure.Angle
import scala.annotation.tailrec
import scalafx.scene.shape.TriangleMesh

/**
Represents a mesh consisting of one or more faces.

@constructor Construct a new mesh of faces.

@param faces List of faces making up the mesh. Note that each face instance can
contain one or more triangular faces.
*/

private[anim] final class Mesh(private val faces: List[Face]) {

/**
Add a list of faces to this mesh, resulting in a new mesh.

@param newFaces List of faces to be added to this mesh.

@return New mesh instance containing the current list of faces plus the new
list of faces.
*/
  def +(newFaces: List[Face]) = { //scalastyle:ignore
    assertNonNull(newFaces)
    new Mesh(faces ::: newFaces)
  }

/**
Add another mesh to this mesh, resulting in a new mesh.

@param newMesh Mesh to be added to this mesh.

@return New mesh instance containing the current list of faces plus the new
mesh's list of faces.
*/
  def +(newMesh: Mesh) = { //scalastyle:ignore
    assertNonNull(newMesh)
    new Mesh(faces ::: newMesh.faces)
  }

/**
Transform the supplied faces into a flattened list of triangular faces.

@return List of triangular faces making up the list.
*/
  private def fs = faces.map(_.toList).flatten

/**
Obtain a list of 3D animation points making up the faces in this mesh.

@note Some duplication of points is possible; this is not an issue, but the
number of duplications should be minimized for performance reasons. For this
reason, we look at the points on the passed face list, rather than the points
on the list of triangular faces.

@return List of 3D animation points making up the faces.
*/
  private def pointList = faces.map(_.points).flatten

/**
Obtain a list of texture map points making up the faces in this mesh.

@note Some duplication of points is possible; this is not an issue, but the
number of duplications should be minimized for performance reasons. For this
reason, we look at the points on the passed face list, rather than the points
on the list of triangular faces.

@return List of texture map points making up the faces.
*/
  private def texturePointList = faces.map(_.texturePoints).flatten

/**
For each unique item in a list, create a map associating the item with a unique
index value and a vector associating the index value with the list item.

The index values mapped to should be consecutive and zero-based.

@param list List to be analyzed. List members should be comparable and must
implement the [[scala.Equals]] trait.

@tparam T Type of element for which a map and vector is sought.

@return Pair containing a map, associating each unique value in `list` to a
unique integer index value, and a vector, associating each unique index value
to the associated list element. Index values should be consecutive in range [0,
n-1], where ''n'' is the number of elements in the resulting map & vector.
*/
  private def createMapVect[T <: Equals](list: List[T]) = {

/*
Tail-recursive helper function to build map & vector from remaining elements.
*/

    @tailrec
    def buildMapVect(xs: List[T], map: Map[T, Int], vect: Vector[T]):
   (Map[T, Int], Vector[T]) = {

/*
If there are no more elements to be processed, return the map and vector we
have built.
*/

      if(xs.isEmpty)(map, vect)

/*
Otherwise, determine the map & vector builder for the new iteration.
*/

      else {
        val x = xs.head

/*
If the head of the list is already in the map, then we retain the current map
and vector builder for the next iteration.
*/

        val(m, v) = if(map.contains(x))(map, vect)

/*
Otherwise, add the new element to the map and vector.
*/

        else(map +(x -> map.size), vect :+ x)

/*
Verify that the map and vector tally.
*/

        assert(v(m(x)) == x)

/*
Iterate to process the remaining elements of the list.
*/

        buildMapVect(xs.tail, m, v)
      }
    }

/*
Start the ball rolling by passing in the whole list with an empty map and
vector.
*/

    buildMapVect(list, Map.empty, Vector.empty)
  }

/**
Convert mesh to form for rendering by animation system.

@return [[scalafx.scene.shape.TriangleMesh]] representing this mesh instance.
*/
  private[anim] def triangleMesh = {

/*
Create a map of 3D animation points to their index value, and a vector
associating the index values to the associated points.
*/

    val(pMap, pVect) = createMapVect(pointList)

/*
Create a map of texture map points to their index value, and a vector
associating the index values to the associated points.
*/

    val(tMap, tVect) = createMapVect(texturePointList)

/*
Construct the TriangleMesh instance to hold this mesh's data.
*/

    val mesh = new TriangleMesh()

/*
Output each unique 3D face vertex in the form of a floating point array, in
which each point is represented by three floating point values equivalent to
that point's x, y & x coordinates.
*/

    mesh.points = pVect.flatMap(_.toFloatList).toArray

/*
Output each unique 3D face texture map point in the form of a floating point
array, in which each point is represented by two floating point values
equivalent to that point's u & v coordinates.
*/

    mesh.texCoords = tVect.flatMap(_.toList).toArray

/*
Output each face as a zipped array of its 3D animation point and texture map
point indices.
*/

    mesh.faces = fs.flatMap(f => f.indices(pMap, tMap)).toArray

/*
Finally, output the smoothing group associated with each face as a list.
*/

    mesh.faceSmoothingGroups = fs.map(_.smoothingGroup).toArray

/*
Return the resulting mesh.
*/

    mesh
  }
}

/**
Mesh companion object.

Utility methods for constructing common types of mesh.
*/

private[anim] object Mesh {

/**
Construct a conic mesh.

The circumference is relative to the local ''X-Y'' plane, with its center at
`c`. The conic apex is either above, below, or at the same height as the
center, measured along the local ''Z'' axis (possibly displaced along the local
''X''- and/or ''Y''-axes).

This function is useful for assisting with the construction a number of
different 3D shapes:

  - Segments (if the `drawAngle` angle is less than τ (2π) radians).
  - Circles (if `a.z` is zero).
  - Cones (if `a.z` is positive).
  - Cylinder lid and base sections (if `a.z` is zero).
  - Sphere poles (if `a.z` is none-zero)
  - Hemisphere poles (if `a.z` is positive).

@param c Center of the circle whose circumference forms the base of the conic
mesh.

@param r Radius of the cone or circle. This value must be finite and > zero.

@param a Apex of the cone. If the apex is at the same height as `c` (that is,
if `c.z` equals `a.z`), then `c` and `a` must be the same point (`c.x` must
equal `a.x` and `c.y` must equal `a.y`), with the result being a circle or a
segment. If c.z is non-zero, then a conic mesh will result.

@param topLeft Texture map point for the top left of the rectangle to be mapped
to the conic section. This must be above and to the left of `botRight`.

@param botRight Texture map point for the bottom right of the rectangle to be
mapped to the conic section. This must be below and to the right of `topLeft`.

@param divisions Number of segments to be employed to draw the shape. The
higher this number, the higher the fidelity of the resulting shape. This value
must be greater than 2.

@param beginAngle Angle at which drawing of the shape should start.

@param drawAngle Angle through which the shape should be drawn. This value
must be in the range (0, τ] radians.

@param smoothGroup Smoothing group to which all faces will be added.

@param down If `true`, then the resulting faces should defined by listing their
vertices in clockwise order, so that the resulting face normal points down the
local ''Z''-axis, typically making them visible from below the plane of the
circumference; if `false`, vertices are listed in counterclockwise order, so
that the resulting face normal point up the local ''Z''-axis, typically making
them visible from above the plane of the circumference.
*/
  private def conicMesh(c: Point3D, r: Double, a: Point3D,
  divisions: Int, topLeft: TexturePoint = TexturePoint.TopLeft,
  botRight: TexturePoint = TexturePoint.BottomRight,
  beginAngle: Angle.Measure = Angle.Zero, drawAngle: Angle.Measure = Angle.τ,
  smoothGroup: Int = 0, down: Boolean = false): Mesh = {

/*
Sanity checks. Assertions should be OK, since these are internal. If this is
ever made public, then we need "requireFinite" & "requireValid" macros.
*/

    assert(r > 0.0)
    assert(a.z != c.z || a == c)
    assert(divisions > 2)
    assert(topLeft.u < botRight.u)
    assert(topLeft.v < botRight.v)
    assert(drawAngle > Angle.Zero && drawAngle <= Angle.τ)

/*
Determine the angle for drawing each segment.
*/

    val segmentAngle = drawAngle / divisions

/*
Determine the u range of the texture co-ordinate box.
*/

    val uRange = botRight.u - topLeft.u

/*
Determine the spacing between texture coordinate points.
*/

    val uSpacing = uRange / divisions

/*
Determine the apex and circumference v coordinates.

See schematic below for a further explanation of this code.
*/

    val(vApex, vCircumf) = if(down)(botRight.v, topLeft.v)
    else(topLeft.v, botRight.v)

/*
Helper function to create a new face and add it to the current mesh. We start
with the last face and work our way back towards the first.
*/

    @tailrec
    def buildMesh(segment: Int, last: RichPoint, faces: List[Face]): Mesh = {

/*
If segment is negative, then we've created all the faces, so return them as a
mesh.
*/

      if(segment < 0) new Mesh(faces)

/*
Otherwise, create the current division's face from the apex, the last
circumference point and a new circumference point.
*/

      else {

/*
Start by creating a rich point for the apex of the face. This will utilize the
apex 3D point, and will create a texture point based upon the following
schematic, in which the ANs (N is segment number) represent successive apex
texture points and TNs represent successive circumference texture points:

  U →
V +--A0----A1----A2----A3----A4----A5----A6----A7--+
↓ |  /\    /\    /\    /\    /\    /\    /\    /\  |
  | /XX\  /XX\  /XX\  /XX\  /XX\  /XX\  /XX\  /XX\ |
  |/XXXX\/XXXX\/XXXX\/XXXX\/XXXX\/XXXX\/XXXX\/XXXX\|
  T0----T1----T2----T3----T4----T5----T6----T7----T8

T8, in this case, being supplied as the initial "last" point. Note that the Xs
are just used to shade in the portions of the texture map that are mapped to
each face.

For example, the points (A1, T0, T1) define the texture map vertices of the
first face, with the corresponding portion of the texture map being mapped to
the associated 3D face, defined by the associated 3D points.

Note: If this conic mesh has face normals pointing down, instead of up, then
the text map appears like this:

  U →
V T0----T1----T2----T3----T4----T5----T6----T7----T8
↓ |\XXXX/\XXXX/\XXXX/\XXXX/\XXXX/\XXXX/\XXXX/\XXXX/|
  | \XX/  \XX/  \XX/  \XX/  \XX/  \XX/  \XX/  \XX/ |
  |  \/    \/    \/    \/    \/    \/    \/    \/  |
  +--A0----A1----A2----A3----A4----A5----A6----A7--+

The reason is the we must map the texture coordinate to the underside of the
face, so it becomes a mirror image.
*/

        val apex = RichPoint(a, TexturePoint(uSpacing *(segment + 0.5f),
        vApex))

/*
Determine the angle from the center of the circle to this circumference point.
*/

        val angle = beginAngle + segmentAngle * segment

/*
Now create a rich point for the next vertex on the circumference.
*/

        val next = RichPoint(Point3D(c.x + r * angle.cos,
        c.y + r * angle.sin, c.z), TexturePoint(uSpacing * segment, vCircumf))

/*
Create the face by listing the points in the appropriate order. For a face
that's visible from above, the order of faces is counterclockwise, from below
it's clockwise.
*/

        val points = if(down) List(apex, last, next)
        else List(apex, next, last)

/*
Prepend the resulting face to the list defining this mesh, and perform the next
iteration.
*/

        buildMesh(segment - 1, next, new Face(points, smoothGroup) :: faces)
      }
    }

/*
Create the last rich circumference point.
*/

    val endAngle = if(drawAngle == Angle.τ) beginAngle
    else beginAngle + drawAngle
    val beginTexPoint = if(down)(botRight.u, vCircumf)
    else botRight
    val begin = RichPoint(Point3D(c.x + r * endAngle.cos,
    c.y + r * endAngle.sin, c.z), botRight)

/*
Create the mesh from the List of a faces.
*/

    buildMesh(divisions - 1, begin, Nil)
  }

/**
Construct a circular wall mesh.

The base circumference is relative to the local ''X-Y'' plane, with its center
at `cb`. The top circumference has its center at `ct` and is above `cb`,
measured along the local ''Z'' axis (possibly displaced along the local ''X''-
and/or ''Y''-axes).

This function is useful for assisting with the construction a number of
different 3D shapes:

  - Cyclinders, forming the sides.
  - Spheres, forming bands of latitude.
  - Hemispheres, forming bands of latitude.

@param cb Center of the circle whose circumference forms the base of the
circular wall mesh.

@param rb Radius of the base. This value must be finite and > zero.

@param ct Center of the circle whose circumference forms the top of the
circular wall mesh. This point must be above `cb` measured along the local
''Z''-axis.

@param rt Radius of the top. This value must be finite and > zero.

@param topLeft Texture map point for the top left of the rectangle to be mapped
to the circular wall section. This must be above and to the left of `botRight`.

@param botRight Texture map point for the bottom right of the rectangle to be
mapped to the circular wall section. This must be below and to the right of
`topLeft`.

@param divisions Number of sections to be employed to draw the shape. The
higher this number, the higher the fidelity of the resulting shape. This value
must be greater than 2.

@param smoothGroup Smoothing group to which all faces will be added.
*/
  private def circularWallMesh(cb: Point3D, rb: Double, ct: Point3D,
  rt: Double, divisions: Int, topLeft: TexturePoint = TexturePoint.TopLeft,
  botRight: TexturePoint = TexturePoint.BottomRight, smoothGroup: Int = 0) = {

/*
Sanity checks. Assertions should be OK, since these are internal. If this is
ever made public, then we need "requireFinite" & "requireValid" macros.
*/

    assert(rb > 0.0)
    assert(rt > 0.0)
    assert(ct.z > cb.z)
    assert(divisions > 2)
    assert(topLeft.u <= botRight.u)
    assert(topLeft.v <= botRight.v)

/*
Determine the angle for drawing each segment.
*/

    val sectionAngle = Angle.τ / divisions

/*
Determine the u range of the texture co-ordinate box.
*/

    val uRange = botRight.u - topLeft.u

/*
Determine the spacing between texture coordinate points on the U-axis.
*/

    val uSpacing = uRange / divisions

/*
Determine the top and base texture V-coordinates.

See schematic below for a further explanation of this code.
*/

    val vTop = topLeft.v
    val vBase = botRight.v

/*
Helper function to create a pair of new faces, representing the current wall
segment, to the current list of faces. We start with the last face and work our
way back towards the first.
*/

    @tailrec
    def buildMesh(section: Int, lastBase: RichPoint, lastTop: RichPoint,
    faces: List[Face]): Mesh = {

/*
If section is negative, then we've created all the faces, so create a new mesh
from the list of faces and return it.
*/

      if(section < 0) new Mesh(faces)

/*
Otherwise, create the current division's two faces from the the last base
circumference point, the last top circumference point, and a pair of new base
and top circumference points.
*/

      else {

/*
Each section is defined by two faces. We start by creating rich coordinates for
the next pair of base and top points. The texture coordintes are mapped to
the resulting faces as follows:

  U →
V T0----T1----T2----T3----T4----T5----T6----T7----T8
↓ |\    |\    |\    |\    |\    |\    |\    |\    |
  | \   | \   | \   | \   | \   | \   | \   | \   |
  |  \  |  \  |  \  |  \  |  \  |  \  |  \  |  \  |
  |   \ |   \ |   \ |   \ |   \ |   \ |   \ |   \ |
  |    \|    \|    \|    \|    \|    \|    \|    \|
  B0----B1----B2----B3----B4----B5----B6----B7----B8

for a texture map with 8 divisions. Note that, in this case, B0 and B8, and T0
and T8 will be mapped to the same 3D point on the base and top circumferences.

Determine the angle from the center of the base and top to the next base and
top circumference points.
*/

        val angle = sectionAngle * section

/*
Determine the next points on the base and top circumferences.
*/

        val nextBase = RichPoint(Point3D(cb.x + rb * angle.cos,
        cb.y + rb * angle.sin, cb.z), TexturePoint(uSpacing * section, vBase))
        val nextTop = RichPoint(Point3D(ct.x + rt * angle.cos,
        ct.y + rt * angle.sin, ct.z), TexturePoint(uSpacing * section, vTop))

/*
Create a face by listing the points in the appropriate order (we can define
each pair of triangular faces as a single Face instance).

Note that lastTop and lastBase are ahead of us on the circumference, not behind
us.
*/

        val face = new Face(nextTop :: nextBase :: lastBase :: lastTop :: Nil,
        smoothGroup)

/*
Prepend the resulting face to the list defining this mesh, and perform the next
iteration.
*/

        buildMesh(section - 1, nextBase, nextTop, face :: faces)
      }
    }

/*
Create the last top & bottom rich circumference points.
*/

    val endBase = RichPoint(Point3D(cb.x + rb, cb.y, cb.z), botRight)
    val endTop = RichPoint(Point3D(ct.x + rt, ct.y, ct.z),
    TexturePoint(botRight.u, vTop))

/*
Create the mesh from the List of a faces.
*/

    buildMesh(divisions - 1, endBase, endTop, Nil)
  }

/**
Construct an arc/circle/segment.

A arc is a 2D shape, but represented on the X-Y plane in 3D space, with a
defined center, radius, begin angle and draw through angle.

@param c 3D point defining centre of arc on the X-Y plane.

@param r Radius of the arc. This value must be greater than 0.

@param begin Angle at which drawing of the arc starts. This value must be in
the range [0, τ) radians (τ = 2π).

@param draw Angle through which the arc is drawn, counterclockwise from
`angle`. This value must be in the range [0, τ] radians (τ = 2π). If this value
is τ, then the result is a circle.

@param divisions Number of divisions into which the arc is divided. The higher
this number, the higher the resolution of the resulting arc. This value cannot
be less than three.

@return Mesh representing the resulting arc.
*/
  def arc(c: Point3D, r: Double, begin: Angle.Measure, draw: Angle.Measure,
  divisions: Int) = {

/*
Sanity checks.
*/

    assert(r > 0.0)
    assert(begin >= Angle.Zero && begin < Angle.τ)
    assert(draw >= Angle.Zero && draw <= Angle.τ)
    assert(divisions > 2)

/*
Create and return a (flat) conic mesh representing the arc. The "apex" of the
conic mesh and the "base center" are one and the same.

The entire texture map is used for this purpose.

Note: The arc is drawn with (implicitly) the down argument set to false.
However, since the arc is a plane, it is actually rendered with face normals
displayed both up and down, rather than just up.
*/

    conicMesh(c, r, c, divisions, beginAngle = begin, drawAngle = draw,
    smoothGroup = 1)
  }

/**
Construct a cone.

A cone is a 3D shape, with a base defined on the X-Y plane and an apex above
it, but not necessarily directly above it.

@param c 3D point defining centre of base on the X-Y plane.

@param r Radius of the base. This value must be greater than 0.

@param a 3D point defining the apex of the cone. This point must lie above the
base. That is `a.z` must be greater than `c.z`.

@param divisions Number of divisions into which the cone is divided around its
base. The higher this number, the higher the resolution of the resulting cone.
This value cannot be less than three.

@return Mesh representing the resulting cone.
*/
  def cone(c: Point3D, r: Double, a: Point3D, divisions: Int) = {

/*
Sanity checks.
*/

    assert(r > 0.0)
    assert(a.z > c.z)
    assert(divisions > 2)

/*
Create a (flat) conic mesh representing the base. The "apex" of the conic mesh
and the "base center" are one and the same.

The bottom half of the texture map is used for this purpose.
*/

    val base = conicMesh(c, r, c, divisions, TexturePoint(0.0f, 0.5f),
    TexturePoint.BottomRight, down = true, smoothGroup = 1)

/*
Create a conic mesh representing the walls of the cone.

The top half of the texture map is used for this purpose.
*/

    val walls = conicMesh(c, r, a, divisions, TexturePoint.Origin,
    TexturePoint(1.0f, 0.5f), smoothGroup = 2)

/*
Add the two meshes together and return.
*/

    base + walls
  }

/**
Construct a conic frustum.

A conic frustum is a 3D shape, with a base defined on the X-Y plane and a top
on another X-Y plane above it. The base and top have different radii.

@param cb 3D point defining centre of base on the X-Y plane.

@param rb Radius of the base. This value must be greater than 0.

@param ct 3D point defining centre of top on the X-Y plane.

@param rt Radius of the top. This value must be greater than 0.

@param divisions Number of divisions into which the conic frustum is divided
around its base. The higher this number, the higher the resolution of the
resulting conic frustum. This value cannot be less than three.

@return Mesh representing the resulting conic frustum.
*/
  def conicFrustum(cb: Point3D, rb: Double, ct: Point3D, rt: Double,
  divisions: Int) = {

/*
Sanity checks.
*/

    assert(rb > 0.0)
    assert(ct.z > cb.z)
    assert(rt > 0.0)
    assert(divisions > 2)

/*
Create a (flat) conic mesh representing the base. The "apex" of the conic mesh
and the "base center" are one and the same.

The bottom third of the texture map is used for this purpose.
*/

    val twoThirds =(1.0 / 3.0).toFloat
    val base = conicMesh(cb, rb, cb, divisions,
    TexturePoint(0.0f, twoThirds), TexturePoint.BottomRight, down = true,
    smoothGroup = 1)

/*
Create a circular wall mesh representing the walls of the conic frustum.

The middle third of the texture map is used for this purpose.
*/

    val oneThird =(1.0 / 3.0).toFloat
    val walls = circularWallMesh(cb, rb, ct, rt, divisions,
    TexturePoint(0.0f, oneThird), TexturePoint(1.0f, twoThirds),
    smoothGroup = 2)

/*
Create a (flat) conic mesh representing the top. The "apex" of the conic mesh
and the "base center" are one and the same.

The top third of the texture map is used for this purpose.
*/

    val top = conicMesh(ct, rt, ct, divisions, TexturePoint.Origin,
    TexturePoint(1.0f, oneThird), smoothGroup = 4) // scalastyle:ignore

/*
Add the meshes together and return.
*/

    base + walls + top
  }

/**
Construct a cylinder.

A cylinder is a 3D shape, with a base defined on the X-Y plane and a top above
it, but not necessarily directly above it. The cylinder walls seemlessly link
the base to its top.

@param cb 3D point defining centre of the base on the X-Y plane.

@param r Radius of the cylinder. This value must be greater than 0.

@param ct 3D point defining the center of the top of the cylinder also on the
X-Y plane. This point must lie above the base. That is `ct.z` must be greater
than `cb.z`.

@param divisions Number of divisions into which the cylinder is divided around
its circumference. The higher this number, the higher the resolution of the
resulting cylinder. This value cannot be less than three.

@return Mesh representing the resulting cylinder.
*/
  def cylinder(cb: Point3D, r: Double, ct: Point3D, divisions: Int) = {

/*
Sanity checks.
*/

    assert(r > 0.0)
    assert(ct.z > cb.z)
    assert(divisions > 2)

/*
Create a (flat) conic mesh representing the base. The "apex" of the conic mesh
and the "base center" are one and the same.

The bottom third of the texture map is used for this purpose.
*/

    val twoThirds =(2.0 / 3.0).toFloat
    val base = conicMesh(cb, r, cb, divisions, TexturePoint(0.0f, twoThirds),
    TexturePoint.BottomRight, down = true, smoothGroup = 1)

/*
Create a circular wall mesh representing the walls of the cylinder.

The middle third of the texture map is used for this purpose.
*/

    val oneThird =(1.0 / 3.0).toFloat
    val walls = circularWallMesh(cb, r, ct, r, divisions,
    TexturePoint(0.0f, oneThird), TexturePoint(1.0f, twoThirds),
    smoothGroup = 2)

/*
Create a (flat) conic mesh representing the top. The "apex" of the conic mesh
and the "base center" are one and the same.

The top third of the texture map is used for this purpose.
*/

    val top = conicMesh(ct, r, ct, divisions, TexturePoint.Origin,
    TexturePoint(1.0f, oneThird), smoothGroup = 4) // scalastyle:ignore

/*
Add the meshes together and return.
*/

    base + walls + top
  }

/**
Construct a hemisphere.

A hemisphere is a 3D shape, with a base defined on the X-Y plane and extending
above it as a hemisphere with the specified radius.

@param c 3D point defining centre of the base on the X-Y plane.

@param r Radius of the hemisphere. This value must be greater than 0.

@param divisions Number of divisions into which the hemisphere is divided
around its circumference. The higher this number, the higher the resolution of
the resulting hemisphere. This value cannot be less than three.

@return Mesh representing the resulting hemisphere.
*/
  def hemisphere(c: Point3D, r: Double, divisions: Int) = {

/*
Sanity checks.
*/

    assert(r > 0.0)
    assert(divisions > 2)

/*
The number of vertical divisions for the sphere is divisions / 2, rounded up to
the nearest whole number.
*/

    val vDiv = if(divisions % 2 == 0) divisions / 2 else 1 + divisions / 2

/*
The number of texture divisions is the number of vertical divisions + one for
the base.
*/

    val tDiv = vDiv + 1

/*
Create a (flat) conic mesh representing the base. The "apex" of the conic mesh
and the "base center" are one and the same.

The bottom tDiv'th of the texture map is used for this purpose.
*/

    val base = conicMesh(c, r, c, divisions, TexturePoint(0.0f,
   (vDiv.toDouble / tDiv.toDouble).toFloat), TexturePoint.BottomRight,
    down = true, smoothGroup = 1)

/*
Helper function create the circular walls of the hemisphere, including the
pole, and returning a mesh representing the entire hemisphere.
*/

    @tailrec
    def walls(wall: Int, prior: Mesh): Mesh = {

/*
Determine the latitude of the base of this wall.
*/

      val bl =(Angle.τ / 4) * wall / vDiv

/*
Determine the radius of the base of this wall.
*/

      val br = r * bl.cos

/*
Determine the height of the base of this wall.
*/

      val bh = c.z + r * bl.sin

/*
Point defining the center of the base of this wall.
*/

      val bc = Point3D(c.x, c.y, bh)

/*
If this is the last wall, then create the pole, add it to the prior meshes and
return the result.
*/

      if(wall == vDiv - 1) prior + conicMesh(bc, br,
      Point3D(c.x, c.y, c.z + r), divisions,
      botRight = TexturePoint(1.0f,(1.0 / tDiv).toFloat), smoothGroup = 2)

/*
Otherwise, create the current wall, and iterate again, adding it to the prior
meshes.
*/

      else {

/*
Determine the latitude of the top of the wall.
*/

        val tl =(Angle.τ / 4) *(wall + 1) / vDiv

/*
Determine the radius of the top of the wall.
*/

        val tr = r * tl.cos

/*
Determine the height of the top of the wall.
*/

        val th = c.z + r * tl.sin

/*
Point defining the center of the top of this wall.
*/

        val tc = Point3D(c.x, c.y, th)

/*
Create this wall.
*/

        val wallMesh = circularWallMesh(bc, br, tc, tr, divisions,
        TexturePoint(0.0f,((vDiv -(wall + 1)).toDouble /
        tDiv.toDouble).toFloat),
        TexturePoint(1.0f,((vDiv - wall).toDouble / tDiv.toDouble).toFloat),
        smoothGroup = 2)

/*
Append to the prior meshes and iterate for the next wall.
*/

        walls(wall + 1, prior + wallMesh)
      }
    }

/*
Create the sides of the hemisphere (everything above the base, with the
exception of the pole).
*/

    walls(0, base)
  }

/**
Construct a quadrilateral plane.

A quadrilateral is a 4-sided, 2D shape with coplanar vertices.

@param tl 3D point of the top-left-hand corner of the quadrilateral, as viewed
from the front face (determined by counterclockwise vertex winding).

@param bl 3D point of the bottom-left-hand corner of the quadrilateral, as
viewed from the front face (determined by counterclockwise vertex winding).

@param br 3D point of the bottom-right-hand corner of the quadrilateral, as
viewed from the front face (determined by counterclockwise vertex winding).

@param tr 3D point of the top-right-hand corner of the quadrilateral, as viewed
from the front face (determined by counterclockwise vertex winding).

@param ttl Texture coordinate to be mapped to the top-left-hand corner of the
quadrilateral, as viewed from the front face (determined by counterclockwise
vertex winding).

@param tbr Texture coordinate to be mapped to the bottom-right-hand corner of
the quadrilateral, as viewed from the front face (determined by
counterclockwise vertex winding).

@return Mesh representing the resulting quadrilateral.
*/
  def quadrilateral(tl: Point3D, bl: Point3D, br: Point3D, tr: Point3D,
  ttl: TexturePoint, tbr: TexturePoint) = {

/*
Create the list of rich points that will define the face by decorating the
vertices with the texture co-ordinates.
*/

    val faces = List(
      RichPoint(tl, ttl),
      RichPoint(bl, TexturePoint(ttl.u, tbr.v)),
      RichPoint(br, tbr),
      RichPoint(tr, TexturePoint(tbr.u, ttl.v))
    )

/*
Create and return the quadrilateral mesh.
*/

    new Mesh(List(new Face(faces, 0)))
  }


/**
Construct a triangular frustum.

A triangular frustum is a 3D shape, constructed from a triangular base and top,
which lie on nonintersecting X-Y planes, joined by three quadrilateral sides.


@param bc 3D point defining centre of the base on the X-Y plane.

@param bd Base dimension, which is the length of each side of the base's
equilateral triangle. This value must be greater than or equal to zero.

@param tc 3D point defining centre of the top on the X-Y plane.

@param td Base dimension, which is the length of each side of the top's
equilateral triangle. This value must be greater than or equal to zero.

@return Mesh representing the resulting triangular frustum.
*/
  def triangularFrustum(bc: Point3D, bd: Double, tc: Point3D, td: Double) = {

/*
Sanity checks.
*/

    assert(tc.z > bc.z)
    assert(bd >= 0.0)
    assert(td >= 0.0)
//    assert (bd > 0.0 || td > 0.0)

/*
Points making up the base (as points of the compass).
*/

    val theta = Angle.τ / 12.0
    val bsy = bc.y - bd * theta.tan / 2.0
    val bn = Point3D(bc.x, bc.y + bd /(theta.cos * 2.0), bc.z)
    val bsw = Point3D(bc.x - bd /2.0, bsy, bc.z)
    val bse = Point3D(bc.x + bd /2.0, bsy, bc.z)

/*
Points making up the top (as points of the compass).
*/

    val tsy = tc.y - td * theta.tan / 2.0
    val tn = Point3D(tc.x, tc.y + td /(theta.cos * 2.0), tc.z)
    val tsw = Point3D(tc.x - td /2.0, tsy, tc.z)
    val tse = Point3D(tc.x + td /2.0, tsy, tc.z)

/*
Create the base. This uses the middle third of the bottom third of the texture
map.
*/

    val oneThird =(1.0 / 3.0).toFloat
    val twoThirds =(2.0 / 3.0).toFloat
    val tp12 = TexturePoint(oneThird, twoThirds)
    val tp22 = TexturePoint(twoThirds, twoThirds)
    val baseFace = new Face(List(RichPoint(bn, TexturePoint(0.5f, 1.0f)),
    RichPoint(bse, tp22), RichPoint(bsw, tp12)))
    val base = new Mesh(List(baseFace))

/*
Now for the left side. This uses the first third of the middle third of the
texture map.
*/

    val left = quadrilateral(tn, bn, bsw, tsw, TexturePoint(0.0f, oneThird),
    tp12)

/*
Now for the front side. This uses the second third of the middle third of the
texture map.
*/

    val tp11 = TexturePoint(oneThird, oneThird)
    val front = quadrilateral(tsw, bsw, bse, tse, tp11, tp22)

/*
Now for the right side. This uses the third third of the middle third of the
texture map.
*/

    val tp21 = TexturePoint(twoThirds, oneThird)
    val right = quadrilateral(tse, bse, bn, tn, tp21,
    TexturePoint(1.0f, twoThirds))

/*
Now for the top side. This uses the middle third of the top third of the
texture map.
*/

    val topFace = new Face(List(RichPoint(tn, TexturePoint(0.5f, 0.0f)),
    RichPoint(tsw, tp11), RichPoint(bse, tp21)))
    val top = new Mesh(List(topFace))

/*
Now add all the sides and return the result.
*/

    base + left + front + right + top
  }

/**
Construct a rectangular frustum.

A rectangular frustum is a 3D shape with six quadrilateral faces, the top and
bottom being rectangular and on nonintersecting X-Y planes.

@param bc 3D point defining centre of the base on the X-Y plane.

@param bl Length of the base, measured along the local X-axis. This value must
be greater than or equal to zero.

@param bw Width of the base, measured along the local Y-axis. This value must
be greater than or equal to zero.

@param tc 3D point defining centre of the top on the X-Y plane.

@param tl Length of the top, measured along the local X-axis. This value must
be greater than or equal to zero.

@param tw Width of the top, measured along the local Y-axis. This value must be
greater than or equal to zero.

@return Mesh representing the resulting rectangular frustum.
*/
  def rectangularFrustum(bc: Point3D, bl: Double, bw: Double, tc: Point3D,
  tl: Double, tw: Double) = {

/*
Sanity checks.
*/

    assert(tc.z > bc.z)
    assert(bl >= 0.0)
    assert(bw >= 0.0)
    assert(tl >= 0.0)
    assert(tw >= 0.0)

/*
Points making up the base (as points of the compass).
*/

    val bne = Point3D(bc.x + bl / 2.0, bc.y + bw / 2.0, bc.z)
    val bnw = Point3D(bc.x - bl / 2.0, bc.y + bw / 2.0, bc.z)
    val bse = Point3D(bc.x + bl / 2.0, bc.y - bw / 2.0, bc.z)
    val bsw = Point3D(bc.x - bl / 2.0, bc.y - bw / 2.0, bc.z)

/*
Points making up the top (as points of the compass).
*/

    val tne = Point3D(tc.x + tl / 2.0, tc.y + tw / 2.0, tc.z)
    val tnw = Point3D(tc.x - tl / 2.0, tc.y + tw / 2.0, tc.z)
    val tse = Point3D(tc.x + tl / 2.0, tc.y - tw / 2.0, tc.z)
    val tsw = Point3D(tc.x - tl / 2.0, tc.y - tw / 2.0, tc.z)

/*
Create the base. This uses the second quarter of the bottom third of the
texture map.
*/

    val twoThirds =(2.0 / 3.0).toFloat
    val base = quadrilateral(bsw, bnw, bne, bse,
    TexturePoint(0.25f, twoThirds), TexturePoint(0.5f, 1.0f))

/*
Now for the left side. This uses the first quarter of the middle third of the
texture map.
*/

    val oneThird =(1.0 / 3.0).toFloat
    val left = quadrilateral(tnw, bnw, bsw, tsw,
    TexturePoint(0.0f, oneThird), TexturePoint(0.25f, twoThirds))

/*
Now for the front side. This uses the second quarter of the middle third of the
texture map.
*/

    val front = quadrilateral(tsw, bsw, bse, tse,
    TexturePoint(0.25f, oneThird), TexturePoint(0.5f, twoThirds))

/*
Now for the right side. This uses the third quarter of the middle third of the
texture map.
*/

    val right = quadrilateral(tse, bse, bne, tne,
    TexturePoint(0.5f, oneThird), TexturePoint(0.75f, twoThirds))

/*
Now for the back side. This uses the fourth quarter of the middle third of the
texture map.
*/

    val back = quadrilateral(tne, bne, bnw, tnw,
    TexturePoint(0.75f, oneThird), TexturePoint(1.0f, twoThirds))

/*
Now for the top side. This uses the second quarter of the top third of the
texture map.
*/

    val top = quadrilateral(tnw, tsw, tse, tne, TexturePoint(0.25f, 0.0f),
    TexturePoint(0.5f, oneThird))

/*
Now add all the sides and return the result.
*/

    base + left + front + right + back + top
  }
}