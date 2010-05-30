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
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p><em><a href="http://www.automod.com/">AutoMod</a> cell primitive bounding
box</em>.</p>

<p>A <em>bounding box</em> is a box with dimensions just large enough to
completely enclose the corresponding <em>cell primitive</em>'s graphics.  The
bounding box is aligned with its parent primitive's local axes, and is scaled
relative to its parent primitive.  If the primitive contains child primitives,
then the bounding box will be large enough to enclose all of the child
primitives.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
final class BoundingBox
{

/**
<p>Bounding box minimum X co-ordinate.</p>

<p>This value must be less than or equal to {@linkplain #xMax}.</p>
*/

    private final double xMin;

/**
<p>Bounding box maximum X coordinate.</p>

<p>This value must be greater than or equal to {@linkplain #xMin}.</p>
*/

    private final double xMax;

/**
<p>Bounding box minimum Y coordinate.</p>

<p>This value must be less than or equal to {@linkplain #yMax}.</p>
*/

    private final double yMin;

/**
<p>Bounding box maximum Y coordinate.</p>

<p>This value must be greater than or equal to {@linkplain #yMin}.</p>
*/

    private final double yMax;

/**
<p>Bounding box minimum Z coordinate.</p>

<p>This value must be less than or equal to {@linkplain #zMax}.</p>
*/

    private final double zMin;

/**
<p>Bounding box maximum Z coordinate.</p>

<p>This value must be greater than or equal to {@linkplain #zMin}.</p>
*/

    private final double zMax;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tokenizer constructor.</p>

<p>Constructs a bounding box instance by reading the coordinates of the box
directly from a tokenized <em>cell</em> data stream.</p>

@param tokenizer Tokenized <em>cell</em> data stream from which the bounding
box's coordinates are to be read.

@throws IncorrectFormatException Thrown if the tokenizer does not identify a
valid <em>cell</em>.

@throws ParsingErrorException Thrown if an error occurs while parsing the
tokenized <em>cell</em>.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    BoundingBox (CellTokenizer tokenizer)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Read the minimum and maximum X co-ordinates from the tokenized stream.  If the
minimum value is greater than the maximum value, then throw the parse error
exception.
*/

        this.xMin = tokenizer.readDoubleField ();
        this.xMax = tokenizer.readDoubleField ();
        if (this.xMin > this.xMax)
        {
            throw new ParsingErrorException ();
        }

/*
Read the minimum and maximum Y co-ordinates from the tokenized stream.  If the
minimum value is greater than the maximum value, then throw the parse error
exception.
*/

        this.yMin = tokenizer.readDoubleField ();
        this.yMax = tokenizer.readDoubleField ();
        if (this.yMin > this.yMax)
        {
            throw new ParsingErrorException ();
        }

/*
Read the minimum and maximum Z co-ordinates from the tokenized stream.  If the
minimum value is greater than the maximum value, then throw the parse error
exception.
*/

        this.zMin = tokenizer.readDoubleField ();
        this.zMax = tokenizer.readDoubleField ();
        if (this.zMin > this.zMax)
        {
            throw new ParsingErrorException ();
        }
    }
}
