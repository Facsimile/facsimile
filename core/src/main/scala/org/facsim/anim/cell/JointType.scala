/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2025, Michael J Allen.

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
Joint type enumeration.

Encodes _[[http://www.automod.com/ AutoMod®]]_ joint type codes and maps them
to the corresponding joint types.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/JointType.html
Joint Type]]
*/

private[cell] object JointType
extends Enumeration {

/**
TCFOnly, having the joint type 0.

@note A _terminal control frame_ (_TCF_) is not a joint proper joint, but a
location to which _AutoMod_ will attach loads and/or vehicles.
*/

  private[cell] val TCFOnly = Value

/**
Rotational, having the joint type 1.
*/

  private[cell] val Rotational = Value

/**
Translational, having the joint type 2.
*/

  private[cell] val Translational = Value

/**
Minimum joint type code value.
*/

  private[cell] val minValue = 0

/**
Maximum joint type code vlaue.
*/

  private[cell] val maxValue = maxId - 1

/**
Verify a joint type code.

@param jointTypeCode Code for the joint type to be verified.

@return `true` if the code maps to a valid joint type, `false` otherwise.
*/
  private[cell] def verify(jointTypeCode: Int) =(jointTypeCode >= minValue
  && jointTypeCode <= maxValue)

/**
Read joint type from _cell_ data stream.

@param scene Scene from which the joint type is to be read.

@return Joint type read, if valid.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/JointType.html
Joint Type]]
*/
  private[cell] def read(scene: CellScene) = {

/*
Sanity checks.
*/

    assertNonNull(scene)

/*
Read the joint type code from the data stream.
*/

    val code = scene.readInt(verify, LibResource("anim.cell.JointType.read",
    minValue, maxValue))

/*
Convert to a joint type and return.
*/

    JointType(code)
  }

/**
Read joint _cell_ data stream.

@param scene Scene from which the joint type is to be read.

@param flags The current _cell's_ flags.

@return Joint read, if valid.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Joint.html Cell
Joint Data]]
*/
  private[cell] def readJoint(scene: CellScene, flags: CellFlags) = {

/*
Sanity checks.
*/

    assertNonNull(scene)

/*
Determine the type of joint to be read and create it.
*/

    read(scene) match {
      case TCFOnly => new NullJoint(scene, flags)
      case Rotational => new RotationalJoint(scene, flags)
      case Translational => new TranslationalJoint(scene, flags)
    }
  }
}