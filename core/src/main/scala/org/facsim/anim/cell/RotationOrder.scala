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
import scalafx.geometry.Point3D
import scalafx.scene.transform.Rotate

//=============================================================================
/**
Cell rotation order enumeration.

Encodes ''[[http://www.automod.com/ AutoMod®]]'' axis rotation order and maps
them to the appropriate rotations.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Rotation.html
Rotation Order & Rotations]]
*/
//=============================================================================

private [cell] object RotationOrder
extends Enumeration {

/**
Vector to a sequence of axes corresponding to each enumeration.
*/

  private val axisSequence = Vector [List [Point3D]] (
    List (Rotate.XAxis, Rotate.YAxis, Rotate.ZAxis),
    List (Rotate.XAxis, Rotate.ZAxis, Rotate.YAxis),
    List (Rotate.YAxis, Rotate.XAxis, Rotate.ZAxis),
    List (Rotate.YAxis, Rotate.ZAxis, Rotate.XAxis),
    List (Rotate.ZAxis, Rotate.XAxis, Rotate.YAxis),
    List (Rotate.ZAxis, Rotate.YAxis, Rotate.XAxis)
  )

/**
X, then Y, then Z, code 0.
*/

  private [cell] val XYZ = Value

/**
X, then Z, then Y, code 1.
*/

  private [cell] val XZY = Value

/**
Y, then X, then Z, code 2.
*/

  private [cell] val YXZ = Value

/**
Y, then Z, then X, code 3.
*/

  private [cell] val YZX = Value

/**
Z, then X, then Y, code 4.
*/

  private [cell] val ZXY = Value

/**
Z, then Y, then X, code 5.
*/

  private [cell] val ZYX = Value

/**
Minimum rotation order code value.
*/

  private [cell] val minValue = 0

/**
Maximum rotation order code value.
*/

  private [cell] val maxValue = maxId - 1

//-----------------------------------------------------------------------------
/**
Conversion of ''cell'' axis rotation order to a sequence of ''ScalaFX'' axis
values.

@note This could be made an implicit function, but that might encourage the use
of ''cell'' axis rotation order values in regular code, when ideally we want to
bury them.

@param rotationOrder ''Cell'' axis rotation value to be converted.

@return Corresponding ''ScalaFX'' axis rotation sequence.
*/
//-----------------------------------------------------------------------------

  def toAxisSequence (rotationOrder: RotationOrder.Value) = {
    assertNonNull (rotationOrder)
    axisSequence (rotationOrder.id)
  } ensuring (_.length == 3)

//-----------------------------------------------------------------------------
/**
Verify an axis rotation order code.

@param rotationOrderCode Code for the rotation order to be verified.

@return `true` if the code maps to a valid axis rotation order, `false`
otherwise.
*/
//-----------------------------------------------------------------------------

  def verify (rotationOrderCode: Int) =
  rotationOrderCode >= minValue && rotationOrderCode <= maxValue

//-----------------------------------------------------------------------------
/**
Read axis rotation order code from ''cell'' data stream.

@param scene Scene from which the rotation order is to be read.

@return Sequence of ScalaFX axis rotations, in specified order.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Rotation.html
Rotation Order & Rotations]]
*/
//-----------------------------------------------------------------------------

  def read (scene: CellScene) = {

/*
Sanity checks.
*/

    assertNonNull (scene)

/*
Read the axis rotation code from the data stream.
*/

    val code = scene.readInt (verify, LibResource
    ("anim.cell.RotationOrder.read", minValue, maxValue))

/*
Convert to a sequence of ScalaFX axes and return.
*/

    axisSequence (code)
  }
}