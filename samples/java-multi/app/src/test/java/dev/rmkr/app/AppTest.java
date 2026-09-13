package dev.rmkr.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AppTest {
  @Test
  void run() {
    assertEquals("hello, multi", App.run());
  }
}
