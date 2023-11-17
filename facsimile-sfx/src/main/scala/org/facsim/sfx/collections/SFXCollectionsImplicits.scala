//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
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
// Scala source file belonging to the org.facsim.sfx.collections package.
//======================================================================================================================
package org.facsim.sfx.collections

import javafx.collections._
import org.facsim.sfx.event.subscriptions.Subscription
import org.facsim.util.requireNonNull

/** Rich wrappers for [[javafx.collections]] elements.
 *
 *  @since 0.0
 */
object SFXCollectionsImplicits {

  /** Rich wrapper for [[javafx.collections.ObservableArray]] interface instances.
   *
   *  @tparam A Type of array being observed.
   *
   *  @param oa _JavaFX_ observable array being wrapped.
   *
   *  @see [[javafx.collections.ObservableArray]] for further information.
   *
   *  @since 0.0
   */
  implicit final class RichObservableArray[A <: ObservableArray[A]](private val oa: ObservableArray[A])
  extends AnyVal {

    /** Install an array _change listener_.
     *
     *  Registers the specified function to be called when the associated observable array is changed.
     *
     *  @param cl _Change listener_ function to be called when this observable array is changed. The first argument
     *  supplied to this function will identify the observable array that has been changed, the second argument will be
     *  a flag indicating whether the size of the array was changed, the third argument is the index of the first cell
     *  to be changed, and the fourth argument is the index of the last element to be changed. Note that this function
     *  should not attempt to change the associated observable.
     *
     *  @return Subscription to the registered listener, allowing the listener to be removed by simply canceling the
     *  subscription.
     *
     *  @throws scala.NullPointerException if `cl` is `null`.
     *
     *  @see [[javafx.collections.ArrayChangeListener]] for information about array change listeners.
     *
     *  @see [[javafx.collections.ObservableArray#addListener]] for information about registering change listeners.
     *
     *  @see [[javafx.collections.ObservableArray#removeListener]] for information about removing change listeners.
     *  This method is called when the onChange subscription is canceled.
     *
     *  @since 0.0
     */
    def onChange(cl: (A, Boolean, Int, Int) => Unit): Subscription = {
      // Sanity check. This value cannot be null.
      requireNonNull(cl)

      // Create a change listener instance, which will invoke the specified function.
      val listener = new ArrayChangeListener[A] {
        final override def onChanged(observable: A, sizeChanged: Boolean, from: Int, to: Int): Unit = {
          cl(observable, sizeChanged, from, to)
        }
      }

      // Register the listener with the observable.
      oa.addListener(listener)

      // Create and return a new subscription allowing the caller to unsubscribe from listening to this observable.
      new Subscription {
        final override def cancel(): Unit = oa.removeListener(listener)
      }
    }
  }

  /** Rich wrapper for [[javafx.collections.ObservableList]] interface instances.
   *
   *  @tparam A Type of value in list being observed.
   *
   *  @param ol _JavaFX_ observable list being wrapped.
   *
   *  @see [[javafx.collections.ObservableList]] for further information.
   *
   *  @since 0.0
   */
  implicit final class RichObservableList[A](private val ol: ObservableList[A])
  extends AnyVal {

    /** Install a list _change listener_.
     *
     *  Registers the specified function to be called when the associated observable list is changed.
     *
     *  @param cl _Change listener_ function to be called when this observable list is changed. The argument supplied
     *  to this function will identify the change record detailing the changes made to the observable list. Note that
     *  this function should not attempt to change the associated observable. There are also many other restrictions on
     *  the execution and behaviour of this function and you are strongly recommended to review the documentation at
     *  [[javafx.collections.ListChangeListener.Change]].
     *
     *  @return Subscription to the registered listener, allowing the listener to be removed by simply canceling the
     *  subscription.
     *
     *  @throws scala.NullPointerException if `cl` is `null`.
     *
     *  @see [[javafx.collections.ListChangeListener]] for information about list change listeners.
     *
     *  @see [[javafx.collections.ListChangeListener.Change]] for information about list change listener information.
     *
     *  @see [[javafx.collections.ObservableList#addListener]] for information about registering change listeners.
     *
     *  @see [[javafx.collections.ObservableList#removeListener]] for information about removing change listeners.
     *  This method is called when the onChange subscription is canceled.
     *
     *  @since 0.0
     */
    def onChange(cl: ListChangeListener.Change[_ <: A] => Unit): Subscription = {
      // Sanity check. This value cannot be null.
      requireNonNull(cl)

      // Create a change listener instance, which will invoke the specified function.
      val listener = new ListChangeListener[A] {
        final override def onChanged(changeRecord: ListChangeListener.Change[_ <: A]): Unit = cl(changeRecord)
      }

      // Register the listener with the observable.
      ol.addListener(listener)

      // Create and return a new subscription allowing the caller to unsubscribe from listening to this observable.
      new Subscription {
        final override def cancel(): Unit = ol.removeListener(listener)
      }
    }
  }

  /** Rich wrapper for [[javafx.collections.ObservableMap]] interface instances.
   *
   *  @tparam K Type of value used as the key in the map being observed.
   *
   *  @tparam V Type for referenced values stored in the map being observed.
   *
   *  @param om _JavaFX_ observable map being wrapped.
   *
   *  @see [[javafx.collections.ObservableMap]] for further information.
   *
   *  @since 0.0
   */
  implicit final class RichObservableMap[K, V](private val om: ObservableMap[K, V])
  extends AnyVal {

    /** Install a map _change listener_.
     *
     *  Registers the specified function to be called when the associated observable map is changed.
     *
     *  @param cl _Change listener_ function to be called when this observable map is changed. The argument supplied
     *  to this function will identify the change record detailing the changes made to the observable map. Note that
     *  this function should not attempt to change the associated observable. There are also many other restrictions on
     *  the execution and behaviour of this function and you are strongly recommended to review the documentation at
     *  [[javafx.collections.MapChangeListener.Change]].
     *
     *  @return Subscription to the registered listener, allowing the listener to be removed by simply canceling the
     *  subscription.
     *
     *  @throws scala.NullPointerException if `cl` is `null`.
     *
     *  @see [[javafx.collections.MapChangeListener]] for information about map change listeners.
     *
     *  @see [[javafx.collections.MapChangeListener.Change]] for information about map change listener information.
     *
     *  @see [[javafx.collections.ObservableMap#addListener]] for information about registering change listeners.
     *
     *  @see [[javafx.collections.ObservableMap#removeListener]] for information about removing change listeners. This
     *  method is called when the onChange subscription is canceled.
     *
     *  @since 0.0
     */
    def onChange(cl: MapChangeListener.Change[_ <: K, _ <: V] => Unit): Subscription = {
      // Sanity check. This value cannot be null.
      requireNonNull(cl)

      // Create a change listener instance, which will invoke the specified function.
      val listener = new MapChangeListener[K, V] {
        final override def onChanged(changeRecord: MapChangeListener.Change[_ <: K, _ <: V]): Unit = cl(changeRecord)
      }

      // Register the listener with the observable.
      om.addListener(listener)

      // Create and return a new subscription allowing the caller to unsubscribe from listening to this observable.
      new Subscription {
        final override def cancel(): Unit = om.removeListener(listener)
      }
    }
  }
}