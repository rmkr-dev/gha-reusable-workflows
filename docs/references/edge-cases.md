# Caller edge cases

This page lists **supported** and **unsupported** edges when calling the reusable
workflows. Sample fixtures that exercise the happy paths live under [`samples/`](../../samples/README.md).

## Python (`python-ci.yml`)

| Situation | Behavior |
| --- | --- |
| `pyproject.toml` with `[project.optional-dependencies].dev` | Editable install + pytest; `ruff check` if ruff is installed |
| `requirements-dev.txt` present | `pip install -r requirements-dev.txt` then pytest if tests exist |
| Only `requirements.txt` | Install runtime deps; pytest if present |
| No metadata files | `python -m compileall` on the working directory (no pytest) |
| Empty `working-directory` | Uses `.` (repository root) |
| Tests directory missing | Pytest may be skipped depending on composite setup; compile/lint still run |

**Unsupported:** Poetry/Pipenv lock-only flows without an install path the composite
understands; tox/nox as the primary runner; Windows/macOS runners.

## Java (`java-maven-ci.yml`)

| Situation | Behavior |
| --- | --- |
| Missing `pom.xml` under `working-directory` | Setup composite fails fast |
| Default `maven-goals: test` | Surefire only |
| `maven-goals: verify -Pintegration-test` | Runs Failsafe when the profile is defined (see `java-hello`) |
| Multi-module reactor root | Pass reactor root as `working-directory`; use `-pl` / `-am` in `maven-goals` if needed (see [`java-multi` README](../../samples/java-multi/README.md)) |
| `fail-fast: true` | Sets `surefire.skipAfterFailureCount=1` |

**Unsupported:** Gradle; non-Temurin JDKs via this workflow's inputs; publishing artifacts.

## Docker (`docker-build.yml`)

| Situation | Behavior |
| --- | --- |
| `push: false` (default) | `load: true` so Trivy can scan locally |
| Empty `tags` | Tags `local/<image-name>:ci` |
| Multi-platform + `push: false` | Not recommended — buildx cannot easily load multi-arch |
| Registry push | Requires login **in the same job**; prefer caller-owned push (see [docker-build.md](docker-build.md)) |

**Unsupported:** Built-in GHCR login; caching beyond GitHub Actions cache (`type=gha`).

## SBOM / security

| Situation | Behavior |
| --- | --- |
| Directory with little/no ecosystem metadata | SPDX document still produced; verify step requires parseable non-empty JSON |
| Custom artifact retention | Set `upload-artifact-retention` (days); `0` keeps the repo/org default |
| `format` other than `spdx-json` | Generated; SPDX-specific parse checks are skipped |
| Dependency review without Dependency graph | GitHub rejects the action — enable Dependency graph on the **caller** repo |

## Release (`release-tag.yml`)

| Situation | Behavior |
| --- | --- |
| Empty `tag` | Safe no-op |
| Tag without leading `v` | Fails |
| Tag/release already exists | Skips create; leaves notes unchanged |
| No matching `## [X.Y.Z]` in CHANGELOG | Falls back per `notes-fallback` / `generate-notes` |

See [release-notes.md](release-notes.md) for CHANGELOG conventions.

## More help

Symptom → fix tables: [troubleshooting.md](troubleshooting.md).

## Full git history

Python and Java reusable CI accept `fetch-depth` (default `1`). Pass `fetch-depth: 0` when tools need tags or full history (for example setuptools-scm, git describe).

## Alternate JDK distributions

`java-maven-ci.yml` accepts `java-distribution` (default `temurin`). Prefer the latest annotated tag for composites and workflows.

## Docker build-args

Pass `build-args: FOO=bar,BAZ=qux` to `docker-build.yml` when the Dockerfile declares `ARG`s.
