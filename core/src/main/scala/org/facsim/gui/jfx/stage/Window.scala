package org.facsim.gui.jfx.stage

import javafx.event.{EventHandler, EventDispatcher}
import javafx.stage.{Window => JFXWindow, WindowEvent}

/**
  * Created by mike on 1/6/2016.
  */
implicit final class Window (window: JFXWindow)
extends AnyVal {

  def eventDispatcher = window.getEventDispatcher
  def eventDispatcher_: (ed: EventDispatcher) = window.setEventDispatcher (ed)

  def height = window.getHeight
  def height_: (h: Double) = window.setHeight (h)

  def onCloseRequest = window.getOnCloseRequest
  def onCloseRequest_: (e: => Unit) = window.setOnCloseRequest (new EventHandler [WindowEvent])
  getOnCloseRequest, getOnHidden, getOnHiding, getOnShowing, getOnShown, getOpacity, getProperties, getScene, getUserData, getWidth, getX, getY}
