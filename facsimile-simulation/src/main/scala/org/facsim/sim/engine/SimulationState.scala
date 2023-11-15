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
// Scala source file belonging to the org.facsim.sim.engine package.
//======================================================================================================================
package org.facsim.sim.engine

import org.facsim.sim.PriorityQueue
import org.facsim.sim.model.ModelState
import scala.reflect.runtime.universe.TypeTag
import squants.Time
import squants.time.Seconds

/** Encapsulates the state of a simulation model at in instant in (simulation) time.
 *
 *  @tparam M Final type of the simulation's model state.
 *
 *  @constructor Create a new simulation state.
 *
 *  @param modelState Simulation model state, tracking changes in the state of the model itself. Instances must be
 *  immutable.
 *
 *  @param nextEventId Identifier of the next simulation event to be created.
 *
 *  @param current Event currently being dispatched, wrapped in `[[scala.Some Some]]`; if `[[scala.None None]]`, then
 *  the simulation has typically not yet started running.
 *
 *  @param events Set of simulation events scheduled to occur at a future simulation time.
 *
 *  @param runState Current state of the simulation run.
 *
 *  @param sim Simulation to which this simulation state applies, typically passed implicitly.
 *
 *  @since 0.0
 */
final class SimulationState[M <: ModelState[M]: TypeTag] private[engine](private[engine] val modelState: M,
private[engine] val nextEventId: Long, private[engine] val current: Option[Event[M]],
private[engine] val events: PriorityQueue[Event[M]], private[engine] val runState: RunState)
(implicit sim: Simulation[M]) {

  /** Copy the existing state to a new state with the indicated new values.
   *
   *  @param newModelState New simulation model state, tracking changes in the state of the model itself. Instances must
   *  be immutable.
   *
   *  @param newNextEventId New identifier of the next simulation event to be created.
   *
   *  @param newCurrent New event to become the current event being dispatched, wrapped in `[[scala.Some Some]]`; if
   *  `[[scala.None None]]`, then the simulation has typically not yet started running.
   *
   *  @param newEvents New set of simulation events scheduled to occur at a future simulation time.
   *
   *  @param newRunState New run state of the simulation run.
   *
   *  @return Updated simulation state.
   */
  private[engine] def update(newModelState: M = modelState, newNextEventId: Long = nextEventId,
  newCurrent: Option[Event[M]] = current, newEvents: PriorityQueue[Event[M]] = events,
  newRunState: RunState = runState): SimulationState[M] = {
    new SimulationState(newModelState, newNextEventId, newCurrent, newEvents, newRunState)
  }

  /** Report the current simulation time.
   *
   *  @return Current simulation time.
   */
  private[engine] def simTime: Time = current.fold(Seconds(0.0))(_.dueAt)

  /** Report the model's current state.
   *
   *  @return State of the model associated with this simulation.
   */
  private[engine] def state: ModelState[M] = modelState

  /** Report the model's current execution state.
   *
   *  @return Current execution state of the model.
   */
  private[engine] def execState = runState

  /** Report the simulation to which this state belongs.
   *
   *  @return Simulation instance to which this state belongs.
   *
   *  @since 0.0
   */
  def simulation: Simulation[M] = sim
}
