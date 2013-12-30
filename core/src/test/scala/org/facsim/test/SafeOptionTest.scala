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
Scala source file from the org.facsim.test package.
*/
//=============================================================================

package org.facsim.test

import org.facsim.SafeOption
import org.facsim.SafeNone
import org.facsim.SafeSome
import org.scalatest.FunSpec

//=============================================================================
/**
Test suite for the [[org.facsim.SafeOption!]] hierarchy of classes.
*/
//=============================================================================

class SafeOptionTest extends FunSpec with CommonTestMethods {

/**
Test data.
*/

  trait TestData {
    val nullString: String = null
    val nullOption: Option [String] = null
    val nullStringOption = Option (nullString)
    val goodString = "Good"
    val goodElseString = "StillGood"
    val goodStringOption = Option (goodString)
    val goodValue = 1
    val goodElseValue = 2
    val goodValueOption = Option (goodValue)
    assert (goodString != goodElseString)
    assert (goodValue != goodElseValue)
  }

/*
SafeOption Object testing.
*/

  describe (classOf [SafeOption [Any]].getCanonicalName) {

/*
Test apply method.
*/

    describe (".apply [T] (T)") {
      new TestData {
        it ("must return SafeNone if passed a value of null") {
          assert (SafeOption (nullString) === SafeNone)
        }
        it ("must return SafeSome(value) if passed a non-null value") {
          assert (SafeOption (goodString).getClass ===
          classOf [SafeSome [String]])
          assert (SafeOption (goodValue).getClass === classOf [SafeSome [Int]])
        }
      }
    }

/*
Test empty method.
*/

    describe (".empty") {
      it ("must return SafeNone when called") {
        assert (SafeOption.empty == SafeNone)
      }
    }

/*
Test implicit conversion function from a SafeOption to an Option.
*/

    describe (".safeOptionToOption [T] (SafeOption [T])") {
      new TestData {
        it ("must convert SafeNone to None successfully") {
          assert (SafeOption.safeOptionToOption (SafeNone) === None)
        }
        it ("must convert SafeSome values to Some values successfully") {
          assert (SafeOption.safeOptionToOption (SafeOption (goodString)) ===
          Option (goodString))
          assert (SafeOption.safeOptionToOption (SafeOption (goodValue)) ===
          Option (goodValue))
        }
      }
    }

/*
Test implicit conversion function from an Option to a SafeOption.
*/

    describe (".optionToSafeOption [T] (Option [T])") {
      new TestData {
        it ("must convert None to SafeNone successfully") {
          assert (SafeOption.optionToSafeOption (None) === SafeNone)
        }
        it ("must throw an exception if passed a null option value") {
          val e = intercept [NullPointerException] {
            SafeOption.optionToSafeOption (nullOption)
          }
          assertRequireNonNullMsg (e, "x")
        }
        it ("must throw an exception if passed a Some(null) value") {
          val e = intercept [NullPointerException] {
            SafeOption.optionToSafeOption (nullStringOption)
          }
          assertRequireNonNullMsg (e, "x")
        }
        it ("must convert a Some(value) to a SafeSome(value), for value <> " +
        "null") {
          assert (SafeOption.optionToSafeOption (goodStringOption) ===
          SafeOption (goodString))
          assert (SafeOption.optionToSafeOption (goodValueOption) ===
          SafeOption (goodValue))
        }
      }
    }
  }

/*
Test SafeNone object.
*/

  describe (SafeNone.getClass.getCanonicalName) {

/*
Test .isEmpty function.
*/

    describe (".isEmpty") {
      it ("must always return true") {
        assert (SafeNone.isEmpty === true)
      }
    }

/*
Test .isDefined function.
*/

    describe (".isDefined") {
      it ("must always return false") {
        assert (SafeNone.isDefined === false)
      }
    }

/*
Test .get function.
*/

    describe (".get") {
      it ("must always throw a java.util.NoSuchElementException") {
        val e = intercept [NoSuchElementException] {
          SafeNone.get
        }
        assert (e.getMessage === "SafeNone has no value to get")
      }
    }

/*
Test .getOrElse function.
*/

    describe (".getOrElse") {
      new TestData {
        it ("must always return the else condition") {
          assert (SafeNone.getOrElse (goodElseString) === goodElseString)
          assert (SafeNone.getOrElse (goodElseValue) === goodElseValue)
        }
      }
    }

/*
Test .filter function.
*/

    describe (".filter (T => Boolean") {
      new TestData {
        it ("must always return SafeNone") {
          val stringOption = SafeOption (nullString)
          val valueOption = SafeOption.empty
          assert (stringOption.filter (_ == goodString) === SafeNone)
          assert (stringOption.filter (_ != goodString) === SafeNone)
          assert (stringOption.filter (_ == nullString) === SafeNone)
          assert (stringOption.filter (_ != nullString) === SafeNone)
          assert (valueOption.filter (_ == goodValue) === SafeNone)
          assert (valueOption.filter (_ != goodValue) === SafeNone)
        }
      }
    }
  }

/*
Test SafeSome class.
*/

  describe (classOf [SafeSome[Any]].getCanonicalName) {

/*
Test constructor.
*/

    describe (".this [T] (T)") {
      new TestData {
        it ("must throw a NullPointerException if passed null") {
          val e = intercept [NullPointerException] {
            SafeSome (nullString)
          }
          assertRequireNonNullMsg (e, "x")
        }
        it ("must accept valid values") {
          SafeSome (goodString)
          SafeSome (goodValue)
        }
      }
    }

/*
Test .isEmpty function.
*/

    describe (".isEmpty") {
      new TestData {
        it ("must always return false") {
          assert (SafeSome (goodString).isEmpty === false)
          assert (SafeSome (goodValue).isEmpty === false)
        }
      }
    }

/*
Test .isDefined function.
*/

    describe (".isDefined") {
      new TestData {
        it ("must always return true") {
          assert (SafeSome (goodString).isDefined === true)
          assert (SafeSome (goodValue).isDefined === true)
        }
      }
    }

/*
Test .get function.
*/

    describe (".get") {
      new TestData {
        it ("must always return stored value") {
          assert (SafeSome (goodString).get === goodString)
          assert (SafeSome (goodValue).get === goodString)
        }
      }
    }

/*
Test .getOrElse function.
*/

    describe (".getOrElse") {
      new TestData {
        it ("must always return stored value") {
          assert (SafeSome (goodString).getOrElse (goodElseString) ===
          goodString)
          assert (SafeSome (goodValue).getOrElse (goodElseValue) === goodValue)
        }
      }
    }

/*
Test .filter function.
*/

    describe (".filter (T => Boolean") {
      new TestData {
        it ("must apply predicate correctly") {
          val stringOption = SafeOption (goodString)
          val valueOption = SafeOption (goodValue)
          assert (stringOption.filter (_ == goodString) === stringOption)
          assert (stringOption.filter (_ != goodString) === SafeNone)
          assert (valueOption.filter (_ == goodValue) === valueOption)
          assert (valueOption.filter (_ != goodValue) === SafeNone)
        }
      }
    }
  }
}