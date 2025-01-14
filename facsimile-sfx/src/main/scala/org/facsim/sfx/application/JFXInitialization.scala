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
// Scala source file belonging to the org.facsim.sfx.application package.
//======================================================================================================================
package org.facsim.sfx.application

/** Base trait for initializing _JavaFX_.
 *
 *  @note Construction code for derived classes and objects is executed on the _JavaFX event-dispatch thread_. To
 *  achieve this, the execution of such code must be delayed until after the _JavaFX_ `javafx.application.Application`
 *  instance has been created and initialized. However, the construction code for traits that extend this trait will be
 *  executed upon construction, on the current thread, in the normal manner. This functionality is provided by the
 *  [[DelayedInit]] trait.
 */
private[application] trait JFXInitialization
extends DelayedInit {

  /** Application initialization state. */
  private var state: InitializationState = Uninitialized()

  /** Queue construction code for execution once the _JavaFX_ application instance has been initialized.
   *
   *  @note [[DelayedInit]] causes the compiler to wrap `class` and `object` constructor code in a call to this
   *  function.
   *
   *  @param c Constructor code to be queued for later execution.
   *
   *  @throws IllegalStateException if the application has already been initialized when called. This would typically
   *  happen only if this function is called explicitly from user code.
   */
  override final def delayedInit(c: => Unit): Unit = synchronized {
    state = state match {
      case Uninitialized(cs) => Uninitialized((() => c) :: cs)
      case _ => throw new IllegalStateException("Application initialization has completed.")
    }
  }

  /** Perform the required initialization.
   *
   *  This function will be called from the _JavaFX_ application instance, during its construction.
   *
   *  @param app The newly created _JavaFX_ application instance.
   */
  private final def init(app: JFXApplication) = synchronized {
    val preInitState = state
  }
}