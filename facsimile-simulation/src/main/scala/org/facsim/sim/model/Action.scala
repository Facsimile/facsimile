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
// Scala source file belonging to the org.facsim.sim.model package.
//======================================================================================================================
package org.facsim.sim.model

import org.facsim.sim.SimulationAction
import scala.reflect.runtime.universe.TypeTag

/** An _action_ is a _state transition_ that takes the state of the simulation and results in a new simulation
 *  state.
 *
 *  Simulation state changes include:
 *   - Scheduling another action to occur at a future time in the simulation.
 *   - Changing the state of a simulation model element.
 *   - Moving a simulation model entity from one element to another.
 *   - etc.
 *
 *  @tparam M Final model state type.
 *
 *  @constructor Create a new simulation action.
 *
 *  @since 0.0
 */
abstract class Action[M <: ModelState[M]: TypeTag] {

  /** Actions to be performed by this instance.
   *
   *  @since 0.0
   */
  protected val actions: SimulationAction[M]

  /** Dispatch these actions.
   *
   *  @return Simulation state after dispatching (executing) these actions.
   */
  final def dispatch: SimulationAction[M] = for {
    r <- actions
  } yield r

  /** Name of this event.
   *
   *  Short (and, ideally, unique) description of these actions, to be utilized in the simulation event log, debugging
   *  operations, etc.
   *
   *  @since 0.0
   */
  val name: String

  /** Description of these actions.
   *
   *  Brief description of these actions, including any additional relevant details.
   *
   *  @since 0.0
   */
  val description: String
}