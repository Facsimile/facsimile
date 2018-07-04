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

import javafx.scene.paint.{Color => JFXColor, Material => JFXMaterial, PhongMaterial}

/** Enumeration for ''cell'' color codes.
 *
 *  In ''AutoMod cell'' scenes, colors are identified by integer values that correspond to a specific color.
 *
 *  @note ''JavaFX 3D'' mingles color with opacity (color ''alpha'' channels)&mdash;and, as diffuse colors with alpha
 *  channels&mdash;in materials. However, ''JavaFX nodes'' also have opacity settings independent of color or material
 *  (which do not appear to work in 3D as of ''JavaFX 8''). For simplicity, when dealing with ''cell'' scenes, we use
 *  opaque alpha channels in all colors, and must rely upon opacity being fixed in a future ''JavaFX'' release.
 */
private[cell] object CellColor
extends Enumeration {

  // DO NOT CHANGE THE ORDER OF THESE DEFINITIONS!

  /** Black, having the ''cell'' color code 0. */
  val Black = Value

  /** Red, having the ''cell'' color code 1. */
  val Red = Value

  /** Green, having the ''cell'' color code 2. */
  val Green = Value

  /** Yellow, having the ''cell'' color code 3. */
  val Yellow = Value

  /** Blue, having the ''cell'' color code 4. */
  val Blue = Value

  /** Magenta, having the ''cell'' color code 5. */
  val Magenta = Value

  /** Cyan, having the ''cell'' color code 6. */
  val Cyan = Value

  /** White, having the ''cell'' color code 7. */
  val White = Value

  /** Light gray, having the ''cell'' color code 8. */
  val LightGray = Value

  /** Dark gray, having the ''cell'' color code 9. */
  val DarkGray = Value

  /** Brown, having the ''cell'' color code 10. */
  val Brown = Value

  /** Light blue, having the ''cell'' color code 11. */
  val LightBlue = Value

  /** Purple, having the ''cell'' color code 12. */
  val Purple = Value

  /** Orange, having the ''cell'' color code 13. */
  val Orange = Value

  /** Light green, having the ''cell'' color code 14. */
  val LightGreen = Value

  /** Light yellow, having the ''cell'' color code 15. */
  val LightYellow = Value

  /** Default color, which is used if an explicit color is not specified. */
  val Default = Red

  /** Minimum color code value. */
  private val minValue = 0

  /** Maximum color code value. */
  private val maxValue = maxId - 1

  /** Color values corresponding to each color code, for each supported color scheme.
   *
   *  The supported color schemes are:
   *   - Old ''AutoMod'' color scheme, in effect for versions prior to ''AutoMod'' release 8.0.
   *   - New ''AutoMod'' color scheme, in effect for versions starting with ''AutoMod'' release 8.0.
   *   - JavaFX color scheme, which employs the color definitions with equivalent names to the ''AutoMod'' colors. This
   *     color scheme is used by default in ''Facsimile'' to ensure that models look more modern.
   *
   *  @note colors defined in ''VRML'' scenes embedded within ''cell'' scenes are unaffected by the choice of ''cell''
   *  color scheme.
   */
  private val Color = Vector(

    // Old AutoMod colors, denoted by CellColorScheme.Old
    //
    // These definitions are obtained from the values written into .asilibrc, as "Old Colors", by the ASI Configuration
    // Editor utility supplied with AutoMod. These were the default colors used in AutoMod prior to release 8.0.
    Vector(
      JFXColor.BLACK, // Black (equivalent to rgb(0, 0, 0))
      JFXColor.RED, // Red (equivalent to rgb(255, 0, 0))
      JFXColor.rgb(0, 255, 0), // Green
      JFXColor.YELLOW, // Yellow (equivalent to rgb(255, 255, 0))
      JFXColor.BLUE, // Blue (equivalent to rgb(0, 0, 255))
      JFXColor.MAGENTA, // Magenta (equivalent to rgb(255, 0, 255))
      JFXColor.CYAN, // Cyan (equivalent to rgb(0, 255, 255))
      JFXColor.WHITE, // White (equivalent to rgb(255, 255, 255))
      JFXColor.rgb(163, 163, 163), // Light gray
      JFXColor.rgb(91, 91, 91), // Dark gray
      JFXColor.rgb(127, 72, 0), // Brown
      JFXColor.rgb(127, 0, 255), // Purple
      JFXColor.rgb(255, 36, 0), // Orange
      JFXColor.rgb(63, 145, 255), // Light blue
      JFXColor.rgb(127, 255, 63), // Light green
      JFXColor.rgb(255, 255, 63), // Light yellow
    ),

    // New colors, denoted by CellColorScheme.New
    //
    // These are obtained from the values defined in header file: %ASI%\include\colorwin.h (the ASI Configuration Editor
    // does not actually write definitions into .asilibrc when "New Colors" are requested). These are the default colors
    // used in AutoMod since release 8.0.
    //
    // Also, as of AutoMod 8.0, it became possible to customize the set of defined colors by specifying RGB values in a
    // .asilibrc configuration file. At present, these custom color files are not supported.
    Vector(
      JFXColor.BLACK, // Black (equivalent to rgb(0, 0, 0))
      JFXColor.rgb(224, 0, 0), // Red
      JFXColor.rgb(0, 224, 0), // Green
      JFXColor.rgb(232, 232, 0), // Yellow
      JFXColor.rgb(0, 0, 224), // Blue
      JFXColor.rgb(224, 0, 224), // Magenta
      JFXColor.rgb(0, 224, 224), // Cyan
      JFXColor.WHITE, // White (equivalent to rgb(255, 255, 255))
      JFXColor.rgb(176, 176, 176), // Light gray
      JFXColor.rgb(96, 96, 96), // Dark gray
      JFXColor.rgb(96, 48, 0), // Brown
      JFXColor.rgb(128, 0, 160), // Purple
      JFXColor.rgb(232, 128, 0), // Orange
      JFXColor.rgb(160, 160, 255), // Light blue
      JFXColor.rgb(160, 255, 160), // Light green
      JFXColor.rgb(255, 255, 160), // Light yellow
    ),

    // JavaFX colors, denoted by CellColorScheme.JFX
    //
    // These are the corresponding colors as defined by JavaFX, which are used in lieu of any further information.
    Vector(
      JFXColor.BLACK, // Black
      JFXColor.RED, // Red
      JFXColor.GREEN, // Green
      JFXColor.YELLOW, // Yellow
      JFXColor.BLUE, // Blue
      JFXColor.MAGENTA, // Magenta
      JFXColor.CYAN, // Cyan
      JFXColor.WHITE, // White
      JFXColor.LIGHTGRAY, // Light gray
      JFXColor.DARKGRAY, // Dark gray
      JFXColor.BROWN, // Brown
      JFXColor.PURPLE, // Purple
      JFXColor.ORANGE, // Orange
      JFXColor.LIGHTBLUE, // Light blue
      JFXColor.LIGHTGREEN, // Light green
      JFXColor.LIGHTYELLOW, // Light yellow
    ),
  )
  //scalastyle:on magic.number

  // Sanity checks.
  assert(Color.length == 3) //scalastyle:ignore magic.number
  assert(Color.forall(_.length == maxId))

  /** Materials corresponding to each color definition.
   *
   *  @note Default settings will be used for all material parameters except for the diffuse color.
   */
  private lazy val Material = Color.foldLeft(Map.empty[JFXColor, JFXMaterial]) {(map, set) =>
    set.foldLeft(map) {(map, color) =>
      if(map.contains(color)) map
      else map + (color -> new PhongMaterial(color))
    }
  }

  /** Retrieve ''JavaFX'' material for specified color code from specified scheme.
   *
   *  @param color Code of color for which a material is to be retrieved.
   *
   *  @param scheme ''Cell'' color scheme to be utilized.
   *
   *  @return Corresponding ''JavaFX'' material.
   *
   *  @throws scala.IndexOutOfBoundsException if `color` is not a valid color code.
   */
  def apply(color: Int, scheme: CellColorScheme) = Material(Color(scheme.id)(color))

  /** Verify a color code.
   *
   *  @param color Color code to be verified.
   *
   *  @return `true` if the code maps to a valid color, `false` otherwise.
   */
  def verify(color: Int) = color >= minValue && color <= maxValue
}
