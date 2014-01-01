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
Scala source file belonging to the org.facsim.measure package.
*/
//=============================================================================

package org.facsim.measure

import org.facsim.requireFinite

//=============================================================================
/**
Abstract base class for all ''Facsimile
[[http://en.wikipedia.org/wiki/Physical_quantity physical quantity]]''
elements.

Physical quantity types include specific types (such as
''[[org.facsim.measure.Time$]]'', ''[[org.facsim.measure.Length$]]'',
''[[org.facsim.measure.Angle$]]'', ''[[org.facsim.measure.Temperature$]]'',
''[[org.facsim.measure.Mass$]]'', ''[[org.facsim.measure.Velocity$]]'',
''[[org.facsim.measure.AngularAcceleration$]]'',
''[[org.facsim.measure.Area$]]'', ''[[org.facsim.measure.Volume$]]'', etc.), as
well as a ''[[org.facsim.measure.Generic$]]'' convenience type for the result
of multiplication and division between the specific types. Each of these types
has measurement values and units in which those measurements are expressed.

@see [[http://en.wikipedia.org/wiki/Physical_quantity Physical quantity]] on
''Wikipedia''.

@since 0.0
*/
/*
Developer notes:

This is an abstract class, rather than a trait, to prevent it from being used
as a base class. The rationale is that the implementation of this class, from
the viewpoint of a subclass, might change dramatically during Facsimile's
existence. Since there are no user-serviceable parts inside, it has been deemed
that the best approach is simply to keep a tight lid on things.
*/
//=============================================================================

abstract class Physical protected [measure] {

/**
Type for measurements of this physical quantity.

@since 0.0
*/

  type Measure <: PhysicalMeasure

/**
Type for units of this physical quantity.

@since 0.0
*/

  type Units <: PhysicalUnits

/**
''[[http://en.wikipedia.org/wiki/SI SI]]'' standard units for this physical
quantity.

@see [[[[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/

  val siUnits: Units

//-----------------------------------------------------------------------------
/**
User's preferred units for this physical quantity, or the associated
''[[http://en.wikipedia.org/wiki/SI SI]] units'' if no preference has been
specified.

@todo The SI units for this physical quantity are currently reported. It is
intended that&mdash;in future&mdash;this returns the user's preferred units, if
specified, or the SI units otherwise; the mechanism for specifying preferred
units is currently not implemented.

@return User's preferred units for this physical quantity.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def preferredUnits: Units = siUnits

//-----------------------------------------------------------------------------
/**
Abstract base class for all ''Facsimile
[[http://en.wikipedia.org/wiki/Physical_quantity physical quantity]]''
measurement classes.

Measurements are stored internally in the corresponding
''[[http://en.wikipedia.org/wiki/SI SI]]'' units for this physical quantity
family.

@constructor Create new measurement for the associated
''[[http://en.wikipedia.org/wiki/Physical_quantity physical quantity]]''.

@param value Value of the measurement expressed in the associated
''[[http://en.wikipedia.org/wiki/SI SI]]'' units. This value must be finite,
but sub-classes may impose additional restrictions.

@throws [[java.lang.IllegalArgumentException!]] if `value` is not finite or is
invalid for these units.

@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
[[http://en.wikipedia.org/ Wikipedia]].
*/
//-----------------------------------------------------------------------------

  abstract class PhysicalMeasure protected [measure]
  (protected [measure] final val value: Double)
  extends Equals
  with NotNull {

/*
Ensure that value is a finite number, and is not infinite or not-a-number
(NaN).
*/

    requireFinite (value)

//.............................................................................
/**
Physical quantity family to which this value belongs.

@return Physical quantity family to which this measurement belongs.
*/
//.............................................................................

    protected [measure] def family: Family

//.............................................................................
/**
Retrieve measurement expressed in the specified units.

@note This function is not public to prevent users from by-passing the
unit-protection logic provided by these physical quantity classes.

@param units Units in which the measurement is to be expressed.

@return Measurement in specified `units`.
*/
//.............................................................................

    @inline
    private [measure] final def inUnits (units: Units) =
    units.exportValue (value)

//.............................................................................
/**
Create new measurement in the same physical quantity family as this
measurement.

@param newMeasure New measurement expressed in this physical quantity's
''[[http://en.wikipedia.org/wiki/SI SI]]'' units.

@return Measurement in the same physical quantity family as this measurement.

@throws [[java.lang.IllegalArgumentException!]] if `newMeasure` is not finite
or is invalid for these units.
*/
//.............................................................................

    protected [measure] def createNew (newMeasure: Double): Measure

//.............................................................................
/**
Change the sign of a measurement value.

@note All measurements that do not permit negative values will throw exceptions
when this operation is invoked on a valid value.

@return Measurement value having a sign opposite that of this value.

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units.

@since 0.0
*/
//.............................................................................

    final def unary_- = createNew (-value)

//.............................................................................
/**
Add a measurement value of the same physical quantity family to this
measurement.

@param addend Measurement to be added to this measurement.

@return Sum of this measurement plus `addend`. The result is of the same
physical quantity family as this measurement.

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units.

@since 0.0
*/
//.............................................................................

    final def + (addend: Measure) = createNew (value + addend.value)

//.............................................................................
/**
Subtract a measurement of the same physical quantity family from this
measurement.

@param subtrahend Measurement to be subtracted from this measurement.

@return Difference of this measurement minus `subtrahend`. The result is of the
same physical quantity family as this measurement.

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units.

@since 0.0
*/
//.............................................................................

    final def - (subtrahend: Measure) = createNew (value - subtrahend.value)

//.............................................................................
/**
Multiply this measurement by specified factor.

@param factor Factor to be multiplied by this measurement.

@return Product of this measurement multiplied by `factor`. The result is of
the same physical quantity family as this measurement.

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units.

@since 0.0
*/
//.............................................................................

    final def * (factor: Double) = createNew (value * factor)

//.............................................................................
/**
Multiply this measurement by another measurement.

@param factor Measurement used to multiply this measurement.

@return Product of this measurement multiplied by `factor`. The result is a
measurement in a different physical quantity family to this measurement (unless
`factor` is a ''unitless'' generic measurement, in which case it will have the
same family as this measurement, although in generic form).

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units.

@since 0.0
*/
//.............................................................................

    final def * (factor: PhysicalMeasure) =
    Generic (value * factor.value, family * factor.family)

//.............................................................................
/**
Divide this measurement by specified divisor.

@param divisor Divisor to be applied to this measurement.

@return Quotient of this measurement divided by `divisor`. The result is of the
same physical quantity family as this measurement.

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units. For example, an infinite result will occur if
`divisor` is zero, which will cause this exception to be thrown.

@since 0.0
*/
//.............................................................................

    final def / (divisor: Double) = createNew (value / divisor)

//.............................................................................
/**
Divide this measurement by another measurement of the same physical quantity
family.

@param divisor Measurement to be applied as a divisor to this measurement.

@return Ratio of this measurement to the other measurement. The result is a
scalar value that has no associated measurement type.

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units. For example, an infinite result will occur if
`divisor` is zero, which will cause this exception to be thrown.

@since 0.0
*/
//.............................................................................

    final def / (divisor: Measure) = value / divisor.value

//.............................................................................
/**
Divide this measurement by another measurement.

@param divisor Measurement to be applied as a divisor to this measurement.

@return Quotient of this measurement divided by `divisor`. The result is a
measurement in a different physical quantity family to this measurement (unless
`divisor` is a ''unitless'' generic measurement, in which case it will have the
same family as this measurement, although in generic form).

@throws [[java.lang.IllegalArgumentException!]] if the result is not finite or
is invalid for these units. For example, an infinite result will occur if
`divisor` is zero, which will cause this exception to be thrown.

@since 0.0
*/
//.............................................................................

    final def / (divisor: PhysicalMeasure) =
    Generic (value / divisor.value, family / divisor.family)

//.............................................................................
/**
Determine whether this measurement can be compared equal to another
measurement.

@param that Object being tested for possible equality to a measurement
instance. If this value is `null`, the value can never be compared to a
measurement value and this method will return false.

@return `true` if `that` is of a type that can compare equal to this
measurement, or `false` otherwise.

@see Chapter 30 of '''Programming in Scala''', ''2nd Edition'', by Odersky,
Spoon & Venners.

@since 0.0
*/
//.............................................................................

    final override def canEqual (that: Any) = that match {

/*
If "that" is a subclass of PhysicalMeasure, then we can equal the other value
provided that "that" is a measurement of the same physical quantity family as
this instance. If they are for different families, then we cannot equal the
other value.
*/

      case other: PhysicalMeasure => family == other.family

/*
The default case, that "that" is not a measure value, means that we cannot
compare values as being equal.
*/

      case _ => false
    }

//.............................................................................
/**
Compare this measurement to another object for equality.

@note If two objects compare equal, then their hash codes must compare equal
too. Similarly, if two objects have different hash codes, then they must not
compare equal. However, if two objects have the same hash codes, they may or
may not compare equal, since hash codes do not necessary map to unique values.
That is, the balance of probability is that if two values are equal if they
have the same hash codes, however this is not guaranteed and should not be
relied upon.

@param that Object being tested for equality with this measurement.

@return `true` if `that` is a measurement belonging to the same family as this
measurement and both have the same exact value. `false` is returned if `that`
is `null` or is not a measurement value, if `that` is a measurement belonging
to a different family to this value, or if `that` has a different value to this
measurement.

@see [[scala.Any!.equals(Any)*]]

@since 0.0
*/
//.............................................................................

    final override def equals (that: Any) = that match {

/*
If the other object is a PhysicalMeasure subclass, and that value can be
compared as equal to this value, and they have the same value, then that equals
this.
*/

      case other: PhysicalMeasure => other.canEqual (this) &&
      value == other.value

/*
All other values (including null), cannot be equal to this value.
*/

      case _ => false
    }

//.............................................................................
/**
Return this measurement value's hash code.

@note If two objects compare equal, then their hash codes must compare equal
too. Similarly, if two objects have different hash codes, then they must not
compare equal. However, if two objects have the same hash codes, they may or
may not compare equal, since hash codes do not necessary map to unique values.
That is, the balance of probability is that if two values are equal if they
have the same hash codes, however this is not guaranteed and should not be
relied upon.

@see [[scala.Any!.hashCode()*]]

@since 0.0
*/
//.............................................................................

/*
Since measurements have a value and a family, we take the hash code of each and
use a binary XOR on them to obtain our hash code. This ensures that
measurements from different families, which do not compare equal, have
different hashes while also ensuring that equal values for the same families,
which do compare equal, have identical hash codes too. This fulfills the hash
code/equality contract.

There is probably no need to compare measurements to doubles (generic, unitless
measurements are, in effect, the same as Doubles), since we can implicitly
convert either to the other.
*/

    final override def hashCode = value.hashCode ^ family.hashCode

//.............................................................................
/**
Convert this measurement value to a string, expressed in the user's preferred
units.

@return A string containing the value of the measurement and the units in which
the measurement is expressed, in the user's preferred locale.

@see [[scala.Any!.toString()*]]

@since 0.0
*/
//.............................................................................

    final override def toString =
    preferredUnits.format (this.asInstanceOf [Measure])
  }

//-----------------------------------------------------------------------------
/**
Abstract base class for all physical quantity measurement units.

Each concrete subclass represents a single ''physical quantity unit family''.
For example, time units are represented by the
[[org.facsim.measure.Time$.TimeUnit!]] `PhysicalUnits` subclass.

Each unit family supports one or more ''units of measure''. For example, time
quantities may be measured in ''seconds'', ''minutes'', ''hours'', etc. These
units are represented by instances of the concrete `PhysicalUnits` subclass.
For each unit family, there is a standard unit of measure defined by the
''[[http://en.wikipedia.org/wiki/SI International System of
Units]]''&mdash;commonly abbreviated to ''SI Units''.

These standard units are used by ''Facsimile'' internally to store measurement
quantities (as immutable instances of
[[org.facsim.measure.PhysicalQuantity$.PhysicalMeasure!]] subclasses, with each
subclass corresponding to each measurement type.)  For example, the ''SI''
standard unit of measure for ''time'' is the ''second''; consequently,
''Facsimile'' stores and calculates all time quantities, internally, in
''seconds''. Adoption of the ''SI'' standard units simplifies the
implementation of physics calculations within ''Facsimile'' and provides a
clearly-defined basis for developing simulation models of the real-world.
(Many other simulation modeling tools suffer from ''unit of measure
confusion'', both internally and externally, creating a wide variety of
problems.)

However, it is unreasonable to expect that ''Facsimile'' users would be
comfortable entering and reviewing data solely in these units. For instance,
the ''SI'' standard unit of measure for ''angles'' is the ''radian''$mdash;and
there are few people who don't find the ''degree'' a far more intuitive
alternative. Similarly, users in the United States&mdash;or their
customers$mdash;might prefer ''feet'' & ''inches'', ''pounds'', ''Fahrenheit'',
etc. to their metric equivalents. Consequently, ''Facsimile'' allows users to
work with whichever units they prefer. ''Facsimile'' converts values to the
standard ''SI'' units on input and converts them to the required units on
output.

@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
[[http://en.wikipedia.org/]].

@constructor Create a new unit of measure for this physical quantity.

@since 0.0
*/
//-----------------------------------------------------------------------------

  abstract class PhysicalUnits protected [measure]
  extends Converter
  with NotNull {

//.............................................................................
/**
Format a value in these units for output.

@param measure Measurement value to be output, preferably expressed in the
user's preferred units. If a ''unitless'' generic measurement value, the value
is output ''as is'' without any specified units.

@return Formatted string, containing the value and the units (if any).
*/
//.............................................................................

    private [measure] def format (measure: Measure): String
  }
}