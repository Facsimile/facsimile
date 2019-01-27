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
Scala source file from the org.facsim.anim.cell package.
*/

package org.facsim.anim.cell

import org.facsim.LibResource
import org.facsim.anim.Mesh
import org.facsim.anim.Point3D
import org.facsim.anim.TexturePoint

/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell rectangle''
primitives.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Rectangles.html
Rectangles]] for further information.

@constructor Construct a new rectangle primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Rectangles.html
Rectangles]] for further information.
*/

private[cell] final class Rectangle(scene: CellScene, parent: Option[Set])
extends Mesh2D(scene, parent) {

/**
Rectangle Y-axis dimension.

Rectangle measured along the Y-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''length'',
although most people would refer to it as the ''depth'' or ''width''. Here, we
refer to it as the Y-axis dimension to avoid confusion.
*/

  private val yDim = scene.readDouble(_ >= 0.0, LibResource
 (Rectangle.ReadDimKey, 1))

/**
Rectangle X-axis dimension.

Rectangle measured along the X-Axis.

@note ''ACE'' (the ''AutoMod Creation Editor'') terms this the ''width'',
although most people would refer to it as the ''length''. Here, we refer to it
as the X-axis dimension to avoid confusion.
*/

  private val xDim = scene.readDouble(_ >= 0.0, LibResource
 (Rectangle.ReadDimKey, 0))

/*
Read, but discard, the X-axis offset.
*/

  scene.readDouble(LibResource(Rectangle.ReadOffsetKey, 0))

/*
Read, but discard, the Y-axis offset.
*/

  scene.readDouble(LibResource(Rectangle.ReadOffsetKey, 1))

/*
Create an arc mesh to represent this cell and return it.

The origin of the cell is at its center.

@return Mesh representing the cell.

@see [[org.facsim.anim.cell.Mesh3D.cellMesh]].
*/
  protected[cell] override def cellMesh: Mesh = {

/*
Create the necessary X and Y plane coordinates.
*/

    val xr = xDim / 2.0
    val xl = -xr
    val yt = yDim / 2.0
    val yb = -yt

/*
Create and return the quadrilateral using these coordinates.
*/

    Mesh.quadrilateral(Point3D(xl, yt, 0.0), Point3D(xl, yb, 0.0),
    Point3D(xr, yb, 0.0), Point3D(xr, yt, 0.0), TexturePoint.Origin,
    TexturePoint.BottomRight)
  }
}

/**
Rectangle companion object.
*/

private object Rectangle {

/**
Read dimension string resource key.
*/

  val ReadDimKey = "anim.cell.Rectangle.readDim"

/**
Read offset string resource key.
*/

  val ReadOffsetKey = "anim.cell.Rectangle.readOffset"
}