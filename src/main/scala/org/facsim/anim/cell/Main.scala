package org.facsim.anim.cell

import scala.swing.Dimension
import scala.swing.MainFrame
import scala.swing.SwingApplication

object Main extends SwingApplication {

  override def startup (args: Array[String]) {
    if (args.length == 0) {
      println ("No cell file specified...")
      println ("Done")
    }
    else {
      val t = new MainFrame {
        title = "The Incredible Java3D Cell File Reader!"
        contents = new Panel3D (args (0))
      }
      if (t.size == new Dimension(0,0)) t.pack()
      t.visible = true
    }
  }
}
