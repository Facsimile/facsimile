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

C# source file for the Dürr .NET Cell Library Triad class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>Visual tool showing orientation of each axis.

<para>A triad is simply a represention showing the local orientation of the
three axes; it is otherwise invisible and useful for debugging purposes only.
</para></summary>
*/
//=============================================================================

    public sealed class Triad : Cell {

/**
<summary>Flag indicating whether the triad is visible or not.</summary>
*/

	private bool visible;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a triad with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Triad () : base () {

/*
Initialise the visibility flag.
*/

	    visible = true;
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a triad from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Triad (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {

/*
Read in the visibility flag.
*/

	    visible = cellFile.ReadBool ();
	}
    }
}
