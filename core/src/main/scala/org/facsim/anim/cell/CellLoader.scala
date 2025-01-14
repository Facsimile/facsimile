/*
Facsimile: A Discrete-Event Simulation Library
Copyright © 2004-2025, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file from the org.facsim.anim.cell package.
*/

package org.facsim.anim.cell

import java.io.InputStreamReader
import java.net.URL
import org.facsim.requireNonNull
import org.facsim.io.TextReader
import scalafx.scene.Node

/**
_[[http://www.automod.com/ AutoMod®]] cell_ file loader object.

This helper object is responsible for loading 3D scenes stored in
_[[http://www.automod.com AutoMod®]] cell_ format and returning them as
_[[http://scalafx.org/ ScalaFX]]_ 3D scenes.

The _AutoMod®_ cell format defines a number of _3D_ graphics primitives,
including _sets_, _trapezoids_ (which include _boxes_), _cylinders_,
_cones_, _frustums_, _polyhedra_, etc. as well as a _compiled picture_
format. Furthermore, cell files can also reference and/or embed _VRML 1.0_,
_VRML 97_ and _OpenInventor_ graphics files (but note that _Facsimile_
actually permits _cell_ files to embed and/or reference any 3D file format
that is supported by _ScalaFX_/_JavaFX_).

This loader is designed to support all _cell_ scenes to assist with migration
from _AutoMod®_ to _Facsimile_.

_Cell_ files have _[[http://en.wikipedia.org/wiki/Windows-1252
Windows-1252]]_ text encoding, and do not support the full
_[[http://en.wikipedia.org/wiki/Unicode Unicode]]_ character set.

®_AutoMod_ is a registered trademark of _[[https://www.appliedmaterials.com/
Applied Materials, Inc.]]_.

=Location of Referenced Files=

3D files that are referenced, but not embedded, within _cell_ files will be
located as follows:
  - If the referenced file specifies an absolute location, then the loader
    function will attempt to find it at that specified location.
  - Otherwise, if the referenced file specifies a relative location, or
    specifies no location at all&mdash;it just has a file name&mdash;then the
    loader function will apply these rules:
    - If a base URL was specified, it will attempt to find the referenced file
      relative to this base URL location.
    - Otherwise, it will search the referenced file relative to the location of
      the _cell_ file being processed.

In all cases, once a search location has been established, the search will fail
if the file could not be found at, or relative to, that search location.

Most of the time, these rules will do what you expect of them. In short, you
should only specify a base URL for the loader function if, and only if, you
have reason to expect files to be located relative to a specific URL that
differs from the location of the _cell_ file being processed; in all other
cases, simply do not specify a base URL.

@note At the time of writing, it appears that a standardized non-native scene
loader mechanism for _ScalaFX_/_JavaFX_ has yet to be defined.
Consequently, until such a standard _is_ defined, this loader should be
regarded as experimental and subject to frequent and substantial revision.

@see _[[http://www.automod.com/ AutoMod®]]_ web-site.

@see _[[http://facsim.org/Documentation/Resources/AutoModCellFile AutoMod®
Cell File Format]]_ (_Facsimile_ web-site).

@see _[[http://en.wikipedia.org/wiki/Windows-1252 Windows-1252]]_ character
encoding (Wikipedia).

@since 0.0
*/

object CellLoader {

/**
Load the _[[http://www.automod.com/ AutoMod®]] cell_ file from the specified
URL and return it as a _ScalaFX 3D Parent_ node.

@note If a base URL is specified, then any files referenced by the cell data
will be searched for relative to that URL; otherwise, files should be present
at the same location as the named file. Refer to
[[org.facsim.anim.cell.CellLoader]] for further information.

@param url URL of file from which cell data is to be read.

@param baseUrl Optional base URL identifying the location relative to which any
referenced files should be located. If `None`, then referenced files should be
located relative to the processed _cell_ file's location.

@param faceColor Face color (as a material) to be assigned to all _cell_
elements in the scene that inherit their face color from the root node. This
value cannot be `null`.

@param edgeColor Edge color (as a material) to be assigned to all _cell_
elements in the scene that inherit their edge color from the root node. This
value cannot be `null`.

@return _ScalaFX_ [[scalafx.scene.Node!]] containing the _cell's_ contents.

@throws NullPointerException if `url`, `faceColor` or `edgeColor` are
`null`.

@throws java.lang.SecurityException if `url` is protected from reading by
Java's security mechanism.

@throws java.io.FileNotFoundException if `url` could not be found, or if it
could not be opened due to file access restrictions (such as the current user
having insufficient privileges to read the file, etc.).

@throws org.facsim.anim.cell.IncorrectFormatException if the file supplied is
not an _AutoMod® cell_ file.

@throws org.facsim.anim.cell.ParsingErrorException if errors are encountered
during parsing of the file.

@since 0.0
*/
  def load(url: URL, baseUrl: Option[URL] = None,
  faceColor: CellColor.Value = CellColor.Default,
  edgeColor: CellColor.Value = CellColor.Default): Node = {

/*
Verify that certain arguments are not null.
*/

    requireNonNull(url)
    requireNonNull(baseUrl)
    requireNonNull(faceColor)
    requireNonNull(edgeColor)

/*
Helper function to retrieve the absolute path of the file, so that we can
identify the base URL if one is not defined.
*/

    def getSearchLocation = {
      val urlString = url.toString
      new URL(urlString.take(urlString.lastIndexOf('/')))
    }

/*
Create the text reader for this file.

Note: We may get a SecurityException if we're forbidden from accessing files by
Java's security features, or a FileNotFoundException if the file either does
not exist, or cannot be read due to access restrictions.

Note that the cell file's encoding is Windows-1252.
*/

    val reader = new TextReader(new InputStreamReader(url.openStream(),
    "windows-1252"))

/*
Determine where we'll search for files referenced in the cell data.
*/

    val searchLocation = baseUrl.getOrElse(getSearchLocation)

/*
Create a cell scene instance and populate it from the reader.
*/

    val cellScene = new CellScene(reader, searchLocation, faceColor,
    edgeColor)

/*
Return the cell's contents as a ScalaFX node.
*/

    cellScene.toNode
  } ensuring(_ ne null)
}