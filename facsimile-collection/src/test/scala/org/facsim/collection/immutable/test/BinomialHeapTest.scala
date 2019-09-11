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
import org.scalacheck.Gen
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
   *  @tparam A Type of element being stored in the heap.
   *
   *  @param h An empty heap which is to be verified.
   */
  private def verifyEmptyHeap[A](h: BinomialHeap[A]): Unit = {

    // Verify that the heap gives the correct replies for an empty heap.
    assert(h.isEmpty === true)
    assert(h.nonEmpty === false)
    val em: Option[A] = None
    assert(h.minimum === em)
    val eh: Option[BinomialHeap[A]] = None
    assert(h.removeMinimum === eh)
    val (em2, eh2) = h.minimumRemove
    assert(em2 === None)
    assert(eh2.isEmpty)
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
    val (min, eh) = omh
    assert(min === Some(a))
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

      // If the heap is empty, then the list must be too. Verify that the heap is empty.
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
        val (min2, nextH2) = rh.minimumRemove
        assert(min2 === min)
        assert(nextH2 === nextH)

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

  // Now for the class methods.
  describe(classOf[BinomialHeap[_]].getCanonicalName) {

    // Test the canEqual method.
    describe(".canEqual(Any)") {

      // Verify that it reports false for different types of object, including heaps of different types.
      it("must reject objects of a different type") {
        forAll {li: List[Int] =>
          val h = BinomialHeap(li: _*)
          assert(h.canEqual(li) === false, "Fails on List[Int] comparison")
          forAll {i: Int =>
            assert(h.canEqual(i) === false, "Fails on Int comparison")
          }
          forAll {d: Double =>
            assert(h.canEqual(d) === false, "Fails on Double comparison")
          }
          forAll {s: String =>
            assert(h.canEqual(s) === false, "Fails on String comparison")
          }
          forAll {ld: List[Double] =>
            val hd = BinomialHeap(ld: _*)
            assert(h.canEqual(hd) === false, "Fails on BinomialHeap[Double] comparison")
          }
        }
      }

      // Verify that it reports true for heaps of the same type.
      it("must accept heaps of the same type") {
        forAll {(l1: List[Int], l2: List[Int]) =>
          val h1 = BinomialHeap(l1: _*)
          val h2 = BinomialHeap(l2: _*)
          assert(h1.canEqual(h2))
        }
      }

      // Verify that it accepts itself.
      it("must accept itself") {
        forAll {li: List[Int] =>
          val h = BinomialHeap(li: _*)
          assert(h.canEqual(h) === true)
        }
      }
    }

    // Test the equals method.
    describe(".equals(Any)") {

      // Verify that it reports false for different types of object, including heaps of different types.
      it("must reject objects of a different type") {
        forAll {li: List[Int] =>
          val h = BinomialHeap(li: _*)
          assert(h.equals(li) === false, "Fails on List[Int] comparison")
          forAll {i: Int =>
            assert(h.equals(i) === false, "Fails on Int comparison")
          }
          forAll {d: Double =>
            assert(h.equals(d) === false, "Fails on Double comparison")
          }
          forAll {s: String =>
            assert(h.equals(s) === false, "Fails on String comparison")
          }
          forAll {ld: List[Double] =>
            val hd = BinomialHeap(ld: _*)
            assert(h.equals(hd) === false, "Fails on BinomialHeap[Double] comparison")
          }
        }
      }

      // Verify that it reports the correct result for heaps of the same type.
      it("must compare heaps of the same type correctly") {
        forAll {(l1: List[Int], l2: List[Int]) =>
          val l1s = l1.sorted
          val l2s = l2.sorted
          val h1 = BinomialHeap(l1: _*)
          val h2 = BinomialHeap(l2: _*)
          assert(h1.equals(h2) === l1s.equals(l2s))
        }
      }

      // Verify that it compares equal to itself.
      it("must equal itself") {
        forAll {li: List[Int] =>
          val h = BinomialHeap(li: _*)
          assert(h.equals(h) === true)
        }
      }
    }

    // Test the hashcode method.
    describe(".hashCode") {

      // Verify that it reports the same value for heaps that should compare equal.
      it("must return the same value for heaps that compare equal") {
        forAll {li: List[Int] =>
          val h1 = BinomialHeap(li: _*)
          val h2 = BinomialHeap(li.reverse: _*)
          assert(h1 === h2)
          assert(h1.hashCode === h2.hashCode)
        }
      }

      // Verify that it reports reasonably unique values for each heap. This may fail due to pure chance, but it's
      // highly unlikely if the hash function is any good.
      it("must return reasonably unique values") {
        def updateState(hashCodes: Set[Int], count: Int, heap: BinomialHeap[Int]): (Set[Int], Int) = {
          (hashCodes + heap.hashCode, count + 1)
        }
        var state = (Set.empty[Int], 0) //scalastyle:ignore var.field
        forAll {li: List[Int] =>
          val h = BinomialHeap(li: _*)
          state = updateState(state._1, state._2, h)
        }
        assert(state._1.size / state._2.toDouble >= 0.9)
      }
    }

    // Test the member addition operator.
    describe(".+(A)") {

      // It must add new member to a heap, resulting in a new heap.
      it("must accept a new member, resulting in a new heap") {
        forAll {(e: Int, li: List[Int]) =>
          val h = BinomialHeap(li: _*)
          val newH = h + e
          assert(h !== newH)
          verifyMultiMemberHeap(newH, (e :: li).sorted)
        }
      }
    }

    // Test the heap merge operator.
    describe(".++(BinomialHeap[A])") {

      // One of the heaps is empty.
      it("must handle empty heaps correctly") {

        // Use positive numbers, as there is no generator for just numbers, right now.
        forAll(Gen.nonEmptyListOf(Gen.posNum[Int])) {li =>
          val h = BinomialHeap(li: _*)
          val eh = BinomialHeap.empty[Int]
          val newH1 = h ++ eh
          assert(newH1 === h)
          val newH2 = eh ++ h
          assert(newH2 === h)
          val newH3 = eh ++ eh
          assert(newH3 === eh)
        }
      }

      // It must create a new heap out of the initial two heaps.
      it("must create a new heap from the initial two heaps") {

        // Use positive numbers, as there is no generator for just numbers, right now.
        forAll(Gen.nonEmptyListOf(Gen.posNum[Int]), Gen.nonEmptyListOf(Gen.posNum[Int])) {(l1, l2) =>
          val h1 = BinomialHeap(l1: _*)
          val h2 = BinomialHeap(l2: _*)
          val newH = h1 ++ h2
          val newL = l1 ::: l2
          verifyMultiMemberHeap(newH, newL.sorted)
        }
      }
    }
  }
}

// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on magic.numbers
//scalastyle:on multiple.string.literals
//scalastyle:on public.methods.have.type
//scalastyle:on scaladoc