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
// Scala source file belonging to the org.facsim.collection.immutable.test package.
//======================================================================================================================
package org.facsim.collection.immutable.test

import org.facsim.collection.immutable.BinomialHeap
import org.scalatest.FunSpec
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.annotation.tailrec

// Disable test-problematic Scalastyle checkers.
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers

/** Test harness for the [[org.facsim.collection.immutable.BinomialHeap]] class. */
final class BinomialHeapTest
extends FunSpec
with ScalaCheckPropertyChecks {

  /** Check that an empty heap responds as such.
   *
   *  @param h An empty heap which is to be verified.
   */
  private def verifyEmptyHeap(h: BinomialHeap[_]): Unit = {

    // Verify that the heap gives the correct replies for an empty heap.
    assert(h.isEmpty === true)
    assert(h.nonEmpty === false)
    assert(h.minimum.isEmpty === true) // Get scalatic exception on h.minimum === None
    assert(h.removeMinimum.isEmpty === true) // etc.
    assert(h.minimumRemove.isEmpty === true) // etc.
    () // Return unit to avoid "discarded non-unit value" compiler warning
  }

  /** Check that a heap with a single member responds as such.
   *
   *  @param h A heap with a single member which is to be verified.
   *
   *  @param a Value of the sole member of the heap.
   */
  private def verifyOneMemberHeap[A](h: BinomialHeap[A], a: A): Unit = {

    // Verify that the heap is not empty.
    assert(h.isEmpty === false)
    assert(h.nonEmpty === true)

    // Finding the minimum should result in the specified value, wrapped in Some.
    assert(h.minimum === Some(a))

    // Removing the minimum should result in an empty heap.
    val oh = h.removeMinimum
    assert(oh !== None)
    verifyEmptyHeap(oh.get)

    // Finding and removing the minimum should result in a tuple of the specified value and an empty heap.
    val omh = h.minimumRemove
    assert(omh !== None)
    val (min, eh) = omh.get
    assert(min === a)
    verifyEmptyHeap(eh)
  }

  /** Check that a heap with any number of members correctly sorts those members.
   *
   *  The heap must also contain the same number of elements, as well as have them in the same order.
   *
   *  @tparam A Type of value being stored in the heap.
   *
   *  @param h A heap to be verified.
   *
   *  @param la Unsorted list of elements stored in the heap.
   *
   *  @param ordering Ordering used for sorting values of type `A`.
   */
  private def verifyMultiMemberHeap[A](h: BinomialHeap[A], la: List[A])(implicit ordering: Ordering[A]): Unit = {

    // Helper function to check each value in turn.
    @tailrec
    def nextMinimum(rh: BinomialHeap[A], rla: List[A]): Unit = {

      // If the heap is empty, then the list must be too.
      if(rh.isEmpty) {
        assert(rla.isEmpty === true)
        verifyEmptyHeap(rh)
      }

      // Otherwise, it must have a minimum value. Check that this is the expected minimum value.
      else {

        // Firstly, check that we have a matching value in the sorted list.
        assert(rla.nonEmpty === true)

        // Check that the minimum value is as expected.
        val min = rh.minimum
        assert(min === Some(rla.head))

        // Retrieve the heap for the next iteration.
        val nextH = rh.removeMinimum.get

        // Get and remove the minimum, verifying the result matches the same information from previous sources.
        val pair = rh.minimumRemove
        assert(pair === Some((min.get, nextH)))

        // Perform the next iteration.
        nextMinimum(nextH, rla.tail)
      }
    }

    // Start the ball rolling, by sorting the expected contents.
    nextMinimum(h, la.sorted)
  }

  // Look at the companion element.
  describe(BinomialHeap.getClass.getCanonicalName) {

    // Create empty heap.
    describe(".empty[A]") {

      // Verify that an empty tree is empty.
      it("must create an empty heap") {
        verifyEmptyHeap(BinomialHeap.empty[Int])
      }
    }

    // Test the apply method..
    describe(".apply[A](A*)") {

      // Verify that it can handle empty argument lists.
      it("can create an empty heap") {
        verifyEmptyHeap(BinomialHeap[Int]())
      }

      // Verify that it can handle a single argument.
      it("can create a heap with one member") {
        forAll {i: Int =>
          verifyOneMemberHeap(BinomialHeap(i), i)
        }
      }

      // Verify that it can handle arbitrary numbers of elements.
      it("can create a heap with any number of members") {
        forAll {li: List[Int] =>
          verifyMultiMemberHeap(BinomialHeap(li: _*), li)
        }
      }
    }
  }

  // Now for the companion methods.
}

// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on magic.numbers
//scalastyle:on multiple.string.literals
//scalastyle:on public.methods.have.type
//scalastyle:on scaladoc