/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
Scala source file belonging to the org.facsim.measure package.
*/
//======================================================================================================================

package org.facsim.measure

import org.facsim.{assertNonNull, LibResource}
import scala.language.implicitConversions

//======================================================================================================================
/**
Class representing a measurement from any physical quantity family.

The role of this class is to allow general multiplication and division of pairs
of measurements from different physical quantity families, such that the result
belongs to a different family to either of the operands.

For example, when a [[org.facsim.measure.Length]] measurement is divided by a
[[org.facsim.measure.Time]] measurement, the result is a
[[org.facsim.measure.Velocity]] measurement. However creating tables that
define the result of each pair of operands for each operation would be
time-consuming, error prone and non-exhaustive.

A simpler approach is to create a generic measurement, capturing the family's
characteristics, which can be converted to the corresponding specific physical
measurement for that family by implicit conversion. If the generic
measurement's family is not identical to the specific measurement it is being
converted to, an exception will result.

For example, we could obtain a generic measurement that has ''velocity''
characteristics by dividing a ''length'' by a ''time''. If we then attempt to
convert this generic value to a ''velocity'', it will succeed; however, if we
attempt to convert it to a ''temperature'', an exception will result.

@since 0.0
*/
//======================================================================================================================

object Generic
extends Physical {

/**
There is only set of units for this type, which will be the ''SI'' units by
definition.

@since 0.0
*/

  val Basic = GenericUnits

/*
Generic measurement values.
*/

  override type Measure = GenericMeasure

/*
Generic measurement units.
*/

  override type Units = GenericUnits.type

/**
@inheritdoc
*/

  override val siUnits = Basic

//----------------------------------------------------------------------------------------------------------------------
/**
Implicit conversion from a generic measurement value to a Double value.

In order for this conversion to succeed, the family associated with the generic
measurement must be ''unitless'' (i.e., all base measurement exponents must be
zero).

@param measure Generic measurement value to be converted, which must be
''unitless''.

@return The unitless value of `measure` as a Double.

@throws org.facsim.measure.GenericConversionException if `measure` is not
''unitless''.

@since 0.0
*/
//----------------------------------------------------------------------------------------------------------------------

  implicit def toDouble (measure: Measure): Double = {
    if (measure.family.isUnitless) measure.value
    else throw new GenericConversionException (measure, Family.unitless)
  }

//----------------------------------------------------------------------------------------------------------------------
/**
Create a new generic measurement.

@todo There are currently no checks to ensure that `measure` is within a valid
range for the associated family. This needs to be added for consistency.

@param measure Generic measurement expressed in
''[[http://en.wikipedia.org/wiki/SI SI]]'' units.

@param family Physical quantity family to which `measure` belongs.

@throws java.lang.IllegalArgumentException if `measure` is not finite.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [measure] def apply (measure: Double, family: Family) =
  new GenericMeasure (measure, family)

//----------------------------------------------------------------------------------------------------------------------
/**
Generic ''Facsimile [[http://en.wikipedia.org/wiki/Physical_quantity physical
quantity]]'' measurement class.

Instances of this class represent the results of measurement calculations that
result, or potentially result, in a measurement from a different physical
quantity family.

However, generic measurements may also be ''unitless'', in which case they are
equivalent to ordinary [[scala.Double]] values.

Generic measurements can be converted to an equivalent specific
measurement&mdash;one that belongs to the same physical quantity
family&mdash;when a specific equivalent exists. For example, if a
[[org.facsim.measure.Length]] is divided by a [[org.facsim.measure.Time]],
then the result is a ''generic velocity'', which can be converted to a
[[org.facsim.measure.Velocity]]. However a ''time'' multiplied by a ''time'',
results in a generic ''time-squared'' measurement has no equivalent specific
class and can be expressed only in generic form.

Attempts to convert ''generic'' measurements to measurements of a different
physical quantity family will resulting in an exception being thrown.

Generic measurements are stored internally in the corresponding
''[[http://en.wikipedia.org/wiki/SI SI]]'' units for the associated physical
quantity family.

@constructor Create new ''generic'' measurement for the associated
''[[http://en.wikipedia.org/wiki/Physical_quantity physical quantity]]''
family.

@param measure Value of the measurement expressed in the associated
''[[http://en.wikipedia.org/wiki/SI SI]]'' units. This value must be finite,
and may have additional restrictions, depending upon the `family` to which it
belongs.

@param family Physical quantity family to which `measure` belongs.

@throws java.lang.IllegalArgumentException if `measure` is not finite or
is invalid for the associated `family`.

@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
[[http://en.wikipedia.org/ Wikipedia]].

@since 0.0
*/
//----------------------------------------------------------------------------------------------------------------------

  final class GenericMeasure private [measure] (measure: Double, override val
  family: Family)
  extends PhysicalMeasure (measure) {

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

    override def toString = GenericUnits.format (this)

//.............................................................................
/*
Create a new instance via the Generic factory method.
*/
//.............................................................................

    protected [measure] override def createNew (newMeasure: Double) =
    apply (newMeasure, family)
  }

//----------------------------------------------------------------------------------------------------------------------
/**
Generic unit class for all generic physical quantity measurement units.

The sole generic unit instance represents the
''[[http://en.wikipedia.org/wiki/SI SI]]'' units for the associated physical
quantity family, if any&mdash;''unitless'' generic values do not have units, by
definition.

@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
[[http://en.wikipedia.org/]].

@since 0.0
*/
//----------------------------------------------------------------------------------------------------------------------

  object GenericUnits
  extends PhysicalUnits {

/*
Self-reference to these units.
*/

    self =>

//.............................................................................
/*
Importing simply accepts values "as is".
*/
//.............................................................................

    private [measure] override def importValue (value: Double) = value

//.............................................................................
/*
Exporting simply outputs values "as is".
*/
//.............................................................................

    private [measure] override def exportValue (value: Double) = value

//.............................................................................
/*
Format generic values for output.
*/
//.............................................................................

    private [measure] def format (value: Measure): String = {
      assertNonNull (value)
      LibResource ("measure.Generic.Units.format", value.inUnits (self),
      value.family.baseSymbol)
    }
  }
}