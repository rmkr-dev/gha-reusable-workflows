# gha-reusable-workflows

Reusable GitHub Actions workflows that other repositories can call with:

```yaml
uses: rmkr-dev/gha-reusable-workflows/.github/workflows/<name>.yml@v1
```

Until the first release tag exists, callers may pin `@main`. Prefer an annotated version tag once published.

## Why

Keep CI consistent across personal Java/Maven and Python repositories without copying workflow YAML into every repo. One place to harden checkout, setup, lint/test, CodeQL, dependency review, SBOM, and release helpers.

## Supported stacks

| Stack | Workflow | Caller contract |
| --- | --- | --- |
| Python | `python-ci.yml` | Optional `pyproject.toml` / `requirements-dev.txt`; falls back to `compileall` on samples |
| Java / Maven | `java-maven-ci.yml` | Caller must provide `pom.xml` under `working-directory` |
| Any | `codeql.yml`, `dependency-review.yml`, `sbom.yml`, `release-tag.yml` | Language / event inputs as documented |

## How consumers call workflows

```yaml
# .github/workflows/ci.yml in a consumer repo
name: CI
on:
  push:
    branches: [main]
  pull_request:

jobs:
  python:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v1
    with:
      working-directory: .
      python-version: "3.12"

  java:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v1
    with:
      working-directory: .
      java-version: "21"
```

See [docs/references/examples.md](docs/references/examples.md) for copy-paste snippets (filled in after workflows land).

## Free-first GitHub Actions stance

- Prefer GitHub-hosted runners and first-party or widely used free actions.
- No paid SaaS CI required.
- No secrets in workflow logs; keep reusable workflows secret-agnostic unless a caller explicitly passes an input they own.
- Dependabot for `github-actions` keeps action pins current.

## Versioning

- `main` — latest merged work; OK for early adopters.
- Annotated tags (`v0.1.0`, later `v1`) — preferred for consumers.
- Breaking input/output changes bump the major tag line; document in PR and release notes.

## Docs

- [Architecture](docs/architecture/architecture.md) · [Diagram](docs/architecture/architecture-diagram.md) · [Network](docs/architecture/network-diagram.md)
- [Development](docs/development/development.md)
- [Security](docs/security/security.md)
- [Contributing](CONTRIBUTING.md) · [Agent notes](AGENTS.md)

## License

[MIT](LICENSE)
