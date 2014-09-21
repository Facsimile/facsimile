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

import java.io.PrintStream
import javafx.scene.Scene
import javafx.scene.control.TextArea
import org.facsim.LibResource

//=============================================================================
/**
Window displaying log messages.

The application's log window is employed by ''graphical user interface''
(''GUI'') applications to display text messages written to both the standard
output and the standard error output.

The window is always available, and can be closed and/or opened at any time.

@constructor Create a new log viewer window.

@param app ''GUI'' application to which the log window belongs.

@throws java.lang.NullPointerException if `app` is `null`.
*/
//=============================================================================

private [gui] final class LogWindow (app: GuiApplication)
extends Window (app) {

/**
Text area control storing the log messages.
*/

  private val ta = new TextArea ()
  ta.setPromptText (LibResource ("gui.LogWindow.prompt"))
  ta.setEditable (false)

/*
Create a print stream that will write to the log window, and register it as the
system's standard output and standard error output streams.

Output will be autoflushed, so that it is always readable.
*/

  private val ps = new PrintStream (new LogOutputStream (ta), true)

/*
Set the window's title.
*/

  title = LibResource ("gui.LogWindow.title", app.title)

/*
Create a scene to store the log window's text area contents.
*/

  scene = new Scene (ta)

//-----------------------------------------------------------------------------
/**
Register this log window with the system.

@note Registering this log window will cause all output written to the standard
output and standard error output streams to be redirected to this log window.
However, it is possible that either user code or third-party libraries might
also attempt to register other output streams with the system, which will cause
log window output to be bypassed.
*/
//-----------------------------------------------------------------------------

  private [gui] def register (): Unit = {
    System.setOut (ps)
    System.setErr (ps)
  }
}
