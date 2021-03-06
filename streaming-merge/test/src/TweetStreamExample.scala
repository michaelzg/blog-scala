import akka.actor.ActorSystem
import akka.stream.scaladsl.Sink
import org.joda.time.{ DateTime, DateTimeZone }
import org.scalatest.freespec.AsyncFreeSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.duration._

class TweetStreamExample extends AsyncFreeSpec with Matchers {
  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher
  val scheduler = system.scheduler
  val referenceTime = utcTimeNow()
  val users = List("bruce", "wayne")

  "Finite historical stream" - {
    val tweetSream = {
      val tweetStore = new StubAutoRefreshingTweetStore(users, referenceTime, scheduler)
      TweetStreamImpl(pollInterval = 1.second, store = tweetStore)
    }
    "should return 5 historical tweets from the past" in {
      println("Historical")
      tweetSream
        .stream("bruce", start = referenceTime.minusDays(2), end = Some(referenceTime))
        .map(println)
        .runWith(Sink.seq)
        .map(_.size shouldBe 5)
    }
  }

  "Infinite stream with new tweets" - {
    val tweetStream = {
      val tweetStore = new StubAutoRefreshingTweetStore(users, referenceTime, scheduler)
      TweetStreamImpl(pollInterval = 1.second, store = tweetStore)
    }

    val maxTweets = 10
    s"should return an stream of live tweets as they come in (up to $maxTweets)" in {
      println("Historical + new data stream")
      tweetStream
        .stream("wayne", start = referenceTime.minusDays(2), end = None)
        .take(maxTweets)
        .map(println)
        .runWith(Sink.seq)
        .map(_.size shouldBe maxTweets)
    }
  }

  private def utcTimeNow(): DateTime = DateTime.now(DateTimeZone.UTC)
}
