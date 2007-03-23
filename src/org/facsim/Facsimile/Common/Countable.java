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

Java source file for the Countable interface, and related elements, that are
integral members of the org.facsim.Facsimile.Common package.
===============================================================================
*/

package org.facsim.Facsimile.Common;

//=============================================================================
/**
<p>Interface for count nouns.</p>

<p>A <em>count noun</em> is a noun that refers to discrete, countable objects,
such as a <em>dog</em>, <em>meter</em>, <em>machine</em>, etc.  Count nouns
have both singular and plural forms and can be associated with quantities, such
as <em>one</em>, <em>two</em>, <em>several</em>, <em>each</em>, etc.  By
contrast, a <em>mass noun</em>, such as <em>hydrogen</em> or
<em>furniture</em>, cannot be counted.  Those curious in the subject should
refer to <a href="http://en.wikipedia.org/wiki/Count_noun">Wikipedia</a> for
further information.</p>

<p>In the context of Facsimile, count nouns are used to provide end-user
visible names for simulation objects and quantities.</p>

<p>The plural and singular forms of the noun may be identical, but neither can
be empty or null.</p>

@see CountNoun
*/
//=============================================================================

public interface Countable
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Singular form of name.</p>

<p>Usually, the name should be supplied in its lowercase form, such as
<em>machine</em>, <em>millimeter</em>, etc.  However, for certain nouns, it is
more appropriate for the name to be capitalized, such as <em>Newton</em>, or
<em>Brussels sprout</em>.</p>

@return A {@link String} object containing the singular form of this object's
name.  This value should never be null or empty.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getSingularName ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Plural form of name.</p>

<p>Usually, the name should be supplied in its lowercase form, such as
<em>machines</em>, <em>millimeters</em>, etc.  However, for certain nouns, it
is more appropriate for the name to be capitalized, such as <em>Newtons</em>,
or <em>Brussels sprouts</em>.</p>

@return A {@link String} object containing the plural form of this object's
name.  This value should never be null or empty.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public String getPluralName ();
}
