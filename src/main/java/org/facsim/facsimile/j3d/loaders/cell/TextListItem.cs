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

C# source file for the Dürr .NET Cell Library TextListItem class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {
    using Length = Dürr.Utils.Measurement <Dürr.Utils.LengthUnit>;

//=============================================================================
/**
<summary>A 3D text list item.

<para>A text list item is a single line of text belonging to a text list.
</para></summary>
*/
//=============================================================================

    public sealed class TextListItem {

/**
<summary>Reference of the text list item's parent text list.</summary>
*/
	private TextList parent;

/**
<summary>Point representing the translation offset of this item relative to its
text list.</summary>
*/

	private Dürr.Utils.Point translation;

/**
<summary>The contents of this text list item.</summary>
*/

	private string text;

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Construct a new text list item from the cell file.</para></summary>
*/
//-----------------------------------------------------------------------------

	public TextListItem (TextList itemsParent, Dürr.Utils.TokenisedStream
	cellFile) {

/*
Store the parent.
*/

	    parent = itemsParent;

/*
Read in the line of X, Y and Z-axis translations (relative to the parent text
list).  Before reading the last value, change to the new line delimiter.
*/

	    translation.X = new Length (cellFile.ReadType <double> ());
	    translation.Y = new Length (cellFile.ReadType <double> ());
	    cellFile.Delimiter = Cell.NewlineDelimiter;
	    translation.Z = new Length (cellFile.ReadType <double> ());

/*
Restore the whitespace delimiter.
*/

	    cellFile.Delimiter = Cell.WhitespaceDelimiter;

/*
Read the text in as the remainder of the following line.
*/

	    text = cellFile.ReadLine ();
	}
    }
}
