# Unified Historical and New Datastreams
Example of a unified historical and polling-new-data query pattern with Akka Sreams.

Future improvements include improving the polling pattern with a real-time consumer so
real-time data is delivered to the client faster than the polling interval.


## Run Tests

```
mill streaming-merge.test
```

And you should expect to see an output like the following.

```
Historical
2019-12-15T06:31:36.729Z - User bruce tweeted 'Hello World! Tweet number 1' [Likes: 54, Retweets: 49]
2019-12-15T07:31:36.729Z - User bruce tweeted 'Hello World! Tweet number 2' [Likes: 64, Retweets: 41]
2019-12-15T08:31:36.729Z - User bruce tweeted 'Hello World! Tweet number 3' [Likes: 56, Retweets: 20]
2019-12-15T09:31:36.729Z - User bruce tweeted 'Hello World! Tweet number 4' [Likes: 8, Retweets: 40]
2019-12-15T10:31:36.729Z - User bruce tweeted 'Hello World! Tweet number 5' [Likes: 49, Retweets: 8]
Historical + new data stream
2019-12-15T06:31:36.729Z - User wayne tweeted 'Hello World! Tweet number 1' [Likes: 40, Retweets: 0]
2019-12-15T07:31:36.729Z - User wayne tweeted 'Hello World! Tweet number 2' [Likes: 60, Retweets: 42]
2019-12-15T08:31:36.729Z - User wayne tweeted 'Hello World! Tweet number 3' [Likes: 28, Retweets: 35]
2019-12-15T09:31:36.729Z - User wayne tweeted 'Hello World! Tweet number 4' [Likes: 16, Retweets: 36]
2019-12-15T10:31:36.729Z - User wayne tweeted 'Hello World! Tweet number 5' [Likes: 65, Retweets: 48]
2019-12-16T05:31:37.786Z - User wayne tweeted 'I'm frantically live tweeting!' [Likes: 0, Retweets: 0]
2019-12-16T05:31:38.786Z - User wayne tweeted 'I'm frantically live tweeting!' [Likes: 0, Retweets: 4]
2019-12-16T05:31:39.786Z - User wayne tweeted 'I'm frantically live tweeting!' [Likes: 4, Retweets: 4]
2019-12-16T05:31:40.786Z - User wayne tweeted 'I'm frantically live tweeting!' [Likes: 5, Retweets: 4]
2019-12-16T05:31:41.788Z - User wayne tweeted 'I'm frantically live tweeting!' [Likes: 1, Retweets: 4]
[info] TweetStreamExample:
[info] Finite historical stream
[info] - should return 5 historical tweets from the past
[info] Infinite stream with new tweets
[info] - should return an stream of live tweets as they come in (up to 10)
[info] Run completed in 5 seconds, 916 milliseconds.
[info] Total number of tests run: 2
[info] Suites: completed 1, aborted 0
[info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[success] Total time: 11 s, completed Dec 15, 2019 9:31:42 PM
```
