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
import scala.annotation.tailrec

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
Create a hemisphere mesh to represent this cell and return it.

The origin of the cell is at the center of its base.

@return Mesh representing the cell.

@see [[org.facsim.anim.cell.Mesh3D.cellMesh]].
*/
//-----------------------------------------------------------------------------

  protected [cell] override def cellMesh: Mesh =
  Mesh.hemisphere (Point3D.Origin, radius, Hemisphere.divisions)
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