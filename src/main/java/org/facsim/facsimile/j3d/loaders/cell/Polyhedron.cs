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

C# source file for the Dürr .NET Cell Library Polyhedron class.
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

//=============================================================================
/**
<summary>Cell representing a user-defined 3D meshes.

<para>A polyhedron is simply a list of points, and faces defined by these
points, in 3D space.</para></summary>
*/
//=============================================================================

    public sealed class Polyhedron : Cell {

/**
<summary>List of vertices making up the polyhedra.</summary>
*/

	Dürr.Utils.Point [] vertex;

/**
<summary>List of defined faces, each made up of a vertex list.</summary>
*/

	System.Collections.Generic.List <uint []> face;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a polyhedron with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Polyhedron () : base () {

/*
ACE cannot create or edit polyhedra, so there are no recognised default
settings.  Simply initialise the values and do no more.
*/

	    vertex = null;
	    face = new System.Collections.Generic.List<uint []> ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a polyhedron from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Polyhedron (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Initialise the list of faces.
*/

	    face = new System.Collections.Generic.List<uint []> ();

/*
Read the number of vertices used to define the faces.  This value cannot be
less than 0.
*/

	    uint numVertices = cellFile.ReadType <uint> ();

/*
If we have a positive value, then create the vertex array and read the vertices
from the file.
*/

	    if (numVertices > 0) {
		vertex = new Dürr.Utils.Point [numVertices];
		for (uint i = 0; i < numVertices; ++i) {
//		    double x = cellFile.ReadType <double> ();
//		    double y = cellFile.ReadType <double> ();
//		    double z = cellFile.ReadType <double> ();
//		    vertex [i] = new Dürr.Utils.Point (new Length (x), new
//		    Length (y),  new Length (z));
		    vertex [i].X = new Length (cellFile.ReadType <double> ());
		    vertex [i].Y = new Length (cellFile.ReadType <double> ());
		    vertex [i].Z = new Length (cellFile.ReadType <double> ());
		}

/*
Now for the number of faces.
*/

		uint numFaces = cellFile.ReadType <uint> ();

/*
As before, if we have more than 0 faces, then create them.
*/

		if (numFaces > 0) {
		    for (uint i = 0; i < numFaces; ++i) {

/*
Firstly, read the number of faces from the stream.  This cannot be less than
three.
*/

			uint numVerticesInFace = cellFile.ReadMin <uint> (3,
			"Face vertices");

/*
Now read in all of the vertices, checking that none is less than 0 or greater
than or equal to the number of vertices.
*/

			uint [] vertices = new uint [numVerticesInFace];
			for (uint j = 0; j < numVerticesInFace; ++j) {
			    vertices [j] = cellFile.ReadMinMax <uint> (0,
			    numVertices - 1, "Face vertex index");
			}
			face.Add (vertices);
		    }
		}
	    }
	}
    }
}
