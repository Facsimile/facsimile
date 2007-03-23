/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the:

    Free Software Foundation, Inc.
    51 Franklin St, Fifth Floor
    Boston, MA  02110-1301
    USA

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

Java source file for the SomeCountNoun class, and associated elements, that are
integral members of the org.facsim.Facsimile.CommonTest package.
===============================================================================
*/

package org.facsim.Facsimile.CommonTest;

import org.facsim.Facsimile.Common.CountNoun;

//=============================================================================
/**
 <p>This class is used by the {@link CountNounTest} text fixture.</p>

<p>It represents a simple class that extends the {@link CountNoun} class.</p>
 */
//=============================================================================

public final class SomeCountNoun
extends CountNoun
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Single form contructor.</p>

<p>This constructor is basically used to test the {@link CountNoun#CountNoun
(String)} constructor.</p>

@param name The singular and plural form of the name of this object.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public SomeCountNoun (String name)
    {
        super (name);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Singular and plural form contructor.</p>

<p>This constructor is basically used to test the {@link CountNoun#CountNoun
(String, String)} constructor.</p>

@param singularName The singular form of the name of this object.

@param pluralName The plural form of the name of this object.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public SomeCountNoun (String singularName, String pluralName)
    {
        super (singularName, pluralName);
    }
}
