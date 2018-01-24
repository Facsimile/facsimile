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
// Scala source file belonging to the org.facsim.util.test package.
//======================================================================================================================
package org.facsim.util.test

import org.facsim.util.Memoize
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatest.FunSpec
import org.scalatest.prop.PropertyChecks

// Disable test-problematic Scalastyle checkers.
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers
//scalastyle:off scaladoc
/** Test harness for the [[Memoize]] classes. */
class MemoizeTest
extends FunSpec
with PropertyChecks {

  /** Test data. */
  trait TestData {

    /** Called flag.
     *
     *  This is set to `false` before a memoized function is called, and it set to `true` by the memoized function; as a
     *  side-effect, it should only be set by the call to the function, not when the function's cached value is
     *  recovered.
     */
    private final var wasCalled = false //scalastyle:ignore var.field

    /** Wrap a single-argument test function.
     *
     *  Sets the [[wasCalled]] flag and invokes the function, returning the result.
     *
     *  @tparam A Type of argument value in function being tested.
     *
     *  @tparam R Type of result obtained from function being tested.
     *
     *  @param f Function to be called.
     *
     *  @param a Argument value to be passed to the function.
     *
     *  @return Resutl of `f(a)`
     */
    final def wrapFn[A, R](f: A => R)(a: A): R = {
      wasCalled = true
      f(a)
    }

    /** Single-argument test function. Calculate hash of argument type.
     *
     *  @tparam A Type of argument value being hashed.
     *
     *  @param a Argument value to be hashed.
     *
     *  @return Hash value of argument.
     */
    final def getHash[A](a: A): Int = a.hashCode()

    /** Double-argument test function. Calculate hashes of argument types.
     *
     *  @tparam A1 Type of first argument value being hashed.
     *
     *  @tparam A2 Type of second argument value being hashed.
     *
     *  @param a1 First argument value to be hashed.
     *
     *  @param a2 Second argument value to be hashed.
     *
     *  @return Hash value of arguments.
     */
    final def getHash[A1, A2](a1: A1, a2: A2): Int = getHash(a1) ^ getHash(a2)

    /** Triple-argument test function. Calculate hashes of argument types.
     *
     *  @tparam A1 Type of first argument value being hashed.
     *
     *  @tparam A2 Type of second argument value being hashed.
     *
     *  @tparam A3 Type of third argument value being hashed.
     *
     *  @param a1 First argument value to be hashed.
     *
     *  @param a2 Second argument value to be hashed.
     *
     *  @param a3 Third argument value to be hashed.
     *
     *  @return Hash value of arguments.
     */
    final def getHash[A1, A2, A3](a1: A1, a2: A2, a3: A3): Int = getHash(a1, a2) ^ getHash(a3)

    /** Call a single-argument function. Verify that it is called the first time, but not subsequently.
     *
     *  @tparam A Type of argument value in function being tested.
     *
     *  @tparam R Type of result obtained from function being tested.
     *
     *  @param la List of argument value to be passed to `f`. The arguments in the list do not have to be unique.
     *
     *  @param f Single-argument function to be memoized and tested. Memoized results should match the un-memoized
     *  results.
     */
    final def callFn[A, R](la: List[A])(f: A => R): Unit = {

      // Ensure we have a unique list.
      val ula = la.toSet.toList

      // Create a wrapped function that both calls the original function while setting the wasCalled flag in the
      // process. (This is the function that we'll actually memoize.)
      val wf = wrapFn(f) _

      // Create a memoized version of the wrapped function.
      val mf = Memoize(wf)

      // For each value in the list of arguments, perform the following tests.
      ula.foreach {a =>

        // Get the result of calling the function with the argument. All other versions of this function must always
        // return this same value.
        val result = f(a)

        // Check that the wrapped function returns the right value and sets the wasCalled flag in the process.
        wasCalled = false
        assert(wf(a) === result, s"Wrapped version of function $f returned incorrect value")
        assert(wasCalled, s"Wrapped version of function $f did not set called flag")

        // Clear the called flag. Call the memoized function, checking the return value and that the original function
        // was called.
        wasCalled = false
        assert(mf(a) === result, s"Memoized version of function $f returned incorrect value on 1st call")
        assert(wasCalled, s"Memoized version of function $f did not set called flag on 1st call")

        // Clear the called flag once more. Call the memoized function, checking the return value and that the original
        // function was NOT called.
        wasCalled = false
        assert(mf(a) === result, s"Memoized version of function $f returned incorrect value on 2nd call")
        assert(!wasCalled, s"Memoized version of function $f set called flag on 2nd call")
      }

      // Just to be ultra-cautious, in case we're only caching the last parameter value seen, call the memoized version
      // once more, after all arguments have been previously cached.
      ula.foreach {a =>
        wasCalled = false
        assert(mf(a) === f(a), s"Memoized version of function $f returned incorrect value on 3rd call")
        assert(!wasCalled, s"Memoized version of function $f set called flag on 3rd call")
      }
    }

    /** Call the double-argument function. Verify that it is called the first time, but not subsequently.
     *
     *  @tparam A1 Type of the first argument value in function being tested.
     *
     *  @tparam A2 Type of the second argument value in function being tested.
     *
     *  @tparam R Type of result obtained from function being tested.
     *
     *  @param al List of argument value tuples to be passed to `f`.
     *
     *  @param f Double-argument function to be memoized and tested. Memoized results should match the un-memoized
     *  results.
     */
    final def callFn[A1, A2, R](al: List[(A1, A2)])(f: (A1, A2) => R): Unit = callFn[(A1, A2), R](al) {t =>
      f(t._1, t._2)
    }

    /** Call the triple-argument function. Verify that it is called the first time, but not subsequently.
     *
     *  @tparam A1 Type of the first argument value in function being tested.
     *
     *  @tparam A2 Type of the second argument value in function being tested.
     *
     *  @tparam A3 Type of the third argument value in function being tested.
     *
     *  @tparam R Type of result obtained from function being tested.
     *
     *  @param al List of argument values to be passed to `f`.
     *
     *  @param f Triple-argument function to be memoized and tested. Memoized results should match the un-memoized
     *  results.
     */
    final def callFn[A1, A2, A3, R](al: List[(A1, A2, A3)])(f: (A1, A2, A3) => R): Unit = {
      callFn[(A1, A2, A3), R](al) {t =>
        f(t._1, t._2, t._3)
      }
    }
  }

  // Tell user which class we're testing.
  describe(Memoize.getClass.getCanonicalName) {

    // Test single-argument function memoization.
    describe(".apply(A => R)") {

      // It must memoize single-argument functions correctly.
      it("must memoize single-argument functions correctly") {
        new TestData {

          // Try the integer hashing function first.
          forAll {il: List[Int] =>
            callFn(il)(getHash[Int])
          }

          // Now for strings.
          forAll {sl: List[String] =>
            callFn(sl)(getHash[String])
          }
        }
      }
    }

    // Test double argument function memoization.
    describe(".apply((A1, A2) => R") {

      // It must memoize double-argument functions correctly.
      it("must memoize double-argument functions correctly") {
        new TestData {

          // Generator to create tuples of integers and strings.
          val genISTuple = for {
            i <- arbitrary[Int]
            s <- arbitrary[String]
          } yield(i, s)

          // Generator to create a list of such tuples.
          val genISTupleList = Gen.nonEmptyListOf(genISTuple)

          // Try the integer & string hashing function first.
          forAll(genISTupleList) {istl =>
            callFn(istl)(getHash[Int, String])
          }

          // Generator to create tuples of strings and integers.
          val genSITuple = for {
            s <- arbitrary[String]
            i <- arbitrary[Int]
          } yield (s, i)

          // Generator to create a list of such tuples.
          val genSITupleList = Gen.nonEmptyListOf(genSITuple)

          // Now for strings with integers.
          forAll(genSITupleList) {sitl =>
            callFn(sitl)(getHash[String, Int])
          }
        }
      }
    }

    // Test triple argument function memoization.
    describe(".apply((A1, A2, A3) => R") {

      // It must memoize triple-argument functions correctly.
      it("must memoize triple-argument functions correctly") {
        new TestData {

          // Generator to create tuples of integers, strings and doubles.
          val genISDTuple = for {
            i <- arbitrary[Int]
            s <- arbitrary[String]
            d <- arbitrary[Double]
          } yield (i, s, d)

          // Generator to create a list of such tuples.
          val genISDTupleList = Gen.nonEmptyListOf(genISDTuple)

          // Try the integer, string & double hashing function first.
          forAll(genISDTupleList) {isdl =>
            callFn(isdl)(getHash[Int, String, Double])
          }

          // Generator to create tuples of strings, integers and doubles.
          val genSIDTuple = for {
            s <- arbitrary[String]
            i <- arbitrary[Int]
            d <- arbitrary[Double]
          } yield (s, i, d)

          // Generator to create a list of such tuples.
          val genSIDTupleList = Gen.nonEmptyListOf(genSIDTuple)

          // Try the integer, string & double hashing function first.
          forAll(genSIDTupleList) {sidl =>
            callFn(sidl)(getHash[String, Int, Double])
          }
        }
      }
    }
  }
}
// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on scaladoc
//scalastyle:on public.methods.have.type
//scalastyle:on multiple.string.literals
//scalastyle:on magic.numbers
