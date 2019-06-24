//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
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
// Scala source file belonging to the org.facsim.types.phys types.
//======================================================================================================================
package org.facsim.types.phys

import org.facsim.util.requireValid
import scala.reflect.ClassTag

/** Abstract base class for all physical quantity types that cannot have negative measurement values in the associated
 *  ''[[http://en.wikipedia.org/wiki/SI SI]]'' units.
 *
 *  Subclasses are forbidden from having negative measurement values, which is appropriate for a number of unit
 *  families, including [[Time]], [[Temperature]], [[Mass]], etc.
 *
 *  @since 0.0
 */
// Developer notes:
//
// This is an abstract class, rather than a trait, to prevent it from being used as a base class. The rationale is that
// the implementation of this class, from the viewpoint of a subclass, might change dramatically during Facsimile's
// existence. Since there are no user-serviceable parts inside, it has been deemed that the best approach is simply to
// keep a tight lid on things.
abstract class NonNegative protected[phys]
extends Specific {

  /** @inheritdoc */
  override type Measure <: NonNegativeMeasure[Measure]

  /** @inheritdoc */
  override type Units <: NonNegativeUnits

  /** Abstract base class for physical quantity measurements that cannot be negative.
   *
   *  @tparam F Final measurement type.
   *
   *  @constructor Construct new non-negative measurement value.
   *
   *  @param measure Value of the measurement type in the associated ''SI'' units. This value must be finite and
   *  non-negative. Sub-classes may impose additional restrictions.
   *
   *  @throws IllegalArgumentException If the result is not finite or is negative.
   *
   *  @since 0.0
   */
  //scalastyle:off disallow.space.before.token
  abstract class NonNegativeMeasure[F <: NonNegativeMeasure[F] : ClassTag] protected[phys](measure: Double)
  //scalastyle:on disallow.space.before.token
  extends SpecificMeasure[F](measure) {

    // If the types is negative, then report an error.
    requireValid(measure, measure >= 0.0)
  }

  /** Abstract base class for all specific physical quantity measurement units, that do not support negative SI unit
   *  measurement values.
   *
   *  @constructor Construct new non-negative measurement units.
   *
   *  @param converter Rules to be applied to convert a quantity measured in these units to and from the standard ''SI''
   *  units for this unit family.
   *
   *  @param symbol Symbol to be used when outputting measurement values expressed in these units.
   *
   *  @since 0.0
   */
  abstract class NonNegativeUnits protected[phys](converter: Converter, symbol: String)
  extends SpecificUnits(converter, symbol)
}