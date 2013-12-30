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
Scala source file from the org.facsim.anim.cell.test package.
*/
//=============================================================================

package org.facsim.anim.cell.test

import java.io.FileNotFoundException
import java.net.URL
import org.facsim.anim.cell.CellLoader
import org.scalatest.FunSpec
import scala.annotation.tailrec

//=============================================================================
/**
Test suite for the [[org.facsim.anim.cell.CellLoader]] class.
*/
//=============================================================================

class CellLoaderTest extends FunSpec {

/**
Function to retrieve a test resource file's URL.

Files should be present in src/test/resources/cellFiles.
*/

  def testRscFile (file: String) = getClass.getResource("cellFiles/" + file)

/**
Trait populated with cell file URL's to be loaded.
*/

  trait CellFiles {
    val files = List (
      testRscFile ("ArcCoarse.cell"),
      testRscFile ("ArcCoarseReverse.cell"),
      testRscFile ("ArcFine.cell"),
      testRscFile ("ArcFineReverse.cell"),
      testRscFile ("CircleCoarseSolid.cell"),
      testRscFile ("CircleCoarseWire.cell"),
      testRscFile ("CircleFineSolid.cell"),
      testRscFile ("CircleFineWire.cell"),
      testRscFile ("ConeCoarse.cell"),
      testRscFile ("ConeFine.cell"),
      testRscFile ("CylinderCoarse.cell"),
      testRscFile ("CylinderFine.cell"),
      testRscFile ("FrustumCoarse.cell"),
      testRscFile ("FrustumFine.cell"),
      testRscFile ("HemisphereCoarse.cell"),
      testRscFile ("HemisphereFine.cell"),
      testRscFile ("ScreenFastText.cell"),
      testRscFile ("ScreenNormalText.cell"),
      testRscFile ("SectorCoarse.cell"),
      testRscFile ("SectorCoarseReverse.cell"),
      testRscFile ("SectorFine.cell"),
      testRscFile ("SectorFineReverse.cell"),
      testRscFile ("Tetrahedron.cell"),
      testRscFile ("Trapezoid.cell"),
      testRscFile ("Triad.cell"),
      testRscFile ("UnrotateFastText.cell"),
      testRscFile ("UnrotateNormalText.cell"),
      testRscFile ("WorldText.cell")
    )
  }

/*
Test fixture description.
*/

  describe (CellLoader.getClass.getCanonicalName) {

/*
Cell file load tests.
*/

    describe (".load (URL, SafeOption[URL], CellColor.Value, CellColor.Value)")
    {
      it ("must load all files with default arguments") {
        new CellFiles {
          @tailrec
          def loadFile (list: List[URL]): Unit = {
            if (list.isEmpty) {
              CellLoader.load (list.head)
              loadFile (list.tail)
            }
          }
          loadFile (files)
        }
      }
    }
  }
}