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
Scala source file defining the org.facsim package.
*/
//=============================================================================

package org

import scala.reflect.macros.Context

//=============================================================================
/**
''[[http://facsim.org/ Facsimile]]'' Simulation Library.

''Facsimile'' is a high-quality, 3D, discrete-event simulation library
designed from the ground up to assist with performance analysis of production,
manufacturing and warehousing & distribution facilities.

@since 0.0
*/
//=============================================================================

package object facsim {

/*
The Scala "macros" language feature is currently experimental, and needs to be
enabled via this import statements.
*/

  import scala.language.experimental.macros

//-----------------------------------------------------------------------------
/**
Require that argument value is non-`null`.

Throw a [[java.lang.NullPointerException!]] if supplied argument value is
`null`.

Normally, a `NullPointerException` will be thrown by the ''Java'' virtual
machine (''JVM'') if an attempt is made to dereference a `null` pointer.
However, if a function takes an object reference argument and that argument is
not dereferenced until after the function has returned, then the function must
verify that the reference is non-`null` as one of its preconditions; this
function makes such precondition verification simpler.

Furthermore, even if the ''JVM'' can be relied upon to throw this exception,
performing this verification explicitly is regarded as good practice.  One
reason is that exceptions thrown by the ''JVM'' provide limited explanation to
the user as to their cause; this function provides an explanation
automatically.

@param arg Argument whose value is to be compared to `null`.

@throws java.lang.NullPointerException if '''arg''' is `null`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def requireNonNull (arg: AnyRef): Unit = macro requireNonNullImpl

//-----------------------------------------------------------------------------
/**
Require that argument value is valid.

Throw a [[java.lang.IllegalArgumentException!]] if supplied parameter value is
invalid.

@note This function supersedes the [[scala.Predef$]] `require` methods.

@note Tests for non-`null` argument values should be verified by the
`requireNonNull` function.

@param arg Argument being verified.

@param isValid Flag representing the result of a condition determining the
validity of '''arg'''.  If `true`, function merely returns; if `false` an
`IllegalArgumentException` is raised.

@throws java.lang.IllegalArgumentException if '''isValid''' is `false`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def requireValid (arg: Any, isValid: Boolean): Unit =
  macro requireValidImpl

//-----------------------------------------------------------------------------
/**
Require a finite double value.

Double arguments that equate to `NaN` (''not a number'') or ''infinity'' will
result in an [[java.lang.IllegalArgumentException!]] being thrown.

@param arg Argument whose value is being validated.

@throws java.lang.IllegalArgumentException if '''arg''' does not have a finite
value.
*/
//-----------------------------------------------------------------------------

  final def requireFinite (arg: Double): Unit = macro requireFiniteImpl

//-----------------------------------------------------------------------------
/**
Convert an expression into a string expression.

@param c AST context for the conversion.

@param arg Expression to be converted.

@return String expression capturing contents of original expression.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def exprAsString (c: Context)(arg: c.Expr [Any]):
  c.Expr [String] = {
    import c.universe._
    c.Expr [String] (Literal (Constant (show (arg.tree))))
  }

//-----------------------------------------------------------------------------
/**
Provides implementation of the [[org.facsim.requireNonNull(AnyRef)*]] macro.

@param c Abstract syntax tree (AST) context for this macro definition.

@param arg Argument whose value is to be tested.  If this argument evaluates to
`null`, then a [[java.lang.NullPointerException!]] is thrown by the macro
implementation, together with the name of the failed argument.

@return Implementation of this instance of the ''requireNonNull'' macro.
 
@since 0.0
*/
//-----------------------------------------------------------------------------

  final def requireNonNullImpl (c: Context)(arg: c.Expr [AnyRef]):
  c.Expr [Unit] = {

/*
Convert the argument into a string that represents the expression that was
passed to the requireNonNull macro - we'll output that as part of the
exception.
*/

    import c.universe._
    val argString = exprAsString (c)(arg)

/*
Generate the AST to be substituted for the macro reference.

If the argument evaluates to be null, throw a NullPointerException with some
useful information.
*/

    reify {
      if (arg.splice eq null) throw new NullPointerException (LibResource
      ("requireNonNull", argString.splice))
    }
  }

//-----------------------------------------------------------------------------
/**
Provides implementation of the [[org.facsim.requireValid(Any,Boolean)*]] macro.

@param c Abstract syntax tree (AST) context for this macro definition.

@param arg Argument whose value is to be tested.  If '''isValid''' is evaluated
to `false`, then a [[java.lang.IllegalArgumentException!]] is thrown by the
macro implementation, together with the name of the failed argument.

@return Implementation of this instance of the ''requireValid'' macro.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def requireValidImpl (c: Context)(arg: c.Expr [Any], isValid: c.Expr
  [Boolean]): c.Expr [Unit] = {

/*
Convert the arguments to strings.
*/

    import c.universe._
    val argString = exprAsString (c)(arg)

/*
Generate the AST to be substituted for the macro reference.

If the argument is deemed invalid, then throw an IllegalArgumentException with
some useful information.
*/

    reify {
      if (!isValid.splice) throw new IllegalArgumentException (LibResource
      ("requireValid", argString.splice, arg.splice))
    }
  }

//-----------------------------------------------------------------------------
/**
Provides implementation of the [[org.facsim.requireFinite(Double)*]] macro.

@param c Abstract syntax tree (AST) context for this macro definition.

@param arg Argument whose value is to be tested.  If evaluated as `NaN`, `+∞`
or `-∞`, then a [[java.lang.IllegalArgumentException!]] is thrown by the macro
implementation, together with the name of the failed argument.

@return Implementation of this instance of the ''requireFinite'' macro.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def requireFiniteImpl (c: Context)(arg: c.Expr [Double]):
  c.Expr [Unit] = {

/*
Convert the argument to a string.
*/

    import c.universe._
    val argString = exprAsString (c)(arg)

/*
Generate the AST to be substituted for the macro reference.

Determine whether the value is valid.
*/

    reify {
      arg.splice match {

/*
In the case of the following values, throw the exception.

Note: It's not possible to pattern match on NaN (since NaN != NaN), hence the
slightly convoluted syntax of the first match statement.
*/

        case x if x.isNaN => throw new IllegalArgumentException (LibResource
        ("requireFinite", argString.splice, 0))
        case Double.PositiveInfinity => throw new
        IllegalArgumentException (LibResource ("requireFinite",
        argString.splice, 1))
        case Double.NegativeInfinity =>  throw new
        IllegalArgumentException (LibResource ("requireFinite",
        argString.splice, 2))

/*
All other values are valid, so do nothing.
*/

        case _ =>
      }
    }
  }
}
