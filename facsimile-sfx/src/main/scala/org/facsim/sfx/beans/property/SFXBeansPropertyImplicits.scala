//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2023, Michael J Allen.
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
// Scala source file belonging to the org.facsim.sfx.beans.property package.
//======================================================================================================================
package org.facsim.sfx.beans.property

import javafx.beans.property.{Property, ReadOnlyProperty}
import javafx.beans.value.ObservableValue

/** Rich wrappers for [[javafx.beans.property]] elements.
 *
 *  @since 0.0
 */
object SFXBeansPropertyImplicits {

  /** Rich wrapper for [[javafx.beans.property.Property]] interface instances.
   *
   *  @tparam A Type of property.
   *
   *  @param p _JavaFX_ property being wrapped. This value cannot be `null`.
   *
   *  @see [[javafx.beans.property.Property]] for further more information.
   *
   *  @since 0.0
   */
  implicit final class RichProperty[A](private val p: Property[A])
  extends AnyVal {

    /** Bind this property to the specified observable value.
     *
     *  @note When `ov` changes value, the value of this property will be updated to match.
     *
     *  @tparam B Type of observable value being bound, which can be a subtype of A.
     *
     *  @param ov Observable value to which this property is to be bound. This cannot be `null`, nor can a property be
     *  bound to itself.
     *
     *  @throws scala.NullPointerException if `ov` is `null`.
     *
     *  @throws scala.IllegalArgumentException if `ov` is `p`.
     *
     *  @see [[javafx.beans.property.Property#bind]] for further details about this operation.
     *
     *  @since 0.0
     */
    def <==[B <: A](ov: ObservableValue[B]): Unit = p.bind(ov)

    /** Bi-directionally bind this property to the specified property.
     *
     *  @note When either property changes value, the other property will be updated to have the same value.
     *  Consequently, both properties need to be of the same type.
     *
     *  @param op Property to which this property will be bound, bi-directionally. This cannot be `null`, nor can a
     *  property be bound to itself.
     *
     *  @throws scala.NullPointerException if `op` is `null`.
     *
     *  @throws scala.IllegalArgumentException if `op` is `p`.
     *
     *  @see [[javafx.beans.property.Property#bindBidirectional]] for further details about this operation.
     *
     *  @since 0.0
     */
    def <==>(op: Property[A]): Unit = p.bindBidirectional(op)

    /** Unbind this property from the specified property.
     *
     *  Removes a previously created bi-directional binding between the two properties.
     *
     *  @note If the two properties are not currently bound bi-directionally, then this operation has no effect.
     *
     *  @param op Property from which this property will be unbound, bi-directionally. This cannot be `null`, nor can a
     *  property be unbound from itself.
     *
     *  @throws scala.NullPointerException if `op` is `null`.
     *
     *  @throws scala.IllegalArgumentException if `op` is `p`.
     *
     *  @see [[javafx.beans.property.Property#unbindBidirectional]] for further details about this operation.
     *
     *  @since 0.0
     */
    def >==<(op: Property[A]): Unit = p.unbindBidirectional(op)
  }

  /** Rich wrapper for [[javafx.beans.property.ReadOnlyProperty]] interface instances.
   *
   *  @tparam A Type of read-only property.
   *
   *  @param rop _JavaFX_ read-only property being wrapped.
   *
   *  @since 0.0
   */
  implicit final class RichReadOnlyProperty[A](private val rop: ReadOnlyProperty[A])
  extends AnyVal {

    /** Obtain optional _bean_ instance that contains this property.
     *
     *  @return _Bean_ that contains this property, wrapped in [[scala.Some]], or [[scala.None]] if the property does
     *  not belong to a bean.
     */
    def bean: Option[AnyRef] = Option(rop.getBean)

    /** Obtain the optional name of this property.
     *
     *  @note In _JavaFX_, properties with no names use empty strings; by using [[scala.Option]], we're applying a
     *  more functional approach that treats missing names differently.
     *
     *  @return Name of this property, wrapped in [[scala.Some]], or [[scala.None]] if the property has no name.
     */
    def name: Option[String] = {

      // Retrieve the property name and verify that the name is not null. This will result in an NPE if this requirement
      // fails (which it should not).
      val n = rop.getName
      assert(n ne null)

      // If the name is an empty string, then return None, otherwise return the string wrapped in Some.
      if(n.isEmpty) None
      else Some(n)
    }
  }
}