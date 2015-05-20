/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2015, Michael J Allen.

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

import javafx.scene.Scene
import javafx.stage.{Stage, StageStyle}
import org.facsim.requireNonNull

//=============================================================================
/**
Abstract base class for application windows.

@constructor Create a new window.

@param app ''GUI'' application to which the log window belongs.

@param style Style of window to be created. Refer to
[[javafx.stage.StageStyle]] for further details.

@throws java.lang.NullPointerException if either `app` or `style` is `null`.

@since 0.0
*/
//=============================================================================

abstract class Window (app: GuiApplication,
style: StageStyle = StageStyle.DECORATED) {

/*
Sanity checks.
*/

  requireNonNull (app)
  requireNonNull (style)

/**
This window's stage.

All application windows should be owned by the application window, so that they
can all be minimized individually.
*/

  private final val stage = new Stage (style)
  stage.initOwner (app.primaryStage)

//-----------------------------------------------------------------------------
/**
Report window's title.

@return Contents of window's title bar.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def title = stage.getTitle

//-----------------------------------------------------------------------------
/**
Define the window's title.

@param value Text to be written to the window's title bar.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def title_= (value: String): Unit = stage.setTitle (value)

//-----------------------------------------------------------------------------
/**
Report window's scene.

@note It would be preferable not to have this method, but ''Scala'' does not
support the setter syntax (scene_= (value)) otherwise.

@return Scene contained by the window.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def scene = stage.getScene

//-----------------------------------------------------------------------------
/**
Define the window's scene.

@param value Scene to be applied to this window.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def scene_= (value: Scene): Unit = stage.setScene (value)

//-----------------------------------------------------------------------------
/**
Show this window.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def show () = stage.show ()
}
