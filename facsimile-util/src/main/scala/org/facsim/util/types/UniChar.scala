//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
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

import org.facsim.util.requireValidFn
import scala.collection.immutable.ArraySeq
import scala.jdk.StreamConverters.IntStreamHasToScala
import scala.language.implicitConversions

/** Encapsulates a single, valid, non-_surrogate [[https://unicode.org/ Unicode]]_ character.
 *
 *  _Java_ (and, hence, _Scala_, _Kotlin_ and other _JVM_ languages) suffer from a broken `char` implementation
 *  (and hence also the `[[Character]]` and `[[Char]]` type implementations).
 *
 *  In the early days of _Java_, a decision was made to support the then new _Unicode_ standard. At that time, a
 *  single _Unicode_ character required only 16 bits of storage capacity (two 8-bit bytes) to encode all of its
 *  characters. Consequently, _Java_ defined a `char` to be 2 bytes in size, and each _Unicode_ character mapped to
 *  a single `char` value. Simple!
 *
 *  Alas, since then, as more and more characters have been added to the _Unicode_ standard, some characters now
 *  require more than 16 bits in order to be encoded (the reference number for a character is termed a _codepoint_).
 *  At the time of writing, the [[http://www.unicode.org/versions/Unicode13.0.0/ current _Unicode_ standard (13.0.0)]]
 *  defines characters with codepoints in the range [0x000000, 0x10FFFF], requiring 21 bits of storage for any single
 *  character. Rather than break compatibility with earlier versions of _Java_, it was decided to break the
 *  relationship that mapped a single _Unicode_ character to a single _Java_ `char` value. Now, some _Unicode_
 *  characters must be represented by a _surrogate pair_ of _Java_ `char` values (this is also referred to as
 *  _UTF-16 encoding_). Each pair consists of a _high-surrogate_ (or _leading surrogate_) character and a
 *  _low-surrogate_ (or _trailing surrogate_) characters; each surrogate character by itself is meaningless and
 *  the presence of surrogates that do not form a pair indicates an encoding error.
 *
 *  The presence of surrogate characters leads to a number of gnarly programming problems when processing `[[String]]`s
 *  as sequences of `char` values. In essence, the `char` type is broken and is no longer fit for purpose. To work
 *  around this, _Facsimile_ has created a new character data type, called `UniChar`, to represent individual
 *  _Unicode_ characters, replacing the `char` type (and the _Java_ `Character` and _Scala_ `Char` types).
 *  `UniChar` instances are based upon the _codepoint_ value of each _Unicode_ character, in the form of a 4-byte
 *  (32-bit) integer value.
 *
 *  As a consequence, every `UniChar` instance is a valid, non-surrogate _Unicode_ character, simplifying string
 *  parsing operations.
 *
 *  @note This class should be used to assist with per-character parsing operations. It is not intended for use as a
 *  means of storing character data, or for replacement of _Java_ `[[String]]`s.
 *
 *  @constructor Create a new, single _Unicode_ character from a _Unicode_ `codepoint`. If `codepoint` does not
 *  represent a defined _Unicode_ character, or if it is defined but represents a _surrogate_ character, then an
 *  exception will be thrown.
 *
 *  @param codepoint _Unicode_ codepoint value of the corresponding character. Invalid codepoints, and codepoints for
 *  surrogate characters, will be rejected by the associated factory methods.
 *
 *  @throws IllegalArgumentException if `codepoint` is not a defined, non-surrogate _Unicode_ codepoint.
 *
 *  @see _[[https://unicode.org/ Unicode standard.]]_
 *
 *  @since 0.2
 */
// Developer note: To improve the efficiency of this type, it was originally made a value class (by extending AnyVal).
// However, it was decided to move away from that, for the following reasons:
// 1. Value classes cannot have any constructor code, and so cannot validate their arguments. One of the key design
//    goals for the UniChar type was to be able to reject invalid codepoints, and so this would not have been possible.
// 2. There is the possibility of type confusion for implicit conversion methods, if the underlying base class is Int.
//
// It might be possible to review this decision in future releases should performance be identified as a significant
// issue. Alternatively, class methods could be inlined and benchmarked to also address performance.
final case class UniChar(codepoint: Int) {

  // Verify that the resulting codepoint is valid.
  requireValidFn(codepoint, UniChar.isValid, "codepoint")

  /** Returns the numeric value of this character in the specified radix.
   *
   *  @param radix Radix for which this digit is to be processed.
   *
   *  @return Numeric value of the digit, wrapped in `[[Some]]`, or `[[None]]` if either the digit is a not a valid
   *  digit in the specified `radix`, or if `radix` is outside of the range [`Character.MIN_RADIX`,
   *  `Character.MAX_RADIX`].
   *
   *  @see [[UniChar.forDigit()]] for identifying the character representing a digit value.
   *
   *  @since 0.0
   */
  def digit(radix: Int): Option[Int] = Character.digit(codepoint, radix) match {
    case -1 => None
    case x: Int => Some(x)
  }

  /** Determine if this represents an alphabetic _Unicode_ character.
   *
   *  A _Unicode_ character is considered alphabetic if it belongs to an alphabet; this can be _any_ alphabet, not
   *  just the _Latin_ alphabet.
   *
   *  @return `true` if `codepoint` identifies an alphabetic _Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isAlphabetic: Boolean = Character.isAlphabetic(codepoint)

  /** Determine if this _Unicode_ character is in the _basic multilingual plane_ (_BMP_).
   *
   *  A _Unicode_ character is in the _BMP_ if it can be represented by a single `[[Char]]` character. Note that
   *  surrogate characters are not considered to be in the _BMP_.
   *
   *  @return `true` if `codepoint` identifies a _BMP Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isBMPCodePoint: Boolean = Character.isBmpCodePoint(codepoint)

  /** Determine if this is a defined _Unicode_ character.
   *
   *  Codepoints that do not have defined meanings in the _Unicode_ standard are deemed undefined. Note that
   *  surrogate characters are considered to be defined characters.
   *
   *  @note Since all `UniChar` instances *must* have defined codepoints, this function will always return `true`.
   *
   *  @return `true` if `codepoint` identifies a defined _Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isDefined: Boolean = {
    UniChar.isDefined(codepoint)
  } ensuring(r => r)

  /** Determine if this represents a decimal digit _Unicode_ character.
   *
   *  Decimal digits include _Latin_, _Arab-Indic_, _Devanagari_, and _fullwidth_ digits.
   *
   *  @return `true` if `codepoint` identifies a decimal digit character, or false otherwise.
   *
   *  @since 0.2
   */
  def isDigit: Boolean = Character.isDigit(codepoint)

  /** Determine if this is a _high-surrogate Unicode_ character.
   *
   *  A high-surrogate character, also called a _leading surrogate_, is used in a pair with a following low-surrogate
   *  character to encode _supplemental_ characters that cannot be encoded in a single _Java_ `[[Character]]`
   *  instance. High-surrogate characters are invalid _Unicode_ characters, because they represent nothing by
   *  themselves.
   *
   *  @note Since all `UniChar` instances *cannot* be surrogate codepoints, this function must always return
   *  `false`.
   *
   *  @return `true` if `codepoint` identifies a high-surrogate character, or `false` otherwise.
   *
   *  @since 0.2
   */
  def isHighSurrogate: Boolean = {
    UniChar.isHighSurrogate(codepoint)
  } ensuring(r => !r)

  /** Determine if this should be regarded as ignorable in a _Java_ or _Unicode_ identifier.
   *
   *  Ignorable identifier characters are either non-whitespace ISO control characters, or those characters classified
   *  as formatting characters.
   *
   *  @return `true` if `codepoint` identifies a defined _Unicode_ character, or false otherwise.
   *
   *  @see [[isJavaIdentifierPart]] for the set of _Unicode_ characters than can be used as part of a _Java_
   *  identifier, other than for the first character.
   *
   *  @see [[isJavaIdentifierStart]] for the set of _Unicode_ characters that can be used as the start of a _Java_
   *  identifier.
   *
   *  @see [[isUnicodeIdentifierPart]] for the set of _Unicode_ characters than can be used as part of a _Unicode_
   *  identifier, other than for the first character.
   *
   *  @see [[isUnicodeIdentifierStart]] for the set of _Unicode_ characters that can be used as the start of a
   *  _Unicode_ identifier.
   *
   *  @since 0.2
   */
  def isIdentifierIgnorable: Boolean = Character.isIdentifierIgnorable(codepoint)

  /** Determine if this is a _Chinese, Japanese, Korean, or Vietnamese_ ideograph, as defined by the _Unicode_
   *  standard.
   *
   *  @return `true` if `codepoint` identifies an ideographic _Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isIdeographic: Boolean = Character.isIdeographic(codepoint)

  /** Determine if this is an _International Standards Organization_ (_ISO_) _control_ character.
   *
   *  ISO control characters come in two ranges:
   *  - The _C0_ range has codepoints in the range 'U+000000' through 'U+00001F'.
   *  - The _C1_ range has codepoints in the range 'U+00007F' through 'U+00009F'.
   *
   *  @return `true` if `codepoint` identifies an ISO control _Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isISOControl: Boolean = Character.isISOControl(codepoint)

  /** Determine if this may be used as in a _Java_ identifier, other than as the first character.
   *
   *  Identifier part characters include letters, digits, currency symbols, connecting punctuation characters, digits,
   *  numeric letters, combining marks, non-spacing marks and the set of identifier ignorable characters (see
   *  [[isIdentifierIgnorable]]).
   *
   *  @return `true` if `codepoint` identifies a _Unicode_ character that can be used in a _Java_ identifier (other
   *  than as the first character), or false otherwise.
   *
   *  @see [[isIdentifierIgnorable]] for the set of ignorable identifier _Unicode_ characters.
   *
   *  @see [[isJavaIdentifierStart]] for the set of _Unicode_ characters that can be used as the start of a _Java_
   *  identifier.
   *
   *  @since 0.2
   */
  def isJavaIdentifierPart: Boolean = Character.isJavaIdentifierPart(codepoint)

  /** Determine if this may be used as the first character in a _Java_ identifier.
   *
   *  Identifier start characters include letters, letter numbers, currency symbols, and connecting punctuation
   *  characters only.
   *
   *  @return `true` if `codepoint` identifies a _Unicode_ character that can be used at the start of a _Java_
   *  identifier, or false otherwise.
   *
   *  @see [[isIdentifierIgnorable]] for the set of ignorable identifier _Unicode_ characters.
   *
   *  @see [[isJavaIdentifierPart]] for the set of _Unicode_ characters than can be used as part of a _Java_
   *  identifier, other than for the first character.
   *
   *  @since 0.2
   */
  def isJavaIdentifierStart: Boolean = Character.isJavaIdentifierStart(codepoint)

  /** Determine if this represents a _Unicode_ letter character.
   *
   *  @return `true` if `codepoint` identifies a _Unicode_ letter character, or false otherwise.
   *
   *  @since 0.2
   */
  def isLetter: Boolean = Character.isLetter(codepoint)

  /** Determine if this represents a _Unicode_ letter or decimal digit character.
   *
   *  @return `true` if `codepoint` identifies a _Unicode_ letter or decimal digit character, or false otherwise.
   *
   *  @since 0.2
   */
  def isLetterOrDigit: Boolean = Character.isLetterOrDigit(codepoint)

  /** Determine if this represents a lower case _Unicode_ letter character.
   *
   *  @return `true` if `codepoint` identifies a lower case _Unicode_ letter character, or false otherwise.
   *
   *  @since 0.2
   */
  def isLowerCase: Boolean = Character.isLowerCase(codepoint)

  /** Determine if this defines a _low-surrogate_ character.
   *
   *  A low-surrogate character, also called a _trailing surrogate_, is used in a pair with a preceding
   *  high-surrogate character to encode _supplemental_ characters that cannot be encoded in a single _Java_
   *  `[[Character]]` instance. Low-surrogate characters are invalid _Unicode_ characters, because they represent
   *  nothing by themselves.
   *
   *  @return `true` if `codepoint` identifies an identifier ignorable character, or `false` otherwise.
   *
   *  @since 0.2
   */
  def isLowSurrogate: Boolean = {
    UniChar.isLowSurrogate(codepoint)
  } ensuring(r => !r)

  /** Determine if this is a _mirrored Unicode_ character.
   *
   *  Mirrored characters, according to the _Unicode_ specification, have their glyphs horizontally mirrored when
   *  displayed in text that is right-to-left. For example, 'U+000028' is semantically defined to be an opening
   *  parenthesis. This will appear as '(' in text that is left-to-right but as ')' in text that is right-to-left.
   *
   *  @return `true` if `codepoint` identifies a mirrored _Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isMirrored: Boolean = Character.isMirrored(codepoint)

  /** Determine if this is a _Unicode space_ character.
   *
   *  Space characters are categorized as such according to the _Unicode_ specification; this includes various forms
   *  of space separators, line separators and paragraph separators.
   *
   *  @return `true` if `codepoint` identifies a _Unicode space_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isSpaceChar: Boolean = Character.isSpaceChar(codepoint)

  /** Determine if this is a _supplemental Unicode_ character.
   *
   *  Supplementary characters cannot be represented by a single _Java_ `char` (`[[Character]]`, or `[[Char]]`) value,
   *  and, instead, need must be encoded as a _surrogate pair_, consisting of a high- (or leading-) surrogate
   *  character, followed by a low- (or trailing-) surrogate character.
   *
   *  @note All supplemental characters can be represented by a single `UniChar` value.
   *
   *  @return `true` if `codepoint` identifies a _Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isSupplementary: Boolean = Character.isSupplementaryCodePoint(codepoint)

  /** Determine if this is a _surrogate Unicode_ character.
   *
   *  A surrogate character, which may either be a high- or a low-surrogate, is used with another surrogate to encode
   *  _supplemental_ characters that cannot be encoded in a single _Java_ `[[Character]]` instance. Surrogate
   *  characters are invalid _Unicode_ characters, because they represent nothing by themselves.
   *
   *  @note Since all `UniChar` instances *cannot* be surrogate codepoints, this function must always return
   *  `false`.
   *
   *  @return `true` if `codepoint` identifies a low- or a high-surrogate character, or `false` otherwise.
   *
   *  @since 0.2
   */
  def isSurrogate: Boolean = {
    UniChar.isSurrogate(codepoint)
  } ensuring(r => !r)

  /** Determine if this represents a title case _Unicode_ letter character.
   *
   *  @note The distinction between an upper case letter and a title case letter can be found in characters that
   *  resemble pairs of latin characters, such as 'ǉ' (in lower case). The upper case form has both letters in their
   *  upper case representation ('Ǉ', in this instance), while the title case has only the first character in upper case
   *  and the second in lower case ('ǈ'). In all other situations, `isUpperCase` and `isTitleCase` will return the same
   *  result for the same character.
   *
   *  @return `true` if `codepoint` identifies a title case _Unicode_ letter character, or false otherwise.
   *
   *  @since 0.2
   */
  def isTitleCase: Boolean = Character.isTitleCase(codepoint)

  /** Determine if this may be used as in a _Unicode_ identifier, other than as the first character.
   *
   *  Identifier part characters include letters, digits, connecting punctuation characters, numeric letters,
   *  combining marks, non-spacing marks and the set of identifier ignorable characters (see
   *  [[isIdentifierIgnorable]]).
   *
   *  @return `true` if `codepoint` identifies a _Unicode_ character that can be used in a _Unicode_ identifier
   *  (other than as the first character), or false otherwise.
   *
   *  @see [[isIdentifierIgnorable]] for the set of ignorable identifier _Unicode_ characters.
   *
   *  @see [[isUnicodeIdentifierStart]] for the set of _Unicode_ characters that can be used as the start of a
   *  _Unicode_ identifier.
   *
   *  @since 0.2
   */
  def isUnicodeIdentifierPart: Boolean = Character.isUnicodeIdentifierPart(codepoint)

  /** Determine if this is may be used as the first character in a _Unicode_ identifier.
   *
   *  Identifier start characters include letters, and letter numbers only.
   *
   *  @return `true` if `codepoint` identifies a _Unicode_ character that can be used at the start of a _Unicode_
   *  identifier, or false otherwise.
   *
   *  @see [[isIdentifierIgnorable]] for the set of ignorable identifier _Unicode_ characters.
   *
   *  @see [[isUnicodeIdentifierPart]] for the set of _Unicode_ characters than can be used as part of a _Unicode_
   *  identifier, other than for the first character.
   *
   *  @since 0.2
   */
  def isUnicodeIdentifierStart: Boolean = Character.isUnicodeIdentifierStart(codepoint)

  /** Determine if this represents an upper case _Unicode_ letter character.
   *
   *  @return `true` if `codepoint` identifies an upper case _Unicode_ letter character, or false otherwise.
   *
   *  @see [[isTitleCase]] for more information on the distinction between upper and title case characters.
   *
   *  @since 0.2
   */
  def isUpperCase: Boolean = Character.isUpperCase(codepoint)

  /** Determine if this is a valid (defined, non-surrogate) _Unicode_ character.
   *
   *  Codepoints that do not have defined meanings in the _Unicode_ standard, or that identify _surrogate_
   *  characters, are deemed invalid.
   *
   *  @note Since all `UniChar` instances *must* have valid (defined, non-surrogate) codepoints, this function will
   *  always return `true`.
   *
   *  @return `true` if `codepoint` identifies a defined _Unicode_ character that is not also a _surrogate_
   *  character, or false otherwise.
   *
   *  @since 0.2
   */
  def isValid: Boolean = {
    UniChar.isValid(codepoint)
  } ensuring(r => r)

  /** Determine if this is considered a whitespace character.
   *
   *  @return `true` if this character is regarded as a _whitespace_ character by _Java_.
   *
   *  @since 0.2
   */
  def isWhitespace: Boolean = Character.isWhitespace(codepoint)

  /** Convert to a lower case character.
   *
   *  If this character has a lower case equivalent, then this function returns that equivalent character; otherwise, it
   *  returns this character.
   *
   *  @return Lower case equivalent of this character, if available; otherwise, just return this character.
   *
   *  @since 0.2
   */
  def toLowerCase: UniChar = {

    // Obtain the lower case equivalent to this character. If the codepoint is different, return the new character;
    // otherwise, return this character.
    val ch = Character.toLowerCase(codepoint)
    if(ch != codepoint) UniChar(ch)
    else this
  }

  /** Convert the codepoint value to a _Unicode_ character, represented as a string.
   *
   *  @note The return value may be more than one `[[java.lang.Character]]` in length.
   *
   *  @return String containing this _Unicode_ character.
   *
   *  @since 0.2
   */
  override def toString: String = Character.toChars(codepoint).mkString

  /** Convert to a title case character.
   *
   *  If this character has a title case equivalent, then this function returns that equivalent character; otherwise, it
   *  returns this character.
   *
   *  @return Title case equivalent of this character, if available; otherwise, just return this character.
   *
   *  @since 0.2
   */
  def toTitleCase: UniChar = {

    // Obtain the title case equivalent to this character. If the codepoint is different, return the new character;
    // otherwise, return this character.
    val ch = Character.toTitleCase(codepoint)
    if(ch != codepoint) UniChar(ch)
    else this
  }

  /** Convert to an upper case character.
   *
   *  If this character has an upper case equivalent, then this function returns that equivalent character; otherwise,
   *  it returns this character.
   *
   *  @return Upper case equivalent of this character if available; otherwise, just return this character.
   *
   *  @since 0.2
   */
  def toUpperCase: UniChar = {

    // Obtain the upper case equivalent to this character. If the codepoint is different, return the new character;
    // otherwise, return this character.
    val ch = Character.toUpperCase(codepoint)
    if(ch != codepoint) UniChar(ch)
    else this
  }
}

/** _Unicode_ character companion object.
 *
 *  @since 0.2
 */
object UniChar {

  /** Backslash character.
   *
   *  @since 0.2
   */
  val BS: UniChar = '\\'

  /** Colon character.
   *
   *  @since 0.2
   */
  val Colon: UniChar = ':'

  /** Comma character.
   *
   *  @since 0.2
   */
  val Comma: UniChar = ','

  /** Carriage return character.
   *
   *  @note This is considered a whitespace character by Java. It is used, followed by a `[[LF]]` character, as the
   *  _line termination sequence_ for _Windows_, _DOS_, and _OS/2_ text files. In isolation, it is treated as
   *  a line terminator in _MacOS_ 9 and earlier text files.
   *
   *  @since 0.2
   */
  val CR: UniChar = '\r'
  assert(CR.isWhitespace)

  /** Double quote character.
   *
   *  @since 0.2
   */
  val DQ: UniChar = '"'

  /** Form-feed character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val FF: UniChar = '\f'
  assert(FF.isWhitespace)

  /** File separator character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val FS: UniChar = 0x1C
  assert(FS.isWhitespace)

  /** Group separator character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val GS: UniChar = 0x1D
  assert(GS.isWhitespace)

  /** Horizontal tab character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val HT: UniChar = '\t'
  assert(HT.isWhitespace)

  /** Line feed character.
   *
   *  @note This is considered a whitespace character by Java. It is used, following a `[[CR]]` character, as the
   *  _line termination sequence_ for _Windows_, _DOS_, and _OS/2_ text files. In isolation, it is treated as a
   *  line terminator in all _UNIX_-like operating system text files (_Linux_, _BSD_, etc.).
   *
   *  @since 0.2
   */
  val LF: UniChar = '\n'
  assert(LF.isWhitespace)

  /** Minus sign.
   *
   *  @since 0.2
   */
  val Minus: UniChar = '-'

  /** NUL character.
   *
   *  @since 0.2
   */
  val NUL: UniChar = 0x00

  /** Period character.
   *
   *  @since 0.2
   */
  val Period: UniChar = '.'

  /** Plus sign.
   *
   *  @since 0.2
   */
  val Plus: UniChar = '+'

  /** Record separator character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val RS: UniChar = 0x1E
  assert(RS.isWhitespace)

  /** Semicolon character.
   *
   *  @since 0.2
   */
  val Semicolon: UniChar = ';'

  /** Space character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val SPC: UniChar = ' '
  assert(SPC.isWhitespace)

  /** Single quote character.
   *
   *  @since 0.2
   */
  val SQ: UniChar = '\_

  /** Unit separator character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val US: UniChar = 0x1F
  assert(US.isWhitespace)

  /** Vertical tab character.
   *
   *  @note This is considered a whitespace character by Java.
   *
   *  @since 0.2
   */
  val VT: UniChar = 0x0B
  assert(VT.isWhitespace)

  /** Implicit conversion of integer codepoint values to `[[UniChar]]` characters.
   *
   *  @param codepoint _Unicode_ codepoint value of the corresponding character. Invalid codepoints, and codepoints
   *  for surrogate characters, will be rejected.
   *
   *  @return `[[UniChar]]` with specified codepoint value.
   *
   *  @throws IllegalArgumentException if `codepoint` is not a defined, non-surrogate _Unicode_ codepoint.
   *
   *  @since 0.2
   */
  implicit def codepointToUniChar(codepoint: Int): UniChar = UniChar(codepoint)

  /** Implicit conversion of `[[Char]]` values to `[[UniChar]]` characters.
   *
   *  @param character `[[Char]]` to be converted to a `UniChar` value. Surrogate characters, will be rejected.
   *
   *  @return `[[UniChar]]` equivalent of the specified `character`.
   *
   *  @throws IllegalArgumentException if `character` is not a defined, non-surrogate _Unicode_ character.
   *
   *  @since 0.2
   */
  implicit def charToUniChar(character: Char): UniChar = UniChar(character.toInt)

  /** Returns `UniChar` character representing the specified digit value in the specified radix.
   *
   *  @param value Digit value in the specified radix. This should be in the range [0, radix].
   *
   *  @param radix Radix for which a digit is required.
   *
   *  @return `UniChar` character representing the specified digit `value` in the indicated `radix`, wrapped in
   *  `[[Some]]`; or `[[None]]` if either `radix` is outside of the range [`Character.MIN_RADIX`, `Character.MAX_RADIX`]
   *  or if `value` is not a valid digit value for `radix`.
   *
   *  @see [[UniChar.digit()]] for recovering the digit value from a character.
   *
   *  @since 0.2
   */
  def forDigit(value: Int, radix: Int): Option[UniChar] = Character.forDigit(value, radix) match {
    case '\u0000' => None
    case x: Char => Some(UniChar(x.toInt))
  }

  /** Determine if a codepoint represents a defined _Unicode_ character.
   *
   *  Codepoints that do not have defined meanings in the _Unicode_ standard are deemed undefined. Note that
   *  surrogate characters are considered to be defined characters.
   *
   *  @param codepoint _Unicode_ codepoint being checked.
   *
   *  @return `true` if `codepoint` identifies a defined _Unicode_ character, or false otherwise.
   *
   *  @since 0.2
   */
  def isDefined(codepoint: Int): Boolean = Character.isDefined(codepoint)

  /** Determine if a codepoint defines a _high-surrogate_ character.
   *
   *  A high-surrogate character, also called a _leading surrogate_, is used in a pair with a following low-surrogate
   *  character to encode _supplemental_ characters that cannot be encoded in a single _Java_ `[[Character]]`
   *  instance. High-surrogate characters are invalid _Unicode_ characters, because they represent nothing by
   *  themselves.
   *
   *  @param codepoint _Unicode_ codepoint being checked.
   *
   *  @return `true` if `codepoint` identifies a high-surrogate character, or `false` otherwise.
   *
   *  @since 0.2
   */
  def isHighSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_HIGH_SURROGATE && codepoint <= Character.MAX_HIGH_SURROGATE
  }

  /** Determine if a codepoint defines a _low-surrogate_ character.
   *
   *  A low-surrogate character, also called a _trailing surrogate_, is used in a pair with a preceding
   *  high-surrogate character to encode _supplemental_ characters that cannot be encoded in a single _Java_
   *  `[[Character]]` instance. Low-surrogate characters are invalid _Unicode_ characters, because they represent
   *  nothing by themselves.
   *
   *  @param codepoint _Unicode_ codepoint being checked.
   *
   *  @return `true` if `codepoint` identifies an identifier ignorable character, or `false` otherwise.
   *
   *  @since 0.2
   */
  def isLowSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_LOW_SURROGATE && codepoint <= Character.MAX_LOW_SURROGATE
  }

  /** Determine if a codepoint defines a _surrogate_ character.
   *
   *  A surrogate character, which may either be a high- or a low-surrogate, is used with another surrogate to encode
   *  _supplemental_ characters that cannot be encoded in a single _Java_ `[[Character]]` instance. Surrogate
   *  characters are invalid _Unicode_ characters, because they represent nothing by themselves.
   *
   *  @param codepoint _Unicode_ codepoint being checked.
   *
   *  @return `true` if `codepoint` identifies a low- or a high-surrogate character, or `false` otherwise.
   *
   *  @since 0.2
   */
  def isSurrogate(codepoint: Int): Boolean = {
    codepoint >= Character.MIN_SURROGATE && codepoint <= Character.MAX_SURROGATE
  }

  /** Determine if a codepoint is a valid (defined, non-surrogate) _Unicode_ character.
   *
   *  Codepoints that do not have defined meanings in the _Unicode_ standard, or that identify _surrogate_
   *  characters, are deemed invalid.
   *
   *  @param codepoint _Unicode_ codepoint being checked.
   *
   *  @return `true` if `codepoint` identifies a defined _Unicode_ character that is not also a _surrogate_
   *  character, or false otherwise.
   *
   *  @since 0.2
   */
  def isValid(codepoint: Int): Boolean = isDefined(codepoint) && !isSurrogate(codepoint)

  /** Implicit conversion of a sequence of `UniChar` characters to a string.
   *
   *  @param chars Sequence of `UniChar` characters to be converted into a string.
   *
   *  @return String representing the sequence of `UniChar` characters.
   *
   *  @since 0.2
   */
  implicit def seqToString(chars: Seq[UniChar]): String = chars.map(_.toString).mkString

  /** Implicit conversion of a String to an `[[ArraySeq]]` of `UniChar` instances.
   *
   *  An `ArraySeq` is used because it provides efficient storage, lookup and retrieval of individual characters through
   *  a index value.
   *
   *  @param string String to be converted.
   *
   *  @return An `[[ArraySeq]]` of the `UniChar` characters contained in the string.
   *
   *  @since 0.2
   */
  implicit def stringToArraySeq(string: String): ArraySeq[UniChar] = {
    ArraySeq.from(string.codePoints().toScala(LazyList).map(UniChar(_)))
  }
}