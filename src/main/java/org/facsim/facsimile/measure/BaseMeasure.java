/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2010, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
Facsimile.  If not, see http://www.gnu.org/licenses/.

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

Java source file belonging to the org.facsim.facsimile.measure package.
*/
//=============================================================================

package org.facsim.facsimile.measure;

import java.lang.Override;
import java.io.Serializable;
import net.jcip.annotations.Immutable;
import org.facsim.facsimile.measure.IllegalMeasurementValueException;
import org.facsim.facsimile.util.PackagePrivate;

//=============================================================================
/**
<p>Abstract base class for all measurement classes.</p>

<p>This is the base class used by all measurement types, storing the underlying
values in the corresponding <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a>
units.</p>

<p>Do not derive classes directly from this base class; derive classes from the
{@link org.facsim.facsimile.measure.Measure Measure&lt;T&gt;} class
instead.</p>

@see org.facsim.facsimile.measure.Measure Measure&lt;T&gt;
*/
//=============================================================================

@Immutable
@PackagePrivate
abstract class BaseMeasure
implements Serializable
{

/**
<p>Class serialization schema number.</p>
*/
    
    private static final long serialVersionUID = 1L;

/**
<p>Value of this measurement in SI units.</p>

<p>This value cannot be changed once the measurement has been created to ensure
that values are immutable.</p>
*/

    private final double value;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Construct a base measurement from a value in the standard <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units
for this measurement type.</p>

@param initialValue Initial measurement value in the underlying <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units.

@throws java.lang.IllegalArgumentException if value is invalid (infinity, NaN,
etc.)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected BaseMeasure (double initialValue)
    {

/*
Initialize the super class.
*/

        super ();


/*
Verify that the value we're being asked to store is valid.  If the value is
not-a-number (Nan) or infinite (positive or negative), then it's invalid and an
exception should be thrown.
*/

        if (Double.isNaN (initialValue) || Double.isInfinite (initialValue))
        {
            throw new IllegalMeasurementValueException (initialValue);
        }

/*
Store the initial value.
*/

        this.value = initialValue;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Report the measurement value in the underlying <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units
for this measurement type.</p>

@return The value of this measurement type in <a
href="http://en.wikipedia.org/wiki/International_System_of_Units">SI</a> units.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected double getValue ()
    {
        return this.value;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Object#equals(java.lang.Object)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final boolean equals (Object other)
    {

/*
If the other argument is null, then return false.  This is a required part of
the "equals contract".
*/

        if (other == null)
        {
            return false;
        }

/*
If the pointer to the other object and this object's address are the same, then
they must be equal.
*/

        if (this == other)
        {
            return true;
        }

/*
If the other value does not have the same class as this object, then they
cannot be equal.
*/

        if (this.getClass () != other.getClass ())
        {
            return false;
        }

/*
OK.  The other object is the same type of value as this instance.  If they have
the same value, then they're equal.
*/

        return (this.value == ((BaseMeasure) other).value);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/*
(non-Javadoc)

@see java.lang.Object#hashCode()
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public final int hashCode ()
    {

/*
Relay to the Double class.

This looks like a relatively expensive procedure, but we should be able to rely
on the Java compiler to optimize this quite nicely.
*/

        return Double.valueOf (this.value).hashCode ();
    }
}
