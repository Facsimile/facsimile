//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2025, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================

//======================================================================================================================
// Scala source file belonging to the org.facsim.util package.
//======================================================================================================================
package org.facsim.util

// The Scala "macros" language feature is currently experimental, and needs to be enabled via the import statement
// below.
import java.io.File
import java.net.{URI, URL}
import java.time.ZonedDateTime
import java.util.{Date, GregorianCalendar}
import java.util.jar.JarFile
import scala.annotation.elidable
import scala.language.implicitConversions
import scala.quoted.{Expr, Quotes, Type}
import scala.util.matching.Regex

/** Regular expression for identifying periods in package path names.
 */
private val PeriodRegEx = """(\.)""".r

/** Regular expression for extracting a _jar_ file _URI_ from a _URL_.
 *
 *  _jar URL_s are typically of the form:
 *
 *  `jar:file:/_path/to/jar/file.jar_!/_some/package/classname_.class`.
 *
 *  For example, in _Java_ 8, the URL for the [[java.lang.String]] class might look like this (on an _Ubuntu_ system):
 *
 *  `jar:file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar!/java/lang/String.class`
 *
 *  such that `file:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/rt.jar` is the URI for the _JAR_ file that contains this
 *  class.
 *
 *  @note Such URLs are reported for types packaged as _JAR_ files; this may not be the case for types packages in other
 *  formats.
 */
private val JarUriRegEx = """^jar:(.+)!.+$""".r

/** Regular expression for extracting a _Java module name_ from a _URL_.
 *
 *  _jrt URL_s are typically of the form:
 *
 *  `jrt:/_module-name_/_some/package/classname_.class`.
 *
 *  For example, in _Java_ 11, the URL for the [[java.lang.String]] class might look like this:
 *
 *  `jrt:/java.base/java/lang/String.class`
 *
 *  such that `java.base` is the module-bane that contains this class.
 *
 *  @note Such URLs are reported for types packaged as _JIMAGE_ files; this may not be the case for types packaged in
 *  other formats.
 */
private val JrtModuleNameRegEx = """^jrt:/(java\.[a-z_]+)/.+$""".r

/** File separator.
 */
private[facsim] val FS: String = File.separator.nn

/** _JAR_ file class file separator.
 */
private[facsim] val JFS: String = "/"

/** Path separator.
 */
private[facsim] val PS: String = File.pathSeparator.nn

/** Line separator.
 */
private[facsim] val LS: String = sys.props("line.separator")

/** Single quote character.
 */
private[facsim] val SQ: String = "'"

/** Double quote character.
 */
private[facsim] val DQ: String = "\""

/** Comparison return value signaling _less than_.
 *
 *  @note This value should not be compared with the results of comparison functions in general, since _less than_
 *  comparison results can be signaled by any negative value; instead, this value should only be used as a return value
 *  indicating a _less than_ comparison result.
 */
private[facsim] val CompareLessThan = -1

/** Comparison return value signaling equality.
 */
private[facsim] val CompareEqualTo = 0

/** Comparison return value signaling greater than.
 *
 *  @note This value should not be compared with the results of comparison functions in general, since _greater than_
 *  comparison results can be signaled by any positive value; instead, this value should only be used as a return value
 *  indicating a _greater than_ comparison result.
 */
private[facsim] val CompareGreaterThan = 1

/** Key for assertNonNull string resource.
 */
private[facsim] val AssertNonNullKey = "assertNonNull"

/** Key for requireNonNull string resource.
 */
private[facsim] val RequireNonNullKey = "requireNonNull"

/** Key for requireValid string resource.
 */
private[facsim] val RequireValidKey = "requireValid"

/** Key for requireFinite string resource.
 */
private[facsim] val RequireFiniteKey = "requireFinite"

/** Implicit conversion of a [[java.time.ZonedDateTime]] to a [[java.util.Date]].
 *
 *  Conversion between pre-_Java 1.8_ `java.util` time classes (such as `Date`, [[java.util.GregorianCalendar]], etc.)
 *  and the new post-_Java 1.8_ `java.time` time classes ([[java.time.Instant]], `ZonedDateTime`, etc) is cumbersome at
 *  best. The former could be dispensed with completely if it wasn't for the fact that [[java.text.MessageFormat]]
 *  currently supports only the `Date` class. This function makes working with the new time classes, and text message
 *  formatting, a little more straightforward.
 *
 *  @throws java.lang.IllegalArgumentException if `date` is too large to represent as a `GregorianCalendar` value.
 */
private[facsim] given toDate: Conversion[ZonedDateTime, Date] =
  (date: ZonedDateTime) => GregorianCalendar.from(date).nn.getTime.nn

/** Obtain the resource _URL_ associated with a class's type information.
 *
 *  @param elementType Element type instance for which a resource _URL_ will be sought.
 *
 *  @return Resource _URL_ associated with `elementType` wrapped in [[scala.Some]], or [[scala.None]] if the element
 *  type's resource _URL_ could not be identified.
 */
private[util] def resourceUrl(elementType: Class[?]): Option[URL] =

  // (BTW, this is a rather convoluted process. If you know of a better (i.e. simpler or quicker) approach, feel free to
  // implement it...)
  //
  // Retrieve the name of the class, and convert it into a resource path. To do this, we need to prefix it with a slash,
  // replace all periods with slashes and add a ".class" extension.
  //
  // NOTE: DO NOT use the system-dependent separator character, as only slashes (not backslashes, as on Windows) are
  // accepted. We quote the replacement string (the slash) just in case it contains characters that require quoting.
  val name = elementType.getName.nn
  val correctedName = PeriodRegEx.replaceAllIn(name, Regex.quoteReplacement(JFS))
  val path = s"$JFS$correctedName.class"

  // Now retrieve the resource URL for this element path and wrap it in an Option.
  val pathRsc = elementType.getResource(path)
  if pathRsc == null then None
  else Some(pathRsc.nn)

/** Obtain the manifest associated with the specified element type.
 *
 *  @param elementType Element type instance for which a manifest will be sought.
 *
 *  @return Manifest associated with `elementType`.
 */
private[util] def manifestOf(elementType: Class[?]): Manifest =

  // If a URL could not be identified, return the null manifest. Otherwise, process the resulting URL.
  resourceUrl(elementType).fold[Manifest](NullManifest): url =>
    url.toString match

      // If the URL identifies a JAR file, then it will be of the (String) form:
      //
      // jar:file:/{path-of-jar-file}!/{element-path}
      //
      // The jar file URI (file:/{path-of-jar-file}) is extracted from this URL, in the form of a string, and used to
      // create a new JAR file instance. The Java manifest is then retrieved, and used to populate the returned
      // Facsimile Manifest instance.
      //
      // Note: As of Java 9, the Java runtime library is no longer packaged in a JAR file, and so URLs for Java standard
      // runtime classes will not resolve as having JAR file URLs.
      case JarUriRegEx(uri) =>

        // Retrieve the manifest for the indicated JAR file.
        val jManifest = Option(new JarFile(new File(new URI(uri))).getManifest)

        // If the JAR file has no manifest, then use the null manifest, otherwise construct a new JARManifest from the
        // JAR manifest.
        jManifest.fold[Manifest](NullManifest)(m => new JARManifest(m.nn))

      // If the URL identifies a class belonging to a module in a Java image file (JIMAGE), then the URL will be of the
      // (String) form:
      //
      // jrt:/{module-name}/{element-path}
      //
      // For example, the java.lang.String class has the URL (from Java 9 onwards):
      //
      // jrt:/java.base/java/lang/String.class
      //
      // If the module name begins "java.", then return the JREManifest, which simulates a JAR-type manifest.
      //
      // Note: URLs of this form are only returned if using Java 9+ runtimes.
      case JrtModuleNameRegEx(_) => JREManifest

      // If there's no match on the URL, report a null manifest instead.
      case _ => NullManifest

/** Assertion that a possibly `null` expression is not actually `null`.
 *
 *  Code using this assertion is only generated if the `-Xelide-below` _Scala_ compiler option is at least `ASSERTION`.
 *
 *  @note Assertions should only be used to verify internal state; they must _never_ be used to verify external state
 *  (use the [[scala.Predef.require()]] methods to verify external state instead), since assertions will not execute in
 *  production code.
 *
 *  @tparam T Type of expression if not `null`.
 *
 *  @param expr Nullable expression whose value is to be compared to `null`.
 *
 *  @throws java.lang.AssertionError if `expr` is `null`.
 *
 *  @since 0.0
 */
@elidable(elidable.ASSERTION)
inline def assertNonNull[T](inline expr: T | Null): Unit = ${assertNonNullImpl('expr)}

/** Require that argument value is valid.
 *
 *  Throw an [[java.lang.IllegalArgumentException]] if supplied parameter value is invalid.
 *
 *  @note This function supersedes the [[scala.Predef.require()]] methods.
 *
 *  @note Tests for non-`null` argument values should be verified by the `requireNonNull` function.
 *
 *  @note This is a non-macro version of [[requireValid()]] for use within the _facsimile-util_ project.
 *
 *  @tparam T Type of argument value.
 *
 *  @param arg Value of the argument being tested.
 *
 *  @param isValid Predicate determining the validity of `arg`. If `true`, function merely returns; if `false` an
 *  `IllegalArgumentException` is raised.
 *
 *  @param name Name of the argument being tested.
 *
 *  @throws java.lang.IllegalArgumentException if `isValid` is `false`.
 */
private[util] def requireValidFn[T](arg: T, isValid: T => Boolean, name: => String): Unit =
  if !isValid(arg) then throw new IllegalArgumentException(LibResource(RequireValidKey, name, arg))

/** Require that argument value is non-`null`.
 *
 *  Throw a [[java.lang.NullPointerException]] if supplied argument value is `null`.
 *
 *  Normally, a `NullPointerException` will be thrown by the _Java_ virtual machine (_JVM_) if an attempt is made to
 *  dereference a `null` pointer. However, if a function takes an object reference argument and that argument is not
 *  dereferenced until after the function has returned, then the function must verify that the reference is non-`null`
 *  as one of its preconditions; this function makes such precondition verification simpler.
 *
 *  Furthermore, even if the _JVM_ can be relied upon to throw this exception, performing this verification explicitly
 *  is regarded as good practice. One reason is that exceptions thrown by the _JVM_ provide limited explanation to the
 *  user as to their cause; this function provides an explanation automatically.
 *
 *  @tparam T Type for non-null argument values.
 *
 *  @param arg Nullable argument whose value is to be compared to `null`.
 *
 *  @throws NullPointerException if `arg` is `null`.
 *
 *  @since 0.0
 */
inline def requireNonNull[T](inline arg: T | Null): Unit = ${requireNonNullImpl('arg)}

/** Require that argument value is valid.
 *
 *  Throw an [[java.lang.IllegalArgumentException]] if supplied parameter value is invalid.
 *
 *  @note This function supersedes the [[scala.Predef.require()]] methods.
 *
 *  @note Tests for non-`null` argument values should be verified by the [[requireNonNull()]] function.
 *
 *  @tparam T Type of value represented by the argument.
 *
 *  @param arg Argument being verified.
 *
 *  @param isValid Flag representing the result of a condition determining the validity of `arg`. If `true`, function
 *  merely returns; if `false` an `IllegalArgumentException` is raised.
 *
 *  @throws java.lang.IllegalArgumentException if `isValid` is `false`.
 *
 *  @since 0.0
 */
inline def requireValid[T](inline arg: T, inline isValid: Boolean): Unit = ${requireValidImpl('arg, 'isValid)}

/** Require a finite double value.
 *
 *  Double arguments that equate to `NaN` (_not a number_) or _infinity_ will result in an
 *  [[java.lang.IllegalArgumentException]] being thrown.
 *
 *  @param arg Argument whose value is being validated.
 *
 *  @throws java.lang.IllegalArgumentException if `arg` does not have a finite value.
 *
 *  @since 0.0
 */
inline def requireFinite(inline arg: Double): Unit = ${requireFiniteImpl('arg)}

/** Implementation of the [[assertNonNull())]] macro.
 *
 *  @param expr Possibly `null` expression to be asserted as non-`null`; if this value is `null`, then an
 *  [[java.lang.AssertionError]] is thrown by the macro implementation, together with failed expression.
 *
 *  @return Implementation of this instance of the `assertNonNull` macro.
 *
 *  @since 0.0
 */
private def assertNonNullImpl[T: Type](expr: Expr[T | Null])(using Quotes): Expr[Unit] = '{

  // Text whether the expression is null. If it is, then throw an assertion exception with the failed expression
  // as a string.
  if $expr == null then
    val exprAsString = ${Expr(expr.show)}
    throw new AssertionError(LibResource(AssertNonNullKey, exprAsString), null)
}

/** Implementation of the [[requireNonNull()]] macro.
 *
 *  @tparam T Type of non-null values of the argument.
 *
 *  @param arg Argument whose value is to be tested. If this argument evaluates to `null`, then a
 *  [[java.lang.NullPointerException]] is thrown by the macro implementation, together with the name of the failed
 *  argument.
 *
 *  @return Implementation of this instance of the `requireNonNull` macro.
 *
 *  @since 0.0
 */
def requireNonNullImpl[T: Type](arg: Expr[T | Null])(using Quotes): Expr[Unit] = '{

  // If the argument is null, then throw the NullPointerException.
  if $arg == null then
    val argAsString = ${Expr(arg.show)}
    throw new NullPointerException(LibResource(RequireNonNullKey, argAsString))
}

/** Provides implementation of the [[requireValid()]] macro.
 *
 *  @param arg Argument whose value is to be tested. If `isValid` is evaluated to `false`, then an
 *  [[java.lang.IllegalArgumentException]] is thrown by the macro implementation, together with the name of the failed
 *  argument.
 *
 *  @param isValid Flag representing the result of a condition determining the validity of `arg`. If `true`, function
 *  merely returns; if `false` an `IllegalArgumentException` is raised.
 *
 *  @return Implementation of this instance of the `requireValid` macro.
 *
 *  @since 0.0
 */
def requireValidImpl[T: Type](arg: Expr[T], isValid: Expr[Boolean])(using Quotes): Expr[Unit] = '{

  // Evaluate the argument to determine if it is valid. If it is not, then throw the exception.
  if !${isValid} then
    val argValue = $arg
    val argAsString = ${Expr(arg.show)}
    throw new IllegalArgumentException(LibResource(RequireValidKey, argAsString, argValue.toString))
}

/** Provides implementation of the [[requireFinite()]] macro.
 *
 *  @param arg Argument whose value is to be tested. If evaluated as `NaN`, `+∞` or `-∞`, then an
 *  [[java.lang.IllegalArgumentException]] is thrown by the macro implementation, together with the name of the failed
 *  argument.
 *
 *  @return Implementation of this instance of the `requireFinite` macro.
 *
 *  @since 0.0
 */
def requireFiniteImpl(arg: Expr[Double])(using Quotes): Expr[Unit] = '{

  // Determine whether the value is finite; if not, then throw an exception.
  val argResult = $arg
  if argResult.isNaN || argResult.isInfinite then
    val argAsString = ${Expr(arg.show)}
    throw new IllegalArgumentException(LibResource(RequireFiniteKey, argAsString, argResult))
}