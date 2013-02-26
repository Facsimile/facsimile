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
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import scala.io.Source

//=============================================================================
/**
Cell Factory object.

Creates cell primitives given a cell type code number.
*/
//=============================================================================

private [cell] object CellFactory {

/**
Map associating cell type code with corresponding cell class.
*/

/*  private val classMap = Map [Int, Class [Cell]] (
    100 -> classOf [Triad],
    115 -> classOf [VectorList],
    125 -> classOf [Polyhedron],
    130 -> classOf [CoarseArc],
    131 -> classOf [FineArc],
    140 -> classOf [WorldText],
    141 -> classOf [ScreenFastText],
    142 -> classOf [ScreenNormalText],
    143 -> classOf [UnrotateFastText],
    144 -> classOf [UnrotateNormalText],
    150 -> classOf [WorldTextList],
    151 -> classOf [ScreenFastTextList],
    152 -> classOf [ScreenNormalTextList],
    153 -> classOf [UnrotateFastTextList],
    154 -> classOf [UnrotateNormalTextList],
    308 -> classOf [Definition],
    310 -> classOf [Trapezoid],
    311 -> classOf [Tetrahedron],
    315 -> classOf [Rectangle],
    330 -> classOf [CoarseHemisphere],
    331 -> classOf [FineHemisphere],
    340 -> classOf [CoarseCone],
    341 -> classOf [FineCone],
    350 -> classOf [CoarseCylinder],
    351 -> classOf [FineCylinder],
    360 -> classOf [CoarseFrustum],
    361 -> classOf [FineFrustum],
    388 -> classOf [FileReference],
    408 -> classOf [Instance],
    555 -> classOf [CompiledPicture],
    599 -> classOf [EmbeddedFile],
    700 -> classOf [Set],
    7000 -> classOf [Set],
    10000 -> classOf [Set]
  )
*/
//-----------------------------------------------------------------------------
/**
Cell factory.

Create an appropriate cell primitive instance from the specified source.  If
the created cell is a set (a non-leaf node that contains other cell
primitives), then this method will be called recursively and will fully
populate that set's contents (and any sub-set's contents) from the sources.

Each cell type is constructed via recursion, which implies that all cell
primitive classes must have an identical construction argument lists.

@param cellCode Cell code number to be looked up.  If there is no cell
primitive with the specified code, then a ParsingErrorException will result.
If the next source field is not a short integer, then an
IncorrectFormatException will result.

@return Cell primitive created from the specified stream.

@throws com.sun.j3d.loaders.IncorrectFormatException if the first source field
is not a short integer; this is a clear sign that the supplied source isn't
formatted as a cell file.

@throws com.sun.j3d.loaders.ParsingErrorException if a problem arises during
processing of the source data.
*/
//-----------------------------------------------------------------------------

  def factory (source: Source): Cell = null
}
