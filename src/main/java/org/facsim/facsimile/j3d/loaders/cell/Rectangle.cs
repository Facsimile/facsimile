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

C# source file for the Dürr .NET Cell Library Rectangle class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D rectangle.

<para>Represents a rectangle drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class Rectangle : Cell {

/**
<summary>The width of the rectangle.</summary>
*/

	private double width;

/**
<summary>The length of the rectangle.</summary>
*/

	private double length;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a rectangle with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Rectangle () : base () {

/*
Initialise the width and length to 0 - the default values.
*/

	    width = 0.0;
	    length = 0.0;
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a rectangle from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Rectangle (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Read the width from the file - this cannot be less than 0.
*/

	    width = cellFile.ReadMin <double> (0.0, "Rectangle width");

/*
Now for the length - this also cannot be less than 0.
*/

	    length = cellFile.ReadMin <double> (0.0, "Rectangle length");

/*
For reasons unknown, there's also a pair of X-Y offset values, which can only
be zero.  Just give a warning if they are not zero.
*/

	    XYOffset ignored = new XYOffset (cellFile);
	    if (ignored.X != 0.0) {
		cellFile.RaiseFormatWarning
		("Non-zero Rectangle X-offset ignored: " + ignored.X);
	    }
	    if (ignored.Y != 0.0) {
		cellFile.RaiseFormatWarning
		("Non-zero Rectangle Y-offset ignored: " + ignored.Y);
	    }
	}
    }
}
