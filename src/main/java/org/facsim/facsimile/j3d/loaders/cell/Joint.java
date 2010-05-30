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
<p>Class storing <em>cell</em> primitive joint data.</p>

<p>In <em>cell</em> scenes, <em>joints</em> are used to implement
<em>kinematic</em> motion.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
final class Joint
{

/**
<p>Joint type.</p>
*/

    private final JointType jointType;

/**
<p>Joint velocity.</p>

<p>Units of measure to be determined.</p>

<p>This field is only used if {@linkplain #jointType} is not JT_TCF_ONLY.</p>
*/

    private final double velocity;

/**
<p>Joint minimum displacement.</p>

 <p>This value cannot be greater than the {@linkplain #maximum}
 displacement.</p>

<p>This field is only used if {@linkplain #jointType} is not JT_TCF_ONLY.</p>
*/

    private final double minimum;

/**
<p>Joint maximum displacement.</p>

<p>This value cannot be less than the {@linkplain #minimum} displacement.</p>

<p>This field is only used if {@linkplain #jointType} is not JT_TCF_ONLY.</p>
*/

    private final double maximum;

/**
<p>Joint current displacement.</p>

<p>This value cannot be less than the {@linkplain #minimum} displacement, not
can it be greater than the {@linkplain #maximum} displacement.</p>

<p>This field is only used if {@linkplain #jointType} is not JT_TCF_ONLY.</p>
*/

    private final double current;

/**
<p>Flag indicating whether joint has associated terminal control frame (TCF)
data.</p>

<p>This field must be true if {@linkplain #jointType} is JT_TCF_ONLY.</p>
*/

    private final boolean tcfDataPresent;

/**
<p>The joint's dynamic geometry.</p>
*/

    private final Geometry dynamicGeometry;

/**
<p>The joint's TCF geometry.</p>
*/

    private final Geometry tcfGeometry;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Default constructor.<p>

<p>Constructs a joint with default settings.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    Joint ()
    {

/*
Default initialisation for a joint (same as ACE).
*/

        this.jointType = JointType.getDefault ();
        this.velocity = 10.0;
        this.minimum = 0.0;
        this.maximum = 0.0;
        this.current = 0.0;
        this.tcfDataPresent = false;
        this.dynamicGeometry = new Geometry ();
        this.tcfGeometry = null;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tokenizer constructor.</p>

<p>Constructs a joint from a stream of cell data.</p>

@param tokenizer Tokenized stream from which the <em>cell</em> joint data, will
be read.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> data stream.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> data stream.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    Joint (CellTokenizer tokenizer)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Read in the joint type.
*/

        this.jointType = JointType.readType (tokenizer);

/*
Now for the joint velocity.  If this value is less than zero, then throw a
parsing error exception.
*/

        this.velocity = tokenizer.readDoubleField ();
        if (this.velocity < 0.0)
        {
            throw new ParsingErrorException ();
        }

/*
Now read the joint minimum and maximum values.  If the minimum is greater than
the maximum, then throw a parsing error exception.
*/

        this.minimum = tokenizer.readDoubleField ();
        this.maximum = tokenizer.readDoubleField ();
        if (this.minimum > this.maximum)
        {
            throw new ParsingErrorException ();
        }

/*
Now for the current joint value.  This value must lie with the minimum and
maximum values.
*/

        this.current = tokenizer.readDoubleField ();
        if (this.current < this.minimum || this.current > this.maximum)
        {
            throw new ParsingErrorException ();
        }

/*
Now read in the TCF data present flag.  If the joint type is JT_TCF_ONLY, and
this value is false, then throw a parsing error exception.
*/

        this.tcfDataPresent = tokenizer.readBooleanField ();
        if (!this.tcfDataPresent && this.jointType == JointType.JT_TCF_ONLY)
        {
            throw new ParsingErrorException ();
        }

/*
Now read in the dynamic joint geometry.
*/

        this.dynamicGeometry = new Geometry (tokenizer, false);

/*
The next field is a curio.  It must be an integer value of 0.
*/

        String requiredField = tokenizer.readField ();
        if (requiredField != "0")
        {
            throw new ParsingErrorException ();
        }

/*
If we have TCF data present, then read it in.  Otherwise, signal that we have
none.
*/

        if (this.tcfDataPresent)
        {
            this.tcfGeometry = new Geometry (tokenizer, false);
        }
        else
        {
            this.tcfGeometry = new Geometry ();
        }
    }
}
