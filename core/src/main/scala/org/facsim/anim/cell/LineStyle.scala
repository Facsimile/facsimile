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
Line style enumeration.

Encodes ''[[http://www.automod.com/ AutoMod®]]'' line style codes and maps them
to the corresponding line styles.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/LineStyle.html
Line Styles]]
*/
//=============================================================================

private [cell] object LineStyle
extends Enumeration {

/**
Solid, having the ''cell'' line style 0.
*/

  private [cell] val Solid = Value

/**
Dashed, having the ''cell'' line style 1.
*/

  private [cell] val Dashed = Value

/**
Dotted, having the ''cell'' line style 2.
*/

  private [cell] val Dotted = Value

/**
Halftone, having the ''cell'' line style 3.
*/

  private [cell] val Halftone = Value

/**
Default line style, which is used if an explicit style is not available.
*/

  private [cell] val Default = Solid

/**
Minimum line width value.
*/

  private [cell] val minValue = 0

/**
Maximum line width value.
*/

  private [cell] val maxValue = maxId - 1

//-----------------------------------------------------------------------------
/**
Verify a line style code.

@param lineStyleCode Code for the line style to be verified.

@return `true` if the code maps to a valid line style, `false` otherwise.
*/
//-----------------------------------------------------------------------------

  private [cell] def verify (lineStyleCode: Int) =
  (lineStyleCode >= minValue && lineStyleCode <= maxValue)

//-----------------------------------------------------------------------------
/**
Read line style from ''cell'' data stream.

@param scene Scene from which the line style is to be read.

@return Line style read, if valid.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/LineStyle.html
Line Styles]]
*/
//-----------------------------------------------------------------------------

  private [cell] def read (scene: CellScene) = {

/*
Read the line style code from the data stream.
*/

    val code = scene.readInt (verify (_), LibResource
    ("anim.cell.LineStyle.read", minValue, maxValue))

/*
Convert to a line style and return.
*/

    LineStyle (code)
  }
}