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
Scala source file defining the org.facsim package.
*/
//=============================================================================

package org

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

//-----------------------------------------------------------------------------
/**
Require that argument value is non-`null`.

Throw a [[java.lang.NullPointerException]] if supplied argument value is
`null`.

Normally, a `NullPointerException` will be thrown by the ''Java'' virtual
machine (''JVM'') if an attempt is made to de-reference a `null` pointer.
However, if a function takes an object reference argument and that argument is
not de-referenced until after the function has returned, then the function must
verify that the reference is non-`null` as one of its preconditions; this
function makes such precondition verification simpler.

Furthermore, even if the ''JVM'' can be relied upon to throw this exception,
performing this verification explicitly is regarded as good practice.  One
reason is that exceptions thrown by the ''JVM'' provide limited explanation to
the user as to their cause; this function provides an explanation
automatically.

@note All exceptions originating in ''Facsimile'' library code must provide a
detailed, locale-specific explanation to the user.

@todo It would be nice, once Scala 2.10 is available, if this could be
converted into a macro.  The macro would take a single parameter, which is the
argument expression.  The "name" of the argument could, presumably, be
extracted from the expression, and it's value compared to null reified.

@param argName Name of argument whose value is being verified.

@param argValue Argument value to be compared to `null`.

@throws java.lang.NullPointerException if '''argValue''' is `null`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  def requireNonNull (argName: String, argValue: AnyRef): Unit = if (argValue
  eq null) throw new NullPointerException (LibResource.format
  ("requireNonNull", argName))

//-----------------------------------------------------------------------------
/**
Require that argument value is valid.

Throw a [[java.lang.IllegalArgumentException]] if function parameter value is
invalid.

@note This function supersedes the [[scala.Predef#require()]] methods.

@note Tests for non-`null` argument values should be verified by the
`requireNonNull` function.

@note All exceptions originating in ''Facsimile'' library code must provide a
detailed, locale-specific explanation to the user.

@todo It would be nice, once Scala 2.10 is available, if this could be
converted into a macro.  The macro would take two parameters, the first being
the argument expression.  The "name" of the argument could, presumably, be
extracted from the expression, and it's value reified.

@param argName Name of argument whose value is being verified.

@param argValue Value of argument being verified.

@param isValid Flag representing the result of a condition determining the
validity of '''argValue'''.  If `true`, function merely returns; if `false` an
`IllegalArgumentException` is raised.

@throws java.lang.IllegalArgumentException if '''isValid''' is `false`.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  def requireValid (argName: String, argValue: Any, isValid: Boolean): Unit =
  if (!isValid) throw new IllegalArgumentException (LibResource.format
  ("requireValid", argName, argValue.toString))
}
