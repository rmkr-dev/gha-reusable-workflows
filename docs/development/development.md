# Development

## Layout

```
.github/workflows/     # reusable + self-test workflows
.github/actions/       # composite helpers (setup-python-project, setup-maven-project)
samples/python-hello/  # Python package used by self-test CI
samples/java-hello/    # Maven project used by self-test CI
samples/python-requirements-dev/  # requirements-dev.txt install path
samples/java-multi/   # multi-module Maven reactor
samples/python-requirements-only/  # requirements.txt install path
samples/python-compileall-only/    # compileall fallback, no tests
samples/docker-hello/             # docker-build + Trivy self-test
docs/                  # architecture, security, examples
```

## Prerequisites

- Python 3.12+ (local sample checks)
- JDK 21 + Maven 3.9+ (local Java sample checks)
- `gh` authenticated for PR/merge work

No Node/npm toolchain.

## Composite actions

| Action | Responsibility |
| --- | --- |
| `.github/actions/setup-python-project` | `setup-python` (+ optional pip cache) and install deps per caller contract |
| `.github/actions/setup-maven-project` | `setup-java` (+ optional Maven cache) and verify `pom.xml` |

Reusable workflows reference these with an absolute `owner/repo/.github/actions/...@ref` pin (same ref consumers use for the workflow) so cross-repo callers resolve actions correctly.

## Editing reusable workflows

1. Change the smallest set of YAML under `.github/workflows/`.
2. Keep `workflow_call` inputs backward compatible when possible.
3. Update `samples/` and docs in the same PR when contracts change.
4. Open a PR; merge only when checks pass.

### Notable `workflow_call` inputs

| Workflow | Input | Default | Notes |
| --- | --- | --- | --- |
| `python-ci.yml` | `timeout-minutes` | `30` | Job-level timeout |
| `python-ci.yml` | `fail-fast` | `true` | Documented; pytest fails the job on error |
| `python-ci.yml` | `enable-pip-cache` | `true` | Uses `actions/setup-python` pip cache |
| `java-maven-ci.yml` | `timeout-minutes` | `30` | Job-level timeout |
| `java-maven-ci.yml` | `fail-fast` | `true` | Sets `surefire.skipAfterFailureCount=1` |
| `java-maven-ci.yml` | `enable-maven-cache` | `true` | Uses `actions/setup-java` Maven cache |
| `java-maven-ci.yml` | `maven-goals` | `test` | Space-separated goals |

Job summaries are written to `$GITHUB_STEP_SUMMARY`. Workflow outputs expose resolved language versions and whether tests ran.

## Monorepo callers

Consumer repos with multiple modules should call each reusable workflow with a distinct
`working-directory` (or Docker `context`). Supported patterns:

- Explicit jobs per module
- `strategy.matrix` on the caller job that `uses:` the reusable workflow
- Path filters (`dorny/paths-filter`) gating each call
- Language/version matrices and per-service Docker matrices

Copy-paste examples: [monorepo.md](../references/monorepo.md).

## Security self-test jobs

- `sbom-sample` — calls `sbom.yml` on `samples/python-hello` (SPDX artifact `sbom-python-hello`)
- `sbom-java-sample` — calls `sbom.yml` on `samples/java-hello` (SPDX artifact `sbom-java-hello`)
- `dependency-review.yml` is provided for callers with Dependency graph enabled (not self-tested in this repo yet)

## Local sample commands

```bash
# Python
cd samples/python-hello
python -m venv .venv && source .venv/bin/activate
python -m pip install -e ".[dev]"
ruff check .
pytest

# Java
mvn -B -f samples/java-hello/pom.xml test
mvn -B -f samples/java-hello/pom.xml verify -Pintegration-test

# Python requirements-dev path
cd samples/python-requirements-dev
python -m pip install -r requirements-dev.txt
PYTHONPATH=src pytest

# Java multi-module
mvn -B -f samples/java-multi/pom.xml test

# Docker sample
docker build -t local/docker-hello:dev -f samples/docker-hello/Dockerfile samples/docker-hello
```

## SBOM self-test inputs

| Workflow | Input | Default | Notes |
| --- | --- | --- | --- |
| `sbom.yml` | `path` | `.` | Directory to scan |
| `sbom.yml` | `format` | `spdx-json` | Passed to anchore/sbom-action |
| `sbom.yml` | `artifact-name` | `sbom` | Basename; `.spdx.json` suffix applied |
| `sbom.yml` | `upload-artifact` | `true` | Actions artifact upload |
| `sbom.yml` | `upload-artifact-retention` | `0` | Days to retain; `0` = repo default |

The reusable workflow verifies a local `output-file` after generation so empty/broken SBOMs fail CI.

## Release helper inputs

| Input | Default | Notes |
| --- | --- | --- |
| `tag` | `""` | Empty = safe no-op |
| `generate-notes` | `true` | Used when changelog section missing |
| `changelog-path` | `CHANGELOG.md` | Soft-extract `## [X.Y.Z]`; empty skips |
| `notes-fallback` | `generate` | `generate` \| `notes` \| `tag` |

## Publishing a version

After CI is green on `main`:

```bash
# Prefer CHANGELOG soft notes via release-tag.yml, or:
gh release create v0.5.1 --notes-file /tmp/notes.md --target main
```

See [versioning.md](../references/versioning.md), [release-notes.md](../references/release-notes.md),
and [CHANGELOG.md](../../CHANGELOG.md).

Callers should prefer `@v0.5.1` / `@v1` over floating `@main` once tags exist.

Docker build inputs: [docker-build.md](../references/docker-build.md).
Matrix / monorepo: [monorepo.md](../references/monorepo.md).
See [examples.md](../references/examples.md) for consumer pins.

## Releases

Prefer annotated tags via `release-tag.yml`. Use `draft: true` when previewing GitHub Release notes before publish.

## Manual self-test

`.github/workflows/ci.yml` accepts `workflow_dispatch` so maintainers can re-run the sample matrix without an empty commit.

## Current release

Latest annotated tag: `v0.5.1`. Composite actions in reusable CI workflows pin to the latest annotated tag (follow-up PR after cut; see `AGENTS.md`).

When stacking PRs, regenerate `CHANGELOG.md` Unreleased from current `main` to avoid duplicate bullets.
