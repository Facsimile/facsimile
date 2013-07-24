/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

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