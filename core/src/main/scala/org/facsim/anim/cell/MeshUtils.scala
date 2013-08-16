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

import org.facsim.measure.Angle
import scalafx.collections.ObservableFloatArray
import scalafx.collections.ObservableIntegerArray

//=============================================================================
/**
Collection of utilities for drawing meshes.

@since 0.0
*/
//=============================================================================

private [cell] object MeshUtils {

//-----------------------------------------------------------------------------
/**
Create point array describing a circle.

@param radius Radius of the circle.

@param height Height of circle above the X-Y plane.

@param divisions Number of equal sectors that this circle is divided into. This
value must be > 0.

@param xOffset X offset of circle center.

@param yOffset Y offset of circle center.

@return Array of floats defining coordinates for points of the circle.  There
are three floats for each coordinate, and the total number of coordinates is
`divisions` + 1.  The first coordinate is for the center of the circle.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def circleCoordinates (radius: Double, height: Double,
  divisions: Int, xOffset: Double, yOffset: Double) = {
    circSectCoordinates (radius, height, Angle (0.0, Angle.radians), Angle
    (Angle.τ, Angle.radians), divisions, xOffset, yOffset)
  } ensuring (_.length == 3 * (divisions + 1))

//-----------------------------------------------------------------------------
/**
Create point array describing a sector of a circle.

@param radius Radius of the sector.

@param height Height of sector above the X-Y plane.

@param startAngle Angle (in radians) at which the sector's circumference
begins.

@param drawAngle Angle (in radians) through which the sector's circumference is
drawn.  This value must lie in the range [0, τ) (τ = 2π, refer to the
[[http://tauday.com/tau-manifesto Tau Manifesto]] for further details).

@param divisions Number of equal sectors that this sector is divided into. This
value must be > 0.

@param xOffset X offset of sector's circle center.

@param yOffset Y offset of sector's circle center.

@return Array of floats defining coordinates for points of the sector.  There
are three floats for each coordinate, and the total number of coordinates is
`divisions` + 2.  The first coordinate is for the center of the sector's
circle.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def sectorCoordinates (radius: Double, height: Double,
  startAngle: Angle.Measure, drawAngle: Angle.Measure, divisions: Int, xOffset:
  Double, yOffset: Double) = {
    assert (drawAngle < Angle (Angle.τ, Angle.radians))
    circSectCoordinates (radius, height, startAngle, drawAngle, divisions,
    xOffset, yOffset)
  } ensuring (_.length == 3 * (divisions + 2))

//-----------------------------------------------------------------------------
/**
Create point array describing a circle or sector of a circle.

@param radius Radius of the circle/sector.

@param height Height of circle/sector above the X-Y plane.

@param startAngle Angle (in radians) at which the circle's/sector's
circumference begins.

@param drawAngle Angle (in radians) through which the circle's/sector's
circumference is drawn.  This value must lie in the range [0, τ] (τ = 2π, refer
to the [[http://tauday.com/tau-manifesto Tau Manifesto]] for further details).
If this value equals 2π, then a circle will be defined, otherwise, a sector
will be defined.

@param divisions Number of equal sectors that this circle/sector is divided
into.  This value must be > 0.

@param xOffset X offset of circle/sector center.

@param yOffset Y offset of circle/sector center.

@return Array of floats defining coordinates for points of the circle.  There
are three floats for each coordinate, and the total number of coordinates is
`divisions` + 1 (for a circle) or `divisions` + 2 (for a sector).  The first
coordinate is for the center of the circle.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def circSectCoordinates (radius: Double, height: Double, startAngle:
  Angle.Measure, drawAngle: Angle.Measure, divisions: Int, xOffset: Double,
  yOffset: Double) = {

/*
Sanity checks.
*/

    assert (radius >= 0.0)
    assert (height >= 0.0)
    assert (startAngle >= Angle (0.0, Angle.radians) && startAngle <= Angle
    (Angle.τ, Angle.radians))
    assert (drawAngle >= Angle (0.0, Angle.radians) && drawAngle <= Angle
    (Angle.τ, Angle.radians))
    assert (divisions > 0)

/*
The z value of each coordinate is the height expressed as a float.
*/

    val z = height.toFloat

/*
Determine the number of circumference points that we will define.  If this is a
sector, then it will equal the number of divisions plus one; if a circle, then
it will equal the number of divisions (since the first and last point are the
same).
*/

    val sectors = divisions + (if (drawAngle == Angle.τ) 0 else 1)

/*
The angle between each successive pair of points on the circumference and the
center is drawAngle divided by the number of sectors.
*/

    val sliceAngle = drawAngle / sectors

/*
Create the array to store the points.

We add one to the number of sectors since we need to define the center of the
circle/sector.
*/

    val pointArray = new ObservableFloatArray ((sectors + 1) * 3)

/*
Set the first coordinate to be the center of the circle/sector.
*/

    pointArray (0) = xOffset.toFloat
    pointArray (1) = yOffset.toFloat
    pointArray (2) = z

/*
The remaining points are defined on the circumference.

X coordinates are determined as the radius * cosine of the angle from the
center to the point, measured about the Z-axis.  Y coordinates are determined
as the radius * sine of the angle from the centre to the point, measured about
the Z-axis.  Z coordinates are simply the height of the element.
*/

    for (p <- 1 to sectors) {
      val angle = startAngle + sliceAngle * (p - 1)
      val i = 3 * p
      pointArray (i) = (radius * angle.cos).toFloat
      pointArray (i + 1) = (radius * angle.sin).toFloat
      pointArray (i + 2) = z
    }

/*
Return the point array.
*/

    pointArray
  }

//-----------------------------------------------------------------------------
/**
Create face array defining a circle.

@param divisions Number of equal sectors that this circle is divided into.  One
triangular face is defined for each division.

@param centerIndex Index of the point in the associated point array that
identifies the center of the circle.  Points on the circumference of the circle
(numbering `divisions` in total) must follow immediately.

@return Array of point indices defining the points making up each triangular
face.  There are three points per face and a total of `divisions` faces.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def circleFaces (divisions: Int, centerIndex: Int) =
  circSectConeFaces (divisions, centerIndex, centerIndex + 1, true)

//-----------------------------------------------------------------------------
/**
Create face array defining a sector.

@param divisions Number of equal sectors that this sector is divided into.  One
triangular face is defined for each division.

@param centerIndex Index of the point in the associated point array that
identifies the cone apex.

@param firstFaceIndex Points on the circumference of the sector (numbering
`divisions` in total) must follow immediately.

@return Array of point indices defining the points making up each triangular
face.  There are three points per face and a total of `divisions` faces.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def coneFaces (divisions: Int, centerIndex: Int,
  firstFaceIndex: Int) = circSectConeFaces (divisions, centerIndex,
  firstFaceIndex, true)

//-----------------------------------------------------------------------------
/**
Create face array defining a sector.

@param divisions Number of equal sectors that this sector is divided into.  One
triangular face is defined for each division.

@param centerIndex Index of the point in the associated point array that
identifies the center of the sector's circle.  Points on the circumference of
the sector (numbering `divisions` + 1 in total) must follow immediately.

@return Array of point indices defining the points making up each triangular
face.  There are three points per face and a total of `divisions` faces.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def sectorFaces (divisions: Int, centerIndex: Int) =
  circSectConeFaces (divisions, centerIndex, centerIndex + 1, false)

//-----------------------------------------------------------------------------
/**
Create face array describing a circle or sector of a circle, or sides of a
cone.

@param divisions Number of equal sectors that this circle/sector/cone is
divided into.  One triangular face is defined for each division.

@param centerIndex Index of the point in the associated point array that
identifies the center of the circle/sector's circle/cone.

@param firstFaceIndex Index of the first points on the circumference of the
circle/sector/cone (numbering `divisions` if `closed`, or `divisions` + 1 if
not) must follow immediately.

@param closed If `true`, then a face is defined that links the first and last
circumference point with the center (and the number of circumference points
equals the number of divisions); if `false` then there is no face defined that
links the first and last circumference points (and the number of circumference
points is the number of divisions + 1).  In either case, the number of faces is
the same.
 
@return Array of point indices defining the points making up each triangular
face.  There are three points per face and a total of `divisions` faces.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def circSectConeFaces (divisions: Int, centerIndex: Int,
  firstFaceIndex: Int, closed: Boolean) = {

/*
Sanity checks.
*/

    assert (divisions > 0)
    assert (centerIndex >= 0)
    assert (firstFaceIndex >= 0)

/*
Create the face array.
*/

    val faceArray = new ObservableIntegerArray (divisions * 3)

/*
Define the points making up the first n - 1 faces.
*/

    for (f <- 0 until (divisions - 1)) {
      val i = f * 3
      val p = firstFaceIndex + i
      faceArray (i) = p
      faceArray (i + 1) = centerIndex
      faceArray (i + 2) = p + 1
    }

/*
For the final face, if this is a closed face, then the last face joins the last
circumference point to the first circumference point.  Otherwise, the final
face joins the penultimate circumference point (#division) to the final
circumference point (#division + 1).  In either case, the first two points
making up the face are the same.
*/

    val fi = divisions * 3
    val fp = firstFaceIndex + fi
    faceArray (fi) = fp
    faceArray (fi + 1) = centerIndex
    if (closed) faceArray (fi + 2) = firstFaceIndex
    else faceArray (fi + 2) = fp + 1

/*
Return the face array.
*/

    faceArray
  } ensuring (_.length == divisions * 3)

//-----------------------------------------------------------------------------
/**
Create face array describing a wall wrapping around two circles at either end.

This is suitable for the walls of a cylinder or conic frustum as well as for
the bands of a hemisphere.

The walls are closed, so that the last set of points defined wraps around to
first.

@param divisions Number of equal sectors that the associated base/top circles
are divided into.  Two triangular faces are defined for each division in the
wall.

@param firstFaceIndex Index of the first points on the circumference of the
lower circle/sector/cone.  The remaining points must follow immediately
(numbering 2 * `divisions` in total).

@return Array of point indices defining the points making up each triangular
face.  There are three points per face and a total of 2 * `divisions` faces.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def wallFaces (divisions: Int, firstFaceIndex: Int = 1) = {

/*
Create the face array.
*/

    val faceArray = new ObservableIntegerArray (divisions * 6)

/*
Index of first circumference point of the base.
*/

    val b1 = firstFaceIndex

/*
Index of first circumference point of the top.
*/

    val t1 = b1 + divisions + 1

/*
Create the wall of faces.  There are two triangular faces created for each
portion of the wall.  Note that point 0 and point divisions plus 1 define the
center of the base and top circles respectively.

Start off with the first n - 2 faces (we'll stitch the wall together at the
end.
*/

    for (f <- 0 until (divisions - 1)) {
      val i = f * 6
      val bp = f + b1
      val tp = f + t1
      faceArray (i) = bp
      faceArray (i + 1) = tp
      faceArray (i + 2) = bp + 1
      faceArray (i + 3) = tp
      faceArray (i + 4) = tp + 1
      faceArray (i + 5) = bp + 1
    }

/*
Now stitch together the last and first points together.
*/

    val fi = (divisions - 1) * 6
    val fbp = fi + b1
    val ftp = fi + t1
    faceArray (fi) = fbp
    faceArray (fi + 1) = ftp
    faceArray (fi + 2) = b1
    faceArray (fi + 3) = ftp
    faceArray (fi + 4) = t1
    faceArray (fi + 5) = b1

/*
Return the face array.
*/

    faceArray
  } ensuring (_.length == divisions * 6)
}