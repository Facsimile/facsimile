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
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import java.io.Reader
import java.net.URL
import java.util.Hashtable
import javax.media.j3d.Background
import javax.media.j3d.Behavior
import javax.media.j3d.BranchGroup
import javax.media.j3d.Fog
import javax.media.j3d.Light
import javax.media.j3d.Sound
import javax.media.j3d.TransformGroup
import com.sun.j3d.loaders.IncorrectFormatException
import com.sun.j3d.loaders.Scene
import com.sun.j3d.loaders.ParsingErrorException
import org.facsim.io.TextReader

//=============================================================================
/**
''Java3D'' scene retrieved from an ''[[http://www.automod.com/ AutoMod®]]
cell'' file.

@constructor Create a new scene from the specified reader and baseURL.

@param reader Reader for the stream of ''cell'' data to be processed.

@param baseURL Base URL to be employed to process any external graphics files
referenced by the cell data stream.

@throws com.sun.j3d.loaders.IncorrectFormatException if the first source field
read is not a integer; this is a clear sign that the supplied source isn't
formatted as a cell file.

@throws com.sun.j3d.loaders.ParsingErrorException if a problem arises during
processing of the cell data stream.

@since 0.0
*/
//=============================================================================

final class CellScene private [cell] (reader: Reader, baseURL: URL) extends
Scene {

/**
Flag indicating whether or not the scene has been fully constructed.
*/

  private final var constructed = false

/**
Reader for processing cell data.
*/

  private final val cellData = new TextReader (reader)

/**
Process the cell data.

Each time a new cell is read, this scene is notified so that it can be indexed
and cataloged correctly.

Note: There is a single ''root'' cell element that is either a leaf primitive
or a collection that contains all remaining cells making up the scene.
Consequently, the root cell contains the entire scene itself.
*/

  private final val rootCell = cellFactory (None)

/*
OK.  At this point, the scene has been constructed.  Activate the scene's
query methods.
*/

  constructed = true

//-----------------------------------------------------------------------------
/**
Cell factory.

Create an appropriate cell primitive instance from the cell data reader.  If
the created cell is a set (a non-leaf node that contains other cell
primitives), then this method will be called recursively and will fully
populate that set's contents (and any sub-set's contents) from the sources.

Each cell type is constructed via recursion, which implies that all cell
primitive classes must have an identical construction argument lists.
Specifically, the arguments every concrete cell sub-class must have are:
 1. A reference to the scene to which it belongs (i.e. this class instance.)
 2. A reference to the cell's parent set.  If this value is None, then the cell
    is the ''root cell'' of the scene.

@param parent Cell [[org.facsim.anim.cell.Set]] instance that is the ''parent''
(or ''owner'' of this cell.

@return Cell primitive created from the cell data stream.

@throws com.sun.j3d.loaders.IncorrectFormatException if the first source field
read is not a integer; this is a clear sign that the supplied source isn't
formatted as a cell file.

@throws com.sun.j3d.loaders.ParsingErrorException if a problem arises during
processing of the cell data stream.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] final def cellFactory (parent: Option [Set]): Cell = {

/*
Read the cell type from the data stream.  If this value is not an integer, then
this likely isn't a cell file we're reading.
*/

    val cellClass = try {
      val cellCode = cellData.readAsInt ()
      if (!CellScene.classMap.contains (cellCode)) throw new
      IncorrectFormatException ("Unrecognized cell type code: " +
      cellData.toString ())
      CellScene.classMap (cellCode)
    }
    catch {
      case e: NumberFormatException => throw new
      IncorrectFormatException (e.getMessage ())
    }

/*
Now use the class of the required cell file to construct the required new cell
primitive.
*/

    val ctor = cellClass.getConstructor (getClass (), parent.getClass ())
    val cell = ctor.newInstance (this, parent).asInstanceOf
    [Cell]

/*
Categorize and index this cell file.
*/

    // TODO

/*
Report the newly constructed cell.
*/

    cell
  }

//-----------------------------------------------------------------------------
/**
Report the text reader, for use by cell constructors.

@return Text reader currently in use.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] final def getCellData () = cellData

//-----------------------------------------------------------------------------
/**
Determine the URL for a referenced file, given the file reference from the cell
file.


If the file reference provided is an absolute file reference, then it is
returned as is.  Otherwise, it is processed according to the baseURL specified
during construction.

@param fileRef File reference obtained from a cell file.

@return URL to be used to read the referenced file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] final def getURL (fileRef: String): URL = {
    // This hasn't been implemented just yet.
    throw new
    UnsupportedOperationException ("org.facsim.anim.cell.CellScene.getURL")
  }

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

//=============================================================================
/**
CellScene companion object.

@since 0.0
*/
//=============================================================================

private object CellScene {

/**
Map associating cell type code with corresponding cell class.
*/

  private val classMap = Map [Int, Class [_ <: Cell]] (
    100 -> classOf [Triad],
    115 -> classOf [VectorList],
    125 -> classOf [Polyhedron],
    130 -> classOf [CoarseArc],
    131 -> classOf [FineArc],
    140 -> classOf [WorldText],
    141 -> classOf [ScreenFastText],
    142 -> classOf [ScreenNormalText],
    143 -> classOf [UnrotateFastText],
    144 -> classOf [UnrotateNormalText],
    150 -> classOf [WorldTextList],
    151 -> classOf [ScreenFastTextList],
    152 -> classOf [ScreenNormalTextList],
    153 -> classOf [UnrotateFastTextList],
    154 -> classOf [UnrotateNormalTextList],
    308 -> classOf [Definition],
    310 -> classOf [Trapezoid],
    311 -> classOf [Tetrahedron],
    315 -> classOf [Rectangle],
    330 -> classOf [CoarseHemisphere],
    331 -> classOf [FineHemisphere],
    340 -> classOf [CoarseCone],
    341 -> classOf [FineCone],
    350 -> classOf [CoarseCylinder],
    351 -> classOf [FineCylinder],
    360 -> classOf [CoarseFrustum],
    361 -> classOf [FineFrustum],
    388 -> classOf [FileReference],
    408 -> classOf [Instance],
    555 -> classOf [CompiledPicture],
    599 -> classOf [EmbeddedFile],
    700 -> classOf [Set],
    7000 -> classOf [Set],
    10000 -> classOf [Set]
  )
}