/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program.  If not, see <http://www.gnu.org/licenses/>.

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

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

C# source file for the NonNegativeMeasurementUnit class, and associated
elements, that are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Non-negative measurement unit family abstract base class.</summary>

<remarks>This class represents the concept of a family of measurement units
whose standard unit values cannot be negative; each derived class represents a
specific family (such as time units, mass units, etc.).  Each derived class
instance represents a specific measurement unit within its family.  For
example, a kilogram measurement unit would be represented by an instance of the
<see cref="MassUnit" /> class, which is a subclass of <see
cref="NonNegativeMeasurementUnit" />.

<para>Refer to the <see cref="MeasurementUnit" /> for further
information.</para></remarks>
*/
//=============================================================================

    public abstract class NonNegativeMeasurementUnit:
        MeasurementUnit
    {

/**
<summary>Minimum allowed value in these units.</summary>

<remarks>Measurements, in these units, that are less than this value are deemed
invalid.</remarks>
*/

        private readonly double minValue;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Standard non-negative measurement unit constructor.</summary>

<remarks>This constructor should only be used to create the standard
(non-negative) measurement unit for this family; non-standard (non-negative)
measurement units should be defined using the <see
cref="NonNegativeMeasurementUnit (System.Double, System.Double)" /> constructor
instead.  Furthermore, non-standard measurement units should not be created
until after the standard unit has been constructed.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected internal NonNegativeMeasurementUnit ():
            base ()
        {

/*
For non-negative measurement units, the minimum standard value must be zero.
*/

            minValue = 0.0;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard non-negative measurement constructor.</summary>

<remarks>This constructor should only be used to create non-standard
(non-negative) measurement units for this family; standard (non-negative)
measurement units should be defined using the <see
cref="NonNegativeMeasurementUnit ()" /> constructor instead.  Furthermore,
non-standard measurement units should not be created until after the standard
unit has been constructed.</remarks>

<param name="unitScaleFactor">A <see cref="System.Double" /> defining the
number of standard units corresponding to a single unit of these units.  This
value must be positive.</param>

<param name="unitOriginOffset">A <see cref="System.Double" /> defining the
distance, measured in standard units along the Y-axis, between the standard
units' origin and the origin for these units.  If this value is zero, then zero
of the standard units is exactly equal to zero of these units.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected internal NonNegativeMeasurementUnit (double unitScaleFactor,
        double unitOriginOffset):
            base (unitScaleFactor, unitOriginOffset)
        {

/*
Get the reference to our standard units.  (That we already have standard units
should have been verified by our base class, but - because we're paranoid -
we'll confirm that again with an assertion.)
*/

            MeasurementUnit standardUnits = GetStandard (GetType ());
            System.Diagnostics.Debug.Assert (standardUnits != null);

/*
For non-negative, non-standard measurement units, the minimum value is the
equivalent of the minimum value in standard units.
*/

            System.Diagnostics.Debug.Assert (standardUnits.MinimumValue ==
            0.0);
            minValue = FromStandard (standardUnits.MinimumValue);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Minimum possible unit value.</summary>

<remarks>This is the value, in these units, corresponding to the minimum
possible value in the standard units.</remarks>
 
<value>A <see cref="System.Double" /> representing the minimum posible value
supported by these units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public override double MinimumValue
        {
            get
            {
                return minValue;
            }
        }
    }
}
