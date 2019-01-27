/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2019, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for inclusion
as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If your code
fails to comply with the standard, then your patches will be rejected. For further information, please visit the coding
standards at:

  http://facsim.org/Documentation/CodingStandards/
========================================================================================================================
Scala source file belonging to the org.facsim.facsimile.engine package.
*/

package org.facsim.engine

/**
Abstract superclass for all simulation actions.

In the context of the ''Facsimile'' library, an ''action'' represents a
discrete simulation state change, executed during an
[[org.facsim.engine.Event]].

@since 0.0
*/

abstract class Action {

/**
Apply action.

Subclasses must override this function to implement the required actions.
Actions are allowed to change the state of the simulation and its mutable
objects, as well as scheduling other actions to be performed.

@since 0.0
*/
  def apply(): Unit

/**
Return description of this action.

Subclasses must override this function to provide as much detail about this
action as possible; action descriptions can be output as a debugging aid
immediately prior to its associated event being dispatched, as well as for
event logging purposes.

@return String describing what the action does, or is about to do.

@since 0.0
*/
  def description: String
}
