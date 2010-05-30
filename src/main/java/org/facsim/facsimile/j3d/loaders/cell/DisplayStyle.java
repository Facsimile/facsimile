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

import net.jcip.annotations.Immutable;
import org.facsim.facsimile.util.PackagePrivate;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;

//=============================================================================
/**
<p>Enumeration for each <em>cell</em> display style.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
enum DisplayStyle
{

/**
<p>Wireframe display style.</p>
*/

    DS_WIREFRAME ((byte) 0),

/**
<p>Solid display style.</p>
*/

    DS_SOLID ((byte) 1),

/**
<p>Transparent display style #1.</p>
*/

    DS_TRANSPARENT_1 ((byte) 2),

/**
<p>Transparent display style #2.</p>
*/

    DS_TRANSPARENT_2 ((byte) 3),

/**
<p>Transparent display style #3.</p>
*/

    DS_TRANSPARENT_3 ((byte) 4),

/**
<p>Transparent display style #4.</p>
*/

    DS_TRANSPARENT_4 ((byte) 5),

/**
<p>Transparent display style #5.</p>
*/

    DS_TRANSPARENT_5 ((byte) 6),

/**
<p>Transparent display style #6.</p>
*/

    DS_TRANSPARENT_6 ((byte) 7),

/**
<p>Transparent display style #7.</p>
*/

    DS_TRANSPARENT_7 ((byte) 8),

/**
<p>Transparent display style #8.</p>
*/

    DS_TRANSPARENT_8 ((byte) 9),

/**
<p>Transparent display style #9.</p>
*/

    DS_TRANSPARENT_9 ((byte) 10),

/**
<p>Transparent display style #10.</p>
*/

    DS_TRANSPARENT_10 ((byte) 11),

/**
<p>Transparent display style #11.</p>
*/

    DS_TRANSPARENT_11 ((byte) 12),

/**
<p>Transparent display style #12.</p>
*/

    DS_TRANSPARENT_12 ((byte) 13),

/**
<p>Transparent display style #13.</p>
*/

    DS_TRANSPARENT_13 ((byte) 14),

/**
<p>Transparent display style #14.</p>
*/

    DS_TRANSPARENT_14 ((byte) 15),

/**
<p>Transparent display style #15.</p>
*/

    DS_TRANSPARENT_15 ((byte) 16);

/**
<p>Array associating each display style code with the associated enumerated
display style constant.</p>

<p>This array must be populated during static initialization of the
enumeration, since the (static) enumerations are created before the static
initializer is invoked.</p>
*/

    private final static DisplayStyle styleArray [];

/**
<p><em>Cell</em> display style code value.</p>
*/

    private final byte code;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Static initialization.</p>

<p>Note that static initialization is performed <em>after</em> each enumeration
constant has been constructed.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {

/*
Create the array that allows each display style enumeration to be looked up
from the corresponding display style code.
*/

        assert DisplayStyle.values ().length <= Byte.MAX_VALUE;
        styleArray = new DisplayStyle [DisplayStyle.values ().length];

/*
Populate the array from the corresponding codes.

This loop will only work if the display styles are defined in order of their
display style codes.
*/

        byte styleCode = 0;
        for (DisplayStyle displayStyle: DisplayStyle.values ())
        {
            assert displayStyle.getCode () == styleCode;
            styleArray [styleCode] = displayStyle;
            ++styleCode;
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve display style instance from a tokenized cell data stream.<p>

@param tokenizer Tokenized stream from which the display style code will be
read.  The next token to be read should be a cell display style code.

@return Cell display style instance read from the stream.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> display style code.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> display style code.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static DisplayStyle readStyle (CellTokenizer tokenizer)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Attempt to read the cell's display style code from the file.
*/

        byte displayStyleCode = tokenizer.readByteField ();

/*
Now convert the code into a cell display style.  It if is not a code we
recognize, then that would be a parse error.
*/

        if (displayStyleCode < 0 || displayStyleCode > styleArray.length)
        {
            throw new ParsingErrorException ();
        }

/*
Retrieve and return the matching value.
*/

        assert styleArray [displayStyleCode] != null;
        return styleArray [displayStyleCode];
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve default display style if not explicitly specified.</p>

@return Default display style to be assumed if not explicitly specified.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static DisplayStyle getDefault ()
    {
        return DS_SOLID;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Enumeration constant constructor.</p>

<p>Note that this constructor is called for each enumeration before the
enumeration's static initializer.</p>

@param code Corresponding display style code for each enumeration constant.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private DisplayStyle (byte code)
    {

/*
Store the result.
*/

        this.code = code;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve display style code for corresponding enumeration.</p>

@return The integer code for this display style.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final byte getCode ()
    {
        return this.code;
    }
}
