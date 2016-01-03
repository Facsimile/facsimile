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
Scala source file from the org.facsim.gui package.
*/
//=============================================================================

package org.facsim.gui

import javafx.application.{Platform, Application => JApplication}
import javafx.stage.Stage
import org.facsim.{assertNonNull, App, AppInformation}

//=============================================================================
/**
''Graphical user interface'' (''GUI'') application trait.

Applications implementing this trait will execute utilizing a
''[[http://docs.oracle.com/javafx JavaFX]]'' rich client interface.

@since 0.0
*/
//=============================================================================

trait GuiApp
extends App {
  self: AppInformation =>

/**
JavaFX application instance.

The application instance is created by ''JavaFX''.
*/

  private final var app: Option [JfxApp] = None // scalastyle:ignore

//-----------------------------------------------------------------------------
/**
@inheritdoc
*/
//-----------------------------------------------------------------------------

  protected [facsim] final override def createApp (): Unit = {

/*
Create a new JavaFX application.

This function call creates a new instance of our JfxApp class, passing in the
command line arguments, creates the primary stage, and initializes subclasses
by executing their constructor code.

An exception is thrown if a JavaFX application has already been launched (for
example, if `main` is invoked manually).

NOTE: The "launch" function does not return until the application has
terminated.
*/

    JApplication.launch (classOf [JfxApp], args: _*)
  }

//-----------------------------------------------------------------------------
/**
Automatically show application's primary stage.

The function's default implementation always displays the primary stage
automatically. Override this function if different behavior is required.

@note If this function returns `false`, it is the responsibility of the user
application to display the primary stage manually.

@return `true` if application's primary stage should be automatically shown
following construction; `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  protected def autoShow = true

//-----------------------------------------------------------------------------
/**
Report the application's primary stage.

@note Attempts to modify the primary stage should only be made on the ''JavaFX
Application Thread''.

@return ''JavaFX'' application's primary stage.

@throws java.lang.NoSuchElementException if the ''JavaFX'' application has yet
to be initialized and started.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def primaryStage = app.get.primaryStage

//-----------------------------------------------------------------------------
/**
Display the application's log window.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def showLog () = app.get.showLog ()

//-----------------------------------------------------------------------------
/**
Perform clean-up on application exit.

This function provides an opportunity for the application to release resources
and perform miscellaneous clean-up operations when the application exits.  The
default function does nothing; override to perform application-specific
clean-up.

@note This method is called on the ''JavaFX Application Thread''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def onStop (): Unit = {
  }

//-----------------------------------------------------------------------------
/**
Terminate the application.

This function signals to the application that termination has been requested.

By default, ''JavaFX'' applications will also terminate automatically when the
last window has been closed.

@note This method may be called from any thread.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def terminate () = Platform.exit ()

//-----------------------------------------------------------------------------
/**
JavaFX application class.

This is a helper class, the sole instance of which is created by the JavaFX
runtime. All functions invoked on this instance execute on the main JavaFX
thread.
*/
//-----------------------------------------------------------------------------

  private final class JfxApp
  extends JApplication {

/**
JavaFX application primary stage.

This is created by ''JavaFX'' and passed to the `init` method by the JavaFX
application.
*/

    private var primStage: Option [Stage] = None // scalastyle:ignore

/**
Log window to which all output will be sent.

@note This value is initialized lazily (with the first reference during the
start process, after the applicaiton has been initialized) to avoid using a
variable Option type.
*/

    private lazy val logWindow = new LogWindow (self)

//.............................................................................
/**
Initialize this ''JavaFX'' application.

@note This method is called on the ''JavaFX Application Thread''.

@param stage Application's primary ''JavaFX'' stage, created by JavaFX.
*/
//.............................................................................

    override def start (stage: Stage): Unit = {

/*
Sanity checks.
*/

      assertNonNull (stage)

/*
Store a reference to this app in the outer class.

Initializing this here, rather than in this inner class's constructor, means
that the application must have been fully initialized when accessed externally.
*/

      assert (app.isEmpty)
      app = Some (this)

/*
Store the value of the primary stage.
*/

      assert (primStage.isEmpty)
      primStage = Some (stage)

/*
Configure the primary stage.

NOTE: This takes places before the outer class's subclasses have been
constructed. Provided that the application has been mixed-in with a trait that
implements AppInformation (since trait construction will have been completed),
then everything should be OK. However, there could be problems if
AppInformation has been implemented in a class or object (since their
construction will not yet have taken place).

Still, let's see how well this works...
*/

      stage.setTitle (self.title)
      stage.getIcons.addAll (self.icons: _*)

/*
Create the log window and register it with the system so that all output
written to the standard output and standard error output streams is redirected
to the log window.

By default, the log window will be hidden.
*/

      logWindow.register ()

/*
Now initialize our outer class, so that sub-class construction is completed.
*/

      self.init ()

/*
Show the stage - if required.
*/

      if (self.autoShow) stage.show ()
    }

//.............................................................................
/**
Clean-up upon application exit.

@note This method is called on the ''JavaFX Application Thread''.
*/
//.............................................................................

    override def stop (): Unit = self.onStop ()

//.............................................................................
/**
Report the application's primary [[javafx.stage.Stage]].

@return Application's primary stage.

@throws java.lang.NoSuchElementException if the application has yet to start
(i.e. the `start` function has yet to be called) and no primary stage is
available.
*/
//.............................................................................

    def primaryStage = primStage.get

//.............................................................................
/**
Display the log window.
*/
//.............................................................................

    def showLog () = logWindow.show ()
  }
}
