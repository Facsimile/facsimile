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
// Scala source file belonging to the org.facsim.sim.model package.
//======================================================================================================================
package org.facsim.sim

/** ''Facsimile Simulation Library'' model package.
 *
 *  Package containing simulation modeling elements, their actions and location in 3D space.
 *
 *  =Note on Orientation=
 *
 *  In ''Facsimile'', the ''world'' axes are oriented as follows:
 *
 *   - X-Axis: Positive X-axis points to the ''East'', negative to the ''West''.
 *   - Y-Axis: Positive Y-axis points to the ''North'', negative to the ''South''.
 *   - Z-Axis: Positive Z-axis points ''Up'', negative points ''Down''.
 *
 *  This is the same ''right-handed orientation'' convention adopted by ''3D Studio'', ''AutoCAD'' and ''AutoMod''
 *  among others.
 *
 *  Note that ''JavaFX'' utilizes the following unusual axis orientation:
 *
 *   - X-Axis: Positive X-axis points to the ''East'', negative to the ''West''.
 *   - Y-Axis: Positive Z-axis points ''Up'', negative points ''Down''.
 *   - Z-Axis: Positive Y-axis points to the ''South'', negative to the ''North''.
 *
 *  Transformations must be applied to the 3D model in order to accurately render the same scene in ''JavaFX''.
 *
 *  @since 0.0
 */
package object model