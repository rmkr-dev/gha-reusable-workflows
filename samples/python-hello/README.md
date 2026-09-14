# python-hello

Minimal package used by this repository's self-test CI to exercise `python-ci.yml`.

## Lint

Dev extras include `ruff`. The reusable `python-ci.yml` runs `ruff check .` when `ruff` is on `PATH` after install.

## Caller tips

- Pass `ruff-args` for extra Ruff flags (`--select`, `--ignore`) when `ruff` is installed.
- Pass `pytest-args` for markers/`-k`/verbosity (appended after `pytest -q`).
- `fail-fast: true` adds pytest `-x` when tests run.
- Prefer `@v0.5.2` (or newer) when calling `python-ci.yml`.

