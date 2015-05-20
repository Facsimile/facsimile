/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2015, Michael J Allen.

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
import org.facsim.anim.{Mesh, Point3D}

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell conic frustum''
primitives.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/ConicFrustums.html
Conic Frustums]] for further information.

@constructor Construct a new conic frustum primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/ConicFrustums.html
Conic Frustums]] for further information.
*/
//=============================================================================

private [cell] final class ConicFrustum (scene: CellScene,
parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Conic frustum base radius.

Base radius, measured on the X-Y plane.
*/

  private val baseRadius = scene.readDouble (_ >= 0.0, LibResource
  (ConicFrustum.ReadDimKey, 0))

/**
Conic frustum top radius.

Top radius, measured on the X-Y plane.
*/

  private val topRadius = scene.readDouble (_ >= 0.0, LibResource
  (ConicFrustum.ReadDimKey, 1))

/**
ConicFrustum height (Z-dimension).

Height of the conic frustum measured along the Z-Axis.
*/

  private val height = scene.readDouble (_ >= 0.0, LibResource
  (ConicFrustum.ReadDimKey, 2))

/**
ConicFrustum top X-axis offset.
*/

  private val xOffset = scene.readDouble (LibResource
  (ConicFrustum.ReadOffsetKey, 0))

/**
ConicFrustum top Y-axis offset.
*/

  private val yOffset = scene.readDouble (LibResource
  (ConicFrustum.ReadOffsetKey, 1))

//-----------------------------------------------------------------------------
/**
@inheritdoc

@note The origin of the conic frustum is at the center of its base.
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh: Mesh =
  Mesh.conicFrustum (Point3D.Origin, baseRadius,
  Point3D (xOffset, yOffset, height), topRadius, ConicFrustum.Divisions)
}

//=============================================================================
/**
ConicFrustum companion object.
*/
//=============================================================================

private object ConicFrustum {

/**
Read dimension string resource key.
*/

  val ReadDimKey = "anim.cell.ConicFrustum.readDim"

/**
Read offset string resource key.
*/

  val ReadOffsetKey = "anim.cell.ConicFrustum.readOffset"

/**
Number of divisions per conic frustum.

The number of divisions for a fine conic frustum in AutoMod is 16, and for a
course conic frustum it's 8. For simplicity, we'll convert all conic frustums
to have 16 divisions.
*/

  val Divisions = 16
}