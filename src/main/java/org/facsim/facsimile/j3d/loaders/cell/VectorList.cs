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

C# source file for the Dürr .NET Cell Library VectorList class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

/*
Useful aliases.
*/

    using Length = Dürr.Utils.Measurement <Dürr.Utils.LengthUnit>;

/*
Move/draw flag enumeration.
*/

    internal enum MoveDrawFlag : byte {
	MD_MOVE = 0,
	MD_DRAW = 1,
    }

//=============================================================================
/**
<summary>Cell representing a set of straight lines.

<para>A vector list is simply a list of points, some connected by straight
lines, in 3D space.</para></summary>
*/
//=============================================================================

    public sealed class VectorList : Cell {

/**
<summary>List of vertices making up the vector list.</summary>
*/

	Dürr.Utils.Point [] vertex;

/**
<summary>List of move/draw commands for each vertex.  The first MUST be a move
command.</summary>
*/

	
	MoveDrawFlag [] moveDrawFlag;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a vector list with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public VectorList () : base () {

/*
Initialise to an empty list.
*/

	    vertex = null;
	    moveDrawFlag = null;
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a vector list from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public VectorList (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Start off by reading the number of vertices.  This value must be greater than
or equal to zero.
*/

	    uint numVertices = cellFile.ReadMin <uint> (0,
	    "Vector list vertices");

/*
If this value is greater than zero, then initialise the arrays and populate
them from the file.
*/

	    if (numVertices > 0) {
		vertex = new Dürr.Utils.Point [numVertices];
		moveDrawFlag = new MoveDrawFlag [numVertices];
		for (uint i = 0; i < numVertices; ++i) {
		    vertex [i].X = new Length (cellFile.ReadType <double> ());
		    vertex [i].Y = new Length (cellFile.ReadType <double> ());
		    vertex [i].Z = new Length (cellFile.ReadType <double> ());
		    moveDrawFlag [i] = (MoveDrawFlag) cellFile.ReadMinMax <int>
		    (0, 1, "Vector list move/draw flag");
		}
	    }
	}
    }
}
