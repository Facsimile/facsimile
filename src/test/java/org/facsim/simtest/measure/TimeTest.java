/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2009, Michael J Allen.

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

Java source file belonging to the org.facsim.simtest.measure package.
*/
//=============================================================================


package org.facsim.simtest.measure;

import java.lang.Math;
import org.junit.Test;
import static org.junit.Assert.*;
import org.facsim.facsimile.measure.Time;
import org.facsim.simtest.javalang.CompareToContract;

//=============================================================================
/**
Test fixture for the {@link org.facsim.facsimile.measure.Time Time} class.
*/
//=============================================================================

public class TimeTest
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test time class NaN construction.

Tests that an illegal argument exception is thrown when passed not-a-number
(NaN) as its initial value.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = java.lang.IllegalArgumentException.class)
    public void timeConstructionNaN ()
    {
        new Time (Double.NaN);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test time class negative epsilon construction.

Tests that an illegal argument exception is thrown when passed the smallest
possible negative value.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = java.lang.IllegalArgumentException.class)
    public void timeConstructionNegativeSmall ()
    {
        new Time (-Double.MIN_VALUE);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test time class negative maximum value construction.

Tests that an illegal argument exception is thrown when passed the largest
possible negative value.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = java.lang.IllegalArgumentException.class)
    public void timeConstructionNegativeMax ()
    {
        new Time (-Double.MAX_VALUE);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Tests time class negative infinity construction.

Tests that an illegal argument exception is thrown when passed negative
infinity.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = java.lang.IllegalArgumentException.class)
    public void timeConstructionNegativeInfinity ()
    {
        new Time (Double.NEGATIVE_INFINITY);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Tests time class positive infinity construction.

Tests that an illegal argument exception is thrown when passed positive
infinity.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test (expected = java.lang.IllegalArgumentException.class)
    public void timeConstructionPositiveInfinity ()
    {
        new Time (Double.POSITIVE_INFINITY);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Tests time class valid construction.

Tests various Time constructors to ensure that they all complete successfully.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void timeConstructionValid ()
    {
        new Time (0.0);
        new Time (Double.MIN_VALUE);
        new Time (Double.MAX_VALUE);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Tests that {@link org.facsim.facsimile.measure.Time Time} fulfills the
<em>compareTo contract</em> and the <em>equals contract</em>.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void timeFulfillsCompareToContract ()
    {

/*
Check that the Time class correctly implements the equals contract.
*/

        CompareToContract.testConformance (new Time (10.0), new Time (10.0),
        new Time (9.0), new Time (11.0));
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Verify that {@link org.facsim.facsimile.measure.Time#zero() Time#zero()}
returns a zero time.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void zeroTimeValid ()
    {
        Time zero = new Time (0.0);
        assertTrue (Time.zero ().equals (zero));
        assertTrue (Time.zero ().compareTo (zero) == 0);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test that time values can be added correctly.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void timeAdditionValid ()
    {

/*
Check that adding 0.0 to a time results in the original value (but not the
original object).

The inequality operator compares the pointers only and confirms that the two
objects are not one and the same.  This counter-intuitive Java logic brought to
you by the grown-ups at Sun.
*/

        Time five = new Time (5.0);
        Time fivePlusZero = Time.add (five, Time.zero ());
        assertTrue (fivePlusZero != five);
        assertTrue (fivePlusZero.equals (five));
        assertTrue (fivePlusZero.compareTo (five) == 0);

/*
Check that adding two numbers results in a new number with the right value.

The inequality operator compares the pointers only and confirms that the two
objects are not one and the same.

Note: Do the addition with doubles so that we get the exact same rounding error
(if any) as the Time class's result.
*/

        Time ten = new Time (5.0 + 5.0);
        Time fivePlusFive = Time.add (five, five);
        assertTrue (fivePlusFive != ten);
        assertTrue (fivePlusFive.equals (ten));
        assertTrue (fivePlusFive.compareTo (ten) == 0);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Test that time values are printed out correctly via the overridden toString()
method.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void timeStringOutput ()
    {

/*
Use PI as the basis for comparison.  Right now, we ought to get the same value
as would be returned by Double.  That's going to change when we output times in
a more sophisticated manner, but this will do for now.
*/

        assertEquals (new Time (Math.PI).toString (), new Double
        (Math.PI).toString ());
    }
}
