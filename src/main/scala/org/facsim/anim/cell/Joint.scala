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
Abstract joint class.

''[[http://www.automod.com/ AutoMod®]] cell'' file format includes support for
two different types of kinematic joint:
  - Rotational, in which a ''cell'' and its contents can be rotated about an
    axis.
  - Translational, in which a ''cell'' and its contents can be translated along
    an axis.

In addition, ''AutoMod's cell'' files support the concept of a ''terminal
control frame'' (''TCF'')&mdash;a ''cell'' marking a location at which another
object can be attached.

Typically, joints and TCFs are implemented in separate sets, but$mdash;for some
reason$mdash;their data sets are tied together.  As a consequence, it is
possible for:
  - Joints to be defined without an accompanying TCF.
  - Joints to be defined with an accompanying TCF.
  - TCFs to be defined without an accompanying joint.

The latter case requires a ''null'' joint to be defined&mdash;it possesses TCF
data only.

This class is the base class for all types of Joint.

@constructor Create a new joint instrance.

@param scene Scene to which the associated ''cell'' element belongs.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Joints.html
Cell Joint Data]]

@since 0.0
*/
//=============================================================================

private [cell] abstract class Joint (scene: CellScene) extends NotNull {

/**
Joint data.
*/

  private val jointData = new JointData (scene)
}