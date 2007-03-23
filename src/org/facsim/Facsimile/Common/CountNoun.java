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

Java source file for the CountNoun class, and associated elements, that are
integral members of the org.facsim.Facsimile.Common package.
===============================================================================
*/

package org.facsim.Facsimile.Common;

//=============================================================================
/**
<p>Class representing a count noun.</p>

<p>This class is intended to be used as a base class by other count noun
objects.</p>

@see Countable
*/
//=============================================================================

public abstract class CountNoun
implements Countable
{

/**
<p>Singular form of the count noun.</p>
*/

    private final String singular;

/**
<p>Plural form of the count noun.</p>
*/

    private final String plural;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Single form contructor.</p>

<p>Count nouns that have identical singular and plural forms, such as
<em>sheep</em>, may use this constructor.</p>

@param name The singular and plural form of the name of this object.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public CountNoun (String name)
    {
        this (name, name);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Singular and plural form contructor.</p>

<p>Count nouns that have dissimilar singular and plural forms, such as
<em>goat</em> and <em>goats</em>, should use this constructor.</p>

@param singularName The singular form of the name of this object.

@param pluralName The plural form of the name of this object.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public CountNoun (String singularName, String pluralName)
    {

/*
Validate both arguments.
*/

        if (Util.isNullOrEmpty (singularName))
        {
            throw new EmptyStringArgumentException
            ("singularName"); //$NON-NLS-1$
        }
        if (Util.isNullOrEmpty (pluralName))
        {
            throw new EmptyStringArgumentException
            ("pluralName"); //$NON-NLS-1$
        }

/*
Store the arguments.
*/

        this.singular = singularName;
        this.plural = pluralName;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
@see Countable#getSingularName()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final String getSingularName ()
    {
      return this.singular;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
@see Countable#getPluralName()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public final String getPluralName ()
    {
        return this.plural;
    }
}
