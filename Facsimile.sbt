//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2026, Michael J Allen.
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
// SBT build configuration for Facsimile and its sub-projects.
//======================================================================================================================
import java.time.ZonedDateTime
import java.util.jar.Attributes.Name
import sbtrelease.ReleaseStateTransformations.*
import sbtrelease.Version
import scala.util.Properties
import scoverage.ScoverageKeys

// Library dependency version information.
//
// Keep all compiler and library version numbers here, in alphabetical order, for easy maintenance.
val CatsVersion = "2.13.0"
val IzumiReflectVersion = "3.0.10"
val PekkoVersion = "1.0.2"
val PrimaryScalaVersion = "3.9.0"
val ScalaCheckVersion = "1-19" // Formatted this way due to usage.
val ScalaTestVersion = "3.2.20"
val ScoptVersion = "4.1.0"
val SquantsVersion = "1.8.3"

/** URI prefix for standard Javadoc documentation.
 */
val JavaDocPrefix = s"https://docs.oracle.com/en/java/javase/${Properties.javaSpecVersion}/docs/api"

/** Regular expression for matching release versions.
 */
val ReleaseVersion = """(\d+)\.(\d+)\.(\d+)""".r

/** Regular expression for matching snapshot versions.
 */
val SnapshotVersion = """(\d+)\.(\d+)\.(\d+)-SNAPSHOT""".r

/** Date the _Facsimile Project_ was started.
 *
 *  This is the date that the _Facsimile Project_ was announced on the _Facsimile_ web-site (which was actually a few
 *  days after the project was first registered on _Sourceforge_, but it's the best date we have).
 */
val facsimileStartDate = ZonedDateTime.parse("2004-06-22T18:16:00-04:00[America/New_York]")

/** Date this build was performed.
 *
 *  Ideally, this ought to be the date of the current commit, but that's not so easy to determine. This should be OK,
 *  particularly for custom builds.
 */
val facsimileBuildDate = ZonedDateTime.now()

/** Copyright range.
 *
 *  If the year of the start date differs from the year of the build, then the copyright message will have a year range
 *  (e.g. 2004-2026); otherwise, since both years are the same, it will be a single year value (e.g. 2026). The return
 *  value is a string with the year range or value as appropriate.
 */
val copyrightRange =
  val startYear = facsimileStartDate.getYear.toString
  val currentYear = facsimileBuildDate.getYear.toString
  if startYear == currentYear then startYear else s"$startYear-$currentYear"

/** Retrieve base version number.
 *
 *  Return a base version number made up of just the major and minor version numbers, without a release/revision/build
 *  number or a _SNAPSHOT_ tail.
 */
def baseVersion(ver: String): String =
  ver match
    case ReleaseVersion(maj, min, _) => s"$maj.$min"
    case SnapshotVersion(maj, min, _) => s"$maj.$min"
    case _ => s"Invalid(\'$ver\')"

/** Git repository path.
 */
val gitRepo = "Facsimile/facsimile"

/** Git repository URI.
 */
val gitURI = s"https://github.com/$gitRepo"

/** Git source template.
 */
val gitSrcTemplate = s"github://$gitRepo"

/** Git source code management reference.
 */
val gitSCM = s"scm:git:$gitURI.git"

/** Dependency criteria for both compile and test.
 *
 *  @note This appears to prevent _Scaladoc_ from resolving links to library dependency documentation. Refer to the
 *  following bug report for further details: [[https://github.com/sbt/sbt/issues/4929 #4929]].
 */
val dependsOnCompileTest = "compile;test->test"

// Common project settings.
//
// These settings are common to all SBT root- and sub-projects.
//
// Note: that we implement release versioning for artifacts through the _sbt-release_ plugin. The current version is
// stored in the `version.sbt` file.
//
//  Owning organization.
//
// This is the _Maven_/_SBT_/_Ivy_ group ID and should match the root package name of the Scala sources. It should also
// be the reverse of the web-site name (less any "www" prefix). Thus, "http://facsim.org/" yields an organization
// ID/root package name of "org.facsim".
organization := "org.facsim"

// Human-readable legal name of the owning organization.
organizationName := "Michael J Allen"

// Web-site of the owning organization.
organizationHomepage := Some(uri("http://facsim.org/"))

// Web-site of the associated project.
homepage := Some(uri("http://facsim.org/"))

// Scala version.
//
// NOTE: While it might appear that these Scala version options should be placed in "sourceProjectSettings", SBT will
// use the Scala version to decorate the project's artifact/normalized name. Hence, even if a project does not contain
// any sources, it is still necessary to provide the version of Scala that is in use.
scalaVersion := PrimaryScalaVersion

// Versions of Scala to cross compile with.
//
// Given binary compatibility with Scala 3, there should be no need to compile more than one Scala 3
crossScalaVersions ++= Seq(PrimaryScalaVersion)

// Support automated builds in GitHub, following commits, PRs, etc.
//
// Use the following Java releases to run continuous integration tests on GitHub.
githubWorkflowJavaVersions := Seq(
  JavaSpec.temurin("25"),
  JavaSpec.zulu("25"),
)

// Publish using Java Temurin 25.
githubWorkflowPublishJavaVersion := JavaSpec.temurin("25")

// Publish artifacts to the Sonatype Central Release repository.
//
// It appears that this needs to be set globally.
publishTo `:=`:
  val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
  if version.value.endsWith("-SNAPSHOT") then Some("central-snapshots" at centralSnapshots)
  else localStaging.value

/** Common Scala compilation options (for compiling sources and generating documentation).
 *
 *  We'll enforce the new braceless style (using significant indentation), and updated language syntax.
 */
lazy val commonScalaCSettings = Seq(
  "-deprecation",
  "-encoding", "UTF-8",
)

/** Doc project settings.
 *
 *  These settings should be added to projects that generate documentation.
 */
lazy val docProjectSettings = Seq(

  // Allow the generated ScalaDoc to link to the ScalaDoc documentation of dependent libraries that have included an
  // "apiURL" property in their library's Maven POM configuration.
  Compile / doc / autoAPIMappings := true,

  // ScalaDoc generation options.
  //
  // Note: Coverage output information configurations is provided automatically by the Scoverage SBT plugin.
  // Note: API mappings to external documentation (such as the primary Java and Scala API documentation) configuration
  // is managed by the API Mappings SBT plugin.
  //
  // For the Javadoc references, we have to specify which Java module the corresponding namespace belongs to.
  Compile / doc / scalacOptions := commonScalaCSettings ++ Seq(
    "-author",
    "-comment-syntax:markdown",
    "-groups",
    "-project", s"${name.value} API Documentation",
    "-project-footer", s"Copyright © $copyrightRange, ${organizationName.value}. All rights reserved.",
    "-project-logo", "FacsimileIcon.png",
    "-project-version", baseVersion(version.value),
    "-snippet-compiler:compile",
    s"-social-links:github::$gitURI",
  ),
)

/** Published project settings.
 *
 *  Published projects must define the artifacts to be published, and take care of publishing them to the _Sonatype
 *  Central_ repository. Consequently, there is a lot of _Maven_/_SBT_/_Ivy_ configuration information here.
 *
 *  Note that publishing artifacts to the _Sonatype Central_ repository ensures that those same artifacts are available
 *  through the _Maven Central Repository_.
 *
 *  NOTES:
 *  1. Test artifacts should NOT be published. This is disabled by the line `Test / publishArtifact := false` below.
 *  2. Third-party artifacts referenced by _Facsimile_ must be available from the _Maven Central Repository_.
 *  3. _Maven_ metadata that is not defined by _SBT_ properties must be defined in the `pomExtra` setting as XML.
 *  4. Artifacts must be signed via _GNU Privacy Guard_ (_GPG_) for verification and authenticity purposes. This is also
 *     essential for artifacts to be published to the _Sonatype Central_ repository; in this case, the software must be
 *     signed using the key for _authentication@facsim.org_. (If your version of _Facsimile_ is signed by a different
 *     key, then you do not have the official version.)
 *
 *  The _sbt-gpg_ plugin uses _GPG_ to sign artifacts, and this must be installed on the current machine. The key ID
 *  must be specified in a file configured by the Release Manager, typically "~/.sbt/2/Credentials.sbt". Finally, the
 *  key (including the private key) must be registered in _GPG_ on the local machine.
 *
 *  For best results, all releases and code release signing should be undertaken on a _Linux_ or _macOS_ system.
 *
 *  __WARNING: THE GPG SECRET KEY AND THE CREDENTIALS FILE MUST NEVER BE MADE PUBLIC AND SHOULD NEVER BE COMMITTED AS
 *  PART OF ANY SOURCES__.
 *
 *  We publish to the snapshots repository if this is a snapshot, or to the releases staging repository if this is an
 *  official release.
 */
lazy val publishedProjectSettings = Seq(

  // Identifier of the GPG key used to sign artifacts during publication.
  //
  // The identifier is for the "authentication@facsim.org" key, and it obtained by entering the command:
  //   gpg --list-keys
  pgpSigningKey := Some("57B8D8577B0926F23CBEBA0CC08B4D86EACCE720"),

  // Start year of this project.
  startYear := Some(facsimileStartDate.getYear),

  // All Facsimile libraries are published under the LGPL as specified below.
  licenses := Seq(
    "GNU Lesser General Public License version 3 (LGPLv3)" ->
    uri("http://www.gnu.org/licenses/lgpl-3.0-standalone.html")
  ),

  // Facsimile utilizes git for version control, hosted by GitHub.
  scmInfo := Some(
    ScmInfo(uri(gitURI), gitSCM, Some(gitSCM))
  ),

  // Test artifacts should not be published.
  Test / publishArtifact := false,

  // Developers. Add yourself here if you've contributed code the Facsimile project.
  //
  // Developer fields are: ID, name, email & URI.
  developers := List(
    Developer("mja", "Michael J Allen", "mike.allen@facsim.org", uri("http://facsim.org")),
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
  apiURL := organizationHomepage.value.map(h => uri(s"$h/Documentation/API/${version.value}")),

  // Manifest additions for the library jar file.
  //
  // The jar file should be sealed so that the packages contained cannot be extended. We also add inception & build
  // timestamps for information purposes.
  Compile / packageBin / packageOptions ++= Seq(

    // Standard manifest attributes.
    Package.ManifestAttributes(

      // *** Standard manifest attributes ***

      // The jar file should be sealed so that the packages contained withing it cannot be extended.
      Name.SEALED.toString -> "true",

      // Override the version of the specification to be just the base version.
      Name.SPECIFICATION_VERSION.toString -> baseVersion(version.value),

      // *** Custom attributes ***
      //
      // These are useful for documenting the conditions under which a build was made, as well as for providing useful

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
  // This differs from the standard sbt-release process in that we employ the sbt-sonatype plugin to publish the
  // project.
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

    // Update the "Version.sbt" file so that it contains the release version number.
    setReleaseVersion,

    // Commit and tag the release version.
    commitReleaseVersion,
    tagRelease,

    // Sign, publish and release artifacts to the Sonatype Central release repository.
    //
    // This requires publishTo defined.
    //
    // IMPORTANT: The following criteria are required to push to this artifact repository:
    // 1. SNAPSHOT releases cannot be published, only full releases.
    // 2. Sonatype Central (https://central.sonatype.com/) user tag used to authenticate user performing upload
    //    (populated in $HOME/.sbt/2/Credentials.sbt).
    // 3. Namespace (i.e. artifact organization, "org.facsim" in this case) must be identified and authorized as part of
    //    the user's Sonatype account. (Authorization is done through a Sonatype Central JIRA ticket.)
    // 4. Artifacts must be signed, in this case, by GPG key authentication@facsim.org (see above).
    // 5. Artifact POM file includes specification of Maven formatting, license definition, organization URI, SCM
    //    definition, and developer list. Many of these features can be entered into the pomExtra property.
    //
    // NOTE: If cross-publishing, use 'releaseStepCommandAndRemaining("+publishSigned")' in place of
    // 'releaseStepCommand("publishSigned")'.
    releaseStepCommand(s"sonatypeOpen \"${organization.value}\" \"Facsimile\""),
    releaseStepCommand("publishSigned"),
    releaseStepCommand("sonaRelease"),

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

/** Source project settings.
 *
 *  These settings are common to all projects that contain source files, which must be compiled and tested.
 *
 *  Any library dependencies listed here MUST be universal and not non-transitive.
 */
lazy val sourceProjectSettings = Seq(

  // Scala compiler options.
  //
  // Note: Coverage output information configurations is provided automatically by the Scoverage SBT plugin.
  Compile / scalacOptions := commonScalaCSettings ++ Seq(
    "-feature",
    "-indent",
    "-java-output-version:25",
    "-new-syntax",
    "-project-url", s"$gitURI",
    "-unchecked",
    "-uniqid",
    "-Werror", // Fail compilation if there are any errors.
    //"-Wnonunit-statement",
    "-Wsafe-init",
    "-Wunused:all", // Enable all warnings about unused elements (imports, privates, etc.).
    //"-Wvalue-discard", Disabled: too many false positives.
    "-Xverify-signatures",
    "-Yexplicit-nulls", // Don't allow reference types to be null.
  ),

  // Fork the tests, so that they run in a separate process. This improves test reliability.
  Test / fork := true,

  // As recommended by ScalaTest, disable buffered logs in Test, in order to use ScalaTest's built-in buffering. This
  // helps to present test results in a logical manner when tests are executed in parallel.
  Test / logBuffered := false,

  // Execute tests in parallel.
  //
  // Output makes more sense using the ScalaTest log buffering algorithm (enabled by disabling logBuffered for testing,
  // above).
  Test / parallelExecution := true,

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
  ScoverageKeys.coverageFailOnMinimum := true,
  ScoverageKeys.coverageMinimumStmtTotal := 75,
  ScoverageKeys.coverageMinimumBranchTotal := 75,
  ScoverageKeys.coverageMinimumStmtPerPackage := 75,
  ScoverageKeys.coverageMinimumBranchPerPackage := 75,
  ScoverageKeys.coverageMinimumStmtPerFile := 75,
  ScoverageKeys.coverageMinimumBranchPerFile := 75,

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

    // Scalactic is used by ScalaTest to support other features, such as floating point equality with tolerances.
    "org.scalactic" %% "scalactic" % ScalaTestVersion,

    // ScalaTest plus ScalaCheck, property-based testing library dependencies. This is used by ScalaTest.
    //
    // Note: This adds ScalaCheck as a test dependency, so it is no longer necessary to add it as a separate dependency.
    "org.scalatestplus" %% s"scalacheck-$ScalaCheckVersion" % s"$ScalaTestVersion.0" % Test,
  ),
)

/** Settings for all projects that should not publish artifacts to the _Sonatype Central_ repository.
 */
lazy val unpublishedProjectSettings = Seq(

  // Ensure that the current project does not publish any of its artifacts.
  publishArtifact := false, // <- Is this still needed?
  publish / skip := true,
)

/** Name of the _Facsimile Utility_ library subproject.
 */
val FacsimileUtilName = "facsimile-util"

/** _Facsimile Utility_ library subproject.
 *
 *  This library contains common utility code that is utilized by other _Facsimile_ libraries, as well as third-party
 *  projects.
 */
lazy val facsimileUtil = project.in(file(FacsimileUtilName))
.settings(sourceProjectSettings*)
.settings(docProjectSettings*)
.settings(publishedProjectSettings*)
.settings(

  // Name and description of this project.
  name := "Facsimile Utility Library",
  normalizedName := FacsimileUtilName,
  description := """The Facsimile Utility library provides a number of utilities required by other Facsimile libraries
  |as well as third-party libraries.""".stripMargin.replaceAll("\n", " "),

  // Provide source links in the documentation files.
  //
  // This must match a specific pattern in order to be utilized by Scaladoc.
  Compile / doc / scalacOptions ++= Seq(
    s"-source-links:${normalizedName.value}=$gitSrcTemplate/v${version.value}#${normalizedName.value}/",
  ),

  // Utility library dependencies.
  libraryDependencies ++= Seq(

    // Pekko streams library & testkit (the latter scoped for testing only).
    //
    // This is used for creating streams of data.
    //
    // Pekko is a fork of Akka, which switched to a restrictive, proprietary license, thereby making it unsuitable for
    // use by the Facsimile project.
    "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
    "org.apache.pekko" %% "pekko-stream-testkit" % PekkoVersion % Test,
  ),

  // Help the test code find the test JAR files that we use to verify JAR file manifests.
  Test / unmanagedBase := baseDirectory.value / "src/test/lib",
)

/** Name of the _Facsimile Collection_ library subproject.
 */
val FacsimileCollectionName = "facsimile-collection"

/** _Facsimile Collection_ library subproject.
 *
 *  This library contains custom, immutable collections that are utilized by other _Facsimile_ libraries.
 */
lazy val facsimileCollection = project.in(file(FacsimileCollectionName))
.dependsOn(facsimileUtil % dependsOnCompileTest)
.settings(sourceProjectSettings*)
.settings(docProjectSettings*)
.settings(publishedProjectSettings*)
.settings(

  // Name and description of this project.
  name := "Facsimile Collection Library",
  normalizedName := FacsimileCollectionName,
  description := """The Facsimile Collection library provides custom, immutable collections that are required by other
  |Facsimile libraries as well as third-party libraries.""".stripMargin.replaceAll("\n", " "),

  // Provide source links in the documentation files.
  Compile / doc / scalacOptions ++= Seq(
    s"-source-links:${normalizedName.value}=$gitSrcTemplate/v${version.value}#${normalizedName.value}/",
  ),

  // Required libraries.
  libraryDependencies ++= Seq(

    // The izumi-reflect library provides run-time access to generic type.
    "dev.zio" %% "izumi-reflect" % IzumiReflectVersion
  ),
)

/** Name of the _Facsimile Simulation_ library subproject.
 */
val FacsimileSimulationName = "facsimile-simulation"

/** _Facsimile Simulation_ library subproject.
 *
 *  This library provides a purely functional simulation engine for running simulations.
 */
lazy val facsimileSimulation = project.in(file(FacsimileSimulationName))
.dependsOn(facsimileCollection % dependsOnCompileTest)
.settings(sourceProjectSettings*)
.settings(docProjectSettings*)
.settings(publishedProjectSettings*)
.settings(

  // Name and description of this project.
  name := "Facsimile Simulation Library",
  normalizedName := FacsimileSimulationName,
  description := """The Facsimile Simulation library is a purely functional, discrete-event simulation engine. It is the
  |beating heart at the center of all Facsimile simulation models.""".stripMargin.replaceAll("\n", " "),

  // Provide source links in the documentation files.
  Compile / doc / scalacOptions ++= Seq(
    s"-source-links:${normalizedName.value}=$gitSrcTemplate/v${version.value}#${normalizedName.value}/",
  ),

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

/** Facsimile _root_ project.
 *
 *  This project simply aggregates actions across all _Facsimile_ subprojects.
 */
// TODO: Merge all documentation for sub-projects and publish it to the Facsimile web-site/elsewhere.
lazy val facsimile = project.in(file("."))
.aggregate(facsimileUtil, facsimileCollection, facsimileSimulation)
.enablePlugins(ScalaUnidocPlugin)
.settings(unpublishedProjectSettings*)
.settings(

  // Name and description of this project.
  name := "Facsimile",
  normalizedName := "facsimile",
  description := """The Facsimile project's goal is to develop and maintain a high-quality, 3D, discrete-event
  |simulation library that can be used for industrial simulation projects in an engineering and/or manufacturing
  |environment. Facsimile simulations run on Microsoft Windows as well as on Linux, Mac OS, BSD and Unix on the Java
  |virtual machine.""".stripMargin.replaceAll("\n", " "),
)
