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

import org.facsim.requireValid

//=============================================================================
/**
Class encapsulating ''cell'' line width pixel values.

@note This would be a value class, except that it is not possible to access
anything referenced via `this`&mdash;even an implied one.  So, consequently,
value classes cannot be employed to self-verify their values.  Or so it
currently appears (Scala 2.10).

@constructor Create a new valid line width value.

@param lineWidth Line width value in pixels.

@since 0.0
*/
//=============================================================================

private [cell] final class LineWidth (final val lineWidth: Int) {

/*
Verify that the value is within the valid range.
*/

  requireValid (lineWidth, verify (lineWidth))
}

//=============================================================================
/**
Line width companion object.

@since 0.0
*/
//=============================================================================

private [cell] object LineWidth {

/**
Minimum line width value in pixels.
*/

  final val minValue = 1

/**
Maximum line width value in pixels.
*/

  final val maxValue = 7

/**
Default line width value.
*/

  final val default = new LineWidth (1)

//-----------------------------------------------------------------------------
/**
Verify that an integer line width value is valid.

@param lineWidth Line width value, in pixels, to be verified.

@return `true` if `lineWidth` is a valid value, `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def verify (lineWidth: Int) = (lineWidth >= minValue && lineWidth <=
  maxValue)
}