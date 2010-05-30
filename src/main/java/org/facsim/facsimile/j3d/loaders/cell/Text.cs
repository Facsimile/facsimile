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

C# source file for the Dürr .NET Cell Library Text classes.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D text.

<para>Text is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public abstract class Text : Cell {

/**
<summary>The text to be displayed.</summary>
*/

	private string text;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a text string with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Text () : base () {
	    text = "";
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs an text from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Text (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {

/*
Read the text from the file.  This text will take up the remainder of the line.
*/

	    text = cellFile.ReadLine ();
	}
    }

//=============================================================================
/**
<summary>World text.

<para>Text is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextWorld : Text {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs world text with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextWorld () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs world text from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextWorld (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Screen fast text.

<para>Text is drawn on the screen's X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextScreenFast : Text {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs screen fast text with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextScreenFast () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs screen fast text from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextScreenFast (Set owner, Dürr.Utils.TokenisedStream cellFile)
	: base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Screen normal text.

<para>Text is drawn on the screen's X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextScreenNormal : Text {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs screen normal text with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextScreenNormal () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs screen normal text from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextScreenNormal (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Unrotate fast text.

<para>Text is drawn on the screen facing the user.</para></summary>
*/
//=============================================================================

    public sealed class TextUnrotateFast : Text {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs unrotate fast text with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextUnrotateFast () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs unrotate fast text from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextUnrotateFast (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Unotate normal text.

<para>Text is drawn on the screen's X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextUnrotateNormal : Text {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs unrotate normal text with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextUnrotateNormal () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs unrotate normal text from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextUnrotateNormal (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }
}
