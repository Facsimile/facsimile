/*
 * Facsimile -- A Discrete-Event Simulation Library
 * Copyright © 2004-2016, Michael J Allen.
 *
 * This file is part of Facsimile.
 *
 * Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
 * http://www.gnu.org/licenses/lgpl.
 *
 * The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
 * project home page at:
 *
 *   http://facsim.org/
 *
 * Thank you for your interest in the Facsimile project!
 *
 * IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
 * inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
 * your code fails to comply with the standard, then your patches will be rejected. For further information, please
 * visit the coding standards at:
 *
 *   http://facsim.org/Documentation/CodingStandards/
 * =====================================================================================================================
 * Scala source file belonging to the org.facsim.util.test package.
 */
package org.facsim.util.test

import org.scalatest.FunSpec
import scala.annotation.tailrec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/**
 * Test behaviors for [[Equals]] trait-implementing classes.
 *
 * This trait can be used as part of the test suite for a class that must honor the ''equals contract'':
 *
 * - ''Reflexive'': `x == x` should be true for various non-`null` `x` of the associated type.
 * - ''Symmetric'': `x == y` and `y == x` should both give the same, expected answer for various non-`null` `x` & `y`.
 * - ''Transitive'': `x == y`, `y == z` and `x == z` should all give the same, expected answer for various non-`null`
 * `x`, `y` & `z`.
 * - ''Consistent'': `x == y` must always hold `true`, or always hold `false`, if `x` & `y` are not modified
 * (guaranteed if `x` & `y` are immutable).
 * - `x != y` if `x` and `y` are not comparable objects, regardless of contents, for various non-`null` `x` & `y`.
 * - `x != null` for various non-`null` `x`. Should not see [[NullPointerException]] thrown.
 * - If `x == y`, then verify also that `x.hashMap = y.hashMap`, for various non-`null` `x` & `y`.
 * - etc.
 *
 * @tparam V Type, implementing the [[Equals]] trait, to be tested.
 */
trait EqualsBehaviors[V <: Equals] {
  this: FunSpec =>

  /**
   * Verify a [[Equals]]-implementing object.
   */
  final def equalsBehavior(equalsFixture: EqualsFixture[V]): Unit = {

    /*
     * Function to compare a value to every value in a list, verifying the result.
     */
    def compare(list: List[V], expectedResult: Boolean): Unit = {
      @tailrec
      def doCompare(value: V, remainder: List[V]): Unit = {
        if(remainder.nonEmpty) {
          remainder.foreach {
            other =>
            assert(value.equals(other) === expectedResult, s"$value dir not equal $other")
            assert(other.equals(value) === expectedResult)
          }
          doCompare(remainder.head, remainder.tail)
        }
      }
      doCompare(list.head, list.tail)
    }

    /*
     * Function to compare the hash code for every value in a list to every other value in a list.
     */
    def compareHash(list: List[V]): Unit = {
      @tailrec
      def doCompareHash(value: V, list: List[V]): Unit = {
        if(list.nonEmpty) {
          list.foreach {
            other =>
            assert(value.hashCode === other.hashCode)
          }
          doCompareHash(list.head, list.tail)
        }
      }
      doCompareHash(list.head, list.tail)
    }

    /*
     * Tests for the canEquals method.
     */
    describe(".canEqual(Any)") {

      /*
       * Verify that the object cannot compare as equal to null and does not result in a NullPointerException being
       * thrown.
       */
      it("must return false if passed null") {
        equalsFixture.valueSample.foreach {
          value =>
          assert(!value.canEqual(null)) //scalastyle:ignore null
        }
      }

      /*
       * Verify that it reports false if passed an object of a different type.
       */
      it("must return false if passed an object of a different type") {
        equalsFixture.valueSample.foreach {
          value =>
          equalsFixture.nonValueSample.foreach {
            that =>
            assert(!value.canEqual(that))
          }
        }
      }

      /*
       * Verify that it reports true if passed an object of the same type.
       */
      it("must return true if passed an object of the same type - including itself") {
        equalsFixture.valueSample.foreach {
          value =>
          equalsFixture.valueSample.foreach {
            that =>
            assert(value.canEqual(that))
          }
        }
      }
    }

    /*
     * Tests for the equals method.
     */
    describe("equals(Any)") {

      /*
       * Verify that the object never compares equal to null and does not result in a NullPointerException being thrown.
       */
      it("must return false if compared to null") {
        equalsFixture.valueSample.foreach {
          value =>
          assert(!value.equals(null)) //scalastyle:ignore null
        }
      }

      /*
       * Verify that equals returns false if passed a different type of object.
       */
      it("must return false if compared to object of different type") {
        equalsFixture.valueSample.foreach {
          value =>
          equalsFixture.nonValueSample.foreach {
            that =>
            assert(!value.equals(that))
          }
        }
      }

      /*
       * Verify that each object compares as equal to itself.
       */
      it("must return true if compared to self (reflexive term)") {
        equalsFixture.valueSample.foreach {
          value =>
          assert(value.equals(value))
        }
      }

      /*
       * Verify that each object compares unequal to every other different object and vice versa.
       */
      it("must return false when compared to different value (and false when different value compared to this value)") {
        compare(equalsFixture.valueSample, false)
      }

      /*
       * Verify that each object compares true to every other equal object and vice versa.
       */
      it("must return true when compared to an equal value (and true when an equal value compared to this value)") {
        equalsFixture.equalListSample.foreach {
          compare(_, true)
        }
      }
    }

    /*
     * Test the hash code function, whose operation must be consistent with equals.
     */
    describe("hashCode") {

      /*
       * The hash codes of dissimilar values are not necessarily different (we can have the same hash code for two
       * different values purely by chance, although that should happen rarely). However, for the purposes of our test,
       * we ought to get a different hash code for every unique value we test. If this test should fail, down to pure
       * bad luck (two completely different values end up with the same hash code purely by chance), rather than bad
       * hash code implementation (two completely different values end up with the same hash code because, say, every
       * object is given the same hash code), then simply choose a different set of test values.
       *
       * If the set of unique hash codes is the same size as the list of test values, then we're doing OK.
       */
      it("must return a reasonable spread of hash codes for different values") {
        assert(equalsFixture.valueSample.map(_.hashCode).toSet.size === equalsFixture.valueSample.size)
      }

      /*
       * Every object that compares equal MUST have the same hash code.
       */
      it("must return same value as any object it is equal to") {
        equalsFixture.equalListSample.foreach(compareHash)
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc