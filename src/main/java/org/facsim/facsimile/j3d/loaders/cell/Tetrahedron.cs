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

C# source file for the Dürr .NET Cell Library Tetrahedron class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D tetrahedron.

<para>A 3D tetrahedron.</para></summary>
*/
//=============================================================================

    public sealed class Tetrahedron : Cell {

/**
<summary>Length of a side on the base equilateral triangle.</summary>
*/

	private double baseLength;

/**
<summary>Length of a side on the top equilateral triangle.</summary>
*/

	private double topLength;

/**
<summary>Height of the tetrahedron.</summary>
*/

	private double height;

/**
<summary>The X and Y offsets of this tetrahedron.</summary>
*/

	private XYOffset xyOffsets;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a tetrahedron with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Tetrahedron () : base () {

/*
Initialise all to zero.
*/

	    baseLength = 0.0;
	    topLength = 0.0;
	    height = 0.0;
	    xyOffsets = new XYOffset ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a tetrahedron from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Tetrahedron (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Read in the base height from the cell file.  This cannot be less than 0.
*/

	    baseLength = cellFile.ReadMin <double> (0.0,
	    "Tetrahedron base length");

/*
Read in the top height from the cell file.  This cannot be less than 0.
*/

	    topLength = cellFile.ReadMin <double> (0.0,
	    "Tetrahedron top length");

/*
Read in the base height from the cell file.  This cannot be less than 0.
*/

	    height = cellFile.ReadMin <double> (0.0, "Tetrahedron height");

/*
Now for the X & Y offsets.
*/

	    xyOffsets = new XYOffset (cellFile);
	}
    }
}
