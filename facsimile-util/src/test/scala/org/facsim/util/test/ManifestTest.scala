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

import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.util.jar.Attributes.Name
import org.facsim.dummy.Dummy
import org.facsim.invalid.Invalid
import org.facsim.missing.Missing
import org.facsim.util.{Manifest, Version}
import org.scalatest.FunSpec
import scala.util.Properties

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/** Test harness for the [[Manifest]] class.
  */
class ManifestTest
extends FunSpec
with CommonTestMethods {

  /** Dummy class that will not be loaded from a JAR file, and can thus be tested as such.
    */
  class NonJARFileClass

  /** Manifest test data.
    */
  trait TestData {

    /** ''Java'' runtime library manifest. Should exist ;-).
      */
    lazy val javaManifest = Manifest(classOf[String])

    /** A manifest, having some invalid attributes, loaded from a ''JAR'' file.
      */
    lazy val invalidManifest = Manifest(classOf[Invalid])

    /** A null manifest for a JAR file that has no manifest defined.
      */
    lazy val missingManifest = Manifest(classOf[Missing])

    /** A null manifest for a class that has no associated ''JAR'' file.
      */
    lazy val noJarManifest = Manifest(classOf[NonJARFileClass])

    /** A manifest, having defined, valid attributes, loaded from a ''JAR'' file.
      */
    lazy val dummyManifest = Manifest(classOf[Dummy])

    /** Non-existent attribute name.
      */
    lazy val nonExistentAttr = new Name("UndefinedAttributeName")

    /** Dummy manifest title.
      */
    lazy val dummyTitle = "Facsimile Dummy Test Jar"

    /** Dummy manifest vendor.
      */
    lazy val dummyVendor = "Michael J. Allen"

    /** Dummy version number.
      */
    lazy val dummyVersion = Version(1, 0)

    /** Inception timestamp in the dummy manifest.
      */
    lazy val inceptionTime = ZonedDateTime.parse("2004-06-22T18:16-04:00[America/New_York]")

    /** Build timestamp in the dummy manifest.
      */
    lazy val buildTime = ZonedDateTime.parse("2014-08-14T13:40:00.000-04:00[America/New_York]")
  }

  // Name the class we're testing.
  describe(classOf[Manifest].getCanonicalName) {

    // Test the attribute method.
    describe(".attribute(Name)") {

      // Check null name values.
      it("must throw a NullPointerException if passed a null name") {
        new TestData {
          val e = intercept[NullPointerException] {
            Manifest.NullManifest.attribute(null) //Scalastyle:ignore null
          }
          assertRequireNonNullMsg(e, "name")
        }
      }

      // It must return None if passed a non-existent attribute name.
      it("must return None if passed an undefined attribute value") {
        new TestData {
          assert(javaManifest.attribute(nonExistentAttr).isEmpty)
          assert(invalidManifest.attribute(nonExistentAttr).isEmpty)
          assert(missingManifest.attribute(nonExistentAttr).isEmpty)
          assert(noJarManifest.attribute(nonExistentAttr).isEmpty)
          assert(dummyManifest.attribute(nonExistentAttr).isEmpty)
        }
      }

      // Verify that the value of a valid attribute is returned.
      it("must return value of defined attribute value") {
        new TestData {
          assert(javaManifest.attribute(Name.IMPLEMENTATION_TITLE).isDefined) // Properties.javaName missing.
          assert(dummyManifest.attribute(Name.IMPLEMENTATION_TITLE) === Some(dummyTitle))
        }
      }
    }

    // Test the dateAttribute method.
    describe(".dateAttribute(Name)") {

      // Check null name values.
      it("must throw a NullPointerException if passed a null name") {
        val e = intercept[NullPointerException] {
          Manifest.NullManifest.dateAttribute(null) //Scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "name")
      }

      // Verify that we get a DateTimeParseException if attribute has an invalid date/time.
      it("must throw a DateTimeParseException if asked to retrieve a non-date attribute") {
        new TestData {

          // Mo meed to check message, since this is not thrown by Facsimile.
          intercept[DateTimeParseException] {
            invalidManifest.dateAttribute(Manifest.InceptionTimestamp)
          }
        }
      }

      // Verify that it returns None if the date attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.dateAttribute(Manifest.InceptionTimestamp).isEmpty)
      }

      // Verify that it retrieves date attribute values OK.
      it("must return valid date if attribute defined") {
        new TestData {
          assert(dummyManifest.dateAttribute(Manifest.InceptionTimestamp) === Some(inceptionTime))
        }
      }
    }

    // Verify the inceptionTimestamp function.
    describe(".inceptionTimestamp") {

      // Verify that we get a DateTimeParseException if attribute has an invalid date/time.
      it("must throw a DateTimeParseException if asked to retrieve a non-date attribute") {
        new TestData {

          // Mo meed to check message, since this is not thrown by Facsimile.
          intercept[DateTimeParseException] {
            invalidManifest.inceptionTimestamp
          }
        }
      }

      // Verify that it returns None if the date attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.inceptionTimestamp.isEmpty)
      }

      // Verify that it retrieves date attribute values OK.
      it("must return valid date if attribute defined") {
        new TestData {
          assert(dummyManifest.inceptionTimestamp === Some(inceptionTime))
        }
      }
    }

    // Verify the buildTimestamp function.
    describe(".buildTimestamp") {

      // Verify that we get a DateTimeParseException if attribute has an invalid date/time.
      it("must throw a DateTimeParseException if asked to retrieve a non-date attribute") {
        new TestData {

          // Mo meed to check message, since this is not thrown by Facsimile.
          intercept[DateTimeParseException] {
            invalidManifest.buildTimestamp
          }
        }
      }

      // Verify that it returns None if the date attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.buildTimestamp.isEmpty)
      }

      // Verify that it retrieves date attribute values OK.
      it("must return valid date if attribute defined") {
        new TestData {
          assert(dummyManifest.buildTimestamp === Some(buildTime))
        }
      }
    }

    // Verify the title function.
    describe(".title") {

      // Verify that it returns None if this attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.title.isEmpty)
      }

      // Verify that it retrieves attribute values OK.
      it("must return valid title if attribute defined") {
        new TestData {
          assert(javaManifest.title.isDefined) // There is no Properties.javaName to compare this to.
          assert(dummyManifest.title === Some(dummyTitle))
        }
      }
    }

    // Verify the vendor function.
    describe(".vendor") {

      // Verify that it returns None if this attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.vendor.isEmpty)
      }

      // Verify that it retrieves attribute values OK.
      it("must return valid vendor name if attribute defined") {
        new TestData {
          assert(javaManifest.vendor === Some(Properties.javaVendor))
          assert(dummyManifest.vendor === Some(dummyVendor))
        }
      }
    }

    // Verify the version function.
    describe(".version") {

      // Verify that we get an IllegalArgumentException if attribute has an invalid version.
      it("must throw an IllegalArgumentException if asked to retrieve a non-version attribute") {
        new TestData {
          val e = intercept[IllegalArgumentException] {
            invalidManifest.version
          }
          assertRequireValidMsg(e, "version", "invalid-version")
        }
      }

      // Verify that it returns None if this attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.version.isEmpty)
      }

      // Verify that it retrieves attribute values OK.
      it("must return valid version if attribute defined") {
        new TestData {
          assert(javaManifest.version.isDefined) // Can't compare to Properties.javaVersion because of funky format.
          assert(dummyManifest.version === Some(dummyVersion))
        }
      }
    }

    // Verify the specTitle function.
    describe(".specTitle") {

      // Verify that it returns None if this attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.specTitle.isEmpty)
      }

      // Verify that it retrieves attribute values OK.
      it("must return valid title if attribute defined") {
        new TestData {
          assert(javaManifest.specTitle == Some(Properties.javaSpecName))
          assert(dummyManifest.specTitle === Some(dummyTitle))
        }
      }
    }

    // Verify the specVendor function.
    describe(".specVendor") {

      // Verify that it returns None if this attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.specVendor.isEmpty)
      }

      // Verify that it retrieves attribute values OK.
      it("must return valid vendor name if attribute defined") {
        new TestData {
          assert(javaManifest.specVendor === Some(Properties.javaVendor))
          assert(dummyManifest.specVendor === Some(dummyVendor))
        }
      }
    }

    // Verify the specVersion function.
    describe(".specVersion") {

      // Verify that we get an IllegalArgumentException if attribute has an invalid version.
      it("must throw an IllegalArgumentException if asked to retrieve a non-version attribute") {
        new TestData {
          val e = intercept[IllegalArgumentException] {
            invalidManifest.specVersion
          }
          assertRequireValidMsg(e, "version", "invalid-version")
        }
      }

      // Verify that it returns None if this attribute is undefined.
      it("must return None if attribute undefined") {
        assert(Manifest.NullManifest.specVersion.isEmpty)
      }

      // Verify that it retrieves attribute values OK.
      it("must return valid version if attribute defined") {
        new TestData {
          assert(javaManifest.specVersion === Some(Version(Properties.javaSpecVersion)))
          assert(dummyManifest.specVersion === Some(dummyVersion))
        }
      }
    }
  }

  // Name the companion object we're testing.
  describe(Manifest.getClass.getCanonicalName) {

    // Test the factory method.
    describe(".apply(Class[_])") {

      // Verify that it handles a null argument/
      it("must throw a NullPointerException if passed a null value") {
        val e = intercept[NullPointerException] {
           Manifest(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "elementType")
      }

      // Verify that looking up the manifest for a primitive class reference (which does not have associated resources)
      // results in a NullManifest.
      //
      // Primitive types typically have no associated resources, because they are not implemented in library code.
      it("must return a NullManifest if passed a class reference having no associated resource") {
        val dc = classOf[Double]
        assert(Manifest(dc) eq Manifest.NullManifest)
      }

      // Verify that looking up the manifest for a class that was not loaded from a JAR file fails with a
      // FileNotFoundException.
      //
      // Test classes are loaded from .class files as-is, without being assembled into JAR files. Consequently, we can
      // use a reference to a locally-defined class here.
      it("must return a NullManifest if passed a non-JAR file class reference") {
        new TestData {
          assert(noJarManifest eq Manifest.NullManifest)
        }
      }

      // Verify it retrieves a NullManifest for a class whose JAR file has no manifest.
      it("must return a NullManifest if passed a JAR file class reference that has no manifest") {
        new TestData {
          assert(missingManifest eq Manifest.NullManifest)
        }
      }

      // Verify that we can construct our test data objects without a problem.
      it("must retrieve manifest data for valid JAR file class references") {
        new TestData {
          assert(javaManifest ne Manifest.NullManifest)
          assert(invalidManifest ne Manifest.NullManifest)
          assert(dummyManifest ne Manifest.NullManifest)
        }
      }
    }
  }
}
//scalastyle:on multiple.string.literals
//scalastyle:on scaladoc