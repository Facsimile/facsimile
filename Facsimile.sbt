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
// SBT build configuration specific to the main Facsimile project.
//======================================================================================================================

import FacsimileBuild._

//scalastyle:off scaladoc
// ID of the main Facsimile project.
//
// This is the "normalized name" of this project, also known as a Maven/SBT/Ivy artifact name (such as "myfabapp"). This
// must also match the name of the project on GitHub.

facsimileProjId in Global := "facsimile"

// Name of the main Facsimile project.
//
// Human-readable phrase that describes (in a shortened form) this project (such as "My Fabulous App").
//
// This is equivalent to the "name" Maven POM information field, and is employed as such during deployment to the
// Sonatype OSS Nexus repository. It is also used by SBT for naming each project and sub-project.

facsimileProjName in Global := "Facsimile Simulation Library"

// Description of the main Facsimile project.
//
// This is a detailed description for this project.

facsimileProjDesc in Global := """
  The Facsimile project's goal is to develop and maintain a high-quality, 3D, discrete-event simulation library that can
  be used for industrial simulation projects in an engineering and/or manufacturing environment.

  Facsimile simulations run on Microsoft Windows as well as on Linux, Mac OS, BSD and Unix on the Java virtual machine.
"""

// Home page of the main Facsimile project.

facsimileProjHomePage in Global := "http://facsim.org/"

// Facsimile root project.

lazy val root = project.in(file(".")).
settings(commonSettings: _*).
settings(rootSettings: _*).
settings(rootProjectUnidocSettings: _*).
settings(

  // Ensure that core and macro classes and sources are copied to the corresponding distribution jar files.

  mappings in(Compile, packageBin) ++= mappings.in(macros, Compile, packageBin).value,
  mappings in(Compile, packageBin) ++= mappings.in(core, Compile, packageBin).value,
  mappings in(Compile, packageSrc) ++= mappings.in(macros, Compile, packageSrc).value,
  mappings in(Compile, packageSrc) ++= mappings.in(core, Compile, packageSrc).value
).
aggregate(core, macros)

// Facsimile core sub-project.
//
// This sub-project defines the bulk of the Facsimile simulation library.

lazy val core = project.in(file("core")).
settings(commonSettings: _*).
settings(baseSourceSettings: _*).
settings(subProjectSourceSettings: _*).
settings(

  // Define the ID and name of this sub-project.

  normalizedName := facsimileProjId.value + "-core",
  name := facsimileProjName.value + " Core",

  // Add library dependencies for the core sub-project.

  libraryDependencies ++= Seq(

    // ScalaFX libraries, for user-interface design and 3D animation.
    //
    // This library is being phased-out in favor of the Facsimile sfx library.

    "org.scalafx" %% "scalafx" % "8.0.40-R8"
  )
).
dependsOn(macros % "test->test;compile->compile")

// Facsimile macros sub-project.
//
// This sub-project defines macros that are required by the core sub-project.

lazy val macros = project.in(file("macro")).
settings(commonSettings: _*).
settings(baseSourceSettings: _*).
settings(subProjectSourceSettings: _*).
settings(

  // Define the ID and name of this sub-project.

  normalizedName := facsimileProjId.value + "-macro",
  name := facsimileProjName.value + " Macros",

  // Add library dependencies for the macros sub-project.

  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )
)
//scalastyle:on scaladoc