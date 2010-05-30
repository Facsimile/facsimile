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

C# source file for the Dürr .NET Cell Library Arc classes.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D arc.

<para>An arc is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public abstract class Arc : Cell {

/**
<summary>The radius (in model units) of the arc.</summary>
*/

	private double radius;

/**
<summary>The angle (in degrees) at which the arc starts.</summary>
*/

	private double startAngle;

/**
<summary>The angle (in degrees) at which the arc ends.</summary>
*/

	private double endAngle;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs an arc with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Arc () : base () {

/*
Set the arguments to ACE default values.  (This doesn't look much like an arc
to me!)
*/

	    radius = 0.0;
	    startAngle = 0.0;
	    endAngle = 0.0;
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs an arc from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Arc (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {

/*
Read the radius.  This cannot be less than 0.0, but anything else goes.
*/

	    radius = cellFile.ReadMin <double> (0.0, "Arc radius");

/*
Now for the start and end angles.  These can be anything.  Note that angle 0.0
is aligned with the local positive X-axis.
*/

	    startAngle = cellFile.ReadType <double> ();
	    endAngle = cellFile.ReadType <double> ();
	}
    }

//=============================================================================
/**
<summary>A coarse 3D arc.

<para>An arc is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class ArcCoarse : Arc {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a coarse arc with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public ArcCoarse () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a coarse arc from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public ArcCoarse (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>A fine 3D arc.

<para>An arc is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class ArcFine : Arc {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a fine arc with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public ArcFine () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a fine arc from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public ArcFine (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {
	}
    }
}
