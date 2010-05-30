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

C# source file for the Dürr .NET Cell Library XYOffset struct.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>X-Y offset co-ordinates.

<para>These values are used by a number of different cell types to allow an X-Y
offset to be specified for the element.</para></summary>
*/
//=============================================================================

    public struct XYOffset {

/**
<summary>The X offset co-ordinates.</summary>
*/

	private double x;

/**
<summary>The Y offset co-ordinates.</summary>
*/

	private double y;

//-----------------------------------------------------------------------------
/**
<summary>Cell file stream constructor.

<para>Construct a new X-Y offset co-ordinate pair from a cell file stream.
</para></summary>

<param name="cellFile">The stream from which the X-Y co-ordinate pair should be
read.</param>
*/
//-----------------------------------------------------------------------------

	public XYOffset (Dürr.Utils.TokenisedStream cellFile) {

/*
Read the X and Y offsets from the stream.  These values are unconstrained and
may be anything.
*/

	    x = cellFile.ReadType <double> ();
	    y = cellFile.ReadType <double> ();
	}

//-----------------------------------------------------------------------------
/**
<summary>X-axis offset property.</summary>
*/
//-----------------------------------------------------------------------------

	public double X {
	    get {
		return x;
	    }
	    set {
		x = value;
	    }
	}

//-----------------------------------------------------------------------------
/**
<summary>Y-axis offset property.</summary>
*/
//-----------------------------------------------------------------------------

	public double Y {
	    get {
		return y;
	    }
	    set {
		y = value;
	    }
	}
    }
}
