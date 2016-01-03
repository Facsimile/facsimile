/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2016, Michael J Allen.

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
Scala source file from the org.facsim.cli package.
*/
//=============================================================================

package org.facsim.cli

import org.facsim.{App, AppInformation}

//=============================================================================
/**
''Command line interface'' (''CLI'') application trait.

Applications implementing this trait will execute from the command line and
will not feature a ''graphical user interface'' (''GUI''). All output will be
in the form of text written to the system's standard output and standard error
output. Consequently, such applications are suitable for execution as batch
jobs on a server.

@since 0.0
*/
//=============================================================================

trait CliApp
extends App  {
  self: AppInformation =>

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  protected [facsim] final override def createApp (): Unit = {

/*
Initialize the application to run as a regular command line application.
*/

    self.init ()
  }
}
