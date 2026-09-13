package dev.rmkr.app;

import dev.rmkr.lib.Messages;

public final class App {
  private App() {}

  public static String run() {
    return Messages.hello("multi");
  }
}
