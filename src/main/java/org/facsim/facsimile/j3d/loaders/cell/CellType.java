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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import net.jcip.annotations.Immutable;
import org.facsim.facsimile.util.PackagePrivate;
import com.sun.j3d.loaders.IncorrectFormatException;
import com.sun.j3d.loaders.ParsingErrorException;

//=============================================================================
/**
<p><em>Cell</em> primitive type enumerator.</p>
*/
//=============================================================================

@Immutable
@PackagePrivate
enum CellType
{

/**
<p><em>Triad</em> type code.</p>
*/

    CT_TRIAD ((short) 100, Triad.class),

/**
<p><em>Vector list</em> type code.</p>
*/

    CT_VECTOR_LIST ((short) 115, VectorList.class),

/**
<p><em>Polyhedron</em> type code.</p>
*/

    CT_POLYHEDRON ((short) 125, Polyhedron.class),

/**
<p><em>Arc</em> (coarse) type code.</p>
*/

    CT_ARC_COARSE ((short) 130, Arc.class),

/**
<p><em>Arc</em> (fine) type code.</p>
*/

    CT_ARC_FINE ((short) 131, Arc.class),

/**
<p><em>Text</em> (world) type code.</p>
*/

    CT_TEXT_WORLD ((short) 140, Text.class),

/**
<p><em>Text</em> (screen fast) type code.</p>
*/

    CT_TEXT_SCREEN_FAST ((short) 141, Text.class),

/**
<p><em>Text</em> (screen normal) type code.</p>
*/

    CT_TEXT_SCREEN_NORMAL ((short) 142, Text.class),

/**
<p><em>Text</em> (unrotated fast) type code.</p>
*/

    CT_TEXT_UNROTATE_FAST ((short) 143, Text.class),

/**
<p><em>Text</em> (unrotated normal) type code.</p>
*/

    CT_TEXT_UNROTATE_NORMAL ((short) 144, Text.class),

/**
<p><em>Text list</em> (world) type code.</p>
*/

    CT_TEXTLIST_WORLD ((short) 150, TextList.class),

/**
<p><em>Text list</em> (screen fast) type code.</p>
*/

    CT_TEXTLIST_SCREEN_FAST ((short) 151, TextList.class),

/**
<p><em>Text list</em> (screen normal) type code.</p>
*/

    CT_TEXTLIST_SCREEN_NORMAL ((short) 152, TextList.class),

/**
<p><em>Text list</em> (unrotated fast) type code.</p>
*/

    CT_TEXTLIST_UNROTATE_FAST ((short) 153, TextList.class),

/**
<p><em>Test list</em> (unrotated normal) type code.</p>
*/

    CT_TEXTLIST_UNROTATE_NORMAL ((short) 154, TextList.class),

/**
<p><em>Block definition</em> type code.</p>
*/

    CT_BLOCK_DEFINITION ((short) 308, Definition.class),

/**
<p><em>Trapezoid</em> type code.</p>
*/

    CT_TRAPEZOID ((short) 310, Trapezoid.class),

/**
<p><em>Tetrahedron</em> type code.</p>
*/

    CT_TETRAHEDRON ((short) 311, Tetrahedron.class),

/**
<p><em>Rectangle</em> type code.</p>
*/

    CT_RECTANGLE ((short) 315, Rectangle.class),

/**
<p><em>Hemisphere</em> (coarse) type code.</p>
*/

    CT_HEMISPHERE_COARSE ((short) 330, Hemisphere.class),

/**
<p><em>Hemisphere</em> (fine) type code.</p>
*/

    CT_HEMISPHERE_FINE ((short) 331, Hemisphere.class),

/**
<p><em>Cone</em> (coarse) type code.</p>
*/

    CT_CONE_COARSE ((short) 340, Cone.class),

/**
<p><em>Cone</em> (fine) type code.</p>
*/

    CT_CONE_FINE ((short) 341, Cone.class),

/**
<p><em>Cylinder</em> (coarse) type code.</p>
*/

    CT_CYLINDER_COARSE ((short) 350, Cylinder.class),

/**
<p><em>Cylinder</em> (fine) type code.</p>
*/

    CT_CYLINDER_FINE ((short) 351, Cylinder.class),

/**
<p><em>Frustum</em> (coarse) type code.</p>
*/

    CT_FRUSTUM_COARSE ((short) 360, Frustum.class),

/**
<p><em>Frustum</em> (fine) type code.</p>
*/

    CT_FRUSTUM_FINE ((short) 361, Frustum.class),

/**
<p><em>File reference</em> type code.</p>
*/

    CT_FILE_REFERENCE ((short) 388, FileReference.class),

/**
<p><em>Instance</em> type code.</p>
*/

    CT_INSTANCE ((short) 408, Instance.class),

/**
<p><em>Compiled picture</em> type code.</p>
*/

    CT_COMPILED_PICTURE ((short) 555, CompiledPicture.class),

/**
<p><em>Embedded file</em> type code.</p>
*/

    CT_EMBEDDED_FILE ((short) 599, EmbeddedFile.class),

/**
<p><em>Set</em> type code.</p>
*/

    CT_SET ((short) 700, Set.class),

/**
<p><em>Main set</em> type code.</p>
*/

    CT_MAIN_SET ((short) 7000, Set.class),

/**
<p><em>Root set</em> type code.</p>
*/

    CT_ROOT_SET ((short) 10000, Set.class);

/**
<p>Hash map associating each cell type code with the associated enumerated cell
type constant.</p>

<p>This map must be populated during static initialization of the enumeration,
since the (static) enumerations are created before the static initializer is
invoked.</p>
*/

    private final static HashMap <Short, CellType> typeMap;

/**
<p>Cell primitive type code.</p>
*/

    private final short code;

/**
<p>Constructor associated with each <em>cell</em> type.</p>

<p>This constructor is used to create an instance of the corresponding cell
primitive upon demand.</p>
*/

    private final Constructor <? extends Cell> ctor;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Static initialization.</p>

<p>Note that static initialization is performed <em>after</em> each enumeration
constant has been constructed.</p>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    static
    {

/*
Create the hash map that allows each type enumeration to be looked up from the
corresponding type code.
*/

        typeMap = new HashMap <Short, CellType> (CellType.values ().length);

/*
Populate the map from the corresponding codes.
*/

        for (CellType cellType: CellType.values ())
        {
            assert !typeMap.containsKey (Short.valueOf (cellType.code));
            typeMap.put (Short.valueOf (cellType.code), cellType);
        }
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve CellType instance from a tokenized cell data stream.<p>

@param tokenizer Tokenized stream from which the cell type code will be read.
The next token to be read should be a cell type code.

@return CellType instance read from the stream.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em> type code.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em> type code.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    static CellType readType (CellTokenizer tokenizer)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Read the cell type code from the tokenized stream.
*/

        short cellTypeCode = tokenizer.readShortField ();

/*
Now convert the code into a cell type.  It if is not a code we recognize, then
that would be a parse error.
*/

        Short typeCode = Short.valueOf (cellTypeCode);
        if (!typeMap.containsKey (typeCode))
        {
            throw new ParsingErrorException ();
        }

/*
Retrieve and return the matching value.
*/

        assert typeMap.get (typeCode) != null;
        return typeMap.get (typeCode);
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Enumeration constant constructor.</p>

@param <T> {@linkplain Cell} sub-class representing the corresponding cell
primitive.

@param code The corresponding code for each enumeration constant. 

@param cellPrimitiveClass Class type representing the corresponding
<em>cell</em> primitive (which must be a sub-class of {@linkplain Cell}.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private <T extends Cell> CellType (short code, Class <T>
    cellPrimitiveClass)
    {

/*
Store the type code.
*/

        assert code > 0;
        this.code = code;

/*
Determine the constructor that takes a Cell argument and a Tokenizer stream.
Verifying that it is present for this class and store it for later retrieval.
*/

        Constructor <? extends Cell> classCtor;
        try
        {
            classCtor = (Constructor <? extends Cell>)
            cellPrimitiveClass.getConstructor (CellScene.class,
            CellTokenizer.class, BaseSet.class);
        }

/*
If there is no such constructor, then we somehow forgot to define it for the
corresponding cell primitive class.  This is an error.

NOTE: Since this code should never execute, we should not see it arise even
during testing.  Consequently, this code is not marked as covered by the test
coverage analyzer, preventing 100% test coverage compliance.
*/

        catch (NoSuchMethodException e)
        {
            classCtor = null;
            e.printStackTrace ();
            System.exit (1);
        }

/*
A security exception indicates that the constructor is not accessible here,
which is an error (such as the constructor being declared private instead of
package-private or public).

NOTE: Since this code should never execute, we should not see it arise even
during testing.  Consequently, this code is not marked as covered by the test
coverage analyzer, preventing 100% test coverage compliance.

*/

        catch (SecurityException e)
        {
            classCtor = null;
            e.printStackTrace ();
            System.exit (1);
        }

/*
Initialize the constructor.  By the time we get to here, the code should be OK.
*/

        assert classCtor != null;
        this.ctor = classCtor;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Factory method to create a new instance of the corresponding <em>cell</em>
primitive.</p>

@param scene Scene being populated.

@param tokenizer Tokenized stream from which the <em>cell</em> primitive, and
its contents, will be read.

@param parent <em>Cell</em> primitive's parent in the scene hierarchy.

@return The newly created primitive.

@throws IncorrectFormatException If the tokenizer does not identify a valid
<em>cell</em>.

@throws ParsingErrorException If an error occurs while parsing the tokenized
<em>cell</em>.
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final Cell cellFactory (CellScene scene, CellTokenizer tokenizer, BaseSet
    parent)
    throws IncorrectFormatException, ParsingErrorException
    {

/*
Create and return the cell from from the tokenizer.  An exception will result
if the cell cannot be read successfully.
*/

        assert this.ctor != null;
        try
        {
            return this.ctor.newInstance (scene, tokenizer, parent);
        }

/*
Handle a situation in which the constructor threw an exception.  Re-throw the
original, wrapped exception - provided that it matches this function's
checked exception list.
*/

        catch (InvocationTargetException wrapperException)
        {

/*
Retrieve the exception that was thrown by the constructor.
*/

            Throwable wrappedException = wrapperException.getCause ();
            assert wrappedException != null;

/*
If the exception thrown by the constructor was an IncorrectFormatException,
then convert to that exception type and re-throw.
*/

            if (wrappedException instanceof IncorrectFormatException)
            {
                throw (IncorrectFormatException) wrappedException;
            }

/*
Otherwise, if the exception thrown was a ParsingErrorException, then convert it
to that exception type and re-throw.
*/

            if (wrappedException instanceof ParsingErrorException)
            {
                throw (ParsingErrorException) wrappedException;
            }

/*
Otherwise, if the exception thrown is a run-time exception, then re-throw it as
such.  RuntimeExceptions are unchecked, and so this will not violate this
function's checked exception list.
*/

            if (wrappedException instanceof RuntimeException)
            {
                throw (RuntimeException) wrappedException;
            }

/*
Finally, if the exception thrown is an error, then re-throw it as such.  Errors
are unchecked, and so this will not violate this function's checked exception
list.
*/

            if (wrappedException instanceof Error)
            {
                throw (Error) wrappedException;
            }

/*
If we've made it this far, then the constructor threw an unexpected checked
exception.

This code should never execute.  If it does, then check that the list of
checked exceptions for the associated constructor are limited to those
supported by this function.

NOTE: Since this code should never execute, we should not see it arise even
during testing.  Consequently, this code is not marked as covered by the test
coverage analyzer, preventing 100% test coverage compliance.
*/

            wrappedException.printStackTrace ();
            System.exit (1);
        }

/*
Handle condition indicating that the arguments passed to the referenced cell
primitive's constructor were rejected as invalid by the constructor.

This exception should never occur.  If it does occur, then check that the
arguments passed to the constructor are valid, and check the argument
validation code in the constructor is valid also.

NOTE: Since this exception should never occur, we should not see it arise even
during testing.  Consequently, this code is not marked as covered by the test
coverage analyzer, preventing 100% test coverage compliance.
*/

        catch (IllegalArgumentException e)
        {
            e.printStackTrace ();
            System.exit (1);
        }

/*
Handle condition indicating that the referenced cell primitive constructor
belongs to an object that cannot be instantiated.

This exception should never occur.  If it does occur, then check that the
declaration of the associated cell primitive class is not abstract, is a class
not an interface, etc. 

NOTE: Since this exception should never occur, we should not see it arise even
during testing.  Consequently, this code is not marked as covered by the test
coverage analyzer, preventing 100% test coverage compliance.
*/

        catch (InstantiationException e)
        {
            e.printStackTrace ();
            System.exit (1);
        }

/*
Handle condition indicating that the referenced cell primitive constructor is
not accessible from this function.

This exception should never occur.  If it does occur, then check that the
declaration of the associated cell primitive constructor is not private. 

NOTE: Since this exception should never occur, we should not see it arise even
during testing.  Consequently, this code is not marked as covered by the test
coverage analyzer, preventing 100% test coverage compliance.
*/

        catch (IllegalAccessException e)
        {
            e.printStackTrace ();
            System.exit (1);
        }

/*
If we've made it this far, then we've somehow managed to get past all of the
checks in place above.

This code should never execute.  I can't think how or why we might get here,
short of a compiler bug.  If you reach this point in a debugging exercise,
please update this comment with your experiences.

NOTE: Since this code should never execute, we should not see it arise even
during testing.  Consequently, this code is not marked as covered by the test
coverage analyzer, preventing 100% test coverage compliance.

The "return null" statement is required to prevent the Java compiler
complaining about this function not returning a value.
*/

        System.exit (1);
        return null;
    }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<p>Retrieve primitive code for corresponding enumeration.</p>

@return The integer code for this primitive.
 */
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @PackagePrivate
    final short getCode ()
    {
        return this.code;
    }
}
