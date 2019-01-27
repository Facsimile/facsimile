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
// Scala source file belonging to the org.facsim.util.parse package.
//======================================================================================================================
package org.facsim.util.parse

import org.parboiled2.{CharPredicate, Parser, Rule1}
import scala.util.Try
import shapeless.T

/** Enhanced ''[[http://parboiled2.org/ Parboiled2]]'' input parser.
 *
 *  @constructor Construct a new abstract base parser class.
 */
private[facsim] abstract class BaseParser
extends Parser {

  /** A conditional rule, which is only matched if the predicate is `true`.
   *
   *  @tparam T Value pushed onto the stack by `trueRule`. If the rule handles multiple fields, then they must be
   *  combined into a single value pushed onto the stack.
   *
   *  @param condition Value that must be true for the rule to be evaluated.
   *
   *  @param trueRule Rule function to be evaluated if `condition` is `true`. The result to be wrapped in an Option.
   *
   *  @return Rule that returns the result of `trueRule` if `condition` is `true`, wrapped in [[scala.Some]];
   *  [[scala.None]] if `condition` is `false`.
   */
  protected[facsim] def conditional[T](condition: Boolean, trueRule: () => Rule1[T]): Rule1[Option[T]] = rule {

    // If the condition is true, control flows to the rule and - if that's successful - onto the stack wrapped in a
    // some. If the test and parse fails, then None is pushed onto the stack.
    test(condition) ~!~ trueRule() ~> (Some(_)) | push(None)
  }

  /** A pair of conditional rules, with selection determined by a predicate.
   *
   *  @tparam T Value pushed onto the stack by `trueRule` and `falseRule`. If the rule handles multiple fields, then
   *  they must be combined into a single value pushed onto the stack.
   *
   *  @param condition Value that determines which rule is evaluated.
   *
   *  @param trueRule Rule function to be evaluated if `condition` is `true`.
   *
   *  @param falseRule Rule function to be evaluated if `condition` is `false`
   *
   *  @return Rule that returns the result of `trueRule` if `condition` is `true` or the result of `falseRule`
   *  otherwise.
   */
  protected[facsim] def conditional[T](condition: Boolean, trueRule: () => Rule1[T], falseRule: () => Rule1[T]):
  Rule1[T] = rule {

    // If the condition is true, control flows to the true rule, otherwise to the falseRule. The result is pushed to
    // the stack.
    test(condition) ~!~ trueRule() | falseRule()
  }

  /** Line termination rule.
   *
   *  This default parser rule matches the three most common text line termination sequences:
   *   - Carriage return + linefeed (common to Windows, DOS, OS/2, etc.)
   *   - Linefeed only (common to Linux, BSD, Unix, MacOS 10+, etc.)
   *   - Carriage return only (MacOS prior to release 10, etc.)
   *
   *  Override this rule definition to support specific line termination sequences.
   */
  protected[facsim] def eol = rule {
    (BaseParser.CR ~ optional(BaseParser.LF)) | BaseParser.LF
  }

  /** Whitespace characters.
   *
   *  This default parser rule matches a single common whitespace character. Whitespace characters are those for which
   *  [[java.lang.Character.isWhitespace(Char)]] returns `true`.
   *
   *  Override this rule definition to support different sets of whitespace characters. In particular, note that this
   *  default rule matches line termination sequences.
   */
  protected[facsim] val wsChar = CharPredicate.from(Character.isWhitespace)

  /** Whitespace rule.
   *
   *  Matches a single whitespace character.
   */
  protected[facsim] final def ws = rule {
    quiet(wsChar)
  }

  /** Consecutive whitespace rule.
   *
   *  Allows consecutive whitespace to be grouped together and removed simultaneously.
   */
  protected[facsim] final def cws = rule {
    quiet(oneOrMore(wsChar))
  }

  /** Decimal digit rule.
   *
   *  Matches a single character in the range [0-9] inclusive.
   */
  protected[facsim] final val digit = CharPredicate.Digit

  /** Hexadecimal digit rule.
   *
   *  Matches a single character in the range [0-9a-fA-F].
   */
  protected[facsim] final val hexDigit = CharPredicate.HexDigit

  /** Integer value.
   *
   *  Matches with integer values, pushing the associated value onto the stack.
   */
  protected[facsim] final def intValue = rule {
    capture(optional(BaseParser.Minus) ~ oneOrMore(digit)) ~> {s =>
      val i = Try(s.toInt)
      test(i.isSuccess) ~ push(i.get)
    }
  }

  /** Integer hex value.
   *
   *  Matches with hexadecimal integer values, pushing the associated value onto the stack.
   */
  protected[facsim] final def hexValue = rule {
    capture(oneOrMore(hexDigit)) ~> {s =>
      val i = Try(Integer.parseInt(s, 16)) //scalastyle:ignore magic.number
      test(i.isSuccess) ~ push(i.get)
    }
  }

  /** Double value.
   *
   *  Matches with double values, pushing the associated value onto the stack.
   *
   *  @note This rule does not accept double precision values in exponent form.
   */
  protected[facsim] final def dblValue = rule {
    capture(optional(BaseParser.Minus) ~ oneOrMore(digit) ~ optional(BaseParser.Period ~ zeroOrMore(digit))) ~> {s =>
      val d = Try(s.toDouble)
      test(d.isSuccess) ~ push(d.get)
    }
  }

  /** Double value, optionally in exponent form.
   *
   *  Matches with double values, including doubles expressed in exponent form, pushing the associated value onto the
   *  stack.
   */
  protected[facsim] final def expValue = rule {
    capture(optional(BaseParser.Minus) ~ oneOrMore(digit) ~ optional(BaseParser.Period ~ zeroOrMore(digit)) ~
    optional((ch('E') | ch('e')) ~ optional(BaseParser.Minus) ~ oneOrMore(digit))) ~> {s =>
      val d = Try(s.toDouble)
      test(d.isSuccess) ~ push(d.get)
    }
  }
}

/** Parser companion class.
 *
 *  Defines common control, escape and delimiter characters that may have special significance when parsing strings and
 *  files.
 *
 *  @since 0.0
 */
object BaseParser {

  /** Backslash character.
   *
   *  @since 0.0
   */
  val BS: Char = '\\'

  /** Colon character.
   *
   *  @since 0.0
   */
  val Colon: Char = ':'

  /** Comma character.
   *
   *  @since 0.0
   */
  val Comma: Char = ','

  /** Carriage return character.
   *
   *  @since 0.0
   */
  val CR: Char = '\r'

  /** Double-quote character.
   *
   *  @since 0.0
   */
  val DQ: Char = '\"'

  /** Formfeed character.
   *
   *  @since 0.0
   */
  val FF: Char = '\f'

  /** Horizontal tab character.
   *
   *  @since 0.0
   */
  val HT: Char = '\t'

  /** Linefeed character.
   *
   *  @since 0.0
   */
  val LF: Char = '\n'

  /** Minus sign.
   *
   *  @since 0.0
   */
  val Minus: Char = '-'

  /** NUL character.
   *
   *  @since 0.0
   */
  val NUL: Char = '\0'

  /** Period character.
   *
   *  @since 0.0
   */
  val Period: Char = '.'

  /** Plus sign.
   *
   *  @since 0.0
   */
  val Plus: Char = '+'

  /** Semicolon character.
   *
   *  @since 0.0
   */
  val Semicolon: Char = ';'

  /** Space character.
   *
   *  @since 0.0
   */
  val SPC: Char = ' '

  /** Single-quote character.
   *
   *  @since 0.0
   */
  val SQ: Char = '\''

  /** Vertical tab character.
   *
   *  @since 0.0
   */
  val VT: Char = '\u000B'
}



