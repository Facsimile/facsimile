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

Java source file for the Resource class, and associated elements, that are
integral members of the org.facsim.Facsimile.Common package.
===============================================================================
*/

package org.facsim.Facsimile.Common;

import java.text.*;
import java.util.*;

//=============================================================================
/**
<p>Resource information class.<p>

<p>This class provides common facilities to the entire package, including
locale information and internationalization information.</p>
*/
//=============================================================================

public final class Resource
{

/**
<p>Name of the resource bundle file to be processed.
*/

    private static final String bundleName;

/**
<p>Message resource bundle for all classes and methods within this package.</p>

<p>This resource bundle is used to internationalize all text output by the
library.</p>
*/

    private static final ResourceBundle bundle;

/**
<p>Message formatter associated with this message bundle.</p>

<p>The formatter is used to construct compound messages that take one or more
arguments.  Output is also formatted using the user's current default locale
information.</p>
*/

    private static final MessageFormat formatter;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Static initialization.</p>

<p>This pseudo-method initializes this class's static fields upon their first
use.</p> 
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {

/*
Initialise the name of the message bundle that we'll be processing.
*/

        bundleName = "org.facsim.Facsimile.Common.resource"; //$NON-NLS-1$

/*
Create the resource bundle for the user's currently specified locale.
*/
        
        bundle = ResourceBundle.getBundle (bundleName);

/*
Create the message formatter and ensure that it's locale is the same as that of
the message bundle.
*/

        formatter = new MessageFormat (""); //$NON-NLS-1$
        formatter.setLocale (bundle.getLocale ());
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve a locale-specified message.</p>

<p>This function retrieves a non-compound message from this package's message
resource bundle.  The resulting tring will be in the user's preferred language
- provided that a suitable translation has been provided.</p>

<p>This function should only be called from within this package.  Alas, it is
not possible to enforce this condition.</p>

@param resource A {@link String} object identifying the message resource to be
retrieved from the message bundle properties file.

@return A {@link String} object containing the locale-specific version of the
requested message.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static String format (String resource)
    {

/*
Retrieve, and return, the message in the user's language.
*/

        assert bundle != null;
        return bundle.getString (resource);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Construct a locale-specific, formatted message.</p>

<p>This function retrieves a compound message resource template from this
package's message resource bundle and populates it using the arguments
provided.  The resulting string is formatted in the appropriate manner for the
user's default locale, and be in the user's preferred language - provided that
a suitable translation has been provided.</p>

<p>Performance is an important requirement for this method, since it may be
heavily used by the library.</p>

<p>This function should only be called from within this package.  Alas, it is
not possible to enforce this condition.</p>

@param resource A {@link String} object identifying the compound message
resource template to be retrieved from the message bundle properties file.

@param arguments An array of {@link Object} instances whose values correspond
to the arguments required by the template.

@return A {@link String} object containing the populated and locale-specific
version of the message.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static synchronized String format (String resource, Object []
    arguments)
    {

/*
Apply the retrieved resource as a pattern to the formatter.  Then format the
string using the supplied arguments.
*/

        assert formatter != null;
        formatter.applyPattern (format (resource));
        return formatter.format (arguments);
    }
}
