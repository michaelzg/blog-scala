import org.joda.time.DateTime

import scala.concurrent.Future

trait TweetStore {
  def query(user: String, start: DateTime, end: DateTime): Future[List[Tweet]]
}
