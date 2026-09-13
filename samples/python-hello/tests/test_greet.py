from hello_pkg import greet


def test_greet_default():
    assert greet() == "hello, world"


def test_greet_name():
    assert greet("ci") == "hello, ci"
