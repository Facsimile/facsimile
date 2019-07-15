/*
Facsimile: A Discrete-Event Simulation Library
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
import scalafx.scene.transform.Transform

/**
''[[http://www.automod.com/ AutoMod®]] cell'' transformation.

In ''AutoMod'', a ''transformation'' is called ''geometry data''. It
encompasses a translation along each local axis, a rotation about each local
axis (applied in a specified sequence) and a scaling along each local axis.

@constructor Create a new transformation (also known as a ''cell geometry''
instance from the ''cell'' data stream.

@param scene ''Cell'' scene from which the transformation is to be read.

@param inMatrixForm Flag indicating the format of the transformation data:
`true` indicates that the transformation is specified as a 4x4 affine matrix;
`false` indicates that the transformation is specified in regular format.

@todo Implement support for reading the transformation in matrix form.
*/

private[cell] final class Transformation(scene: CellScene,
inMatrixForm: Boolean) {

/*
Matrix form not implemented yet. See Issue #10 for further information.
*/

  if(inMatrixForm) throw new NotImplementedError(LibResource
 ("anim.cell.Transformation.matrixForm"))

/**
Read the translation data from the stream.
*/

  private val translate = Translation.read(scene)

/**
Read the rotation data from the stream.
*/

  private val rotate = Rotation.read(scene)

/**
Read the scaling data from the stream.
*/

  private val scaling = Scaling.read(scene)

/**
Report this transformation for use by the associated cell.

@return A sequence of transformations to be applied to the cell.
*/
  private[cell] def toList: List[Transform] = translate ::(rotate ::: List
 (scaling))
}