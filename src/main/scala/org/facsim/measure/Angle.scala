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

import org.facsim.requireValid

//=============================================================================
/**
''Plane Angle'' physical quantity type.

All angles are stored internally in radians, which is the SI standard unit for
plane angles.

@see [[http://en.wikipedia.org/wiki/Radian Radians]] on Wikipedia.
 
@since 0.0
*/
//=============================================================================

object Angle extends Specific {

/**
The mathematical constant π.

π is defined as the ratio of the circumference of a circle to its diameter.

@note The authors of ''Facsimile'' prefer the use of
[[org.facsim.measure.Angle$.τ]] to π, because the former is significantly
easier to work with.

@see [[http://tauday.com/tau-manifesto The Tau Manifesto]]

@see [[org.facsim.measure.Angle$.τ]]
*/

  final val π = Math.PI

/**
A proposed mathematical constant τ.

τ is defined as the ratio of the circumference of its circle to its radius.

This constant, whose value is 2π, is preferred throughout the facsimile
codebase for its simplicity and ease-of-use compared to π.

@see [[http://tauday.com/tau-manifesto The Tau Manifesto]]
*/

  final val τ = 2.0 * π

/**
@inheritdoc
*/

  final override type Measure = AngleMeasure

/**
@inheritdoc
*/

  final override type Units = AngleUnits

/**
Units for angles measured in ''degrees''.
*/

  final val degrees = new Units (new LinearScaleConverter (τ / 360.0), "°")

/**
Units for angles measured in ''gradients''.
*/

  final val gradients = new Units (new LinearScaleConverter (τ / 400.0),
  "grad")

/**
Units for angles measured in ''radians''.

@note Radians are the ''SI'' standard units for plane angle measurement, and
the units that are used to store angle measurements internally in
''Facsimile''.

@see [[http://en.wikipedia.org/wiki/Radian Radians]] on Wikipedia.
*/

  final val radians = new Units (SIConverter, "rad")

/**
Units for angles measured in ''revolutions''.
*/

  final val revolutions = new Units (new LinearScaleConverter (τ), "revs")

/**
Physical quantity family for plane angle measurements.
*/

  private [measure] final val family = Family (planeAngleExponent = 1)

/**
@inheritdoc
*/

  final override val getSIUnits = radians

/*
Register this family.
*/

  Family.register (family, Angle)

//-----------------------------------------------------------------------------
/**
Compute arc sine from sine value

@param sine Sine value, which must be in the range [-1, 1].

@return Angle in the range [-τ/4, τ/4] radians ([-90, 90] degrees).

@throws java.lang.IllegalArgumentException if '''sine''' is NaN or is outside
of the range [-1, 1].

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def asin (sine: Double) = {
    requireValid ("sine", sine, sine >= -1.0 && sine <= 1.0)
    apply (Math.asin (sine))
  } ensuring (!_.getValue.isNaN)

//-----------------------------------------------------------------------------
/**
Compute arc cosine from cosine value.

@param cosine Cosine value, which must be in the range [-1, 1].

@return Angle in the range [0, τ/2] radians ([0, 180] degrees).

@throws java.lang.IllegalArgumentException if '''cosine''' is NaN or is outside
of the range [-1, 1].

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def acos (cosine: Double) = {
    requireValid ("cosine", cosine, cosine >= -1.0 && cosine <= 1.0)
    apply (Math.acos (cosine))
  } ensuring (!_.getValue.isNaN)

//-----------------------------------------------------------------------------
/**
Compute arc tangent from tangent value.

@note The [[org.facsim.measure.Angle$.atan2(org.facsim.measure.Length$.Measure,
org.facsim.measure.Length$.Measure)*]] function should be preferred where
applicable.

@param tangent Tangent value, which must be in the range [-∞, ∞].

@return Angle in the range [-τ/4, τ/4] radians ([-90, 90] degrees).

@throws java.lang.IllegalArgumentException if '''tangent''' is NaN.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def atan (tangent: Double) = {
    requireValid ("tangent", tangent, tangent != Double.NaN)
    apply (Math.atan (tangent))
  } ensuring (!_.getValue.isNaN)

//-----------------------------------------------------------------------------
/**
Returns the angle ''θ'' from the conversion of rectangular coordinates
('''x''', '''y''') to polar coordinates (''r'', ''θ'').

@note This function should be preferred over the
[[org.facsim.measure.Angle$.atan(Double)*]] function where applicable.

@param y Ordinate (Y-axis value) of the rectangular coordinate.

@param x Abscissa (X-axis value) of the rectangular coordinate.

@return Angle in the range [-τ/2, τ/2] radians ([-180, 180] degrees).

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def atan2 (y: Length.Measure, x: Length.Measure) = {
    apply (Math.atan2 (y.getValue, x.getValue))
  } ensuring (!_.getValue.isNaN)

//-----------------------------------------------------------------------------
/**
''Plane angle'' measurement class.

@constructor Create new plane angle value.

@param value Angle value in radians.  This value must be finite.

@throws java.lang.IllegalArgumentException if '''value''' is not finite.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class AngleMeasure (value: Double) extends SpecificMeasure (value) {

//.............................................................................
/**
Calculate the sine of this angle.

@return Sine of this angle, a value in the range [-1, 1].

@since 0.0
*/
//.............................................................................

    @inline
    final def sin = Math.sin (getValue)

//.............................................................................
/**
Calculate the cosine of this angle.

@return Cosine of this angle, a value in the range [-1, 1].

@since 0.0
*/
//.............................................................................

    @inline
    final def cos = Math.cos (getValue)

//.............................................................................
/**
Calculate the tangent of this angle.

@return Tangent of this angle, a value in the range [-∞, ∞].

@since 0.0
*/
//.............................................................................

    @inline
    final def tan = Math.tan (getValue)

//.............................................................................
/**
Determine whether this angle is a normalized value in the range [0, τ) radians
([0, 360) degrees).

@return `true` if this angle is in the range [0, τ) radians ([0, 360) degrees)
or `false` otherwise.
*/
//.............................................................................

    @inline
    final def isNormalized = getValue >= 0.0 && getValue < τ 

//.............................................................................
/**
Normalize the angle to a value in the range [0, τ) radians ([0, 360) degrees).

@returns Angle in the range [0, τ) radians ([0, 360) degrees).
*/
//.............................................................................

    @inline
    final def normalize = {

/*
Note: I originally tried this as getValue % τ - but, alas, that yields a value
in the range (-τ, τ) instead of [0, τ).  The solution below provides the right
answer, even if it is a little more long-winded. 
*/

      apply (getValue - τ * (getValue / τ).floor)
    } ensuring (_.isNormalized)
  }

//-----------------------------------------------------------------------------
/**
''Plane angle'' unit of measurement family class.

@constructor Create new angle unit of measurement.

@param converter Rules to be applied to convert a quantity measured in these
units to the standard ''Plane Angle SI'' units, ''radians''.

@param symbol Symbol to be used when outputting measurement values in these
units.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class AngleUnits (converter: Converter, symbol: String) extends
  SpecificUnits (converter, symbol)
}