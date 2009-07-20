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

Java source file belonging to the org.facsim.simtest.javalang package.
*/
//=============================================================================


package org.facsim.simtest.javalang;

import java.lang.Integer;
import static junit.framework.Assert.*;

//=============================================================================
/**
Test class to verify a type's conformance to the <em>equals contract</em>.

This class contains a single function that verifies whether a class's
overridden {@link java.lang.Object#equals(Object) equals(Object)} method
fulfills the terms of the <em>equals contract</em> - the definition of what it
means for two instances to be considered <em>equal</em>.  The <em>equals
contract</em> is defined by the documentation for the {@link
java.lang.Object#equals(Object) equals(Object)} method.

Since classes that override their equals(Object) method should also override
their {@link java.lang.Object#hashCode() hashCode()} method, so that equal
objects have the same hash codes, hash code tests are also conducted.

@see java.lang.Object#equals(Object)

@see java.lang.Object#hashCode()
*/
//=============================================================================

public class EqualsContract
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Tests the supplied arguments to verify that the terms of the <em>equals
contract</em> are being adhered to by the corresponding object type. 

If the class overrides the {@link java.lang.Comparable#compareTo(Object)
compareTo(Object)} method, then it should be tested for conformance to the
<em>compareTo contract</em>, which includes testing it for conformance to the
<em>equals contract</em>.  Refer to
{@link org.facsim.simtest.javalang.CompareToContract#testConformance
(Comparable, Comparable, Comparable, Comparable)
CompareToContract#testConformance(Comparable, Comparable, Comparable,
Comparable)} for further information.

@param <T> The non-primitive type whose overridden {@link
java.lang.Object#equals(Object) equals(Object)} method is to be tested for
adherence to the <em>equals contract</em>.  All arguments to this procedure
must be of this same type.

@param primary Instance that should test as equal to equalToPrimary by virtue
of their identical contents.  Both primary and equalToPrimary should be
instances of the same class, should compare as equal and should have identical
hash codes.  primary and equalToPrimary should not be references to the same
object.

@param equalToPrimary Instance that should test as equal to primary by virtue
of their identical contents.  Both primary and equalToPrimary should be
instances of the same class, should compare as equal and should have identical
hash codes.  primary and equalToPrimary should not be references to the same
object.

@param notEqualToPrimary Instance that should test as not equal to both primary
and equalToPrimary by virtue of its contents not being identical to the
contents of primary and equalToPrimary alone.  This should be an instance of
the same class as both primary and equalToPrimary, but should not compare equal
to either.  It's hash code can be the same as the hash code of both primary and
equalToPrimary (although, if the hashing function is good, the chances of a
match, statistically, should be extremely low).

@see java.lang.Object#equals(Object)

@see org.facsim.simtest.javalang.CompareToContract#testConformance(Comparable,
Comparable, Comparable, Comparable)
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static <T> void testConformance (T primary, T equalToPrimary, T
    notEqualToPrimary)
    {

/*
Verify that none of the arguments are null.  If any of these fails, then the
test case was not set up properly.  The assertions use the inequality operator,
which compares pointers only and does not use each object's equals() method.
*/

        assertNotNull (primary);
        assertNotNull (equalToPrimary);
        assertNotNull (notEqualToPrimary);

/*
Verify that all of the objects referenced are distinctly different.  If any of
these fails, then the test case was not set up properly.  We use the equality
operator, since this compares pointers only and does not use each object's
equals() method.
*/

        assertTrue (primary != equalToPrimary);
        assertTrue (primary != notEqualToPrimary);
        assertTrue (equalToPrimary != notEqualToPrimary);

/*
Verify that all arguments are instances of the same class.
*/

        assertTrue (primary.getClass () == equalToPrimary.getClass ());
        assertTrue (primary.getClass () == notEqualToPrimary.getClass ());
        assertTrue (equalToPrimary.getClass () == notEqualToPrimary.getClass
        ());

/*
OK.  At this point, our base assumptions have been confirmed and we can test
the adherence of the type to the equals contract.

First-up: All of the arguments should report that they're not equal to null.
This tests the not-equal-to-null condition of the contract.

These tests will only fail if there's a bug in T's overridden equals method;
it ought to be impossible for them to fail as a result of the test case being
set up badly.
*/

        assertFalse (primary.equals (null));
        assertFalse (equalToPrimary.equals (null));
        assertFalse (notEqualToPrimary.equals (null));

/*
Second test.  Verify that all of the arguments report that they're equal to
themselves.  This tests the reflexive condition of the contract.

These tests will only fail if there's a bug in T's overridden equals method;
it ought to be impossible for them to fail as a result of the test case being
set up badly.
*/

        assertTrue (primary.equals (primary));
        assertTrue (equalToPrimary.equals (equalToPrimary));
        assertTrue (notEqualToPrimary.equals (notEqualToPrimary));

/*
Now verify that primary equals equalToPrimary and that equalToPrimary equals
primary.  This tests the symmetric condition of the contract.

These tests may fail if the test case was set up so that primary and
equalToPrimary are not actually equal, so check their values before checking
the overridden equals method.
*/

        assertTrue (primary.equals (equalToPrimary));
        assertTrue (equalToPrimary.equals (primary));

/*
Now verify that primary and equalToPrimary and not the same as
notEqualToPrimary, and that notEqualToPrimary is not the same as primary or
equalToPrimary.  This further tests the implied symmetry of unequal value
comparisons.

These tests may fail if the test case was set up so that either primary or
equalToPrimary is legitimately equal to notEqualToPrimary, so check their
values before checking the overridden equals method.
*/

        assertFalse (primary.equals (notEqualToPrimary));
        assertFalse (notEqualToPrimary.equals (primary));
        assertFalse (equalToPrimary.equals (notEqualToPrimary));
        assertFalse (notEqualToPrimary.equals (equalToPrimary));

/*
Strictly speaking, the equals contract has no conditions regarding the types of
the objects being compared for equality.  However, it's an obvious requirement
that you can only compare like with like, so there should be some consideration
of the types involved.  Two objects with dissimilar types ought not to compare
equal.  The symmetric condition dictates that if one object compares not equal
to a dissimilar object, then the dissimilar object should likewise compare not
equal to the first object.  This implies that the standard behavior for all
overridden equals methods is to reject comparisons with dissimilar objects.

Alas, the waters are muddied, both by inheritance and by Java's "type erasure"
feature.  If two different instances share a common super class, and the
overridden equals method was implemented at the level of that inheritance tree,
then they can be legitimately compared as equal, otherwise they cannot.  As a
result of "type erasure" we cannot be too specific about the types involved.
This test assumes that the types being tested are not java.lang.Integer
instances.

If badType's class fails to fulfill the equals contract correctly, then
these tests may fail.
*/

        Integer badType = new Integer (0);
        assertFalse (primary.getClass () == badType.getClass ());
        assertFalse (primary.equals (badType));
        assertFalse (badType.equals (primary));
        assertFalse (equalToPrimary.equals (badType));
        assertFalse (badType.equals (equalToPrimary));
        assertFalse (notEqualToPrimary.equals (badType));
        assertFalse (badType.equals (notEqualToPrimary));
    }
}
