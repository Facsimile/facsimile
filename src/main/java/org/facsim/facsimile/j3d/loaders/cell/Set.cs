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

C# source file for the Dürr .NET Cell Library Set class.
===============================================================================
*/

/*
The contents of this file belong to the Dürr .NET Cell Library's namespace.
*/

namespace Dürr.CellLib {

//=============================================================================
/**
<summary>Base class for all types of set.

<para>Sets are a special type of set that can contain other cells.</para>
</summary>
*/
//=============================================================================

    public abstract class Set : Cell {

/**
<summary>Child cells belonging to this set.</summary>
*/

	System.Collections.Generic.List <Cell> children;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a set with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public Set () : base () {

/*
Initialise the list of children.
*/

	    children = new System.Collections.Generic.List <Cell> ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a set from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public Set (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {

/*
Read the number of children from the stream.  This cannot be less than 0.
*/

	    uint numChildren = cellFile.ReadType <uint> ();

/*
Initialise the list of children.
*/

	    children = new System.Collections.Generic.List <Cell> ((int)
	    numChildren);

/*
Now read each child in also.
*/

	    for (uint i = 0; i < numChildren; ++i) {

/*
Read the next cell element from the file.
*/

		Cell.ReadNextCell (this, cellFile, false);
	    }
	}

//-----------------------------------------------------------------------------
/**
<summary>Add child cell to set.</summary>

<param name="child">The child cell to be added to the set.</param>
*/
//-----------------------------------------------------------------------------

	public void AddChild (Cell child) {
	    children.Add (child);
	}
    }

//-----------------------------------------------------------------------------
/**
<summary>Ordinary set element.</summary>
*/
//-----------------------------------------------------------------------------

    public sealed class OrdinarySet : Set {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs an ordinary set with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public OrdinarySet () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs an ordinary set from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public OrdinarySet (Set owner, Dürr.Utils.TokenisedStream cellFile) :
	base (owner, cellFile) {
	}

//-----------------------------------------------------------------------------
/**
<summary>Determine whether this cell type supports joint data.</summary>

<returns>True if the cell type supports dynamic joint and terminal control
frame (TCF) data, false otherwise.</returns>
*/
//-----------------------------------------------------------------------------

	protected override bool CanAcceptJointData () {

/*
Only ordinary sets support joint data.
*/

	    return true;
	}
    }

//-----------------------------------------------------------------------------
/**
<summary>Main set element.</summary>
*/
//-----------------------------------------------------------------------------

    public sealed class MainSet : Set {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a main set with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public MainSet () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a main set from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public MainSet (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {
	}
    }

//-----------------------------------------------------------------------------
/**
<summary>Root set element.</summary>
*/
//-----------------------------------------------------------------------------

    public sealed class RootSet : Set {

/**
<summary>Map of definition names to definitions.</summary>
*/

	private System.Collections.Generic.Dictionary <string, IDefinition>
	definitions;

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a root set with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public RootSet () : base () {

/*
Initialise the definition map for this cell file.
*/

	    definitions = new System.Collections.Generic.Dictionary <string,
	    IDefinition> ();
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a root set from a stream of cell data.</para></summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public RootSet (Set owner, Dürr.Utils.TokenisedStream cellFile) : base
	(owner, cellFile) {
	}

//-----------------------------------------------------------------------------
/**
<summary>Get the main set to which this cell belongs.</summary>

<returns>A reference to the main set to which this cell belongs, or null if it
does not have a main set.</returns>
*/
//-----------------------------------------------------------------------------

	protected override RootSet Root {
	    get {
		return this;
	    }
	}

//-----------------------------------------------------------------------------
/**
<summary>Fetch the definition reference with the specified name.</summary>

<param name="definitionName">The name of the definition to be looked up.
</param>

<returns>A reference to the matching definion, or null if no such definition is
found.</returns>
*/
//-----------------------------------------------------------------------------

	internal IDefinition lookupDefinition (string definitionName) {

/*
Lazy construction.  Putting this in the constructor means that it doesn't get
initialised until AFTER the file has been read (i.e. until after this code
executes) resulting in definitions being null.  Initialise if it is null.
*/

	    if (definitions == null) {
		definitions = new System.Collections.Generic.Dictionary <string,
		IDefinition> ();
	    }

/*
Look for the name within the map and return the matching value (if any).
*/

	    if (definitions.ContainsKey (definitionName)) {
		return definitions [definitionName];
	    }

/*
No such luck!
*/

	    return null;
	}

//-----------------------------------------------------------------------------
/**
<summary>Add a definition to the definition map.</summary>

<param name="definition">Reference to the associated definition.</param>

<returns>True if the name was added or false if the name is already associated
with a definition.</returns>
*/
//-----------------------------------------------------------------------------

	internal bool addDefinition (IDefinition definition) {

/*
If the definition name is already registered, then we may have a problem.
*/

	    if (definitions.ContainsKey (definition.Name)) {

/*
If the associated definition is the same as the current definition, then just
return true - it's already in there.
*/

		if (definitions [definition.Name] == definition) {
		    return true;
		}

/*
Otherwise, we're attempting to use the same name for a second definition.  This
is not allowed and should indicate a warning condition.
*/

		return false;
	    }

/*
Store the definition against the specified name and return true.
*/

	    definitions [definition.Name] = definition;
	    return true;
	}
    }

//-----------------------------------------------------------------------------
/**
<summary>Block Definition element.</summary>
*/
//-----------------------------------------------------------------------------

    public sealed class BlockDefinition : Set, IDefinition {

//-----------------------------------------------------------------------------
/**
<summary>Default constructor.

<para>Constructs a block definition with default settings.</para></summary>
*/
//-----------------------------------------------------------------------------

	public BlockDefinition () : base () {
	}

//-----------------------------------------------------------------------------
/**
<summary>Cell file constructor.

<para>Constructs a block definition from a stream of cell data.</para>
</summary>

<param name="owner">The set that is the owner of this cell element.  If null,
then the element is the root cell element.</param>
<param name="cellFile">The cell data stream from which this cell element is to
be created.</param>
*/
//-----------------------------------------------------------------------------

	public BlockDefinition (RootSet owner, Dürr.Utils.TokenisedStream
	cellFile) : base (owner, cellFile) {
	}
    }
}
