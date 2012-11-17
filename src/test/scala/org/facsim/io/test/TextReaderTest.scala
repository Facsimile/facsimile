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
Scala source file from the org.facsim.io.test package.
*/
//=============================================================================

package org.facsim.io.test

import java.io.EOFException
import java.io.StringReader
import scala.math.abs
import org.facsim.io.FieldConversionException
import org.facsim.io.FieldVerificationException
import org.facsim.io.Delimiter
import org.facsim.io.EOFDelimiter
import org.facsim.io.LineDelimiter
import org.facsim.io.TextReader
import org.facsim.io.WhitespaceDelimiter
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.io.Delimiter!]] class.
*/
//=============================================================================

class TextReaderTest extends FunSpec {

/**
Trait for empty data & reader.
*/

  trait EmptyDataReader {
    val emptyData = new StringReader ("")
    val emptyReader = new TextReader (emptyData)
  }

/*
Trait for line termination data & reader.
*/

  trait EOLDataReader {
    val pcEOLData = new StringReader ("\r\n\r\n\r\n")
    val pcEOLReader = new TextReader (pcEOLData)
    val unixEOLData = new StringReader ("\n\n\n")
    val unixEOLReader = new TextReader (unixEOLData)
    val oldMacOsEOLData = new StringReader ("\r\r\r")
    val oldMacOsEOLReader = new TextReader (oldMacOsEOLData)
  }

/**
Trait with test data.

The test data includes PC, Unix and old Mac style data files, together with
appropriate delimiters
*/

  trait TestData {
    val data = "a\t1\t1.1e1\t-1\t-1.1e-1\t 2\t3 \t 4 \t\t56fred"
    val emptyLine = ""
    val dataEnd = "end"
    val rawFileData = data + "\n" + data + "\n" + emptyLine + "\n" + dataEnd
    val pcData = new StringReader (data + "\r\n" + data + "\r\n" + emptyLine +
    "\r\n" + dataEnd)
    val unixData = new StringReader (rawFileData)
    val oldMacOsData = new StringReader (data + "\r" + data + "\r" + emptyLine
    + "\r" + dataEnd)
  }

/**
Trait for test data readers using default delimiter.
*/

  trait DefaultDelimitedReaders extends TestData {
    val pcReader = new TextReader (pcData)
    val unixReader = new TextReader (unixData)
    val oldMacOsReader = new TextReader (oldMacOsData)
  }

/**
Trait for test data readers using an EOF delimiter.
*/

  trait EOFDelimitedReaders extends TestData {
    val pcReader = new TextReader (pcData, EOFDelimiter)
    val unixReader = new TextReader (unixData, EOFDelimiter)
    val oldMacOsReader = new TextReader (oldMacOsData, EOFDelimiter)
  }

/**
Trait for test data readers using a line delimiter.
*/

  trait LineDelimitedReaders extends TestData {
    val pcReader = new TextReader (pcData, LineDelimiter)
    val unixReader = new TextReader (unixData, LineDelimiter)
    val oldMacOsReader = new TextReader (oldMacOsData, LineDelimiter)
  }

/**
Trait with test delimiter to parse data using single tabs (i.e. adjacent tabs
are not merged) which is used to test whether leading/trailing spaces, or empty
fields, affect field parsing.
*/

  trait TestDelimitedReaders extends TestData {
    val testDelimiter = new Delimiter (Set (TextReader.HT, TextReader.LF),
    false)
    val pcReader = new TextReader (pcData, testDelimiter)
    val unixReader = new TextReader (unixData, testDelimiter)
    val oldMacOsReader = new TextReader (oldMacOsData, testDelimiter)
  }

/*
Test fixture description.
*/

  describe (classOf [TextReader].getCanonicalName) {

/*
Primary constructor tests.

Firstly, test construction with default delimiter.
*/

    describe (".this (Reader)") {
      it ("must throw NullPointerException if passed null reader") {
        intercept [NullPointerException] {
          new TextReader (null)
        }
      }
      it ("must construct valid readers") {
        new DefaultDelimitedReaders {
        }
      }
    }

/*
Now with an explicit delimiter.
*/

    describe (".this (Reader, Delimiter)") {
      it ("must throw NullPointerException if passed null reader") {
        intercept [NullPointerException] {
          new TextReader (null)
        }
      }
      it ("must construct a valid reader") {
        new EOFDelimitedReaders {
        }
      }
    }

/*
Verify that reading characters works.
*/

    describe (".read ()") {

/*
Handle end-of-file conditions.

The first read() call must return an EOF signal, the second must throw the
EOFException.
*/

      it ("must handle EOF condition correctly") {
        new EmptyDataReader {
          assert (emptyReader.read () === TextReader.EOF)
          intercept [EOFException] {
            emptyReader.read ()
          }
        }
      }

/*
Handle end-of-line conditions.

All line termination sequences must be reported as line-feeds, regardless of
the actual line termination sequence.  After three reads, we must see an
end-of-file (to ensure that we're not re-reading data etc.
*/

      it ("must handle line termination sequences correctly") {
        new EOLDataReader {
          def readLines (reader: TextReader): Unit = {
            assert (pcEOLReader.read () === TextReader.LF)
            assert (pcEOLReader.read () === TextReader.LF)
            assert (pcEOLReader.read () === TextReader.LF)
            assert (pcEOLReader.read () === TextReader.EOF)
          }
          readLines (pcEOLReader)
          readLines (unixEOLReader)
          readLines (oldMacOsEOLReader)
        }
      }

/*
Verify that read () can process all of a file's data correctly, regardless of
format.
*/

      it ("must read data correctly") {
        new DefaultDelimitedReaders {
          def iterate (reader: TextReader, data: String): Unit = {
            val char = reader.read ()
            if (char == TextReader.EOF) {
              assert (char === data.head)
              iterate (reader, data.tail)
            }
          }
          iterate (pcReader, rawFileData)
          iterate (unixReader, rawFileData)
          iterate (oldMacOsReader, rawFileData)
        }
      }
    }

/*
Verify that peeking characters works.
*/

    describe (".peek ()") {

/*
Handle end-of-file conditions.

The first and second peek calls must return an EOF signal.  After that we read
the EOF signal.  If we peek a third time, we must see the exception.
*/

      it ("must handle EOF condition correctly") {
        new EmptyDataReader {
          assert (emptyReader.peek () === TextReader.EOF)
          assert (emptyReader.peek () === TextReader.EOF)
          assert (emptyReader.read () === TextReader.EOF)
          intercept [EOFException] {
            emptyReader.peek ()
          }
        }
      }

/*
Handle end-of-line conditions.

All line termination sequences must be reported as line-feeds, regardless of
the actual line termination sequence.  After three reads, we must see an
end-of-file (to ensure that we're not re-reading data etc.  We also peek each
character twice before reading it to ensure correct synchronization too.
*/

      it ("must handle line termination sequences correctly") {
        new EOLDataReader {
          def readLines (reader: TextReader): Unit = {
            assert (reader.peek () === TextReader.LF)
            assert (reader.peek () === TextReader.LF)
            assert (reader.read () === TextReader.LF)
            assert (reader.peek () === TextReader.LF)
            assert (reader.peek () === TextReader.LF)
            assert (reader.read () === TextReader.LF)
            assert (reader.peek () === TextReader.LF)
            assert (reader.peek () === TextReader.LF)
            assert (reader.read () === TextReader.LF)
            assert (reader.peek() === TextReader.EOF)
          }
          readLines (pcEOLReader)
          readLines (unixEOLReader)
          readLines (oldMacOsEOLReader)
        }
      }

/*
Verify that peek () can process all of a file's data correctly, regardless of
format.  We peek each character twice before reading it.
*/

      it ("must peek data correctly") {
        new DefaultDelimitedReaders {
          def iterate (reader: TextReader, data: String): Unit = {
            if (data.isEmpty) {
              assert (reader.peek () === TextReader.EOF)
              assert (reader.peek () === TextReader.EOF)
              assert (reader.read () === TextReader.EOF)
            }
            else {
              val char = data.head
              assert (reader.peek () === char)
              assert (reader.peek () === char)
              assert (reader.read () === char)
              iterate (reader, data.tail)
            }
          }
          iterate (pcReader, rawFileData)
          iterate (unixReader, rawFileData)
          iterate (oldMacOsReader, rawFileData)
        }
      }
    }

/*
Verify that row is reported correctly.
*/

    describe (".getRow") {
      it ("must report row correctly") {
        new DefaultDelimitedReaders {
          def doRead (reader: TextReader) {
            for (i <- 1 to data.length + 1) {
              assert (reader.getRow === 1)
              reader.read ()
            }
            for (i <- 1 to data.length + 1) {
              assert (reader.getRow === 2)
              reader.read ()
            }
            assert (reader.getRow == 3)
            reader.read ()
            for (i <- 1 to dataEnd.length + 1) {
              assert (reader.getRow === 4)
              reader.read ()
            }
          }
          doRead (pcReader)
          doRead (unixReader)
          doRead (oldMacOsReader)
        }
      }
    }

/*
Verify that column is reported correctly.
*/

    describe (".getColumn") {
      it ("must report collumn correctly") {
        new DefaultDelimitedReaders {
          def doRead (reader: TextReader) {
            for (i <- 1 to data.length + 1) {
              assert (reader.getColumn === i)
              reader.read ()
            }
            for (i <- 1 to data.length + 1) {
              assert (reader.getColumn === i)
              reader.read ()
            }
            assert (reader.getColumn == 1)
            reader.read ()
            for (i <- 1 to dataEnd.length + 1) {
              assert (reader.getColumn === i)
              reader.read ()
            }
          }
          doRead (pcReader)
          doRead (unixReader)
          doRead (oldMacOsReader)
        }
      }
    }

/*
Verify that we can tell when we've read the end-of-file marker.
*/

    describe (".atEOF") {

/*
Handle end-of-file conditions.

The first read() call must return an EOF signal, a second throws the
EOFException.  atEOF must return false prior to the EOF signal being read and
true afterwards.
*/

      it ("must handle EOF condition correctly") {
        new EmptyDataReader {
          assert (emptyReader.atEOF === false)
          assert (emptyReader.read () === TextReader.EOF)
          assert (emptyReader.atEOF === true)
          intercept [EOFException] {
            emptyReader.read ()
          }
          assert (emptyReader.atEOF === true)
        }
      }
    }

/*
Verify that the readString function operates correctly with the default
delimiter and default or specific verification functions.

Much of the code for retrieving string fields is shared with the other read
functions, so we can achieve most of our testing goals here.
*/

    describe (".readString (Verifier [String])") {

/*
The function must fail correctly when reading an invalid field.  That is, the
reader must be reset back to the beginning of the field read.
*/

      it ("must fail correctly") {
        new DefaultDelimitedReaders {
          def doFail (reader: TextReader) {
            def failField (row: Int, column: Int, field: String) {
              val e = intercept [FieldVerificationException] {
                reader.readString {s =>
                  assert (s === field)
                  false
                }
              }
              assert (e.getMessage () === "Field verification of value '" +
              field + "' failed. Row: " + row + ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.head.toInt)
              assert (reader.readString () === field)
            }
            failField (1, 1, "a")
            failField (1, 3, "1")
            failField (1, 5, "1.1e1")
            failField (1, 11, "-1")
            failField (1, 14, "-1.1e-1")
            failField (1, 23, "2")
            failField (1, 25, "3")
            failField (1, 29, "4")
            failField (1, 33, "56fred")
            failField (2, 1, "a")
          }
          doFail (pcReader)
          doFail (unixReader)
          doFail (oldMacOsReader)
        }
      }

/*
The function must work correctly when reading a valid field.  That is, the
reader must advance to the next field and the field value must be returned.
*/

      it ("must accept valid fields") {
        new DefaultDelimitedReaders {
          def doValid (reader: TextReader) {
            def acceptField (row: Int, column: Int, field: String) {
              reader.readString {s =>
                assert (s === field)
                true
              }
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
            }
            acceptField (1, 3, "a")
            acceptField (1, 5, "1")
            acceptField (1, 11, "1.1e1")
            acceptField (1, 14, "-1")
            acceptField (1, 23, "-1.1e-1")
            acceptField (1, 25, "2")
            acceptField (1, 29, "3")
            acceptField (1, 33, "4")
            acceptField (2, 1, "56fred")
            acceptField (2, 3, "a")
          }
          doValid (pcReader)
          doValid (unixReader)
          doValid (oldMacOsReader)
        }
      }

/*
If we pass the function an EOF delimiter, then the whole file must be returned
as a field, regardless of format.
*/

      it ("must retrieve the entire file if passed an EOFDelimiter") {
        new EOFDelimitedReaders {
          def readFile (reader: TextReader): Unit = {
            assert (reader.readString () === rawFileData)
            assert (reader.atEOF === true)
          }
          readFile (pcReader)
          readFile (unixReader)
          readFile (oldMacOsReader)
        }
      }

/*
If we pass a line delimiter, we must get individual lines of data.
*/

      it ("must retrieve individual lines if passed a LineDelimiter") {
        new LineDelimitedReaders {
          def readLines (reader: TextReader): Unit = {
            assert (reader.readString () === data)
            assert (reader.atEOF === false)
            assert (reader.readString () === data)
            assert (reader.atEOF === false)
            assert (reader.readString () === emptyLine)
            assert (reader.atEOF === false)
            assert (reader.readString () === dataEnd)
            assert (reader.atEOF === true)
          }
          readLines (pcReader)
          readLines (unixReader)
          readLines (oldMacOsReader)
        }
      }
    }

/*
Verify that the readString function operates correctly with a specific
delimiter.
*/

    describe (".readString (Delimiter)(Verifier [String])") {

/*
If we pass the function an EOF delimiter, then the whole file must be returned
as a field, regardless of format.
*/

      it ("must be able to override the default delimiter") {
        new LineDelimitedReaders {
          def readFile (reader: TextReader): Unit = {
            assert (reader.readString (EOFDelimiter)() === rawFileData)
            assert (reader.atEOF === true)
          }
          readFile (pcReader)
          readFile (unixReader)
          readFile (oldMacOsReader)
        }
      }
    }

/*
Verify that the readByte function operates correctly.
*/

    describe (".readByte (Verifier [Byte])") {

/*
The function must fail correctly when reading an invalid field.  That is, the
reader must be reset back to the beginning of the field read.

Here, we test both conversion and verification.
*/

      it ("must fail correctly") {
        new DefaultDelimitedReaders {
          def doFail (reader: TextReader) {
            def failFieldConversion (row: Int, column: Int, field: String) {
              val e = intercept [FieldConversionException] {
                reader.readByte ()
              }
              assert (e.getMessage () === "Field conversion of value '" + field
              + "' to type 'java.lang.Byte' failed. Row: " + row + ", Column: "
              + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.head.toInt)
              assert (reader.readString () === field)
            }
            def failFieldVerification (row: Int, column: Int, field: Byte) {
              val e = intercept [FieldVerificationException] {
                reader.readByte {i =>
                  assert (i === field)
                  false
                }
              }
              assert (e.getMessage () === "Field verification of value '" +
              field + "' failed. Row: " + row + ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.toString.head.toInt)
              assert (reader.readString () === field)
            }
            failFieldConversion (1, 1, "a")
            failFieldVerification (1, 3, 1.toByte)
            failFieldConversion (1, 5, "1.1e1")
            failFieldVerification (1, 11, -1.toByte)
            failFieldConversion (1, 14, "-1.1e-1")
            failFieldVerification (1, 23, 2.toByte)
            failFieldVerification (1, 25, 3.toByte)
            failFieldVerification (1, 29, 4.toByte)
            failFieldConversion (1, 33, "56fred")
            failFieldConversion (2, 1, "a")
          }
          doFail (pcReader)
          doFail (unixReader)
          doFail (oldMacOsReader)
        }
      }

/*
Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readBytes (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readByte () // "a"
            }
            assert (reader.readString () === "a")
            assert (reader.readByte () === 1)
            intercept [FieldConversionException] {
              reader.readByte () // "1.1e1"
            }
            assert (reader.readString () === "1.1e1")
            assert (reader.readByte () === -1)
            intercept [FieldConversionException] {
              reader.readByte () // "-1.1e-1"
            }
            assert (reader.readString () === "-1.1e-1")
            intercept [FieldConversionException] {
              reader.readByte () // " 2"
            }
            assert (reader.readString () === " 2")
            intercept [FieldConversionException] {
              reader.readByte () // "3 "
            }
            assert (reader.readString () === "3 ")
            intercept [FieldConversionException] {
              reader.readByte () // " 4 "
            }
            assert (reader.readString () === " 4 ")
            intercept [FieldConversionException] {
              reader.readByte () // ""
            }
            assert (reader.readString () === "")
            intercept [FieldConversionException] {
              reader.readByte () // "56fred"
            }
            assert (reader.readString () === "56fred")
          }
          readBytes (pcReader)
          readBytes (unixReader)
          readBytes (oldMacOsReader)
        }
      }
    }

/*
Now process the same data as above by overriding the default delimiter with the
whitespace delimiter that DOES merge adjacent delimiters.  This will remove
many of the exceptions (due to spaces being merged with tabs) and will remove
empty fields.
*/

    describe ("readByte (Delimiter)(Verifier [Byte])") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readBytes (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readByte (WhitespaceDelimiter)() // "a"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "a")
            assert (reader.readByte (WhitespaceDelimiter)() === 1)
            intercept [FieldConversionException] {
              reader.readByte (WhitespaceDelimiter)() // "1.1e1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "1.1e1")
            assert (reader.readByte (WhitespaceDelimiter)() === -1)
            intercept [FieldConversionException] {
              reader.readByte (WhitespaceDelimiter)() // "-1.1e-1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "-1.1e-1")
            assert (reader.readByte (WhitespaceDelimiter)() === 2)
            assert (reader.readByte (WhitespaceDelimiter)() === 3)
            assert (reader.readByte (WhitespaceDelimiter)() === 4)
            intercept [FieldConversionException] {
              reader.readByte (WhitespaceDelimiter)() // "56fred"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "56fred")
          }
          readBytes (pcReader)
          readBytes (unixReader)
          readBytes (oldMacOsReader)
        }
      }
    }

/*
Verify that the readShort function operates correctly.
*/

    describe (".readShort (Verifier [Short])") {

/*
The function must fail correctly when reading an invalid field.  That is, the
reader must be reset back to the beginning of the field read.

Here, we test both conversion and verification.
*/

      it ("must fail correctly") {
        new DefaultDelimitedReaders {
          def doFail (reader: TextReader) {
            def failFieldConversion (row: Int, column: Int, field: String) {
              val e = intercept [FieldConversionException] {
                reader.readShort ()
              }
              assert (e.getMessage () === "Field conversion of value '" + field
              + "' to type 'java.lang.Short' failed. Row: " + row +
              ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.head.toInt)
              assert (reader.readString () === field)
            }
            def failFieldVerification (row: Int, column: Int, field: Short) {
              val e = intercept [FieldVerificationException] {
                reader.readShort {i =>
                  assert (i === field)
                  false
                }
              }
              assert (e.getMessage () === "Field verification of value '" +
              field + "' failed. Row: " + row + ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.toString.head.toInt)
              assert (reader.readString () === field)
            }
            failFieldConversion (1, 1, "a")
            failFieldVerification (1, 3, 1.toShort)
            failFieldConversion (1, 5, "1.1e1")
            failFieldVerification (1, 11, -1.toShort)
            failFieldConversion (1, 14, "-1.1e-1")
            failFieldVerification (1, 23, 2.toShort)
            failFieldVerification (1, 25, 3.toShort)
            failFieldVerification (1, 29, 4.toShort)
            failFieldConversion (1, 33, "56fred")
            failFieldConversion (2, 1, "a")
          }
          doFail (pcReader)
          doFail (unixReader)
          doFail (oldMacOsReader)
        }
      }

/*
Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readShorts (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readShort () // "a"
            }
            assert (reader.readString () === "a")
            assert (reader.readShort () === 1)
            intercept [FieldConversionException] {
              reader.readShort () // "1.1e1"
            }
            assert (reader.readString () === "1.1e1")
            assert (reader.readShort () === -1)
            intercept [FieldConversionException] {
              reader.readShort () // "-1.1e-1"
            }
            assert (reader.readString () === "-1.1e-1")
            intercept [FieldConversionException] {
              reader.readShort () // " 2"
            }
            assert (reader.readString () === " 2")
            intercept [FieldConversionException] {
              reader.readShort () // "3 "
            }
            assert (reader.readString () === "3 ")
            intercept [FieldConversionException] {
              reader.readShort () // " 4 "
            }
            assert (reader.readString () === " 4 ")
            intercept [FieldConversionException] {
              reader.readShort () // ""
            }
            assert (reader.readString () === "")
            intercept [FieldConversionException] {
              reader.readShort () // "56fred"
            }
            assert (reader.readString () === "56fred")
          }
          readShorts (pcReader)
          readShorts (unixReader)
          readShorts (oldMacOsReader)
        }
      }
    }

/*
Now process the same data as above by overriding the default delimiter with the
whitespace delimiter that DOES merge adjacent delimiters.  This will remove
many of the exceptions (due to spaces being merged with tabs) and will remove
empty fields.
*/

    describe ("readShort (Delimiter)(Verifier [Short])") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readShorts (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readShort (WhitespaceDelimiter)() // "a"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "a")
            assert (reader.readShort (WhitespaceDelimiter)() === 1)
            intercept [FieldConversionException] {
              reader.readShort (WhitespaceDelimiter)() // "1.1e1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "1.1e1")
            assert (reader.readShort (WhitespaceDelimiter)() === -1)
            intercept [FieldConversionException] {
              reader.readShort (WhitespaceDelimiter)() // "-1.1e-1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "-1.1e-1")
            assert (reader.readShort (WhitespaceDelimiter)() === 2)
            assert (reader.readShort (WhitespaceDelimiter)() === 3)
            assert (reader.readShort (WhitespaceDelimiter)() === 4)
            intercept [FieldConversionException] {
              reader.readShort (WhitespaceDelimiter)() // "56fred"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "56fred")
          }
          readShorts (pcReader)
          readShorts (unixReader)
          readShorts (oldMacOsReader)
        }
      }
    }

/*
Verify that the readInt function operates correctly.
*/

    describe (".readInt (Verifier [Int])") {

/*
The function must fail correctly when reading an invalid field.  That is, the
reader must be reset back to the beginning of the field read.

Here, we test both conversion and verification.
*/

      it ("must fail correctly") {
        new DefaultDelimitedReaders {
          def doFail (reader: TextReader) {
            def failFieldConversion (row: Int, column: Int, field: String) {
              val e = intercept [FieldConversionException] {
                reader.readInt ()
              }
              assert (e.getMessage () === "Field conversion of value '" + field
              + "' to type 'java.lang.Int' failed. Row: " + row + ", Column: "
              + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.head.toInt)
              assert (reader.readString () === field)
            }
            def failFieldVerification (row: Int, column: Int, field: Int) {
              val e = intercept [FieldVerificationException] {
                reader.readInt {i =>
                  assert (i === field)
                  false
                }
              }
              assert (e.getMessage () === "Field verification of value '" +
              field + "' failed. Row: " + row + ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.toString.head.toInt)
              assert (reader.readString () === field)
            }
            failFieldConversion (1, 1, "a")
            failFieldVerification (1, 3, 1.toInt)
            failFieldConversion (1, 5, "1.1e1")
            failFieldVerification (1, 11, -1.toInt)
            failFieldConversion (1, 14, "-1.1e-1")
            failFieldVerification (1, 23, 2.toInt)
            failFieldVerification (1, 25, 3.toInt)
            failFieldVerification (1, 29, 4.toInt)
            failFieldConversion (1, 33, "56fred")
            failFieldConversion (2, 1, "a")
          }
          doFail (pcReader)
          doFail (unixReader)
          doFail (oldMacOsReader)
        }
      }

/*
Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readInts (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readInt () // "a"
            }
            assert (reader.readString () === "a")
            assert (reader.readInt () === 1)
            intercept [FieldConversionException] {
              reader.readInt () // "1.1e1"
            }
            assert (reader.readString () === "1.1e1")
            assert (reader.readInt () === -1)
            intercept [FieldConversionException] {
              reader.readInt () // "-1.1e-1"
            }
            assert (reader.readString () === "-1.1e-1")
            intercept [FieldConversionException] {
              reader.readInt () // " 2"
            }
            assert (reader.readString () === " 2")
            intercept [FieldConversionException] {
              reader.readInt () // "3 "
            }
            assert (reader.readString () === "3 ")
            intercept [FieldConversionException] {
              reader.readInt () // " 4 "
            }
            assert (reader.readString () === " 4 ")
            intercept [FieldConversionException] {
              reader.readInt () // ""
            }
            assert (reader.readString () === "")
            intercept [FieldConversionException] {
              reader.readInt () // "56fred"
            }
            assert (reader.readString () === "56fred")
          }
          readInts (pcReader)
          readInts (unixReader)
          readInts (oldMacOsReader)
        }
      }
    }

/*
Now process the same data as above by overriding the default delimiter with the
whitespace delimiter that DOES merge adjacent delimiters.  This will remove
many of the exceptions (due to spaces being merged with tabs) and will remove
empty fields.
*/

    describe ("readInt (Delimiter)(Verifier [Int])") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readInts (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readInt (WhitespaceDelimiter)() // "a"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "a")
            assert (reader.readInt (WhitespaceDelimiter)() === 1)
            intercept [FieldConversionException] {
              reader.readInt (WhitespaceDelimiter)() // "1.1e1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "1.1e1")
            assert (reader.readInt (WhitespaceDelimiter)() === -1)
            intercept [FieldConversionException] {
              reader.readInt (WhitespaceDelimiter)() // "-1.1e-1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "-1.1e-1")
            assert (reader.readInt (WhitespaceDelimiter)() === 2)
            assert (reader.readInt (WhitespaceDelimiter)() === 3)
            assert (reader.readInt (WhitespaceDelimiter)() === 4)
            intercept [FieldConversionException] {
              reader.readInt (WhitespaceDelimiter)() // "56fred"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "56fred")
          }
          readInts (pcReader)
          readInts (unixReader)
          readInts (oldMacOsReader)
        }
      }
    }

/*
Verify that the readLong function operates correctly.
*/

    describe (".readLong (Verifier [Long])") {

/*
The function must fail correctly when reading an invalid field.  That is, the
reader must be reset back to the beginning of the field read.

Here, we test both conversion and verification.
*/

      it ("must fail correctly") {
        new DefaultDelimitedReaders {
          def doFail (reader: TextReader) {
            def failFieldConversion (row: Int, column: Int, field: String) {
              val e = intercept [FieldConversionException] {
                reader.readLong ()
              }
              assert (e.getMessage () === "Field conversion of value '" + field
              + "' to type 'java.lang.Long' failed. Row: " + row + ", Column: "
              + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.head.toInt)
              assert (reader.readString () === field)
            }
            def failFieldVerification (row: Int, column: Int, field: Long) {
              val e = intercept [FieldVerificationException] {
                reader.readLong {i =>
                  assert (i === field)
                  false
                }
              }
              assert (e.getMessage () === "Field verification of value '" +
              field + "' failed. Row: " + row + ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.toString.head.toInt)
              assert (reader.readString () === field)
            }
            failFieldConversion (1, 1, "a")
            failFieldVerification (1, 3, 1.toLong)
            failFieldConversion (1, 5, "1.1e1")
            failFieldVerification (1, 11, -1.toLong)
            failFieldConversion (1, 14, "-1.1e-1")
            failFieldVerification (1, 23, 2.toLong)
            failFieldVerification (1, 25, 3.toLong)
            failFieldVerification (1, 29, 4.toLong)
            failFieldConversion (1, 33, "56fred")
            failFieldConversion (2, 1, "a")
          }
          doFail (pcReader)
          doFail (unixReader)
          doFail (oldMacOsReader)
        }
      }

/*
Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readLongs (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readLong () // "a"
            }
            assert (reader.readString () === "a")
            assert (reader.readLong () === 1)
            intercept [FieldConversionException] {
              reader.readLong () // "1.1e1"
            }
            assert (reader.readString () === "1.1e1")
            assert (reader.readLong () === -1)
            intercept [FieldConversionException] {
              reader.readLong () // "-1.1e-1"
            }
            assert (reader.readString () === "-1.1e-1")
            intercept [FieldConversionException] {
              reader.readLong () // " 2"
            }
            assert (reader.readString () === " 2")
            intercept [FieldConversionException] {
              reader.readLong () // "3 "
            }
            assert (reader.readString () === "3 ")
            intercept [FieldConversionException] {
              reader.readLong () // " 4 "
            }
            assert (reader.readString () === " 4 ")
            intercept [FieldConversionException] {
              reader.readLong () // ""
            }
            assert (reader.readString () === "")
            intercept [FieldConversionException] {
              reader.readLong () // "56fred"
            }
            assert (reader.readString () === "56fred")
          }
          readLongs (pcReader)
          readLongs (unixReader)
          readLongs (oldMacOsReader)
        }
      }
    }

/*
Now process the same data as above by overriding the default delimiter with the
whitespace delimiter that DOES merge adjacent delimiters.  This will remove
many of the exceptions (due to spaces being merged with tabs) and will remove
empty fields.
*/

    describe ("readLong (Delimiter)(Verifier [Long])") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readLongs (reader: TextReader) {
            intercept [FieldConversionException] {
              reader.readLong (WhitespaceDelimiter)() // "a"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "a")
            assert (reader.readLong (WhitespaceDelimiter)() === 1)
            intercept [FieldConversionException] {
              reader.readLong (WhitespaceDelimiter)() // "1.1e1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "1.1e1")
            assert (reader.readLong (WhitespaceDelimiter)() === -1)
            intercept [FieldConversionException] {
              reader.readLong (WhitespaceDelimiter)() // "-1.1e-1"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "-1.1e-1")
            assert (reader.readLong (WhitespaceDelimiter)() === 2)
            assert (reader.readLong (WhitespaceDelimiter)() === 3)
            assert (reader.readLong (WhitespaceDelimiter)() === 4)
            intercept [FieldConversionException] {
              reader.readLong (WhitespaceDelimiter)() // "56fred"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "56fred")
          }
          readLongs (pcReader)
          readLongs (unixReader)
          readLongs (oldMacOsReader)
        }
      }
    }

/*
Verify that the readFloat function operates correctly.
*/

    describe (".readFloat (Verifier [Float])") {

/*
The function must fail correctly when reading an invalid field.  That is, the
reader must be reset back to the beginning of the field read.

Here, we test both conversion and verification.
*/

      it ("must fail correctly") {
        new DefaultDelimitedReaders {
          def doFail (reader: TextReader) {
            def failFieldConversion (row: Int, column: Int, field: String) {
              val e = intercept [FieldConversionException] {
                reader.readFloat ()
              }
              assert (e.getMessage () === "Field conversion of value '" + field
              + "' to type 'java.lang.Float' failed. Row: " + row +
              ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.head.toInt)
              assert (reader.readString () === field)
            }
            def failFieldVerification (row: Int, column: Int, field: Float) {
              val e = intercept [FieldVerificationException] {
                reader.readFloat {i =>
                  assert (i === field)
                  false
                }
              }
              assert (e.getMessage () === "Field verification of value '" +
              field + "' failed. Row: " + row + ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.toString.head.toInt)
              assert (reader.readString () === field)
            }
            failFieldConversion (1, 1, "a")
            failFieldVerification (1, 3, 1.toFloat)
            failFieldVerification (1, 5, 1.1e1.toFloat)
            failFieldVerification (1, 11, -1.toFloat)
            failFieldVerification (1, 14, -1.1e-1.toFloat)
            failFieldVerification (1, 23, 2.toFloat)
            failFieldVerification (1, 25, 3.toFloat)
            failFieldVerification (1, 29, 4.toFloat)
            failFieldConversion (1, 33, "56fred")
            failFieldConversion (2, 1, "a")
          }
          doFail (pcReader)
          doFail (unixReader)
          doFail (oldMacOsReader)
        }
      }

/*
Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readFloats (reader: TextReader) {
            val tolerance = 1.0e-8
            intercept [FieldConversionException] {
              reader.readFloat () // "a"
            }
            assert (reader.readString () === "a")
            assert (abs (reader.readFloat () - 1.0) < tolerance)
            assert (abs (reader.readFloat () - 1.1e1) < tolerance)
            assert (abs (reader.readFloat () - -1.0) < tolerance)
            assert (abs (reader.readFloat () - -1.1e-1) < tolerance)
            intercept [FieldConversionException] {
              reader.readFloat () // " 2"
            }
            assert (reader.readString () === " 2")
            intercept [FieldConversionException] {
              reader.readFloat () // "3 "
            }
            assert (reader.readString () === "3 ")
            intercept [FieldConversionException] {
              reader.readFloat () // " 4 "
            }
            assert (reader.readString () === " 4 ")
            intercept [FieldConversionException] {
              reader.readFloat () // ""
            }
            assert (reader.readString () === "")
            intercept [FieldConversionException] {
              reader.readFloat () // "56fred"
            }
            assert (reader.readString () === "56fred")
          }
          readFloats (pcReader)
          readFloats (unixReader)
          readFloats (oldMacOsReader)
        }
      }
    }

/*
Now process the same data as above by overriding the default delimiter with the
whitespace delimiter that DOES merge adjacent delimiters.  This will remove
many of the exceptions (due to spaces being merged with tabs) and will remove
empty fields.
*/

    describe ("readFloat (Delimiter)(Verifier [Float])") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readFloats (reader: TextReader) {
            val tolerance = 1.0e-8
            intercept [FieldConversionException] {
              reader.readFloat (WhitespaceDelimiter)() // "a"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "a")
            assert (abs (reader.readFloat (WhitespaceDelimiter)() - 1.0) <
            tolerance)
            assert (abs (reader.readFloat (WhitespaceDelimiter)() - 1.1e1) <
            tolerance)
            assert (abs (reader.readFloat (WhitespaceDelimiter)() - -1.0) <
            tolerance)
            assert (abs (reader.readFloat (WhitespaceDelimiter)() - -1.1e-1) <
            tolerance)
            assert (abs (reader.readFloat (WhitespaceDelimiter)() - 2.0) <
            tolerance)
            assert (abs (reader.readFloat (WhitespaceDelimiter)() - 3.0) <
            tolerance)
            assert (abs (reader.readFloat (WhitespaceDelimiter)() - 4.0) <
            tolerance)
            intercept [FieldConversionException] {
              reader.readFloat (WhitespaceDelimiter)() // "56fred"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "56fred")
          }
          readFloats (pcReader)
          readFloats (unixReader)
          readFloats (oldMacOsReader)
        }
      }
    }

/*
Verify that the readDouble function operates correctly.
*/

    describe (".readDouble (Verifier [Double])") {

/*
The function must fail correctly when reading an invalid field.  That is, the
reader must be reset back to the beginning of the field read.

Here, we test both conversion and verification.
*/

      it ("must fail correctly") {
        new DefaultDelimitedReaders {
          def doFail (reader: TextReader) {
            def failFieldConversion (row: Int, column: Int, field: String) {
              val e = intercept [FieldConversionException] {
                reader.readDouble ()
              }
              assert (e.getMessage () === "Field conversion of value '" + field
              + "' to type 'java.lang.Double' failed. Row: " + row +
              ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.head.toInt)
              assert (reader.readString () === field)
            }
            def failFieldVerification (row: Int, column: Int, field: Double) {
              val e = intercept [FieldVerificationException] {
                reader.readDouble {i =>
                  assert (i === field)
                  false
                }
              }
              assert (e.getMessage () === "Field verification of value '" +
              field + "' failed. Row: " + row + ", Column: " + column + ".")
              assert (reader.getRow === row)
              assert (reader.getColumn === column)
              assert (reader.peek () === field.toString.head.toInt)
              assert (reader.readString () === field)
            }
            failFieldConversion (1, 1, "a")
            failFieldVerification (1, 3, 1.toDouble)
            failFieldVerification (1, 5, 1.1e1.toDouble)
            failFieldVerification (1, 11, -1.toDouble)
            failFieldVerification (1, 14, -1.1e-1.toDouble)
            failFieldVerification (1, 23, 2.toDouble)
            failFieldVerification (1, 25, 3.toDouble)
            failFieldVerification (1, 29, 4.toDouble)
            failFieldConversion (1, 33, "56fred")
            failFieldConversion (2, 1, "a")
          }
          doFail (pcReader)
          doFail (unixReader)
          doFail (oldMacOsReader)
        }
      }

/*
Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readDoubles (reader: TextReader) {
            val tolerance = 1.0e-14
            intercept [FieldConversionException] {
              reader.readDouble () // "a"
            }
            assert (reader.readString () === "a")
            assert (abs (reader.readDouble () - 1.0) < tolerance)
            assert (abs (reader.readDouble () - 1.1e1) < tolerance)
            assert (abs (reader.readDouble () - -1.0) < tolerance)
            assert (abs (reader.readDouble () - -1.1e-1) < tolerance)
            intercept [FieldConversionException] {
              reader.readDouble () // " 2"
            }
            assert (reader.readString () === " 2")
            intercept [FieldConversionException] {
              reader.readDouble () // "3 "
            }
            assert (reader.readString () === "3 ")
            intercept [FieldConversionException] {
              reader.readDouble () // " 4 "
            }
            assert (reader.readString () === " 4 ")
            intercept [FieldConversionException] {
              reader.readDouble () // ""
            }
            assert (reader.readString () === "")
            intercept [FieldConversionException] {
              reader.readDouble () // "56fred"
            }
            assert (reader.readString () === "56fred")
          }
          readDoubles (pcReader)
          readDoubles (unixReader)
          readDoubles (oldMacOsReader)
        }
      }
    }

/*
Now process the same data as above by overriding the default delimiter with the
whitespace delimiter that DOES merge adjacent delimiters.  This will remove
many of the exceptions (due to spaces being merged with tabs) and will remove
empty fields.
*/

    describe ("readDouble (Delimiter)(Verifier [Double])") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readDoubles (reader: TextReader) {
            val tolerance = 1.0e-14
            intercept [FieldConversionException] {
              reader.readDouble (WhitespaceDelimiter)() // "a"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "a")
            assert (abs (reader.readDouble (WhitespaceDelimiter)() - 1.0) <
            tolerance)
            assert (abs (reader.readDouble (WhitespaceDelimiter)() - 1.1e1) <
            tolerance)
            assert (abs (reader.readDouble (WhitespaceDelimiter)() - -1.0) <
            tolerance)
            assert (abs (reader.readDouble (WhitespaceDelimiter)() - -1.1e-1) <
            tolerance)
            assert (abs (reader.readDouble (WhitespaceDelimiter)() - 2.0) <
            tolerance)
            assert (abs (reader.readDouble (WhitespaceDelimiter)() - 3.0) <
            tolerance)
            assert (abs (reader.readDouble (WhitespaceDelimiter)() - 4.0) <
            tolerance)
            intercept [FieldConversionException] {
              reader.readDouble (WhitespaceDelimiter)() // "56fred"
            }
            assert (reader.readString (WhitespaceDelimiter)() === "56fred")
          }
          readDoubles (pcReader)
          readDoubles (unixReader)
          readDoubles (oldMacOsReader)
        }
      }
    }
  }
}