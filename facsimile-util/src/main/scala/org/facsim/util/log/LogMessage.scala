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
// Scala source file belonging to the org.facsim.util.log package.
//======================================================================================================================
package org.facsim.util.log

import org.facsim.util.LibResource

/** A message, categorized by severity, designed for use with data streams.
 *
 *  Messages are suitable for use with execution logs, etc. The prefix, scope and severity can all be utilized for
 *  filtering messag
 *
 *  @tparam A Type for use as the message `prefix`, such as a timestamp. This type must implement a useful "toString"
 *  message, formatted appropriately.
 *
 *  @constructor Create a new message instance.
 *
 *  @param prefix Prefix for the message.
 *
 *  @param msg Message itself, which should contain all necessary information.
 *
 *  @param scope Defined message scope, typically relating to the source of the message.
 *
 *  @param severity Severity of the message, used for relative message importance.
 *
 *  @since 0.2
 */
final case class LogMessage[A](prefix: A, msg: String, scope: Scope, severity: Severity = ErrorSeverity) {

  /** Convert the message to a string.
   *
   * @return Message as a string value.
   *
   * @since 0.2
   */
  override def toString: String = {
    LibResource("log.LogMessageToString", prefix.toString, msg, scope.name, severity.abbrName)
  }
}