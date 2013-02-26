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
Scala source file belonging to the org.facsim.measure package.
*/
//=============================================================================

package org.facsim.measure

//=============================================================================
/**
''Time'' physical quantity type.

Both relative and absolute times can be represented by this class.  Time values
cannot be negative.

All times are stored internally in seconds, which is the SI standard unit of
time.

@since 0.0
*/
//=============================================================================

object Time extends NonNegative {

/**
Units for time measured in ''milliseconds''.

In ''Facsimile'', a millisecond is defined as being exactly 1/1000 of a
''second'' (refer to [[org.facsim.measure.Time.seconds]] for the definition of
a second.
*/

  final val milliseconds = new TimeUnits (new LinearScaleConverter (1.0 /
  1000.0), "ms")

/**
Units for time measured in ''seconds''.

@note Seconds are the ''SI'' standard units for time measurement, and the units
that are used to stored time measurements internally in ''Facsimile''.

In ''Facsimile'', a second is defined in accordance with ''SI'' standards.

@see [[http://en.wikipedia.org/wiki/Second Second]] for further information.
*/

  final val seconds = new TimeUnits (SIConverter, "s")

/**
Units for time measured in ''minutes''.

In ''Facsimile'', a minute is defined as being exactly 60 ''seconds'' (refer to
[[org.facsim.measure.Time.seconds]] for the definition of a second.
*/

  final val minutes = new TimeUnits (new LinearScaleConverter (60.0), "min")

/**
Units for time measured in ''hours''.

In ''Facsimile'', an hour is defined as being exactly 60 ''minutes'' (refer to
[[org.facsim.measure.Time.minutes]] for the definition of a minute.
*/

  final val hours = new TimeUnits (new LinearScaleConverter (60.0 * 60.0),
  "h")

/**
Units for time measured in ''days''.

In ''Facsimile'', a day is defined as being exactly 24 ''hours'' (refer to
[[org.facsim.measure.Time.hours]] for the definition of an hour.
*/

  final val days = new TimeUnits (new LinearScaleConverter (60.0 * 60.0 *
  24.0), "d")

/**
Units for time measured in ''weeks''.

In ''Facsimile'', a week is defined as being exactly 7 ''days'' (refer to
[[org.facsim.measure.Time.days]] for the definition of a day.

@note This is the largest unit of time supported by ''Facsimile''.  Note that
months, years, decades, centuries, millenia, etc. vary in duration depending
upon a number of factors: leap seconds, leap years, days per month, etc.
Consequently, there is no simple ''standard'' definition for higher units of
time.
*/

  final val weeks = new TimeUnits (new LinearScaleConverter (60.0 * 60.0 * 24.0
  * 7.0), "wk")

/**
Physical quantity family for time measurements.
*/

  private [measure] final val family = Family (timeExponent = 1)

/**
@inheritdoc
*/

  final override type Measure = TimeMeasure

/**
@inheritdoc
*/

  final override type Units = TimeUnits

/**
@inheritdoc
*/

  final override val getSIUnits = seconds

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  private [measure] final override def apply (value: Double) = new
  TimeMeasure (value)

//-----------------------------------------------------------------------------
/**
''Time'' measurement class.

@constructor Create new time value.

@param value Time value in seconds.  This value must be finite and
non-negative.

@throws java.lang.IllegalArgumentException if '''value''' is not finite or is
negative.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class TimeMeasure private [measure] (value: Double) extends
  NonNegativeMeasure (value) {

//.............................................................................
/**
@inheritdoc
*/
//.............................................................................

    final override def getFamily = family
  }

//-----------------------------------------------------------------------------
/**
''Time'' unit of measurement family class.

@constructor Create new time unit of measurement.

@param converter Rules to be applied to convert a quantity measured in these
units to the standard ''Time SI'' units, ''seconds''.

@param symbol Symbol to be used when outputting measurement values in these
units.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class TimeUnits private [measure] (converter: Converter, symbol:
  String) extends AbstractUnits (converter, symbol)
}