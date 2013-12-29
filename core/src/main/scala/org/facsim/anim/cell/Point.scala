/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

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
import scalafx.scene.transform.Translate

//=============================================================================
/**
Class representing a basic 3D point.

@constructor Construct a new basic point from the cell data stream.

@param scene Reference to the CellScene of which this point is a part.

@param pointType Type of point represented by this instance.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//=============================================================================

private [cell] class Point (scene: CellScene, pointType: Point.Value) {

/**
Read the point's X co-ordinate.
*/

  private final val x = scene.readDouble (LibResource ("anim.cell.Point.read",
  pointType.id, 0))

/**
Read the point's Y co-ordinate.
*/

  private final val y = scene.readDouble (LibResource ("anim.cell.Point.read",
  pointType.id, 1))

/**
Read the point's Z co-ordinate.
*/

  private final val z = scene.readDouble (LibResource ("anim.cell.Point.read",
  pointType.id, 2))

//-----------------------------------------------------------------------------
/**
Convert the point to a list of Float values.

@return List with the co-ordinates are members, as Floats.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] final def toFloatList = List (x.toFloat, y.toFloat, z.toFloat)

//-----------------------------------------------------------------------------
/**
Convert the point to a ''ScalaFX'' translation.

@return Translation along local axes by co-ordinate values.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] final def toTranslate = new Translate (x, y, z)
}

//=============================================================================
/**
Point companion object and point classification enumeration.

@since 0.0
*/
//=============================================================================

private [cell] object Point extends Enumeration {

/**
Polyhedron point.
*/

  private [cell] val Polyhedron = Value

/**
Text list point.
*/

  private [cell] val TextList = Value

/**
Vector list point.
*/

  private [cell] val VectorList = Value
}