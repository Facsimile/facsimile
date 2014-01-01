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

import org.facsim.LibResource
import org.facsim.requireValid

//=============================================================================
/**
''[[http://en.wikipedia.org/wiki/Angle Plane angle]]'' physical quantity type.

All angles are stored internally in ''[[http://en.wikipedia.org/wiki/Radian
radians]]'', which is the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard
unit of measure.

@see [[http://en.wikipedia.org/wiki/Angle Plane Angle]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/Radian Radians]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/
//=============================================================================

object Angle
extends Specific {

/*
Angle measurement values.
*/

  override type Measure = AngleMeasure

/*
Angle measurement units.
*/

  override type Units = AngleUnits

/*
Name of this physical quantity.
*/

  override val name = "planar angle"

/**
The mathematical constant ''[[http://en.wikipedia.org/wiki/Pi π]]'' in the form
of an angle value.

''π'' is defined as the ratio of the circumference of a circle to its diameter.

@note The authors of ''Facsimile'' prefer the use of
''[[org.facsim.measure.Angle$.τ]]'' to ''π'', because the former is
significantly easier to work with.

@see [[http://tauday.com/tau-manifesto The Tau Manifesto]] for further
information about ''τ''.
@see [[http://en.wikipedia.org/wiki/Pi Pi]] on ''Wikipedia''.
@see [[org.facsim.measure.Angle$.τ]].

@since 0.0
*/

  val π = apply (Math.PI)

/**
A proposed mathematical constant,
''[[http://en.wikipedia.org/wiki/Turn_(geometry)#Tau_proposal τ]]'' (tau).

''τ'' is defined as the ratio of the circumference of a circle to its radius.

@see ''[[http://tauday.com/tau-manifesto The Tau Manifesto]]'' for further
information.
*/

  private val τValue = 2.0 * Math.PI

/**
A proposed mathematical constant,
''[[http://en.wikipedia.org/wiki/Turn_(geometry)#Tau_proposal τ]]'' (tau), in
the form of an angle value.

''τ'' is defined as the ratio of the circumference of a circle to its radius.

This constant, whose value is 2''π'', is preferred throughout ''Facsimile'' for
its simplicity and ease-of-use compared to ''π''. A ''radian'' measure of ''τ''
is one ''turn'' (or ''revolution''), so fractions of ''τ'' are also fractions
of a circle:
  - ''τ'' = 360°
  - ^''τ''^/,,2,, = 180°
  - ^''τ''^/,,3,, = 120°
  - ^''τ''^/,,4,, = 90°
  - ^''τ''^/,,6,, = 60°
  - ^''τ''^/,,8,, = 45°
  - ^''τ''^/,,12,, = 30°
  - ^''τ''^/,,360,, = 1°
  - etc.

@see ''[[http://tauday.com/tau-manifesto The Tau Manifesto]]'' for further
information.

@since 0.0
*/

  val τ = apply (τValue)

/**
Units for angles measured in ''[[http://en.wikipedia.org/wiki/Degree_(angle)
degrees]]''.

@see [[http://en.wikipedia.org/wiki/Degree_(angle) Degrees]] on ''Wikipedia''.

@since 0.0
*/

  val Degrees = new Units (new LinearScaleConverter (τValue / 360.0),
  LibResource ("measure.Angle.Degree.sym"))

/**
Units for angles measured in ''[[http://en.wikipedia.org/wiki/Gradian
gradians]]''.

@see [[http://en.wikipedia.org/wiki/Gradian Gradians]] on ''Wikipedia''.

@since 0.0
*/

  val Gradians = new Units (new LinearScaleConverter (τValue / 400.0),
  LibResource ("measure.Angle.Gradian.sym"))

/**
Units for angles measured in ''[[http://en.wikipedia.org/wiki/Radian
radians]]''.

@note ''Radians'' are the ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard
units for plane angle measurement, and the units that are used to store such
measurements internally in ''Facsimile''.

In ''Facsimile'', a ''radian'' is defined in accordance with ''SI'' standards.

@see [[http://en.wikipedia.org/wiki/Radian Radians]] on ''Wikipedia''.
@see [[http://en.wikipedia.org/wiki/SI International System of Units]] on
''Wikipedia''.

@since 0.0
*/

  val Radians = new Units (SIConverter,
  LibResource ("measure.Angle.Radian.sym"))

/**
Units for angles measured in ''[[http://en.wikipedia.org/wiki/Turn_(geometry)
turns]]'' (also known as ''cycles'' or ''revolutions'').

@see [[http://en.wikipedia.org/wiki/Turn_(geometry) Turns]] on ''Wikipedia''.

@since 0.0
*/

  val Turns = new Units (new LinearScaleConverter (τValue),
  LibResource ("measure.Angle.Turn.sym"))

/**
Physical quantity family for plane angle measurements.
*/

  protected [measure] val family = Family (planeAngleExponent = 1)

/*
SI angle units.
*/

  override val siUnits = Radians

/*
Register this family.
*/

  Family.register (family, Angle)

//-----------------------------------------------------------------------------
/*
Angle measurement factory.
*/
//-----------------------------------------------------------------------------

  private [measure] override def apply (measure: Double) =
  new AngleMeasure (measure)

//-----------------------------------------------------------------------------
/**
Compute arc sine from sine value

@param sine Sine value, which must be in the range [-1, 1].

@return Angle in the range [-^''τ''^/,,4,,, ^''τ''^/,,4,,] radians ([-90, 90]
degrees).

@throws [[java.lang.IllegalArgumentException!]] if `sine` is `NaN` or is
outside of the range [-1, 1].

@since 0.0
*/
//-----------------------------------------------------------------------------

  def asin (sine: Double) = {
    requireValid (sine, sine >= -1.0 && sine <= 1.0)
    apply (Math.asin (sine))
  } ensuring (!_.value.isNaN)

//-----------------------------------------------------------------------------
/**
Compute arc cosine from cosine value.

@param cosine Cosine value, which must be in the range [-1, 1].

@return Angle in the range [0, ^''τ''^/,,2,,] radians ([0, 180] degrees).

@throws [[java.lang.IllegalArgumentException!]] if `cosine` is `NaN` or is
outside of the range [-1, 1].

@since 0.0
*/
//-----------------------------------------------------------------------------

  def acos (cosine: Double) = {
    requireValid (cosine, cosine >= -1.0 && cosine <= 1.0)
    apply (Math.acos (cosine))
  } ensuring (!_.value.isNaN)

//-----------------------------------------------------------------------------
/**
Compute arc tangent from tangent value.

@note The [[org.facsim.measure.Angle$.atan2(org.facsim.measure.Length$.Measure,
org.facsim.measure.Length$.Measure)*]] function should be preferred where
applicable, since it returns a fuller range of angle values.

@param tangent Tangent value, which must be in the range [-∞, ∞].

@return Angle in the range [-^''τ''^/,,4,,, ^''τ''^/,,4,,] radians ([-90, 90]
degrees).

@throws [[java.lang.IllegalArgumentException!]] if `tangent` is `NaN`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def atan (tangent: Double) = {
    requireValid (tangent, !tangent.isNaN())
    apply (Math.atan (tangent))
  } ensuring (!_.value.isNaN)

//-----------------------------------------------------------------------------
/**
Returns the angle ''θ'' from the conversion of rectangular coordinates (`x`,
`y`) to polar coordinates (''r'', ''θ'').

@note This function should be preferred over the
[[org.facsim.measure.Angle$.atan(Double)*]] function where applicable.

@param y ''Ordinate'' (Y-axis value) of the rectangular coordinate.

@param x ''Abscissa'' (X-axis value) of the rectangular coordinate.

@return Angle in the range [-^''τ''^/,,2,,, ^''τ''^/,,2,,] radians ([-180, 180]
degrees).

@since 0.0
*/
//-----------------------------------------------------------------------------

  def atan2 (y: Length.Measure, x: Length.Measure) = {
    apply (Math.atan2 (y.value, x.value))
  } ensuring (!_.value.isNaN)

//-----------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Angle Plane angle]]'' measurement class.

Instances of this class represent ''plane angle'' measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Angle plane angle]]''
measurement value.

@param measure ''Plane angle'' measurement expressed in
''[[org.facsim.measure.Angle$.radians]]''. This value must be finite.

@throws [[java.lang.IllegalArgumentException!]] if `measure` is not finite.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class AngleMeasure private [measure] (measure: Double)
  extends SpecificMeasure (measure) {

//.............................................................................
/**
Calculate the sine of this angle.

@return Sine of this angle, a value in the range [-1, 1].

@since 0.0
*/
//.............................................................................

    def sin = Math.sin (value)

//.............................................................................
/**
Calculate the cosine of this angle.

@return Cosine of this angle, a value in the range [-1, 1].

@since 0.0
*/
//.............................................................................

    def cos = Math.cos (value)

//.............................................................................
/**
Calculate the tangent of this angle.

@return Tangent of this angle, a value in the range [-∞, ∞].

@since 0.0
*/
//.............................................................................

    def tan = Math.tan (value)

//.............................................................................
/**
Determine whether this angle is a normalized value in the range [0, ''τ'')
''radians'' ([0, 360) ''degrees'').

@return `true` if this angle is in the range [0, ''τ'') ''radians'' ([0, 360)
degrees); `false` otherwise.

@since 0.0
*/
//.............................................................................

    def isNormalized = value >= 0.0 && value < τValue

//.............................................................................
/**
Normalize the angle to a value in the range [0, ''τ'') ''radians'' ([0, 360)
''degrees'').

@return Angle in the range [0, ''τ'') ''radians'' ([0, 360) ''degrees'').

@since 0.0
*/
//.............................................................................

    def normalize () = {

/*
Note: I originally tried this as value % τ - but, alas, that yields a value in
the range (-τ, τ) instead of [0, τ). The solution below provides the right
answer, even if it is a little more long-winded. 
*/

      apply (value - τValue * (value / τValue).floor)
    } ensuring (_.isNormalized)
  }

//-----------------------------------------------------------------------------
/**
''[[http://en.wikipedia.org/wiki/Angle Plane angle]]'' unit of measurement
family class.

Instances of this class represent units for expressing ''plane angle''
measurements.

@constructor Create new ''[[http://en.wikipedia.org/wiki/Angle plane angle]]''
unit of measurement.

@param converter Rules to be applied to convert a quantity measured in these
units to the standard ''plane angle [[http://en.wikipedia.org/wiki/SI SI]]''
units, ''radians''.

@param symbol Symbol to be used when outputting measurement values expressed in
these units.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final class AngleUnits private [measure] (converter: Converter,
  symbol: String)
  extends SpecificUnits (converter, symbol)
}