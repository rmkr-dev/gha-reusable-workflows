# python-requirements-dev

Sample that exercises the **`requirements-dev.txt`** install branch of `python-ci.yml` (no `pyproject.toml`).

The reusable workflow installs `requirements-dev.txt` when present and no `pyproject.toml` exists, then runs `compileall` / `pytest`.
