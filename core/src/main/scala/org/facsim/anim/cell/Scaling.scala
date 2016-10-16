/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
import scalafx.scene.transform.Scale

/**
Scaling object.
*/

private[cell] object Scaling {

/**
Read string resource key.
*/

  private val ReadKey = "anim.cell.Scaling.read"

/**
Read scale data from ''cell'' data stream.

@param scene Scene from which the scale data is to be read.

@return A scaling transformation to be applied to the associated node.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Scaling.html
Scaling]]
*/
  def read(scene: CellScene) = {

/*
Sanity checks.
*/

    assertNonNull(scene)

/*
Function to verify scaling values, which must all be != 0.
*/

    def verify(scale: Double) = scale != 0.0

/*
Read the X axis scaling from the data stream.
*/

    val x = scene.readDouble(verify, LibResource(ReadKey, 0))

/*
Read the Y axis translation from the data stream.
*/

    val y = scene.readDouble(verify, LibResource(ReadKey, 1))

/*
Read the Z axis translation from the data stream.
*/

    val z = scene.readDouble(verify, LibResource(ReadKey, 2))

/*
Convert to a Translate instance and return.
*/

    new Scale(x, y, z)
  }
}