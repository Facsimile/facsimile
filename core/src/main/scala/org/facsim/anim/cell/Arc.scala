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
Scala source file from the org.facsim.anim.cell package.
*/
//======================================================================================================================

package org.facsim.anim.cell

import org.facsim.LibResource
import org.facsim.anim.{Mesh, Point3D}
import org.facsim.measure.Angle

//======================================================================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell arc & circle''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Arcs.html Arcs
& Circles]] for further information.

@constructor Construct a new arc primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Arcs.html Arcs
& Circles]] for further information.
*/
//======================================================================================================================

private [cell] final class Arc (scene: CellScene, parent: Option [Set])
extends Mesh2D (scene, parent) {

/**
Arc radius.

Arc radius, measured on the X-Y plane.
*/

  private val radius = scene.readDouble (_ >= 0.0,
  LibResource (Arc.ReadDimKey, 0))

/**
Arc start angle.

The starting angle for this arc. This may be any valid double value.
*/

  private val startAngle = Angle (scene.readDouble (LibResource
  (Arc.ReadAngleKey, 0)), Angle.Degrees).normalize

/**
Arc end angle.

This is the end angle for this arc. This may be any valid double value.
*/

  private val endAngle = Angle (scene.readDouble (LibResource
  (Arc.ReadAngleKey, 1)), Angle.Degrees).normalize

/**
Flag indicating whether this is a circle or a sector/arc.

If both start and end angle are 0 radians, then the.
*/

  private val isCircle = startAngle == Angle.Zero && endAngle == Angle.Zero

//----------------------------------------------------------------------------------------------------------------------
/**
@inheritdoc

@note The origin of the arc is the center of as the circle of which it is a
part.
*/
//----------------------------------------------------------------------------------------------------------------------

  protected [cell] override def cellMesh: Mesh = {
    val drawAngle = if (isCircle) {
      Angle.τ
    }
    else {
      (endAngle - startAngle).normalize
    }
    Mesh.arc (Point3D.Origin, radius, startAngle, drawAngle, Arc.Divisions)
  }
}

//======================================================================================================================
/**
Arc companion object.
*/
//======================================================================================================================

private object Arc {

/**
Arc read dimension string resource key.
*/

  private val ReadDimKey = "anim.cell.Arc.readDim"

/**
Arc read angle string resource key.
*/

  private val ReadAngleKey = "anim.cell.Arc.readAngle"

/**
Number of divisions per arc.

The number of divisions for a fine arc in AutoMod is 16, and for a course arc
it's 8. For simplicity, we'll convert all arcs to have 16 divisions.
*/

  private val Divisions = 16
}