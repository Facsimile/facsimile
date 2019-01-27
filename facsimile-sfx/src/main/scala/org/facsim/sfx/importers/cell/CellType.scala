//======================================================================================================================
// Facsimile -- A Discrete-Event Simulation Library
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
// Scala source file belonging to the org.facsim.sfx.importers.cell package.
//======================================================================================================================
package org.facsim.sfx.importers.cell

/** Represent a specific ''cell'' type. */
private[cell] sealed trait CellType

/** ''Cell'' type companion. */
private[cell] object CellType {

  /** Map of ''cell'' type code to ''cell'' type instance. */
  private val codeToInstance: Map[Int, CellType] = Map(
    700 -> Set,
    7000 -> Set,
    10000 -> Set,
  )
  /** ''Cell'' type instance corresponding to the specified type code.
   *
   *  @param ct ''Cell'' type code.
   *
   *  @return ''Cell'' type instance corresponding to the code.
   *
   *  @throws scala.NoSuchElementException if `ct` is not a valid ''cell'' type code.
   */
  def apply(ct: Int): CellType = codeToInstance(ct)

  /** Verify the value of a ''cell'' type field.
   *
   *  Valid ''cell'' type values are the keys of the [[codeToInstance]] map.
   *
   *  @param ct ''Cell'' type code to be verified.
   *
   *  @return `true` if `ct` is a valid ''cell'' type code; `false` otherwise.
   */
  def verify(ct: Int): Boolean = codeToInstance.contains(ct)
}

/** ''Set'' cell type. */
private[cell] case object Set
extends CellType