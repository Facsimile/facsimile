# _Facsimile_ Project

The _Facsimile_ project's goal is to develop and maintain a high-quality set of libraries supporting the development of
industrial _[discrete-event](https://en.wikipedia.org/wiki/Discrete-event_simulation)_, _physics-based emulation_, and
live-streamed _[process digital twin](https://en.wikipedia.org/wiki/Digital_twin)_ simulations in an engineering,
logistics and/or manufacturing environment.

All libraries are developed using a hybrid _functional programming_ (_FP_)/_object-oriented programming_ (_OOP_)
paradigm, and are developed in the _[Scala](https://scala-land.org)_ programming language. The resulting libraries run
on the _Java Virtual Machine_ (_JVM_) under _Microsoft Windows_, _Linux_, _macOS_, _BSD_, and _Unix_.

_Facsimile_ is open-source/free software and is distributed under version 3 of the
_[GNU General Public License](http://www.gnu.org/licenses/lgpl-3.0-standalone.html)_ (_GPLv3_).

Current status of official master _Facsimile_ branch:

_*Note*: The test coverage badge, which is provided by [Coveralls](https://coveralls.io/), is not currently being
updated because the
[sbt-coveralls SBT](https://github.com/scoverage/sbt-coverallshttps://github.com/scoverage/sbt-coveralls) plugin has
been temporarily disabled; this is due to the plugin not supporting SBT 2. We will re-enable it when SBT 2 support is
available._

[![Maven Central](https://maven-badges.sml.io/sonatype-central/org.facsim/facsimile-simulation_3/badge.svg)](https://maven-badges.sml.io/sonatype-central/org.facsim/facsimile-simulation_3)
[![Coverage Status](https://coveralls.io/repos/github/Facsimile/facsimile/badge.svg?branch=master)](https://coveralls.io/github/Facsimile/facsimile?branch=master)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/ba518cbf7a13430b9f3854933b5e94e9)](https://app.codacy.com/gh/Facsimile/facsimile/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
[![Scala Steward badge](https://img.shields.io/badge/Scala_Steward-helping-brightgreen.svg?style=flat&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAAQCAMAAAARSr4IAAAAVFBMVEUAAACHjojlOy5NWlrKzcYRKjGFjIbp293YycuLa3pYY2LSqql4f3pCUFTgSjNodYRmcXUsPD/NTTbjRS+2jomhgnzNc223cGvZS0HaSD0XLjbaSjElhIr+AAAAAXRSTlMAQObYZgAAAHlJREFUCNdNyosOwyAIhWHAQS1Vt7a77/3fcxxdmv0xwmckutAR1nkm4ggbyEcg/wWmlGLDAA3oL50xi6fk5ffZ3E2E3QfZDCcCN2YtbEWZt+Drc6u6rlqv7Uk0LdKqqr5rk2UCRXOk0vmQKGfc94nOJyQjouF9H/wCc9gECEYfONoAAAAASUVORK5CYII=)](https://scala-steward.org)

## Using _Facsimile_

### Status

__Note__: _Facsimile_ is not currently usable as a simulation library; it is currently anticipated that this will be
addressed by a future release.

In the meantime, please feel free to use the library (and its sub-components), and provide feedback in the form of bug
reports and enhancement requests.

The library API should be regarded as _experimental_ until the 1.0 release is achieved; please be aware that significant
API changes may be made before this release, which may break code that relies up on it. Do not use _Facsimile_ for your
day-today projects just yet.

### Pre-requisites

__Note__: Installers for _Facsimile_ that allow it to be utilized for simulation modeling are currently unavailable;
 check back for developments.

_Facsimile_ utilizes the most recent _Java LTS_ (_long-term support_) release that is supported by its dependencies. The
current _Facsimile_ version depends upon _Java 25 LTS_, and it is recommended that you utilize only this version of
_Java_ for _Facsimile_ for the time being.

Once a stable _Facsimile_ release has been announced, we'll support the oldest freely and widely supported _Java LTS_
release, or _Java 25 LTS_, whichever is newer.

Any _Java 25 LTS_ release, including those by _Oracle_, _OpenJDK_, _Azul Systems_, _Eclipse_, etc. should work just
fine. It is strongly recommended that your organization reviews the licensing of your chosen _Java_ supplier to ensure
that you are in compliance with its terms.

Once _Java_ has been installed, it is trivial to build models using _[SBT](https://scala-sbt.org)_; this will download
all necessary resources, including the _Scala_ compiler, _Facsimile_ libraries themselves, and their dependencies.

A template _SBT_ project (for use with the `sbt new` command) will be released shortly. In the meantime, the following
example should suffice:

```sbt
// Human-readable name of your project.
name := "Your project's name"

// Repository artifact name of your project.
normalizedName := "yourproject"

// Repository organization name of your project.
organization := "your.organization"

// Human-readable name of your organization.
organizationName := "Name of your organization"

// Version number for this release of your project.
version := "1.0.0-SNAPSHOT"

// Version of the Scala compiler and libraries to be used to build your project.
//
// This should match the supported version of _Scala_ for the specified release.
scalaVersion := "3.3.4"

// Version of Facsimile required to build your project.
libraryDependencies += "org.facsim" %% "facsimile-simulation" % "0.3.0"
```

Use of _[JetBrains' IntelliJ IDEA](https://www.jetbrains.com/idea/)_ is highly recommended for developing both
_Facsimile_ itself, and code using it; the _Scala_ and _HOCON IntelliJ_ plugins are essential.
