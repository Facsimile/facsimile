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
// Scala source file belonging to the org.facsim.util.test package.
//======================================================================================================================
package org.facsim.util.test

import java.time.ZonedDateTime
import java.time.format.DateTimeParseException
import java.util.jar.Attributes.Name
import org.facsim.dummy.Dummy
import org.facsim.invalid.Invalid
import org.facsim.missing.Missing
import org.facsim.util.{Manifest, NoSuchAttributeException, Version, VersionParseException}
import scala.util.{Failure, Success}
import org.scalatest.funspec.AnyFunSpec

//scalastyle:off scaladoc
//scalastyle:off multiple.string.literals
/** Test harness for the [[Manifest]] class. */
final class ManifestTest
extends AnyFunSpec
with CommonTestMethods {

  /** ''Java'' specification vendor.
   *
   *  This should be configured to match the value returned as the "specification-vendor" attribute of the ''Java
   *  run-time'' library.
   */
  val JavaSpecVendor = "Oracle Corporation"

  /** Dummy class that will not be loaded from a JAR file, and can thus be tested as such. */
  class NonJARFileClass

  /** Manifest test data. */
  trait TestData {

    /** ''Java'' runtime library manifest. Should exist ;-). */
    lazy val javaManifest = Manifest(classOf[String])

    /** A manifest, having some invalid attributes, loaded from a ''JAR'' file. */
    lazy val invalidManifest = Manifest(classOf[Invalid])

    /** A null manifest for a JAR file that has no manifest defined. */
    lazy val missingManifest = Manifest(classOf[Missing])

    /** A null manifest for a class that has no associated ''JAR'' file. */
    lazy val noJarManifest = Manifest(classOf[NonJARFileClass])

    /** A manifest, having defined, valid attributes, loaded from a ''JAR'' file. */
    lazy val dummyManifest = Manifest(classOf[Dummy])

    /** Non-existent attribute name. */
    lazy val nonExistentAttr = new Name("UndefinedAttributeName")

    /** Dummy manifest title. */
    lazy val dummyTitle = "Facsimile Dummy Test Jar"

    /** Dummy manifest vendor. */
    lazy val dummyVendor = "Michael J. Allen"

    /** Dummy version number. */
    lazy val dummyVersion = Version(1, 0)

    /** Inception timestamp in the dummy manifest. */
    lazy val inceptionTime = ZonedDateTime.parse("2004-06-22T18:16-04:00[America/New_York]")

    /** Build timestamp in the dummy manifest. */
    lazy val buildTime = ZonedDateTime.parse("2014-08-14T13:40:00.000-04:00[America/New_York]")

    /** Invalid version string. */
    lazy val invalidVersion = "invalid-version"
  }

  // Name the class we're testing.
  describe(classOf[Manifest].getCanonicalName) {

    // Test the attribute method.
    describe(".attribute(Name)") {

      // Check null name values.
      it("must throw a NullPointerException if passed a null name") {
        new TestData {
          val e = intercept[NullPointerException] {
            Manifest.NullManifest.attribute(null) //scalastyle:ignore null
          }
          assertRequireNonNullMsg(e, "name")
        }
      }

      // It must return a Failure(NoSuchAttributeException) if passed a non-existent attribute name.
      it("must return Failure(NoSuchAttributeException) if passed an undefined attribute value") {
        new TestData {
          assert(javaManifest.attribute(nonExistentAttr) === Failure(NoSuchAttributeException(nonExistentAttr)))
          assert(invalidManifest.attribute(nonExistentAttr) === Failure(NoSuchAttributeException(nonExistentAttr)))
          assert(missingManifest.attribute(nonExistentAttr) === Failure(NoSuchAttributeException(nonExistentAttr)))
          assert(noJarManifest.attribute(nonExistentAttr) === Failure(NoSuchAttributeException(nonExistentAttr)))
          assert(dummyManifest.attribute(nonExistentAttr) === Failure(NoSuchAttributeException(nonExistentAttr)))
        }
      }

      // Verify that the value of a valid attribute is returned.
      it("must return value of defined attribute value wrapped in a Success") {
        new TestData {
          assert(dummyManifest.attribute(Name.IMPLEMENTATION_TITLE) === Success(dummyTitle))
        }
      }
    }

    // Test the dateAttribute method.
    describe(".dateAttribute(Name)") {

      // Check null name values.
      it("must throw a NullPointerException if passed a null name") {
        val e = intercept[NullPointerException] {
          Manifest.NullManifest.dateAttribute(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "name")
      }

      // Verify that we get a Failure(DateTimeParseException) if attribute has an invalid date/time.
      it("must return Failure(DateTimeParseException) if asked to retrieve a non-date attribute") {
        new TestData {
          val result = invalidManifest.dateAttribute(Manifest.InceptionTimestamp)
          assert(result.isFailure)
          intercept[DateTimeParseException] {
            result.get
          }
        }
      }

      // Verify that it returns a Failure(NoSuchAttributeException) if the date attribute is undefined.
      it("must return Failure(NoSuchAttributeException) if attribute undefined") {
        assert(Manifest.NullManifest.dateAttribute(Manifest.InceptionTimestamp) ===
        Failure(NoSuchAttributeException(Manifest.InceptionTimestamp)))
      }

      // Verify that it retrieves date attribute values OK.
      it("must return defined valid date wrapped in a success") {
        new TestData {
          assert(dummyManifest.dateAttribute(Manifest.InceptionTimestamp) === Success(inceptionTime))
        }
      }
    }

    // Test the versionAttribute method.
    describe(".versionAttribute(Name)") {

      // Check null name values.
      it("must throw a NullPointerException if passed a null name") {
        val e = intercept[NullPointerException] {
          Manifest.NullManifest.versionAttribute(null) //scalastyle:ignore null
        }
        assertRequireNonNullMsg(e, "name")
      }

      // Verify that we get a Failure(VersionParseException) if attribute has an invalid version.
      it("must return Failure(VersionParseException) if asked to retrieve a non-date attribute") {
        new TestData {
          assert(invalidManifest.versionAttribute(Name.IMPLEMENTATION_VERSION) ===
          Failure(VersionParseException(invalidVersion, 0)))
        }
      }

      // Verify that it returns a Failure(NoSuchAttributeException) if the date attribute is undefined.
      it("must return Failure(NoSuchAttributeException) if attribute undefined") {
        assert(Manifest.NullManifest.versionAttribute(Name.IMPLEMENTATION_VERSION) ===
        Failure(NoSuchAttributeException(Name.IMPLEMENTATION_VERSION)))
      }

      // Verify that it retrieves version attribute values OK.
      it("must return defined valid date wrapped in a success") {
        new TestData {
          assert(dummyManifest.versionAttribute(Name.IMPLEMENTATION_VERSION) === Success(dummyVersion))
        }
      }
    }

    // Verify the inceptionTimestamp function.
    describe(".inceptionTimestamp") {

      // Verify that we get a Failure(DateTimeParseException) if attribute has an invalid date/time.
      it("must return Failure(DateTimeParseException) if the date is not formatted correctly") {
        new TestData {

          val result = invalidManifest.inceptionTimestamp
          assert(result.isFailure)
          intercept[DateTimeParseException] {
            result.get
          }
        }
      }

      // Verify that it returns Failure(NoSuchAttributeException) if the inception date attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.inceptionTimestamp ===
        Failure(NoSuchAttributeException(Manifest.InceptionTimestamp)))
      }

      // Verify that it retrieves date attribute values OK.
      it("must return defined valid date wrapped in a success") {
        new TestData {
          assert(dummyManifest.inceptionTimestamp === Success(inceptionTime))
        }
      }
    }

    // Verify the buildTimestamp function.
    describe(".buildTimestamp") {

      // Verify that we get a Failure(DateTimeParseException) if attribute has an invalid date/time.
      it("must return Failure(DateTimeParseException) if the date is not formatted correctly") {
        new TestData {

          val result = invalidManifest.buildTimestamp
          assert(result.isFailure)
          intercept[DateTimeParseException] {
            result.get
          }
        }
      }

      // Verify that it returns Failure(NoSuchAttributeException) if the build date attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.buildTimestamp === Failure(NoSuchAttributeException(Manifest.BuildTimestamp)))
      }

      // Verify that it retrieves date attribute values OK.
      it("must return defined valid date wrapped in a success") {
        new TestData {
          assert(dummyManifest.buildTimestamp === Success(buildTime))
        }
      }
    }

    // Verify the title function.
    describe(".title") {

      // Verify that it returns Failure(NoSuchAttributeException) if the title attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.title === Failure(NoSuchAttributeException(Name.IMPLEMENTATION_TITLE)))
      }

      // Verify that it retrieves attribute values OK.
      it("must return define valid title wrapped in a success") {
        new TestData {
          assert(dummyManifest.title === Success(dummyTitle))
        }
      }
    }

    // Verify the vendor function.
    describe(".vendor") {

      // Verify that it returns Failure(NoSuchAttributeException) if the vendor attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.vendor === Failure(NoSuchAttributeException(Name.IMPLEMENTATION_VENDOR)))
      }

      // Verify that it retrieves attribute values OK.
      it("must return defined valid vendor name wrapped in a success") {
        new TestData {
          assert(dummyManifest.vendor === Success(dummyVendor))

          // For some reason, the various Java JDK releases do not always have meaningful, or correct, vendor entries
          // and values can differ from the Java Vendor reported via the system Properties. So testing the vendor
          // reported by the JDK's manifest is not included in testing.
          /*Properties.javaVendor match {
            case vendor: String => assert(javaManifest.vendor === Success(vendor))
          }*/
        }
      }
    }

    // Verify the version function.
    describe(".version") {

      // Verify that we get a Failure(VersionParseException) if attribute has an invalid version.
      it("must return Failure(VersionParseException) if asked to retrieve a non-date attribute") {
        new TestData {
          assert(invalidManifest.version === Failure(VersionParseException(invalidVersion, 0)))
        }
      }

      // Verify that it returns Failure(NoSuchAttributeException) if the version attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.version === Failure(NoSuchAttributeException(Name.IMPLEMENTATION_VERSION)))
      }

      // Verify that it retrieves attribute values OK.
      it("must return defined valid version wrapped in a success") {
        new TestData {
          assert(dummyManifest.version === Success(dummyVersion))
        }
      }
    }

    // Verify the specTitle function.
    describe(".specTitle") {

      // Verify that it returns Failure(NoSuchAttributeException) if the title attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.specTitle === Failure(NoSuchAttributeException(Name.SPECIFICATION_TITLE)))
      }

      // Verify that it retrieves attribute values OK.
      it("must return define valid title wrapped in a success") {
        new TestData {
          assert(dummyManifest.specTitle === Success(dummyTitle))
        }
      }
    }

    // Verify the specVendor function.
    describe(".specVendor") {

      // Verify that it returns Failure(NoSuchAttributeException) if the vendor attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.specVendor === Failure(NoSuchAttributeException(Name.SPECIFICATION_VENDOR)))
      }

      // Verify that it retrieves attribute values OK.
      it(s"must return '${JavaSpecVendor}' wrapped in a success") {
        new TestData {
          assert(javaManifest.specVendor === Success(JavaSpecVendor))
          assert(dummyManifest.specVendor === Success(dummyVendor))
        }
      }
    }

    // Verify the specVersion function.
    describe(".specVersion") {

      // Verify that we get a Failure(VersionParseException) if attribute has an invalid version.
      it("must return Failure(VersionParseException) if asked to retrieve a non-date attribute") {
        new TestData {
          assert(invalidManifest.specVersion === Failure(VersionParseException(invalidVersion, 0)))
        }
      }

      // Verify that it returns Failure(NoSuchAttributeException) if the specification version attribute is undefined.
      it("must return Failure(NoSuchAttribute) if attribute undefined") {
        assert(Manifest.NullManifest.specVersion === Failure(NoSuchAttributeException(Name.SPECIFICATION_VERSION)))
      }

      // Verify that it retrieves attribute values OK.
      it("must return defined valid version wrapped in a success") {
        new TestData {
          assert(dummyManifest.specVersion === Success(dummyVersion))
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