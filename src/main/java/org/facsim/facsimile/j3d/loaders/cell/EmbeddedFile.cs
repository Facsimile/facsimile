/*
Copyright © 2005, Dürr Systems, Inc.  All rights reserved.

This source code is the proprietary intellectual property of Dürr Systems, Inc.
and may not be redistributed in any form whatsoever without the prior express
written permission of Dürr Systems, Inc.

Dürr Systems, Inc.
12755 East Nine Mile Road
Warren, MI 48089
USA

Tel: +1 (586) 755-7500
Fax: +1 (586) 758-1901
===============================================================================
$Id$

$Name$

Refer to the current documentation standards before making any modifications to
this file.  The documentation standards are available on DurrNet.

Comments beginning with "/**" have been formatted for use with the C# language
automated code documentation utility.  The automated code documentation is
available through Intellisense.

C# source file for the Dürr .NET Cell Library EmbeddedFile class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D embedded file.

<para>A 3D embedded file.</para></summary>
*/
//=============================================================================

    public sealed class EmbeddedFile : Cell {

/**
<summary>String used to terminate the contents of an embedded file.</summary>
*/

	private static readonly string TERMINATION_STRING;

/**
<summary>The contents of the embedded file.</summary>
*/

	private System.Collections.Generic.List <string> contents;

//-----------------------------------------------------------------------------
/**
<summary>Static constructor.</summary>
*/
//-----------------------------------------------------------------------------

	static EmbeddedFile () {
	    TERMINATION_STRING = "#Inventor END";
	}

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs an embedded file with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public EmbeddedFile () : base () {

/*
Initialise the embedded file contents to nothing.
*/

	    contents = new System.Collections.Generic.List <string> ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs an embedded file from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public EmbeddedFile (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Firstly, set the contents of the embedded file to nothing.
*/

	    contents = new System.Collections.Generic.List <string> ();

/*
Now read each line from the file in turn.
*/

	    string line;
	    while ((line = cellFile.ReadLine ()) != TERMINATION_STRING) {

/*
If we read an end-of-stream condition, then report an error.
*/

		if (line == null) {
		    throw new System.IO.EndOfStreamException
		    ("Unexpected end of stream reading embedded file");
		}

/*
Otherwise, this line is part of the embedded file and can be appended to the
current contents.
*/

		contents.Add (line);
	    }
	}
    }
}
