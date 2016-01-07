/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
Scala source file from the org.facsim.akka.gui.jfx package.
*/
//======================================================================================================================

package org.facsim.akka.gui.jfx

import akka.actor.Actor
import javafx.stage.Stage

//======================================================================================================================
/**
''Akka actor'' for ''JavaFX'' user interfaces.

Code executed by this actor will run on the ''JavaFX event-dispatch thread''.

@note Although not recommended, it is possible to have multiple ''JavaFX'' actors in the same application. However, all
such actors will run on the same ''JavaFX event-dispatch thread'', since this is a requirement of ''JavaFX''.

@param stage Primary stage for this actor.
*/
//======================================================================================================================

abstract class JFXActor (stage: Stage)
extends Actor {

}
