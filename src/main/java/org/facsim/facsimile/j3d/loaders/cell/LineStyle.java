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
<p>Enumeration defining the line style of each cell element.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
enum LineStyle
{

/**
<p>Solid line-style.</p>
*/

    LS_SOLID ((byte) 0),

/**
<p>Dashed line-style</p>
*/

    LS_DASHED ((byte) 1),

/**
<p>Dotted line-style</p>
*/

    LS_DOTTED ((byte) 2),

/**
<p>Halftone line-style.</p>
*/

    LS_HALFTONE ((byte) 3);

/**
<p>Array associating each line style code with the associated enumerated line
style constant.</p>

<p>This array must be populated during static initialization of the
enumeration, since the (static) enumerations are created before the static
initializer is invoked.</p>
*/

    private final static LineStyle styleArray [];

/**
<p><em>Cell</em> line style code value.</p>
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
Create the array that allows each line style enumeration to be looked up from
the corresponding line style code.
*/

        assert LineStyle.values ().length <= Byte.MAX_VALUE;
        styleArray = new LineStyle [LineStyle.values ().length];

/*
Populate the array from the corresponding codes.

This loop will only work if the line styles are defined in order of their line
style codes.
*/

        byte styleCode = 0;
        for (LineStyle lineStyle: LineStyle.values ())
        {
            assert lineStyle.getCode () == styleCode;
            styleArray [styleCode] = lineStyle;
            ++styleCode;
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve line style instance from a tokenized cell data stream.<p>

@param tokenizer Tokenized stream from which the line style code will be read.
The next token to be read should be a cell line style code.

@return Cell line style instance read from the stream.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> line style code.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> line style code.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static LineStyle readStyle (CellTokenizer tokenizer)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Attempt to read the cell's line style code from the file.
*/

        byte lineStyleCode = tokenizer.readByteField ();

/*
Now convert the code into a cell line style.  It if is not a code we recognize,
then that would be a parse error.
*/

        if (lineStyleCode < 0 || lineStyleCode > styleArray.length)
        {
            throw new ParsingErrorException ();
        }

/*
Retrieve and return the matching value.
*/

        assert styleArray [lineStyleCode] != null;
        return styleArray [lineStyleCode];
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve default line style if not explicitly specified.</p>

@return Default line style to be assumed if not explicitly specified.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static LineStyle getDefault ()
    {
        return LS_SOLID;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Enumeration constant constructor.</p>

<p>Note that this constructor is called for each enumeration before the
enumeration's static initializer.</p>

@param code Corresponding line style code for each enumeration constant.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private LineStyle (byte code)
    {

/*
Store the result.
*/

        this.code = code;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve line style code for corresponding enumeration.</p>

@return The integer code for this line style.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final byte getCode ()
    {
        return this.code;
    }
}
