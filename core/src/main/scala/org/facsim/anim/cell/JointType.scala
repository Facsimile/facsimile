/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

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

import org.facsim.LibResource

//=============================================================================
/**
Joint type enumeration.

Encodes ''[[http://www.automod.com/ AutoMod®]]'' joint type codes and maps them
to the corresponding joint types.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/JointType.html
Joint Type]]

@since 0.0
*/
//=============================================================================

private [cell] object JointType extends Enumeration {

/**
TCFOnly, having the joint type 0.

@note A ''terminal control frame'' (''TCF'') is not a joint proper joint, but a
location to which ''AutoMod'' will attach loads and/or vehicles.
*/

  private [cell] val TCFOnly = Value

/**
Rotational, having the joint type 1.
*/

  private [cell] val Rotational = Value

/**
Translational, having the joint type 2.
*/

  private [cell] val Translational = Value

/**
Minimum joint type code value.
*/

  private [cell] val minValue = 0

/**
Maximum joint type code vlaue.
*/

  private [cell] val maxValue = maxId - 1

//-----------------------------------------------------------------------------
/**
Verify a joint type code.

@param jointTypeCode Code for the joint type to be verified.

@return `true` if the code maps to a valid joint type, `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def verify (jointTypeCode: Int) = (jointTypeCode >= minValue
  && jointTypeCode <= maxValue)

//-----------------------------------------------------------------------------
/**
Read joint type from ''cell'' data stream.

@param scene Scene from which the joint type is to be read.

@return Joint type read, if valid.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/JointType.html
Joint Type]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def read (scene: CellScene) = {

/*
Read the joint type code from the data stream.
*/

    val code = scene.readInt (verify (_), LibResource
    ("anim.cell.JointType.read", minValue, maxValue))

/*
Convert to a joint type and return.
*/

    JointType (code)
  }

//-----------------------------------------------------------------------------
/**
Read joint ''cell'' data stream.

@param scene Scene from which the joint type is to be read.

@param flags The current ''cell'''s flags.

@return Joint read, if valid.
 
@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see
[[http://facsim.org/Documentation/Resources/AutoModCellFile/Joint.html Cell
Joint Data]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readJoint (scene: CellScene, flags: CellFlags) = {

/*
Determine the type of joint to be read and create it.
*/

    read (scene) match {
      case TCFOnly => new NullJoint (scene, flags)
      case Rotational => new RotationalJoint (scene, flags)
      case Translational => new TranslationalJoint (scene, flags)
    }
  }
}