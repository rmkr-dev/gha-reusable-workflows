package dev.rmkr.lib;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MessagesTest {
  @Test
  void hello() {
    assertEquals("hello, world", Messages.hello("world"));
  }
}
