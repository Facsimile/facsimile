/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2011, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

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

Scala source file belonging to the org.facsim.facsimile.engine package.
*/
//=============================================================================

package org.facsim.facsimile.engine
import scala.collection.mutable.PriorityQueue

//=============================================================================
/**
Simulation object.

@since 0.0-0
*/
//=============================================================================

object Simulation {

/**
Event queue.

Events are queued up and dispatched in order (see [[Event.compare(Event)]] for
further information).  Events are removed, and dispatched one-at-a-time - with
the event being dispatched right now termed the ''current event''.
*/

  private val eventQueue = PriorityQueue [Event] ()

/**
Current event.
*/

  private var currentEvent: Event = null //new NullEvent ()

//-----------------------------------------------------------------------------
/**
Report current simulation time.

@return Current absolute simulation time.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  final def currentTime = currentEvent.timeDue

//-----------------------------------------------------------------------------
/**
Schedule event.

@param event Event to be scheduled for dispatch.

@since 0.0-0
*/
//-----------------------------------------------------------------------------

  private [engine] def schedule (event: Event): Unit = (eventQueue += event)
}
