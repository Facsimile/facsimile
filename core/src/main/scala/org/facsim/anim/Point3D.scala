/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2023, Michael J Allen.

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
Scala source file from the org.facsim.anim package.
*/

package org.facsim.anim

import org.facsim.requireFinite
import scalafx.scene.transform.Translate

/**
Defines a point in 3D animation space.

The defined animation point may utilize world coordinates, or may be relative
to a parent object (with local _X_-, _Y_- and _Z_-axes).

@constructor Create new 3D animation point.

@param x _X_-axis coordinate. This value must be finite.

@param y _Y_-axis coordinate. This value must be finite.

@param z _Z_-axis coordinate. This value must be finite.

@throws IllegalArgumentException if `x`, `y` or `z` has a non-finite
value.
*/

private[anim] final case class Point3D(x: Double, y: Double, z: Double) {

/*
Sanity checks.
*/

  requireFinite(x)
  requireFinite(y)
  requireFinite(z)

/**
Convert this point into a List of coordinate values.

@return List of coordinate values, with _x_ coordinate first, _y_
coordinate second, and _z_ coordinate third.
*/
  private[anim] def toList = List(x, y, z)

/**
Convert this point into a List of single-precision coordinate values.

@return List of coordinate values as single-precision floating point values,
with _x_ coordinate first, _y_ coordinate second, and _z_ coordinate
third.
*/
  private[anim] def toFloatList = List(x.toFloat, y.toFloat, z.toFloat)

/**
Convert this point to a _ScalaFX_ translation.

@return Translation along local axes by coordinate values.
*/
  private[anim] def toTranslate = new Translate(x, y, z)
}

/**
Point3D companion object.
*/

private[anim] object Point3D {

/**
Origin.
*/

  private[anim] val Origin = Point3D(0.0, 0.0, 0.0)
}