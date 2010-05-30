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

import org.facsim.facsimile.util.PackagePrivate;
import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p>Cell flag bits.</p>

<p>Classifies the bit-flag values in each cell primitive's header.<p>
*/
//=============================================================================

@Immutable
@PackagePrivate
enum CellFlag
{

/**
<p>Flag indicating that cell primitive attributes are present.</p>
*/

    CF_ATTRIBUTES_PRESENT ((byte) 0),

/**
<p>Flag indicating that cell primitive kinematic joint data is present.</p>
*/

    CF_JOINT_DATA_PRESENT ((byte) 1),

/**
<p>Flag indicating that cell primitive geometry data is present.</p>
*/

    CF_CELL_GEOMETRY_PRESENT ((byte) 2),

/**
<p>Flag indicating that cell primitive geometry data is in matrix form.</p>

<p>This flag should not be set unless the @{link CF_CELL_GEOMETRY_PRESENT} flag
is also set.</p>
*/

    CF_GEOMETRY_IN_MATRIX_FORM ((byte) 3),

/**
<p>Flag indicating that cell primitive inherits its colors from its parent.</p>
*/

    CF_INHERIT_COLOUR ((byte) 4),

/**
<p>Cell primitive flag currently of unknown significance.</p>
*/

    CF_UNKNOWN1 ((byte) 5),

/**
<p>Flag indicating that cell primitive bounding box data is present.</p>
*/

    CF_BOUNDING_BOX_PRESENT ((byte) 6);

/**
<p>Value of each flag.</p>
*/

    private final short flag;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Enumeration constant constructor.</p>

@param bitNumber The number of the bit (bit numbering commences at 0) that
encodes this flag.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private CellFlag (byte bitNumber)
    {
        assert bitNumber >= 0 && bitNumber < 16;
        this.flag = (short) (1 << bitNumber);
        assert this.flag > 0;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Determine whether the indicated value has the corresponding flag set.</p>

@param value Value to be tested for presence of corresponding flag.

@return <strong>true</strong> if value contains the corresponding flag, or
<strong>false</strong> otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final boolean isSetIn (short value)
    {
        return (value & this.flag) == 0? false: true;
    }
}
