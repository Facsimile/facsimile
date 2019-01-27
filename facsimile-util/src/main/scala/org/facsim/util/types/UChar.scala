//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
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

// Scala source file belonging to the org.facsim.util.types package.
//======================================================================================================================
package org.facsim.util.types

import org.facsim.util.requireValidFn
import scala.language.implicitConversions

/** Safe replacement for the _Scala_ [[scala.Char]] primitive type and for the underlying _Java_ `char` primitive type.
 *
 *  The _Java_ `char` type consumes two bytes of storage and was originally intended to represent the full range of
 *  ''[[https://unicode.org]] Unicode'' characters. Unfortunately, after the type was defined, the ''Unicode'' standard
 *  was extended to include such a large range of characters that two bytes (16 bits) became insufficient to store every
 *  possible character; now, many ''Unicode'' characters have codepoints that exceed 16 bits.
 *
 *  As a direct consequence, _Java_&mdash;and hence _Scala_ and all other ''JVM'' languages&mdash;now have a very ugly
 *  issue to deal with: there is no longer a direct mapping between a single `char` value and a single ''Unicode''
 *  character. Some ''Unicode'' characters are represented by a sequence of two `char`s&mhash;the first termed a ''high
 *  surrogate character'' and the second, a ''low surrogate character''. Processing [[scala.String]] values are though
 *  they are sequences of `char` values is now complex and, consequently, buggy.
 *
 *  The solution that the ''Facsimile'' library has adopted is to replace the use of [[scala.Char]] values with a new
 *  value type&mash;UChar`&mdash; that utilizes 4 bytes (32 bits) to store individual character values. As a result,
 *  processing of string and character values is now much simpler. (Admittedly, this does require additional memory to
 *  process character strings. However, this overhead is slight in practice, memory is cheap, address space is large and
 *  the elimination of a whole class of bugs is viewed as a price worth paying.
 *
 *  In ''Facsimile'', use of [[scala.Char]] is deprecated.
 *
 *  @param codepoint ''Unicode'' codepoint of this character. This must be a valid ''Unicode'' codepoint (as defined by
 *  [[org.facsim.util.types.UChar.isValid()]]) or an exception will be thrown.
 *
 *  @throws scala.IllegalArgumentException if `codepoint` is not valid.
 *
 *  @since 0.0
 */
final case class UChar(codepoint: Int)
extends AnyVal {

  // Sanity check. What is impact of this on value type? Called on each function call?
  requireValidFn(codepoint, UChar.isValid, "codepoint")

  /** Convert this codepoint to the equivalent string.
   *
   *  @return String containing the single ''Unicode'' character represented by this character. Note, if this is a
   *  ''supplemental'' character, then the resulting string will contain two [[scala.Char]]s (the ''high-'' and
   *  ''low-surrogate'' character pair representing the supplemental character).
   */
  override def toString: String = Character.toChars(codepoint).mkString
}

/** ''Unicode character'' companion object.
 *
 *  @since 0.0
 */
object UChar {

  /** Convert a string to an array of integer codepoint values.
   *
   *  @param s String to be converted.
   *
   *  @return Array of individual ''Unicode'' characters contained in `s`; if `s` is empty, an empty array will be
   *  returned.
   */
  def toUCharArray(s: String): Array[UChar] = s.codePoints.toArray.map(toUChar)

  /** Convert an array of integers to a string.
   *
   *  @param ia Array of integer values to be converted into a string.
   *
   *
   *  @return String containing the ''Unicode'' characters represented by each integer codepoint value.
   */
  def toString(ia: Array[UChar]): String = ia.flatMap(c => Character.toChars(c.codepoint)).mkString

  /** Implicitly convert an integer value to a UChar instance.
   *
   *  @param codepoint Integer value to be converted.
   *
   *  @return Corresponding ''Unicode'' character, provided that `codepoint` is valid.
   *
   *  @throws scala.IllegalArgumentException if `codepoint` is undefined, or if it is a ''surrogate'' character.
   */
  implicit def toUChar(codepoint: Int): UChar = new UChar(codepoint)

  /** Determine if `codepoint` is a ''Unicode high-surrogate'' character.
   *
   *  High-surrogate characters are used in the [[scala.Char]] data type to represent the first half of a
   *  ''supplemental'' character (one that cannot be represented by a single [[scala.Char]] value).
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character's codepoint represents a high surrogate ''Unicode'' character.
   */
  def isHighSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_HIGH_SURROGATE && codepoint <= Character.MAX_HIGH_SURROGATE
  }

  /** Determine if `codepoint` is a ''Unicode low-surrogate'' character.
   *
   *  Low-surrogate characters are used in the [[scala.Char]] data type to represent the second half of a
   *  ''supplemental'' character (one that cannot be represented by a single [[scala.Char]] value).
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character's codepoint represents a low surrogate ''Unicode'' character.
   */
  def isLowSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_LOW_SURROGATE && codepoint <= Character.MAX_LOW_SURROGATE
  }

  /** Determine if `codepoint` is a ''Unicode'' surrogate character.
   *
   *  Surrogate characters are not characters in their own right, but are used as a ''high-'' and ''low-surrogate'' pair
   *  in the definition of ''Unicode supplemental'' characters (those that cannot be represented by a single
   *  [[scala.Char]] instance).
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character's codepoint represents a high or surrogate ''Unicode'' character.
   */
  def isSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_HIGH_SURROGATE && codepoint <= Character.MAX_LOW_SURROGATE
  }

  /** Determine if `codepoint` is a valid ''Unicode'' character.
   *
   *  @note ''Unicode surrogate'' characters are treated as invalid, as are codepoints that do not map to ''Unicode''
   *  characters supported by the ''JVM''.
   *
   *  @param codepoint ''Unicode'' codepoint value being tested.
   *
   *  @return `true` if this character is a defined, non-surrogate ''Unicode'' character; `false` otherwise.
   */
  def isValid(codepoint: Int): Boolean = Character.isDefined(codepoint) && !isSurrogate(codepoint)
}