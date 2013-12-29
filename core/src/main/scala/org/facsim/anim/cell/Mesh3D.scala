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
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import scalafx.collections.ObservableFloatArray
import scalafx.collections.ObservableIntegerArray
import scalafx.scene.Node
import scalafx.scene.shape.Mesh
import scalafx.scene.shape.MeshView

//=============================================================================
/**
Abstract base class for all ''[[http://www.automod.com/ AutoMod]] cell''
primitives implmented as 3D meshes.

@constructor Construct a new 3D-mesh-based primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive.  If this value is `None`, then
this cell is the scene's root cell.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//=============================================================================

private [cell] abstract class Mesh3D (scene: CellScene, parent: Option [Set])
extends Cell(scene, parent) {

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.Cell!.toNode]]
*/
//-----------------------------------------------------------------------------

  private [cell] final override def toNode =

/*
If the cell is to be displayed as a wireframe, then draw it as a series of
lines; otherwise, draw as a solid.
*/

  if (isWireframe) drawAsWireframe
  else drawAsSolid

//-----------------------------------------------------------------------------
/**
Create a mesh to represent this cell and return it.

@return Mesh representing the cell.

@since 0.0
*/
//-----------------------------------------------------------------------------

  protected [cell] def cellMesh: Mesh

//-----------------------------------------------------------------------------
/**
Draw this ''cell'' in wireframe mode.

@note At the time of writing, ''JavaFX''/''ScalaFX'' does not appear to support
3D polylines, so rendering of shapes in wireframe is not supported.  Instead,
rather than not render the primitives, they are currently rendered as solids.

@return Base node storing the wireframe form of this trapezoid.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def drawAsWireframe: Node = drawAsSolid

//-----------------------------------------------------------------------------
/**
Draw this ''cell'' in solid mode, as a collection of 3D meshes.

@return Base node storing the solid form of this cell.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def drawAsSolid: Node =
  new MeshView {

/*
If this cell has a name, then use it as an ID.
*/

    id = name.orNull

/*
Apply the required transformations to the node.
*/

    transforms = cellTransforms

/*
Ensure that the cell is drawn with the correct materials and opacity.
*/

    material = cellMaterial
    opacity = cellOpacity

/*
Have the concrete cell create the mesh to represent the image.
*/

    mesh = cellMesh
  }
}

//=============================================================================
/**
Mesh3D companion object.

@since 0.0
*/
//=============================================================================

private [cell] object Mesh3D {

/*
Allow implicit conversions.
*/

  import scala.language.implicitConversions

//-----------------------------------------------------------------------------
/**
Implicit conversion from an Array[Float] to a
javafx.collection.ObservableFloatArray.

@todo This is a little scrappy and ineffecient.  We can do better, but ScalaFX
currently doesn't support ObservableFloatArrays.

@param array Array to be converted.

@return ObservableFloatArray with contents of initial array.

@since 0.0
*/
//-----------------------------------------------------------------------------

  implicit def arrayToFloatArray (array: Array [Float]) =
  ObservableFloatArray (array)

//-----------------------------------------------------------------------------
/**
Implicit conversion from an Array[Int] to a
javafx.collection.ObservableIntegerArray.

@todo This is a little scrappy and ineffecient.  We can do better, but ScalaFX
currently doesn't support ObservableIntegerArrays.

@param array Array to be converted.

@return ObservableIntegerArray with contents of initial array.

@since 0.0
*/
//-----------------------------------------------------------------------------

  implicit def arrayToIntegerArray (array: Array [Int]) =
  ObservableIntegerArray (array)
}