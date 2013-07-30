/*
Miscellaneous test worksheet.  Do not commit to source repository!
*/

import scalafx.geometry.Point3D
import scalafx.scene.transform.Rotate
import scalafx.scene.transform.Translate

object TestingTestingTesting {
  val translation = new Translate (0.0, 0.0, 0.0) //> translation  : scalafx.scene.transform.Translate = [SFX]Translate [x=0.0, y=
                                                  //| 0.0, z=0.0]
  translation.isIdentity                          //> res0: Boolean = true
  Rotate.XAxis                                    //> res1: scalafx.geometry.Point3D = [SFX]Point3D [x = 1.0, y = 0.0, z = 0.0]
  Rotate.YAxis                                    //> res2: scalafx.geometry.Point3D = [SFX]Point3D [x = 0.0, y = 1.0, z = 0.0]
  Rotate.ZAxis                                    //> res3: scalafx.geometry.Point3D = [SFX]Point3D [x = 0.0, y = 0.0, z = 1.0]
}