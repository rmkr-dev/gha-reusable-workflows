# python-compileall-only

No `pyproject.toml`, no `requirements*.txt`, and no `tests/` directory.

## Self-test contract

`python-ci.yml` should:

1. Skip project/requirements installs (after the pip upgrade step)
2. Run `python -m compileall` on sources under the working directory
3. Skip pytest

This keeps the resilient **no-metadata** fallback honest.

## Edge notes

| Edge | Expected |
| --- | --- |
| Empty or missing `tests/` | No pytest invocation |
| No ruff installed | No `ruff check` (compileall path) |
| Syntax error in `src/` | Job fails on compileall |

See the samples catalog: [README.md](../README.md) and [edge-cases.md](../../docs/references/edge-cases.md).
