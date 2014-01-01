/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

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

import org.facsim.anim.cell.CellFlags
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.anim.cell.CellFlags!]] class.
*/
//=============================================================================

class CellFlagsTest extends FunSpec {

/*
Test data.
*/

  trait TestData {
    val none = new CellFlags (0x0)
    val bit0 = new CellFlags (0x1)
    val bit1 = new CellFlags (0x2)
    val bit2 = new CellFlags (0x4)
    val bit3 = new CellFlags (0x8)
    val bit4 = new CellFlags (0x10)
    val bit5 = new CellFlags (0x20)
    val bit6 = new CellFlags (0x40)
    val bits0248 = new CellFlags (0x55)
    val bits135 = new CellFlags (0x2A)
    val with0 = List (bit0, bits0248)
    val without0 = List (none, bit1, bit2, bit3, bit4, bit5, bit6, bits135)
    val with1 = List (bit1, bits135)
    val without1 = List (none, bit0, bit2, bit3, bit4, bit5, bit6, bits0248)
    val with2 = List (bit2, bits0248)
    val without2 = List (none, bit0, bit1, bit3, bit4, bit5, bit6, bits135)
    val with3 = List (bit3, bits135)
    val without3 = List (none, bit0, bit1, bit2, bit4, bit5, bit6, bits0248)
    val with4 = List (bit4, bits0248)
    val without4 = List (none, bit0, bit1, bit2, bit3, bit5, bit6, bits135)
    val with6 = List (bit6, bits0248)
    val without6 = List (none, bit0, bit1, bit2, bit3, bit4, bit5, bits135)
  }

/*
Test fixture description.
*/

  describe (classOf [CellFlags].getCanonicalName) {

/*
Test the attributes present method (bit 0).
*/

    describe (".attributesPresent") {
      new TestData {
        it ("must return true if bit 0 set") {
          with0.foreach {
            flags =>
            assert (flags.attributesPresent === true)
          }
        }
        it ("must return false if bit 0 clear") {
          without0.foreach {
            flags =>
            assert (flags.attributesPresent === false)
          }
        }
      }
    }

/*
Test the joint data present method (bit 1).
*/

    describe (".jointDataPresent") {
      new TestData {
        it ("must return true if bit 1 set") {
          with1.foreach {
            flags =>
            assert (flags.jointDataPresent === true)
          }
        }
        it ("must return false if bit 1 clear") {
          without1.foreach {
            flags =>
            assert (flags.jointDataPresent === false)
          }
        }
      }
    }

/*
Test the geometry data present method (bit 2).
*/

    describe (".geometryDataPresent") {
      new TestData {
        it ("must return true if bit 2 set") {
          with2.foreach {
            flags =>
            assert (flags.geometryDataPresent === true)
          }
        }
        it ("must return false if bit 2 clear") {
          without2.foreach {
            flags =>
            assert (flags.geometryDataPresent === false)
          }
        }
      }
    }

/*
Test the geometry data in matrix form method (bit 3).
*/

    describe (".geometryDataInMatrixForm") {
      new TestData {
        it ("must return true if bit 3 set") {
          with3.foreach {
            flags =>
            assert (flags.geometryDataInMatrixForm === true)
          }
        }
        it ("must return false if bit 3 clear") {
          without3.foreach {
            flags =>
            assert (flags.geometryDataInMatrixForm === false)
          }
        }
      }
    }

/*
Test the colors inherited method (bit 4).
*/

    describe (".colorsInherited") {
      new TestData {
        it ("must return true if bit 4 set") {
          with4.foreach {
            flags =>
            assert (flags.colorsInherited === true)
          }
        }
        it ("must return false if bit 4 clear") {
          without4.foreach {
            flags =>
            assert (flags.colorsInherited === false)
          }
        }
      }
    }

/*
Test the bounding box present method (bit 6).
*/

    describe (".boundingBoxPresent") {
      new TestData {
        it ("must return true if bit 6 set") {
          with6.foreach {
            flags =>
            assert (flags.boundingBoxPresent === true)
          }
        }
        it ("must return false if bit 6 clear") {
          without6.foreach {
            flags =>
            assert (flags.boundingBoxPresent === false)
          }
        }
      }
    }
  }
}