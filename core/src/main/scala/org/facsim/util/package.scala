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
Scala source file defining the org.facsim.util package.
*/
//=============================================================================

package org.facsim

import java.time.ZonedDateTime
import java.util.GregorianCalendar
import scala.language.implicitConversions

//=============================================================================
/**
''[[http://facsim.org/ Facsimile]]'' Simulation Library miscellaneous
utilities.

Package providing miscellaneous utility elements.

@since 0.0
*/
//=============================================================================

package object util {

//-----------------------------------------------------------------------------
/**
Implicit conversion of a [[java.time.ZonedDateTime]] to a [[java.util.Date]].

For some reason best known only to themselves, conversion between older
`java.util` time classes (such as [[java.util.Date]],
[[java.util.GregorianCalendar]], etc.) and the new `java.time` time classes
[[java.time.Instant]], [[java.time.ZonedDateTime]], etc) is cumbersome at best.
The former could be dispensed with completly if if wasn't for the fact that
only the `java.util.Date` class is supported by [[java.text.MessageFormat]].

@param date Date, expressed as a [[java.time.ZonedDateTime]] to be converted.

@return `date` expressed as a [[java.util.Date]].

@since 0.0
*/
//-----------------------------------------------------------------------------

  implicit def toDate (date: ZonedDateTime) =
  GregorianCalendar.from (date).getTime ()
}