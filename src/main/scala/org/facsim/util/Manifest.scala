/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2012, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile.  If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance.  For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.util package.
*/
//=============================================================================

package org.facsim.util

import org.joda.time.DateTime

//=============================================================================
/**
Provide information about the specified package's manifest.

@param package Package reference for which manifest information is requested.
If null, an exception will be thrown.

@throws java.lang.NullPointerException if manifest is null.

@since 0.0
*/
//=============================================================================

abstract class Manifest (manifest: Package) {

/*
If package is null, throw null pointer exception.
*/

  throwIfNull (manifest)

//-----------------------------------------------------------------------------
/**
Auxilliary default constructor, which constructs the manifest for the package
associated with this instance.

@throws NullPointerException if this instance has no associated package.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def this () = this (getClass ().getPackage ())

//-----------------------------------------------------------------------------
/**
Auxilliary package name constructor, which constructs the manifest for the
package with the given name.

@param packageName Name of the package for which manifest information is
sought.

@throws NullPointerException if there is no package with the specified name.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def this (packageName: String) = this (Package.getPackage (packageName))

//-----------------------------------------------------------------------------
/**
Retrieve the build timestamp of this manifest.

This is a custom field that is unavailable for many packages.

@return Date and time that package associated with the manifest was built.

@throws java.util.NoSuchElementException if the manifest has no build
timestamp.
*/
//-----------------------------------------------------------------------------

  final def buildTimestamp = {
    val timestampString = Option (manifest.)
  }
//-----------------------------------------------------------------------------
/**
Title of this application or library.

@return Manifest implementation title.

@throws java.util.NoSuchElementException if the manifest has no title field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def title = Option (manifest.getImplementationTitle ()).get

//-----------------------------------------------------------------------------
/**
Vendor publishing this application or library.

This may be an individual or an organization, depending upon circumstances.

@return Manifest implementation vendor.

@throws java.util.NoSuchElementException if the manifest has no vendor field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def vendor = Option (manifest.getImplementationVendor ()).get

//-----------------------------------------------------------------------------
/**
Version of this release of this application or library.

@return Manifest implementation version.

@throws java.util.NoSuchElementException if the manifest has no version field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def version = Option (manifest.getImplementationVersion ()).get

//-----------------------------------------------------------------------------
/**
Title of the specification to which this application or library conforms.

@return Manifest specification title.

@throws org.facsim.inf.MissingManifestDataException if
the manifest specification title field is undefined.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def specTitle = Option (manifest.getSpecificationTitle ()).get

//-----------------------------------------------------------------------------
/**
Vendor that produced the specification to which this application or library
conforms.

This may be an individual or an organization, depending upon circumstances.

@return Manifest specification vendor.

@throws java.util.NoSuchElementException if the manifest has no specification
vendor field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def specVendor = Option (manifest.getSpecificationVendor ()).get

//-----------------------------------------------------------------------------
/**
Version of the specification to which this release of the application or
library conforms.

@return Manifest specification version.

@throws java.util.NoSuchElementException if the manifest has no specification
version field.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def specVersion = Option (manifest.getSpecificationVersion ()).get
}
