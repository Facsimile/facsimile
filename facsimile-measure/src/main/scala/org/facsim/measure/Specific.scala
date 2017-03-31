//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2017, Michael J Allen.
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
// Scala source file belonging to the org.facsim.measure package.
//======================================================================================================================
package org.facsim.measure

import org.facsim.util.requireNonNull
import scala.language.implicitConversions

/**
 * Abstract base class for all physical quantity types associated with specific physical quantity families.
 *
 * @since 0.0
 */
/*
 * Developer notes:
 *
 * This is an abstract class, rather than a trait, to prevent it from being used as a base class. The rationale is that
 * the implementation of this class, from the viewpoint of a subclass, might change dramatically during Facsimile's
 * existence. Since there are no user-serviceable parts inside, it has been deemed that the best approach is simply to
 * keep a tight lid on things.
 */
abstract class Specific protected[measure]
extends Physical  {

  /*
   * Reference to this specific instance, for use in measure class definition below.
   */
  specific =>

  /**
   * @inheritdoc
   */
  override type Measure <: SpecificMeasure

  /**
   * @inheritdoc
   */
  override type Units <: SpecificUnits

  /**
   * Physical quantity family represented by this specific type.
   */
  protected[measure] val family: Family

  /**
   * Value representing a zero measurement in the associated ''[[http://en.wikipedia.org/wiki/SI SI]]'' units.
   *
   * @since 0.0
   */
  final lazy val Zero = apply(0.0)

  /**
   * Name of this physical quantity.
   *
   * @since 0.0
   */
  val name: String

  /**
   * Generic measurement to specific measurement conversion function.
   *
   * If an attempt is made to convert a generic measurement associated with a different family to that of the target
   * type, then a run-time exception will result.
   *
   * @param measure Generic measure to be converted.
   *
   * @return Specific measurement equivalent to the specified generic `measure`.
   *
   * @throws NullPointerException if `measure` is `null`.
   *
   * @throws org.facsim.measure.GenericConversionException if `measure` is associated with different family to the
   * target specific family.
   *
   * @since 0.0
   */
  implicit final def fromGeneric(measure: Generic.Measure): Measure = {
    requireNonNull(measure)
    if(measure.family == family) apply(measure.value)
    else throw new GenericConversionException(measure, family)
  }

  /**
   * Factory method to create a new measurement value in the specified units.
   *
   * @param value Measurement's value in specified `units`. This value must be finite and must lie within the defined
   * range for the associated physical quantity.
   *
   * @param units Units in which the measurement's `value` is expressed.
   *
   * @return Corresponding measurement value.
   *
   * @throws NullPointerException if `units` are `null`.
   *
   * @throws IllegalArgumentException if `value` in the specified `units` is not finite or is outside of the
   * defined domain for the associated physical quantity.
   *
   * @since 0.0
   */
  final def apply(value: Double, units: Units): Measure = {
    requireNonNull(units)
    apply(units.importValue(value))
  }

  /**
   * Factory method to create a new measurement value in ''[[http://en.wikipedia.org/wiki/SI SI]]'' units.
   *
   * @note This function is not public because it introduces the potential for unit confusion. Measurements can only be
   * manipulated by users as [[org.facsim.measure.Physical.PhysicalMeasure]] subclass instances, not as raw values.
   * Allowing access to raw values encourages by-passing of the unit protection logic provided by these measurement
   * classes.
   *
   * @param measure Measurement's value in ''SI'' units. This value must be finite and must lie within the defined
   * range for the associated physical quantity.
   *
   * @return Corresponding measurement value.
   *
   * @throws IllegalArgumentException If `measure` in ''SI'' units is not finite or is outside of the defined
   * range for the associate physical quantity.
   */
  private[measure] def apply(measure: Double): Measure

  /**
   * Abstract base class for all specific ''Facsimile [[http://en.wikipedia.org/wiki/Physical_quantity physical
   * quantity]]'' measurement classes.
   *
   * Measurements are stored internally in the corresponding ''[[http://en.wikipedia.org/wiki/SI SI]]'' units for this
   * physical quantity family.
   *
   * @constructor Create new measurement for this ''[[http://en.wikipedia.org/wiki/Physical_quantity physical
   * quantity]]''.
   *
   * @param measure Value of the measurement expressed in the associated ''[[http://en.wikipedia.org/wiki/SI SI]]''
   * units. This value must be finite, but subclasses may impose additional restrictions.
   *
   * @throws IllegalArgumentException if `measure` is not finite or is invalid for these units.
   *
   * @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on [[http://en.wikipedia.org/ Wikipedia]].
   *
   * @since 0.0
   */
  abstract class SpecificMeasure protected[measure](measure: Double)
  extends PhysicalMeasure(measure)
  with Ordered[Measure] {

    /**
     * @inheritdoc
     */
    protected[measure] final override def family = specific.family

    /**
     * @inheritdoc
     */
    protected[measure] final override def createNew(newMeasure: Double) = apply(newMeasure)

    /**
     * Convert this measurement value to a string, expressed in the user's preferred units.
     *
     * @return A string containing the value of the measurement and the units in which the measurement is expressed,
     * in the user's preferred locale.
     *
     * @see [[scala.AnyRef.toString()*]]
     *
     * @since 0.0
     */
    final override def toString = preferredUnits.format(value)

    /**
     * Compare this measurement with another measurement of the same type.
     *
     * @param that Measurement that this measurement is to be compared to.
     *
     * @return Negative value is this measurement is less than `that`'s value, 0 if this measurement equals `that`'s
     * value, or a positive value if this measurement is greater than `that`'s value.
     *
     * @throws NullPointerException if `that` is `null`.
     *
     * @since 0.0
     */
    final override def compare(that: Measure) = {
      requireNonNull(that)
      value.compare(that.value)
    }
  }

  /**
   * Abstract base class for all specific physical quantity measurement units.
   *
   * @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on [[http://en.wikipedia.org/]].
   *
   * @constructor Create new instance of a specific physical quantity unit.
   *
   * @param converter Rules to be applied to convert a quantity measured in these units to and from the standard ''SI''
   * units for this unit family.
   *
   * @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   * @since 0.0
   */
  abstract class SpecificUnits protected[measure](converter: Converter, protected[measure] final val symbol: String)
  extends PhysicalUnits {

    /**
     * Employ the converter to handle importing of values.
     *
     * @inheritdoc
     */
    private[measure] final override def importValue(value: Double) = converter.importValue(value)

    /**
     * Employ the converter to handle exporting of values.
     *
     * @inheritdoc
     */
    private[measure] final override def exportValue(value: Double) = converter.exportValue(value)

    /**
     * Format a value in these units for output.
     *
     * @param value Measurement value, expressed in ''SI'' units, to be output.
     *
     * @return Formatted string, containing the value and the units (if any).
     */
    private[measure] final def format(value: Double) = {
      LibResource("Physical.Units.format", exportValue(value), symbol)
    }
  }
}