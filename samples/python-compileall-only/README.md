# python-compileall-only

No `pyproject.toml`, no `requirements*.txt`, and no `tests/` directory.

`python-ci.yml` should skip pip install (after the upgrade step), run `python -m compileall` on `src/`, and skip pytest. Keeps the resilient fallback honest.
