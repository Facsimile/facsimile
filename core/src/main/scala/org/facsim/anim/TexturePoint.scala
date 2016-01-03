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
Scala source file from the org.facsim.anim package.
*/
//=============================================================================

package org.facsim.anim

import org.facsim.requireValid

//=============================================================================
/**
Texture map point.

A texture map is a rectangular image that is mapped onto one or more 3D faces,
giving them the texture of the image.

Texture points identify a location on the image that corresponds to the vertex
of a 3D face.

@constructor Create new texture map point.

@param u Horizontal texture map coordinate. This value must be in the range [0,
1], with 0 representing the extreme left edge of the texture map, and 1 the
extreme right edge. Horizontal texture map coordinates are conventionally
denoted by ''u''.

@param v Vertical texture map coordinate. This value must be in the range [0,
1], with 0 representing the extreme top edge of the texture map, and 1 the
extreme bottom edge. Vertical texture map coordinates are conventionally
denoted by ''v''.

@throws java.lang.IllegalArgumentException if either `u` or `v` is outside of
the range [0, 1].
*/
//=============================================================================

private [anim] final case class TexturePoint (u: Float, v: Float) {

/*
Sanity checks. Both co-ordinates must be in the range [0, 1].
*/

  requireValid (u, u >= 0.0F && u <= 1.0F)
  requireValid (v, v >= 0.0F && u <= 1.0F)

//-----------------------------------------------------------------------------
/**
Convert the coordinates to a list of floating point values.

@return List of the two texture coordinates, with the horizontal coordinate,
''u'', first and the vertical, ''v'', second.
*/
//-----------------------------------------------------------------------------

  def toList = List (u, v)
}

//=============================================================================
/**
Texture point companion object.
*/
//=============================================================================

private [anim] object TexturePoint {

/**
Bottom-left-hand corner of the associated texture image.
*/

  val BottomLeft = TexturePoint (0.0f, 1.0f)

/**
Bottom-right-hand corner of the associated texture image.
*/

  val BottomRight = TexturePoint (1.0f, 1.0f)

/**
Texture point origin.

This is mapped to the top-left-hand corner of the associated texture image.
*/

  val Origin = TexturePoint (0.0f, 0.0f)

/**
Top-left-hand corner of the associated texture image.
*/

  val TopLeft = Origin

/**
Top-right-hand corner of the associated texture image.
*/

  val TopRight = TexturePoint (1.0f, 0.0f)
}