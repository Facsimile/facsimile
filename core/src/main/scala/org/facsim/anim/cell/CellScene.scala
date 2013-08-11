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

import java.io.IOException
import java.net.URL
import org.facsim.LibResource
import org.facsim.io.FieldConversionException
import org.facsim.io.FieldVerificationException
import org.facsim.io.LineDelimiter
import org.facsim.io.TextReader
import scala.collection.mutable.Map

//=============================================================================
/**
''Java3D'' scene retrieved from an ''[[http://www.automod.com/ AutoMod®]]
cell'' file.

@constructor Create a new scene with the indicated reader and default
information.

@param reader Text reader that assists with processing the ''cell'' file's
contents.

@param baseUrl Location at which, or relative to which, files referenced within
''cell'' files that have non-absolute paths, will be searched.

@param faceColor Face color to be assigned to all ''cell'' elements in this
scene that inherit their face color from the root node.  This value cannot be
`null`.

@param edgeColor Edge color to be assigned to all ''cell'' elements in this
scene that inherit their edge color from the root node.  This value cannot be
`null`.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//=============================================================================

private [cell] final class CellScene (reader: TextReader, baseUrl: URL,
faceColor: CellColor.Value, edgeColor: CellColor.Value) extends NotNull {

/*
Sanity checks.
*/

  assert (reader.isInstanceOf [NotNull])
  assert (baseUrl ne null)
  assert (faceColor ne null)
  assert (edgeColor ne null)

/**
Flag indicating whether we have finished constructing the scene.

@todo This is a little fugly, but it works, so hey...  :-(
*/

  private var sceneRead = false

/**
''Cell'' definitions, indexed by name and initially empty.
*/

  private val definitions: Map [String, Cell] = Map ()

/**
Process the cell data.

Note: There is a single ''root'' cell element that is either a leaf primitive
or a set primitive that contains all remaining cells making up the scene.
Consequently, the root cell contains the entire scene itself.
*/

  private val rootCell = readNextCell ()

/*
By the time execution reaches this point, we'll have constructed the scene.
*/

  sceneRead = true

//-----------------------------------------------------------------------------
/**
Report the default face color for this scene.

@return Specified face color as an optional value.
*/
//-----------------------------------------------------------------------------

  private [cell] def defaultFaceColor = Some (faceColor)

//-----------------------------------------------------------------------------
/**
Report the default edge color for this scene.

@return Specified edge color as an optional value.
*/
//-----------------------------------------------------------------------------

  private [cell] def defaultEdgeColor = Some (edgeColor)

//-----------------------------------------------------------------------------
/**
Return the scene read as a ''ScalaFX'' 3D scene graph.

@return Cell's contents as a ''ScalaFX'' 3D scene graph.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def toNode = {
    assert (sceneRead)
    rootCell.toNode
  }

//-----------------------------------------------------------------------------
/**
Read next cell element from the stream and return it.

@param parent Set primitive that is to contain the cell read.  If `None`, then
the cell is the root cell of the scene, or is a definition.

@param isDefinition Flag indicating whether the cell to be read is a definition
cell (`true`) or a regular cell (`false`).  This should be known in advance by
the caller.  If this flag is `true`, then `parent` must be `None`.

@return Cell instance read from the file.  Note that the root cell contains all
cells belonging to this scene as its contents.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readNextCell (parent: Option [Set] = None, isDefinition:
  Boolean = false) = {

/*
Sanity check.

If this is a definition, then the parent must be undefined.
*/

    assert (!isDefinition || parent == None)

/*
Determine the code of the next cell element in the file.
*/

    val cellCode = readInt (CellScene.verifyCellCode (isDefinition),
    LibResource ("anim.cell.CellScene.readNextCell.cellCodeDesc", if
    (isDefinition) 1 else 0, CellScene.permittedCellCodes
    (isDefinition)))

/*
Retrieve the cell class associated with the indicated cell code.
*/

    val cellClass = CellScene.getCellClass (isDefinition, cellCode)

/*
Determine the constructor for this cell, and invoke it with the appropriate
arguments.

This will fail with an exception if a constructor taking the appropriate
arguments cannot be found.  Needless to say, this shouldn't happen if the
associated class has been supplied with such a constructor.
*/

    val classCtor = cellClass.getConstructor (getClass, classOf [Set])

/*
Create the new cell instance and return it.
*/

    val cell = classCtor.newInstance (this, parent)

/*
If this is a definition, then add it to the list of definitions.

Note that if the cell has no name, then this will result in an exception.
*/

    if (isDefinition) {
      assert (cell.isInstanceOf [Definition])
      definitions += (cell.name.get -> cell)
    }

/*
Return the cell read.
*/

    cell
  }

//-----------------------------------------------------------------------------
/**
Helper function to read a text value from the stream.

@note Unlike reading a string, reading text uses a different delimiter, that
allows spaces and tabs to be included in text: everything is read from the
stream up to the next line delimiter.  Note, however, that tabs are replaced
with spaces during the read operation.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readText (description: => String) = {
    val value = try {
      reader.readString (LineDelimiter)()
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }

/*
Replace any tabs in the input with spaces before returning.
*/

    value.map {
      c =>
      if (c == '\t') ' '
      else c
    }
  }

//-----------------------------------------------------------------------------
/**
Helper function to read an unverified string value from the stream.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readString (description: => String) = {
    val value = try {
      reader.readString ()
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value
  }

//-----------------------------------------------------------------------------
/**
Helper function to read a verified string value from the stream.

@param verifier Field verification function, used to verify value of field read
before it is returned.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readString (verifier: TextReader.Verifier [String],
  description: => String) = {
    val value = try {
      reader.readString (verifier)
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value
  }

//-----------------------------------------------------------------------------
/**
Helper function to read an unrestricted boolean value from the stream.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readBool (description: => String) = {
    val value = try {
      reader.readInt (value => value == 0 || value == 1)
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value == 1
  }

//-----------------------------------------------------------------------------
/**
Helper function to read a verified boolean value from the stream.

@param verifier Verification function to impose additional constraints upon the
boolean value read.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readBool (verifier: Int => Boolean, description: =>
  String) = {
    val value = try {
      reader.readInt (value => (value == 0 || value == 1) && verifier (value))
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value == 1
  }

//-----------------------------------------------------------------------------
/**
Helper function to read an unrestricted integer value from the stream.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readInt (description: => String) = {
    val value = try {
      reader.readInt ()
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value
  }

//-----------------------------------------------------------------------------
/**
Helper function to read a verified integer value from the stream.

@param verifier Field verification function, used to verify value of field read
before it is returned.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readInt (verifier: TextReader.Verifier [Int], description:
  => String) = {
    val value = try {
      reader.readInt (verifier)
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value
  }

//-----------------------------------------------------------------------------
/**
Helper function to read an unrestricted double value from the stream.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readDouble (description: => String) = {
    val value = try {
      reader.readDouble ()
    }
    catch {
      case e: Throwable => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value
  }

//-----------------------------------------------------------------------------
/**
Helper function to read a verified double value from the stream.

@param verifier Field verification function, used to verify value of field read
before it is returned.

@param description Function that is called to provide a description to supply
to the user in the event that an exception occurs.

@return Value read, if no exceptions arise.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def readDouble (verifier: TextReader.Verifier [Double],
  description: => String) = {
    val value = try {
      reader.readDouble (verifier)
    }
    catch {
      case e: Throwable  => CellScene.translateReaderException (e, LibResource
      ("anim.cell.CellScene.readValue", description))
    }
    value
  }

//-----------------------------------------------------------------------------
/**
Retrieve definition with specified name.

@param definitionName Name of the definition to be retrieved.

@return `Some` definition, if the definition has already been defined; `None`
if the definition has not yet been seen.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] final def getDefinition (definitionName: String) =
  definitions.get (definitionName)
}

//=============================================================================
/**
CellScene companion object.

@since 0.0
*/
//=============================================================================

private [cell] object CellScene {

/**
Type representing class information for a sub-class of
[[org.facsim.anim.cell.Cell!]].
*/

  type CellClass = Class [_ <: Cell]

/**
Map linking definition state (true = definition cell, false = regular cell) to
a map linking cell code to cell class.
*/

  private [this] val partitionedClassMap = partitionClassMap

//-----------------------------------------------------------------------------
/**
Function to initialize the relation between definition/regular cell status and
maps of cell code to cell class.

Regular cell elements (sets, tetrahedra, vector lists, instances, etc.) can
appear in the normal tree of elements.  However, definition cell elements can
only appear at the root of a definition&mdash;they are then included in the
scene via reference (by an instance cell element).

Because of the different places in which these two different types of cell are
defined, it makes sense to utilize two different ''cell code to cell class''
maps, one for regular cell elements and the other for definition cell elements.

This function starts with a map of cell codes to cell classes, and then
partitions it into two maps, one for definition cells and the other for regular
cells.

@return Map relating cell definition state (`true` = definition cell, `false` =
regular cell) to a map that relates cell codes to cell classes.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Type.html
AutoMod Cell Type Codes]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [this] def partitionClassMap = {

/*
Map associating cell type code with corresponding ''regular'' cell class.

All classes contained in his map must be concrete classes and must provide a
constructor taking CellScene reference and an Option [Set] arguments.
 
@note This map is defined in order of the cell codes for ease of maintenance by
a human (the resulting map itself is not ordered by cell code).  Please
maintain this order when modifying the list.
*/

    val classMap = Map [Int, CellClass] (
      100 -> classOf [Triad],
      115 -> classOf [VectorList],
      125 -> classOf [Polyhedron],
      130 -> classOf [Arc],                   // Originally, coarse arc
      131 -> classOf [Arc],                   // Originally, fine arc
      140 -> classOf [WorldText],
      141 -> classOf [ScreenText],
      142 -> classOf [ScreenText],
      143 -> classOf [UnrotateText],
      144 -> classOf [UnrotateText],
      150 -> classOf [WorldTextList],
      151 -> classOf [ScreenTextList],
      152 -> classOf [ScreenTextList],
      153 -> classOf [UnrotateTextList],
      154 -> classOf [UnrotateTextList],
      308 -> classOf [BlockDefinition],
      310 -> classOf [Trapezoid],
      311 -> classOf [Tetrahedron],
      315 -> classOf [Rectangle],
      330 -> classOf [Hemisphere],            // Originally, coarse hemisphere
      331 -> classOf [Hemisphere],            // Originally, fine hemisphere
      340 -> classOf [Cone],                  // Originally, coarse cone
      341 -> classOf [Cone],                  // Originally, fine cone
      350 -> classOf [Cylinder],              // Originally, coarse cylinder
      351 -> classOf [Cylinder],              // Originally, fine cylinder
      360 -> classOf [ConicFrustum],          // Originally, coarse frustum
      361 -> classOf [ConicFrustum],          // Originally, fine frustum
      388 -> classOf [FileReference],
      408 -> classOf [Instance],
      555 -> classOf [CompiledPicture],
      599 -> classOf [EmbeddedFile],
      700 -> classOf [RegularSet],
      7000 -> classOf [RegularSet],
      10000 -> classOf [RegularSet]
  )

/*
Class of the Definition cell type.

All sub-classes of the Definition trait are regarded as special cases: they can
only be defined as a definition, and not as part of the primary cell scene.
*/

    val definition = classOf [Definition]

/*
Helper function to determine if a class is a definition sub-class or not.
*/

    def isDefinition (cellClass: CellClass) =
    definition.isAssignableFrom (cellClass)

/*
Partition the map into two: one containing regular cell classes, and the other
containing definition cell classes.
*/

    val (definitionClassMap, regularClassMap) = classMap.partition (p =>
    isDefinition (p._2))

/*
Now construct, and return, the map relating definition state to class map.
*/

    Map [Boolean, Map [Int, CellClass]] (
      true -> definitionClassMap,
      false -> regularClassMap
    )
  }

//-----------------------------------------------------------------------------
/**
Function to verify a cell code.

@param definitionExpected If `true` the cell code read must be for a definition
cell; if `false`, the cell code must be for a regular cell.

@param cellCode Cell code read from the data file.

@return `true` if the cell code read from the file is value; `false` if it is
not a valid expected cell code.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Type.html
AutoMod Cell Type Codes]]

@since 0.0 
*/
//-----------------------------------------------------------------------------

  private def verifyCellCode (definitionExpected: Boolean)(cellCode: Int) =
  partitionedClassMap (definitionExpected).contains (cellCode)

//-----------------------------------------------------------------------------
/**
Function to report the set of permitted cell codes.

The set of codes permitted depends upon whether we're expecting a definition
(`definitionExpected` == `true`) or regular (`definitionExpected` == `false`)
cell code.

@param definitionExpected If `true` the cell code read must be for a definition
cell; if `false`, the cell code must be for a regular cell.

@return String containing a list of permitted cell codes.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Type.html
AutoMod Cell Type Codes]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def permittedCellCodes (definitionExpected: Boolean) =
  partitionedClassMap (definitionExpected).keys.toList.sorted.mkString (", ")

//-----------------------------------------------------------------------------
/**
Function to lookup the associated cell class for the specified cell code.

@param definitionExpected Flag indicating whether we're expecting a definition
cell (`true`) or a regular cell (`false`).

@param cellCode Integer code for which a cell class is to be looked-up.

@return Class information for the specified cell code.

@see [[http://facsim.org/Documentation/Resources/AutoModCellFile/Type.html
AutoMod Cell Type Codes]]

@since 0.0
*/
//-----------------------------------------------------------------------------

  private def getCellClass (definitionExpected: Boolean, cellCode: Int) =
  partitionedClassMap (definitionExpected)(cellCode)

//-----------------------------------------------------------------------------
/**
Translate a reader exception.

This function translates caught exceptions from [[org.facsim.io.TextReader!]]
operations into more appropriate cell error exceptions.  In addition, it adds
some extra information to the exception to assist with debugging cell read
failures.

@note The distinction between a [[org.facsim.anim.cell.ParsingErrorException!]]
(indicating that a file of the wrong type was passed to a loader) and a
[[org.facsim.anim.cell.IncorrectFormatException!]] (indicating that a problem
parsing the file was encountered) can be a fine one.  This function attempts to
address this distinction in a standard manner.

Firstly, all [[org.facsim.io.FieldConversionException]]s, indicating that cell
data failed to match the expected type of data (string, integer, double, etc.),
are treated as `IncorrectFormatException`s.  If a string is encountered when an
integer is expected, for instance, then that's a pretty good sign that the file
may not be a valid cell file.

[[org.facsim.io.FieldVerificationException]]s are more troublesome.  Mapping
all such exceptions to `IncorrectFormatException`s may be too severe, while
mapping them to `ParsingErrorException`s may be too lenient.  Still, a standard
mapping is required, so ''Facsimile'' treats such exceptions as the latter
type.

Similarly, IOExceptions are also treated as `ParsingErrorException`s.

It's possible that these rules may be refined in future editions to improve
handling of 3D file importing.

@param exception Exception to be translated.

@param msg A message to be added to the exception to explain what might have
just happened.

@return This function does not return and always throws an exception.

@throws [[org.facsim.anim.cell.IncorrectFormatException!]] if the file supplied
is not an ''AutoMod® cell'' file.

@throws [[org.facsim.anim.cell.ParsingErrorException!]] if errors are
encountered during parsing of the file.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [cell] def translateReaderException (exception: Throwable, msg:
  String): Nothing = exception match {

/*
Map field conversion exceptions (expected an Int, but got a string, for
example) to incorrect format exceptions.
*/

    case e: FieldConversionException =>
    throw new IncorrectFormatException (msg, e)

/*
Map field verification exceptions (data is correct type, but doesn't have an
acceptable value, such as an integer being outside of an allowed range) to
parsing error exceptions.
*/

    case e: FieldVerificationException =>
    throw new ParsingErrorException (msg, e)

/*
Map I/O exceptions (of which there are many different types) to parsing error
exceptions.
*/

    case e: IOException => throw new ParsingErrorException (msg, e)

/*
For all other exceptions, re-throw the original exception.
*/

    case _ => throw exception
  }
}