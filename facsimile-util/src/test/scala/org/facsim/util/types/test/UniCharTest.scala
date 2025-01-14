//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
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
// Scala source file belonging to the org.facsim.util.types.test package
//======================================================================================================================
package org.facsim.util.types.test

import org.facsim.util.test.{CommonTestMethods, Generator}
import org.facsim.util.test.implicits.{intNoShrink, stringNoShrink}
import org.facsim.util.types.InvalidCodepointException
import org.facsim.util.types.UniChars.UniChar
import org.scalacheck.{Gen, Shrink}
import org.scalatest.funspec.AnyFunSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.jdk.StreamConverters.IntStreamHasToScala
import scala.language.implicitConversions

// Disable Scalastyle checks that make no sense for test code.

// Disable Scalastyle for this entire file, as it reports spurious errors with an unknown cause.

/** Test fixture for [[UniChar]].
 */
final class UniCharTest
extends AnyFunSpec, ScalaCheckPropertyChecks, CommonTestMethods:

  /** Test data and generators.
   */
  trait TestData:

    /** Set of defined and undefined _Unicode_ codepoints, inside the valid range.
     */
    lazy val (definedCodepoints, undefinedCodepoints) =
      (Character.MIN_CODE_POINT to Character.MAX_CODE_POINT).partition(Character.isDefined)

    /** Set of valid and surrogate _Unicode_ codepoints.
     */
    lazy val (validCodepoints, surrogateCodepoints) =
      definedCodepoints.partition(cp => cp < Character.MIN_SURROGATE || cp > Character.MAX_SURROGATE)

    /** Generator for invalid, out-of-range _Unicode_ codepoints.
     *
     *  These are codepoints that have values outside the defined codepoint range.
     */
    lazy val genOutOfRangeCodepoints: Gen[Int] = Gen.oneOf(
      Gen.choose(Int.MinValue, Character.MIN_CODE_POINT - 1),
      Gen.choose(Character.MAX_CODE_POINT + 1, Int.MaxValue)
    )

    /** Generator for defined _Unicode_ codepoints.
     *
     *  These are codepoints that are defined, but which may be surrogates.
     */
    lazy val genDefinedCodepoints: Gen[Int] = Gen.oneOf(definedCodepoints)

    /** Generator for undefined in-range _Unicode_ codepoints.
     *
     *  These are codepoints that have values within the defined codepoint range, but are undefined.
     */
    lazy val genUndefinedInRangeCodepoints: Gen[Int] = Gen.oneOf(undefinedCodepoints)

    /** Generator for valid _Unicode_ codepoints.
     *
     *  These are codepoints that are both defined and non-surrogates.
     */
    lazy val genValidCodepoints: Gen[Int] = Gen.oneOf(validCodepoints)

    /** Generator for surrogate _Unicode_ codepoints.
     */
    lazy val genSurrogateCodepoints: Gen[Int] = Gen.oneOf(surrogateCodepoints)

    /** Set of high- and low-surrogate _Unicode_ codepoints.
     */
    lazy val (genHighSurrogateCodepoints, genLowSurrogateCodepoints) =
      createGenerators(surrogateCodepoints, cp =>
        cp >= Character.MIN_HIGH_SURROGATE && cp <= Character.MAX_HIGH_SURROGATE
      )

    /** Create generators for partitions of the character sets based up the indicated predicate.
     *
     *  @param cps Indexed sequence of character codepoints to which the predicate should be applied. Indexed sequences
     *  offer improved generation performance of codepoint values.
     *
     *  @param p Predicate function, taking an integer and returning a boolean.
     *
     *  @return A tuple of integer generators: the first generating integers that satisfy the predicate; the second
     *  generating integers that fail the predicate.
     */
    protected def createGenerators(cps: IndexedSeq[Int], p: Int => Boolean): (Gen[Int], Gen[Int]) =
      val (s, f) = cps.partition(p)
      (Gen.oneOf(s), Gen.oneOf(f))

  // Element being tested.
  describe("org.facsim.util.types.UniChars.UniChar$"):

    // Test the factory method.
    describe(".apply(Int)"):

      // Test data for the constructor.
      new TestData:

        // Verify that it creates characters with valid codepoints.
        it("must accept all valid codepoints"):
          forAll(genValidCodepoints): cp =>
            UniChar(cp)

        // Verify that it accepts characters with codepoints outside the Unicode range.
        it("must accept codepoints that are outside of the valid Unicode range"):
          forAll(genOutOfRangeCodepoints): cp =>
            UniChar(cp)

        // Verify that it accepts undefined codepoints within the Unicode range.
        it("must accept undefined codepoints that are within the valid Unicode range"):
          forAll(genUndefinedInRangeCodepoints): cp =>
            UniChar(cp)

        // Verify that it accepts defined codepoints if they are surrogates
        it("must reject defined codepoints representing high- or low-surrogate characters"):
          forAll(genSurrogateCodepoints): cp =>
            UniChar(cp)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the constant values.
    describe(".Backslash"):
      it("must have the correct backslash definition"):
        assert(UniChar.Backslash === '\\'.toInt)
    describe(".Colon"):
      it("must have the correct colon definition"):
        assert(UniChar.Colon === ':'.toInt)
    describe(".Comma"):
      it("must have the correct comma definition"):
        assert(UniChar.Comma === ','.toInt)
    describe(".CR"):
      it("must have the correct carriage return definition"):
        assert(UniChar.CR === '\r'.toInt)
    describe(".DQ"):
      it("must have the correct double quote definition"):
        assert(UniChar.DQ === '"'.toInt)
    describe(".FF"):
      it("must have the correct form feed definition"):
        assert(UniChar.FF === '\f'.toInt)
    describe(".FS"):
      it("must have the correct file separator definition"):
        assert(UniChar.FS === 28)
    describe(".GS"):
      it("must have the correct group separator definition"):
        assert(UniChar.GS === 29)
    describe(".HT"):
      it("must have the correct horizontal tab definition"):
        assert(UniChar.HT === '\t'.toInt)
    describe(".LF"):
      it("must have the correct line feed definition"):
        assert(UniChar.LF === '\n'.toInt)
    describe(".Minus"):
      it("must have the correct minus definition"):
        assert(UniChar.Minus === '-'.toInt)
    describe(".NUL"):
      it("must have the correct nul character definition"):
        assert(UniChar.NUL === 0)
    describe(".Period"):
      it("must have the correct period definition"):
        assert(UniChar.Period === '.'.toInt)
    describe(".Plus"):
      it("must have the correct plus definition"):
        assert(UniChar.Plus === '+'.toInt)
    describe(".RS"):
      it("must have the correct record separator definition"):
        assert(UniChar.RS === 30)
    describe(".Semicolon"):
      it("must have the correct semi-colon definition"):
        assert(UniChar.Semicolon === ';'.toInt)
    describe(".SPC"):
      it("must have the correct space definition"):
        assert(UniChar.SPC === ' '.toInt)
    describe(".SQ"):
      it("must have the correct single quote definition"):
        assert(UniChar.SQ === '\''.toInt)
    describe(".US"):
      it("must have the correct unit separator definition"):
        assert(UniChar.US === 31)
    describe(".VT"):
      it("must have the correct vertical tab definition"):
        assert(UniChar.VT === 11)

    // Test the forDigit method, which creates characters
    describe("forDigit(Int, Int)"):

      // Test that it returns a valid digit value, wrapped in Some.
      it("must return a valid digit character, wrapped in Some"):
        forAll(Gen.choose(Character.MIN_RADIX, Character.MAX_RADIX)): radix =>
          forAll(Gen.choose(0, radix - 1)): value =>
            assert(UniChar.forDigit(value, radix) === Some(UniChar(Character.forDigit(value, radix).toInt)))

      // Test that it returns None for values outside the radix range.
      it("must return None for values outside of the radix range"):
        forAll(Gen.choose(Character.MIN_RADIX, Character.MAX_RADIX)): radix =>
          val gen = Gen.oneOf(
            Generator.negInt,
            Gen.choose(radix, Int.MaxValue),
          )
          forAll(gen): value =>
            assert(UniChar.forDigit(value, radix) === None)

      // Test that it returns None for invalid radix values.
      it("must return None for invalid radix values"):
        val gen = Gen.oneOf(
          Gen.choose(Int.MinValue, Character.MIN_RADIX - 1),
          Gen.choose(Character.MAX_RADIX + 1, Int.MaxValue),
        )
        forAll(gen): radix =>

          // 0 is a valid value for all supported radix values.
          assert(UniChar.forDigit(0, radix) === None)

    // Test the isDefined method.
    describe(".isDefined(Int)"):

      // Test data for this method.
      new TestData:

        // Test that it returns true for defined codepoints.
        it("must return true for all defined codepoints"):
          forAll(genDefinedCodepoints): cp =>
            assert(UniChar.isDefined(cp))

        // Test that it returns false for codepoints outside the valid range.
        it("must return false for all codepoints outside of the valid range"):
          forAll(genOutOfRangeCodepoints): cp =>
            assert(!UniChar.isDefined(cp))

        // Test that it returns false for undefined codepoints inside the valid range.
        it("must return false for all undefined codepoints inside of the valid range"):
          forAll(genUndefinedInRangeCodepoints): cp =>
            assert(!UniChar.isDefined(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isHighSurrogate method.
    describe(".isHighSurrogate(Int)"):

      // Test data for this method.
      new TestData:

        // Test that it returns true for all high surrogate codepoints.
        it("must return true for all high surrogate codepoints"):
          forAll(genHighSurrogateCodepoints): cp =>
            assert(UniChar.isHighSurrogate(cp))

        // Test that it returns false for all low surrogate codepoints.
        it("must return false for all low surrogate codepoints"):
          forAll(genLowSurrogateCodepoints): cp =>
            assert(!UniChar.isHighSurrogate(cp))

        // Test that it returns false for valid codepoints.
        it("must return false for all valid codepoints"):
          forAll(genValidCodepoints): cp =>
            assert(!UniChar.isHighSurrogate(cp))

        // Test that it returns false for out-of-range codepoints.
        it("must return false for all out-of-range codepoints"):
          forAll(genOutOfRangeCodepoints): cp =>
            assert(!UniChar.isHighSurrogate(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isLowSurrogate method.
    describe(".isLowSurrogate(Int)"):

      // Test data for this method.
      new TestData:

        // Test that it returns true for all low surrogate codepoints.
        it("must return true for all low surrogate codepoints"):
          forAll(genLowSurrogateCodepoints): cp =>
            assert(UniChar.isLowSurrogate(cp))

        // Test that it returns false for all high surrogate codepoints.
        it("must return false for all high surrogate codepoints"):
          forAll(genHighSurrogateCodepoints): cp =>
            assert(!UniChar.isLowSurrogate(cp))

        // Test that it returns false for valid codepoints.
        it("must return false for all valid codepoints"):
          forAll(genValidCodepoints): cp =>
            assert(!UniChar.isLowSurrogate(cp))

        // Test that it returns false for out-of-range codepoints.
        it("must return false for all out-of-range codepoints"):
          forAll(genOutOfRangeCodepoints): cp =>
            assert(!UniChar.isLowSurrogate(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isSurrogate method.
    describe(".isSurrogate(Int)"):

      // Test data for this method.
      new TestData:

        // Test that it returns true for all high surrogate codepoints.
        it("must return true for all high surrogate codepoints"):
          forAll(genHighSurrogateCodepoints): cp =>
            assert(UniChar.isSurrogate(cp))

        // Test that it returns true for all low surrogate codepoints.
        it("must return true for all low surrogate codepoints"):
          forAll(genLowSurrogateCodepoints): cp =>
            assert(UniChar.isSurrogate(cp))

        // Test that it returns false for valid codepoints.
        it("must return false for all valid codepoints"):
          forAll(genValidCodepoints): cp =>
            assert(!UniChar.isSurrogate(cp))

        // Test that it returns false for out-of-range codepoints.
        it("must return false for all out-of-range codepoints"):
          forAll(genOutOfRangeCodepoints): cp =>
            assert(!UniChar.isSurrogate(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isValid method.
    describe(".isValid(Int)"):

      // Test data for this method.
      new TestData:

        // Test that it returns true for valid codepoints.
        it("must return true for all valid codepoints"):
          forAll(genValidCodepoints): cp =>
            assert(UniChar.isValid(cp))

        // Test that it returns false for all high surrogate codepoints.
        it("must return false for all high surrogate codepoints"):
          forAll(genHighSurrogateCodepoints): cp =>
            assert(!UniChar.isValid(cp))

        // Test that it returns false for all low surrogate codepoints.
        it("must return false for all low surrogate codepoints"):
          forAll(genLowSurrogateCodepoints): cp =>
            assert(!UniChar.isValid(cp))

        // Test that it returns false for all undefined in-range codepoints.
        it("must return false for all undefined in-range codepoints"):
          forAll(genUndefinedInRangeCodepoints): cp =>
            assert(!UniChar.isValid(cp))

        // Test that it returns false for out-of-range codepoints.
        it("must return false for all out-of-range codepoints"):
          forAll(genOutOfRangeCodepoints): cp =>
            assert(!UniChar.isValid(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test conversion of an Int to a UniChar.
    describe(".intToUniChar(Int)"):

      // Test that it works explicitly.
      it("must convert Int values to UniChars explicitly"):
        forAll: (cp: Int) =>
          assert(UniChar.intToUniChar(cp) === UniChar(cp))

      // Test that it works implicitly.
      it("must convert Int values to UniChars implicitly"):
        forAll: (cp: Int) =>
          val uc: UniChar = cp
          assert(uc === UniChar(cp))

    // Test conversion of a Char to a UniChar.
    describe(".charToUniChar(Char)"):

      // Test that it works explicitly.
      it("must convert Char values to UniChars explicitly"):
        forAll: (c: Char) =>
          assert(UniChar.charToUniChar(c) === UniChar(c.toInt))

      // Test that it works implicitly.
      it("must convert Char values to UniChars implicitly"):
        forAll: (c: Char) =>
          val uc: UniChar = c
          assert(uc === UniChar(c.toInt))

    // Test conversion of a sequence of UniChar characters to a String.
    describe(".seqUniCharToString(Seq[UniChar])"):

      // Test that it works explicitly.
      it("must convert Seq[UniChar] values to Strings explicitly"):
        forAll: (s: String) =>
          val suc: Seq[UniChar] = s.codePoints().nn.toScala(IndexedSeq).map(cp => UniChar.apply(cp))
          assert(UniChar.seqUniCharToString(suc) === s)

      // Test that it works implicitly.
      it("must convert Seq[UniChar] values to Strings implicitly"):
        forAll: (s: String) =>
          val suc: Seq[UniChar] = s.codePoints().nn.toArray.nn.map(cp => UniChar.apply(cp)).toIndexedSeq
          val rs: String = suc
          assert(rs === s)

    // Test conversion of a String to a sequence of UniChar characters.
    describe(".stringTosSeqUniChar(String)"):

      // Test that it works explicitly.
      it("must convert Strings to Seq[UniChar] explicitly"):
        forAll: (s: String) =>
          val suc = UniChar.stringToSeqUniChar(s)
          val esuc = s.codePoints().nn.toArray.nn.map(cp => UniChar(cp)).toIndexedSeq
          assert(suc === esuc)

      it("must convert Strings to Seq[UniChar] implicitly"):
        forAll: (s: String) =>
          val suc: Seq[UniChar] = s
          val esuc = s.codePoints().nn.toArray.nn.map(cp => UniChar(cp)).toIndexedSeq
          assert(suc === esuc)

  // Element being tested.
  describe("org.facsim.util.types.UniChars.UniChar"):

    // Test the codepoint method.
    describe(".codepoint"):

      // Verify that it reports the correct codepoint for each value.
      it("must return the correct codepoint for all UniChar instances"):
        forAll: (cp: Int) =>
          assert(UniChar(cp).codepoint == cp)

    // Test the digit method, that returns the value of a digit character, in a specified radix.
    //
    // Note that we do not need to test the full capabilities of this method, which can be complex, since this is just a
    // wrapper method. Instead, we just need to verify that we've not ballsed-up the wrapper.
    describe(".digit(Int)"):

      // Verify that it returns a decimal digit value wrapped in Some.
      it("must return correct digit values, wrapped in Some, for decimal digits"):
        val radix = 10
        for(i <- 0 until radix)
          val cp = i + '0'.toInt
          assert(UniChar(cp).digit(radix) === Some(i))

      // Verify that it returns a hexadecimal digit value wrapped in Some.
      it("must return correct digit values, wrapped in Some, for hexadecimal digits"):
        val radix = 16
        val decLimit = 10

        // Test digits 0 through 9.
        for(i <- 0 until decLimit)
          val cp = i + '0'.toInt
          assert(UniChar(cp).digit(radix) === Some(i))

        // Test digits A through F.
        for(i <- decLimit until radix)
          val cp = i + 'A'.toInt - decLimit
          assert(UniChar(cp).digit(radix) === Some(i))

      // Verify that it returns Some(0) for all supported radix values.
      it("must return Some(0) for '0' in all supported radix values "):
        val gen = Gen.choose(Character.MIN_RADIX, Character.MAX_RADIX)
        val ch = UniChar('0'.toInt)
        forAll(gen): radix =>
          assert(ch.digit(radix) === Some(0))

      // Verify that it returns None for invalid or unsupported radix values.
      it("must return None for invalid or unsupported radix value"):
        val gen = Gen.oneOf(
          Gen.choose(Int.MinValue, Character.MIN_RADIX - 1),
          Gen.choose(Character.MAX_RADIX + 1, Int.MaxValue)
        )
        val ch = UniChar('0'.toInt)
        forAll(gen): radix =>
          assert(ch.digit(radix) === None)

      // Verify that it returns None for characters that are not valid decimal digits.
      it("must return none for non-digit characters"):
        val radix = 10
        val gen = Gen.oneOf(
          Gen.choose(Character.MIN_CODE_POINT, '0'.toInt - 1),
          Gen.choose('9'.toInt + 1, 255)
        )
        forAll(gen): cp =>
          assert(UniChar(cp).digit(radix) === None)

    // Test the isAlphabetic method.
    describe(".isAlphabetic"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (alphabetic, nonAlphabetic) = createGenerators(validCodepoints, Character.isAlphabetic)

        // Test that it reports true for alphabetic characters.
        it("must report true for alphabetic characters"):
            forAll(alphabetic): cp =>
              assert(UniChar(cp).isAlphabetic)

        // Test that it reports false for alphabetic characters.
        it("must report false for non-alphabetic characters"):
          forAll(nonAlphabetic): cp =>
            assert(!UniChar(cp).isAlphabetic)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isBMPCodePoint method.
    describe(".isBMPCodePoint"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (bmp, nonBMP) = createGenerators(validCodepoints, Character.isBmpCodePoint)

        // Test that it reports true for BMP characters.
        it("must report true for basic multilingual plane characters"):
          forAll(bmp): cp =>
            assert(UniChar(cp).isBMPCodePoint)

        // Test that it reports false for supplemental characters.
        it("must report false for supplementary characters"):
          forAll(nonBMP): cp =>
            assert(!UniChar(cp).isBMPCodePoint)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isDefined method.
    describe(".isDefined"):

      // Test data for this method.
      new TestData:

        // All valid codepoints are inherently defined, so this method must always return true.
        it("must always return true"):
          forAll(genValidCodepoints): cp =>
            assert(UniChar(cp).isDefined)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isDigit method.
    describe(".isDigit"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (digits, nonDigits) = createGenerators(validCodepoints, Character.isDigit)

        // Test that it reports true for decimal digit characters.
        it("must report true for decimal digit characters"):
          forAll(digits): cp =>
            assert(UniChar(cp).isDigit)

        // Test that it reports false for non-decimal digit characters.
        it("must report false for non-decimal digit characters"):
          forAll(nonDigits): cp =>
            assert(!UniChar(cp).isDigit)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isHighSurrogate method.
    describe(".isHighSurrogate"):

      // Test data for this method.
      new TestData:

        // All valid codepoints are inherently non-surrogates, so this method must always return false.
        it("must always return false"):
          forAll(genValidCodepoints): cp =>
            assert(!UniChar(cp).isHighSurrogate)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isIdentifierIgnorable method.
    describe(".isIdentifierIgnorable"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (ignorable, nonIgnorable) = createGenerators(validCodepoints, Character.isIdentifierIgnorable)

        // Test that it reports true for identifier ignorable characters.
        it("must report true for identifier ignorable characters"):
          forAll(ignorable): cp =>
            assert(UniChar(cp).isIdentifierIgnorable)

        // Test that it reports false for identifier non-ignorable characters.
        it("must report false for identifier non-ignorable characters"):
          forAll(nonIgnorable): cp =>
            assert(!UniChar(cp).isIdentifierIgnorable)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isIdeographic method.
    describe(".isIdeographic"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (ideographic, nonIdeographic) = createGenerators(validCodepoints, Character.isIdeographic)

        // Test that it reports true for ideographic characters.
        it("must report true for ideographic characters"):
          forAll(ideographic): cp =>
            assert(UniChar(cp).isIdeographic)

        // Test that it reports false for non-ideographic characters.
        it("must report false for non-ideographic characters"):
          forAll(nonIdeographic): cp =>
            assert(!UniChar(cp).isIdeographic)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isISOControl method.
    describe(".isISOControl"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (isoControl, nonISOControl) = createGenerators(validCodepoints, Character.isISOControl)

        // Test that it reports true for ISO control characters.
        it("must report true for ISO control characters"):
          forAll(isoControl): cp =>
            assert(UniChar(cp).isISOControl)

        // Test that it reports false for non-ISO control characters.
        it("must report false for non-ISO control characters"):
          forAll(nonISOControl): cp =>
            assert(!UniChar(cp).isISOControl)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isJavaIdentifierPart method.
    describe(".isJavaIdentifierPart"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (javaPart, nonJavaPart) = createGenerators(validCodepoints, Character.isJavaIdentifierPart)

        // Test that it reports true for Java identifier part characters.
        it("must report true for Java identifier part characters"):
          forAll(javaPart): cp =>
            assert(UniChar(cp).isJavaIdentifierPart)

        // Test that it reports false for Java identifier non-part characters.
        it("must report false for Java identifier non-part characters"):
          forAll(nonJavaPart): cp =>
            assert(!UniChar(cp).isJavaIdentifierPart)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isJavaIdentifierStart method.
    describe(".isJavaIdentifierStart"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (javaStart, nonJavaStart) = createGenerators(validCodepoints, Character.isJavaIdentifierStart)

        // Test that it reports true for Java identifier start characters.
        it("must report true for Java identifier start characters"):
          forAll(javaStart): cp =>
            assert(UniChar(cp).isJavaIdentifierStart)

        // Test that it reports false for Java identifier non-start characters.
        it("must report false for Java identifier non-start characters"):
          forAll(nonJavaStart): cp =>
            assert(!UniChar(cp).isJavaIdentifierStart)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isLetter method.
    describe(".isLetter"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (letters, nonLetters) = createGenerators(validCodepoints, Character.isLetter)

        // Test that it reports true for letter characters.
        it("must report true for letter characters"):
          forAll(letters): cp =>
            assert(UniChar(cp).isLetter)

        // Test that it reports false for non-letter characters.
        it("must report false for non-letter characters"):
          forAll(nonLetters): cp =>
            assert(!UniChar(cp).isLetter)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isLetterOrDigit method.
    describe(".isLetterOrDigit"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (letterDigits, nonLetterDigits) = createGenerators(validCodepoints, Character.isLetterOrDigit)

        // Test that it reports true for letter characters.
        it("must report true for letter-or-digit characters"):
          forAll(letterDigits): cp =>
            assert(UniChar(cp).isLetterOrDigit)

        // Test that it reports false for non-letter characters.
        it("must report false for non-letter-or-digit characters"):
          forAll(nonLetterDigits): cp =>
            assert(!UniChar(cp).isLetterOrDigit)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isLowSurrogate method.
    describe(".isLowSurrogate"):

      // Test data for this method.
      new TestData:

        // All valid codepoints are inherently non-surrogates, so this method must always return false.
        it("must always return false"):
          forAll(genValidCodepoints): cp =>
            assert(!UniChar(cp).isLowSurrogate)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isLowerCase method.
    describe(".isLowerCase"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (lowerCase, nonLowerCase) = createGenerators(validCodepoints, Character.isLowerCase)

        // Test that it reports true for lower case characters.
        it("must report true for lower case characters"):
          forAll(lowerCase): cp =>
            assert(UniChar(cp).isLowerCase)

        // Test that it reports false for non-lower case characters.
        it("must report false for non-lower case characters"):
          forAll(nonLowerCase): cp =>
            assert(!UniChar(cp).isLowerCase)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isMirrored method.
    describe(".isMirrored"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (mirrored, nonMirrored) = createGenerators(validCodepoints, Character.isMirrored)

        // Test that it reports true for mirrored characters.
        it("must report true for mirrored characters"):
          forAll(mirrored): cp =>
            assert(UniChar(cp).isMirrored)

        // Test that it reports false for non-mirrored characters.
        it("must report false for non-mirrored characters"):
          forAll(nonMirrored): cp =>
            assert(!UniChar(cp).isMirrored)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isSpaceChar method.
    describe(".isSpaceChar"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (space, nonSpace) = createGenerators(validCodepoints, Character.isSpaceChar)

        // Test that it reports true for space characters.
        it("must report true for space characters"):
          forAll(space): cp =>
            assert(UniChar(cp).isSpaceChar)

        // Test that it reports false for non-space characters.
        it("must report false for non-space characters"):
          forAll(nonSpace): cp =>
            assert(!UniChar(cp).isSpaceChar)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isSupplementary method.
    describe(".isSupplementary"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (supplementary, nonSupplementary) = createGenerators(validCodepoints, Character.isSupplementaryCodePoint)

        // Test that it reports true for supplementary characters.
        it("must report true for supplementary characters"):
          forAll(supplementary): cp =>
            assert(UniChar(cp).isSupplementary)

        // Test that it reports false for BMP characters.
        it("must report false for basic multilingual plane characters"):
          forAll(nonSupplementary): cp =>
            assert(!UniChar(cp).isSupplementary)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isSurrogate method.
    describe(".isSurrogate"):

      // Test data for this method.
      new TestData:

        // All valid codepoints are inherently non-surrogates, so this method must always return false.
        it("must always return false"):
          forAll(genValidCodepoints): cp =>
            assert(!UniChar(cp).isSurrogate)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isTitleCase method.
    describe(".isTitleCase"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (titleCase, nonTitleCase) = createGenerators(validCodepoints, Character.isTitleCase)

        // Test that it reports true for title case characters.
        it("must report true for title case characters"):
          forAll(titleCase): cp =>
            assert(UniChar(cp).isTitleCase)

        // Test that it reports false for non-title case characters.
        it("must report false for non-title case characters"):
          forAll(nonTitleCase): cp =>
            assert(!UniChar(cp).isTitleCase)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isUnicodeIdentifierPart method.
    describe(".isUnicodeIdentifierPart"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (ucPart, nonUCPart) = createGenerators(validCodepoints, Character.isUnicodeIdentifierPart)

        // Test that it reports true for Unicode identifier part characters.
        it("must report true for Unicode identifier part characters"):
          forAll(ucPart): cp =>
            assert(UniChar(cp).isUnicodeIdentifierPart)

        // Test that it reports false for Unicode identifier non-part characters.
        it("must report false for Unicode identifier non-part characters"):
          forAll(nonUCPart): cp =>
            assert(!UniChar(cp).isUnicodeIdentifierPart)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isUnicodeIdentifierStart method.
    describe(".isUnicodeIdentifierStart"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (ucStart, nonUCStart) = createGenerators(validCodepoints, Character.isUnicodeIdentifierStart)

        // Test that it reports true for Unicode identifier start characters.
        it("must report true for Unicode identifier start characters"):
          forAll(ucStart): cp =>
            assert(UniChar(cp).isUnicodeIdentifierStart)

        // Test that it reports false for Unicode identifier non-start characters.
        it("must report false for Unicode identifier non-start characters"):
          forAll(nonUCStart): cp =>
            assert(!UniChar(cp).isUnicodeIdentifierStart)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isUpperCase method.
    describe(".isUpperCase"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (upperCase, nonUpperCase) = createGenerators(validCodepoints, Character.isUpperCase)

        // Test that it reports true for upper case characters.
        it("must report true for upper case characters"):
          forAll(upperCase): cp =>
            assert(UniChar(cp).isUpperCase)

        // Test that it reports false for non-upper case characters.
        it("must report false for non-upper case characters"):
          forAll(nonUpperCase): cp =>
            assert(!UniChar(cp).isUpperCase)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isValid method.
    describe(".isValid"):

      // Test data for this method.
      new TestData:

        // All valid codepoints are inherently valid, so this method must always return true.
        it("must always return true"):
          forAll(genValidCodepoints): cp =>
            assert(UniChar(cp).isValid)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the isWhitespace method.
    describe(".isWhitespace"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (whitespace, nonWhiteSpace) = createGenerators(validCodepoints, Character.isWhitespace)

        // Test that it reports true for whitespace characters.
        it("must report true for whitespace characters"):
          forAll(whitespace): cp =>
            assert(UniChar(cp).isWhitespace)

        // Test that it reports false for non-whitespace characters.
        it("must report false for non-whitespace characters"):
          forAll(nonWhiteSpace): cp =>
            assert(!UniChar(cp).isWhitespace)

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test to toLowerCase method.
    describe(".toLowerCase"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (notChanged, changed) = createGenerators(validCodepoints, cp =>
          !(Character.isTitleCase(cp) || Character.isUpperCase(cp))
        )

        // Test that it returns the original character, if no conversion should be performed.
        it("must report same character if no conversion is required"):
          forAll(notChanged): cp =>
            val ch = UniChar(cp)
            assert(ch.toLowerCase === ch)

        // Test that it the expected conversion character otherwise.
        it("must report the correct converted character if a conversion is required"):
          forAll(changed): cp =>
            assert(UniChar(cp).toLowerCase.codepoint === Character.toLowerCase(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test the asString method.
    describe(".asString"):

      // Test data for this method.
      new TestData:

        // Test that characters are correctly converted into strings.
        it("must convert valid characters to strings correctly"):
          forAll(genValidCodepoints): cp =>
            val ch = UniChar(cp)
            assert(ch.asString === Character.toString(cp))

        // Test that out of range characters throw an InvalidCodepointException.
        it("must throw an InvalidCodepointException if given an out-of-range character"):
          forAll(genOutOfRangeCodepoints): cp =>
            intercept[InvalidCodepointException]:
              UniChar(cp).asString

        // For undefined in-range characters, a string should be returned, but it will not contain recognized
        // characters. The explanation is that such characters may be used by a future Unicode standard.
        it("must convert undefined in-range characters (may exist in a later Unicode standard)"):
          forAll(genUndefinedInRangeCodepoints): cp =>
            UniChar(cp).asString
              val ch = UniChar(cp)
              assert(ch.asString === Character.toString(cp))

        // Test that surrogate characters throw an InvalidCodepointException.
        it("must throw an InvalidCodepointException if given a surrogate character"):
          forAll(genOutOfRangeCodepoints): cp =>
            intercept[InvalidCodepointException]:
              UniChar(cp).asString

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test to toTitleCase method.
    describe(".toTitleCase"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (notChanged, changed) = createGenerators(validCodepoints, cp =>
          !(Character.isLowerCase(cp) || Character.isUpperCase(cp))
        )

        // Test that it returns the original character, if no conversion should be performed.
        it("must report same character if no conversion is required"):
          forAll(notChanged): cp =>
            val ch = UniChar(cp)
            assert(ch.toTitleCase === ch)

        // Test that it the expected conversion character otherwise.
        it("must report the correct converted character if a conversion is required"):
          forAll(changed): cp =>
            assert(UniChar(cp).toTitleCase.codepoint === Character.toTitleCase(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()

    // Test to toUpperCase method.
    describe(".toUpperCase"):

      // Test data for this method.
      new TestData:

        // Create generators.
        val (notChanged, changed) = createGenerators(validCodepoints, cp =>
          !(Character.isLowerCase(cp) || Character.isTitleCase(cp))
        )

        // Test that it returns the original character, if no conversion should be performed.
        it("must report same character if no conversion is required"):
          forAll(notChanged): cp =>
            val ch = UniChar(cp)
            assert(ch.toUpperCase === ch)

        // Test that it the expected conversion character otherwise.
        it("must report the correct converted character if a conversion is required"):
          forAll(changed): cp =>
            assert(UniChar(cp).toUpperCase.codepoint === Character.toUpperCase(cp))

      // Avoid "discarded non-Unit value" compiler warning.
      ()
