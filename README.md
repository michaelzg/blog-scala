# Scala Blog Code

Scala code for my blog https://michaelzg.com.
Multi-project repository to keep things consolidated for the blog posts.
Each project has it's own README to refer to.

Compile Examples:

```
mill fsm.compile
mill streaming-merge.compile
```

Test:

```
mill fsm.test
mill fsm.test.testOne cooking.chef.ChefBasicSpec

mill streaming-merge.test
```
