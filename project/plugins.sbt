// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2014, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under
// the terms of the GNU Lesser General Public License as published by the Free
// Software Foundation, either version 3 of the License, or (at your option)
// any later version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance.
// For further information, please visit the project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the
// addition of new files) submitted for inclusion as part of the official
// Facsimile code base, must comply with the published Facsimile Coding
// Standards. If your code fails to comply with the standard, then your patches
// will be rejected. For further information, please visit the coding standards
// at:
//
//   http://facsim.org/Documentation/CodingStandards/
//=============================================================================
// SBT plugins file.
//=============================================================================

// SBT sbt-git plugin.
//
// This plugin provides support for using git commit SHA codes for versioning
// builds, which is essential for ensuring that the "git bisect" command
// functions correctly.
//
// This plugin implements the behavior outlined by Josh Suereth (one of SBT's
// authors) in his Scala Days 2013 presentation, "Effective SBT". Refer to the
// following link for his presentation:
//
//   http://www.parleys.com/play/51c3790ae4b0d38b54f46259
//
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.6.3")

//resolvers += "jgit-repo" at "http://download.eclipse.org/jgit/maven"

// Support SBT within Scala Eclipse IDE.
addSbtPlugin ("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.0")

// GPG plugin for signing distributed artifacts.
addSbtPlugin ("com.typesafe.sbt" % "sbt-pgp" % "0.8")

resolvers += "sonatype-releases" at
"https://oss.sonatype.org/content/repositories/releases/"
