//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2020, Michael J Allen.
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
// Scala source file belonging to the org.facsim.util.stream.test package.
//======================================================================================================================
package org.facsim.util.stream.test

import akka.stream.scaladsl.{Flow, Keep, Sink}
import akka.stream.QueueOfferResult.{Enqueued, QueueClosed}
import akka.stream.StreamDetachedException
import org.facsim.util.stream.DataSource
import org.facsim.util.test.{AkkaStreamsTestHarness, Generator}
import org.facsim.util.test.implicits._
import org.facsim.util.LibResource
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

// Disable test-problematic Scalastyle checkers.
//scalastyle:off scaladoc
//scalastyle:off public.methods.have.type
//scalastyle:off multiple.string.literals
//scalastyle:off magic.numbers

/** Test harness for the [[org.facsim.util.stream.DataSource]] class. */
final class DataSourceTest
extends AkkaStreamsTestHarness
with ScalaCheckPropertyChecks {

  /** Timeout for awaiting the result of a future. */
  val futureTimeout: FiniteDuration = 5.seconds

  /** Expected illegal argument exception message.
   *
   *  @param bufferSize Size of buffer that caused the error.
   *
   *  @return Expected error message for the invalid buffer size.
   */
  def expectedBufferSizeExceptionMessage(bufferSize: Int): String = {
    val errorMsg = LibResource("stream.DataSourceInvalidBufferSize", bufferSize, DataSource.MaxBufferSize)
    s"requirement failed: $errorMsg"
  }

  /** Create a flow for directing a source of strings into a sequence of strings.
   *
   *  @note This sink does not run the stream. Typically, the function is used by passing the result of the function to
   *  the .runWith method on a flow.
   *
   *  @return Sink, into which a source of strings will be consumed and stored as a sequence.
   */
  def seqSink(): Sink[String, Future[Seq[String]]] = Flow[String].toMat(Sink.seq)(Keep.right)

  // Test the DataSource class.
  describe("org.facsim.util.stream.DataSource[A]]") {

    // Verify the constructor fails for invalid buffer sizes.
    describe(".ctor(Int)(ActorMaterializer)") {

      // Test invalid construction.
      it("must throw an IllegalArgumentException if called with an invalid buffer size") {
        forAll(invalidBufferSizes) {bufferSize =>
          try {

            // Attempt to create the data source with this buffer size: we should get an exception.
            new DataSource[String](bufferSize)

            // If we get this far, we didn't get an exception, so the test failed.
            fail("Data source with invalid buffer size did not throw an exception")
          }
          catch {

            // Check the exception caught.
            case e: Throwable => {

              // Verify that this is an IllegalArgumentException.
              assert(e.getClass === classOf[IllegalArgumentException])

              // Verify that the message is the one expected.
              assert(e.getMessage === expectedBufferSizeExceptionMessage(bufferSize))
            }
          }
        }
      }

      // Test valid construction.
      it("must create a valid data source if supplied a valid buffer size") {
        forAll(validBufferSizes) {bufferSize =>
          new DataSource[String](bufferSize)
        }
      }
    }

    // Test that a source is produced OK.
    describe(".source") {

      // Source must be available for sending upon creation.
      it("must report a valid source") {
        forAll(validBufferSizes) {bufferSize =>
          val ds = new DataSource[String](bufferSize)
          assert(ds.source !== null) //scalastyle:ignore null
        }
      }
    }

    // Test that sent data is received OK.
    describe(".send(A)") {

      // Verify that we can send data, and have it show up in a sink.
      it("must send data to an uncompleted stream") {
        forAll(validBufferSizes, Generator.unicodeStringListNonEmpty) {(bufferSize, data) =>

          // Create the data source.
          val ds = new DataSource[String](bufferSize)

          // Get the source and add a sink to a sequence.
          val futureData = ds.source.runWith(seqSink())

          // Send all of the data.
          val dataFutures = data.map(s => ds.send(s))

          // Verify that all of the data is sent.
          dataFutures.foreach {df =>
            assert(Await.result(df, futureTimeout) === Enqueued)
          }

          // Complete the stream.
          val streamCompleted = ds.complete()
          Await.ready(streamCompleted, futureTimeout)

          // Verify that the result is the original sequence of data.
          assert(Await.result(futureData, futureTimeout) === data)
        }
      }

      // Verify that data is rejected after the stream has been completed successfully.
      it("must fail to send data to a completed stream") {
        forAll(validBufferSizes, Generator.unicodeString) {(bufferSize, data) =>

          // Create the data source.
          val ds = new DataSource[String](bufferSize)

          // Get the source and add a sink to a sequence.
          val futureData = ds.source.runWith(seqSink())

          // Complete the stream.
          val streamCompleted = ds.complete()
          Await.ready(streamCompleted, futureTimeout)

          // Write the data to the stream. It should return a Success(QueueClosed), or a failure containing a
          // StreamDetachedException.
          val df = ds.send(data)
          Await.ready(df, futureTimeout)
          df.value.get match {
            case Success(r) => assert(r === QueueClosed)
            case Failure(e) => assert(e.getClass === classOf[StreamDetachedException])
          }

          // Verify that we didn't receive any data.
          assert(Await.result(futureData, futureTimeout) === Nil)
        }
      }

      // Verify that data is rejected after the stream has been completed unsuccessfully.
      it("must fail to send data to a failed stream") {
        forAll(validBufferSizes, Generator.unicodeString) {(bufferSize, data) =>

          // Create the data source.
          val ds = new DataSource[String](bufferSize)

          // Get the source and add a sink to a sequence.
          val futureData = ds.source.runWith(seqSink())

          // Complete the stream, using an exception.
          val failureException = new RuntimeException("Some test exception")
          val streamCompleted = ds.fail(failureException)
          Await.ready(streamCompleted, futureTimeout)

          // Write the data to the stream. It should return a Success(QueueClosed), or a failure containing either the
          // exception used to close the queue, or a StreamDetachedException.
          val df = ds.send(data)
          Await.ready(df, futureTimeout)
          df.value.get match {
            case Success(r) => assert(r === QueueClosed)
            case Failure(e) => if(e != failureException) assert(e.getClass === classOf[StreamDetachedException])
          }

          // Verify that the source completes with the exception passed as the failure.Util
          Await.ready(futureData, futureTimeout)
          assert(futureData.value.get === Failure(failureException))
        }
      }
    }
  }
}

// Re-enable test-problematic Scalastyle checkers.
//scalastyle:on magic.numbers
//scalastyle:on multiple.string.literals
//scalastyle:on public.methods.have.type
//scalastyle:on scaladoc