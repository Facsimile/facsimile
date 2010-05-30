/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2010, Michael J Allen.

This file is part of Facsimile.

Facsimile is free software: you can redistribute it and/or modify it under the
terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

Facsimile is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
Facsimile.  If not, see http://www.gnu.org/licenses/.

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

Java source file belonging to the org.facsim.facsimile.j3d.loaders.cell
package.
*/
//=============================================================================

package org.facsim.facsimile.j3d.loaders.cell;

import javax.media.j3d.Transform3D;
import org.facsim.facsimile.util.PackagePrivate;
import net.jcip.annotations.Immutable;

//=============================================================================
/**
<p>Class supporting geometry data.</p>

<p>This class stores, and handles the processing of, all geometry data
fields.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
final class Geometry
{

/**
<p>Transformation applying the geometry read from the cell scene.</p>
*/

    private final Transform3D transform;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Default constructor.</p>

<p>Constructs a transformation with default cell settings.</p>

<p>The default transformation is 0 translation along each axis, 0 rotation
about each axis and a scale of 1 along each axis.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    Geometry ()
    {
        this.transform = new Transform3D ();
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Cell tokenizer constructor.</p>

<p>Constructs a geometry record from a tokenized stream of cell data, in matrix
or the more common non-matrix form.</p>

<p>
@param tokenizer Tokenized stream from which the geometry data should be read.

@param inMatrixForm If <strong>true</strong>, geometry data in the stream is in
a 4x4 matrix form, and should be processed accordingly; if clear, then the
geometry data is in the more common non-matrix form.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    Geometry (CellTokenizer tokenizer, boolean inMatrixForm)
    {
up to here
/*
If this data is not in matrix form, then read using the standard AutoMod
geometry format.
*/

        if (!inMatrixForm)
        {

/*
Read in the translation data.
*/

            translation.x = tokenizer.n
            translation.X = new Length (cellFile.ReadType <double> ());
            translation.Y = new Length (cellFile.ReadType <double> ());
            translation.Z = new Length (cellFile.ReadType <double> ());

/*
Now for the rotation order.
*/

                rotationOrder = (RotationOrder) cellFile.ReadMax <byte>
                ((byte) RotationOrder.RO_ZYX, "Rotation order");

/*
Now for the rotation values.
*/

                rotation = new double [3];
                for (uint i = 0; i < 3; ++i) {
                    rotation [i] = cellFile.ReadType <double> ();
                }

/*
Now for the scaling values.  Each must be greater than 0.  When reading the
last value, use the newline delimiter.
*/

                scaling = new double [3];
                for (uint i = 0; i < 3; ++i) {
                    if (i == 2) {
                        cellFile.Delimiter = Cell.NewlineDelimiter;
                    }
                    scaling [i] = cellFile.ReadMin (1.0e-20, "Scaling [" + i +
                    "]");
                }
            }

/*
Otherwise, the data is in matrix form.  This is an affine, 4x4 transformation
matrix derived as follows:

    translation, z-axis rotation, x-axis rotation, y-axis rotation, scaling.

The values in the matrix are listed as four lines, each of four values.
*/

            else {

/*
Create the matrix to store this data.
*/

                const ushort dim = 4;
                D�rr.Utils.Matrix geometry = new D�rr.Utils.Matrix (dim, dim);

/*
Now read the data from the file.  When reading the last value, use the newline
delimiter.
*/

                for (ushort i = 1; i <= dim; ++i) {
                    for (ushort j = 1; j <= dim; ++j) {
                        if (i == dim && j == dim) {
                            cellFile.Delimiter = Cell.NewlineDelimiter;
                        }
                        geometry [i, j] = cellFile.ReadType <double> ();
                    }
                }

/*
Now determine the translation, etc. from the data.

TODO: Implement this logic.
*/

            }

/*
Restore the whitespace delimiter.
*/

            cellFile.Delimiter = Cell.WhitespaceDelimiter;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Create an X-axis rotation transformation matrix.</p>

<param name="theta">The angle, measured counter-clockwise (in radians) about
the X-axis.</param>

<returns>A 4x4 affine matrix containing the transformation.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static D�rr.Utils.Matrix XRotationAffine (double theta) {

/*
Create the matrix.
*/

            const ushort dim = 4;
            D�rr.Utils.Matrix result = new D�rr.Utils.Matrix (dim, dim);

/*
Now populate the cells that transform the Y and Z co-ordinates.
*/

            double cosTheta = System.Math.Cos (theta);
            double sinTheta = System.Math.Sin (theta);
            result [1, 1] = 1.0;
            result [2, 2] = cosTheta;
            result [2, 3] = sinTheta;
            result [3, 2] = -sinTheta;
            result [3, 3] = cosTheta;
            result [4, 4] = 1.0;

/*
Return the result.
*/

            return result;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Create a Y-axis rotation transformation matrix.</p>

<param name="theta">The angle, measured counter-clockwise (in radians) about
the Y-axis.</param>

<returns>A 4x4 affine matrix containing the transformation.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static D�rr.Utils.Matrix YRotationAffine (double theta) {

/*
Create the matrix.
*/

            const ushort dim = 4;
            D�rr.Utils.Matrix result = new D�rr.Utils.Matrix (dim, dim);

/*
Now populate the cells that transform the X and Z co-ordinates.
*/

            double cosTheta = System.Math.Cos (theta);
            double sinTheta = System.Math.Sin (theta);
            result [1, 1] = cosTheta;
            result [1, 3] = -sinTheta;
            result [2, 2] = 1.0;
            result [3, 1] = sinTheta;
            result [3, 3] = cosTheta;
            result [4, 4] = 1.0;

/*
Return the result.
*/

            return result;
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Create a Z-axis rotation transformation matrix.</p>

<param name="theta">The angle, measured counter-clockwise (in radians) about
the Z-axis.</param>

<returns>A 4x4 affine matrix containing the transformation.</returns>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static D�rr.Utils.Matrix ZRotationAffine (double theta) {

/*
Create the matrix.
*/

            const ushort dim = 4;
            D�rr.Utils.Matrix result = new D�rr.Utils.Matrix (dim, dim);

/*
Now populate the cells that transform the X and Y co-ordinates.
*/

            double cosTheta = System.Math.Cos (theta);
            double sinTheta = System.Math.Sin (theta);
            result [1, 1] = cosTheta;
            result [1, 2] = -sinTheta;
            result [2, 1] = sinTheta;
            result [2, 2] = cosTheta;
            result [3, 3] = 1.0;
            result [4, 4] = 1.0;

/*
Return the result.
*/

            return result;
        }
    }
}
