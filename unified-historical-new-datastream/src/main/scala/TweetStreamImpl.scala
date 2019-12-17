import akka.NotUsed
import akka.stream.scaladsl.{ Concat, Source }
import org.joda.time.{ DateTime, DateTimeZone }

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

object TweetStreamImpl {
  def apply(pollInterval: FiniteDuration, store: TweetStore): TweetStream = new TweetStreamImpl(pollInterval, store)
}

class TweetStreamImpl(pollInterval: FiniteDuration, store: TweetStore) extends TweetStream {
  def stream(user: String, start: DateTime, end: Option[DateTime]): Source[Tweet, NotUsed] = {
    end match {
      case None =>
        // infinite
        val oldTweets = historical(user, start, utcTimeNow())
        val incomingNewTweets = periodicPoll(user)
        Source.combine(oldTweets, incomingNewTweets)(Concat(_))

      case Some(e) =>
        // finite
        historical(user, start, e)
    }
  }

  private def historical(user: String, start: DateTime, end: DateTime): Source[Tweet, NotUsed] = {
    Source.future(query(user, start, end)).mapConcat(identity)
  }

  // IMPROVEMENT: can be more real-time if transformed into consumer of an event log or push events.
  private def periodicPoll(user: String): Source[Tweet, NotUsed] = {
    Source
      .tick(initialDelay = pollInterval, interval = pollInterval, Unit)
      .statefulMapConcat { () =>
        var bookmark = utcTimeNow()
        _ =>
          {
            val newBookend = utcTimeNow()
            val (start, end) = (bookmark, newBookend)
            bookmark = newBookend
            List(start -> end)
          }
      }
      .mapAsync(parallelism = 1) {
        case (start, end) => query(user, start, end)
      }
      .mapConcat(identity)
      .mapMaterializedValue(_ => NotUsed)
  }

  private def query(user: String, start: DateTime, end: DateTime): Future[List[Tweet]] = {
    store.query(user, start, end)
  }

  private def utcTimeNow(): DateTime = DateTime.now(DateTimeZone.UTC)
}
