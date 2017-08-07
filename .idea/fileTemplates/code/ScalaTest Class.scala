#parse("ScalaHeader.scala")
#if ((${PACKAGE_NAME} && ${PACKAGE_NAME} != ""))package ${PACKAGE_NAME} #end

import org.scalatest.FunSpec
import org.scalatest.prop.PropertyChecks

/** Test harness for the [[???]] class.
 */
class ${NAME}
extends FunSpec
with PropertyChecks