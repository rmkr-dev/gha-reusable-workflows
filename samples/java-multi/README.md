# java-multi

Multi-module Maven sample (`lib` + `app`) used to exercise `java-maven-ci.yml` against a reactor `pom.xml`.

## Self-test contract

`ci.yml` calls `java-maven-ci.yml` with `working-directory: samples/java-multi` and
`maven-goals: test`, which runs Surefire across the reactor.

## Caller tips (reactor / `-pl`)

From a reactor root you can narrow the build with Maven flags in `maven-goals`:

```yaml
with:
  working-directory: .
  maven-goals: -pl app -am test
```

| Flag | Meaning |
| --- | --- |
| `-pl app` | Only the `app` module |
| `-am` | Also make dependencies of the selected modules |
| `verify -Pintegration-test` | Failsafe profile (see `java-hello`) |

## Fail-on-test behavior

`java-maven-ci.yml` defaults `fail-fast: true`, which passes `-Dsurefire.skipAfterFailureCount=1` so Maven stops after the first failing test. Set `fail-fast: false` on the caller if you want the full Surefire report for every module/test class in one run.

This sample’s tests are intentionally green so self-test CI stays green.
