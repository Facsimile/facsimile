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

import org.facsim.sim.{LibResource, SimulationAction}
import org.facsim.sim.engine.{Completed, Simulation}
import scala.reflect.runtime.universe.TypeTag
import squants.Time

/** Standard simulation actions to perform a reset of the simulation's statistics.
 *
 *  @constructor Create a new end warmup action.
 *
 *  @tparam M Final type of the simulation's model state.
 *
 *  @param snapLength Length of subsequent simulation snaps.
 *
 *  @param snapsRemaining Number of simulation snaps to be performed.
 *
 *  @param simulation Reference to the executing simulation.
 */
private[sim] final class EndSnapAction[M <: ModelState[M]: TypeTag](snapLength: Time, snapsRemaining: Int)
(implicit simulation: Simulation[M])
extends Action[M] {

  /** @inheritdoc */
  override protected val actions: SimulationAction[M] = {

    // Sanity check.
    assert(snapsRemaining >= 0)

    // Report to all subscribers that the simulation snap has completed. Statistics should be reset and a report
    // generated accordingly.
    // TODO

    // If this is the last snap, then change the simulation state to completed.
    if(snapsRemaining == 0) for {
      r <- simulation.updateRunState(Completed)
    } yield r

    // Otherwise, schedule the end of the next snap.
    else for {
      r <- simulation.at(snapLength, Int.MaxValue)(new EndSnapAction[M](snapLength, snapsRemaining - 1))
    } yield r
  }

  /** @inheritdoc */
  override val name: String = LibResource("model.EndSnapActionName")

  /** @inheritdoc */
  override val description: String = LibResource("model.EndSnapActionDesc")
}
