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

//======================================================================================================================
// Scala source file belonging to the org.facsim.util.log.test package.
//======================================================================================================================
package org.facsim.util.log.test

import org.apache.pekko.stream.scaladsl.{Flow, Keep, Sink}
import org.apache.pekko.stream.QueueOfferResult.{Enqueued, QueueClosed}
import org.apache.pekko.stream.StreamDetachedException
import org.facsim.util.log.*
import org.facsim.util.test.PekkoStreamsTestHarness
import org.facsim.util.LibResource
import org.facsim.util.stream.DataSource
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.*
import scala.util.{Failure, Success}

// Disable test-problematic Scalastyle checkers.

/** Test harness for the [[LogStream]] class.
 */
final class LogStreamTest
extends PekkoStreamsTestHarness, ScalaCheckPropertyChecks:

  /** Timeout for awaiting the result of a future.
   */
  val futureTimeout: FiniteDuration = 5.seconds

  /** Expected illegal argument exception message.
   *
   *  @param bufferSize Size of buffer that caused the error.
   *
   *  @return Expected error message for the invalid buffer size.
   */
  def expectedBufferSizeExceptionMessage(bufferSize: Int): String =
    val errorMsg = LibResource("stream.DataSourceInvalidBufferSize", bufferSize, DataSource.MaxBufferSize)
    s"requirement failed: $errorMsg"

  /** Create a flow for directing a source of log messages into a sequence of log messages.
   *
   *  @note This sink does not run the stream. Typically, the function is used by passing the result of the function to
   *  the .runWith method on a flow.
   *
   *  @return Sink, into which a source of log messages will be consumed and stored as a sequence.
   */
  def seqSink(): Sink[LogMessage[String], Future[Seq[LogMessage[String]]]] =
    Flow[LogMessage[String]].toMat(Sink.seq)(Keep.right)

  // Test the LogStream class.
  describe("org.facsim.util.log.LogStream[A]]"):

    // Verify the constructor fails for invalid buffer sizes.
    describe(".ctor(Int)(ActorMaterializer)"):
      
      // Test invalid construction.
      it("must throw an IllegalArgumentException if called with an invalid buffer size"):
        forAll(invalidBufferSizes): bufferSize =>
          try

            // Attempt to create the log stream with this buffer size: we should get an exception.
            new LogStream[String](bufferSize)

            // If we get this far, we didn't get an exception, so the test failed.
            fail("Log stream with invalid buffer size did not throw an exception")
          catch

            // Check the exception caught.
            case e: Throwable =>

              // Verify that this is an IllegalArgumentException.
              assert(e.getClass === classOf[IllegalArgumentException])

              // Verify that the message is the one expected.
              assert(e.getMessage === expectedBufferSizeExceptionMessage(bufferSize))

      // Test default construction.
      it("must create a valid log stream if using the default buffer size"):
        new LogStream[String]()

      // Test valid construction.
      it("must create a valid log stream if supplied a valid buffer size"):
        forAll(validBufferSizes): bufferSize =>
          new LogStream[String](bufferSize)

    // Test that a source is produced OK.
    describe(".source"):

      // Source must be available for sending upon creation.
      it("must report a valid source"):
        forAll(validBufferSizes): bufferSize =>
          val ds = new LogStream[String](bufferSize)
          assert(ds.source !== null)

    // Test that logged messages are sent OK.
    describe(".log(LogMessage[A])"):

      // Verify that we can send data, and have it show up in a sink.
      it("must send data to an uncompleted stream"):
        forAll(validBufferSizes, logListNonEmpty): (bufferSize, msgs) =>

          // Create the log stream.
          val ds = new LogStream[String](bufferSize)

          // Get the source and add a sink to a sequence.
          val futureData = ds.source.runWith(seqSink())

          // Send all of the log messages.
          val dataFutures = msgs.map(s => ds.log(s))

          // Verify that all of the log messages are sent.
          dataFutures.foreach: df =>
            assert(Await.result(df, futureTimeout) === Enqueued)

          // Close the log.
          val streamCompleted = ds.close()
          Await.ready(streamCompleted, futureTimeout)

          // Verify that the result is the original sequence of log messages.
          assert(Await.result(futureData, futureTimeout) === msgs)

      // Verify that data is rejected after the stream has been completed successfully.
      it("must fail to send data to a completed stream"):
        forAll(validBufferSizes, logs): (bufferSize, msg) =>

          // Create the log stream.
          val ds = new LogStream[String](bufferSize)

          // Get the source and add a sink to a sequence.
          val futureData = ds.source.runWith(seqSink())

          // Close the stream.
          val streamCompleted = ds.close()
          Await.ready(streamCompleted, futureTimeout)

          // Write the data to the stream. It should return a Success(QueueClosed), or a failure containing a
          // StreamDetachedException.
          val df = ds.log(msg)
          Await.ready(df, futureTimeout)
          df.value.get match
            case Success(r) => assert(r === QueueClosed)
            case Failure(e) => assert(e.getClass === classOf[StreamDetachedException])

          // Verify that we didn't receive any data.
          assert(Await.result(futureData, futureTimeout) === Nil)