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
// Scala source file belonging to the org.facsim.util.test package.
//======================================================================================================================
package org.facsim.util.test

import akka.actor.ActorSystem
import org.scalatest.BeforeAndAfterAll
import scala.concurrent.ExecutionContext
import org.scalatest.funspec.AnyFunSpec

/** Test harness trait for ''Akka actors''.
 *
 *  Creates an ''Akka'' actor system, executes the indicated test, then destroys the actor system.
 */
trait AkkaTestHarness
extends AnyFunSpec
with BeforeAndAfterAll {

  /** Implicit actor system which can be used for the tests in this harness. */
  protected implicit final val system: ActorSystem = {

    val instanceName = this.getClass.getCanonicalName.replace('.', '_')
    ActorSystem(s"AkkaTestHarness_$instanceName")
  }

  /** Provide an execution context for any futures used. */
  protected implicit final val executionContext: ExecutionContext = system.dispatcher

  /** When all tests have been completed, terminate the actor system. */
  override def afterAll(): Unit = {
    system.terminate()
    super.afterAll()
  }
}