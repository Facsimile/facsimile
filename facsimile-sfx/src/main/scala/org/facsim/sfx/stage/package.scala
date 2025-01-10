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
// Scala source file belonging to the org.facsim.sfx.stage package.
//======================================================================================================================
package org.facsim.sfx

import javafx.event.EventDispatcher
import javafx.scene.Scene
import javafx.stage.Window

/** Stage package mirroring the `javafx.stage` package. */
//
// Note: Although this might look odd, implicit definitions - even if they are classes - cannot be defined within a
// package (as a top-level object); instead, they must be defined as members of another trait, class or object -
// typically, a package object. Should this change in a future Scala release, then the implicit class declarations
// should be moved to individual source files, as is the case with regular, non-implicit classes.
package object stage {

  /** Rich _JavaFX_ window.
   *
   * @tparam W Actual [[javafx.stage.Window]] class being decorated.
   *
   * @see See [[javafx.stage.Window]] for further information.
   *
   * @constructor Create _Scala_ decorated [[javafx.stage.Window]] instance.
   *
   * @param w [[javafx.stage.Window]] instance being decorated.
   *
   * @since 0.0
   */
  implicit final class RichWindow[W <: Window](val w: W)
  extends AnyVal {

    /** _Scala_-style [[javafx.event.EventDispatcher]] getter method.
     *
     *  @return Event dispatcher currently registered with the associated window.
     *
     *  @see See [[javafx.stage.Window.getEventDispatcher()]] for further information.
     */
    def eventDispatcher: EventDispatcher = w.getEventDispatcher

    /** _Scala_-style [[javafx.event.EventDispatcher]] setter method.
     *
     *  @param ed [[javafx.event.EventDispatcher]] to be registered with the associated window.
     *
     *  @see See [[javafx.stage.Window.setEventDispatcher()]] for further information.
     */
    def eventDispatcher_=(ed: EventDispatcher): Unit = w.setEventDispatcher(ed)

    /** _Scala_-style height getter method.
     *
     *  @return Height of the associated window.
     *
     *  @see See [[javafx.stage.Window.getHeight()]] for further information.
     */
    def ht: Double = w.getHeight

    /** _Scala_-style height setter method.
     *
     *  @param h New height for the associated window.
     *
     *  @see See [[javafx.stage.Window.setHeight()]] for further information.
     */
    //def height_=(h: Double): Unit = w.setHeight(h)

    def onCloseRequest = w.getOnCloseRequest
    def onCloseRequest_=(e: => Unit) = w.setOnCloseRequest(???)

    def onHidden = w.getOnHidden
    def onHiddin_=(e: => Unit) = w.setOnHidden(???)

    def onHiding = w.getOnHiding
    def onHiding_=(e: => Unit) = w.setOnHiding(???)

    def onShowing = w.getOnShowing
    def onShowing_=(e: => Unit) = w.setOnShowing(???)

    def onShown = w.getOnShown
    def onShown_=(e: => Unit) = w.setOnShown(???)

    def opacity = w.getOpacity
    def opacity_=(e: => Unit) = w.setOpacity(???)

    def properties = w.getProperties

    def scene = w.getScene
    def scene_=(s: Scene) = w.setScene(s)

    def userData = w.getUserData
    def userData_=(ud: AnyRef) = w.setUserData(ud)

    def width = w.getWidth
    def width_=(width: Double) = w.setWidth(width)

    def x = w.getX
    def x_=(coord: Double) = w.setX((coord))

    def y = w.getY
    def y_=(coord: Double) = w.setY((coord))
  }
}