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

C# source file for the SimulationAttribute class, and associated elements, that
are integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Attribute identifying a user-defined simulation class.</summary>

<remarks>In order to provide a custom simulation class, a user needs to both
create a custom simulation class (derived from <see cref="Simulation" />) and
flag it with this attribute.  No more than a single class should have this
attribute assigned to it.

<para>During simulation start-up, the Facsimile library searches for a class
decorated with this attribute.  If it finds such a class, and that class is
derived from <see cref="Simulation" />, and that class has a default
constructor, then an instance of that class will be
created as the primary simulation controller.  If no such class is found, then
an instance of <see cref="Simulation" /> will be created as the primary
simulation controller.</para></remarks>
*/
//=============================================================================

    [System.AttributeUsage (System.AttributeTargets.Class, AllowMultiple =
    false, Inherited = false)]
    public class SimulationAttribute:
        System.Attribute
    {
    }
}
