//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2019, Michael J Allen.
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

//======================================================================================================================
// Scala source file belonging to the org.facsim.util.types package.
//======================================================================================================================
package org.facsim.util.types

import scala.language.implicitConversions

/** Safe replacement for the ''Scala'' `[[scala.Char Char]]` primitive type and for the underlying ''Java'' `char`
 *  primitive type.
 *
 *  The ''Java'' `char` type consumes two bytes of storage and was originally intended to represent the full range of
 *  ''[[https://unicode.org Unicode]]'' characters. Unfortunately, after the type was defined, the ''Unicode'' standard
 *  was extended to include such a large range of characters that two bytes (16 bits) became insufficient to store every
 *  possible character; now, many ''Unicode'' characters have codepoints that exceed 16 bits.
 *
 *  As a direct consequence, ''Java''&mdash;and hence ''Scala'' and all other ''JVM'' languages&mdash;now have a very
 *  ugly issue to deal with: there is no longer a direct mapping between a single `char` value and a single ''Unicode''
 *  character. Some ''Unicode'' characters are represented by a sequence of two `char`s&mhash;the first termed a ''high
 *  surrogate character'' and the second, a ''low surrogate character''. Processing `[[java.lang.String String]]` values
 *  as though they are sequences of `char` values is now complex and, consequently, buggy.
 *
 *  The solution that the ''Facsimile'' library has adopted is to replace the use of `Char` values with a new value
 *  type&mdash;`UChar`&mdash; that utilizes 4 bytes (32 bits) to store individual character values. As a result,
 *  processing of string and character values is now much simpler. (Admittedly, processing character strings in this
 *  manner does require additional memory; however, this overhead is slight in practice, memory is cheap, address space
 *  is large and the elimination of a whole class of bugs is viewed as a price worth paying.)
 *
 *  In ''Facsimile'', use of `Char` is deprecated.
 *
 *  @constructor Create a new ''Unicode'' character instance.
 *
 *  @param codepoint ''Unicode'' codepoint of this character. This should be a valid ''Unicode'' codepoint (as defined
 *  by `[[org.facsim.util.types.CharU.isValid isValid]]`), but cannot be validated except manually.
 *
 *  @since 0.0
 */
final class CharU private[types](private val codepoint: Int)
extends AnyVal {

  /** Convert this codepoint to the equivalent string.
   *
   *  @return String containing the single ''[[https://unicode.org Unicode]]'' character represented by this character.
   *  Note, if this is a ''supplemental'' character, then the resulting string will contain two `[[scala.Char Char]]`s
   *  (the ''high-'' and ''low-surrogate'' character pair representing the supplemental character).
   *
   *  @since 0.0
   */
  override def toString: String = Character.toChars(codepoint).mkString

  /** Determine whether this character has a valid codepoint.
   *
   *  @note In this context, ''valid'' means that the character is a defined ''[[https://unicode.org Unicode]]''
   *  codepoint value that is recognized and supported by ''Java'', and that the defined character is not a
   *  ''surrogate''.
   *
   *  @return `true` if the associated `codepoint` is valid; `false` otherwise.
   *
   *  @since 0.0
   */
  def isValid: Boolean = CharU.isValid(codepoint)
}

/** ''Unicode character'' companion object.
 *
 *  @since 0.0
 */
object CharU {

  /** Implicitly convert a `[[org.facsim.util.types.CharU CharU]]` character to a `[[java.lang.String String]]`.
   *
   *  @note Implicit conversion to a `[[scala.Char Char]]` is not possible because some `CharU` instances cannot be
   *  represented by a single `Char` character; this is the next best alternative.
   *
   *  @param c Character to be converted.
   *
   *  @return String corresponding to the supplied character.
   */
  implicit def toString(c: CharU): String = c.toString

  /** Implicitly convert an `[[scala.Int Int]]` value to a `[[org.facsim.util.types.CharU CharU]]` instance.
   *
   *  @param codepoint Codepoint value to be converted.
   *
   *  @return Corresponding ''[[https://unicode.org Unicode]]'' character, provided that `codepoint` is valid.
   *
   *  @throws scala.IllegalArgumentException if `codepoint` is undefined, or if it is a ''surrogate'' character.
   *
   *  @since 0.0
   */
  implicit def toCharU(codepoint: Int): CharU = {
    require(isValid(codepoint))
    new CharU(codepoint)
  }

  /** Implicitly convert a `[[scala.Char Char]]` value to a `[[org.facsim.util.types.CharU CharU]]` instance.
   *
   *  @param char Character value to be converted.
   *
   *  @return Corresponding ''[[https://unicode.org Unicode]]'' character, provided that `char` is valid.
   *
   *  @throws scala.IllegalArgumentException if `char` is a ''surrogate'' character.
   *
   *  @since 0.0
   */
  implicit def toCharU(char: Char): CharU = {
    require(isValid(char.toInt))
    new CharU(char.toInt)
  }

  /** Determine if `codepoint` is a ''[[https://unicode.org Unicode]] high-surrogate'' character.
   *
   *  High-surrogate characters are used in the `[[scala.Char Char]]` data type to represent the first half of a
   *  ''supplemental'' character (one that cannot be represented by a single `Char` value).
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character's codepoint represents a high surrogate ''Unicode'' character.
   *
   *  @since 0.0
   */
  def isHighSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_HIGH_SURROGATE && codepoint <= Character.MAX_HIGH_SURROGATE
  }

  /** Determine if `codepoint` is a ''[[https://unicode.org Unicode]] low-surrogate'' character.
   *
   *  Low-surrogate characters are used in the `[[scala.Char Char]]` data type to represent the second half of a
   *  ''supplemental'' character (one that cannot be represented by a single `Char` value).
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character's codepoint represents a low surrogate ''Unicode'' character.
   *
   *  @since 0.0
   */
  def isLowSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_LOW_SURROGATE && codepoint <= Character.MAX_LOW_SURROGATE
  }

  /** Determine if `codepoint` is a ''[[https://unicode.org Unicode]] surrogate'' character.
   *
   *  Surrogate characters are not characters in their own right, but are used as part of a ''high-'' and
   *  ''low-surrogate'' pair in the definition of ''Unicode supplemental'' characters (those that cannot be represented
   *  by a single `[[scala.Char Char]]` instance).
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character's codepoint represents a high or surrogate ''Unicode'' character.
   *
   *  @since 0.0
   */
  def isSurrogate(codepoint: Int): Boolean = isLowSurrogate(codepoint) || isHighSurrogate(codepoint)

  /** Determine if `codepoint` is a valid ''[[https://unicode.org Unicode]]'' character.
   *
   *  @note ''Unicode surrogate'' characters are treated as invalid, as are codepoints that do not map to ''Unicode''
   *  characters supported by the ''JVM''.
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character is a defined, non-surrogate ''Unicode'' character; `false` otherwise.
   *
   *  @since 0.0
   */
  def isValid(codepoint: Int): Boolean = Character.isDefined(codepoint) && !isSurrogate(codepoint)

  /** Backslash character.
   *
   *  @since 0.0
   */
  val BS: CharU = '\\'

  /** Colon character.
   *
   *  @since 0.0
   */
  val Colon: CharU = ':'

  /** Comma character.
   *
   *  @since 0.0
   */
  val Comma: CharU = ','

  /** Carriage return character.
   *
   *  @since 0.0
   */
  val CR: CharU = '\r'

  /** Double-quote character.
   *
   *  @since 0.0
   */
  val DQ: CharU = '\"'

  /** Formfeed character.
   *
   *  @since 0.0
   */
  val FF: CharU = '\f'

  /** Horizontal tab character.
   *
   *  @since 0.0
   */
  val HT: CharU = '\t'

  /** Linefeed character.
   *
   *  @since 0.0
   */
  val LF: CharU = '\n'

  /** Minus sign.
   *
   *  @since 0.0
   */
  val Minus: CharU = '-'

  /** NUL character.
   *
   *  @since 0.0
   */
  val NUL: CharU = '\u0000'

  /** Period character.
   *
   *  @since 0.0
   */
  val Period: CharU = '.'

  /** Plus sign.
   *
   *  @since 0.0
   */
  val Plus: CharU = '+'

  /** Semicolon character.
   *
   *  @since 0.0
   */
  val Semicolon: CharU = ';'

  /** Space character.
   *
   *  @since 0.0
   */
  val SPC: CharU = ' '

  /** Single-quote character.
   *
   *  @since 0.0
   */
  val SQ: CharU = '\''

  /** Vertical tab character.
   *
   *  @since 0.0
   */
  val VT: CharU = '\u000B'
}