# Example consumer workflows

Copy these into a caller repository under `.github/workflows/`. Prefer a version tag (`@v0.1.0` or `@v1`) once published; use `@main` only while iterating.

Caller contract reminders:

- **Python** (`python-ci.yml`): optional `pyproject.toml` with `[project.optional-dependencies].dev`, or `requirements-dev.txt` / `requirements.txt`. Without those, the workflow still runs `python -m compileall`.
- **Java** (`java-maven-ci.yml`): `pom.xml` must exist under `working-directory`.
- Reusable workflows run on **GitHub-hosted** `ubuntu-latest` runners.

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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.1.0
    with:
      working-directory: .
      python-version: "3.12"
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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.1.0
    with:
      working-directory: .
      java-version: "21"
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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/codeql.yml@v0.1.0
    with:
      languages: '["python"]'
      # or: languages: '["java-kotlin"]'
```

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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/dependency-review.yml@v0.1.0
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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/sbom.yml@v0.1.0
    with:
      path: .
      artifact-name: sbom
```

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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/release-tag.yml@v0.1.0
    with:
      tag: ${{ inputs.tag }}
      generate-notes: true
```

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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.1.0
    with:
      python-version: "3.12"

  codeql:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/codeql.yml@v0.1.0
    with:
      languages: '["python"]'

  dependency-review:
    if: github.event_name == 'pull_request'
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/dependency-review.yml@v0.1.0
```

## Pinning `@main` vs tags

| Ref | When to use |
| --- | --- |
| `@main` | Early adoption / dogfooding this repo |
| `@v0.1.0` | Reproducible pin to a release |
| `@v1` | Moving major line once a `v1` tag (or `v1` major alias) exists |

This repository tags annotated releases from `main` after CI is green.
