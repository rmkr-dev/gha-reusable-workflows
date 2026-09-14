# Docker build reusable workflow

Workflow: [`.github/workflows/docker-build.yml`](../../.github/workflows/docker-build.yml)

Pin: `rmkr-dev/gha-reusable-workflows/.github/workflows/docker-build.yml@v0.4.2`

## Inputs

| Input | Type | Default | Notes |
| --- | --- | --- | --- |
| `context` | string | `.` | Build context relative to repository root |
| `file` | string | `Dockerfile` | Dockerfile path relative to repository root |
| `image-name` | string | `app` | Used for default local tag / summary |
| `tags` | string | `""` | Comma-separated tags; empty → `local/<image-name>:ci` |
| `labels` | string | `""` | Comma-separated OCI labels (`key=value`) for build-push-action |
| `build-args` | string | `""` | Comma-separated `KEY=VALUE` build args |
| `target` | string | `""` | Multi-stage Dockerfile target (empty = final stage) |
| `enable-gha-cache` | boolean | `true` | `type=gha` cache-from/cache-to for buildx |
| `push` | boolean | `false` | Build-only by default (self-test safe) |
| `platforms` | string | `linux/amd64` | Prefer a single platform when `push=false` + `load` |
| `scan` | boolean | `true` | Run Trivy after build |
| `trivy-severity` | string | `CRITICAL,HIGH` | Severities that fail the job |
| `trivy-ignore-unfixed` | boolean | `true` | Trivy `ignore-unfixed` |
| `timeout-minutes` | number | `30` | Job timeout |

## Outputs

| Output | Meaning |
| --- | --- |
| `image-name` | Resolved `image-name` input |
| `pushed` | Whether `push` was true |
| `scanned` | Whether Trivy ran |

## Build-only (PR / self-test)

```yaml
jobs:
  image:
    uses: rmkr-dev/gha-reusable-workflows/.github/workflows/docker-build.yml@v0.4.2
    with:
      context: samples/docker-hello
      file: samples/docker-hello/Dockerfile
      image-name: docker-hello
      push: false
      scan: true
      trivy-severity: CRITICAL
```

When `push` is false the workflow sets `load: true` so Trivy can scan the image from the local Docker engine.

## Push to GHCR (caller-owned login)

The reusable workflow **does not** log in to a registry. Use a thin wrapper job:

```yaml
jobs:
  login-and-build:
    runs-on: ubuntu-latest
    permissions:
      contents: read
      packages: write
    steps:
      - uses: actions/checkout@v7
      - uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}
      # Option A: call build-push-action here after login
      # Option B: keep reusable workflow with push:true only if login ran in the *same* job
      #            (workflow_call jobs are isolated — prefer a caller-owned build when pushing)
```

Because `workflow_call` jobs are isolated, **registry login must happen inside the same job that pushes**. For GHCR pushes, either:

1. Copy the build/scan steps into a caller-owned job after `docker/login-action`, or
2. Extend a fork/wrapper that adds login before `docker/build-push-action`.

Keeping `push: false` in this reusable workflow is intentional for safe dogfooding without packages permissions.

## Trivy

- Action: `aquasecurity/trivy-action` (pinned in the workflow).
- `ignore-unfixed: true` so findings without fixes do not fail the job.
- Tune `trivy-severity` (for example `CRITICAL` only) for noisy base images.


## Labels

Optional OCI labels (comma-separated `key=value`) flow through to `docker/build-push-action`:

```yaml
with:
  image-name: api
  labels: org.opencontainers.image.source=https://github.com/example/api,org.opencontainers.image.title=api
  push: false
```

Empty `labels` leaves the build-push default (no extra labels).

## Build-args

Comma-separated `KEY=VALUE` pairs passed to `docker/build-push-action`:

```yaml
with:
  image-name: api
  build-args: BUILD_VERSION=1.2.3,GIT_SHA=${{ github.sha }}
  push: false
```

Empty `build-args` leaves the build without extra args.

## Target (multi-stage)

Pass `target` to select a named build stage (empty string uses the final stage):

```yaml
with:
  file: Dockerfile
  target: runtime
  push: false
```

## GHA cache

`enable-gha-cache` (default `true`) sets buildx `cache-from` / `cache-to` to `type=gha`. Set `false` for cold builds or when GHA cache is undesirable.

## Platforms

- Default `linux/amd64` matches GitHub-hosted `ubuntu-latest`.
- Multi-platform builds typically require `push: true` (buildx cannot `load` multi-arch easily).

## In-repo self-test

`ci.yml` → `docker-sample` builds [`samples/docker-hello`](../../samples/docker-hello) with `push: false` and `trivy-severity: CRITICAL`.

See also: [examples.md](examples.md), [monorepo.md](monorepo.md).
