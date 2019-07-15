#parse("ScalaHeader.scala")
#if ((${PACKAGE_NAME} && ${PACKAGE_NAME} != ""))package ${PACKAGE_NAME} #end

import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

// Disable test-problematic Scalastyle checkers.
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers

/** Test harness for the [[???]] class. */
final class ${NAME}
with ScalaCheckPropertyChecks

// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on magic.numbers
//scalastyle:on multiple.string.literals
//scalastyle:on public.methods.have.type
//scalastyle:on scaladoc