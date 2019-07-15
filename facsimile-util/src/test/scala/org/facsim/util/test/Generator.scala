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
// Scala source file belonging to the org.facsim.util.test package.
//======================================================================================================================
package org.facsim.util.test

import org.scalacheck.Gen

/** Custom ''ScalaCheck'' generators.
 *
 *  @since 0.0
 */
object Generator {

  /** Negative integers.
   *
   *  Produce integers across the entire supported range of negative integer values.
   *
   *  The `suchThat` condition is required because the generator can take values from outside the specified range,
   *  which then causes test failures. Refer to [[https://github.com/rickynils/scalacheck/issues/189 ScalaCheck issue
   *  #189]] for further information.
   */
  val negInt = Gen.choose(Integer.MIN_VALUE, -1).suchThat(_ < 0) //scalastyle:ignore magic.number

  /** Non-negative integers.
   *
   *  Produce integers across the entire supported range of non-negative integer values, including zero.
   */
  val nonNegInt = Gen.choose(0, Integer.MAX_VALUE)

  /** Generate any integer value. */
  // A range of [Integer.MinValue, Integer.MaxValue] exceeds the capacity of a signed 32-bit integer value (31 bits for
  // the value, 1 for the sign), so we get around this by having two generators and selecting one or the other at
  // random).
  val int = Gen.oneOf(negInt, nonNegInt)

  /** ISO-8859-1 characters. */
  private val iso8859_1Char = Gen.choose(0.toChar, 255.toChar) //scalastyle:ignore field.name

  /** Strings that can be encoded using ISO-8859-1. */
  val iso8859_1String = Gen.listOf(iso8859_1Char).map(_.mkString) //scalastyle:ignore field.name

  /** Function to determine whether a Unicode codepoint is a reserved high or low surrogate value.
   *
   *  @param c Codepoint to be examined.
   *
   *  @return `true` if the codepoint is a reserved surrogate value (and not a valid ''Unicode'' value), or `false`
   *  otherwise.
   */
  private def isSurrogate(c: Int) = c >= Character.MIN_SURROGATE && c <= Character.MAX_SURROGATE

  /** Function to determine whether a Unicode codepoint is mappable.
   *
   *  A codepoint is mappable if there is a defined ''Unicode'' character, and if that character is not a high or low
   *  surrogate value.
   *
   *  @param c Codepoint to be examined.
   *
   *  @return `true` if there is a mappable Unicode character with the specified codepoint.
   */
  private def isMappable(c: Int) = Character.isDefined(c) && !isSurrogate(c)

  /** Set of valid ''[[http://unicode.org/ Unicode]]'' characters.
   *
   *  Java will throw exceptions if attempts are map to create characters from invalid Unicode codepoints.
   */
  private val unicodeVector = (Character.MIN_CODE_POINT to Character.MAX_CODE_POINT).filter(isMappable)

  /** ''[[http://unicode.org/ Unicode]]'' characters. */
  private val unicodeChar = Gen.choose(0, unicodeVector.length - 1).map(unicodeVector)

  /** Strings that can be encoded in ''[[http://unicode.org/ Unicode]]''. */
  val unicodeString = Gen.listOf(unicodeChar).map(_.flatMap(i => Character.toChars(i)).mkString)
}