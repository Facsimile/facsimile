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
Scala source file from the org.facsim.akka.gui package.
*/
//======================================================================================================================

package org.facsim.akka.gui

import java.util.Collections
import java.util.concurrent.{AbstractExecutorService, TimeUnit}

//======================================================================================================================
/**
Executor service (dispatcher) for ''graphical user interface'' (''GUI'') ''[[http://akka.io/ Akka]]'' actors.

@todo Shutdown functionality is currently not implemented. It is assumed that GUI executor services will shutdown at
application termination, but it might be possible to improve on this. All shutdown and termination implementation logic
is currently pretty basic.
*/
//======================================================================================================================

abstract class GUIExecutorService private [gui]
extends AbstractExecutorService {

/**
Initiates an orderly shutdown of this executor service.

Previously submitted tasks are executed, but no new tasks will be accepted. Calling this function has no effect if
already shutdown.

This function does not wait for previously submitted tasks to complete&mdash;use awaitTermination to do that.

@todo Shutdown functionality is currently not implemented. It is assumed that GUI executor services will shutdown at
application termination, but it might be possible to improve on this.

@throws SecurityException if a security manager exists and shutting down this executor service may manipulate threads
that the caller is not permitted to modify because it does not hold RuntimePermission("modifyThread"), or the security
manager's checkAccess method denies access.
*/

  final override def shutdown (): Unit = ()

/**
Attempt to stop all actively executing tasks and halt processing of waiting tasks.

This function does not wait for actively executing tasks to terminate&mdash;use awaitTermination to do that.

There are no guarantees beyond best-effort attempts to stop processing actively executing tasks; some tasks may fail to
terminate.

@todo Shutdown functionality is currently not implemented. It is assumed that GUI executor services will shutdown at
application termination, but it might be possible to improve on this. An empty task list is returned currently.

@return List of tasks that were awaiting execution.

@throws SecurityException if a security manager exists and shutting down this executor service may manipulate threads
that the caller is not permitted to modify because it does not hold RuntimePermission("modifyThread"), or the security
manager's checkAccess method denies access.
*/

  final override def shutdownNow () = Collections.emptyList [Runnable]

/**
Determine if this executor has been shutdown.

@todo Shutdown functionality is currently not implemented. It is assumed that GUI executor services will shutdown at
application termination, but it might be possible to improve on this. For now, we always return `false`.

@return `true` if the executor has been shutdown, `false` otherwise.
*/

  final override def isShutdown = false

/**
Determine if this executor has terminated.

@todo Shutdown functionality is currently not implemented. It is assumed that GUI executor services will shutdown at
application termination, but it might be possible to improve on this. For now, we always return `false`.

@return `true` if the executor has terminated, `false` otherwise.
*/

  final override def isTerminated = false

  final override def awaitTermination (l: Long, timeUnit: TimeUnit) = true
}
