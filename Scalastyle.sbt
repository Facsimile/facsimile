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
// SBT configuration for the Scalastyle plugin.
//
// NOTE: This file is maintained as part of the Facsimile "skeleton" project, and is common to a number of other
// Facsimile projects, termed client projects. It must only be modified in the "skeleton" project, with changes being
// merged into client projects. Refer to the skeleton project for further details:
//
//   https://github.com/Facsimile/skeleton
========================================================================================================================

// Configure Scalastyle configuration file location.
//
// IntelliJ IDEA looks for the Scalastyle configuration file (whicb, at the time of writing, must be called
// "scalastyle_config.xml") in either the /.idea or the /project directories, rather than the project root directory,
// which is the Scalastyle default location. To keep both systems happy, this file is placed in the /project directory,
// and so Scalastyle must be configured to look there for this file.

scalastyleConfig := file("./project/scalastyle_config.xml")
