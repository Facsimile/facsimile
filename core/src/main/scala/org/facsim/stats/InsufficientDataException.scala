/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2015, Michael J Allen.

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
Scala source file belonging to the org.facsim.stats package.
*/
//=============================================================================

package org.facsim.stats

import org.facsim.LibResource

//=============================================================================
/**
Exception indicating that a statistic cannot be reported due to insufficient
observed data.

@constructor Construct new insufficient data exception.

@param statisticName Name of the statistic for which there is insufficient data
to calculate a result.

@param minimumObservations Minimum number of observations that are required
before the associated statistic can be reported.

@param observations Number of observations made at the time the statistic was
requested.

@since 0.0
*/
//=============================================================================

final class InsufficientDataException private [stats]
(private final val statisticName: String,
private final val minimumObservations: Int,
private final val observations: Int)
extends RuntimeException {

//-----------------------------------------------------------------------------
/*
@see [[java.lang.Throwable!.getMessage()]]
*/
//-----------------------------------------------------------------------------

  override def getMessage = LibResource ("stats.InsufficientData",
  statisticName, minimumObservations, observations)
}