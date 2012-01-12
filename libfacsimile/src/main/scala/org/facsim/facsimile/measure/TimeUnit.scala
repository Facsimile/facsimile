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
Class representing a unit of time measurement.

@since 0.0-0
*/
//=============================================================================

final class TimeUnit private [measure] (converter: LinearFactorConverter,
keyStub: String)
extends UnitOfMeasure (converter, keyStub + ".singular", keyStub + ".plural",
keyStub + ".symbol") {

//-----------------------------------------------------------------------------
/*
Compare this object to another for equality.

@see UnitOfMeasure.typeNameKey (that: Any)
*/
//-----------------------------------------------------------------------------

  protected [measure] def typeNameKey (): String = "measure.timeUnit.name"
}

//=============================================================================
/**
Set of pre-defined time measurement units.

@since 0.0-0
*/
//=============================================================================

object TimeUnit {

/**
Multiplication factor to convert milliseconds into seconds.
*/

  private val secondsPerMillisecond = 1.0 / 1000.0

/**
Multiplication factor to convert milliseconds into seconds.
*/

  private val secondsPerMinute = 60.0

/**
Multiplication factor to convert minutes into seconds.
*/

  private val secondsPerHour = 60.0 * secondsPerMinute

/**
Multiplication factor to convert days into seconds.
*/

  private val secondsPerDay = 24.0 * secondsPerHour

/**
Multiplication factor to convert weeks into seconds.
*/

  private val secondsPerWeek = 7.0 * secondsPerDay

/**
Units for time measured in milliseconds.

@since 0.0-0
*/

  val milliseconds = new TimeUnit (new LinearFactorConverter
  (secondsPerMillisecond), "measure.timeUnit.millisecond")

/**
Units for time measured in seconds.

Seconds are the $SI standard unit of time measurement.

@since 0.0-0
*/

  val seconds = new TimeUnit (new LinearFactorConverter (1.0),
  "measure.timeUnit.second")

/**
Units for time measured in minutes.

@since 0.0-0
*/

  val minutes = new TimeUnit (new LinearFactorConverter (secondsPerMinute),
  "measure.timeUnit.minute")

/**
Units for time measured in hours.

@since 0.0-0
*/

  val hours = new TimeUnit (new LinearFactorConverter (secondsPerHour),
  "measure.timeUnit.hour")

/**
Units for time measured in days.

@since 0.0-0
*/

  val days = new TimeUnit (new LinearFactorConverter (secondsPerDay),
  "measure.timeUnit.day")

/**
Units for time measured in weeks.

@since 0.0-0
*/

  val weeks = new TimeUnit (new LinearFactorConverter (secondsPerWeek),
  "measure.timeUnit.week")
}