/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2011, Michael J Allen.

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

Scala source file belonging to the org.facsim.facsimile.util package.
===============================================================================
*/

package org.facsim.facsimile.util

import java.text.MessageFormat
import java.util.ResourceBundle

//=============================================================================
/**
$facsimile resource provider.

Provides access to $facsimile resources, that are used throughout the library.
*/
//=============================================================================

object Resource
{

/**
$facsimile library resource bundle name.

Identifies the src/main/resources/{resourceBundleName}.properties file that
contains resources for the entire $facsimile library.  This value should not be
changed without good reason.
*/

  private val resourceBundleName = "facsimile"

/**
$facsimile library message resource bundle.

Resource bundle containing internationalized text messages suitable for
output.

Create the resource bundle.  The locale of the bundle defaults to the JVM's
default locale; if a locale-specific resource bundle matching the default
locale is available, then it will be loaded - otherwise, the "en_US" (United
States English) resource bundle will be loaded.

Note: A MissingResourceException may be thrown if the resource bundle cannot be
located.  Since the resource is part of the same jar file as the rest of
Facsimile, this should never fail; consequently, we should not attempt to
handle this exception and should allow the exception to terminate the
application.
*/

  private val bundle = ResourceBundle.getBundle (resourceBundleName)
  assert (bundle ne null)

//-----------------------------------------------------------------------------
/**
Retrieve and format a locale-specific string resource.

Retrieves the string resource corresponding to the supplied key from the
$facsimile resource bundle.  Optional arguments are used to format the
resulting string, which will be in the locale that best matches the user's
preference.

@param key Key used to identify the string resource to be retrieved.

@param arguments Arguments to be merged into, and formatted as part of, the
resulting string resource.

@return Locale-specific, formatted version of the requested string resource.

@throws java.lang.NullPointerException if supplied key is null.

@throws java.util.MissingResourceException if there is no string resource
indexed by the supplied key.

@throws java.lang.ClassCastException if the resource indexed by the supplied
key is not a string.

@throws java.lang.IllegalArgumentException if the arguments supplied do not
mesh with the formatting encoded within the retrieved string resource.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  private [facsimile] def format (key: String, arguments: Any*): String =
  MessageFormat.format (bundle.getString (key), arguments)
}
