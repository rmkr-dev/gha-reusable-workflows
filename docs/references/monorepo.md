# Matrix and monorepo callers

Reusable workflows in this repository accept a single `working-directory` (or Docker
`context`) per job. Callers that house multiple modules should **invoke the reusable
workflow once per module**, optionally behind path filters or a matrix.

Pin the same tag you use elsewhere (for example `@v0.5.0`).

## Pattern A — Explicit jobs per module

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

permissions:
  contents: read

jobs:
  python-api:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.5.0
    with:
      working-directory: services/api
      python-version: "3.12"

  python-worker:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.5.0
    with:
      working-directory: services/worker
      python-version: "3.12"

  java-lib:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.5.0
    with:
      working-directory: libs/common
      java-version: "21"
      maven-goals: test
```

## Pattern B — `strategy.matrix` on a reusable workflow job

GitHub Actions **does** support `strategy` (including `matrix`) on jobs that call a
reusable workflow. Each matrix combination becomes a separate reusable-workflow run.
Pass matrix values into `with:` like any other expression.

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

permissions:
  contents: read

jobs:
  python-modules:
    strategy:
      fail-fast: false
      matrix:
        module: [services/api, services/worker, libs/shared-py]
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.5.0
    with:
      working-directory: ${{ matrix.module }}
      python-version: "3.12"
      timeout-minutes: 20
      enable-pip-cache: true

  java-modules:
    strategy:
      fail-fast: false
      matrix:
        include:
          - dir: libs/common
            goals: test
          - dir: apps/java-service
            goals: verify
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.5.0
    with:
      working-directory: ${{ matrix.dir }}
      java-version: "21"
      maven-goals: ${{ matrix.goals }}
      enable-maven-cache: true
```

### Matrix tips

- Prefer `fail-fast: false` in monorepos so one module failure does not cancel siblings.
- Keep matrix axes small; prefer Pattern C (path filters) when most PRs touch one module.
- You cannot expand a matrix *inside* the reusable workflow from the caller — only on the
  caller job that uses `uses:`.

## Pattern C — Optional path filters (paths-filter)

Run module CI only when that module (or shared libs) changed. This example uses the
free [`dorny/paths-filter`](https://github.com/dorny/paths-filter) action in a thin
detector job, then guards each reusable call with `if:`.

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

permissions:
  contents: read
  pull-requests: read

jobs:
  changes:
    runs-on: ubuntu-latest
    outputs:
      api: ${{ steps.filter.outputs.api }}
      worker: ${{ steps.filter.outputs.worker }}
      java: ${{ steps.filter.outputs.java }}
    steps:
      - uses: actions/checkout@v7
      - uses: dorny/paths-filter@v3
        id: filter
        with:
          filters: |
            api:
              - 'services/api/**'
              - 'libs/shared-py/**'
            worker:
              - 'services/worker/**'
              - 'libs/shared-py/**'
            java:
              - 'libs/common/**'
              - 'apps/java-service/**'

  python-api:
    needs: changes
    if: needs.changes.outputs.api == 'true'
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.5.0
    with:
      working-directory: services/api
      python-version: "3.12"

  python-worker:
    needs: changes
    if: needs.changes.outputs.worker == 'true'
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.5.0
    with:
      working-directory: services/worker
      python-version: "3.12"

  java-lib:
    needs: changes
    if: needs.changes.outputs.java == 'true'
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.5.0
    with:
      working-directory: libs/common
      java-version: "21"
```

### Push vs pull_request

- On `pull_request`, `dorny/paths-filter` compares against the base branch.
- On `push` to `main`, configure the filter's `base`/`ref` as needed, or always run
  modules on `main` by omitting the `if:` guards for that event.

## Pattern D — Language / version matrix (caller-owned)

When you need multiple language versions for **one** module, put the version on the
matrix and pass it through:

```yaml
jobs:
  python-versions:
    strategy:
      fail-fast: false
      matrix:
        python-version: ["3.11", "3.12"]
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.5.0
    with:
      working-directory: .
      python-version: ${{ matrix.python-version }}

  java-versions:
    strategy:
      fail-fast: false
      matrix:
        java-version: ["17", "21"]
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.5.0
    with:
      working-directory: .
      java-version: ${{ matrix.java-version }}
```

## Pattern E — Docker images per service

```yaml
jobs:
  images:
    strategy:
      fail-fast: false
      matrix:
        include:
          - context: services/api
            file: services/api/Dockerfile
            name: api
          - context: services/worker
            file: services/worker/Dockerfile
            name: worker
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/docker-build.yml@v0.5.0
    with:
      context: ${{ matrix.context }}
      file: ${{ matrix.file }}
      image-name: ${{ matrix.name }}
      push: false
      scan: true
```

## Inputs that help monorepos

| Workflow | Input | Monorepo use |
| --- | --- | --- |
| `python-ci.yml` | `working-directory` | Module root with `pyproject.toml` / requirements |
| `java-maven-ci.yml` | `working-directory` | Module (or reactor) root with `pom.xml` |
| `java-maven-ci.yml` | `maven-goals` | e.g. `test` or `-pl app -am test` from a reactor root |
| `docker-build.yml` | `context` / `file` | Per-service Dockerfile paths |
| all CI workflows | `timeout-minutes` | Cap runaway module builds |

## What this repo does **not** do

- No built-in `paths:` filter inside the reusable workflows themselves (callers own
  change detection).
- No Node/npm matrices.
- No automatic discovery of every module under the tree.

See also: [examples.md](examples.md), [docker-build.md](docker-build.md), [development.md](../development/development.md).

## In-repo example

This repository's own `ci.yml` calls the Java workflow twice — once for
`samples/java-hello` and once for `samples/java-multi` — which is the same
"one job per module root" pattern consumers should copy. The Python samples are
likewise separate jobs (explicit Pattern A), not a matrix, to keep the dogfood
surface easy to read in the Actions UI.
