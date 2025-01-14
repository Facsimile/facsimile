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
// Scala source file belonging to the org.facsim.util package.
//======================================================================================================================
package org.facsim.util

import scala.util.{Failure, Success, Try}

/** Encapsulate software version information, providing information about each version's components.
 *
 *  @todo This is a work-in-progress and will change in the near future. Support for alpha, beta, milestone and release
 *  candidate, etc. versions will be supported in future. Currently, only release and snapshots are supported.
 *
 *  @constructor Compose a version from its components.
 *
 *  @param major Major version number. This value cannot be negative.
 *
 *  @param minor Minor version number. This value cannot be negative.
 *
 *  @param bugFix Optional bug-fix number. This value, if defined, cannot be negative.
 *
 *  @param isSnapshot If `true`, indicates that this is a pre-release _snapshot_. Note that multiple snapshot versions
 *  may share the same version number, so comparing version numbers is an unreliable method for comparing pre-release
 *  snapshots of the same release.
 *
 *  @throws java.lang.IllegalArgumentException if any argument has an invalid value.
 *
 *  @since 0.0
 */
final case class Version(major: Int = 1, minor: Int = 0, bugFix: Option[Int] = None, isSnapshot: Boolean = false)
extends Ordered[Version]:

  // Sanity checks. Alas, we cannot currently use macros in the compilation unit that they're defined in. :-(
  requireValidFn[Int](major, _ >= 0, "major")
  requireValidFn[Int](minor, _ >= 0, "minor")
  requireValidFn[Option[Int]](bugFix, bf => bf.forall(_ >= 0), "bugFix")

  /** Compare this version to another.
   *
   *  @note Multiple snapshot releases can be issued prior to release&mdash;all having the same version numbers. As a
   *  result, two snapshot releases with the same version numbers may actually differ in their content, even if a
   *  comparison determines that they are equal.
   *
   *  @param that Version to which this version is being compared.
   *
   *  @return < 0 if this is earlier/less than `that` version; 0 if this is the same as/equal to `that` version; > 0 if
   *  this is later/greater than `that` version.
   *
   *  @since 0.0
   */
  override def compare(that: Version): Int =

    // Helper function to compare bug fix numbers.
    def compareBugFix =

      // We can only make a decision based upon the bug fix numbers if they're different.
      if bugFix != that.bugFix then

        // The fold parentheses looks at the case where this bugFix is None. Since the other cannot be None (we already
        // know that they're not equal), it must be defined, so this version is earlier: return -1.
        //
        // The braces looks at the case where this bugFix has a value. If the other is None, return 1 (we're greater
        // than the other value), otherwise, compare their bug fix numbers and return the result (which cannot be 0
        // since the two bug fix numbers are different)
        bugFix.fold(CompareLessThan): thisBf =>
          that.bugFix.fold(CompareGreaterThan)(thatBf => thisBf.compare(thatBf))

      // Otherwise, the two bug fix numbers are the same, so we need to look at the snapshot flag.
      else

        // If the snapshots are the same too, then return the equality value.
        if isSnapshot == that.isSnapshot then CompareEqualTo

        // If this is a snapshot, then this must be less than the other (which cannot be).
        else if isSnapshot then CompareLessThan

        // Otherwise, that must be a snapshot, so we're greater.
        else 1

    // Helper function to compare minor versions.
    def compareMinor = minor.compare(that.minor) match

      // If the minor versions different, return that result.
      case d: Int if d != 0 => d

      // Otherwise, we need to compare bug fix releases. This is more interesting, because no bug fix at all is
      // regarded as being less than any other bug fix (except for None).
      case _ => compareBugFix

    // Helper function to compare major versions.
    def compareMajor = major.compare(that.major) match

      // If the major versions are different, then return that result.
      case d: Int if d != 0 => d

      // Otherwise, we need to compare the minor version. If that's different, return that result.
      case _ => compareMinor

    // Kick the ball off.
    compareMajor

  /** Convert the version to a string.
   *
   *  @return Version represented as a string.
   *
   *  @since 0.0
   */
  override def toString: String =

    // If we have a bugfix version, then it will appear prefixed by a period.
    val bf = bugFix.fold("")(b => s".$b")

    // If this is a snapshot release, then it will end with the snapshot marker.
    val ss = if isSnapshot then Version.Snapshot else ""

    // Build and return the result.
    s"$major.$minor$bf$ss"

/** Version companion object.
 *
 *  @since 0.0
 */
object Version:

  /** Snapshot marker.
   */
  private val Snapshot = "-SNAPSHOT"

  /** Regular expression for parsing version strings.
   */
  private val VersionRegex = s"(\\d+)\\.(\\d+)(\\.\\d+)?($Snapshot)?".r

  /** Regular expression for parsing Java version strings.
   *
   *  Java implementation versions are typically of the form: 1.x.0_y. Since the bug fix position number is always 0,
   *  we'll utilize the number following the underscore as the version number. Snapshots for this version type are
   *  unsupported.
   *
   *  @note This facility was added to support parsing of version numbers in _Java_ manifests. It should be noted that
   *  resulting [[Version]] instances that are converted back to strings will not match the original string passed to
   *  the parsing function.
   */
  private val JavaVersionRegex = """(\d+)\.(\d+)\.0_(\d+)""".r

  /** Convert a string into a version instance.
   *
   *  @param version Version string to be parsed. This must be of the form _`M.m[.b][-SNAPSHOT]`_, where `M` is the
   *  major version number, `m` is the minor version number, and `b` is an optional bug-fix number. If the string ends
   *  with _`-SNAPSHOT`_, then a pre-release _snapshot_ is indicated. Alternatively, _Java_ version numbers of the form
   *  _`M.m.0_b`_ are accepted.  No other version string formats are currently supported.
   *
   *  @return A [[Version]] instance equivalent to the information contained in `version` wrapped in a
   *  [[scala.util.Success]] if successful, or a [[scala.util.Failure]] containing a [[VersionParseException]].
   *
   *  @since 0.0
   */
  // TODO: Use parboiled2 to parse version numbers so that we can report failures better and faster.
  def apply(version: String): Try[Version] =

    // Parse the version string.
    version match

      // Does this match supported versions, indicated by the version regular expression?
      //
      // If so, the bug fix number needs to remove the leading period before converting to an integer.
      case VersionRegex(maj, min, bug, snap) =>
        Success(Version(maj.toInt, min.toInt, Option(bug).map(_.tail.toInt), Option(snap).isDefined))

      // Does this match supported Java versions, indicated by the Java version regular expression?
      //
      // We should only get matches on Java versions of the form 1.8.0_121 (i.e. having a ".0_" prefix to the bug fix
      // number.
      case JavaVersionRegex(maj, min, bug) => Success(Version(maj.toInt, min.toInt, Option(bug.toInt)))

      // If there was no match, then throw the appropriate exception.
      case _ => Failure(VersionParseException(version, 0))