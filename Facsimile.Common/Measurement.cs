/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the:

    Free Software Foundation, Inc.
    51 Franklin St, Fifth Floor
    Boston, MA  02110-1301
    USA

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

C# source file for the Measurement <UnitType> generic class, and associated
elements, that are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common {

//=============================================================================
/**
<summary>Measurement.</summary>

<remarks>Generic class encapsulating measurements in any of the supported
measurement unit families.

<para>There are arguments for making this a class and others for making it a
struct.  The benefits of making it a class include being able to derive classes
with measurement-family-specific functions and properties (such as providing a
<c>sin ()</c> function for use with angle measurements) and disallowing default
constructors (needed if the standard units are to be arbitrary from the user's
viewpoint).  However, classes have higher overhead during calculations, and so
structs are a tempting alternative.  Given the pros and cons, expect changes to
be made prior to the 1.0 release being finalized.  If only it were possible to
derive new structs from existing structs...</para></remarks>

<typeparam name="FinalType">The ultimate type of measurement being handled by
this class; must be derived from <see cref="Measurement {FinalType, UnitType}"
/>.</typeparam>

<typeparam name="UnitType">Represents the family of measurement units to which
this measurement belongs; must be derived from <see cref="MeasurementUnit"
/>.</typeparam>
*/
//=============================================================================

    public abstract class Measurement <FinalType, UnitType>:
        System.Object, System.IComparable, System.IComparable <FinalType>,
        System.IEquatable <FinalType>
    where FinalType:
        Measurement <FinalType, UnitType>
    where UnitType:
        MeasurementUnit
    {

/**
<summary>The standard units for this measurement type.</summary>

<remarks>This is utilised to perform range checking, normalization and other
tasks.</remarks>
*/

        private static readonly UnitType standardUnits;

/**
<summary>This measurement's value.</summary>

<remarks>This value is stored in "standard units" (that are typically also the
SI standard units) for the corresponding measurement unit family.</remarks>
*/

        private double measurement;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static initialization.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Measurement ()
       {

/*
Get and cache the standard units for this measurement type.
*/

            standardUnits = (UnitType) MeasurementUnit.GetStandard (typeof
            (UnitType));
            System.Diagnostics.Debug.Assert (standardUnits != null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Measurement value constructor.</summary>

<remarks>Construct a new measurement with the indicated initial value, in the
specified units.</remarks>

<param name="initialValue">The initial value of the measurement in <paramref
name="units" /> units.</param>

<param name="units">Reference to the <typeparamref name="UnitType" /> instance
identifying the units for the initial value.  This reference cannot be
null.</param>

<exception cref="System.ArgumentOutOfRangeException">Thrown if the combination
of <paramref name="initialValue" /> and <paramref name="units" /> yields an
invalid value.</exception>

<exception cref="System.ArgumentNullException">Thrown if <paramref name="units"
/> is null.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Measurement (double initialValue, UnitType units)
        {

/*
Check that the type of this instance is FinalType.
*/

            System.Diagnostics.Debug.Assert (GetType () == typeof (FinalType));

/*
Ensure that the selected units are non-null.
*/

            if (units == null)
            {
                throw new System.ArgumentNullException ("units");
            }

/*
Verify that the value is valid.
*/

            if (!units.IsValid (initialValue))
            {

/*
It's not valid.  Report the minimum and maximum values in the chosen units as
part of the exception data.
*/

                throw new ArgumentOutOfRangeException <double> ("initialValue",
                units.MinValue, units.MaxValue, initialValue);
            }

/*
Store the standard unit version of the value.
*/

            measurement = units.ToStandard (initialValue);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Copy constructor.</summary>

<remarks>Construct a new measurement by copying the value of another
measurement.</remarks>

<param name="other">Reference to the <typeparamref name="FinalType" />-derived
measurement to be copied.</param>

<exception cref="System.ArgumentNullException">Thrown if <paramref name="other"
/> is null.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Measurement (FinalType other)
        {

/*
Check that the type of this instance is FinalType.
*/

            System.Diagnostics.Debug.Assert (GetType () == typeof (FinalType));

/*
Ensure that reference to the other measurement is not null.
*/

            if (other == null)
            {
                throw new System.ArgumentNullException ("other");
            }

/*
Just store the other's value - it must be valid.
*/

            Measurement <FinalType, UnitType> fudge = other;
            System.Diagnostics.Debug.Assert (standardUnits != null);
            System.Diagnostics.Debug.Assert (standardUnits.IsValid
            (fudge.measurement));
            measurement = fudge.measurement;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Generate a hash code for each measurement.</summary>

<remarks>Good hash functions improve performance when measurements are added to
hash tables.  The simplest hash function for measurements is to return the
hash code of the <see cref="System.Double" /> storing the measurement.  We can
only hope that this is a good hash function (if it's not, then we can do better
in the future).

<para>The .NET framework recommends overriding this method when the <see
cref="System.Object.Equals (System.Object)" /> method is overridden, to ensure
that two objects that are considered equal have the same hash code.  (Whilst it
is highly desirable, it cannot be relied upon that two objects with the same
hash code are equal.)</para>

<para>For further information, refer to <see cref="System.Object.GetHashCode
()" />.</para></remarks>

<returns>A <see cref="System.Int32" /> containing the hash code for this
type.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public override int GetHashCode ()
        {
            return measurement.GetHashCode ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if this object equals another object.</summary>

<remarks>Two measurements are considered equal if, and only if:

<list type="bullet">
    <item>
        <description>Both objects are non-null.</description>
    </item>
    <item>
        <description>Both objects are the same type (measurements of a specific
        family of measurement units).</description>
    </item>
    <item>
        <description>Both measurements have the same value in standard
        units.</description>
    </item>
</list>

<para>The .NET framework imposes additional constraints on this function. See
<see cref="System.Object.Equals (System.Object)" /> for further
information.</para>

<para>This equality method is less efficient than the <see
cref="Equals (FinalType)" /> method, provided that the type is
known.</para></remarks>

<param name="other">Reference to a <see cref="System.Object" /> instance that
is being compared for equality with this measurement.</param>

<returns>A <see cref="System.Boolean" /> value that is true if the two objects
are equal, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public sealed override bool Equals (System.Object other)
        {

/*
Convert the passed object to a FinalType reference.  If the converted value is
null, then either the object is not a measurement belonging to the same
measuement unit family as this object, or the passed object was null to begin
with; in either case, it cannot be equal to this measurement's value.  If the
other object is a comparable measurement, then they're still not equal if the
measurements are different values.
*/

//            FinalType otherMeasurement = other as FinalType;
            Measurement <FinalType, UnitType> otherMeasurement = other as
            FinalType;
            if (otherMeasurement == null || measurement !=
            otherMeasurement.measurement)
            {
                return false;
            }

/*
The two values must be equal.
*/

            return true;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if two measurements, from the same family, are
equal.</summary>

<remarks>Two measurements are equal provided that they are non null and have
the same value in the standard units.

<para>This function is required to implement the <see
cref="System.IEquatable {FinalType}" /> interface.</para></remarks>

<param name="other">Reference to a <typeparamref name="FinalType" /> instance
that is being compared for equality with this measurement.</param>

<returns>A <see cref="System.Boolean" /> value that is true if the two objects
are equal, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool Equals (FinalType other)
        {

/*
They're not equal if the other value is null or if the two measurements are not
equal.
*/

            Measurement <FinalType, UnitType> fudge = other;
            System.Diagnostics.Debug.Assert (GetType () == typeof (FinalType));
            if (fudge == null || measurement != fudge.measurement)
            {
                return false;
            }

/*
By the process of elimination, we can deduce that they are equal, Watson.
*/

            return true;
        }


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Compare two measurements from the same family.</summary>

<remarks>Two measurements are equal provided that they are non null and have
the same value in standard units.

<para>This function is required to implement the <see cref="System.IComparable"
/> interface.</para></remarks>

<param name="other">Reference to a <see cref="System.Object" /> instance
that is being compared to this measurement.  This value cannot be null and must
be a <typeparamref name="FinalType" /> reference.</param>

<returns>A <see cref="System.Int32" /> value that is negative if this is less
than the other measurement, 0 if the two measurements are equal, or positive if
this is greater than the other measurement.</returns>

<exception cref="System.ArgumentException">Thrown if <paramref name="other" />
is null or is not a <typeparamref name="FinalType" /> instance.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int CompareTo (System.Object other)
        {

/*
TODO: Write this function.
*/

            return 0;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Compare two measurements from the same family.</summary>

<remarks>Two measurements are equal provided that they are non null and have
the same value in standard units.

<para>This function is required to implement the <see cref="System.IComparable
{FinalType}" /> interface.</para></remarks>

<param name="other">Reference to a <typeparamref name="FinalType" /> instance
that is being compared to this measurement.  This value cannot be null.</param>

<returns>A <see cref="System.Int32" /> value that is negative if this is less
than the other measurement, 0 if the two measurements are equal, or positive if
this is greater than the other measurement.</returns>

<exception cref="System.ArgumentNullException">Thrown if <paramref name="other"
/> is null.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int CompareTo (FinalType other)
        {

/*
TODO: Write this function.
*/

            return 0;
        }
    }
}
