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

C# source file for the Measure <UnitType> generic struct, and associated
elements, that are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Measure.</summary>

<remarks>Generic struct encapsulating measurements in any of the supported
measurement unit families.

<para>The C# compiler will automatically generate a public, default constructor
for this struct; it is illegal to define an explicit parameterless (i.e.
"default") constructor.  This default constructor initializes the measure to
zero standard units.  One side-effect of this is that, for certain families of
measurements that do not have a common origin for all supported units (the
most obvious is the <see cref="TemperatureUnit" /> family), the standard units
are not arbitrary from the user's perspective.  Another problem is that we
cannot assert whether a zero value, in standard units, is even a value valid in
standard units.  Consequently, although it cannot be enforced, the use of the
default constructor is strongly discouraged.</para>

<para>This struct does not provide an implicit conversion from <see
cref="System.Double" /> values to <see cref="Measure {UnitType}" /> values,
because the units used for the conversion cannot be determined, resulting in
the standard units being used.  Allowing such a conversion would mean that the
standard units would not be arbitrary from the user's perspective.  For the
same reason, there is no explicit conversion from a <see cref="Measure
{UnitType}" /> value to a <see cref="System.Double" /> value.</para>

<para>There are arguments for making this a class and others for making it a
struct.  The benefits of making it a class include being able to derive classes
with measurement-family-specific functions and properties (such as providing a
<c>sin ()</c> function for use with angle measurements) and disallowing default
constructors (needed if the standard units are to be arbitrary from the user's
viewpoint).  However, classes have higher overhead during calculations, allow
null references that must be filtered out, and do not obey the usual value
assignment rules, and so structs are a tempting alternative.  Given the pros
and cons, expect changes to be made prior to the 1.0 release being finalized.
If only it were possible to derive new structs from existing
structs...</para></remarks>

<typeparam name="UnitType">Represents the family of measurement units to which
this measure belongs; must be derived from <see cref="MeasurementUnit"
/>.</typeparam>
*/
//=============================================================================

    public struct Measure <UnitType>:
        System.IComparable, System.IComparable <Measure <UnitType>>,
        System.IEquatable <Measure <UnitType>>
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
<summary>Value representing the relative significance of a number.</summary>

<remarks>This is used to create a default tolerance when comparing two
measurements.  When comparing two numbers, if the difference between them is
less than or equal to the first value multiplied by this constant, then they
will be considered approximately equal.</remarks>
*/
// TODO: The description and name of this constant should be improved.

        private const double significance = 1.0e-15;

/**
<summary>This measure's value.</summary>

<remarks>This value is stored in "standard units" (that are typically also the
SI standard units) for the corresponding measurement unit family.</remarks>
*/

        private double measure;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static initialization.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Measure ()
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
<summary>Unary plus operator.</summary>

<remarks>From the perspective of this struct, this is a
non-operation: for any <see cref="Measure {UnitType}" /> value X, X and +X are
identical.</remarks>

<param name="value">The <see cref="Measure {UnitType}" /> value to be
considered.</param>

<returns>The unmodified <paramref name="value" /> value.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static Measure <UnitType> operator + (Measure <UnitType> value)
        {
            return value;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Unary minus operator.</summary>

<remarks>This operation converts the measure by changing it's sign.  Note that,
for some measurement unit families, this results in an invalid value.</remarks>

<param name="value">The <see cref="Measure {UnitType}" /> value whose sign is
to be changed.</param>

<returns>A new <see cref="Measure {UnitType}" /> value whose absolute value is
the same as <paramref name="value" />, but whose sign is opposite.</returns>

<exception cref="System.OverflowException">Thrown if the resulting standard
measurement value is not valid for the associated family of measurement
units.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static Measure <UnitType> operator - (Measure <UnitType> value)
        {

/*
Verify that the resulting value is valid.  If it is not, then throw an overflow
exception.
*/

            double newMeasure = -value.measure;
            CheckValid (newMeasure);

/*
Return a new measure with the specified value.
*/

            return new Measure <UnitType> (newMeasure);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Addition operator.</summary>

<remarks>Adds two measurements from the same family together.  The result is
then checked for an overflow condition.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> operand.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> operand.</param>

<returns>A new <see cref="Measure {UnitType}" /> value this is the sum of
<paramref name="a" /> and <paramref name="b" />.</returns>

<exception cref="System.OverflowException">Thrown if the resulting standard
measurement value is not valid for the associated family of measurement
units.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static Measure <UnitType> operator + (Measure <UnitType> a,
        Measure <UnitType> b)
        {

/*
Perform the addition and check that the value is value.  If it is not valid,
then throw an overflow exception.
*/

            double newMeasure = a.measure + b.measure;
            CheckValid (newMeasure);

/*
Return a new measure with the specified value.
*/

            return new Measure <UnitType> (newMeasure);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Subtraction operator.</summary>

<remarks>Subtracts one measurement from another measurement from the same
family.  The result is then checked for an overflow condition.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> operand.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> operand.</param>

<returns>A new <see cref="Measure {UnitType}" /> value this is the result of
<paramref name="a" /> minus <paramref name="b" />.</returns>

<exception cref="System.OverflowException">Thrown if the resulting standard
measurement value is not valid for the associated family of measurement
units.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static Measure <UnitType> operator - (Measure <UnitType> a,
        Measure <UnitType> b)
        {

/*
Perform the subtraction and check that the value is value.  If it is not valid,
then throw an overflow exception.
*/

            double newMeasure = a.measure - b.measure;
            CheckValid (newMeasure);

/*
Return a new measure with the specified value.
*/

            return new Measure <UnitType> (newMeasure);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Equality operator.</summary>

<remarks>Compares two <see cref="Measure {UnitType}" /> values for
equality.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> value to be
compared.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> value to be
compared.</param>

<returns>A <see cref="System.Boolean" /> that is true if both <paramref
name="a" /> and <paramref name="b" /> have the same measurement value, or false
otherwise.</returns>

<seealso cref="Equals (System.Object)" />

<seealso cref="Equals (Measure {UnitType})" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool operator == (Measure <UnitType> a, Measure
        <UnitType> b)
        {

/*
Have the Equals method do the hard work.
*/

            return (a.Equals (b));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Inequality operator.</summary>

<remarks>Compares two <see cref="Measure {UnitType}" /> value for
inequality.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> value to be
compared.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> value to be
compared.</param>

<returns>A <see cref="System.Boolean" /> that is true if <paramref name="a" />
or <paramref name="b" /> have different values, or false otherwise.</returns>

<seealso cref="Equals (System.Object)" />

<seealso cref="Equals (Measure {UnitType})" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool operator != (Measure <UnitType> a, Measure
        <UnitType> b)
        {

/*
Have the Equals method do the hard work.
*/

            return !(a.Equals (b));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Less-than operator.</summary>

<remarks>Determine if <see cref="Measure {UnitType}" /> <paramref name="a" />
is less than <see cref="Measure {UnitType}" /> <paramref name="b" />.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> value to be
compared.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> value to be
compared.</param>

<returns>A <see cref="System.Boolean" /> that is true if <paramref name="a"
/>'s measurement value is less than <paramref name="b" /> measurement value, or
false otherwise.</returns>

<seealso cref="CompareTo (Measure {UnitType})" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool operator < (Measure <UnitType> a, Measure <UnitType>
        b)
        {

/*
Have the CompareTo method do the hard work.
*/

            return (a.CompareTo (b) < 0);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Greater-than operator.</summary>

<remarks>Determine if <see cref="Measure {UnitType}" /> <paramref name="a" />
is greater than <see cref="Measure {UnitType}" /> <paramref name="b"
/>.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> value to be
compared.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> value to be
compared.</param>

<returns>A <see cref="System.Boolean" /> that is true if <paramref name="a"
/>'s measurement value is greater than <paramref name="b" /> measurement value,
or false otherwise.</returns>

<seealso cref="CompareTo (Measure {UnitType})" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool operator > (Measure <UnitType> a, Measure <UnitType>
        b)
        {

/*
Have the CompareTo method do the hard work.
*/

            return (a.CompareTo (b) > 0);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Less-than-or-equal-to operator.</summary>

<remarks>Determine if <see cref="Measure {UnitType}" /> <paramref name="a" />
is less than or equal to <see cref="Measure {UnitType}" /> <paramref name="b"
/>.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> value to be
compared.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> value to be
compared.</param>

<returns>A <see cref="System.Boolean" /> that is true if <paramref name="a"
/>'s measurement value is less than or equal to <paramref name="b" />
measurement value, or false otherwise.</returns>

<seealso cref="CompareTo (Measure {UnitType})" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool operator <= (Measure <UnitType> a, Measure
        <UnitType> b)
        {

/*
Have the CompareTo method do the hard work.
*/

            return (a.CompareTo (b) <= 0);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Greater-than-or-equal-to operator.</summary>

<remarks>Determine if <see cref="Measure {UnitType}" /> <paramref name="a" />
is greater than or equal to <see cref="Measure {UnitType}" /> <paramref
name="b" />.</remarks>

<param name="a">The first <see cref="Measure {UnitType}" /> value to be
compared.</param>

<param name="b">The second <see cref="Measure {UnitType}" /> value to be
compared.</param>

<returns>A <see cref="System.Boolean" /> that is true if <paramref name="a"
/>'s measurement value is greater than or equal to <paramref name="b" />
measurement value, or false otherwise.</returns>

<seealso cref="CompareTo (Measure {UnitType})" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static bool operator >= (Measure <UnitType> a, Measure
        <UnitType> b)
        {

/*
Have the CompareTo method do the hard work.
*/

            return (a.CompareTo (b) >= 0);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check a standard unit value for validity.</summary>

<remarks>This method should be called to validate new measurement values; an
exception is thrown if the value is invalid.</remarks>

<param name="value">A <see cref="System.Double" /> standard unit value to be
checked for validity.</param>

<exception cref="MeasureOverflowException">Thrown if the specified value is
invalid.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void CheckValid (double value)
        {

/*
If the value is not valid, throw the exception.  It's as simple as that!
*/

            System.Diagnostics.Debug.Assert (standardUnits != null);
            if (!standardUnits.IsValid (value))
            {
                throw new MeasureOverflowException (standardUnits, value);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Measure value constructor.</summary>

<remarks>Construct a new measure with the indicated initial value, in the
standard units.

<para>This constructor is private to prevent general use.  Being able to create
a new measure without specifying the corresponding units (and therefore relying
upon allegedly "arbitrary" standard units) breaks the condition that the user
need not know about the underlying implementation.</para></remarks>

<param name="initialValue">The initial value of the measure in standard
units.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private Measure (double initialValue)
        {

/*
If the value in these units is invalid, report the minimum and maximum values
in the chosen units as part of the resulting exception data.
*/

            System.Diagnostics.Debug.Assert (standardUnits != null);
            System.Diagnostics.Debug.Assert (standardUnits.IsValid
            (initialValue));

/*
Store the value.
*/

            measure = initialValue;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Measure value constructor.</summary>

<remarks>Construct a new measure with the indicated initial value, in the
specified units.</remarks>

<param name="initialValue">The initial value of the measure in <paramref
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

        public Measure (double initialValue, UnitType units)
        {

/*
Ensure that the selected units are non-null.
*/

            if (units == null)
            {
                throw new System.ArgumentNullException ("units");
            }

/*
If the value in these units is invalid, report the minimum and maximum values
in the chosen units as part of the resulting exception data.
*/

            if (!units.IsValid (initialValue))
            {
                throw new ArgumentOutOfRangeException <double> ("initialValue",
                units.MinimumValue, units.MaximumValue, initialValue);
            }

/*
Store the standard unit version of the value.
*/

            measure = units.ToStandard (initialValue);
            System.Diagnostics.Debug.Assert (standardUnits.IsValid (measure));
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Copy constructor.</summary>

<remarks>Construct a new measure by copying the value of another
measure.</remarks>

<param name="other">The <see cref="Measure {UnitType}" /> measurement value to
be copied.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Measure (Measure <UnitType> other)
        {

/*
Just store the other's value - it must be valid.
*/

            System.Diagnostics.Debug.Assert (standardUnits != null);
            System.Diagnostics.Debug.Assert (standardUnits.IsValid
            (other.measure));
            measure = other.measure;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Generate a hash code for each measure.</summary>

<remarks>Good hash functions improve performance when measurements are added to
hash tables.  The simplest hash function for measurements is to return the hash
code of the <see cref="System.Double" /> storing the measurement.  We can only
hope that this is a good hash function (if it's not, then we can do better in
the future).

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
            return measure.GetHashCode ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if two measurements (one boxed as a <see
cref="System.Object" /> reference), from the same family, are equal.</summary>

<remarks>Two measurements are considered equal if, and only if:

<list type="bullet">
    <item>
        <description>Both measurements are non-null.</description>
    </item>
    <item>
        <description>Both measurements are the same type (measurements of a
        specific family of measurement units).</description>
    </item>
    <item>
        <description>Both measurements have the same value in standard
        units.</description>
    </item>
</list>

<para>The .NET framework imposes additional constraints on this function. See
<see cref="System.Object.Equals (System.Object)" /> for further
information.</para>

<para>This Equals method is less efficient than the <see cref="Equals
(Measure {UnitType})" /> method, provided that the type is known.</para>

<para>The default version of this method, provided by the <see
cref="System.ValueType" /> class, uses reflection to compare the members of the
class.  Whilst this works, this overridden version is more efficient since it
does not require reflection to determine the struct members.</para></remarks>

<param name="other">Reference to a <see cref="System.Object" /> instance that
is being compared for equality with this measurement.  This must be a boxed
instance of a <see cref="Measure {UnitType}" /> value.</param>

<returns>A <see cref="System.Boolean" /> value that is true if the two objects
are equal, or false otherwise.</returns>

<seealso cref="Equals (Measure {UnitType})" />
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public override bool Equals (System.Object other)
        {

/*
If the other object is null, then return false.
*/

            if (other == null)
            {
                return false;
            }

/*
Attempt to unbox the passed reference as a Measure <UnitType> value.  If an
invalid cast exception is thrown, then the passed object is not a boxed Measure
<UnitType> value and the two objects are not equal.
*/

            Measure <UnitType> otherMeasure;
            try
            {
                otherMeasure = (Measure <UnitType>) other;
            }
            catch (System.InvalidCastException)
            {
                return false;
            }

/*
Compare the two measurements (we'll use the Equals method for the measurement
itself).
*/

            return measure.Equals (otherMeasure.measure);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine if two measurements, from the same family, are
equal.</summary>

<remarks>Two measurements are equal provided that they have the same value in
the standard units.

<para>This function is required to implement the <see cref="System.IEquatable
{T}" /> interface.</para></remarks>

<param name="other">A <see cref="Measure {UnitType}" /> value that is being
compared for equality with this measurement's value.</param>

<returns>A <see cref="System.Boolean" /> value that is true if the two objects
are equal, or false otherwise.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public bool Equals (Measure <UnitType> other)
        {

/*
Compare the two measurements.
*/

            return measure.Equals (other.measure);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Compare two measurements from the same family.</summary>

<remarks>The two measurements' values will provide the sole basis for the
comparison.

<para>According to the Microsoft definition, all non-null values are, by
definition, greater than null.  Consequently, the function should return a
positive value if <paramref name="other" /> is null.</para>

<para>This function is required to implement the <see cref="System.IComparable"
/> interface.</para></remarks>

<param name="other">Reference to a <see cref="System.Object" /> instance that
is being compared for equality with this measurement.  This must be a boxed
instance of a <see cref="Measure {UnitType}" /> value.</param>

<returns>A <see cref="System.Int32" /> value that is negative if this is less
than the other measurement, 0 if the two measurements are equal, or positive if
this is greater than the other measurement (including the case where the other
argument is null).</returns>

<exception cref="System.ArgumentException">Thrown if <paramref name="other" />
is not a <see cref="Measure {UnitType}" /> instance.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int CompareTo (System.Object other)
        {

/*
If the other reference is null, then this measurement is (by definition)
greater than it.
*/

            if (other == null) {
                return 1;
            }

/*
Attempt to unbox the passed reference as a Measure <UnitType> value.  If an
invalid cast exception is thrown, then the passed object is not a boxed
Measure <UnitType> value and so the exception should be thrown.

NB: We cannot use the "as" operator, because there is no such thing as a null
struct value.
*/

            Measure <UnitType> otherMeasure;
            try
            {
                otherMeasure = (Measure <UnitType>) other;
            }
            catch (System.InvalidCastException)
            {
                throw new ArgumentTypeException ("other", typeof
                (System.Object), typeof (Measure <UnitType>), other.GetType
                ());
            }

/*
So.  Return a comparison of the two measurements.
*/

            return measure.CompareTo (otherMeasure.measure);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tolerantly compare two measurements from the same family.</summary>

<remarks>The absolute value of the two measurements will be compared to the
smallest possible <see cref="System.Double" /> value.  If the absolute value is
less than or equal to this value, then the two measurements will be considered
equal.  This is an attempt to overcome the problem of comparing floating point
values that differ solely as the result of rounding errors.

<para>This function cannot be used to implement the <see
cref="System.IEquatable {Type}" /> interface since "equal" objects also require
an identical hash code.  If the values are different, and only approximately
equal, then the hash codes will be different also.  Attempting to force
identical hash codes is a monumentally difficult problem.</para></remarks>

<param name="other">A <see cref="Measure {UnitType}" /> instance that is being
compared to this measurement.</param>

<returns>A <see cref="System.Int32" /> value that is negative if this is less
than the other measurement, 0 if the two measurements are approximately equal,
or positive if this is greater than the other measurement.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int TolerantCompareTo (Measure <UnitType> other)
        {
            return TolerantCompareTo (other.measure, measure * significance);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tolerantly compare two measurements from the same family.</summary>

<remarks>The absolute value of the two measurements will be compared to a
tolerance value.  If the absolute value is less than or equal to the tolerance
value, then the two measurements will be considered equal.  This is an attempt
to overcome the problem of comparing floating point values that differ solely
as the result of rounding errors.

<para>This function cannot be used to implement the <see
cref="System.IEquatable {Type}" /> interface since "equal" objects also require
an identical hash code.  If the values are different, and only approximately
equal, then the hash codes will be different also.  Attempting to force
identical hash codes is a monumentally difficult problem.</para></remarks>

<param name="other">A <see cref="Measure {UnitType}" /> instance that is being
compared to this measurement.</param>

<param name="tolerance">A <see cref="Measure {UnitType}" /> instance that acts
as the tolerance for the comparison.  If the absolute difference between this
measurement and the other measurement is less than this tolerance, then they
will be considered equal by this function.  Note that this tolerance must
have a non-negative standard value.</param>

<returns>A <see cref="System.Int32" /> value that is negative if this is less
than the other measurement, 0 if the two measurements are approximately equal,
or positive if this is greater than the other measurement.</returns>

<exception cref="System.ArgumentOutOfRangeException">Thrown if the <paramref
name="tolerance" /> measurement's standard value is negative.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int TolerantCompareTo (Measure <UnitType> other, Measure
        <UnitType> tolerance)
        {

/*
If the measurement is negative, then throw an exception.
*/

            if (tolerance.measure < 0.0)
            {
                throw new ArgumentOutOfRangeException <double> ("tolerance",
                0.0, double.PositiveInfinity, tolerance.measure);
            }

/*
Perform the comparison.
*/

            return TolerantCompareTo (other.measure, tolerance.measure);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Tolerantly compare two measurements from the same family.</summary>

<remarks>The absolute value of the two measurements will be compared to a
tolerance value.  If the absolute value is less than or equal to the tolerance
value, then the two measurements will be considered equal.  This is an attempt
to overcome the problem of comparing floating point values that differ solely
as the result of rounding errors.</remarks>

<param name="otherMeasure">A <see cref="System.Double" /> value that is being
compared to this measurement.</param>

<param name="tolerance">A <see cref="System.Double" /> value that acts
as the tolerance for the comparison.  If the absolute difference between this
measurement and the other measurement is less than this tolerance, then they
will be considered equal by this function.</param>

<returns>A <see cref="System.Int32" /> value that is negative if this is less
than the other measurement, 0 if the two measurements are approximately equal,
or positive if this is greater than the other measurement.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private int TolerantCompareTo (double otherMeasure, double tolerance)
        {

/*
Take the absolute difference of the two values.
*/

            double diff = System.Math.Abs (measure - otherMeasure);

/*
If this difference is less than the tolerance, then they're equal.
*/

            System.Diagnostics.Debug.Assert (tolerance >= 0.0);
            if (diff <= tolerance)
            {
                return 0;
            }

/*
Otherwise, report which the the large of the two values.
*/

            if (measure < otherMeasure)
            {
                return -1;
            }
            return 1;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Compare two measurements from the same family.</summary>

<remarks>The two measurements' values will provide the sole basis for the
comparison.

<para>This function is required to implement the <see cref="System.IComparable
{T}" /> interface.</para></remarks>

<param name="other">A <see cref="Measure {UnitType}" /> instance that is being
compared to this measurement.</param>

<returns>A <see cref="System.Int32" /> value that is negative if this is less
than the other measurement, 0 if the two measurements are equal, or positive if
this is greater than the other measurement.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public int CompareTo (Measure <UnitType> other)
        {

/*
Return a comparison of the two measurements.
*/

            return measure.CompareTo (other.measure);
        }
    }
}
