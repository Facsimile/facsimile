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

// Scala source file belonging to the org.facsim.sfx.beans.value package.
//======================================================================================================================
package org.facsim.sfx.beans.value

import javafx.beans.value.{ChangeListener, ObservableValue, WritableValue}
import org.facsim.sfx.event.subscriptions.Subscription
import org.facsim.util.requireNonNull

/** Rich wrappers for [[javafx.beans.value]] elements.
 *
 *  @since 0.0
 */
object SFXBeansValueImplicits {

  /** Rich wrapper for [[javafx.beans.value.ObservableValue]] interface instances.
   *
   *  @tparam A Type of value being observed.
   *
   *  @param ov ''JavaFX'' observable value being wrapped.
   *
   *  @see [[javafx.beans.value.ObservableValue]] for further information.
   *
   *  @since 0.0
   */
  implicit final class RichObservableValue[A](private val ov: ObservableValue[A])
  extends AnyVal {

    /** Retrieve the value of the observable.
     *
     *  @return Value currently associated with the observable.
     *
     *  @see [[javafx.beans.value.ObservableValue#getValue]] for further information about this operation.
     *
     *  @since 0.0
     */
    def apply(): A = ov.getValue

    /** Install a ''change listener''.
     *
     *  Registers the specified function to be called when the associated observable value is changed.
     *
     *  @param cl ''Change listener'' function to be called when this observable value is changed. The first argument
     *  supplied to this function will identify the observable value that has been changed, the second argument will be
     *  that observable value's previous value, and the third argument will be the new value. Note that this function
     *  should not attempt to change the value of the associated observable.
     *
     *  @return Subscription to the registered listener, allowing the listener to be removed by simply canceling the
     *  subscription.
     *
     *  @throws scala.NullPointerException if `cl` is `null`.
     *
     *  @see [[javafx.beans.value.ChangeListener]] for information about change listeners.
     *
     *  @see [[javafx.beans.value.ObservableValue#addListener]] for information about registering change listeners.
     *
     *  @see [[javafx.beans.value.ObservableValue#removeListener]] for information about removing change listeners.
     *  This method is called when the onChange subscription is canceled.
     *
     *  @since 0.0
     */
    def onChange(cl: (ObservableValue[_ <: A], A, A) => Unit): Subscription = {
      // Sanity check. This value cannot be null.
      requireNonNull(cl)

      // Create a change listener instance, which will invoke the specified function.
      val listener = new ChangeListener[A] {
        final override def changed(observable: ObservableValue[_ <: A], oldVal: A, newVal: A): Unit = {
          cl(observable, oldVal, newVal)
        }
      }

      // Register the listener with the observable.
      ov.addListener(listener)

      // Create and return a new subscription allowing the caller to unsubscribe from listening to this observable.
      new Subscription {
        final override def cancel(): Unit = ov.removeListener(listener)
      }
    }
  }

  /** Rich wrapper for [[javafx.beans.value.WritableValue]] interface instances.
   *
   *  @tparam A Type of value that can be written into an instance.
   *
   *  @param wv ''JavaFX'' writable value being wrapped.
   *
   *  @see [[javafx.beans.value.WritableValue]] for further information.
   *
   *  @since 0.0
   */
  implicit final class RichWritableValue[A](private val wv: WritableValue[A])
  extends AnyVal {

    /** Write a new value into this instance, replacing the current value.
     *
     *  @param v New value to be stored.
     *
     *  @see [[javafx.beans.value.WritableValue#setValue]] for further information on this operation.
     */
    def update(v: A): Unit = wv.setValue(v)
  }
}