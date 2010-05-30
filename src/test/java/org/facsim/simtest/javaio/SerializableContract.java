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

Java source file belonging to the org.facsim.simtest.javaio package.
*/
//=============================================================================

package org.facsim.simtest.javaio;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import static junit.framework.Assert.*;

//=============================================================================
/**
<p>Test class to verify a type's conformance to the <em>serialization
contract</em>.</p>

<p>This code within this class was inspired by a need for improved
serialization testing, and by the IBM DeveloperWorks article: <a
href="http://www.ibm.com/developerworks/library/j-serialtest.html">Testing
object serialization</a></p>
*/
//=============================================================================

public class SerializableContract
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests the supplied arguments to verify that the terms of the
<em>serializable contract</em> are being adhered to by the implementing
type.</p>

@param serializable Serializable object to be tested for its serialization
capabilities.

@see java.io.Serializable
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public static void testConformance (Serializable serializable)
    {

/*
Firstly, verify that the object can be serialized.
*/

        try
        {
            testIsSerializable (serializable);
        }

/*
If we encounter any exceptions, then fail the test.
*/

        catch (IOException e)
        {
            fail ();
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Verifies that an object is serializable.</p>

<p>This test serializes an object and verifies that serializable output was
written without resulting in a {@linkplain java.io.NotSerializableException}
exception being thrown.</p>

@param serializable Object to be tested that it can be serialized.

@throws IOException Thrown if an exception occurs during serialization of the
object.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private static void testIsSerializable (Serializable serializable)
    throws IOException
    {

/*
Create a byte array output stream to hold the serialized data, then associate
the stream with the object serialization class,
*/

        ByteArrayOutputStream output = new ByteArrayOutputStream ();
        ObjectOutputStream objectOutput = new ObjectOutputStream (output);

/*
Serialize the object by writing it to the object output stream.  This will
yield a NotSerializableException if the object is not actually serializable;
if we do not raise this exception, then the object is indeed serializable.
*/

        objectOutput.writeObject (serializable);
        objectOutput.close ();

/*
Verify that we actually wrote something to the output.  This is likely to be
redundant, but it verifies that something got written to the output stream
during serialization, which is a good sign.
*/

        assertTrue (output.toByteArray().length > 0);
    }
}
