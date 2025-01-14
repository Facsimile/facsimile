//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
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
// Scala source file belonging to the org.facsim.sfx.importers.cell package.
//======================================================================================================================
package org.facsim.sfx.importers.cell

import javafx.scene.transform.Transform

/** _Cell_ element's joint data.
 *
 *  Stores the current _cell_ element's dynamic _joint_ and/or _terminal control frame_ (_TCF_) data.
 *
 *  @note The `jointTransform`, which typically comprises solely rotational transformations, is used to align the
 *  joint's local Z-axis with the axis of motion. Translational joints translate the joint along this axis, while
 *  rotational joints rotate about this axis. Similarly, the `tcfTransform` is used to position and orient elements
 *  that are dynamically added to the element with this joint information.
 *
 *  @constructor Create a new _cell_ joint data instance.
 *
 *  @param joint Type of joint represented.
 *
 *  @param speed Maximum speed (in translational distance units, or rotational units measured in _degrees_, per time
 *  unit) for motion along the joint.
 *
 *  @param range Range of joint motion, together with current (initial) displacement.
 *
 *  @param jointTransform Sequence of transformations to be applied to joint's motion.
 *
 *  @param tcfTransform Sequence of transformations to be applied to associated _TCF_ (if any).
 */
private[cell] final class JointData(joint: JointType, speed: Double, range: JointRange, jointTransform: Seq[Transform],
tcfTransform: Seq[Transform])