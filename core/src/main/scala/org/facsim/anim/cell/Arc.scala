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
import scala.math.abs
import scala.math.max
import scala.math.min
import scalafx.collections.ObservableIntegerArray
import scalafx.scene.shape.TriangleMesh

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell arc & circle''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Arcs.html Arcs
& Circles]] for further information.

@constructor Construct a new arc primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive.  If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Arcs.html Arcs
& Circles]] for further information.

@since 0.0
*/
//=============================================================================

private [cell] final class Arc (scene: CellScene, parent: Option [Set]) extends
Mesh3D (scene, parent) {

/**
Arc radius.

Arc radius, measured on the X-Y plane.
*/

  private val radius = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Arc.readDim", 0))

/**
Arc first angle.

First angle associated with this arc.
*/

  private val angle1 = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.Arc.readDim", 1))

/**
Arc second angle.

Second angle associated with this arc.  This value must be positive, and it
must be within ± 360° of angle 1.
*/

  private val angle2 = {
    val minAngle = max (0.0, angle1 - 360.0)
    val maxAngle = angle1 + 360.0
    scene.readDouble ((angle: Double) => (angle >= minAngle) && (angle <=
    maxAngle), LibResource ("anim.cell.Arc.readDim2", minAngle, maxAngle))
  }

/**
Flag indicating whether this is a circle or a sector/arc.

If the difference between the two is 360°, then we're drawing a circle, not a
sector/arc.
*/

  private val isCircle = abs (abs (angle1 - angle2) - 360.0) < 1.0e-6

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Mesh3D!]]

The mesh is a custom TriangleMesh object.

Note that the arc is on the X-Y plane, with the center at (0, 0, 0), relative
to its parent.  Since this is not being drawn in wireframe mode, the arc is
either drawn as a circle or a sector of a circle.
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh = new TriangleMesh {

/*
Create the list of vertices.
*/

    override val points = {

/*
If the arc is actually a circle, then draw it as such; otherwise, draw it as a
sector.
*/

      if (isCircle) MeshUtils.circleCoordinates (radius, 0.0, Arc.divisions,
      0.0, 0.0)
      else {

/*
Find the start angle and the draw angle.  The start angle is the lower of the
two values and the draw angle larger angle less the smaller.  If the difference
between the two
is 360°, then we're drawing a circle, not a sector.
*/

        val startAngle = Angle (min (angle1, angle2), Angle.Degrees)
        val drawAngle = Angle (max (angle1, angle2), Angle.Degrees) -
        startAngle
        MeshUtils.sectorCoordinates (radius, 0.0, startAngle, drawAngle,
        Arc.divisions, 0.0, 0.0)
      }
    }

/*
Now create the list of faces (triangles), constructed from indices of the
associated points defined above.
*/

    override val faces = {

/*
Use the MeshUtils to generate the faces making up the circle or sector as
required.
*/

      if (isCircle) MeshUtils.circleFaces (Arc.divisions, 0)
      else MeshUtils.sectorFaces (Arc.divisions, 0)
    }

/*
Now create the smoothing face groups (face index map to smoothing group),
constructed from indices of the associated faces defined above.

All faces in the circle/sector belong to the same group, 0.
*/

    override val faceSmoothingGroups =
    ObservableIntegerArray.tabulate (Arc.divisions)(_ => 0)

/*
For now, don't define texture mapping coordinates.  We will typically not apply
textures to cells.
*/

    //override val getTexCoords =
  }
}

//=============================================================================
/**
Arc companion object.

@since 0.0
*/
//=============================================================================

private object Arc {

/**
Number of divisions per arc.

The number of divisions for a fine arc in AutoMod is 16, and for a course arc
it's 8.  For simplicity, we'll convert all arcs to have 16 divisions.
*/

  val divisions = 16
}