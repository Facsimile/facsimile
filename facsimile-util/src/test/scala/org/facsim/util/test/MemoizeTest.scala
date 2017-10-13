//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
// Copyright © 2004-2017, Michael J Allen.
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
import org.scalatest.FunSpec
import org.scalatest.prop.PropertyChecks

// Disable test-problematic Scalastyle checkers.
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers

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
     *  @param a Argument value to be passed to `f`.
     *
     *  @param f Single-argument function to be memoized and tested. Memoized results should match the un-memoized
     *  results.
     */
    final def callFn[A, R](a: A)(f: A => R): Unit = {

      // Get the result of calling the function with the argument. All other versions of this function must always
      // return this same value.
      val result = f(a)

      // Create a wrapped function that boths calls the original function while setting the wasCalled flag in the
      // process. (This is the function that we'll actually memoize.)
      val wf = wrapFn(f) _

      // Check that the wrapped function returns the right value and sets the wasCalled flag in the process.
      wasCalled = false
      assert(wf(a) === result, s"Wrapped version of function $f returned incorrect value")
      assert(wasCalled, s"Wrapped version of function $f did not set called flag")

      // Create a memoized version of the wrapped function.
      val mf = Memoize(wf)

      // Clear the called flag. Call the memoized function, checking the return value and that the original function was
      // called.
      wasCalled = false
      assert(mf(a) === result, s"Memoized version of function $f returned incorrect value on 1st call")
      assert(wasCalled, s"Memoized version of function $f did not set called flag on 1st call")

      // Clear the called flag once more. Call the memoized function, checking the return value and that the original
      // function was NOT called.
      wasCalled = false
      assert(mf(a) === result, s"Memoized version of function $f returned incorrect value on 2nd call")
      assert(!wasCalled, s"Memoized version of function $f set called flag on 2nd call")
    }

    /** Call the double-argument function. Verify that it is called the first time, but not subsequently.
     *
     *  @tparam A1 Type of the first argument value in function being tested.
     *
     *  @tparam A2 Type of the second argument value in function being tested.
     *
     *  @tparam R Type of result obtained from function being tested.
     *
     *  @param a1 First argument value to be passed to `f`.
     *
     *  @param a2 Second argument value to be passed to `f`.
     *
     *  @param f Double-argument function to be memoized and tested. Memoized results should match the un-memoized
     *  results.
     */
    final def callFn[A1, A2, R](a1: A1, a2: A2)(f: (A1, A2) => R): Unit = callFn[(A1, A2), R]((a1, a2)) {t =>
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
     *  @param a1 First argument value to be passed to `f`.
     *
     *  @param a2 Second argument value to be passed to `f`.
     *
     *  @param a3 Third argument value to be passed to `f`.
     *
     *  @param f Triple-argument function to be memoized and tested. Memoized results should match the un-memoized
     *  results.
     */
    final def callFn[A1, A2, A3, R](a1: A1, a2: A2, a3: A3)(f: (A1, A2, A3) => R): Unit = {
      callFn[(A1, A2, A3), R]((a1, a2, a3)) {t =>
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
          forAll {i: Int =>
            callFn(i)(getHash[Int])
          }

          // Now for strings.
          forAll {s: String =>
            callFn(s)(getHash[String])
          }
        }
      }
    }

    // Test double argument function memoization.
    describe(".apply((A1, A2) => R") {

      // It must memoize double-argument functions correctly.
      it("must memoize double-argument functions correctly") {
        new TestData {

          // Try the integer & string hashing function first.
          forAll {(i: Int, s: String) =>
            callFn(i, s)(getHash[Int, String])
          }

          // Now for strings with integers.
          forAll {(s: String, i: Int) =>
            callFn(s, i)(getHash[String, Int])
          }
        }
      }
    }

    // Test triple argument function memoization.
    describe(".apply((A1, A2, A3) => R") {

      // It must memoize triple-argument functions correctly.
      it("must memoize triple-argument functions correctly") {
        new TestData {

          // Try the integer, string & double hashing function first.
          forAll {(i: Int, s: String, d: Double) =>
            callFn(i, s, d)(getHash[Int, String, Double])
          }

          // Now for strings with integers and doubles.
          forAll {(s: String, i: Int, d: Double) =>
            callFn(s, i, d)(getHash[String, Int, Double])
          }
        }
      }
    }
  }
}
// Disable test-problematic Scalastyle checkers.
//scalastyle:on magic.numbers
//scalastyle:on multiple.string.literals
