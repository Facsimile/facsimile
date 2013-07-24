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
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import com.sun.j3d.loaders.Loader
import com.sun.j3d.loaders.Scene
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import org.facsim.DeprecatedException
import org.facsim.requireNonNull
import org.facsim.SafeNone
import org.facsim.SafeOption
import org.facsim.SafeSome
import org.facsim.io.TextReader

//=============================================================================
/**
''[[http://www.automod.com/ AutoMod®]] cell'' file loader class.

This helper class implements the ''Java3D'' standard mechanism for loading 3D
scenes stored in a non-native data format&mdash;from ''[[http://www.automod.com
AutoMod®]] cell'' format, in this particular case.

The ''AutoMod®'' cell format defines a number of ''3D'' graphics primitives,
including ''sets'', ''boxes'', ''cylinders'', ''cones'', ''frustums'',
''tetrahedra'' and ''polyhedra'', as well as a ''compiled picture'' format.
Furthermore, cell files can also reference and/or embed ''VRML 1.0'',
''VRML 97'' and ''OpenInventor'' graphics files.  This loader is designed to
accommodate all such scenes to assist with migration from ''AutoMod®'' to
''Facsimile''.

Cell files have Windows-1252 encoding, and do not support the full Unicode
character set.

®''AutoMod'' is a registered trademark of ''[[https://www.appliedmaterials.com
Applied Materials, Inc.]]''.

@note This class implements the [[com.sun.j3d.loaders.Loader!]] interface in a
functional programming style that results in ''cell'' loader objects that are
''immutable''.  Consequently, all of the interface's ''setter'' functions are
deprecated, and will result in a [[org.facsim.DeprecatedException!]] being
thrown if called.  Instead, to ensure immutability, all loader configuration
must be specified during construction.

@note The behavior of this loader differs from the ''Java3D''
[[com.sun.j3d.loaders.Loader!]] norm in terms of how referenced files (files
referenced from within a processed cell file) are located, with an approach
that is at once simpler and more flexible than the traditional ''Java3D''
approach adopted.  Specifically, for each referenced file, the loader will
attempt to locate the file as follows:
  - If the referenced file specifies an absolute location, then the loader will
    attempt to find it at that specified location.
  - Otherwise, if the referenced file specifies a relative location, or
    specifies no location at all&mdash;it just has a file name&mdash;then the
    loader will apply these rules:
    - If a base URL was specified during construction of this loader, then the
      loader will attempt to find the referenced file relative to this base URL
      location.
    - Otherwise, if this loader has not defined a base URL, then the loader's
      search location will depend upon the method used to load the ''cell''
      file:
      - If loaded via the
        [[org.facsim.anim.cell.CellLoader!.load(java.io.Reader)*]] method, then
        referenced files will be searched for relative to the application
        user's current directory.
      - Otherwise, if loaded via the
        [[org.facsim.anim.cell.CellLoader!.load(java.net.URL)*]] or
        [[org.facsim.anim.cell.CellLoader!.load(String)*]] methods, then
        referenced files will be searched for relative to the location of the
        ''cell'' file being processed.
        
In all cases, once a search location has been established, the search will fail
if the file could not be found at, or relative to, that search location.

Most of the time, these rules will do what you expect of them.  In short, you
should only specify a base URL for this loader if, and only if, you have reason
to expect files to be located relative to a specific URL that differs from the
default location of the chosen method&mdash;in all other cases, simply do not
specify a base URL.

(And yes, this is all simpler than the traditional ''Java 3D'' loader
approach.)

@note When attempting to determine the base search location for referenced
files, you should favor the use of
[[org.facsim.anim.cell.CellLoader!.getBaseUrl]] over
[[org.facsim.anim.cell.CellLoader!.getBasePath]]&mdash;the latter is
''deprecated'' because it can only report base paths for ''file''
protocol-based base URL's, while the former can report any base URL.

@constructor Create a new loader instance with the specified configuration.

@param loadFlags An integer encoding loader flags, which is used to filter
loaded scene content.  The default value of 0 instructs the loader to include
scene geometry information only.  For further details on supported loader
flags, refer to [[com.sun.j3d.loaders.Loader!]].

@param baseUrl Default search location for files referenced during processing
of this ''cell'' file.  Refer to [[org.facsim.anim.cell.CellLoader!]] for
further information.

@since 0.0
*/
//=============================================================================

final class CellLoader (loadFlags: Int = 0, baseUrl: SafeOption [URL] =
SafeNone) extends Loader with NotNull {

//-----------------------------------------------------------------------------
/**
Report load flags.

@return Load flags associated with this cell scene loader.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final override def getFlags = loadFlags

//-----------------------------------------------------------------------------
/**
Determine whether all nodes should be loaded.

By default, only scene geometry nodes are loaded.

@return `true` if all nodes should be loaded, `false` if some nodes should not
be be loaded.
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] final def loadAllNodes = (loadFlags & Loader.LOAD_ALL) != 0

//-----------------------------------------------------------------------------
/**
Determine whether background nodes should be loaded.

By default, only scene geometry nodes are loaded.

@note ''Cell'' file format does not support background nodes directly, but such
nodes may be present in supported embedded file formats (''VRML 1.0'', ''VRML
97'' & ''OpenInventor'').

@return `true` if background nodes should be loaded, `false` if they should not
be loaded.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] final def loadBackgroundNodes =
  (loadFlags & Loader.LOAD_BACKGROUND_NODES) != 0

//-----------------------------------------------------------------------------
/**
Determine whether behavior nodes should be loaded.

By default, only scene geometry nodes are loaded.

@note ''Cell'' file format does not support behavior nodes directly, but such
nodes may be present in supported embedded file formats (''VRML 1.0'', ''VRML
97'' & ''OpenInventor'').  However, it is recommended that behavior nodes not
be loaded.

@return `true` if behavior nodes should be loaded, `false` if they should not
be loaded.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] final def loadBehaviorNodes =
  (loadFlags & Loader.LOAD_BEHAVIOR_NODES) != 0

//-----------------------------------------------------------------------------
/**
Determine whether fog nodes should be loaded.

By default, only scene geometry nodes are loaded.

@note ''Cell'' file format does not support fog nodes directly, but such nodes
may be present in supported embedded file formats (''VRML 1.0'', ''VRML 97'' &
''OpenInventor'').

@return `true` if fog nodes should be loaded, `false` if they should not be
loaded.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] final def loadFogNodes =
  (loadFlags & Loader.LOAD_FOG_NODES) != 0

//-----------------------------------------------------------------------------
/**
Determine whether light nodes should be loaded.

By default, only scene geometry nodes are loaded.

@note ''Cell'' file format does not support light nodes directly, but such
nodes may be present in supported embedded file formats (''VRML 1.0'', ''VRML
97'' & ''OpenInventor'').

@return `true` if light nodes should be loaded, `false` if they should not be
loaded.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] final def loadLightNodes =
  (loadFlags & Loader.LOAD_LIGHT_NODES) != 0

//-----------------------------------------------------------------------------
/**
Determine whether sound nodes should be loaded.

By default, only scene geometry nodes are loaded.

@note ''Cell'' file format does not support sound nodes directly, but such
nodes may be present in supported embedded file formats (''VRML 1.0'', ''VRML
97'' & ''OpenInventor'').

@return `true` if sound nodes should be loaded, `false` if they should not be
loaded.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] final def loadSoundNodes =
  (loadFlags & Loader.LOAD_SOUND_NODES) != 0

//-----------------------------------------------------------------------------
/**
Determine whether view groups (cameras) should be loaded.

By default, only scene geometry nodes are loaded.

@note ''Cell'' file format does not support view groups directly, but such
nodes may be present in supported embedded file formats (''VRML 1.0'', ''VRML
97'' & ''OpenInventor'').

@return `true` if view groups should be loaded, `false` if they should not be
loaded.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [cell] final def loadViewGroups =
  (loadFlags & Loader.LOAD_VIEW_GROUPS) != 0

//-----------------------------------------------------------------------------
/**
Set load flags.

@note This method is required by the [[com.sun.j3d.loaders.Loader!]] interface,
but is deliberately not implemented, since it would force
[[org.facsim.anim.cell.CellLoader!]] to have mutable state, which is
undesirable.

Instead, load flags must be defined during construction.  If this loader object
has inappropriate load flags, then construct a new loader object with the
appropriate load flags.

@param flags Ignored new load flags.

@throws [[org.facsim.DeprecatedException!]] if called.

@see [[org.facsim.anim.cell.CellLoader!]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  @deprecated ("Load flags must be defined during construction.", "0.0")
  final override def setFlags (flags: Int): Unit = throw new
  DeprecatedException ("function " + classOf [CellLoader].toString +
  ".setFlags(" + flags.toString + ")")

//-----------------------------------------------------------------------------
/**
Report loader's base path.

@note This method is required by the [[com.sun.j3d.loaders.Loader!]] interface,
but, while implemented, it is deprecated in favor of
[[org.facsim.anim.cell.CellLoader!.getBaseUrl]].  While the latter can report
all base URLs, including ''file'' URLs, this function can only report ''file''
URLs, which might be misleading if a non-''file'' path base URL has been
defined.

In particular, it should be noted that a return value of `null` does not imply
that no base URL has been defined.

@return Loader's defined base path (if defined base URL employs a ''file''
protocol), or `null` otherwise.  `null` will also be returned if the loader has
a non-''file'' protocol-based URL.

@see [[org.facsim.anim.cell.CellLoader!]]

@see [[org.facsim.anim.cell.CellLoader!.getBaseUrl]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  @deprecated ("Use .getBaseUrl to determine file search locations.", "0.0")
  final override def getBasePath = baseUrl match {
    case SafeSome (url) => {
      if (url.getProtocol == "file") url.getPath ()
      else null
    }
    case SafeNone => null
  }

//-----------------------------------------------------------------------------
/**
Set base path for [[org.facsim.anim.cell.CellLoader!.load(String)*]] method.

@note This method is required by the [[com.sun.j3d.loaders.Loader!]] interface,
but is deliberately not implemented, since it would force
[[org.facsim.anim.cell.CellLoader!]] to have mutable state, which is
undesirable.

Instead, a base URL (with a ''file'' protocol, if a base path is required) must
be defined during construction.  If this loader object has an inappropriate
base URL, then construct a new loader object with the appropriate base URL.

@param path Ignored new base path.

@throws [[org.facsim.DeprecatedException!]] if called.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @deprecated ("Base URL must be defined during construction.", "0.0")
  final override def setBasePath (path: String): Unit = throw new
  DeprecatedException ("function " + classOf [CellLoader].toString +
  ".setBasePath(\"" + path + "\")")

//-----------------------------------------------------------------------------
/**
Report base URL to be used by
[[org.facsim.anim.cell.CellLoader!.load(java.io.Reader)*]],
[[org.facsim.anim.cell.CellLoader!.load(String)*]] and
[[org.facsim.anim.cell.CellLoader!.load(java.net.URL)*]].

If a base URL has not been defined, it will be reported as `null`, otherwise
the base URL defined during construction will be returned.

Refer to [[org.facsim.anim.cell.CellLoader!]] for further information on how
the base URL is employed.

@return base URL, or `null` if no base URL is currently defined.

@see [[org.facsim.anim.cell.CellLoader!]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final override def getBaseUrl = baseUrl.getOrElse (null)

//-----------------------------------------------------------------------------
/**
Set base URL for [[org.facsim.anim.cell.CellLoader!.load(java.net.URL)*]]
method.

@note This method is required by the [[com.sun.j3d.loaders.Loader!]] interface,
but is deliberately not implemented, since it would force
[[org.facsim.anim.cell.CellLoader!]] to have mutable state, which is
undesirable.

Instead, a base URL must be defined during construction.  If this loader object
has an inappropriate base URL, then construct a new loader object with the
appropriate base URL.

@param path Ignored new base URL.

@throws [[org.facsim.DeprecatedException!]] if called.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @deprecated ("Base URL must be defined during construction.", "0.0")
  final override def setBaseUrl (url: URL): Unit = throw new
  DeprecatedException ("function " + classOf [CellLoader].toString +
  ".setBaseUrl(URL(\"" + url.toString + "\"))")

//-----------------------------------------------------------------------------
/**
Loads an ''[[http://www.automod.com/ AutoMod®]] cell'' scene from the specified
reader and returns it as a ''Java3D'' Scene.

The cell's nodes will be filtered according to this loader's load flags.

@note If a base URL was defined during construction, then any files referenced
by the cell data will be searched for relative to that URL; otherwise, files
should be present in the user's current working directory.  Refer to
[[org.facsim.anim.cell.CellLoader!]] for further information.

@param reader Reader from which cell data is to be read.  For proper
processing, the reader should utilize ''Windows-1252'' file encoding; other
encodings will work seamlessly, but the resulting processed
data&mdash;particularly text data&mdash;may not match the expected ''cell''
file content.

@return Scene containing the cell's contents.

@throws [[java.lang.NullPointerException!]] if reader is `null`.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[org.facsim.anim.cell.CellLoader!]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def load (reader: Reader) = {

/*
Verify that the reader is not null.
*/

    requireNonNull (reader)

/*
Helper function to retrieve the absolute path of the file, so that we can
identify the base URL if one is not defined.
*/

    def getSearchLocation =
    new File (System.getProperty ("user.dir")).toURI.toURL

/*
Create the text reader for this file.
*/

    val cellReader = new TextReader (reader)

/*
Determine where we'll search for files referenced in the cell data.
*/

    val searchLocation = baseUrl.getOrElse (getSearchLocation)

/*
Read cell data and return the scene read.
*/

    new CellScene (this, cellReader, searchLocation)
  }

//-----------------------------------------------------------------------------
/**
Loads the named ''[[http://www.automod.com/ AutoMod®]] cell'' file and returns
it as a ''Java3D'' Scene.

The cell's nodes will be filtered according to this loader's load flags.

@note If a base URL was defined during construction, then any files referenced
by the cell data will be searched for relative to that URL; otherwise, files
should be present at the same location as the named file.  Refer to
[[org.facsim.anim.cell.CellLoader!]] for further information.

@param file File from which cell data is to be read.

@return Scene containing the cell's contents.

@throws [[java.lang.NullPointerException!]] if file is null.

@throws [[java.lang.SecurityException!]] if specified file is protected from
reading.

@throws [[java.io.FileNotFoundException!]] if the specified file could not be
found, or if it could not be opened.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@see [[org.facsim.anim.cell.CellLoader!]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def load (file: String) = {

/*
Verify that the file is not null.
*/

    requireNonNull (file)

/*
Helper function to retrieve the absolute path of the file, so that we can
identify the base URL if one is not defined.
*/

    def getSearchLocation =
    new File (file).getAbsoluteFile.getParentFile.toURI.toURL

/*
Create the text reader for this file.

Note: We may get a FileNotFoundException if the file does not exist, or a
SecurityException if the file cannot be read.
*/

    val cellReader = new TextReader (new InputStreamReader (new FileInputStream
    (file), "windows-1252"))

/*
Determine where we'll search for files referenced in the cell data.
*/

    val searchLocation = baseUrl.getOrElse (getSearchLocation)

/*
Read cell data and return the scene read.
*/

    new CellScene (this, cellReader, searchLocation)
  }

//-----------------------------------------------------------------------------
/**
Loads the ''[[http://www.automod.com/ AutoMod®]] cell'' file from the specified
URL and returns it as a ''Java3D'' Scene.

The cell's nodes will be filtered according to this loader's load flags.

@note If a base URL was defined during construction, then any files referenced
by the cell data will be searched for relative to that URL; otherwise, files
should be present at the same location as the named file.  Refer to
[[org.facsim.anim.cell.CellLoader!]] for further information.

@param url URL of file from which cell data is to be read.

@return Scene containing the cell's contents.

@throws [[java.lang.NullPointerException!]] if reader is null.

@throws [[java.lang.SecurityException!]] if specified file is protected from
reading.

@throws [[java.io.FileNotFoundException!]] if the specified file could not be
found, or if it could not be opened.

@throws [[com.sun.j3d.loaders.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[com.sun.j3d.loaders.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def load (url: URL) = {

/*
Verify that the file is not null.
*/

    requireNonNull (url)

/*
Helper function to retrieve the absolute path of the file, so that we can
identify the base URL if one is not defined.
*/

    def getSearchLocation = {
      val urlString = url.toString ()
      new URL (urlString.take (urlString.lastIndexOf ('/')))
    }

/*
Create the text reader for this file.

Note: We may get a FileNotFoundException if the file does not exist, or a
SecurityException if the file cannot be read.
*/

    val cellReader = new TextReader (new InputStreamReader (url.openStream (),
    "windows-1252"))

/*
Determine where we'll search for files referenced in the cell data.
*/

    val searchLocation = baseUrl.getOrElse (getSearchLocation)

/*
Read cell data and return the scene read.
*/

    new CellScene (this, cellReader, searchLocation)
  }
}