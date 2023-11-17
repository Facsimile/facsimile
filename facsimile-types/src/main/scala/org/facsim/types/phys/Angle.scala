//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================

//======================================================================================================================
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types.phys

import org.facsim.types.LibResource
import org.facsim.util.{requireNonNull, requireValid}

/** _[[http://en.wikipedia.org/wiki/Angle Plane angle]]_ physical quantity type.
 *
 *  All angles are stored internally in _[[http://en.wikipedia.org/wiki/Radian radians]]_, which is the
 *  _[[http://en.wikipedia.org/wiki/SI SI]]_ standard unit of plane angle types.
 *
 *  @see [[http://en.wikipedia.org/wiki/Angle Plane Angle]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/Radian Radians]] on _Wikipedia_.
 *
 *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on _Wikipedia_.
 *
 *  @since 0.0
 */
object Angle
extends Specific {

  /** @inheritdoc */
  override type Measure = AngleMeasure

  /** @inheritdoc */
  override type Units = AngleUnits

  /** @inheritdoc */
  override val name: String = LibResource("phys.Angle.name")

  /** The mathematical constant _[[http://en.wikipedia.org/wiki/Pi π]]_ in the form of an angle value.
   *
   *  _π_ is defined as the ratio of the circumference of a circle to its diameter.
   *
   *  @note The authors of _Facsimile_ prefer the use of _[[Angle.τ]]_ to _π_, because the former is significantly
   *  easier to work with.
   *
   *  @see [[http://tauday.com/tau-manifesto The Tau Manifesto]] for further information about _τ_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Pi Pi]] on _Wikipedia_.
   *
   *  @see [[Angle.τ]].
   *
   *  @since 0.0
   */
  lazy val π: Measure = newMeasure(Math.PI)

  /** A proposed mathematical constant, _[[http://en.wikipedia.org/wiki/Turn_(geometry)#Tau_proposal τ]]_ (tau).
   *
   *  _τ_ is defined as the ratio of the circumference of a circle to its radius.
   *
   *  @see _[[http://tauday.com/tau-manifesto The Tau Manifesto]]_ for further information.
   */
  private val τValue = 2.0 * Math.PI

  /** A proposed mathematical constant, _[[http://en.wikipedia.org/wiki/Turn_(geometry)#Tau_proposal τ]]_ (tau), in
   *  the form of an angle value.
   *
   *  _τ_ is defined as the ratio of the circumference of a circle to its radius.
   *
   *  This constant, whose value is 2_π_, is preferred throughout _Facsimile_ for its simplicity and ease-of-use
   *  compared to _π_. A _radian_ types of _τ_ is one _turn_ (or _revolution_), so fractions of _τ_ are
   *  also fractions of a circle:
   *  - _τ_ = 360°
   *  - ^_τ_^/,,2,, = 180°
   *  - ^_τ_^/,,3,, = 120°
   *  - ^_τ_^/,,4,, = 90°
   *  - ^_τ_^/,,6,, = 60°
   *  - ^_τ_^/,,8,, = 45°
   *  - ^_τ_^/,,12,, = 30°
   *  - ^_τ_^/,,360,, = 1°
   *  - etc.
   *
   *  @see _[[http://tauday.com/tau-manifesto The Tau Manifesto]]_ for further information.
   *
   *  @since 0.0
   */
  lazy val τ: Measure = newMeasure(τValue)

  /** Units for angles measured in _[[http://en.wikipedia.org/wiki/Degree_(angle) degrees]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Degree_(angle) Degrees]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Degrees: Units = new Units(new LinearScaleConverter(τValue / 360.0), LibResource("phys.Angle.Degree.sym"))

  /** Units for angles measured in _[[http://en.wikipedia.org/wiki/Gradian gradians]]_.
   *
   *  @see [[http://en.wikipedia.org/wiki/Gradian Gradians]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Gradians: Units = new Units(new LinearScaleConverter(τValue / 400.0), LibResource("phys.Angle.Gradian.sym"))

  /** Units for angles measured in _[[http://en.wikipedia.org/wiki/Radian radians]]_.
   *
   *  @note _Radians_ are the _[[http://en.wikipedia.org/wiki/SI SI]]_ standard units for plane angle measurement,
   *  and the units that are used to store such measurements internally in _Facsimile_.
   *
   *  In _Facsimile_, a _radian_ is defined in accordance with _SI_ standards.
   *
   *  @see [[http://en.wikipedia.org/wiki/Radian Radians]] on _Wikipedia_. @see [[http://en.wikipedia.org/wiki/SI
   *  International System of Units]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Radians: Units = new Units(SIConverter, LibResource("phys.Angle.Radian.sym"))

  /** Units for angles measured in _[[http://en.wikipedia.org/wiki/Turn_(geometry) turns]]_ (also known as _cycles_
   *  or _revolutions_).
   *
   *  @see [[http://en.wikipedia.org/wiki/Turn_(geometry) Turns]] on _Wikipedia_.
   *
   *  @since 0.0
   */
  val Turns: Units = new Units(new LinearScaleConverter(τValue), LibResource("phys.Angle.Turn.sym"))

  /** Physical quantity family for plane angle measurements.
   *
   *  Plane angles are _unitless_ measurements, with radian types defined as the ratio one [[Length]] (the arc of
   *  the circumference subtended by the angle) to another (the radius), resulting in a unitless value.
   *
   *  @note Although angles can be expressed in variety of different units, angles are themselves unitless.
   */
  protected[phys] override val family: Family = Family.Unitless

  /** @inheritdoc */
  override val siUnits: Units = Radians

  // Register this family.
  Family.register(family, Angle)

  /** Angle measurement factory function.
   *
   *  @param measure Measurement, in radians, to be converted into a new types.
   *
   *  @return `types` in the form of an Angle measurement.
   */
  private[phys] override def newMeasure(measure: Double) = new Measure(measure)

  /** Compute arc sine from sine value
   *
   *  @param sine Sine value, which must be in the range [-1, 1].
   *
   *  @return Angle in the range [-^_τ_^/,,4,,, ^_τ_^/,,4,,] radians ([-90, 90] degrees).
   *
   *  @throws IllegalArgumentException if `sine` is outside of the range [-1, 1].
   *
   *  @since 0.0
   */
  def asin(sine: Double): Measure = {
    requireValid(sine, sine >= -1.0 && sine <= 1.0)
    apply(Math.asin(sine))
  } ensuring(!_.value.isNaN)

  /** Compute arc cosine from cosine value.
   *
   *  @param cosine Cosine value, which must be in the range [-1, 1].
   *
   *  @return Angle in the range [0, ^_τ_^/,,2,,] radians ([0, 180] degrees).
   *
   *  @throws IllegalArgumentException if `cosine` is outside of the range [-1, 1].
   *
   *  @since 0.0
   */
  def acos(cosine: Double): Measure = {
    requireValid(cosine, cosine >= -1.0 && cosine <= 1.0)
    apply(Math.acos(cosine))
  } ensuring(!_.value.isNaN)

  /** Compute arc tangent from tangent value.
   *
   *  @note The [[atan2()]] function should be preferred where applicable, since it returns a fuller range of angle
   *  values.
   *
   *  @param tangent Tangent value, which must be in the range [-∞, ∞].
   *
   *  @return Angle in the range [-^_τ_^/,,4,,, ^_τ_^/,,4,,] radians ([-90, 90] degrees).
   *
   *  @throws IllegalArgumentException if `tangent` is `NaN`.
   *
   *  @since 0.0
   */
  def atan(tangent: Double): Measure = {
    requireValid(tangent, !tangent.isNaN)
    apply(Math.atan(tangent))
  } ensuring(!_.value.isNaN)

  /** Returns the angle _θ_ from the conversion of rectangular coordinates (`x`, `y`) to polar coordinates (_r_,
   * _θ_).
   *
   *  @note This function should be preferred over the [[atan()]] function where applicable.
   *
   *  @param y _Ordinate_ (Y-axis value) of the rectangular coordinate.
   *
   *  @param x _Abscissa_ (X-axis value) of the rectangular coordinate.
   *
   *  @return Angle in the range [-^_τ_^/,,2,,, ^_τ_^/,,2,,] radians ([-180, 180] degrees).
   *
   *  @throws NullPointerException if `x` or `y` are `null`.
   *
   *  @since 0.0
   */
  def atan2(y: Length, x: Length): Measure = {
    requireNonNull(y)
    requireNonNull(x)
    apply(Math.atan2(y.value, x.value))
  } ensuring(!_.value.isNaN)

  /** _[[http://en.wikipedia.org/wiki/Angle Plane angle]]_ measurement class.
   *
   *  Instances of this class represent _plane angle_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Angle plane angle]]_ measurement value.
   *
   *  @param measure _Plane angle_ measurement expressed in _[[Radians]]_. This value must be finite.
   *
   *  @throws IllegalArgumentException if `types` is not finite.
   *
   *  @since 0.0
   */
  final class AngleMeasure private[phys](measure: Double)
  extends SpecificMeasure[AngleMeasure](measure) {

    /** Calculate the sine of this angle.
     *
     *  @return Sine of this angle, a value in the range [-1, 1].
     *
     *  @since 0.0
     */
    def sin: Double = Math.sin(value)

    /** Calculate the cosine of this angle.
     *
     *  @return Cosine of this angle, a value in the range [-1, 1].
     *
     *  @since 0.0
     */
    def cos: Double = Math.cos(value)

    /** Calculate the tangent of this angle.
     *
     *  @return Tangent of this angle, a value in the range [-∞, ∞].
     *
     *  @since 0.0
     */
    def tan: Double = Math.tan(value)

    /** Determine whether this angle is a normalized value in the range [0, _τ_) _radians_ ([0, 360) _degrees_).
     *
     *  @return `true` if this angle is in the range [0, _τ_) _radians_ ([0, 360) degrees); `false` otherwise.
     *
     *  @since 0.0
     */
    def isNormalized: Boolean = value >= 0.0 && value < τValue

    /** Normalize the angle to a value in the range [0, _τ_) _radians_ ([0, 360) _degrees_).
     *
     *  @return Angle in the range [0, _τ_) _radians_ ([0, 360) _degrees_).
     *
     *  @since 0.0
     */
    def normalize: AngleMeasure = {

      // Note: I originally tried this as value % τ - but, alas, that yields a value in the range (-τ, τ) instead of [0,
      // τ). The solution below provides the right answer, even if it is a little more long-winded.
      apply(value - τValue *(value / τValue).floor)
    } ensuring(_.isNormalized)
  }

  /** _[[http://en.wikipedia.org/wiki/Angle Plane angle]]_ unit of measurement family class.
   *
   *  Instances of this class represent units for expressing _plane angle_ measurements.
   *
   *  @constructor Create new _[[http://en.wikipedia.org/wiki/Angle plane angle]]_ unit of measurement.
   *
   *  @param converter Rules to be applied to convert a quantity measured in these units to the standard _plane angle
   *  [[http://en.wikipedia.org/wiki/SI SI]]_ units, _radians_.
   *
   *  @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   *  @since 0.0
   */
  final class AngleUnits private[phys](converter: Converter, symbol: String)
  extends SpecificUnits(converter, symbol)
}