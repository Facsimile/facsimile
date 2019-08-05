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

//======================================================================================================================
// SBT build configuration Facsimile and its sub-projects.
//======================================================================================================================

import java.time.ZonedDateTime
import java.util.jar.Attributes.Name
import sbtrelease.ReleaseStateTransformations._
import sbtrelease.Version
import scoverage.ScoverageKeys
import xerial.sbt.Sonatype.sonatypeSettings

// Disable certain Scalastyle options, which IntelliJ IDEA insists on applying to SBT sources.
//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals

// Library dependency version information.
//
// Keep all compiler and library version numbers here for easy maintenance.
val CatsVersion = "1.6.0"
val LightbendConfigVersion = "1.3.4"
val ParboiledVersion = "2.1.6"
val ScalaVersion = "2.12.8"
val ScalaCheckVersion = "1.14.0"
val ScalaTestVersion = "3.0.8"
//val SpireVersion = "0.16.2"
val SquantsVersion = "1.4.0"

// Add external resolvers here (and also in project/Resolvers.sbt).
//
// This is necessary because Travis CI (and other users) will likely not have the correct resolvers defined in
// ~/.sbt/1.0/resolvers.sbt (or equivalent). Refer to the issue below for further details:
//
//   https://github.com/sbt/sbt/issues/4103#issuecomment-509162557
resolvers in ThisBuild += "Artima Maven Repository" at "http://repo.artima.com/releases"

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

// Git URL.
val gitURL = "https://github.com/Facsimile/facsimile"

// Git SCM record
val gitSCM = s"scm:git:$gitURL.git"

// Dependency criteria for both compile and test.
val dependsOnCompileTest = "test->test;compile->compile"

// Publish artifacts to the Sonatype OSS repository.
//
// It appears that this needs to be set globally.
publishTo in ThisBuild := sonatypePublishTo.value

// Common Scala compilation options (for compiling sources and generating documentation).
lazy val commonScalaCSettings = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  //"-Xfatal-warnings",
)

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

  // Homepage of the associated project.
  homepage := Some(url("http://facsim.org/")),

  // Scala cross compiling.
  //
  // Right now, the complexities of supporting multiple Scala versions has been deferred in favor of supporting a single
  // version that meets the project's requirements. This decision forces all users to use the same Scala release that
  // was used to build Facsimile and so will be reviewed in future.
  //
  // IMPORTANT: These values MUST be synchronized with the Travis CI .travis.yml file in the project's root directory,
  // or Travis CI Facsimile builds may yield unexpected results.
  crossScalaVersions := Seq(ScalaVersion),

  // Scala default version.
  //
  // NOTE: While it might appear that these Scala version options should be placed in "sourceProjectSettings", SBT will
  // use the Scala version to decorate the project's artifact/normalized name. Hence, even if a project does not contain
  // any sources, it it still necessary to provide the version of Scala that is in use.
  scalaVersion := crossScalaVersions.value.head,
)

// Doc project settings.
//
// These settings should be added to projects that generate documentation.
lazy val docProjectSettings = Seq(

  // ScalaDoc generation options.
  //
  // The -Ymacro-no-expand prevents macro definitions from being expanded in macro sub-classes (Unidoc is currently
  // unable to accommodate macros, so this is necessary).
  //
  // Note that diagram generation requires that GraphViz be installed on the build machine. However, at the time of
  // writing, ScalaDoc's use of GraphViz is broken and does not work correctly.
  scalacOptions in (Compile, doc) := commonScalaCSettings ++ Seq(
    "-author",
    "-diagrams",
    //"-diagrams-debug", //<-- Uncomment option to debug diagramming errors. Make sure graphviz is installed.
    "-doc-footer", s"Copyright © $copyrightRange, ${organizationName.value}. All rights reserved.",
    "-doc-format:html",
    "-doc-title", "Facsimile API Documentation",
    "-doc-version", version.value,
    "-groups",
    //"-implicits",
    //"-no-prefixes",
    "-Ymacro-expand:none",
  ),

  // Allow Facsimile's ScalaDoc to link to the ScalaDoc documentation of dependent libraries that have included an
  // "apiURL" property in their library's Maven POM configuration.
  autoAPIMappings := true,
)

// Published project settings.
//
// Published projects must define the artifacts to be published, and take of publishing them to the Sonatype OSS
// repository. Consequently, there is a lot of Maven/SBT/Ivy configuration information here.
//
// Note that the Sonatype plugin's settings are used to ensure that the resulting artifacts can be published to the
// Sonatype OSS repository, which automatically pushes project information to the Maven Central Repository.
//
// NOTES:
// 1.  Test artifacts should NOT be published. This is disabled by the line "publishArtifact in Test := false" below.
// 2.  Third-party artifacts referenced by Facsimile must be available in the Maven Central Repository.
// 3.  Maven metadata that is not defined by SBT properties must be defined in the "pomExtra" setting as XML.
// 4.  Artifacts must be signed via GPG to be published to the Sonatype OSS Nexus repository. In this case, the software
//     must be signed using the key for "authentication@facsim.org". If your version of Facsimile is signed by a
//     different key, then you do not have the official version.)
//
// The sbt-gpg plugin uses GPG (GNU Privacy Guard) to sign artifacts, and this must be installed on the current
// machine. The key ID must be specified in a file configured by the Release Manager, typically
// "~/.sbt/1.0/Credentials.sbt". Finally, the key (including the private key) must be registered in GPG on the local
// machine.
//
// WARNING: THE GPG SECRET KEY AND THE CREDENTIALS FILE MUST NEVER BE MADE PUBLIC AND SHOULD NEVER BE COMMITTED AS PART
// OF THE FACSIMILE SOURCES.
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

  // Facsimile utilizes git for version control, hosted by GitHub.
  scmInfo := Some(
    ScmInfo(url(gitURL), gitSCM, Some(gitSCM))
  ),

  // Test artifacts should not be published.
  publishArtifact in Test := false,

  // Developers. Add yourself here if you've contributed code the Facsimile project.
  //
  // Developer fields are: ID, name, email & URL.
  developers := List(
    Developer("mja", "Michael J Allen", "mike.allen@facsim.org", url("http://facsim.org")),
  ),

  // Maven POM information which is not available elsewhere through SBT settings.
  pomExtra :=
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
  apiURL := organizationHomepage.value.map(h => url(h.toString + s"/Documentation/API/${version.value}")),

  // Manifest additions for the main library jar file.
  //
  // The jar file should be sealed so that the packages contained cannot be extended. We also add inception & build
  // timestamps for information purposes.
  packageOptions in (Compile, packageBin) ++= Seq(
    Package.ManifestAttributes(Name.SEALED -> "true"),
    Package.ManifestAttributes("Inception-Timestamp" -> facsimileStartDate.toString),
    Package.ManifestAttributes("Build-Timestamp" -> facsimileBuildDate.toString)
  ),

  // By default, we'll bump the bug-fix/release number of the version following a release.
  releaseVersionBump := Version.Bump.Bugfix,

  // Have the release plugin write current version information into Version.sbt, in the project's root directory.
  //
  // NOTE: The Version.sbt file MUST NOT be manually edited and must be maintained under version control.
  //
  // Commented out, as this no longer appears to be working correctly. Also renamed the version file form "Version.sbt"
  // (my preference) to "version.sbt", so that it works by default. Issue raised:
  //
  //    https://github.com/sbt/sbt-release/issues/252
  //releaseVersionFile := file("Version.sbt"),

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

    // Run scalastyle on sources to ensure that sources are correctly formatted and contain no static errors.
    releaseStepInputTask(scalastyle),

    // Run scalastyle on test sources to ensure that sources are correctly formatted and contain no static errors.
    releaseStepInputTask(scalastyle in Test),

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

    // Push all commits to the upstream repository.
    //
    // This is typically determined by including the "-u" argument to a git push command. The upstream repository for a
    // release MUST be the primary Facsimile repository, not a fork.
    pushChanges,

    // Publish the released version to the Sonatype OSS repository.
    //
    // The "publish-artifacts" step above takes care of publishing the artifacts to Sonatype, so the following command
    // initiates the process of having Sonatype release them, which may fail if Sonatype determines that the artifacts
    // do not meet their current specifications.
    releaseStepCommand("sonatypeReleaseAll"),
  ),
)

// Source project settings.
//
// These settings are common to all projects that contain source files, which must be compiled and tested.
//
// Any library dependencies listed here MUST be universal and not non-transitive.
lazy val sourceProjectSettings = Seq(

  // Scala compiler options.
  //
  // -Xfatal-warnings is disabled because some deprecated code features are still in use, resulting in warnings that
  // cannot currently be suppressed. (The Scala team have been deprecating a lot of features as of 2.11, but there are
  // no alternatives to many of the deprecated classes, which is becoming a nuisance.)
  //
  // As Xfatal-warnings is not in use, it's possible to have builds that generate tons of warnings, but which do not
  // fail a build. This is unacceptable: projects must build clean, without any errors or warnings, as a basic
  // requirement for any release to be performed.
  //
  // -Xstrict-inference is currently disabled as it outputs erroneous warnings for some generic code. See
  // https://issues.scala-lang.org/browse/SI-7991 for further details.
  scalacOptions in Compile := commonScalaCSettings ++ Seq(

    // Code compilation options. YpartialUnification is required by the Typelevel Cats library.
    "-feature",
    "-g:vars",
    "-opt:l:method",
    "-opt-warnings:_",
    "-target:jvm-1.8",
    "-unchecked",
    "-Xcheckinit",
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
    "-Ywarn-value-discard",
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
  ScoverageKeys.coverageMinimum := 0,

  // Required libraries common to all source projects.
  //
  // As stated above, these must be universal and non-transitive for all projects. In particular, indirect dependencies
  // (dependencies that are required by direct dependencies) should not be explicitly included, as this can lead to
  // versioning problems (such as depending upon two or more different versions of the same library).
  //
  // Right now, the only universal dependencies are libraries required by the test phase.
  libraryDependencies ++= Seq(

    // ScalaTest dependencies.
    "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,

    // ScalaCheck dependency.
    "org.scalacheck" %% "scalacheck" % ScalaCheckVersion % Test,
  ),
)

// Settings for all projects that should not publish artifacts to the Sonatype OSS repository.
lazy val unpublishedProjectSettings = Seq(

  // Ensure that the current project does not publish any of its artifacts.
  publishArtifact := false,
)

// Name of the facsimile-util project.
val FacsimileUtilName = "facsimile-util"

// Facsimile-Util project.
//
// The Facsimile-Util project contains common utility code that is utilized by other Facsimile projects, as well as
// third-party projects.
lazy val facsimileUtil = project.in(file(FacsimileUtilName))
.settings(commonSettings: _*)
.settings(sourceProjectSettings: _*)
.settings(docProjectSettings: _*)
.settings(publishedProjectSettings: _*)
.settings(

  // Name and description of this project.
  name := "Facsimile Utility Library",
  normalizedName := FacsimileUtilName,
  description := """The Facsimile Utility library provides a number of utilities required by other Facsimile libraries
  |as well as third-party libraries.""".stripMargin.replaceAll("\n", " "),

  // Utility library dependencies.
  libraryDependencies ++= Seq(

    // The Scala reflection library is required for implementing macros.
    "org.scala-lang" % "scala-reflect" % ScalaVersion,

    // Lightbend configuration library.
    //
    // This library supports configuration file management, and the Human-Optimized Config Object Notation (HOCON)
    // configuration file format. HOCON can be viewed as a superset of both the Java properties and JavaScript Object
    // Notation (JSON) file formats.
    "com.typesafe" % "config" % LightbendConfigVersion,

    // Parboiled 2 is a parsing library, required for Facsimile's file parsing capabilities.
    "org.parboiled" %% "parboiled" % ParboiledVersion,
  ),

  // Help the test code find the test JAR files that we use to verify JAR file manifests.
  unmanagedBase in Test := baseDirectory.value / "src/test/lib",
)

// Name of the facsimile-collection project.
val FacsimileCollectionName = "facsimile-collection"

// Facsimile-Collection project.
//
// The Facsimile-Collection project contains custom, immutable collections that are utilized by other Facsimile
// projects.
lazy val facsimileCollection = project.in(file(FacsimileCollectionName))
.dependsOn(facsimileUtil % dependsOnCompileTest)
.settings(commonSettings: _*)
.settings(sourceProjectSettings: _*)
.settings(docProjectSettings: _*)
.settings(publishedProjectSettings: _*)
.settings(

  // Name and description of this project.
  name := "Facsimile Collection Library",
  normalizedName := FacsimileCollectionName,
  description := """The Facsimile Collection library provides custom, immutable collections that are required by other
                   |Facsimile libraries as well as third-party libraries.""".stripMargin.replaceAll("\n", " "),
)

// Temporarily commented out - not ready for launch, right now.
//// Name of the facsimile-sfx project.
//lazy val FacsimileSFXName = "facsimile-sfx"
//
//// Facsimile-SFX project.
////
//// The Facsimile-SFX project is a lightweight Scala wrapper for JavaFX.
//lazy val facsimileSFX = project.in(file(FacsimileSFXName))
//.dependsOn(facsimileUtil % dependsOnCompileTest)
//.settings(commonSettings: _*)
//.settings(sourceProjectSettings: _*)
//.settings(docProjectSettings: _*)
//.settings(publishedProjectSettings: _*)
//.settings(
//
//  // Name and description of this project.
//  name := "Facsimile SFX Library",
//  normalizedName := FacsimileSFXName,
//  description:= """The Facsimile SFX library is a lightweight Scala wrapper for JavaFX.""".stripMargin.
//  replaceAll("\n", " "),
//)

// Temporarily commented out - not ready for launch, right now.
//// Name of the facsimile-types project.
//val FacsimileTypesName = "facsimile-types"
//
//// Facsimile-Types project.
////
//// The Facsimile-Types project supports custom value type classes, which support dimensional analysis, physics
//// calculations, probabilities, etc., in a variety of supported units.
//lazy val facsimileTypes = project.in(file(FacsimileTypesName))
//.dependsOn(facsimileUtil % dependsOnCompileTest)
//.settings(commonSettings: _*)
//.settings(sourceProjectSettings: _*)
//.settings(docProjectSettings: _*)
//.settings(publishedProjectSettings: _*)
//.settings(
//
//  // Name and description of this project.
//  name := "Facsimile Types Library",
//  normalizedName := FacsimileTypesName,
//  description := """The Facsimile Types library supports dimensional analysis, physics calculations, probabilities,
//  |specified in a variety of value classes, in a variety of supported units.""".stripMargin.replaceAll("\n", " "),
//
//  libraryDependencies ++= Seq(
//    "org.typelevel" %% "spire" % SpireVersion,
//  ),
//)

// Temporarily commented out - not ready for launch, right now.
//// Name of the facsimile-stat project.
//val FacsimileStatName = "facsimile-stat"
//
//// Facsimile-Stat project.
////
//// The Facsimile-Stat project supports statistical distribution sampling, reporting, analysis and inference testing.
//lazy val facsimileStat = project.in(file(FacsimileStatName))
//.dependsOn(facsimileUtil % dependsOnCompileTest, facsimileTypes % dependsOnCompileTest)
//.settings(commonSettings: _*)
//.settings(sourceProjectSettings: _*)
//.settings(docProjectSettings: _*)
//.settings(publishedProjectSettings: _*)
//.settings(
//
//  // Name and description of this project.
//  name := "Facsimile Statistical Library",
//  normalizedName := FacsimileStatName,
//  description := """The Facsimile Statistical library supports statistical distribution sampling, reporting, analysis
//  |and inference testing.""".stripMargin.replaceAll("\n", " "),
//)

// Name of the facsimile-simulation project.
val FacsimileSimulationName = "facsimile-simulation"

// Facsimile-Simulation project.
//
// The Facsimile-Simulation project provides a purely functional simulation engine for running simulations.
lazy val facsimileSimulation = project.in(file(FacsimileSimulationName))
// Temporarily remove dependency on SFX and Stat modules - not ready for launch, right now.
//.dependsOn(facsimileCollection % dependsOnCompileTest, facsimileSFX % dependsOnCompileTest,
//facsimileStat % dependsOnCompileTest)
.dependsOn(facsimileCollection % dependsOnCompileTest)
.settings(commonSettings: _*)
.settings(sourceProjectSettings: _*)
.settings(docProjectSettings: _*)
.settings(publishedProjectSettings: _*)
.settings(

  // Name and description of this project.
  name := "Facsimile Simulation Library",
  normalizedName := FacsimileSimulationName,
  description := """The Facsimile Simulation library is a purely functional, discrete-event simulation engine. It is the
  |beating heart at the center of all Facsimile simulation models.""".stripMargin.replaceAll("\n", " "),

  // Facsimile Engine dependencies.
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % CatsVersion,
    "org.typelevel" %% "squants" % SquantsVersion,
  )
)

// Facsimile root project.
//
// The Facsimile root project simply aggregates actions across all Facsimile projects.
//
// TODO: Merge all documentation for sub-projects and publish it ti the Facsimile web-site/elsewhere.
lazy val facsimile = project.in(file("."))
// Temporarily remove dependency on SFX and Stat modules - not ready for launch, right now.
//.aggregate(facsimileUtil, facsimileCollection, facsimileTypes, facsimileSFX, facsimileStat, facsimileSimulation)
.aggregate(facsimileUtil, facsimileCollection, facsimileSimulation)
.enablePlugins(ScalaUnidocPlugin)
.settings(commonSettings: _*)
.settings(unpublishedProjectSettings: _*)
.settings(

  // Name and description of this project.
  name := "Facsimile",
  normalizedName := "facsimile",
  description := """The Facsimile project's goal is to develop and maintain a high-quality, 3D, discrete-event
  |simulation library that can be used for industrial simulation projects in an engineering and/or manufacturing
  |environment. Facsimile simulations run on Microsoft Windows as well as on Linux, Mac OS, BSD and Unix on the Java
  |virtual machine.""".stripMargin.replaceAll("\n", " "),
)
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc
