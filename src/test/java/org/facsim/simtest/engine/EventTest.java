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

Java source file belonging to the org.facsim.simtest.engine package.
*/
//=============================================================================

package org.facsim.simtest.engine;

import org.junit.Test;
//import static org.junit.Assert.*;
import org.facsim.facsimile.engine.Event;
import org.facsim.facsimile.measure.Time;
import org.facsim.simtest.engine.TestEvent;
import org.facsim.simtest.javalang.CompareToContract;

//=============================================================================
/**
<p>Test fixture for the {@link org.facsim.facsimile.engine.Event Event}
class.</p>
*/
//=============================================================================

public class EventTest
{

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Tests that {@link org.facsim.facsimile.engine.Event Event} fulfills the
<em>compareTo contract</em> and the <em>equals contract</em>.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Test
    public void eventFulfillsCompareToContract ()
    {

/*
Our primary events for comparison purposes.
*/

        Event primary = new TestEvent (new Time (10.0), 0);
        Event equalToPrimary = new TestEvent (new Time (10.0), 0);

/*
The following events have identical priority to the primary events, but differ
by their due time.
*/

        Event lessThanPrimaryByTime = new TestEvent (new Time (5.0), 0);
        Event greaterThanPrimaryByTime = new TestEvent (new Time (15.0), 0);

/*
Test that the event is able to correctly order these events.
*/

        CompareToContract.testConformance (primary, equalToPrimary,
        lessThanPrimaryByTime, greaterThanPrimaryByTime);

/*
The following events have identical due times to the primary events, but differ
by their priority.

Don't forget that an event with higher priority than another event will compare
less than that event, since it should occur first.
*/

        Event lessThanPrimaryByPriority = new TestEvent (new Time (10.0), 1);
        Event greaterThanPrimaryByPriority = new TestEvent (new Time (10.0),
        -1);

/*
Test that the event is able to correctly order these events.
*/

        CompareToContract.testConformance (primary, equalToPrimary,
        lessThanPrimaryByPriority, greaterThanPrimaryByPriority);
    }
}
