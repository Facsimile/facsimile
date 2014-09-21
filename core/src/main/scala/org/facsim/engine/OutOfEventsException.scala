/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file belonging to the org.facsim.facsimile.engine package.
*/
//=============================================================================

package org.facsim.engine

import org.facsim.LibResource

//=============================================================================
/**
Thrown when the simulation runs of out events.

In general, this should be regarded as abnormal termination of a simulation
run and treated accordingly.

A ''non-terminating'' simulation should never run out of events.

A ''terminating'' simulation is anticipated to ultimately run out of events;
however, since ''(a)'' a terminating simulation might run out of events
prematurely, due to a bug, and ''(b)'' terminating simulations may have
background events (such as scheduled operator breaks, etc.) that continue to
occur event after the simulation has effectively terminated. it is preferable
that terminating simulations detect that termination criteria have been met
resulting in a controlled termination of the simulation run.

@constructor Create new out-of-events exception.

@since 0.0
*/
//=============================================================================

final class OutOfEventsException private [engine]
extends RuntimeException {

//-----------------------------------------------------------------------------
/*
@see [[java.lang.Throwable!.getMessage()]]
*/
//-----------------------------------------------------------------------------

  override def getMessage =
  LibResource ("engine.OutOfEventsException")
}
