/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2019, Michael J Allen.

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
import scalafx.scene.Group

/**
Class representing ''[[http://www.automod.com/ AutoMod®]] cell triad''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Triads.html
Triads]] for further information.

@todo In a cell file, a triad is used as a debugging aid to demonstrate the
alignment of a primitive's local X-, Y- and Z-axes. It may be a useful tool for
users importing ''cell'' files into ''Facsimile''. However, for now, we do not
implement triads&mdash;they are processed, but otherwise ignored.

@constructor Construct a new triad primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Triads.html
Triads]] for further information.
*/

private[cell] final class Triad(scene: CellScene, parent: Option[Set])
extends Cell(scene, parent) {

/*
Read and disregard the "unknown" triad flag.

(The current theory is that this value is a visibility flag: a 0 value means
that the triad is invisible; any other value, that it's visible. It's academic
here as we simply ignore it.)
*/

  scene.readInt(LibResource("anim.cell.Triad.read"))

/**
@inheritdoc

@note Triads are not currently rendered.
*/
  private[cell] override def toNode = new Group()
}