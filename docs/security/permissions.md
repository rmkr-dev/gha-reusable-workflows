# Caller permissions reference

Reusable workflows inherit the caller's `GITHUB_TOKEN` permissions. Set `permissions:`
narrowly on the caller workflow (or per job) so tokens stay least-privilege.

## Minimum permissions by workflow

| Workflow | Typical caller permissions | Why |
| --- | --- | --- |
| `python-ci.yml` | `contents: read` | Checkout + run tests |
| `java-maven-ci.yml` | `contents: read` | Checkout + Maven |
| `docker-build.yml` (`push: false`) | `contents: read` | Build + Trivy locally |
| `docker-build.yml` (GHCR push in **caller** job) | `contents: read`, `packages: write` | Login + push (caller-owned job) |
| `sbom.yml` | `contents: read` | Generate/upload SBOM artifact |
| `dependency-review.yml` | `contents: read`, `pull-requests: write` | Review + optional PR comment |
| `codeql.yml` | `contents: read`, `actions: read`, `security-events: write` | Upload CodeQL SARIF |
| `release-tag.yml` | `contents: write` | Create annotated tag + release |

## Recommended caller top-level block

```yaml
permissions:
  contents: read

jobs:
  test:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.5.0

  codeql:
    permissions:
      contents: read
      actions: read
      security-events: write
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/codeql.yml@v0.5.0
    with:
      languages: '["python"]'

  dependency-review:
    if: github.event_name == 'pull_request'
    permissions:
      contents: read
      pull-requests: write
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/dependency-review.yml@v0.5.0
```

## Anti-patterns

- Do **not** set `permissions: write-all` on workflows that only call these reusable jobs.
- Do **not** grant `packages: write` to the reusable `docker-build.yml` job unless you
  have extended a **caller-owned** job that logs in and pushes in the same job.
- Do **not** grant `security-events: write` to jobs that do not upload SARIF / advisories.

## This repository's self-test

`ci.yml` uses `permissions: contents: read` only. Sample jobs never push images or
create releases.

See also: [security.md](security.md), [../../SECURITY.md](../../SECURITY.md), [../references/examples.md](../references/examples.md).
