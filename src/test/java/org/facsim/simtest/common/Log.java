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

Java source file belonging to the org.facsim.simtest.common package.
*/
//=============================================================================

package org.facsim.simtest.common;

import java.io.FileWriter;
import java.io.IOException;
import static org.junit.Assert.*;

//=============================================================================
/**
<p>Class to assist with writing regression log files.</p>

<p>Regression log files are used to verify text output during testing.  In
particular, they are used to verify messages produced by exception classes.</p>
*/
//=============================================================================

public class Log
{

/**
<p>Log file into which messages are written.</p>
*/

    private FileWriter log;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Log constructor.</p>

@param logFile Path of the log file to be created.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public Log (String logFile)
    {

/*
If there's an error opening the log file, then report a test failure.
*/

        try
        {
            this.log = new FileWriter (logFile);
        }
        catch (IOException e)
        {
            fail ();
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Write an exception's message into the log.</p>

@param e Exception whose message is to be written to the log.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void writeExceptionMessage (Exception e)
    {

/*
Argument verification.
*/

        assert (e != null);

/*
Verify that the message is the same for both the primary and localized
versions.  Facsimile exceptions ought to produce the same localized message for
both.
*/

        assertTrue (e.getMessage ().equals (e.getLocalizedMessage ()));

/*
Write the message into the log.  If we get an output error, then record a test
failure.
*/

        writeMessage (e.getClass (), e.getMessage ());
    }


//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Write a message to the log.  The message is prefixed by the class name of the
object that created the message.

@param <T> Type that generated the message.

@param testType Class of the type that generated the message.

@param testMessage Test message to be written to the log.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public <T> void writeMessage (Class <T> testType, String testMessage)
    {

/*
Write the message into the log.  If we get an output error, then record a test
failure.
*/

        try
        {
            this.log.write (testType.getSimpleName () + ": " + testMessage);
        }
        catch (IOException e1)
        {
            fail ();
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Close the log file.</p>

<p>Once the log has been closed, it cannot be written to subsequently.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    public void close ()
    {
        try
        {
            this.log.close ();
        }
        catch (IOException e)
        {
            fail ();
        }
    }
}
