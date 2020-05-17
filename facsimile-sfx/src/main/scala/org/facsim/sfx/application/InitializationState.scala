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

// Scala source file belonging to the org.facsim.sfx.application package.
//======================================================================================================================
package org.facsim.sfx.application

/** Base trait storing ''JavaFX'' application initialization state. */
private[application] sealed trait InitializationState

/** Case class representing an initialized ''JavaFX'' application.
 *
 *  @constructor Create a newly initialized ''JavaFX'' application initialization state.
 *
 *  @param app Initialized ''JavaFX'' application instance.
 */
private[application] case class Initialized(app: JFXApplication)
extends InitializationState

/** Case class representing an uninitialized ''JavaFX'' application state.
 *
 *  @constructor Create a new uninitialized ''JavaFX'' application state, with the specified set of constructors.
 *
 *  @param ctors List of constructors to be executed, in reverse order. New constructor code blocks should be prepended
 *  to this list.
 */
private[application] case class Uninitialized(ctors: List[() => Unit] = Nil)
extends InitializationState {

  /** Execute the constructors, returning an initialized application state..
   *
   *  @param app ''JavaFX'' a
   */
}