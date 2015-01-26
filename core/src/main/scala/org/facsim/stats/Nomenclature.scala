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

//=============================================================================
/**
Trait defining strings for a variety of statistical terms.

This trait is employed by objects providing local-specific names and symbols
for these terms.

@since 0.0
*/
//=============================================================================

private [stats] trait Nomenclature {

/**
Population mean.

@since 0.0
*/

  def PopulationMean: String // scalastyle:ignore

/**
Estimate of population mean.

@since 0.0
*/

  def PopulationMeanEstimate: String // scalastyle:ignore

/**
Population standard deviation.

@since 0.0
*/

  def PopulationStandardDeviation: String // scalastyle:ignore

/**
Estimate of population standard deviation.

@since 0.0
*/

  def PopulationStandardDeviationEstimate: String // scalastyle:ignore

/**
Population variance.

@since 0.0
*/

  def PopulationVariance: String // scalastyle:ignore

/**
Estimate of population variance.

@since 0.0
*/

  def PopulationVarianceEstimate: String // scalastyle:ignore

/**
Sample minimum.

@since 0.0
*/

  def SampleMinimum: String // scalastyle:ignore

/**
Sample mean.

@since 0.0
*/

  def SampleMean: String // scalastyle:ignore

/**
Sample maximum.

@since 0.0
*/

  def SampleMaximum: String // scalastyle:ignore

/**
Sample standard deviation.

@since 0.0
*/

  def SampleStandardDeviation: String // scalastyle:ignore

/**
Sample variance.

@since 0.0
*/

  def SampleVariance: String // scalastyle:ignore
}