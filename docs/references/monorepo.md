# Matrix and monorepo callers

Reusable workflows in this repository accept a single `working-directory` (or Docker
`context`) per job. Callers that house multiple modules should **invoke the reusable
workflow once per module**, optionally behind path filters or a matrix.

Pin the same tag you use elsewhere (for example `@v0.2.0` until `v0.3.0` lands).

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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.2.0
    with:
      working-directory: services/api
      python-version: "3.12"

  python-worker:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.2.0
    with:
      working-directory: services/worker
      python-version: "3.12"

  java-lib:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.2.0
    with:
      working-directory: libs/common
      java-version: "21"
      maven-goals: test
```

## Pattern B — Matrix of working directories

GitHub Actions does not expand `strategy.matrix` into `uses:` inputs on a reusable
workflow job the same way as a normal job. Prefer a thin wrapper job that maps matrix
values, **or** keep explicit jobs (Pattern A). When you do use a matrix on a
**caller-owned** job that checks out and then shells into modules, keep lint/test logic
in the reusable workflows where possible.

Example using **path-filtered** reusable calls (Pattern C below) is usually clearer for
monorepos than forcing a matrix through `workflow_call`.

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
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.2.0
    with:
      working-directory: services/api
      python-version: "3.12"

  python-worker:
    needs: changes
    if: needs.changes.outputs.worker == 'true'
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.2.0
    with:
      working-directory: services/worker
      python-version: "3.12"

  java-lib:
    needs: changes
    if: needs.changes.outputs.java == 'true'
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/java-maven-ci.yml@v0.2.0
    with:
      working-directory: libs/common
      java-version: "21"
```

### Push vs pull_request

- On `pull_request`, `dorny/paths-filter` compares against the base branch.
- On `push` to `main`, configure the filter's `base`/`ref` as needed, or always run
  modules on `main` by omitting the `if:` guards for that event.

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

See also: [examples.md](examples.md), [development.md](../development/development.md).

## In-repo example

This repository's own `ci.yml` calls the Java workflow twice — once for
`samples/java-hello` and once for `samples/java-multi` — which is the same
"one job per module root" pattern consumers should copy.
