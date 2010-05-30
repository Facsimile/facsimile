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

C# source file for the Dürr .NET Cell Library Hemisphere classes.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D hemisphere.

<para>A 3D hemisphere.</para></summary>
*/
//=============================================================================

    public abstract class Hemisphere : Cell {

/**
<summary>Radius of the hemisphere.</summary>
*/

	private double radius;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a hemisphere with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Hemisphere () : base () {

/*
Set all values to the ACE defaults.
*/

	    radius = 0.0;
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a hemisphere from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Hemisphere (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Read in the radius.  This cannot be negative.
*/

	    radius = cellFile.ReadMin <double> (0.0, "Hemisphere radius");
	}
    }

//=============================================================================
/**
<summary>A coarse 3D hemisphere.

<para>A 3D hemisphere.</para></summary>
*/
//=============================================================================

    public sealed class HemisphereCoarse : Hemisphere {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a coarse hemisphere with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public HemisphereCoarse () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a coarse hemisphere from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public HemisphereCoarse (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>A fine 3D hemisphere.

<para>A 3D hemisphere.</para></summary>
*/
//=============================================================================

    public sealed class HemisphereFine : Hemisphere {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a fine hemisphere with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public HemisphereFine () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a fine hemisphere from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public HemisphereFine (Set owner, Dürr.Utils.TokenisedStream cellFile)
	: base (owner, cellFile) {
	}
    }
}
