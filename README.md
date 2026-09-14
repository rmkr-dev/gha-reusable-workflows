# gha-reusable-workflows

Reusable GitHub Actions workflows that other repositories can call with:

```yaml
uses: rmkr-dev/gha-reusable-workflows/.github/workflows/<name>.yml@v0.4.0
```

Until you prefer a floating major, pin the annotated tag. `@main` is available for early adopters.

## Why

Keep CI consistent across personal Java/Maven and Python repositories without copying workflow YAML into every repo. One place to harden checkout, setup, lint/test, CodeQL, dependency review, SBOM, and release helpers.

## Workflows

| Workflow | Trigger type | Purpose |
| --- | --- | --- |
| [`python-ci.yml`](.github/workflows/python-ci.yml) | `workflow_call` | Setup Python, install deps, compile/lint, pytest |
| [`java-maven-ci.yml`](.github/workflows/java-maven-ci.yml) | `workflow_call` | Setup Temurin JDK, Maven goals (default `test`) |
| [`codeql.yml`](.github/workflows/codeql.yml) | `workflow_call` | CodeQL analyze for caller-provided languages |
| [`dependency-review.yml`](.github/workflows/dependency-review.yml) | `workflow_call` | PR dependency review |
| [`sbom.yml`](.github/workflows/sbom.yml) | `workflow_call` | Generate SPDX SBOM, verify, upload artifact (optional retention days) |
| [`release-tag.yml`](.github/workflows/release-tag.yml) | `workflow_call` | Create annotated `v*` tag + GitHub Release (CHANGELOG soft notes) |
| [`docker-build.yml`](.github/workflows/docker-build.yml) | `workflow_call` | Build image (push default false), optional Trivy scan |
| [`ci.yml`](.github/workflows/ci.yml) | `push`/`pull_request` | Self-test: Python, Java, Docker, and SBOM samples |

### Hardening inputs (Python / Java)

| Input | Default | Applies to |
| --- | --- | --- |
| `timeout-minutes` | `30` | both |
| `fail-fast` | `true` | both |
| `enable-pip-cache` | `true` | Python |
| `enable-maven-cache` | `true` | Java |
| `maven-goals` | `test` | Java |

### Docker build inputs

| Input | Default | Notes |
| --- | --- | --- |
| `push` | `false` | Build-only unless caller logs into a registry |
| `scan` | `true` | Trivy via `aquasecurity/trivy-action` |
| `trivy-severity` | `CRITICAL,HIGH` | Fail the job on matching findings |
| `context` / `file` | `.` / `Dockerfile` | Build context paths |
| `labels` | `""` | Optional OCI labels (`key=value`, comma-separated) |

Jobs emit a step summary and expose workflow outputs (`python-version` / `tests-ran`, `java-version` / `maven-goals`).

### SBOM inputs

| Input | Default | Notes |
| --- | --- | --- |
| `format` | `spdx-json` | Passed to anchore/sbom-action |
| `upload-artifact` | `true` | Upload SPDX JSON artifact |
| `upload-artifact-retention` | `0` | Days to retain; `0` = repo default |


## Supported stacks

| Stack | Workflow | Caller contract |
| --- | --- | --- |
| Python | `python-ci.yml` | Optional `pyproject.toml` / `requirements-dev.txt`; falls back to `compileall` |
| Java / Maven | `java-maven-ci.yml` | Caller must provide `pom.xml` under `working-directory` |
| Any | `codeql.yml`, `dependency-review.yml`, `sbom.yml`, `release-tag.yml` | Language / path / tag inputs as documented |

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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.4.0
    with:
      working-directory: .
      python-version: "3.12"
      enable-pip-cache: true

  java:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.4.0
    with:
      working-directory: .
      java-version: "21"
      enable-maven-cache: true
```

Full copy-paste snippets (CodeQL, dependency review, SBOM, Docker, release): [docs/references/examples.md](docs/references/examples.md).
Docker build deep-dive (inputs, Trivy, GHCR): [docs/references/docker-build.md](docs/references/docker-build.md).
Action pin matrix (Dependabot majors): [docs/references/action-pins.md](docs/references/action-pins.md).

## Composite actions

| Action | Used by |
| --- | --- |
| [`.github/actions/setup-python-project`](.github/actions/setup-python-project) | `python-ci.yml` |
| [`.github/actions/setup-maven-project`](.github/actions/setup-maven-project) | `java-maven-ci.yml` |

Workflows pin these with `rmkr-dev/gha-reusable-workflows/.github/actions/<name>@v0.4.0`.

## Free-first GitHub Actions stance

- Prefer GitHub-hosted runners and first-party or widely used free actions.
- No paid SaaS CI required.
- No secrets in workflow logs; reusable workflows stay secret-agnostic unless a caller explicitly passes an input they own.
- Dependabot for `github-actions` keeps action pins current.
- Caller permission tables: [docs/security/permissions.md](docs/security/permissions.md).

## Versioning

- `main` — latest merged work; OK for early adopters.
- Annotated tags (`v0.1.0`, later `v0.2.0` / `v1`) — preferred for consumers.
- Breaking input/output changes bump the major tag line; document in PR and release notes.

## Samples

Self-test packages exercised by this repo's `ci.yml` (catalog: [`samples/README.md`](samples/README.md)):

- [`samples/python-hello`](samples/python-hello) — tiny package + pytest + ruff
- [`samples/java-hello`](samples/java-hello) — minimal Maven app + JUnit 5 + Failsafe IT profile (Java 21)
- [`samples/python-requirements-dev`](samples/python-requirements-dev) — `requirements-dev.txt` install path
- [`samples/java-multi`](samples/java-multi) — multi-module Maven reactor
- [`samples/python-requirements-only`](samples/python-requirements-only) — `requirements.txt` only
- [`samples/python-compileall-only`](samples/python-compileall-only) — no metadata; compileall fallback
- [`samples/docker-hello`](samples/docker-hello) — BusyBox Dockerfile for docker-build self-test

## Monorepo and matrices

Call Python/Java/Docker reusable workflows **once per module**, via explicit jobs,
`strategy.matrix`, or path filters.
Copy-paste patterns: [docs/references/monorepo.md](docs/references/monorepo.md).

## Docs

- [Architecture](docs/architecture/architecture.md) · [Diagram](docs/architecture/architecture-diagram.md) · [Network](docs/architecture/network-diagram.md)
- [Development](docs/development/development.md)
- [Security](docs/security/security.md) · [SECURITY.md](SECURITY.md)
- [Examples](docs/references/examples.md) · [Docker build](docs/references/docker-build.md) · [Monorepo / matrix](docs/references/monorepo.md) · [Edge cases](docs/references/edge-cases.md) · [Release notes](docs/references/release-notes.md) · [Versioning](docs/references/versioning.md) · [Changelog](CHANGELOG.md)
- [Contributing](CONTRIBUTING.md) · [Agent notes](AGENTS.md)

## License

[MIT](LICENSE)
