/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2010, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

	http://www.facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

	http://www.facsim.org/Documentation/CodingStandards/
===============================================================================
$Id$

Java source file belonging to the org.facsim.facsimile.j3d.loaders.cell
package.
*/
//=============================================================================

package org.facsim.facsimile.j3d.loaders.cell;

import javax.media.j3d.BranchGroup;
import org.facsim.facsimile.util.PackagePrivate;
import com.sun.j3d.loaders.SceneBase;

//=============================================================================
/**
<p>Stores details about an <em><a href="http://www.automod.com/">AutoMod</a>
cell</em> scene that has been translated into a <em>Java3D</em> scene.</p>
*/
//=============================================================================

@PackagePrivate
class CellScene
extends SceneBase
{

/**
<p>Reference to the loader that performed the conversion.</p>
*/

    private final CellLoader loader;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Loader constructor.</p>

<p>Construct the loader from the supplied loader instance, to make it easier to
query the loader during scene construction.</p>

@param cellLoader Loader employed to process the scene.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    CellScene (CellLoader cellLoader)
    {
        super ();
        this.loader = cellLoader;

/*
Assign the scene its branch group node.
*/

        setSceneGroup (new BranchGroup ());
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve associated loader instance.</p>

@return Associated loader instance.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    CellLoader getLoader ()
    {
        return this.loader;
    }
}
