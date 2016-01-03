/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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

import org.facsim.anim.Mesh
import scalafx.scene.Node
import scalafx.scene.shape.CullFace
import scalafx.scene.shape.DrawMode
import scalafx.scene.shape.MeshView

//=============================================================================
/**
Abstract base class for all ''[[http://www.automod.com/ AutoMod]] cell''
primitives implemented as 3D meshes.

@constructor Construct a new 3D-mesh-based primitive from the data stream.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive. If this value is `None`, then
this cell is the scene's root cell.

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.
*/
//=============================================================================

private [cell] abstract class Mesh3D (scene: CellScene, parent: Option [Set])
extends Cell (scene, parent) {

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  private [cell] final override def toNode = {

/*
Have the sub-class create and populate a mesh that we will associate with a
mesh view.
*/

    new MeshView (cellMesh.triangleMesh) {

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
Use the default back cull-face option.
*/

      cullFace = faceCulling

/*
Determine how the mesh is to be rendered.

(Note: This may not work too well for elements such as "Arc", which look very
different in wireframe compared to solid).
*/

      drawMode = if (isWireframe) DrawMode.Line
      else DrawMode.Fill
    }
  }

//-----------------------------------------------------------------------------
/**
Determine the cull face setting for this shape.

This default version of the function applies the default cull face setting that
removes the back face. Shapes that require an alternative cull face setting
should override this method.

@return Cull face setting for this shape.
*/
//-----------------------------------------------------------------------------

  protected [cell] def faceCulling: CullFace = CullFace.Back

//-----------------------------------------------------------------------------
/**
Create a mesh to represent this cell and return it.

@return Mesh representing the cell.
*/
//-----------------------------------------------------------------------------

  protected [cell] def cellMesh: Mesh
}