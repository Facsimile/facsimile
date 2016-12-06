// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2016, Michael J Allen.
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
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not,
// see http://www.gnu.org/licenses/lgpl.
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
// SBT build configuration specific for all Facsimile projects.
//======================================================================================================================

import java.time.ZonedDateTime
import java.util.jar.Attributes.Name
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Version
import scoverage.ScoverageKeys
import xerial.sbt.Sonatype.sonatypeSettings

// Disable certain Scalastyle options, which IntelliJ IDEA insists on applying to SBT sources.
//scalastyle:off scaladoc

// Date the facsimile project was started.
//
// This is the date that the Facsimile project was announced on the Facsimile web-site (which was actually a few days
// after the project was first registered on Sourceforge, but it's the best date we have).
val facsimileStartDate = ZonedDateTime.parse("2004-06-22T18:16:00-04:00[America/New_York]")

// Date this build was performed.
//
// Ideally, this ought to be the date of the current commit, but that's not so easy to determine. This should be OK,
// particularly for custom builds.
val facsimileBuildDate = ZonedDateTime.now()

// Function to determine the copyright range.
//
// If the year of the start date differs from the year of the build, then the copyright message will have a year range
// (e.g. 2004-2016); otherwise, since both years are the same, it will be a single year value (e.g. 2016). The return
// value is a string with the year range or value as appropriate.
val copyrightRange = {
  val startYear = facsimileStartDate.getYear.toString
  val currentYear = facsimileBuildDate.getYear.toString
  if(startYear == currentYear) startYear
  else s"$startYear-$currentYear"
}

// Common project settings.
//
// These settings are common to all SBT root- and sub-projects.
//
// Note that we implement release versioning for artifacts through the Release plugin. The current version is stored in
// the project-specific Version.sbt file.
lazy val commonSettings = Seq(

  // Owning organization.
  //
  // This is the Maven/SBT/Ivy group ID and should match the root package name of the Scala sources. It should also be
  // the reverse of the web-site name (less any "www" prefix). Thus "http://facsim.org/" yields an organization ID/root
  // package name of "org.facsim"
  organization := "org.facsim",

  // Human-readable name of the owning organization.
  organizationName := "Michael J Allen",

  // Web-site of the owning organization.
  organizationHomepage := Some(url("http://facsim.org/")),

  // Scala cross compiling.
  //
  // Right now, the complexities of supporting multiple Scala versions has been deferred in favor of supporting a single
  // version that meets the project's requirements. This decision forces all users to use the same Scala release that
  // was used to build Facsimile and so will be reviewed in future.
  //
  // IMPORTANT: These values MUST be synchronized with the Travis CI .travis.yml file in the project's root directory,
  // or Travis CI Facsimile builds may yield unexpected results.
  crossScalaVersions := Seq("2.12.0"),

  // Scala default version.
  //
  // NOTE: While it might appear that these Scala version options should be placed in "sourceProjectSettings", SBT will
  // use the Scala version to decorate the project's artifact/normalized name. Hence, even if a project does not contain
  // any sources, it it still necessary to provide the version of Scala that is in use.
  scalaVersion := crossScalaVersions.value.head
)

// Published project settings.
//
// Published projects must define the artifacts to be published, and take of publishing them to the Sonatype OSS
// repository. Conseqeuently, there is a lot of Maven/SBT/Ivy configuration information here.
//
// Note that the Sonatype plugin's settings are used to ensure that the resulting artifacts can be published to the
// Sonatype OSS repository, which automatically pushes project information to the Maven Central Repository.
//
// NOTES:
// 1.  Test artifacts should NOT be published. This is disabled by the line "publishArtifact in Test := false" below.
// 2.  Third-party artifacts referenced by Facsimile must be available in the Maven Central Repository.
// 3.  Maven metadata that is not defined by SBT properties must be defined in the "pomExtra" setting as XML.
// 4.  Artifacts must be signed via GPG to be published to the Sonatype OSS Nexus repository. For security reasons
//     (to prevent signing by unauthorized publishers), this must be configured locally on each release manager's
//     machine. In this case, the software must be signed using the key for "authentication@facsim.org" - with public
//     key "0xC08B4D86EACCE720". If your version of Facsimile is signed by a different key, then you do not have the
//     official version.)
//
// We publish to the snapshots repository, if this is a snapshot, or to the releases staging repository if this is
// an official release.
lazy val publishedProjectSettings = sonatypeSettings ++ Seq(

  // Start year of this project.
  startYear := Some(facsimileStartDate.getYear),

  // All Facsimile libraries are published under the LGPL as specified below.
  licenses := Seq(
    "GNU Lesser General Public License version 3 (LGPLv3)" ->
    url("http://www.gnu.org/licenses/lgpl-3.0-standalone.html")
  ),

  // Facsimile utilitizes git for version control, hosted by GitHub.
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/Facsimile/facsimile"),
      "scm:git:https://github.com/Facsimile/facsimile.git",
      Some("scm:git:https://github.com/Facsimile/facsimile.git")
    )
  ),

  // Test artifacts should not be published.
  publishArtifact in Test := false,

  // Maven POM information which is not available elsewhere through SBT settings.
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

  // Tell any dependent projects where to find published Facsimile API documentation to link to (via autoAPIMappings).
  // This link will be published in the project's Maven POM file.
  //
  // Note: This documentation is versioned so that links will always be to the version of Facsimile in use by the
  // dependent project.
  apiURL := organizationHomepage.value.map(h => url(h.toString + "/Documentation/API/${version.value}")),

  // Manifest additions for the main library jar file.
  //
  // The jar file should be sealed so that the packages contained cannot be extended. We also add inception & build
  // timestamps for information purposes.
  packageOptions in (Compile, packageBin) ++= Seq(
    Package.ManifestAttributes(Name.SEALED -> "true"),
    Package.ManifestAttributes("Inception-Timestamp" -> facsimileStartDate.toString),
    Package.ManifestAttributes("Build-Timestamp" -> facsimileBuildDate.toString)
  ),

  // SBT-GPG plugin configuration.
  //
  // For best results, all releases and code release signing should be undertaken on a Linux system via GNU GPG.
  PgpKeys.useGpg in Global := true,

  // Identify the key to be used to sign release files.
  //
  // Facsimile software is published to the Sonatype OSS repository, with artifacts signed as part of the release
  // process. (Releases are performed using the SBT "release" command; snapshots are published via the "publish-signed"
  // command, with a version that ends with the string "SNAPSHOT", and which does not utilize this procedure.)
  //
  // To obtain the hexadecimal key ID, enter the command:
  //
  //   gpg --keyid-format 0xLONG --list-secret-keys authentication@facsim.org
  //
  // Look for the key ID in the line beginning with "sec".
  //
  // Note that, for security, the private signing key and passcode are not publicly available.
  PgpKeys.pgpSigningKey in Global := Some(0xC08B4D86EACCE720L),

  // Sign releases prior to publication.
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,

  // Employ the following custom release process.
  //
  // This differs from the standard sbt-release process in that:
  // a) We must perform static source checking (using Scalastyle) before setting the release version.
  // b) We employ the sbt-sonatype plugin to publish the project, which also takes care of signing published artifacts.
  releaseProcess := Seq[ReleaseStep](

    // Firstly, verify that none of this project's dependencies are SNAPSHOT releases.
    checkSnapshotDependencies,

    // Prompt for the version to be released and for the next development version.
    inquireVersions,

    // Clean all build files, to ensure the release is built from scratch.
    //
    // NOTE: This does not appear to work too well, so running "sbt clean" before running "sbt release" is highly
    // recommended.
    runClean,

    // Run the test suite, to verify that all tests pass. (This will also compile all code.)
    runTest,

    // Run scalastyle to ensure that sources are correctly formatted and contain no static errors.
    ReleaseStep(action = Command.process("scalastyle", _)),

    // Update the "Version.sbt" file so that it contains the release version number.
    setReleaseVersion,

    // Commit and tag the release version.
    commitReleaseVersion,
    tagRelease,

    // Publish associated artifacts (requires "publishTo" and "releasePublishArtifactsAction" settings to be configured
    // appropriately).
    publishArtifacts,

    // Update the "Version.sbt" file so that it contains the new development version number.
    setNextVersion,

    // Commit the updated working directory, so that the new development version takes effect.
    commitNextVersion,

    // Publish the released version to the Sonatype OSS repository.
    //
    // The "publish-artifacts" step above takes care of publishing the artifacts to Sonatype, so the following command
    // initiates the process of having Sonatype release them, which may fail if Sonatype determines that the artifacts
    // do not meet their current specifications.
    ReleaseStep(action = Command.process("sonatypeReleaseAll", _)),

    // Push all commits to the upstream respository.
    //
    // This is typically determined by including the "-u" argument to a git push command. The upstream repository for a
    // release MUST be the primary Facsimile repository, not a fork.
    pushChanges
  ),

  // By default, we'll bump the bug-fix/release number of the version following a release.
  releaseVersionBump := Version.Bump.Bugfix,

  // Have the release plugin write current version information into Version.sbt, in the project's root directory.
  //
  // NOTE: The Version.sbt file MUST NOT be manually edited and must be maintained under version control.
  releaseVersionFile := file("Version.sbt")
)

// Source project settings.
//
// These settings are common to all projects that contain source files, which must be compiled and tested.
//
// Any library dependencies listed here MUST be universal and not nontransitive.
lazy val sourceProjectSettings = Seq(

  // Scala compiler options.
  //
  // -Xfatal-warnings is disabled because some deprecated code features are still in use, resulting in warnings that
  // cannot currently be suppressed. (The Scala team have been deprecating a lot of features as of 2.11, but there are
  // no alternatives to many of the deprecated classes, which is becoming a nuisance.)
  //
  // As Xfatal-warnings is not in use, it's possible to have builds that generate tons of warnings, but which do not
  // fail a build. This is unacceptable: projects must build clean, without any errors or warnings, as a basic
  // requirement for any release to be performned.
  //
  // -Xstrict-inference is currently disabled as it outputs erroneous warnings for some generic code. See
  // https://issues.scala-lang.org/browse/SI-7991 for further details.
  scalacOptions in Compile := Seq(

    // Code compilation options.
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-g:vars",
    "-opt:l:method",
    "-opt-warnings:_",
    "-target:jvm-1.8",
    "-unchecked",
    "-Xcheckinit",
    //"-Xfatal-warnings",
    "-Xlint:_",
    //"-Xstrict-inference",
    "-Ypartial-unification",
    "-Ywarn-adapted-args",
    "-Ywarn-dead-code",
    "-Ywarn-inaccessible",
    "-Ywarn-infer-any",
    "-Ywarn-nullary-override",
    "-Ywarn-nullary-unit",
    "-Ywarn-numeric-widen",
    "-Ywarn-unused",
    "-Ywarn-unused-import",
    "-Ywarn-value-discard"
  ),

  // Fork all tests, so that they run in a separate process.
  fork in Test := true,

  // Make sure that tests execute in sequence (we may change this in future, but, for now, it's a lot easier to
  // understand test output if tests execute sequentially.
  parallelExecution in Test := false,

  // Code test coverage settings.
  //
  // Target coverage is 100%. Over time, the test coverage required for a successful build will be raised to this value
  // to ensure code is as fully tested as possible.
  ScoverageKeys.coverageEnabled := true,
  ScoverageKeys.coverageHighlighting := true,
  ScoverageKeys.coverageFailOnMinimum := true,
  ScoverageKeys.coverageMinimum := 50,

  // Required scala standard libraries.
  //
  // As stated above, these must be universal and nontrasnsitive for all projects. In particular, indirect dependences
  // (dependencies that are required by projects upon which all Facsimile projects are dependent) should not be
  // explicitly included, as this can lead to versioning problems (such as depending upon two or more different versions
  // of the same library).
  //
  // Right now, the only universal dependencies are libraries required by the test phase.
  libraryDependencies ++= Seq(

    // ScalaTest dependency.
    "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  )
)

// Unidoc project settings.
//
// These settings should be added to projects that generate documentation. Documentation for sub-projects will be
// combined into a single API document.
lazy val unidocProjectSettings = unidocSettings ++ Seq(

  // Scaladoc generation options.
  //
  // The -Ymacro-no-expand prevents macro definitions from being expanded in macro sub-classes (Unidoc is currently
  // unable to accommodate macros, so this is necessary).
  scalacOptions in (ScalaUnidoc, UnidocKeys.unidoc) := Seq(  
    "-diagrams",
    "-doc-footer", s"Copyright © ${copyrightRange}, ${organizationName.value}. All rights reserved.",
    "-doc-format:html",
    "-doc-title", "Facsimile API Documentation",
    "-doc-version", version.value,
    "-groups",
    "-implicits",
    "-no-link-warnings",
    "-Xfatal-warnings",
    "-Ymacro-expand:none"
  ),

  // Scaladoc configuration.
  //
  // Allow Scaladoc to link to the Scala documentation for third-party libraries (through the apiURL setting).
  autoAPIMappings in (ScalaUnidoc, UnidocKeys.unidoc) := true,
  //apiMappings in (ScalaUnidoc, UnidocKeys.unidoc) += (
  //  unmanagedBase.value / "jt.jar" -> url("http://docs.oracle.com/javase/8/docs/api/")
  //),

  // Have SBT run the "unidoc" command when given the "doc" command.
  doc in Compile := (doc in ScalaUnidoc).value,

  // Have unidoc write output to the "api" directory, instead of the "unidoc" directory.
  target in (ScalaUnidoc, UnidocKeys.unidoc) := crossTarget.value / "api"
)

// Settings for all projects that should not publish artifacts to the Sonatype OSS repository.
lazy val unpublishedProjectSettings = Seq(

  // Ensure that the current project does not publish any of its artifacts.
  publishArtifact := false
)

// Facsimile root project.
//
// The Facsimile root project simply aggregates actions across all Facsimile projects. However, it is responsible for
// merging the documentation from those projects and publishing it on the Facsimile web-site.
lazy val facsimile = project.in(file(".")).
aggregate(facsimileUtil).
settings(commonSettings: _*).
settings(unidocProjectSettings: _*).
settings(

  // Name and description of this project.
  name := "Facsimile",
  normalizedName := "facsimile",
  description := """
    The Facsimile project's goal is to develop and maintain a high-quality, 3D, discrete-event simulation library that
    can be used for industrial simulation projects in an engineering and/or manufacturing environment.

    Facsimile simulations run on Microsoft Windows as well as on Linux, Mac OS, BSD and Unix on the Java virtual
    machine.
  """,

  // Disable aggregation of the "doc" command, so that we do not attempt to generate documentation for the core and
  // macro sub-projects individually - we will rely on the Unidoc plugin to take care of that for us.
  aggregate in doc := false
)

// Facsimile-Util project.
//
// The Facsimile-Util project contains common utility code that is utilized by other Facsimile projects, as well as
// third-party projects.
//
// Since it contains macros, it is itself composed of two sub-projects, facsimile-util-core and facsimile-util-macro, to
// which this project aggregates actions. The sub-projects are necessary because macros must be compiled before they can
// be utilized, and the utility macros are referenced by the core portion of the library. Consequently, the two
// sub-projects are built separately then combined into a single project library afterwards.
//
// Actions are aggregated to these sub-projects, with the results being merged into a single library that is published
// to the Sonatype OSS repository.
lazy val facsimileUtil = project.in(file("facsimile-util")).
aggregate(facsimileUtilMacro, facsimileUtilCore).
settings(commonSettings: _*).
settings(publishedProjectSettings: _*).
settings(unidocProjectSettings: _*).
settings(

  // Name and description of this project.
  name := "Facsimile Utility Library",
  normalizedName := "facsimile-util",
  description := """
    The Facsimile Utility library provides a number of utilities required by other Facsimile libraries as well as
    third-party libraries.
  """,

  // Ensure that core and macro classes and sources are copied to the corresponding distribution jar files.
  mappings in(Compile, packageBin) ++= mappings.in(facsimileUtilMacro, Compile, packageBin).value,
  mappings in(Compile, packageBin) ++= mappings.in(facsimileUtilCore, Compile, packageBin).value,
  mappings in(Compile, packageSrc) ++= mappings.in(facsimileUtilMacro, Compile, packageSrc).value,
  mappings in(Compile, packageSrc) ++= mappings.in(facsimileUtilCore, Compile, packageSrc).value
)

// Facsimile-Util-Core project.
//
// This is a sub-project of Facsimile-Util. It contains non-macro code providing common utilities.
//
// Artifacts from this project are merged into Facsimile-Util's artifacts and should not be published.
lazy val facsimileUtilCore = project.in(file("facsimile-util/core")).
dependsOn(facsimileUtilMacro % "test->test;compile->compile").
settings(commonSettings: _*).
settings(sourceProjectSettings: _*).
settings(unpublishedProjectSettings: _*).
settings(

  // Name and description of this project.
  name := "Facsimile Utility Library - Core",
  normalizedName := "facsimile-util-core",
  description := """
    Facsimile Utility Library core code.
  """,

  // Help the test code find the test JAR files that we use to verify JAR file manifests.
  unmanagedBase in Test := baseDirectory.value / "src/test/lib"
)

// Facsimile-Util-Macro project.
//
// This is a sub-project of Facsimile-Util. It contains macro code providing common utilities.
//
// Artifacts from this project are merged into Facsimile-Util's artifacts and should not be published.
lazy val facsimileUtilMacro = project.in(file("facsimile-util/macro")).
settings(commonSettings: _*).
settings(sourceProjectSettings: _*).
settings(unpublishedProjectSettings: _*).
settings(

  // Name and description of this project.
  name := "Facsimile Utility Library - Macro",
  normalizedName := "facsimile-util-macro",
  description := """
    Facsimile Utility Library macro code.
  """,

  // Add library dependencies for the macros sub-project.
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)
//scalastyle:on scaladoc