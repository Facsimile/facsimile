/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2018, Michael J Allen.

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

import org.facsim.{assertNonNull, LibResource}
import scalafx.geometry.Point3D
import scalafx.scene.transform.Rotate

/**
Rotation object.
*/

private[cell] object Rotation {

/**
Read rotation data from ''cell'' data stream.

@param scene Scene from which the rotation data is to be read.

@return A sequence of rotation transformations to be applied to the associated
node.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Rotation.html
Rotation Order & Rotations]]
*/
  private[cell] def read(scene: CellScene) = {

/*
Sanity checks.
*/

  assertNonNull(scene)

/*
Helper function to read an axis rotation and change it into a rotation
transformation, building a list of such transformations.
*/

    def applyRotation(count: Int, axes: List[Point3D]): List[Rotate] = {
      if(axes.isEmpty) Nil
      else {

/*
Read the angle from the cell file.

NOTE: Angle is measured in degrees - in both the cell file and in ScalaFX, so
we do not need to worry about conversions.
*/

        val angle = scene.readDouble(LibResource("anim.cell.Rotation.read",
        count))

/*
If the angle is zero, then we can ignore it.
*/

        if(angle == 0.0) applyRotation(count + 1, axes.tail)

/*
Otherwise, create a new rotation transformation and prepend it to the list.
*/

        else new Rotate(angle, axes.head) :: applyRotation(count + 1,
        axes.tail)
      }
    }

/*
Create and return the rotation sequence, after reading the rotation order
sequence from the cell file.
*/

    applyRotation(1, RotationOrder.read(scene))
  }
}