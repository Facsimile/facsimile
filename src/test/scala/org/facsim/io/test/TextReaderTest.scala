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

Scala source file from the org.facsim.io.test package.
*/
//=============================================================================

package org.facsim.io.test

import java.io.EOFException
import java.io.StringReader
import scala.math.abs
import org.facsim.io.Delimiter
import org.facsim.io.EOFDelimiter
import org.facsim.io.LineDelimiter
import org.facsim.io.TextReader
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.io.Delimiter]] class.
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

  describe (classOf [TextReader].getCanonicalName ()) {

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

The first read() call should return an EOF signal, the second should throw the
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

All line termination sequences should be reported as line-feeds, regardless of
the actual line termination sequence.  After three reads, we should see an
end-of-file (to ensure that we're not re-reading data etc.
*/

      it ("must handle PC line termination sequences correctly") {
        new EOLDataReader {
          assert (pcEOLReader.read () === TextReader.LF)
          assert (pcEOLReader.read () === TextReader.LF)
          assert (pcEOLReader.read () === TextReader.LF)
          assert (pcEOLReader.read () === TextReader.EOF)
        }
      }
      it ("must handle Unix line termination sequences correctly") {
        new EOLDataReader {
          assert (unixEOLReader.read () === TextReader.LF)
          assert (unixEOLReader.read () === TextReader.LF)
          assert (unixEOLReader.read () === TextReader.LF)
          assert (unixEOLReader.read () === TextReader.EOF)
        }
      }
      it ("must handle old Mac OS line termination sequences correctly") {
        new EOLDataReader {
          assert (oldMacOsEOLReader.read () === TextReader.LF)
          assert (oldMacOsEOLReader.read () === TextReader.LF)
          assert (oldMacOsEOLReader.read () === TextReader.LF)
          assert (oldMacOsEOLReader.read () === TextReader.EOF)
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

The first and second peek calls should return an EOF signal.  After that we
read the EOF signal.  If we peek a third time, we should see the exception.
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

All line termination sequences should be reported as line-feeds, regardless of
the actual line termination sequence.  After three reads, we should see an
end-of-file (to ensure that we're not re-reading data etc.  We also peek each
character twice before reading it to ensure correct synchronization too.
*/

      it ("must handle PC line termination sequences correctly") {
        new EOLDataReader {
          assert (pcEOLReader.peek () === TextReader.LF)
          assert (pcEOLReader.peek () === TextReader.LF)
          assert (pcEOLReader.read () === TextReader.LF)
          assert (pcEOLReader.peek () === TextReader.LF)
          assert (pcEOLReader.peek () === TextReader.LF)
          assert (pcEOLReader.read () === TextReader.LF)
          assert (pcEOLReader.peek () === TextReader.LF)
          assert (pcEOLReader.peek () === TextReader.LF)
          assert (pcEOLReader.read () === TextReader.LF)
          assert (pcEOLReader.peek() === TextReader.EOF)
        }
      }
      it ("must handle Unix line termination sequences correctly") {
        new EOLDataReader {
          assert (unixEOLReader.peek () === TextReader.LF)
          assert (unixEOLReader.peek () === TextReader.LF)
          assert (unixEOLReader.read () === TextReader.LF)
          assert (unixEOLReader.peek () === TextReader.LF)
          assert (unixEOLReader.peek () === TextReader.LF)
          assert (unixEOLReader.read () === TextReader.LF)
          assert (unixEOLReader.peek () === TextReader.LF)
          assert (unixEOLReader.peek () === TextReader.LF)
          assert (unixEOLReader.read () === TextReader.LF)
          assert (unixEOLReader.peek() === TextReader.EOF)
        }
      }
      it ("must handle old Mac OS line termination sequences correctly") {
        new EOLDataReader {
          assert (oldMacOsEOLReader.peek () === TextReader.LF)
          assert (oldMacOsEOLReader.peek () === TextReader.LF)
          assert (oldMacOsEOLReader.read () === TextReader.LF)
          assert (oldMacOsEOLReader.peek () === TextReader.LF)
          assert (oldMacOsEOLReader.peek () === TextReader.LF)
          assert (oldMacOsEOLReader.read () === TextReader.LF)
          assert (oldMacOsEOLReader.peek () === TextReader.LF)
          assert (oldMacOsEOLReader.peek () === TextReader.LF)
          assert (oldMacOsEOLReader.read () === TextReader.LF)
          assert (oldMacOsEOLReader.peek() === TextReader.EOF)
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
Verify that we can tell when we've read the end-of-file marker.
*/

    describe (".atEOF") {

/*
Handle end-of-file conditions.

The first read() call should return an EOF signal, a second throws the
EOFException.  atEOF should return false prior to the EOF signal being read
and true afterwards.
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
Verify that the readAsString function operates correctly with the default
delimiter.

The readAsString function is important because it underpins all other readAs...
functions, and serves up data to those functions.
*/

    describe (".readAsString ()") {

/*
If we pass the function an EOF delimiter, then the whole file should be
returned as a field, regardless of format.
*/

      it ("must retrieve the entire PC file if passed an EOFDelimiter") {
        new EOFDelimitedReaders {
          assert (pcReader.readAsString () === rawFileData)
          assert (pcReader.atEOF === true)
        }
      }
      it ("must retrieve the entire Unix file if passed an EOFDelimiter") {
        new EOFDelimitedReaders {
          assert (unixReader.readAsString () === rawFileData)
          assert (unixReader.atEOF === true)
        }
      }
      it ("must retrieve the entire old Mac OS file if passed an EOFDelimiter")
      {
        new EOFDelimitedReaders {
          assert (oldMacOsReader.readAsString () === rawFileData)
          assert (oldMacOsReader.atEOF === true)
        }
      }

/*
If we pass a line delimiter, we should get individual lines of data.
*/

      it ("must retrieve the entire PC file if passed a LineDelimiter") {
        new LineDelimitedReaders {
          assert (pcReader.readAsString () === data)
          assert (pcReader.atEOF === false)
          assert (pcReader.readAsString () === data)
          assert (pcReader.atEOF === false)
          assert (pcReader.readAsString () === emptyLine)
          assert (pcReader.atEOF === false)
          assert (pcReader.readAsString () === dataEnd)
          assert (pcReader.atEOF === true)
        }
      }
      it ("must retrieve the entire Unix file if passed an LineDelimiter") {
        new LineDelimitedReaders {
          assert (unixReader.readAsString () === data)
          assert (unixReader.atEOF === false)
          assert (unixReader.readAsString () === data)
          assert (unixReader.atEOF === false)
          assert (unixReader.readAsString () === emptyLine)
          assert (unixReader.atEOF === false)
          assert (unixReader.readAsString () === dataEnd)
          assert (unixReader.atEOF === true)
        }
      }
      it ("must retrieve the entire old Mac OS file if passed an " +
      "LineDelimiter") {
        new LineDelimitedReaders {
          assert (oldMacOsReader.readAsString () === data)
          assert (oldMacOsReader.atEOF === false)
          assert (oldMacOsReader.readAsString () === data)
          assert (oldMacOsReader.atEOF === false)
          assert (oldMacOsReader.readAsString () === emptyLine)
          assert (oldMacOsReader.atEOF === false)
          assert (oldMacOsReader.readAsString () === dataEnd)
          assert (oldMacOsReader.atEOF === true)
        }
      }
    }

/*
Verify that the readAsString function operates correctly with a specific
delimiter.
*/

    describe (".readAsString (Delimiter)") {

/*
If we pass the function an EOF delimiter, then the whole file should be
returned as a field, regardless of format.
*/

      it ("must be able to override the default delimiter") {
        new LineDelimitedReaders {
          assert (pcReader.readAsString (EOFDelimiter) === rawFileData)
          assert (pcReader.atEOF === true)
        }
      }
    }

/*
Verify that the readAsByte function operates correctly.

Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

    describe (".readAsByte ()") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readAsBytes (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsByte () // "a"
            }
            assert (reader.readAsByte () === 1)
            intercept [NumberFormatException] {
              reader.readAsByte () // "1.1e1"
            }
            assert (reader.readAsByte () === -1)
            intercept [NumberFormatException] {
              reader.readAsByte () // "-1.1e-1"
            }
            intercept [NumberFormatException] {
              reader.readAsByte () // " 2"
            }
            intercept [NumberFormatException] {
              reader.readAsByte () // "3 "
            }
            intercept [NumberFormatException] {
              reader.readAsByte () // " 4 "
            }
            intercept [NumberFormatException] {
              reader.readAsByte () // ""
            }
            intercept [NumberFormatException] {
              reader.readAsByte () // "56fred"
            }
          }
          readAsBytes (pcReader)
          readAsBytes (unixReader)
          readAsBytes (oldMacOsReader)
        }
      }

/*
Now process the same data with the default (whitespace) delimiter that DOES
merge adjacent delimiters.  This will remove many of the exceptions (due to
spaces being merged with tabs) and will remove empty fields.
*/

      it ("must be able to read valid data and handle bad data 2") {
        new DefaultDelimitedReaders {
          def readAsBytes (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsByte () // "a"
            }
            assert (reader.readAsByte () === 1)
            intercept [NumberFormatException] {
              reader.readAsByte () // "1.1e1"
            }
            assert (reader.readAsByte () === -1)
            intercept [NumberFormatException] {
              reader.readAsByte () // "-1.1e-1"
            }
            assert (reader.readAsByte () === 2)
            assert (reader.readAsByte () === 3)
            assert (reader.readAsByte () === 4)
            intercept [NumberFormatException] {
              reader.readAsByte () // "56fred"
            }
          }
          readAsBytes (pcReader)
          readAsBytes (unixReader)
          readAsBytes (oldMacOsReader)
        }
      }
    }

/*
Verify that the readAsShort function operates correctly.

Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

    describe (".readAsShort ()") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readAsShorts (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsShort () // "a"
            }
            assert (reader.readAsShort () === 1)
            intercept [NumberFormatException] {
              reader.readAsShort () // "1.1e1"
            }
            assert (reader.readAsShort () === -1)
            intercept [NumberFormatException] {
              reader.readAsShort () // "-1.1e-1"
            }
            intercept [NumberFormatException] {
              reader.readAsShort () // " 2"
            }
            intercept [NumberFormatException] {
              reader.readAsShort () // "3 "
            }
            intercept [NumberFormatException] {
              reader.readAsShort () // " 4 "
            }
            intercept [NumberFormatException] {
              reader.readAsShort () // ""
            }
            intercept [NumberFormatException] {
              reader.readAsShort () // "56fred"
            }
          }
          readAsShorts (pcReader)
          readAsShorts (unixReader)
          readAsShorts (oldMacOsReader)
        }
      }

/*
Now process the same data with the default (whitespace) delimiter that DOES
merge adjacent delimiters.  This will remove many of the exceptions (due to
spaces being merged with tabs) and will remove empty fields.
*/

      it ("must be able to read valid data and handle bad data 2") {
        new DefaultDelimitedReaders {
          def readAsShorts (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsShort () // "a"
            }
            assert (reader.readAsShort () === 1)
            intercept [NumberFormatException] {
              reader.readAsShort () // "1.1e1"
            }
            assert (reader.readAsShort () === -1)
            intercept [NumberFormatException] {
              reader.readAsShort () // "-1.1e-1"
            }
            assert (reader.readAsShort () === 2)
            assert (reader.readAsShort () === 3)
            assert (reader.readAsShort () === 4)
            intercept [NumberFormatException] {
              reader.readAsShort () // "56fred"
            }
          }
          readAsShorts (pcReader)
          readAsShorts (unixReader)
          readAsShorts (oldMacOsReader)
        }
      }
    }

/*
Verify that the readAsInt function operates correctly.

Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

    describe (".readAsInt ()") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readAsInts (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsInt () // "a"
            }
            assert (reader.readAsInt () === 1)
            intercept [NumberFormatException] {
              reader.readAsInt () // "1.1e1"
            }
            assert (reader.readAsInt () === -1)
            intercept [NumberFormatException] {
              reader.readAsInt () // "-1.1e-1"
            }
            intercept [NumberFormatException] {
              reader.readAsInt () // " 2"
            }
            intercept [NumberFormatException] {
              reader.readAsInt () // "3 "
            }
            intercept [NumberFormatException] {
              reader.readAsInt () // " 4 "
            }
            intercept [NumberFormatException] {
              reader.readAsInt () // ""
            }
            intercept [NumberFormatException] {
              reader.readAsInt () // "56fred"
            }
          }
          readAsInts (pcReader)
          readAsInts (unixReader)
          readAsInts (oldMacOsReader)
        }
      }

/*
Now process the same data with the default (whitespace) delimiter that DOES
merge adjacent delimiters.  This will remove many of the exceptions (due to
spaces being merged with tabs) and will remove empty fields.
*/

      it ("must be able to read valid data and handle bad data 2") {
        new DefaultDelimitedReaders {
          def readAsInts (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsInt () // "a"
            }
            assert (reader.readAsInt () === 1)
            intercept [NumberFormatException] {
              reader.readAsInt () // "1.1e1"
            }
            assert (reader.readAsInt () === -1)
            intercept [NumberFormatException] {
              reader.readAsInt () // "-1.1e-1"
            }
            assert (reader.readAsInt () === 2)
            assert (reader.readAsInt () === 3)
            assert (reader.readAsInt () === 4)
            intercept [NumberFormatException] {
              reader.readAsInt () // "56fred"
            }
          }
          readAsInts (pcReader)
          readAsInts (unixReader)
          readAsInts (oldMacOsReader)
        }
      }
    }

/*
Verify that the readAsLong function operates correctly.

Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

    describe (".readAsLong ()") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readAsLongs (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsLong () // "a"
            }
            assert (reader.readAsLong () === 1)
            intercept [NumberFormatException] {
              reader.readAsLong () // "1.1e1"
            }
            assert (reader.readAsLong () === -1)
            intercept [NumberFormatException] {
              reader.readAsLong () // "-1.1e-1"
            }
            intercept [NumberFormatException] {
              reader.readAsLong () // " 2"
            }
            intercept [NumberFormatException] {
              reader.readAsLong () // "3 "
            }
            intercept [NumberFormatException] {
              reader.readAsLong () // " 4 "
            }
            intercept [NumberFormatException] {
              reader.readAsLong () // ""
            }
            intercept [NumberFormatException] {
              reader.readAsLong () // "56fred"
            }
          }
          readAsLongs (pcReader)
          readAsLongs (unixReader)
          readAsLongs (oldMacOsReader)
        }
      }

/*
Now process the same data with the default (whitespace) delimiter that DOES
merge adjacent delimiters.  This will remove many of the exceptions (due to
spaces being merged with tabs) and will remove empty fields.
*/

      it ("must be able to read valid data and handle bad data 2") {
        new DefaultDelimitedReaders {
          def readAsLongs (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsLong () // "a"
            }
            assert (reader.readAsLong () === 1)
            intercept [NumberFormatException] {
              reader.readAsLong () // "1.1e1"
            }
            assert (reader.readAsLong () === -1)
            intercept [NumberFormatException] {
              reader.readAsLong () // "-1.1e-1"
            }
            assert (reader.readAsLong () === 2)
            assert (reader.readAsLong () === 3)
            assert (reader.readAsLong () === 4)
            intercept [NumberFormatException] {
              reader.readAsLong () // "56fred"
            }
          }
          readAsLongs (pcReader)
          readAsLongs (unixReader)
          readAsLongs (oldMacOsReader)
        }
      }
    }

/*
Verify that the readAsFloat function operates correctly.

Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

    describe (".readAsFloat ()") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readAsFloats (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsFloat () // "a"
            }
            assert (abs (reader.readAsFloat () - 1.0) < 1.0e-8)
            assert (abs (reader.readAsFloat () - 1.1e1) < 1.0e-8)
            assert (abs (reader.readAsFloat () - -1.0) < 1.0e-8)
            assert (abs (reader.readAsFloat () - -1.1e-1) < 1.0e-8)
            intercept [NumberFormatException] {
              reader.readAsFloat () // " 2"
            }
            intercept [NumberFormatException] {
              reader.readAsFloat () // "3 "
            }
            intercept [NumberFormatException] {
              reader.readAsFloat () // " 4 "
            }
            intercept [NumberFormatException] {
              reader.readAsFloat () // ""
            }
            intercept [NumberFormatException] {
              reader.readAsFloat () // "56fred"
            }
          }
          readAsFloats (pcReader)
          readAsFloats (unixReader)
          readAsFloats (oldMacOsReader)
        }
      }

/*
Now process the same data with the default (whitespace) delimiter that DOES
merge adjacent delimiters.  This will remove many of the exceptions (due to
spaces being merged with tabs) and will remove empty fields.
*/

      it ("must be able to read valid data and handle bad data 2") {
        new DefaultDelimitedReaders {
          def readAsFloats (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsFloat () // "a"
            }
            assert (abs (reader.readAsFloat () - 1.0) < 1.0e-8)
            assert (abs (reader.readAsFloat () - 1.1e1) < 1.0e-8)
            assert (abs (reader.readAsFloat () - -1.0) < 1.0e-8)
            assert (abs (reader.readAsFloat () - -1.1e-1) < 1.0e-8)
            assert (abs (reader.readAsFloat () - 2.0) < 1.0e-8)
            assert (abs (reader.readAsFloat () - 3.0) < 1.0e-8)
            assert (abs (reader.readAsFloat () - 4.0) < 1.0e-8)
            intercept [NumberFormatException] {
              reader.readAsFloat () // "56fred"
            }
          }
          readAsFloats (pcReader)
          readAsFloats (unixReader)
          readAsFloats (oldMacOsReader)
        }
      }
    }

/*
Verify that the readAsDouble function operates correctly.

Firstly, read using the test delimiter that does not merge adjacent delimiters.
*/

    describe (".readAsDouble ()") {
      it ("must be able to read valid data and handle bad data") {
        new TestDelimitedReaders {
          def readAsDoubles (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsDouble () // "a"
            }
            assert (abs (reader.readAsDouble () - 1.0) < 1.0e-10)
            assert (abs (reader.readAsDouble () - 1.1e1) < 1.0e-10)
            assert (abs (reader.readAsDouble () - -1.0) < 1.0e-10)
            assert (abs (reader.readAsDouble () - -1.1e-1) < 1.0e-10)
            intercept [NumberFormatException] {
              reader.readAsDouble () // " 2"
            }
            intercept [NumberFormatException] {
              reader.readAsDouble () // "3 "
            }
            intercept [NumberFormatException] {
              reader.readAsDouble () // " 4 "
            }
            intercept [NumberFormatException] {
              reader.readAsDouble () // ""
            }
            intercept [NumberFormatException] {
              reader.readAsDouble () // "56fred"
            }
          }
          readAsDoubles (pcReader)
          readAsDoubles (unixReader)
          readAsDoubles (oldMacOsReader)
        }
      }

/*
Now process the same data with the default (whitespace) delimiter that DOES
merge adjacent delimiters.  This will remove many of the exceptions (due to
spaces being merged with tabs) and will remove empty fields.
*/

      it ("must be able to read valid data and handle bad data 2") {
        new DefaultDelimitedReaders {
          def readAsDoubles (reader: TextReader) {
            intercept [NumberFormatException] {
              reader.readAsDouble () // "a"
            }
            assert (abs (reader.readAsDouble () - 1.0) < 1.0e-8)
            assert (abs (reader.readAsDouble () - 1.1e1) < 1.0e-8)
            assert (abs (reader.readAsDouble () - -1.0) < 1.0e-8)
            assert (abs (reader.readAsDouble () - -1.1e-1) < 1.0e-8)
            assert (abs (reader.readAsDouble () - 2.0) < 1.0e-8)
            assert (abs (reader.readAsDouble () - 3.0) < 1.0e-8)
            assert (abs (reader.readAsDouble () - 4.0) < 1.0e-8)
            intercept [NumberFormatException] {
              reader.readAsDouble () // "56fred"
            }
          }
          readAsDoubles (pcReader)
          readAsDoubles (unixReader)
          readAsDoubles (oldMacOsReader)
        }
      }
    }
  }
}