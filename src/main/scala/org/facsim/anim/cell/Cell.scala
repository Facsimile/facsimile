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
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import java.awt.Color
import javax.media.j3d.Appearance
import javax.media.j3d.LineAttributes
import javax.media.j3d.Material
import javax.media.j3d.TransparencyAttributes
import javax.vecmath.Color3f
import com.sun.j3d.loaders.IncorrectFormatException
import com.sun.j3d.loaders.ParsingErrorException
import org.facsim.io.TextReader

//=============================================================================
/**
Abstract base class for all ''[[http://www.automod.com/ AutoMod]] cell''
primitives.

@constructor Construct a new basic cell primitive.

@param scene Reference to the CellScene of which this cell is a part.

@param parent Parent set of this cell primitive.  If this value is
[[org.facsim.anim.cell.NullSet]], then this cell is the scene's root cell.

@param isDefinition If `true`, then the cell to be read is expected to be a
definition; if `false`, then it is an ordinary set member.

@since 0.0
*/
//=============================================================================

private [cell] abstract class Cell (scene: CellScene, parent: Option [Set],
isDefinition: Boolean) {

/**
Copy reference to the cell data.
*/

  private val data = scene.getCellData ()

/**
Read this cell's flags.

These flags determine which fields, if any, are included with the cell's
definition in the cell data stream, and also which characteristics are
inherited from the cell's parent.
*/

  private val flags = data.readAsInt ()

/**
Process bounding box data, if present.

We ignore bounding box data, but verify that the specified data makes sense.
*/

  processBoundingBox ()

/**
Retrieve cell ''face color''.

Face color is used to display the primitive in solid mode.  If cell attributes
are not present, the default cell face color is assumed.

Cell colors are represented by an integer code.

@note This value is ignored if the ''inherit color'' flag is set, with the face
color being inherited from the parent object.  However, even if color is
inherited, the field will be present in the cell data stream if the
''attributes present'' flag is set and must be read accordingly.
*/

  private val faceColor = processColor ()

/**
Retrieve cell ''edge color''.

Edge color is used to display the primitive in wireframe mode.  If cell
attributes are not present, the default cell edge color is assumed.

Cell colors are represented by an integer code.

@note This value is ignored if the ''inherit color'' flag is set, with the edge
color being inherited from the parent object.  However, even if color is
inherited, the field will be present in the cell data stream if the
''attributes present'' flag is set and must be read accordingly.
*/

  private val edgeColor = processColor ()

/**
Cell line style.

Cells support a number of different line-styles, which are applied in wireframe
mode (primarily).  If cell attributes are not present, the default line-style
is solid.

@note Cell data streams support line styles (such as dotted and halftone) that
are no longer supported by ''AutoMod''; ''Facsimile'' supports and recognizes
all cell line styles.
*/

  private val lineStyle = processLineStyle ()

/**
Cell line width.

Line width is measured in pixels.  If cell attributes are not present, the
default line width is 1 pixel.

@note Use of line width's other than 1 should be strongly discouraged.
*/

  private val lineWidth = processLineWidth ()

/**
Cell transparency flag.

Cells have a transparency level that varies between visible and almost
invisible (truly invisible cell's are unsupported by the cell format) - all of
which indicate that the cell primitive should be rendered as a solid.  However,
the cell format also supports drawing primitives solely in wireframe mode.  If
cell attributes are not present, the default transparency is solid (and fully
opaque).
*/

  private val transparency = processTransparency ()

/**
Cell name.
*/

  private val name = processName ()

/**
Now read the cell's joint data.
*/

  private val jointData = processJointData ()

//-----------------------------------------------------------------------------
/**
If bounding box data is present, read it in and verify it makes sense.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if discrepancies in bounding
box data are found.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processBoundingBox (): Unit = {

/*
If bounding box data is present, read it in from the data stream.  The data is
read in pairs of min & max for each axis.  If an error is found, an exception
is thrown.
*/

    if (Cell.Flag.isBoundingBoxPresent (flags)) {
      val minX = data.readAsDouble ()
      val maxX = data.readAsDouble ()
      if (minX > maxX) throw new ParsingErrorException ("Bounding box: MinX '"
      + minX.toString () + "' > MaxX '" + maxX.toString () + "' (line: " +
      data.getFieldRow () + ", col: " + data.getFieldColumn () + ")")
      val minY = data.readAsDouble ()
      val maxY = data.readAsDouble ()
      if (minY > maxY) throw new ParsingErrorException ("Bounding box: MinY '"
      + minY.toString () + "' > MaxY '" + maxY.toString () + "' (line: " +
      data.getFieldRow () + ", col: " + data.getFieldColumn () + ")")
      val minZ = data.readAsDouble ()
      val maxZ = data.readAsDouble ()
      if (minZ > maxZ) throw new ParsingErrorException ("Bounding box: MinZ '"
      + minZ.toString () + "' > MaxZ '" + maxZ.toString () + "' (line: " +
      data.getFieldRow () + ", col: " + data.getFieldColumn () + ")")
    }
  }

//-----------------------------------------------------------------------------
/**
Determine a cell's color.

''AutoMod cell'' colors have integer codes, corresponding to the colors defined
in the color array in the companion object.  Color data is present in the cell
data stream only if the AttributesPresent flag is set.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Cell color code.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if a color code read from the
cell data stream is not a valid color code.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processColor () = {

/*
If cell attribute information is present, then read it from the data stream.
*/

    if (Cell.Flag.areAttributesPresent (flags)) {
      val color = data.readAsInt ()
      if (!Cell.color.isDefinedAt (color)) throw new ParsingErrorException
      ("Invalid color code (line: " + data.getFieldRow ().toString () +
      ", col: " + data.getFieldColumn ().toString () + "): " + color.toString
      ())
      color
    }

/*
If attribute information is not present, the default color is red.
*/

    else Cell.CellColor.Red.id
  }

//-----------------------------------------------------------------------------
/**
Determine a cell's line style.

''AutoMod cell''s have an integer line-style code (corresponding to the
lineStyle array in the companion object).  This field is present in the cell
data stream only if the AttributesPresent flag is set.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Line style code.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if a line style code value
is outside of the permitted range.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processLineStyle () = {

/*
If cell attribute information is present, then read it from the data stream.
*/

    if (Cell.Flag.areAttributesPresent (flags)) {
      val lineStyle = data.readAsInt ()
      if (!Cell.lineStyle.isDefinedAt (lineStyle)) throw new
      ParsingErrorException ("Invalid line style code (line: " +
      data.getFieldRow ().toString () + ", col: " + data.getFieldColumn
      ().toString () + "): " + lineStyle.toString ())
      lineStyle
    }

/*
If attribute information is not present, the default linestyle is solid (0).
*/
    
    else Cell.LineStyle.Solid.id
  }

//-----------------------------------------------------------------------------
/**
Determine a cell's line width.

''AutoMod cell''s have an integer pixel line width.  This field is present in
the cell data stream only if the AttributesPresent flag is set.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Cell line width in pixels.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if a line width field has a
value outside of the permitted range.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processLineWidth () = {

/*
If cell attribute information is present, then read it from the data stream.
*/

    if (Cell.Flag.areAttributesPresent (flags)) {
      val lineWidth = data.readAsInt ()
      if (lineWidth < 1 || lineWidth > 7) throw new ParsingErrorException
      ("Invalid line pixel width (line: " + data.getFieldRow ().toString () +
      ", col:" + data.getFieldColumn ().toString () + "): " +
      lineWidth.toString ())
      lineWidth
    }

/*
If attribute information is not present, the default line width is 1.
*/
    
    else 1
  }

//-----------------------------------------------------------------------------
/**
Determine a cell's transparency.

''AutoMod cell''s have an integer transparency code (corresponding to the
transparency array in the companion object).  This field is present in the cell
data stream only if the AttributesPresent flag is set.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Cell transparency code.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if a transparency code has a
value outside of the permitted range.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processTransparency () = {

/*
If cell attribute information is present, then read transparency information
from the data stream.
*/

    if (Cell.Flag.areAttributesPresent (flags)) {
      val transparency = data.readAsInt ()
      if (!Cell.Transparency.isValidCode (transparency)) throw new
      ParsingErrorException ("Invalid transparency value (line: " +
      data.getFieldRow ().toString () + ", col:" + data.getFieldColumn
      ().toString () + "): " + transparency.toString ())
      transparency
    }

/*
Otherwise, the transparency is solid (1).
*/

    else Cell.Transparency.default
  }

//-----------------------------------------------------------------------------
/**
Determine a cell's name.

''AutoMod cell''s have associated names.  Cell names do not necessarily have to
be unique.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Cell name, or None if no cell name is defined.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if a cell name has invalid
characters.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processName () = {

/*
If cell attribute information is present, then read the cell name from the data
stream.
*/

    if (Cell.Flag.areAttributesPresent (flags)) {
      val name = data.readAsString ()
      Option (name)
    }

/*
Otherwise, this cell has no name.
*/

    else None
  }
//-----------------------------------------------------------------------------
/**
Determine a cell's joint data.

Cell joint data will be present if the ''joint data present'' flag is set.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Cell joint data.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if a cell name has invalid
characters.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processJointData () = {

/*
If cell joint data is present, then read it from the data stream.
*/

    if (Cell.Flag.isJointDataPresent (flags)) {
      val joint = new Joint (data)
      Option (joint)
    }

/*
Otherwise, this cell has no joint data.
*/

    else None
  }
}

//=============================================================================
/**
Cell companion object.

@since 0.0
*/
//=============================================================================

private object Cell {

/**
Cell flag bit field masks.

Each bit is a flag indicating whether corresponding information is available.
*/
Use a BitSet instead?
  private object Flag extends Enumeration {
    val AttributesPresent = Value (0x01)
    val JointDataPresent = Value (0x02)
    val CellGeometryPresent = Value (0x04)
    val GeometryInMatrixForm = Value (0x08)
    val InheritColor = Value (0x10)
    val Unknown1 = Value (0x20)
    val BoundingBoxPresent = Value (0x40)
    def areAttributesPresent (flags: Int) =
    ((flags & AttributesPresent.id) > 0)
    def isJointDataPresent (flags: Int) =
    ((flags & JointDataPresent.id) > 0)
    def isCellGeometryPresent (flags: Int) =
    ((flags & CellGeometryPresent.id) > 0)
    def isGeometryInMatrxiForm (flags: Int) =
    ((flags & GeometryInMatrixForm.id) > 0)
    def isColorInherited (flags: Int) =
    ((flags & InheritColor.id) > 0)
    def isBoundingBoxPresent (flags: Int) =
    ((flags & BoundingBoxPresent.id) > 0)
  }

/**
Cell color name enumeration.

@note A deliberate effort has been made to use the standard ''Java'' colors
were possible, rather than the original ''AutoMod RGB'' color encodings.  We
want ''Facsimile'' to look a little different to ''AutoMod''.
*/

  private object CellColor extends Enumeration {
    val Black = Value
    val Red = Value
    val Green = Value
    val Yellow = Value
    val Blue = Value
    val Magenta = Value
    val Cyan = Value
    val White = Value
    val LightGray = Value
    val DarkGray = Value
    val Brown = Value
    val Purple = Value
    val Orange = Value
    val LightBlue = Value
    val LightGreen = Value
    val LightYellow = Value
    private val color = Array (
      new Color3f (Color.BLACK),
      new Color3f (Color.RED),
      new Color3f (Color.GREEN),
      new Color3f (Color.YELLOW),
      new Color3f (Color.BLUE),
      new Color3f (Color.MAGENTA),
      new Color3f (Color.CYAN),
      new Color3f (Color.WHITE),
      new Color3f (Color.LIGHT_GRAY),
      new Color3f (Color.DARK_GRAY),
      new Color3f (Color.RED.darker ()),
      new Color3f (Color.MAGENTA.darker ()),
      new Color3f (Color.ORANGE),
      new Color3f (Color.BLUE.brighter ()),
      new Color3f (Color.GREEN.brighter ()),
      new Color3f (Color.YELLOW.brighter ())
    )
    def toColor3f (code: Int) = color (code)
    def apply (value: Value) = toColor3f (value.id)
  }

/**
Array of cell color code to color (as a material).

A note on ''Java3D'' materials:
 -  ''Ambient'' color is the color an object is displayed in ambient light.
    If lighting is important, then this would be black, indicating that an
    object is in shadow unless it's illuminated.  Since cell file scenes are
    typically exclusively lit with ambient lighting, we'll use the object's
    color here. 
 -  ''Emissive'' color is the color emitted by objects that generate their own
    light, such as light bulbs, etc.  We turn off emissive color by setting
    this to black.
 -  ''Diffuse'' color is the color emitted by objects when illuminated by
    non-ambient lighting, with the intensity determined by the angle of
    illumination.  We set this to the object's color.
 -  ''Specular'' color is the color reflected back when illuminated.  We set
    this to white.
 -  ''Shininess'' determines the reflectivity of a material.  Since we're
    picking materials for cell files, we'll be conservative here, picking a
    shininess of 0.2 (on a scale of 0.0 to 1.0).
*/

  import CellColor._
  val shininess = 0.2f
  private val color = Array [Material] (
    new Material (CellColor (Black), CellColor (Black), CellColor (Black),
    CellColor (White), shininess),
    new Material (CellColor (Red), CellColor (Black), CellColor (Red),
    CellColor (White), shininess),
    new Material (CellColor (Green), CellColor (Black), CellColor (Green),
    CellColor (White), shininess),
    new Material (CellColor (Yellow), CellColor (Black), CellColor (Yellow),
    CellColor (White), shininess),
    new Material (CellColor (Blue), CellColor (Black), CellColor (Blue),
    CellColor (White), shininess),
    new Material (CellColor (Magenta), CellColor (Black), CellColor (Magenta),
    CellColor (White), shininess),
    new Material (CellColor (Cyan), CellColor (Black), CellColor (Cyan),
    CellColor (White), shininess),
    new Material (CellColor (White), CellColor (Black), CellColor (White),
    CellColor (White), shininess),
    new Material (CellColor (LightGray), CellColor (Black), CellColor
    (LightGray), CellColor (White), shininess),
    new Material (CellColor (DarkGray), CellColor (Black), CellColor
    (DarkGray), CellColor (White), shininess),
    new Material (CellColor (Brown), CellColor (Black), CellColor (Brown),
    CellColor (White), shininess),
    new Material (CellColor (Purple), CellColor (Black), CellColor (Purple),
    CellColor (White), shininess),
    new Material (CellColor (Orange), CellColor (Black), CellColor (Orange),
    CellColor (White), shininess),
    new Material (CellColor (LightBlue), CellColor (Black), CellColor
    (LightBlue), CellColor (White), shininess),
    new Material (CellColor (LightGreen), CellColor (Black), CellColor
    (LightGreen), CellColor (White), shininess),
    new Material (CellColor (LightYellow), CellColor (Black), CellColor
    (LightYellow), CellColor (White), shininess)
  )

/**
Cell linestyle enumeration.
*/

  private object LineStyle extends Enumeration {
    val Solid = Value
    val Dashed = Value
    val Dotted = Value
    val Halftone = Value
  }

/**
Map of line style to ''Java3D'' line styles.

Note that ''AutoMod'' as of version 10.0 and later only appears to support
solid and dashed styles.  This utility does better.
*/

  private val lineStyle = Array [Int] (
    LineAttributes.PATTERN_SOLID,       // Solid
    LineAttributes.PATTERN_DASH,        // Dashed
    LineAttributes.PATTERN_DOT,         // Dotted
    LineAttributes.PATTERN_DASH_DOT     // Half-tone
  )

/**
Default line attributes.
*/

  private val defaultLineAttributes = new LineAttributes ()

/**
Cell transparency settings.
*/

  private object Transparency extends Enumeration {
    val Wireframe = Value
    val Solid = Value
    val Transparent1 = Value
    val Transparent2 = Value
    val Transparent3 = Value
    val Transparent4 = Value
    val Transparent5 = Value
    val Transparent6 = Value
    val Transparent7 = Value
    val Transparent8 = Value
    val Transparent9 = Value
    val Transparent10 = Value
    val Transparent11 = Value
    val Transparent12 = Value
    val Transparent13 = Value
    val Transparent14 = Value
    val Transparent15 = Value
    private val minCode = Wireframe.id
    private val maxCode = Transparent15.id
    val default = Solid.id
    def isValidCode (code: Int) = (code >= minCode && code <= maxCode)
  }
}
