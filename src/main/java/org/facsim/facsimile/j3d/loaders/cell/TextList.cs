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

C# source file for the Dürr .NET Cell Library TextList classes.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>A 3D text list.

<para>Text lists are drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public abstract class TextList : Cell {

/**
<summary>The items belonging to this text list.</summary>
*/

	private System.Collections.Generic.List <TextListItem> items;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs an text list with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextList () : base () {

/*
There are no default ACE settings for text lists, since they are not supported.
Ho hum!  Create an empty list instead.
*/

	    items = new System.Collections.Generic.List <TextListItem> ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs an text list from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextList (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {

/*
Firstly, read in the number of items in the list.
*/

	    ulong numItems = cellFile.ReadMin <ulong> (0,
	    "Number of text list items");

/*
Create the item list.
*/

	    items = new System.Collections.Generic.List<TextListItem> ();

/*
Now read in each text list item in turn.
*/

	    for (ulong i = 0; i < numItems; ++i) {
		TextListItem item = new TextListItem (this, cellFile);
		items.Add (item);
	    }
	}
    }

//=============================================================================
/**
<summary>World text list.

<para>Text list is drawn on the local X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextListWorld : TextList {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs world text list with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextListWorld () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs world text list from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextListWorld (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Screen fast text list.

<para>Text list is drawn on the screen's X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextListScreenFast : TextList {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs screen fast text list with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextListScreenFast () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs screen fast text list from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextListScreenFast (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Screen normal text list.

<para>Text list is drawn on the screen's X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextListScreenNormal : TextList {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs screen normal text list with default settings.</para>
</summary>
*/
//-----------------------------------------------------------------------------

	public TextListScreenNormal () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs screen normal text list from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextListScreenNormal (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Unrotate fast text list.

<para>Text list is drawn on the screen facing the user.</para></summary>
*/
//=============================================================================

    public sealed class TextListUnrotateFast : TextList {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs unrotate fast text list with default settings.</para>
</summary>
*/
//-----------------------------------------------------------------------------

	public TextListUnrotateFast () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs unrotate fast text list from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextListUnrotateFast (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }

//=============================================================================
/**
<summary>Unotate normal text list.

<para>Text list is drawn on the screen's X-Y plane.</para></summary>
*/
//=============================================================================

    public sealed class TextListUnrotateNormal : TextList {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs unrotate normal text list with default settings.</para>
</summary>
*/
//-----------------------------------------------------------------------------

	public TextListUnrotateNormal () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs unrotate normal text list from a stream of cell data.
</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public TextListUnrotateNormal (Set owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }
}
