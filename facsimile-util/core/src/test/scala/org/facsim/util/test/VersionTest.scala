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

import org.facsim.util.Version
import org.scalatest.FunSpec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/**
 * Test harness for the [[Version]] class and object.
 */
class VersionTest
extends FunSpec
with CommonTestMethods {

  /*
   * Name the class we're testing.
   */
  describe(classOf[Version].getCanonicalName) {

    /*
     * Application tests.
     */
    describe("Version.apply(String)") {
      it("must throw a NullPointerException if passed a null string") {
        val e = intercept[NullPointerException] {
          Version(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "version")
      }
      it("must return a version if passed a valid version string") {
        val v = Version("1.0")
        assert(v ne null) //scalastyle:ignore null
      }
    }

    /*
     * Conversion to strings.
     */
    describe(".toString") {
      it("must return the same version string as originally applied") {
        assert(Version("1.2").toString === "1.2")
        assert(Version("1.2-3").toString === "1.2-3")
        assert(Version("1.2.3-4").toString === "1.2.3-4")
        assert(Version("1.2-RC-3").toString === "1.2-RC-3")
        assert(Version("1.2-BETA-3-SNAPSHOT").toString === "1.2-BETA-3-SNAPSHOT")
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc