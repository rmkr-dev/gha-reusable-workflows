# Contributing

Thanks for helping keep these reusable workflows small, correct, and caller-friendly.

## Workflow

1. Open a focused PR against `main` (one concern per PR when practical; 2–4 commits is fine).
2. Use a conventional commit message (for example `feat:`, `fix:`, `docs:`, `ci:`).
3. Keep changes compatible with existing `workflow_call` inputs unless you intentionally break and retag.
4. Exercise samples under `samples/` when touching Python, Java, Docker, or SBOM workflows (see [`samples/README.md`](samples/README.md)).
5. Update `CHANGELOG.md` Unreleased when behavior or caller docs change.
6. After merge, maintainers may tag a release when CI on `main` is green.
7. After cutting an annotated tag, bump composite action pins in a follow-up PR (the tag must exist first).

## Local checks

- Edit YAML carefully; invalid workflow syntax fails at runtime.
- For Java sample: `mvn -B -f samples/java-hello/pom.xml test`
- For Java Failsafe: `mvn -B -f samples/java-hello/pom.xml verify -Pintegration-test`
- For Python sample: `cd samples/python-hello && pip install -e ".[dev]" && ruff check . && pytest`
- For Docker sample: `docker build -t local/docker-hello:dev -f samples/docker-hello/Dockerfile samples/docker-hello`

## Style

- No Node/npm tooling in this repo.
- No company-specific naming.
- Docs must match the workflows that actually ship.
- Prefer free GitHub-hosted actions; avoid paid SaaS CI.

## References

- [development.md](docs/development/development.md)
- [edge-cases.md](docs/references/edge-cases.md)
- [versioning.md](docs/references/versioning.md)
- [release-notes.md](docs/references/release-notes.md)

## Support

Usage questions: [SUPPORT.md](SUPPORT.md). Security: [SECURITY.md](SECURITY.md).

## Pins

Prefer `@v0.5.1` (or newer annotated tags) in consumer `uses:` lines. Dogfood `@main` only when validating Unreleased work.

Reviewers are guided by `.github/CODEOWNERS` (workflows, composites, docs).
