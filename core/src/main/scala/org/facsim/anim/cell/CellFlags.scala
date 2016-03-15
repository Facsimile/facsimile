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

import org.facsim.{assertNonNull, LibResource}

//======================================================================================================================
/**
Value class representing a ''cell'' element's flags.

@constructor Create a new ''cell'' flags instance from an integer bit-field
value.

@param flags ''Cell'' element's flags to be parsed.
*/
//======================================================================================================================

private [cell] final class CellFlags (val flags: Int)
extends AnyVal {

//----------------------------------------------------------------------------------------------------------------------
/**
Report whether the ''attributes present'' flag is set.

If this flag is set, then the cell's definition includes explicitly defined
[[org.facsim.anim.cell.Attributes]]; if clear, then default attributes will be
assigned.

@return `true` if attributes are present in the ''cell'' definition; `false` if
not.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def attributesPresent = (flags & 0x1) != 0

//----------------------------------------------------------------------------------------------------------------------
/**
Report whether the ''joint data present'' flag is set.

If this flag is set, then the cell's definition includes joint (kinematic)
data; if clear, no joint data is present.

@return `true` if joint data is present in the ''cell'' definition; `false` if
not.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def jointDataPresent = (flags & 0x2) != 0

//----------------------------------------------------------------------------------------------------------------------
/**
Report whether the ''geometry data present'' flag is set.

If this flag is set, then the cell's definition includes geometry data; if
clear, no geometry data is present.

@note In a ''cell'' file definition, ''geometry data'' refers to translation,
rotation and scaling data, rather than shape geometry.

@return `true` if geometry data is present in the ''cell'' definition; `false`
if not.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def geometryDataPresent = (flags & 0x4) != 0

//----------------------------------------------------------------------------------------------------------------------
/**
Report whether the ''geometry data in matrix form'' flag is set.

If this flag is set, then the cell's definition includes geometry data
expressed in matrix form; if clear, geometry data is expressed in terms of
translation along the X, Y and Z axes, a series of rotations about axes in a
defined sequence, and a scaling factor along each axis.

This flag is ignored unless the ''geometry data present'' flag is set.

@note In a ''cell'' file definition, ''geometry data'' refers to translation,
rotation and scaling data, rather than shape geometry.

@return `true` if geometry data is in matrix form in the ''cell'' definition;
`false` if in non-matrix form.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def geometryDataInMatrixForm = (flags & 0x8) != 0

//----------------------------------------------------------------------------------------------------------------------
/**
Report whether the ''colors inherited'' flag is set.

If this flag is set, then the cell's face and edge colors are inherited from
its parent; if clear, then it has defined colors that are not inherited from
its parent.

@return `true` if colors are inherited from the ''cell's'' parent; `false` if
not.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def colorsInherited = (flags & 0x10) != 0

//----------------------------------------------------------------------------------------------------------------------
/**
Report whether the ''bounding box present'' flag is set.

If this flag is set, then the cell's definition specifies a bounding box that
the cell's content will fit within; if clear, then no bounding box
specification is provided.

@note Bounding box information is read but discarded by ''Facsimile''.

@return `true` if ''bounding box data'' is present; `false` if not.
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def boundingBoxPresent = (flags & 0x40) != 0
}

//======================================================================================================================
/**
CellFlags companion object.
*/
//======================================================================================================================

private [cell] object CellFlags {

//----------------------------------------------------------------------------------------------------------------------
/**
Read ''cell'' flags from data stream.

@param scene Scene from which the flags are to be read.

@return Flags read, if valid.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Flags.html
Cell Flags]]
*/
//----------------------------------------------------------------------------------------------------------------------

  private [cell] def read (scene: CellScene) = {

/*
Sanity checks.
*/

    assertNonNull (scene)

/*
Read the flags from the data stream.
*/

    val flags = scene.readInt (LibResource ("anim.cell.CellFlags.read"))

/*
Convert to a line width and return.
*/

    new CellFlags (flags)
  }
}