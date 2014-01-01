/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

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

import org.facsim.requireNonNull
import org.facsim.requireValid

//=============================================================================
/**
Field delimiters to be used with [[org.facsim.io.TextReader!]]-based readers.

Delimiters are sets of one or more characters that delimit fields in a text
data stream.

In some data formats, a single delimiter character from the set is used to
delimit fields.  Examples include ''comma-separated value'' (''CSV'') files
that use comma (',') and ''line feed'' ('\n') characters as delimiters, and
tab-delimited files that use ''horizontal tab'' ('\t') and ''line feed'' ('\n')
characters as delimiters.  Consecutive delimiters denote empty fields and are
not merged.  Such delimiters must be constructed with the
'''mergeConsecutive''' argument set to `false`.

In other data formats, consecutive delimiters are merged together and treated
as a single delimiter.  Examples include whitespace-delimited text files that
use ''space'' (' '), ''horizontal tab'' ('\t') and ''line feed'' ('\n')
characters as delimiters.  Such delimiters must set '''mergeConsecutive''' to
`true`.

@constructor Construct a new delimiter instance.

@param delimiters Set of characters to be treated as delimiters.  Note that the
''line feed'' ('\n') character is not automatically added to the set and must
be explicitly included if required.  Note also that the ''line feed'' character
matches all line termination sequences$mdash;refer to
[[org.facsim.io.TextReader!]] for further information.  The ''null'' ('\0') and
''carriage return'' ('\r') characters cannot be used as delimiters and will
result in an exception being thrown if included in the delimiter set.  If the
set of delimiters is empty, then an associated reader will return the entire
file as a single field.

@param mergeConsecutive If `true`, consecutive delimiter characters will be
treated as a single delimiter.  If `false`, consecutive delimiter characters
will be treated as separate delimiters, and will consequently delimit empty
fields.

@throws java.lang.NullPointerException if '''delimiters''' is `null`.

@throws java.lang.InvalidArgumentException if '''delimiters''' contains invalid
delimiter characters.

@see [[org.facsim.io.TextReader!]] for further information.

@since 0.0
*/
//=============================================================================

class Delimiter (delimiters: Set [Int], mergeConsecutive: Boolean) extends
NotNull {

/*
Preconditions: forbid the use of null and carriage return characters as
delimiters.
*/

  requireNonNull (delimiters)
  requireValid (delimiters, !delimiters.contains (TextReader.NUL) &&
  !delimiters.contains (TextReader.CR))

//-----------------------------------------------------------------------------
/**
Determine if reader has reached this delimiter.

If the character at the current reader position belongs to this delimiter set,
then that character is read (together with any adjacent delimiter characters,
if we're merging consecutive delimiters) and the function returns `true`.
Alternatively, if the current character is not a delimiter, then it is not read
and the function returns `false`.

@param reader Reader being processed.

@return `true` if a delimiter sequence was read from '''reader'''; `false`
otherwise.

@throws java.io.IOException if an attempt is made to read a character after an
end-of-file condition has been signaled by a previous read operation, or if any
other I/O error occurs during a read operation.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private [io] final def reached (reader: TextReader): Boolean = {

/*
Tail-recursive helper function to handle reading of consecutive delimiters, if
required.  The result argument keeps track of whether a delimiter was found,
and is initialized to false.
*/

    def peek (result: Boolean): Boolean = {

/*
If end-of-file comes next, or if the character is not a delimiter, then
we're done.
*/

      val peekedChar = reader.peek ()
      if (peekedChar == TextReader.EOF || !delimiters.contains (peekedChar))
      result

/*
Otherwise, we have a delimiter character, so first read it from the stream to
consume it. 
*/

      else {
        reader.read ()

/*
If we're merging consecutive delimiters, then go round the loop again, noting
that we have found a delimiter.  Otherwise, we have already identified the end
of the delimiter.
*/

        if (mergeConsecutive) peek (true)
        else true
      }
    }

/*
Start the ball rolling, noting that we have yet to find a delimiter character.
*/

    peek (false)
  }
}