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

//======================================================================================================================
// Scala source file belonging to the org.facsim.engine package.
//======================================================================================================================
package org.facsim.engine

import cats.data.State
import scala.util.Success
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
 */
private[engine] final class EndSnapAction[M <: ModelState[M]](snapLength: Time, snapsRemaining: Int)
extends Action[M](EndSnapAction.actions(snapLength, snapsRemaining)) {

  /** @inheritdoc */
  override val name: String = LibResource("EndSnapActionName")

  /** @inheritdoc */
  override val description: String = LibResource("EndSnapActionDesc")
}

/** End snap action companion. */
private object EndSnapAction {

  /** Actions to be performed when dispatched.
   *
   *  @param snapLength Length of subsequent simulation snaps.
   *
   *  @param snapsRemaining Number of simulation snaps to be performed after this snap.
   */
  private def actions[M <: ModelState[M]](snapLength: Time, snapsRemaining: Int): SimulationAction[M] = {

    // Sanity check.
    assert(snapsRemaining >= 0)

    // Report to all subscribers that the simulation snap has completed. Statistics should be reset and a report
    // generated accordingly.
    // TODO

    // If this is the last snap, then change the simulation state to completed.
    if(snapsRemaining == 0) State {s =>
      (s.update(newRunState = Completed), Success(()))
    }

    // Otherwise, schedule the end of the next snap.
    else for {
      r <- SimulationState.at[M](snapLength, Int.MaxValue)(new EndSnapAction[M](snapLength, snapsRemaining - 1))
    } yield r
  }
}