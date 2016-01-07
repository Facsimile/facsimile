package org.facsim.gui.jfx.stage

import javafx.stage.{Stage => JFXStage}
/**
Implicit class wrapping a ''JavaFX'' `Stage` class.
*/
implicit class Stage (stage: JFXStage)
extends AnyVal {

  def apply(init: => Unit): Unit = init

  def title () = stage.getTitle
  def title_:(value: String) = stage.setTitle (value)
}
