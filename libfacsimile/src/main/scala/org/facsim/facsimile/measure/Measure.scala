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
Abstract base class for all measurement type classes.

This base class is used by all measurement types, storing the underlying values
in the corresponding SI units.

@param value Value of the measurement type in the corresponding SI units.

@tparam T Actual measurement type.  This must be a sub-type of Measure.

@since 0.0-0
*/
//=============================================================================

private [measure] abstract class Measure [T <: Measure [T]] (value: Double)
extends BaseMeasure (value)
with Ordered [T]
{

//-----------------------------------------------------------------------------
/*
Result of comparing this with operand that.

@see Ordered [T].compare (that: T)
*/
//-----------------------------------------------------------------------------

  override final def compare (that: T): Int = {

/*
Compare the two instances by comparing their values.
*/

    getValue.compare (that.getValue)
  }

//-----------------------------------------------------------------------------
/**
Addition operator.

Compute the result of adding a measurement value of the same type as this value
to this value.

@param that Measurement value to be added to this value.

@throws InvalidArgumentException if the result of the expression is out of the
measurement type's valid range.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  final def + (that: T): T = {
    newMeasurement (this.getValue + that.getValue)
  }

//-----------------------------------------------------------------------------
/**
Subtraction operator.

Compute the result of subtracting a measurement value of the same type as this
value from this value.

@param that Measurement value to be subtracted from this value.

@throws InvalidArgumentException if the result of the expression is out of the
measurement type's valid range.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  final def - (that: T): T = {
    newMeasurement (this.getValue - that.getValue)
  }

//-----------------------------------------------------------------------------
/**
Multiplication operator.

Compute the result of multiplying this measurement value by the indicated
factor.

@param factor Factor to be applied to this measurement value.

@throws InvalidArgumentException if the result of the expression is out of the
measurement type's valid range.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  final def * (factor: Double): T = {
    newMeasurement (this.getValue * factor)
  }

//-----------------------------------------------------------------------------
/**
Division operator.

Compute the result of dividing this measurement value by the indicated
divisor.

@param divisor Divisor to be applied to this measurement value.

@throws InvalidArgumentException if the result of the expression is out of the
measurement type's valid range.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  final def / (divisor: Double): T = {
    require (divisor != 0.0)
    newMeasurement (this.getValue / divisor)
  }

//-----------------------------------------------------------------------------
/**
Create a new measurement of the same type as this type with the indicated
value.

If Scala (and Java) ever gets true generics (or, at least, reifiable types), we
wouldn't need this function - we would just say "new T (value)" instead - but
we don't, so we need this sorry mess.

@param value Value of the new measurement.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  protected [measure] def newMeasurement (value: Double): T
}
