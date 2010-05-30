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
<p><em>Cell</em> primitive joint type enumerator.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
enum JointType
{

/**
<p>Associated joint is a <em>terminal control frame</em> (<em>TCF</em>) only;
there is no kinematic data present.</p>
*/

    JT_TCF_ONLY ((byte) 0),

/**
<p>Associated joint is rotational.</p>

<p><em>TCF</em> may also be present.</p>
*/

    JT_ROTATIONAL ((byte) 1),

/**
<p>Associated joint is translational.</p>

<p><em>TCF</em> may also be present.</p>
*/

    JT_TRANSLATIONAL ((byte) 2);

/**
<p>Array associating each joint type code with the associated enumerated
joint type constant.</p>

<p>This array must be populated during static initialization of the
enumeration, since the (static) enumerations are created before the static
initializer is invoked.</p>
*/

    private final static JointType typeArray [];

/**
<p>Joint type code.</p>
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
Create the array that allows each type enumeration to be looked up from the
corresponding type code.
*/

        assert JointType.values ().length <= Byte.MAX_VALUE;
        typeArray = new JointType [JointType.values ().length];

/*
Populate the array from the corresponding codes.

This loop will only work if the colors are defined in order of their color
codes.
*/

        byte typeCode = 0;
        for (JointType jointType: JointType.values ())
        {
            assert jointType.getCode () == typeCode;
            typeArray [typeCode] = jointType;
            ++typeCode;
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve default joint type if not explicitly specified.</p>

@return Default joint type to be assumed if not explicitly specified.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static JointType getDefault ()
    {
        return JT_ROTATIONAL;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve JointType instance from a tokenized cell data stream.<p>

@param tokenizer Tokenized stream from which the joint type code will be read.
The next token to be read should be a joint type code.

@return JointType instance read from the stream.

@throws IncorrectFormatException If the tokenizer does not identify a valid
joint type code.

@throws ParsingErrorException If an error occurs while parsing the tokenized
joint type code.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static JointType readType (CellTokenizer tokenizer)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Read the joint type code from the tokenized stream.
*/

        byte jointTypeCode = tokenizer.readByteField ();

/*
Now convert the code into a joint type.  It if is not a code we recognize, then
that would be a parse error.
*/

        if (jointTypeCode < 0 || jointTypeCode > typeArray.length)
        {
            throw new ParsingErrorException ();
        }

/*
Retrieve and return the matching value.
*/

        assert typeArray [jointTypeCode] != null;
        return typeArray [jointTypeCode];
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Enumeration constant constructor.</p>

<p>Note that this constructor is called for each enumeration before the
enumeration's static initializer.</p>

@param code Corresponding joint type code for each enumeration constant.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private JointType (byte code)
    {

/*
Store the result.
*/

        this.code = code;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve joint type code for corresponding enumeration.</p>

@return The byte code for this joint type.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final byte getCode ()
    {
        return this.code;
    }
}
