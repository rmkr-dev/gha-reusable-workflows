# java-multi

Multi-module Maven sample (`lib` + `app`) used to exercise `java-maven-ci.yml` against a reactor `pom.xml`.

## Fail-on-test behavior

`java-maven-ci.yml` defaults `fail-fast: true`, which passes `-Dsurefire.skipAfterFailureCount=1` so Maven stops after the first failing test. Set `fail-fast: false` on the caller if you want the full Surefire report for every module/test class in one run.

This sample’s tests are intentionally green so self-test CI stays green.
