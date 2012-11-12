package org.facsim.anim.cell

import com.sun.j3d.utils.universe.SimpleUniverse
import javax.media.j3d.Canvas3D
import scala.swing.Dimension
import scala.swing.Panel

class Panel3D (cellFileName: String) extends Panel {
  preferredSize = new Dimension (512, 512)
  opaque = false
  private val config = SimpleUniverse.getPreferredConfiguration ()
  private val canvas = new Canvas3D (config)
  canvas.setFocusable (true)
  canvas.requestFocus ()
  peer.add ("Center", canvas) // 'cos canvas is not a Scala Swing Component.
  private val univ = new SimpleUniverse (canvas)
  // Add the cell file to the universe.
  univ.addBranchGraph (new CellGraph (cellFileName))
}