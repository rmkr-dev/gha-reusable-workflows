# Troubleshooting

Common caller issues when using these reusable workflows.

## Python CI

| Symptom | Likely cause | Fix |
| --- | --- | --- |
| `No module named pytest` | Deps not installed | Provide `pyproject.toml` with `[project.optional-dependencies] dev` including pytest, or `requirements-dev.txt` / `requirements.txt` |
| Only `compileall` runs | No `ruff` on PATH and no tests | Expected for compile-only samples; add `tests/` or install `ruff` for lint |
| Pip cache miss every run | Lockfile path not under defaults | Ensure `pyproject.toml` or `requirements*.txt` live under `working-directory` |
| Wrong package installed | Multiple projects in repo | Set `working-directory` to the module root |

- Need custom pytest flags (markers, `-k`, verbosity): pass `pytest-args` (appended after `pytest -q`).

- Need custom Ruff flags (`--select`, `--ignore`): pass `ruff-args` when `ruff` is on PATH.

## Java Maven CI

| Symptom | Likely cause | Fix |
| --- | --- | --- |
| `pom.xml must exist` | Wrong `working-directory` | Point at the module that contains `pom.xml` |
| Reactor module not built | Goals lack `-pl`/`-am` | Pass `maven-goals: test -pl :module -am` (see `samples/java-multi`) |
| ITs skipped | Failsafe profile not active | Use `maven-goals: verify -Pintegration-test` (see `samples/java-hello`) |
| Cache not restoring | pom path | Composite caches `${working-directory}/pom.xml` |

## Docker build

| Symptom | Likely cause | Fix |
| --- | --- | --- |
| Trivy cannot find image | `push: true` without load, or multi-platform + load | Keep `push: false` for local scan; single `platforms` value |
| Push fails / unauthorized | Login not in same job | Reusable job is isolated — use a caller-owned job with `docker/login-action` (see [docker-build.md](docker-build.md)) |
| Base image CRITICAL noise | Broad severity | Tighten `trivy-severity` (self-test uses `CRITICAL`) |

## SBOM

| Symptom | Likely cause | Fix |
| --- | --- | --- |
| Verify step fails | Empty / non-JSON output | Keep `format: spdx-json` (default); ensure `path` points at real sources |
| Artifact missing | `upload-artifact: false` | Set `upload-artifact: true` or download from job logs / regenerate |
| Retention shorter than expected | `upload-artifact-retention: 0` | Pass an explicit day count (1–90) |

## CodeQL / dependency-review

| Symptom | Likely cause | Fix |
| --- | --- | --- |
| CodeQL permission error | Missing `security-events: write` | See [permissions.md](../security/permissions.md) |
| Dependency review no-op on push | Action requires `pull_request` | Gate the job with `if: github.event_name == 'pull_request'` |
| Autobuild fails (Java) | Module not buildable at `working-directory` | Fix build or narrow `working-directory` |

## Release tag

| Symptom | Likely cause | Fix |
| --- | --- | --- |
| Notes empty | No `## [X.Y.Z]` in CHANGELOG | Add section or set `notes-fallback: generate` |
| Tag already exists | Re-run on same version | Safe no-op; bump version / tag input |

## Getting help

1. Compare against `samples/` and `.github/workflows/ci.yml` self-tests.
2. Check [edge-cases.md](edge-cases.md), [examples.md](examples.md), and [permissions.md](../security/permissions.md).
3. Open a PR or discussion with the failing job URL and the `uses:` pin (`@v0.4.2`).
