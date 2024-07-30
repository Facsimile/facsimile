//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
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
import scala.util.Properties
import scoverage.ScoverageKeys
import xerial.sbt.Sonatype.sonatypeSettings

// Disable certain Scalastyle options, which IntelliJ IDEA insists on applying to SBT sources.
//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals

// Library dependency version information.
//
// Keep all compiler and library version numbers here, in alphabetical order, for easy maintenance.
//
// NOTE: When changing the primary Scala version, remember to update "./.travis.yml" to match.
val AkkaVersion = "2.6.17"
val CatsVersion = "2.1.0"
val ParboiledVersion = "2.5.1"
val PrimaryScalaVersion = "2.13.7"
val ScalaMeterVersion = "0.21"
val ScalaTestPlusScalaCheckVersion = "3.1.0.0-RC2"
val ScalaTestVersion = "3.2.19"
val ScoptVersion = "4.1.0"
val SquantsVersion = "1.8.3"

// Regular expression for matching release versions.
val ReleaseVersion = """(\d+)\.(\d+)\.(\d+)""".r

// Regular expression for matching snapshot versions.
val SnapshotVersion = """(\d+)\.(\d+)\.(\d+)-SNAPSHOT""".r

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

// Retrieve base version number.
//
// Return a base version number made up of just the major and minor version numbers, without a release/revision/build
// number or a SNAPSHOT tail.
def baseVersion(ver: String): String = ver match {
  case ReleaseVersion(maj, min, _) => s"$maj.$min"
  case SnapshotVersion(maj, min, _) => s"$maj.$min"
  case _ => s"Invalid(\'$ver\')"
}

// Git URL.
val gitURL = "https://github.com/Facsimile/facsimile"

// Git SCM record
val gitSCM = s"scm:git:$gitURL.git"

// Dependency criteria for both compile and test.
//
// NOTE: This appears to prevent Scaladoc from resolving links to library dependency documentation. Refer to the
// following bug report for further details:
//
//   https://github.com/sbt/sbt/issues/4929
val dependsOnCompileTest = "compile;test->test"

// Common project settings.
//
// These settings are common to all SBT root- and sub-projects.
//
// Note that we implement release versioning for artifacts through the Release plugin. The current version is stored in
// the "version.sbt" file.
//
// Owning organization.
//
// This is the Maven/SBT/Ivy group ID and should match the root package name of the Scala sources. It should also be the
// reverse of the web-site name (less any "www" prefix). Thus "http://facsim.org/" yields an organization ID/root
// package name of "org.facsim"
ThisBuild / organization := "org.facsim"

// Human-readable legal name of the owning organization.
ThisBuild / organizationName := "Michael J Allen"

// Web-site of the owning organization.
ThisBuild / organizationHomepage := Some(url("http://facsim.org/"))

// Web-site of the associated project.
ThisBuild / homepage := Some(url("http://facsim.org/"))

// Scala version.
//
// NOTE: While it might appear that these Scala version options should be placed in "sourceProjectSettings", SBT will
// use the Scala version to decorate the project's artifact/normalized name. Hence, even if a project does not contain
// any sources, it it still necessary to provide the version of Scala that is in use.
ThisBuild / scalaVersion := PrimaryScalaVersion

// Publish artifacts to the Sonatype OSS repository.
//
// It appears that this needs to be set globally.
ThisBuild / publishTo := sonatypePublishToBundle.value

// Add external resolvers here (and also in project/Resolvers.sbt).
//
// This is currently necessary because SBT does not appear to allow certain resolvers (such as the "Artima Maven
// Repository") to specify a project-wide resolver in just the "project/Resolves.sbt" file (or equivalent). Refer to the
// following issues for further details.
//
//   https://github.com/sbt/sbt/issues/4103
//   https://github.com/sbt/sbt/issues/4103#issuecomment-509162557
//   https://github.com/sbt/sbt/issues/5070
//   https://github.com/scalatest/scalatest/issues/1696
//
// Artima repository is required for SuperSafe compiler plugin.
ThisBuild / resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"

// Common Scala compilation options (for compiling sources and generating documentation).
lazy val commonScalaCSettings = Seq(
  "-encoding", "UTF-8",
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
  Compile / doc / scalacOptions := commonScalaCSettings ++ Seq(
    "-author",
    "-diagrams",
    //"-diagrams-debug", //<-- Uncomment option to debug diagramming errors. Make sure graphviz is installed.
    "-doc-footer", s"Copyright © $copyrightRange, ${organizationName.value}. All rights reserved.",
    "-doc-format:html",
    "-doc-title", s"{name.value} API Documentation",
    "-doc-version", baseVersion(version.value),
    "-groups",
    //"-implicits",
    //"-no-prefixes",
    "-Ymacro-expand:none",
  ),

  // Allow the generated ScalaDoc to link to the ScalaDoc documentation of dependent libraries that have included an
  // "apiURL" property in their library's Maven POM configuration.
  autoAPIMappings := true,
)

// Published project settings.
//
// Published projects must define the artifacts to be published, and take care of publishing them to the Sonatype OSS
// repository. Consequently, there is a lot of Maven/SBT/Ivy configuration information here.
//
// Note that the Sonatype plugin's settings are used to ensure that the resulting artifacts can be published to the
// Sonatype OSS repository, which automatically pushes project information to the Maven Central Repository.
//
// NOTES:
// 1. Test artifacts should NOT be published. This is disabled by the line "Test / publishArtifact := false" below.
// 2. Third-party artifacts referenced by Facsimile must be available in the Maven Central Repository.
// 3. Maven metadata that is not defined by SBT properties must be defined in the "pomExtra" setting as XML.
// 4. Artifacts must be signed via GPG for verification and authenticity purposes. This is also essential for artifcats
//    to be published to the Sonatype OSS Nexus repository; in this case, the software must be signed using the key for
//    "authentication@facsim.org". If your version of Facsimile is signed by a different key, then you do not have the
//    official version.)
//
// The sbt-gpg plugin uses GPG (GNU Privacy Guard) to sign artifacts, and this must be installed on the current machine.
// The key ID must be specified in a file configured by the Release Manager, typically "~/.sbt/1.0/Credentials.sbt".
// Finally, the key (including the private key) must be registered in GPG on the local machine.
//
// WARNING: THE GPG SECRET KEY AND THE CREDENTIALS FILE MUST NEVER BE MADE PUBLIC AND SHOULD NEVER BE COMMITTED AS PART
// OF ANY SOURCES.
//
// We publish to the snapshots repository if this is a snapshot, or to the releases staging repository if this is an
// official release.
lazy val publishedProjectSettings = sonatypeSettings ++ Seq(

  // Identifier of the GPG key used to sign artifacts during publication.
  gpgKey := Some("authentication@facsim.org"),

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
  Test / publishArtifact := false,

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

  // Scaladoc API.
  //
  // Tell any dependent projects where to find published API documentation to link to (via autoAPIMappings).
  //
  // This link will be published in the project's Maven POM file.
  //
  // Note: This documentation is versioned, using the base version, so that links will always be to the version of
  // this software in use by the dependent project.
  apiURL := organizationHomepage.value.map(h => url(h.toString + s"/Documentation/API/${version.value}")),

  // Manifest additions for the library jar file.
  //
  // The jar file should be sealed so that the packages contained cannot be extended. We also add inception & build
  // timestamps for information purposes.
  Compile / packageBin / packageOptions ++= Seq(

    // Standard manifest attributes.
    Package.ManifestAttributes(

      // The jar file should be sealed so that the packages contained withing it cannot be extended.
      Name.SEALED -> "true",

      // Override the version of the specification to be just the base version.
      Name.SPECIFICATION_VERSION -> baseVersion(version.value),
    ),

    // Now for some custom attributes.
    //
    // These are useful for documenting the conditions under which a build was made, as well as for providing useful
    // information to the user.
    Package.ManifestAttributes(

      // Add inception timestamp so that project knows it's start date.
      "Inception-Timestamp" -> facsimileStartDate.toString,

      // Document the tool employed to build this project.
      "Build-SBT-Version" -> sbtVersion.value,

      // Document the version of the JDK used to build this project.
      "Build-JDK-Version" -> Properties.javaVersion,

      // Document the version of the Scala compiler used to create the build.
      //
      // Note: This is NOT necessarily the same as the version of Scala used to compile the project - it is the
      // version of Scala that was used to compile the SBT build.
      "Build-Scala-Version" -> Properties.versionNumberString,

      // Add build timestamp so that project knows when it was built.
      "Build-Timestamp" -> facsimileBuildDate.toString,
    ),
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
    releaseStepCommand("scalastyle"),

    // Run scalastyle on test sources to ensure that sources are correctly formatted and contain no static errors.
    // This is temporarily disabled because of errors in Scalastyle parsing some of the recent test sources. See also
    // .travis.yml.
    //
    //releaseStepInputTask(Test / scalastyle),

    // Update the "Version.sbt" file so that it contains the release version number.
    setReleaseVersion,

    // Commit and tag the release version.
    commitReleaseVersion,
    tagRelease,

    // Sign, publish and release artifacts to the Sonatype OSS repository.
    //
    // This requires publishTo to be defined correctly (see above).
    //
    // NOTE: If cross-publishing, use 'releaseStepCommandAndRemaining("+publishSigned")' in place of
    // 'releaseStepCommand("publishSigned")'.
    releaseStepCommand("publish"),
    releaseStepCommand("sonatypeBundleRelease"),

    // Update the "Version.sbt" file so that it contains the new development version number.
    setNextVersion,

    // Commit the updated working directory, so that the new development version takes effect.
    commitNextVersion,

    // Push all commits to the upstream repository.
    //
    // This is typically determined by including the "-u" argument to a git push command. The upstream repository for a
    // release MUST be the primary Facsimile repository, not a fork.
    pushChanges,
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
  // -Woctal-literal gives false positives in Scala 2.13.2 for any use of 0 as an integer literal. This issue will be
  // fixed in Scala 2.13.3. See https://github.com/scala/bug/issues/11950 for further details. The resulting warnings
  // currently prevents -Werror from being utilized.
  //
  // Artima SuperSafe issues a generic warning for "errors" that are not reported by the free, Community Edition. The 8
  // errors reported in facsimile-collection\src\test\scala\org\facsim\collection\immutable\test\BinomialHeapTest.scala
  // are false positives, and can be ignored. However, the resulting warning currently prevents -Werror from being
  // utilized. This issue was reported to Artima: See https://github.com/scalatest/scalatest/issues/1737 for futher
  // details.
  //
  // Scaladoc generation is currently a little buggy, and produces an lot of incorrect warnings. For this reason,
  // -Werror can only specified when compiling code, rather than when generating documentation. When these issues are
  // resolved, -Werror should be added to commonScalaCSettings.
  Compile / scalacOptions := commonScalaCSettings ++ Seq(
    "-feature",
    "-g:vars",
    "-opt:l:method",
    "-opt-warnings:_", // Enable all optimization warnings.
    "-target:8",
    "-unchecked",
    "-Wdead-code",
    //"-Werror", // Fail compilation if there are any errors. See above.
    "-Wextra-implicit",
    "-Wmacros:before",
    "-Wnumeric-widen",
    "-Woctal-literal",
    "-Wself-implicit",
    "-Wunused:_", // Enable all warnings about unused elements (imports, privates, etc.).
    "-Wvalue-discard",
    "-Xcheckinit",
    "-Xlint:_", // Enable all Xlint warnings.
    "-Ymacro-annotations",
  ),

  // Fork the tests, so that they run in a separate process. This is a requirement for forked benchmarking with
  // ScalaMeter, and improves test reliability.
  Test / fork := true,

  // As recommended by ScalaTest, disable buffered logs in Test, in order to use ScalaTest's built-in buffering. This
  // helps to present test results in a logical manner when tests are executed in parallel.
  Test / logBuffered := false,

  // Execute tests in parallel.
  //
  // Output makes more sense using the ScalaTest log buffering algorithm (enabled by disabling logBuffered for testing,
  // above).
  //
  // Currently disabled because it interferes with ScalaMeter benchmarking.
  Test / parallelExecution := false,

  // Code test coverage settings.
  //
  // Target coverage is 100%. Over time, the test coverage required for a successful build will be raised to this value
  // to ensure code is as fully tested as possible.
  //
  // Note: NEVER set ScoverageKeys.coverageEnabled to true, as it will cause the library to look for the Scoverage
  // measurement files on the original build machine, and throw a FileNotFoundException when it fails to locate them.
  // Instead, issuing "coverage" or "coverageOn" commands to SBT will enable coverage (prior to testing on Travis CI,
  // for example). Also, make sure that "clean" is issued prior to any build operation.
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

    // ScalaTest is a library providing a framework for unit-testing
    "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,

    // ScalaTest plus ScalaCheck, property-based testing library dependencies. This is used by ScalaTest.
    //
    // Note: This adds ScalaCheck as a test dependency, so it is no longer necessary to add it as a separate dependency.
    "org.scalatestplus" %% "scalatestplus-scalacheck" % ScalaTestPlusScalaCheckVersion % Test,

    // ScalaMeter, micro-benchmarking library dependencies.
    "com.storm-enroute" %% "scalameter" % ScalaMeterVersion % Test,
  ),

  // Add ScalaMeter as a test framework.
  testFrameworks += new TestFramework("org.scalameter.ScalaMeterFramework"),
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
    "org.scala-lang" % "scala-reflect" % PrimaryScalaVersion,

    // Akka streams library & testkit (the latter scoped for testing only).
    //
    // This is used for creating streams of data.
    //
    // Akka streams additionally includes the following library dependencies:
    //
    // "com.typesafe" %% "config"
    // "com.typesafe" %% "ssl-config-core"
    // "com.typesafe.akka" %% "akka-core"
    // "com.typesafe.akka" %% "akka-actor"
    // "com.typesafe.akka" %% "akka-protobuf"
    // "org.reactivestreams" %% "reactive-streams"
    // "org.scala-lang.modules" %% "scala-java8-compat"
    // "org.scala-lang.modules" %% "scala-parser-combinators"
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,

    // Parboiled 2 is a parsing library, required for Facsimile's file parsing capabilities.
    "org.parboiled" %% "parboiled" % ParboiledVersion,
  ),

  // Help the test code find the test JAR files that we use to verify JAR file manifests.
  Test / unmanagedBase := baseDirectory.value / "src/test/lib",
)

// Name of the facsimile-collection project.
val FacsimileCollectionName = "facsimile-collection"

// Facsimile-Collection project.
//
// The Facsimile-Collection project contains custom, immutable collections that are utilized by other Facsimile
// projects.
lazy val facsimileCollection = project.in(file(FacsimileCollectionName))
.dependsOn(facsimileUtil % dependsOnCompileTest)
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

    // Scopt is a functional command line parsing library.
    "com.github.scopt" %% "scopt" % ScoptVersion,

    // Cats is a general-purpose library supporting functional programming. Facsimile uses the Cat State monad heavily
    // for simulation state transitions.
    "org.typelevel" %% "cats-core" % CatsVersion,

    // Squants is a dimensional analysis library, providing support for modeling physical quantities such as Time,
    // Length, Angle, Velocity, etc.
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
