# docker-hello

Minimal BusyBox image used by this repository's `ci.yml` to exercise
`docker-build.yml` with `push: false` and Trivy enabled.

## Local build

```bash
docker build -t local/docker-hello:dev -f samples/docker-hello/Dockerfile samples/docker-hello
docker run --rm local/docker-hello:dev
```

## Self-test contract

| Setting | Value in `ci.yml` |
| --- | --- |
| `context` / `file` | `samples/docker-hello` |
| `push` | `false` (load locally for Trivy) |
| `scan` | `true` |
| `trivy-severity` | `CRITICAL` (keeps the BusyBox base quiet) |

## Caller tips

- Prefer your own Dockerfile and base image in consumer repos.
- Start with `push: false` on pull requests; only enable push after registry login in a **caller-owned** job (see [docker-build.md](../../docs/references/docker-build.md)).
- If Trivy fails on HIGH findings for a fat base image, narrow `trivy-severity` or upgrade the base — do not disable `scan` without a documented exception.
- Optional: `labels` / `annotations`, multi-stage `target`, `enable-gha-cache: false`, or `trivy-exit-code: "0"` for report-only scans (see `@v0.5.2` docker-build inputs).
