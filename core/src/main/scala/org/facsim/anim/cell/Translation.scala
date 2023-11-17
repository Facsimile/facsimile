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
Scala source file from the org.facsim.anim.cell package.
*/

package org.facsim.anim.cell

import org.facsim.LibResource
import scalafx.scene.transform.Translate

/**
Translation object.
*/

private[cell] object Translation {

/**
Read dimension string resource key.
*/

  val ReadKey = "anim.cell.Translation.read"

/**
Read translation data from _cell_ data stream.

@param scene Scene from which the translation data is to be read.

@return A translation transformation to be applied to the associated node.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Translation.html
Translation]]
*/
  private[cell] def read(scene: CellScene) = {

/*
Read the X axis translation from the data stream.
*/

    val x = scene.readDouble(LibResource(ReadKey, 0))

/*
Read the Y axis translation from the data stream.
*/

    val y = scene.readDouble(LibResource(ReadKey, 1))

/*
Read the Z axis translation from the data stream.
*/

    val z = scene.readDouble(LibResource(ReadKey, 2))

/*
Convert to a Translate instance and return.
*/

    new Translate(x, y, z)
  }
}