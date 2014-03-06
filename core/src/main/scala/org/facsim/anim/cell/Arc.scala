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
import org.facsim.anim.Mesh
import org.facsim.anim.Point3D
import org.facsim.measure.Angle
import scala.math.abs
import scala.math.max
import scala.math.min

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell arc & circle''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Arcs.html Arcs
& Circles]] for further information.

@constructor Construct a new arc primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
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

private [cell] final class Arc (scene: CellScene, parent: Option [Set])
extends Mesh2D (scene, parent) {

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

Second angle associated with this arc. This value must be positive, and it must
be within ± 360° of angle 1.
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
Create an arc mesh to represent this cell and return it.

The origin of the cell is at its center.

@return Mesh representing the cell.

@see [[org.facsim.anim.cell.Mesh3D.cellMesh]].
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh: Mesh = Mesh.arc (Point3D.Origin,
  radius, Angle (angle1, Angle.Degrees),
  Angle (angle2 - angle1, Angle.Degrees), Arc.divisions)
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
it's 8. For simplicity, we'll convert all arcs to have 16 divisions.
*/

  val divisions = 16
}