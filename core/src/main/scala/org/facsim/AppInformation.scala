/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2014, Michael J Allen.

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
Scala source file from the org.facsim package.
*/
//=============================================================================

package org.facsim

import java.time.ZonedDateTime
import javafx.scene.image.Image
import org.facsim.util.{Version, toDate}

//=============================================================================
/**
Ensures a common interface for providing application information.

An implementation of this interface must either be defined, or mixed in, when
creating instances of [[org.facsim.App]].

@since 0.0
*/
//=============================================================================

trait AppInformation {

//-----------------------------------------------------------------------------
/**
Report the executing application's official title.

@return Executing application's title.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def title: String

//-----------------------------------------------------------------------------
/**
Report name of the organization responsible for developing the executing
application.

@return Executing application's owning organization name.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def organization: String

//-----------------------------------------------------------------------------
/**
Report date upon which development of the executing application commenced.

@return Executing application's commencement date.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def inceptionDate: ZonedDateTime

//-----------------------------------------------------------------------------
/**
Report release date of this version of the executing application.

@return Release date of executing application's current version.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def releaseDate: ZonedDateTime

//-----------------------------------------------------------------------------
/**
Report the executing application's copyright message.

@note The copyright message is built from the [[.inceptionDate]] and
[[.releaseDate]] properties. If these properaties are unavailable, then an
exception will result.

@return Executing application's copyright message.

@throws java.util.NoSuchElementException if either the [[.inceptionDate]] or
[[.releaseDate]] properties are unavailable.

@since 0.0
*/
//-----------------------------------------------------------------------------

  final def copyright = {

/*
If the organization name ends in a period, then remove it.
*/

    val org = organization
    val orgAdj = if (org.last == '.') org.init else org

/*
Format and retrieve this application's copyright string.

What the hell is going on with date & time in Java? The java.text.MessageFormat
class (employed by LibResource) does not recognize anything but java.util.Date
or java.lang.Number (milliseconds from 1st Jan 1970) objects. But there's no
conversion from the new date & time classes.
*/

    if (inceptionDate.getYear < releaseDate.getYear) {
      LibResource ("AppInformation.CopyrightRange", orgAdj,
      toDate (inceptionDate), toDate (releaseDate))
    }
    else {
      LibResource ("AppInformation.CopyrightBasic", orgAdj,
      toDate (inceptionDate))
    }
  }

//-----------------------------------------------------------------------------
/**
Report executing application's version.

@return Executing application's version.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def version: Version

//-----------------------------------------------------------------------------
/**
Provide a set of icons for the executing application.

By default, the icons for the ''Facsimile'' project are provided. To customize
the application icons, this function should be overridden and a custom icon set
returned instead. If an empty set of icons are provided, then the application
will use the default ''JavaFX'' application icon set.

For highest quality icon rendering, a number of different size icons (measured
in ''pixels'') should be returned. Note that not all sizes are required for all
platforms, but it is recommended that all of the following sizes are provided:

 - 16x16
 - 32x32
 - 48x48
 - 64x64
 - 128x128
 - 256x256
 - 512x512
 - 1024x1024

The most appropriate size will be selected selected automatically. All icon
sizes should contain the same image (although some simplification may be
required for smaller icon sizes).

@note This function will only be called if the application is running as a
''GUI'' application.

@return A list of images to be utilized as application icons.

@since 0.0
*/
//-----------------------------------------------------------------------------

  def icons = List {
    new Image ("file:Images/FacsimileIcon_16x16.png")
    new Image ("file:Images/FacsimileIcon_32x32.png")
    new Image ("file:Images/FacsimileIcon_48x48.png")
    new Image ("file:Images/FacsimileIcon_64x64.png")
    new Image ("file:Images/FacsimileIcon_128x128.png")
    new Image ("file:Images/FacsimileIcon_256x256.png")
    new Image ("file:Images/FacsimileIcon_512x512.png")
    new Image ("file:Images/FacsimileIcon_1024x1024.png")
  }
}