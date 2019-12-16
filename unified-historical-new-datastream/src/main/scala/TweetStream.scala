import akka.NotUsed
import akka.stream.scaladsl.Source
import org.joda.time.DateTime

trait TweetStream {
  /**
    * Historical and real-time stream interface.
    * @param user user id
    * @param start beginning timestamp.
    * @param end Optional end time. When defined, a finite stream is returned for data between
    *            start and end. When not, an infinite is returned where historical data
    *            will be returned from start until now and new data will continue to be emitted
    *            as it is available.
    */
  def stream(user: String, start: DateTime, end: Option[DateTime]): Source[Tweet, NotUsed]
}
