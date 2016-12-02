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

import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import org.facsim.util.LibResource
import org.facsim.dummy.Dummy
import org.facsim.invalid.Invalid
import org.facsim.missing.Missing
import org.facsim.util.{Manifest, jarFile, resourceUrl}
import org.scalatest.FunSpec
import scala.util.Properties

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/**
 * Test harness for the [[Manifest]] trait.
 */
class ManifestTest
extends FunSpec
with CommonTestMethods {

  /**
   * Manifest test data.
   */
  trait TestData {
    lazy val javaManifest = Manifest(classOf[String])
    lazy val invalidManifest = Manifest(classOf[Invalid])
    lazy val dummyManifest = Manifest(classOf[Dummy])
  }

  /**
   * Dummy class that will not be loaded from a JAR file, and can thus be tested as such.
   */
  class NonJARFileClass

  /**
   * Verify that missing attribute values are described by NoSuchElementExceptions.
   */
  def assertNoSuchAttribute(e: NoSuchElementException, name: String) = {
    assert(e.getMessage === LibResource("Manifest.NoSuchElement.Attribute", name))
  }

  /*
   * Name the companion object we're testing.
   */
  describe(Manifest.getClass.getCanonicalName) {

    /*
     * Test the factory method.
     */
    describe(".apply(Class[T])") {

      /*
       * Verify that looking up the manifest for a null class reference fails with a NullPointerException.
       */
      it("must throw a NullPointerException if passed null class reference") {
        val e = intercept[NullPointerException] {
          Manifest(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "elementType")
      }

      /*
       * Verify that looking up the manifest for a primitive class reference (which do not have associated resources)
       * fails with a NoSuchElementException.
       *
       * Primitive types typically have no associated resources, because they are not implemented in library code.
       */
      it("must throw a NoSuchElementException if passed a class reference having no associated resource") {
        val c = classOf[Double]
        val e = intercept[NoSuchElementException] {
          Manifest(c)
        }
        assert(e.getMessage === LibResource("resourceUrl.NoSuchElement", c.getName))
      }

      /*
       * Verify that looking up the manifest for a class that was not loaded from a JAR file fails with a
       * NoSuchElementException.
       *
       * Test classes are loaded from .class files as-is, without being assembled into JAR files. Consequently, we can
       * use a reference to a locally-defined class here.
       */
      it("must throw a NoSuchElementException if passed a non-JAR file class reference") {
        val c = classOf[NonJARFileClass]
        val e = intercept[NoSuchElementException] {
          Manifest(c)
        }
        val url = resourceUrl(c)
        assert(e.getMessage === LibResource("jarFile.NoSuchElement", url.toString))
      }

      /*
       * Verify that looking up the manifest for a JAR file class reference that has no manifest information fails with
       * a NoSuchElementException.
       */
      it("must throw a NoSuchElementException if passed a JAR file class reference that has no manifest") {
        val c = classOf[Missing]
        val e = intercept[NoSuchElementException] {
          Manifest(c)
        }
        val url = resourceUrl(c)
        val jar = jarFile(url)
        assert(e.getMessage === LibResource("Manifest.NoSuchElement.Missing", c.getName, jar.getName))
      }

      /*
       * Verify that we can construct our test data objects without a problem.
       */
      it("must retrieve manifest data for valid JAR file class references") {
        new TestData {
          //scalastyle:off null
          assert(javaManifest ne null)
          assert(invalidManifest ne null)
          assert(dummyManifest ne null)
          //scalastyle:on null
        }
      }
    }
  }

  /*
   * Now name the class we're testing.
   */
  describe(classOf[Manifest].getCanonicalName) {

    /*
     * Test the getAttribute method.
     */
    describe(".getAttribute(String)") {

      /*
       * Verify that a NullPointerException is thrown if null is passed as the attribute name.
       */
      it("must throw a NullPointerException if passed a null attribute name") {
        new TestData {
          val e = intercept[NullPointerException] {
            javaManifest.getAttribute(null) // scalastyle:ignore null
          }
          assertRequireNonNullMsg(e, "name")
        }
      }

      /*
       * It must throw an IllegalArgumentException if passed an empty string.
       *
       * Note: There is no need to test the message returned, as it is generated from inside the Java runtime.
       */
      it("must throw an IllegalArgumentException if passed an empty string") {
        new TestData {
          intercept[IllegalArgumentException] {
            javaManifest.getAttribute("")
          }
        }
      }

      /*
       * It must throw an IllegalArgumentException if passed a space.
       *
       * Note: There is no need to test the message returned, as it is generated from inside the Java runtime.
       */
      it("must throw an IllegalArgumentException if passed a space") {
        new TestData {
          intercept[IllegalArgumentException] {
            javaManifest.getAttribute(" ")
          }
        }
      }

      /*
       * Verify that a NoSuchElementException is thrown if asked to retrieve a non-existent attribute.
       */
      it("must throw a NoSuchElementException if passed a non-existent attribute name") {
        val noneSuch = "NoSuchAttribute"
        new TestData {
          val e = intercept[NoSuchElementException] {
            javaManifest.getAttribute(noneSuch)
          }
          assertNoSuchAttribute(e, noneSuch)
        }
      }

      /*
       * Verify that the value of a valid attribute is returned.
       */
      it("must return value of defined attribute value") {
        new TestData {
          assert(javaManifest.getAttribute("Implementation-Title") !== "")
        }
      }
    }

    /*
     * Verify the inceptionTimestamp function.
     */
    describe(".inceptionTimestamp") {

      /*
       * Verify that a NoSuchElementException is thrown if the inception timestamp is undefined.
       *
       * Note: Most JAR files will not define this; this attribute is only defined by Facsimile and its client projects.
       * As a consequence the main Java runtime JAR file will not have an inception timestamp attribute defined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            javaManifest.inceptionTimestamp
          }
          assertNoSuchAttribute(e, "Inception-Timestamp")
        }
      }

      /*
       * Verify that a DateTimeParseException is thrown if an invalid inception timestamp has been defined.
       */
      it("must throw a DateTimeParseException if timestamp is invalid") {
        new TestData {
          intercept[DateTimeParseException] {
            invalidManifest.inceptionTimestamp
          }
        }
      }

      /*
       * Verify that a valid inception date is returned for correctly formatted inception timestamp attributes.
       */
      it("must return a date/time if timestamp is valid") {
        new TestData {
          val validDate = ZonedDateTime.parse("2004-06-22T18:16-04:00[America/New_York]")
          assert(validDate.getYear === 2004)
          assert(validDate.getMonthValue === 6)
          assert(validDate.getDayOfMonth === 22)
          assert(dummyManifest.inceptionTimestamp === validDate)
        }
      }
    }

    /*
     * Verify the inceptionTimestamp function.
     */
    describe(".buildTimestamp") {

      /*
       * Verify that a NoSuchElementException is thrown if the build timestamp is undefined.
       *
       * Note: Most JAR files will not define this; this attribute is only defined by Facsimile and its client projects.
       * As a consequence the main Java runtime JAR file will not have an inception timestamp attribute defined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            javaManifest.buildTimestamp
          }
          assertNoSuchAttribute(e, "Build-Timestamp")
        }
      }

      /*
       * Verify that a DateTimeParseException is thrown if an invalid inception timestamp has been defined.
       */
      it("must throw a DateTimeParseException if timestamp is invalid") {
        new TestData {
          intercept[DateTimeParseException] {
            invalidManifest.buildTimestamp
          }
        }
      }

      /*
       * Verify that a valid inception date is returned for correctly formatted inception timestamp attributes.
       */
      it("must return a valid date/time if timestamp is valid") {
        new TestData {
          val validDate = ZonedDateTime.parse("2014-08-14T13:40:00.000-04:00[America/New_York]")
          assert(validDate.getYear === 2014)
          assert(validDate.getMonthValue === 8)
          assert(validDate.getDayOfMonth === 14)
          assert(dummyManifest.buildTimestamp === validDate)
        }
      }
    }

    /*
     * Verify the title function.
     */
    describe(".title") {

      /*
       * Verify that a NoSuchElementException is thrown if the title is undefined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            invalidManifest.title
          }
          assertNoSuchAttribute(e, "Implementation-Title")
        }
      }

      /*
       * Verify that a valid title is returned if defined.
       */
      it("must return a valid title if defined") {
        new TestData {
          //assert(javaManifest.title === NO SYSTEM PROPERTY FOR THIS!)
          assert(dummyManifest.title === "Facsimile Dummy Test Jar")
        }
      }
    }

    /*
     * Verify the vendor function.
     */
    describe(".vendor") {

      /*
       * Verify that a NoSuchElementException is thrown if the vendor is undefined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            invalidManifest.vendor
          }
          assertNoSuchAttribute(e, "Implementation-Vendor")
        }
      }

      /*
       * Verify that a valid vendor is returned if defined.
       */
      it("must return a valid vendor if defined") {
        new TestData {
          assert(javaManifest.vendor === Properties.javaVendor)
          assert(dummyManifest.vendor === "Michael J. Allen")
        }
      }
    }

    /*
     * Verify the version function.
     */
    describe(".version") {

      /*
       * Verify that a NoSuchElementException is thrown if the version is undefined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            invalidManifest.version
          }
          assertNoSuchAttribute(e, "Implementation-Version")
        }
      }

      /*
       * Verify that a valid version is returned if defined.
       */
      it("must return a valid version if defined") {
        new TestData {
          assert(javaManifest.version.toString === Properties.javaVersion)
          assert(dummyManifest.version.toString === "1.0")
        }
      }
    }

    /*
     * Verify the specTitle function.
     */
    describe(".specTitle") {

      /*
       * Verify that a NoSuchElementException is thrown if the title is undefined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            invalidManifest.specTitle
          }
          assertNoSuchAttribute(e, "Specification-Title")
        }
      }

      /*
       * Verify that a valid title is returned if defined.
       */
      it("must return a valid title if defined") {
        new TestData {
          assert(javaManifest.specTitle === Properties.javaSpecName)
          assert(dummyManifest.specTitle === "Facsimile Dummy Test Jar")
        }
      }
    }

    /*
     * Verify the specVendor function.
     */
    describe(".specVendor") {

      /*
       * Verify that a NoSuchElementException is thrown if the specification vendor is undefined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            invalidManifest.specVendor
          }
          assertNoSuchAttribute(e, "Specification-Vendor")
        }
      }

      /*
       * Verify that a valid vendor is returned if defined.
       */
      it("must return a valid vendor if defined") {
        new TestData {
          assert(javaManifest.specVendor === Properties.javaSpecVendor)
          assert(dummyManifest.specVendor === "Michael J. Allen")
        }
      }
    }

    /*
     * Verify the specVersion function.
     */
    describe(".specVersion") {

      /*
       * Verify that a NoSuchElementException is thrown if the version is undefined.
       */
      it("must throw a NoSuchElementException if undefined") {
        new TestData {
          val e = intercept[NoSuchElementException] {
            invalidManifest.specVersion
          }
          assertNoSuchAttribute(e, "Specification-Version")
        }
      }

      /*
       * Verify that a valid version is returned if defined.
       */
      it("must return a valid version if defined") {
        new TestData {
          assert(javaManifest.specVersion.toString === Properties.javaSpecVersion)
          assert(dummyManifest.specVersion.toString === "1.0")
        }
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc