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

//=============================================================================
/**
Abstract base class for all ''[[http://www.automod.com/ AutoMod]] cell''
primitives.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile AutoMod Cell
File Format]] for further information.

@constructor Construct a new basic cell primitive.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive.  If this value is `None`, then
this cell is the scene's root cell.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//=============================================================================

private [cell] abstract class Cell (scene: CellScene, private val parent:
Option [Set]) extends CellAttributes with NotNull {

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
  if (flags.jointDataPresent) Some (JointType.readJoint (scene))
  else None

/*
@see [[org.facsim.anim.cell.CellAttributes!.lineStyle]]
*/

  final override val lineStyle = attrs.lineStyle

/*
@see [[org.facsim.anim.cell.CellAttributes!.lineWidth]]
*/

  final override val lineWidth = attrs.lineWidth

/*
@see [[org.facsim.anim.cell.CellAttributes!.displayStyle]]
*/

  final override val displayStyle = attrs.displayStyle

/*
@see [[org.facsim.anim.cell.CellAttributes!.name]]
*/

  final override val name = attrs.name

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.CellAttributes!.faceColor]]
*/
//-----------------------------------------------------------------------------

  final override def faceColor = attrs.faceColor.orElse (parent.flatMap
  (_.faceColor))

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.anim.cell.CellAttributes!.edgeColor]]
*/
//-----------------------------------------------------------------------------

  final override def edgeColor = attrs.edgeColor.orElse (parent.flatMap
  (_.edgeColor))
}
