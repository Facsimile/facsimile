/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

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
Scala source file used as the Simple Build Tool (SBT) build file.

This file controls how the Facsimile library (and its associated tools) are
built and distributed.

NOTE: This file is compiled using the version of Scala associated with the SBT
version (specified in project/build.properties), which is not necessarily the
same version of Scala that is used to build Facsimile (specified here).
*/
//=============================================================================

//import java.io.File
//import java.net.URL
import sbt._
import Keys._
import com.typesafe.sbt.pgp.PgpKeys._

//=============================================================================
/**
Facsimile SBT Build object.

@since 0.0
 */
//=============================================================================

object FacsimileBuild extends Build {

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

  val scalaVersionLong = scalaVersionShort + ".2"

/**
Project artifact ID.

@note This is equivalent to the `project/artifactId` Maven POM co-ordinate, and
is employed as such during deployment to the Sonatype OSS Nexus repository.
*/

  val projectArtifactId = "facsimile"

/**
Project group ID.

@note This is equivalent to the `project/groupId` Maven POM co-ordinate, and is
employed as such during deployment to the Sonatype OSS Nexus repository.
*/

  val projectGroupId = "org.facsim"

/**
Project version number.

A suffix of "-SNAPSHOT" indicates that this is a snapshot&mdash;an unstable,
un-versioned build that preceeds a formal release, the version of which is the
prefix. For example, a version number of 1.0-SNAPSHOT is a temporary build
leading up to the release of version 1.0.

@note This is equivalent to the `project/version` Maven POM co-ordinate, has
the same numbering system, and is employed as such during deployment to the
Sonatype OSS Nexus repository. Refer to Maven version numbering documentation
for further information.
*/

  val projectVersion = "0.0-SNAPSHOT"

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
Sonatype OSS repository to publish to.

We publish to the snapshots repository, if this is a snapshot, or to the
releases staging repository if this is an official release.
*/

  val sonatypeRepository = {
    val nexus = "https://oss.sonatype.org/"
    if (projectVersion.endsWith ("-SNAPSHOT")) {
      Some ("snapshots" at nexus + "content/repositories/snapshots")
    }
    else Some ("releases"  at nexus + "service/local/staging/deploy/maven2")
  }

/**
Common library dependencies.
*/

  def commonDependencies = Seq (

/*
Required scala standard libraries.
*/

    "org.scala-lang" % "scala-swing" % (scalaVersionLong),
    "org.scala-lang" % "scala-reflect" % (scalaVersionLong),

/*
Joda Time library for processing dates & times accurately.
*/

    "joda-time" % "joda-time" % "2.2",

/*
Joda Time Convert library for conversion of Joda dates & times to Java dates &
times.
*/

    "org.joda" % "joda-convert" % "1.3.1",

/*
Java 3D libraries.

This is a temporary arrangement for now. When JavaFX 8 becomes available, we'll
switch to using that for 3D graphics; Java3D is a dead system, and no releases
above 1.3.1 are available in any repository.
*/
    "java3d" % "j3d-core" % "1.3.1",
    "java3d" % "j3d-core-utils" % "1.3.1",
    "java3d" % "vecmath" % "1.3.1",

/*
ScalaTest unit-testing framework for Scala.
*/

    "org.scalatest" % ("scalatest_" + scalaVersionShort) % "2.0.M5b" % "test"
  )

/**
Scala compiler options.
*/

  val projectScalacOptions = Seq (
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-explaintypes",
    "-g:vars",
    "-target:jvm-1.6",
    "-unchecked",
    "-Xcheckinit",
    //"-Xcheck-null",
    "-Xfatal-warnings",
    "-Xlint",
    "-Ynotnull",
    "-Ywarn-all"
  )

/**
Primary Facsimile library build.
*/

  lazy val facsimile = Project (projectArtifactId, file ("."))
  .settings (

/*
Scala configuration.
*/

    scalaVersion := scalaVersionLong,

/*
Scala compiler options.
*/

    scalacOptions := projectScalacOptions,

/*
Library dependencies.
*/

    libraryDependencies ++= commonDependencies,

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
*/

    normalizedName := projectArtifactId,
    organization := projectGroupId,
    version := projectVersion,
    name := projectName,
    description := projectDescription,
    homepage := projectHomepage,
    startYear := projectStartYear,
    organizationName := projectOrganizationName,
    organizationHomepage := projectOrganizationHomepage,
    licenses := projectLicenses,
    scmInfo := projectScmInfo,
    publishMavenStyle := true,
    publishTo := sonatypeRepository,
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
Ensure that macro classes, sources and documentation are copied to the
corresponding distribution jar file.
*/

    mappings in (Compile, packageBin) ++= mappings.in (macros, Compile,
    packageBin).value,
    mappings in (Compile, packageSrc) ++= mappings.in (macros, Compile,
    packageSrc).value,
    mappings in (Compile, packageDoc) ++= mappings.in (macros, Compile,
    packageDoc).value,

/*
SBT-GPG plugin configuration.

Specify the key to be used to sign the distribted jar file.
*/

    pgpSigningKey := Some (0x3D700BBB797D614CL) // Public key
  ).dependsOn (macros)

/**
Macro project.

This is a sub-project that the primary project depends upon. (Scala currently
requires that macro implementation code is compiled before code that uses the
macro implementation is compiled.
*/

  lazy val macros = Project (projectArtifactId + "-macro", file ("macro"))
  .settings (

/*
Scala configuration.
*/

    scalaVersion := scalaVersionLong,

/*
Scala compiler options.
*/

    scalacOptions := projectScalacOptions,

/*
Macro dependencies.
*/

    libraryDependencies ++= commonDependencies,

/*
Basic package information.
*/

    normalizedName := projectArtifactId + "-macro",
    organization := projectGroupId,
    version := projectVersion,
    name := projectName + " Macros"
  )
}
