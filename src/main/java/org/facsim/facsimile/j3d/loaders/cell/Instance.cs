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

C# source file for the Dürr .NET Cell Library Instance class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>Reference to an definition object.

<para>The reference may be to any object that implements the IDefinition
interface.</para></summary>
*/
//=============================================================================

    public sealed class Instance : Cell {

/**
<summary>Reference to the definition that this instance represents.</summary>
*/

	private IDefinition definition;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs an instance with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Instance () : base () {

/*
Initialise the definition to be null.  This is technically illegal, but this is
the best that we can do.
*/

	    definition = null;
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs an instance from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Instance (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {

/*
Read the name of the referenced definition from the file.
*/

	    string definitionName = cellFile.ReadToken (false);

/*
Lookup this name to see if there's a definition that has this name.

Don't worry about checking the name of this definition - it has to match an
element name, so there's no point.  Also, if it doesn't match the name of a
definition, then it is changed to the whatever name the next definition has.
*/

	    definition = Root.lookupDefinition (definitionName);

/*
If this definition is null, then we do not yet have this definition in place,
so read it in.
*/

	    if (definition == null) {
		definition = (IDefinition) Cell.ReadNextCell (Root, cellFile,
		true);
		Root.addDefinition (definition);
	    }
	}
    }
}
