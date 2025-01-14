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
// Scala source file belonging to the org.facsim.sim.model package.
//======================================================================================================================
package org.facsim.sim.model

import izumi.reflect.Tag
import org.facsim.sim.{LibResource, SimulationAction}

/** Anonymous action for wrapping bare actions as [[org.facsim.sim.model.Action]] instances.
 *
 *  @tparam M Final type of the simulation's model state.
 *
 *  @constructor Create a new anonymous action.
 *
 *  @param actions Simulation state transitions (a.k.a. _actions_) to be executed at some point in the future.
 *
 *  @since 0.0
 */
final class AnonymousAction[M <: ModelState[M]: Tag] private[sim](override protected val actions: SimulationAction[M])
extends Action[M]:

  /** @inheritdoc
   */
  override val name: String = LibResource("model.AnonymousActionName")

  /** @inheritdoc
   */
  override val description: String = LibResource("model.AnonymousActionDesc")