"""Tiny hello package for reusable workflow self-tests."""

__version__ = "0.1.0"


def greet(name: str = "world") -> str:
    return f"hello, {name}"
