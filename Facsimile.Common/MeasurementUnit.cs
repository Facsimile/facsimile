/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2008, Michael J Allen.

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

C# source file for the MeasurementUnit class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Measurement unit family abstract base class.</summary>

<remarks>This class represents the concept of a family of measurement units;
each derived class represents a specific family (such as time units, distance
units, etc.).  Each derived class instance represents a specific measurement
unit within its family.  For example, a meter measurement unit would be
represented by an instance of the <see cref="DistanceUnit" /> class, which is
a subclass of <see cref="MeasurementUnit" />.

<para>Through using these classes, an application can support a number of
different measurement unit families - and specific units of measurement for
each family - allowing users to work in whatever units are most appropriate for
any particular task.</para>

<para>For each family, a "standard" measurement unit is selected; these units
are used to store all measurements, belonging to that family, internally.
These should be the International System (SI) of units' standard units (refer
to the SI unit listings at http://physics.nist.gov/cuu/Units/units.html for
more information).  Measurements are converted to/from other units - belonging
to the same family - on the fly, without any significant loss of
precision.</para>

<para>For additional background information on rates of conversion and other
issues, refer to the document "Guide for the Use of the International System of
Units (SI)", by B. N. Taylor, available at:
http://physics.nist.gov/Pubs/SP811/</para>

<para>The constructors for this class prevent classes outside of the
Facsimile.Common namespace from deriving from this.  This is a design
feature.</para></remarks>
*/
//=============================================================================

    public abstract class MeasurementUnit:
        System.Object
    {

/**
<summary>Standard unit map.</summary>

<remarks>Static map allowing the standard unit instance for each measurement
unit family (identified by the type of the derived class instance) to be
retrieved using the family's type information.

<para>This is implementation code intended for use internally by the Facsimile
library.  Do not expose to the end user.</para></remarks>
*/

        private static System.Collections.Generic.Dictionary <System.Type,
        MeasurementUnit> standardUnitMap;

/**
<summary>Conversion scaling factor.</summary>

<remarks>This value defines the number of standard units that are equivalent to
one of these units.

<para>If these are the standard units, then this value will be 1.0.</para>

<para>This value cannot be zero.</para></remarks>
*/

        private readonly double scaleFactor;

/**
<summary>Conversion origin offset.</summary>

<remarks>This value defines, in standard units, the distance, measured along
the Y-axis from the standard unit's origin to this unit's origin.  A negative
value indicates that these units have a lower origin than the standard units.

<para>If this value is zero, then this unit and the standard unit have a common
origin (that is, zero of the standard units is exactly zero of these units).
If these are the standard units, then this value will be zero.</para></remarks>
*/

        private readonly double originOffset;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static MeasurementUnit ()
        {

/*
Make sure that the map that is used to identify the standard measurement units
for each measurement unit family is correctly initialised.
*/

            System.Diagnostics.Debug.Assert (standardUnitMap == null);
            standardUnitMap = new System.Collections.Generic.Dictionary
            <System.Type, MeasurementUnit> ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve standard unit for a specific unit family.</summary>

<remarks>This is not a particularly efficient function, so store the result for
as long as it may be needed, whenever feasible.</remarks>

<param name="family">A <see cref="System.Type" /> instance identifying the <see
cref="MeasurementUnit" />-derived class representing the family of measurement
units for which the standard measurement unit is sought.</param>

<returns>A <see cref="MeasurementUnit" /> instance representing the standard
units for the indicated unit <paramref name="family" />.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static MeasurementUnit GetStandard (System.Type family)
        {

/*
This method suffers from a potential problem: if it is called before any static
member belonging to the "family" derived class, then it will fail to find its
standard units because the static constructor for that class will not have been
called.

That is, if my code contains:

    MeasurementUnit.GetStandard (typeof (TimeUnit))

and this line executes before any static member of TimeUnit is referenced, then
the function will return null because the static constructor (a.k.a. "type
initializer") for TimeUnit will not yet have been called.  Consequently, there
will be no units registered with this class, standard or otherwise.

To get around this problem, and to guarantee that the standardUnitMap will be
appropriately populated for this particular query, we must have the static
constructor executed - but only if it has not yet been executed!
*/

            Util.InitializeType (family);

/*
Return what we have for this family from the map.  (We need to lock access to
the map to assist with preventing another thread from modifying it whilst
we're reading it.)
*/

            System.Diagnostics.Debug.Assert (standardUnitMap != null);
            lock (standardUnitMap)
            {
                System.Diagnostics.Debug.Assert (standardUnitMap.ContainsKey
                (family), "Specified measurement unit family has yet to be " +
                "initialised, or no standard units have been defined for " +
                "this family.");
                return standardUnitMap [family];
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Standard measurement unit constructor.</summary>

<remarks>This constructor should only be used to create the standard
measurement unit for this family; non-standard measurement units should be
defined using the <see cref="MeasurementUnit (System.Double, System.Double)" />
constructor instead.  Furthermore, non-standard measurement units should not be
created until after the standard unit has been constructed.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        protected internal MeasurementUnit ():
            base ()
        {

/*
Verify that we do not already have any standard units.
*/

            System.Diagnostics.Debug.Assert (standardUnitMap != null);
            System.Diagnostics.Debug.Assert (!standardUnitMap.ContainsKey
            (GetType ()));

/*
These are the standard units, so the scale factor is one and the offset origin
is zero.  Verify that these units are recognised as the standard units.
*/

            scaleFactor = 1.0;
            originOffset = 0.0;
            System.Diagnostics.Debug.Assert (IsStandard);

/*
Verify that the minimum value is less than the maximum value.
*/

            System.Diagnostics.Debug.Assert (MinimumValue < MaximumValue);

/*
Finally, add these units to the map.
*/

            standardUnitMap [GetType ()] = this;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard measurement constructor.</summary>

<remarks>This constructor should only be used to create non-standard
measurement units for this family; standard measurement units should be defined
using the <see cref="MeasurementUnit ()" /> constructor instead.  Furthermore,
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

        protected internal MeasurementUnit (double unitScaleFactor, double
        unitOriginOffset):
            base ()
        {

/*
Get the reference to our standard units.
*/

            MeasurementUnit standardUnits = GetStandard (GetType ());
            System.Diagnostics.Debug.Assert (standardUnits != null);

/*
Store the settings.
*/

            System.Diagnostics.Debug.Assert (unitScaleFactor > 0.0);
            scaleFactor = unitScaleFactor;
            originOffset = unitOriginOffset;

/*
This constructor should not be used to create standard units - verify this.
*/

            System.Diagnostics.Debug.Assert (!IsStandard);

/*
Verify that the minimum value is less than the maximum value.
*/

            System.Diagnostics.Debug.Assert (MinimumValue < MaximumValue);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Are these units the standard units?</summary>

<remarks>Determine if this instance represents the standard units for the
measurement unit family represented by the class.</remarks>

<value>A <see cref="System.Boolean" /> value that is true if this instance
does represent the standard units of this class (measurement unit family), or
false otherwise.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsStandard
        {
            get
            {
                return (scaleFactor == 1.0 && originOffset == 0.0);
            }
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

        public virtual double MinimumValue
        {
            get
            {
                return double.NegativeInfinity;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Maximum possible unit value.</summary>

<remarks>This is the value, in these units, corresponding to the maximum
possible value in the standard units.</remarks>

<value>A <see cref="System.Double" /> representing the maximum posible value
supported by these units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public double MaximumValue
        {
            get
            {
                return double.PositiveInfinity;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if specified measurement value, in these units, is
valid.</summary>

<remarks>In order for a value to be value, it must be within the defined range
for the units.</remarks>

<param name="value">A <see cref="System.Double" /> value in these units to be
checked for validity.</param>

<returns>A <see cref="System.Boolean" /> that is true if <paramref name="Value"
/> is valid, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool IsValid (double value)
        {

/*
If the value is NaN, then it's not a valid value.
*/

            if (double.IsNaN (value)) {
                return false;
            }

/*
It's an invalid value if it's less than the minimum or greater than the maximum
allowed values.
*/

            if (value < MinimumValue || value > MaximumValue)
            {
                return false;
            }

/*
It must be valid.
*/

            return true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Convert a value in these units to standard units.</summary>

<remarks>This function is not intended for general use; it is primarily
intended for use within the Facsimile library.</remarks>

<param name="value">A <see cref="System.Double" /> value in these units to be
converted to standard units</param>

<returns>A <see cref="System.Double" /> representing the converted value.  If
these units are the standard units, then this value is equal to <paramref
name="value" />.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal double ToStandard (double value)
        {
            return value * scaleFactor + originOffset;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Convert a value in standard units to these units.</summary>

<remarks>This function is not intended for general use; it is primarily
intended for use within the Facsimile library.</remarks>

<param name="value">A <see cref="System.Double" /> value in standard units to
be converted to these units</param>

<returns>A <see cref="System.Double" /> representing the converted value.  If
these units are the standard units, then this value is equal to <paramref
name="value" />.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        internal double FromStandard (double value)
        {
            return (value - originOffset) / scaleFactor;
        }
    }
}
