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

import org.facsim.AppImplementation
import org.facsim.Behavior
import org.facsim.BehaviorRedefinitionException
import org.facsim.BehaviorUndefinedException
import org.facsim.LibResource
import org.facsim.util.Version
import org.joda.time.DateTime
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.AppImplementation!]] trait.
*/
//=============================================================================

class AppImplementationTest
extends FunSpec {

/**
Test data.
*/

  trait TestData {
    val nullBehavior = new Behavior {}
    val behavior = new Behavior {
      val now = DateTime.now ()
      val ver = new Version ("7.3.1")
      override def title = "appImplementationTitle"
      override def organization = "appImplementationOrganization"
      override def inceptionDate = releaseDate.minus (600)
      override def releaseDate = now
      override def version = ver
    }
    val app = new AppImplementation {}
    val redefined = LibResource ("BehaviorRedefinition", behavior,
    nullBehavior)
    val undefined = LibResource ("BehaviorUndefined")
  }

/*
Function to verify a NoSuchElementException's message.
*/

  def assertMessage (e: NoSuchElementException, field: String) =
  assert (e.getMessage () === LibResource ("Behavior.NoSuchElement", field))

/*
Test fixture description.
*/

  describe (classOf [AppImplementation].getCanonicalName) {

/*
Test behavior application. 
*/

    describe (".apply (Behavior)") {
      it ("must accept a single behavior application") {
        new TestData {
          app (behavior)
        }
      }

/*
Hmmm.  Should we allow this, or trap "bad" behaviors when they're applied?

In any case, we do allow null behaviors, right now.  Furthermore, we rely on
this behavior for some of the tests regarding failure atomicity below.
*/

      it ("must accept a single behavior application with missing fields") {
        new TestData {
          app (nullBehavior)
        }
      }
      it ("must reject a second behavior application") {
        new TestData {
          app (behavior)

/*
Test using the null behavior, which would cause an exception to be thrown if
we access any fields subsequently.  If we don't get an exception when we look
at the app's title, then we can claim we've implemented failure atomicity OK.
*/

          val e = intercept [BehaviorRedefinitionException] {
            app (nullBehavior)
          }
          assert (e.getMessage () === redefined)
          assert (app.title === behavior.title)

/*
Repeat a second time, just-in-case.
*/

          val e2 = intercept [BehaviorRedefinitionException] {
            app (nullBehavior)
          }
          assert (e2.getMessage () === redefined)
          assert (app.title === behavior.title)
        }
      }
    }

/*
Test title function.
*/

    describe (".title") {
      it ("must throw BehaviorUndefinedException if no behavior defined") {
        new TestData {
          val e = intercept [BehaviorUndefinedException] {
            app.title
          }
          assert (e.getMessage === undefined)
        }
      }
      it ("must throw a NoSuchElementException if not available") {
        new TestData {
          app (nullBehavior)
          val e = intercept [NoSuchElementException] {
            app.title
          }
          assertMessage (e, "title")
        }
      }
      it ("must correctly report defined behavior") {
        new TestData {
          app (behavior)
          assert (app.title === behavior.title)
        }
      }
    }
    
/*
Test organization function.
*/

    describe (".organization") {
      it ("must throw BehaviorUndefinedException if no behavior defined") {
        new TestData {
          val e = intercept [BehaviorUndefinedException] {
            app.organization
          }
          assert (e.getMessage === undefined)
        }
      }
      it ("must throw a NoSuchElementException if not available") {
        new TestData {
          app (nullBehavior)
          val e = intercept [NoSuchElementException] {
            app.organization
          }
          assertMessage (e, "organization")
        }
      }
      it ("must correctly report defined behavior") {
        new TestData {
          app (behavior)
          assert (app.organization === behavior.organization)
        }
      }
    }

/*
Test inceptionDate function.
*/

    describe (".inceptionDate") {
      it ("must throw BehaviorUndefinedException if no behavior defined") {
        new TestData {
          val e = intercept [BehaviorUndefinedException] {
            app.inceptionDate
          }
          assert (e.getMessage === undefined)
        }
      }
      it ("must throw a NoSuchElementException if not available") {
        new TestData {
          app (nullBehavior)
          val e = intercept [NoSuchElementException] {
            app.inceptionDate
          }
          assertMessage (e, "inceptionDate")
        }
      }
      it ("must correctly report defined behavior") {
        new TestData {
          app (behavior)
          assert (app.inceptionDate === behavior.inceptionDate)
        }
      }
    }

/*
Test releaseDate function.
*/

    describe (".releaseDate") {
      it ("must throw BehaviorUndefinedException if no behavior defined") {
        new TestData {
          val e = intercept [BehaviorUndefinedException] {
            app.releaseDate
          }
          assert (e.getMessage === undefined)
        }
      }
      it ("must throw a NoSuchElementException if not available") {
        new TestData {
          app (nullBehavior)
          val e = intercept [NoSuchElementException] {
            app.releaseDate
          }
          assertMessage (e, "releaseDate")
        }
      }
      it ("must correctly report defined behavior") {
        new TestData {
          app (behavior)
          assert (app.releaseDate === behavior.releaseDate)
        }
      }
    }

/*
Test copyright function.
*/

    describe (".copyright") {
      it ("must throw BehaviorUndefinedException if no behavior defined") {
        new TestData {
          val e = intercept [BehaviorUndefinedException] {
            app.copyright
          }
          assert (e.getMessage === undefined)
        }
      }
      it ("must throw a NoSuchElementException if not available") {
        new TestData {
          app (nullBehavior)

/*
The copyright field is built from the organization name, and the inception &
release date fields.  If any or all of these fields are missing, we'll get a
NoSuchElementException.  We do not test which field is complained about
(although it should be the organization field first), since it could be any of
them in reality.  Nor do we exhaustively test which field is complained about,
as it doesn't really matter - all that matters is that we get the exception.
*/

          intercept [NoSuchElementException] {
            app.copyright
          }
        }
      }
      it ("must correctly report defined behavior") {
        new TestData {
          app (behavior)
          assert (app.copyright === behavior.copyright)
        }
      }
    }

/*
Test organization function.
*/

    describe (".version") {
      it ("must throw BehaviorUndefinedException if no behavior defined") {
        new TestData {
          val e = intercept [BehaviorUndefinedException] {
            app.version
          }
          assert (e.getMessage === undefined)
        }
      }
      it ("must throw a NoSuchElementException if not available") {
        new TestData {
          app (nullBehavior)
          val e = intercept [NoSuchElementException] {
            app.version
          }
          assertMessage (e, "version")
        }
      }
      it ("must correctly report defined behavior") {
        new TestData {
          app (behavior)
          assert (app.version === behavior.version)
        }
      }
    }
  }
}