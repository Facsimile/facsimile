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

C# source file for the Resource class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Resource information class.</summary>

<remarks>This class provides common resource facilities to the entire Facsimile
library, including locale and internationalization information.

<para>Note: This class, and its members, should only be used from with the
Facsimile library; alas, it is not possible to enforce this.  As such, it is
not part of the official Facsimile public interface and is subject to change
without notice.  Use at your own risk.</para></remarks>
*/
//=============================================================================

    public static class Resource
    {

/**
<summary>The resource manager for the Facsimile library.</summary>
*/

        private readonly static System.Resources.ResourceManager
        resourceManager;

/**
<summary>The culture information for retrieving resources.</summary>

<remarks>This is used to attempt to serve resources to the user in their
preferred language - provided that appropriate translations of the resources
are available.</remarks>
*/

        private readonly static System.Globalization.CultureInfo culture;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static initialization.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static Resource ()
        {

/*
Create the resource manager.

TODO: Compare the impact of preserving a single resource manager instance with
the impact of creating one each time a resource needs to be read.
*/

            resourceManager = new System.Resources.ResourceManager ("Resource",
            System.Reflection.Assembly.GetExecutingAssembly ());
            System.Diagnostics.Debug.Assert (resourceManager != null);

/*
Now retrieve the culture associated with the current thread, so that we can
serve messages in the user's preferred language (if such a translation is
available).
*/

            culture = System.Threading.Thread.CurrentThread.CurrentCulture;
            System.Diagnostics.Debug.Assert (culture != null);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve a non-compound, culture-specific string resource.</summary>

<remarks>Retrieve the specified non-compound string resource from this
assembly's resources.  The resulting string will be in the user's preferred
language - provided that a suitable translation has been provided.

<para>Note: This function should only be called from with the Facsimile
library; alas, it is not possible to enforce this.  As such, it is not part of
the official Facsimile public interface and is subject to change without
notice.  Use at your own risk.</para></remarks>

<param name="resource">A <see cref="System.String" /> reference identifying the
string resource to be retrieved.  This cannot be null or empty.</param>

<returns>A <see cref="System.String" /> reference containing the
culture-specific version of the requested string resouce.  If there is no "best
match" for the user's culture, then a null reference is returned.</returns>

<exception cref="System.ArgumentNullException">Thrown if <paramref
name="resource" /> is null.</exception>

<exception cref="System.InvalidOperationException">Thrown if <paramref
name="resource" /> is not a string resource.</exception>

<exception cref="System.Resources.MissingManifestResourceException">Thrown if
the main assembly does not contain the resources for the neutral culture, and
they are required because the satellite assembly for the user's preferred
culture are not available.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static string Format (string resource)
        {

/*
This method is intended for internal use only.  Consequently, we do not do any
parameter checking - just an assertion.  Use this function at your own risk!
*/

            System.Diagnostics.Debug.Assert (!Util.IsNullOrEmpty (resource));

/*
Retrieve the message in the user's language.

Note: Supplying invalid information may cause this call to fail with an
exception.  We'll allow these to filter through to the caller so that they can
decide what to do.
*/

            System.Diagnostics.Debug.Assert (resourceManager != null);
            System.Diagnostics.Debug.Assert (culture != null);
            string message = resourceManager.GetString (resource, culture);

/*
Perform some diagnostics on this value before returning.
*/

            System.Diagnostics.Debug.Assert (!Util.IsNullOrEmpty (message));
            return message;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve a compound, culture-specific string resource.</summary>

<remarks>Retrieve the specified compound string resource from this assembly's
resources and populates it using the arguments provided.  The resulting string
will be in the user's preferred language - provided that a suitable translation
has been provided.

<para>Note: This function should only be called from with the Facsimile
library; alas, it is not possible to enforce this.  As such, it is not part of
the official Facsimile public interface and is subject to change without
notice.  Use at your own risk.</para></remarks>

<param name="resource">A <see cref="System.String" /> reference identifying the
string resource to be retrieved.  This cannot be null or empty.</param>

<param name="arguments">An array of <see cref="System.Object" /> references
each of which corresponds to an argument encoded within the <paramref
name="resource" /> string resource.  The number and run-time types of these
reference must match the number and types of arguments expected.</param>

<returns>A <see cref="System.String" /> reference containing the
culture-specific version of the requested string resouce, formatted with the
specified arguments.  If there is no "best match" for the user's culture, then
a null reference is returned.</returns>

<exception cref="System.ArgumentNullException">Thrown if <paramref
name="resource" /> or <paramref name="arguments" /> are null.</exception>

<exception cref="System.InvalidOperationException">Thrown if <paramref
name="resource" /> is not a string resource.</exception>

<exception cref="System.Resources.MissingManifestResourceException">Thrown if
the main assembly does not contain the resources for the neutral culture, and
they are required because the satellite assembly for the user's preferred
culture are not available.</exception>

<exception cref="System.FormatException">Thrown if the retrieved string
resource is not a valid format, or if the number of an argument within the
retrieved string resource is less than zero or is greater than or equal to the
length of the <paramref name="arguments" /> array.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static string Format (string resource, System.Object []
        arguments)
        {

/*
This method is intended for internal use only.  Consequently, we do not do any
parameter checking - just assertions.  Use this function at your own risk!
*/

            System.Diagnostics.Debug.Assert (!Util.IsNullOrEmpty (resource));
            System.Diagnostics.Debug.Assert (arguments != null);

/*
Firstly, retrieve the string resource.  This may throw an exception.
*/

            string message = Format (resource);

/*
If we didn't actually retrieve a meaningful string, then return now.
*/

            if (Util.IsNullOrEmpty (message))
            {
                return "";
            }

/*
Now format the string, using the specified cultural information, replacing the
arguments in the string resource with the specified argument values.  This may
throw an exception.
*/

            string formattedMessage = string.Format (culture, message,
            arguments);
            System.Diagnostics.Debug.Assert (formattedMessage != null);
            return formattedMessage;
        }
    }
}
