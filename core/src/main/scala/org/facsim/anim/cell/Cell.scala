/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file from the org.facsim.anim.cell package.
*/
//======================================================================================================================

package org.facsim.anim.cell

import org.facsim.assertNonNull
import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.paint.Color

//======================================================================================================================
/**
Abstract base class for all ''[[http://www.automod.com/ AutoMod]] cell''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile AutoMod Cell
File Format]] for further information.

@constructor Construct a new basic cell primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile AutoMod Cell
File Format]] for further information.
*/
//======================================================================================================================

private [cell] abstract class Cell (scene: CellScene,
private val parent: Option [Set])
extends CellAttributes {

/*
Sanity checks.
*/

  assertNonNull (scene)
  assertNonNull (parent)

/**
Cell flags.

These flags determine which fields, if any, are included in the cell's
definition in the cell data stream, and also which characteristics are
inherited from the cell's parent.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Flags.html
AutoMod Cell Flags]]
*/

  private final val flags = CellFlags.read (scene)

/*
Process bounding box data, if present.

If bounding box date is present, we must read it, but will ignore it.
*/

  if (flags.boundingBoxPresent) BoundingBox.read (scene)

/**
Cell attributes.

Determine the cell's attribute values.
*/

  private final val attrs = new Attributes (scene, flags)

/**
Cell joint data.

Determine the cell's joint information, if any.

@note Ideally, only Sets should be defined with joint data, but it probably
doesn't matter too much if non-Sets do too.
*/

  private final val joint =
  if (flags.jointDataPresent) Some (JointType.readJoint (scene, flags))
  else None

/**
Cell transformation data.
*/

  private final val transformation =
  if (flags.geometryDataPresent) Some (new Transformation (scene,
  flags.geometryDataInMatrixForm))
  else None

/**
@inheritdoc
*/

  private [cell] final override val lineStyle = attrs.lineStyle

/**
@inheritdoc
*/

  private [cell] final override val lineWidth = attrs.lineWidth

/**
@inheritdoc
*/

  private [cell] final override val displayStyle = attrs.displayStyle

/**
@inheritdoc
*/

  private [cell] final override val name = attrs.name

//----------------------------------------------------------------------------------------------------------------------
/**
Face color of the parent.

@return If we have a parent, then return it's face color. Otherwise, we return
the scene's default face color as an option.
*/
//----------------------------------------------------------------------------------------------------------------------

  private final def parentFaceColor: Option [CellColor.Value] = parent match {
    case Some (parentCell) => parentCell.faceColor
    case None => scene.defaultFaceColor
  }

//----------------------------------------------------------------------------------------------------------------------
/**
Edge color of the parent.

@return If we have a parent, then return it's edge color. Otherwise, we return
the scene's default edge color as an option.
*/
//----------------------------------------------------------------------------------------------------------------------

  private final def parentEdgeColor: Option [CellColor.Value] = parent match {
    case Some (parentCell) => parentCell.edgeColor
    case None => scene.defaultEdgeColor
  }

//----------------------------------------------------------------------------------------------------------------------
/**
@inheritdoc

Face color is determined thus:
  - If face color is defined, return it.
  - Otherwise, if color is inherited and parent is defined, return its face
    color.
  - Otherwise, use this scene's default face color.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] final override def faceColor =
  attrs.faceColor.orElse (parentFaceColor)

//----------------------------------------------------------------------------------------------------------------------
/**
@inheritdoc

Edge color is determined thus:
  - If edge color is defined, return it.
  - Otherwise, if color is inherited and parent is defined, return its edge
    color.
  - Otherwise, use this scene's default edge color.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] final override def edgeColor =
  attrs.edgeColor.orElse (parentEdgeColor)

//----------------------------------------------------------------------------------------------------------------------
/**
Report whether this cell is wireframe or not.

@return `true` if the cell is drawn in wireframe mode, `false` if solid (with
varying degrees of transparency.
*/
//----------------------------------------------------------------------------------------------------------------------

  @inline
  protected [cell] final def isWireframe =
  attrs.displayStyle == DisplayStyle.Wireframe

//----------------------------------------------------------------------------------------------------------------------
/**
Color of this cell.

If cell is drawn in wireframe, then the cell will be drawn with the edge color,
otherwise, with face color.

@return Cell color with which the cell is to be drawn. A valid color must be
defined at some point in the chain of face/edge colors.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] final def color =
  if (isWireframe) edgeColor.get
  else faceColor.get

//----------------------------------------------------------------------------------------------------------------------
/**
Color of this cell, as a ''ScalaFX'' color.

If cell is drawn in wireframe, then the cell will be drawn with the edge color,
otherwise, with face color.

@return Cell color with which the cell is to be drawn. A valid color must be
defined at some point in the chain of face/edge colors.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] final def cellColor = CellColor.toColor (color)

//----------------------------------------------------------------------------------------------------------------------
/**
Color of this cell, as a ''ScalaFX'' material.

If cell is drawn in wireframe, then the cell will be drawn with the edge color,
otherwise, with face color.

@note Material is employed by [[scalafx.scene.shape.Shape3D!]] subclasses.

@return Material with which the cell is to be drawn. A valid color must be
defined at some point in the chain of face/edge colors.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] final def cellMaterial = CellColor.toMaterial (color)

//----------------------------------------------------------------------------------------------------------------------
/**
Color of this cell, as a paint.

The cell's opacity is included in the paint definition.

If cell is drawn in wireframe, then the cell will be drawn with the edge color,
otherwise, with face color.

@note Paint is employed by [[scalafx.scene.shape.Shape!]] subclasses.

@return Paint with which the cell is to be drawn. A valid color must be defined
at some point in the chain of face/edge colors.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] final def cellPaint: Color = cellColor.opacity (cellOpacity)

//----------------------------------------------------------------------------------------------------------------------
/**
Opacity of this cell.

@return Opacity of the cell as a value in the range 0 (invisible) through 1
(fully opaque).
*/
//----------------------------------------------------------------------------------------------------------------------

  protected [cell] final def cellOpacity =
  DisplayStyle.asOpacity (attrs.displayStyle)

//----------------------------------------------------------------------------------------------------------------------
/**
Transforms for this cell relative to its parent.

@return A sequence of transforms to be applied to the cell relative to its
parent.
*/
//----------------------------------------------------------------------------------------------------------------------

  protected [cell] final def cellTransforms = transformation match {
    case Some (t) => t.toList
    case None => Nil
  }

//----------------------------------------------------------------------------------------------------------------------
/**
Function to convert this ''cell'' and its contents (if any) to a ''ScalaFX''
[[scalafx.scene.Node!]] instance.

@return Cell as a [[scalafx.scene.Node!]].
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def toNode: Node
}
