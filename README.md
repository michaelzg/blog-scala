# Scala Blog Code

Scala code for my blog https://michaelzg.com.
Multi-project repository to keep things consolidated for the blog posts.
Each project has it's own README to refer to.

* [`fsm`](fsm/): Akka state machine with actors
* [`streaming-merge`](streaming-merge/): Akka streams
combined historical and new data streams.

### Compile & Test

Applies to all modules. See more in `Makefile`.

```
make compile
make test
```

### Mill: Example Commands

```
mill fsm.compile
mill streaming-merge.compile
mill fsm.test
mill fsm.test.testOne cooking.chef.ChefBasicSpec
mill streaming-merge.test
```
