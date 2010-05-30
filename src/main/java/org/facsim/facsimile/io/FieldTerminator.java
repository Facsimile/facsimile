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

Java source file belonging to the org.facsim.facsimile.io package.
*/
//=============================================================================

package org.facsim.facsimile.io;

import net.jcip.annotations.Immutable;
import org.facsim.facsimile.util.PackagePrivate;

//=============================================================================
/**
<p>Enumeration defining how a {@linkplain Field} instance was terminated.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
enum FieldTerminator
{

/**
<p>Field was terminated by a regular field delimiter.</p>
*/

    FT_DELIMITER (false, false),

/**
<p>Field was terminated by an <em>end-of-line</em> sequence, otherwise known as
a <em>line termination</em> sequence.</p>
*/

    FT_EOL (true, false),

/**
<p>Field was terminated by an <em>end-of-file</em>, otherwise known as an
<em>end-of-stream</em>, sequence.</p>
*/

    FT_EOF (true, true);

/**
<p>Flag indicating whether associated stream must acknowledge an
<em>end-of-line</em> sequence before reading further fields.</p>

<p>Attempts to read fields from the stream without acknowledging the
<em>end-of-line</em> sequence will result in an exception.</p>
*/

    private final boolean mustAcknowledgeEOL;

/**
<p>Flag indicating whether associated field is the last in the associated
stream.</p>

<p>Attempts to read fields from a stream after such a field terminator has been
seen will result in a {@linkplain java.io.EOFException} being thrown.</p>
*/

    private final boolean signalsEOF;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Constructor.</p>

@param mustAcknowledgeEOL If <strong>true</strong>, indicates that the field
was terminated by an <em>end-of-line</em> sequence that must be acknowledged
before further reads may be made.</p>

@param signalsEOF If <strong>true</strong>, indicates that further attempts to
read from the associated stream will result in an
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private FieldTerminator (boolean mustAcknowledgeEOL, boolean signalsEOF)
    {
        this.mustAcknowledgeEOL = mustAcknowledgeEOL;
        this.signalsEOF = signalsEOF;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report whether the stream must acknowledge an <em>end-of-line</em> sequence
before further fields can be read.</p>

@return <strong>true</strong> if stream must acknowledge <em>end-of-line</em>
sequence before attempting further reads, <strong>false</strong> if an
acknowledgment is not required.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final boolean requiresEOLAcknowledgment ()
    {
        return this.mustAcknowledgeEOL;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report whether the stream has reached its end.</p>

@return <strong>true</strong> if the stream has been exhausted,
<strong>false</strong> if further fields may be read.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final boolean hasReachedEOF ()
    {
        return this.signalsEOF;
    }
}
