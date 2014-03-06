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

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/ConicFrustums.html
Conic Frustums]] for further information.

@since 0.0
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
  ("anim.cell.ConicFrustum.readDim", 0))

/**
Conic frustum top radius.

Top radius, measured on the X-Y plane.
*/

  private val topRadius = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.ConicFrustum.readDim", 1))

/**
ConicFrustum height (Z-dimension).

Height of the conic frustum measured along the Z-Axis.
*/

  private val height = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.ConicFrustum.readDim", 2))

/**
ConicFrustum top X-axis offset.
*/

  private val xOffset = scene.readDouble (LibResource
  ("anim.cell.ConicFrustum.readOffset", 0))

/**
ConicFrustum top Y-axis offset.
*/

  private val yOffset = scene.readDouble (LibResource
  ("anim.cell.ConicFrustum.readOffset", 1))

//-----------------------------------------------------------------------------
/*
Create a conic mesh to represent this cell and return it.

The origin of the cell is at the center of its base.

@return Mesh representing the cell.

@see [[org.facsim.anim.cell.Mesh3D.cellMesh]].
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh: Mesh =
  Mesh.conicFrustum (Point3D.Origin, baseRadius,
  Point3D (xOffset, yOffset, height), topRadius, ConicFrustum.divisions)
}

//=============================================================================
/**
ConicFrustum companion object.

@since 0.0
*/
//=============================================================================

private object ConicFrustum {

/**
Number of divisions per conic frustum.

The number of divisions for a fine conic frustum in AutoMod is 16, and for a
course conic frustum it's 8. For simplicity, we'll convert all conic frustums
to have 16 divisions.
*/

  val divisions = 16
}