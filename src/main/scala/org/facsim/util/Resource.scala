/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.util package.
*/
//=============================================================================

package org.facsim.util

import java.net.URL
import java.text.MessageFormat
import java.util.MissingResourceException
import java.util.ResourceBundle
import scala.swing.Swing

//=============================================================================
/**
Provides access to a locale-specific resource bundle belonging to a library or
application.

Further information on how locale-specific resources are identified is at
[[http://docs.oracle.com/javase/6/docs/api/java/util/ResourceBundle.html
java.util.ResourceBundle]]

The user's current preferred locale is identified through a call to:
[[http://docs.oracle.com/javase/6/docs/api/java/util/Locale.html#getDefault()
java.util.Local.getDefault()]]

@constructor Retrieve the resource bundle with the specified name.

@param bundleName Identifies the base name of the resource bundle to be loaded.

@throws java.lang.NullPointerException if bundleName is null.

@throws java.util.MissingResourceException if a resource bundle with base name
bundleName cannot be found.

@see [[http://docs.oracle.com/javase/6/docs/api/java/util/ResourceBundle.html
java.util.ResourceBundle]]

@see [[http://docs.oracle.com/javase/6/docs/api/java/util/Locale.html
java.util.Locale]]

@since 0.0
*/
//=============================================================================

class Resource (bundleName: String) {

/**
Create resource bundle reference.

The locale of the bundle defaults to the JVM's default locale; if a
locale-specific resource bundle matching the default locale is available, then
it will be loaded - otherwise, the "en_US" (United States English) resource
bundle will be loaded.
*/

  private final val bundle = ResourceBundle.getBundle (bundleName)
  assert (bundle ne null)

//-----------------------------------------------------------------------------
/**
Retrieve and format a locale-specific string resource.

Retrieves the string resource corresponding to the supplied key from the
application resource bundle.  Optional arguments are used to populate the
formatted version of the resulting string, which will be in the locale that
best matches the user's preference.

@param key Key used to identify the string resource to be retrieved.

@param arguments Arguments to be merged into, and formatted as part of, the
resulting string resource.  May be omitted if no arguments are required.

@return Locale-specific, formatted version of the requested string resource.

@throws java.lang.NullPointerException if supplied key is null.

@throws java.util.MissingResourceException if there is no string resource
indexed by the supplied key.

@throws java.lang.ClassCastException if the resource indexed by the supplied
key is not a string.

@throws java.lang.IllegalArgumentException if the retrieved string is invalid
or if the arguments supplied are of the wrong type for the corresponding format
elements in the retrieved string.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def format (key: String, arguments: Any*) = {

/*
To convert a Scala vararg to a Java vararg, while boxing numbers requires this
rather ugly code...
*/

    MessageFormat.format (bundle.getString (key), arguments.map (_.asInstanceOf
    [AnyRef]): _*)
  }

//-----------------------------------------------------------------------------
/**
Retrieve an image file resource and return as an icon.

Typically, resource images are located in the images folder of the
application's, or library's, jar file.  For example, to retrieve an image
resource named "Icon.png" from such a directory, this function would be called
with a filename of "/images/Icon.png".

@note Resource files are typically not found unless they begin with a "/".
However, this is not enforced - expect to see a
[[java.util.MissingResourceException]] thrown if filename does not begin with a
slash.

@note In Java Swing, an ''icon'' is ''a small fixed size picture, typically
used to decorate components''.  This definition includes any images that may be
included in a Swing frame or dialog box, not just the square images associated
with a window or application/file.

@todo How should corrupt files be handled?  That is, what if the requested file
was found, but doesn't contain an image, or contains corrupt data?

@param filename File containing the image to be retrieved.

@return Retrieved image, if successful.

@throws java.lang.NullPointerException if filename is null.

@throws java.util.MissingResourceException if there is no resource with the
indicated file name.  In many cases, this exception will be thrown if filename
does not begin with a slash.

@note Images resources are not stored in resource bundles.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def getIcon (filename: String) = {

/*
Retrieve the URL of the requested image file.  If the file could not be found,
a null pointer will be returned instead - in which case, we throw the
MissingResourceException.  However, if filename is null, a NullPointerException
is thrown instead.
*/

    val url = getClass ().getResource (filename)
    if (url eq null) throw new MissingResourceException (
    "Image file not found", "javax.swing.ImageIcon", filename)

/*
The file appears to exist, so load it up.
*/

    Swing.Icon (url)
  }
}