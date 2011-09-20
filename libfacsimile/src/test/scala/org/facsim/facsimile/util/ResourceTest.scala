/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2011, Michael J Allen.

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
with Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://www.facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://www.facsim.org/Documentation/CodingStandards/
===============================================================================
$Id$

Scala source file belonging to the org.facsim.facsimile.util package.
*/
//=============================================================================

package org.facsim.facsimile.util

import java.util.MissingResourceException
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

@RunWith (classOf [JUnitRunner])
class ResourceSpec extends Spec with ShouldMatchers {

  describe ("Resource") {
    describe ("String Resource Format") {
      it ("should throw NullPointerException when given a null key") {
        evaluating {
          Resource.format (null)
        } should produce [NullPointerException]
      }
      it ("should throw MissingResourceException when given a missing key") {
        evaluating {
          Resource.format ("MISSING_KEY")
        } should produce [MissingResourceException]
      }
      it ("should throw ClassCastException when given a non-string key") {
        (pending)
        //evaluating {
        //  Resource.format ("testNonStringResource")
        //} should produce [ClassCastException]
      }
      it ("should throw IllegalArgumentException when given invalid argument")
      {
        evaluating {
          Resource.format ("testMessage", "Invalid extra argument")
        } should produce [IllegalArgumentException]
        evaluating {
          Resource.format ("testCompoundMessage0")
        } should produce [IllegalArgumentException]
        evaluating {
          Resource.format ("testCompoundMessage0", "Valid argument",
          "Invalid extra argument")
        } should produce [IllegalArgumentException]
      }
      it ("should retrieve a simple string resource correctly") {
        Resource.format ("testMessage") should equal ("Test message")
      }
      it ("should retrieve a compound string resource correctly") {
        Resource.format ("testCompoundMessage0", "zero") should
        equal ("Test compound message 0: 0=zero")
        Resource.format ("testCompoundMessage1", "zero", "one") should
        equal ("Test compound message 1: 0=zero, 1=one")
        Resource.format ("testCompoundMessage2", "zero", "one", "two") should
        equal ("Test compound message 2: 0=zero, 1=one, 2=two")
      }
    }
  }
}
