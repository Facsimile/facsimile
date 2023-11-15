//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
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

import org.facsim.util.{Version, VersionParseException}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.util.Failure
import org.scalatest.funspec.AnyFunSpec

//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
/** Test harness for the [[Version]] class and object. */
final class VersionTest
extends AnyFunSpec
with ScalaCheckPropertyChecks
with CommonTestMethods {

  /** Compare versions and a specific number.
   *
   *  @param n1 First version number. May be minor, major or bugFix.
   *
   *  @param n2 Second version number. Should be same type of number as `n1`
   *
   *  @param v1 First version instance. Should relate to `v2` as `n1` relates to `n2`.
   *
   *  @param v2 Second version number. Should relate to `v1` as `n2` relates to `n1`.
   */
  def doCompare(n1: Int, n2: Int, v1: Version, v2: Version) = {

    // If n1 is less than n2, the versions should compare similarly.
    if(n1 < n2) {
      assert(v1.compare(v2) < 0)
      assert(v2.compare(v1) > 0)
      assert(v1 < v2)
      assert(v2 > v1)
    }

    // Otherwise, if n1 is greater than n2, then the versions should compare accordingly.
    else if(n1 > n2) {
      assert(v1.compare(v2) > 0)
      assert(v2.compare(v1) < 0)
      assert(v1 > v2)
      assert(v2 < v1)
    }

    // Otherwise, they're equal. Compare for equality and for equal hash codes too.
    else {
      assert(n1 === n2)
      assert(v1.compare(v2) === 0)
      assert(v2.compare(v1) === 0)
      assert(v1 === v2)
      assert(v2 === v1)
      assert(v1.hashCode === v2.hashCode)
    }
  }

  // Tell the user which element we're testing.
  describe(classOf[Version].getCanonicalName) {

    // Test the primary constructor. (Constructor methods called "this", but primary constructor is anonymous.)
    describe(".this(Int, Int, Option[Int], Boolean)") {

      // Verify that we get a valid default version.
      it("must return a version of '1.0' when called without arguments.") {
        val v = Version()
        assert(v.major === 1)
        assert(v.minor === 0)
        assert(v.bugFix === None)
        assert(v.isSnapshot === false)
      }

      // Test invalid major version numbers.
      it("must throw an InvalidArgumentException when passed negative major version numbers") {
        forAll(Generator.negInt) {n =>
          val e = intercept[IllegalArgumentException] {
            Version(major = n)
          }
          assertRequireValidMsg(e, "major", n)
        }
      }

      // Test valid major version numbers.
      it("must construct valid Versions when passed zero or positive major version numbers") {
        forAll(Generator.nonNegInt) {n =>
          val v = Version(major = n)
          assert(v.major === n)
        }
      }

      // Test invalid minor version numbers.
      it("must throw an InvalidArgumentException when passed negative minor version numbers") {
        forAll(Generator.negInt) {n =>
          val e = intercept[IllegalArgumentException] {
            Version(minor = n)
          }
          assertRequireValidMsg(e, "minor", n)
        }
      }

      // Test valid minor version numbers.
      it("must construct valid Versions when passed zero or positive minor version numbers") {
        forAll(Generator.nonNegInt) {n =>
          val v = Version(minor = n)
          assert(v.minor === n)
        }
      }

      // Test null bug fix numbers.
      it("must throw a NullPointerException when passed a null bug fix option") {
        val e = intercept[NullPointerException] {
          Version(bugFix = null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "bugFix")
      }

      // Test "None" as a bug fix number.
      it("must accept None as a bug fix number") {
        val x = Version(bugFix = None)
        assert(x.bugFix === None)
      }

      // Test invalid bug fix numbers.
      it("must throw an InvalidArgumentException when passed a wrapped negative bug fix number") {
        forAll(Generator.negInt) {n =>
          val e = intercept[IllegalArgumentException] {
            Version(bugFix = Some(n))
          }
          assertRequireValidMsg(e, "bugFix", Some(n))
        }
      }

      // Test valid bug fix numbers.
      it("must construct valid Versions when passed wrapped zero or positive bug fix numbers") {
        forAll(Generator.nonNegInt) {n =>
          val bf = Some(n)
          val v = Version(bugFix = bf)
          assert(v.bugFix === bf)
        }
      }

      // Test snapshot vs. release.
      it("must construct valid Versions for snapshots and releases") {
        forAll {s: Boolean =>
          val v = Version(isSnapshot = s)
          assert(v.isSnapshot === s)
        }
      }
    }

    // Compare method.
    describe(".compare(Version)") {

      // Test null version supplied.
      it("must throw a NullPointerException when passed a null Version") {
        val e = intercept[NullPointerException] {
          Version().compare(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "that")
      }

      // Test major version comparisons correctly. (In this case, the other numbers will always be the same, so the
      // differentiating factor will be the major version number.)
      it("must compare major version numbers correctly") {

        // Firstly check different values.
        forAll(Generator.nonNegInt, Generator.nonNegInt) {(m1, m2) =>
          whenever(m1 != m2) {
            val v1 = Version(major = m1)
            val v2 = Version(major = m2)
            doCompare(m1, m2, v1, v2)
          }
        }

        // Now check equal equal major version numbers. In this case, they should all compare equal and have identical
        // hash values.
        forAll(Generator.nonNegInt) {m =>
          val v1 = Version(major = m)
          val v2 = Version(major = m)
          doCompare(m, m, v1, v2)
        }
      }

      // Test minor version comparisons correctly. (In this case, the other numbers will always be the same, so the
      // differentiating factor will be the minor version number.)
      it("must compare minor version numbers correctly") {

        // Firstly check different values.
        forAll(Generator.nonNegInt, Generator.nonNegInt) {(m1, m2) =>
          whenever(m1 != m2) {
            val v1 = Version(minor = m1)
            val v2 = Version(minor = m2)
            doCompare(m1, m2, v1, v2)
          }
        }

        // Now check equal minor version numbers. In this case, they should also compare equal and have identical hash
        // values.
        forAll(Generator.nonNegInt) {m =>
          val v1 = Version(minor = m)
          val v2 = Version(minor = m)
          doCompare(m, m, v1, v2)
        }
      }

      // Test bug fix number comparisons correctly. (In this case, the other numbers will always be the same, so the
      // differentiating factor will be the bug fix number.)
      it("must compare bug fix numbers correctly") {

        // Firstly check different values.
        forAll(Generator.nonNegInt, Generator.nonNegInt) {(bf1, bf2) =>
          whenever(bf1 != bf2) {
            val v1 = Version(bugFix = Some(bf1))
            val v2 = Version(bugFix = Some(bf2))
            doCompare(bf1, bf2, v1, v2)
          }
        }

        // Check equal bug fix numbers. In this case, they should also compare equal and have identical hash values.
        // Also check that anything with a bug fix number is greater than the same major/minor version without one.
        val v0 = Version()
        forAll(Generator.nonNegInt) {bf =>
          val v1 = Version(bugFix = Some(bf))
          val v2 = Version(bugFix = Some(bf))
          doCompare(bf, bf, v1, v2)

          // Compare to a version without a bugfix number.
          assert(v1.compare(v0) > 0)
          assert(v0.compare(v1) < 0)
          assert(v1 > v0)
          assert(v0 < v1)
        }
      }

      // Check snapshot/version release comparisons correctly. (In this case, the other numbers will always be the same,
      // so the differentiating factor will be the snapshot flag.)
      it("must compare snapshot status correctly") {
        val vSnap = Version(isSnapshot = true)
        val vRel = Version() // Is a release by default
        assert(vSnap.compare(vSnap) === 0)
        assert(vSnap === vSnap)
        assert(vRel.compare(vRel) === 0)
        assert(vRel === vRel)
        assert(vSnap.compare(vRel) < 0)
        assert(vSnap < vRel)
        assert(vRel.compare(vSnap) > 0)
        assert(vRel > vSnap)
      }
    }

    // toString method.
    describe(".toString") {

      // Check that we get back what we put in.
      it("must correctly encode versions") {

        // Check major/minor version without bug fix, adding snapshot.
        forAll(Generator.nonNegInt, Generator.nonNegInt) {(maj, min) =>
          val vRel = Version(major = maj, minor = min)
          val vSnap = Version(major = maj, minor = min, isSnapshot = true)
          val root = s"$maj.$min"
          assert(vRel.toString === root)
          assert(vSnap.toString === s"$root-SNAPSHOT")
        }

        // Now add in bug fix numbers.
        forAll(Generator.nonNegInt, Generator.nonNegInt, Generator.nonNegInt) {(maj, min, bf) =>
          val vRel = Version(major = maj, minor = min, bugFix = Some(bf))
          val vSnap = Version(major = maj, minor = min, bugFix = Some(bf), isSnapshot = true)
          val root = s"$maj.$min.$bf"
          assert(vRel.toString === root)
          assert(vSnap.toString === s"$root-SNAPSHOT")
        }
      }
    }
  }

  // Name the companion object we're testing.
  describe(Version.getClass.getCanonicalName) {

    // Check the Factory method.
    describe(".apply(String)") {

      // Verify null string behavior.
      it("must throw a NullPointerException when passed a null string") {
        val e = intercept[NullPointerException] {
          Version(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "version")
      }

      // Verify that it doesn't accept strings that do not represent version numbers.
      it("must return a Failure(VersionParseException) if passed invalid version strings") {

        // List of as many distinctly different invalid version strings as we can think of.
        val invalid = List(
          "",
          ".",
          "..",
          ".0",
          ".0-",
          ".0-SNAPSHOT",
          ".0-snapshot",
          ".0-Snapshot",
          ".0-SnapShot",
          ".0-NONSENSE",
          ".0.",
          ".0.-",
          ".0.-SNAPSHOT",
          ".0.-snapshot",
          ".0.-Snapshot",
          ".0.-SnapShot",
          ".0.-NONSENSE",
          ".0.0-",
          ".0.0-SNAPSHOT",
          ".0.0-snapshot",
          ".0.0-Snapshot",
          ".0.0-SnapShot",
          ".0.0-NONSENSE",
          "1",
          "1.",
          "1..",
          "1.0-",
          "1.0-snapshot",
          "1.0-Snapshot",
          "1.0-SnapShot",
          "1.0-NONSENSE",
          "1..0-",
          "1..0-SNAPSHOT",
          "1..0-snapshot",
          "1..0-Snapshot",
          "1..0-SnapShot",
          "1..0-NONSENSE",
          "1.0.",
          "1.0.-",
          "1.0.-snapshot",
          "1.0.-Snapshot",
          "1.0.-SnapShot",
          "1.0.-NONSENSE",
          "1.0.0-",
          "1.0.0-snapshot",
          "1.0.0-Snapshot",
          "1.0.0-SnapShot",
          "1.0.0-NONSENSE",
          "1.0.0_",
          "1.0.1_1",
          "-1.0",
          "1.-1",
          "1.0.-1",
          "1-SNAPSHOT",
          "RUBBISH"
        )
        invalid.foreach{v =>
          assert(Version(v) === Failure(VersionParseException(v, 0)))
        }
      }

      // Verify that it accepts valid string versions (and reports the same back via toString).
      //
      // Note: Will not work if leading zero's are included in version strings. Known issue.
      it("must return valid versions from string representations") {

        // Check major/minor version without bug fix, adding snapshot.
        forAll(Generator.nonNegInt, Generator.nonNegInt) {(maj, min) =>

          // Without snapshot.
          val root = s"$maj.$min"
          val vRel = Version(root).get
          assert(vRel.major === maj)
          assert(vRel.minor === min)
          assert(vRel.bugFix.isEmpty)
          assert(!vRel.isSnapshot)
          assert(vRel.toString === root)

          val rootSnap = s"$root-SNAPSHOT"
          val vSnap = Version(rootSnap).get
          assert(vSnap.major === maj)
          assert(vSnap.minor === min)
          assert(vSnap.bugFix.isEmpty)
          assert(vSnap.isSnapshot)
          assert(vSnap.toString === rootSnap)
        }

        // Now add in bug fix numbers.
        forAll(Generator.nonNegInt, Generator.nonNegInt, Generator.nonNegInt) {(maj, min, bf) =>

          // Regular style.
          val root = s"$maj.$min.$bf"
          val vRel = Version(root).get
          assert(vRel.major === maj)
          assert(vRel.minor === min)
          assert(vRel.bugFix === Some(bf))
          assert(!vRel.isSnapshot)
          assert(vRel.toString === root)

          // Regular style with SNAPSHOT
          val rootSnap = s"$root-SNAPSHOT"
          val vSnap = Version(rootSnap).get
          assert(vSnap.major === maj)
          assert(vSnap.minor === min)
          assert(vSnap.bugFix === Some(bf))
          assert(vSnap.isSnapshot)
          assert(vSnap.toString === rootSnap)

          // Java style.
          val rootJava = s"$maj.$min.0_$bf"
          val vJavaRel = Version(rootJava).get
          assert(vJavaRel.major === maj)
          assert(vJavaRel.minor === min)
          assert(vJavaRel.bugFix === Some(bf))
          assert(!vJavaRel.isSnapshot)
          assert(vJavaRel.toString === root) // Not a mistake. Do not compare to rootJava - as it will differ.
        }
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on public.methods.have.type
//scalastyle:on scaladoc