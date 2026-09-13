"""Minimal package installed via requirements-dev.txt path (no pyproject)."""


def greet(name: str = "world") -> str:
    return f"hello, {name}"
