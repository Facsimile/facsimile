/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2006, Michael J Allen BSc.

This program is free software; you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation; either version 2 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program; if not, write to the:

    Free Software Foundation, Inc.
    51 Franklin St, Fifth Floor
    Boston, MA  02110-1301
    USA

The developers welcome all comments, suggestions and offers of assistance.
For further information, please visit the project home page at:

    http://facsimile.sourceforge.net/

Thank you for your interest in the Facsimile project!

IMPORTANT NOTE: All patches (modifications to existing files and/or the
addition of new files) submitted for inclusion as part of the official
Facsimile code base, must comply with the published Facsimile Coding Standards.
If your code fails to comply with the standard, then your patches will be
rejected.  For further information, please visit the coding standards at:

    http://facsimile.sourceforge.net/Documentation/CodingStandards.html
===============================================================================
$Id$

C# source file for the ISuspendable and Suspendable types, and related
elements, that are an integral member of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common {

//=============================================================================
/**
<summary>Delegate for <see cref="System.Common.ISuspendable">ISuspendable</see>
state change event handling functions.</summary>

<remarks>You must provide functions that match this delegate's signature in
order to handle <see cref="System.Common.ISuspendable">ISuspendable</see>
state change events.</remarks>

<param name="sender">Reference to the <see
cref="System.Common.ISuspendable">ISuspendable</see> object whose state has
changed.  Examine this object to determine the new state, if necessary.</param>
*/
//=============================================================================

    public delegate void SuspendableEventHandler (ISuspendable sender);

//=============================================================================
/**
<summary>Suspendable object interface.</summary>

<remarks>Interface for all objects that are capable of being suspended and
resumed.

<para>Objects may be suspended more than once, but are considered active
provided that their suspension level (the difference between the suspension
count and the resumption count) is zero.  This implies that, initially, when
both the suspension and resumption ounts are zero, the initial state of such
objects is to be active.</para>

<para>The ability to suspend objects multiple times is useful when considering
that each suspension can be related to a specific constraint; each time a
contraint becomes applicable, the affected object is suspended once.  When a
constraint is removed, the affected object is resumed once.  The object is only
active when none of the associated constraints apply.</para>

<para>The <see
cref="Facsimile.Common.ISuspendable.Deactivated">Deactivated</see> event is
raised when the object transitions from an active state to an inactive state;
that is, when the object's suspension level changes from 0 to 1.  Similarly,
the <see cref="Facsimile.Common.ISuspendable.Reactivated">Reactivated</see>
event is raised when the object transitions from an inactive state to an active
state; that is, when the object's suspension level changes from 1 to 0.</para>

<para>The recommended method of implementing the suspension level count is to
keep track using a single integer count object (such as a <see
cref="Facsimile.Common.Counter">Counter</see> object).  This count is
incremented when a suspend operation succeeds and is decremented when a resume
operation succeeds.  The suspendable object is then considered active when the
counter is zero, and inactive whenever the counter is positive; it is an error
for the counter to become negative, which implies that it is an error to
attempt a resume operation upon an active object.  It is also recommended that
checks for overflow are performed upon the counter's value to protect against
large imbalances between suspend and resume operations.  It is recommened that
the value of the counter is not made available to users of this library, since
this is an implementation detail and, therefore, potentially subject to
change.</para>

<para>A fully functioning implementation of this interface is defined by the
<see cref="Facsimile.Common.Suspendable">Suspendable</see> class.</para>

<para>Implementations are free to add additional restrictions to the <see
cref="Facsimile.Common.ISuspendable.Suspend">Suspend</see> and <see
cref="Facsimile.Common.ISuspendable.Resume"/>Resume</see> operations.  For
example, an object that is still in the process of being suspended will likely
not be in a valid state for it to be resumed; the suspension operation must be
completed for a resumption is attempted.</para></remarks>
*/
//=============================================================================

    public interface ISuspendable {

/**
<summary>Event signalling that this object has been deactivated.</summary>

<remarks>This event is raised when this object's state transitions from active
to inactive; it is not raised if <see
cref="System.Common.ISuspendable.Suspend">Suspend</see> is called and the
object is already inactive.</remarks>
*/

	event SuspendableEventHandler Deactivated;

/**
<summary>Event signalling that this object has been reactivated.</summary>

<remarks>This event is raised when this object's state transitions from
inactive to active; it is not raised if <see
cref="System.Common.ISuspendable.Resume">Resume</see> is called and the object
remains inactive.</remarks>
*/

	event SuspendableEventHandler Reactivated;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Suspend object.</summary>

<remarks>Increment this object's suspension level.  If the object's suspension
level raises to one, then the object will be deactivated and the <see
cref="Facsimile.Common.ISuspendable.Deactivated">Deactivated</see> event will
be raised; otherwise, the object has already been deactivated and will remain
so.

<para>The effects of calling this function can be undone by a corresponding
call to the <see cref="System.Common.ISuspendable.Resume">Resume</see>
function.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	void Suspend ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Resume object.</summary>

<remarks>Decrement this object's suspension count.  If the object's suspension
counts drops to zero, then the object will be reactivated and the <see
cref="Facsimile.Common.ISuspendable.Reactivated">Reactivated</see> event will
be raised; otherwise, the object will remain deactivated.

<para>An exception should be thrown in the event that this call results in the
suspension count dropping below zero; it is not possible to resume an already
active object.

<para>A call to this function undoes the effects of a corresponding call to
the <see cref="System.Common.ISuspendable.Suspend">Suspend</see>
function.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	void Resume ();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve object's active state.</summary>

<remarks>The object is regarded as being active if its suspension count is zero
and inactive if the suspension count is positive; it is not possible for the
suspension count to become negative.</remarks>

<value>True if the object is active, false if it is not.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	bool IsActive {
	    get;
	}
    }

//=============================================================================
/**
<summary>Default <see cref="Facsimile.Engine.ISuspendable">ISuspendable</see>
interface implementation.</summary>

<remarks>This class provides a full implementation for the <see
cref="Facsimile.Engine.ISuspendable"/>ISuspendable</see> interface - refer to
the documentation for that interface for more information.</remarks>
*/
//=============================================================================

    public abstract class Suspendable: ISuspendable {

/**
<summary>Event signalling that this object has been deactivated.</summary>

<remarks>This event is raised when this object's state transitions from active
to inactive; it is not raised if <see
cref="System.Common.Suspendable.Suspend">Suspend</see> is called and the object
is already inactive.</remarks>
*/

	public event SuspendableEventHandler Deactivated;

/**
<summary>Event signalling that this object has been reactivated.</summary>

<remarks>This event is raised when this object's state transitions from
inactive to active; it is not raised if <see
cref="System.Common.Suspendable.Resume">Resume</see> is called and the object
remains inactive.</remarks>
*/

	public event SuspendableEventHandler Reactivated;

/**
<summary><see cref="Facsimile.Common.Counter">Counter</see> to keep track of
 this object's suspension count.</summary>

<remarks>The counter is always initialised to zero, implying that this object's
initial state is active.</remarks>
*/

	private Counter count;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Default constructor.</summary>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public Suspendable () {
	    count = new Counter ();
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Suspend object.</summary>

<remarks>Increment this object's suspension level.  If the object's suspension
level raises to one, then the object will be deactivated and the <see
cref="Facsimile.Common.Suspendable.Deactivated">Deactivated</see> event will be
raised; otherwise, the object has already been deactivated and will remain so.

<para>The effects of calling this function can be undone by a corresponding
call to the <see cref="System.Common.Suspendable.Resume">Resume</see>
function.</para></remarks>

<exception cref="System.InvalidOperationException">This exception is thrown if
the object is not in a suitable state for a suspend operation to be
performed.</exception>
<exception cref="System.OverflowException">This exception is thrown if the
suspension count exceeds the capacity of the underlying counter storage
type.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void Suspend () {

/*
Confirm whether it is possible to suspend this object.  If not, raise an
exception.
*/

	    if (!CanSuspendInternal || !CanSuspend) {
		throw new System.InvalidOperationException
		(Facsimile.Properties.Resources.E_CannotSuspend);
	    }

/*
If the object is currently active, then this call will deactivate it.  Record
the fact.  We must make this check before incrementing the suspension count.
*/

	    bool deactivated = IsActive;
	    count.Increment ();
	    if (deactivated) {

/*
Allow interested parties to react to the change in state.

TODO: If exceptions thrown here, we could be in the brown smelly stuff.
*/

		OnDeactivatedInternal ();
		OnDeactivated ();
		if (Deactivated != null) {
		    Deactivated (this);
		}
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Resume object.</summary>

<remarks>Decrement this object's suspension count.  If the object's suspension
counts drops to zero, then the object will be reactivated and the <see
cref="Facsimile.Common.Suspendable.Reactivated">Reactivated</see> event will be
raised; otherwise, the object will remain deactivated.

<para>An exception should be thrown in the event that this call results in the
suspension count dropping below zero; it is not possible to resume an already
active object.

<para>A call to this function undoes the effects of a corresponding call to
the <see cref="System.Common.ISuspendable.Suspend">Suspend</see>
function.</para></remarks>

<exception cref="System.InvalidOperationException">This exception is thrown if
the object is not in a suitable state for a resume operation to be
performed.</exception>
<exception cref="System.OverflowException">This exception is thrown if the
suspension count becomes negative; it is not possible to resume an already
active object.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public void Resume () {

/*
Confirm whether it is possible to resume this object.  If not, raise an
exception.
*/

	    if (!CanResumeInternal || !CanResume) {
		throw new System.InvalidOperationException
		(Facsimile.Properties.Resources.E_CannotResume);
	    }

/*
Decrement the suspension count and check if it has been reactivated.
*/

	    count.Decrement ();
	    if (isActive) {

/*
Allow interested parties to react to the change in state.

TODO: If exceptions thrown here, we could be in the brown smelly stuff.
*/

		OnReactivatedInternal ();
		OnReactivated ();
		if (Reactivated != null) {
		    Reactivated (this);
		}
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Determine whether this object is active.</summmary>

<value>True if the object is currently active, false if the object is currently
inactive.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public bool IsActive {
	    get {

/*
If the suspension count is 0/empty, then the object is active - otherwise it is
inactive.
*/
 
		return count.IsEmpty;
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Object suspension property.</summary>

<remarks>Indicates whether a derived object is in a suitable state for a
suspension operation to succeed.  This property should be overridden by
derived classes to include any additionally required suspension checks.

<para>Do not use this property to determine whether a call to <see
cref="Facsimile.Common.Suspendable.Suspend">Suspend</see> will succeed or not;
use the <see
cref="Facsimile.Common.Suspendable.IsSuspendable">IsSuspendable</see> property
instead.</para>

<para>This property should refrain from throwing exceptions and should not
attempt to modify the state of the current object.</para></remarks>

<value>True if the derived object is in a state that allows it to be
suspended; false if it is not.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected virtual bool CanSuspend {
	    get {

/*
This default version of the property merely returns true.
*/

		return true;
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Object resumption property.</summary>

<remarks>Indicates whether a derived object is in a suitable state for a
resumption operation to succeed.  This property should be overridden by
derived classes to include any additionally required resumption checks.

<para>Do not use this property to determine whether a call to <see
cref="Facsimile.Common.Suspendable.Resume">Resume</see> will succeed or
not.  This property is solely intended to be used by the Facsimile library to
guard against attempts to change the state of an object whilst that object is
currently in the process of changing state.</para>

<para>This property should refrain from throwing exceptions and should not
attempt to modify the state of the current object.</para></remarks>

<value>True if the derived object is in a state that allows it to be
suspended; false if it is not.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected virtual bool CanResume {
	    get {

/*
This default version of the property merely returns true.
*/

		return true;
	    }
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Object de-activation method.</summary>

<remarks>Provides derived classes an opportunity to react to this object being
de-activated.  Override this class to implement derived-object specific

<para>This function should avoid throwing exceptions.
</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected virtual void OnDeactivated () {
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Object re-activation method.</summary>

<remarks>Provides derived classes an opportunity to react to this object being
re-activated.

<para>An exception will arise should this function attempt to call either <see
cref="Facsimile.Common.Suspendable.Suspend">Suspend</see> or <see
cref="Facsimile.Common.Suspendable.Resume">Resume</see>; it is not possible to
change the suspendable state of the object whilst that object is attempting to
handle the transition to its new state.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected virtual void OnReactivated () {
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Internal de-activation method.</summary>

<remarks>Provides internal derived classes to an opportunity to react to this
object being de-activated.

<para>It is safer to provide an internal interface for this operation than
sharing the external interface, since we have no control over how external
derived classes implement this method.  If such methods forget, intentionally
or otherwise, to call their base class's version of this method, then they may
break our implementation.  By separating the internal and external interfaces,
we improve our robustness against poor external
implementation.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	internal virtual void OnDeactivatedInternal () {
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Internal re-activation method.</summary>

<remarks>Provides internal derived classes to an opportunity to react to this
object being re-activated.

<para>It is safer to provide an internal interface for this operation than
sharing the external interface, since we have no control over how external
derived classes implement this method.  If such methods forget, intentionally
or otherwise, to call their base class's version of this method, then they may
break our implementation.  By separating the internal and external interfaces,
we improve our robustness against poor external
implementation.</para></remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	internal virtual void OnReactivatedInternal () {
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Internal suspension check.</summary>

<remarks>If the object is in an unsuitable state for the 
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

// TODO: Suspendable should check this - but Event has additional tests.  Need a more generic mechanism for this.
	internal virtual void ConfirmCanSuspendInternal () {
	}
	internal virtual void ConfirmCanResumeInternal () {
	}

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Check that this object is not busy.</summary>

<remarks>This function looks at the busy flag; if it is set, the function
throws an exception.</remarks>

<exception cref="System.InvalidOperationException">This exception is thrown if
the busy flag is set when this function is called.</exception>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void checkNotBusy () {
	    if (busy) {
		throw new System.InvalidOperationException
		(Facsimile.Properties.Resources.E_CannotSuspend);
	    }
	}
    }
}
