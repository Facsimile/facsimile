//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2018, Michael J Allen.
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
// Scala source file belonging to the org.facsim.measure.phys.test package.
//======================================================================================================================
package org.facsim.measure.phys.test

import org.facsim.measure.phys.Physical
import org.facsim.measure.test.EqualsFixture

//scalastyle:off scaladoc
/** Test fixture for testing [[Physical]] subclasses.
 *
 *  @tparam Q The `Physical` subclass being tested.
 */
trait PhysicalFixture[Q <: Physical] {

  /** Retrieve the associated concrete [[Physical]] subclass instance.
   *
   *  @return Physical quantity instance associated with this fixture.
   */
  val instance: Q

  /** Retrieve this physical quantities expected SI units.
   *
   *  @return The expected SI units for this physical quantity.
   */
  val expectedSIUnits: Q#Units

  /** List of non-finite measurement values, in associated SI units, that ought to be incapable of valid measurement
   *  construction, and which should result in an exception being thrown.
   *
   *  @return List of non-finite values none of which should be capable of valid construction when expressed in SI
   *  units.
   */
  final val nonFiniteValues = List(
    Double.NaN,
    Double.NegativeInfinity,
    Double.PositiveInfinity
  )

  /** Retrieve a fixture for testing the equality of different measurements.
   *
   *  @return Equals test fixture for this physical quantity. The test fixture will be used to verify that this physical
   *  quantity's measurement class correctly implements the ''equality contract''.
   */
  val equalsFixture: EqualsFixture[Q#Measure]
}
//scalastyle:on scaladoc