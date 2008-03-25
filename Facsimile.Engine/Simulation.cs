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

C# source file for the Simulation class, and associated elements, that are
integral members of the Facsimile.Engine namespace.
===============================================================================
*/

namespace Facsimile.Engine
{

//=============================================================================
/**
<summary>Polymorphic singleton class representing a simulation.</summary>

<remarks>This class provides a static, but polymorphic, interface to the
simulation.

<para>In design pattern terms, this class is a "singleton" and fulfills the
"state context" role of the "state" pattern.</para>

<para>To create a customised simulation class, derive your own sub-class, and
decorate it with the the <see cref="AutoInstantiateAttribute" /> attribute.
Refer to <see cref="Singleton {SingletonBase}" /> for further information.
Class members should be accessed via the static functions declared by this
class.</para></remarks>

<seealso cref="Facsimile.Common.Singleton {SingletonBase}" />

<seealso cref="Facsimile.Common.AutoInstantiateAttribute" />
*/
//=============================================================================

    public class Simulation:
        Facsimile.Common.SingletonStateContext <Simulation, SimulationContext,
        SimulationState>
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static Facsimile.Common.Measure <Facsimile.Common.TimeUnit>
        Time
        {
        }
    }
}
