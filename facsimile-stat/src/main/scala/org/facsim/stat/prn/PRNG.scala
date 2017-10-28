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
// Scala source file belonging to the org.facsim.stat.prn package.
//======================================================================================================================
package org.facsim.stat.prn

/** ''Pseudo-random number'' generator trait.
 *
 *  ''Pseudo-random'' numbers (''PRN''s) are streams of numbers that appear to have random properties, but are, in fact,
 *  completely deterministic. In order for simulation results to be repeatable, the sequence of generated numbers must
 *  be repeatable.
 *
 *  @tparam G Final type of the generator, which must be a subclass of `PRNG[G]`.
 *
 *  @since 0.0
 */
trait PRNG[G <: PRNG[G]] {

  /** Generate next random number in current stream.
   *
   *  @return A uniformly-distributed integer value in the range [Int.MinValue, Int.MaxValue], together with the next
   *  generator instance.
   *
   *  @since 0.0
   */
  def nextInt: (Int, G)
}

/** ''Pseudo-random number'' companion object.
 *
 *  @since 0.0
 */
object PRNG {

  /** ''State transition'' (a.k.a. ''state action'') for converting the state of a PRNG instance to a new instance.
   *
   *  This signature, for a function that takes a [[PRNG]] instance and uses it to generate a random value of some type
   *  `A`, returning that value and an updated generator instance as a tuple, forms the basis for a number of
   *  ''combinator'' methods that are defined below.
   *
   *  @tparam G Type of the generator, which must be derived from [[PRNG]].
   *
   *  @tparam A Type of value to be generated at random.
   *
   *  @since 0.0
   */
  type Rand[G <: PRNG[G], A] = G => (A, G)

  /** Next ''pseudo-random'' non-negative integer in the range [0, Int.MaxValue].
   *
   *  @tparam G Final type of the generator, which must be a subclass of [[PRNG]][G].
   *
   *  @param g Generator instance to be employed to sample the non-negative integer.
   *
   *  @return ''pseudo-random'' integer in the range [0, Int.MaxValue], together with the next instance of the
   *  generator.
   *
   *  @since 0.0
   */
  final def nextNonNegInt[G <: PRNG[G]](g: G): (Int, G) = {
    val (i, nextG) = g.nextInt
    (i & 0x7FFFFFFF, nextG)
  } ensuring (_._1 >= 0)

  /** Next ''pseudo-random'' probability value, in the range [0, 1).
   *
   *  @tparam G Final type of the generator, which must be a subclass of [[PRNG]][G].
   *
   *  @param g Generator instance to be employed to sample the probability value.
   *
   *  @return ''pseudo-random'' probability value, together with the next instance of the generator.
   *
   *  @since 0.0
   */
  final def nextProb[G <: PRNG[G]](g: G): (Probability, G) = {
    val (i, nextG) = nextNonNegInt(g)
    val p = Probability(i / (Int.MaxValue + 1.0))
    (p, nextG)
  }

  /** Return a pair of a random integer and probability.
   *
   *  @tparam G Final type of the generator, which must be a subclass of [[PRNG]][G].
   *
   *  @param g Generator instance to be employed to sample the integer and probability value.
   *
   *  @return Tuple of an integer and ''pseudo-random'' probability value, together with the next instance of the
   *  generator.
   *
   *  @since 0.0
   */
  final def nextIntProb[G <: PRNG[G]](g: G): ((Int, Probability), G) = {
    val (i, g1) = g.nextInt
    val (p, g2) = nextProb(g1)
    ((i, p), g2)
  }

  /** Return a pair of a random probability and integer.
   *
   *  @tparam G Final type of the generator, which must be a subclass of [[PRNG]][G].
   *
   *  @param g Generator instance to be employed to sample the integer and probability value.
   *
   *  @return Tuple of a ''pseudo-random'' probability value and an integer, together with the next instance of the
   *  generator.
   *
   *  @since 0.0
   */
  final def nextProbInt[G <: PRNG[G]](g: G): ((Probability, Int), G) = {
    val (p, g1) = nextProb(g)
    val (i, g2) = g1.nextInt
    ((p, i), g2)
  }
}