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

//=============================================================================
/**
Cell rotation order enumeration.

Encodes ''[[http://www.automod.com/ AutoMod®]]'' axis rotation order and maps
them to the appropriate rotations.

@see [http://facsim.org/Documentation/Resources/AutoModCellFile/Rotation.html
Rotation Order & Rotations]]

@since 0.0
*/
//=============================================================================

private [cell] object RotationOrder extends Enumeration {

/**
X, then Y, then Z, code 0.
*/

  final val XYZ = Value

/**
X, then Z, then Y, code 1.
*/

  final val XZY = Value

/**
Y, then X, then Z, code 2.
*/

  final val YXZ = Value

/**
Y, then Z, then X, code 3.
*/

  final val YZX = Value

/**
Z, then X, then Y, code 4.
*/

  final val ZXY = Value

/**
Z, then Y, then X, code 5.
*/

  final val ZYX = Value

/**
Minimum rotation order code value.
*/

  final val minValue = 0

/**
Maximum rotation order code value.
*/

  final val maxValue = maxId - 1
}