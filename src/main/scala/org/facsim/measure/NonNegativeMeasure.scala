/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

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
with Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://www.facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://www.facsim.org/Documentation/CodingStandards/
===============================================================================
$Id$

Scala source file belonging to the org.facsim.facsimile.measure package.
*/
//=============================================================================

package org.facsim.facsimile.measure

//=============================================================================
/**
Abstract base class for all non-negative measurement type classes.

Measurement value type sub-classes cannot have negative values, which is a
useful characteristic of many measurement types (including [[Time]],
[[Temperature]], [[Mass]] etc.).

@param value Value of the measurement type in the corresponding $SI units.

@tparam T Actual measurement type.  This must be a sub-type of
NonNegativeMeasure.

@since 0.0-0
*/
//=============================================================================

private [measure] abstract class NonNegativeMeasure [T <: NonNegativeMeasure
[T]] (value: Double)
extends Measure [T] (value)
{

/*
If the value is negative, then report an error.
*/

  require (value >= 0.0)
}
