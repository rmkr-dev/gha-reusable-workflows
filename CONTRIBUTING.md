# Contributing

Thanks for helping keep these reusable workflows small, correct, and caller-friendly.

## Workflow

1. Open a focused PR against `main` (one concern per PR when practical).
2. Use a conventional commit message (for example `feat:`, `fix:`, `docs:`, `ci:`).
3. Keep changes compatible with existing `workflow_call` inputs unless you intentionally break and retag.
4. Exercise samples under `samples/` when touching Python or Java CI workflows.
5. After merge, maintainers may tag a release when CI on `main` is green.

## Local checks

- Edit YAML carefully; invalid workflow syntax fails at runtime.
- For Java sample: `mvn -B -f samples/java-hello/pom.xml test`
- For Python sample: `cd samples/python-hello && pip install -e ".[dev]" && pytest`

## Style

- No Node/npm tooling in this repo.
- No company-specific naming.
- Docs must match the workflows that actually ship.
