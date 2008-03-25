/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2008, Michael J Allen.

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program.  If not, see <http://www.gnu.org/licenses/>.

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

    http://www.facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

    http://www.facsim.org/Documentation/CodingStandards/
===============================================================================
$Id$

C# source file for the Reflection class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Provide reflection data to the library.</summary>

<remarks>This class provides a simple-to-use interface to reflection data about
the currently application and all referenced assemblies.

<para>All attribute instances that decorate the current application's primary
assembly, as well as all referenced assemblies (except those provided within
third-party assemblies, such as those in namespace "System", "NUnit") are
stored and indexed for quick retrieval.  This represents a fair bit of run-time
and memory overhead, but there is apparently no alternative if you wish to,
say, find all classes decorated with a specific attribute.</para>

<para>This class is implemented as a "Singleton".  Refer to Gamma, et al:
"Design Patterns: Elements of Reusable Object-Oriented Software",
Addison-Wesley, for further information.  Note that as it is a non-polymorphic
singleton, it does not need to be derived from <see cref="Singleton
{SingletonBase}" />.</para></remarks>
*/
//=============================================================================

    public static class Reflection:
        System.Object
    {

/**
<summary>Attribute to type map.</summary>

<remarks>Each element of the map is a list of of the types to which a specific
attribute type has been assigned.  This makes it relatively easy to determine
all of the type decorated with a specific attribute.</remarks>
*/

        private static System.Collections.Generic.Dictionary <System.Type,
        System.Collections.Generic.List <System.Type>> attributeTypeMap;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor</summary>

<remarks>Initialise the static data members.

<para>This routine is responsible for building and populating the indices to
the current application's attributes.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Reflection ()
        {

/*
Initialise the attribute type map.
*/

            attributeTypeMap = new System.Collections.Generic.Dictionary
            <System.Type, System.Collections.Generic.List <System.Type>> ();

/*
Go through each of the assemblies in the current domain and process it.
*/

            foreach (System.Reflection.Assembly assembly in
            System.AppDomain.CurrentDomain.GetAssemblies ())
            {
                ProcessAssembly (assembly);
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Process each assembly belonging to the current application.</summary>

<param name="assembly">The <see cref="System.Reflection.Assembly" /> instance
to be processed.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private static void ProcessAssembly (System.Reflection.Assembly
        assembly)
        {

/*
Get the types defined by this assembly and go through each in turn.
*/

            foreach (System.Type type in assembly.GetTypes ())
            {

/*
Note the following code could be generalized, using a System.MemberInfo
argument for the type data (since System.Type is derived from
System.MemberInfo) - with the exception of the map used - should it become
necessary to examine attributes on type members, arguments, etc.
*/

/*
Go through each custom attribute on this type.
*/

                foreach (object attribute in type.GetCustomAttributes (true))
                {

/*
Get the type of the attribute.
*/

                    System.Type attributeType = attribute.GetType ();
                    System.Diagnostics.Debug.Assert (attributeType.IsSubclassOf
                    (typeof (System.Attribute)));

/*
If this attribute is not already in the map, then initialise its list of types.
*/

                    if (!attributeTypeMap.ContainsKey (attributeType))
                    {
                        attributeTypeMap [attributeType] = new
                        System.Collections.Generic.List <System.Type> ();
                    }

/*
Add the type to the attribute's list.  (Attributes that are assigned multiple
times will have multiple entries of the same decorated type in their list.)
*/

                    attributeTypeMap [attributeType].Add (type);
                }
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the list of types with specified attribute.</summary>

<remarks>This function retrieves a list of types (classes, structs, interfaces,
etc.) that have been decorated with a specific attribute.</remarks>

<param name="attribute">A <see cref="System.Type" /> representing the type of
the attribute for which a list of associated types is requested.</param>

<returns>A <see cref="System.Collections.Generic.List {Type}" /> list of types
associated with the specified <paramref name="attribute" />.  If no types are
found, then an empty list will be returned.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static System.Collections.Generic.List <System.Type>
        GetTypesWithAttribute (System.Type attribute)
        {

/*
If the indicated type is not in the map, then return a new empty list.
*/

            if (!attributeTypeMap.ContainsKey (attribute))
            {
                return new System.Collections.Generic.List <System.Type> ();
            }

/*
Otherwise, return the associated list.
*/

            return attributeTypeMap [attribute];
        }
    }
}
