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

import java.util.NoSuchElementException

//=============================================================================
/**
Safe equivalent of [[scala.Option!]] that prohibits storage of `null` values
and that does not permit `null` instances.

@note If you need to treat `null` values as non-optional values (that is, if
`null` is a different value to `None`), then you should use [[scala.Option!]],
[[scala.Some]] and [[scala.None]] instead of this class hierarchy.
 
@tparam T Type of optional value to be stored.  Note that instance of T
subclasses can be stored and the result will still be a SafeOption [T] type.

@since 0.0
*/
//=============================================================================

sealed abstract class SafeOption [+T] extends Product with Serializable with
NotNull {

//-----------------------------------------------------------------------------
/**
Determine if optional value is missing.

@return `true` if option is [[org.facsim.SafeNone$]], `false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def isEmpty: Boolean

//-----------------------------------------------------------------------------
/**
Determine if optional value is present.

@return `false` if option is [[org.facsim.SafeNone$]], `true` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def isDefined = !isEmpty

//-----------------------------------------------------------------------------
/**
Return the optional value, or throw an exception if missing.

@return Optional value, if present.

@throws [[java.util.NoSuchElementException]] if optional value is missing.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def get: T

//-----------------------------------------------------------------------------
/**
Return the optional value if present, or perform a default action otherwise.

@param default Function to execute if optional value is missing.

@tparam V Actual type of the stored value, if present.  V must the same type as
T or a sub-class of it.

@return Optional value, if present; value returned by `default` if missing.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def getOrElse [V >: T] (default: => V): V =
  if (isEmpty) default
  else get

//-----------------------------------------------------------------------------
/**
Return optional value, if present and if it passes predicate function.

@param pred Predicate function that evaluates optional value, if present, and
either passes it (by returning `true`) or failing it (by returning `false`).

@return Optional value, if present and it it passes predicate; `SafeNone`
otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def filter (pred: T => Boolean) =
  if (!isEmpty && pred (get)) this
  else SafeNone
}

//=============================================================================
/**
SafeOption companion object.

@since 0.0
*/
//=============================================================================

object SafeOption {

//-----------------------------------------------------------------------------
/**
Safe option factory method.

@param x Value to be stored.

@tparam T Type of optional value to be stored.  Note that instances of T
subclasses can be stored and the result will still have a SafeOption [T] type.

@return `SafeSome (x)` if x is non-`null`; `SafeNone` otherwise. 

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def apply [T] (x: T): SafeOption [T] =
  if (x == null) SafeNone
  else new SafeSome (x)

//-----------------------------------------------------------------------------
/**
Safe option factory method that returns an ''empty'' option.

@tparam T Type of optional value to be stored.  Note that instances of T
subclasses can be stored and the result will still have a SafeOption [T] type.

@return An empty safe option value, i.e. `SafeNone`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def empty [T]: SafeOption [T] = SafeNone

//-----------------------------------------------------------------------------
/**
Implicit conversion of a SafeOption value to a standard Scala Option value.

@param x SafeOption value to be converted.

@return Equivalent Option to `x`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final implicit def safeOptionToOption [T] (x: SafeOption [T]): Option [T] =
  x match {
    case SafeSome (v) => Some (v)
    case SafeNone => None
  }

//-----------------------------------------------------------------------------
/**
Implicit conversion of a standard Scala Option value to a SafeOption value.

@param x Option value to be converted.

@return Equivalent SafeOption to `x`.

@throws [[java.lang.NullPointerException]] if optional stored value is `null`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final implicit def optionToSafeOption [T] (x: Option [T]): SafeOption [T] = {
    requireNonNull (x)
    x match {
      case Some (v) => SafeSome (v)
      case None => SafeNone
    }
  }
}

//=============================================================================
/**
Object representing a non-missing optional value in [[org.facsim.SafeOption!]].

@constructor Create a new optional value with the specified, non-missing
contents.

@param x Non-missing optional value to be stored, which cannot be `null`.

@throws [[java.lang.NullPointerException]] if x is `null`.

@since 0.0
*/
//=============================================================================

final case class SafeSome [+T] (x: T) extends SafeOption [T] {

/*
If we've been passed a null reference, then throw an exception.

The fact that SafeSome does not permit storage of null values is the primary
difference between SafeSome and the scala.Some class provided with the Scala
language.

Note also that we deliberately do not use the requireNonNull macro here - this
is because the macro requires an AnyRef argument, while x could be an Any -
even an AnyVal.
*/

  if (x == null) throw new NullPointerException (LibResource ("requireNonNull",
  "x"))

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.SafeOption!.isEmpty]]
*/
//-----------------------------------------------------------------------------

  final override def isEmpty = false

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.SafeOption!.
 */
//-----------------------------------------------------------------------------

  final override def get = x
}

//=============================================================================
/**
Object representing a missing optional value in [[org.facsim.SafeOption!]].

@since 0.0
*/
//=============================================================================

case object SafeNone extends SafeOption [Nothing] {

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.SafeOption!.isEmpty]]
*/
//-----------------------------------------------------------------------------

  final override def isEmpty = true

//-----------------------------------------------------------------------------
/*
@see [[org.facsim.SafeOption!.
 */
//-----------------------------------------------------------------------------

  final override def get = throw new NoSuchElementException (LibResource
  ("SafeNone.get"))
}