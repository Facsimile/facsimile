/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

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
Scala source file from the org.facsim package.
*/
//=============================================================================

package org.facsim

//=============================================================================
/**
[[org.facsim.App$]] object's implementation.

All of the functionality offered by `App` must be defined here. The primary
reason is to support more thorough testing: if implemented within the `App`
object, it would be possible to test the application of a single behavior only;
this trait allows us to create test subclasses that can be used to test the
application of a wide variety of behaviors.

This trait delegates its functionality to the applied [[org.facsim.Behavior!]]
instance.

@since 0.0
*/
//=============================================================================

private [facsim] trait AppImplementation
extends AppBehaviorInterface {

/**
Applied behavior.

This value is None until a behavior is applied via the
[[org.facsim.AppImplementation!.apply(Behavior)*]] function. Only a single
behavior can be applied successfully.
*/

  private final var appBehavior: Option [Behavior] = None

//-----------------------------------------------------------------------------
/**
Retrieve applied behavior, or throw an
[[org.facsim.BehaviorUndefinedException!]] if no behavior has yet been defined.

@return Applied behavior.

@throws org.facsim.UndefinedBehaviorException if a behavior has yet to be
applied.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def getOrThrow = appBehavior.getOrElse (throw new
  BehaviorUndefinedException ())

//-----------------------------------------------------------------------------
/**
Apply the [[org.facsim.Behavior!]] for this application.

A ''behavior'' can only be applied once; if a second attempt is made to apply a
behavior, then a [[org.facsim.BehaviorRedefinitionException!]] will be thrown.

@param behavior Behavior to be applied.

@return This application with it's behavior defined.

@throws org.facsim.BehaviorRedefinitionException if a behavior has already been
applied; the application's state will be unchanged and the existing behavior
will be retained.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def apply (newBehavior: Behavior): AppImplementation =
  appBehavior match {

/*
If we already have an applied behavior, then we must throw an exception.
*/

    case Some (behavior) => throw new BehaviorRedefinitionException (behavior,
    newBehavior)

/*
Otherwise, store the new behavior.
*/

    case None => {
      appBehavior = Option (newBehavior)
      this
    }
  }

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws org.facsim.UndefinedBehaviorException if a behavior has yet to be
applied.
*/
//-----------------------------------------------------------------------------

  @inline
  final override def title = getOrThrow.title

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws org.facsim.UndefinedBehaviorException if a behavior has yet to be
applied.
*/
//-----------------------------------------------------------------------------

  @inline
  final override def organization = getOrThrow.organization

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws org.facsim.UndefinedBehaviorException if a behavior has yet to be
applied.
*/
//-----------------------------------------------------------------------------

  @inline
  final override def inceptionDate = getOrThrow.inceptionDate

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws org.facsim.UndefinedBehaviorException if a behavior has yet to be
applied.
*/
//-----------------------------------------------------------------------------

  @inline
  final override def releaseDate = getOrThrow.releaseDate

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws org.facsim.UndefinedBehaviorException if a behavior has yet to be
applied.
*/
//-----------------------------------------------------------------------------

  @inline
  final override def copyright = getOrThrow.copyright

//-----------------------------------------------------------------------------
/**
@inheritdoc

@throws org.facsim.UndefinedBehaviorException if a behavior has yet to be
applied.
*/
//-----------------------------------------------------------------------------

  @inline
  final override def version = getOrThrow.version
}