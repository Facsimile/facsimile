package org.facsim.util
import java.io.File
import java.net.URI
import java.util.jar.JarFile
import org.joda.time.DateTime
import scala.collection.JavaConversions._

object Ideas {;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(1929); 
  val manifest = {

    val theClass = classOf [java.io.File]

/*
If there's a simpler way of doing this, feel free to implement it!

This is expressed as a path within a jar file.  It is obtained by retrieving
the name of the package to which this instance belongs, replacing any periods
('.') with slashes ('/'), then prefixing the result with another slash.

For example, if our concrete subclass has the fully-qualified name
`com.mycompany.myproject.MyManifestClass`, then the resulting package path will
be `/com/mycompany/myproject`.

@note It doesn't matter whether the package containing the class is the root
package of the jar file.
*/

    val packagePath = '/' +: theClass.getPackage.getName.map (c => if (c ==
    '.') '/' else c)

    packagePath.toString

/*
Now retrieve the URL for the package path.  If this value is null, indicating
that we do not have an associated jar file, then return None as the manifest
value.
*/

    val jarURL = theClass.getResource (packagePath)
    if (jarURL eq null) None

/*
Otherwise, we have some further processing...
*/

    else {

/*
OK.  So that jarURL will be of the (String) form:

jar:file:/{path-of-jar-file}!{pacakgePath}

In order to create a jar file instance, we need to convert this into a
hierarchical URI.  We do this using a regular expression extraction.  What we
want is just the file:{path-of-jar-file} portion of the URL.
*/

      val jarExtractor = """^jar\:(.+)\!.+$""".r
      val jarURI = jarURL.toString match {
        case jarExtractor (uriString) => new URI (uriString)
        case _ => throw new Error ()
      }

/*
Now obtain a JarFile object from this URI.
*/

      val jarFile = new JarFile (new File (jarURI))
      Option (jarFile.getManifest ())
    }
  };System.out.println("""manifest  : Option[java.util.jar.Manifest] = """ + $show(manifest ));$skip(174); 
  
/**
Entries defined in the manifest.
*/

  val jarEntries = manifest match {
    case None => new java.util.jar.Attributes ()
    case Some (m) => m.getMainAttributes
  };System.out.println("""jarEntries  : java.util.jar.Attributes = """ + $show(jarEntries ));$skip(62); 

  def getField (field: String) = jarEntries.getValue (field);System.out.println("""getField: (field: String)String""");$skip(25); val res$0 = 

  getField ("Built-By");System.out.println("""res0: String = """ + $show(res$0))}
}
