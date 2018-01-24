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

import org.facsim.LibResource

/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell instance''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Instances.html
Instances]] for further information.

@constructor Construct a new instance primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Instances.html
Instances]] for further information.
*/

private[cell] final class Instance(scene: CellScene, parent: Option[Set])
extends Set(scene, parent) {

/**
Obtain definition reference.

This is a slightly complex process...

Firstly, we read the name of the definition from the cell file. If there is
already a definition with that name, then we retrieve and store a reference to
it. Otherwise, the definition follows immediate after the instance as a new
cell hierarchy that needs to be read in.
*/

  private val definition = {

/*
Retrieve the name of the definition from the file. The name cannot be an empty
string, or be made up of purely whitespace (although the delimiter should
ensure that doesn't happen).

There are rules governing the make up of cell element names, but it's probably
not worth enforcing them since there also seem to be common exceptions to those
rules. Maybe in the future...
*/

    val defName = scene.readString(!_.trim.isEmpty, LibResource
   ("anim.cell.Instance.name"))

/*
If we have a definition, then use it, otherwise read it from the cell data
stream.
*/

    scene.getDefinition(defName).getOrElse {
      val instDef = scene.readNextCell(None, true)
      assert(instDef.isInstanceOf[Definition]) // scalastyle:ignore

/*
If the definition's name doesn't match the name specified in the preceding
instance, then give an error.

@see [[https://github.com/Facsimile/facsimile/issues/1 Issue 1]].
*/

      val instDefName = instDef.name.getOrElse("")
      if(instDefName != defName) {
        throw new ParsingErrorException(LibResource
       ("anim.cell.Definition.name", defName, instDefName), null)
      }
      instDef
    }
  }

/*
@see [[org.facsim.anim.cell.Set!.getChildren]]

Report the definition as the children of this cell.
*/
  protected[cell] override def getChildren = List(definition)
}