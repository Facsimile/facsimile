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

/**
Class representing _[[http://www.automod.com/ AutoMod®]] cell unrotate fast &
normal text_ primitives.

_Unrotate_ text is always aligned so that it faces the viewer. Unit height is
approximately 1.25% of the available screen height&mdash;it is currently
unsupported.

@note In _ScalaFX_/_JavaFX_, there is no difference between the _cell_
text styles of _normal_ and _fast_ (the former is a 3D font, the latter is
a screen font). Consequently, we represent both with a screen text class.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Text.html
Text]] for further information.

@constructor Construct a new unrotate text primitive from the data
stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Text.html
Text]] for further information.
*/

private[cell] final class UnrotateText(scene: CellScene,
parent: Option[Set])
extends Text(scene, parent, Text.Unrotate)