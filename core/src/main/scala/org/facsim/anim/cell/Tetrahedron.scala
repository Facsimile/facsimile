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
import org.facsim.anim.{Mesh, Point3D}

//=============================================================================
/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell tetrahedron''
primitives.

@note The use by ''AutoMod'' of the term ''tetrahedron'' is a slight misnomer.
These shapes are more correctly described as
''[[http://en.wikipedia.org/wiki/Frustum triangular frusta]]''. A
''tetrahedon'' is a degenerate case in which the base side length, or top side
length - but not both - is zero. However, the term ''tetrahedron'' is used
throughout this file since it will be familiar to users of ''AutoMod'' & ''cell
files''.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Tetrahedron.html
Tetrahedra]] for further information.

@constructor Construct a new tetrahedron primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Tetrahedron.html
Tetrahedra]] for further information.
*/
//=============================================================================

private [cell] final class Tetrahedron (scene: CellScene, parent: Option [Set])
extends Mesh3D (scene, parent) {

/**
Tetrahedron base dimension.

Base side dimension of the equilateral triangle forming the base of the
tetrahedron, measured on the X-Y plane.
*/

  private val baseDim = scene.readDouble (_ >= 0.0, LibResource
  (Tetrahedron.ReadDimKey, 0))

/**
Tetrahedron top dimension.

Top side dimension of the equilateral triangle forming the top of the
tetrahedron, measured on the X-Y plane.
*/

  private val topDim = scene.readDouble (_ >= 0.0, LibResource
  (Tetrahedron.ReadDimKey, 1))

/**
Tetrahedron height (Z-dimension).

Height of the tetrahedron measured along the Z-Axis.
*/

  private val height = scene.readDouble (_ >= 0.0, LibResource
  (Tetrahedron.ReadDimKey, 2))

/**
Tetrahedron top X-axis offset.
*/

  private val xOffset = scene.readDouble (LibResource
  (Tetrahedron.ReadOffsetKey, 0))

/**
Tetrahedron top Y-axis offset.
*/

  private val yOffset = scene.readDouble (LibResource
  (Tetrahedron.ReadOffsetKey, 1))

//-----------------------------------------------------------------------------
/**
@inheritdoc

@note The origin of the tetrahedron is at its center of its base.
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh: Mesh =
  Mesh.triangularFrustum (Point3D.Origin, baseDim,
  Point3D (xOffset, yOffset, height), topDim)
}

//=============================================================================
/**
Tetrahedron companion object.
*/
//=============================================================================

private object Tetrahedron {

/**
Read dimension string resource key.
*/

  val ReadDimKey = "anim.cell.Tetrahedron.readDim"

/**
Read offset string resource key.
*/

  val ReadOffsetKey = "anim.cell.Tetrahedron.readOffset"
}