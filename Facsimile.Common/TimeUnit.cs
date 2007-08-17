/*
Facsimile -- A Discrete-Event Simulation Library
Copyright © 2004-2007, Michael J Allen.

This program is free software: you can redistribute it and/or modify it under
the terms of the GNU General Public License as published by the Free Software
Foundation, either version 3 of the License, or (at your option) any later
version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
PARTICULAR PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with
this program.  If not, see <http://www.gnu.org/licenses/>.

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

C# source file for the TimeUnit class, and associated elements, that are
integral members of the Facsimile.Common namespace.
===============================================================================
*/

namespace Facsimile.Common
{

//=============================================================================
/**
<summary>Represents the family of time measurement units.</summary>

<remarks>There will be a single instance of this class for each supported
measurement unit, such as "milliseconds", "seconds", "minutes", "hours",
"days", "weeks", etc.

<para>The SI standard time unit is the second.  Time values cannot be
negative.</para>

<para>Certain time measurement units, including "month", "year", "decade",
"century" and "millennium" cannot be supported as there is no standard
conversion to the standard units (as a consequence of leap-years and the
varying number of days per month, etc.).</para>

<para>This class is intended for measuring time; it is not intended for use as
a date-time class; use <see cref="System.DateTime" /> for such
requirements.</para></remarks>
*/
//=============================================================================

    public sealed class TimeUnit:
        NonNegativeMeasurementUnit
    {

/**
<summary>Second time measurement unit.</summary>
*/

        private static readonly TimeUnit seconds;

/**
<summary>Minute time measurement unit.</summary>
*/

        private static readonly TimeUnit minutes;

/**
<summary>Hour time measurement unit.</summary>
*/

        private static readonly TimeUnit hours;

/**
<summary>Day time measurement unit.</summary>
*/

        private static readonly TimeUnit days;

/**
<summary>Week time measurement unit.</summary>
*/

        private static readonly TimeUnit weeks;

/**
<summary>Millisecond time measurement unit.</summary>
*/

        private static readonly TimeUnit milliseconds;

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Static constructor.</summary>

<remarks>Initialise static members.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        static TimeUnit ()
        {

/*
Create the standard time unit - seconds.
*/

            System.Diagnostics.Debug.Assert (seconds == null);
            seconds = new TimeUnit ();

/*
Create each of the non-standard time measurement units.
*/

            double secondsPerMinute = 60.0;
            System.Diagnostics.Debug.Assert (minutes == null);
            minutes = new TimeUnit (secondsPerMinute);

            double secondsPerHour = 60.0 * secondsPerMinute;
            System.Diagnostics.Debug.Assert (hours == null);
            hours = new TimeUnit (secondsPerHour);

            double secondsPerDay = 24.0 * secondsPerHour;
            System.Diagnostics.Debug.Assert (days == null);
            days = new TimeUnit (secondsPerDay);

            double secondsPerWeek = 7.0 * secondsPerDay;
            System.Diagnostics.Debug.Assert (weeks == null);
            weeks = new TimeUnit (secondsPerWeek);

            double secondsPerMillisecond = 1.0 / 1000.0;
            System.Diagnostics.Debug.Assert (milliseconds == null);
            milliseconds = new TimeUnit (secondsPerMillisecond);
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the seconds time unit.</summary>

<value>A <see cref="TimeUnit" /> instance representing the chosen time
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TimeUnit Seconds
        {
            get
            {
                return seconds;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the minutes time unit.</summary>

<value>A <see cref="TimeUnit" /> instance representing the chosen time
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TimeUnit Minutes
        {
            get
            {
                return minutes;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the hours time unit.</summary>

<value>A <see cref="TimeUnit" /> instance representing the chosen time
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TimeUnit Hours
        {
            get
            {
                return hours;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the days time unit.</summary>

<value>A <see cref="TimeUnit" /> instance representing the chosen time
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TimeUnit Days
        {
            get
            {
                return days;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the weeks time unit.</summary>

<value>A <see cref="TimeUnit" /> instance representing the chosen time
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TimeUnit Weeks
        {
            get
            {
                return weeks;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Retrieve the milliseconds time unit.</summary>

<value>A <see cref="TimeUnit" /> instance representing the chosen time
units.</value>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        public static TimeUnit Milliseconds
        {
            get
            {
                return milliseconds;
            }
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Standard time unit constructor.</summary>

<remarks>This constructor is used to create that standard time units.  Any
non-negative double value (times cannot be negative) is a valid standard time
measurement.</remarks>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private TimeUnit ():
            base ()
        {
        }

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
<summary>Non-standard time unit constructor.</summary>

<remarks>This constructor is used to create non-standard time units.</remarks>

<param name="unitScaleFactor">A <see cref="System.Double" /> defining the
number of standard units corresponding to a single unit of these units.  This
value must be positive.</param>
*/
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        private TimeUnit (double unitScaleFactor):
            base (unitScaleFactor, 0.0)
        {
        }
    }
}
