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

import org.facsim.facsimile.util.Resource

//=============================================================================
/**
Abstract base class for all measurement types.

Each subclass represents a single _measurement type_ - a physical quantity such
as _time_, _distance_, _velocity_, etc.

Each type supports one or more units of measure.  For example, time quantities
may be measured in _seconds_, _minutes_, _hours_, etc.  These units are
represented by instances of the implementing measurement type's class.  For
each measurement type, there is a standard unit of measure defined by the
_[http://en.wikipedia.org/wiki/International_System_of_Units International
System of Units]_ - commonly abbreviated as _SI_.

These standard units are used by _Facsimile_ internally to store measurement
quantities.  (Measurement quantities are stored as immutable instances of
[measure.Measure Measure] subclasses, with each subclass corresponding to each
measurement type.)  For example, the _SI_ standard unit of measure for _time_
is the _second_; consequently, _Facsimile_ stores and calculates all time
quantities in _seconds_ also.  Adoption of the _SI_ standard units simplifies
the implementation of physics calculations within _Facsimile_ and provides a
clearly-defined basis for developing simulation models of the real-world.
(Many other simulation modeling tools suffer from unit of measure confusion,
both internally and externally, creating a wide variety of problems.)

However, it is unreasonable to expect that _Facsimile_ users would be
comfortable entering and reviewing data solely in these units.  For instance,
the _SI_ standard unit of measure for _angles_ is the _radian_ - and there are
few people who don't find the _degree_ a far more intuitive alternative.
Similarly, users in the United States might prefer to use feet & inches,
pounds, Fahrenheit, etc. instead of their metric equivalents.  Consequently,
_Facsimile_ allows users to work with whichever units they - or their customers
or employers - prefer.  _Facsimile_ converts values to the standard _SI_ units
on input and converts them to the required units on output.

@param nameSingular Singular form of the unit name.  This name should be in the
closest match to the user's preferred locale.

@param namePlural Plural form of the unit name.  This name should be in the
closest match to the user's preferred locale.

@param symbol Symbol for this unit.  This name should be in the closest match
to the user's preferred locale.

@see [measure.Measure Measure]

@see [http://en.wikipedia.org/wiki/International_System_of_Units International
System of Units (Wikipedia)]
*/
//=============================================================================

private [measure] abstract class UnitOfMeasure (private val converter:
Converter, unitNameSingularKey: String, unitNamePluralKey: String,
unitSymbolKey: String)
extends NotNull with Converter
{

/*
Sanity checks.
*/

  require (!unitNameSingularKey.isEmpty ())
  require (!unitNamePluralKey.isEmpty ())
  require (!unitSymbolKey.isEmpty ())

/**
Singular form of this unit's name.
*/

  private val unitNameSingular = Resource.format (unitNameSingularKey)

/**
Plural form of this unit's name.
*/

  private val unitNamePlural = Resource.format (unitNamePluralKey)

/**
Symbol associated with this unit.
*/

  private val unitSymbol = Resource.format (unitSymbolKey)

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report the name of the associated measurement type.</p>

<p>The name should be reported in the singular form (e.g. "time", rather than
"times"), and should match the user's preferred locale as closely as possible.
If the type name is typically capitalized then the returned name should also be
capitalized, otherwise it should be returned in lower case (or the
locale-equivalent).</p>

<p>All units of measure for the same measurement type report the same type name
value.</p>

<p>The measurement type name should be unique across all measurement types.</p>

@return Name of this unit of measure's type.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  final def typeName () = Resource.format (typeNameKey ())

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report the name of this unit of measure in singular form.</p>

<p>The name should be reported in the singular form, and should match the
user's preferred locale as closely as possible.</p>

<p>If the name of the unit is typically capitalized (such as <em>Celsius</em>,
<em>Fahrenheit</em>, <em>Kelvin</em>, etc.) then the returned name should also
be capitalized, otherwise it should be returned in lower case (or the
locale-equivalent).</p>

<p>The singular form of the name should be unique across all units of measure,
but can be the same as the plural form of the same unit of measure.</p>

@return Singular form of this unit of measure's name.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  final def getUnitNameSingular () = unitNameSingular

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report the name of this unit of measure in plural form.</p>

<p>The name should be reported in the plural form, and should match the user's
preferred locale as closely as possible.</p>

<p>If the name of the unit is typically capitalized (such as <em>Celsius</em>,
<em>Fahrenheit</em>, <em>Kelvin</em>, etc.) then the returned name should also
be capitalized, otherwise it should be returned in lower case (or the
locale-equivalent).</p>

<p>The plural form of the name should be unique across all units of measure,
but can be the same as the singular form of the same unit of measure.</p>

@return Plural form of this unit of measure's name.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  final def getUnitNamePlural () = unitNamePlural

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report the symbol associated with this unit of measure.</p>

<p>The symbol should be unique across all units of measure.</p>

 @return Symbol associated with this unit of measure.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  final def symbol () = unitSymbol

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see org.facsim.facsimile.measure.Converter#importValue(Double)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private [measure] final override def importValue (value: Double): Double =
  converter.importValue (value)

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see org.facsim.facsimile.measure.Converter#exportValue(Double)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private [measure] final override def exportValue (value: Double): Double =
  converter.exportValue (value)

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report the resource key identifying the name of the associated measurement
type.</p>

<p>The name itself should be reported in the singular form (e.g. "time", rather
than "times"), and should match the user's preferred locale as closely as
possible.  If the type name is typically capitalized then the returned name
should also be capitalized, otherwise it should be returned in lower case (or
the locale-equivalent).</p>

<p>All units of measure for the same measurement type report the same type name
value.</p>

<p>The measurement type name should be unique across all measurement types.</p>

@return Name of this unit of measure's type.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  protected [measure] def typeNameKey (): String
}