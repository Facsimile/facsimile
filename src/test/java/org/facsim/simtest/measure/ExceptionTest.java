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

Java source file belonging to the org.facsim.simtest.measure package.
*/
//=============================================================================

package org.facsim.simtest.measure;

import java.util.Locale;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.facsim.simtest.common.Log;
import org.facsim.facsimile.measure.IllegalMeasurementValueException;
import org.facsim.simtest.javaio.SerializableContract;

//=============================================================================
/**
<p>Test fixture examining consistency of org.facsim.facsimile.measure package
exception error reporting messages.</p>
*/
//=============================================================================

public class ExceptionTest
{

/**
<p>Log file into which exception messages are written.</p>

<p>The contents of the file are compared with versions produced during earlier
tests.  If the new output matches the old output, then it will be assumed that
the output remains valid.  If any differences are detected, then a manual
inspection becomes necessary.  If the files are different for valid reasons
(such as the message of a new exception being written into the file), then the
new file can be manually approved; if the files are different because of a bug,
then the bug should be fixed.</p>

<p>The log file is opened by the openLog function (annotated with BeforeClass),
and closed by the closeLog function (annotated with AfterClass).</p>
*/

    private static Log log;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Open and initialize the exception message log file.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @BeforeClass
    public static void openLog ()
    {
        log = new Log ("data/tmp/org.facsim.simtest.measure.ExceptionMessage."
        + Locale.getDefault () + ".log");
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Test {@link org.facsim.facsimile.measure.IllegalMeasurementValueException}
exception messages.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void testIllegalMeasurementValueException ()
    {

/*
Create as many variants of the exception as possible and log their
explanations.
*/

        IllegalMeasurementValueException e1 = new
        IllegalMeasurementValueException (-1.234);
        log.writeExceptionMessage (e1);

/*
Verify that the class is serializable.
*/

        SerializableContract.testConformance (e1);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Close the exception message log file and compare with known OK version.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @AfterClass
    public static void closeLog ()
    {

/*
Close the log file, and set log to null to prevent further access to the file.
*/

        log.close ();
        log = null;

/*
Perform a file comparison between the version of the log file just generated
and the previously recorded OK version.  Fail the tests if the two files
differ. 
*/

        // TODO...
    }
}
