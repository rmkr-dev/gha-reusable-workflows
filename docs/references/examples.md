# Example consumer workflows

Copy these into a caller repository under `.github/workflows/`. Prefer a version tag (`@v0.4.0`) once published; use `@main` only while iterating.

Caller contract reminders:

- **Python** (`python-ci.yml`): optional `pyproject.toml` with `[project.optional-dependencies].dev`, or `requirements-dev.txt` / `requirements.txt`. Without those, the workflow still runs `python -m compileall`. When `ruff` is installed (for example via `dev` extras), the workflow runs `ruff check .` instead of compileall.
- **Java** (`java-maven-ci.yml`): `pom.xml` must exist under `working-directory`.
- Reusable workflows run on **GitHub-hosted** `ubuntu-latest` runners.
- Optional hardening inputs: `timeout-minutes`, `fail-fast`, `enable-pip-cache` / `enable-maven-cache`, plus job summary outputs.

## Python CI

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

permissions:
  contents: read

jobs:
  test:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.4.0
    with:
      working-directory: .
      python-version: "3.12"
      timeout-minutes: 20
      fail-fast: true
      enable-pip-cache: true
```

## Java / Maven CI

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

permissions:
  contents: read

jobs:
  test:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.4.0
    with:
      working-directory: .
      java-version: "21"
      timeout-minutes: 20
      fail-fast: true
      enable-maven-cache: true
      maven-goals: test
      # or: maven-goals: verify -Pintegration-test   # Failsafe IT profile
```

## CodeQL

```yaml
name: CodeQL

on:
  push:
    branches: [main]
  pull_request:
  schedule:
    - cron: "0 6 * * 1"

permissions:
  contents: read
  security-events: write
  actions: read

jobs:
  analyze:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/codeql.yml@v0.4.0
    with:
      languages: '["python"]'
      # queries: security-extended
      # or: languages: '["java-kotlin"]'
```

Optional `working-directory` sets CodeQL `source-root` for monorepos. This repository does not run CodeQL in its own `ci.yml` self-tests; enable it on consumer repos that hold real application code.

## Dependency review (pull requests)



```yaml
name: Dependency review

on:
  pull_request:

permissions:
  contents: read
  pull-requests: write

jobs:
  review:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/dependency-review.yml@v0.4.0
    with:
      fail-on-severity: high
```

## SBOM artifact

```yaml
name: SBOM

on:
  workflow_dispatch:
  push:
    tags: ["v*"]

permissions:
  contents: read

jobs:
  sbom:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/sbom.yml@v0.4.0
    with:
      path: .
      artifact-name: sbom
      upload-artifact: true
      format: spdx-json
      upload-artifact-retention: 0
```

This repository's own `ci.yml` includes `sbom-sample` and `sbom-java-sample` jobs as live self-tests.
The reusable workflow verifies a local SPDX JSON `output-file` so empty/broken documents fail the job.

**Retention:** Actions artifacts follow the repository/org retention setting (GitHub default **90 days**). The reusable workflow writes a step-summary reminder. For longer retention, download the SPDX JSON in a follow-up job or attach it to a GitHub Release (`upload-release-assets` stays false by design so callers stay in control).

## OpenSSF Scorecard (caller-owned)

Scorecard is **not** wrapped as a reusable workflow here (see [security.md](../security/security.md#openssf-scorecard)). Example caller-owned workflow:

```yaml
name: Scorecard
on:
  schedule:
    - cron: "0 7 * * 1"
  workflow_dispatch:
permissions:
  security-events: write
  id-token: write
  contents: read
  actions: read
jobs:
  analysis:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v7
      - uses: ossf/scorecard-action@v2
        with:
          results_file: results.sarif
          results_format: sarif
          publish_results: true
```


## Docker build (no push by default)

```yaml
name: Docker

on:
  push:
    branches: [main]
  pull_request:

permissions:
  contents: read

jobs:
  image:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/docker-build.yml@v0.4.0
    with:
      context: .
      file: Dockerfile
      image-name: myapp
      tags: ghcr.io/OWNER/myapp:latest
      push: false
      scan: true
      trivy-severity: CRITICAL,HIGH
```

Set `push: true` only when the caller has already authenticated to a registry
(for example `docker/login-action` in a thin wrapper job, or `permissions: packages: write`
plus GHCR login). The reusable workflow itself does not log in.

## Release tag helper

```yaml
name: Release

on:
  workflow_dispatch:
    inputs:
      tag:
        description: Annotated tag to create (must start with v)
        required: true
        type: string

permissions:
  contents: write

jobs:
  release:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/release-tag.yml@v0.4.0
    with:
      tag: ${{ inputs.tag }}
      generate-notes: true
      changelog-path: CHANGELOG.md
      notes-fallback: generate
      # draft: true   # optional: create unpublished GitHub Release
```

When `CHANGELOG.md` contains a `## [X.Y.Z]` section matching the tag without the leading
`v`, those notes are used. Otherwise the workflow falls back to `--generate-notes`
(or a plain tag title when `generate-notes: false` / `notes-fallback: notes`).
Existing tags and releases remain untouched (safe no-op / skip).

## Combined Python + security jobs

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

permissions:
  contents: read
  security-events: write
  actions: read
  pull-requests: write

jobs:
  test:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.4.0
    with:
      python-version: "3.12"
      enable-pip-cache: true

  codeql:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/codeql.yml@v0.4.0
    with:
      languages: '["python"]'

  dependency-review:
    if: github.event_name == 'pull_request'
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/dependency-review.yml@v0.4.0
```



## Matrix callers

Reusable workflow jobs may use `strategy.matrix`. Example — two Python modules:

```yaml
jobs:
  python-modules:
    strategy:
      fail-fast: false
      matrix:
        module: [services/api, services/worker]
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.4.0
    with:
      working-directory: ${{ matrix.module }}
      python-version: "3.12"
```

More patterns (path filters, Java `include`, Docker per-service, language versions):
[monorepo.md](monorepo.md).



## Concurrency (caller)

```yaml
concurrency:
  group: ${{ github.workflow }}-${{ github.event.pull_request.number || github.ref }}
  cancel-in-progress: true
```

Place this at the top level of the **caller** workflow so superseded PR runs cancel.
Details: [concurrency.md](concurrency.md).

## Pinning `@main` vs tags

| Ref | When to use |
| --- | --- |
| `@main` | Early adoption / dogfooding this repo |
| `@v0.4.0` | Reproducible pin to the latest minor release |
| `@v0.3.0` | Previous minor |
| `@v1` | Moving major line once a `v1` tag (or `v1` major alias) exists |

This repository tags annotated releases from `main` after CI is green.

Semver rules: [versioning.md](versioning.md). Release history: [CHANGELOG.md](../../CHANGELOG.md).

Monorepo / multi-module callers: [monorepo.md](monorepo.md).
