/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2010, Michael J Allen.

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

Java source file belonging to the org.facsim.facsimile.j3d.loaders.cell
package.
*/
//=============================================================================

package org.facsim.facsimile.j3d.loaders.cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import org.facsim.facsimile.io.Delimiter;
import org.facsim.facsimile.io.TextReader;
import org.facsim.facsimile.io.Tokenizer;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.LoaderBase;
import com.sun.j3d.loaders.ParsingErrorException;
import com.sun.j3d.loaders.Scene;

//=============================================================================
/**
<p>Loader class to translate an <em><a
href="http://www.automod.com/">AutoMod</a> cell</em> scene into a
<em><a href="http://java3d.dev.java.net/">Java3D</a></em> scene.</p>

<p>The translation is unidirectional; the resulting <em>Java3D</em> scene, or
any other <em>Java3D</em> scene, cannot be saved as an <em>AutoMod cell</em>
file.</p>
*/
//=============================================================================

public final class CellLoader
extends LoaderBase
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Default constructor.</p>

<p>Creates an <em><a href="http://www.automod.com/">AutoMod</a> cell</em>
loader with all load options set.  Refer to {@linkplain #CellLoader(int)} for
futher information.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public CellLoader ()
    {
        this (LOAD_ALL);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Feature flag constructor.</p>

<p>Creates an <em><a href="http://www.automod.com/">AutoMod</a> cell</em>
loader with the specified set of load options set.</p>

<p><em><a href="http://java3d.dev.java.net/">Java3D<a/></em> supports the
following flags, which are interpreted by this <em>cell</em> loader as
described below.  <cem>Cell</em> scenes, while limited in native complexity,
can contain embedded <em><a
href="http://oss.sgi.com/projects/inventor/">OpenInventor</a></em> and <em><a
href="http://www.web3d.org/x3d/specifications/vrml/vrml97/index.htm">VRML
97</a></em> scenes; it is these embedded scenes that may need to honor these
flags.</p>

<table width="100%">
  <tr align="left" valign="baseline">
    <th>Flag</th>
    <th>Meaning</th>
    <th>Comments</th>
  </tr>
  <tr align="left" valign="baseline">
    <td>LOAD_ALL</td>
    <td>Set all options.</td>
    <td></td>
  </tr>
  <tr align="left" valign="baseline">
    <td>LOAD_BACKGROUND_NODES</td>
    <td>If clear, no background nodes should be loaded into the <em>Java3D</em>
    scene.</td>
    <td>Not applicable to <em>cell</em> native scenes.  Honored by embedded
    <em>OpenInventor</em> &amp; <em>VRML</em> scenes.</td>
  </tr>
  <tr align="left" valign="baseline">
    <td>LOAD_BEHAVIOR_NODES</td>
    <td>If clear, no behavior nodes should be loaded into the <em>Java3D</em>
    scene.</td>
    <td>Not applicable to <em>cell</em> native scenes.  Honored by embedded
    <em>OpenInventor</em> &amp; <em>VRML</em> scenes.</td>
  </tr>
  <tr align="left" valign="baseline">
    <td>LOAD_FOG_NODES</td>
    <td>If clear, no fog nodes should be loaded into the <em>Java3D</em>
    scene.</td>
    <td>Not applicable to <em>cell</em> native scenes.  Honored by embedded
    <em>OpenInventor</em> &amp; <em>VRML</em> scenes.</td>
  </tr>
  <tr align="left" valign="baseline">
    <td>LOAD_LIGHT_NODES</td>
    <td>If clear, no light nodes should be loaded into the <em>Java3D</em>
    scene.</td>
    <td>Not applicable to <em>cell</em> native scenes.  Honored by embedded
    <em>OpenInventor</em> &amp; <em>VRML</em> scenes.</td>
  </tr>
  <tr align="left" valign="baseline">
    <td>LOAD_SOUND_NODES</td>
    <td>If clear, no sound nodes should be loaded into the <em>Java3D</em>
    scene.</td>
    <td>Not applicable to <em>cell</em> native scenes.  Honored by embedded
    <em>OpenInventor</em> &amp; <em>VRML</em> scenes.</td>
  </tr>
  <tr align="left" valign="baseline">
    <td>LOAD_VIEW_GROUPS</td>
    <td>If clear, no view nodes should be loaded into the <em>Java3D</em>
    scene.</td>
    <td>Not applicable to <em>cell</em> native scenes.  Honored by embedded
    <em>OpenInventor</em> &amp; <em>VRML</em> scenes.</td>
  </tr>
</table>

@param flags Bit-field indicating which load options are being set by default.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public CellLoader (int flags)
    {
        super (flags);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see com.sun.j3d.loaders.Loader#load(java.lang.String)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final Scene load (String filename)
    throws FileNotFoundException
    {

/*
Create a TextReader to open the file.  If we encounter an IOException, then
throw a CellParseException in it's place.  If we encounter a
FileNotFoundException (which is derived from IOException - hence the need to
handle it separately), then just pass that on to the caller.
*/

        TextReader reader = null;
        try
        {
            reader = new TextReader (filename);
        }
        catch (FileNotFoundException exception)
        {
            throw exception;
        }
        catch (IOException exception)
        {
            throw new CellParseException (reader, exception);
        }

/*
Pass the reader to the load method and return the result.
*/

        return loadScene (reader);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see com.sun.j3d.loaders.Loader#load(java.net.URL)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final Scene load (URL url)
    throws FileNotFoundException
    {

/*
Convert the specified URL to a URI (which may cause a URI syntax exception if
not a valid URL, or a NullPointerException, if null).  Pass the converted URI
to a file
*/

        File file;
        try
        {
            file = new File (url.toURI ());
        }

/*
Handle condition indicating that the URI passed to the file object fails
conversion to a URIsyntax check.
*/

        catch (URISyntaxException exception)
        {
            exception.printStackTrace();
            file = null;
            System.exit (1);
        }

/*
Now create a text reader from the file in order to process it consistently.
*/

        assert file != null;
        TextReader reader = null;
        try
        {
            reader = new TextReader (file);
        }
        catch (IOException exception)
        {
            throw new CellParseException (reader, exception);
        }
        return loadScene (reader);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see com.sun.j3d.loaders.Loader#load(java.io.Reader)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final Scene load (Reader reader)
    throws FileNotFoundException, IncorrectFormatException,
    ParsingErrorException
    {

/*
Now create a text reader from the file in order to process it consistently.
*/

        TextReader textReader = null;
        try
        {
            textReader = new TextReader (reader);
        }
        catch (IOException exception)
        {
            throw new CellParseException (textReader, exception);
        }
        return loadScene (textReader);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Create the <em>Java3D</em> scene from the <em>cell</em> text reader.</p>

@param reader TextReader from which the <em>cell</em> data will be read.

@return <em>Java3D</em> scene constructed from the <em>cell</em> data.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final Scene loadScene (TextReader reader)
    {

/*
Create a cell tokenizer for reading the cell data from the reader.
*/

        Tokenizer tokenizer = new Tokenizer (reader,
        Delimiter.getWhitespaceDelimiter ());
        CellTokenizer cellTokenizer = new CellTokenizer (this, tokenizer);

/*
Populate the cell scene from the tokenized cell stream, and return the
populated scene.
*/

        return Cell.readCellData (cellTokenizer);
    }
}
