# Concurrency patterns for callers

Reusable workflows run as **called jobs** in the caller's workflow. Concurrency is
usually declared on the **caller** workflow (or on a caller-owned wrapper job), not
inside every `workflow_call` definition.

## Self-test in this repository

`.github/workflows/ci.yml` uses:

```yaml
concurrency:
  group: ci-${{ github.workflow }}-${{ github.event.pull_request.number || github.ref }}
  cancel-in-progress: true
```

Effect: a new push to the **same pull request** cancels the previous in-flight self-test
run, saving Actions minutes. Pushes to `main` share a concurrency group but do **not**
cancel in-flight runs (`cancel-in-progress` is false unless `github.event_name == 'pull_request'`),
so release validation on `main` is not aborted by a fast follow-up merge.

## Caller examples

### Cancel superseded PR runs

```yaml
name: CI
on:
  pull_request:
  push:
    branches: [main]

concurrency:
  group: ${{ github.workflow }}-${{ github.event.pull_request.number || github.ref }}
  cancel-in-progress: true

jobs:
  python:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/python-ci.yml@v0.4.0
    with:
      python-version: "3.12"
```

### Keep production deploys non-cancelling

```yaml
concurrency:
  group: deploy-${{ github.ref }}
  cancel-in-progress: false
```

Use `cancel-in-progress: false` for release or deploy workflows so a second push does
not abort a tag/release job mid-flight.

### Matrix + concurrency

`strategy.matrix` on a reusable workflow job still shares the **workflow-level**
concurrency group. Prefer `fail-fast: false` on large matrices so one leg does not
cancel siblings; let workflow concurrency only cancel the entire run when a newer
SHA supersedes it.

## Guidance

| Scenario | `cancel-in-progress` |
| --- | --- |
| PR / branch CI | `true` (default recommendation) |
| Release / deploy / tag | `false` |
| Scheduled audit (CodeQL nightly) | `false` or a dedicated group |

See also: [examples.md](examples.md), [monorepo.md](monorepo.md).
