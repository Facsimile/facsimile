/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim package.
*/
//=============================================================================

package org.facsim

import org.facsim.util.throwIfNull

//=============================================================================
/**
App object's implementation.

All of the functionality offered by [[org.facsim.App]] must be defined here.
The primary reason is to support more thorough testing: if implemented within
the App class, it would be possible to test the application of a single
behavior only; this trait allows us to create test sub-classes that can be used
to test the application of a wide variety of behaviors.

@since 0.0
*/
//=============================================================================

private [facsim] trait AppImplementation extends AppBehaviorInterface {

/**
Application's behavior.

This value is None until a behavior is applied via the apply function.  Only a
single behavior can be applied successfully.
*/

  private final var appBehavior: Option [Behavior] = None

//-----------------------------------------------------------------------------
/**
Retrieve behavior associated with the application, or throw a
NullPointerException if no behavior has been defined.

@return Defined behavior.

@throws NullPointerException if a behavior has yet to be defined.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def getOrThrow = appBehavior match {
    case Some (behavior) => behavior
    case None => throw new NullPointerException ()
  }

//-----------------------------------------------------------------------------
/**
Apply the [[org.facsim.Behavior]] for this application.

Only one ''behavior'' can be applied.  If an attempt is made to apply a second
behavior, then an exception will be thrown.

@param behavior Behavior to be applied.

@throws java.lang.NullPointerException if behavior is null.

@throws org.facsim.BehaviorRedefinitionException if a behavior has already been
applied.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def apply (newBehavior: Behavior): Unit = appBehavior match {

/*
If we already have an applied behavior, then we must throw an exception.
*/

    case Some (behavior) => throw new BehaviorRedefinitionException (behavior,
    newBehavior)

/*
Otherwise, verify that the new behavior is not null before applying it.
*/

    case None => {
      throwIfNull (newBehavior)
      appBehavior = Option (newBehavior)
    }
  }

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws NullPointerException if no Behavior has been defined.
*/
//-----------------------------------------------------------------------------

  final override def title = getOrThrow.title

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws NullPointerException if no Behavior has been defined.
*/
//-----------------------------------------------------------------------------

  final override def organization = getOrThrow.organization

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws NullPointerException if no Behavior has been defined.
*/
//-----------------------------------------------------------------------------

  final override def inceptionDate = getOrThrow.inceptionDate

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws NullPointerException if no Behavior has been defined.
*/
//-----------------------------------------------------------------------------

  final override def releaseDate = getOrThrow.releaseDate

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws NullPointerException if no Behavior has been defined.
*/
//-----------------------------------------------------------------------------

  final override def copyright = getOrThrow.copyright

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws NullPointerException if no Behavior has been defined.
*/
//-----------------------------------------------------------------------------

  final override def version = getOrThrow.version
}