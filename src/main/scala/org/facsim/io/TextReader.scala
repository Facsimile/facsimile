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
$Id$

Scala source file from the org.facsim.io package.
*/
//=============================================================================

package org.facsim.io

import java.io.EOFException
import java.io.Reader
import java.lang.StringBuilder

//=============================================================================
/**
A reader for text data streams.

This class simplifies the parsing of text streams by reading the next field
(delimited as specified) from the stream and returning it as the required data
type (or throwing an exception if the field cannot be converted successfully).

Furthermore, this reader harmonizes line termination sequences, so that all
lines appear to terminate with a ''line feed'' character
('\n')&mdash;regardless of the actual line termination sequence.  (Typically,
''Windows'', ''DOS'' and ''OS/2'' text streams terminate lines with a
''carriage return'' character followed by a ''line feed'' character ("\r\n");
''Unix'' and ''Unix''-like systems, such as ''Linux'', ''Mac OS'' (versions X
and higher) and the ''BSD'' variants, terminate with a single ''line feed''
character ('\n'); older Mac OS systems (9 and earlier) terminate with a single
''carriage return'' character ('\r').  This reader, and its sub-classes, allow
all text streams to be treated as though they come from a ''Unix''-like system.

@todo Implement support for comments (ignore remainder of line, whole of line).

@todo Implement support for quoted fields (such as those in CSV files).

@constructor Constructs a new reader from a reader sequence.

@param reader Reader from which data is to be read.

@param defaultDelimiter Default set of characters and associated rules for
delimiting fields.  This delimiter is used if an explicit delimiter is not
passed to each field read operation.

@throws java.lang.NullPointerException if reader is null.

@since 0.0
*/
//=============================================================================

class TextReader (reader: Reader, defaultDelimiter: Delimiter =
WhitespaceDelimiter) extends NotNull {

/*
Verify that reader is not NULL.
*/

  if (reader == null) throw new NullPointerException ()

/**
Variable storing the last peeked character read from the reader.
*/

  private var peekedChar: Option [Int] = None

/**
Row from which we're currently reading data.

By convention, line numbering begins at 1 and is incremented each time a line
termination sequence is read.

Line numbering is not to be updated until a line termination sequence has been
read (rather than being peeked).
*/

  private var row = 1

/**
Row at which last field read started.
*/

  private var fieldRow = 0

/**
Column at which last field read started.
*/

  private var fieldColumn = 0

/**
Column from which the next character will be read.

Ideally, column numbering counts whole Unicode characters, not simply bytes or
partial characters (as can be the case in ''UTF-8'' and ''UTF-16'' formats).

By convention, column numbering beings at 1 and is incremented each time a
character is read.  The column number must be reset to 1 each time a line
termination sequence has been read&mdash;the same point at which the line
number is incremented.

Column numbering is not to be updated until a character has been read (rather
than being peeked).
*/

  private var column = 1

/**
Last character read from the stream.

This records the last character actually read from the stream, and is used in
processing line termination sequences.  The initial value of -2 guarantees that
no match will be made with any value returned by [[java.io.Reader.read()]].
*/

  private var lastChar = -2

//-----------------------------------------------------------------------------
/**
Determine if the end-of-file has been reached.

@return `true` if the end-of-file marker has been read from the stream,
`false` otherwise.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def atEOF = lastChar == TextReader.EOF

//-----------------------------------------------------------------------------
/**
Read the next field from the stream and return it as a string.

@param delimiter Delimiter to be used for this read operation.  If omitted,
this will default to the delimiter specified when the reader was constructed.

@return Next field read from the stream.  If there is no data in the field, an
empty string will be returned.

@throws java.io.EOFException if an attempt is made to read a field after an
end-of-file condition has been signaled by a previous read operation.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readAsString (delimiter: Delimiter = defaultDelimiter): String = {

/*
Tail-recursive helper function to build the field read.
*/

    def buildField (field: StringBuilder): StringBuilder = {

/*
If we've reached a field delimiter, then return what we have.
*/

      if (delimiter.reached (this)) field

/*
If we've reached the end-of-file condition, then read it to ensure that we
throw the EOFException if we attempt a further read operation, and then return
what we have.  (If we don't read the EOF marker, we'll just return an infinite
set of empty fields on subsequent reads.)
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
Cache the current row and column position in case there's an error parsing the
field.
*/

    fieldRow = row
    fieldColumn = column

/*
Starting with an empty field, build the field and return it.
*/

    buildField (new StringBuilder ()).toString
  }

//-----------------------------------------------------------------------------
/**
Read the field from the stream and return its value as a byte.

@param delimiter Delimiter to be used for this read operation.  If omitted,
this will default to the delimiter specified when the reader was constructed.

@return Next byte field read from the stream.  If the field does not contain a
valid byte value, an exception is thrown.

@throws java.io.EOFException if an attempt is made to read a field after an
end-of-file condition has been signaled by a previous read operation.

@throws java.lang.NumberFormatException if the field's value cannot be
converted.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readAsByte (delimiter: Delimiter = defaultDelimiter) =
  readAsString (delimiter).toByte

//-----------------------------------------------------------------------------
/**
Read the field from the stream and return its value as a short integer.

@param delimiter Delimiter to be used for this read operation.  If omitted,
this will default to the delimiter specified when the reader was constructed.

@return Next short integer field read from the stream.  If the field does not
contain a valid short value, an exception is thrown.

@throws java.io.EOFException if an attempt is made to read a field after an
end-of-file condition has been signaled by a previous read operation.

@throws java.lang.NumberFormatException if the field's value cannot be
converted.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readAsShort (delimiter: Delimiter = defaultDelimiter) =
  readAsString (delimiter).toShort

//-----------------------------------------------------------------------------
/**
Read the field from the stream and return its value as an integer.

@param delimiter Delimiter to be used for this read operation.  If omitted,
this will default to the delimiter specified when the reader was constructed.

@return Next integer field read from the stream.  If the field does not contain
a valid integer value, an exception is thrown.

@throws java.io.EOFException if an attempt is made to read a field after an
end-of-file condition has been signaled by a previous read operation.

@throws java.lang.NumberFormatException if the field's value cannot be
converted.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readAsInt (delimiter: Delimiter = defaultDelimiter) =
  readAsString (delimiter).toInt

//-----------------------------------------------------------------------------
/**
Read the field from the stream and return its value as a long integer.

@param delimiter Delimiter to be used for this read operation.  If omitted,
this will default to the delimiter specified when the reader was constructed.

@return Next long integer field read from the stream.  If the field does not
contain a valid long value, an exception is thrown.

@throws java.io.EOFException if an attempt is made to read a field after an
end-of-file condition has been signaled by a previous read operation.

@throws java.lang.NumberFormatException if the field's value cannot be
converted.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readAsLong (delimiter: Delimiter = defaultDelimiter) =
  readAsString (delimiter).toLong

//-----------------------------------------------------------------------------
/**
Read the field from the stream and return its value as a float.

@param delimiter Delimiter to be used for this read operation.  If omitted,
this will default to the delimiter specified when the reader was constructed.

@return Next float field read from the stream.  If the field does not contain a
valid float, an exception is thrown.

@throws java.io.EOFException if an attempt is made to read a field after an
end-of-file condition has been signaled by a previous read operation.

@throws java.lang.NumberFormatException if the field's value cannot be
converted.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readAsFloat (delimiter: Delimiter = defaultDelimiter) = {

/*
The default toFloat method (actually java.lang.Float.parseFloat (String))
allows leading and trailing whitespace, when it should not.  To overcome this,
if the trimmed version of the string differs from the string, we'll throw a
NumberFormatException.
*/

    val value = readAsString (delimiter)
    if (value != value.trim) throw new NumberFormatException ("For input " +
    "string: \"" + value + "\"")
    value.toFloat
  }    

//-----------------------------------------------------------------------------
/**
Read the field from the stream and return its value as a double.

@param delimiter Delimiter to be used for this read operation.  If omitted,
this will default to the delimiter specified when the reader was constructed.

@return Next double field read from the stream.  If the field does not contain
a valid double, an exception is thrown.

@throws java.io.EOFException if an attempt is made to read a field after an
end-of-file condition has been signaled by a previous read operation.

@throws java.lang.NumberFormatException if the field's value cannot be
converted.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def readAsDouble (delimiter: Delimiter = defaultDelimiter) = {

/*
The default toDouble method (actually java.lang.Double.parseDouble (String))
allows leading and trailing whitespace, when it should not.  To overcome this,
if the trimmed version of the string differs from the string, we'll throw a
NumberFormatException.
*/

    val value = readAsString (delimiter)
    if (value != value.trim) throw new NumberFormatException ("For input " +
    "string: \"" + value + "\"")
    value.toDouble
  }

//-----------------------------------------------------------------------------
/**
Peek at the next character in the stream.

@return Character that will be returned by the next call to the read () method.
If the end-of-file will be reached on the next read, then a value of `EOF` (-1)
will be returned.

@throws java.io.EOFException if an attempt is made to peek a character after an
end-of-file condition has been signaled by a previous read operation.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [io] final def peek (): Int = peekedChar match {

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

//-----------------------------------------------------------------------------
/**
Read the next character from the stream.

@return Next character from the stream.  If the end-of-file was reached on this
read operation, then a value of `EOF` (-1) will be returned.

@throws java.io.EOFException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [io] final def read (): Int = {

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

    updateRowColumn (peekedOrRead ())
  }

//-----------------------------------------------------------------------------
/**
Update the row and column number to the appropriate values after reading the
indicated character.

@param char Character that we're about to report as having been read.

@return Character read.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def updateRowColumn (char: Int): Int = {
    char match {

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

/*
Report the character read.
*/

     char
  }

//-----------------------------------------------------------------------------
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
//-----------------------------------------------------------------------------

  private final def readChar (): Int = {

/*
If the last character read indicated end-of-file, then throw the EOFException.
*/

    if (atEOF) throw new EOFException

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
  }
}

//=============================================================================
/**
Text reader companion object.

A number of character constants are defined to assist with processing text
files.

@since 0.0
*/
//=============================================================================

private [io] object TextReader  {

/**
Magic number, employed by the [[java.io.Reader]] class hierarchy, that
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
Magic number, storing Unciode value of the ''space'' character.
*/

  val SPC = ' '.toInt
}