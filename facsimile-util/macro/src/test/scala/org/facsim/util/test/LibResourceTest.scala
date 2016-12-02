/*
 * Facsimile -- A Discrete-Event Simulation Library
 * Copyright © 2004-2016, Michael J Allen.
 *
 * This file is part of Facsimile.
 *
 * Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or(at your option) any later
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
 * IMPORTANT NOTE: All patches(modifications to existing files and/or the addition of new files) submitted for
 * inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
 * your code fails to comply with the standard, then your patches will be rejected. For further information, please
 * visit the coding standards at:
 *
 *   http://facsim.org/Documentation/CodingStandards/
 * =====================================================================================================================
 * Scala source file belonging to the org.facsim.util.test package.
 */
package org.facsim.util.test

import java.util.{GregorianCalendar, Locale, MissingResourceException}
import org.facsim.util.LibResource
import org.scalatest.FunSpec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/**
 * Test harness for the [[LibResource]] object.
 *
 * Although `LibResource` is a very basic implementation of [[org.facsim.util.Resource]], it was producing some very
 * different results, hence this specific test fixture, based upon the `ResourceTest` test fixture.
 */
class LibResourceTest
extends FunSpec
with CommonTestMethods {

  /**
   * Some commonly used test strings.
   */
  trait testResources {
    val compoundResource = Array("testCompoundResource0", "testCompoundResource1", "testCompoundResource2")
    val dateResource = "testDateResource"
    val helloResource = "testHelloResource"
    val realResource = "testRealResource"
    val singleResource = "testSingleResource"
  }

  /*
   * Name the class we're testing.
   */
  describe(LibResource.getClass.getCanonicalName) {

    /*
     * Test string resource formatting.
     */
    describe("apply(String, Any*)") {
      it("must throw NullPointerException when key is null") {
        new testResources {
          val e = intercept[NullPointerException] {
            LibResource.apply(null) //scalastyle:ignore null
          }
          assertRequireNonNullMsg(e, "key")
        }
      }
      it("must throw MissingResourceException when key undefined") {
        new testResources {
          intercept[MissingResourceException] {
            LibResource.apply("UNDEFINED_KEY")
          }
        }
      }
      ignore("must throw ClassCastException when key identifies non-string resource") {
        new testResources {
          // Cannot define non-string resources currently - test ignored
          intercept[ClassCastException] {
            LibResource.apply("testNonStringResource")
          }
        }
      }
      ignore("must throw IllegalArgumentException when argument passed to non-compound resource") {
        new testResources {
          // Java doesn't throw an exception in this case - test ignored.
          intercept[IllegalArgumentException] {
            LibResource.apply(singleResource, "Invalid extra argument")
          }
        }
      }
      ignore("must throw IllegalArgumentException when no argument passed to compound resource") {
        new testResources {
          // Java doesn't throw an exception in this case - test ignored.
          intercept[IllegalArgumentException] {
            LibResource.apply(compoundResource(0))
          }
        }
      }
      ignore("must throw IllegalArgumentException when extra arguments passed to compound resource") {
        new testResources {
          // Java doesn't throw an exception in this case - test ignored.
          intercept[IllegalArgumentException] {
            LibResource.apply(compoundResource(0), "Valid argument", "Invalid extra argument")
          }
        }
      }
      ignore("must throw IllegalArgumentException when insufficient arguments passed to compound resource") {
        new testResources {
          // Java doesn't throw an exception in this case - test ignored.
          intercept[IllegalArgumentException] {
            LibResource.apply(compoundResource(1), "Valid argument")
          }
        }
      }
      it("must throw IllegalArgumentException when string passed to date resource") {
        new testResources {
          intercept[IllegalArgumentException] {
            LibResource.apply(dateResource, "Bob")
          }
        }
      }
      it("must throw IllegalArgumentException when string passed to real resource") {
        new testResources {
          intercept[IllegalArgumentException] {
            LibResource.apply(realResource, "Fred")
          }
        }
      }
      it("must retrieve a non-compound resource with no arguments OK") {
        new testResources {
          assert(LibResource.apply(singleResource) === "Test non-compound resource")
        }
      }
      it("must retrieve compound resources with appropriate arguments OK") {
        new testResources {
          assert(LibResource.apply(compoundResource(0), "zero") === "Test compound resource 0: 0=zero")
          assert(LibResource.apply(compoundResource(1), "zero", "one") === "Test compound resource 1: 0=zero, 1=one")
          assert(LibResource.apply(compoundResource(2), "zero", "one", "two") ===
          "Test compound resource 2: 0=zero, 1=one, 2=two")
        }
      }
      it("must parse choice resources OK") {
        new testResources {
          val choiceResource = "testChoiceResource"
          assert(LibResource.apply(choiceResource, 0) === "On your marks...")
          assert(LibResource.apply(choiceResource, 1) === "Get set...")
          assert(LibResource.apply(choiceResource, 2) === "Go!")
        }
      }
      ignore("must retrieve localized string resources OK") {
        new testResources {
          val default = Locale.getDefault()
          // For some reason, maybe because the resource bundle loaded is always the same, the US English resource is
          // always returned here. Although the other locale-specific tests in this file seem to work OK.
          //
          // Also, so long as these tests pass in the base class's test suite, then we're probably OK anyway.
          //
          // A better solution would be be run the test suite for multiple locale's to verify the data returned.
          try {
            // Check that we get the correct en_US response.
            Locale.setDefault(Locale.US)
            assert(LibResource.apply(helloResource) === "Howdy!")
            // Should get a different result for Brits...
            Locale.setDefault(Locale.UK)
            assert(LibResource.apply(helloResource) === "Wotcha!")
            // Germans should have a good day...
            Locale.setDefault(Locale.GERMANY)
            assert(LibResource.apply(helloResource) === "Guten Tag!")
            // ...and so should the French...
            Locale.setDefault(Locale.FRANCE)
            assert(LibResource.apply(helloResource) === "Bonjour!")
            // ...and anyone who speaks Spanish...
            Locale.setDefault(new Locale("es"))
            assert(LibResource.apply(helloResource) === "¡Hola!")
          }
          // Restore original default.
          finally {
            Locale.setDefault(default)
          }
        }
      }
      it("must retrieve numeric resources OK") {
        new testResources {
          val default = Locale.getDefault()
          val number = 1234.56
          try {
            // Check that we get the correct en_US response.
            Locale.setDefault(Locale.US)
            assert(LibResource.apply(realResource, number) === "1,234.56")
            // Germans do things differently...
            Locale.setDefault(Locale.GERMANY)
            assert(LibResource.apply(realResource, number) === "1.234,56")
          }
          // Restore original default.
          finally {
            Locale.setDefault(default)
          }
        }
      }
      it("must retrieve localized date resources OK") {
        new testResources {
          val default = Locale.getDefault()
          // Oct 14, 2010 - months are zero-based numbers(!)
          val date = new GregorianCalendar(2010, 9, 14).getTime //scalastyle:ignore magic.number
          try {
            // Check that we get the correct en_US response.
            Locale.setDefault(Locale.US)
            assert(LibResource.apply(dateResource, date) === "10/14/10")
            // Month & day are in different order in the UK
            Locale.setDefault(Locale.UK)
            assert(LibResource.apply(dateResource, date) === "14/10/10")
            // Germans use same order as UK, but different separator...
            Locale.setDefault(Locale.GERMANY)
            assert(LibResource.apply(dateResource, date)  === "14.10.10")
          }
          // Restore original default.
          finally {
            Locale.setDefault(default)
          }
        }
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc