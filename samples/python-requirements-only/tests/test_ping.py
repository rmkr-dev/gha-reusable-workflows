import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1] / "src"))

from reqonly_pkg import ping


def test_ping():
    assert ping() == "pong"
