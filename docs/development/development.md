# Development

## Layout

```
.github/workflows/     # reusable + self-test workflows
samples/python-hello/  # Python package used by self-test CI
samples/java-hello/    # Maven project used by self-test CI
docs/                  # architecture, security, examples
```

## Prerequisites

- Python 3.12+ (local sample checks)
- JDK 21 + Maven 3.9+ (local Java sample checks)
- `gh` authenticated for PR/merge work

No Node/npm toolchain.

## Editing reusable workflows

1. Change the smallest set of YAML under `.github/workflows/`.
2. Keep `workflow_call` inputs backward compatible when possible.
3. Update `samples/` and docs in the same PR when contracts change.
4. Open a PR; merge only when checks pass.

## Local sample commands

```bash
# Python
cd samples/python-hello
python -m venv .venv && source .venv/bin/activate
python -m pip install -e ".[dev]"
pytest

# Java
mvn -B -f samples/java-hello/pom.xml test
```

## Publishing a version

After CI is green on `main`:

```bash
gh release create v0.1.0 --generate-notes --target main
```

Callers should prefer `@v0.1.0` / `@v1` over floating `@main` once tags exist.

See [examples.md](../references/examples.md) for consumer pins.
