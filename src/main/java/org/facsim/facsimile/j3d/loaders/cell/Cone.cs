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

C# source file for the Dürr .NET Cell Library Cone classes.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D cone.

<para>An cone is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public abstract class Cone : Cell {

/**
<summary>Radius of the base of the cone.</summary>
*/

	private double radius;

/**
<summary>Height of the cone.</summary>
*/

	private double height;

/**
<summary>Offset co-ordinates for the top of the cone.</summary>
*/

	private XYOffset xyOffsets;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs an cone with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Cone () : base () {

/*
Set all values to the ACE defaults.
*/

	    radius = 0.0;
	    height = 0.0;
	    xyOffsets = new XYOffset ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs an cone from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Cone (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {

/*
Read in the radius.  This cannot be negative.
*/

	    radius = cellFile.ReadMin <double> (0.0, "Cone radius");

/*
Now for the height.  This also cannot be negative.
*/

	    height = cellFile.ReadMin <double> (0.0, "Cone height");

/*
Now for the X-Y offsets.
*/

	    xyOffsets = new XYOffset (cellFile);
	}
    }

//=============================================================================
/**
<summary>A coarse 3D cone.

<para>An cone is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class ConeCoarse : Cone {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a coarse cone with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public ConeCoarse () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a coarse cone from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public ConeCoarse (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>A fine 3D cone.

<para>An cone is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class ConeFine : Cone {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a fine cone with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public ConeFine () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a fine cone from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public ConeFine (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {
	}
    }
}
