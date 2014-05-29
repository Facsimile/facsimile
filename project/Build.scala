/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file used as the Simple Build Tool (SBT) build file.

This file controls how the Facsimile library (and its associated tools) are
built and distributed.

NOTE: This file is compiled using the version of Scala associated with the SBT
version (specified in project/build.properties), which is not necessarily the
same version of Scala that is used to build Facsimile (specified here).
*/
//=============================================================================

import sbt._
import Keys._
import com.typesafe.sbt.SbtGit
import com.typesafe.sbteclipse.plugin.EclipsePlugin._
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.jar.Attributes.Name
import org.scalastyle.sbt.ScalastylePlugin
import sbtunidoc.Plugin.{unidocSettings, ScalaUnidoc, UnidocKeys}
import xerial.sbt.Sonatype._

//=============================================================================
/**
Facsimile SBT Build object.

@since 0.0
 */
//=============================================================================

object FacsimileBuild
extends Build {

/**
Project artifact ID.

@note This is equivalent to the `project/artifactId` Maven POM coordinate, and
is employed as such during deployment to the Sonatype OSS Nexus repository.
*/

  val projectArtifactId = "facsimile"

/**
Project group ID.

@note This is equivalent to the `project/groupId` Maven POM coordinate, and is
employed as such during deployment to the Sonatype OSS Nexus repository.
*/

  val projectGroupId = "org.facsim"

/**
Project base version number.

We employ the sbt-git plugin to uniquely version each commit, so that we can
support '''`git bisect`'''. To guarantee support, even if git isn't installed
(which raises interesting questions about how the source was obtained), we use
JGit to handle git commands.

For further information, refer to Josh Suereth's presentation on "Effective
SBT" at Scala Days 2013:

[[http://www.parleys.com/play/51c3790ae4b0d38b54f46259 Effective SBT]]
*/

  val projectBaseVersion = "0.0"

/**
Project name.

Human readable short form of the project's name.

@note This is equivalent to the `project/name` Maven POM information field, and
is employed as such during deployment to the Sonatype OSS Nexus repository.
*/

  val projectName = "Facsimile Simulation Library"

/**
Project homepage.

@note This is equivalent to the `project/url` Maven POM information field, and
is employed as such during development to the Sonatype OSS Nexus repository.
*/

  val projectHomepage = Some (url ("http://facsim.org/"))

/**
Default settings.

These settings are common to all projects.

Note that we implement git versioning for artifacts (see note on
projectBaseVersion above). Note: In the settings below, the assignment of the
baseVersion value must follow the instruction to versionWithGit.

NOTE: Previously, it was necessary to inherit from Defaults.defaultSettings,
but this has been deprecated SBT as of 0.13.2. Since that release, it appears
that default settings are automatically provided.
*/

  lazy val defaultSettings = super.settings ++ SbtGit.useJGit ++
  SbtGit.versionWithGit ++ (SbtGit.git.baseVersion:= projectBaseVersion) ++
  Seq (

/*
Scala cross compiling.
*/

    crossScalaVersions := Seq ("2.10.4", "2.11.1"),

/*
Scala configuration.
*/

    scalaVersion <<= crossScalaVersions {
      versions => versions.head
    }
  )

/**
Base settings for source projects.

These settings are common to all sub-projects that contain Scala sources. In
particular, note that the Macro sub-project has very few dependencies.
*/

  lazy val baseSourceSettings = defaultSettings ++ ScalastylePlugin.Settings ++
  Seq (

/*
Ensure that we only publish/package the root project and source subprojects.
This assumes that all projects implementing these settings are subprojects,
rather than primary projects.
*/

    publishArtifact := false,

/*
Even though we're not publishing anything, it appears that we still need to
specify a location to publish to to prevent unnecessary output.
*/

    publishTo := Some (Resolver.file ("Unpublished files",
    file ("target/unusedrepo"))),

/*
Ensure that doc only operates for the root project (using Unidoc) and not any
subprojects.
*/

    sources in doc in Compile := List(),

/*
Exclude Java source directories (use Scala source directories only).

NOTE: If this is not done, the sbteclipse plug-in creates src/main/java and
/src/test/java directories.  If we ever need to add Java sources to the
project, they'll go here.
*/

    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)( _ ::
    Nil),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)( _ :: Nil),

/*
Scala compiler options.

This is a conundrum: -Xfatal-warnings is essential, since it forces all
warnings to be addressed. However, when -optimize is specified, Scala will
generate some inline warnings when initializing large maps (such as the cell
code to cell type map in org.facsim.anim.cell.CellScene). Although the inline
warnings themselves will only be issued when -Yinline-warnings is specified,
Scala will still emit a warning that inline warnings occurred, which is then
treated as fatal by Xfatal-warnings. It seems that the only way around this,
right now, is to disable -optimize. This must be reviewed when newer Scala
releases become available.
*/

    scalacOptions := Seq (
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-g:vars",
      //"-optimize",
      "-target:jvm-1.7",
      "-unchecked",
      "-Xcheckinit",
      //"-Xcheck-null",
      "-Xfatal-warnings",
      "-Xlint",
      //"-Yinline-warnings",
      "-Ynotnull",
      "-Ywarn-all"
    ),

/*
Make sure that tests execute in sequence (we may change this in future, but,
for now, it's a lot easier to understand test output if tests execute
sequentially.
*/

    parallelExecution in Test := false,

/*
SBT-Eclipse plugin configuration.
*/

    EclipseKeys.useProjectId := false,
    EclipseKeys.createSrc := EclipseCreateSrc.Default +
    EclipseCreateSrc.Resource,

/*
Required scala standard libraries.

Scala 2.11 introduced a more modular library structure. This section allows
dependencies to be specified by supported Scala version.
*/

    libraryDependencies := {
      CrossVersion.partialVersion (scalaVersion.value) match {

/*
Scala 2.11+ dependencies.

The scala-xml package is currently only required by Scalatest, hence the "test"
scope.
*/

        case Some ((2, scalaMajor)) if scalaMajor >= 11 =>
        libraryDependencies.value :+ "org.scala-lang.modules" %%
        "scala-reflect" % scalaVersion.value
        libraryDependencies.value :+ "org.scala-lang.modules" %% "scala-xml" %
        "1.0.2" % "test"

/*
Scala 2.10 dependencies.
*/

        case _ =>
        libraryDependencies.value :+ "org.scala-lang" % "scala-reflect" %
        scalaVersion.value
      }
    },

/*
Other base library dependencies.
*/

    libraryDependencies ++= Seq (
      "org.scalatest" %% "scalatest" % "2.1.6" % "test"
    )
  )

/**
Common source settings.
*/

  lazy val commonSourceSettings = baseSourceSettings ++ Seq (

/*
Additional library dependencies.
*/

    libraryDependencies ++= Seq (

/*
ScalaFX libraries, for user-interface design and 3D animation.
*/

      "org.scalafx" %% "scalafx" % "8.0.0-R5-SNAPSHOT",

/*
Joda Time library for processing dates & times accurately.
*/

      "joda-time" % "joda-time" % "2.2",

/*
Joda Time Convert library for conversion of Joda dates & times to Java dates &
times.
*/

      "org.joda" % "joda-convert" % "1.3.1"
    )
  )

/**
Customize the Unidoc settings.

This ensures that the "doc" command executes "unidoc", and changes the output
directory to "/api" from "/unidoc" so that the generated documentation is
included in packaging & publishing.
*/

  lazy val customUnidocSettings = unidocSettings ++ Seq (
    doc in Compile := (doc in ScalaUnidoc).value,
    target in UnidocKeys.unidoc in ScalaUnidoc := crossTarget.value / "api"
  )

/**
Primary Facsimile root project.

This "project" contains no sources, but owns - and is dependant upon - the
others. It also creates documentation for all sub-documents by employing
Unidoc.
*/

  lazy val facsimile = Project (projectArtifactId, file ("."), settings =
  defaultSettings ++ sonatypeSettings ++ customUnidocSettings ++ Seq (

/*
Maven POM (project object model) metadata.

This information is required in order to deploy the project on the Sonatype OSS
Nexus repository, which ensures that the project is synchronized with the Maven
Central Repository.

NOTES:
1.  Test artifacts should NOT be published. This is disabled by the line
    "publishArtifact in Test := false" below.
2.  Third-party artifacts referenced by Facsimile that are not available in the
    Maven Central Repository should not be listed in a project's POM file. If
    such artifacts are required, then uncomment the "pomIncludeRepository"
    setting so that the repositories from which they're referenced are ignored.
3.  Maven metadata that is not defined by SBT properties must be defined in the
    "pomExtra" setting as XML.
4.  Artifacts must be signed via GPG to be published to the Sonatype OSS Nexus.
    For security reasons (to prevent signing of unauthorized), this must be
    configured locally on each release manager's machine. In this case, the
    software must be signed using the key for "software@facsim.org" - with
    public key "797D614C". (If your version of Facsimile is signed by a
    different key, then you do not have the official version.)

We publish to the snapshots repository, if this is a snapshot, or to the
releases staging repository if this is an official release (or a committed
version - which seems wrong, right now).
*/

    normalizedName := projectArtifactId,
    organization := projectGroupId,
    name := projectName,
    description := """
      The Facsimile project's goal is to develop and maintain a high-quality,
      3D, discrete-event simulation library that can be used for industrial
      simulation projects in an engineering and/or manufacturing environment.

      Facsimile simulations run on Microsoft Windows as well as on Linux, Mac
      OS, BSD and Unix on the Java virtual machine.
    """,
    homepage := projectHomepage,
    startYear := Some (2004),
    organizationName := "Michael J. Allen",
    organizationHomepage := projectHomepage,
    licenses := Seq (
      "GNU Lesser General Public License version 3 (LGPLv3)" ->
      url ("http://www.gnu.org/licenses/lgpl-3.0-standalone.html")
    ),
    scmInfo := Some (
      ScmInfo (
        url ("https://github.com/Facsimile/facsimile"),
        "scm:git:https://github.com/Facsimile/facsimile.git",
        Some ("scm:git:https://github.com/Facsimile/facsimile.git")
      )
    ),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := {
      _ => false
    },
    pomExtra := (
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
      </issueManagement>
    ),

/*
Scaladoc configuration.

The -Ymacro-no-expand prevents macro definitions from being expanded in macro
sub-classes.
*/

    scalacOptions in (ScalaUnidoc, UnidocKeys.unidoc) := Seq (
      "-diagrams",
      "-doc-footer",
      "Copyright © 2004-2014, Michael J Allen. All rights reserved.",
      "-doc-format:html",
      "-doc-title",
      projectName + " API Documentation",
      "-doc-version",
      projectBaseVersion,
      "-groups",
      "-implicits",
      "-Ymacro-no-expand"
    ),
    autoAPIMappings := true,
    apiMappings += (
      unmanagedBase.value / "jt.jar" ->
      url ("http://docs.oracle.com/javase/8/docs/api/")
    ),

/*
Tell any dependent projects where to find published Facsimile API documentation
to link to (via autoAPIMappings).

This link will be published in the project's Maven POM file.

Note: This documentation is versioned so that links will always be to the
version of Facsimile in use by the dependent project.
*/

    apiURL := Some (url ("http://facsim.org/Documentation/API/" +
    projectBaseVersion)),

/*
Manifest additions for the main library jar file.

The jar file should be sealed so that the org.facsim packages cannot be
extended. We also add a timestamp for eporting purposes.
*/

    packageOptions in (Compile, packageBin) +=
    Package.ManifestAttributes (Name.SEALED -> "true"),
    packageOptions in (Compile, packageBin) +=
    Package.ManifestAttributes ("Build-Timestamp" ->
    LocalDateTime.now (ZoneOffset.UTC).toString),

/*
Ensure that core and macro classes and sources are copied to the corresponding
distribution jar files.
*/

    mappings in (Compile, packageBin) ++= mappings.in (macros, Compile,
    packageBin).value,
    mappings in (Compile, packageBin) ++= mappings.in (core, Compile,
    packageBin).value,
    mappings in (Compile, packageSrc) ++= mappings.in (macros, Compile,
    packageSrc).value,
    mappings in (Compile, packageSrc) ++= mappings.in (core, Compile,
    packageSrc).value
  )).aggregate (core, macros)

/**
Core project.

This is the sub-project containing the core library.

This library is dependant upon the Macro sub-project.  (Scala currently
requires that macro implementation code is compiled before code that uses the
macro implementation is compiled.)
*/

  lazy val core = Project (projectArtifactId + "-core", file ("core"),
  settings = commonSourceSettings ++ Seq (

/*
Basic package information.
*/

    normalizedName := projectArtifactId + "-core",
    organization := projectGroupId,
    name := projectName + " Core"
  )).dependsOn (macros % "test->test;compile->compile")

/**
Macro project.

This is a sub-project that the primary project depends upon. (Scala currently
requires that macro implementation code is compiled before code that uses the
macro implementation is compiled.)
*/

  lazy val macros = Project (projectArtifactId + "-macro", file ("macro"),
  settings = baseSourceSettings ++ Seq (

/*
Basic package information.
*/

    normalizedName := projectArtifactId + "-macro",
    organization := projectGroupId,
    name := projectName + " Macros"
  ))
}
