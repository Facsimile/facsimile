/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

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

import java.io.File
import java.net.URI
import java.util.jar.JarFile
import org.facsim.LibResource
import org.joda.time.DateTime

//=============================================================================
/**
Provide ''manifest'' information for the user's library or application.

To use this trait, simply create a concrete subclass instance; this instance
can then be used to access the manifest information of the package (whether a
library or an application) to which it belongs.

The manifest is provided by a file named `MANIFEST.MF` located in the
`/META-INF` folder of the ''Java'' archive file (or ''jar file'') that contains
the concrete `Manifest` subclass definition.  If this subclass does not belong
to a jar file, then no manifest information will be available.

@since 0.0
*/
//=============================================================================

trait Manifest {

/**
Manifest for the application.
*/

  private final val manifest = {

/*
If there's a simpler way of doing this, feel free to implement it!

This is expressed as a path within a jar file.  It is obtained by retrieving
the name of the package to which this instance belongs, replacing any periods
('.') with slashes ('/'), then prefixing the result with another slash.

For example, if our concrete subclass has the fully-qualified name
`com.mycompany.myproject.MyManifestClass`, then the resulting package path will
be `/com/mycompany/myproject`.

@note It doesn't matter whether the package containing the class is the root
package of the jar file.
*/

    val packagePath = '/' +: getClass.getPackage.getName.map (c => if (c ==
    '.') '/' else c)

/*
Now retrieve the URL for the package path.  If this value is null, indicating
that we do not have an associated jar file, then return None as the manifest
value.
*/

    val jarURL = getClass.getResource (packagePath)
    if (jarURL eq null) None

/*
Otherwise, we have some further processing...
*/

    else {

/*
OK.  So that jarURL will be of the (String) form:

jar:file:/{path-of-jar-file}!{pacakgePath}

In order to create a jar file instance, we need to convert this into a
hierarchical URI.  We do this using a regular expression extraction.  What we
want is just the file:{path-of-jar-file} portion of the URL.
*/

      val jarExtractor = """^jar\:(.+)\!.+$""".r
      val jarURI = jarURL.toString match {
        case jarExtractor (uriString) => new URI (uriString)
        case _ => throw new Error ()
      }

/*
Now obtain a JarFile object from this URI.
*/

      val jarFile = new JarFile (new File (jarURI))
      Option (jarFile.getManifest ())
    }
  }

/**
Entries defined in the manifest.
*/

  private final val entries = manifest match {
    case None => new java.util.jar.Attributes ()
    case Some (m) => m.getMainAttributes
  }

//-----------------------------------------------------------------------------
/**
Retrieve specified manifest attribute as a string.

@param name Name of attribute to be retrieved.

@return Attribute's value as a string, if the attributes is defined.

@throws java.util.NoSuchElementException if there is there is no attribute with
the specified name.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def getAttribute (name: String) = {

/*
Attempt to retrieve the attribute value.
*/

    try {
      entries.getValue (name)
    }

/*
If there was no such attribute, then throw a NoSuchElementException, as
required.
*/

    catch {
      case e: IllegalArgumentException => throw new
      NoSuchElementException (LibResource ("util.Manifest.NoSuchElement",
      name))
    }
  }

//-----------------------------------------------------------------------------
/**
Retrieve the build timestamp of this manifest.

This is a custom field that will likely be unavailable for many packages.  To
include it in your jar files, ensure that the META-INF/MANIFEST.MF file
contains an entry of the following form:

`Build-Timestamp: yyyy-MM-ddTHH:mm:ss.SSSZ`

Refer to the joda.time.DateTime.parse(String) documentation for further
information on supported formats.

@return Date and time that package associated with the manifest was built.

@throws java.util.NoSuchElementException if the manifest has no build
timestamp.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def buildTimestamp = DateTime.parse (getAttribute ("Build-Timestamp"))

//-----------------------------------------------------------------------------
/**
Title of this application or library.

@return Manifest implementation title.

@throws java.util.NoSuchElementException if the manifest has no title field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def title = getAttribute ("Implementation-Title")

//-----------------------------------------------------------------------------
/**
Vendor publishing this application or library.

This may be an individual or an organization, depending upon circumstances.

@return Manifest implementation vendor.

@throws java.util.NoSuchElementException if the manifest has no vendor field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def vendor = getAttribute ("Implementation-Vendor")

//-----------------------------------------------------------------------------
/**
Version of this release of this application or library.

@return Manifest implementation version.

@throws java.util.NoSuchElementException if the manifest has no version field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def version = new Version (getAttribute ("Implementation-Version"))

//-----------------------------------------------------------------------------
/**
Title of the specification to which this application or library conforms.

@return Manifest specification title.

@throws org.facsim.inf.MissingManifestDataException if
the manifest specification title field is undefined.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def specTitle = getAttribute ("Specification-Title")

//-----------------------------------------------------------------------------
/**
Vendor that produced the specification to which this application or library
conforms.

This may be an individual or an organization, depending upon circumstances.

@return Manifest specification vendor.

@throws java.util.NoSuchElementException if the manifest has no specification
vendor field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def specVendor = getAttribute ("Specification-Vendor")

//-----------------------------------------------------------------------------
/**
Version of the specification to which this release of the application or
library conforms.

@return Manifest specification version.

@throws java.util.NoSuchElementException if the manifest has no specification
version field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def specVersion = new Version (getAttribute ("Specification-Version"))
}
