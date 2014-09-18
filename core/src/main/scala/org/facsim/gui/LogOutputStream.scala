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
Scala source file from the org.facsim.gui package.
*/
//=============================================================================

package org.facsim.gui

import java.io.OutputStream
import javafx.scene.control.TextArea

//=============================================================================
/**
Class that writes output into a text area element.

@param textArea Text area to be written to.
*/
//=============================================================================

private [gui] final class LogOutputStream (private val textArea: TextArea)
extends OutputStream {

/*
Sanity check.
*/

  assert (textArea ne null)

//-----------------------------------------------------------------------------
/**
Write a single character into the log window.

@todo This may be a problem if the executing thread is not the ''JavaFX
Application Thread'' (''JAT''). If so, it would be necessary to use the
[[javafx.application.Platform.runLater(Runnable)]] method to append text to the
text area on the JAT. However, it would be very inefficient to schedule every
single character to be written in this way. Instead, each line of output would
need to be buffered and only written to the text area when a line termination
sequence is encountered.

@param c Character to be written.
*/
//-----------------------------------------------------------------------------

  override def write (c: Int): Unit =
  textArea.appendText (String.valueOf (c.toChar))
}
