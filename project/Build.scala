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
import com.typesafe.sbt.pgp.PgpKeys
import com.typesafe.sbteclipse.plugin.EclipsePlugin._
import org.scalastyle.sbt.ScalastylePlugin

//=============================================================================
/**
Facsimile SBT Build object.

@since 0.0
 */
//=============================================================================

object FacsimileBuild
extends Build {

/**
Required short scala version.

This includes just the major and minor version number of scala, for projects
that are cross built against this information (such as ScalaTest).

@note The version specified must be compatible with all third-party Scala
libraries utilized by Facsimile.
*/

  val scalaVersionShort = "2.10"

/**
Required long scala version.

This is the full scala version required to compile the sources. Typically, it
is the most recent point release of the required short version.
*/

  val scalaVersionLong = scalaVersionShort + ".3"

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
Project version number.

We employ the sbt-git plugin to uniquely version each commit, so that we can
support '''`git bisect`'''. To guarantee support, even if git isn't installed
(which raises interesting questions about how the source was obtained), we use
JGit to handle git commands.

For further information, refer to Josh Suereth's presentation on "Effective
SBT" at Scala Days 2013:

[[http://www.parleys.com/play/51c3790ae4b0d38b54f46259 Effective SBT]]

Note: In the settings below, the assignment of the baseVersion value must
follow the instruction to versionWithGit.
*/

  val projectBaseVersion = "0.0"
  override val settings: Seq [Setting [_]] =
  super.settings ++ SbtGit.useJGit ++ SbtGit.versionWithGit ++
  (SbtGit.git.baseVersion := projectBaseVersion)

/**
Project name.

Human readable short form of the project's name.

@note This is equivalent to the `project/name` Maven POM information field, and
is employed as such during deployment to the Sonatype OSS Nexus repository.
*/

  val projectName = "Facsimile Simulation Library"

/**
Project description.

Brief description of the project, its purpose and aims.

@note This is equivalent to the `project/description` Maven POM information
field, and is employed as such during deployment to the Sonatype OSS Nexus
repository.
*/

  val projectDescription = """
    The Facsimile project's goal is to develop and maintain a high-quality, 3D,
    discrete-event simulation library that can be used for industrial
    simulation projects in an engineering and/or manufacturing environment.

    Facsimile simulations run on Microsoft Windows as well as on Linux, Mac OS,
    BSD and Unix on the Java virtual machine.
  """

/**
Project homepage.

@note This is equivalent to the `project/url` Maven POM information field, and
is employed as such during development to the Sonatype OSS Nexus repository.
*/

  val projectHomepage = Some (url ("http://facsim.org/"))

/**
Project start year.

@note This is equivalent to the `project/inceptionYear` Maven POM information
field, and is employed as such during development to the Sonatype OSS Nexus
repository.
*/

  val projectStartYear = Some (2004)

/**
Project organization name.

@note This is equivalent to the `project/organization/name` Maven POM field,
and is employed as such during development to the Sonatype OSS Nexus
repository.
*/

  val projectOrganizationName = "Michael J. Allen"

/**
Project organization URL.

@note This is equivalent to the `project/organization/url` Maven POM field, and
is employed as such during development to the Sonatype OSS Nexus repository.
*/

  val projectOrganizationHomepage = projectHomepage

/**
Project licenses.

@note This is equivalent to the `project/licenses` Maven POM fields, and is
employed as such during development to the Sonatype OSS Nexus repository.
*/

  val projectLicenses =
  Seq ("GNU Lesser General Public License version 3 (LGPLv3)" ->
  url ("http://www.gnu.org/licenses/lgpl-3.0-standalone.html"))

/**
Source code management repository for this project.

@note This is equivalent to the `project/scm` Maven POM fields, and is employed
as such during development to the Sonatype OSS Nexus repository.
* */

  val projectScmInfo = Some (
    ScmInfo (
      url ("https://github.com/Facsimile/facsimile"),
      "scm:git:https://github.com/Facsimile/facsimile.git",
      Some ("scm:git:https://github.com/Facsimile/facsimile.git")
    )
  )

/**
Base dependencies.

These libraries are common to all sub-projects.  In particular, note that the
Macro sub-project has very few dependencies.
*/

  def baseDependencies = Seq (

/*
Required scala standard libraries.
*/

    "org.scala-lang" % "scala-reflect" % scalaVersionLong,

/*
ScalaTest unit-testing framework for Scala.
*/

    "org.scalatest" %% "scalatest" % "2.1.0" % "test"
  )

/**
Common library dependencies.
*/

  def commonDependencies = baseDependencies ++ Seq (

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

/**
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

  val projectScalacOptions = Seq (
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
  )

/**
Scaladoc options.

These should be scoped Compile,doc (for primary sources) or (Test,doc) for test
sources.
*/

  val projectScaladocOptions = Seq (
    "-diagrams",
    "-doc-footer",
    "Copyright © 2004-2014, Michael J Allen. All rights reserved.",
    "-doc-format:html",
    "-doc-title",
    projectName + " API Documentation",
    "-doc-version",
    projectBaseVersion,
    "-groups",
    "-implicits"
  )

/**
Default settings.

These settings are common to all projects.
*/

  lazy val defaultSettings = Defaults.defaultSettings ++ Seq (

/*
Scala configuration.
*/

    scalaVersion := scalaVersionLong
  )

/**
Source project settings.

Projects and sub-projects with source files have these settings, which include
the default settings and Scalastyle.
*/

  lazy val sourceSettings = defaultSettings ++ ScalastylePlugin.Settings ++
  Seq (

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
*/

    scalacOptions := projectScalacOptions,

/*
Scaladoc configuration.
*/

    scalacOptions in (Compile, doc) ++= projectScaladocOptions,
    autoAPIMappings := true,
    apiMappings += (
      unmanagedBase.value / "jt.jar" ->
      url ("http://download.java.net/jdk8/docs/api/")
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
    EclipseCreateSrc.Resource
  )

/**
Primary Facsimile root project.

This "project" contains no sources, but owns - and is dependant upon - the
others.
*/

  lazy val facsimile = Project (projectArtifactId, file ("."),
  settings = defaultSettings ++ Seq (

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
    description := projectDescription,
    homepage := projectHomepage,
    startYear := projectStartYear,
    organizationName := projectOrganizationName,
    organizationHomepage := projectOrganizationHomepage,
    licenses := projectLicenses,
    scmInfo := projectScmInfo,
    publishMavenStyle := true,
    publishTo <<= (version) {
      version: String =>
      val nexus = "https://oss.sonatype.org/"
      val (name, u) =
      if (version.contains ("-SNAPSHOT")) {
        ("snapshots", nexus + "content/repositories/snapshots")
      }
      else ("releases", nexus + "service/local/staging/deploy/maven2")
      Some(Resolver.url (name, url (u))(Resolver.ivyStylePatterns))
    },
    publishArtifact in Test := false,
    pomIncludeRepository := {_ => false},
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
      <issueManagement>
        <system>GitHub Issues</system>
        <url>https://github.com/Facsimile/facsimile/issues</url>
      </issueManagement>
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
Ensure that core and macro classes, sources and documentation are copied to the
corresponding distribution jar files.
*/

    mappings in (Compile, packageBin) ++= mappings.in (macros, Compile,
    packageBin).value,
    mappings in (Compile, packageBin) ++= mappings.in (core, Compile,
    packageBin).value,
    mappings in (Compile, packageSrc) ++= mappings.in (macros, Compile,
    packageSrc).value,
    mappings in (Compile, packageSrc) ++= mappings.in (core, Compile,
    packageSrc).value,
    mappings in (Compile, packageDoc) ++= mappings.in (macros, Compile,
    packageDoc).value,
    mappings in (Compile, packageDoc) ++= mappings.in (core, Compile,
    packageDoc).value,

/*
SBT-GPG plugin configuration.

Specify the key to be used to sign the distribted jar file.

NOTE: The full hex ID of the private key is obtained by using the command:

  gpg --keyid-format 0xLONG -k {key-id}

where {key-id} is the short key ID - which ought to match the last 8 characters
of the long key ID.
*/

    PgpKeys.pgpSigningKey := Some (0xAC65F306C9B7223FL)
  )).aggregate (core, macros)

/**
Core project.

This is the sub-project containing the core library.

This library is dependant upon the Macro sub-project.  (Scala currently
requires that macro implementation code is compiled before code that uses the
macro implementation is compiled.)
*/

  lazy val core = Project (projectArtifactId + "-core", file ("core"),
  settings = sourceSettings ++ Seq (

/*
Library dependencies.
*/

    libraryDependencies ++= commonDependencies,

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
macro implementation is compiled.
*/

  lazy val macros = Project (projectArtifactId + "-macro", file ("macro"),
  settings = sourceSettings ++ Seq (

/*
Macro dependencies.
*/

    libraryDependencies ++= baseDependencies,

/*
Basic package information.
*/

    normalizedName := projectArtifactId + "-macro",
    organization := projectGroupId,
    name := projectName + " Macros"
  ))
}
