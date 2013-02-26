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

import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import com.sun.j3d.loaders.Loader

//=============================================================================
/**
''[[http://www.automod.com/ AutoMod®]] cell'' file loader class.

This helper class implements the ''Java3D'' standard mechanism for loading 3D
scenes stored in a non-native data format&mdash;from ''[[http://www.automod.com
AutoMod®]] cell'' format, in this particular case.

The ''AutoMod®'' cell format defines a number of graphics primitives (''sets'',
''boxes'', ''cylinders'', ''cones'', ''frustums'', etc.), as well as a compiled
picture format.  Furthermore, cell files can also reference and/or embed
''VRML 1.0'', ''VRML 97'' and ''OpenInventor'' graphics files.  This loader is
designed to accommodate all such scenes to assist with migration from
''AutoMod®'' to ''Facsimile''.

Cell files have Windows-1252 encoding, and do not support the full Unicode
character set.

''AutoMod'' is a registered trademark of ''[[https://www.appliedmaterials.com
Applied Materials, Inc.]]''.

@note This class implements the [[com.sun.j3d.loaders.Loader]] interface, but
in a functional programming style that results in ''cell'' loader objects that
are ''immutable''.  Consequently, all of the interface's ''setter'' functions
are deprecated, and will resulting in a
[[java.lang.UnsupportedOperationException]] being thrown if called.  Instead,
all loader configuration must be specified during construction.

@note The behavior of this loader also differs from the ''Java3D''
[[com.sun.j3d.loaders.Loader]] norm in terms of how referenced files (files
referenced from a processed cell file) are handled, which is more flexible in
many respects than standard Loader behavior: when constructing a cell loader
instance, a base URL may be defined.  This base URL (which may identify a file
on a web-site or the user's local file system) is employed as follows:
 -  For load(Reader), if a base URL is undefined, referenced files will be
    searched for relative to the user's current directory&mdash;this is
    standard behavior.  However, if a base URL is defined, then referenced
    files will be searched for relative to the base URL&mdash;this behavior is
    non-standard, but offers increased flexibility.
 -  For load(String), if a base URL is undefined, referenced files will be
    searched for relative to the location of the specified file&mdash;this is
    standard behavior.  However, if a base URL is defined, then referenced
    files will be searched for relative to the base URL&mdash;this differs
    from the standard Loader implementation, which only allows a base
    path&mdash;not a base URL&mdash;to be identified, but that offers increased
    flexibility.
 -  For load(URL), if a base URL is undefined, referenced files will be
    searched for relative to the location of the specified file.  If a base URL
    is defined, referenced files will be searched for relative to the base URL.
    In both cases, this is standard behavior.

@constructor Create a new loader instance with the specified configuration.

@param loadFlags An integer encoding loader flags, which is used to filter
loaded scene content.  The default value of 0 instructs the loader to include
scene geometry information only.  For further details on supported loader
flags, refer to [[com.sun.j3d.loaders.Loader]].

@param baseURL If this value is not `None`, then when loading ''cell'' scenes
via the load(Reader), load(URL) or load(String) methods, any files referenced
by the cell file being processed must be located relative to this base URL.
Otherwise, if this parameter is `None`, then files referenced by load(Reader)
must be present in the user's current directory and files referenced by
load(URL) and load(String) must be present at the same location as the
specified file.  Refer to [[org.facsim.anim.cell.CellLoader]] for further
information.  Note that we cannot verify (at present) whether the base URL
identifies a directory and not a file; if the latter, errors will occur during
processing of embedded files.

@since 0.0
*/
//=============================================================================

final class CellLoader (loadFlags: Int = 0, baseURL: Option [URL] = None)
extends Loader {

//-----------------------------------------------------------------------------
/**
Report load flags.

@return Load flags associated with this cell scene loader.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def getFlags () = loadFlags

//-----------------------------------------------------------------------------
/**
Set load flags.

This method is required to implement the [[com.sun.j3d.loaders.Loader]]
interface, but would require that [[org.facsim.anim.cell.CellLoader]] become a
mutable object.  Instead, users should specify load flags during construction.
If a loader object has inappropriate load flags, then create a new loader
object that meets your requirements. 

@param flags Ignored new load flags.

@throws UnsupportedOperationException if called.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @deprecated ("Load flags should be defined in the constructor", "0.0")
  final override def setFlags (flags: Int): Unit = throw new
  UnsupportedOperationException ("TODO I18N: CellLoader.setFlags () is " +
  "deprecated.")

//-----------------------------------------------------------------------------
/**
Report base path to be used by load (String).

If a base URL has been defined, then a base path will be returned only if the
base URL employs a ''file'' protocol.  In all other cases, `null` will be
returned.  This most closely approximates the expected behavior of this method
for [[com.java.j3d.loaders.Loader]] implementations.

@note Due to the implementation of this cell loader class, a `null` return
value does not necessarily imply that load(String) will search for referenced
files at the same location as the specified file.  However, if getBaseUrl()
returns `null`, then load(String) will indeed search for referenced files at
the same location as the specified file, ortherwise it will search at the
return URL.  See [[org.facsim.anim.cell.CellLoader]] for further information.

@return base path, or `null` if a web-based base URL has been defined, or if no
base URL has been defined.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def getBasePath () = baseURL match {
    case Some (url) => {
      if (url.getProtocol () == "file") url.getPath ()
      else null
    }
    case None => null
  }

//-----------------------------------------------------------------------------
/**
Set base path for load (String) method.

This method is required to implement the com.sun.j3d.loaders.Loader interface,
but would require that org.facsim.CellLoader become a mutable object.  Instead,
users should specify base URLs (using a ''file'' protocol) during construction.
If a loader object has an inappropriate base path, then create a new loader
object that meets your requirements. 

@param path Ignored new base path.

@throws UnsupportedOperationException if called.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @deprecated ("Base path should be defined in the constructor", "0.0")
  final override def setBasePath (path: String): Unit = throw new
  UnsupportedOperationException ("TODO I18N: CellLoader.setBasePath () is " +
  "deprecated.")

//-----------------------------------------------------------------------------
/**
Report base URL to be used by load (URL).

If a base URL has not been defined, it will be reported as `null`, otherwise
the base URL defined will be returned.

A `null` base URL will cause load (URL) to look for referenced files at the
same location as the specified ''cell'' file.

@return base URL, or `null` if no base URL is currently defined.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def getBaseUrl () = baseURL.orNull

//-----------------------------------------------------------------------------
/**
Set base URL for load (URL) method.

This method is required to implement the com.sun.j3d.loaders.Loader interface,
but would require that org.facsim.CellLoader become a mutable object.  Instead,
users should specify base URLs during construction.  If a loader object has an
inappropriate base URL, then create a new loader object that meets your
requirements. 

@param url Ignored new base URL.

@throws UnsupportedOperationException if called.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @deprecated ("Base URL should be defined in the constructor", "0.0")
  final override def setBaseUrl (url: URL): Unit = throw new
  UnsupportedOperationException ("TODO I18N: CellLoader.setBaseUrl () is " +
  "deprecated.")


//-----------------------------------------------------------------------------
/**
Loads an ''[[http://www.automod.com/ AutoMod®]] cell'' scene from the specified
reader and returns it as a ''Java3D'' Scene.

The cell's nodes will be filtered, if necessary, according to this loader's
load flags.

@note If a base URL was defined during construction, then any files referenced
by the cell data will be searched for relative to that URL; otherwise, files
should be present in the user's current working directory.  See
[[org.facsim.anim.cell.CellLoader]] for further information.

@param reader Reader from which cell data is to be read.

@return Scene containing the cell's contents.

@throws java.lang.NullPointerException if reader is null.

@throws com.sun.j3d.loaders.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws com.sun.j3d.loaders.ParsingErrorException if errors are encountered
during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def load (reader: Reader) = new CellScene (reader,
  baseURL.getOrElse (new URL ("file:///" + System.getProperty ("user.dir"))))

//-----------------------------------------------------------------------------
/**
Loads the named ''[[http://www.automod.com/ AutoMod®]] cell'' file and returns
it as a ''Java3D'' Scene.

The cell's nodes will be filtered, if necessary, according to this loader's
load flags.

@note If a base URL was defined during construction, then any files referenced
by the cell data will be searched for relative to that URL; otherwise, files
should be present at the same location as the named file.  See
[[org.facsim.anim.cell.CellLoader]] for further information.

@param file File from which cell data is to be read.

@return Scene containing the cell's contents.

@throws java.lang.NullPointerException if file is null.

@throws java.lang.SecurityException if specified file is protected from
reading.

@throws java.io.FileNotFoundException if the specified file could not be found.

@throws com.sun.j3d.loaders.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws com.sun.j3d.loaders.ParsingErrorException if errors are encountered
during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def load (file: String) = new
  CellScene (CellLoader.getFileReader (file), baseURL.getOrElse
  (CellLoader.getFileBaseURL (file)))

//-----------------------------------------------------------------------------
/**
Loads the ''[[http://www.automod.com/ AutoMod®]] cell'' file from the specified
URL and returns it as a ''Java3D'' Scene.

The cell's nodes will be filtered, if necessary, according to this loader's
load flags.

@note If this cell loader was constructed with a default base URL, then any
data files referenced by this file should be located in the same place as the
named file; otherwise users should specify their required base URL during cell
loader construction.

@param url URL of file from which cell data is to be read.

@return Scene containing the cell's contents.

@throws java.lang.NullPointerException if reader is null.

@throws java.lang.SecurityException if specified file is protected from
reading.

@throws java.io.FileNotFoundException if the specified file could not be found,
or if it could not be opened.

@throws com.sun.j3d.loaders.IncorrectFormatException if the file supplied is
not an ''AutoMod® cell'' file.

@throws com.sun.j3d.loaders.ParsingErrorException if errors are encountered
during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final override def load (url: URL) = new CellScene (CellLoader.getURLReader
  (url), baseURL.getOrElse (CellLoader.getURLBaseURL (url)))
}

//=============================================================================
/**
CellLoader companion object.

@since 0.0
*/
//=============================================================================

private [cell] object CellLoader {

//-----------------------------------------------------------------------------
/**
Create reader for specified ''cell'' file.

@param file Name of file to be read, which must be relative to the user's
current directory.

@return a reader, with an appropriate encoding, for the specified file.

@throws java.lang.NullPointerException if file is null.

@throws java.lang.SecurityException if specified file is protected from
reading.

@throws java.io.FileNotFoundException if the specified file could not be found.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def getFileReader (file: String): Reader = new InputStreamReader (new
  FileInputStream (file), "windows-1252")

//-----------------------------------------------------------------------------
/**
Return a base URL for the specified file.

The base path of the file is determined and returned as a URL having a ''file''
protocol.

@param file Name of file for which a base URL is required.
*/
//-----------------------------------------------------------------------------

  private def getFileBaseURL (file: String) = new File (new File
  (file).getCanonicalFile ().getParent ()).toURI ().toURL ()

//-----------------------------------------------------------------------------
/**
Create reader for specified ''cell'' URL.

@param url Url of cell data to be read.

@return a reader, with an appropriate encoding, for the specified file.

@throws java.lang.NullPointerException if file is null.

@throws java.lang.SecurityException if specified file is protected from
reading.

@throws java.io.FileNotFoundException if the specified file could not be found.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def getURLReader (url: URL): Reader = new
  InputStreamReader (url.openStream (), "windows-1252")

//-----------------------------------------------------------------------------
/**
Return a base URL for the specified file.

The base path of the file is determined and returned as a URL having a ''file''
protocol.

@param file Name of file for which a base URL is required.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def getURLBaseURL (url: URL) = {
    val urlString = url.toString ()
    new URL (urlString.take (urlString.lastIndexOf ('/')))
  }
}