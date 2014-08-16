/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file defining the org.facsim.util package.
*/
//=============================================================================

package org.facsim

import java.io.File
import java.net.{URI, URL}
import java.time.ZonedDateTime
import java.util.GregorianCalendar
import java.util.jar.JarFile

import scala.language.implicitConversions

//=============================================================================
/**
''[[http://facsim.org/ Facsimile]]'' Simulation Library miscellaneous
utilities.

Package providing miscellaneous utility elements.

@note ''Facsimile'' is, first and foremost, a simulation library. It is not a
collection of miscellaneous, utility classes and functions with general
applicability. Only simulation-related utilities are publicly accessible.

@since 0.0
*/
//=============================================================================

package object util {

/**
Regular expression for identifying periods in package path names.
*/

  private val PeriodRegEx = """(\.)""".r

/**
Regular expression for extracting a ''jar'' file URI from a URL.
*/

  private val JarUriRegEx = """^jar\:(.+)\!.+$""".r

/**
Java file separator.
*/

  private [facsim] val fs = "/"

//-----------------------------------------------------------------------------
/**
Implicit conversion of a [[java.time.ZonedDateTime]] to a [[java.util.Date]].

Conversion between pre-''Java 1.8'' `java.util` time classes (such as
[[java.util.Date]], [[java.util.GregorianCalendar]], etc.) and the new
post-''Java 1.8'' `java.time` time classes ([[java.time.Instant]],
[[java.time.ZonedDateTime]], etc) is cumbersome at best. The former could be
dispensed with completely if if wasn't for the fact that
[[java.text.MessageFormat]] currently supports only the [[java.util.Date]]
class. This function makes working with the new time classes, and text message
formatting, a little more straightforward.

@param date Date, expressed as a [[java.time.ZonedDateTime]] to be converted.

@return `date` expressed as a [[java.util.Date]].

@throws java.lang.NullPointerException if `date` is null.

@throws java.lang.IllegalArgumentException if `date` is too large to represent
as a [[java.util.GregorianCalendar]] value.
*/
//-----------------------------------------------------------------------------

  private [facsim] implicit def toDate (date: ZonedDateTime) =
  GregorianCalendar.from (date).getTime

//-----------------------------------------------------------------------------
/**
Obtain the resource URL associated with a class's type information.

@param elementType Element type instance for which a resource ''URL'' will be
sought.

@return Resource ''URL'' associated with `elementType`, if found.

@throws java.util.NoSuchElementException if `elementType` has no associated
resource ''URL''.
*/
//-----------------------------------------------------------------------------

  private [facsim] def resourceUrl (elementType: Class [_]) = {

/*
Argument validation.
*/

    assert (elementType ne null)

/*
NOTE: This is a rather convoluted process. If you know of a better (i.e.
simpler or quicker) approach, feel free to implement it.

Retrieve the name of the class, and convert it into a resource path. To do
this, we need to prefix it with a slash, replace all periods with slashes and
add a ".class" extension.

Note: The Class[T].getSimpleName method crashes for some Scala elements. This
is a known bug. Refer to [[https://issues.scala-lang.org/browse/SI-2034]] for
further details.
*/

    val name = elementType.getName
    val path = fs + PeriodRegEx.replaceAllIn (name, fs) + ".class"

/*
Now retrieve the resource URL for this element path.
*/

    val url = elementType.getResource (path)

/*
If the element URL is null, then its provenance is unknown and we will not be
able to find a manifest for it. Throw an exception instead.

Typically, we will fail to find a URL if element identifies a Java primitive.
*/

    if (url eq null) throw new NoSuchElementException (LibResource
    ("util.resourceUrl.NoSuchElement", name))

/*
Return the resulting URL.
*/

    url
  }

//-----------------------------------------------------------------------------
/**
Obtain the resource URL associated with a class's type information.

@param url ''URL'' identifying the location of a ''JAR'' file.

@return ''JAR'' file identified by the `url`, if found.

@throws java.util.NoSuchElementException if `elementType` has no associated
resource ''URL''.
*/
//-----------------------------------------------------------------------------

  private [facsim] def jarFile (url: URL) = {

/*
Argument validation.
*/

    assert (url ne null)

/*
If the URL identifies a JAR file, then it will be of the (String) form:

jar:file:/{path-of-jar-file}!{elementPath}

In order to create a JAR file instance, we need to convert this into a
hierarchical URI.  We do this using a regular expression extraction.  What we
want is just the file:{path-of-jar-file} portion of the URL.

If we do not have such a match, then the element was not loaded from a JAR
file, so throw an exception instead.
*/

    val jarUri = url.toString match {
      case JarUriRegEx (uri) => new URI (uri)
      case _ => throw new NoSuchElementException (LibResource
      ("util.jarFile.NoSuchElement", url.toString))
    }

/*
Create and return a JarFile instance from the obtained uri.
*/

    new JarFile (new File (jarUri))
  }
}