//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
//
// This file is part of Facsimile.
//
// Facsimile is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
// version.
//
// Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
// warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
// details.
//
// You should have received a copy of the GNU Lesser General Public License along with Facsimile. If not, see:
//
//   http://www.gnu.org/licenses/lgpl.
//
// The developers welcome all comments, suggestions and offers of assistance. For further information, please visit the
// project home page at:
//
//   http://facsim.org/
//
// Thank you for your interest in the Facsimile project!
//
// IMPORTANT NOTE: All patches (modifications to existing files and/or the addition of new files) submitted for
// inclusion as part of the official Facsimile code base, must comply with the published Facsimile Coding Standards. If
// your code fails to comply with the standard, then your patches will be rejected. For further information, please
// visit the coding standards at:
//
//   http://facsim.org/Documentation/CodingStandards/
//======================================================================================================================
// Scala source file belonging to the org.facsim.sfx.application package.
//======================================================================================================================
package org.facsim.sfx.application

import javafx.application.Application

/**
 * Created by mike on 1/26/2016.
 */
trait SFXApp
extends
JFXInitialization {

  /**
   * Main function.
   * Initialize ''JavaFX'' and create the JavaFX application isntannce. Then execute buffered constructor code on the
   * ''JavaFX event-dispatch thread''.
   *
   * @note This main function will only become active as the application's starting point if this trait is extended as an
   * object.
   *
   * @param args Arguments passed to this application from the command line.
   *
   * @throws IllegalStateException if the application has already been initialized when called. This would typically happen
   * only if this function is called explicitly from user code.
   */
  final def main(args: Array[String]): Unit = Application.launch(classOf[JFXApplication], args: _*)
}