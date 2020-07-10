import java.util.UUID

import org.joda.time.DateTime

final case class Tweet(id: UUID, user: String, timestamp: DateTime, message: String, likes: Int, retweets: Int) {
  override def toString: String = {
    s"$timestamp - User $user tweeted '$message' [Likes: $likes, Retweets: $retweets]"
  }
}
