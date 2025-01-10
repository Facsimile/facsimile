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

/** Message severity enumeration.
 *
 *  Message severities are ordered by their ordinal values.
 *  
 *  @param name Name of the message severity, localized. This is not the same as the literal message severity value.
 *  
 *  @param abbrName Abbreviated name of the message severity.
 *                  
 *  @since 0.2
 */
enum Severity(val name: String, val abbrName: String)
extends Ordered[Severity]:
  
  /** Rank of the severity of this message severity.
   * 
   *  Lower values have lower severities.
   */
  def severity: Int = ordinal
  
  /** Compare this message severity instance to another instance.
   *
   *  @param that Message severity instance being compared to.
   *
   *  @return A value less than zero if this severity is less than `that` severity; zero if the two severities are
   *  equal; a value greater than zero if this severity is greater than the `that` severity.
   */
  final override def compare(that: Severity): Int = severity.compareTo(that.severity)

  /** Debug log message classification.
   *
   *  Debug log messages, of which there may be many, can be utilized for detailed debug logging.
   *
   *  @since 0.2
   */
  case DebugSeverity
  extends Severity(LibResource("log.DebugSeverityName"), LibResource("log.DebugSeverityAbbrName"))

  /** Informational log message classification.
   *
   *  Informational log messages, typically used to document program state changes and operations.
   *
   *  @since 0.2
   */
  case InformationSeverity
  extends Severity(LibResource("log.InformationSeverityName"), LibResource("log.InformationSeverityAbbrName"))
  
  /** Warning log message classification.
   *
   *  Warning log messages are typically used to document potential problems, unrecommended usage, etc..
   *
   *  @since 0.2
   */
  case WarningSeverity
  extends Severity(LibResource("log.WarningSeverityName"), LibResource("log.WarningSeverityAbbrName"))

  /** Important log message classification.
   *
   *  Important log messages are typically used to document significant program state changes and operations.
   *
   *  @since 0.2
   */
  case ImportantSeverity
  extends Severity(LibResource("log.ImportantSeverityName"), LibResource("log.ImportantSeverityAbbrName"))
  
  /** Error log message classification.
   *
   *  Error log messages are typically used to document errors that are non-fatal and recoverable. Such errors should
   *  not, by themselves, prevent the application from continuing execution.
   *
   *  @since 0.2
   */
  case ErrorSeverity
  extends Severity(LibResource("log.ErrorSeverityName"), LibResource("log.ErrorSeverityAbbrName"))

  /** Fatal error log message classification.
   *
   *  Fatal error log messages are typically used to document errors that are fatal and unrecoverable. They indicate
   *  that the application is about to exit and/or crash.
   *
   *  @since 0.2
   */
  case FatalSeverity
  extends Severity(LibResource("log.FatalSeverityName"), LibResource("log.FatalSeverityAbbrName"))

/** Severity companion object.
 *
 *  @since 0.2
 */
object Severity:
  
  /** Set of all severity object classes.
   *
   *  @return Set of all severity objects.
   */
  private val severities: Set[Severity] = values.toSet

  /** Map of severity localized name to severity.
   *
   *  This is used to look up each severity by localized name.
   */
  private val severityDictionary = severities.map(s => (s.name, s)).toMap

  /** Retrieve a sequence of severities, ordered by rank.
   *
   *  @return List of severities, ordered from lowest rank to highest rank.
   *
   *  @since 0.2
   */
  def severityList: List[String] = severities.toList.sorted.map(_.name)

  /** Lookup a severity by name.
   *
   *  @todo Names are currently locale-specific. It might be better to register all names, in all locales, to each
   *  severity instance.
   *
   *  @param name Name of the severity to be retrieved.
   *
   *  @return Severity matching `name` wrapped-up in a [[Some]], or [[None]] if no match was found.
   *
   *  @since 0.2
   */
  def withName(name: String): Option[Severity] = severityDictionary.get(name)
