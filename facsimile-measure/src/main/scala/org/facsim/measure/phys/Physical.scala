//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2019, Michael J Allen.
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
// Scala source file belonging to the org.facsim.measure.phys package.
//======================================================================================================================
package org.facsim.measure.phys

import org.facsim.util.{requireFinite, requireNonNull}
import scala.util.Try

/** Abstract base class for all ''Facsimile [[http://en.wikipedia.org/wiki/Physical_quantity physical quantity]]''
 *  elements.
 *
 *  Physical quantity types include specific types (such as ''[[Angle]]'', ''[[Length]]'', ''[[Time]]'', etc.), as well
 *  as a ''[[Generic]]'' convenience type for the result of multiplication and division between the specific types. Each
 *  of these types has measurement values and units in which those measurements are expressed.
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
  // Developer notes: This type must typically be overridden, even if just specify additional type constraints, in each
  // sub-class.
  type Measure <: PhysicalMeasure

  /** Type representing units which measurements of this physical quantity may be expressed.
   *
   *  @since 0.0
   */
  // Developer notes: This type must typically be overridden, even if just specify additional type constraints, in each
  // sub-class.
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
   *  @param value Value of the measurement expressed in ''SI'' units.
   *
   *  @return A measurement with the specified `value` in ''SI'' units, wrapped in a [[scala.util.Success]] if
   *  successful, or a [[scala.util.Failure]] wrapping a failure exception in the event that `value` is invalid.
   */
  protected[phys] def apply(value: Double): Try[Measure]

  /** Value representing a ''zero'' measurement in this physical quantity's ''[[http://en.wikipedia.org/wiki/SI SI]]
   *  units''.
   *
   *  @since 0.0
   */
  // Developer note: This value must NOT throw an exception for any measurement family.
  final val Zero: Measure = apply(0.0).get

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
   *  @throws IllegalArgumentException if `value` is not finite or is invalid for these units.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on [[http://en.wikipedia.org/ Wikipedia]].
   */
  abstract class PhysicalMeasure protected[phys](protected[measure] val value: Double)
  extends Equals {

    // Ensure that value is a finite number, and is not infinite or not-a-number (NaN).
    requireFinite(value)

    /** Create new measurement in the same physical quantity family as this measurement.
     *
     *  @param newValue New measurement expressed in this physical quantity's ''[[http://en.wikipedia.org/wiki/SI SI]]''
     *  units.
     *
     *  @return Measurement with the specified `value` in ''SI'' units, wrapped in a [[scala.util.Success]] if
     *  successful, or a [[scala.util.Failure]] wrapping a failure exception in the event that `value` is invalid.
     */
    private[measure] final def createNew(newValue: Double) = apply(newValue)

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
    private[phys] final def inUnits(units: Units) = units.exportValue(value)

    /** @inheritdoc */
    // This method is overridden because physical measurements can be compared for equality if the other value is a
    // physical quantity in the same family.
    final override def canEqual(that: Any): Boolean = that match {

      // If "that" is a subclass of PhysicalMeasure, then we can equal the other value provided that "that" is a
      // measurement of the same physical quantity family as this instance. If they are for different families, then we
      // cannot equal the other value.
      case other: PhysicalMeasure => family === other.family

      // The default case, that "that" is not a measure value, means that we cannot compare values as being equal.
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
     *  @return `true` if `that` is a measurement belonging to the same family as this measurement and both have the same
     *  exact value. `false` is returned if `that` is `null` or is not a measurement value, if `that` is a measurement
     *  belonging to a different family to this value, or if `that` has a different value to this measurement.
     *
     *  @see [[scala.AnyRef.equals(Any)*]]
     *
     *  @since 0.0
     */
    final override def equals(that: Any): Boolean = that match {

      // If the other object is a PhysicalMeasure subclass, and that value can be compared as equal to this value (they
      // belong to the same physical quantity family), and they have the same value, then that equals this.
      case other: PhysicalMeasure => other.canEqual(this) && value == other.value

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
     *  @see [[Any!.hashCode]]
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
  //scalastyle:on disallow.space.before.token

  /** Abstract base class for all physical quantity measurement units.
   *
   *  Each concrete subclass represents a single ''physical quantity unit family''. For example, time units are
   *  represented by the [[Time.TimeUnits]] `PhysicalUnits` subclass.
   *
   *  Each unit family supports one or more ''units of measure''. For example, time quantities may be measured in
   *  ''seconds'', ''minutes'', ''hours'', etc. These units are represented by instances of the concrete `PhysicalUnits`
   *  subclass. For each unit family, there is a standard unit of measure defined by the
   *  ''[[http://en.wikipedia.org/wiki/SI International System of Units]]''&mdash;commonly abbreviated to ''SI Units''.
   *
   *  These standard units are used by ''Facsimile'' internally to store measurement quantities (as immutable instances
   *  of [[Physical.PhysicalMeasure]] subclasses, with each subclass corresponding to each measurement type.) For
   *  example, the ''SI'' standard unit of measure for ''time'' is the ''second''; consequently, ''Facsimile'' stores
   *  and calculates all time quantities, internally, in ''seconds''. Adoption of the ''SI'' standard units simplifies
   *  the implementation of physics calculations within ''Facsimile'' and provides a clearly-defined basis for
   *  developing simulation models of the real-world. (Many other simulation modeling tools suffer from ''unit of
   *  measure confusion'', both internally and externally, creating a wide variety of problems.)
   *
   *  However, it is unreasonable to expect that ''Facsimile'' users would be comfortable entering and reviewing data
   *  solely in these units. For instance, the ''SI'' standard unit of measure for ''angles'' is the
   *  ''radian''&mdash;and there are few people who don't find the ''degree'' a far more intuitive alternative.
   *  Similarly, users in the United States&mdash;or their customers&mdash;might prefer ''feet'' & ''inches'',
   *  ''pounds'', ''Fahrenheit'', etc. to their metric equivalents. Consequently, ''Facsimile'' allows users to work
   *  with whichever units they prefer. ''Facsimile'' converts values to the standard ''SI'' units on input and converts
   *  them to the required units on output.
   *
   *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on [[http://en.wikipedia.org/]].
   *
   *  @constructor Create a new unit of measure for this physical quantity.
   *
   *  @since 0.0
   */
  abstract class PhysicalUnits protected[phys]
  extends Converter
}