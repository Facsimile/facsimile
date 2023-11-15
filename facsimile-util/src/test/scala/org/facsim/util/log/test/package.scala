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
// Scala source file belonging to the org.facsim.util.test.implicits package.
//======================================================================================================================
package org.facsim.util.log

import org.facsim.util.test.Generator.unicodeString
import org.scalacheck.Gen

/** Elements for assisting with testing of the log package.
 *
 *  @since 0.2
 */
package object test {

  /** Generator for log message severities. */
  // TEMPORARY NOTE:
  //
  // Scalastyle/Scalariform cannot parse lists that terminate with a comma, so avoid doing that for now.
  //
  // #SCALASTYLE_BUG
  val severities = Gen.oneOf(
    DebugSeverity,
    InformationSeverity,
    WarningSeverity,
    ImportantSeverity,
    ErrorSeverity,
    FatalSeverity
  )

  /** Custom scope for generated messages. */
  object LogTestScope
  extends Scope {

    /** @inheritdoc */
    override val name: String = "log stream test"
  }

  /** Generator for synthetic log messages, with string prefixes. */
  val logs = for {
    prefix <- unicodeString
    msg <- unicodeString
    severity <- severities
  } yield LogMessage(prefix, msg, LogTestScope, severity)

  /** Generator for non-empty lists of log messages. */
  val logListNonEmpty = Gen.nonEmptyListOf(logs)
}
