//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2018, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================
// Scala source file belonging to the org.facsim.sfx.importers.cell package.
//======================================================================================================================
package org.facsim.sfx.importers.cell

import java.io.InputStream
import javafx.geometry.Point3D
import javafx.scene.Node
import javafx.scene.paint.Material
import javafx.scene.transform._
import org.facsim.util.parse.BaseParser
import org.parboiled2._
import scala.util.Try
import shapeless.PolyDefns.~>

/** Parser for ''AutoMod® cell''-format 3D scenegraph files.
 *
 *  A ''cell'' scenegraph is constructed from graphics primitives, termed ''cells''. The majority of ''cell'' types
 *  correspond to graphical elements, but certain types of ''cell'' can contain other ''cell'' elements, references to
 *  external graphics files, block definitions, block references, etc.
 *
 *  A cell definition has the following basic structure:
 *
 *  {{{
 *  cell header
 *  cell bounding box (if specified flag set)
 *  cell attributes (if specified flag set)
 *  cell joint data (if specified flag set)
 *  cell transformation data (if specified flag set)
 *  cell type-specific data
 *  }}}
 *
 *  Most cell fields are delimited by ''white space'', with no distinction between ''spaces'', ''tabs'', ''line
 *  terminators'', etc. and with consecutive ''white space'' characters treated as a single delimiter. (The sole
 *  exception to this rule text elements, which are terminated by ''line terminators''.)
 *
 *  Many ''cell'' fields are numeric in nature, which makes them particularly hard for a human to comprehend.
 *
 *  Traditionally, ''cell'' files were 7-bit US ''ASCII'' encoded files. However, ''cell'' files can now contain text
 *  encoded in ''UTF-8'' form, and should be processed as such. There is no formal method for specifying file encoding
 *  within a ''cell'' scene. This parser assumes that the input has been decoded, and so represents natural _Java_
 *  strings; input should not be initialized from a raw byte array.
 *
 *  @constructor Construct a ''Parboiled2'' parser for an ''AutoMod® cell''-format 3D scene.
 *
 *  @param input Input to be parsed.
 *
 *  @param colorScheme Color scheme to be employed for the ''cell'' scene's colors.
 */
private[cell] final class CellParser(input: ParserInput, colorScheme: CellColorScheme)
extends BaseParser {

  /** Rule to process a ''cell'' Boolean field.
   *
   *  In ''cell'' scenes, a Boolean value is represented by integers 0 (denoting `false`) and 1 (denoting `true`).
   *
   *  @return Rule to process a ''cell'' Boolean field into a Boolean value.
   */
  private[cell] def boolValue: Rule1[Boolean] = rule {
    intValue.named("boolean") ~> {i =>
      test(i == 0 || i == 1) ~ push (i == 1)
    }
  }

  /** Rule to process ''cell type'' fields.
   *
   *  These are integer values that identify the type of ''cell'' element being dealt with. The corresponding cell type
   *  is pushed onto the stack for use in subsequent processing (each ''cell'' type has a different set of parameter
   *  values that need to be read from the ''cell'' definition).
   *
   *  @note If ''AutoMod'' adds new ''cell'' types to the scene format, which they are perfectly entitled to do, then
   *  this rule will fail. Any such failure should be reported to the _Facsimile_ development team so that we can update
   *  this parser to handle the new type.
   *
   *  @return Rule converting a ''cell'' type code to a ''cell'' type instance.
   */
  private[cell] def cellType: Rule1[CellType] = rule {
    intValue.named("cell type") ~> {ct =>
      test(CellType.verify(ct)) ~ push(CellType(ct))
    }
  }

  /** Rule to process ''cell flag'' fields.
   *
   *  Flag fields are converted into Flag field instances, and can be used for subsequent cell parsing.
   *
   *  @return Rule to process a ''cell flag'' field into a CellFlags instance.
   */
  private[cell] def cellFlags: Rule1[CellFlags] = rule {
    intValue.named("cell flags") ~> (new CellFlags(_))
  }

  /** Rule to process a ''cell'' header record.
   *
   *  The contents of this header identify how the remainder of the cell are to be processed.
   *
   *  @return Rule to process a ''cell'' header record, pushing a [[CellHeader]] instance onto the stack.
   */
  private[cell] def cellHeader: Rule1[CellHeader] = rule {
    cellType ~ cws ~ cellFlags ~> {(ch, cf) =>
      CellHeader(ch, cf)
    }
  }

  /** Rule to process a range pair.
   *
   *  Range pairs are employed in a ''cell bounding box'' to specify the minimum and maximum values for the bounding box
   *  as measured along a particular axis. This rule verifies that the maximum value is greater than or equal to the
   *  minimum value, assuring consistency. This rule pushes nothing onto the stack.
   *
   *  @return Rule that processes a range pair. Nothing is pushed onto the stack.
   */
  private[cell] def rangePair: Rule0 = rule {
    dblValue.named("range minimum") ~ cws ~ dblValue.named("range maximum") ~> {(min, max) =>
      test(min <= max)
    }
  }

  /** Rule function to process a bounding box.
   *
   *  Bounding boxes are defined by three range pair values, for the X-, Y- and Z-axes, respectively, with each pair
   *  identifying the minimum and maximum position on that each axis of the bounding box.
   *
   *  Bounding boxes are present in a ''cell'' element definition, only if the corresponding ''cell flag'' is set.
   *
   *  @note Bounding boxes are redundant, since they can be generated by ''JavaFX'' (as well as by the ''cell'' scene
   *  renderer) from the defined geometry information; consequently, there are discarded by this parser. However, the
   *  fields must still be parsed and verified to ensure the validity of the ''cell'' scene. Despite this, we must
   *  still push a value onto the stack so that we can use the rule in a condition.
   */
  private[cell] val boundingBox: () => Rule1[Int] = () => rule {
    rangePair.named("X-axis") ~ cws ~ rangePair.named("Y-axis") ~ cws ~ rangePair.named("Z-axis") ~ push(0)
  }

  /** Rule to conditionally parse a ''cell bounding box'' record.
   *
   *  A bounding box record is only present for the current ''cell'' element if the corresponding ''cell'' flag is set;
   *  if the flag is clear, then no bounding box record will present. Either way, bounding box information is redundant
   *  and is discarded after processing; this rule pushes nothing to the stack.
   *
   *  @param hdr Header for the current ''cell'' element.
   *
   *  @return Rule parsing, but discarding, bounding box of the current ''cell''.
   */
  private[cell] def boundingBoxOption(hdr: CellHeader): Rule0 = rule {
    conditional(hdr.flags.boundingBoxPresent, boundingBox) ~> {_ =>
      // Consume dummy value from stack posted by the boundingBox rule function.
    }
  }

  /** Rule to process ''cell'' fields containing color definitions.
   *
   *  Color fields are converted to equivalent ''JavaFX'' material instances.
   *
   *  @note Colors are represented by integers in the range [0-15 and are treated as fully opaque. Whether that color is
   *  employed, or the color is inherited from a parent, depends upon the ''inherited color'' field in the ''cell
   *  flags'' field.
   *
   *  @return Rule to process a ''cell'' color field into an equivalent ''JavaFX'' material.
   */
  private[cell] def color: Rule1[Material] = rule {
    intValue.named("color") ~> {c =>
      test(CellColor.verify(c)) ~ push(CellColor(c, colorScheme))
    }
  }

  /** Rule to process ''cell'' line style fields that define how lines are drawn.
   *
   *  @note ''JavaFX'' currently does not support line styles in 3D scenes, and so this rule merely consumes the field,
   *  and checks it's validity. It pushes nothing onto the stack in return.
   *
   *  @return Rule to process and verify a ''cell'' line style field; nothing is pushed to the stack.
   */
  private[cell] def lineStyle: Rule0 = rule {
    intValue.named("line style") ~> {ls =>
      test(LineStyle.verify(ls))
    }
  }

  /** Rule to process ''cell'' line width fields that define how thick lines are drawn.
   *
   *  @note ''JavaFX'' currently does not support line widths in 3D scenes, and so this rule merely consumes the field,
   *  and checks it's validity. It pushes nothing onto the stack in return.
   *
   *  @return Rule to process and verify a ''cell'' line width field; nothing is pushed to the stack.
   */
  private[cell] def lineWidth: Rule0 = rule {
    intValue.named("line width") ~> {lw =>
      test(LineWidth.verify(lw))
    }
  }

  /** Rule to process display style fields that define how the associated ''cell'' primitive appears.
   *
   *  Display styles may be wireframe, or solid with a specified transparency/opacity.
   *
   *  @note ''JavaFX'' supports the display of 3D elements in wireframe mode, but currently has issues relating to
   *  opacity. In particular, there is a [[https://bugs.openjdk.java.net/browse/JDK-8090548 bug]] relating to how
   *  elements with opacity are rendered. Furthermore, ''JavaFX'' seems to support two different mechanisms for
   *  specifying opacity: the `opacity` property that all [[javafx.scene.Node]] elements possess (which does not
   *  currently appear to work in 3D scenes) and the `alpha` channel property of [[javafx.scene.paint.Color]] (which
   *  does appear to work in 3D scenes, but which isn't ideal). The ''Facsimile cell'' parser preserves opacity
   *  information and uses it with the former method. It is hoped that this will be supported better by ''JavaFX'' in
   *  the future.
   *
   *  @return Rule converting a ''cell'' display style field into an opacity value, wrapped in [[scala.Some]] or
   *  [[scala.None]] if the element is to be rendered in wireframe.
   */
  private[cell] def displayStyle: Rule1[Option[Double]] = rule {
    intValue.named("display style") ~> {ds =>
      test(DisplayStyle.verify(ds)) ~ push(DisplayStyle(ds))
    }
  }

  /** Set of valid first characters of a ''cell'' identifier.
   *
   *  ''Cell identifier'' names must begin with an alphabetic character.
   */
  private[cell] val cellIdStart = CharPredicate.Alpha

  /** Set of valid subsequent characters (i.e. not the first) of a ''cell'' identifier.
   *
   *  ''Cell identifiers'' can contain alphanumeric characters plus the underscore.
   */
  private[cell] val cellIdPart = CharPredicate.AlphaNum ++ '_'

  /** Rule to process cell identifier names.
   *
   *  @note Stricly, cell names are no longer than 22 characters in length, and contain only alphanumeric characters and
   *  the underscore (and the first character must be alphabetic). Identifiers do not need to be unique, except when
   *  identifying ''instances'', ''definitions'', ''joints'' and ''terminal control frames''
   *
   *  @return Rule to convert a ''cell identifier'' record into an optional string.
   */
  private[cell] def cellId: Rule1[Option[String]] = rule {
    capture(cellIdStart ~ zeroOrMore(cellIdPart)).named("cell identifier") ~> (Some(_))
  }

  /** Function returning rule to process a ''cell'' attributes record.
   *
   *  @note ''Edge'' colors are not supported by ''JavaFX'' and are therefore ignored. However, it should be noted that
   *  they are also ignored in the ''OpenInventor''-based ''cell'' scene renderer in ''AutoMod'' (and, probably, in the
   *  new ''Hoops''-based ''cell'' scene renderer too).
   */
  private[cell] val attributes: () => Rule1[CellAttributes] = () => rule {
    color.named("face color") ~ cws ~ color.named("edge color") ~ cws ~ lineStyle ~ cws ~ lineWidth ~ cws ~
    displayStyle ~  cws ~ cellId ~> {(fm, _, ds, id) =>

      // Note that the edge color parsed is ignored (it has to be placed on the stack, but we do nothing with it).
      CellAttributes(fm, ds, id)
    }
  }

  /** Rule to conditionally process the attributes record of a ''cell'' element.
   *
   *  If the ''cell'' flags indicate that the attributes record is present, it is processed and acted upon. Otherwise, a
   *  default set of attributes will be provided.
   *
   *  @param hdr Header defined for the current ''cell'' element.
   *
   *  @return Rule determining attributes of the current ''cell'', using default values if none provided.
   */
  private[cell] def attributesOption(hdr: CellHeader): Rule1[CellAttributes] = rule {

    // If the flags indicate that cell attributes are present, then read them in using the attributes function.
    conditional(hdr.flags.attributesPresent, attributes) ~> {oca =>

      // If attributes are not present, then provide a default set for this color scheme.
      oca.getOrElse(CellAttributes.default(colorScheme))
    }
  }

  /** Rule to process a ''cell joint type'' field.
   *
   *  A corresponding joint type value will be pushed to the stack.
   *
   *  @return Rule to convert a ''cell'' joint type field into a JointType instance.
   */
  private[cell] def jointType: Rule1[JointType] = rule {
    intValue.named("joint type") ~> {jt =>
      test(JointType.verify(jt)) ~ push(JointType(jt))
    }
  }

  /** Rule to process a ''cell'' joint speed field.
   *
   *  ''Cell'' joint speeds are double fields, with a value greater than or equal to zero. The resulting speed, measured
   *  in a joint delta per hundredth of a time unit, is pushed onto the stack. Note that a speed of zero is treated as
   *  being ''infinite'' when calculating joint transition times.
   *
   *  @note If the associated ''join type'' is translational, then the speed is a translational distance per time unit
   *  value; if rotational, then a degrees per time unit value; if not a joint, then this value is ignored.
   *
   *  @return Rule converting a joint speed value into a Double value.
   */
  private[cell] def jointSpeed: Rule1[Double] = rule {
    dblValue.named("joint speed") ~> {js =>
      test(js >= 0.0) ~ push(js)
    }
  }

  /** Rule to process a ''cell'' joint's range (minimum, maximum and current position).
   *
   *  Each field is a double value that must satisfy the relationship: minimum <= current <= maximum. A joint range
   *  configuration containing this information is pushed to the stack.
   *
   *  @return Rule converting joint range fields into a [[JointRange]] value.
   */
  private[cell] def jointRange: Rule1[JointRange] = rule {
    dblValue.named("joint minimum") ~ cws ~ dblValue.named("joint maximum") ~> {(min, max) =>
      test(min <= max) ~ push(min) ~ push(max) ~ cws ~ dblValue.named("joint current") ~> {(minimum, maximum, current)
      =>
        test(current >= minimum && current <= maximum) ~ push(JointRange(minimum, current, maximum))
      }
    }
  }

  /** Rule to process a ''cell terminal control frame'' (''TCF'') present flag.
   *
   *  If this flag is defined, then the current ''cell'' is a location for attaching elements to the scene dynamically.
   *  For instance, a ''cell'' defining a ''vehicle'' might have one or more ''TCF''s identifying the location(s) at
   *  which part(s) are carried.
   *
   *  @return Rule converting a ''TCF'' data present flag into a Boolean value that is pushed onto the stack.
   */
  private[cell] def tcfPresentFlag: Rule1[Boolean] = rule {
    boolValue.named("TCD data present")
  }

  /** Rule to process the ''joint termination'' field.
   *
   *  This is an integer field that always contains the value 0. Nothing is pushed to the stack by this rule.
   *
   *  @return Rule that consumes a valid ''joint termination'' field, pushing nothing to the stack.
   */
  private[cell] def jointTerminator: Rule0 = rule {
    intValue.named("joint terminator") ~> {jt =>
      test(jt == 0)
    }
  }

  /** Rule to process ''cell'' translation transformation records.
   *
   *  Three consecutive double fields, with no value restrictions, represent respective translations along the local X-,
   *  Y- and Z-axes.
   *
   *  @return Rule to convert a translation transformation record into an optional ''translation transformation''.
   */
  private[cell] def translations: Rule1[Option[Transform]] = rule {
    dblValue.named("X translate") ~ cws ~ dblValue.named("Y translate") ~ cws ~ dblValue.named("Z translate") ~>
    {(xTran, yTran, zTran) =>

      // If all of the translations are zero, then push None as the result. This prevents us from applying a translation
      // that is unnecessary.
      if(xTran == 0.0 && yTran == 0.0 && zTran == 0.0) None

      // Otherwise, create a new translation transformation for this element.
      else Some(new Translate(xTran, yTran, zTran))
    }
  }

  /** Rule to process ''cell'' rotation order fields that define the sequence in which axis rotations are applied.
   *
   *  @return Rule converting a rotation order field to sequence of axes.
   */
  private[cell] def rotationOrder: Rule1[Seq[Point3D]] = rule {
    intValue.named("rotation order") ~> {ro =>
      test(RotationOrder.verify(ro)) ~ push(RotationOrder(ro))
    }
  }

  /** Convert an axis rotation to a [[javafx.scene.transform.Rotate]] transformation.
   *
   *  @param pair A tuple, composed of a rotation angle, measured in degrees, and the axis to which the rotation is to
   *  be applied.
   *
   *  @return A Rotate transformation wrapped in [[scala.Some]] if the rotation angle is not 0.0; [[scala.None]]
   *  otherwise.
   */
  private def processRotate(pair: (Double, Point3D)): Option[Transform] = {
    if(pair._1 == 0.0) None
    else Some(new Rotate(pair._1, pair._2))
  }

  /** Rule to process a rotation transformation record.
   *
   *  A rotation record consists of a rotation order (which specifies the sequence of axes to which rotations will be
   *  applied), followed by three consecutive double fields, each representing a local rotation in degrees, with no
   *  value restriction.
   *
   *  Rotations of zero should be filtered out since they have no effect upon element position.
   *
   *  @return Rule to convert a rotation transformation record into an sequence of optional ''rotation
   *  transformations''.
   */
  private[cell] def rotations: Rule1[Seq[Option[Transform]]] = rule {
    rotationOrder ~ cws ~ dblValue.named("rotation 1") ~ cws ~ dblValue.named("rotation 2") ~ cws ~
    dblValue.named("rotation 3") ~>  {(axes, rot1, rot2, rot3) =>

      // Create a zipped sequence of axes and rotations.
      val rots = Seq(rot1, rot2, rot3).zip(axes)

      // Process each rotation into an optional transformation.
      rots.map(processRotate)
    }
  }

  /** Rule to process scaling transformation records.
   *
   *  Three consecutive double fields, with no value restrictions, represent respective scalings along the local X-, Y-
   *  and Z-axes.
   *
   *  @return Rule to convert a scaling transformation record into an optional ''scaling transformation''.
   */
  private[cell] def scalings: Rule1[Option[Transform]] = rule {
    dblValue.named("X scale") ~ cws ~ dblValue.named("Y scale") ~ cws ~ dblValue.named("Z scale") ~> {(xScl, yScl, zScl)
    =>

      // If all of the scalings are one, then push None as the result. This prevents us from applying a scaling that is
      // unnecessary.
      if(xScl == 1.0 && yScl == 1.0 && zScl == 1.0) None

      // Otherwise, create a new translation transformation for this element.
      else Some(new Scale(xScl, yScl, zScl))
    }
  }

  /** Function returning rule to process a ''cell'' transformation record (regular, non-''matrix form'').
   *
   *  A non-matrix transformation record is comprised of a translation record, followed by a rotation record, followed
   *  by a scaling record. Each of these records returns optional transformations, which can then be merged into a
   *  single sequence of transformations. If no transformations are required (that is, if all of the transformations are
   *  [[scala.None]], this resulting sequence will be empty.
   *
   *  @note A ''cell'' definition may have multiple transformation records. This function can be used to process all of
   *  them, in association with the relevant conditions, provided that transformations are not specified in matrix form.
   *  Transformations in matrix form should be processed using [[transformMatrix]] instead.
   */
  private[cell] val transformations: () => Rule1[Seq[Transform]] = () => rule {
    translations ~ cws ~ rotations ~ cws ~ scalings ~> {(ot, sor, os) =>

      // Merge all of the transformations into a single sequence, then filter out those that are not defined. Note that
      // the order in which the transformations are merged matters: translations take effect before rotations, which
      // take effect before scalings.
      val all = ot +: (sor :+ os)
      all.flatten
    }
  }

  /** Function returning rule to process a ''cell'' transformation record (matrix form).
   *
   *  A ''matrix form'' transformation matrix comprises a 4 x 4 ''affine'' transformation made up of double values. (In
   *  practice, the matrix is made up of 16 values, organized into 4 rows of 4 values.)
   *
   *  This matrix is processed and converted into a single transformation that is returned in a sequence (for
   *  compatibility with the [[transformations]] function.
   *
   *  @note A ''cell'' definition may have multiple transformation records. This function can be used to process all of
   *  them, in associated with the relevant conditions, provided that transformations are specified in matrix form.
   *  Regular, non-''matrix form'' transformations should be processed using [[transformMatrix]] instead.
   */
  private[cell] val transformMatrix: () => Rule1[Seq[Transform]] = () => rule {
    16.times(dblValue.named("transform matrix")).separatedBy(cws) ~> {x => //scalastyle:ignore magic.number

      // Try creating the affine matrix. This might result in an exception if the matrix is not affine or if the last
      // row of the matrix is not [0, 0, 0, 1]. If an exception occurs, the rule should fail.
      val m = Try(new Affine(x.toArray, MatrixType.MT_3D_4x4, 0))

      // Verify we were successful, and push the transformation within a sequence.
      test(m.isSuccess) ~ push(Seq(m.get))
    }
  }

  /** Conditional rule to process ''cell'' transformation records according to the ''cell'' flags.
   *
   *  Process a transformation record, depending upon whether it is in regular, non-''matrix form'', or (the much rarer)
   *  ''affine matrix form''.
   *
   *  @note This rule is used for processing joint, ''terminal control frame'' and ''cell'' transformation records.
   *
   *  @param hdr Header for the current ''cell'' element, which determines which format is used for the transformation
   *  record.
   *
   *  @return Rule processing transformation records&mdash;in the format appropriate to the current
   *  ''cell''&mdash;resulting in a sequence of equivalent ''JavaFX'' transformations.
   */
  private[cell] def transformType(hdr: CellHeader): Rule1[Seq[Transform]] = rule {
    conditional(hdr.flags.transformationInMatrixForm, transformMatrix, transformations)
  }

  /** Rule function to process ''cell'' joint information.
   *
   *  ''Cell'' elements can possess dynamic ''joint'' information (in which the contents of the joint can be translated
   *  or rotated about the joint's local Z-axis) as we as ''TCF'' information (supporting the dynamic addition of
   *  elements to the ''cell'' element).
   *
   *  @param hdr Header for the current ''cell'' element, which determines whether joint data is present, and which
   *  format is used for transformation data.
   *
   *  @return Rule process a ''cell'' element's joint data, pushing the result onto the stack.
   */
  private[cell] def jointData(hdr: CellHeader): Rule1[JointData] = rule {
    jointType ~ cws ~ jointSpeed ~ cws ~ jointRange ~ cws ~ tcfPresentFlag ~ cws ~ transformType(hdr) ~ cws ~
    jointTerminator ~> {(jt, js, jr, tcf, jts) =>
      if(tcf) {
        if(hdr.flags.transformationInMatrixForm) rule {
          transformMatrix ~> {tcfTS: Seq[Transform] =>
            JointData(jt, js, jr, jts, tcfTS)
          }
        }
        else rule {
          transformations ~> {tcfTS =>
            JointData(jt, js, jr, jts, tcfTS)
          }
        }
      }
      else rule {
        JointData(jt, js, jr, jts, Nil)
      }
    }
  }

  /** Conditional rule to process the primary transformation record of a ''cell'', if the corresponding flag is set.
   *
   *  If the ''cell'' header indicates that a cell transformation record is present (in whatever form), then this rule
   *  will process it and push the resulting transformation sequence to the stack. If no such records are present, an
   *  empty sequence will be pushed to the stack instead.
   *
   *  @param hdr Header for the current ''cell'' element, which determines which format is used for the transformation
   *  record, and whether such records are present.
   *
   *  @return Rule to convert a ''cell'' transformation record to a sequence of ''JavaFX'' transformations, if present;
   *  an empty sequence otherwise.
   */
  private[cell] def transformOption(hdr: CellHeader): Rule1[Seq[Transform]] = {

    // If a transformation record is present, then read it as appropriate.
    if(hdr.flags.transformationPresent) transformType(hdr)

    // Otherwise, push an empty list to the stack.
    else rule {
      push(Nil)
    }
  }

  /** A ''cell set'' primitive.
   *
   *  At the end of this primitive's definition is a count, followed by that number of child cells. The count must be 0
   *  or greater, and the number of child cells present specified must all be present.
   *
   *  The cell type codes for this primitive are: 10000 (''root set''), 7000 (''main set'') and 700 (''regular
   *  set'')&mdash;all of which are treated identically by this parser.
   */
  private[cell] def set = rule {
    intValue
  }

  /** A single ''cell'' primitive definition.
   *
   *  ''Sets'' may contain other cells.
   *
   *  @return Rule to convert associated ''cell'' primitive into a ''JavaFX'' [[javafx.scene.Node]].
   */
  private[cell] def cell: Rule1[Node] = rule {

    // Start by processing the cell header. We'll use the header to process the remainder of the cell.
    cellHeader ~> {ch ~>

      boundingBoxOption(ch) ~ cws ~ attributesOption(ch) ~ cws ~ jointDataOption(ch) ~ cws ~ transformOption(ch)

    }
  }

  /** Entire ''cell scene'' rule.
   *
   *  @note A ''cell scene'' terminates with ''end-of-input''
   */
  private[cell] def cellScene = rule {
    cell ~ optional(ws) ~ EOI
  }
}

/** Support for parsing ''AutoMod® cell''-format 3D scenegraph files.
 *
 *  ''[[http://www.automod.com/ AutoMod®]]'' is a proprietary ''commercial off-the-shelf'' (''COTS'') simulation
 *  product. ''AutoMod®'' is a registered trademark of ''[[http://www.appliedmaterials.com Applied Materials, Inc.]]''.
 *
 *  Within ''AutoMod®'' models, 3D objects are represented using the proprietary ''cell format'', in which 3D scenes
 *  are described by a number of primitives, called ''cells''. The format is hierarchical, with a special type of
 *  ''cell'', termed a ''set'', able to hold zero or more other ''cell'' definitions.
 *
 *  ''Cell format'' provides support for:
 *   - Rudimentary translational and rotational kinematic joint information.
 *   - Colors inherited from a parent ''cell''.
 *   - Opacity,
 *   - Block definitions and insertions.
 *   - Embedded and externally referenced ''Virtual Reality Markup Language'' 97 (''VRML97'') scenes.
 *   - Embedded and extenerally referenced ''OpenInventor'' (''OIV'') scenes.
 *   - ''Compiled cells'' (optimized to render faster).
 *
 *  This parser is able to translate ''cell'' files into largely equivalent ''JavaFX'' scenes.
 *
 *  The following ''cell format'' features are currently unsupported:
 *   - ''Vector List cell'' primitives. (Due to lack of ''JavaFX'' support.)
 *   - ''Triad cell'' primitives. (Due to lack of ''JavaFX'' support.)
 *   - ''Compiled picture cell'' primitives. (Due to lack of information about the format of these primitives.)
 *   - ''Edge colors''. (Due to lack of ''JavaFX'' support.) ''Cell'' elements are represented solely by their defined
 *     ''face colors''.
 *
 *  @since 0.0
 */
object CellParser {

  /** Parse an input stream containing an ''AutoMod cell'' scene definition.
   *
   *  @param is Input stream to be parsed as a ''cell format'' scene.
   *
   *  @return ''JavaFX'' [[javafx.scene.Node]] wrapped in a [[scala.util.Success]] containing the equivalent ''JavaFX''
   *  scene, if `is` was parsed successfully, or a failure exception wrapped in a [[scala.util.Failure]] otherwise. In
   *  the latter case, an exception will describe the cause of the failure and indicate where in the stream the parser
   *  failed.
   *
   *  @since 0.0
   */
  def apply(is: InputStream): Try[Node] = {

    ??? // TODO: Convert input stream to a byte array.
    val bs = Array[Byte]()
    new CellParser(is).cellScene.run()
  }

  /** Parse a string containing an ''AutoMod cell'' scene definition.
   *
   *  @param s String to be parsed as a ''cell format'' scene.
   *
   *  @return ''JavaFX'' [[javafx.scene.Node]] wrapped in a [[scala.util.Success]] containing the equivalent ''JavaFX''
   *  scene, if `is` was parsed successfully, or a failure exception wrapped in a [[scala.util.Failure]] otherwise. In
   *  the latter case, an exception will describe the cause of the failure and indicate where in the stream the parser
   *  failed.
   *
   *  @since 0.0
   */
  def apply(s: String): Try[Node] = new CellParser(s).cellScene.run()

  /** X-, Y- then Z-axis rotation order. */
  private val XYZ = 0

  /** X-, Z- then Y-axis rotation order. */
  private val XZY = 1

  /** Y-, X- then Z-axis rotation order. */
  private val YXZ = 2

  /** Y-, Z- then X-axis rotation order. */
  private val YZX = 3

  /** Z-, X- then Y-axis rotation order. */
  private val ZXY = 4

  /** Z-, Y- then X-axis rotation order. */
  private val ZYX = 5

  /** Enumeration for AutoMod cell color sets. */
  private[cell] object CellColorSet
  extends Enumeration {

    /** Original AutoMod cell colors. */
    val Old = 0

    /** Newer AutoMod cell colors. */
    val New = 1
  }
}