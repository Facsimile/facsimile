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
// Scala source file belonging to the org.facsim.sim.engine package.
//======================================================================================================================
package org.facsim.sim.engine

import org.facsim.sim.LibResource

/** Base trait for all simulation run states.
 *
 *  @since 0.0
 */
sealed trait RunState {

  /** Name of this run-state.
   *
   *  @since 0.0
   */
  val name: String

  /** Flag indicating whether this state supports event iteration.
   *
   *  @return If `true`, the simulation's current run state allows event iteration (updating of the simulation state to
   *  update the current event); if `false`, event iteration is not supported.
   */
  private[engine] val canIterate: Boolean = false

  /** Flag indicating whether this state supports event scheduling.
   *
   *  @return If `true`, the simulation's current run state allows event scheduling (updating of the simulation state to
   *  include a newly scheuled event); if `false`, event iteration is not supported.
   */
  private[engine] val canSchedule: Boolean = false
}

/** State of the simulation prior to being run for the first time.
 *
 *  @since 0.0
 */
case object Initializing
extends RunState {

  /** @inheritdoc */
  override val name: String = LibResource("engine.RunState.Initializing")

  /** @inheritdoc */
  private[engine] override val canSchedule: Boolean = true
}

/** State of the simulation while it is executing.
 *
 *  @since 0.0
 */
case object Executing
extends RunState {

  /** @inheritdoc */
  override val name: String = LibResource("engine.RunState.Executing")

  /** @inheritdoc */
  private[engine] override val canIterate: Boolean = true

  /** @inheritdoc */
  private[engine] override val canSchedule: Boolean = true
}

/** State indicating that the simulation has terminated due to having exhausted its events.
 *
 *  @since 0.0
 */
case object Terminated
extends RunState {

  /** @inheritdoc */
  override val name: String = LibResource("engine.RunState.Terminated")
}

/** State of the simulation when it has finished execution and cannot be run further.
 *
 *  @since 0.0
 */
case object Completed
extends RunState {

  /** @inheritdoc */
  override val name: String = LibResource("engine.RunState.Completed")
}