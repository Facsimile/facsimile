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

import akka.stream.ActorMaterializer
import org.facsim.util.stream.DataSource
import org.scalacheck.Gen

/** Test harness trait for ''Akka streams''.
 *
 *  Creates an ''Akka'' actor system, and a stream materializer, executes the indicated test, then destroys the actor
 *  system.
 */
trait AkkaStreamsTestHarness
extends AkkaTestHarness {

  /** Generator for invalid buffer size numbers. */
  // TEMPORARY NOTE:
  //
  // Scalastyle/Scalariform cannot parse lists that terminate with a comma, so avoid doing that for now.
  //
  // #SCALASTYLE_BUG
  protected final val invalidBufferSizes = Gen.oneOf(
    Generator.nonPosInt,
    Gen.choose(DataSource.MaxBufferSize + 1, Int.MaxValue)
  )

  /** Generator for valid buffer size numbers. */
  protected final val validBufferSizes = Gen.choose(1, DataSource.MaxBufferSize)

  /** Implicit actor materializer for default stream materialization. */
  protected implicit final val materializer: ActorMaterializer = ActorMaterializer()
}