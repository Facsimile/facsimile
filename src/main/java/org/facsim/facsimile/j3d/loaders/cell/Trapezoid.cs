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

C# source file for the Dürr .NET Cell Library Trapezoid class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D trapezoid.

<para>A 3D trapezoid.</para></summary>
*/
//=============================================================================

    public sealed class Trapezoid : Cell {

/**
<summary>Width of trapezoid base in model units.</summary>
*/

	private double baseWidth;

/**
<summary>Length of trapezoid base in model units.</summary>
*/

	private double baseLength;

/**
<summary>Width of trapezoid top in model units.</summary>
*/

	private double topWidth;

/**
<summary>Length of trapezoid batopse in model units.</summary>
*/

	private double topLength;

/**
<summary>Height of trapezoid in model units.</summary>
*/

	private double height;

/**
<summary>The x-y offset co-ordinates for the top of the trapezoid.</summary>
*/

	private XYOffset xyOffsets;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a trapezoid with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Trapezoid () : base () {

/*
Set up default values, as per ACE.
*/

	    baseWidth = 0.0;
	    baseLength = 0.0;
	    topWidth = 0.0;
	    topLength = 0.0;
	    height = 0.0;
	    xyOffsets = new XYOffset ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a trapezoid from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Trapezoid (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Read in the base width.  This cannot be negative.
*/

	    baseWidth = cellFile.ReadMin (0.0, "Trapezoid base width");

/*
Read in the base length.  This cannot be negative.
*/

	    baseLength = cellFile.ReadMin (0.0, "Trapezoid base length");

/*
Read in the top width.  This cannot be negative.
*/

	    topWidth = cellFile.ReadMin (0.0, "Trapezoid top width");

/*
Read in the top length.  This cannot be negative.
*/

	    topLength = cellFile.ReadMin (0.0, "Trapezoid top length");

/*
Read in the height.  This cannot be negative.
*/

	    height = cellFile.ReadMin (0.0, "Trapezoid height");

/*
Read in the x and y off-sets.  Anything goes here...
*/

	    xyOffsets = new XYOffset (cellFile);
	}
    }
}
