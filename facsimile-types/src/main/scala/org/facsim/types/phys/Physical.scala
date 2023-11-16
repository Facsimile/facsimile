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

import org.facsim.types.algebra.AdditiveTypedSemigroup
import org.facsim.util.requireFinite
import scala.util.Try
import spire.algebra.Order

/** Abstract base class for all ''Facsimile [[http://en.wikipedia.org/wiki/Physical_quantity physical quantity]]''
 *  elements.
 *
 *  Physical quantity types include specific types (such as `[[org.facsim.types.phys.Angle Angle]]`,
 *  `[[org.facsim.types.phys.Length Length]]`, `[[org.facsim.types.phys.Time Time]]`, etc.), as well as a
 *  `[[org.facsim.types.phys.Generic Generic]]` convenience type for the result of multiplication and division between
 *  the specific types. Each of these types has measurement values and units in which those measurements are expressed.
 *
 *  @see [[http://en.wikipedia.org/wiki/Physical_quantity Physical quantity]] on ''Wikipedia''.
 *
 *  @since 0.0
 */
// Developer notes:
//
// This is an abstract class, rather than a trait, to prevent it from being used as a base class. The rationale is that
// the implementation of this class, from the viewpoint of a subclass, might change dramatically during Facsimile's
// existence. Since there are no user-serviceable parts inside, it has been deemed that the best approach is simply to
// keep a tight lid on things. Currently, Scala does not restrict the instantiation of traits in the same way that
// instantiation of abstract classes can be controlled.
abstract class Physical {

  /** Type presenting measurements of this physical quantity.
   *
   *  @since 0.0
   */
  // Developer notes: This type must typically be overridden, even if just to specify additional type constraints, in
  // each sub-class.
  type Measure <: PhysicalMeasure

  /** Type representing units which measurements of this physical quantity may be expressed.
   *
   *  @since 0.0
   */
  // Developer notes: This type must typically be overridden, even if just to specify additional type constraints, in
  // each sub-class.
  type Units <: PhysicalUnits

  /** ''[[http://en.wikipedia.org/wiki/SI SI]]'' standard units for this physical quantity.
   *
   *  @see [[[[http://en.wikipedia.org/wiki/SI International System of Units]] on ''Wikipedia''.
   *
   *  @since 0.0
   */
  // Developer notes: This value must be overridden by each final physical measurement, and must identify the SI units
  // of this element.
  val siUnits: Units

  /** User's preferred units for this physical quantity, or the associated ''[[http://en.wikipedia.org/wiki/SI SI]]
   *  units'' if no preference has been specified.
   *
   *  @todo The SI units for this physical quantity are currently reported. It is intended that&mdash;in
   *  future&mdash;this returns the user's preferred units, if specified, or the SI units otherwise; the mechanism for
   *  specifying preferred units is currently not implemented.
   *
   *  @return User's preferred units for this physical quantity.
   *
   *  @since 0.0
   */
  // Developer notes. As per the TODO note, no further work is required right now. However, it is anticipated that this
  // function will retrieve and validate an application user-configuration property.
  final def preferredUnits: Units = siUnits

  /** Factory method to create a measurement expressed in this physical quantity's ''[[http://en.wikipedia.org/wiki/SI
   *  SI]] units''.
   *
   *  @note This function is not public because it introduces the potential for unit confusion. Measurements can only be
   *  manipulated by users as `[[org.facsim.types.phys.Physical.PhysicalMeasure PhysicalMeasure]]` subclass instances,
   *  not as raw values. Allowing access to raw values encourages by-passing of the unit protection logic provided by
   *  these measurement classes.
   *
   *  @param value Value of the measurement expressed in ''SI'' units.
   *
   *  @return A measurement with the specified `value` in ''SI'' units, wrapped in a `[[scala.util.Success Success]]` if
   *  successful, or a `[[scala.util.Failure Failure]]` wrapping a failure exception in the event that `value` is
   *  invalid.
   */
  protected[phys] final def apply(value: Double): Try[Measure] = Try(newMeasure(value))

  /** Factory method to create a measurement expressed in this physical quantity's ''[[http://en.wikipedia.org/wiki/SI
   *  SI]] units''.
   *
   *  @note This function is not public for two reasons: firstly, because it is a partial function that is defined only
   *  if `value` is a valid ''SI'' unit measurements; secondly, because it introduces the potential for unit confusion.
   *  Measurements can only be manipulated by users as `[[org.facsim.types.phys.Physical.PhysicalMeasure
   *  PhysicalMeasure]]` subclass instances, not as raw values. Allowing access to raw values encourages by-passing of
   *  the unit protection logic provided by these measurement classes.
   *
   *  @param value Value of the measurement expressed in ''SI'' units.
   *
   *  @return A measurement with the specified `value` in ''SI'' units, provided that the measurement is valid.
   *
   *  @throws scala.IllegalArgumentException if creation of the types value fails due to the value being out of range.
   */
  protected[phys] def newMeasure(value: Double): Measure

  /** Value representing a ''zero'' measurement in this physical quantity's ''[[http://en.wikipedia.org/wiki/SI SI]]
   *  units''.
   *
   *  @since 0.0
   */
  // Developer note: This must NOT throw an exception for any measurement family.
  final lazy val Zero: Measure = newMeasure(0.0)

  /** Ordering for physical measurements.
   *
   *  Supports comparison operators for physical quantity measurements.
   *
   *  @since 0.0
   */
  final implicit val physicalOrder: Order[Measure] = new Order[Measure] {

    /** Compare two physical measurement values, determining their relative order.
     *
     *  @param x
     *
     *  @param y
     *
     *  @return 0 if `x` and `y` compare as equal, a negative value is `x` compares as ''less than'' `y` or a positive
     *  value if `x` compares as ''greater than'' `y`.
     *
     *  @since 0.0
     */
    override def compare(x: Measure, y: Measure): Int = x.value.compareTo(y.value)
  }

  /** Addition typed semigroup for physical quantity measurements.
   *
   *  Supports addition operators for physical quantity measurements.
   *
   *  @since 0.0
   */
  final implicit val physicalAdditionSemigroup1: AdditiveTypedSemigroup[Measure, Measure, Try[Measure]] = {
    new AdditiveTypedSemigroup[Measure, Measure, Try[Measure]] {

      /** Add two physical measurement quantities belonging to the same family together.
       *
       *  @param a First value being added.
       *
       *  @param b Second value being added.
       *
       *  @return Result of the addition, wrapped in a `[[scala.util.Success Success]]` if successful, or a
       *  `[[scala.util.Failure Failure]]` if unsuccessful.
       */
      override def combine(a: Measure, b: Measure): Try[Measure] = apply(a.value + b.value)
    }
  }

  /** Addition typed semigroup for physical quantity measurements.
   *
   *  Supports addition operators for physical quantity measurements.
   *
   *  @since 0.0
   */
  final implicit val physicalAdditionSemigroup2: AdditiveTypedSemigroup[Measure, Try[Measure], Try[Measure]] = {
    new AdditiveTypedSemigroup[Measure, Try[Measure], Try[Measure]] {

      /** Add two physical measurement quantities belonging to the same family together.
       *
       *  @param a First value being added.
       *
       *  @param bt Second value being added, wrapped in a `[[scala.util.Try Try]]`.
       *
       *  @return Result of the addition, wrapped in a `[[scala.util.Success Success]]` if successful, or a
       *  `[[scala.util.Failure Failure]]` if unsuccessful.
       */
      override def combine(a: Measure, bt: Try[Measure]): Try[Measure] = bt.flatMap(b => apply(a.value + b.value))
    }
  }

  /** Addition typed semigroup for physical quantity measurements.
   *
   *  Supports addition operators for physical quantity measurements.
   *
   *  @since 0.0
   */
  final implicit val physicalAdditionSemigroup3: AdditiveTypedSemigroup[Try[Measure], Measure, Try[Measure]] = {
    new AdditiveTypedSemigroup[Try[Measure], Measure, Try[Measure]] {

      /** Add two physical measurement quantities belonging to the same family together.
       *
       *  @param at First value being added, wrapped in a `[[scala.util.Try Try]]`.
       *
       *  @param b Second value being added.
       *
       *  @return Result of the addition, wrapped in a `[[scala.util.Success Success]]` if successful, or a
       *  `[[scala.util.Failure Failure]]` if unsuccessful.
       */
      override def combine(at: Try[Measure], b: Measure): Try[Measure] = at.flatMap(a => apply(a.value + b.value))
    }
  }

  /** Addition typed semigroup for physical quantity measurements.
   *
   *  Supports addition operators for physical quantity measurements.
   *
   *  @since 0.0
   */
  final implicit val physicalAdditionSemigroup4: AdditiveTypedSemigroup[Try[Measure], Try[Measure], Try[Measure]] = {
    new AdditiveTypedSemigroup[Try[Measure], Try[Measure], Try[Measure]] {

      /** Add two physical measurement quantities belonging to the same family together.
       *
       *  @param at First value being added, wrapped in a `[[scala.util.Try Try]]`.
       *
       *  @param bt Second value being added, wrapped in a `[[scala.util.Try Try]]`.
       *
       *  @return Result of the addition, wrapped in a `[[scala.util.Success Success]]` if successful, or a
       *  `[[scala.util.Failure Failure]]` if unsuccessful.
       */
      override def combine(at: Try[Measure], bt: Try[Measure]): Try[Measure] = at.flatMap {a =>
        bt.flatMap {b =>
          apply(a.value + b.value)
        }
      }
    }
  }

  /** Abstract base class for all ''Facsimile [[http://en.wikipedia.org/wiki/Physical_quantity physical quantity]]''
   *  measurement classes.
   *
   *  Measurements are stored internally in the corresponding ''[[http://en.wikipedia.org/wiki/SI SI]] units'' for this
   *  physical quantity family.
   *
   *  @constructor Create new measurement for the associated ''[[http://en.wikipedia.org/wiki/Physical_quantity physical
   *  quantity]]''.
   *
   *  @param value Value of the measurement expressed in the associated ''[[http://en.wikipedia.org/wiki/SI SI]]''
   *  units. This value must be finite, but sub-classes may impose additional restrictions. It is a core design goal of
   *  the ''Facsimile Measurement Library'' that these raw values must be unavailable to end user code.
   *
   *  @throws scala.IllegalArgumentException if `value` is not finite or is invalid for these units.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on [[http://en.wikipedia.org/ Wikipedia]].
   */
  abstract class PhysicalMeasure protected[phys](protected[types] val value: Double)
  extends Equals {

    // Ensure that value is a finite number, and is not infinite or not-a-number (NaN).
    requireFinite(value)

    /** Create new measurement in the same physical quantity family as this measurement.
     *
     *  @param newValue New measurement expressed in this physical quantity's ''[[http://en.wikipedia.org/wiki/SI SI]]''
     *  units.
     *
     *  @return Measurement with the specified `value` in ''SI'' units, wrapped in a `[[scala.util.Success Success]]` if
     *  successful, or a `[[scala.util.Failure Failure]]` wrapping a failure exception in the event that `value` is
     *  invalid.
     */
    private[types] final def createNew(newValue: Double): Try[Measure] = apply(newValue)

    /** Create new measurement in the same physical quantity family as this measurement.
     *
     *  @param newValue New measurement expressed in this physical quantity's ''[[http://en.wikipedia.org/wiki/SI SI]]''
     *  units.
     *
     *  @return Measurement with the specified `value` in ''SI'' units.
     *
     *  @throws scala.IllegalArgumentException if creation of the types value fails due to the value being out of range.
     */
    private[types] final def createNewMeasure(newValue: Double): Measure = newMeasure(newValue)

    /** Physical quantity family to which this measurement value belongs.
     *
     *  @return Physical quantity family to which this measurement belongs.
     */
    protected[phys] def family: Family

    /** Retrieve measurement expressed in the specified units.
     *
     *  @note This function is not public to prevent users from by-passing the unit-protection logic provided by these
     *  physical quantity classes.
     *
     *  @param units Units in which the measurement is to be expressed.
     *
     *  @return Measurement in specified `units`.
     */
    private[phys] final def inUnits(units: Units): Double = units.exportValue(value)

    /** Calculate the absolute value of the measurement.
     *
     *  @note The absolute value is based upon the measurement in ''SI'' units. For measurement unit families that do
     *  not have a common origin (such as `[[org.facsim.types.Temperature Temperature]]`), this can result in
     *  unintuitive results. For example, the absolute value of -5°C is -5°C, not 5°C, due to the fact that -5°C is
     *  268.15K, whose absolute value is unchanged.
     *
     *  @return The absolute value of the measurement, based upon it's ''SI'' units.
     *
     *  @since 0.0
     */
    final def abs: Measure = {
      if(value < 0.0) createNewMeasure(-value)
      else this.asInstanceOf[Measure]
    }

    /** Change the sign of a measurement value.
     *
     *  @note All measurements that do not permit negative values will throw exceptions when this operation is invoked
     *  on a valid value.
     *
     *  @return Measurement value having a sign opposite that of this value.
     *
     *  @since 0.0
     */
    final def unary_- : Try[Measure] = createNew(-value)

    /** @inheritdoc */
    // This method is overridden because physical measurements can be compared for equality if the other value is a
    // physical quantity in the same family.
    final override def canEqual(that: Any): Boolean = that match {

      // If "that" is a subclass of PhysicalMeasure, then we can equal the other value provided that "that" is a
      // measurement of the same physical quantity family as this instance. If they are for different families, then we
      // cannot equal the other value.
      case other: Measure => family === other.family

      // The default case, that "that" is not a types value, means that we cannot compare values as being equal.
      case _ => false
    }

    /** Compare this measurement to another object for equality.
     *
     *  @note If two objects compare equal, then their hash codes must compare equal too. Similarly, if two objects have
     *  different hash codes, then they must not compare equal. However, if two objects have the same hash codes, they
     *  may or may not compare equal, since hash codes do not necessary map to unique values. That is, the balance of
     *  probability is that if two values are equal if they have the same hash codes, however this is not guaranteed and
     *  should not be relied upon.
     *
     *  @param that Object being tested for equality with this measurement.
     *
     *  @return `true` if `that` is a measurement belonging to the same family as this measurement and both have the
     *  same exact value. `false` is returned if `that` is `null` or is not a measurement value, if `that` is a
     *  measurement belonging to a different family to this value, or if `that` has a different value to this
     *  measurement.
     *
     *  @since 0.0
     */
    final override def equals(that: Any): Boolean = that match {

      // If the other object is a PhysicalMeasure subclass, and that value can be compared as equal to this value (they
      // belong to the same physical quantity family), and they have the same value, then that equals this.
      case other: Measure => other.canEqual(this) && value == other.value

      // All other values (including null), cannot be equal to this value.
      case _ => false
    }

    /** Return this measurement value's hash code.
     *
     *  @note If two objects compare equal, then their hash codes must compare equal too. Similarly, if two objects have
     *  different hash codes, then they must not compare equal. However, if two objects have the same hash codes, they
     *  may or may not compare equal, since hash codes do not necessary map to unique values. That is, the balance of
     *  probability is that if two values are equal if they have the same hash codes, however this is not guaranteed and
     *  should not be relied upon.
     *
     *  @return Hash code for this measurement value.
     *
     *  @since 0.0
     */
    // Since measurements have a value and a family, we take the hash code of each and use a binary XOR on them to
    // obtain our hash code. This ensures that measurements from different families, which do not compare equal, have
    // different hashes while also ensuring that equal values for the same families, which do compare equal, have
    // identical hash codes too. This fulfills the hash code/equality contract.
    //
    // There is probably no need to compare measurements to doubles (generic, unitless measurements are, in effect, the
    // same as Doubles), since we can implicitly convert either to the other.
    final override def hashCode: Int = value.hashCode ^ family.hashCode
  }

  /** Abstract base class for all physical quantity measurement units.
   *
   *  Each concrete subclass represents a single ''physical quantity unit family''. For example, time units are
   *  represented by the `[[org.facsim.types.phys.Time.TimeUnits TimeUnits]]` subclass of `PhysicalUnits`.
   *
   *  Each unit family supports one or more ''units of types''. For example, time quantities may be measured in
   *  ''seconds'', ''minutes'', ''hours'', etc. These units are represented by instances of the concrete `PhysicalUnits`
   *  subclass. For each unit family, there is a standard unit of types defined by the
   *  ''[[http://en.wikipedia.org/wiki/SI International System of Units]]''&mdash;commonly abbreviated to ''SI Units''.
   *
   *  These standard units are used by ''Facsimile'' internally to store measurement quantities (as immutable instances
   *  of `[[org.facsim.types.phys.Physical.PhysicalMeasure PhysicalMeasure]]` subclasses, with each subclass
   *  corresponding to each measurement type.) For example, the ''SI'' standard unit of types for ''time'' is the
   *  ''second''; consequently, ''Facsimile'' stores and calculates all time quantities, internally, in ''seconds''.
   *  Adoption of the ''SI'' standard units simplifies the implementation of physics calculations within ''Facsimile''
   *  and provides a clearly-defined basis for developing simulation models of the real-world. (Many other simulation
   *  modeling tools suffer from ''unit of types confusion'', both internally and externally, creating a wide variety of
   *  problems.)
   *
   *  However, it is unreasonable to expect that ''Facsimile'' users would be comfortable entering and reviewing data
   *  solely in these units. For instance, the ''SI'' standard unit of types for ''angles'' is the
   *  ''radian''&mdash;and there are few people who don't find the ''degree'' a far more intuitive alternative.
   *  Similarly, users in the United States&mdash;or their customers&mdash;might prefer ''feet'' & ''inches'',
   *  ''pounds'', ''Fahrenheit'', etc. to their metric equivalents. Consequently, ''Facsimile'' allows users to work
   *  with whichever units they prefer. ''Facsimile'' converts values to the standard ''SI'' units on input and converts
   *  them to the required units on output.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on [[http://en.wikipedia.org/]].
   *
   *  @constructor Create a new unit of types for this physical quantity.
   *
   *  @since 0.0
   */
  abstract class PhysicalUnits protected[phys]
  extends Converter
}