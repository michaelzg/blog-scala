import java.util.UUID
import java.util.concurrent.TimeUnit

import akka.actor.Scheduler
import org.joda.time.{ DateTime, DateTimeZone, Interval }

import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future }

class StubAutoRefreshingTweetStore(users: List[String], referenceTime: DateTime, scheduler: Scheduler)(
    implicit ec: ExecutionContext)
    extends TweetStore {

  private val oneSecond = Duration(1, TimeUnit.SECONDS)

  private var data: Map[String, List[Tweet]] =
    users.map(user => user -> initializeTweets(user)).toMap

  scheduler.scheduleAtFixedRate(initialDelay = oneSecond, interval = oneSecond)(new Runnable() {
    override def run(): Unit = users.foreach(newTweet)
  })

  def query(user: String, start: DateTime, end: DateTime): Future[List[Tweet]] = {
    val interval = new Interval(start, end)
    val result = data
      .get(user)
      .map { tweets =>
        tweets.filter(t => interval.contains(t.timestamp))
      }
      .getOrElse(List.empty)
    Future.successful(result)
  }

  private def initializeTweets(user: String): List[Tweet] = {
    val random = scala.util.Random
    val yesterday = referenceTime.minusDays(1)
    (1 to 5).map { i =>
      Tweet(
        id = UUID.randomUUID(),
        user = user,
        timestamp = yesterday.plusHours(i),
        message = s"Hello World! Tweet number $i",
        likes = random.nextInt(100),
        retweets = random.nextInt(50))
    }.toList
  }

  private def newTweet(user: String): Unit = {
    val random = scala.util.Random
    data = data + (user -> {
        data(user) :+ Tweet(
          id = UUID.randomUUID(),
          user = user,
          timestamp = utcTimeNow(),
          message = s"I'm frantically live tweeting!",
          likes = random.nextInt(10),
          retweets = random.nextInt(5))
      })
  }

  private def utcTimeNow(): DateTime = DateTime.now(DateTimeZone.UTC)
}
