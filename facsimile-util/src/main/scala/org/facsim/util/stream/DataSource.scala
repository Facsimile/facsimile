//======================================================================================================================
// Facsimile: A Discrete-Event Simulation Library
// Copyright © 2004-2019, Michael J Allen.
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
// Scala source file belonging to the org.facsim.util.stream package.
//======================================================================================================================
package org.facsim.util.stream

import akka.stream.{Materializer, OverflowStrategy, QueueOfferResult}
import akka.stream.scaladsl.Source
import akka.{Done, NotUsed}
import org.facsim.util.{LibResource, NonPure}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.reflect.runtime.universe.TypeTag

/** Create a new ''Akka Streams'' data source, to which data can be sent on demand.
 *
 *  @note This class does not provide ''pure'' functions; return values from functions with the same arguments may
 *  return different values depending upon their internal state. However, since the class is used for sending data to
 *  data stream consumers, should should not affect program behavior significantly.
 *
 *  @tparam A Type of data to be sent to this stream.
 *
 *  @constructor Create a new data stream.
 *
 *  @param bufferSize Number of unprocessed data elements that can be stored in the buffer before back pressure is
 *  exerted. This value must be greater than zero and than `[[DataSource.MaxBufferSize MaxBufferSize]]`, or an
 *  `[[scala.IllegalArgumentException IllegalArgumentException]]` will be thrown.
 *
 *  @param materializer Stream materializer to be utilized when creating the stream.
 *
 *  @throws IllegalArgumentException if `bufferSize` is less than 1 or greater than `[[DataSource.MaxBufferSize
 *  MaxBufferSize]]`.
 *
 *  @since 0.2
 */
final class DataSource[A: TypeTag](bufferSize: Int)(implicit materializer: Materializer) {

  // Sanity check.
  require(bufferSize > 0 && bufferSize <= DataSource.MaxBufferSize,
  LibResource("stream.DataSourceInvalidBufferSize", bufferSize, DataSource.MaxBufferSize))

  /** Queue stream and source requested. */
  private val streamSource = Source.queue[A](bufferSize, OverflowStrategy.backpressure).preMaterialize()

  /** Future resulting from the previous send operation.
   *
   *  @note Each send operation's future must complete before attempting to send further data. In essence, this is how
   *  backpressure is implemented. It should be noted that when the future completes, the data has not necessarily been
   *  sent, just that the data has been queued for sending.
   */
  private var lastSendFuture: Future[QueueOfferResult] = {  //scalastyle:ignore var.field
    Future.successful(QueueOfferResult.Enqueued)
  }
  assert(lastSendFuture.isCompleted, "Initial last send future has not completed.")

  /** Send data to the stream.
   *
   *  Data will not flow through the stream unless the stream is materialized and run.
   *
   *  @param data Data to be sent to the stream.
   *
   *  @return Future containing the result of the data queuing operation. If successful, the result can be
   *  `[[akka.stream.QueueOfferResult.Enqueued Enqueued]]` if data was sent successfully,
   *  `[[akka.stream.QueueOfferResult.Dropped Dropped]]` if the data was dropped due to a buffer failure, or
   *  `[[akka.stream.QueueOfferResult.QueueClosed QueueClosed]]` if the queue was closed before the data could be
   *  processed. If the queue was closed before the data was sent, the result is a `[[scala.util.Failure Failure]]`
   *  wrapping an `[[akka.stream.StreamDetachedException StreamDetachedException]]`. If a failure closed the queue, it
   *  will respond with a `Failure` wrapping the exception that was passed to the `[[fail]]` method.
   *
   *  @since 0.2
   */
  @NonPure
  def send(data: A): Future[QueueOfferResult] = synchronized {

    // If the previous future did not complete, then wait for it to do so. If the future has already completed, then no
    // time should elapse.
    //
    // Note: This can lock the current thread, should nothing be consuming the messages
    Await.result(lastSendFuture, Duration.Inf)
    assert(lastSendFuture.isCompleted, "Last data future did not complete.")

    // Send the data, receiving a new future in the process.
    lastSendFuture = streamSource._1.offer(data)

    // Return the future for the sent data.
    lastSendFuture
  }

  /** Report the source to which flows and sinks can be attached.
   *
   *  @return Source for streamed data. This must be connected to a sink, and run, in order to for data to be processed.
   *
   *  @since 0.2
   */
  def source: Source[A, NotUsed] = streamSource._2

  /** Signal successful completion of the data stream.
   *
   *  @note Once the data stream has been completed, no further data can be sent to it.
   *
   *  @return Future that completes when the stream has finished this operation.
   *
   *  @since 0.2
   */
  @NonPure
  def complete(): Future[Done] = {

    // Stream concerned.
    val logStream = streamSource._1

    // Successfully complete the stream.
    logStream.complete()

    // Report a future that can be used to determine when the stream has completed.
    logStream.watchCompletion()
  }

  /** Signal a failure, which also results in completion of the data stream.
   *
   *  @param e Exception identifying the cause of the data stream failure.
   *
   *  @return Future that completes when the stream has finished this operation.
   *
   *  @since 0.2
   */
  @NonPure
  def fail(e: Throwable): Future[Done] = {

    // Stream concerned.
    val logStream = streamSource._1

    // Complete the stream with the indicated failure.
    logStream.fail(e)

    // Report a future that can be used to determine when the stream has completed.
    logStream.watchCompletion()
  }
}

/** Data source companion object.
 *
 *  @since 0.2
 */
object DataSource {

  /** Maximum number of unprocessed data items that can be buffered before back-pressure is exerted.
   *
   *  @since 0.2
   */
  val MaxBufferSize: Int = 4096
}