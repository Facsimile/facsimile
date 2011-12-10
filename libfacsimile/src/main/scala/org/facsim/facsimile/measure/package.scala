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

Scala source file defining the org.facsim.facsimile.measure package.
*/
//=============================================================================

package org.facsim.facsimile

//=============================================================================
/**
$facsimile simulation measurement elements.

The ''org.facsim.facsimile.measure'' package contains elements for measuring a
variety of physical quantities, such as ''time'', ''mass'', ''length'',
''angle'', ''temperature'', ''linear velocity'', etc. in the user's preferred
units.

@since 0.0-0

@todo Expand on $facsimile's use of units.  $facsimile allows users to use one
or more measurement units, as they see fit, rather than being forced to adopt a
single unit of measure (say seconds for time, inches for length, etc.).  Since
all measurements are stored internally in SI units, no matter which units are
employed by the user, there are never conversion issues when merging models
that use different units.

@todo Expand on $facsimile's use of types for each measurement quantity.
Instead of using a Double, like other simulation systems, $facsimile uses
different types for each measurement quantity.  Consequently, it's not possible
to confuse a distance with a time, or a linear velocity with an angular
velocity, or a mass with a temperature.  By employing an appropriate set of
operators, mechanics formulae can be accommodated by $facsimile in a very
natural way.  By ensuring data ranges are valid (no negative times or masses),
$facsimile ensures data integrity throughout all runs.

@define measure ''[[org.facsim.facsimile.measure]]''

@define si ''[[http://en.wikipedia.org/wiki/International_System_of_Units
SI]]''

@define siFull ''[[http://en.wikipedia.org/wiki/International_System_of_Units
International System of Units (Wikipedia)]]''
*/
//=============================================================================

package object measure
{
}
