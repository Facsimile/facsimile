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

import org.facsim.LibResource

//=============================================================================
/**
Class representing a 3D point with move/draw flag.

@todo Do something with the data read when there's an opportunity to do so.
Refer to [[org.facsim.anim.cell.VectorList!]] for further information.

@constructor Construct a new decorated point from the cell data stream.

@param scene Reference to the CellScene of which this point is a part.

@param isFirst If `true`, then this is the first point, which '''must''' be a
move point, rather than a draw point.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
//=============================================================================

private [cell] final class VectorListPoint (scene: CellScene, isFirst:Boolean)
extends Point (scene, Point.VectorList) {

/**
Read the point's move/draw flag.

A value of 0 indicates that this is a ''move'' point (''cursor'' is
repositioned to the point); a value of 1 indicates that this is a ''draw''
point, and that a 3D line should be drawn from the previous point (whether a
draw or a move point) to this point. As a consequence, the first point ''must''
be a move point.

@note Consecutive ''move'' points make no sense, but we do not disallow them.
Nor do we verify whether this point differs from the last point.
*/

  private val isDrawTo = scene.readBool (_ == 0 || !isFirst, LibResource
  ("anim.cell.VectorListPoint.read"))
}