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

Scala source file belonging to the org.facsim.facsimile.measure package.
 */
//=============================================================================

package org.facsim.facsimile.measure

//=============================================================================
/**
Linear factor unit of measure value converter.

Converts between measurement values between different units (belonging to the
same measurement type) using a linear factor.

To import a value, that is, to convert a measurement quantity's value from the
units associated with this converter to a value in the standard
''[[http://en.wikipedia.org/wiki/International_System_of_Unitsv SI]]'' units
for the associated measurement type, the value is multiplied by the given
'''factor'''.

To export a value, that is, to convert a measurement quantity's value from the
standard ''SI'' units for the associated measurement type to a value in the units
associated with this converter, the value is divided by the given '''factor'''.

@param factor Linear conversion factor.  This value cannot be zero.

@since 0.0-0
*/
//=============================================================================

private [measure] final class LinearFactorConverter (factor: Double)
extends Converter
{

/*
Verify the factor's value is not zero.
*/

  require (factor != 0.0d)

//-----------------------------------------------------------------------------
/*
@see [measure.Converter.importValue(Double)]
*/
//-----------------------------------------------------------------------------

  private [measure] final override def importValue (value: Double): Double =
  value * factor

//-----------------------------------------------------------------------------
/*
@see [measure.Converter.exportValue(Double)]
*/
//-----------------------------------------------------------------------------

  private [measure] final override def exportValue (value: Double): Double =
  value / factor
}
