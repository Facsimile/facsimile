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

//=============================================================================
/**
EventId object.

@note This object could have been called "Event" and could have been a
companion object to the [[Event]] class.  However, classes and their companion
objects can access each other's private members - meaning that it would be
possible for the [[Event]] class to access the [[id]] member directly,
by-passing the synchronized access provided by the [[nextId]] method.  By,
instead, choosing to make it a separate, package-private object, we restrict
the visibility of the object generally, while also ensuring that the [[id]]
member is solely access through the [[nextId]] method.  Furthermore, it's
possible that we may not even need this object in future ''Facsimile'' releases,
so hiding it as much as possible makes even more sense.

@since 0.0-0
*/
//=============================================================================

private [engine] object EventId {

/**
Identifier of the last event created.

This event is initialized to be 0, so that events are effectively numbered 1,
2, 3, etc.
*/

  private var id = 0L

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
Retrieve the identifier of the next event created.

@since 0.0-0
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  private [engine] def nextId = {

/*
Increment the ID by 1 and return it.

Since this mutates the state of the Event object, we synchronize access to it
to prevent more than one thread accessing this code at the same time.
*/

    synchronized {
      id += 1
      id
    }
  }
}
