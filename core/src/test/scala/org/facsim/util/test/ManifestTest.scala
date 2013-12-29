/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2013, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU Lesser General Public License as published by the Free
Software Foundation, either version 3 of the License, or (at your option) any
later version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
details.

You should have received a copy of the GNU Lesser General Public License along
with Facsimile. If not, see http://www.gnu.org/licenses/lgpl.

The developers welcome all comments, suggestions and offers of assistance. For
further information, please visit the project home page at:

  http://facsim.org/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected. For further information, please visit the coding standards at:

  http://facsim.org/Documentation/CodingStandards/
===============================================================================
Scala source file from the org.facsim.util.test package.
*/
//=============================================================================

package org.facsim.util.test

import org.facsim.util.Manifest
import org.scalatest.FunSpec

//=============================================================================
/**
Test harness for the [[org.facsim.util.Manifest!]] trait.
*/
//=============================================================================

class ManifestTest extends FunSpec {

/*
Name the class we're testing.
*/

  describe (classOf [Manifest].getCanonicalName) {

/*
Testing is currently not possible.  This is because we can only report
manifests as part of a jar file, and ScalaTest tests run on classes that are
not yet part of a jar file.

Possible solutions: Create a sub-project that contains a Manifest subclass
instance that can be used for testing purposes.
*/

  }
}