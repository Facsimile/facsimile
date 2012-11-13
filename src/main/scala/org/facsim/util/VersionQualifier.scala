/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.util package.
*/
//=============================================================================

package org.facsim.util

//=============================================================================
/**
Qualifies the characteristics of a [[org.facsim.util.Version]].

Release phases determine the status of a release, with releases progressing
from one phase to the next without returning to a prior phase.  The Facsimile
library supports the following release phases, in increasing order of
stability:

 1. ''Alpha''.  Alpha releases are experimental, with one or more new features
    added.  They are unstable: newly added features may evolve or be removed
    and additional new features may be added.  They are completely unsuitable
    for production use and should be avoided by users, with their primary
    purpose being for evaluation by the developers.  Zero or more alpha
    releases may be made, differentiated by a serial revision number, prior to
    the release switching to the next phase.  This release phase is denoted by
    an `-alpha[-revision]` suffix to the version string, preceding the
    snapshot suffix (if any), where `revision` represents the alpha revision
    number.  If the revision number is omitted, it is assumed to be 0,
    representing the initial release.
 1. ''Beta''.  Beta releases provide previews of forthcoming new features.
    They are typically unstable, and the new functionality may change, or even
    be removed, prior to the final release, but no new features will be added
    once beta has been achieved.  They should not be used for production use,
    but users are encouraged to try the new features, report defects and
    comment upon their design and usability.  Zero or more beta releases may be
    made, differentiated by a serial revision number, prior to the release
    switching to the next phase.  This release phase is denoted by a
    `-beta[-revision]` suffix to the version string, preceding the snapshot
    suffix (if any), where `revision` represents the beta revision number.  If
    the revision number is omitted, it is assumed to be 0, representing the
    initial release.
 1. ''Release Candidate''.  Release candidates are releases that are believed
    stable enough to become final releases, but that do not yet have a stamp of
    approval.  Functionality should not change in the interval between a
    release candidate being made and any subsequent final release, with only
    bug-fixes being applied.  They are not recommended for production use, but
    users are encouraged to kick the tires by testing the release and reporting
    any defects found.  Zero or more release candidates may be made,
    differentiated by a serial revision number, prior to the release switching
    to the next phase.  This release phase is denoted by an `-rc[-revision]`
    suffix to the version string, preceding the snapshot suffix (if any), where
    `revision` represents the release candidate revision number.  If the
    revision number is omitted, it is assumed to be 0, representing the initial
    release.
 1. ''Final''.  Final releases should be stable.  They are recommended for
    production use and their quality can be relied upon.  New functionality
    will not be added to a final release, although subsequent bug-fix releases
    will be made over the period that the release is supported.  One or more
    final releases may be made, differentiated by a serial revision number,
    prior to the release switching to the next phase.  This release phase is
    denoted by a `[-revision]` suffix to the version string, preceding the
    snapshot suffix (if any), where `revision` represents the final release
    revision number.  If the revision number is omitted, it is assumed to be 0,
    representing the initial release.

Orthogonal to the release phase is the ''snapshot'' property.  Snapshots have
no quality guarantees associated with them and reflect development progress
towards the next release, regardless of its phase.  Snapshot releases are
denoted by a `-SNAPSHOT` or `-yyyyMMdd-hhmmss-n` suffix, (denoting the
timestamp of the snapshot release) which follows the release phase suffix.
*/
//=============================================================================

final class VersionQualifier (qualifier: String) extends Ordered
[VersionQualifier] with Equals with NotNull {


  private val (phase, revision, isSnaphot, snapshotTimestamp) =
  parseString (qualifier)

//-----------------------------------------------------------------------------
/*
A version qualifier has the potential to equal another object if that other
object is also a VersionQualifier instance; otherwise, the two are different
objects and cannot be considered equal.

See scala.Equals.canEqual for further information.
*/
//-----------------------------------------------------------------------------

  override def canEqual (other: Any) = other.isInstanceOf [VersionQualifier]

//-----------------------------------------------------------------------------
/*
If that value can equal this value, and the two objects compare equal, then
they're equal, otherwise they're not.

See Chapter 30 in "Programming in Scala, 2nd Edition" for a more detailed
analysis.
*/
//-----------------------------------------------------------------------------

  override def equals (other: Any) = other match {
    case that: VersionQualifier => that.canEqual (this) && compare (that) == 0
    case _ => false
  }

//-----------------------------------------------------------------------------
/*
When overriding "equals", must also override hashCode - since hash codes must
be equal if equal returns true, and since hash codes are used in many
collections to identify equal objects.

The simplest way to achieve a new hash code is to exclusive-or the hash codes
of each value member.

See Chapter 30 in "Programming in Scala, 2nd Edition" for a more detailed
analysis.
*/
//-----------------------------------------------------------------------------

  override def hashCode () = // TODO

//-----------------------------------------------------------------------------
/*
Compare this version to another version and return the result of the
comparison.

See scala.math.Ordered.compare for further information.
*/
//-----------------------------------------------------------------------------

  override def compare (that: Version) = // TODO

//-----------------------------------------------------------------------------
/*
Convert this version to a string.

see java.lang.Object.toString for further information.
*/
//-----------------------------------------------------------------------------

  override def toString () = // TODO

  def isPreRelease: Boolean = true
}

//=============================================================================
//=============================================================================

private object VersionQualifier {

/**
Time-stamp regular expression string, employed by time-stamped snapshots.

The format of a snaphot timestamp, in UTC, is:

{{{
yyyyMMdd-HHmmss-r
}}}

where `yyyy` is a 4-digit year, `MM` is a two digit month number (01-12), `dd`
is a two-digit day-of-the-month number (01-31), `HH` is the hour (00-23), `mm`
is the minute past the hour (00-59), and `ss` is the number of seconds past the
minute (00-59).  `r` is a positive integer identifying the number of the
snapshot with the same timestamp; in practice, `r` is typically always 1 and is
discarded here.
*/

  private val timestamp = """(\d{8}-\d{6})-\d+"""

/**
Regular expression representing an alpha release.  No snapshot, not revision
number.
*/

  private val alphaRegex = ("^-" + Phase.Alpha.toString + "$").r

/**
Regular expression representing an alpha release with a revision number.
*/

  private val alphaRevRegex = ("^-" + Phase.Alpha.toString + "-(\d+)$").r

/**
Regular expression representing an snapshot alpha release with a revision
number, but no release timestamp.
*/

  private val alphaRevRegexSnapshot = ("^-" + Phase.Alpha.toString +
  """-(\d+)-SNAPSHOT$""").r

//-----------------------------------------------------------------------------
/**
Enumeration representing release phase.

Phases should be listed in order of increasing stability, for comparison
purposes.
*/
//-----------------------------------------------------------------------------

  private object Phase extends Enumeration {

/**
Alpha phase.
*/

    val Alpha = Value ("alpha")

/**
Beta phase.
*/

    val Beta = Value ("beta")

/**
Release candidate phase.
*/

    val ReleaseCandidate = Value ("rc")

/**
Final release phase.
*/

    val Final = Value ("")
  }

//-----------------------------------------------------------------------------
//-----------------------------------------------------------------------------

  private def parseString (qualifier: String) = {
  }
}
