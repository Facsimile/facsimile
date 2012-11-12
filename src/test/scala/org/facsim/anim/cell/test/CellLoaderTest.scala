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
$Id$

Scala source file from the org.facsim.anim.cell.test package.
*/
//=============================================================================

package org.facsim.anim.cell.test

import java.io.FileNotFoundException
import java.net.URL
import org.facsim.anim.cell.CellLoader
import org.scalatest.FunSpec
import org.scalatest.PrivateMethodTester

//=============================================================================
/**
Test suite for the [[org.facsim.anim.cell.CellLoader]] class.
*/
//=============================================================================

class CellLoaderTest extends FunSpec with PrivateMethodTester {

/**
Trait for default cell loader object.
*/

  trait DefaultCellLoader {
    val defaultLoader = new CellLoader ()
  }

/*
Test fixture description.
*/

  describe (classOf [CellLoader].getCanonicalName ()) {

/*
Default constructor tests.
*/

    describe (".this ()") {
      it ("must construct without error") {
        new DefaultCellLoader {
        }
      }
      it ("must report default load flags") {
        new DefaultCellLoader {
          assert (defaultLoader.getFlags () === 0)
        }
      }
      it ("must report null base path") {
        new DefaultCellLoader {
          assert (defaultLoader.getBasePath () === null)
        }
      }
      it ("must report null base url") {
        new DefaultCellLoader {
          assert (defaultLoader.getBaseUrl () === null)
        }
      }
    }

/*
load (String) tests.
*/

    describe (".load (String)") {
      it ("must throw NullPointerException if passed a null file name") {
        new DefaultCellLoader {
          val nullFile: String = null
          intercept [NullPointerException] {
            defaultLoader.load (nullFile)
          }
        }
      }
      it ("must throw FileNotFoundException if file does not exist") {
        new DefaultCellLoader {
          intercept [FileNotFoundException] {
            defaultLoader.load ("IBMissing.cell")
          }
        }
      }
    }

/*
load (URL) tests.
*/

    describe (".load (URL)") {
      it ("must throw NullPointerException if passed a null URL") {
        new DefaultCellLoader {
          val nullUrl: URL = null
          intercept [NullPointerException] {
            defaultLoader.load (nullUrl)
          }
        }
      }
      it ("must throw FileNotFoundException if file does not exist") {
        new DefaultCellLoader {
          intercept [FileNotFoundException] {
            defaultLoader.load (new URL ("file:///IBMissing.cell"))
          }
        }
      }
    }

/*
setFlags (Int) tests.

This function is deprecated.  We have to call it via reflection (using
ScalaTest's private method invoker) to avoid compiler warnings about calling a
deprecated function.
*/

    describe (".setFlags (Int) - DEPRECATED") {
      it ("must throw UnsupportedOperationException when called") {
        val setFlags = PrivateMethod [Unit] ('setFlags)
        new DefaultCellLoader {
          val thrown = intercept [UnsupportedOperationException] {
            defaultLoader invokePrivate setFlags (0)
          }
          assert (thrown.getMessage === "TODO I18N: CellLoader.setFlags () " +
          "is deprecated.")
        }
      }
    }

/*
setBasePath (String) tests.

This function is deprecated.  We have to call it via reflection (using
ScalaTest's private method invoker) to avoid compiler warnings about calling a
deprecated function.
*/

    describe (".setBasePath (String) - DEPRECATED") {
      it ("must throw UnsupportedOperationException when called") {
        val setBasePath = PrivateMethod [Unit] ('setBasePath)
        val default: String = "."
        new DefaultCellLoader {
          val thrown = intercept [UnsupportedOperationException] {
            defaultLoader invokePrivate setBasePath (default)
          }
          assert (thrown.getMessage === "TODO I18N: CellLoader.setBasePath () "
          + "is deprecated.")
        }
      }
    }

/*
setBaseUrl (String) tests.

This function is deprecated.  We have to call it via reflection (using
ScalaTest's private method invoker) to avoid compiler warnings about calling a
deprecated function.
*/

    describe (".setBaseUrl (String) - DEPRECATED") {
      it ("must throw UnsupportedOperationException when called") {
        val setBaseUrl = PrivateMethod [Unit] ('setBaseUrl)
        val default: URL = new URL ("http://facsim.org/")
        new DefaultCellLoader {
          val thrown = intercept [UnsupportedOperationException] {
            defaultLoader invokePrivate setBaseUrl (default)
          }
          assert (thrown.getMessage === "TODO I18N: CellLoader.setBaseUrl () "
          + "is deprecated.")
        }
      }
    }
  }
}