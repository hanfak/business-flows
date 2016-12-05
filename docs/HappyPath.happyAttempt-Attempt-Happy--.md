---
title: HappyPath.happyAttempt(Attempt&lt;Happy&gt;)
layout: post
---
[javadoc](https://oss.sonatype.org/service/local/repositories/releases/archive/io/github/theangrydev/business-flows/10.1.12/business-flows-10.1.12-javadoc.jar/!/io/github/theangrydev/businessflows/HappyPath.html#happyAttempt-io.github.theangrydev.businessflows.Attempt-) [usage tests](https://github.com/theangrydev/business-flows/blob/master/src/test/java/api/HappyAttemptApiTest.java)

Added in version 1.0.0

These tests exist to prevent the failed solution to <a href="https://github.com/theangrydev/business-flows/issues/12">#12</a>
from being attempted again in the future without realising it :)

## Happy attempt can introduce sad type via then
```java
Sad sad = new Sad();

HappyPath<Happy, Sad> happyPath = HappyPath.<Happy, Sad>happyAttempt(Happy::new)
    .then(happy -> HappyPath.sadPath(sad));

assertThat(happyPath.getSad()).isEqualTo(sad);
```
An attempt that was once happy can be turned into a sad path.
The `happyAttempt` method is playing the role of the Try monad here, but is lifted into the Either monad immediately, which is why the `Sad` type has to be specified up front.
