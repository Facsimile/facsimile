/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

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
<summary>Base class representing a simulation.</summary>

<remarks>In design pattern terms, this class is a "singleton" and fulfills the
"state context" role of the "state" pattern.</remarks>
*/
//=============================================================================

    public class Simulation:
        Facsimile.Common.StateContext <Simulation, SimulationState>
    {

/**
<summary>Reference to the one and only simulation instance.</summary>
*/

        private static Simulation simulation;

/**
<summary>The simulation starting state.</summary>
*/

        private static readonly SimulationStarting starting;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialize static data members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Simulation ()
        {

/*
Ensure that the simulation reference is initially null.
*/

            simulation = null;

/*
Create the simulation starting state.
*/

            starting = new SimulationStarting ();
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>

<remarks>Constructs the basic simulation class.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public Simulation ():
            base (starting)
        {

/*
If the simulation reference is not still null, that is, if a second concurrent
simulation instance has been created, then throw an exception.
*/

            if (simulation != null) {
                // throw some exception, TBD
            }
        }
    }
}
