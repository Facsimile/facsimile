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

import java.io.IOException;
import java.lang.reflect.Constructor;
import javax.media.j3d.Group;
import javax.vecmath.Point3d;
import net.jcip.annotations.Immutable;
import org.facsim.facsimile.io.Tokenizer;
import org.facsim.facsimile.util.PackagePrivate;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;

//=============================================================================
/**
<p>Abstract base class for all <em><a
href="http://www.automod.com/">AutoMod</a></em> <em>cell</em>-format 3D
graphics primitives.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
abstract class Cell
{

/**
<p>Maximum permitted length of a cell element's name.</p>

<p>This value can be exceeded without error in ACE/AutoMod; however, such names
are not recommended and should be accepted only after issuing a warning.</p>
*/

    private static final int MAX_NAME_LENGTH;

/**
<p>Set of characters that may be used to form the first character of a cell
element's name.</p>
*/

    private static final String VALID_FIRST_CHARACTERS;

/**
<p>Set of characters that may be used to form an element's name.</p>
*/

    private static final String VALID_NAME_CHARACTERS;

/**
<p>Cell scene being populated.</p>
*/

    private final Scene scene;

/**
<p>Cell's parent set.</p>

<p>Cell set to which this cell belongs.</p>
*/

    private final BaseSet parent;

/**
<p>Cell element's name.</p>

<p>Cell names do not have to be unique (except in the case of {@linkplain
Definition} primitives), and may even be <strong>null</strong>.</p>
*/

    private final String name;

/**
<p>Bounding box holding this cell and its contents.</p>

<p>This value is <strong>null</strong> if the cell has no bounding box data
associated with it.</p>
*/

    private final BoundingBox boundingBox;

/**
<p>Flag indicating whether this cell element inherits its colors from its
parent.</p>
*/

    private final boolean colorInherited;

/**
<p>Cell's face color.</p>

<p>This value is ignored unless {@linkplain #colorInherited} is
<strong>false</strong>.</p>
*/

    private final CellColor faceColor;

/**
<p>Cell's edge color.</p>

<p>In <em>AutoMod</em> V10.0 and above, when <em>VR Graphics</em> are enabled,
this value is always ignored and {@linkplain #faceColor} is used instead.</p>

<p>This value is ignored unless {@linkplain #colorInherited} is
<strong>false</strong>.</p>
*/

    private final CellColor edgeColor;

/**
<p>Cell's linestyle.</p>
*/

    private final LineStyle lineStyle;

/**
<p>Cell's line width, in pixels.</p>
*/

    private final byte lineWidth;

/**
<p>Cell's display style.</p>
*/

    private final DisplayStyle displayStyle;

/**
<p>Cell's joint data.</p>

<p>This data should only be present if the cell is a set.</p>
*/

    private final Joint joint;

/**
<p>Cell's geometry.</p>
*/

    private final Geometry geometry;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Static element initialization.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {

/*
The maximum number of characters supported in a cell name (officially).
*/

        MAX_NAME_LENGTH = 22;

/*
Initialize the characters that may be used to name a cell.
*/

        VALID_FIRST_CHARACTERS =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        VALID_NAME_CHARACTERS = VALID_FIRST_CHARACTERS + "0123456789_";
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Read a <em>cell</em> scene from the indicated tokenizer.</p>

@param tokenizer Tokenized <em>cell</em> data stream to be processed into a
<em>Java3D</em> scene.

@return Scene populated from the tokenized <em>cell</em> stream.

@throws IncorrectFormatException If the tokenizer does not identify valid
<em>cell</em> data.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> scene.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static CellScene readCellData (CellTokenizer tokenizer)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Create the cell scene and provide it with the corresponding loader, should it
be necessary to retrieve loader-level information.
*/

        CellScene scene = new CellScene (tokenizer.getLoader ());

/*
Read the root cell of the object.  There will be exactly one cell primitive at
this level of the cell scene.
*/

        assert scene != null;
        assert tokenizer != null;
        readNextCell (scene, tokenizer, null);

/*
Now return the populated Java3D scene.
*/

        return scene;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Read next cell from tokenizer.</p>

<p>Note that if the cell is a container, then this function will read all of
the cells contained within it before returning.</p>

@param scene Scene instance being populated.  This value should never be null.

@param tokenizer Tokenizer from which the next cell will be read.  This value
should never be null.

@param owner Base set to which this cell belongs.  If this value is null, then
the cell is the root of the scene.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static void readNextCell (CellScene scene, CellTokenizer tokenizer, BaseSet
    owner)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Retrieve the cell type code from the tokenizer.
*/

        CellType cellType = CellType.readType (tokenizer);

/*
OK.  By this point, we've read a valid cell type code from the tokenizer and
converted it into a valid cell type.  Now use the factory method to create an
instance of this class that will read the cell's data from the tokenizer.
*/

        assert scene != null;
        assert owner != null;
        cellType.cellFactory (scene, tokenizer, owner);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that a line width value is value.</p>

@param lineWidth Line width value, in pixels, to be validated.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void validateLineWidth (byte lineWidth)
    throws IncorrectFormatException, ParsingErrorException
    {
        if (lineWidth < 1 || lineWidth > 8)
        {
            throw new ParsingErrorException ();
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that a cell element name is valid.</p>

@param name Name to be validated.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void validateCellName (String name)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
The name cannot be empty or null.
*/

        if (name == null || name == "")
        {
            throw new ParsingErrorException ();
        }

/*
Issue a warning if the name is too long.  This also invalidates the name, but
not seriously.
*/

        if (name.length () > MAX_NAME_LENGTH)
        {
            // TODO: Add cell file warning code here.
        }

/*
Check that the name is valid.  We'll allow any first character (but we may
issue a warning later).
*/

        for (int i = 0; i < name.length (); ++i)
        {
            if (VALID_NAME_CHARACTERS.indexOf (name.charAt (i)) == -1)
            {
                throw new ParsingErrorException ();
            }
        }

/*
If the first character of the name is not strictly valid, then issue a warning.
*/

        if (VALID_FIRST_CHARACTERS.indexOf (name.charAt (1)) == -1)
        {
            // TODO: Add cell file warning code here.
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Cell tokenizer constructor.</p>

<p>Constructs a basic <em>cell primitive</em> from tokenized cell data.</p>

@param scene Scene that is being populated.

@param tokenizer Tokenizer from which this cell element is to be read.

@param owner Base set node that is the owner of this cell's scene element.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    Cell (CellScene scene, CellTokenizer tokenizer, BaseSet owner)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Store the scene being populated.
*/

        assert scene != null;
        this.scene = scene;

/*
Store the owner of this cell.  If this value is not null, then this object is
the root object of the scene.

Note: The parent, if non-null, will take care of adding this child cell object
to itself; it's not the responsibility of the child to add itself to its
parent.
*/

        this.parent = owner;

/*
Now read the cell flags field from the tokenizer, so that we can determine
how to process the remainder of the basic cell data.
*/

        short flags = tokenizer.readShortField ();

/*
OK.  If we make it to here, we have a valid set of cell flags.

Start off by reading the cell's bounding box data, if present.  If this data is
not present, then simply create a null bounding box.
*/

        if (CellFlag.CF_BOUNDING_BOX_PRESENT.isSetIn (flags))
        {
            this.boundingBox = new BoundingBox (tokenizer);
        }
        else
        {
            this.boundingBox = null;
        }

/*
Read the cell's attributes, if present.  If not present, then default values
need to be provided.
*/

        if (CellFlag.CF_ATTRIBUTES_PRESENT.isSetIn (flags))
        {
            this.faceColor = CellColor.readColor (tokenizer);
            this.edgeColor = CellColor.readColor (tokenizer);
            this.lineStyle = LineStyle.readStyle (tokenizer);
            this.lineWidth = tokenizer.readByteField ();
            this.displayStyle = DisplayStyle.readStyle (tokenizer);
            this.name = tokenizer.readField ();
        }
        else
        {
            this.faceColor = CellColor.getDefault ();
            this.edgeColor = CellColor.getDefault ();
            this.lineStyle = LineStyle.getDefault ();
            this.lineWidth = 1;
            this.displayStyle = DisplayStyle.getDefault ();
            this.name = "none";
        }
        validateLineWidth (this.lineWidth);
        validateCellName (this.name);

/*
Do we have any joint data?
*/

        if (CellFlag.CF_JOINT_DATA_PRESENT.isSetIn (flags))
        {

/*
If this type of cell file cannot have joint data associated with it, then issue
a parse error.
*/

            if (!canAcceptJointData ())
            {
                throw new ParsingErrorException ();
            }

/*
OK, so we have joint data - read it in.
*/

            this.joint = new Joint (tokenizer);
        }

/*
Otherwise, we do not have any joint data.
*/

        else
        {
            this.joint = null;
        }

/*
OK.  If we have geometry data present, then read that in now.
*/

        if (CellFlag.CF_CELL_GEOMETRY_PRESENT.isSetIn (flags))
        {
            this.geometry = new Geometry (tokenizer,
            CellFlag.CF_GEOMETRY_IN_MATRIX_FORM.isSetIn (flags));
        }

/*
Otherwise, note that we have no geometry data.
*/

        else
        {
            this.geometry = null;
        }

/*
Determine whether the color is inherited.
*/

        this.colorInherited = CellFlag.CF_INHERIT_COLOUR.isSetIn (flags);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Determine whether this cell type supports joint data.</p>

@return <strong>true</strong> if the cell type supports dynamic joint and
terminal control frame (TCF) data, <strong>false</strong> otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected abstract boolean canAcceptJointData ();
}
