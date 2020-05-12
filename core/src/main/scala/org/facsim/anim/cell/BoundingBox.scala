/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-202020, Michael J Allen.

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

/**
Bounding Box companion object.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/BoundingBox.html
Cell Bounding Box]]
*/

private[cell] object BoundingBox {

/**
Read minimum value string resource key.
*/

  val ReadMinKey = "anim.cell.BoundingBox.read.min"

/**
Read minimum value string resource key.
*/

  val ReadMaxKey = "anim.cell.BoundingBox.read.max"

/**
Read in bounding box data and verify it makes sense.

If bounding box data is present, we must read it, but will ignore it. However,
note that we must verify the data read (in terms of the consistency of the
specified minimum & maximum values, rather than whether the bounding box is
appropriately sized). This is to ensure that we stay in sync with cell data and
so can report meaningful errors to the user.

@note This function must only be called if the current cell has bounding box
data present, and must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.

@param scene Scene to which this ''cell'' definition belongs.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/BoundingBox.html
Cell Bounding Box]]
*/
  final def read(scene: CellScene): Unit = {

/*
Sanity checks.
*/

    assertNonNull(scene)

/*
Read in the minimum and maximum X coordinate values. The maximum coordinate
must be greater than the minimum coordinate.
*/

    val minX = scene.readDouble(LibResource(ReadMinKey, 0))
    scene.readDouble(_ >= minX, LibResource(ReadMaxKey, 0, minX))

/*
Read in the minimum and maximum Y coordinate values. The maximum coordinate
must be greater than the minimum coordinate.
*/

    val minY = scene.readDouble(LibResource(ReadMinKey, 1))
    scene.readDouble(_ >= minY, LibResource(ReadMaxKey, 1, minY))

/*
Read in the minimum and maximum Z coordinate values. The maximum coordinate
must be greater than the minimum coordinate.
*/

    val minZ = scene.readDouble(LibResource(ReadMinKey, 2))
    scene.readDouble(_ >= minZ, LibResource(ReadMaxKey, 2, minZ))
  }
}