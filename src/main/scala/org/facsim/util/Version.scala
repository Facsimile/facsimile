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

import org.facsim.LibResource
import org.joda.time.DateTime

//=============================================================================
/**
Encapsulate software version information, providing information about each
version's components.

=Version Formats=

Version numbering is based upon the model supported by the
[[http://maven.apache.org/ Maven]] software project management tool.
Specifically, supported version numbers are strings of the form:

{{{
M.m[.i][-phase][-r][-SNAPSHOT|-yyyyMMdd-HHmmss-b]
}}}

where:
<table>
  <tr>
    <td>`M`</td>
    <td>Major version number.  This value cannot be negative.  A value of zero
    conventionally indicates that the software has yet to fulfill the basic
    functionality to achieve a ''1.0'' release.</td>
  </tr>
  <tr>
    <td>`m`</td>
    <td>Minor version number.  This value cannot be negative.</td>
  </tr>
  <tr>
    <td>`i`</td>
    <td>Incremental version number.  This value cannot be negative.  This field
    is optional.</td>
  </tr>
  <tr>
    <td>`phase`</td>
    <td>Release phase.  If omitted, this indicates the ''final'' release phase;
    otherwise this may be one of the following: `alpha`, denoting an ''alpha''
    release phase; `beta`, denoting a ''beta'' release phase; or `rc` denoting
    a ''release candidate'' release phase.  No other release phases are
    currently supported.</td>
  </tr>
  <tr>
    <td>`r`</td>
    <td>Revision version number.  This value cannot be negative.  This field is
    optional.</td>
  </tr>
  <tr>
    <td>`SNAPSHOT` or ``yyyyMMdd-HHmmss-s``</td>
    <td>Indicates a development snapshot.  ``SNAPSHOT`` will be present in
    unreleased builds from a project's source distribution, whereas the
    timestamp form is present in snapshot releases.  The timestamp, which is
    specified in [[ UTC]], has the following components:
    <table>
      <tr>
        <td>`yyyy`</td>
        <td>Year of snapshot release.
      </tr>
      <tr>
        <td>`MM`</td>
        <td>Two-digit number representing month of year (01-12).
      </tr>
      <tr>
        <td>`dd`</td>
        <td>Two-digit number representing day of month (01-31).
      </tr>
      <tr>
        <td>`HH`</td>
        <td>Two-digit number representing hour of day (00-23).
      </tr>
      <tr>
        <td>`mm`</td>
        <td>Two-digit number representing minute past hour (00-59).
      </tr>
      <tr>
        <td>`ss`</td>
        <td>Two-digit number representing seconds past minute (00-59).
      </tr>
      <tr>
        <td>`s`</td>
        <td>One or more digits representing the sequential order in which
        timestamped snapshot releases were made (1+).  In practice, this value
        is almost always 1 and is discarded by this class.
      </tr>
    </table></td>
  </tr>
</table>

How these numbers are used depends upon the preferences and procedures of the
associated organization.  For further information, refer to
[[http://en.wikipedia.org/wiki/Software_versioning Software Versioning on
Wikipedia]].

=Version Ordering=

It is often useful to order, or rank, software versions, for example to compare
two versions and determine the most up-to-date of the two.  This class supports
version number ordering, and operates as follows:

 1. If the major version numbers are different, then the version with the
    larger number is more up-to-date.
 1. Otherwise, if the minor version numbers are different, then the version
    with the larger number is more up-to-date.
 1. Otherwise, if the incremental version numbers are different, then the
    version with the larger number is more up-to-date.  (If no incremental
    number is present, it is treated as the value -1 for the purposes of
    comparison.)
 1. Otherwise, if the phases are different, then the versions are ranked by
    whichever phase is most up-to-date: ''final'' (most up-to-date), ''release
    candidate'', ''beta'', ''alpha'' (least up-to-date).
 1. Otherwise, if the revision numbers are different, then the version with the
    larger number is more up-to-date.
 1. Otherwise, if one version is a snapshot and the other is not, then the
    non-snapshot version is more up-to-date.
 1. Otherwise, if both versions are timestamped snapshots then the version
    with the more recent timestamp is more up-to-date.
 1. Otherwise, if both versions are snapshots, but one or the other has no
    timestamp, then it is impossible to compare the two and an exception is
    thrown.
 1. Otherwise, the two versions are the same.


@see [[maven.apache.org Maven]]

@see [[http://en.wikipedia.org/wiki/Software_versioning Wikipedia: Software
Versioning]]

@since 0.0

@constructor Constructs a new Version instance from a version string.

@param version String encoding a software product's version.  See
[[org.facsim.util.Version]] for further information about the format of a
version string.

@throws java.lang.NullPointerException if version is null.

@throws java.lang.IllegalArgumentException if version could not be parsed into
its constituent components.
*/
//=============================================================================

final class Version (version: String) extends Ordered [Version] with Equals
with NotNull {

/**
Version components.

This is a tuple from which numerous version components can be extracted.
*/

  private val components = Version.parseVersion (version)

//-----------------------------------------------------------------------------
/**
Report whether this is a snapshot release.

@return true if this is a snapshot release, or false otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def isSnapshot = // TODO

//-----------------------------------------------------------------------------
/**
Report whether this is a pre-release.

@return true if this is a pre-release or snapshot version, or false if it is
a final release.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def isPreRelease = // TODO

//-----------------------------------------------------------------------------
/*
A version has the potential to equal another object if that other object is
also a Version instance; otherwise, the two are different objects and cannot be
considered equal.

See scala.Equals.canEqual, as well as Chapter 30 of "Programming in Scala, 2nd
Edition" for more information.
*/
//-----------------------------------------------------------------------------

  override def canEqual (other: Any) = other.isInstanceOf [Version]

//-----------------------------------------------------------------------------
/*
If that value can equal this value, and the two objects compare equal, then
they're equal, otherwise they're not.

See java.lang.Object.equals, as well as Chapter 30 in "Programming in Scala,
2nd Edition" for more information.
*/
//-----------------------------------------------------------------------------

  override def equals (other: Any) = other match {
    case that: Version => that.canEqual (this) && compare (that) == 0
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

  override def hashCode () = major.hashCode () ^ minor.hashCode () ^
  revision.hashCode () ^ qualifier.hashCode ()

//-----------------------------------------------------------------------------
/*
Compare this version to another version and return the result of the
comparison.

See scala.math.Ordered.compare for further information.
*/
//-----------------------------------------------------------------------------

  override def compare (that: Version) = {

/*
If the major version numbers differ, then return their comparison.
*/

    if (major != that.major) major.compare (that.major)

/*
Otherwise, if the minor version numbers differ, then return their comparison.
*/

    else if (minor != that.minor) minor.compare (that.minor)

/*
Otherwise, if the revision numbers differ, then return their comparison.
*/

    else if (revision != that.revision) revision.compare (that.revision)

/*
OK.  The decision depends upon the qualifier, so return the qualifier
comparison.
*/

    else qualifier.compare (that.qualifier)
  }

//-----------------------------------------------------------------------------
/*
Convert this version to a string.

see java.lang.Object.toString for further information.
*/
//-----------------------------------------------------------------------------

  override def toString () = {
    if (qualifier.isPreRelease) LibResource.format ("util.Version.PreRelease",
    major, minor, revision, qualifier.toString ())
    else LibResource.format ("util.Version.Final", major, minor, revision)
  }
}

//=============================================================================
/**
Version companion object.

@since 0.0
*/
//=============================================================================

private object Version {

/**
Regular expression for a major.minor version string.
*/

  private val majorMinor = """^(\d+)\.(\d+)$""".r

/**
Regular expression for a major.minor.release version string.
*/

  private val majorMinorRevision = """^(\d+)\.(\d+)\.(\d+)$""".r

/**
Regular expression for a simple major.minor-qualifier version string.
*/

  private val majorMinorQualifier = """^(\d+)\.(\d+)-([\-0-9A-Z_a-z]+)$""".r

/**
Regular expression for a simple major.minor-qualifier version string.
*/

  private val majorMinorRevisionQualifier =
  """^(\d+)\.(\d+)\.(\d+)-([\-0-9A-Z_a-z]+)$""".r

  private def parseString (version: String) = {

/*
Split the version into everything prior to the first hyphen ('-'), and
everything after.
*/

    val (start, remainder) = version.splitAt (vers)
  }
//-----------------------------------------------------------------------------
/**
Conversion function to convert a string into a version.

Strings of the following form are recognized:

@param String to be converted.

@throws java.lang.NumberFormatException
@since 0.0
*/
//-----------------------------------------------------------------------------

  implicit def stringToVersion (version: String) = version match {
    case majorMinor (major, minor) => new Version (major.toInt, minor.toInt)
    case majorMinorRevision (major, minor, revision) => new
    Version (major.toInt, minor.toInt, revision.toInt)
    case majorMinorQualifier (major, minor, qualifier) => new
    Version (major.toInt, minor.toInt, 0, qualifier)
    case majorMinorRevisionQualifier (major, minor, revision, qualifier) => new
    Version (major.toInt, minor.toInt, revision.toInt, qualifier)
    case _ => throw new StringParseException (version)
  }
}