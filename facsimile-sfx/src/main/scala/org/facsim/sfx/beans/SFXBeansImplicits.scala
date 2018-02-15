//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2018, Michael J Allen.
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

// Scala source file belonging to the org.facsim.sfx.beans package.
//======================================================================================================================
package org.facsim.sfx.beans

import javafx.beans.{InvalidationListener, Observable}
import org.facsim.sfx.event.subscriptions.Subscription
import org.facsim.util.requireNonNull

/** Rich wrappers for [[javafx.beans]] elements.
 *
 *  @since 0.0
 */
object SFXBeansImplicits {

  /** Rich wrapper for [[javafx.beans.Observable]] interface instances.
   *
   *  @param o ''JavaFX'' observable being wrapped.
   *
   *  @see [[javafx.beans.Observable]] for further information.
   *
   *  @since 0.0
   */
  final implicit class RichObservable(private val o: Observable)
  extends AnyVal {

    /** Install an ''invalidation listener''.
     *
     *  Registers the specified function to be called when the associated observable is invalidated.
     *
     *  @param il ''Invalidation listener'' function to be notified when this observable is invalidated. The first
     *  argument to this function will be instance of the observable that has been invalidated.
     *
     *  @return Subscription to the registered listener, allowing the listener to be removed by canceling the
     *  subscription.
     *
     *  @throws scala.NullPointerException if `il` is `null`.
     *
     *  @see [[javafx.beans.InvalidationListener]] for information about invalidation listeners.
     *
     *  @see [[javafx.beans.Observable#addListener]] for information about registering invalidation listeners.
     *
     *  @see [[javafx.beans.Observable#removeListener]] for information about removing invalidation listeners. This
     *  method is called when the onInvalidate subscription is canceled.
     *
     *  @since 0.0
     */
    def onInvalidate(il: Observable => Unit): Subscription = {
      // Sanity check. This value cannot be null.
      requireNonNull(il)

      // Create an invalidation listener instance, which will invoke the specified function.
      val listener = new InvalidationListener {
        final override def invalidated(observable: Observable): Unit = il(observable)
      }

      // Register the listener with the observable.
      o.addListener(listener)

      // Create and return a new subscription allowing the caller to unsubscribe from listening to this observable.
      new Subscription {
        final override def cancel(): Unit = o.removeListener(listener)
      }
    }
  }
}