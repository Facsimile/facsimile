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
Scala source file belonging to the org.facsim.measure package.
*/
//=============================================================================

package org.facsim.measure

import org.facsim.requireFiniteValue

//=============================================================================
/**
Abstract base class for all physical quantity types.

Physical quantity types include specific types, such as ''time'', ''length'',
''plane angle'', ''temperature'', ''mass'', ''linear velocity'', ''angular
acceleration'', ''area'', ''volume'', etc., as well as a ''generic''
convenience type for the result of multiplication and division between the
specific types.  Each of these types has measurement values and units in which
those measurements are expressed.

@since 0.0
*/
//=============================================================================

private [measure] abstract class PhysicalQuantity {

/**
Type of measurement for this physical quantity.
*/

  type Measure <: AbstractMeasure

/**
Type for units of this physical quantity.
*/

  type Units <: AbstractUnits

/**
SI units for this physical quantity.
*/

  val getSIUnits: Units

//-----------------------------------------------------------------------------
/**
Retrieve the user's preferred units for this physical quantity.

@return User's preferred units for this physical quantity, or the associated
''[[http://en.wikipedia.org/wiki/SI SI]] units'' if no preference has been
specified.

@todo This function currently reports the SI units for this physical quantity.
It is intended that&mdash;in future&mdash;this function returns the user's
preferred units, if present, or the SI units otherwise; the mechanism for
specifying preferred units is currently not implemented.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def getPreferredUnits: Units = getSIUnits

//-----------------------------------------------------------------------------
/**
Abstract base class for all ''Facsimile'' physical quantity measurement
classes.

Underlying physical quantity measurement values are stored in the
''[[http://en.wikipedia.org/wiki/SI SI]] units'' for the corresponding unit
family.

No subclass should add data members, with the exception of the
[[org.facsim.measure.Generic$.GenericMeasure!]] class, which additionally has
a family data member identifying the physical quantity family that the generic
measure represents.

@constructor New base measurement value in associated ''SI'' units.

@param value Value of the measurement type in the associated ''SI'' units.
This value must be finite&mdash;it cannot be infinite or ''not-a-number''.
Sub-classes may impose additional restrictions.

@throws java.lang.IllegalArgumentException if '''value''' in the ''SI'' units
is not valid.

@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
[[http://en.wikipedia.org/ Wikipedia]].

@since 0.0
*/
//-----------------------------------------------------------------------------

  abstract class AbstractMeasure private [measure] (private final val value:
  Double) extends Equals with NotNull {

/*
Sanity tests.  Ensure that value is a finite number, and is not infinite or
not-a-number (NaN).
*/

    requireFiniteValue ("value", value)

//.............................................................................
/**
Retrieve measurement value in the associated ''SI'' units.

@note This function is not public because it introduces the potential for unit
confusion.  Measurements can only be manipulated by users as
[[org.facsim.measure.PhysicalQuantity.AbstractMeasure!]] subclass instances,
not as raw values.  Allowing access to raw values encourages by-passing of the
unit protection logic provided by these measurement classes.

@return Measurement value in associated ''[[http://en.wikipedia.org/wiki/SI
SI]] units''.

@since 0.0
*/
//.............................................................................

    @inline
    private [measure] final def getValue = value

//.............................................................................
/**
Retrieve measurement value in the specified units.

@note This function is not public because it introduces the potential for unit
confusion.  Measurements can only be manipulated by users as
[[org.facsim.measure.PhysicalQuantity.AbstractMeasure!]] subclass instances,
not as raw values.  Allowing access to raw values encourages by-passing of the
unit protection logic provided by these measurement classes.

@param units Units in which the value is to be reported.

@return Measurement value in specified '''units'''.

@since 0.0
*/
//.............................................................................

    @inline
    private [measure] final def getValue (units: Units) =
    units.exportValue (value)

//.............................................................................
/**
Retrieve physical quantity family to which this value belongs.

@return Physical quantity family to which this value belongs.

@since 0.0
*/
//.............................................................................

    def getFamily: Family

//.............................................................................
/**
Helper function to create a new measurement value in the same family as this
measurement.

@param value Value of the new measurement.

@return Measurement instance in the same family as this instance.

@throws java.lang.IllegalArgumentException if '''value''' is outside of the
valid range of permitted values for the current family, is infinite or
''not-a-number''.

@since 0.0
*/
//.............................................................................

    protected [measure] def createNew (value: Double): Measure

//.............................................................................
/**
Add a measurement value of the same type.

@param addend Measurement value to be added to this value.

@return Sum of this measurement and '''addend'''.  The result is of the same
type as this measurement value.

@throws java.lang.IllegalArgumentException If the result is not finite or is
outside of the defined domain for the associated physical quantity.

@since 0.0
*/
//.............................................................................

    @inline
    final def + (addend: Measure) = createNew (value + addend.value)

//.............................................................................
/**
Subtract a measurement value of the same type.

@param subtrahend Measurement value to be subtracted from this value.

@return Difference of this measurement minus '''subtrahend'''.  The result is
of the same type as this measurement value.

@throws java.lang.IllegalArgumentException If the result is not finite or is
outside of the defined domain for the associated physical quantity.

@since 0.0
*/
//.............................................................................

    @inline
    final def - (subtrahend: Measure) = createNew (value - subtrahend.value)

//.............................................................................
/**
Multiply a measurement value by specified factor.

@param multiplier Multiplier to be applied to this measurement value.

@return Product of this measurement value multiplied by '''multiplier'''.  The
result is of the same type as this measurement value.

@throws java.lang.IllegalArgumentException If the result is not finite or is
outside of the defined domain for the associated physical quantity.

@since 0.0
*/
//.............................................................................

    @inline
    final def * (multiplier: Double) = createNew (value * multiplier)

//.............................................................................
/**
Multiply a measurement value by specified measurement value.

@param multiplier Multiplier to be applied to this measurement value.

@return Product of this measurement value multiplied by '''multiplier'''.  The
result will be a measurement in a different physical quantity family to this
measurement (unless the multiplier is a unitless generic value, in which case
it will have the same family as this measurement, although expressed as a
generic measurement instance).

@throws java.lang.IllegalArgumentException If the result is not finite or is
outside of the defined domain for the associated physical quantity.

@since 0.0
*/
//.............................................................................

    @inline
    final def * (multiplier: AbstractMeasure) = Generic (value *
    multiplier.value, getFamily * multiplier.getFamily)

//.............................................................................
/**
Divide a measurement value by specified divisor.

@param divisor Divisor to be applied to this measurement value.

@return Quotient of this measurement value divided by '''divisor'''.  The
result is of the same type as this measurement value.

@throws java.lang.IllegalArgumentException If the result is not finite or is
outside of the defined domain for the associated physical quantity.

@since 0.0
*/
//.............................................................................

    @inline
    final def / (divisor: Double) = createNew (value / divisor)

//.............................................................................
/**
Divide a measurement value by another measurement value of the same type.

@param divisor Divisor to be applied to this measurement value.

@return Ratio of this measurement value to the other measurement value.  The
result is a scalar value that has no associated measurement type.

@since 0.0
*/
//.............................................................................

    @inline
    final def / (divisor: Measure) = value / divisor.value

//.............................................................................
/**
Divide a measurement value by another measurement value of a different physical
quantity family.

@param Divisor Divisor to be applied to this measurement value.

@return Quotient of this measurement value divided by '''divisor'''.  The
result will be a measurement in a different physical quantity family to this
measurement (unless the divisor is a unitless generic value, in which case it
will have the same family as this measurement, although expressed as a generic
measurement instance).

@throws java.lang.IllegalArgumentException If the result is not finite or is
outside of the defined domain for the associated physical quantity.

@since 0.0
*/
//.............................................................................

    @inline
    final def / (divisor: AbstractMeasure) = Generic (value / divisor.value,
    getFamily / divisor.getFamily)

//.............................................................................
/*
Determine whether another object can equal this object.

Refer to Chapter 30 of "Programming in Scala", 2nd Edition, by Odersky, Spoon &
Venners.

@since 0.0
*/
//.............................................................................

    final override def canEqual (that: Any): Boolean = that match {

/*
If "that" is a subclass of AbstractMeasure, then we can equal the other value
provided that "that" is an measurement of the same physical quantity family as
this instance.  If they are for different families, then we cannot equal the
other value.
*/

      case other: AbstractMeasure => getFamily == other.getFamily

/*
The default case is that "that" is not a measure value, so we cannot compare
values as being equal.
*/

      case _ => false
    }

//.............................................................................
/*
Compare this object to another for equality.

@see Any.equals (that: Any)

If two objects compare equal, then their hash-codes must compare equal too;
Similarly, if two objects have different hash-codes, then they must not compare
equal.  However if two objects have the same hash-codes, they may or may not
compare equal, since hash-codes do not map to unique values.

@since 0.0
*/
//.............................................................................

    final override def equals (that: Any): Boolean = that match {

/*
If the other object is an AbstractMeasure subclass, and that value can be
compared as equal to this value, and they have the same value, then that equals
this.
*/

      case other: AbstractMeasure => other.canEqual (this) && value ==
      other.value

/*
All other values (including null), cannot be equal to this value.
*/

      case _ => false
    }

//.............................................................................
/*
Return this measurement value's hash code.

@see Any.hashCode ()

If two objects compare equal, then their hash-codes must compare equal too;
Similarly, if two objects have different hash-codes, then they must not compare
equal.  However if two objects have the same hash-codes, they may or may not
compare equal, since hash-codes do not necessarily map to unique values.

Since measurements have a value and a family, we take the hash code of each an
use a binary XOR on them to obtain our hash code.  This ensures that
measurements from different families, which do not compare equal, have
different hashes while also ensuring that equal values for the same families,
which do compare equal, have identical hash codes too.  This fulfills the hash
code/equality contract.

I (Mike) don't think that there is a need to compare measurements to doubles
(generic, unitless measurements are, in effect, the same as Doubles), since we
can implicitly convert either to the other, but I could be wrong., but we cab

@since 0.0
*/
//.............................................................................

    @inline
    final override def hashCode = value.hashCode ^ getFamily.hashCode
  }

//-----------------------------------------------------------------------------
/**
Abstract base class for all physical quantity measurement units.

Each subclass represents a single ''physical quantity unit family''.  For
example, time units are represented by the
[[org.facsim.measure.Time.TimeUnit!]] `AbstractUnits` subclass.

Each unit family supports one or more ''units of measure''.  For example, time
quantities may be measured in ''seconds'', ''minutes'', ''hours'', etc.  These
units are represented by instances of the concrete `AbstractUnits` subclass.
For each unit family, there is a standard unit of measure defined by the
''[[http://en.wikipedia.org/wiki/SI International System of
Units]]''&mdash;commonly abbreviated to ''SI Units''.

These standard units are used by ''Facsimile'' internally to store measurement
quantities (as immutable instances of
[[org.facsim.measure.PhysicalQuantity.AbstractMeasure!]] subclasses, with each
subclass corresponding to each measurement type.)  For example, the ''SI''
standard unit of measure for ''time'' is the ''second''; consequently,
''Facsimile'' stores and calculates all time quantities, internally, in
''seconds''.  Adoption of the ''SI'' standard units simplifies the
implementation of physics calculations within ''Facsimile'' and provides a
clearly-defined basis for developing simulation models of the real-world.
(Many other simulation modeling tools suffer from ''unit of measure
confusion'', both internally and externally, creating a wide variety of
problems.)

However, it is unreasonable to expect that ''Facsimile'' users would be
comfortable entering and reviewing data solely in these units.  For instance,
the ''SI'' standard unit of measure for ''angles'' is the ''radian''$mdash;and
there are few people who don't find the ''degree'' a far more intuitive
alternative.  Similarly, users in the United States&mdash;or their
customers$mdash;might prefer ''feet'' & ''inches'', ''pounds'', ''Fahrenheit'',
etc. to their metric equivalents.  Consequently, ''Facsimile'' allows users to
work with whichever units they prefer.  ''Facsimile'' converts values to the
standard ''SI'' units on input and converts them to the required units on
output.

@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
[[http://en.wikipedia.org/]].

@since 0.0
*/
//-----------------------------------------------------------------------------

  abstract class AbstractUnits private [measure] extends Converter with NotNull
  {

//.............................................................................
/**
Format a value in these units for output.

@param value Value, expressed in these units, to be output.

@return Formatted string, containing the value and the units.

@since 0.0
*/
//.............................................................................

    private [measure] def format (value: Double): String
  }
}