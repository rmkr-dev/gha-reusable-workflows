package dev.rmkr.hello;

public final class Hello {
  private Hello() {}

  public static String greet(String name) {
    if (name == null || name.isBlank()) {
      return "hello, world";
    }
    return "hello, " + name;
  }

  public static void main(String[] args) {
    String name = args.length > 0 ? args[0] : "world";
    System.out.println(greet(name));
  }
}
