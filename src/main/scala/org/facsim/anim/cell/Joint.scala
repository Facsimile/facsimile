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
Scala source file from the org.facsim.anim.cell package.
*/
//=============================================================================

package org.facsim.anim.cell

import scala.Math.abs
import com.sun.j3d.loaders.ParsingErrorException
import org.facsim.io.TextReader

//=============================================================================
/**
Kinematic joint.

''AutoMod'' cell primitives can contain either, none or both of the folllowing:
 -  Dynamic joint information.  A dynamic joint can be either rotational or
    translational.
 -  Terminal joint information.  A terminal joint is a placeholder to which the
    origin of other graphic objects can be attached.
*/
//=============================================================================

private [cell] abstract class Joint (data: TextReader) {

/**
Determine the type of the joint.
*/

  private val jointType = processJointType ()

/**
Determine the joint's speed.
*/

  private val jointVelocity = processVelocity ()

/**
Determine the joint's minimum value.
*/

  private val jointMinimum = processMinimum ()

/**
Determine the joint's maximum value.
*/

  private val jointMaximum = processMaximum ()

/**
Determine the joint's current value.
*/

  private val jointCurrent = processCurrent ()

/**
Determine whether this is a ''terminal control frame'' (TCF).
*/

  private val isTCF = processCurrent ()

//-----------------------------------------------------------------------------
/**
Determine a cell's joint type.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Cell joint type.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if a joint type has a value
outside of the permitted range.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processJointType () = {

/*
Read in the joint type from the file.
*/

    val jointType = data.readInt ()

/*
If the joint type code is invalid, throw an exception.
*/

    if (!Joint.JointType.isValidCode (jointType)) throw new
    ParsingErrorException ("Invalid joint type value (line: " +
    data.getFieldRow ().toString () + ", col: " + data.getFieldColumn
    ().toString () + "): " + jointType.toString ())
    jointType
  }

//-----------------------------------------------------------------------------
/**
Determine this joint's velocity.

The velocity must be 0 if this is a TCF-only joint, or non-negative otherwise.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Joint velocity.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if an invalid velocity is
specified.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processVelocity () = {

/*
Read the velocity from the data stream.
*/

    val velocity = data.readDouble ()
    jointType match {

/*
If this is a TCF-only joint, then verify that the velocity is 0.
*/

      case Joint.JointType.TcfOnly => if (abs (velocity) > 1.0e-10) throw new
      ParsingErrorException ("TCF-only joint velocity must be zero (line: " +
      data.getFieldRow ().toString () + ", col: " + data.getFieldColumn
      ().toString () + "): " + velocity.toString ())

/*
Otherwise, the velocity cannot be negative.
*/

      case _ => if (velocity < 0.0) throw new ParsingErrorException
      ("Joint velocity cannot be positive (line: " + data.getFieldRow
      ().toString () + ", col: " + data.getFieldColumn ().toString () + "): " +
      velocity.toString ())
    }
    velocity
  }

//-----------------------------------------------------------------------------
/**
Determine this joint's minimum joint value.

The minimum must be 0 if this is a TCF-only joint, or any other value
otherwise.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Joint minimum.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if an invalid minimum is
specified.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processMinimum () = {

/*
Read the minimum from the data stream.
*/

    val minimum = data.readDouble ()
    jointType match {

/*
If this is a TCF-only joint, then verify that the minimum is 0.
*/

      case Joint.JointType.TcfOnly => if (abs (minimum) > 1.0e-10) throw new
      ParsingErrorException ("TCF-only joint minimum must be zero (line: " +
      data.getFieldRow ().toString () + ", col: " + data.getFieldColumn
      ().toString () + "): " + minimum.toString ())

/*
Otherwise, any value OK.
*/

      case _ => // OK
    }
    minimum
  }

//-----------------------------------------------------------------------------
/**
Determine this joint's maximum joint value.

The maximum must be 0 if this is a TCF-only joint, or not less than the minimum
otherwise.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Joint maximum.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if an invalid maximum is
specified.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processMaximum () = {

/*
Read the maximum from the data stream.
*/

    val maximum = data.readDouble ()
    jointType match {

/*
If this is a TCF-only joint, then verify that the maximum is 0.
*/

      case Joint.JointType.TcfOnly => if (abs (maximum) > 1.0e-10) throw new
      ParsingErrorException ("TCF-only joint maximum must be zero (line: " +
      data.getFieldRow ().toString () + ", col: " + data.getFieldColumn
      ().toString () + "): " + maximum.toString ())

/*
Otherwise, the maximum must not be less than the minimum.
*/

      case _ => if (maximum < jointMinimum) throw new ParsingErrorException
      ("Joint maximum must be >= joint minimum (minimum: " +
      jointMinimum.toString () + ", line: " + data.getFieldRow ().toString () +
      ", col: " + data.getFieldColumn ().toString () + "): " + maximum.toString
      ())
    }
    maximum
  }

//-----------------------------------------------------------------------------
/**
Determine this joint's current joint value.

The current value must be 0 if this is a TCF-only joint, or within the range
defined by the minimum and maximum values otherwise.

@note This function must be called in the correct sequence for reading from the
cell file, otherwise exceptions will occur.
 
@return Joint current value.

@throws java.lang.NumberFormatException if processed fields cannot be converted
to the required type.

@throws com.sun.j3d.loaders.ParsingErrorException if an invalid current value
is specified.

@since 0.0
*/
//-----------------------------------------------------------------------------

  private final def processCurrent () = {

/*
Read the current value from the data stream.
*/

    val current = data.readDouble ()
    jointType match {

/*
If this is a TCF-only joint, then verify that the current value is 0.
*/

      case Joint.JointType.TcfOnly => if (abs (current) > 1.0e-10) throw new
      ParsingErrorException ("TCF-only joint current value must be zero " +
      "(line: " + data.getFieldRow ().toString () + ", col: " +
      data.getFieldColumn ().toString () + "): " + current.toString ())

/*
Otherwise, the current value must be within the range [minimum, maximum].
*/

      case _ => if (current < jointMinimum || current > jointMaximum) throw new
      ParsingErrorException ("Current joint value must be >= joint minimum " +
      "and <= joint maximum (minimum: " + jointMinimum.toString () +
      ", maximum: " + jointMaximum.toString () + ", line: " + data.getFieldRow
      ().toString () + ", col: " + data.getFieldColumn ().toString () + "): " +
      current.toString ())
    }
    current
  }
}

//=============================================================================
/**
Joint companion object.
*/
//=============================================================================

private object Joint {

/**
Joint type enumeration.
*/

  private object JointType extends Enumeration {
    val TcfOnly = Value
    val Rotational = Value
    val Translational = Value
    private val minCode = TcfOnly.id
    private val maxCode = Translational.id
    def isValidCode (code: Int) = (code >= minCode && code <= maxCode)
  }
}