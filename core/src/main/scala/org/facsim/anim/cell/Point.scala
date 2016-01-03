/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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

import org.facsim.{assertNonNull, LibResource}
import org.facsim.anim.Point3D

//=============================================================================
/**
Class representing a basic cell point in 3D space.

@constructor Construct a new basic point from the cell data stream.

@param scene Reference to the CellScene of which this point is a part.

@param pointType Type of point represented by this instance.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
//=============================================================================

private [cell] class Point (scene: CellScene, pointType: Point.Value) {

/*
Sanity checks.
*/

  assertNonNull (scene)
  assertNonNull (pointType)

/**
Read the 3D point from the cell data stream.
*/

  private [cell] final val point = Point.read (scene, pointType)
}

//=============================================================================
/**
Utility enumeration object for processing cell file points.
*/
//=============================================================================

private [cell] object Point
extends Enumeration {

/**
Polyhedron point.
*/

  val Polyhedron = Value

/**
Text list point.
*/

  val TextList = Value

/**
Vector list point.
*/

  val VectorList = Value

/**
Point read string resource key.
*/

  private val ReadKey = "anim.cell.Point.read"

//-----------------------------------------------------------------------------
/**
Read a point from the cell scene and return it.

@param scene Reference to the CellScene of which this point is a part.

@param pointType Type of point represented by this instance.

@return Point read from the scene.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
//-----------------------------------------------------------------------------

  def read (scene: CellScene, pointType: Value): Point3D = {

/*
Sanity checks.
*/

    assertNonNull (scene)
    assertNonNull (pointType)

/**
Read the point's X coordinate.
*/

    val x = scene.readDouble (LibResource (ReadKey, pointType.id, 0))

/**
Read the point's Y coordinate.
*/

    val y = scene.readDouble (LibResource (ReadKey, pointType.id, 1))

/**
Read the point's Z coordinate.
*/

    val z = scene.readDouble (LibResource (ReadKey, pointType.id, 2))

/*
Return the point read.
*/

    Point3D (x, y, z)
  }
}