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

C# source file for the Dürr .NET Cell Library FileReference class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D file reference.

<para>A 3D file reference.</para></summary>
*/
//=============================================================================

    public sealed class FileReference : Cell, IDefinition {

/*
Path (relative or absolute) to the file we're referring to.
*/

	private string path;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a file reference with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public FileReference () : base () {

/*
Hmmm.  A file reference that doesn't exist!

TODO: What the hell is this all about!
*/

	    path = null;
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a file reference from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public FileReference (RootSet owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {

/*
Read the file path from the cell file.
*/

	    path = cellFile.ReadToken (false);
	}

//-----------------------------------------------------------------------------
/**
<summary>Report the path to interested parties.</summary>
*/
//-----------------------------------------------------------------------------

	public string Path {
	    get {
		return path;
	    }
	}
    }
}
