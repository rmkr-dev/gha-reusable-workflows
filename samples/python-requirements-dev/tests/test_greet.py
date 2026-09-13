import sys
from pathlib import Path

# Allow tests to import without an editable install when PYTHONPATH is set;
# the reusable workflow pip-installs requirements-dev.txt which does not
# install this package, so we add src/ on sys.path for the sample.
sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "src"))

from reqdev_pkg import greet


def test_greet_default():
    assert greet() == "hello, world"


def test_greet_name():
    assert greet("rmkr") == "hello, rmkr"
