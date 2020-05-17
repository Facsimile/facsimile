//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
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
// Scala source file belonging to the org.facsim.collection.immutable package.
//======================================================================================================================
package org.facsim.collection.immutable

import scala.annotation.tailrec
import scala.reflect.runtime.universe._

/** Immutable ''[[https://en.wikipedia.org/wiki/Binomial_heap binomial heap]]'' container.
 *
 *  This is based upon the heap interface defined in the paper ''[[http://www.brics.dk/RS/96/37/BRICS-RS-96-37.pdf
 *  Optimal Purely Functional Priority Queues]]''.
 *
 *  @constructor Construct a new immutable binomial heap container, with no elements.
 *
 *  @tparam A Type of element to be stored in the heap. There must be an implicit ordering available for this element
 *  type.
 *
 *  @param rootTree Root binomial tree.
 *
 *  @param ordering Ordering allowing elements of type `A` to be compared.
 *
 *  @param typeTag Actual type of `A`.
 *
 *  @see ''[[https://en.wikipedia.org/wiki/Binomial_heap Binomial heap on Wikipedia]]''.
 *
 *  @see ''[[http://www.brics.dk/RS/96/37/BRICS-RS-96-37.pdf Optimal Purely Functional Priority Queues (PDF file)]]''.
 *
 *  @since 0.0
 */
final class BinomialHeap[A] private(private val rootTree: BinomialTree[A])(implicit private val ordering: Ordering[A],
implicit private val typeTag: TypeTag[A])
extends Heap[A, BinomialHeap[A]] {

  /** Cached minimum value and heap remainder.
   *
   *  This value is lazily computed only when both the minimum and the value of the heap after the minimum has been
   *  removed.
   */
  private lazy val cachedMinimumRemove: (Option[A], BinomialHeap[A]) = minRemove(rootTree) match {
    case(mo, h) => mo.fold[(Option[A], BinomialHeap[A])]((None, this))(m => (Some(m), new BinomialHeap(h)))
  }

  /** @inheritdoc
   */
  override def isEmpty: Boolean = rootTree.isEmpty

  /** @inheritdoc
   *
   *  @note Heap insertion has ''amortized'' ''O(1)'' time.
   */
  override def +(a: A): BinomialHeap[A] = new BinomialHeap(insert(BinomialTreeNode(a, 0, Nil), rootTree))

  /** @inheritdoc
   *
   *  @note Heap merges have ''O(''log'' n)'' time.
   *
   *  @todo Handle different element ordering in a better manner.
   */
  override def ++(h: BinomialHeap[A]): BinomialHeap[A] = {
    require(h.ordering eq ordering, s"Heap ${h} does not have the same element ordering as this heap (${this})")
    new BinomialHeap(meld(rootTree, h.rootTree))
  }

  /** @inheritdoc
   *
   *  @note Identifying the heap minimum value takes ''Θ(''log'' n)'' time.
   */
  override def minimum: Option[A] = findMin(rootTree)

  /** @inheritdoc
   *
   *  @note Removal of the minimum element takes ''Θ(''log'' n)'' time.
   */
  override def minimumRemove: (Option[A], BinomialHeap[A]) = cachedMinimumRemove

  /** Link two nodes together.
   *
   *  @param n1 First node to be linked.
   *
   *  @param n2 Second node to be linked.
   *
   *  @return New node containing the linked nodes.
   */
  private def link(n1: BinomialTreeNode[A], n2: BinomialTreeNode[A]): BinomialTreeNode[A] = {

    // If node 1 compares as lower or equal to node 2, add node 2 as a child of node 1, incrementing node 1's rank.
    if(ordering.lteq(n1.root, n2.root)) n1.copy(rank = n1.rank + 1, children = n2 :: n1.children)

    // Otherwise (node 1 compares as having a higher value than node 2), add node 1 as a child of node 2, incrementing
    // node 2's rank.
    else n2.copy(rank = n2.rank + 1, children = n1 :: n2.children)
  }

  /** Insert a node into a tree.
   *
   *  @param n Node to be inserted.
   *
   *  @param t Tree into which node is to be inserted.
   *
   *  @return New tree containing node and previous tree content.
   */
  @tailrec
  private def insert(n: BinomialTreeNode[A], t: BinomialTree[A]): BinomialTree[A] = t match {

    // If the tree is empty, just create a new tree with this node as a member.
    case Nil => List(n)

    // Otherwise, there are already nodes in the tree.
    case nh :: tt => {

      // If the new node has a lower rank that the node that is currently at the head of the tree, then prepend the new
      // node to the existing tree.
      if(n.rank < nh.rank) n :: nh :: tt

      // Otherwise, link the new node and the head node, and insert the resulting node into the tail of the existing
      // tree.
      else insert(link(n, nh), tt)
    }
  }

  /** Meld two binary trees together to form a single tree with both sets of values.
   *
   *  @param t1 First tree to be merged.
   *
   *  @param t2 Second tree to be merged.
   *
   *  @return New tree containing merged result.
   */
  // TODO: Make this function stack safe.
  private def meld(t1: BinomialTree[A], t2: BinomialTree[A]): BinomialTree[A] = (t1, t2) match {

    // If the first tree is empty, return the second.
    case (Nil, t) => t

    // If the second tree is empty, return the first.
    case(t, Nil) => t

    // Otherwise, we need to perform a merge.
    case (n1 :: t1t, n2 :: t2t) => {

      // If the first tree's head node has a lower rank than that of the the second, then prepend the first tree's node
      // to the meld of the first tree's tail and the second tree.
      if(n1.rank < n2.rank) n1 :: meld(t1t, t2)

      // Otherwise, if the second tree's head node has a lower rank than that of the first, the prepend the second
      // tree's head element to the meld of the second tree's trail and the first tree.
      else if(n2.rank < n1.rank) n2 :: meld(t2t, t1)

      // Otherwise, the two head nodes have equal ranks, so link them and insert the resulting node to the meld of the
      // two tails.
      else insert(link(n1, n2), meld(t1t, t2t))
    }
  }

  /** Find the element that the ordering evaluates as having the minimum value in the tree.
   *
   *  @param t Tree for which a minimum value is sought.
   *
   *  @return Minimum element in the tree, wrapped in `[[scala.Some Some]]`; `[[scala.None None]]` if `t` is empty.
   */
  // TODO: Make this function stack safe.
  private def findMin(t: BinomialTree[A]): Option[A] = t match {

    // If the tree is empty, return None.
    case Nil => None

    // If we have multiple nodes, find the minimum of the tail's minimum and the head node.
    case n :: ts => findMin(ts).map {x =>

      // If the head node compares less than the tail's minimum, then take the head node's value.
      if(ordering.lteq(n.root, x)) n.root

      // Otherwise, take the tail's minimum value.
      else x
    }

    // Otherwise, if the tail was empty, just take the head node's value.
    .orElse(Some(n.root))
  }

  /** Find the element that the ordering evaluates as having the minimum value in the tree, and remove it, returning a
   *  new tree.
   *
   *  @param t Tree for which a minimum value is sought.
   *
   *  @return Tuple whose first member is the minimum element, wrapped in `[[scala.Some Some]]`, or `[[scala.None
   *  None]]` if `t` is empty; the second member of the tuple is the new true with the minimum removed (if the first
   *  element is defined), or the original empty tree otherwise.
   */
  // TODO: Make this function stack-safe.
  private def minRemove(t: BinomialTree[A]): (Option[A], BinomialTree[A]) = t match {

    // If the tree is empty, we cannot remove an element from it, so return None and the empty tree.
    case Nil => (None, t)

    // Otherwise, identify the head node and the tail of this tree.
    case n :: tt => {

      // Helper function to identify and remove the minimum node, returning the removed node and replacement tree.
      def getMin(minN: BinomialTreeNode[A], rem: BinomialTree[A]): (BinomialTreeNode[A], BinomialTree[A]) = rem match {

        // If there are no remaining nodes in the tree, the current minimum must be the minimum element. Return the
        // original empty tree.
        case Nil => (minN, rem)

        // Otherwise, we have at least one other node to consider.
        case rn :: rt => {

          // Find the minimum node among the remaining nodes.
          val (minRem, tailRem) = getMin(rn, rt)

          // If the current minimum is less than the minimum of the remaining nodes, then remove the current minimum
          // by returning the entire tail as the new tree.
          if(ordering.lteq(minN.root, minRem.root)) (minN, rem)

          // Otherwise, remove the minimum node from the tail and prepend the previous minimum to the tail's
          // remainder.
          else (minRem, minN :: tailRem)
        }
      }

      // Extract the value and children of the minimum node and meld the latter with the reminder of the returned tree.
      val (BinomialTreeNode(min, _, c), ttRem) = getMin(n, tt)
      (Some(min), meld(c.reverse, ttRem))
    }
  }

  /** Determine whether it makes sense to compare this heap to the specified object.
   *
   *  - The other object is a heap of the same type.
   *  - The contents of the two heaps are identical.
   *
   *  @param that Object being compared to this heap for equality.
   *
   *  @return `true` if `that` is a heap of the same type as this heap; `false` in all other cases (if `that` is not a
   *  heap, or if `that` is a heap of a different type).
   *
   *  @since 0.0
   */
  override def canEqual(that: Any): Boolean = that match {

    // If that is a heap of type H (i.e. the same type as this heap), then we can compare the two for equality.
    //
    // NOTE: This requires a direct comparison of the types of the two heaps, which we obtain through reflection.
    case other: BinomialHeap[A] => typeTag.tpe =:= other.typeTag.tpe

    // If that is anything else (a different type of heap, or a different object altogether), then we cannot compare for
    // equality.
    case _ => false
  }

  /** Determine whether this object is equal to the other object.
   *
   *  This heap equals the other object if, and only if:
   *  - The other object is a heap of the same type.
   *  - The contents of the two heaps are identical.
   *
   *  @param that Object being compared to this heap for equality.
   *
   *  @return `true` if `that` is a heap of the same type as this heap and contains the same elements (the structures of
   *  the two heaps, however, may differ); `false` in all other cases (if `that` is not a heap, if `that` is a heap  of
   *  a different type, or if the elements in the two heaps differ).
   *
   *  @since 0.0
   */
  @tailrec
  override def equals(that: Any): Boolean = that match {

    // Is that a heap of the same type?
    case other: BinomialHeap[A] => {
      if(!other.canEqual(this)) false
      else {

        // Remove the minimums from each heap and compare them.
        val (tm, tr) = cachedMinimumRemove
        val (om, or) = other.cachedMinimumRemove
        tm match {

          // If this heap is empty, then the other heap should be empty too.
          case None => om.isEmpty

          // Otherwise, the other heap cannot be empty, the two minimums should compare equal and the two tails should
          // compare equal as well.
          case _ => {

            // If the minimums are not equal, including the case that the other heap is empty, then they're not equal.
            if(tm != om) false

            // Otherwise, compare the heap remainders.
            else tr.equals(or)
          }
        }
      }
    }

    // If that is a different type of object, then it cannot be equal to this heap.
    case _ => false
  }

  /** Create a hash code based upon the minimum value of this heap and its remainder heap.
   *
   *  @note If two heaps compare equal, then their hash codes must be identical too; however, two heaps with identical
   *  hash codes are not necessarily equal.
   *
   *  @return Hash code based upon the contents of this heap.
   */
  override def hashCode: Int = {

    // If this heap is empty, return the hash code of the value None.
    if(isEmpty) None.hashCode()

    // Otherwise, take the hash code of the minimum value and XOR it with the hash code of the remaining elements.
    else {
      val (min, rem) = cachedMinimumRemove
      min.hashCode() ^ rem.hashCode
    }
  }
}

/** BinomialHeap companion.
 *
 *  @since 0.0
 */
object BinomialHeap {

  /** Create a new, empty heap.
   *
   *  @tparam A Type of element to be stored in the heap. There must be an implicit ordering available for this type.
   *
   *  @param ordering Ordering allowing elements of type `A` to be compared.
   *
   *  @return Empty heap.
   *
   *  @since 0.0
   */
  def empty[A: TypeTag](implicit ordering: Ordering[A]): BinomialHeap[A] = new BinomialHeap[A](Nil)

  /** Create a new heap containing the specified elements.
   *
   *  @tparam A Type of element to be stored in the heap. There must be an implicit ordering available for this type.
   *
   *  @param as Initial elements to be added to the heap.
   *
   *  @param ordering Ordering allowing elements of type `A` to be compared.
   *
   *  @return New heap containing the specified elements.
   *
   *  @since 0.0
   */
  def apply[A: TypeTag](as: A*)(implicit ordering: Ordering[A]): BinomialHeap[A] = {
    as.foldLeft(empty[A])((h, a) => h + a)
  }
}
