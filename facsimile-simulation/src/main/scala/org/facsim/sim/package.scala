//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
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
// Scala source file belonging to the org.facsim.sim package.
//======================================================================================================================
package org.facsim.sim

import cats.data.State
import org.facsim.collection.immutable.BinomialHeap
import org.facsim.sim.engine.SimulationState
import org.facsim.sim.model.ModelState
import scala.util.Try

/** Type representing an event priority.
 *
 *  @since 0.0
 */
type Priority = Int

/** Type representing used to represent an immutable priority queue in the simulation.
 *
 *  @tparam A Type of element stored in the priority queue. There must be an implicit ordering available for events.
 *
 *  @since 0.0
 */
type PriorityQueue[A] = BinomialHeap[A]

/** Type for simulation state transition results.
 *
 *  Represents a transition in the value of a [[org.facsim.sim.engine.SimulationState]].
 *
 *  @tparam M Actual type of the simulation's model state.
 *
 *  @tparam A Result of the state transition operation.
 *
 *  @since 0.0
 */
type SimulationTransition[M <: ModelState[M], A] = State[SimulationState[M], A]

/** Type for simulation state transition actions, which return a status value.
 *
 *  Represents a set of actions that should be applied to the current [[org.facsim.sim.engine.SimulationState]],
 *  resulting in a new simulation state wrapped in [[scala.util.Success]] if successful, or an exception instance
 *  wrapped in [[scala.util.Failure]] otherwise.
 *
 *  @tparam M Actual type of the simulation's model state.
 *
 *  @since 0.0
 */
type SimulationAction[M <: ModelState[M]] = SimulationTransition[M, Try[Unit]]
