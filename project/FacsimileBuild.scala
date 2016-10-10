/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for  inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
SBT build configuration that is common to all Facsimile projects.

Project-specific build configuration should be placed in a project root-level SBT file named after the project (such as
Facsimile.sbt, for example).

The word "project" is a little over-used in this file, potentially resulting in some confusion. It can refer to a
library or tool that has a separate git repository in Facsimile's GitHub organization (such that this file defines the
build framework for that project). But it can also refer to an SBT project or sub-project - particularly in
multi-project builds. We'll try to make the context as obvious as possible, but the phrases "Facsimile project", "this
project" and "project-specific" will typically refer to a library/tool, while "root project", "multi-project", "source
project", "sub-project" will typically refer to SBT usage. Hope we've cleared that up... ;-)

NOTE: This file is maintained as part of the Facsimile "skeleton" project, and is common to a number of other Facsimile
projects, termed client projects. It must only be modified in the "skeleton" project, with changes being merged into
client projects. Refer to the skeleton project for further details:

  https://github.com/Facsimile/skeleton
*/
//======================================================================================================================

import java.time.ZonedDateTime
import java.util.jar.Attributes.Name
import sbt._
import sbt.Keys._
import sbtrelease.ReleasePlugin.autoImport._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Version
import sbtunidoc.Plugin.{ScalaUnidoc, UnidocKeys, unidocSettings}
import scoverage.ScoverageKeys
import xerial.sbt.Sonatype.sonatypeSettings

/*
Wrap all contents in a FacsimileSettings object for inclusion in client projects.
*/

object FacsimileBuild {

/*
ID of this Facsimile project.

This is the "normalized name" of this project, also known as a Maven/SBT/Ivy artifact name (such as "myfabapp"). This
must also match the name of the project on GitHub.

This setting is defined to assist with automatic project configuration; it must be explicitly provided by each
project-specific build file.
*/

  lazy val facsimileProjId = settingKey [String] ("Artifact ID/normalized name of this Facsimile project.")

/*
Name of this Facsimile project.

Human-readable phrase that describes (in a shortened form) this project (such as "My Fabulous App").

This is equivalent to the "name" Maven POM information field, and is employed as such during deployment to the Sonatype
OSS Nexus repository. It is also used by SBT for naming each project and sub-project.

This setting is defined to assist with automatic project configuration; it must be explicitly provided by each
project-specific build file.
*/

  lazy val facsimileProjName = settingKey [String] ("Short description of this Facsimile project.")

/*
Description of this Facsimile project.

This is a detailed description for this project.

This setting is defined to assist with automatic project configuration; it must be explicitly provided by each
project-specific build file.
*/

  lazy val facsimileProjDesc = settingKey [String] ("Description of this Facsimile project.")

/*
Home page for this Facsimile project.

This setting is defined to assist with automatic project configuration; it must be explicitly provided by each
project-specific build file.
*/

  lazy val facsimileProjHomePage = settingKey [String] ("Homepage of this Facsimile project.")

/*
Date the facsimile project was started.

This is the date that the Facsimile project was announced on the Facsimile web-site (which was actually a few days after
the project was registered on Sourceforge).
*/

  val facsimileProjStartDate = ZonedDateTime.parse ("2004-06-22T18:16:00-04:00[America/New_York]")

/*
Date this build was made.

Ideally, this ought to be the date of the current commit, but the current time is probably OK for custom builds too.
*/

  val facsimileProjBuildDate = ZonedDateTime.now ()

/*
Regular expression for matching release versions.
*/

  private val ReleaseVersion = """(\d+)\.(\d+)\.(\d+)""".r

/*
Regular expression for matching snapshot versions.
*/

  private val SnapshotVersion = """(\d+)\.(\d+)\.(\d+)-SNAPSHOT""".r

/*
Function to determine the copyright range.

@return If the year of the start date differs from the year of the build, then the copyright message will have a year
range (e.g. 2004-2016); otherwise, since both years are the same, it will be a single year value (e.g. 2016). The return
value is a string with the year range or value as appropriate.
*/

  private def copyrightRange = {
    val startYear = facsimileProjStartDate.getYear.toString
    val currentYear = facsimileProjBuildDate.getYear.toString
    if (startYear == currentYear) startYear
    else startYear + "-" + currentYear
  }

/*
Retrieve base version number.

@return A base version number made up of just the major and minor version numbers, without a release/revision/build
number or a SNAPSHOT tail.
*/

  private def baseVersion (ver: String): String = ver match {
    case ReleaseVersion (maj, min, rel) => maj + "." + min
    case SnapshotVersion (maj, min, rel) => maj + "." + min
    case _ => "Invalid (\"" + ver + "\")"
  }

/*
Common project settings.

These settings are common to all SBT root- and sub-projects.

Note that we implement release versioning for artifacts through the Release plugin. The current version is stored in the
project-specific Version.sbt file.
*/

  lazy val commonSettings = Seq (

/*
Owning organization.

This is the Maven/SBT/Ivy group ID and should match the root package name of the Scala sources. It should also be the
reverse of the web-site name (less the "www" prefix). Thus "http://facsim.org/" yields an organization ID/root package
name of "org.facsim"
*/

    organization := "org.facsim",

/*
Scala cross compiling.

IMPORTANT: These values should be synchronized with the Travis CI .travis.yml file in the project's root directory.
*/

    crossScalaVersions := Seq ("2.11.8"),

/*
Scala default version.
*/

    scalaVersion <<= crossScalaVersions {
      versions => versions.head
    }
  )

/*
Base settings for all SBT source projects.

These settings are common to all SBT root- and sub-projects that contain Scala sources. Dependencies listed here should
be universal.
*/

  lazy val baseSourceSettings = Seq (

/*
Scala compiler options.

This is a conundrum: -Xfatal-warnings is essential, since it forces all warnings to be addressed. However, when
-optimize is specified, Scala will generate some inline warnings when initializing large maps. Although the inline
warnings themselves will only be issued when -Yinline-warnings is specified, Scala will still emit a warning that inline
warnings occurred, which is then treated as fatal by Xfatal-warnings. It seems that the only way around this, right now,
is to disable -Xfatal-warnings. This is a known Scala compiler issue documented at:

  https://issues.scala-lang.org/browse/SI-6723.

As Xfatal-warnings is not in use, it's possible to have builds that generate tons of warnings, but which do not fail a
build. This is unacceptable. Facsimile must build clean, without any errors or warnings, as a basic requirement for any
release to be performned.

-Xstrict-inference is currently disabled as it outputs some erroneous warnings for some generic code. See
https://issues.scala-lang.org/browse/SI-7991 for further details.

Also note that the Akka team recommend that "-optimize" is not used for code used with Akka actors.

Most of these issues should go away when Scala 2.12, which sports a new optimizer, is released.
*/

    scalacOptions := Seq (
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-g:vars",
      "-optimize",
      "-target:jvm-1.8",
      "-unchecked",
      "-Xcheckinit",
      //"-Xfatal-warnings",
      "-Xlint:_",
      "-Yclosure-elim",
      "-Yconst-opt",
      "-Ydead-code",
      "-Yinline",
      "-Yinline-handlers",
      "-Yinline-warnings"
    ),

/*
Fork all tests, so that they run in a separate process.
*/

    fork in Test := true,

/*
Make sure that tests execute in sequence (we may change this in future, but, for now, it's a lot easier to understand
test output if tests execute sequentially.
*/

    parallelExecution in Test := false,

/*
Code test coverage settings. Target coverage is 100%.
*/

    ScoverageKeys.coverageEnabled := true,
    ScoverageKeys.coverageHighlighting := true,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageMinimum := 50,

/*
Required scala standard libraries.

As stated above, these must be universal for all projects. In particular, indirect dependences (dependencies that are
required by Facsimile project dependencies) should not be explicitly included, as this can lead to versioning problems
(such as depending upon two or more different versions of the same library dependency).

Right now, the only universal dependencies are libraries required by the test phase.
*/

    libraryDependencies ++= Seq (

/*
ScalaTest dependency.
*/

      "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    )
  )

/*
SBT root project Unidoc settings.

This ensures that the "doc" command executes "unidoc", and changes the output directory to "/api" from "/unidoc" so that
the generated documentation is included in packaging & publishing.

These settings should be added to SBT root projects that contain sub-projects.
*/

  lazy val rootProjectUnidocSettings = unidocSettings ++ Seq (
    doc in Compile := (doc in ScalaUnidoc).value,
    target in UnidocKeys.unidoc in ScalaUnidoc := crossTarget.value / "api"
  )

/*
Settings for all SBT sub-projects.
*/

  lazy val subProjectSourceSettings = Seq (

/*
Ensure that we only publish/package the root project (which should package up relevant content from its sub-projects).
*/

    publishArtifact := false
  )

/*
SBT root project settings.

The SBT root project defines the artifacts to be published, and also takes of publishing them. Conseqeuently, there is a
lot of Maven/SBT/Ivy configuration information here. It also creates documentation for any sub-projects by employing
Unidoc.

Note that the Sonatype plugin's settings are used to ensure that the resulting artifacts can be published to the
Sonatype OSS repository, which synchronizes with the Maven Central Repository.
*/

  lazy val rootSettings = sonatypeSettings ++ Seq (

/*
Maven POM (project object model) metadata.

This information is required in order to deploy the project on the Sonatype OSS Nexus repository, which ensures that the
project is synchronized with the Maven Central Repository.

NOTES:
1.  Test artifacts should NOT be published. This is disabled by the line "publishArtifact in Test := false" below.
2.  Third-party artifacts referenced by Facsimile that are not available in the Maven Central Repository should not be
    listed in a project's POM file. If such artifacts are required, then uncomment the "pomIncludeRepository" setting so
    that the repositories from which they're referenced are ignored.
3.  Maven metadata that is not defined by SBT properties must be defined in the "pomExtra" setting as XML.
4.  Artifacts must be signed via GPG to be published to the Sonatype OSS Nexus repository. For security reasons (to
    prevent signing by unauthorized publishers), this must be configured locally on each release manager's machine. In
    this case, the software must be signed using the key for "software@facsim.org" - with public key "797D614C". (If
    your version of Facsimile is signed by a different key, then you do not have the official version.)

We publish to the snapshots repository, if this is a snapshot, or to the releases staging repository if this is an
official release.

TODO: We need to have Facsimile project-specific Issue sites. For now, we just use the main Facsimile project for all
issues.
*/

    normalizedName := facsimileProjId.value,
    name := facsimileProjName.value,
    description := facsimileProjDesc.value,
    homepage := Some (url (facsimileProjHomePage.value)),
    startYear := Some (facsimileProjStartDate.getYear),
    organizationName := "Michael J. Allen",
    organizationHomepage := Some (url ("http://facsim.org/")),
    licenses := Seq (
      "GNU Lesser General Public License version 3 (LGPLv3)" ->
      url ("http://www.gnu.org/licenses/lgpl-3.0-standalone.html")
    ),
    scmInfo := Some (
      ScmInfo (
        url ("https://github.com/Facsimile/" + facsimileProjId.value),
        "scm:git:https://github.com/Facsimile/" + facsimileProjId.value + ".git",
        Some ("scm:git:https://github.com/Facsimile/" + facsimileProjId.value + ".git")
      )
    ),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := {
      _ => false
    },
    pomExtra :=
    <developers>
      <developer>
        <id>mja</id>
        <name>Michael J Allen</name>
        <email>mike.allen@facsim.org</email>
        <url>https://hindsight-consulting.com/Blog/MikeAllen</url>
        <organization>Hindsight Consulting, Inc.</organization>
        <organizationUrl>http://hindsight-consulting.com/</organizationUrl>
        <roles>
          <role>Project Lead</role>
          <role>Architect</role>
          <role>Developer</role>
        </roles>
        <timezone>US/Eastern</timezone>
      </developer>
    </developers>
    <contributors>
    </contributors>
    <prerequisites>
      <maven>3.0</maven>
    </prerequisites>
    <issueManagement>
      <system>GitHub Issues</system>
      <url>https://github.com/Facsimile/facsimile/issues</url>
    </issueManagement>,

/*
Disable aggregation of the "doc" command, so that we do not attempt to generate documentation for sub-projects
individually - we just rely on the Unidoc plugin to take care of that for us.
*/

    aggregate in doc := false,

/*
Disable aggregation of the "release" command, so that we do not attempt to release sub-projects individually.
*/

    //aggregate in releaseSignedConfiguration := false,

/*
Scaladoc configuration.

The -Ymacro-no-expand prevents macro definitions from being expanded in macro sub-classes.
*/

    scalacOptions in (ScalaUnidoc, UnidocKeys.unidoc) := Seq (
      "-diagrams",
      "-doc-footer",
      "Copyright © " + copyrightRange + ", " + organizationName.value + ". All rights reserved.",
      "-doc-format:html",
      "-doc-title",
      facsimileProjName.value + " API Documentation",
      "-doc-version",
      version.value,
      "-groups",
      "-implicits",
      "-no-link-warnings", // <- Temporarily disable warnings of bad links
      "-Xfatal-warnings",
      "-Ymacro-expand:none"
    ),
    autoAPIMappings := true,
    apiMappings += (
      unmanagedBase.value / "jt.jar" -> url ("http://docs.oracle.com/javase/8/docs/api/")
    ),

/*
Tell any dependent projects where to find published Facsimile API documentation to link to (via autoAPIMappings). This
link will be published in the project's Maven POM file.

Note: This documentation is versioned so that links will always be to the version of Facsimile in use by the dependent
project.
*/

    apiURL := Some (url (facsimileProjHomePage.value + "/Documentation/API/" + version.value)),

/*
Manifest additions for the main library jar file.

The jar file should be sealed so that the packages contained cannot be extended. We also add inception & build
timestamps for information purposes.
*/

    packageOptions in (Compile, packageBin) ++= Seq (
      Package.ManifestAttributes (Name.SEALED -> "true"),
      Package.ManifestAttributes ("Inception-Timestamp" -> facsimileProjStartDate.toString),
      Package.ManifestAttributes ("Build-Timestamp" -> facsimileProjBuildDate.toString)
    ),

/*
Employ the following custom release process.

This differs from the standard sbt-release process in that:
a) We must perform static source checking (using Scalastyle) before setting the release version.
b) We employ the sbt-sonatype plugin to publish the project, which also takes care of signing published artifacts.
*/

    releaseProcess := Seq [ReleaseStep] (

/*
Firstly, verify that none of this project's dependencies are SNAPSHOT releases.
*/

      checkSnapshotDependencies,

/*
Prompt for the version to be released and for the next development version.
*/

      inquireVersions,

/*
Clean all build files, to ensure the release is built from scratch.
*/

      runClean,

/*
Run the test suite, to verify that all tests pass.
*/

      runTest,

/*
Run scalastyle to ensure that sources are correctly formatted and contain no static errors.
*/

      ReleaseStep (action = Command.process ("testScalastyle", _)),

/*
Update the "Version.sbt" file so that it contains the release version number.
*/

      setReleaseVersion,

/*
Commit and tag the release version.
*/

      commitReleaseVersion,
      tagRelease,

/*
Sign the current version.
*/

      ReleaseStep (action = Command.process ("publishSigned", _)),

/*
Update the "Version.sbt" file so that it contains the new development version number.
*/

      setNextVersion,

/*
Commit the updated working directory, so that the new development version takes effect.
*/

      commitNextVersion,

/*
Publish the released version to the Sonatype OSS repository.
*/

      ReleaseStep (action = Command.process ("sonatypeReleaseAll", _)),

/*
Push all commits to the upstream respository (typically "origin").
*/

      pushChanges
    ),

/*
By default, we'll bump the bug-fix/release number of the version following a release.
*/

    releaseVersionBump := Version.Bump.Bugfix,

/*
Have the release plugin write current version information into Version.sbt, in the project's root directory.

NOTE: The Version.sbt file MUST NOT be manually edited and must be maintained under version control.
*/

    releaseVersionFile := file ("Version.sbt")
  )
}
