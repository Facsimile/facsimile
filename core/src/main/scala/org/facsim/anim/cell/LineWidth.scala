/*
Facsimile: A Discrete-Event Simulation Library
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

import org.facsim.{assertNonNull, requireValid, LibResource}

/**
Class encapsulating ''cell'' line width pixel values.

@note This would be a value class, except that it is not possible to access
anything referenced via `this`&mdash;even an implied one. So, consequently,
value classes cannot be employed to self-verify their values. Or so it
currently appears (Scala 2.10).

@constructor Create a new valid line width value.

@param lineWidth Line width value in pixels.
*/

private[cell] final class LineWidth(private[cell] val lineWidth: Int) {

/*
Verify that the value is within the valid range.
*/

  requireValid(lineWidth, LineWidth.verify(lineWidth))
}

/**
Line width companion object.
*/

private[cell] object LineWidth {

/**
Minimum line width value in pixels.
*/

  private[cell] val minValue = 1

/**
Maximum line width value in pixels.
*/

  private[cell] val maxValue = 7

/**
Default line width value.
*/

  private[cell] val default = new LineWidth(1)

/**
Verify that an integer line width value is valid.

@param lineWidth Line width value, in pixels, to be verified.

@return `true` if `lineWidth` is a valid value, `false` otherwise.
*/
  private[cell] def verify(lineWidth: Int) = lineWidth >= minValue &&
  lineWidth <= maxValue

/**
Read line width from ''cell'' data stream.

@param scene Scene from which the line width is to be read.

@return Line width read, if valid.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/LineWidth.html
Line Width]]
*/
  private[cell] def read(scene: CellScene) = {

/*
Sanity checks.
*/

    assertNonNull(scene)

/*
Read the line width value from the data stream.
*/

    val code = scene.readInt(verify, LibResource("anim.cell.LineWidth.read",
    minValue, maxValue))

/*
Convert to a line width and return.
*/

    new LineWidth(code)
  }
}