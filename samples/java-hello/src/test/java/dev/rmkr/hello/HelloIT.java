package dev.rmkr.hello;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Failsafe integration-test sample (runs with -Pintegration-test). */
class HelloIT {
  @Test
  void greetProducesHelloPrefix() {
    assertTrue(Hello.greet("failsafe").startsWith("hello,"));
  }
}
