/*
 * Facsimile -- A Discrete-Event Simulation Library
 * Copyright © 2004-2016, Michael J Allen.
 *
 * This file is part of Facsimile.
 *
 * Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see
 * http://www.gnu.org/licenses/lgpl.
 *
 * The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
 * project home page at:
 *
 *   http://facsim.org/
 *
 * Thank you for your interest in the Facsimile project!
 *
 * IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
 * inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
 * your code fails to comply with the standard, then your patches will be rejected. For further information, please
 * visit the coding standards at:
 *
 *   http://facsim.org/Documentation/CodingStandards/
 * =====================================================================================================================
 * Scala source file belonging to the org.facsim package.
 */
package org

/*
 * The Scala "macros" language feature is currently experimental, and needs to be enabled via the import statement
 * below.
 */
import scala.annotation.elidable
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

/**
 * ''[[http://facsim.org/ Facsimile]]'' Simulation Library.
 *
 * ''Facsimile'' is a high-quality, 3D, discrete-event simulation library designed from the ground up to assist with
 * performance analysis of production, manufacturing and warehousing & distribution facilities.
 *
 * @since 0.0
 */
package object facsim {

  /**
   * Regular expression to match class argument name.
   */
  private val ClassArgRE = """[0-9A-Za-z_]+\.this\.([0-9A-Za-z_]+)""".r

  /**
   * Key for assertNonNull string resource.
   */
  private[facsim] val AssertNonNullKey = "assertNonNull"

  /**
   * Key for requireNonNull string resource.
   */
  private[facsim] val RequireNonNullKey = "requireNonNull"

  /**
   * Key for requireValid string resource.
   */
  private[facsim] val RequireValidKey = "requireValid"

  /**
   * Key for requireFinite string resource.
   */
  private[facsim] val RequireFiniteKey = "requireFinite"

  /**
   * Assertion that a value is not null.
   *
   * Code using this assertion is only generated if the `-Xelide-below` Scala compiler option is at least ASSERTION.
   *
   * @note Assertions should only be used to verify internal state; they must '''never''' be used to verify external
   * state (use the require methods to verify external state instead).
   *
   * @param arg Argument whose value is to be compared to `null`.
   *
   * @throws java.lang.AssertionError if `arg` is `null`
   *
   * @since 0.0
   */
  @elidable(elidable.ASSERTION)
  def assertNonNull(arg: AnyRef): Unit = macro assertNonNullImpl

  /**
   * Require that argument value is non-`null`.
   *
   * Throw a [[NullPointerException]] if supplied argument value is `null`.
   *
   * Normally, a `NullPointerException` will be thrown by the ''Java'' virtual machine (''JVM'') if an attempt is made
   * to dereference a `null` pointer. However, if a function takes an object reference argument and that argument is not
   * dereferenced until after the function has returned, then the function must verify that the reference is non-`null`
   * as one of its preconditions; this function makes such precondition verification simpler.
   *
   * Furthermore, even if the ''JVM'' can be relied upon to throw this exception, performing this verification
   * explicitly is regarded as good practice. One reason is that exceptions thrown by the ''JVM'' provide limited
   * explanation to the user as to their cause; this function provides an explanation automatically.
   *
   * @param arg Argument whose value is to be compared to `null`.
   *
   * @throws NullPointerException if `arg` is `null`.
   *
   * @since 0.0
   */
  def requireNonNull(arg: AnyRef): Unit = macro requireNonNullImpl

  /**
   * Require that argument value is valid.
   *
   * Throw a [[IllegalArgumentException]] if supplied parameter value is invalid.
   *
   * @note This function supersedes the [[Predef]] `require` methods.
   *
   * @note Tests for non-`null` argument values should be verified by the `requireNonNull` function.
   *
   * @param arg Argument being verified.
   *
   * @param isValid Flag representing the result of a condition determining the validity of `arg`. If `true`, function
   * merely returns; if `false` an `IllegalArgumentException` is raised.
   *
   * @throws IllegalArgumentException if `isValid` is `false`.
   *
   * @since 0.0
   */
  def requireValid(arg: Any, isValid: Boolean): Unit = macro requireValidImpl

  /**
   * Require a finite double value.
   *
   * Double arguments that equate to `NaN` (''not a number'') or ''infinity'' will result in a
   * [[IllegalArgumentException]] being thrown.
   *
   * @param arg Argument whose value is being validated.
   *
   * @throws IllegalArgumentException if `arg` does not have a finite value.
   *
   * @since 0.0
   */
  def requireFinite(arg: Double): Unit = macro requireFiniteImpl

  /**
   * Clean argument names.
   *
   * Class argument names are prefixed by "{ClassName}.this." (where "{ClassName}" is the name of the class to which the
   * argument belongs), which creates confusion when identifying failing arguments, and testing that failed argument
   * messages match expected messages. This function removes class argument prefixes so that the raw argument name is
   * returned.
   *
   * @param arg A class or method argument to be cleaned.
   *
   * @return Cleaned argument name, matching the value expected by the user.
   *
   * @since 0.0
   */
  def cleanArgName(arg: String): String = arg match {

    /*
     * If this is a class argument, remove the prefix and return the actual name of the argument.
     */
    case ClassArgRE(classArg) => classArg

    /*
     * Otherwise, just return the value supplied.
     */
    case basicArg: String => basicArg
  }

  /**
   * Convert an expression into a string expression.
   *
   * @param c AST context for the conversion.
   *
   * @param arg Expression to be converted.
   *
   * @return String expression capturing contents of original expression.
   */
  private def exprAsString(c: Context)(arg: c.Expr[Any]): c.Expr[String] = {
    import c.universe._
    c.Expr[String](Literal(Constant(show(arg.tree))))
  }

  /**
   * IndentationCheckerProvides implementation of the [[org.facsim.assertNonNull]] macro.
   *
   * @param c Abstract syntax tree (AST) context for this macro definition.
   *
   * @param arg Argument whose value is to be tested. If this argument evaluates to `null`, then a [[AssertionError]] is
   * thrown by the macro implementation, together with the name of the failed argument.
   *
   * @return Implementation of this instance of the `assertNonNull` macro.
   *
   * @since 0.0
   */
  def assertNonNullImpl(c: Context)(arg: c.Expr[AnyRef]): c.Expr[Unit] = {

    /*
     * Convert the argument into a string that represents the expression that was passed to the requireNonNull macro -
     * we'll output that as part of the exception.
     */
    import c.universe._
    val argString = exprAsString(c)(arg)

    /*
     * Generate the AST to be substituted for the macro reference.
     *
     * If the argument evaluates to be null, throw an AssertionError with some useful information.
     */
    reify {
      //scalastyle:off null
      if(arg.splice eq null) {
        throw new AssertionError(LibResource(AssertNonNullKey, cleanArgName(argString.splice)), null)
      }
      //scalastyle:on null
    }
  }

  /**
   * Provides implementation of the [[org.facsim.requireNonNull]] macro.
   *
   * @param c Abstract syntax tree (AST) context for this macro definition.
   *
   * @param arg Argument whose value is to be tested. If this argument evaluates to `null`, then a
   * [[NullPointerException]] is thrown by the macro implementation, together with the name of the failed
   * argument.
   *
   * @return Implementation of this instance of the `requireNonNull` macro.
   *
   * @since 0.0
   */
  def requireNonNullImpl(c: Context)(arg: c.Expr[AnyRef]): c.Expr[Unit] = {

    /*
     * Convert the argument into a string that represents the expression that was passed to the requireNonNull macro -
     * we'll output that as part of the exception.
     */
    import c.universe._
    val argString = exprAsString(c)(arg)

    /*
     * Generate the AST to be substituted for the macro reference.
     *
     * If the argument evaluates to be null, throw a NullPointerException with some useful information.
     */
    reify {
      if(arg.splice eq null) { //scalastyle:ignore null
        throw new NullPointerException(LibResource(RequireNonNullKey, cleanArgName(argString.splice)))
      }
    }
  }

  /**
   * Provides implementation of the [[org.facsim.requireValid]] macro.
   *
   * @param c Abstract syntax tree (AST) context for this macro definition.
   *
   * @param arg Argument whose value is to be tested. If `isValid` is evaluated to `false`, then a
   * [[IllegalArgumentException]] is thrown by the macro implementation, together with the name of the failed
   * argument.
   *
   * @param isValid Flag representing the result of a condition determining the validity of `arg`. If `true`, function
   * merely returns; if `false` an `IllegalArgumentException` is raised.
   *
   * @return Implementation of this instance of the `requireValid` macro.
   *
   * @since 0.0
   */
  def requireValidImpl(c: Context)(arg: c.Expr[Any], isValid: c.Expr[Boolean]): c.Expr[Unit] = {

    /*
     * Convert the arguments to strings.
     */
    import c.universe._
    val argString = exprAsString(c)(arg)

    /*
     * Generate the AST to be substituted for the macro reference.
     *
     * If the argument is deemed invalid, then throw an IllegalArgumentException with some useful information.
     */
    reify {
      if(!isValid.splice) throw new IllegalArgumentException(LibResource(RequireValidKey,
      cleanArgName(argString.splice), arg.splice.toString))
    }
  }

  /**
   * Provides implementation of the [[org.facsim.requireFinite]] macro.
   *
   * @param c Abstract syntax tree (AST) context for this macro definition.
   *
   * @param arg Argument whose value is to be tested. If evaluated as `NaN`, `+∞` or `-∞`, then a
   * [[IllegalArgumentException]] is thrown by the macro implementation, together with the name of the failed argument.
   *
   * @return Implementation of this instance of the `requireFinite` macro.
   *
   * @since 0.0
   */
  def requireFiniteImpl(c: Context)(arg: c.Expr[Double]): c.Expr[Unit] = {

    /*
     * Convert the argument to a string.
     */
    import c.universe._
    val argString = exprAsString(c)(arg)

    /*
     * Generate the AST to be substituted for the macro reference.
     *
     * Determine whether the value is finite; if not, then throw an exception.
     */
    reify {
      if(arg.splice.isNaN || arg.splice.isInfinite) {
        throw new IllegalArgumentException(LibResource(RequireFiniteKey, cleanArgName(argString.splice), arg.splice))
      }
    }
  }
}
