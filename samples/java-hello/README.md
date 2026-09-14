# java-hello

Minimal Maven (Java 21) sample for self-test CI.

## Unit tests (Surefire)

```bash
mvn -B -f samples/java-hello/pom.xml test
```

Exercised by `java-sample` in `ci.yml` (`maven-goals: test`).

## Integration tests (Failsafe)

Profile `integration-test` runs Failsafe against `*IT.java`:

```bash
mvn -B -f samples/java-hello/pom.xml verify -Pintegration-test
```

Exercised by `java-failsafe-sample` in `ci.yml`.

## Failsafe IT naming

Failsafe (default includes) picks up `*IT.java` / `IT*.java` / `*ITCase.java`.
This sample keeps `HelloIT` under `src/test/java` and enables the plugin via the
`integration-test` profile. Callers who put ITs under `src/it/java` must configure
Failsafe `testSourceDirectory` (or a similar layout) themselves — not covered here.

## Caller tips

- Pass `maven-extra-args` for reactor/property flags (for example `-DskipITs`) instead of stuffing them into `maven-goals`.
- Pass `maven-opts` for JVM flags via `MAVEN_OPTS` (for example `-Xmx1g`).
- Prefer `@v0.5.1` (or newer) when calling `java-maven-ci.yml`.
