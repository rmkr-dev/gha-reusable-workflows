package dev.rmkr.hello;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class HelloTest {
  @Test
  void greetDefault() {
    assertEquals("hello, world", Hello.greet(""));
  }

  @Test
  void greetName() {
    assertEquals("hello, ci", Hello.greet("ci"));
  }
}
