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
// Scala source file belonging to the org.facsim.sim.engine package.
//======================================================================================================================
package org.facsim.sim.engine

import org.facsim.sim.LibResource

/** Enumeration describing simulation run states.
 *
 *  @param name Name of this run-state.
 *    
 *  @param iteratable Flag indicating whether this state supports event iteration.
 *
 *  @param schedulable Flag indicating whether this state supports event scheduling. If `true`, the simulation's current
 *  run state allows event scheduling (updating of the simulation state to include a newly scheduled event); if `false`,
 *  event iteration is not supported. 
 *  
 *  @since 0.0
 */
enum RunState(val name: String, iteratable: Boolean, schedulable: Boolean):
  
  /** Flag indicating whether this state supports event iteration.
   * 
   *  @return `true`, if the simulation's current run state allows event iteration (updating of the simulation state to
   *  update the current event); `false`, if the event iteration is not supported.
   */
  private[engine] val canIterate: Boolean = iteratable
  
  /** Flag indicating whether this state supports event scheduling.
   * 
   *  @return `true`, if the simulation's current run state allows event scheduling (updating of the simulation state to
   *  include a newly scheduled event); `false`, if the event iteration is not supported.
   */
  private[engine] val canSchedule: Boolean = schedulable
  
  /** State of the simulation prior to being run for the first time.
   *
   *  @since 0.0
   */
  case Initializing
  extends RunState(LibResource("engine.RunState.Initializing"), false, true)

  /** State of the simulation while it is executing.
   *
   *  @since 0.0
   */
  case Executing
  extends RunState(LibResource("engine.RunState.Executing"), true, true)

  /** State indicating that the simulation has terminated due to having exhausted its events.
   *
   *  @since 0.0
   */
  case Terminated
  extends RunState(LibResource("engine.RunState.Terminated"), false, false)

  /** State of the simulation when it has finished execution and cannot be run further.
   *
   *  @since 0.0
   */
  case Completed
  extends RunState(LibResource("engine.RunState.Completed"), false, false)
