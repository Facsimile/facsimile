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
// Scala source file belonging to the org.facsim.measure.phys package.
//======================================================================================================================
package org.facsim.measure

/** ''[[http://facsim.org/ Facsimile]]'' physical quantity measurement elements.
 *
 *  The `org.facsim.measure` package contains elements for expressing measurements of a variety of ''physical
 *  quantities'', such as ''time'', ''mass'', ''length'', ''angle'', ''thermodynamic temperature'', etc., in whatever
 *  units the user believes to be the most appropriate.
 *
 *  =Physical Quantity Unit Families=
 *
 *  For each physical quantity, there are typically a set of units in which measurements of that physical quantity can
 *  be expressed. For instance, ''time'' can be measured in ''seconds'', ''minutes'', or ''hours'', etc. In
 *  ''Facsimile'', such a set of units is termed a ''unit family''.
 *
 *  Each unit family is encapsulated as a subclass of the `[[Physical]]` abstract base class. For example, time units
 *  are encapsulated by the `[Time.TimeUnits]] Unit` subclass, mass units by the `[[Mass.MassUnits]] Unit` subclass,
 *  length units by the `[[Length.LengthUnits]] Unit` subclass, etc.
 *
 *  =Physical Quantity Units=
 *
 *  ''Facsimile'' allows users to work with whichever units make the most sense for a particular application, while
 *  interoperating seamlessly with code written using different units; conversion of values between different units in
 *  the same family takes place automatically, eliminating a whole class of conversion errors.
 *
 *  Individual physical quantity units are represented by instances of the corresponding unit family class. For example,
 *  ''seconds'', ''minutes'' and ''hours'' are represented by instances of the `[[Time.TimeUnits]]` class.
 *
 *  =Physical Quantity Measurements=
 *
 *  @todo Expand on Facsimile's use of units. Facsimile allows users to use one or more measurement units, as they see
 *  fit, rather than being forced to adopt a single unit of measure (say seconds for time, inches for length, etc.).
 *  Since all measurements are stored internally in SI units, no matter which units are employed by the user, there are
 *  never conversion issues when merging models that use different units.
 *
 *  @todo Expand on Facsimile's use of types for each measurement quantity. Instead of using a `[[Double]]`, like other
 *  simulation systems, Facsimile uses different types for each measurement quantity. Consequently, it's not possible to
 *  confuse a distance with a time, or a linear velocity with an angular velocity, or a mass with a temperature. By
 *  employing an appropriate set of operators, mechanics formulae can be accommodated by Facsimile in a very natural
 *  way. By ensuring data ranges are valid (no negative times or masses), Facsimile ensures data integrity throughout
 *  all runs.
 *
 *  @see [[http://en.wikipedia.org/wiki/SI International System of Units]] on [[http://en.wikipedia.org/ Wikipedia]].
 *
 *  @since 0.0
 */
package object phys {

}