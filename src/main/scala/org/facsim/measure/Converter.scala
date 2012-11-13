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
Scala source file belonging to the org.facsim.measure package.
*/
//=============================================================================

package org.facsim.measure

//=============================================================================
/**
Unit of measurement value converter trait.

Converter implementations support the conversion of measurement quantity values
between different measurement units of the same type.  For example, converting
a ''time'' expressed in ''hours'' to a time expressed in ''seconds''.

More specifically, each converter instance is associated with a specific
measurement unit.  They support the conversion of values between this
associated measurement unit and the standard ''SI'' units for the associated
type of measurement.

Refer to [[org.facsim.measure.UnitOfMeasure UnitOfMeasure]] for further
information.

@see [[org.facsim.measure.UnitOfMeasure UnitOfMeasure]]

@see http://en.wikipedia.org/wiki/SI SI

@since 0.0
*/
//=============================================================================

trait Converter extends Conversion {

}