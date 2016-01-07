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

import akka.dispatch.{ExecutorServiceConfigurator, ExecutorServiceFactory, DispatcherPrerequisites}
import com.typesafe.config.Config
import java.util.concurrent.{ThreadFactory, ExecutorService}

//======================================================================================================================
/**
Configurator class which creates ''JavaFX'' executor services for ''Akka'' actors.

@note This class '''must''' be referenced by the `jfx-dispatcher` library configuration setting. See
`src/main/resources/reference.conf` for further details. Similarly, the `jfx-dispatcher` must be specified (as a string)
when creating associated ''Akka'' actors.

@constructor Create a new ''JavaFX'' executor service. This should be invoked during Akka actor creation.

@param config The associated configuration of the dispatcher.

@param prerequisites Pre-requisites for the Akka dispatcher.
*/
//======================================================================================================================

private final class JFXExecutorServiceConfigurator (config: Config, prerequisites: DispatcherPrerequisites)
extends ExecutorServiceConfigurator (config, prerequisites) {

/**
Factory used to create the executor service to meet our requirements.

When this factory's createExecutorService function is called, we will return our ''JavaFX'' executor service.
*/

  private val factory = new ExecutorServiceFactory {
    override def createExecutorService: ExecutorService = JFXExecutorService
  }

/**
Create the factory, which will be used to create the executor service.

@note This function and its arguments are not covered by the Akka documentation, so treat this information with a
healthy dose of skepticism.

@param id Identifier for the requested executor service. This ought to be the name of the resulting dispatcher.

@param threadFactory Thread factory to be employed. This value is no used.

@return Executor service factory.
*/

  override def createExecutorServiceFactory (id: String, threadFactory: ThreadFactory): ExecutorServiceFactory = {
    assert (id == JFXExecutorServiceConfigurator.JFXDispatcher)
    factory
  }
}

//======================================================================================================================
/**
''JavaFX'' executor service configurator companion object.
*/
//======================================================================================================================

private object JFXExecutorServiceConfigurator {

/**
Name of the JavaFX dispatcher to be used when creating ''JavaFX'' actors.

@note This string '''must''' match the name of the dispatcher specified in `./core/src/main/resources/reference.conf`.
*/

  val JFXDispatcher = "jfx-dispatcher"
}