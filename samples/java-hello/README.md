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
