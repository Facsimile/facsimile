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

import org.facsim.{assertNonNull, LibResource}

//=============================================================================
/**
Joint data.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Joints.html
Cell Joint Data]] for further information.

@constructor Construct a new joint data instance from the data stream.

@param scene Reference to the CellScene of which this joint data is a part.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
//=============================================================================

private [cell] final class JointData (scene: CellScene) {

/*
Sanity checks.
*/

  assertNonNull (scene)

/**
Joint speed.
*/

  private [cell] val speed = scene.readDouble (_ >= 0.0, LibResource
  ("anim.cell.JointData.speed"))

/**
Joint minimum value.
*/

  private [cell] val min = scene.readDouble (LibResource
  ("anim.cell.JointData.min"))

/**
Joint maximum value.

This value must be ≥ the minimum value.
*/

  private [cell] val max = scene.readDouble (_ >= min, LibResource
  ("anim.cell.JointData.max", min))

/**
Joint current value.

This value must be in the range [minimum, maxium]
*/

  private [cell] val cur = scene.readDouble (value => value >= min &&
  value <= max, LibResource ("anim.cell.JointData.cur", min, max))

/**
TCF data present?
*/

  private [cell] val tcfPresent = scene.readBool (LibResource
  ("anim.cell.JointData.tcfPresent"))
}