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
import org.facsim.measure.Angle
import scala.annotation.tailrec
import scalafx.collections.ObservableFloatArray
import scalafx.collections.ObservableIntegerArray
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell hemisphere''
primitives.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Hemispheres.html
Hemispheres]] for further information.

@constructor Construct a new hemisphere primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Hemispheres.html
Hemispheres]] for further information.

@since 0.0
*/
//=============================================================================

private [cell] final class Hemisphere (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Hemisphere radius.

This value must be >= 0.
*/

  private val radius = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Hemisphere.read"))

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
Helper function to add points for the current band to an array of points.

Use the MeshUtils to generate the points for the base, and then add points for
the the two intermediate bands and the pole.

Note: This will give us (Hemisphere.bands - 1) points in the middle of the
hemisphere that we will not actually reference for faces. Shouldn't be any big
deal.
*/

      @tailrec
      def addBandPoints (count: Int, coords: ObservableFloatArray):
      ObservableFloatArray = {

/*
If we have no more bands, then return what we have.
*/

        if (count == 0) coords
        else {
          
/*
Calculate the latitude, radius and height of this band.
*/

          val latitude = Angle ((Hemisphere.bands - count) *
          Hemisphere.bandAngle, Angle.Degrees)
          val r = radius * latitude.cos
          val h = radius * latitude.sin

/*
Add the points for this band to the array and repeat.
*/

          addBandPoints (count - 1, coords ++ MeshUtils.circleCoordinates (r,
          h, Hemisphere.divisions, 0.0, 0.0))
        }
      }

/*
Create the array of points for each band and add the pole.

Note: The pole will be at (0, 0) on the X-Y plane, and will be radius units
above that point on the Z-axis.
*/

      addBandPoints (Hemisphere.bands, ObservableFloatArray.empty) ++
      Array (0.0f, 0.0f, radius.toFloat)
    }

/*
Now create the list of faces (triangles), constructed from indices of the
associated points defined above.
*/

    override val faces = {

/*
Helper function to add faces for the current band to an array of face vertex
indices.

Use the MeshUtils to generate the faces for each band. Note that the last band
is a cone, not a wall.
*/

      @tailrec
      def addBandFaces (count: Int, coords: ObservableIntegerArray):
      ObservableIntegerArray = {

/*
Calculate the index of the first vertex point on the base of the current band.
We need to add 1 to identify the first circumference point (the zeroth point in
each band is at the center).
*/

        val firstBandIndex = (3 - count) * Hemisphere.bandPoints + 1

/*
If this is the last band, then append a cone to the list of cones so far.
*/

        if (count == 1) coords ++ MeshUtils.coneFaces (Hemisphere.divisions,
        Hemisphere.poleIndex, firstBandIndex)

/*
Otherwise, re-run adding another wall of faces to the current array of faces.
*/

        else {
          addBandFaces (count - 1, coords ++ MeshUtils.wallFaces
          (Hemisphere.divisions, firstBandIndex))
        }
      }

/*
Create the array of points for the base and append the faces for the bands.
*/

      addBandFaces (Hemisphere.bands, MeshUtils.circleFaces
      (Hemisphere.divisions, 0))
    }

/*
Now create the smoothing face groups (face index map to smoothing group),
constructed from indices of the associated faces defined above.

The faces making up the base all belong to the base smoothing group (0), and
the faces making up the bands all belong to the wall smoothing group (1).
*/

    override val faceSmoothingGroups =
    ObservableIntegerArray.tabulate (Hemisphere.totalFaces) {
      face: Int =>
      if (face < Hemisphere.divisions) 0
      else 12
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
Hemisphere companion object.

@since 0.0
*/
//=============================================================================

private object Hemisphere {

/**
Number of divisions per hemisphere.

The number of divisions for a fine hemisphere in AutoMod is 16, and for a
course hemisphere it's 8. For simplicity, we'll convert all hemispheres to have
16 divisions.

Note that these divisions apply around the circumference of the base of the
hemipshere, and each band above it.
*/

  val divisions = 16

/**
Number of bands of faces per hemisphere.

There are only three bands of faces making up the hemisphere (AutoMod's was a
pretty low-resolution 3D system).
*/

  val bands = 3

/**
Increase in latitude at each band.

Each band is 90°/Hemisphere.bands higher "latitude" than the band before it
(the first band is also for the base). The height and radius of each band vary
accordingly.
*/

  val bandAngle = 90.0 / bands

/**
Vertex points per band.

Number of vertex points per band (including the base of the band only).
*/

  val bandPoints = divisions + 1

/**
Pole vertex index.

Index of the vertex corresponding to the pole. We don't need to add 1, since
point numbering starts at 0.
*/

  val poleIndex = bands * bandPoints

/**
Total number of faces making up a hemisphere.

There are 2 * divisions faces for each band, bar the last, divisions faces for
the last band, plus another set of divisions faces for base.

So, there are 2 * divisions * bands faces in total.
*/

  val totalFaces = divisions * bands * 2 
}