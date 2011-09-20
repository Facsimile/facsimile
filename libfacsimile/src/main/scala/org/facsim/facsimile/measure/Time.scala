/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2011, Michael J Allen.

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
Measurement type for time values.

Both relative and absolute times can be represented by this class.  Time values
cannot be negative.

All times are stored internally in seconds, which is the SI standard unit of
time.

@since 0.0-0

@constructor Create a new time value corresponding to the indicated number of
seconds.  This constructor is private to the $measure package, ensuring that

*/
//=============================================================================

final class Time private [measure] (value: Double)
extends NonNegativeMeasure [Time] (value)
{

//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

  def this (value: Double, units: TimeUnit) = this (units.importValue (value))

//-----------------------------------------------------------------------------
/*
Create a new Time value with indicated value.
*/
//-----------------------------------------------------------------------------

  protected [measure] override final def newMeasurement (value: Double): Time =
  new Time (value)
}
