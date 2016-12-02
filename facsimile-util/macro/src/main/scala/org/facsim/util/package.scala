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
 * Scala source file belonging to the org.facsim.util package.
 */
package org.facsim

/*
 * The Scala "macros" language feature is currently experimental, and needs to be enabled via the import statement
 * below.
 */
import java.io.File
import java.net.{URI, URL}
import java.time.ZonedDateTime
import java.util.jar.JarFile
import java.util.{Date, GregorianCalendar}
import scala.annotation.elidable
import scala.language.implicitConversions
import scala.language.experimental.macros
import scala.reflect.macros.blackbox.Context

/**
 * ''[[http://facsim.org/ Facsimile]]'' Simulation Library miscellaneous utilities.
 *
 * Package providing miscellaneous utility elements.
 *
 * @note ''Facsimile'' is, first and foremost, a simulation library. It is not a collection of miscellaneous, utility
 * classes and functions with general applicability. Only general utilities are publicly accessible.
 *
 * @since 0.0
 */
package object util {

  /**
   * Regular expression to match class argument name.
   */
  private val ClassArgRE = """[0-9A-Za-z_]+\.this\.([0-9A-Za-z_]+)""".r

  /**
   * Regular expression for identifying periods in package path names.
   */
  private val PeriodRegEx = """(\.)""".r

  /**
   * Regular expression for extracting a ''jar'' file URI from a URL.
   */
  private val JarUriRegEx = """^jar\:(.+)\!.+$""".r

  /**
   * Java file separator.
   */
  private[facsim] val FS = "/"

  /**
   * Key for assertNonNull string resource.
   */
  private[util] val AssertNonNullKey = "assertNonNull"

  /**
   * Key for requireNonNull string resource.
   */
  private[util] val RequireNonNullKey = "requireNonNull"

  /**
   * Key for requireValid string resource.
   */
  private[util] val RequireValidKey = "requireValid"

  /**
   * Key for requireFinite string resource.
   */
  private[util] val RequireFiniteKey = "requireFinite"

  /**
   * Implicit conversion of a [[ZonedDateTime]] to a [[Date]].
   *
   * Conversion between pre-''Java 1.8'' `java.util` time classes (such as [[Date]], [[GregorianCalendar]], etc.) and
   * the new post-''Java 1.8'' `java.time` time classes ([[java.time.Instant]], [[ZonedDateTime]], etc) is cumbersome at
   * best. The former could be dispensed with completely if if wasn't for the fact that [[java.text.MessageFormat]]
   * currently supports only the [[Date]] class. This function makes working with the new time classes, and text message
   * formatting, a little more straightforward.
   *
   * @param date Date, expressed as a [[ZonedDateTime]] to be converted.
   *
   * @return `date` expressed as a [[Date]].
   *
   * @throws NullPointerException if `date` is null.
   *
   * @throws IllegalArgumentException if `date` is too large to represent as a [[GregorianCalendar]] value.
   */
  private[facsim] implicit def toDate(date: ZonedDateTime): Date = GregorianCalendar.from(date).getTime

  /**
   * Obtain the resource URL associated with a class's type information.
   *
   * @param elementType Element type instance for which a resource ''URL'' will be sought.
   *
   * @return Resource ''URL'' associated with `elementType`, if found.
   *
   * @throws NoSuchElementException if `elementType` has no associated resource ''URL''.
   */
  private[util] def resourceUrl(elementType: Class[_]) = {

    /*
     * Argument validation.
     */
    assert(elementType ne null) //scalastyle:ignore null

    /*
     * NOTE: This is a rather convoluted process. If you know of a better (i.e. simpler or quicker) approach, feel free
     * to implement it.
     *
     * Retrieve the name of the class, and convert it into a resource path. To do this, we need to prefix it with a
     * slash, replace all periods with slashes and add a ".class" extension.
     *
     * Note: The Class[T].getSimpleName method crashes for some Scala elements. This is a known bug. Refer to
     * [[https://issues.scala-lang.org/browse/SI-2034 Scala Issue SI-2034]] for further details.
     */
    val name = elementType.getName

    val path = FS + PeriodRegEx.replaceAllIn(name, FS) + ".class"

    /*
     * Now retrieve the resource URL for this element path.
     */
    val url = elementType.getResource(path)

    /*
     * If the element URL is null, then its provenance is unknown and we will not be able to find a manifest for it.
     * Throw an exception instead.
     *
     * Typically, we will fail to find a URL if element identifies a Java primitive.
     */
    if(url eq null) { //scalastyle:ignore null
      throw new NoSuchElementException(LibResource("resourceUrl.NoSuchElement", name))
    }

    /*
     * Return the resulting URL.
     */
    url
  }

  /**
   * Obtain the resource URL associated with a class's type information.
   *
   * @param url ''URL'' identifying the location of a ''JAR'' file.
   *
   * @return ''JAR'' file identified by the `url`, if found.
   *
   * @throws NoSuchElementException if `elementType` has no associated resource ''URL''.
   */
  private[util] def jarFile(url: URL) = {

    /*
     * Argument validation.
     */
    assert(url ne null) //scalastyle:ignore null

    /*
     * If the URL identifies a JAR file, then it will be of the (String) form:
     *
     * jar:file:/{path-of-jar-file}!{elementPath}
     *
     * In order to create a JAR file instance, we need to convert this into a hierarchical URI. We do this using a
     * regular expression extraction. What we want is just the file:{path-of-jar-file} portion of the URL.
     *
     * If we do not have such a match, then the element was not loaded from a JAR file, so throw an exception instead.
     */
    val jarUri = url.toString match {
      case JarUriRegEx(uri) => new URI(uri)
      case _ => throw new NoSuchElementException(LibResource("jarFile.NoSuchElement", url.toString))
    }

    /*
     * Create and return a JarFile instance from the obtained uri.
     */
    new JarFile(new File(jarUri))
  }

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
   * IndentationCheckerProvides implementation of the [[assertNonNull]] macro.
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
   * Provides implementation of the [[requireNonNull]] macro.
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
   * Provides implementation of the [[requireValid]] macro.
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
   * Provides implementation of the [[requireFinite]] macro.
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
