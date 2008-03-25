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

C# source file for the ISuspendable interface, and associated elements, that
are integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Interface for suspendable objects.</summary>

<remarks>An object that is suspendable is one that can be suspended and
resumed.</remarks>
*/
//=============================================================================

    public interface ISuspendable
    {

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Increment object's suspend count.</summary>

<remarks>This function increments the current object's suspend count.  If the
suspend count is currently zero, then calling this function results in the
object's state changing from active to suspended; for suspend counts higher
than zero, the object remains suspended following the call to this function.

<para>An exception may be thrown if the object is currently in a state that
does not support suspension of the object.</para>

<para>To resume a suspended object, one call to <see cref="Resume ()" /> must
be made for each corresponding call to this function.</para></remarks>

<exception cref="System.InvalidOperationException">Thrown if the object is
currently in a state that does not support suspension of the
object.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        void Suspend ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Decrement object's suspend count.</summary>

<remarks>This function decrements the current object's suspend count.  If the
suspend count is currently zero, then calling this function results in an
exception; the suspend count can never be less than zero.  If the suspend count
is currently one, then calling this function results in the object's state
changing from suspended to active; for suspend counts higher than 1, the object
remains suspended following the call to this function.

<para>An exception may be thrown if the object is currently in a state that
does not support suspension of the object.</para>

<para>To resume a suspended object, one call to <see cref="Resume ()" /> must
be made for each corresponding call to this function.</para></remarks>

<exception cref="System.InvalidOperationException">Thrown if the object is not
currently suspended.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        void Resume ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Property identifying whether object is suspended.</summary>

<remarks>This property should be true if the object's suspend count (the
difference between the number of successful calls to <see cref="Suspend ()" />
and <See cref="Resume ()" /> is greater than zero, or false if the suspend
count is zero.

<para>This property should not throw any exceptions.</para></remarks>

<value>A <see cref="System.Boolean" /> that is true if the object is currently
suspended and false otherwise.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        bool IsSuspended
        {
            get;
        }
    }
}
