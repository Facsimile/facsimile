/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.util.test package.
*/
//=============================================================================

package org.facsim.util.test

import java.util.GregorianCalendar
import java.util.Locale
import java.util.MissingResourceException
import java.util.TimeZone
import org.facsim.util.Resource
import org.scalatest.FunSpec
import org.scalatest.matchers.ShouldMatchers

//=============================================================================
/**
Test harness for the [[org.facsim.util.Resource]] class.
*/
//=============================================================================

class ResourceTest extends FunSpec with ShouldMatchers {

/*
Some commonly used strings.
*/

  private val compoundResource = Array ("testCompoundResource0",
  "testCompoundResource1", "testCompoundResource2")
  private val dateResource = "testDateResource"
  private val helloResource = "hello"
  private val realResource = "testRealResource"
  private val singleResource = "testSingleResource"
  private val testBundleName = "facsimiletest"

/*
Name the class we're testing.
*/

  describe (classOf [Resource].getCanonicalName ()) {

/*
Test primary constructor operation.
*/

    describe ("this (bundleName: String)") {
      it ("should throw NullPointerException when bundleName is null") {
        evaluating {
          new Resource (null)
        } should produce [NullPointerException]
      }
      it ("should throw MissingResourceException when bundleName identifies " +
      "a missing resource bundle") {
        evaluating {
          new Resource ("NameOfNonExistentBundle")
        } should produce [MissingResourceException]
      }
      it ("should find a defined resource bundle OK") {
        new Resource (testBundleName) should not be (null)
      }
    }

/*
Test string resource formatting.
*/

    describe ("format (key: String, arguments: Any*)") {
      it ("should throw NullPointerException when key is null") {
        evaluating {
          new Resource (testBundleName).format (null)
        } should produce [NullPointerException]
      }
      it ("should throw MissingResourceException when key undefined") {
        evaluating {
          new Resource (testBundleName).format ("UNDEFINED_KEY")
        } should produce [MissingResourceException]
      }
      ignore ("should throw ClassCastException when key identifies non-string "
      + "resource") {
        // Cannot define non-string resources currently - test ignored
        evaluating {
          new Resource (testBundleName).format ("testNonStringResource")
        } should produce [ClassCastException]
      }
      ignore ("should throw IllegalArgumentException when argument passed " +
      "passed to non-compound resource") {
        // Java doesn't throw an exception in this case - test ignored.
        evaluating {
          new Resource (testBundleName).format (singleResource,
          "Invalid extra argument")
        } should produce [IllegalArgumentException]
      }
      ignore ("should throw IllegalArgumentException when no argument passed "
      + "to compound resource") {
        // Java doesn't throw an exception in this case - test ignored.
        evaluating {
          new Resource (testBundleName).format (compoundResource (0))
        } should produce [IllegalArgumentException]
      }
      ignore ("should throw IllegalArgumentException when extra arguments " +
      "passed to compound resource") {
        // Java doesn't throw an exception in this case - test ignored.
        evaluating {
          new Resource (testBundleName).format (compoundResource (0),
          "Valid argument", "Invalid extra argument")
        } should produce [IllegalArgumentException]
      }
      ignore ("should throw IllegalArgumentException when insufficient " +
      "arguments passed to compound resource") {
        // Java doesn't throw an exception in this case - test ignored.
        evaluating {
          new Resource (testBundleName).format (compoundResource (1),
          "Valid argument")
        } should produce [IllegalArgumentException]
      }
      it ("should throw IllegalArgumentException when string passed to date " +
      "resource") {
        evaluating {
          new Resource (testBundleName).format (dateResource, "Bob")
        }
      }
      it ("should throw IllegalArgumentException when string passed to " +
      "real resource") {
        evaluating {
          new Resource (testBundleName).format (realResource, "Fred")
        }
      }
      it ("should retrieve a non-compound resource with no arguments OK") {
        new Resource (testBundleName).format (singleResource) should
        equal ("Test non-compound resource")
      }
      it ("should retrieve compound resources with appropriate arguments OK") {
        val resource = new Resource (testBundleName)
        resource.format (compoundResource (0), "zero") should
        equal ("Test compound resource 0: 0=zero")
        resource.format (compoundResource (1), "zero", "one") should
        equal ("Test compound resource 1: 0=zero, 1=one")
        resource.format (compoundResource (2), "zero", "one", "two") should
        equal ("Test compound resource 2: 0=zero, 1=one, 2=two")
      }
      it ("should parse choice resources OK") {
        val choiceResource = "testChoiceResource"
        new Resource (testBundleName).format (choiceResource, 0) should
        equal ("On your marks...")
        new Resource (testBundleName).format (choiceResource, 1) should
        equal ("Get set...")
        new Resource (testBundleName).format (choiceResource, 2) should
        equal ("Go!")
      }
      it ("should retrieve localized string resources OK") {
        val default = Locale.getDefault ()
        try {
          // Check that we get the correct en_US response.
          Locale.setDefault (Locale.US)
          new Resource (testBundleName).format (helloResource) should
          equal ("Howdy!")
          // Should get a different result for Brits...
          Locale.setDefault (Locale.UK)
          new Resource (testBundleName).format (helloResource) should
          equal ("Wotcha!")
          // Germans should have a good day...
          Locale.setDefault (Locale.GERMANY)
          new Resource (testBundleName).format (helloResource) should
          equal ("Guten Tag!")
          // ...and so should the French...
          Locale.setDefault (Locale.FRANCE)
          new Resource (testBundleName).format (helloResource) should
          equal ("Bonjour!")
        }
        // Restore original default.
        finally {
          Locale.setDefault (default)
        }
      }
      it ("should retrieve numeric resources OK") {
        val default = Locale.getDefault ()
        val number = 1234.56
        try {
          // Check that we get the correct en_US response.
          Locale.setDefault (Locale.US)
          new Resource (testBundleName).format (realResource, number) should
          equal ("1,234.56")
          // Germans do things differently...
          Locale.setDefault (Locale.GERMANY)
          new Resource (testBundleName).format (realResource, number) should
          equal ("1.234,56")
        }
        // Restore original default.
        finally {
          Locale.setDefault (default)
        }
      }
      it ("should retrieve localized date resources OK") {
        val default = Locale.getDefault ()
        // Oct 14, 2010 - months are zero-based numbers (!)
        val date = new GregorianCalendar (2010, 9, 14).getTime ()
        try {
          // Check that we get the correct en_US response.
          Locale.setDefault (Locale.US)
          new Resource (testBundleName).format (dateResource, date) should
          equal ("10/14/10")
          // Month & day are in different order in the UK
          Locale.setDefault (Locale.UK)
          new Resource (testBundleName).format (dateResource, date) should
          equal ("14/10/10")
          // Germans use same order as UK, but different separator...
          Locale.setDefault (Locale.GERMANY)
          new Resource (testBundleName).format (dateResource, date) should
          equal ("14.10.10")
        }
        // Restore original default.
        finally {
          Locale.setDefault (default)
        }
      }
    }

/*
Test image file retrieval.
*/

    describe ("getIcon (filename: String)") {
      it ("should throw a NullPointerException when passed a null string") {
        evaluating {
          new Resource (testBundleName).getIcon (null)
        } should produce [NullPointerException]
      }
      it ("should throw MissingResourceException when asked to retrieve " +
      "missing file") {
        evaluating {
          new Resource (testBundleName).getIcon ("/images/NO_SUCH_FILE.png")
        } should produce [MissingResourceException]
      }
      it ("should retrieve a valid icon file OK") {
        val icon = new Resource (testBundleName).getIcon (
        "/images/facsimiletest.png")
        icon should not equal (null)
        icon.getIconHeight () should equal (80)
        icon.getIconWidth () should equal (100)
      }
    }
  }
}
