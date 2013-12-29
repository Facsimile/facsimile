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
Scala source file from the org.facsim.io package.
*/
//=============================================================================

package org.facsim.io

import java.io.BufferedReader
import java.io.EOFException
import java.io.Reader
import java.lang.StringBuilder
import org.facsim.LibResource
import org.facsim.requireNonNull

//=============================================================================
/**
A reader for text data streams.

This class simplifies the parsing of text streams by reading the next field
(delimited as specified) from the stream and returning it as the required data
type after optional verification.

Furthermore, this reader harmonizes line termination sequences, so that all
lines appear to terminate with a ''line feed'' character
('\n')&mdash;regardless of the actual line termination sequence.  (Typically,
''Windows'', ''DOS'' and ''OS/2'' text streams terminate lines with a
''carriage return'' character followed by a ''line feed'' character ('\r\n');
''Unix'' and ''Unix''-like systems, such as ''Linux'', ''Mac OS'' (versions X
and higher) and the ''BSD'' variants, terminate with a single ''line feed''
character ('\n'); older Mac OS systems (9 and earlier) terminate with a single
''carriage return'' character ('\r').  This reader, and its subclasses, allow
all text streams to be treated as though they come from a ''Unix''-like system.

@todo Implement support for comments (ignore remainder of line, whole of line).

@todo Implement support for quoted fields (such as those in CSV files).

@todo Implement support for named fields (to assist with error reporting).

@constructor Constructs a new reader from a data stream reader.

@param textReader Reader from which data is to be read.

@param defaultDelimiter Default set of characters and associated rules for
delimiting fields, if an appropriate delimiter is not explicitly or implicitly
specified for a field read operation. If omitted, this argument defaults to the
WhitespaceDelimiter.

@throws java.lang.NullPointerException if reader is null.

@since 0.0
*/
//=============================================================================

class TextReader (textReader: Reader,
defaultDelimiter: Delimiter = WhitespaceDelimiter)
extends NotNull {

/*
Preconditions: textReader cannot be null.
*/

  requireNonNull (textReader)

/**
Create a buffered reader, if the specified reader is not already a buffered
reader.  Use of a buffered reader will boost performance and provide additional
functionality.

@note This may change.  It may work out better to provide our own buffering
using a regular reader.
*/

  private final val reader: BufferedReader = textReader match {
    case r: BufferedReader => r
    case r: Reader => new BufferedReader (r, TextReader.BufferSize)
  }

/**
Current state of this text reader.
*/

  private final val state = new State ()

//-----------------------------------------------------------------------------
/**
Class representing current state of reader.

This class maintains row & column numbers, marks streams for subsequent reset
operations (in the event that an exception occurs during a field read), etc.

@constructor Create new text reader state with specified characteristics.

@param lastChar Last character read from the stream.  This records the last
character actually read from the stream, and is used in processing line
termination sequences.  If nothing has been read from the stream, this value
will be 0.

@param peekedChar Last peeked character read from the reader.  If no character
has been peeked, the value is `None`.

@param row Row from which we're currently reading data.  By convention, line
numbering begins at 1 and is incremented each time a line termination sequence
is read.  Line numbering is not to be updated until a line termination sequence
has been read (rather than being peeked).

@param column Column from which the next character will be read.  Ideally,
column numbering counts whole Unicode characters, not simply bytes or partial
characters (as can be the case in ''UTF-8'' and ''UTF-16'' formats).  By
convention, column numbering beings at 1 and is incremented each time a
character is read.  The column number must be reset to 1 each time a line
termination sequence has been read&mdash;the same point at which the line
number is incremented.  Column numbering is not to be updated until a character
has been read (rather than being peeked).

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final class State private [this] (private var lastChar: Int, private
  var peekedChar: Option [Int], private var row: Int, private var column: Int)
  extends NotNull {

//.............................................................................
/**
Default auxiliary constructor for new state instances.

The last character read is set to 0 (the ''null character''), which is OK for
initialization purposes.  The peeked character is set to `None`.  Row and
column numbers are initialized to 1.

@since 0.0
*/
//.............................................................................

    def this () = this (0, None, 1, 1)

//.............................................................................
/**
''Copy'' constructor for copying existing state of a text reader.

The copied state is cached in case it needs to be restored later.  This
operation is performed as part of the state caching operation.

@param other State to be copied.

@since 0.0
*/
//.............................................................................

    private def this (other: State) = this (other.lastChar, other.peekedChar,
    other.row, other.column)

//.............................................................................
/**
@see [[org.facsim.io.TextReader!.atEOF]]
*/
//.............................................................................

    @inline
    final def atEOF = lastChar == TextReader.EOF

//.............................................................................
/**
@see [[org.facsim.io.TextReader!.getRow]]
*/
//.............................................................................

    @inline
    final def getRow = row ensuring (_ > 0)

//.............................................................................
/**
@see [[org.facsim.io.TextReader!.getColumn]]
*/
//.............................................................................

    @inline
    final def getColumn = column ensuring (_ > 0)

//.............................................................................
/**
Cache the current state of this stream and return the new state.

@note The default text reader buffer size is used to record all data read from
the current file position.  If more data has been read from the stream than can
be buffered and an attempt to [[org.facsim.io.TextReader!.State!.reset()]] back
to the cached data is made, then an [[java.io.IOException!]] will result.

@todo Change the implementation to avoid using [[java.io.BufferedReader!]]'s
mark and reset operations, which enforce use of a fixed size buffer.

@return Cached state.

@since 0.0
*/
//.............................................................................

    final def cache () = {
      assert (reader.markSupported ())
      reader.mark (TextReader.BufferSize)
      new State (this)
    }

//.............................................................................
/**
Restore the specified prior state of this stream.

@note If more data was read from the stream than could be accommodated by the
buffer specified in the [[org.facsim.io.TextReader!.State!.cache()]] method,
then a [[java.io.IOException!]] will result from this call.

@todo Change the implementation to avoid using [[java.io.BufferedReader!]]'s
mark and reset operations, which enforce use of a fixed size buffer.

@param other Cached state to be copied.  This cached state must match the state
returned by the immediate prior call to `cache`.  If no prior call has been
made to `cache`, then a [[java.io.IOException!]] will result.

@throws java.io.IOException if the reader was not previously cached, or if more
data than could be stored in the reader's buffer was read since the cache
operation.

@since 0.0
*/
//.............................................................................

    final def reset (other: State): Unit = {
      lastChar = other.lastChar
      peekedChar = other.peekedChar
      row = other.row
      column = other.column
      reader.reset
    }

//.............................................................................
/**
Update the row and column number to the appropriate values after reading the
indicated character.

@param char Character that we're about to report as having been read.

@since 0.0
*/
//.............................................................................

    private final def updateRowColumn (char: Int): Unit = char match {

/*
If the character we just read is a line feed, then update the row and column of
the next character to be read.
*/

      case TextReader.LF => {
        row += 1
        column = 1
      }

/*
Otherwise, update the column number being read from.
*/

      case _ => column += 1
    }

//.............................................................................
/**
@see [[org.facsim.io.TextReader!.peek()]]
*/
//.............................................................................

    final def peek (): Int =  {
      peekedChar match {

/*
If we have already peeked at the next character, but we have yet to read it,
then return it.
*/

        case Some (char) => char

/*
Otherwise, we haven't yet peeked at the next character, so read and store it.
*/

        case None => {
          peekedChar = Option (readChar ())
          peekedChar.get
        }
      }
    } ensuring (_ >= -1)

//.............................................................................
/**
@see [[org.facsim.io.TextReader!.read()]]
*/
//.............................................................................

    final def read (): Int = {

/*
Helper function to determine whether we're reporting a previously peeked
character, or a fresh character.
*/

      def peekedOrRead () = peekedChar match {

/*
If we have a previously peeked character, then return it and clear the cached
peeked character.
*/

        case Some (char) => {
          peekedChar = None
          char
        }

/*
Otherwise, supply a fresh character from the stream.  We don't need to cache
anything because we're not peeking at it.
*/

        case None => readChar ()
      }

/*
Update the row and column number for the character we're returning.
*/

      val char = peekedOrRead ()
      updateRowColumn (char)
      char
    } ensuring (_ >= -1)

//.............................................................................
/**
Read the next character from the stream.

Line termination sequences will be consumed and reported as a single ''line
feed'' character to simplify stream  processing on all platforms.

@return The next character read from the stream or `EOF` if the end-of-file was
encountered during the read.

@throws java.io.EOFException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation.

@since 0.0
*/
//.............................................................................

    private final def readChar (): Int = {

/*
If the last character read indicated end-of-file, then throw the EOFException.
*/

      if (atEOF) throw new EOFException (LibResource ("io.TextReader.EOF", row,
      column))

/*
Read the next character from the stream.
*/

      reader.read match {

/*
Is this a carriage return character?  If so, report that we read a line feed
character.

Note: This function should NEVER return a carriage return.

Carriage returns should only be encountered in two situations:
1.  As the first character of a line termination sequence in a Windows, DOS or
    OS/2 text stream.  In this case, we deal with it by reporting it as a line
    feed.  When we subsequently read the accompanying line feed character
    (which will follow immediately afterwards, see next case statement), we
    simply discard it and read another character to replace it.
2.  As the sole character of a line termination sequence in a Mac OS 9 (or
    earlier) data stream.  In this case, we just replace the carriage return
    character with a line feed and we're done.

BUG: Things can get a little out of hand if a text stream has inconsistent line
termination sequences.  We don't check for that, right now, but it's not likely
to happen in practice.
*/

        case TextReader.CR => {
          lastChar = TextReader.CR
          TextReader.LF
        }

/*
Is this character a line feed character?  If so, was our last character a
carriage return?  If not, then just return the line feed, after storing it as
our new last character.  Otherwise, we have already reported that we read a
line feed, so skip this line feed character and return the next character.

Line feeds should only be encountered in two situations:
1.  As the second character of a line termination sequence in a Windows, DOS or
    OS/2 text stream.  In this case, we deal with it by ignoring it (we already
    reported a line feed when we read the carriage return).  However, since we
    still need to return a freshly-read character, read another character from
    the stream and return that.
2.  As the sole line termination sequence in a Unix (or Unix-like) data stream.
    In this case, we just return the line feed.
*/

        case TextReader.LF => {
          if (lastChar != TextReader.CR) {
            lastChar = TextReader.LF
            lastChar
          }
          else {
            lastChar = TextReader.LF
            readChar ()
          }
        }

/*
Otherwise, we have the simple task of reporting the character read - but make
sure we store it, so that we can correctly identify when we're attempting to
read beyond the end-of-file (see top of this function).
*/

        case char => {
          lastChar = char
          lastChar
        }
      }
    } ensuring (_ >= -1)
  }

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as the specified type.

Prior to reading from the stream, the current reader state&mdash;including its
current read position&mdash;will be cached.  If a data exception occurs (for
example, if the data was read but proved to have an invalid format or value),
then the stream's original state will be restored before that exception is
passed to the calling routine.  However, it is not possible to restore state
for some unrecoverable errors.

@param delimiter Delimiter to be used for this read operation.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param convertField Function to convert the field's value from a string to the
required type '''T'''.  If the field cannot be converted, a
[[org.facsim.io.FieldConversionException!]] is thrown.

@tparam T Data type that the field is to be converted to and returned as.

@return Next field read from the stream, as a value of type '''T'''.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldConversionException if the field's string data could
not be converted to '''T''' by the '''convertField''' function.

@throws org.facsim.io.FieldVerificationException if the field's value could not
be verified by the '''verify''' function.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def readField [T] (delimiter: Delimiter, verify:
  TextReader.Verifier [T])(convertField: (String) => T): T = {

/*
Tail-recursive helper function to build the field read.
*/

    def buildField (field: StringBuilder): StringBuilder = {

/*
If we've reached a field delimiter, then return what we have.
*/

      if (delimiter.reached (this)) field

/*
If we've reached the end-of-file condition, then read and discard it to ensure
that we throw the EOFException if we attempt a further read operation, and then
return what we have.  (If we don't read the EOF marker, we'll just return an
infinite set of empty fields on subsequent reads.)
*/

      else if (peek () == TextReader.EOF) {
        read ()
        field
      }

/*
Otherwise, append the next character to the field and return it.
*/

      else buildField (field.appendCodePoint (read ()))
    }

/*
Cache the current state of the stream in case we need to restore it later.
*/

    val cachedState = state.cache ()

/*
Retrieve the field from the helper function.  This may throw an IOException.
*/

    val field = buildField (new StringBuilder ()).toString

/*
Convert the field to the required type using the supplied function.

If this throws a NumberFormatException, then reset the state and convert it to
a FieldConversionException.
*/

    val fieldValue = try {
      convertField (field)
    }
    catch {
      case e: NumberFormatException => {
        state.reset (cachedState)
        throw new FieldConversionException (cachedState.getRow,
        cachedState.getColumn, field, convertField.getClass ())
      }
    }

/*
Verify the field using the supplied function.

If verification fails, then reset the state and throw a
FieldVerificationException.
*/

    if (!verify (fieldValue)) {
      state.reset (cachedState)
      throw new FieldVerificationException (cachedState.getRow,
      cachedState.getColumn, field)
    }
    else fieldValue
  }

//-----------------------------------------------------------------------------
/**
Read from the current file pointer to the next line termination sequence and
return it as a string.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@return Remainder of current line from the stream.  If there is no data in the
field, an empty string will be returned.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readToEOL (verify: TextReader.Verifier [String] =
  TextReader.defaultStringVerifier): String =
  readString (verify)(LineDelimiter)

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as a string.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param delimiter Set of characters and associated rules for delimiting fields.
If an implicit delimiter is in scope, it will be supplied implicitly by the
Scala compiler.

@return Next string field read from the stream.  If there is no data in the
field, an empty string will be returned.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readString (verify: TextReader.Verifier [String] =
  TextReader.defaultStringVerifier)(implicit delimiter: Delimiter =
  defaultDelimiter): String = readField [String] (delimiter, verify) {
    field =>
    field
  }

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as a byte.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param delimiter Set of characters and associated rules for delimiting fields.
If an implicit delimiter is in scope, it will be supplied implicitly by the
Scala compiler.

@return Next byte field read from the stream.  If there is no data in the
field, a [[org.facsim.io.FieldConversionException!]] will result.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldConversionException if this field's string value
could not be converted into a byte value.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readByte (verify: TextReader.Verifier [Byte] =
  TextReader.defaultByteVerifier)(implicit delimiter: Delimiter =
  defaultDelimiter): Byte = readField [Byte] (delimiter, verify)(_.toByte)

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as a short integer.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param delimiter Set of characters and associated rules for delimiting fields.
If an implicit delimiter is in scope, it will be supplied implicitly by the
Scala compiler.

@return Next short integer field read from the stream.  If there is no data in
the field, a [[org.facsim.io.FieldConversionException!]] will result.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldConversionException if this field's string value
could not be converted into a short integer value.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readShort (verify: TextReader.Verifier [Short] =
  TextReader.defaultShortVerifier)(implicit delimiter: Delimiter =
  defaultDelimiter): Short = readField [Short] (delimiter, verify)(_.toShort)

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as an integer.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param delimiter Set of characters and associated rules for delimiting fields.
If an implicit delimiter is in scope, it will be supplied implicitly by the
Scala compiler.

@return Next integer field read from the stream.  If there is no data in the
field, a [[org.facsim.io.FieldConversionException!]] will result.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldConversionException if this field's string value
could not be converted into an integer value.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readInt (verify: TextReader.Verifier [Int] =
  TextReader.defaultIntVerifier)(implicit delimiter: Delimiter =
  defaultDelimiter): Int = readField [Int] (delimiter, verify)(_.toInt)

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as a long integer.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param delimiter Set of characters and associated rules for delimiting fields.
If an implicit delimiter is in scope, it will be supplied implicitly by the
Scala compiler.

@return Next long integer field read from the stream.  If there is no data in
the field, a [[org.facsim.io.FieldConversionException!]] will result.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldConversionException if this field's string value
could not be converted into a long integer value.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readLong (verify: TextReader.Verifier [Long] =
  TextReader.defaultLongVerifier)(implicit delimiter: Delimiter =
  defaultDelimiter): Long = readField [Long] (delimiter, verify)(_.toLong)

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as a float.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param delimiter Set of characters and associated rules for delimiting fields.
If an implicit delimiter is in scope, it will be supplied implicitly by the
Scala compiler.

@return Next float field read from the stream.  If there is no data in the
field, a [[org.facsim.io.FieldConversionException!]] will result.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldConversionException if this field's string value
could not be converted into a float value.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readFloat (verify: TextReader.Verifier [Float] =
  TextReader.defaultFloatVerifier)(implicit delimiter: Delimiter =
  defaultDelimiter): Float = readField [Float] (delimiter, verify) {
    field =>

/*
The default toFloat method (actually java.lang.Float.parseFloat (String))
allows leading and trailing whitespace, when it should not.  To overcome this,
if the trimmed version of the string differs from the string supplied, we'll
throw a NumberFormatException.
*/

    if (field != field.trim) throw new
    NumberFormatException (LibResource ("io.TextReader.numberFormatException",
    field))
    else field.toFloat
  }

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as a double.

@param verify Function to verify the field's value before that value is
returned.  If this function returns `false`, a
[[org.facsim.io.FieldVerificationException!]] will be raised.

@param delimiter Set of characters and associated rules for delimiting fields.
If an implicit delimiter is in scope, it will be supplied implicitly by the
Scala compiler.

@return Next double field read from the stream.  If there is no data in the
field, a [[org.facsim.io.FieldConversionException!]] will result.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@throws org.facsim.io.FieldConversionException if this field's string value
could not be converted into a double value.

@throws org.facsim.io.FieldVerificationException if this field's data could not
be verified by '''verify'''.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readDouble (verify: TextReader.Verifier [Double] =
  TextReader.defaultDoubleVerifier)(implicit delimiter: Delimiter =
  defaultDelimiter): Double = readField [Double] (delimiter, verify) {
    field =>

/*
The default toDouble method (actually java.lang.Double.parseDouble (String))
allows leading and trailing whitespace, when it should not.  To overcome this,
if the trimmed version of the string differs from the string supplied, we'll
throw a NumberFormatException.
*/

    if (field != field.trim) throw new
    NumberFormatException (LibResource ("io.TextReader.numberFormatException",
    field))
    else field.toDouble
  }

//-----------------------------------------------------------------------------
/**
Determine if the ''end-of-file'' has been reached.

@return `true` if the end-of-file marker has been read from the stream,
`false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def atEOF = state.atEOF

//-----------------------------------------------------------------------------
/**
Report row number of next character to be read.

Row numbering starts at one and is incremented each time a line termination
sequence is read from the stream.

@note If this text reader is constructed from a reader that has already had
data read from it, then the row number reported is relative to the stream
location when this reader was created.

@return Current row number for the stream.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def getRow = state.getRow

//-----------------------------------------------------------------------------
/**
Report column number of next character to be read.

Column numbering starts at one and is incremented each time a regular
character (rather than a line termination sequence) is read from the stream; it
is reset to 1 whenever a line termination sequence is encountered.

@note If this text reader is constructed from a reader that has already had
data read from it, then the column number reported will be relative to the
stream location when this reader was created if a line termination sequence has
yet to be encountered.

@return Current row number for the stream.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  final def getColumn = state.getColumn

//-----------------------------------------------------------------------------
/**
Peek at the next character in the stream.

@return Character that will be returned by the next call to the read () method.
If the end-of-file will be reached on the next read, then a value of `EOF` (-1)
will be returned.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [io] final def peek (): Int = state.peek ()

//-----------------------------------------------------------------------------
/**
Read the next character from the stream.

@return Next character from the stream.  If the end-of-file was reached on this
read operation, then a value of `EOF` (-1) will be returned.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@since 0.0
*/
//-----------------------------------------------------------------------------

  @inline
  private [io] final def read (): Int = state.read ()
}

//=============================================================================
/**
Text reader companion object.

A number of character constants are defined to assist with processing text
files.

@since 0.0
*/
//=============================================================================

object TextReader  {

/**
Magic number, employed by the [[java.io.Reader!]] class hierarchy, that
signifies end-of-file when reading a single character.
*/

  val EOF = -1

/**
Magic number, storing Unicode value of the ''carriage return'' character.
*/

  val CR = '\r'.toInt

/**
Magic number, storing Unicode value of the ''horizontal tab'' character.
*/

  val HT = '\t'.toInt

/**
Magic number, storing Unicode value of the ''line feed'' character.
*/

  val LF = '\n'.toInt

/**
Magic number, storing Unicode value of the ''nul'' character.
*/

  val NUL = '\0'.toInt

/**
Magic number, storing Unicode value of the ''space'' character.
*/

  val SPC = ' '.toInt

/**
Size of the default buffer size to be used for readers and for use when marking
streams.
*/

  private val BufferSize = 32768

/**
Verification function.
*/

  type Verifier [T] = T => Boolean

/**
Default string verification function.

This verified verifies every string value.
*/

  val defaultStringVerifier: Verifier [String] = s => true

/**
Default byte verification function.

This verified verifies every byte value.
*/

  val defaultByteVerifier: Verifier [Byte] = i => true

/**
Default short verification function.

This verified verifies every short value.
*/

  val defaultShortVerifier: Verifier [Short] = i => true

/**
Default int verification function.

This verified verifies every int value.
*/

  val defaultIntVerifier: Verifier [Int] = i => true

/**
Default long verification function.

This verified verifies every long value.
*/

  val defaultLongVerifier: Verifier [Long] = i => true

/**
Default float verification function.

This verified verifies every float value.
*/

  val defaultFloatVerifier: Verifier [Float] = x => true

/**
Default double verification function.

This verified verifies every double value.
*/

  val defaultDoubleVerifier: Verifier [Double] = x => true
}