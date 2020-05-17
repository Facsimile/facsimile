//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
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
package org.facsim.sim.model

import squants.{Acceleration, Length, Time, Velocity}
import squants.motion._
import squants.space.{Angle, Meters, Radians}
import squants.time.Seconds

/** Base trait for all motion states.
 *
 *  @since 0.0
 */
sealed trait MotionState {

  /** Time this state was entered.
   *
   *  This property is utilized to determine the position of the element at a time in the future.
   *
   *  @since 0.0
   */
  val startTime: Time

  /** Elapsed simulation time spent in state by specified time.
   *
   *  @param currentTime Current simulation time. This value must be greater than or equal to zero.
   *
   *  @return Simulation time spent in this state.
   *
   *  @since 0.0
   */
  protected final def elapsedTime(currentTime: Time): Time = (currentTime - startTime) ensuring(_ >= Seconds(0.0))
}

/** Simulation element translational motion state base trait.
 *
 *  Base characteristics for all elements having a translational motion component along their local X-axis.
 *
 *  @since 0.0
 */
sealed trait TranslationalMotionState
extends MotionState {

  /** Translation from last position at specified time.
   *
   *  @param currentTime Current simulation time.
   *
   *  @return Distance traveled from original position, along local X-axis.
   *
   *  @since 0.0
   */
  def distance(currentTime: Time): Length
}

/** Simulation element stationary translation state.
 *
 *  Elements having this state are motionless, but still aligned facing forwards along their local X-axis.
 *
 *  @constructor Create new stationary translational motion state.
 *
 *  @param startTime Simulation time at which associated element entered this state.
 *
 *  @since 0.0
 */
final case class TranslationalStationaryState(override val startTime: Time)
extends TranslationalMotionState {

  /** @inheritdoc */
  override def distance(currentTime: Time): Length = Meters(0.0)
}

/** Simulation element accelerating translation state.
 *
 *  Elements having this state are either accelerating or decelerating, at a fixed rate, along their local X-axis.
 *
 *  If the acceleration value is positive, then the element is increasing its velocity; if negative, then it is
 *  decreasing its velocity relative to the positive local X-axis.
 *
 *  @constructor Create new acceleration/deceleration translational motion state.
 *
 *  @param startTime Simulation time at which associated element entered this state.
 *
 *  @param initialVelocity Element's initial velocity when it entered this state.
 *
 *  @param acceleration Constant acceleration to be applied while the associated element is in this state. This value
 *  cannot be zero.
 *
 *  @since 0.0
 */
final case class TranslationalAccelerationState(override val startTime: Time, initialVelocity: Velocity,
acceleration: Acceleration)
extends TranslationalMotionState {

  // Sanity check.
  assert(acceleration != MetersPerSecondSquared(0.0))

  /** @inheritdoc */
  override def distance(currentTime: Time): Length = {

    // Distance traveled:
    val t = elapsedTime(currentTime)
    (initialVelocity * t) + (acceleration * t * t / 2.0)
  }
}

/** Simulation element cruising translation state.
 *
 *  Elements having this state are moving at a constant, non-zero velocity. If the velocity is positive, then the
 *  element is moving forwards, in the direction of the positive local X-axis; if negative, then the element is
 *  traveling backwards.
 *
 *  @constructor Create a new translational cruising motion state.
 *
 *  @param startTime Simulation time at which associated element entered this state.
 *
 *  @param initialVelocity Element's velocity when it entered this state. This value cannot be zero.
 *
 *  @since 0.0
 */
final case class TranslationalCruiseState(override val startTime: Time, initialVelocity: Velocity)
extends TranslationalMotionState {

  // Sanity check.
  assert(initialVelocity != MetersPerSecond(0.0))

  /** @inheritdoc */
  override def distance(currentTime: Time): Length = {

    // Determine time spent in this state.
    val t = elapsedTime(currentTime)
    initialVelocity * t
  }
}

/** Simulation element rotational motion state base trait.
 *
 *  Base characteristics for all elements having a rotational motion component about their local Z-axis.
 *
 *  @since 0.0
 */
sealed trait RotationalMotionState
extends MotionState {

  /** Rotation from last position at specified time.
   *
   *  @param currentTime Current simulation time.
   *
   *  @return Angle traveled from original position, about local Z-axis. Positive values are counter-clockwise,
   *  negative values are clockwise.
   *
   *  @since 0.0
   */
  def theta(currentTime: Time): Angle
}

/** Simulation element stationary rotational state.
 *
 *  Elements having this state are motionless, and are not rotating.
 *
 *  @constructor Create new stationary rotational motion state.
 *
 *  @param startTime Simulation time at which associated element entered this state.
 *
 *  @since 0.0
 */
final case class RotationalStationaryState(override val startTime: Time)
extends RotationalMotionState {

  /** @inheritdoc */
  override def theta(currentTime: Time): Angle = Radians(0.0)
}

/** Simulation element accelerating rotational state.
 *
 *  Elements having this state are either accelerating or decelerating, at a fixed rate, about their local Z-axis.
 *
 *  If the acceleration value is positive, then the element is increasing its counter-clockwise angular velocity
 *  direction; if negative, then it is increasing its clockwise angular velocity.
 *
 *  @constructor Create new angular acceleration/deceleration rotational motion state.
 *
 *  @param startTime Simulation time at which associated element entered this state.
 *
 *  @param initialVelocity Element's initial angular velocity when it entered this state.
 *
 *  @param acceleration Constant angular acceleration to be applied while the associated element is in this state. This
 *  value cannot be zero.
 *
 *  @since 0.0
 */
final case class RotationalAccelerationState(override val startTime: Time, initialVelocity: AngularVelocity,
acceleration: AngularAcceleration)
extends RotationalMotionState {

  // Sanity check.
  assert(acceleration != RadiansPerSecondSquared(0.0))

  /** @inheritdoc */
  override def theta(currentTime: Time): Angle = {

    // Angle traveled:
    val t = elapsedTime(currentTime)
    (initialVelocity * t) + (acceleration * t * t / 2.0)
  }
}

/** Simulation element cruising rotational state.
 *
 *  Elements having this state are moving at a constant, non-zero angular velocity. If the velocity is positive, then
 *  the element is moving counter-clockwise, about positive local Z-axis; if negative, then the element is
 *  traveling clockwise.
 *
 *  @constructor Create a new rotational cruising motion state.
 *
 *  @param startTime Simulation time at which associated element entered this state.
 *
 *  @param initialVelocity Element's angular velocity when it entered this state. This value cannot be zero.
 *
 *  @since 0.0
 */
final case class RotationalCruiseState(override val startTime: Time, initialVelocity: AngularVelocity)
extends RotationalMotionState {

  // Sanity check.
  assert(initialVelocity != RadiansPerSecond(0.0))

  /** @inheritdoc */
  override def theta(currentTime: Time): Angle = {

    // Determine time spent in this state.
    val t = elapsedTime(currentTime)
    initialVelocity * t
  }
}