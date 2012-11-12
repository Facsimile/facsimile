/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

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
$Id$

Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import java.util.Hashtable
import javax.media.j3d.Background
import javax.media.j3d.Behavior
import javax.media.j3d.BranchGroup
import javax.media.j3d.Fog
import javax.media.j3d.Light
import javax.media.j3d.Sound
import javax.media.j3d.TransformGroup
import com.sun.j3d.loaders.Scene
import scala.io.Source

final class CellScene private [cell] (source: Source) extends Scene {

  def getSceneGroup(): BranchGroup = null

  def getViewGroups(): Array [TransformGroup] = Array ()

  def getHorizontalFOVs(): Array [Float] = Array ()

  def getLightNodes(): Array [Light] = Array ()

  def getNamedObjects(): Hashtable [String, Object] = new Hashtable [String,
  Object] ()

  def getBackgroundNodes(): Array [Background] = Array ()

  def getFogNodes(): Array [Fog] = Array ()

  def getBehaviorNodes(): Array [Behavior] = Array ()

  def getSoundNodes(): Array [Sound] = Array ()

  def getDescription(): String = null
}