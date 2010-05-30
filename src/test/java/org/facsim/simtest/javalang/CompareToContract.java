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

Java source file belonging to the org.facsim.simtest.javalang package.
*/
//=============================================================================

package org.facsim.simtest.javalang;

import java.lang.Comparable;
import java.lang.NullPointerException;
import java.lang.Throwable;
import static junit.framework.Assert.*;

//=============================================================================
/**
<p>Test class to verify a type's conformance to the <em>compareTo
contract</em>.</p>

<p>This class contains a single function that verifies whether a class's
overridden {@link java.lang.Comparable#compareTo(Object) compareTo(Object)}
method fulfills the terms of the <em>compareTo contract</em> - the definition
of what it means for two instances to be ranked by a comparison of their
contents.  The <em>compareTo contract</em> is defined by the documentation for
the {@link java.lang.Comparable#compareTo(Object) compareTo(Object)}
method.</p>

<p>Since classes that override their equals(Object) method should also override
their {@link java.lang.Object#hashCode() hashCode()} method, so that equal
objects have the same hash codes, hash code tests are also conducted.</p>

@see java.lang.Object#equals(Object)

@see java.lang.Object#hashCode()
*/
//=============================================================================

public class CompareToContract
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests the supplied arguments to verify that the terms of the <em>compareTo
contract</em> are being adhered to by the corresponding object type.</p>

@param <T> The non-primitive type whose overridden {@link
java.lang.Comparable#compareTo(Object) compareTo(Object)} method is to be
tested for adherence to the <em>compareTo contract</em>.  All arguments to this
procedure must be of this same type.

@param primary The primary instance to which the remaining parameters are to be
compared by virtue of their relative contents.  This value cannot be null and
should compare equal to equalToPrimary, greater than lessThanPrimary and less
than greaterThanPrimary.  primary and equalToPrimary must reference different
objects; they cannot point to the same object.

@param equalToPrimary Non null instance that should compare equal to primary,
greater than lessThanPrimary and less than greaterThanPrimary, by virtue of
their relevant contents.  primary and equalToPrimary must reference different
objects; they cannot point to the same object.

@param lessThanPrimary Non null instance that should compare as less than
primary, less than equalToPrimary and less than greaterThanPrimary, by virtue
of their relevant contents.

@param greaterThanPrimary Non null instance that should compare as greater than
primary, greater than equalToPrimary and greater than lessThanPrimary, by
virtue of their relative contents.

@see java.lang.Comparable#compareTo(Object)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static <T extends Comparable <T>> void testConformance (T primary, T
    equalToPrimary, T lessThanPrimary, T greaterThanPrimary)
    {

/*
Verify that none of the arguments are null.  If any of these fails, then the
test case was not set up properly.  This assertion uses the inequality
operator, since this compares pointers only and does not use each object's
equals() or compareTo() methods.
*/

        assertNotNull (primary);
        assertNotNull (equalToPrimary);
        assertNotNull (lessThanPrimary);
        assertNotNull (greaterThanPrimary);

/*
Verify that all of the objects referenced are distinctly different.  If any of
these fails, then the test case was not set up properly.  We use the equality
operator, since this compares pointers only and does not use each object's
equals() or compareTo() methods.
*/

        assertTrue (primary != equalToPrimary);
        assertTrue (primary != lessThanPrimary);
        assertTrue (primary != greaterThanPrimary);
        assertTrue (equalToPrimary != lessThanPrimary);
        assertTrue (equalToPrimary != greaterThanPrimary);
        assertTrue (lessThanPrimary != greaterThanPrimary);

/*
Verify that all of the arguments are instances of the same class.
*/

        assertTrue (primary.getClass () == equalToPrimary.getClass ());
        assertTrue (primary.getClass () == lessThanPrimary.getClass ());
        assertTrue (primary.getClass () == greaterThanPrimary.getClass ());
        assertTrue (equalToPrimary.getClass () == lessThanPrimary.getClass ());
        assertTrue (equalToPrimary.getClass () == greaterThanPrimary.getClass
        ());
        assertTrue (lessThanPrimary.getClass () == greaterThanPrimary.getClass
        ());

/*
OK.  At this point, our base assumptions have been confirmed and we can test
the adherence of the type to the compareTo contract.

Firstly, verify that the class fills the terms of the equals contract - which
helps guarantee that the compareTo implementation is consistent with the equals
implementation.

TODO: There's some redundancy here, so revisit this in the future.
*/

        EqualsContract.testConformance (primary, equalToPrimary,
        lessThanPrimary);
        EqualsContract.testConformance (primary, equalToPrimary,
        greaterThanPrimary);

/*
All of the arguments should throw a NullPointerException if they are compared
to null.  This tests the compare-to-null condition of the contract.

These tests will only fail if there's a bug in T's overridden compareTo method;
it ought to be impossible for them to fail as a result of the test case being
set up badly.
*/

        assertTrue (throwsOnCompareToNull (primary));
        assertTrue (throwsOnCompareToNull (equalToPrimary));
        assertTrue (throwsOnCompareToNull (lessThanPrimary));
        assertTrue (throwsOnCompareToNull (greaterThanPrimary));

/*
Now verify that all of the arguments report that they're equal to themselves.
This tests the reflexive condition of the contract.

These tests will only fail if there's a bug in T's overridden equals method;
it ought to be impossible for them to fail as a result of the test case being
set up badly.
*/

        assertTrue (primary.compareTo (primary) == 0);
        assertTrue (equalToPrimary.compareTo (equalToPrimary) == 0);
        assertTrue (lessThanPrimary.compareTo (lessThanPrimary) == 0);
        assertTrue (greaterThanPrimary.compareTo (greaterThanPrimary) == 0);

/*
Now verify that primary equals equalToPrimary and that equalToPrimary equals
primary, that primary is greater than lessThanPrimary and that lessThanPrimary
is less than primary, etc, etc.  This tests the symmetric condition of the
contract.

These tests may fail if the test case was set up so that arguments do not have
the ordering suggested by their names; if tests fail, verify the ordering
before looking for bugs in the overridden compareTo method.
*/

        assertTrue (primary.compareTo (equalToPrimary) == 0);
        assertTrue (equalToPrimary.compareTo (primary) == 0);
        assertTrue (primary.compareTo (lessThanPrimary) > 0);
        assertTrue (lessThanPrimary.compareTo (primary) < 0);
        assertTrue (primary.compareTo (greaterThanPrimary) < 0);
        assertTrue (greaterThanPrimary.compareTo (primary) > 0);
        assertTrue (equalToPrimary.compareTo (lessThanPrimary) > 0);
        assertTrue (lessThanPrimary.compareTo (equalToPrimary) < 0);
        assertTrue (equalToPrimary.compareTo (greaterThanPrimary) < 0);
        assertTrue (greaterThanPrimary.compareTo (equalToPrimary) > 0);
        assertTrue (lessThanPrimary.compareTo (greaterThanPrimary) < 0);
        assertTrue (greaterThanPrimary.compareTo (lessThanPrimary) > 0);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verify that the argument throws a {@link java.lang.NullPointerException
NullPointerException} when compared to null.</p>

@param <T> The type being tested.

@param value Non-null instance to be compared to null.

@return Returns true if a {@link java.lang.NullPointerException
NullPointerException} was thrown during the comparison, false otherwise.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static <T extends Comparable <T>> boolean throwsOnCompareToNull
    (T value)
    {

/*
Verify that the argument is not null to rule out the possibility that
de-referencing it causes the exception to be thrown.
*/

        assertNotNull (value);

/*
Compare the value to null.
*/

        try
        {
            value.compareTo (null);
        }

/*
If the comparison resulted in the NullPointerException, then return true.
*/

        catch (NullPointerException e)
        {
            return true;
        }

/*
If any other exception arises, then fail the test suite - this just shouldn't
happen.
*/

        catch (Throwable e)
        {
            fail ();
        }

/*
If no exceptions arose, then just return false.
*/

        return false;
    }
}
