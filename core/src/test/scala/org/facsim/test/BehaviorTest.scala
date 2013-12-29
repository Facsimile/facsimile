/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.test package.
*/
//=============================================================================

package org.facsim.test

import org.facsim.Behavior
import org.facsim.util.Version
import org.joda.time.DateTime
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.Behavior!]] trait.
*/
//=============================================================================

class BehaviorTest extends FunSpec {

/**
Test trait - unpopulated behavior.
*/

  trait UnpopulatedBehavior {
    val behavior = new Behavior {}
  }

/**
Test trait - populated behavior.
*/

  trait PopulatedBehavior {
    val name = "SomeTitle"
    val orgBasic = "SomeOrg, Inc."
    val orgRange = "SomeOrg"
    val release = DateTime.now
    val inceptionThisYear = release
    val inceptionLastYear = release.minusYears (1)
    val ver = new Version ("5.12-231")
    trait BasicBehavior extends Behavior {
      override def title = name
      override def releaseDate = release
      override def version = ver
    }
    val basic = new BasicBehavior {
      override def inceptionDate = inceptionThisYear
      override def organization = orgBasic
    }
    val range = new BasicBehavior {
      override def inceptionData = inceptionLastYear
      override def organization = orgRange
    }
  }

/*
Function to verify a NoSuchElementException's message.
*/

  def assertMessage (e: NoSuchElementException, field: String) =
  assert (e.getMessage () === "Behavior element not implemented: '" + field +
  "'")

/*
Test fixture description.
*/

  describe (classOf [Behavior].getCanonicalName) {

/*
Test title function.
*/

    describe (".title") {
      it ("must throw a NoSuchElementException if undefined") {
        new UnpopulatedBehavior {
          val e = intercept [NoSuchElementException] {
            behavior.title
          }
          assertMessage (e, "title")
        }
      }
      it ("must report defined value") {
        new PopulatedBehavior {
          assert (basic.title === name)
        }
      }
    }

/*
Test organization function.
*/

    describe (".organization") {
      it ("must throw a NoSuchElementException if undefined") {
        new UnpopulatedBehavior {
          val e = intercept [NoSuchElementException] {
            behavior.organization
          }
          assertMessage (e, "organization")
        }
      }
      it ("must report defined value") {
        new PopulatedBehavior {
          assert (basic.organization === orgBasic)
          assert (range.organization === orgRange)
        }
      }
    }

/*
Test inceptionDate function.
*/

    describe (".inceptionDate") {
      it ("must throw a NoSuchElementException if undefined") {
        new UnpopulatedBehavior {
          val e = intercept [NoSuchElementException] {
            behavior.inceptionDate
          }
          assertMessage (e, "inceptionDate")
        }
      }
      it ("must report defined value") {
        new PopulatedBehavior {
          assert (basic.inceptionDate === inceptionThisYear)
        }
      }
    }

/*
Test releaseDate function.
*/

    describe (".releaseDate") {
      it ("must throw a NoSuchElementException if undefined") {
        new UnpopulatedBehavior {
          val e = intercept [NoSuchElementException] {
            behavior.releaseDate
          }
          assertMessage (e, "releaseDate")
        }
      }
      it ("must report defined value") {
        new PopulatedBehavior {
          assert (basic.releaseDate === release)
        }
      }
    }

/*
Test copyright function.
*/

    describe (".copyright") {
      it ("must throw a NoSuchElementException if undefined") {
        new UnpopulatedBehavior {
          val e = intercept [NoSuchElementException] {
            behavior.copyright
          }
        }
      }
      it ("must report defined value") {
        new PopulatedBehavior {

/*
Note.  If the release and inception dates have the same year, we should get a
single year in the copyright message, otherwise, we should get a range of years
reported.  Also, if the organization name ends in a period, it should be
stripped when producing the copyright string (which should always end in a
period ".", not in two periods "..").  We test this explicitly by having one
org name ending in a period, and one that does not.
*/

          assert (basic.copyright === "Copyright © " + release.year + ", " +
          orgBasic + ".")
          assert (range.copyright === "Copyright © " + inceptionLastYear.year +
          "-" + release.year + ", " + orgRange + ".")
        }
      }
    }

/*
Test version function.
*/

    describe (".version") {
      it ("must throw a NoSuchElementException if undefined") {
        new UnpopulatedBehavior {
          val e = intercept [NoSuchElementException] {
            behavior.version
          }
          assertMessage (e, "version")
        }
      }
      it ("must report defined value") {
        new PopulatedBehavior {
          assert (basic.version === ver)
        }
      }
    }
  }
}